package service;

import dataAccess.*;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.*;
import responses.*;

import java.sql.SQLException;

public class UserService {
    public static void clearDB() throws SQLException, DataAccessException {
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
        return MemoryAuthDAO.getInstance().authDAOGetUsername(authToken);
    }

    private static LoginResponse createAuthService(String username){
        AuthDAO authInfo = MemoryAuthDAO.getInstance();
        String authToken = authInfo.createAuth(username);
        return new LoginResponse(username, authToken, null);
    }

    public static LoginResponse logIn(LoginRequest request) throws DataAccessException, SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        UserData user = userInfo.getUser(request.username());
        if(user != null && encoder.matches(request.password(), user.password())){
            response = createAuthService(request.username());
        } else{
            throw new UnauthorizedException();
        }

        return response;
    }

    public static void logOut(String authToken){
        AuthDAO authInfo = MemoryAuthDAO.getInstance();
        authInfo.deleteAuth(authToken);
    }

    public static LoginResponse register (RegisterRequest request) throws DataAccessException, SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDAO userInfo = MemoryUserDAO.getInstance();
        LoginResponse response;
        UserData user = userInfo.getUser(request.username());
        if(request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException();
        } else if(user == null){
            String passHash = encoder.encode(request.password());
            userInfo.createUser(request.username(), passHash, request.email());
            response = createAuthService(request.username());
        } else{
            throw new UsernameTakenException();
        }
        return response;
    }
}
