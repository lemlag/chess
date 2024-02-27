package server;

import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import dataAccess.UsernameTakenException;
import requests.*;
import responses.*;
import service.GameService;
import service.UserService;

import spark.*;

public class Server {

    private Gson gson;
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        gson = new Gson();
        Spark.before("/game", this::authRequest);
        Spark.post("/user", this::regRequest);
        Spark.delete("/db", this::clearRequest);
        Spark.post("/session", this::logRequest);
        Spark.delete("/session", this::logoutRequest);
        Spark.get("/game", this::listGamesRequest);
        Spark.post("/game", this::createGameRequest);
        Spark.put("/game", this::joinGameRequest);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void authRequest(Request req, Response res) throws UnauthorizedException {
        String authToken = req.headers("authorization");
        if(!UserService.containsAuth(authToken)){
            throw new UnauthorizedException();
        }
    }

    private Object logoutRequest(Request req, Response res) throws UnauthorizedException{
        authRequest(req, res);
        UserService.logOut(req.headers("authorization"));
        return gson.toJson("");
    }

    private Object logRequest(Request req, Response res) throws UnauthorizedException {
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResponse response = UserService.logIn(request);
        return gson.toJson(response);
    }

    private Object regRequest (Request req, Response res) throws UsernameTakenException{
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        LoginResponse response = UserService.register(request);
        return gson.toJson(response);
    }

    private Object listGamesRequest(Request req, Response res){
        ListGamesResponse response = GameService.listGames();
        return gson.toJson(response);
    }

    private Object createGameRequest(Request req, Response res){
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResponse response = GameService.createGame(request.gameName());
        return gson.toJson(response);
    }

    private Object joinGameRequest(Request req, Response res){
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        String username = UserService.getUser(req.headers("authorization"));
        GameService.joinGame(request, username);
        return gson.toJson("");
    }

    private Object clearRequest(Request req, Response res){
        UserService.clearDB();
        return gson.toJson("");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
