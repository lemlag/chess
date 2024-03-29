package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {

    Collection<ChessMove> chessSet;

    public KingMovesCalculator(){
        this.chessSet = new HashSet<>();
    }

    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece king = board.getPiece(position);
        int kRow = position.getRow();
        int kCol = position.getColumn();
        ChessPiece enemy;
        ChessPosition enemyPos;
        int enemyRow, enemyCol;

        for(int row = -1; row < 2; row++ ){
            for(int col = -1; col < 2; col++){
                enemyRow = kRow + row;
                enemyCol = kCol + col;
                if(enemyRow > 0 && enemyRow < 9 && enemyCol > 0 && enemyCol < 9){
                    enemyPos = new ChessPosition(enemyRow, enemyCol);
                    if(enemyPos == position){
                        continue;
                    }
                    enemy = board.getPiece(enemyPos);
                    if( enemy == null || enemy.getTeamColor() != king.getTeamColor()){
                        this.chessSet.add(new ChessMove(position, enemyPos, null ));
                    }
                }
            }
        }

        return this.chessSet;
    }
}
