package coursera.assignment3;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PointTest {

    @DataProvider
    public Object[][] provideCoordinates() {
        return new Object[][] {
                {92, 238, 92, 38, Double.POSITIVE_INFINITY},
                {28614, 24470, 28614, 2703, Double.POSITIVE_INFINITY},
                {156, 300, 156, 300, Double.NEGATIVE_INFINITY},
                {8149, 11077, 8149, 11077, Double.NEGATIVE_INFINITY},
                {472, 470, 472, 116, Double.POSITIVE_INFINITY},
                {22973, 14583, 22973, 90, Double.POSITIVE_INFINITY},
                {6, 7, 6, 0, Double.POSITIVE_INFINITY}
        };
    }

    @Test(dataProvider = "provideCoordinates")
    public void slopePositiveInfiniteTest(final int px, final int py, final int qx, final int qy, final double expected) {
        final Point p = new Point(px, py);
        assertThat(p.slopeTo(new Point(qx, qy)), equalTo(expected));
    }
}
