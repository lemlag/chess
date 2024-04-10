package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{

    Integer gameID;
    public JoinObserverCommand(String authToken, int obs_gameID) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
        gameID = obs_gameID;
    }

    public Integer getGameID(){
        return gameID;
    }
}
