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

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * A convenience wrapper around the {@link WifiP2pDevice} object.
 * Provides a more useful toString method and a {@link #connect} method
 * to actually connect to the device.
 */
public class Peer {

    private static final String TAG = Peer.class.getCanonicalName();

    private WifiP2pDevice wifiP2pDevice = null;
    private WifiP2pManager wifiP2pManager = null;
    private WifiP2pManager.Channel channel = null;

    /**
     * Main Constructor.
     *
     * @param wifiP2pDevice  the peer device
     * @param wifiP2pManager the main instance of the {@link android.net.wifi.p2p.WifiP2pManager}
     * @param channel        the {@link android.net.wifi.p2p.WifiP2pManager.Channel} associated with
     *                       the wifiP2pManager
     */
    public Peer(WifiP2pDevice wifiP2pDevice, WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel) {
        this.wifiP2pDevice = wifiP2pDevice;
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", wifiP2pDevice.deviceName,
                wifiP2pDevice.deviceAddress);
    }

    /**
     * Provides access to underlying {@link android.net.wifi.p2p.WifiP2pDevice} instance.
     *
     * @return {@link android.net.wifi.p2p.WifiP2pDevice} instance.
     */
    public WifiP2pDevice getWifiP2pDevice() {
        return this.wifiP2pDevice;
    }

    /**
     * Invites the peer device to form a connection with the current device.
     * The user of the peer device must accept the invitation in order to create a
     * connection.
     * <p/>
     * The caller of this method will receive information about the peer device
     * connection in the {@link PeerConnectionListener}.
     *
     * @param peerConnectionListener callback to receive info about the peer device
     */
    public void connect(final PeerConnectionListener peerConnectionListener) {
        final String deviceName = wifiP2pDevice.deviceName;
        final String deviceAddress = wifiP2pDevice.deviceAddress;
        if (deviceName != null && deviceAddress != null) {
            wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(final WifiP2pInfo infoAboutCallingDevice) {
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = deviceAddress;
                    wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Log.v(TAG, getMessage(true));
                            peerConnectionListener.onPeerConnectionSuccess(Peer.this);
                        }

                        @Override
                        public void onFailure(int reason) {
                            Log.w(TAG, getMessage(false));
                            peerConnectionListener.onPeerConnectionFailure(reason);
                        }

                        private String getMessage(boolean succeeded) {
                            return String.format("connect to device %s. deviceName:%s deviceAddress:%s",
                                    (succeeded ? "successful" : "unsuccessful"), deviceName, deviceAddress);
                        }
                    });
                }
            });
        } else {
            Log.w(TAG, String.format("Cannot attempt connection to peer. Device name (%s) and/or address (%s) not available.",
                    deviceName, deviceAddress));
        }
    }

}
