package dataAccess;

import chess.ChessGame;

public interface GameDAO {

    ChessGame[] listGames();

    String createGame(String gameName);

    ChessGame getGame(String gameID);

    void updateGame(String gameID, String ClientColor);

    void clearGames();
}
