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

import android.net.wifi.p2p.WifiP2pInfo;

/**
 * Device connection callbacks for clients of
 * the {@link WifiDLite} object.
 */
public interface PeerConnectionListener {
    public void onPeerConnectionSuccess(WifiP2pInfo wifiP2pInfo);
    public void onPeerConnectionFailure(int reasonCode);
}
