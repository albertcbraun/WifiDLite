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

package com.albertcbraun.wifidlitedemoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.albertcbraun.wifidlite.Peer;
import com.albertcbraun.wifidlite.PeerListAcquisitionListener;
import com.albertcbraun.wifidlite.impl.SimplePeerConnectionListener;
import com.albertcbraun.wifidlitedemoapp.R;

import java.util.List;

/**
 * A fragment demonstrating the ability of the WifiDLite library
 * to obtain lists of Peer devices and their services.
 */
public class Peers extends FragmentBase {

    private PeerListAcquisitionListener peerListAcquisitionListener = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Peers newInstance(int sectionNumber) {
        Peers fragment = new Peers();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_peers, container, false);

        // customizations here
        rootView.findViewById(R.id.acquire_peers_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayAdapter<Peer> arrayAdapter = new ArrayAdapter<Peer>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
                ListView listView = (ListView) getActivity().findViewById(R.id.peer_list);
                listView.setAdapter(arrayAdapter);
                peerListAcquisitionListener = new PeerListAcquisitionListener() {
                    @Override
                    public void onPeerListAcquisitionSuccess(List<Peer> peers) {
                        arrayAdapter.clear();
                        arrayAdapter.addAll(peers);
                        arrayAdapter.notifyDataSetChanged();
                    }
                };
                wifiDLite.subscribeToUpdatesOfPeerList(peerListAcquisitionListener);
            }
        });

        ListView listView = (ListView)rootView.findViewById(R.id.peer_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Peer peer = (Peer)parent.getItemAtPosition(position);
                peer.connect(new SimplePeerConnectionListener());
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wifiDLite != null) {
            wifiDLite.unsubscribeFromUpdatesOfPeerList(peerListAcquisitionListener);
            wifiDLite = null;
        }
    }


}

