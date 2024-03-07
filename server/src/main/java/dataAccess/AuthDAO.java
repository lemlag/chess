package dataAccess;

import java.sql.SQLException;

public interface AuthDAO {

    String createAuth(String username) throws DataAccessException, SQLException;

    String authDAOGetUsername(String authToken) throws SQLException, DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException, SQLException;

    boolean checkAuth(String authToken) throws DataAccessException, SQLException;

    void clearAuth() throws SQLException, DataAccessException;
}
