package responses;

import model.GameData;

public class ListGamesResponse extends ErrorResponse {

    private final GameData[] games;
    public ListGamesResponse(GameData[] games, String message) {
        super(message);
        this.games = games;
    }

    public GameData[] getGames() {
        return games;
    }
}
