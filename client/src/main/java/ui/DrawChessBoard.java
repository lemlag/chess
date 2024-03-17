package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public void drawBoard(String [] chessBoard, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        drawHeader(player, out);

    }

    private void drawHeader(ChessGame.TeamColor player, PrintStream out) {
        setHeaderColors(out);
    }

    private void setHeaderColors(PrintStream out) {
        out.print()
    }
}
