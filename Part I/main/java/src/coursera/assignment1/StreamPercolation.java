package coursera.assignment1;

import edu.princeton.cs.algs4.StdIn;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class StreamPercolation /* extends WeightedQuickUnionUF */{
    private static final Consumer<Integer> VALIDATE = param -> {
        if (param < 1) {
            throw new IllegalStateException("every parameter should be 1 or bigger");
        }
    };

    private final int n;
    private final BiFunction<Integer, Integer, Integer> INDEXER;
    private final int[] id;

    // create n-by-n grid, with all id blocked
    public StreamPercolation(int n) {
        if (n > 0) {
            this.n = n;
            id = new int[n * n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    //final int index = i * n + j;
                    id[i * n + j] = -1;//index;
                }
            }
            INDEXER = (row, col) -> (row - 1) * n + col - 1;
        } else {
            throw new IllegalStateException("n should be bigger then 0");
        }
    }

    public int find(int p) {
        while (p != id[p]) {
            p = id[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make smaller root point to larger one
        if (id[rootP] < id[rootQ]) {
            id[rootP] = rootQ;
        } else {
            id[rootQ] = rootP;
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        VALIDATE.accept(row);
        VALIDATE.accept(col);

        final int index = INDEXER.apply(row, col);

        if (id[index] < 0) {
            id[index] = index;
        }

        //row iteration
        for (int j = row - 2; j <= row; j += 2) {
            if (j >= 0 && j < n && isOpen(j + 1, col)) {
                union(j * n + (col - 1), index);
            }
        }

        //col iteration
        for (int j = col - 2; j <= col; j += 2) {
            if (j >= 0 && j < n && isOpen(row, j + 1)) {
                union((row - 1) * n + j, index);
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        VALIDATE.accept(row);
        VALIDATE.accept(col);

        return id[INDEXER.apply(row, col)] >= 0;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        VALIDATE.accept(row);
        VALIDATE.accept(col);

        final int index = INDEXER.apply(row, col);
        return id[index] >= 0 && find(index) < n;
    }

    // number of open id
    public int numberOfOpenSites() {
        return (int) IntStream.of(id).filter(i -> i >= 0).count();
    }

    // does the system percolate?
    public boolean percolates() {
        return IntStream.of(id).skip(n * (n - 1)).filter(i -> i >= 0 && i < n).count() >= 1L;
    }

    // test client (optional)
    public static void main(String[] args) {
        final StreamPercolation percolation = new StreamPercolation(StdIn.readInt());
        while(StdIn.hasNextLine()) {
            percolation.open(StdIn.readInt(), StdIn.readInt());
        }
        percolation.percolates();
        percolation.numberOfOpenSites();
        /*
        final StreamPercolation percolation = new StreamPercolation(5);
        percolation.open(1, 1);
        percolation.open(1, 4);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);
        percolation.open(5, 1);

        percolation.find(0);

        percolation.isFull(4, 1);
        percolation.percolates();
        percolation.numberOfOpenSites();
        */
    }
}
