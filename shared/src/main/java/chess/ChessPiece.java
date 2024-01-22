package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> ChessSet;
        PieceMovesCalculator mover;
        ChessPiece myPiece = board.getPiece(myPosition);
        if(myPiece.getPieceType() == PieceType.BISHOP){
            mover = new BishopMovesCalculator();
        } else if (myPiece.getPieceType() == PieceType.KING) {
            mover = new KingMovesCalculator();
        } else if (myPiece.getPieceType() == PieceType.ROOK){
            mover = new RookMovesCalculator();
        } else if (myPiece.getPieceType() == PieceType.QUEEN) {
            mover = new QueenMovesCalculator();
        } else if (myPiece.getPieceType() == PieceType.KNIGHT){
            mover = new KnightMoveCalculator();
        } else if (myPiece.getPieceType() == PieceType.PAWN){
            mover = new PawnMovesCalculator();
        } else{
            mover = null;
        }

        assert mover != null;
        ChessSet = mover.pieceMoves(board, myPosition);
        return ChessSet;
    }
}
