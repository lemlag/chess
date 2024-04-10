package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import responses.*;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

import static ui.DrawChessBoard.drawBoard;
import static ui.DrawChessBoard.drawBoardHighlights;
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
        GameData[] gameList;
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
                clientJoinGame(gameList);
            }
            case "4" -> {
                gameList = listGames(authToken).getGames();
                out.print("Enter the game number of the game you would like to join: ");
                String gameNum = scanner.nextLine();
                game = gameList[Integer.parseInt(gameNum)];
                String gameID = String.valueOf(game.gameID());
                out.println("Joining as observer...");
                String message = joinGame(null, gameID, authToken, this);
                try {
                    ServerFacade.joinObserver(authToken, Integer.valueOf(gameID));
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
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

    private void clientJoinGame(GameData[] gameList){
        out.print("Enter the game number of the game you would like to join: ");
        String gameNum = scanner.nextLine();
        if(Integer.parseInt(gameNum) < gameList.length) {
            game = gameList[Integer.parseInt(gameNum)];
            String gameID = String.valueOf(game.gameID());
            out.println("Enter WHITE to join as white player or BLACK to join as black player:");
            playerColor = scanner.nextLine();
            String message = joinGame(playerColor, gameID, authToken, this);
            try{
                ServerFacade.joinPlayer(authToken, Integer.valueOf(gameID), ChessGame.TeamColor.valueOf(playerColor));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (message.equals("Success")) {
                out.println("Joining Game...");
                gameJoined = true;
                out.println(ERASE_SCREEN);
                drawJoinClient();
            } else {
                out.println(message);
            }
        } else{
            out.println("Error: Game number " + gameNum + " undefined");
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
            case "20" -> {
                ServerFacade.clear();
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
                drawJoinClient();
            }
            case "2" ->{
                out.println("Where is the piece you would like to check?");
                out.print("Column >>> ");
                String col = scanner.nextLine();
                out.println();
                out.print("Row >>> ");
                int row = Integer.parseInt(scanner.nextLine());
                out.println();
                ChessPosition startPosition = getChessPosition(col, row);
                Collection<ChessMove> chessMoves = this.game.game().validMoves(startPosition);
                drawBoardHighlights(this.game.game(), chessMoves, startPosition, ChessGame.TeamColor.valueOf(this.playerColor));
            }
            case "3" ->{
                out.println("Where is the piece you would like to move?");
                out.print("Column >>> ");
                String col = scanner.nextLine();
                out.println();
                out.print("Row >>> ");
                int row = Integer.parseInt(scanner.nextLine());
                out.println();
                ChessPosition startPosition = getChessPosition(col, row);
                out.println("Where would you like to move this piece?");
                out.print("Column >>> ");
                col = scanner.nextLine();
                out.println();
                out.print("Row >>> ");
                row = Integer.parseInt(scanner.nextLine());
                ChessPosition endPosition = getChessPosition(col, row);
                ChessPiece.PieceType promotionType = null;
                if(row == 1 || row == 8) {
                    ChessPiece piece = this.game.game().getBoard().getPiece(startPosition);

                    if (piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
                        out.println("What would you like your pawn to promote into?");
                        out.println("1. Queen");
                        out.println("2. Rook");
                        out.println("3. Knight");
                        out.println("4. Bishop");
                        out.print("Piece >>> ");
                        String promotion = scanner.nextLine();
                        switch (promotion) {
                            case "1" -> promotionType = ChessPiece.PieceType.QUEEN;
                            case "2" -> promotionType = ChessPiece.PieceType.ROOK;
                            case "3" -> promotionType = ChessPiece.PieceType.KNIGHT;
                            case "4" -> promotionType = ChessPiece.PieceType.BISHOP;
                        }
                    }
                }
                ChessMove move = new ChessMove(startPosition, endPosition, promotionType);
                try {
                    System.out.println("trying");
                        ServerFacade.makeMoves(authToken, game.gameID(), move);
                } catch(Exception e){
                            out.println(e.getMessage());
                }


            }
            case "4" ->{
                out.println("Leaving now...");
                try {
                    ServerFacade.leave(authToken, game.gameID());
                } catch (Exception e){
                    out.println(e.getMessage());
                }
                gameJoined = false;
            }
            case "5"-> {
                out.println("Resigning now...");
                try{
                    ServerFacade.resign(authToken, game.gameID());
                } catch(Exception e){
                    out.println(e.getMessage());
                }
            }
            default -> {
                out.println("Help Menu:");
                out.println("Type the number associated with the action you would like to take. Then press enter.");
                drawJoinClient();
            }
        }
    }

    private ChessPosition getChessPosition(String col, int row) {
        int column;
        switch(col){
            case "a" -> column = 1;
            case "b" -> column = 2;
            case "c" -> column = 3;
            case "d" -> column = 4;
            case "e" -> column = 5;
            case "f" -> column = 6;
            case "g" -> column = 7;
            case "h" -> column = 8;
            default -> column = 0;
        }
        return new ChessPosition(row, column);
    }

    public void drawJoinClient(){
        drawBoard(game.game(), ChessGame.TeamColor.valueOf(playerColor));
        out.println("Game - " + game.gameID() + " - User - "+ username + " - Color - " + playerColor);
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
        out.println("Notified");
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }

    }

}
