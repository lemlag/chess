package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class QueenMovesCalculator implements PieceMovesCalculator {


    Collection<ChessMove> ChessSet;

    public QueenMovesCalculator(){
        this.ChessSet = new HashSet<>();
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> RookSet;
        PieceMovesCalculator mover;
        mover = new BishopMovesCalculator();
        this.ChessSet = mover.pieceMoves(board, position);
        mover = new RookMovesCalculator();
        RookSet = mover.pieceMoves(board, position);
        this.ChessSet.addAll(RookSet);

        return this.ChessSet;
    }
}
