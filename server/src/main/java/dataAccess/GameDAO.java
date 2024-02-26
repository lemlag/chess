package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    GameData[] listGames();

    int createGame(String gameName);

    ChessGame getGameFromData(String gameID);

    void updateGame(String gameID, String clientColor);

    void clearGames();
}
