package serviceTests;

import chess.ChessGame;
import dataAccess.BadRequestException;
import dataAccess.MemoryGameDAO;
import dataAccess.UsernameTakenException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.JoinGameRequest;
import requests.RegisterRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.LoginResponse;
import service.GameService;
import service.UserService;

public class GameServiceTests {

    LoginResponse response;
    @BeforeEach
    public void setup() throws BadRequestException, UsernameTakenException {
        UserService.clearDB();
        response = UserService.register(new RegisterRequest("username", "password", "email"));
    }

    @Test
    public void listGamesEmpty(){
        ListGamesResponse resp = new ListGamesResponse(null, null);
        Assertions.assertNotNull(GameService.listGames().getGames());
    }

    @Test
    public void listGamesLarge() throws BadRequestException {
        GameData[] games = new GameData[1];
        GameData model = new GameData(null, null, "One", 1, new ChessGame());
        games[0] = model;
        ListGamesResponse resp = new ListGamesResponse(games,null);
        GameService.createGame("One");
        Assertions.assertEquals(resp.getGames()[0].gameID(), GameService.listGames().getGames()[0].gameID());
    }

    @Test
    public void createGameTrue() throws BadRequestException {
        CreateGameResponse resp = GameService.createGame("One");
        Assertions.assertNotNull(MemoryGameDAO.getInstance().getGameData(resp.getGameID()));
    }

    @Test
    public void createGameFalse(){
        boolean errorFlag;
        try{
            GameService.createGame(null);
            errorFlag = false;
        } catch (BadRequestException e) {
            errorFlag = true;
        }
        Assertions.assertTrue(errorFlag);
    }

    @Test
    public void joinGameTrue() throws BadRequestException, UsernameTakenException {
        JoinGameRequest req = new JoinGameRequest("WHITE", "1");
        GameService.createGame("Orange");
        GameService.joinGame(req, "username");
    }

    @Test
    public void joinGameFalse() throws BadRequestException, UsernameTakenException {
        boolean errorFlag;
        JoinGameRequest req = new JoinGameRequest("WHITE", "1");
        GameService.createGame("Orange");
        GameService.joinGame(req, "username");
        try{
            GameService.joinGame(req, "user2");
            errorFlag = false;
        } catch(UsernameTakenException usEx){
            errorFlag = true;
        }
        Assertions.assertTrue(errorFlag);
    }
}
