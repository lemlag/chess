package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLGameDAO implements GameDAO{

    private final Gson gson;

    private static MySQLGameDAO instance;

    private static int gameIDCounter;

    public MySQLGameDAO() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gson = new Gson();
        gameIDCounter = 1;
        try(Connection connection = DatabaseManager.getConnection()) {

            var createUserTable = """
                    CREATE TABLE IF NOT EXISTS games (
                    gameID VARCHAR(80) NOT NULL,
                    gameData VARCHAR(10000) NOT NULL
                    )""";

            try(var createTableStatement = connection.prepareStatement(createUserTable)){
                createTableStatement.executeUpdate();
            }
        }
    }


    public GameData[] listGames() throws DataAccessException, SQLException {
        GameData[] gameList;
        int totalGames;
        try(Connection connection = DatabaseManager.getConnection()) {
            String count = "SELECT COUNT(*) FROM games";
            try(var preparedStatement = connection.prepareStatement(count)){
                try(var rs = preparedStatement.executeQuery()){
                    rs.next();
                    totalGames = rs.getInt(1);
                }
            }
            gameList = new GameData[totalGames];
            String sql = "SELECT gameData FROM games";
            try(var preparedStatement = connection.prepareStatement(sql)){
                try(var rs = preparedStatement.executeQuery()){
                    int i = 0;
                    while(rs.next()){
                        String foundUserData = rs.getString("gameData");
                        gameList[i] = gson.fromJson(foundUserData, GameData.class);
                        i++;
                    }
                }
            }
        }
        return gameList;
    }


    public String createGame(String gameName) throws DataAccessException, SQLException {
        int gameID = gameIDCounter;
        gameIDCounter++;
        ChessGame chess = new ChessGame();
        GameData gameModel = new GameData(null, null, gameName, gameID, chess, false);
        String gameDataString = gson.toJson(gameModel);
        try(Connection connection = DatabaseManager.getConnection()) {

            String sql = "INSERT INTO games (gameID, gameData) VALUES(?, ?)";
            try(var preparedStatement = connection.prepareStatement(sql)){


                preparedStatement.setString(1, String.valueOf(gameID));
                preparedStatement.setString(2, gameDataString);

                preparedStatement.executeUpdate();
            }
        }
        return String.valueOf(gameID);
    }

    public GameData getGameData(String gameID) throws DataAccessException, SQLException {
        GameData game = null;
        try(Connection connection = DatabaseManager.getConnection()) {

            String sql = "SELECT gameData FROM games WHERE gameID=?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, gameID);
                try(var rs = preparedStatement.executeQuery()){
                    while(rs.next()){
                        String foundGameData = rs.getString("gameData");
                        game = gson.fromJson(foundGameData, GameData.class);
                    }
                }
            }
        }
        return game;
    }

    public void updateGame(String gameID, String clientColor, String username, boolean moveMade, ChessGame board, boolean finish) throws DataAccessException, SQLException {
        GameData game = getGameData(gameID);
        if(game.finished()){
            throw new DataAccessException("This game has finished.");
        }


        if(finish){
            game = game.finish();
        } else if(moveMade) {
            game = game.moveMade(board);
        }else if (clientColor.equals("WHITE")){
            game =  game.gainUserWhite(username);
        } else {
            game = game.gainUserBlack(username);
        }

        try(Connection connection = DatabaseManager.getConnection()) {

            String sql = "UPDATE games SET gameData = ? WHERE gameID = ?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, gson.toJson(game));
                preparedStatement.setString(2, gameID);

                preparedStatement.executeUpdate();
            }
        }
    }

    public void clearGames() throws DataAccessException, SQLException {
        gameIDCounter = 1;
        try(Connection connection = DatabaseManager.getConnection()){

            String sql = "DELETE FROM games";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.executeUpdate();
            }
        }
    }

    public static synchronized MySQLGameDAO getInstance() throws DataAccessException, SQLException {
        if(instance == null){
            instance = new MySQLGameDAO();
        }
        return instance;
    }
}
