
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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.albertcbraun.wifidlite.WifiDLite;
import com.albertcbraun.wifidlitedemoapp.MainActivity;

/**
 * Basic functionality for use by other fragments.
 */
public abstract class FragmentBase extends Fragment {

    private static final String TAG = FragmentBase.class.getCanonicalName();

    protected static final String ARG_SECTION_NUMBER = "section_number";
    protected WifiDLite wifiDLite = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Activity has already initialized the WifiDLite singleton
        if (wifiDLite == null) {
            wifiDLite = ((MainActivity) getActivity()).getWifiDLite();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        int section = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.v(TAG, "onAttach. section:" + section);
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(section);
    }


}
