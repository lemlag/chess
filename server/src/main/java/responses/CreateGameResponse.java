package responses;

public class CreateGameResponse extends ErrorResponse {
    private final String gameID;

    public CreateGameResponse(String gameID, String message) {
        super(message);
        this.gameID = gameID;
    }
}
