package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
public class KingMovesCalculator implements PieceMovesCalculator {

    Collection<ChessMove> ChessSet;

    public KingMovesCalculator(){
        this.ChessSet = new HashSet<>();
    }

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece king = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece enemy;
        ChessPosition enemyPos;

        if(row > 1){
            enemyPos = new ChessPosition(row-1, col);
            enemy = board.getPiece(enemyPos);
            if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                this.ChessSet.add(new ChessMove(position, enemyPos, null ));
            }
            if(col > 1){
                enemyPos = new ChessPosition(row-1, col-1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(col < 8){
                enemyPos = new ChessPosition(row-1, col+1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        if(row<8){
            enemyPos = new ChessPosition(row+1, col);
            enemy = board.getPiece(enemyPos);
            if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                this.ChessSet.add(new ChessMove(position, enemyPos, null ));
            }
            if(col > 1){
                enemyPos = new ChessPosition(row+1, col-1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(col < 8){
                enemyPos = new ChessPosition(row+1, col+1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        if(col > 1){
            enemyPos = new ChessPosition(row, col-1);
            enemy = board.getPiece(enemyPos);
            if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                this.ChessSet.add(new ChessMove(position, enemyPos, null ));
            }
        }

        if(col < 8){
            enemyPos = new ChessPosition(row, col+1);
            enemy = board.getPiece(enemyPos);
            if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                this.ChessSet.add(new ChessMove(position, enemyPos, null ));
            }
        }

        return this.ChessSet;
    }
}
