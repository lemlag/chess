package service;

import dataAccess.*;

public class UserService {

    public static void clearDB(){
        AuthDAO authenticator = MemoryAuthDAO.getInstance();
        authenticator.clearAuth();
        GameDAO gameData = MemoryGameDAO.getInstance();
        gameData.clearGames();
        UserDAO userData = MemoryUserDAO.getInstance();
        userData.clearUsers();
    }
}
