package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    Integer gameID;
    ChessMove move;
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
