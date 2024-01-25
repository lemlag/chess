package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

        public BishopMovesCalculator(){
            ChessSet = new HashSet<>();
        }

        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
            ChessPiece bishop = board.getPiece(position);
            int bRow = position.getRow();
            int bCol = position.getColumn();
            int row;
            int col;

            for(row = bRow + 1, col = bCol +1; row <= 8 && col <= 8; row++, col++){
                if (addMoves(board, position, bishop, row, col)) break;
            }

            for(row = bRow + 1, col = bCol - 1; row <= 8 && col > 0; row++, col--){
                if (addMoves(board, position, bishop, row, col)) break;
            }
            for(row = bRow - 1, col = bCol - 1; row > 0 && col > 0; row--, col--){
                if (addMoves(board, position, bishop, row, col)) break;
            }


            for(row = bRow - 1, col = bCol + 1; row > 0 && col <= 8; row--, col++){
                if (addMoves(board, position, bishop, row, col)) break;
            }

            return this.ChessSet;
        }

    private boolean addMoves(ChessBoard board, ChessPosition position, ChessPiece bishop, int row, int col) {
        if (board.getPiece(new ChessPosition(row, col)) == null) {
            this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
        } else if (board.getPiece(new ChessPosition(row, col)).getTeamColor() == bishop.getTeamColor()) {
            return true;
        } else {
            this.ChessSet.add(new ChessMove(position, new ChessPosition(row, col), null));
            return true;
        }
        return false;
    }
}
