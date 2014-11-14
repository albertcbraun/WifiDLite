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

package com.albertcbraun.wifidlite.impl;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import com.albertcbraun.wifidlite.PeerConnectionListener;

/**
 * A logging only implementation of {@link com.albertcbraun.wifidlite.PeerConnectionListener}.
 */
public class SimplePeerConnectionListener implements PeerConnectionListener {

    private static final String TAG = SimplePeerConnectionListener.class.getCanonicalName();

    @Override
    public void onPeerConnectionSuccess(WifiP2pInfo wifiP2pInfo) {
        Log.v(TAG, "Connection to device succeeded. wifiP2pInfo:" + wifiP2pInfo.toString());
    }
    @Override
    public void onPeerConnectionFailure(int reasonCode) {
        Log.w(TAG, "Connection to device failed. Reason code:" + reasonCode);
    }

}
