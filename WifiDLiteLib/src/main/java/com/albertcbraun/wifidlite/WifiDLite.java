/*
 * Copyright (c) 2014 Albert C. Braun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.albertcbraun.wifidlite;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.albertcbraun.wifidlite.impl.SimpleWifiP2pActionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Singleton which tries to simplify various WiFi Direct tasks on Android devices which
 * already support WiFi Direct operations.
 * <p/>
 * (Sorry. This does <i>not</i> add WiFi Direct functionality to
 * Android devices which do not have WiFi Direct platform support already.)
 * <p/>
 * Also, please note: this class and its methods are intended only for use on the UI thread.
 * There is no guarantee of thread safety.
 * <p/>
 */
public final class WifiDLite {

    private static final String TAG = WifiDLite.class.getCanonicalName();
    private static final String REINITIALIZATION_ERROR_MESSAGE = "Reinitializing the WifiDLite singleton is not allowed.";

    private static final WifiDLite INSTANCE = new WifiDLite();

    private Configuration configuration = null;
    private Context context = null;
    private WiFiDLiteBroadcastReceiver wiFiDLiteBroadcastReceiver = null;
    private WifiP2pManager wifiP2pManager = null;
    private WifiP2pManager.Channel wifiP2pManagerChannel = null;
    private final List<PeerListAcquisitionListener> oneTimePeerListAcquisitionListeners = new ArrayList<PeerListAcquisitionListener>();
    private final List<PeerListAcquisitionListener> ongoingPeerListAcquisitionListeners = new ArrayList<PeerListAcquisitionListener>();
    private final List<CreateGroupListener> oneTimeCreateGroupListeners = new ArrayList<CreateGroupListener>();
    private ScheduledExecutorService scheduler = null;
    private boolean isWifiP2pEnabled = false;

    /**
     * WifiDLite object is a singleton which should be used on the
     * main (UI) thread. After obtaining an instance with this method,
     * the client must call initialize before calling any other
     * methods on this object.
     *
     * @return the singleton instance of the WifiDLite object
     */
    public static WifiDLite getInstance() {
        return INSTANCE;
    }

    /**
     * Sets up the WifiDLite singleton with the supplied configuration.
     * <p/>
     * It's most convenient to call this from within your Activity's onCreate
     * method and then call dispose from within your Activity's onDestroy
     * method. But, this method could also be called from within onStart or
     * onResume.
     *
     * @param context       the Android application context
     * @param configuration a client customized Configuration object
     */
    public void initialize(Context context, Configuration configuration) {
        if (this.configuration == null) {
            wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
            wifiP2pManagerChannel = wifiP2pManager.initialize(context, context.getMainLooper(),
                    new WifiP2pManager.ChannelListener() {
                        @Override
                        public void onChannelDisconnected() {
                            Log.v(TAG, "Channel Disconnected");
                        }
                    });
            this.context = context;
            this.configuration = configuration;
            wiFiDLiteBroadcastReceiver = new WiFiDLiteBroadcastReceiver();
            this.context.registerReceiver(wiFiDLiteBroadcastReceiver, intentFilter);
            this.startHeartbeat();
        } else {
            Log.w(TAG, REINITIALIZATION_ERROR_MESSAGE + "This WifiDLite singleton is already initialized " +
                    "and cannot be reinitialized without disposing of it first. " +
                    " Ignoring configuration object:" + configuration);
        }
    }

    /**
     * Frees the internal resources, including the internal BroadcastReceiver
     * instance. Nulls out the configuration object.
     * <p/>
     * Calling from within onDestroy is convenient because it allows the
     * BroadcastReceiver to remain registered while the user switches to
     * the device's Settings to make and accept WifiDirect invitations. This lets
     * your app learn of changes to the collection of peers, device, etc
     * <p/>
     * Could also be called from onPause or onStop.
     */
    public void dispose() {
        Log.v(TAG, "WifiDLite object being disposed");
        this.stopHeartbeat();
        if (wiFiDLiteBroadcastReceiver != null) {
            this.context.unregisterReceiver(wiFiDLiteBroadcastReceiver);
            wiFiDLiteBroadcastReceiver = null;
        }
        this.configuration = null;
    }

