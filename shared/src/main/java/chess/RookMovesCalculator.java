package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RookMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

    public RookMovesCalculator(){
        ChessSet = new HashSet<>();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece rook = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        row++;
        while(row <= 8){
            if(board.getPiece(new ChessPosition(row, col)) == null){
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                row++;
            } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == rook.getTeamColor()){
                break;
            } else{
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                break;
            }
        }
        row = position.getRow();

        row--;
        while(row >= 1){
            if(board.getPiece(new ChessPosition(row, col)) == null){
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                row--;
            } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == rook.getTeamColor()){
                break;
            } else{
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                break;
            }
        }

        row = position.getRow();

        col++;
        while(col <= 8) {
            if (board.getPiece(new ChessPosition(row, col)) == null) {
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                col++;
            } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == rook.getTeamColor()) {
                break;
            } else {
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                break;
            }
        }

        col = position.getColumn();

        col--;
        while(col >= 1) {
            if (board.getPiece(new ChessPosition(row, col)) == null) {
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                col--;
            } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == rook.getTeamColor()) {
                break;
            } else {
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                break;
            }
        }

        return this.ChessSet;
    }
}

