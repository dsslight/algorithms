package coursera.assignment2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DequeTest extends QueueCommon {
    private Deque actualDeque;

    @BeforeMethod
    @Override
    protected void before() {
        actualDeque = new Deque();
    }

    @Override
    protected Iterable retrieve() {
        return actualDeque;
    }

    @Override
    protected void addToQueue(final Object item){
        actualDeque.addFirst(item);
    }

    @Override
    protected Object removeFromQueue(){
        return actualDeque.removeFirst();
    }

    @Test
    public void constructorTest() {
        assertThat(actualDeque.size(), equalTo(0));
        assertThat(actualDeque.isEmpty(), is(true));
    }

    @Test
    public void addSingleFirstItemTest() {
        final Object item = new Object();
        actualDeque.addFirst(item);

        assertThat(actualDeque.size(), equalTo(1));
    }

    @Test
    public void addSingleLastItemTest() {
        final Object item = new Object();
        actualDeque.addLast(item);

        assertThat(actualDeque.size(), equalTo(1));
    }

    @Test(dataProvider = "batchSizes")
    public void addFirstByBatchSizeTest(int count) {
        final java.util.Deque expectedDeque = new ArrayDeque();
        for(int i = 1; i <= count; i++) {
            final Object item = new Object();
            actualDeque.addFirst(item);
            expectedDeque.addFirst(item);
            assertThat(actualDeque.size(), equalTo(i));
        }
        final Iterator actualIterator = actualDeque.iterator();
        final Iterator expectedIterator = expectedDeque.iterator();
        for(int j = 0; j < count; j++) {
            assertThat(actualIterator.next(), equalTo(expectedIterator.next()));
        }
        assertThat(actualIterator.hasNext(), is(false));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addFirstRainDay() {
        actualDeque.addFirst(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addLastRainDay() {
        actualDeque.addLast(null);
    }

    @Test
    public void removeSingleFirstItemTest() {
        final Object item = new Object();
        actualDeque.addFirst(item);
        actualDeque.removeFirst();
        //for(int i = 0; i <= count; i--) {
            assertThat(actualDeque.size(), equalTo(0));
      //  }
    }

    @Test
    public void addTwoElementsInFirstAndRemoveFromLastTest() {
        final Object item1 = new Object();
        actualDeque.addFirst(item1);
        final Object item2 = new Object();
        actualDeque.addFirst(item2);
        assertThat(actualDeque.removeLast(), equalTo(item1));
        assertThat(actualDeque.removeLast(), equalTo(item2));

        assertThat(actualDeque.isEmpty(), is(true));
    }

    @Test
    public void addTwoElementsInLstAndRemoveFromFirstTest() {
        final Object item1 = new Object();
        actualDeque.addLast(item1);
        final Object item2 = new Object();
        actualDeque.addLast(item2);
        assertThat(actualDeque.removeFirst(), equalTo(item1));
        assertThat(actualDeque.removeFirst(), equalTo(item2));

        assertThat(actualDeque.isEmpty(), is(true));
    }

    @Test(enabled = true)
    public void removeSingleLastItemTest() {
        final Object item = new Object();
        actualDeque.addLast(item);
        actualDeque.removeLast();
        assertThat(actualDeque.size(), equalTo(0));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void removeLastRainDay() {
        actualDeque.removeLast();
    }

}
