package dataAccess;

import chess.ChessGame;

public interface GameDAO {

    public ChessGame[] listGames();

    public String createGame(String gameName);

    public ChessGame getGame(String gameID);

    public void updateGame(String gameID, String ClientColor);

    public void clearGames();
}
