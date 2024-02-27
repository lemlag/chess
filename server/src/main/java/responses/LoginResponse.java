package responses;

public class LoginResponse extends ErrorResponse{
    private final String username;
    private final String authToken;

    public LoginResponse(String username, String authToken, String message) {
        super(message);
        this.username = username;
        this.authToken = authToken;
    }
}
