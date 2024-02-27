package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> chessSet;

    public QueenMovesCalculator(){
        this.chessSet = new HashSet<>();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> rookSet;
        PieceMovesCalculator mover;
        mover = new BishopMovesCalculator();
        this.chessSet = mover.pieceMoves(board, position);
        mover = new RookMovesCalculator();
        rookSet = mover.pieceMoves(board, position);
        this.chessSet.addAll(rookSet);

        return this.chessSet;
    }
}
