package dataAccess;

public interface UserDAO {

    String getUser(String username);

    void createUser(String username, String password, String email);

    void clearUsers();
}
