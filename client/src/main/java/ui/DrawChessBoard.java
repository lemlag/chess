package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static final int BOARD_INDEX = BOARD_SIZE_IN_SQUARES - 1;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    private static squareColor currSquare;


    public enum squareColor {
        LIGHT,
        DARK
    }

    public static void drawBoard(ChessGame game, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        currSquare = squareColor.LIGHT;
        out.print(ERASE_SCREEN);
        drawHeader(player, out);
        drawSquares(game.getBoard(), player);
    }

    private static void drawSquares(ChessBoard board, ChessGame.TeamColor player) {
        int rowNums;
        if(player == ChessGame.TeamColor.WHITE) {
            rowNums = 8;
            for (int row = 0; row < BOARD_SIZE_IN_SQUARES - 2; row++) {

                for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {

                }
            }
        }
    }

    private static void drawHeader(ChessGame.TeamColor player, PrintStream out) {
        setHeaderColors(out);

        String[] headers = { "   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   " };
        if(player == ChessGame.TeamColor.WHITE) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++){
                out.print(headers[col]);
            }
        } else{
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++){
                out.print(headers[BOARD_INDEX - col]);
            }
        }

        out.println();
    }


    private static void setHeaderColors(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private void setNextSquare(PrintStream out){
        if(currSquare == squareColor.LIGHT) {
            out.print(SET_BG_COLOR_DARK_GREEN);
            currSquare = squareColor.DARK;
        } else{
            out.print(SET_BG_COLOR_GREEN);
            currSquare = squareColor.LIGHT;
        }
    }


}
