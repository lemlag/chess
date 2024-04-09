package ui;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;


public class WebSocketCommunicator extends Endpoint {

    public Session session;
    public ServerMessageObserver observer;
    private Gson gson;

    public WebSocketCommunicator(ServerMessageObserver obsvr) throws Exception{
        observer = obsvr;
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        Gson gson = new Gson();

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String msg){
                try {
                    ServerMessage message =
                            gson.fromJson(msg, ServerMessage.class);
                    observer.notify(message);
                } catch(Exception ex) {
                    observer.notify(new ErrorMessage(ex.getMessage()));
                }

            }
        });
    }

    public void send(String msg) throws Exception{
        this.session.getBasicRemote().sendText(msg);
    }
    public void onOpen(Session session, EndpointConfig endpointConfig){
    }

}
