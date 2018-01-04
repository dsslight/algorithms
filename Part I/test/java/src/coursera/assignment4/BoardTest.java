package coursera.assignment4;

import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

public class BoardTest {
    @Test
    public void twinTest() {
        final int[][] blocks = {{1, 0}, {2, 3}};

        final Board board = new Board(blocks);

        final Board twin = board.twin();
        assertThat(twin, not(equalTo(board)));

        for (int i = 0; i < 10; i++) {
            MatcherAssert.assertThat(twin, equalTo(board.twin()));
        }
    }
}
