package coursera.assignment1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte[][] SHIFT_AROUND = {{-1, 0}, {+1, 0}, {0, -1}, {0, +1}};

    private final int n;
    private final boolean[] array;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private int openSites = 0;
    private final int[] topComponents;
    private Boolean isPercolate;
    // create n-by-n grid, with all id blocked
    public Percolation(int n) {
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n);
        if (n > 0) {
            this.n = n;
            array = new boolean[n * n];
            topComponents = new int[n];
            for (int i = 0; i < n; i++) {
                topComponents[i] = i;
            }
            for (int i = 0; i < array.length; i++) {
                array[i] = false;
            }
        } else {
            throw new IllegalArgumentException("n should be bigger then 0");
        }
    }

    private static void validate(final int param) {
        if (param < 1) {
            throw new IllegalArgumentException("every parameter should be 1 or bigger");
        }
    }

    private int index(final int row, final int col) {
        validate(row);
        validate(col);
        final int index = (row - 1) * n + col - 1;
        if (col <= n && row <= n && index < array.length) {
            return index;
        } else {
            throw new IllegalArgumentException("every parameter should suit " + n + '*' + "n" + " model");
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        final int index = index(row, col);

        if (!isOpen(row, col)) {
            array[index] = true;
            openSites++;

            boolean isUnion = false;
            for (final byte[] shift : SHIFT_AROUND) {
                int i = row + shift[0];
                int j = col + shift[1];
                if (i >= 1 && i <= n && j >= 1 && j <= n && isOpen(i, j)) {
                    weightedQuickUnionUF.union((i - 1) * n + (j - 1), index);
                    isUnion = true;
                }
            }
            for (int i = 0; isUnion && i < n; i++) {
                topComponents[i] = weightedQuickUnionUF.find(i);
            }
            isPercolate = null;
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return array[index(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            final int component = weightedQuickUnionUF.find(index(row, col));
            for (int i = 0; i < n; i++) {
                if (topComponents[i] == component) {
                    return true;
                }
            }
        }
        return false;
    }

    // number of open id
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (isPercolate == null) {
            for (int i = 0; i < n; i++) {
                for (int j = n * (n - 1); j < n * n; j++) {
                    if (array[i] && array[j] && weightedQuickUnionUF.connected(i, j)) {
                        break;
                    }
                }
            }
        }
        return isPercolate;
    }

    // test client (optional)
    public static void main(String[] args) {
        /*
        final Percolation percolation = new Percolation(StdIn.readInt());

        try {
            while (!StdIn.isEmpty()) {
                percolation.open(StdIn.readInt(), StdIn.readInt());
            }
        } catch (final Exception e) {

        }

        // percolation.isFull(1, 6);
         percolation.percolates();
         */
        // percolation.numberOfOpenSites();
        /*
        final Percolation percolation = new Percolation(5);
        percolation.open(1, 1);
        percolation.open(1, 4);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);
        percolation.open(5, 1);

        percolation.weightedQuickUnionUF.find(8);
        percolation.isFull(4, 1);
        percolation.percolates();
        percolation.numberOfOpenSites();
        */
    }
}
