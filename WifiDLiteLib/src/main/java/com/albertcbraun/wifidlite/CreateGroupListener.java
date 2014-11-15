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

import android.net.wifi.p2p.WifiP2pGroup;

/**
 * A callback for use by clients of the library who wish to know the
 * name of the Wifi P2P Group created earlier by calling
 * {@link com.albertcbraun.wifidlite.WifiDLite#createGroup}.
 */
public interface CreateGroupListener {
    public void onCreateGroupSuccess(WifiP2pGroup wifiP2pGroup);

    public void onCreateGroupFailure(int status);
}
