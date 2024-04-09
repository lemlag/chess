package ui;

import chess.ChessGame;
import model.GameData;
import responses.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.DrawChessBoard.drawBoard;
import static ui.EscapeSequences.*;
import static ui.ServerFacade.*;

public class Client implements ServerMessageObserver{
    private boolean loggedIn;
    private String username;
    private String authToken;
    private volatile boolean gameJoined;
    private String playerColor;
    private GameData game;
    private final PrintStream out;
    private final Scanner scanner;

    public Client(){
        loggedIn = false;
        gameJoined = false;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        scanner = new Scanner(System.in);
    }

    public void drawMenu() {
        boolean notQuitting = true;
        while (notQuitting) {

            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_GREEN);
            if(gameJoined) {
                joinedMenu();
            } else if (loggedIn) {
                gameMenu();
            } else {
                notQuitting = loginMenu();
            }

        }
    }

    private void gameMenu() {
        out.println(STR."Logged In - \{username}");
        out.println("1. Create Game");
        out.println("2. List Games");
        out.println("3. Join Game");
        out.println("4. Join Observer");
        out.println("5. Logout");
        out.println("6. Help");
        out.println();
        out.print("Menu >>> ");
        String line = scanner.nextLine();
        GameData[] gameList;
        switch (line) {
            case "1" -> {
                out.print("Enter Game Name:   ");
                String gameName = scanner.nextLine();
                out.println("Creating Game...");
                CreateGameResponse createGameResponse = createGame(gameName, authToken);
                if(createGameResponse.getGameID() != null) {
                    out.println(STR."Your game ID is \{createGameResponse.getGameID()}");
                } else{
                    out.println(createGameResponse.getMessage());
                }
            }
            case "2" -> {
                ListGamesResponse listGamesResponse = listGames(authToken);
                if(listGamesResponse.getGames() != null) {
                    out.println("Listing Games...");
                    gameList = listGamesResponse.getGames();
                    if(gameList.length == 0){
                        out.println("There are no games to list");
                    }
                    for (int i = 0; i < gameList.length; i++) {
                        GameData data = gameList[i];
                        out.print(STR."Game number \{i}.  ");
                        out.println(STR."Game ID: \{data.gameID()}");
                        out.println(STR."Game Name: \{data.gameName()}");
                        out.println(STR."White player username: \{data.whiteUsername()}");
                        out.println(STR."Black player username: \{data.blackUsername()}");
                        out.println();
                    }
                } else{
                    out.println(listGamesResponse.getMessage());
                }
            }
            case "3" -> {
                gameList = listGames(authToken).getGames();
                out.print("Enter the game number of the game you would like to join: ");
                String gameNum = scanner.nextLine();
                if(Integer.parseInt(gameNum) < gameList.length) {
                    game = gameList[Integer.parseInt(gameNum)];
                    String gameID = String.valueOf(game.gameID());
                    out.println("Enter WHITE to join as white player or BLACK to join as black player:");
                    playerColor = scanner.nextLine();
                    String message = joinGame(playerColor, gameID, authToken, this);
                    if (message.equals("Success")) {
                        out.println("Joining Game...");
                        gameJoined = true;
                        out.println(ERASE_SCREEN);
                        drawJoinClient();
                    } else {
                        out.println(message);
                    }
                } else{
                    out.println(STR."Error: Game number \{gameNum} undefined");
                }
            }
            case "4" -> {
                gameList = listGames(authToken).getGames();
                out.print("Enter the game number of the game you would like to join: ");
                String gameNum = scanner.nextLine();
                game = gameList[Integer.parseInt(gameNum)];
                String gameID = String.valueOf(game.gameID());
                out.println("Joining as observer...");
                String message = joinGame(null, gameID, authToken, this);
                if(message.equals("Success")) {
                    out.print("Enter the viewpoint you would like to have (WHITE/BLACK): ");
                    playerColor = scanner.nextLine();
                    gameJoined = true;
                    out.println(ERASE_SCREEN);
                    drawJoinClient();
                } else{
                    out.println(message);
                }
            }
            case "5" -> {
                out.println("Logging out...");
                String message = logOut(authToken);
                if(message.equals("Success")) {
                    loggedIn = false;
                } else{
                    out.println(message);
                }
            }
            default -> {
                out.println("Help Menu:");
                out.println("Type the number associated with the action you would like to take. Then press enter.");
            }
        }
    }

    private boolean loginMenu() {
        boolean notQuitting = true;
        out.println("Not Logged In");
        out.println("1. Login");
        out.println("2. Register");
        out.println("3. Quit");
        out.println("4. Help");
        out.println();
        out.print("Menu >>> ");
        String line = scanner.nextLine();
        switch (line) {
            case "1" -> {
                out.print("Username:  ");
                username = scanner.nextLine();
                out.print("Password:  ");
                String password = scanner.nextLine();
                out.println("Logging in...");
                LoginResponse loginResponse = logIn(username, password);
                if(loginResponse.getAuthToken() != null) {
                    authToken = loginResponse.getAuthToken();
                    loggedIn = true;
                } else{
                    out.println(loginResponse.getMessage());
                }
            }
            case "2" -> {
                out.print("E-mail address:   ");
                String email = scanner.nextLine();
                out.print("Username:  ");
                username = scanner.nextLine();
                out.print("Password:  ");
                String password = scanner.nextLine();
                out.println("Logging in...");
                LoginResponse loginResponse = register(username, password, email);
                if(loginResponse.getAuthToken() != null){
                authToken = loginResponse.getAuthToken();
                loggedIn = true;
                } else{
                    out.println(loginResponse.getMessage());
                }
            }
            case "3" -> {
                out.println("Quitting...");
                notQuitting = false;
            }
            default -> {
                out.println("Help Menu:");
                out.println("Type the number associated with the action you would like to take. Then press enter.");
            }
        }
        return notQuitting;
    }

    private void joinedMenu(){
        String line = scanner.nextLine();
        out.println(ERASE_SCREEN);
        switch (line) {
            case "1" ->{
                //get the game again
            }
            case "2" ->{

            }
            case "3" ->{

            }
            case "4" ->{

            }
            case "5"-> {

            }
            default -> {
                out.println("Help Menu:");
                out.println("Type the number associated with the action you would like to take. Then press enter.");
                drawJoinClient();
            }
        }
    }

    public void drawJoinClient(){
        drawBoard(game.game(), ChessGame.TeamColor.valueOf(playerColor));
        out.println(STR."Game - \{game.gameID()} - User - \{username} - Color - \{playerColor}");
        out.println("1. Redraw Chess Board");
        out.println("2. Highlight Legal Moves");
        out.println("3. Make Move");
        out.println("4. Leave");
        out.println("5. Resign");
        out.println("6. Help");
        out.println();
        out.print("ChessGame >>> ");
    }

    private void displayNotification(String message){
        out.println(ERASE_SCREEN);
        drawBoard(game.game(), ChessGame.TeamColor.valueOf(playerColor));
        out.println();
        out.println(message);
    }

    private void displayError(String error){
        out.println(ERASE_SCREEN);
        drawBoard(game.game(), ChessGame.TeamColor.valueOf(playerColor));
        out.println();
        out.println(error);
    }

    private void loadGame(GameData game){
        out.println(ERASE_SCREEN);
        this.game = game;
        drawJoinClient();
    }

    @Override
    public void notify(ServerMessage message){
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }

    }

}
