import chess.*;
import ui.Client;

import static ui.DrawChessBoard.drawBoard;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Client client = new Client();

        client.drawMenu();
    }
}