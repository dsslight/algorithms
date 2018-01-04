package coursera.assignment3;

import java.util.Arrays;

public class BruteCollinearPoints {
    private static final double LOAD_FACTOR = 0.75;
    private LineSegment[] lineSegment = new LineSegment[8];
    // finds all line segments containing 4 points
    public BruteCollinearPoints(final Point[] points) {
        checkArgument(points);
        for (final Point point : points) {
            checkArgument(point);
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("point " + i + " equalTo " + j);
                }
            }
        }

        int lineSegmentCount = 0;
        main: for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                final double slope1 = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    final double slope2 = points[i].slopeTo(points[k]);
                    for (int m = k + 1; m < points.length; m++) {
                        final double slope3 = points[i].slopeTo(points[m]);
                        if (slope1 == slope2 && slope2 == slope3) {
                            if (lineSegmentCount > lineSegment.length * LOAD_FACTOR) {
                                lineSegment = Arrays.copyOf(lineSegment, lineSegmentCount * 2);
                            }
                            final Point[] array = {points[i], points[j], points[k], points[m]};
                            Arrays.sort(array);
                            lineSegment[lineSegmentCount++] = new LineSegment(array[0], array[array.length - 1]);
                        }
                    }
                }
            }
        }
        lineSegment = Arrays.copyOf(lineSegment, lineSegmentCount);
    }

    private static void checkArgument(final Object param) {
        if (param == null) {
            throw new IllegalArgumentException("param should be not null");
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegment.length;
    }
    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegment, lineSegment.length);
    }
}
