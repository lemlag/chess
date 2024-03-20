package ui;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import requests.*;
import responses.*;

import java.io.*;
import java.net.*;

public class ServerCaller{

    private static final Gson gson;

    private static int port;

    static{
        gson = new Gson();
        port = 8080;
    }

    public static void setPort(int portNum){
        port = portNum;
    }
    public static LoginResponse logInHandler(LoginRequest request) throws URISyntaxException, IOException, DataAccessException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:" + port + "/session");
        return getLoginResponse(reqBody, uri);
    }

    public static void logOutHandler(String authToken) throws URISyntaxException, IOException, DataAccessException {
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("DELETE");
        http.addRequestProperty("authorization", authToken);
        http.connect();
        errorHandling(http);
    }

    public static LoginResponse registerHandler(RegisterRequest request) throws IOException, URISyntaxException, DataAccessException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:" + port + "/user");
        return getLoginResponse(reqBody, uri);
    }

    public static ListGamesResponse listGamesHandler(String authToken) throws IOException, URISyntaxException, DataAccessException {

        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("GET");
        http.addRequestProperty("authorization", authToken);
        http.connect();

        ListGamesResponse response;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, ListGamesResponse.class);
            }
        }else {
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, ListGamesResponse.class);
            }
            throw new DataAccessException(response.getMessage());
        }
        return response;
    }

    public static CreateGameResponse createGameHandler(CreateGameRequest request, String authToken) throws URISyntaxException, IOException, DataAccessException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("POST");
        http.addRequestProperty("authorization", authToken);
        writeRequestBody(reqBody, http);
        http.connect();

        CreateGameResponse response;
        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, CreateGameResponse.class);
            }
        } else {
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, CreateGameResponse.class);
            }
            throw new DataAccessException(response.getMessage());
        }
        return response;
    }

    public static void joinGameHandler(JoinGameRequest request, String authToken) throws IOException, URISyntaxException, DataAccessException {
        String reqBody = gson.toJson(request);
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setReadTimeout(5000);
        http.setRequestMethod("PUT");
        http.addRequestProperty("authorization", authToken);
        writeRequestBody(reqBody, http);
        http.connect();
        errorHandling(http);
    }

    private static void errorHandling(HttpURLConnection http) throws IOException, DataAccessException {
        if(http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            ErrorResponse response;
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, ErrorResponse.class);
            }
            throw new DataAccessException(response.getMessage());
        }
    }

    public static void clearDatabaseHandler() throws URISyntaxException, IOException, DataAccessException {
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setReadTimeout(5000);
        http.setRequestMethod("DELETE");
        http.connect();
        errorHandling(http);
    }

    private static LoginResponse getLoginResponse(String reqBody, URI uri) throws IOException, DataAccessException {
        LoginResponse response;
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
        } else {
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                response = gson.fromJson(inputStreamReader, LoginResponse.class);
            }
            throw new DataAccessException(response.getMessage());
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
