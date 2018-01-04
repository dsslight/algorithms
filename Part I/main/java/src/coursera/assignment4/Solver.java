package coursera.assignment4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;

public class Solver {

    private final ArrayDeque<Board> solution = new ArrayDeque<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(final Board initial) {
        if (initial != null) {
            final Comparator<ComparableBoard> boardComparator = Comparator.comparingInt(ComparableBoard::priority);
            final MinPQ<ComparableBoard> minPQ = new MinPQ<>(boardComparator);
            final MinPQ<ComparableBoard> twinMinPQ = new MinPQ<>(boardComparator);
            ComparableBoard comparable = new ComparableBoard(initial);
            minPQ.insert(comparable);

            final Board initialTwin = initial.twin();
            ComparableBoard twinComparable = new ComparableBoard(initialTwin);
            twinMinPQ.insert(twinComparable);
            while (!(comparable.isGoal() || twinComparable.isGoal())) {
                comparable = minPQ.delMin();
                for (final ComparableBoard comparableNeighbor : comparable.neighbors()) {
                    if (comparable.getParent() == null || !comparableNeighbor.equals(comparable.getParent())) {
                        minPQ.insert(comparableNeighbor);
                    }
                }

                twinComparable = twinMinPQ.delMin();
                for (final ComparableBoard twinComparableNeighbor : twinComparable.neighbors()) {
                    if (twinComparable.getParent() == null || !twinComparableNeighbor.equals(twinComparable.getParent())) {
                        twinMinPQ.insert(twinComparableNeighbor);
                    }
                }
            }

            if (comparable.isGoal()) {
                solution.add(comparable.getBoard());
                while (comparable.getParent() != null) {
                    solution.addFirst(comparable.getParent().getBoard());
                    comparable = comparable.getParent();
                }
            }
        } else {
            throw new IllegalArgumentException("initial board should be provided");
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return !solution.isEmpty();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return Collections.unmodifiableCollection(solution);
        } else {
            return null;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        final In in = new In(args[0]);
        final int n = in.readInt();
        final int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        final Board initial = new Board(blocks);
        // solve the puzzle
        final Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (final Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println();
            }
        }

    }
}
