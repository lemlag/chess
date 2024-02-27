package responses;

public class CreateGameResponse extends ErrorResponse {
    private final String gameID;

    public CreateGameResponse(String gameID) {
        super(null);
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }
}
