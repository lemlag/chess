package server;

import com.google.gson.Gson;
import requests.LoginRequest;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.before("/game", this::authRequest);
        Spark.post("/user", this::regRequest);
        Spark.delete("/db", this::clearRequest);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void authRequest(Request req, Response res){
        String authToken = req.headers("authorization");
        if(!UserService.containsAuth(authToken)){
            Spark.halt(401, "Error: unauthorized");
        }
    }

    private Object regRequest(Request req, Response res){
        Gson gson = new Gson();
        LoginRequest request = (LoginRequest) gson.fromJson(String.valueOf(req), LoginRequest.class);

        LoginService service = new LoginService();
        LoginResult result = service.login(request);

        return gson.toJson(result);

    }

    private Object clearRequest(Request req, Response res){
        UserService.clearDB();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
