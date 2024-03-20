package ui;

import chess.ChessGame;
import model.GameData;
import responses.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.DrawChessBoard.drawBoard;
import static ui.EscapeSequences.*;
import static ui.ServerFacade.*;

public class Client {
    private static boolean loggedIn;
    private static String username;
    private static String authToken;

    private static GameData[] gameList;

    static{
        loggedIn = false;
    }

    public static boolean drawMenu(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        boolean notQuitting = true;
        Scanner scanner = new Scanner(System.in);
        if(loggedIn){
            gameMenu(out, scanner, authToken);
        } else{
            notQuitting = loginMenu(out, scanner);
        }

        return notQuitting;
    }

    private static void gameMenu(PrintStream out, Scanner scanner, String authToken) {
        out.println("Logged In - " + username);
        out.println("1. Create Game");
        out.println("2. List Games");
        out.println("3. Join Game");
        out.println("4. Join Observer");
        out.println("5. Logout");
        out.println("6. Help");
        out.println();
        out.print("Menu >>> ");
        String line = scanner.nextLine();
        switch (line) {
            case "1" -> {
                out.print("Enter Game Name:   ");
                String gameName = scanner.nextLine();
                out.println("Creating Game...");
                CreateGameResponse createGameResponse = createGame(gameName, authToken);
                if(createGameResponse.getGameID() != null) {
                    out.println("Your game ID is " + createGameResponse.getGameID());
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
                        out.print("Game number " + i + ".  ");
                        out.println("Game ID: " + data.gameID());
                        out.println("Game Name: " + data.gameName());
                        out.println("White player username: " + data.whiteUsername());
                        out.println("Black player username: " + data.blackUsername());
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
                    GameData game = gameList[Integer.parseInt(gameNum)];
                    String gameID = String.valueOf(game.gameID());
                    out.println("Enter WHITE to join as white player or BLACK to join as black player:");
                    String playerColor = scanner.nextLine();
                    String message = joinGame(playerColor, gameID, authToken);
                    if (message.equals("Success")) {
                        out.println("Joining Game...");
//                drawBoard(game.game(), ChessGame.TeamColor.valueOf(playerColor));
                        drawBoard(game.game(), ChessGame.TeamColor.WHITE);
                        drawBoard(game.game(), ChessGame.TeamColor.BLACK);
                    } else {
                        out.println(message);
                    }
                } else{
                    out.println("Error: Game number " + gameNum + " undefined");
                }
            }
            case "4" -> {
                gameList = listGames(authToken).getGames();
                out.print("Enter the game number of the game you would like to join: ");
                String gameNum = scanner.nextLine();
                GameData game = gameList[Integer.parseInt(gameNum)];
                String gameID = String.valueOf(game.gameID());
                out.println("Joining as observer...");
                String message = joinGame(null, gameID, authToken);
                if(message.equals("Success")) {
                    drawBoard(game.game(), ChessGame.TeamColor.WHITE);
                    drawBoard(game.game(), ChessGame.TeamColor.BLACK);
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

    private static boolean loginMenu(PrintStream out, Scanner scanner) {
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


}
