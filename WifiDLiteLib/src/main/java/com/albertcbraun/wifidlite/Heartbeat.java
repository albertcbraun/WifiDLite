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
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;

import com.albertcbraun.wifidlite.impl.SimpleWifiP2pActionListener;

/**
 * Intended only for internal use.
 *
 * Periodically invokes peer discovery related methods on {@link WifiP2pManager} instance
 * owned by the {@link WifiDLite} instance. This is the mechanism for updating the
 * ongoing subscription methods.
 * <p>
 * Other tasks might be completed here as well, in future versions.
 * <p>
 * This is experimental. It may be removed in a future version.
 */
class Heartbeat implements Runnable {

    private static final String TAG = Heartbeat.class.getCanonicalName();

    private WifiP2pManager wifiP2pManager = null;
    private WifiP2pManager.Channel wifiP2pChannel = null;

    /**
     * Main constructor.
     *
     * @param wifiP2pManager current, initialized instance of the Android SDK's {@link WifiP2pManager}
     * @param wifiP2pChannel instance of the {@link WifiP2pManager.Channel} object associated with the manager
     */
    Heartbeat(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel wifiP2pChannel) {
        this.wifiP2pManager = wifiP2pManager;
        this.wifiP2pChannel = wifiP2pChannel;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        wifiP2pManager.discoverPeers(wifiP2pChannel, new SimpleWifiP2pActionListener("Heartbeat - Discover Peers"));
    }
}
