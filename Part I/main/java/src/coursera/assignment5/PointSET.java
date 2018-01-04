package coursera.assignment5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;


public class PointSET {
    private final NavigableSet<Point2D> tree;

    // construct an empty set of points
    public PointSET() {
        tree = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return tree.size();
    }

    private static void checkArgument(final Object arg) {
        if (arg == null) {
            throw new IllegalArgumentException("argument should be not null");
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(final Point2D p) {
        checkArgument(p);
        tree.add(p);
    }

    // does the set contain point p?
    public boolean contains(final Point2D p) {
        checkArgument(p);
        return tree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        tree.forEach(point2D -> point2D.draw());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(final RectHV rect) {
        checkArgument(rect);
        // TODO one more bull shit, where I can't use Collectors.toList
        return tree.stream().filter(point -> rect.contains(point)).collect(ArrayList::new, List::add,
                (left, right) -> left.addAll(right));
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(final Point2D p) {
        checkArgument(p);
        if (!tree.isEmpty()) {
            return tree.stream().map(point -> new AbstractMap.SimpleImmutableEntry<>(point.distanceSquaredTo(p), point))
                    .sorted(Comparator.comparingDouble(entry -> entry.getKey())).findFirst().get().getValue();
        } else {
            return null;
        }
    }

}
