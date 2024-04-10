package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private final ChessGame.TeamColor playerColor;
    private final Integer gameID;


    public JoinPlayerCommand(String authToken, ChessGame.TeamColor playerColor, Integer gameID) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }



}
