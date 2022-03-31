package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;
import java.util.function.ObjDoubleConsumer;

public abstract class State {

    protected Board board;

    public abstract State start();

    public abstract State move(final Position from, final Position to);

    public abstract State end();

    public abstract void status(final ObjDoubleConsumer<String> printScore);

    public abstract boolean isRunning();

    public abstract boolean isEnded();

    public final Map<Position, Piece> getBoard() {
        return board.toMap();
    }
}
