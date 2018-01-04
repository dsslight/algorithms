package coursera.assignment5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;


public class PointSETTest {
    private final List<Point2D> INITIAL_STATE = asList(new Point2D(4, 3), new Point2D(4, 4), new Point2D(3, 5), new Point2D(4, 5), new Point2D(4.5, 5));
    private PointSET pointSET;

    @BeforeMethod
    public void before() {
        pointSET = new PointSET();
        INITIAL_STATE.forEach(point -> pointSET.insert(point));
    }

    @Test
    public void sizeTest() {
        MatcherAssert.assertThat(pointSET.size(), equalTo(INITIAL_STATE.size()));
    }

    @Test
    public void rangeTest() {
        final RectHV rectHV = new RectHV(1, 1, 5, 5);
        MatcherAssert.assertThat(pointSET.range(rectHV), containsInAnyOrder(INITIAL_STATE.toArray()));
    }

    @Test
    public void nearestTest() {
        MatcherAssert.assertThat(pointSET.nearest(new Point2D(3.9, 4.1)), equalTo(INITIAL_STATE.get(1)));
        MatcherAssert.assertThat(pointSET.nearest(new Point2D(2.8, 3.1)), equalTo(INITIAL_STATE.get(0)));
        MatcherAssert.assertThat(pointSET.nearest(new Point2D(5, 5)), equalTo(INITIAL_STATE.get(4)));
    }
}
