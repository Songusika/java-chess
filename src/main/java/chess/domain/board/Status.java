package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;
import java.util.function.ObjDoubleConsumer;

public class Status {

    public static final int MINIMUM_PAWN_COUNT = 1;
    public static final int DIVIDED = 2;

    private final Board board;

    public Status(final Board board) {
        this.board = board;
    }

    public void show(final ObjDoubleConsumer<String> printScore) {
        for (Color color : Color.sorted()) {
            showColorStatus(printScore, color);
        }
    }

    private void showColorStatus(final ObjDoubleConsumer<String> printScore, final Color color) {
        printScore.accept(color.getName(), calculateScore(color));
    }

    private double calculateScore(final Color color) {
        double pawnScore = calculateScorePawn(color);
        double otherScore = calculateScoreOtherPiece(color);

        return pawnScore + otherScore;
    }

    private double calculateScorePawn(final Color color) {
        Map<Position, Piece> pieces = board.toMap();
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isSameColor(color))
                .filter(entry -> entry.getValue().isPawn())
                .map(entry -> getPawnScore(entry.getKey(), entry.getValue(), color))
                .reduce(0.0, Double::sum);
    }

    private double getPawnScore(final Position position, final Piece pawn, final Color color) {
        long pawnCountInXAxis = getPawnCountInXAxis(position, color);
        if (pawnCountInXAxis > MINIMUM_PAWN_COUNT) {
            return pawn.getScore() / DIVIDED;
        }
        return pawn.getScore();
    }

    private long getPawnCountInXAxis(final Position position, final Color color) {
        Map<Position, Piece> pieces = board.toMap();
        return pieces.entrySet().stream()
                .filter(entry -> entry.getValue().isSameColor(color))
                .filter(entry -> entry.getValue().isPawn())
                .filter(entry -> entry.getKey().getCoordinateX().equals(position.getCoordinateX()))
                .count();
    }

    private double calculateScoreOtherPiece(final Color color) {
        Map<Position, Piece> pieces = board.toMap();
        return pieces.values().stream()
                .filter(piece -> piece.isSameColor(color))
                .filter(piece -> !piece.isPawn())
                .map(Piece::getScore)
                .reduce(0.0, Double::sum);
    }
}
