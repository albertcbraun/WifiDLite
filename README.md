WifiDLite
=========

An (experimental) Android library project designed to make certain Wifi Direct P2P tasks a little easier. 
Also includes demo app. 

The idea behind this library is to make it easier for the client to carry out certain Wifi Direct tasks (like creating a P2P group, or finding the current collection of peer devices). 

The way it tries to do this is through the use of a singleton (WifiDLite) with various convenience methods, and some specialized callback objects.

WifiDLite spares you from having to create a WifiP2PManager, Channel, specialized BroadcastReceiver, etc. 

Instead, you initialize the WifiDLite instance and then just call methods like createGroup, acquireCurrentPeerList, and openWifiSettings. 

There's also an experimental feature that periodically "rediscovers" the peers in the network, allowing you to subscribe to a frequently updated list of peers in the network. 


