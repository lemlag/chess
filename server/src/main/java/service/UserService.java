package service;

import dataAccess.*;

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
}
