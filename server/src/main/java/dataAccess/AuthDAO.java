package dataAccess;

public interface AuthDAO {

    String createAuth(String username);

    String getUser(String authToken);

    void deleteAuth(String authToken);

    boolean checkAuth(String authToken);

    void clearAuth();
}
