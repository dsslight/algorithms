package coursera.assignment5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;


public class KdTreeTest {
    private static final List<Point2D> BASIC_STATE = asList(new Point2D(0.4, 0.3), new Point2D(0.4, 0.4), new Point2D(0.3, 0.5), new Point2D(0.4, 0.5), new Point2D(0.45, 0.5));
    private static final List<Point2D> FOUR_POINT_STATE = asList(new Point2D(.0, .5), new Point2D(.5, 1.), new Point2D(.5, .0), new Point2D(1., .5));
    private static final List<Point2D> DUPLICATE_POINT_STATE = asList(new Point2D(0.75, 0.0), new Point2D(1.0, 0.75), new Point2D(0.25, 0.25), new Point2D(0.25, 0.25),
            new Point2D(0.0, 0.0), new Point2D(0.5, 0.5), new Point2D(0.5, 0.25));
    private static final List<Point2D> TWO_DUPLICATES = asList(new Point2D(1.0, 0.5), new Point2D(0.75, 0.25), new Point2D(0.75, 0.0), new Point2D(1.0, 0.25),
            new Point2D(0.0, 1.0), new Point2D(0.0, 0.75), new Point2D(0.25, 0.25), new Point2D(0.0, 0.75), new Point2D(1.0, 0.5));
    private static final List<Point2D> SINGLE_DUPLICATE_TWO_ELEMENTS_LIST = asList(new Point2D(1., 0.), new Point2D(1., 0.));
    private static final List<Point2D> TWO_DUPLICATES_LONG_LIST = asList(new Point2D(0.375, 0.25), new Point2D(0.5, 0.875), new Point2D(0.75, 0.75), new Point2D(0.875, 0.5),
            new Point2D(1.0, 0.875), new Point2D(0.5, 1.0), new Point2D(0.75, 0.875), new Point2D(0.375, 0.5), new Point2D(0.875, 0.0), new Point2D(0.75, 0.375),
            new Point2D(0.625, 0.75), new Point2D(0.0, 0.5), new Point2D(0.125, 0.25), new Point2D(0.625, 0.25), new Point2D(0.75, 0.0), new Point2D(0.875, 1.0),
            new Point2D(0.75, 0.375), new Point2D(0.375, 0.25));
    private static final List<Point2D> STATE_FOR_CONTAINS = asList(new Point2D(0.28125, 0.53125), new Point2D(0.375, 0.59375), new Point2D(0.6875, 0.78125), new Point2D(0.5, 0.4375),
            new Point2D(0.59375, 0.8125), new Point2D(0.875, 0.96875), new Point2D(0.65625, 0.34375), new Point2D(0.9375, 0.3125), new Point2D(0.09375, 0.875),
            new Point2D(0.84375, 0.5), new Point2D(0.75, 1.0), new Point2D(1.0, 0.625), new Point2D(0.53125, 0.125), new Point2D(0.625, 0.5625),
            new Point2D(0.40625, 0.28125), new Point2D(0.4375, 0.1875), new Point2D(0.8125, 0.71875), new Point2D(0.1875, 0.65625), new Point2D(0.46875, 0.25),
            new Point2D(0.25, 0.6875));
    private static final List<Point2D> STATE_FOR_TINY_RECTANGLE = asList(new Point2D(.0, .0), new Point2D(0.5, .0), new Point2D(.0, 1.0), new Point2D(.0, .0),
            new Point2D(.0, .0));
    private static final List<Point2D> STATE_FOR_NEAREST_TRAVERSAL = asList(new Point2D(.5, .5), new Point2D(.5625, .25), new Point2D(.8125, .125), new Point2D(.6875, .5625),
            new Point2D(.0, .9375), new Point2D(.75, .375), new Point2D(.4375, 1.), new Point2D(.1875, .4375), new Point2D(.25, .0625), new Point2D(.125, .8125));

    private KdTree kdTree;

    @BeforeMethod
    public void before() {
        kdTree = new KdTree();
    }

    public void initialSetUp() {
        insertCustomState(BASIC_STATE);
    }

    public void fourPointSetUp() {
        insertCustomState(FOUR_POINT_STATE);
    }

    @Test
    public void sizeTest() {
        initialSetUp();
        MatcherAssert.assertThat(kdTree.size(), equalTo(BASIC_STATE.size()));
    }

    @Test
    public void rangeTest() {
        initialSetUp();
        final RectHV rectHV = new RectHV(.1, .1, .5, .5);
        MatcherAssert.assertThat(kdTree.range(rectHV), containsInAnyOrder(BASIC_STATE.toArray()));
    }

    @Test
    public void fourPointRangeTest() {
        fourPointSetUp();
        final RectHV rectHV = new RectHV(.0, .0, 1., 1.);
        MatcherAssert.assertThat(kdTree.range(rectHV), containsInAnyOrder(FOUR_POINT_STATE.toArray()));
    }

    @Test
    public void nearestTest() {
        initialSetUp();
        MatcherAssert.assertThat(kdTree.nearest(new Point2D(0.39, 0.41)), equalTo(BASIC_STATE.get(1)));
        MatcherAssert.assertThat(kdTree.nearest(new Point2D(0.28, 0.31)), equalTo(BASIC_STATE.get(0)));
        MatcherAssert.assertThat(kdTree.nearest(new Point2D(0.5, 0.5)), equalTo(BASIC_STATE.get(4)));
    }

    @Test
    public void fourNearestPointTest() {
        fourPointSetUp();
        MatcherAssert.assertThat(kdTree.nearest(new Point2D(.98, 0.57)), equalTo(FOUR_POINT_STATE.get(3)));
    }

    @DataProvider
    public Object[][] duplicateLists() {
        return new Object[][] {
                {DUPLICATE_POINT_STATE, DUPLICATE_POINT_STATE.size() - 1}, {TWO_DUPLICATES, TWO_DUPLICATES.size() - 2},
                {SINGLE_DUPLICATE_TWO_ELEMENTS_LIST, SINGLE_DUPLICATE_TWO_ELEMENTS_LIST.size() - 1}, {TWO_DUPLICATES_LONG_LIST, TWO_DUPLICATES_LONG_LIST.size() - 2}
        };
    }

    @Test(dataProvider = "duplicateLists")
    public void insertionDuplicatePointTest(final List<Point2D> list, final int size) {
        insertCustomState(list);
        MatcherAssert.assertThat(kdTree.size(), equalTo(size));
    }

    @Test
    public void containsAfterBunchOfInsertsTest() {
        insertCustomState(STATE_FOR_CONTAINS);
        MatcherAssert.assertThat(kdTree.contains(new Point2D(0.625, 0.5625)), equalTo(true));
    }

    @Test
    public void containsAfterSingleInsert() {
        kdTree.insert(new Point2D(1., 1.));
        MatcherAssert.assertThat(kdTree.contains(new Point2D(1.0, .0)), equalTo(false));
    }

    @Test
    public void rangeForTinyRectangle() {
        insertCustomState(STATE_FOR_TINY_RECTANGLE);
        MatcherAssert.assertThat(kdTree.range(new RectHV(.0, .0, 0.125, 0.125)), equalTo(asList(new Point2D(.0, .0))));
    }

    @Test
    public void nearestTraversal() {
        insertCustomState(STATE_FOR_NEAREST_TRAVERSAL);
        // calls should be to "A B D F"
        MatcherAssert.assertThat(kdTree.nearest(new Point2D(1., .6875)), equalTo(new Point2D(.6875, .5625)));
    }

    private void insertCustomState(List<Point2D> stateForTinyRectangle) {
        stateForTinyRectangle.forEach(point -> kdTree.insert(point));
    }
}
