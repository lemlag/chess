package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.LoginResponse;
import service.UserService;

import java.sql.SQLException;

public class AuthDAOTests {
    LoginResponse response;
    MySQLAuthDAO authDB;
    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
        authDB = MySQLAuthDAO.getInstance();
    }

    @Test
    public void constructorTest() throws Exception{
        Assertions.assertNotNull(new MySQLAuthDAO());
    }

    @Test
    public void instanceTest() throws Exception{
        Assertions.assertNotNull(MySQLAuthDAO.getInstance());
    }

    @Test
    public void clearAuthTest() throws Exception{
        String authToken;
        authToken = authDB.createAuth("this");
        authDB.clearAuth();
        Assertions.assertFalse(authDB.checkAuth(authToken));
    }

    @Test
    public void authDAOGetUsernameTestPos() throws Exception{
        String username = authDB.authDAOGetUsername(response.getAuthToken());
        Assertions.assertEquals(username, "username");
    }

    @Test
    public void authDAOGetUsernameTestNeg() throws Exception{
        String username = authDB.authDAOGetUsername("1232");
        Assertions.assertNull(username);
    }

    @Test
    public void createAuthPos() throws Exception{
        String authToken = authDB.createAuth("Non");
        Assertions.assertTrue(authDB.checkAuth(authToken));
        Assertions.assertEquals("Non", authDB.authDAOGetUsername(authToken));
    }

    @Test
    public void createAuthNeg() throws SQLException, DataAccessException {
        String authToken = authDB.createAuth("\u4014");
        Assertions.assertNotEquals(authDB.authDAOGetUsername(response.getAuthToken()), "\u4014");
    }

    @Test
    public void deleteAuthPos() throws SQLException, DataAccessException {
        authDB.deleteAuth(response.getAuthToken());
        Assertions.assertFalse(authDB.checkAuth(response.getAuthToken()));
    }

    @Test
    public void deleteAuthNeg() throws Exception{
        authDB.deleteAuth("woeo");
        Assertions.assertTrue(authDB.checkAuth(response.getAuthToken()));
    }

    @Test
    public void checkAuthPos() throws Exception{
        Assertions.assertTrue(authDB.checkAuth(response.getAuthToken()));
    }

    @Test
    public void checkAuthNeg() throws Exception{
        Assertions.assertFalse(authDB.checkAuth("RandomString"));
    }
}
