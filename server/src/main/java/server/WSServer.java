package server;

import chess.*;
import com.google.gson.*;
import dataAccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

@WebSocket
public class WSServer {

    private static final Gson gson;

    private static final HashMap<String, HashSet<String>> observers = new HashMap<>();
    private static final HashMap<String, Session> sessionsMap = new HashMap<>();

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer());
        gson = builder.create();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {

        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String response;
            String authToken = command.getAuthString();

            if (UserService.containsAuth(authToken)) {
                String user = UserService.getUser(authToken);
                System.out.println(user);
                switch (command.getCommandType()) {
                    case LEAVE -> {
                        LeaveCommand com = (LeaveCommand) command;
                        leave(authToken, user, com.getGameID().toString());
                        response = null;
                    }
                    case RESIGN -> {
                        ResignCommand com = (ResignCommand) command;
                        response = resign(authToken, user, com.getGameID().toString());
                    }
                    case MAKE_MOVE -> {
                        MakeMoveCommand com = (MakeMoveCommand) command;
                        makeMove(authToken, com.getGameID().toString(), user, com.getMove());
                        response = null;
                    }
                    case JOIN_PLAYER -> {
                        JoinPlayerCommand com = (JoinPlayerCommand) command;
                        response = joinPlayer(com.getGameID().toString(), com.getAuthString(), session, user, com.getPlayerColor().toString());
                    }
                    case JOIN_OBSERVER -> {
                        JoinObserverCommand com = (JoinObserverCommand) command;
                        response = joinObserver(com.getGameID().toString(), com.getAuthString(), session, user);
                    }
                    default -> throw new DataAccessException("Invalid request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
            System.out.printf("\nReceived: %s", message);
            System.out.printf("\nSending: %s", response);
            if (response != null) {
                session.getRemote().sendString(response);
            }
        } catch(Exception e){
            ErrorMessage errorMessage = new ErrorMessage("error : " + e.getMessage());
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private void leave(String authToken, String user, String gameID) throws SQLException, DataAccessException, IOException {
        GameData game = GameService.getGame(gameID);
        if(user.equals(game.whiteUsername()) || user.equals(game.blackUsername())) {
            GameService.removeUser(user, gameID);
        }
        observers.get(gameID).remove(authToken);
        sessionsMap.remove(authToken);
        NotificationMessage notification = new NotificationMessage(user + " has left the game.");
        notifyObservers(null, gameID, gson.toJson(notification));
    }

    private String resign(String authToken, String user, String gameID) throws SQLException, DataAccessException, IOException {
        GameData game = GameService.getGame(gameID);
        NotificationMessage notification;
        if(game.whiteUsername().equals(user)){
            GameService.finishGame(gameID);
            notification = new NotificationMessage(user + " has resigned. Black player wins!");
        } else if(game.blackUsername().equals(user)){
            GameService.finishGame(gameID);
            notification = new NotificationMessage(user + " has resigned. White player wins!");
        } else{
            throw new DataAccessException("Not a player, cannot resign");
        }
        notifyObservers(authToken, gameID, gson.toJson(notification));
        return gson.toJson(notification);
    }

    private String joinPlayer(String gameID, String authToken, Session session, String user, String color) throws IOException, SQLException, DataAccessException {
        GameData gameData = GameService.getGame(gameID);
        if(color.equals("WHITE") && (!gameData.whiteUsername().equals(user))){
            throw new DataAccessException("Incorrect username");
        } else if(color.equals("BLACK") && (!gameData.blackUsername().equals(user))){
            throw new DataAccessException("Incorrect username");
        } else if(!color.equals("BLACK") && !color.equals("WHITE")){
            throw new DataAccessException("Wrong color");
        }
        sessionsMap.put(authToken, session);
        observers.get(gameID).add(authToken);
        LoadGameMessage loadGame = new LoadGameMessage(GameService.getGame(gameID));
        NotificationMessage notification = new NotificationMessage(user + " has joined the game as " + color + " player.");
        notifyObservers(authToken, gameID, gson.toJson(notification));
        return gson.toJson(loadGame);
    }

    private String joinObserver(String gameID, String authToken, Session session, String user) throws IOException, SQLException, DataAccessException {
        if(GameService.getGame(gameID) == null){
            throw new DataAccessException("Bad game ID");
        }
        sessionsMap.put(authToken, session);
        observers.get(gameID).add(authToken);
        LoadGameMessage loadGame = new LoadGameMessage(GameService.getGame(gameID));
        NotificationMessage notification = new NotificationMessage(user + " has joined the game as an observer.");
        notifyObservers(authToken, gameID, gson.toJson(notification));
        return gson.toJson(loadGame);
    }

    private void makeMove(String authToken, String gameID, String username, ChessMove move) throws SQLException, DataAccessException, InvalidMoveException, IOException {
        GameData gameStats = GameService.getGame(gameID);
        ChessGame game = gameStats.game();
        ChessGame.TeamColor teamColor;
        
        if(username.equals(gameStats.blackUsername())){
            teamColor = ChessGame.TeamColor.BLACK;
        } else if(username.equals(gameStats.whiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        } else{
            throw new DataAccessException("Not a player");
        }
        if(game.makeMove(move) != teamColor){
            throw new DataAccessException("Wrong player");
        }
        GameService.updateMove(gameID, username, game);
        LoadGameMessage loadGame = new LoadGameMessage(GameService.getGame(gameID));
        notifyObservers(null, gameID, gson.toJson(loadGame));
        NotificationMessage notification = getNotificationMessage(username, move);
        notifyObservers(authToken, gameID, gson.toJson(notification));
    }

    private static NotificationMessage getNotificationMessage(String username, ChessMove move) {
        String startingRow = String.valueOf(move.getStartPosition().getRow());
        String endingRow = String.valueOf(move.getEndPosition().getRow());
        String startingCol = null, endingCol = null;
        switch(move.getStartPosition().getColumn()){
            case 1 -> startingCol = "a";
            case 2 -> startingCol = "b";
            case 3 -> startingCol = "c";
            case 4 -> startingCol = "d";
            case 5 -> startingCol = "e";
            case 6 -> startingCol = "f";
            case 7 -> startingCol = "g";
            case 8 -> startingCol = "h";
        }
        switch(move.getEndPosition().getColumn()){
            case 1 -> endingCol = "a";
            case 2 -> endingCol = "b";
            case 3 -> endingCol = "c";
            case 4 -> endingCol = "d";
            case 5 -> endingCol = "e";
            case 6 -> endingCol = "f";
            case 7 -> endingCol = "g";
            case 8 -> endingCol = "h";
        }
        return new NotificationMessage(username + " moved piece " + startingCol + startingRow + " to " + endingCol + endingRow);
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
            Session session = sessionsMap.get(observer);
            session.getRemote().sendString(message);
        }
    }


    private static class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
        @Override
        public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

            return switch(commandType) {
                case LEAVE -> context.deserialize(jsonElement, LeaveCommand.class);
                case JOIN_PLAYER -> context.deserialize(jsonElement, JoinPlayerCommand.class);
                case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
                case JOIN_OBSERVER -> context.deserialize(jsonElement, JoinObserverCommand.class);
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
            };
        }
    }
}