    /**
     * Waits for a "peers changed" broadcast event then calls back one time on the
     * PeerListAcquisitionListener instance. After that, this call is expired and no
     * further callbacks will be made.
     *
     * @param peerListAcquisitionListener a PeerListAcquisitionListener which will be called back only once.
     */
    public void acquireCurrentPeerList(final PeerListAcquisitionListener peerListAcquisitionListener) {
        acquirePeerList(peerListAcquisitionListener, AcquisitionFrequency.ONE_TIME_ONLY);
    }

    /**
     * Requests new list of peers each time the Android platform becomes aware of a change to the existing set of peers.
     * Calls listener back each time.
     *
     * @param peerListAcquisitionListener a PeerListAcquisitionListener which will be called back one or more times.
     */
    public void subscribeToUpdatesOfPeerList(final PeerListAcquisitionListener peerListAcquisitionListener) {
        acquirePeerList(peerListAcquisitionListener, AcquisitionFrequency.ONGOING);
    }

    /**
     * Unsubscribe from updates of available P2P peers.
     *
     * @param peerListAcquisitionListener the PeerListAcquisitionListener instance which you want WifiDLite to stop calling.
     */
    public void unsubscribeFromUpdatesOfPeerList(PeerListAcquisitionListener peerListAcquisitionListener) {
        this.ongoingPeerListAcquisitionListeners.remove(peerListAcquisitionListener);
    }

