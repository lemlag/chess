package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {

    GameData[] listGames();

    String createGame(String gameName);

    GameData getGameData(String gameID);

    void updateGame(String gameID, String clientColor, String username);

    void clearGames();
}
