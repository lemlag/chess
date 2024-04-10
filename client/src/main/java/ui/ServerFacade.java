package ui;

import chess.ChessGame;
import chess.ChessMove;
import requests.*;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.ServerCaller.*;

public class ServerFacade {

    public static WebSocketCommunicator websocket;

    public static LoginResponse logIn(String username, String password){
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response;
        try{
            response = logInHandler(request);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            response = new LoginResponse(null, null, e.getMessage());
        }
        return response;
    }

    public static String logOut(String authToken){
        String message;
        try{
            logOutHandler(authToken);
            message = "Success";
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            message = e.getMessage();
        }
        return message;
    }

    public static String clear(){
        String message;
        try{
            clearDatabaseHandler();
            message = "Success";
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            message = e.getMessage();
        }
        return message;
    }

    public static LoginResponse register(String username, String password, String email){
        RegisterRequest request = new RegisterRequest(username, password, email);
        LoginResponse response;
        try{
            response = registerHandler(request);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            response = new LoginResponse(null, null, e.getMessage());
        }
        return response;
    }

    public static String joinGame(String playerColor, String gameID, String authToken, ServerMessageObserver client) {
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        String message;
        try{
            joinGameHandler(request, authToken);
            message = "Success";
            websocket = new WebSocketCommunicator(client);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            message = e.getMessage();
        }
        return message;
    }

    public static CreateGameResponse createGame(String gameName, String authToken){
        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResponse response;
        try{
            response = createGameHandler(request, authToken);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            response = new CreateGameResponse(null,  e.getMessage());
        }
        return response;
    }

    public static ListGamesResponse listGames(String authToken){
        ListGamesResponse response;
        try{
            response = listGamesHandler(authToken);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            response = new ListGamesResponse(null,  e.getMessage());
        }
        return response;
    }

    public static void makeMoves(String authToken, Integer gameID, ChessMove move) throws Exception {
        System.out.println("wowow");
        MakeMoveCommand mover = new MakeMoveCommand(authToken, gameID, move);
        websocket.makeMove(mover);
    }

    public static void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor color) throws Exception {
        System.out.println("Do stuff");
        JoinPlayerCommand joiner = new JoinPlayerCommand(authToken, color, gameID);
        websocket.joinPlayer(joiner);
    }

    public static void joinObserver(String authToken, Integer gameID) throws Exception {
        System.out.println("More stuff");
        JoinObserverCommand joiner = new JoinObserverCommand(authToken, gameID);
        websocket.joinObserver(joiner);
    }

    public static void leave(String authToken, Integer gameID) throws Exception {
        LeaveCommand leaver = new LeaveCommand(authToken, gameID);
        websocket.leave(leaver);
    }

    public static void resign(String authToken, Integer gameID) throws Exception {
        ResignCommand resigner = new ResignCommand(authToken, gameID);
        websocket.resign(resigner);
    }


}
