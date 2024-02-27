package responses;

public class LoginResponse extends ErrorResponse{
    private final String username;
    private final String authToken;

    public LoginResponse(String username, String authToken) {
        super(null);
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
