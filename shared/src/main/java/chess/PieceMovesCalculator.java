package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface PieceMovesCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}
