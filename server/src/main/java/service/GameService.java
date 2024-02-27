package service;

import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.UsernameTakenException;
import model.GameData;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;

public class GameService {

    public static ListGamesResponse listGames(){
        GameDAO gameInfo = MemoryGameDAO.getInstance();
        return new ListGamesResponse(gameInfo.listGames(), null);
    }

    public static CreateGameResponse createGame(String gameName) throws BadRequestException {
        if(gameName.isEmpty()){
            throw new BadRequestException();
        }
        GameDAO gameInfo = MemoryGameDAO.getInstance();
        return new CreateGameResponse(gameInfo.createGame(gameName), null);
    }

    public static void joinGame(JoinGameRequest request, String username) throws UsernameTakenException, BadRequestException {
        GameDAO gameInfo = MemoryGameDAO.getInstance();
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
