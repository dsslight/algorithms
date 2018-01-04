package coursera.assignment1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private static final double CONST = 1.96d;
    private final double[] array;
    private double mean = Double.NaN;
    private double stddev = Double.NaN;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n > 0 && trials > 0) {
            array = new double[trials];

            for (int i = 0; i < trials; i++) {
                int counter = 0;
                final Percolation percolation = new Percolation(n);
                while (!percolation.percolates()) {
                    int row;
                    int col;
                    do {
                        row = StdRandom.uniform(n) + 1;
                        col = StdRandom.uniform(n) + 1;
                    } while (percolation.isOpen(row, col));
                    percolation.open(row, col);
                    counter++;
                }
                array[i] = counter / Math.pow(n, 2);
            }
        } else {
            throw new IllegalArgumentException("n or trails less then or equal to 0");
        }

    }
    // sample mean of percolation threshold
    public double mean() {
        if (Double.isNaN(mean)) {
            mean = StdStats.mean(array);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (Double.isNaN(stddev)) {
            stddev = StdStats.stddev(array);
        }
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - getInterval();
    }

    private double getInterval() {
        return CONST * stddev() / Math.sqrt(array.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + getInterval();
    }

    // test client (described below)
    public static void main(String[] args) {
        final PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.print("mean = ");
        StdOut.println(percolationStats.mean());

        StdOut.print("stddev = ");
        StdOut.println(percolationStats.stddev());

        StdOut.print("95% confidence interval = [");
        StdOut.print(percolationStats.confidenceLo());
        StdOut.print(',');
        StdOut.print(percolationStats.confidenceHi());
        StdOut.println("]");
    }
}
