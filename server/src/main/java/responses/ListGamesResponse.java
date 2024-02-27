package responses;

import model.GameData;

public class ListGamesResponse extends ErrorResponse {

    private final GameData[] games;
    public ListGamesResponse(GameData[] games) {
        super(null);
        this.games = games;
    }

    public GameData[] getGames() {
        return games;
    }
}
