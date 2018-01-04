package coursera.assignment3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Client {

    public static void main(final String[] args) {
        // read the n points from a file
        final In in = new In(args[0]);
        final int n = in.readInt();
        final Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (final Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        final FastCollinearPoints collinear = new FastCollinearPoints(points);
        //final BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (final LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
