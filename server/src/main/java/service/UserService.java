package service;

import dataAccess.*;
import model.UserData;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.*;

public class UserService {

    public static void clearDB(){
        AuthDAO authenticator = MemoryAuthDAO.getInstance();
        authenticator.clearAuth();
        MemoryGameDAO gameData = MemoryGameDAO.getInstance();
        gameData.clearGames();
        UserDAO userData = MemoryUserDAO.getInstance();
        userData.clearUsers();
    }

    public static boolean containsAuth(String authToken){
        AuthDAO authenticator = MemoryAuthDAO.getInstance();
        return authenticator.checkAuth(authToken);
    }

    public static LoginResponse createAuth(LoginRequest request){
        return null;
    }

    public static LoginResponse logIn(LoginRequest request){
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        UserData user = userInfo.getUser(request.username);
        if(user.getPassword().equals(request.password)){
            response = createAuth(request);
        } else{
            response = null; // genError401();
            System.out.println("No generated password");
        }

        return response;
    }

    public static LoginResponse register(RegisterRequest request){
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        if(userInfo.getUser(request.username) == null){
            userInfo.createUser(request.username, request.password, request.email);
            response = createAuth(request);

            System.out.println("Yes generated password");
        } else{
            response = null; //genError403();
            System.out.println("No generated password");
        }
        return response;
    }
}
