package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> userMap;
    private static MemoryUserDAO instance;

    public MemoryUserDAO(){
        userMap = new HashMap<>();
    }

    public UserData getUser(String username){
        return userMap.get(username);
    }

    public void createUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        userMap.put(username, user);
    }

    public void clearUsers(){
        userMap.clear();
    }

    public static synchronized MemoryUserDAO getInstance(){
        if(instance == null){
            instance = new MemoryUserDAO();
        }
        return instance;
    }
}
