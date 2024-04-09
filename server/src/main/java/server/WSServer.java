package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

@WebSocket
public class WSServer {

    private Gson gson;

    private static final HashMap<String, HashSet<String>> observers = new HashMap<>();
    private static final HashMap<String, Session> sessionsMap = new HashMap<>();

    @OnWebSocketConnect
    public void setup(){
        gson = new Gson();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        String response;
        String authToken = command.getAuthString();
        if(UserService.containsAuth(authToken)) {
            String user = UserService.getUser(authToken);
            switch (command.getCommandType()) {
                case LEAVE -> {
                    LeaveCommand com = (LeaveCommand) command;
                    response = leave(authToken, user, com.getGameID().toString());
                }
                case RESIGN -> {
                    response = resign();
                }
                case MAKE_MOVE -> {
                    response = make_move();
                }
                case JOIN_PLAYER -> {
                    JoinPlayerCommand com = (JoinPlayerCommand) command;
                    response = join_player(com.getGameID().toString(), com.getAuthString(), session, user, com.getPlayerColor().toString());
                }
                case JOIN_OBSERVER -> {
                    JoinObserverCommand com = (JoinObserverCommand) command;
                    response = join_observer(com.getGameID().toString(), com.getAuthString(), session, user);
                }
                default -> response = null;
            }
        } else{
            response = null;
        }
        System.out.printf("Received: %s", message);
        session.getRemote().sendString( response);
    }

    private void leave(String authToken, String user, String gameID) throws SQLException, DataAccessException, IOException {
        GameService.removeUser(user, gameID);
        observers.get(gameID).remove(authToken);
        sessionsMap.remove(authToken);
        NotificationMessage notification = new NotificationMessage(STR."\{user} has left the game.");
        notifyObservers(authToken, gameID, gson.toJson(notification));
    }

    private String join_player(String gameID, String authToken, Session session, String user, String color) throws IOException, SQLException, DataAccessException {
        sessionsMap.put(authToken, session);
        observers.get(gameID).add(authToken);
        LoadGameMessage loadGame = new LoadGameMessage(GameService.getGame(gameID));
        NotificationMessage notification = new NotificationMessage(STR."\{user} has joined the game as \{color} player.");
        notifyObservers(authToken, gameID, gson.toJson(notification));
        return gson.toJson(loadGame);
    }

    private String join_observer(String gameID, String authToken, Session session, String user) throws IOException, SQLException, DataAccessException {
        sessionsMap.put(gameID, session);
        observers.get(gameID).add(authToken);
        LoadGameMessage loadGame = new LoadGameMessage(GameService.getGame(gameID));
        NotificationMessage notification = new NotificationMessage(STR."\{user} has joined the game as an observer.");
        notifyObservers(authToken, gameID, gson.toJson(notification));
        return gson.toJson(loadGame);
    }

    public static void updateObserverGames(String gameID){
        observers.put(gameID, new HashSet<String>());
    }

    private void notifyObservers(String authToken, String gameID, String message) throws IOException {
        HashSet<String> gameObservers = observers.get(gameID);
        for(String observer : gameObservers){
            if(observer.equals(authToken)){
                continue;
            }
            sessionsMap.get(observer).getRemote().sendString(message);
        }
    }
}
