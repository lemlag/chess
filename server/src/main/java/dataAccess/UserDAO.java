package dataAccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {

    UserData getUser(String username) throws DataAccessException, SQLException;

    void createUser(String username, String password, String email) throws DataAccessException, SQLException;

    void clearUsers() throws DataAccessException, SQLException;
}
