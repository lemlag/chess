package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    private Map<String, String> authMap = new HashMap<>();
    private static MemoryAuthDAO instance;


    public String createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, username);
        return authToken;
    }

    public void deleteAuth(String authToken){
        checkAuth(authToken);
        authMap.remove(authToken);
    }
// Make it throw a DataAccessException
    public boolean checkAuth(String authToken){
        return authMap.containsKey(authToken);
    }

    public void clearAuth(){
        authMap.clear();
    }

    public static synchronized MemoryAuthDAO getInstance(){
        if(instance == null){
            instance = new MemoryAuthDAO();
        }
        return instance;
    }
}
