package model;

import chess.ChessGame;

public record GameData (String whiteUsername, String blackUsername, String gameName, int gameID, ChessGame game, boolean finished){
    public GameData gainUserWhite(String username) {
        return new GameData(username, blackUsername, gameName, gameID, game, finished);
    }

    public GameData gainUserBlack(String username){
        return new GameData(whiteUsername, username, gameName, gameID, game, finished);
    }

    public GameData moveMade(ChessGame game){
        return new GameData(whiteUsername, blackUsername, gameName, gameID, game, finished);
    }

    public GameData finish(){
        return new GameData(whiteUsername, blackUsername, gameName, gameID, game, true);
    }
}
