package chess.pieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;


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
        ChessPosition enemyPos;

        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            rowModifier = 1;
        } else{
            rowModifier = -1;
        }

        if (col > 1) { // Test against left side piece
            enemyPos = new ChessPosition(row + rowModifier, col - 1);
            checkDiagonal(board, position, pawn, enemyPos);
        }
        if (col < 8) { // Test against right side piece
            enemyPos = new ChessPosition(row + rowModifier, col + 1);
            checkDiagonal(board, position, pawn, enemyPos);
        }

        enemyPos = new ChessPosition(row + rowModifier, col);
        if (board.getPiece(enemyPos) == null) {
            promotion(position, enemyPos);
        }

        if(pawn.getTeamColor() == ChessGame.TeamColor.WHITE && row == 2){
            if(board.getPiece(new ChessPosition(row+1, col)) == null && board.getPiece(new ChessPosition(row+2, col)) == null){
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row+2, col), null));
            }
        } else if(pawn.getTeamColor() == ChessGame.TeamColor.BLACK && row == 7){
            if(board.getPiece(new ChessPosition(row-1, col)) == null && board.getPiece(new ChessPosition(row-2, col)) == null){
                this.ChessSet.add(new ChessMove(position, new ChessPosition(row-2, col), null));
            }
        }

        return this.ChessSet;
    }

    private void checkDiagonal(ChessBoard board, ChessPosition position, ChessPiece pawn, ChessPosition enemyPos) {
        ChessPiece diagonal = board.getPiece(enemyPos);
        if (diagonal != null && diagonal.getTeamColor() != pawn.getTeamColor()) {
            promotion(position, enemyPos);
        }
    }

    private void promotion(ChessPosition position, ChessPosition enemyPos) {
        if(enemyPos.getRow() == 1 || enemyPos.getRow() == 8){
            this.ChessSet.add(new ChessMove(position, enemyPos, ChessPiece.PieceType.QUEEN));
            this.ChessSet.add(new ChessMove(position, enemyPos, ChessPiece.PieceType.ROOK));
            this.ChessSet.add(new ChessMove(position, enemyPos, ChessPiece.PieceType.KNIGHT));
            this.ChessSet.add(new ChessMove(position, enemyPos, ChessPiece.PieceType.BISHOP));
        } else {
            this.ChessSet.add(new ChessMove(position, enemyPos, null));
        }
    }
}
