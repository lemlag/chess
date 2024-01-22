package chess;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class PawnMovesCalculator implements PieceMovesCalculator{

    public PawnMovesCalculator(){
    }

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece pawn = board.getPiece(position);
        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE){

        }
    }
}
