package service;

import dataAccess.*;
import model.UserData;
import requests.*;
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

    public static String getUser(String authToken){
        return MemoryAuthDAO.getInstance().getUser(authToken);
    }

    public static LoginResponse createAuthService(String username){
        AuthDAO authInfo = MemoryAuthDAO.getInstance();
        String authToken = authInfo.createAuth(username);
        return new LoginResponse(username, authToken);
    }

    public static LoginResponse logIn(LoginRequest request){
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        UserData user = userInfo.getUser(request.username());
        if(user.password().equals(request.password())){
            response = createAuthService(request.username());
        } else{
            response = null; // genError401();
            System.out.println("No generated password");
        }

        return response;
    }

    public static void logOut(String authToken){
        AuthDAO authInfo = MemoryAuthDAO.getInstance();
        authInfo.deleteAuth(authToken);
    }

    public static LoginResponse register(RegisterRequest request){
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        if(userInfo.getUser(request.username()) == null){
            userInfo.createUser(request.username(), request.password(), request.email());
            response = createAuthService(request.username());

            System.out.println("Yes generated password");
        } else{
            response = null; //genError403();
            System.out.println("No generated password");
        }
        return response;
    }
}
