package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import server.WSServer;

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
        String gameID = gameInfo.createGame(gameName);
        WSServer.updateObserverGames(gameID);
        return new CreateGameResponse(gameID, null);
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
            gameInfo.updateGame(request.gameID(), request.playerColor(), username, false, null, false);
        } else if((request.playerColor().equals("WHITE") || request.playerColor().equals("BLACK"))){
            throw new UsernameTakenException();
        } else{
            throw new BadRequestException();
        }
    }

    public static void removeUser(String username, String gameID) throws SQLException, DataAccessException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        GameData game = gameInfo.getGameData(gameID);
        if(game.whiteUsername().equals(username)){
            gameInfo.updateGame(gameID, "WHITE", null, false, null, false);
        } else if(game.blackUsername().equals(username)){
            gameInfo.updateGame(gameID, "BLACK", null, false, null, false);
        }
    }

    public static GameData getGame(String gameID) throws SQLException, DataAccessException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        return gameInfo.getGameData(gameID);
    }

    public static void finishGame(String gameID) throws SQLException, DataAccessException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        gameInfo.updateGame(gameID, null, null, false, null, true);
    }

    public static void updateMove(String gameID, String username, ChessGame game) throws SQLException, DataAccessException {
        GameDAO gameInfo = MySQLGameDAO.getInstance();
        gameInfo.updateGame(gameID, null, username, true, game, false);
    }
}
