package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QueenMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

    public QueenMovesCalculator(){
        ChessSet = new HashSet<>();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> RookSet;
        PieceMovesCalculator mover;
        mover = new BishopMovesCalculator();
        ChessSet = mover.pieceMoves(board, position);
        mover = new RookMovesCalculator();
        RookSet = mover.pieceMoves(board, position);
        ChessSet.addAll(RookSet);

        return this.ChessSet;
    }
}
