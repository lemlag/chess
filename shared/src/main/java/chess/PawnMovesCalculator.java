package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PawnMovesCalculator implements PieceMovesCalculator{

    Collection<ChessMove> ChessSet;

    public PawnMovesCalculator(){
        this.ChessSet = new HashSet<>();
    }

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece pawn = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        int rowModifier;

        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            rowModifier = 1;
        } else{
            rowModifier = -1;
        }

            if (col > 1) { // Test against left side piece
                ChessPiece diagonal = board.getPiece(new ChessPosition(row + rowModifier, col - 1));
                if (diagonal != null && diagonal.getTeamColor() != pawn.getTeamColor()) {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row + rowModifier, col - 1), null));
                }
            }
            if (col < 8) { // Test against right side piece
                ChessPiece diagonal = board.getPiece(new ChessPosition(row + rowModifier, col + 1));
                if (diagonal != null && diagonal.getTeamColor() != pawn.getTeamColor()) {
                    this.ChessSet.add(new ChessMove(position, new ChessPosition(row + rowModifier, col + 1), null));
                }
            }

            if (board.getPiece(new ChessPosition(row + rowModifier, col)) == null) {
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row + rowModifier, col), null));
            }


        return this.ChessSet;
    }
}
