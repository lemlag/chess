package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.RegisterRequest;
import responses.LoginResponse;
import service.UserService;

import java.sql.SQLException;

public class UserDAOTests {

    LoginResponse response;
    MySQLUserDAO userDB;
    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
        userDB = MySQLUserDAO.getInstance();
    }

    @Test
    public void constructorTest() throws Exception{
        Assertions.assertNotNull(new MySQLUserDAO());
    }

    @Test
    public void instanceTest() throws Exception{
        Assertions.assertNotNull(MySQLUserDAO.getInstance());
    }

    @Test
    public void clearUsersTest() throws Exception{
        userDB.createUser("this", "name", "sfjod");
        userDB.clearUsers();
        Assertions.assertNull(userDB.getUser("this"));
    }

    @Test
    public void getUserTestPos() throws Exception{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserData user = userDB.getUser("username");
        Assertions.assertEquals("username", user.username());
        Assertions.assertTrue(encoder.matches("password", user.password()));
        Assertions.assertEquals("email", user.email());
    }

    @Test
    public void getUserTestNeg() throws Exception{
        UserData user = userDB.getUser("InvalidUsername");
        Assertions.assertNull(user);
    }

    @Test
    public void createUserPos() throws Exception{
        userDB.createUser("User", "pass", "em");
        UserData user = userDB.getUser("User");
        Assertions.assertEquals("pass", user.password());
        Assertions.assertEquals("em", user.email());
    }

    @Test
    public void createUserNeg(){
        Assertions.assertThrows(SQLException.class, ()->userDB.createUser("1010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101", "pass", "emai"));
    }


}
