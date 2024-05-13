package se2.carcassonne.helper.network;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketClient {
    private static WebSocketClient instance;
    private CompositeDisposable disposable = new CompositeDisposable();
    private final Map<String, Disposable> disposablesMap = new HashMap<>();
    private StompClient client;

    private static final String CONNECT = "Connect";
    private static final String SEND_MESSAGE = "Send Message";

    private WebSocketClient() {
    }

    public static synchronized WebSocketClient getInstance() {
        if (instance == null) {
            instance = new WebSocketClient();
        }
        return instance;
    }

    public void connect() {
        cancelAllSubscriptions();
        if (client != null && client.isConnected()) client.disconnect();
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/websocket-broker");
        disposable.add(client.lifecycle().subscribe(event -> {
            switch (event.getType()){
                case OPENED:
                    Log.d(CONNECT, "Connection success");
                    break;
                case ERROR:
                    Log.d(CONNECT, "Connection error: "+event.getException().getMessage());
                    break;
                case CLOSED:
                    Log.d(CONNECT, "Connection closed");
                    break;
                default:
                    Log.d(CONNECT, "Connection event occurred: "+event.getType());
            }
        }));
        client.connect();
    }

    public void sendMessage(String destination, String message){
        if (!client.isConnected()) Log.d(SEND_MESSAGE, "Not connected");
        disposable.add(client.send(destination, message).subscribe(() -> Log.d(SEND_MESSAGE, "Sending Message ... "+message), error -> Log.d(SEND_MESSAGE, "ERROR while sending Message: "+error)));
    }

    public void subscribeToTopic(String topic, WebSocketMessageHandler<String> messageHandler){
        Disposable subscription = client.topic(topic).subscribe(
                response -> {
                    Log.d("SubscribeToTopic", "Response from "+topic+": " + response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                },
                error -> Log.d("SubscribeToTopic", "Error subscribing to list-lobbies topic: " + error)
        );
        disposablesMap.put(topic, subscription);
        disposable.add(subscription);
    }


    public void subscribeToQueue(String queue, WebSocketMessageHandler<String> messageHandler){
        Disposable subscription = client.topic(queue).subscribe(
                response -> {
                    Log.d("SubscribeToQueue","Response from "+queue+": " + response.getPayload());
                    messageHandler.onMessageReceived(response.getPayload());
                },
                error -> Log.d("SubscribeToQueue", "Error subscribing to list-lobbies topic: " + error)
        );
        disposablesMap.put(queue, subscription);
        disposable.add(subscription);
    }

    public void unsubscribe(String destination){
        Disposable disposableToRemove = disposablesMap.get(destination);
        if (disposableToRemove != null) {
            disposablesMap.remove(destination);
            disposableToRemove.dispose(); // Unsubscribe from this disposable
            disposable.remove(disposableToRemove); // Remove from CompositeDisposable
        }
    }

    public void cancelAllSubscriptions() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = new CompositeDisposable();
        disposablesMap.clear();
    }

    public void disconnect(){
        if (client.isConnected()){
            cancelAllSubscriptions();
            client.disconnect();
        }
    }
}