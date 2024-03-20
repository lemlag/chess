package service;

import dataAccess.*;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.*;
import responses.*;

import java.sql.SQLException;

public class UserService {
    public static void clearDB() throws SQLException, DataAccessException {
        AuthDAO authenticator = MySQLAuthDAO.getInstance();
        authenticator.clearAuth();
        MySQLGameDAO gameData = MySQLGameDAO.getInstance();
        gameData.clearGames();
        UserDAO userData = MySQLUserDAO.getInstance();
        userData.clearUsers();

    }

    public static boolean containsAuth(String authToken) throws SQLException, DataAccessException {
        AuthDAO authenticator = MySQLAuthDAO.getInstance();
        return authenticator.checkAuth(authToken);
    }

    public static String getUser(String authToken) throws SQLException, DataAccessException {
        return MySQLAuthDAO.getInstance().authDAOGetUsername(authToken);
    }

    private static LoginResponse createAuthService(String username) throws SQLException, DataAccessException {
        AuthDAO authInfo = MySQLAuthDAO.getInstance();
        String authToken = authInfo.createAuth(username);
        return new LoginResponse(username, authToken, null);
    }

    public static LoginResponse logIn(LoginRequest request) throws DataAccessException, SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDAO userInfo = MySQLUserDAO.getInstance();
        LoginResponse response;
        UserData user = userInfo.getUser(request.username());
        if(user != null && encoder.matches(request.password(), user.password())){
            response = createAuthService(request.username());
        } else{
            throw new UnauthorizedException();
        }

        return response;
    }

    public static void logOut(String authToken) throws SQLException, DataAccessException {
        AuthDAO authInfo = MySQLAuthDAO.getInstance();
        authInfo.deleteAuth(authToken);
    }

    public static LoginResponse register (RegisterRequest request) throws DataAccessException, SQLException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDAO userInfo = MySQLUserDAO.getInstance();
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
