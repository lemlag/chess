package ui;

import responses.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.ServerFacade.*;

public class Client {
    private static boolean loggedIn;
    private static String username;
    private static String authToken;
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
        out.println("Logged In");
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
                out.println("Your game ID is " + createGameResponse.getGameID());
            }
            case "2" -> {
                ListGamesResponse listGamesResponse = listGames(authToken);
                out.println("Listing Games...");
            }
            case "3" -> {
                out.println("Joining Game...");
            }
            case "4" -> out.println("Joining as observer...");
            case "5" -> {
                out.println("Logging out...");
                loggedIn = false;
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
                authToken = loginResponse.getAuthToken();
                loggedIn = true;
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
                authToken = loginResponse.getAuthToken();
                loggedIn = true;
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
