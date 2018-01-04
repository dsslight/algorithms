package coursera.assignment3;

import java.util.Arrays;

public class FastCollinearPoints {
    private static final double LOAD_FACTOR = 0.75;
    private LineSegment[] lineSegments = new LineSegment[8];
    private SlopPoint[] storedPoints = new SlopPoint[8];

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(final Point[] points) {
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
        int lastPointIndex = 0;
        int pointIndexInFragment;
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());
            int lastIndex = i;
            do {
                double slope = Double.NaN;
                boolean isTheSame = false;
                Point[] pointInFragment = new Point[16];
                pointIndexInFragment = 0;
                pointInFragment[pointIndexInFragment++] = points[i];
                for (int j = lastIndex + 1; j < points.length; j++) {
                    lastIndex = j;
                    if (slope == pointInFragment[0].slopeTo(points[j])) {
                        if (pointIndexInFragment > pointInFragment.length * LOAD_FACTOR) {
                            pointInFragment = Arrays.copyOf(pointInFragment, pointInFragment.length * 2);
                        }
                        pointInFragment[++pointIndexInFragment] = points[j];
                        isTheSame = true;
                    } else if (!isTheSame) {
                        slope = points[i].slopeTo(points[j]);
                        for (int m = 0; m < storedPoints.length && storedPoints[m] != null; m++) {
                            if (storedPoints[m].compareWithSlop(points[j], slope)) {
                                slope = Double.NaN;
                                continue;
                            }
                        }
                        pointInFragment[pointIndexInFragment] = points[j];
                    } else {
                        break;
                    }
                }
                if (isTheSame && pointIndexInFragment >= 3) {
                    if (lineSegmentCount > lineSegments.length * LOAD_FACTOR) {
                        lineSegments = Arrays.copyOf(lineSegments, lineSegments.length * 2);
                    }
                    pointInFragment = Arrays.copyOf(pointInFragment, pointIndexInFragment + 1);
                    Arrays.sort(pointInFragment);

                    lineSegments[lineSegmentCount++] = new LineSegment(pointInFragment[0], pointInFragment[pointIndexInFragment]);

                    for (final Point point : pointInFragment) {
                        if (lastPointIndex + pointInFragment.length > storedPoints.length * LOAD_FACTOR) {
                            storedPoints = Arrays.copyOf(storedPoints, storedPoints.length * 2);
                        }
                        storedPoints[lastPointIndex++] = SlopPoint.makeSlopPoint(point, slope);
                    }
                    lastIndex--;
                }
            } while (lastIndex != points.length - 1);
        }
        lineSegments = Arrays.copyOf(lineSegments, lineSegmentCount);
    }

    private static void checkArgument(final Object param) {
        if (param == null) {
            throw new IllegalArgumentException("param should be not null");
        }
    }
    /*
(10000, 0) -> (0, 10000)
(13000, 0) -> (5000, 12000)
(30000, 0) -> (0, 30000)-
    */
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }
}
