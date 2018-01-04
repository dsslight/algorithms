package coursera.assignment4;

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {
    private final short[][] blocks;
    private int hamming = -1;
    private int manhattan = -1;
    private List<Board> neighbors = null;
    private Board twin;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(final int[][] blocks) {
        this.blocks = deepCopy(blocks);
    }

    private Board(final short[][] blocks) {
        this.blocks = blocks;
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming < 0) {
            hamming = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] != i * dimension() + j + 1 && (i < dimension() - 1 || j < dimension() - 1)) {
                        hamming++;
                    }
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan < 0) {
            manhattan = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    int value = blocks[i][j];
                    if (value != 0 && value != i * dimension() + j + 1) {
                        final int row = (int) Math.ceil(value * 1. / dimension()) - 1;
                        manhattan += Math.abs(row - i) + Math.abs(value - row * dimension() - j - 1);
                    }
                }
            }
            // optimization to solve hard puzzles like puzzle4x4-78.txt or puzzle4x4-80.txt, required stronger rangers
            /*
            if (System.getProperty("ranger") != null) {
                manhattan += negativePoint() * 8 - positivePoint() * 8;
            }
            */

        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (twin == null) {
            int x1 = 0, y1 = 0, x2 = 0, y2 = 0;

            while ((x1 == x2 && y1 == y2) || blocks[x1][y1] == 0 || blocks[x2][y2] == 0) {
                x1 = StdRandom.uniform(dimension());
                y1 = StdRandom.uniform(dimension());
                x2 = StdRandom.uniform(dimension());
                y2 = StdRandom.uniform(dimension());
            }

            final short[][] twinBlocks = deepCopy(this.blocks);
            twinBlocks[x1][y1] ^= twinBlocks[x2][y2];
            twinBlocks[x2][y2] ^= twinBlocks[x1][y1];
            twinBlocks[x1][y1] ^= twinBlocks[x2][y2];

            twin = new Board(twinBlocks);
        }
        return twin;
    }

    // does this board equal obj?
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        return Arrays.deepEquals(blocks, ((Board) obj).blocks);
    }

    private static short[][] deepCopy(final int[][] array) {
        final short[][] copyArray = new short[array.length][];
        for (int i = 0; i < array.length; i++) {
            copyArray[i] = new short[array.length];
            for (int j = 0; j < array.length; j++) {
                copyArray[i][j] = (short) array[i][j];
            }
        }
        return copyArray;
    }

    private static short[][] deepCopy(final short[][] array) {
        final short[][] copyArray = new short[array.length][];
        for (int i = 0; i < array.length; i++) {
            copyArray[i] = new short[array.length];
            for (int j = 0; j < array.length; j++) {
                copyArray[i][j] = array[i][j];
            }
        }
        return copyArray;
    }

    private int positivePoint() {
        int positivePoint = 0;
        main: for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == i * dimension() + j + 1) {
                    positivePoint++;
                } else {
                    break main;
                }
            }
        }

        return positivePoint;
    }

    private int negativePoint() {
        int negativePoint = 0;
        main: for (int i = dimension() - 1; i >= 0; i--) {
            for (int j = dimension() - 1; j >= 0; j--) {
                if (blocks[i][j] == dimension() * dimension() - (i * blocks.length + j)) {
                    negativePoint++;
                } else {
                    break main;
                }
            }
        }

        return negativePoint;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbors == null) {
            int xZero = -1;
            int yZero = -1;
            main:
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] == 0) {
                        xZero = i;
                        yZero = j;
                        break main;
                    }
                }
            }

            neighbors = new ArrayList<>();
            if (xZero > 0) {
                final short[][] neighborBlocks = deepCopy(this.blocks);
                final short val = neighborBlocks[xZero - 1][yZero];
                neighborBlocks[xZero - 1][yZero] = neighborBlocks[xZero][yZero];
                neighborBlocks[xZero][yZero] = val;
                neighbors.add(new Board(neighborBlocks));
            }
            if (xZero < dimension() - 1) {
                final short[][] neighborBlocks = deepCopy(this.blocks);
                final short val = neighborBlocks[xZero + 1][yZero];
                neighborBlocks[xZero + 1][yZero] = neighborBlocks[xZero][yZero];
                neighborBlocks[xZero][yZero] = val;
                neighbors.add(new Board(neighborBlocks));
            }

            if (yZero > 0) {
                final short[][] neighborBlocks = deepCopy(this.blocks);
                final short val = neighborBlocks[xZero][yZero - 1];
                neighborBlocks[xZero][yZero - 1] = neighborBlocks[xZero][yZero];
                neighborBlocks[xZero][yZero] = val;
                neighbors.add(new Board(neighborBlocks));
            }
            if (yZero < dimension() - 1) {
                final short[][] neighborBlocks = deepCopy(this.blocks);
                final short val = neighborBlocks[xZero][yZero + 1];
                neighborBlocks[xZero][yZero + 1] = neighborBlocks[xZero][yZero];
                neighborBlocks[xZero][yZero] = val;
                neighbors.add(new Board(neighborBlocks));
            }
        }
        return Collections.unmodifiableList(neighbors);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        final StringBuilder sb = new StringBuilder().append(dimension()).append('\n');
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sb.append(blocks[i][j]).append(' ');
            }
            final int pos = sb.lastIndexOf(" ");
            sb.replace(pos, pos + 1, "\n");
        }
        return sb.toString().trim();
    }
}