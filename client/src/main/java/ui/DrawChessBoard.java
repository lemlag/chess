package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int EDGE_SIZE = 2;
    private static final int BOARD_INDEX = BOARD_SIZE_IN_SQUARES - 1;


    private static squareColor currSquare;


    public enum squareColor {
        LIGHT,
        DARK
    }

    public static void drawBoard(ChessGame game, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        currSquare = squareColor.DARK;
        out.print(ERASE_SCREEN);
        drawHeader(player, out);
        drawSquares(game.getBoard(), player, out);
        drawHeader(player, out);
        out.println();
    }

    private static void drawSquares(ChessBoard board, ChessGame.TeamColor player, PrintStream out) {
        if(player == ChessGame.TeamColor.WHITE) {
            for (int row = BOARD_SIZE_IN_SQUARES; row > 0; row--) {
                drawPieces(board, out, row);
            }
        } else{
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                drawPieces(board, out, row);
            }
        }
    }

    private static void drawPieces(ChessBoard board, PrintStream out, int row) {
        setHeaderColors(out);
        out.print(" " + row + " ");
        setNextSquare(out);
        for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
            printNextPiece(out, board.getPiece(new ChessPosition(row, col)));
            setNextSquare(out);
        }
        setHeaderColors(out);
        out.print(" " + row + " ");
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }

    private static void printNextPiece(PrintStream out, ChessPiece piece) {
        if(piece == null){
            out.print(EMPTY);
            return;
        }

        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
        } else{
            out.print(SET_TEXT_COLOR_BLACK);
        }

        ChessPiece.PieceType type = piece.getPieceType();

        if(type == ChessPiece.PieceType.KING){
            out.print(KING);
        } else if(type == ChessPiece.PieceType.QUEEN){
            out.print(QUEEN);
        } else if(type == ChessPiece.PieceType.BISHOP){
            out.print(BISHOP);
        } else if(type == ChessPiece.PieceType.KNIGHT){
            out.print(KNIGHT);
        } else if(type == ChessPiece.PieceType.ROOK){
            out.print(ROOK);
        } else if(type == ChessPiece.PieceType.PAWN){
            out.print(PAWN);
        }
    }

    private static void drawHeader(ChessGame.TeamColor player, PrintStream out) {
        setHeaderColors(out);

        String[] headers = { "   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   " };
        if(player == ChessGame.TeamColor.WHITE) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES + EDGE_SIZE; col++){
                out.print(headers[col]);
            }
        } else{
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES + EDGE_SIZE; col++){
                out.print(headers[BOARD_INDEX + EDGE_SIZE - col]);
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }


    private static void setHeaderColors(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void setNextSquare(PrintStream out){
        if(currSquare == squareColor.LIGHT) {
            out.print(SET_BG_COLOR_DARK_GREEN);
            currSquare = squareColor.DARK;
        } else{
            out.print(SET_BG_COLOR_GREEN);
            currSquare = squareColor.LIGHT;
        }
    }


}
