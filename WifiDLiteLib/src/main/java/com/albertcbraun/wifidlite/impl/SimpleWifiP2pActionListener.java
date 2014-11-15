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

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.albertcbraun.wifidlite.Util;

/**
 * A logging-only implementation of {@link android.net.wifi.p2p.WifiP2pManager.ActionListener}.
 * For convenience, the instanceName provided by the user is reflected
 * in the logged messages.
 */
public class SimpleWifiP2pActionListener implements WifiP2pManager.ActionListener {

    private static final String TAG = SimpleWifiP2pActionListener.class.getCanonicalName();

    private String instanceName = null;

    public SimpleWifiP2pActionListener(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public void onSuccess() {
        Log.v(TAG, String.format("%s successful", this.instanceName));
    }

    @Override
    public void onFailure(int code) {
        Util.logP2pStatus(TAG, this.instanceName, code);
    }


}
