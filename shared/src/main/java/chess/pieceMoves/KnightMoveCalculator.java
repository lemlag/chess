package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveCalculator implements PieceMovesCalculator{

    Collection<ChessMove> ChessSet;

    public KnightMoveCalculator(){
        ChessSet = new HashSet<>();
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece knight = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece enemy;
        ChessPosition enemyPos;

        if (row > 2){
            if (col >=2){
                enemyPos = new ChessPosition(row -2, col-1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(col <= 7){
                enemyPos = new ChessPosition(row-2, col+1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        if (row < 7){
            if (col >=2){
                enemyPos = new ChessPosition(row +2, col-1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(col <= 7){
                enemyPos = new ChessPosition(row+2, col+1);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        if (col > 2){
            if (row >=2){
                enemyPos = new ChessPosition(row -1, col-2);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(row <= 7){
                enemyPos = new ChessPosition(row+1, col-2);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        if (col < 7){
            if (row >=2){
                enemyPos = new ChessPosition(row -1, col+2);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
            if(row <= 7){
                enemyPos = new ChessPosition(row+1, col+2);
                enemy = board.getPiece(enemyPos);
                if( enemy == null || enemy.getTeamColor() != knight.getTeamColor()){
                    this.ChessSet.add(new ChessMove(position, enemyPos, null ));
                }
            }
        }

        return ChessSet;
    }
}
