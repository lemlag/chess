package server;

import com.google.gson.Gson;
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

    private void authRequest(Request req) throws UnauthorizedException {
        String authToken = req.headers("authorization");
        if(!UserService.containsAuth(authToken)){
            throw new UnauthorizedException();
        }
    }

    private Object logoutRequest(Request req, Response res){
        ErrorResponse response;
        try {
            authRequest(req);
            UserService.logOut(req.headers("authorization"));
            response = new ErrorResponse(null);
        } catch(UnauthorizedException unEx){
            response = new ErrorResponse("Error: unauthorized");
            res.status(401);
        }
        return gson.toJson(response);
    }

    private Object logRequest(Request req, Response res) {
        LoginResponse response;
        try {
            LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
            response = UserService.logIn(request);
        } catch(UnauthorizedException unEx){
            response = new LoginResponse(null, null, "Error: unauthorized");
            res.status(401);
        }
        return gson.toJson(response);
    }

    private Object regRequest (Request req, Response res){
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        LoginResponse response;
        try {
            response = UserService.register(request);
        }
        catch(UsernameTakenException userEx){
            response = new LoginResponse(null, null,"Error: already taken");
            res.status(403);
        }
        return gson.toJson(response);
    }

    private Object listGamesRequest(Request req, Response res) {
        ListGamesResponse response;
        try {
            authRequest(req);
            response = GameService.listGames();
        } catch(UnauthorizedException unEx){
            response = new ListGamesResponse(null, "Error: unauthorized");
            res.status(401);
        }
        return gson.toJson(response);
    }

    private Object createGameRequest(Request req, Response res) {
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResponse response;
        try {
            authRequest(req);
            response = GameService.createGame(request.gameName());
        } catch(UnauthorizedException unEx){
            response = new CreateGameResponse(null, "Error: unauthorized");
            res.status(401);
        }
        return gson.toJson(response);
    }

    private Object joinGameRequest(Request req, Response res){
        ErrorResponse response;
        try{
        authRequest(req);
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        String username = UserService.getUser(req.headers("authorization"));
        GameService.joinGame(request, username);
        response = new ErrorResponse(null);
        } catch(UnauthorizedException unEx){
            response = new ErrorResponse("Error: unauthorized");
            res.status(401);
        }
        return gson.toJson(response);
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
