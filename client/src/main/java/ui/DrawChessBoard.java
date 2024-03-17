package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 10;

    private static final int BOARD_INDEX = BOARD_SIZE_IN_SQUARES - 1;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public void drawBoard(String [] chessBoard, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawHeader(player, out);

    }

    private void drawHeader(ChessGame.TeamColor player, PrintStream out) {
        setHeaderColors(out);

        String[] headers = { "   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   " };
        for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++){
            if(player == ChessGame.TeamColor.WHITE) {
                out.print(headers[col]);
            } else{
                out.print(headers[BOARD_INDEX - col]);
            }
        }

        out.println();
    }

    private void drawLabel(PrintStream out, String header) {
    }

    private void setHeaderColors(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private void setBlackSquare(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
    }

    private void setWhiteSquare(PrintStream out){
        out.print(SET_BG_COLOR_WHITE);
    }

}
