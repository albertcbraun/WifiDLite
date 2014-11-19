
### Description
An experimental, Android library project (in alpha) designed to make certain Wifi Direct P2P tasks a little easier.

Also includes demo app.

The idea behind this library is to make it easier for you to carry out certain Wifi Direct tasks (like creating a P2P group,  finding the current collection of peer devices, opening the Wifi Settings dialog). 

The way it tries to do this is through the use of a singleton (WifiDLite) with various convenience methods, and some very simple but specialized callback objects. WifiDLite spares you from having to get a WifiP2PManager, initialize a Channel, implement a specialized BroadcastReceiver, etc. 

Instead, you simply get and initialize the WifiDLite singleton and call its methods (e.g. createGroup, acquireCurrentPeerList, and openWifiSettings). 

There's also an experimental feature that periodically "rediscovers" the peers in the network, allowing you to subscribe to a frequently updated list of peers in the network. 

### License
[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

### Demo App
There is a demo app project separate from the library which is included in this source code. Please see below (and the demo app's MainActivity and fragments) for examples of code use.

You can also download the demo app from the Google Play Store:
https://play.google.com/store/apps/details?id=com.albertcbraun.wifidlitedemoapp

### Code Snippets

Obtain list of peer devices on your Android's local Wifi Direct network:

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize WifiDLite singleton
        WifiDLite wifiDLite = WifiDLite.getInstance();
        wifiDLite.initialize(getApplicationContext(), new DefaultConfiguration());

        // call a method on the singleton by feeding it an appropriate listener
        wifiDLite.acquireCurrentPeerList(new PeerListAcquisitionListener() {
            @Override
            public void onPeerListAcquisitionSuccess(List<Peer> peers) {
                // note that each peer device is represented by a special Peer object
                Log.v("MyListener", "Acquired a list of P2P Peers");
            }
        });
    }

Invite a specific peer to connect:

    peer.connect(new PeerConnectionListener() {
            @Override
            public void onPeerConnectionSuccess(Peer peer) {
            // connection handling code goes here
            ...
            }

            @Override
            public void onPeerConnectionFailure(int reasonCode) {
            // note the Util convenience method for mapping
            // the Android platform's reasonCode to human readable String
            Util.logP2pStatus(TAG, "peer connection failed", reasonCode);

            // invite-failed handling code goes here
            ...
        }
    });

Make the current Android device a P2P group owner:

    wifiDLite.createGroup(new CreateGroupListener() {
        @Override
        public void onCreateGroupSuccess(WifiP2pGroup wifiP2pGroup) {
            // success message handling here
        }

        @Override
        public void onCreateGroupFailure(int status) {
        // failure message handling here
        }
    });

Display the Android Wifi settings dialog:

      rootView.findViewById(R.id.open_wifi_settings_dialog_button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              wifiDLite.openWifiSettings();
          }
      });

Dispose of the WifiDLite object:

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiDLite != null) {
            wifiDLite.dispose();
            wifiDLite = null;
        }
    }

### Checkout and Build
This project was written using Android Studio and Gradle.

You should be able to check it out directly from GitHub in Android Studio:

* VCS|Checkout from Version Control|Github.
* VCS repository url: https://github.com/albertcbraun/WifiDLite.git

(If you want to build it in Eclipse, you'll have to perform some local customizations of your own. See for example: https://code.google.com/p/maven-android-plugin/wiki/AAR ) 


