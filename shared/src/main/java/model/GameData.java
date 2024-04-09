package model;

import chess.ChessGame;

public record GameData (String whiteUsername, String blackUsername, String gameName, int gameID, ChessGame game){
    public GameData gainUserWhite(String username) {
        return new GameData(username, blackUsername, gameName, gameID, game);
    }

    public GameData gainUserBlack(String username){
        return new GameData(whiteUsername, username, gameName, gameID, game);
    }

    public GameData moveMade(ChessGame game){
        return new GameData(whiteUsername, blackUsername, gameName, gameID, game);
    }
}
