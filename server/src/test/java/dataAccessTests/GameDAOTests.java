package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MySQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.LoginResponse;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

public class GameDAOTests {

    LoginResponse response;
    CreateGameResponse response2;
    MySQLGameDAO gameDB;
    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
        response2 = GameService.createGame("gameName");
        gameDB = MySQLGameDAO.getInstance();
    }

    @Test
    public void constructorTest() throws Exception{
        Assertions.assertNotNull(new MySQLGameDAO());
    }

    @Test
    public void instanceTest() throws Exception{
        Assertions.assertNotNull(MySQLGameDAO.getInstance());
    }

    @Test
    public void clearGamesTest() throws Exception{
        String gameID = gameDB.createGame("this");
        gameDB.clearGames();
        Assertions.assertNull(gameDB.getGameData(gameID));
    }

    @Test
    public void getGameDataTestPos() throws Exception{
        GameData game = gameDB.getGameData(response2.getGameID());
        Assertions.assertEquals(String.valueOf(game.gameID()), response2.getGameID());
        Assertions.assertEquals(game.gameName(), "gameName");
        Assertions.assertNull(game.blackUsername());
        Assertions.assertNull(game.whiteUsername());
    }

    @Test
    public void getGameDataTestNeg() throws Exception{
        GameData game = gameDB.getGameData("1232");
        Assertions.assertNull(game);
    }

    @Test
    public void createGamePos() throws Exception{
        String gameID = gameDB.createGame("NotAGame");
        GameData game = gameDB.getGameData(gameID);
        Assertions.assertEquals(String.valueOf(game.gameID()), gameID);
        Assertions.assertEquals("NotAGame", game.gameName());
        Assertions.assertNull(game.blackUsername());
        Assertions.assertNull(game.whiteUsername());
    }

    @Test
    public void createGameNeg() {
        Assertions.assertDoesNotThrow(() -> gameDB.createGame(""));
    }

    @Test
    public void updateGamePos() throws SQLException, DataAccessException {
        gameDB.updateGame(response2.getGameID(), "WHITE", "gamers", false, null, false);
        Assertions.assertEquals(gameDB.getGameData(response2.getGameID()).whiteUsername(), "gamers");
    }

    @Test
    public void updateGameNeg() throws Exception{
        String gameID = gameDB.createGame("Fly");
        gameDB.updateGame(response2.getGameID(), "WHITE", "germs", false, null, false);
        Assertions.assertNull(gameDB.getGameData(gameID).whiteUsername());
    }

    @Test
    public void listGamesPos() throws Exception{
        gameDB.createGame("asg");
        gameDB.createGame("asdf");
        GameData[] games = gameDB.listGames();
        int i = 0;
        for (GameData game : games){
            Assertions.assertNotNull(game);
            i++;
        }
        Assertions.assertEquals(i, 3);
    }

    @Test
    public void listGamesNeg() throws Exception{
        gameDB.clearGames();
        Assertions.assertNotNull(gameDB.listGames());
    }
}
