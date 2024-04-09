package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private ChessGame.TeamColor playerColor;
    private Integer gameID;


    public JoinPlayerCommand(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }



}
