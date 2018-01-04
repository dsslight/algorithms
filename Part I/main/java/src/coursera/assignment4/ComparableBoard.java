package coursera.assignment4;

import java.util.ArrayList;
import java.util.List;

public class ComparableBoard {
    private final int move;
    private int priority = -1;
    private final Board board;
    private final ComparableBoard parent;

    public ComparableBoard(final Board board) {
        this(board, 0, null);
    }

    public ComparableBoard(final Board board, final int move, final ComparableBoard parent) {
        this.board = board;
        this.move = move;
        this.parent = parent;
    }

    public ComparableBoard getParent() {
        return parent;
    }

    public Iterable<ComparableBoard> neighbors() {
        final List<ComparableBoard> list = new ArrayList<>();
        for (final Board neighbor: board.neighbors()) {
            list.add(new ComparableBoard(neighbor, move + 1, this));
        }
        return list;
    }

    public Board getBoard() {
        return board;
    }

    public int priority() {
        if (priority == -1) {
            priority = board.manhattan() + move;
        }
        return priority;
    }

    public boolean isGoal() {
        return board.isGoal();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        return this.board.equals(((ComparableBoard) obj).getBoard());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "ComparableBoard{manhattan=" + board.manhattan() +
                ", move=" + move +
                ", priority=" + priority +
                '}';
    }
}