package clientTests;

import model.GameData;
import org.junit.jupiter.api.*;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import server.Server;
import ui.Client;
import ui.ServerCaller;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println(STR."Started test HTTP server on \{port}");
        ServerCaller.setPort(port);
    }

    @BeforeEach
    public void clearDataB(){
        System.out.println(ServerFacade.clear());
        authToken = ServerFacade.register("User", "Pass", "email").getAuthToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void clearDats(){
        ServerFacade.clear();
    }

    @Test
    public void loginPositive() {
        LoginResponse logPos = ServerFacade.logIn("User", "Pass");
        Assertions.assertEquals("User", logPos.getUsername());
    }

    @Test
    public void loginNegative() {
        LoginResponse logNeg = ServerFacade.logIn("Use", "Pas");
        Assertions.assertEquals(logNeg.getMessage(), "Error: unauthorized");
    }

    @Test
    public void logoutPositive() {
        String message = ServerFacade.logOut(authToken);
        Assertions.assertEquals("Success", message);
    }

    @Test
    public void logoutNegative() {
        String message = ServerFacade.logOut(authToken);
        message = ServerFacade.logOut(authToken);
        Assertions.assertEquals("Error: unauthorized", message);
    }

    @Test
    public void createGamePositive() {
        CreateGameResponse res = ServerFacade.createGame("One", authToken);
        Assertions.assertEquals("1", res.getGameID());
    }

    @Test
    public void createGameNegative() {
        CreateGameResponse res = ServerFacade.createGame("One", "Not an authToken");
        Assertions.assertEquals(res.getMessage(), "Error: unauthorized");
    }

    @Test
    public void joinGamePositive() {
        CreateGameResponse createGameResponse = ServerFacade.createGame("One", authToken);
        ServerFacade.joinGame("WHITE", createGameResponse.getGameID(), authToken, new Client());
        ListGamesResponse response = ServerFacade.listGames(authToken);
        GameData game = response.getGames()[0];
        Assertions.assertEquals("User", game.whiteUsername());
    }

    @Test
    public void joinGameNegative() {
        Assertions.assertEquals("Error: bad request", ServerFacade.joinGame("BLACK", "1", authToken, new Client()));
    }

    @Test
    public void joinObsPositive() {
        CreateGameResponse createGameResponse = ServerFacade.createGame("One", authToken);
        ServerFacade.joinGame(null, createGameResponse.getGameID(), authToken, new Client());
        ListGamesResponse response = ServerFacade.listGames(authToken);
        GameData game = response.getGames()[0];
        Assertions.assertNull(game.whiteUsername());
    }

    @Test
    public void joinObsNegative() {
        Assertions.assertEquals("Error: bad request", ServerFacade.joinGame(null, "1", authToken, new Client()));
    }

    @Test
    public void registerPositive() {
        LoginResponse logPos = ServerFacade.register("Us", "Pa", "Em");
        Assertions.assertEquals("Us", logPos.getUsername());
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void registerNegative() {
        Assertions.assertEquals(ServerFacade.register("User", "Pass", "email").getMessage(), "Error: already taken");
    }


    @Test
    public void listGamesPositive(){
        String gameID = ServerFacade.createGame("One", authToken).getGameID();
        ListGamesResponse listGamesResponse = ServerFacade.listGames(authToken);
        Assertions.assertEquals(Integer.valueOf(gameID), listGamesResponse.getGames()[0].gameID());
    }

    @Test
    public void listGamesNegative(){
        Assertions.assertEquals("Error: unauthorized", ServerFacade.listGames("Not an authtoken").getMessage());
    }
}
