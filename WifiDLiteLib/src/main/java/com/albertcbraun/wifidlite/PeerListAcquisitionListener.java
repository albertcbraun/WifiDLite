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

import java.util.List;

/**
 * Peer discovery and listing callbacks for clients of
 * the {@link WifiDLite} object.
 *
 * The list of {@link Peer} objects returned contains one {@link Peer}
 * for each {@link android.net.wifi.p2p.WifiP2pDevice} discovered
 * on the local Wifi P2P network.
 */
public interface PeerListAcquisitionListener {
    public void onPeerListAcquisitionSuccess(List<Peer> peers);
}
