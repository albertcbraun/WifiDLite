
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

import android.net.wifi.p2p.WifiP2pGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.albertcbraun.wifidlite.CreateGroupListener;
import com.albertcbraun.wifidlite.Util;
import com.albertcbraun.wifidlitedemoapp.R;

/**
 * A fragment demonstrating the ability of the WifiDLite library
 * to display a dialog, etc.
 */
public class Misc extends FragmentBase {

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Misc newInstance(int sectionNumber) {
        Misc fragment = new Misc();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Misc() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_misc, container, false);

        // customizations here
        rootView.findViewById(R.id.open_wifi_settings_dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiDLite.openWifiSettings(null);
            }
        });

        rootView.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiDLite.createGroup(new CreateGroupListener() {
                    @Override
                    public void onCreateGroupSuccess(WifiP2pGroup wifiP2pGroup) {
                        TextView message1 = (TextView) rootView.findViewById(R.id.create_group_message_1);
                        message1.setText("New WiFi Direct Group Created.");

                        TextView message2 = (TextView) rootView.findViewById(R.id.create_group_message_2);
                        message2.setText(String.format("Network Name:%s", wifiP2pGroup.getNetworkName()));

                        TextView message3 = (TextView) rootView.findViewById(R.id.create_group_message_3);
                        message3.setText(String.format("Passphrase: %s", wifiP2pGroup.getPassphrase()));
                    }

                    @Override
                    public void onCreateGroupFailure(int status) {
                        TextView message1 = (TextView) rootView.findViewById(R.id.create_group_message_1);
                        message1.setText("New WiFi Direct Group Not Created.");

                        TextView message2 = (TextView) rootView.findViewById(R.id.create_group_message_2);
                        message2.setText(String.format("Failure. Status Code:%s", Util.getP2pStatus(status)));

                        TextView message3 = (TextView) rootView.findViewById(R.id.create_group_message_3);
                        message3.setText("You may need to manually remove existing Wifi Direct P2P groups before createGroup can succeed.");

                    }
                });
            }
        });

        return rootView;
    }

}

