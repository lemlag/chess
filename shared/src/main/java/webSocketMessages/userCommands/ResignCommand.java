package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{

    Integer gameID;
    public ResignCommand(String authToken) {
        super(authToken);
        commandType = CommandType.RESIGN;
    }

    public Integer getGameID() {
        return gameID;
    }
}
