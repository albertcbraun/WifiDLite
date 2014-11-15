WifiDLite
=========

## Description
An (experimental) Android library project designed to make certain Wifi Direct P2P tasks a little easier. 
Also includes demo app. 

The idea behind this library is to make it easier for you to carry out certain Wifi Direct tasks (like creating a P2P group,  finding the current collection of peer devices, opening the Wifi Settings dialog). 

The way it tries to do this is through the use of a singleton (WifiDLite) with various convenience methods, and some very simple but specialized callback objects. WifiDLite spares you from having to get a WifiP2PManager, initialize a Channel, implement a specialized BroadcastReceiver, etc. 

Instead, you simply get and initialize the WifiDLite singleton and call its methods (e.g. createGroup, acquireCurrentPeerList, and openWifiSettings). 

There's also an experimental feature that periodically "rediscovers" the peers in the network, allowing you to subscribe to a frequently updated list of peers in the network. 

## License
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Demo App 
There is a demo app project separate from the library which is included in this source code. Please see the demo app's MainActivity and various fragments for examples of code use.

You can also download the demo app from Google Play: https://play.google.com/store/apps/details?id=com.albertcbraun.wifidlitedemoapp

## Build
This project was written using Android Studio and Gradle. You should be able to check it out directly from GitHub using  Android Studio. 

(If you want to build it in Eclipse, you'll have to perform some local customizations of your own. See for example: https://code.google.com/p/maven-android-plugin/wiki/AAR ) 


