package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private static MemoryGameDAO instance;
    private final Map<String, GameData> gameMap;
    private static int gameIDCounter;

    public MemoryGameDAO(){
        gameMap = new HashMap<>();
        gameIDCounter = 0;
    }
    public GameData[] listGames(){
        return gameMap.values().toArray(new GameData[0]);
    }

    public String createGame(String gameName){
        int gameID = gameIDCounter;
        gameIDCounter++;
        ChessGame chess = new ChessGame();
        GameData gameModel = new GameData("", "", gameName, gameID, chess);
        gameMap.put(String.valueOf(gameID), gameModel);
        return String.valueOf(gameID);
    }

    public GameData getGameData(String gameID){
        return gameMap.get(gameID);
    }

    public void updateGame(String gameID, String clientColor, String username){
        GameData gameModel = gameMap.get(gameID);
        if (clientColor.equals("WHITE")){
            gameMap.replace(gameID, gameModel.gainUserWhite(username));
        } else{
            gameMap.replace(gameID, gameModel.gainUserBlack(username));
        }
    }

    public void clearGames(){
        gameMap.clear();
    }

    public static MemoryGameDAO getInstance() {
        if(instance == null){
            instance = new MemoryGameDAO();
        }
        return instance;
    }
}
