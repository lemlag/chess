package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import service.UserService;

import java.sql.SQLException;

public class UserServiceTests {

    LoginResponse response;
    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
    }


    @Test
    public void clearPosTest() throws SQLException, DataAccessException {
        UserService.clearDB();
        Assertions.assertNull(UserService.getUser(response.getAuthToken()));
    }

    @Test
    public void containsAuthTrueTest() throws DataAccessException, SQLException {
        Assertions.assertTrue(UserService.containsAuth(response.getAuthToken()));
    }

    @Test
    public void containsAuthFalseTest() throws SQLException, DataAccessException {
        Assertions.assertFalse(UserService.containsAuth("123456"));
    }

    @Test
    public void getUserTrueTest() throws SQLException, DataAccessException {
        Assertions.assertEquals(UserService.getUser(response.getAuthToken()), "username");
    }

    @Test
    public void getUserFalseTest() throws SQLException, DataAccessException {
        Assertions.assertNull(UserService.getUser("12345678"));
    }

    @Test
    public void logInTrueTest() throws DataAccessException, SQLException {
        LoginRequest req = new LoginRequest("username", "password");
        Assertions.assertEquals(UserService.logIn(req).getUsername(), "username");
        Assertions.assertNotNull(UserService.logIn(req).getAuthToken());
    }

    @Test
    public void logInFalseTest() throws DataAccessException, SQLException {
        LoginRequest req = new LoginRequest("notAUsername", "password");
        boolean exceptionThrown;
        try{
            UserService.logIn(req);
            exceptionThrown = false;
        } catch(UnauthorizedException unEx){
            exceptionThrown = true;
        }
        Assertions.assertTrue(exceptionThrown);
    }

    @Test
    public void logOutTrueTest() throws SQLException, DataAccessException {
        UserService.logOut(response.getAuthToken());
        Assertions.assertNull(UserService.getUser(response.getAuthToken()));
    }

    @Test
    public void logOutFalseTest() throws SQLException, DataAccessException {
        UserService.logOut("abcd");
        Assertions.assertEquals(UserService.getUser(response.getAuthToken()), response.getUsername());
    }

    @Test
    public void registerTrueTest() throws DataAccessException, SQLException {
        RegisterRequest req = new RegisterRequest("user2", "passW", "em");
        RegisterRequest req1 = new RegisterRequest("user3", "passW", "em");
        UserService.register(req);
        UserService.register(req1);
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").password(), "passW" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").username(), "user2" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").email(), "em" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").password(), "passW" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").username(), "user3" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").email(), "em" );
    }

    @Test
    public void registerFalseTest() throws DataAccessException, SQLException {
        RegisterRequest req = new RegisterRequest("username", "p", "m");
        boolean errorFlag;
        try{
            UserService.register(req);
            errorFlag = false;
        } catch(UsernameTakenException usEx){
            errorFlag = true;
        }
        Assertions.assertTrue(errorFlag);
    }

}
