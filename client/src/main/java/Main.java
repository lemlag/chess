import chess.*;

import static ui.Client.drawMenu;
import static ui.DrawChessBoard.drawBoard;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        boolean notQuitting = true;
        while(notQuitting){
            notQuitting = drawMenu();
        }
    }
}