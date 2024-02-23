package dataAccess;

public interface AuthDAO {

    String createAuth(String username);

    void deleteAuth(String authToken);

    boolean checkAuth(String authToken);

    void clearAuth();
}
