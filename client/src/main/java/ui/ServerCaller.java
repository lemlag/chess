package ui;

import com.google.gson.Gson;
import requests.*;
import responses.*;

import java.io.*;
import java.net.*;

public class ServerCaller{

    private static final Gson gson;

    static{
        gson = new Gson();
    }
    public static LoginResponse logInHandler(LoginRequest request) throws URISyntaxException, IOException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:8080/session");
        return getLoginResponse(reqBody, uri);
    }

    public static void logOutHandler(String authToken) throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("DELETE");
        http.addRequestProperty("authorization", authToken);
        http.connect();
    }

    public static LoginResponse registerHandler(RegisterRequest request) throws IOException, URISyntaxException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:8080/user");
        return getLoginResponse(reqBody, uri);
    }

    public static ListGamesResponse listGamesHandler(String authToken) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("GET");
        http.addRequestProperty("authorization", authToken);
        http.connect();

        ListGamesResponse response = null;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, ListGamesResponse.class);
            }
        }
        return response;
    }

    public static CreateGameResponse createGameHandler(CreateGameRequest request, String authToken) throws URISyntaxException, IOException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("POST");
        http.addRequestProperty("authorization", authToken);
        writeRequestBody(reqBody, http);
        http.connect();

        CreateGameResponse response = null;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, CreateGameResponse.class);
            }
        }
        return response;
    }

    public static void joinGameHandler(JoinGameRequest request, String authToken) throws IOException, URISyntaxException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("PUT");
        http.addRequestProperty("authorization", authToken);
        writeRequestBody(reqBody, http);
        http.connect();
    }

    public static void clearDatabaseHandler() throws URISyntaxException, IOException {
        URI uri = new URI("http://localhost:8080/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("DELETE");
        http.connect();
    }

    private static LoginResponse getLoginResponse(String reqBody, URI uri) throws IOException {
        LoginResponse response = null;
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("POST");
        writeRequestBody(reqBody, http);
        http.connect();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, LoginResponse.class);
            }
        }
        return response;
    }

    public static void writeRequestBody(String reqBody, HttpURLConnection http) throws IOException{
        if(!reqBody.isEmpty()){
            http.setDoOutput(true);
            try(var outputStream = http.getOutputStream()){
                outputStream.write(reqBody.getBytes());
            }
        }
    }
}
