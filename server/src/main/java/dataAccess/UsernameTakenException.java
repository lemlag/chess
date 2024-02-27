package dataAccess;

public class UsernameTakenException extends DataAccessException{
    public UsernameTakenException() {
        super("Error: already taken");
    }
}
