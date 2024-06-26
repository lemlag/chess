package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int EDGE_SIZE = 2;
    private static final int BOARD_INDEX = BOARD_SIZE_IN_SQUARES - 1;

    private static SquareColor currSquare;


    public enum SquareColor {
        LIGHT,
        DARK
    }

    public static void drawBoard(ChessGame game, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        currSquare = SquareColor.DARK;
        out.print(ERASE_SCREEN);
        drawHeader(player, out);
        drawSquares(game.getBoard(), player, out);
        drawHeader(player, out);
        out.println();
    }

    public static void drawBoardHighlights(ChessGame game, Collection<ChessMove> chessMoves, ChessPosition startingPosition, ChessGame.TeamColor player){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        currSquare = SquareColor.DARK;
        out.print(ERASE_SCREEN);
        drawHeader(player, out);
        Collection<ChessPosition> chessPositions = new HashSet<>();
        for (ChessMove move : chessMoves){
            chessPositions.add(move.getEndPosition());
        }
        drawSquareHighlights(game.getBoard(), player, out, chessPositions, startingPosition);
        drawHeader(player, out);
        out.println();
    }

    private static void drawSquareHighlights(ChessBoard board, ChessGame.TeamColor player, PrintStream out, Collection<ChessPosition> chessPositions, ChessPosition startingPosition) {
        if(player == ChessGame.TeamColor.WHITE) {
            for (int row = BOARD_SIZE_IN_SQUARES; row > 0; row--) {
                setHeaderColors(out);
                out.print(" " + row + " ");
                setNextSquare(out);
                for (int col = 1; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    makeHighlightedSquares(chessPositions, row, col, out, board, startingPosition);
                }
                setHeaderColors(out);
                out.print(" " + row + " ");
                out.print(SET_BG_COLOR_BLACK);
                out.println();
            }
        } else{
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                setHeaderColors(out);
                out.print(" " + row + " ");
                setNextSquare(out);
                for (int col = BOARD_SIZE_IN_SQUARES; col > 0; col--) {
                    makeHighlightedSquares(chessPositions, row, col, out, board, startingPosition);
                }
                setHeaderColors(out);
                out.print(" " + row + " ");
                out.print(SET_BG_COLOR_BLACK);
                out.println();
            }
        }
    }

    private static void makeHighlightedSquares(Collection<ChessPosition> chessPositions, int row, int col, PrintStream out, ChessBoard board, ChessPosition startingPosition){
        if(chessPositions.contains(new ChessPosition(row, col))){
            setNextBlueSquare(out);
        } else if(startingPosition.equals(new ChessPosition(row, col))){
            setNextYellowSquare(out);
        }
        printNextPiece(out, board.getPiece(new ChessPosition(row, col)));
        setNextSquare(out);
    }

    private static void setNextYellowSquare(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
    }

    private static void setNextBlueSquare(PrintStream out) {
        if(currSquare == SquareColor.LIGHT) {
            out.print(SET_BG_COLOR_BLUE);
        } else{
            out.print(SET_BG_COLOR_PURPLE);
        }
    }

    private static void drawSquares(ChessBoard board, ChessGame.TeamColor player, PrintStream out) {
        if(player == ChessGame.TeamColor.WHITE) {
            for (int row = BOARD_SIZE_IN_SQUARES; row > 0; row--) {
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
        } else{
            for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
                setHeaderColors(out);
                out.print(" " + row + " ");
                setNextSquare(out);
                for (int col = BOARD_SIZE_IN_SQUARES; col > 0; col--) {
                    printNextPiece(out, board.getPiece(new ChessPosition(row, col)));
                    setNextSquare(out);
                }
                setHeaderColors(out);
                out.print(" " + row + " ");
                out.print(SET_BG_COLOR_BLACK);
                out.println();
            }
        }
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
        if(currSquare == SquareColor.LIGHT) {
            out.print(SET_BG_COLOR_DARK_GREEN);
            currSquare = SquareColor.DARK;
        } else{
            out.print(SET_BG_COLOR_GREEN);
            currSquare = SquareColor.LIGHT;
        }
    }


}
