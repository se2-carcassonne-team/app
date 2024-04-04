package se2.carcassonne.helper.network;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketClient {

    private CompositeDisposable disposable = new CompositeDisposable();
    private StompClient client;

    public void connect(WebSocketMessageHandler<String> messageHandler) {
        cancelAllSubscriptions();
        if (client != null && client.isConnected()) client.disconnect();
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/websocket-broker");
        disposable.add(client.lifecycle().subscribe(event -> {
            switch (event.getType()){
                case OPENED:
                    System.out.println("Connection success");
                    break;
                case ERROR:
                    System.out.println("Connection error: "+event.getException().getMessage());
                    break;
                case CLOSED:
                    System.out.println("Connection closedA");
                    break;
            }
        }));
        Disposable serverTopic = client.topic("/topic/websocket-broker-response").subscribe(response -> {
                    System.out.println("Response From Server: "+response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                }, error -> System.out.println("Response ERROR: "+error)
        );
        disposable.add(serverTopic);
        client.connect();
    }

    public void sendMessage(String destination, String message){
        if (!client.isConnected()) System.out.println("Not connected");
        disposable.add(client.send(destination, message).subscribe(() -> System.out.println("Sending Message ... "+message), error -> System.out.println("ERROR while sending Message: "+error)));
    }

    public void subscribeToTopic(String topic, WebSocketMessageHandler<String> messageHandler){
        Disposable subscription = client.topic(topic).subscribe(
                response -> {
                    System.out.println("Response from "+topic+": " + response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                },
                error -> System.out.println("Error subscribing to list-lobbies topic: " + error)
        );
        disposable.add(subscription);
    }

    public void subscribeToQueue(String queue, WebSocketMessageHandler<String> messageHandler){
        Disposable subscription = client.topic(queue).subscribe(
                response -> {
                    System.out.println("Response from "+queue+": " + response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                },
                error -> System.out.println("Error subscribing to list-lobbies topic: " + error)
        );
        disposable.add(subscription);
    }

    public void subscribeToListLobbiesTopic(WebSocketMessageHandler<String> messageHandler) {
        Disposable subscription = client.topic("/topic/list-lobby-response").subscribe(
                response -> {
                    System.out.println("Response from list-lobbies topic: " + response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                },
                error -> System.out.println("Error subscribing to list-lobbies topic: " + error)
        );
        disposable.add(subscription);
    }



    public void cancelAllSubscriptions() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = new CompositeDisposable();
    }

    public void disconnect(){
        if (client.isConnected()){
            cancelAllSubscriptions();
            client.disconnect();
        }
    }

}