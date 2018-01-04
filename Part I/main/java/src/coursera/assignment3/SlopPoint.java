package coursera.assignment3;

public class SlopPoint {
    private final Point point;
    private final double slop;

    private SlopPoint(final Point point, final double slop) {
        this.point = point;
        this.slop = slop;
    }

    public static SlopPoint makeSlopPoint(final Point point, final double slop) {
        return new SlopPoint(point, slop);
    }

    public boolean compareWithSlop(final Point point, final double slop) {
        return this.slop == slop && this.point.compareTo(point) == 0;
    }
}