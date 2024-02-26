package model;

import chess.ChessGame;

public class GameData {
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final int gameID;
    private final ChessGame game;

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

    public void setWhiteUsername(String username){whiteUsername = username;}

    public void setBlackUsername(String username){blackUsername = username;}
}
