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

import java.sql.SQLException;

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

    private void authRequest(Request req) throws DataAccessException, SQLException {
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
            res.status(200);
        } catch(UnauthorizedException unEx){
            response = new ErrorResponse("Error: unauthorized");
            res.status(401);
        }  catch(Exception e){
            response = new ErrorResponse(e.getMessage());
            res.status(500);
        }
        return gson.toJson(response);
    }

    private Object logRequest(Request req, Response res) {
        LoginResponse response;
        try {
            LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
            response = UserService.logIn(request);
            res.status(200);
        } catch(UnauthorizedException unEx){
            response = new LoginResponse(null, null, "Error: unauthorized");
            res.status(401);
        } catch(Exception e){
            response = new LoginResponse(null, null, e.getMessage());
            res.status(500);
        }
        return gson.toJson(response);
    }

    private Object regRequest (Request req, Response res){
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        LoginResponse response;
        try {
            response = UserService.register(request);
            res.status(200);
        }
        catch(UsernameTakenException userEx){
            response = new LoginResponse(null, null,"Error: already taken");
            res.status(403);
        } catch(BadRequestException badEx){
            response = new LoginResponse(null, null, "Error: bad request");
            res.status(400);
        }
        catch(Exception e){
            response = new LoginResponse(null, null, e.getMessage());
            res.status(500);
        }

        return gson.toJson(response);
    }

    private Object listGamesRequest(Request req, Response res) {
        ListGamesResponse response;
        try {
            authRequest(req);
            response = GameService.listGames();
            res.status(200);
            response.getGames();
        } catch(UnauthorizedException unEx){
            response = new ListGamesResponse(null, "Error: unauthorized");
            res.status(401);
        }  catch(Exception e){
            response = new ListGamesResponse(null, e.getMessage());
            res.status(500);
        }
        return gson.toJson(response);
    }

    private Object createGameRequest(Request req, Response res) {
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResponse response;
        try {
            authRequest(req);
            response = GameService.createGame(request.gameName());
            res.status(200);
        } catch(UnauthorizedException unEx){
            response = new CreateGameResponse(null, "Error: unauthorized");
            res.status(401);
        } catch (BadRequestException e) {
        response = new CreateGameResponse(null, "Error: bad request");
        res.status(400);
    } catch(Exception e){
        response = new CreateGameResponse(null, e.getMessage());
        res.status(500);
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
        res.status(200);
        } catch(UnauthorizedException unEx){
            response = new ErrorResponse("Error: unauthorized");
            res.status(401);
        } catch (BadRequestException e) {
            response = new ErrorResponse("Error: bad request");
            res.status(400);
        } catch (UsernameTakenException e) {
            response = new ErrorResponse("Error: already taken");
            res.status(403);
        } catch(Exception e){
            response = new ErrorResponse(e.getMessage());
            res.status(500);
        }
        return gson.toJson(response);
    }

    private Object clearRequest(Request req, Response res){

        ErrorResponse response;
        try {
            UserService.clearDB();
            response = new ErrorResponse(null);
            res.status(200);
        } catch(Exception e){
            response = new ErrorResponse(e.getMessage());
            res.status(500);
        }
        return gson.toJson(response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
