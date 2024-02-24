package server;

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
        if(!AuthService.containsAuth(authToken)){
            Spark.halt(401, "Error: unauthorized");
        }
    }
    private Object regRequest(Request req, Response res){
        ClearService.getInstance.clear();

    }

    private Object clearRequest(Request req, Response res){

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
