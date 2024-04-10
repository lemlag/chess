package ui;
import com.google.gson.*;
import server.WSServer;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.lang.reflect.Type;
import java.net.URI;


public class WebSocketCommunicator extends Endpoint {

    public Session session;
    public ServerMessageObserver observer;
    private final Gson gson;

    public WebSocketCommunicator(ServerMessageObserver obsvr) throws Exception{
        observer = obsvr;
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
        gson = builder.create();

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String msg){
                try {
                    System.out.println("in the loop");
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
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
    }

    public void makeMove(MakeMoveCommand move) throws Exception {
        System.out.println("big stuff");
        this.send(gson.toJson(move));
    }

    public void resign(ResignCommand resigner) throws Exception {
        this.send(gson.toJson(resigner));
    }


    public void leave(LeaveCommand leaver) throws Exception {
        this.send(gson.toJson(leaver));
    }

    public void joinObserver(JoinObserverCommand joiner) throws Exception {
        this.send(gson.toJson(joiner));
    }

    public void joinPlayer(JoinPlayerCommand joiner) throws Exception {
        this.send(gson.toJson(joiner));
    }

    private static class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
        @Override
        public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("serverMessageType").getAsString();
            ServerMessage.ServerMessageType serverMessageType  = ServerMessage.ServerMessageType.valueOf(typeString);

            return switch(serverMessageType) {
                case ERROR -> context.deserialize(jsonElement, ErrorMessage.class);
                case LOAD_GAME -> context.deserialize(jsonElement, LoadGameMessage.class);
                case NOTIFICATION -> context.deserialize(jsonElement, ServerMessage.class);
            };
        }
    }
}
