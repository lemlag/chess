package dataAccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO{

    private static MySQLAuthDAO instance;

    private final Gson gson;

    public MySQLAuthDAO() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gson = new Gson();

        try(Connection connection = DatabaseManager.getConnection()) {

            var createUserTable = """
                    CREATE TABLE IF NOT EXISTS authorizations (
                    authToken VARCHAR(80) NOT NULL,
                    authData VARCHAR(800) NOT NULL
                    )""";

            try(var createTableStatement = connection.prepareStatement(createUserTable)){
                createTableStatement.executeUpdate();
            }
        }
    }

    public String createAuth(String username) throws DataAccessException, SQLException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        String authString = gson.toJson(authData);
        try(Connection connection = DatabaseManager.getConnection()){
            String sql = "INSERT INTO authorizations (authToken, authData) VALUES(?, ?)";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, authString);

                preparedStatement.executeUpdate();
            }
        }
        return authToken;
    }

    public String authDAOGetUsername(String authToken) throws SQLException, DataAccessException {
        AuthData authData = null;
        String username = null;
        try(Connection connection = DatabaseManager.getConnection()){
            String sql = "SELECT authData FROM authorizations WHERE authToken=?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, authToken);
                try(var rs = preparedStatement.executeQuery()){
                    while(rs.next()){
                        String foundAuthData = rs.getString("authData");
                        authData = gson.fromJson(foundAuthData, AuthData.class);
                        username = authData.username();
                    }
                }
            }
        }
        return username;
    }

    public void deleteAuth(String authToken) throws DataAccessException, SQLException {
        try(Connection connection = DatabaseManager.getConnection()){
            String sql = "DELETE FROM authorizations WHERE authToken=?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
    }

    public boolean checkAuth(String authToken) throws DataAccessException, SQLException {
        boolean existsAuth = false;
        try(Connection connection = DatabaseManager.getConnection()){
            String sql = "SELECT authToken FROM authorizations WHERE authToken=?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, authToken);
                try(var rs = preparedStatement.executeQuery()){
                    String foundAuthToken = null;
                    while(rs.next()){
                        foundAuthToken = rs.getString("authToken");
                    }
                    if(authToken.equals(foundAuthToken)){
                        existsAuth = true;
                    }
                }
            }
        }
        return existsAuth;
    }

    public void clearAuth() throws SQLException, DataAccessException {
        try(Connection connection = DatabaseManager.getConnection()){
            String sql = "TRUNCATE TABLE IF EXISTS authorizations";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.executeUpdate();
            }
        }
    }

    public static synchronized MySQLAuthDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            instance = new MySQLAuthDAO();
        }
        return instance;
    }

}
