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
        Collection<ChessMove> RookSet;
        PieceMovesCalculator mover;
        mover = new BishopMovesCalculator();
        this.chessSet = mover.pieceMoves(board, position);
        mover = new RookMovesCalculator();
        RookSet = mover.pieceMoves(board, position);
        this.chessSet.addAll(RookSet);

        return this.chessSet;
    }
}
