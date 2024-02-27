package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import service.UserService;

public class UserServiceTests {

    LoginResponse response;
    @BeforeEach
    public void setup() throws BadRequestException, UsernameTakenException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
    }


    @Test
    public void clearPosTest(){
        UserService.clearDB();
        Assertions.assertNull(UserService.getUser(response.getAuthToken()));
    }

    @Test
    public void containsAuthTrueTest() throws DataAccessException {
        Assertions.assertTrue(UserService.containsAuth(response.getAuthToken()));
    }

    @Test
    public void containsAuthFalseTest(){
        Assertions.assertFalse(UserService.containsAuth("123456"));
    }

    @Test
    public void getUserTrueTest(){
        Assertions.assertEquals(UserService.getUser(response.getAuthToken()), "username");
    }

    @Test
    public void getUserFalseTest(){
        Assertions.assertNull(UserService.getUser("12345678"));
    }

    @Test
    public void logInTrueTest() throws DataAccessException {
        LoginRequest req = new LoginRequest("username", "password");
        Assertions.assertEquals(UserService.logIn(req).getUsername(), "username");
        Assertions.assertNotNull(UserService.logIn(req).getAuthToken());
    }

    @Test
    public void logInFalseTest() throws DataAccessException{
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
    public void logOutTrueTest(){
        UserService.logOut(response.getAuthToken());
        Assertions.assertNull(UserService.getUser(response.getAuthToken()));
    }

    @Test
    public void logOutFalseTest(){
        UserService.logOut("abcd");
        Assertions.assertEquals(UserService.getUser(response.getAuthToken()), response.getUsername());
    }

    @Test
    public void registerTrueTest() throws DataAccessException {
        RegisterRequest req = new RegisterRequest("user2", "passW", "em");
        RegisterRequest req1 = new RegisterRequest("user3", "passW", "em");
        LoginResponse res = UserService.register(req);
        LoginResponse res2 = UserService.register(req1);
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").password(), "passW" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").username(), "user2" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user2").email(), "em" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").password(), "passW" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").username(), "user3" );
        Assertions.assertEquals(MemoryUserDAO.getInstance().getUser("user3").email(), "em" );
    }

    @Test
    public void registerFalseTest() throws DataAccessException{
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
