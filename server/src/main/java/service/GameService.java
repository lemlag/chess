package service;

import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;

public class GameService {

    public static ListGamesResponse listGames(){
        GameDAO gameInfo = MemoryGameDAO.getInstance();
        return new ListGamesResponse(gameInfo.listGames(), null);
    }

    public static CreateGameResponse createGame(String gameName){
        GameDAO gameInfo = MemoryGameDAO.getInstance();
        return new CreateGameResponse(gameInfo.createGame(gameName), null);
    }

    public static void joinGame(JoinGameRequest request, String username){
        GameDAO gameInfo = MemoryGameDAO.getInstance();
        GameData gameDetails = gameInfo.getGameData(request.gameID());
        if((request.playerColor().equals("WHITE")  && gameDetails.whiteUsername().isEmpty())
                || (request.playerColor().equals("BLACK") && gameDetails.blackUsername().isEmpty())){
            gameInfo.updateGame(request.gameID(), request.playerColor(), username);
        } else if(request.playerColor().isEmpty()){
            return;
        }
    }
}
