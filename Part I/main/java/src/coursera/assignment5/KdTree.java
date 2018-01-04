package coursera.assignment5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class KdTree {
    private static final double POINT_RADIUS = 0.02;
    private static final double LINE_WIDTH = 0.005;
    private int size = 0;
    private Node root;

    private static final Comparator<Point2D> X_COMPARATOR = Comparator.comparing(Point2D::x);
    private static final Comparator<Point2D> Y_COMPARATOR = Comparator.comparing(Point2D::y);

    private static Util.BiFunction<Node, Point2D, Integer> COMPARATOR = (node, point2D) -> {
        if (!node.point2D.equals(point2D)) {
            return (node.isHorizontal()) ? Y_COMPARATOR.<Point2D>compare(node.point2D, point2D) : X_COMPARATOR.<Point2D>compare(node.point2D, point2D);
        } else {
            return 0;
        }
    };

    private static class Node {
        private final int level;
        private final Point2D point2D;
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left, right;
        private Node parent;

        public Node(final int level, final Point2D point2D) {
            this.level = level;
            this.rect = new RectHV(0, 0, 1, 1);
            this.point2D = point2D;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public Point2D getPoint2D() {
            return point2D;
        }

        public Node getParent() {
            return parent;
        }

        public boolean isRoot() {
            return getParent() == null;
        }

        public RectHV getRect() {
            return rect;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            final Node node = (Node) obj;
            return Objects.equals(point2D, node.point2D);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point2D);
        }

        public Point2D getPoint() {
            return point2D;
        }

        public void setParent(final Node parent) {
            this.parent = parent;

            if (isHorizontal()) {
                if (point2D.x() <= parent.point2D.x()) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point2D.x(), parent.rect.ymax());
                } else {
                    rect = new RectHV(parent.point2D.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                }
            } else {
                if (point2D.y() <= parent.point2D.y()) {
                    rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point2D.y());
                } else {
                    rect = new RectHV(parent.rect.xmin(), parent.point2D.y(), parent.rect.xmax(), parent.rect.ymax());
                }
            }
        }

        public boolean isHorizontal() {
            return level % 2 == 0;
        }

        public void insert(final Point2D point) {
            final Node node = new Node(level + 1, point);
            if (COMPARATOR.apply(this, point) < 0) {
                Util.checkNotArgument(left);
                left = node;
            } else {
                Util.checkNotArgument(right);
                right = node;
            }
            node.setParent(this);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "level=" + level +
                    ", point2D=" + point2D +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(final Point2D p) {
        Util.checkArgument(p);
        if (root != null && !p.equals(root.point2D)) {
            Node current = root;
            Node prev;
            boolean isEqual = false;
            do {
                prev = current;
                if (COMPARATOR.apply(current, p) < 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
            } while (current != null && !(isEqual = p.equals(current.point2D)));
            // no need to do anything, as current point exists already
            if (prev != null && !isEqual) {
                prev.insert(p);
                size++;
            }
        } else if (root == null) {
            root = new Node(1, p);
            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(final Point2D p) {
        Util.checkArgument(p);
        final Point2D nearest = nearest(p);
        return nearest != null && nearest.equals(p) ? true : false;
    }

    // draw all points to standard draw
    public void draw() {
        goRecursive(root, node -> node != null, node -> {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(POINT_RADIUS);
            node.getPoint().draw();
            StdDraw.setPenRadius(LINE_WIDTH);
            if (node.isHorizontal()) { // horizontal, blue
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.point2D.y(), node.rect.xmax(), node.point2D.y());
            } else { // vertical, red
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point2D.x(), node.rect.ymin(), node.point2D.x(), node.rect.ymax());
            }
        });
    }

    private void goRecursive(final Node node, final Util.Function<Node, Boolean> isIncluded, final Util.Consumer<Node> consumer) {
        if (isIncluded.apply(node)) {
            consumer.accept(node);
        }
        if (isIncluded.apply(node.getLeft())) {
            goRecursive(node.getLeft(), isIncluded, consumer);
        }
        if (isIncluded.apply(node.getRight())) {
            goRecursive(node.getRight(), isIncluded, consumer);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(final RectHV rect) {
        Util.checkArgument(rect);
        final List<Point2D> list = new ArrayList<>();
        if (!isEmpty()) {
            final Util.Function<Node, Boolean> isIncluded = node -> node != null && rect.intersects(node.getRect());

            goRecursive(root, isIncluded, node -> {
                if (rect.contains(node.getPoint())) {
                    list.add(node.getPoint());
                }
            });
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(final Point2D p) {
        Util.checkArgument(p);
        if (!isEmpty()) {
            final ClosestPoint closestPoint = new ClosestPoint(p);
            goRecursive(root, node -> node != null && node.getRect().distanceSquaredTo(p) < closestPoint.getDistance(),
                    node -> closestPoint.isClose(node.getPoint()));
            return closestPoint.getCurrentPoint();
        } else {
            return null;
        }

    }

}
