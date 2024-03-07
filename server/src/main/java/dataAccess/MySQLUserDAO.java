package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.*;

public class MySQLUserDAO implements UserDAO{

    private final Gson gson;

    private static MySQLUserDAO instance;

    public MySQLUserDAO() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        gson = new Gson();
        try(Connection connection = DatabaseManager.getConnection()) {

            var createUserTable = """
                    CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(80) NOT NULL,
                    userData VARCHAR(800) NOT NULL
                    )""";

            try(var createTableStatement = connection.prepareStatement(createUserTable)){
                createTableStatement.executeUpdate();
            }
        }
    }

    public UserData getUser(String username) throws DataAccessException, SQLException {
        UserData user = null;
        try(Connection connection = DatabaseManager.getConnection()) {

            String sql = "SELECT userData FROM users WHERE username=?";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, username);
                try(var rs = preparedStatement.executeQuery()){
                    while(rs.next()){
                        String foundUserData = rs.getString("userData");
                        user = gson.fromJson(foundUserData, UserData.class);
                    }
                }
            }
        }
        return user;
    }

    public void createUser(String username, String password, String email) throws DataAccessException, SQLException {
        UserData user = new UserData(username, password, email);
        String userString = gson.toJson(user);
        try(Connection connection = DatabaseManager.getConnection()) {

            String sql = "INSERT INTO users (username, userData) VALUES(?, ?)";
            try(var preparedStatement = connection.prepareStatement(sql)){


                preparedStatement.setString(1, username);
                preparedStatement.setString(2, userString);

                preparedStatement.executeUpdate();
            }
        }
    }

    public void clearUsers() throws DataAccessException, SQLException {
        try(Connection connection = DatabaseManager.getConnection()){

            String sql = "TRUNCATE TABLE IF EXISTS users";
            try(var preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.executeUpdate();
            }
        }
    }

    public static synchronized MySQLUserDAO getInstance() throws DataAccessException, SQLException {
        if(instance == null){
            instance = new MySQLUserDAO();
        }
        return instance;
    }
}
