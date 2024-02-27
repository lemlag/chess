package responses;

import model.GameData;

public record ListGamesResponse(GameData[] games){
}
