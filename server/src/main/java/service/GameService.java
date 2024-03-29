package service;

import dataAccess.*;
import model.GameData;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;

import java.sql.SQLException;

public class GameService {

    public static ListGamesResponse listGames() throws SQLException, DataAccessException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        return new ListGamesResponse(gameInfo.listGames(), null);
    }

    public static CreateGameResponse createGame(String gameName) throws DataAccessException, SQLException {
        if(gameName == null || gameName.isEmpty()){
            throw new BadRequestException();
        }
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        return new CreateGameResponse(gameInfo.createGame(gameName), null);
    }

    public static void joinGame(JoinGameRequest request, String username) throws DataAccessException, SQLException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        if(request.gameID() == null){
            throw new BadRequestException();
        }
        GameData gameDetails = gameInfo.getGameData(request.gameID());
        if(gameDetails == null){
            throw new BadRequestException();
        } else if(request.playerColor() == null){
            return;
        } else if((request.playerColor().equals("WHITE")  && gameDetails.whiteUsername() == null)
                || (request.playerColor().equals("BLACK") && gameDetails.blackUsername() == null)){
            gameInfo.updateGame(request.gameID(), request.playerColor(), username);
        } else if((request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK"))){
            throw new UsernameTakenException();
        } else{
            throw new BadRequestException();
        }
    }
}
