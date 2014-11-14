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

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Utilities for logging, converting status codes into strings, etc.
 */
public class Util {

    /**
     * Writes a message to the Android log. Converts the code
     * to a string.
     * <p>
     * All parameters are required.
     *
     * @param tag the String which identifies the class which is calling this method
     * @param messagePrefix a string which is prepended to the logging message and
     *                      helps clarify the logging message.
     * @param code an int. one of WifiP2pManager.ERROR, WifiP2pManager.P2P_UNSUPPORTED,
     *             WifiP2pManager.BUSY, WifiP2pManager.NO_SERVICE_REQUESTS
     */
    public static void logStatusCode(String tag, String messagePrefix, int code) {
        Log.v(tag, String.format("%s. WifiP2PManager status:%s", messagePrefix, getP2pStatus(code)));
    }

    public static String getP2pStatus(int code) {
        if (code == WifiP2pManager.ERROR) {
            return "Error";
        } else if (code == WifiP2pManager.P2P_UNSUPPORTED) {
            return "P2p Unsupported";
        } else if (code == WifiP2pManager.BUSY) {
            return "Busy";
        } else if (code == WifiP2pManager.NO_SERVICE_REQUESTS) {
            return "No service requests have been added";
        } else {
            return "Unknown";
        }
    }

    public static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

}
