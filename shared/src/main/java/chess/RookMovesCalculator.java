package chess;

import java.util.Collection;
import java.util.HashSet;

import static chess.BishopMovesCalculator.addMovePiece;

public class RookMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

    public RookMovesCalculator(){
        ChessSet = new HashSet<>();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece rook = board.getPiece(position);
        int rookRow = position.getRow();
        int rookCol = position.getColumn();
        int row;
        int col;
        for(row = rookRow + 1, col = rookCol; row < 9; row++){
            if (addSet(board, position, rook, row, col)) break;
        }

        for(row = rookRow - 1; row > 0; row--){
            if (addSet(board, position, rook, row, col)) break;
        }

        for(row = rookRow, col = rookCol + 1; col < 9; col++){
            if (addSet(board, position, rook, row, col)) break;
        }

        for(col = rookCol - 1; col > 0; col--){
            if (addSet(board, position, rook, row, col)) break;
        }

        return this.ChessSet;
    }

    private boolean addSet(ChessBoard board, ChessPosition position, ChessPiece rook, int row, int col) {
        return addMovePiece(board, position, rook, row, col, this.ChessSet);
    }
}

