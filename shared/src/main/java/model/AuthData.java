package model;

public class AuthData {
    private final String authToken;
    private final String username;

    public AuthData(String usernameIn, String authTkn){
        this.username = usernameIn;
        this.authToken = authTkn;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken(){
        return authToken;
    }
}
