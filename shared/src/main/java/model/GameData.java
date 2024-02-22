package model;

import chess.ChessGame;

public class GameData {
    String whiteUsername;
    String blackUsername;
    String gameName;
    int gameID;
    ChessGame game;

    public GameData(String whiteUsername, String blackUsername, String gameName, int gameID, ChessGame game) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.gameID = gameID;
        this.game = game;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame getGame() {
        return game;
    }
}
