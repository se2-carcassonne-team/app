# Android App for Carcassonne

This App connects to a [WebSocket](https://en.wikipedia.org/wiki/WebSocket) using [STOMP](https://stomp.github.io/).


## Networking

- [OkHttp](https://square.github.io/okhttp/) - As a Connection Provider
- [NaikSoftware](https://github.com/NaikSoftware/StompProtocolAndroid) - STOMP protocol via WebSocket for Android

- Allow Internet Connection in AndroidManifest.xml
```
<uses-permission android:name="android.permission.INTERNET"/>
```

- Cleartext Message via Network
```
android:usesCleartextTraffic="true"
```

- [Localhost for Emulator](https://developer.android.com/studio/run/emulator-networking?hl=de) Host Loopback for Localhost
```
10.0.2.2 localhost
```
## App Features

- Startup Screen with animations
- Home Screen with name selection dialog
    - Name selection is equal to player creation
    - Regex to check for invalid input
    - Toast message if username either:
        - Already exists
        - Is invalid (contains emojis or speical characters)
- Lobby Screen with option to either:
    - Create new lobby
        - Player then is in the lobby, yet can't leave
    - List all existing lobbies
        - Lobbies are clickable, yet can't be joined
    - Leaving/Joining lobbies
    - Show players that are currently inside the lobby
    - Show players who's the lobby admin
    - Deleting lobbies when no player is in the lobby
    - Starting the game when there are more than two players inside the lobby
    - GameBoard with Android GridView
        - Navigating with "Arrow Buttons"
        - Zooming In And Out of the Board
        - Highlighting Valid Tile Placement
        - Placing Tiles with respect to current rotation  
