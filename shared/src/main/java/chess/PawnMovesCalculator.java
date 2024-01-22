package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PawnMovesCalculator implements PieceMovesCalculator{

    Collection<ChessMove> ChessSet;

    public PawnMovesCalculator(){
        ChessSet = new HashSet<>();
    }

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece pawn = board.getPiece(position);
        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE){

        }
        return this.ChessSet;
    }
}
