package chess.domain.piece.state;

import chess.domain.chessboard.Coordinate;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceState;
import chess.domain.piece.Team;
import java.util.List;

public final class Pawn extends Piece {

    private boolean isMoved = false;
    private boolean isEnemyOnDiagonal = false;

    public Pawn(final Team team) {
        super(team);
    }

    @Override
    public List<Coordinate> findRoute(final Coordinate from, final Coordinate to) {
        validatePossibleDestination(from, to);
        isEnemyOnDiagonal = false;

        if (from.isSameFile(to)) {
            return verticalRoute(from, to);
        }

        isEnemyOnDiagonal = true;
        if (from.isPositiveDiagonal(to)) {
            return positiveDiagonalRoute(from, to);
        }

        return negativeDiagonalRoute(from, to);
    }

    private void validatePossibleDestination(final Coordinate from, final Coordinate to) {
        final int fileDistance = from.calculateFileDistance(to);
        final int rankDistance = from.calculateRankDistance(to);

        final boolean isMoveDiagonal = checkMoveDiagonal(fileDistance, rankDistance);
        final boolean isMoveOneStep = checkMoveOneStep(fileDistance, rankDistance);
        final boolean isMoveFirst = checkMoveFirst(fileDistance, rankDistance);

        if (!isMoveDiagonal && !isMoveOneStep && !isMoveFirst) {
            throwCanNotMoveException();
        }
    }

    private boolean checkMoveDiagonal(final int fileDistance, final int rankDistance) {
        return Math.abs(fileDistance) == 1 && rankDistance == this.team.getPawnDirection();
    }

    private boolean checkMoveOneStep(final int fileDistance, final int rankDistance) {
        return fileDistance == 0 && rankDistance == this.team.getPawnDirection();
    }

    private boolean checkMoveFirst(final int fileDistance, final int rankDistance) {
        return fileDistance == 0 && rankDistance == 2 * this.team.getPawnDirection() && !isMoved;
    }

    @Override
    public void validateRoute(final List<PieceState> pieceRoute) {
        if (isEnemyOnDiagonal) {
            validateMoveToDiagonal(pieceRoute.get(0));
            isMoved = true;
            return;
        }

        checkSquaresEmpty(pieceRoute);
        isMoved = true;
    }

    private void validateMoveToDiagonal(final PieceState square) {
        if (square.isSameTeam(this) || square.isEmpty()) {
            throwCanNotMoveException();
        }
    }
}
