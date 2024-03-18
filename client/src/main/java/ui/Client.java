package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private static boolean loggedIn;

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
            gameMenu(out, scanner);
        } else{
            notQuitting = loginMenu(out, scanner);
        }

        return notQuitting;
    }

    private static void gameMenu(PrintStream out, Scanner scanner) {
        boolean notQuitting = true;
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
            case "1" -> out.println("Creating Game...");
            case "2" -> out.println("Listing Games...");
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
                out.println("Logging in...");
                loggedIn = true;
            }
            case "2" -> out.println("Registering...");
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
