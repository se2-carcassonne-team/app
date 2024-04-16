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


## To Be Done First Sprint

- Leaving/Joining lobbies
- Show players that are currently inside the lobby
- Deleting lobbies after __x__ minutes, when no player is in the lobby


## User-App interaction (UML state diagram) while playing
![Game States_User Interaction Diagram final](https://github.com/se2-carcassonne-team/app/assets/72282853/cbbeb8e6-1adf-4917-bf1e-0c680fc7b588)


