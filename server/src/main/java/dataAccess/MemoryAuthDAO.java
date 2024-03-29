package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    private final Map<String, AuthData> authMap;
    private static MemoryAuthDAO instance;

    public MemoryAuthDAO(){
        authMap = new HashMap<>();
    }

    public String createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authSet = new AuthData(authToken, username);
        authMap.put(authToken, authSet);
        return authToken;
    }

    public String authDAOGetUsername(String authToken){
        AuthData authModel = authMap.get(authToken);
        if(authModel != null) {
            return authModel.username();
        } else{
            return null;
        }
    }

    public void deleteAuth(String authToken){
        checkAuth(authToken);
        authMap.remove(authToken);

    }

    public boolean checkAuth(String authToken){
        return MemoryAuthDAO.getInstance().authMap.containsKey(authToken);
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
