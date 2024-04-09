package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    Integer gameID;
    ChessMove move;
    public MakeMoveCommand(String authToken) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
