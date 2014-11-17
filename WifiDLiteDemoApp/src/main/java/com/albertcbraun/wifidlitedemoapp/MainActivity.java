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

package com.albertcbraun.wifidlitedemoapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.albertcbraun.wifidlite.WifiDLite;
import com.albertcbraun.wifidlite.impl.DefaultConfiguration;
import com.albertcbraun.wifidlitedemoapp.fragments.Misc;
import com.albertcbraun.wifidlitedemoapp.fragments.NavigationDrawerFragment;
import com.albertcbraun.wifidlitedemoapp.fragments.PeerListAcquisition;
import com.albertcbraun.wifidlitedemoapp.fragments.PeerListSubscription;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private WifiDLite wifiDLite = null;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (wifiDLite == null) {
            wifiDLite = WifiDLite.getInstance();
            wifiDLite.initialize(getApplicationContext(), new DefaultConfiguration());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiDLite != null) {
            wifiDLite.dispose();
            wifiDLite = null;
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.v(TAG, "onNavigationDrawerItemSelection. position:" + position);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                ft.replace(R.id.container, PeerListAcquisition.newInstance(position));
                break;
            case 1:
                ft.replace(R.id.container, PeerListSubscription.newInstance(position));
                break;
            case 2:
                ft.replace(R.id.container, Misc.newInstance(position));
                break;
        }

        ft.commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section0);
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
        }
    }

    void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        //noinspection deprecation
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public WifiDLite getWifiDLite() {
        return wifiDLite;
    }


}
