package coursera.assignment3;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    // constructs the point (x, y)
    public Point(final int x, final int y) {
        checkBorder(x);
        checkBorder(y);
        this.x = x;
        this.y = y;
    }

    private static void checkBorder(final int param) {
        if (param < 0 || param > Short.MAX_VALUE) {
            throw new IllegalArgumentException(param + " should be in [0, 32767]");
        }
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    @Override
    public int compareTo(final Point that) {
        if (y < that.y) {
            return -1;
        } else if (y > that.y) {
            return 1;
        } else if (x < that.x) {
            return -1;
        } else if (x > that.x) {
            return 1;
        } else {
            return 0;
        }
    }
    // the slope between this point and that point
    public double slopeTo(final Point that) {
        if (that == null) {
            throw new NullPointerException("parameter can't be null");
        }
        if (that.y == y && that.x == x) {
            return Double.NEGATIVE_INFINITY;
        } else if (that.y - y == 0) {
            return +0;
        } else if (that.x - x == 0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return (that.y - y) * 1.0 / (that.x - x);
        }
    }
    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return (that1, that2) -> {
            final double res1 = this.slopeTo(that1);
            final double res2 = this.slopeTo(that2);
            return res1 < res2 ? -1 : res1 > res2 ? +1 : 0;
        };
    }
}