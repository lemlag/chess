package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BishopMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

        public BishopMovesCalculator(){
            ChessSet = new HashSet<>();
        }

        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
            ChessPiece bishop = board.getPiece(position);
            int row = position.getRow();
            int col = position.getColumn();
            row++;
            col++;
            while(row <= 8 && col <= 8){
                if(board.getPiece(new ChessPosition(row, col)) == null){
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    row++;
                    col++;
                } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == bishop.getTeamColor()){
                    break;
                } else{
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
            }
            row = position.getRow();
            col = position.getColumn();

            row++;
            col--;
            while(row <= 8 && col >= 1){
                if(board.getPiece(new ChessPosition(row, col)) == null){
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    row++;
                    col--;
                } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == bishop.getTeamColor()){
                    break;
                } else{
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
            }
            row = position.getRow();
            col = position.getColumn();

            row--;
            col++;
            while(row >= 1 && col <= 8) {
                if (board.getPiece(new ChessPosition(row, col)) == null) {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    row--;
                    col++;
                } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == bishop.getTeamColor()) {
                    break;
                } else {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
            }


            row = position.getRow();
            col = position.getColumn();

            row--;
            col--;
            while(row >= 1 && col >= 1) {
                if (board.getPiece(new ChessPosition(row, col)) == null) {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    row--;
                    col--;
                } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == bishop.getTeamColor()) {
                    break;
                } else {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
                    break;
                }
            }

            return this.ChessSet;
        }
}
