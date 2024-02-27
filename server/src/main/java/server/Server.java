package server;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import service.UserService;

import spark.*;
import responses.*;

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

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void authRequest(Request req, Response res){
        String authToken = req.headers("authorization");
        if(!UserService.containsAuth(authToken)){
            Spark.halt(401, "Error: unauthorized");
        }
    }

    private Object logoutRequest(Request req, Response res){
        authRequest(req, res);
        UserService.logOut(req.headers("authorization"));
        return gson.toJson("");
    }

    private Object logRequest(Request req, Response res){
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResponse response = UserService.logIn(request);
        return gson.toJson(response);
    }

    private Object regRequest(Request req, Response res){
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        LoginResponse response = UserService.register(request);
        System.out.println(gson.toJson(response, LoginResponse.class));
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