    /**
     * Opens the system Settings dialog for adjusting WiFi settings
     * by starting a new Activity.
     *
     * @param ignored is not used, but its presence here gives
     *                this method the right signature for use as a Button
     *                onClick handler
     */
    public void openWifiSettings(View ignored) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }

    /**
     * Create a Wifi Direct P2P Group. The current device will be
     * the group owner. Other useful information, such as the group name and
     * pass phrase, will be passed back to the {@link CreateGroupListener}
     * callback supplied by the createGroup caller.
     *
     * Please note that this method first tries to remove any existing group.
     * It does this in order to avoid a "busy" status which can be returned
     * from {@link WifiP2pManager#createGroup}
     *
     * @param listener
     */
    public void createGroup(final CreateGroupListener listener) {
        oneTimeCreateGroupListeners.add(listener);
        wifiP2pManager.removeGroup(wifiP2pManagerChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.v(TAG, "removeGroup succeeded. about to call createGroup");
                wifiP2pManager.createGroup(wifiP2pManagerChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.v(TAG, "createGroup succeeded");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Util.logStatusCode(TAG, "createGroup", reason);
                        listener.onCreateGroupFailure(reason);
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                Util.logStatusCode(TAG, "removeGroup failed:", reason);
                listener.onCreateGroupFailure(reason);
            }
        });
    }



    /**
     * Private Methods
     */

    private void startHeartbeat() {
        if (this.scheduler == null) {
            this.scheduler = Executors.newScheduledThreadPool(1);
            this.scheduler.scheduleAtFixedRate(new Heartbeat(this.wifiP2pManager, this.wifiP2pManagerChannel),
                    0, configuration.getHeartbeatDelay(), TimeUnit.SECONDS);
        }
    }

    private void stopHeartbeat() {
        if (this.scheduler != null) {
            this.scheduler.shutdownNow();
            this.scheduler = null;
        }
    }

    private void acquirePeerList(final PeerListAcquisitionListener peerListAcquisitionListener,
                                 AcquisitionFrequency acquisitionFrequency) {
        sanityCheck();
        switch (acquisitionFrequency) {
            case ONE_TIME_ONLY:
                oneTimePeerListAcquisitionListeners.add(peerListAcquisitionListener);
                break;
            case ONGOING:
                ongoingPeerListAcquisitionListeners.add(peerListAcquisitionListener);
                break;
        }
        wifiP2pManager.discoverPeers(wifiP2pManagerChannel, new SimpleWifiP2pActionListener("discoverPeers call"));
    }

    private void sanityCheck() {
        if (configuration == null) {
            throw new RuntimeException("WifiDLite not initialized. (Please be sure to initialize " +
                    "before calling connect and to reinitialize if dispose was called earlier.)");
        }
        if (Thread.currentThread().getId() != 1) {
            Log.w(TAG, "WifiDLite is apparently being used off the main (UI) thread. This is not supported and may cause errors.");
        }
    }

    private void updatePeers(WifiP2pDeviceList wifiP2pDeviceList) {
        Log.v(TAG, "request peers success. Thread:" + Thread.currentThread().getId());
        Collection<WifiP2pDevice> deviceList = wifiP2pDeviceList.getDeviceList();
        Log.v(TAG, "request peers. peer device count:" + deviceList.size());
        ArrayList<Peer> peers = new ArrayList<Peer>();
        for (WifiP2pDevice wifiP2pDevice : deviceList) {
            Log.v(TAG, "adding device as Peer to local ArrayList:" + wifiP2pDevice.deviceName);
            peers.add(new Peer(wifiP2pDevice, wifiP2pManager, wifiP2pManagerChannel));
        }
        // call existing listeners
        if (oneTimePeerListAcquisitionListeners.size() > 0) {
            for (PeerListAcquisitionListener listener : oneTimePeerListAcquisitionListeners) {
                listener.onPeerListAcquisitionSuccess(peers);
            }
            Log.v(TAG, "request peers cleared peer listeners list");
            oneTimePeerListAcquisitionListeners.clear();
        }
        if (ongoingPeerListAcquisitionListeners.size() > 0) {
            Log.v(TAG, "ongoing onPeerListAcquisitionSuccess handlers (if any) called with updated peer list");
            for (PeerListAcquisitionListener listener : ongoingPeerListAcquisitionListeners) {
                listener.onPeerListAcquisitionSuccess(peers);
            }
        }
    }

    private void updateGroup(WifiP2pGroup group) {
        if (group.getNetworkName() != null && group.getInterface() != null) {
            for (CreateGroupListener listener : oneTimeCreateGroupListeners) {
                listener.onCreateGroupSuccess(group);
            }
            oneTimeCreateGroupListeners.clear();
        }
    }

    /**
     * BroadcastReceiver and related declarations.
     */
    private static final IntentFilter intentFilter = new IntentFilter();

    static {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public boolean isWifiP2pEnabled() {
        return isWifiP2pEnabled;
    }

    private class WiFiDLiteBroadcastReceiver extends BroadcastReceiver {

        private final String TAG = WiFiDLiteBroadcastReceiver.class.getCanonicalName();

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, "WiFi Direct broadcast received. action:" + action);

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                Log.v(TAG, "P2P State Changed.");

                // Get and record the current Wifi P2P state
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.v(TAG, "WIFI P2P is enabled");
                    isWifiP2pEnabled = true;
                } else {
                    Log.v(TAG, "WIFI P2P is NOT enabled");
                    isWifiP2pEnabled = false;
                }

            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                Log.v(TAG, "P2P Peers changed.");

                // call the peer list acquisition listeners
                WifiP2pDeviceList wifiP2pDeviceList = (WifiP2pDeviceList) intent.getExtras().get(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                if (wifiP2pDeviceList != null) {
                    updatePeers(wifiP2pDeviceList);
                }

                // also try to discover services as long as we're here
                // discoverServices();

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                Log.v(TAG, "P2P Connection changed");

                // call the group creation listeners
                WifiP2pGroup group = (WifiP2pGroup) intent.getExtras().get(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
                if (group != null) {
                    updateGroup(group);
                }

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                Log.v(TAG, "This P2P Device changed");
            }
        }
    }

    /**
     * Other Declarations
     */
    private enum AcquisitionFrequency {
        ONE_TIME_ONLY, ONGOING
    }

}
