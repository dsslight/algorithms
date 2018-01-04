package coursera.assignment5;

import edu.princeton.cs.algs4.Point2D;

public final class ClosestPoint {
    private double distance = Double.POSITIVE_INFINITY;
    private final Point2D originalPoint;
    private Point2D currentPoint;

    public ClosestPoint(final Point2D originalPoint) {
        this.originalPoint = originalPoint;
    }

    public boolean isClose(final Point2D point) {
        final double calculatedDistance = point.distanceSquaredTo(originalPoint);
        if (this.distance > calculatedDistance) {
            this.distance = calculatedDistance;
            currentPoint = point;
            return true;
        } else {
            return false;
        }
    }

    public double getDistance() {
        return distance;
    }

    public Point2D getCurrentPoint() {
        return currentPoint;
    }
}