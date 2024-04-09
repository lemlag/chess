package ui;

import dataAccess.DataAccessException;
import requests.*;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.ServerCaller.*;

public class ServerFacade {

    public static LoginResponse logIn(String username, String password){
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response;
        try{
            response = logInHandler(request);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e){
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
        } catch (DataAccessException e){
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
        } catch (DataAccessException e){
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
        } catch (DataAccessException e){
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
            new WebSocketCommunicator(client);
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
        } catch (DataAccessException e){
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
        } catch (DataAccessException e){
            response = new ListGamesResponse(null,  e.getMessage());
        }
        return response;
    }

}
