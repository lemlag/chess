package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand{



    Integer gameID;
    public LeaveCommand(String authToken) {
        super(authToken);
        commandType = CommandType.LEAVE;
    }

    public Integer getGameID() {
        return gameID;
    }
}
