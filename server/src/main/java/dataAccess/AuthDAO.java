package dataAccess;

public interface AuthDAO {

    public String createAuth(String username);

    public void deleteAuth(String authToken);

    public String getAuth(String authToken);

    public void clearAuth();
}
