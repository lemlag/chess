package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;

public interface GameDAO {

    GameData[] listGames() throws DataAccessException, SQLException;

    String createGame(String gameName) throws DataAccessException, SQLException;

    GameData getGameData(String gameID) throws DataAccessException, SQLException;

    void updateGame(String gameID, String clientColor, String username, boolean moveMade, ChessGame board, boolean finish) throws DataAccessException, SQLException;

    void clearGames() throws DataAccessException, SQLException;
}
