package coursera.assignment2;

import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RandomizedQueueTest extends QueueCommon {
    private RandomizedQueue actualRandomizedQueue;

    @BeforeMethod
    @Override
    protected void before() {
        actualRandomizedQueue = new RandomizedQueue();
    }

    @Override
    protected Iterable retrieve() {
        return actualRandomizedQueue;
    }

    @Override
    protected void addToQueue(final Object item){
        actualRandomizedQueue.enqueue(item);
    }

    @Override
    protected Object removeFromQueue(){
        return actualRandomizedQueue.dequeue();
    }

    @Test(dataProvider = "batchSizes")
    public void enqueueDequeByBatchSizeTest(final int count) {
        final List<Object> expectedElements = new ArrayList();
        for(int i = 1; i <= count; i++) {
            final Object item = new Object();
            addToQueue(item);
            expectedElements.add(item);
            assertThat(actualRandomizedQueue.size(), equalTo(i));
        }

        for(int j = 0; j < count; j++) {
            final Object actualElement = removeFromQueue();
            assertThat(actualElement, isIn(expectedElements));
        }
    }

    @Test(dataProvider = "batchSizes")
    public void enqueueBatchDequeueAllButOne(final int count) {
        for(int i = 1; i <= count; i++) {
            addToQueue(new Object());
            assertThat(actualRandomizedQueue.size(), equalTo(i));
        }

        for(int j = 0; j < count - 1; j++) {
            removeFromQueue();
        }
        assertThat(actualRandomizedQueue.size(), is(1));
        final Object[] items = (Object[]) Whitebox.getInternalState(actualRandomizedQueue, "items");
        assertThat(items.length, is(16));
    }

    @Test(dataProvider = "batchSizes")
    public void enqueueDequeueCorrectnessTest(final int count) {
        for(int i = 1; i <= count / 2; i++) {
            addToQueue(new Object());
        }

        for(int i = 1; i <= count / 2; i++) {
            addToQueue(new Object());
            removeFromQueue();
        }

        assertThat(actualRandomizedQueue.size(), is(count / 2));

        int actualSize = 0;
        while(!actualRandomizedQueue.isEmpty()) {
            assertThat(actualRandomizedQueue.dequeue(), notNullValue());
            actualSize++;
        }
        assertThat(actualSize, is(count / 2));
    }

    @DataProvider
    public Object[][] hugeBatchSizes() {
        return new Object[][] {{Math.pow(2, 10)}, {Math.pow(2, 11)}, {Math.pow(2, 12)}, {Math.pow(2, 13)}, {Math.pow(2, 14)},
                {Math.pow(2, 15)}, {Math.pow(2, 16)}, {Math.pow(2, 17)}, {Math.pow(2, 18)}, {Math.pow(2, 19)},
                {Math.pow(2, 20)}, {Math.pow(2, 21)}, {Math.pow(2, 22)}};
    }

    @Test(dataProvider = "hugeBatchSizes")
    private void enqueueAndIteratorNextConstantTimeVerification(final double count) {
        long totalMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            actualRandomizedQueue.enqueue(new Object());
        }
        System.out.println("for " + count + " items an average enqueue time: " + (System.currentTimeMillis() - totalMillis));

        final Iterator iterator = actualRandomizedQueue.iterator();
        totalMillis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            assertThat(iterator.hasNext(), is(true));
            assertThat(iterator.next(), notNullValue());
        }
        assertThat(iterator.hasNext(), is(false));
        System.out.println("for " + count + " items an average iterator next() time: " + (System.currentTimeMillis() - totalMillis));
        System.out.println();
    }

    @Test(dataProvider = "batchSizes")
    public void iteratorRandomizationTest(final int count) {
        final List<Object> expectedElements = new ArrayList();

        for(int i = 1; i <= count; i++) {
            final Object item = new Object();
            addToQueue(item);
            expectedElements.add(item);
        }

        final Iterator firstIterator = actualRandomizedQueue.iterator();
        final Iterator secondIterator = actualRandomizedQueue.iterator();
        int diffCount = 0;
        for (int i = 0; i < count; i++) {
            assertThat(firstIterator.hasNext(), is(true));
            assertThat(secondIterator.hasNext(), is(true));
            final Object firstObject = firstIterator.next();
            final Object secondObject = secondIterator.next();

            assertThat(firstObject, notNullValue());
            assertThat(secondObject, notNullValue());

            assertThat(Arrays.asList(firstObject, secondObject), hasItem(isIn(expectedElements)));
            if (!firstObject.equals(secondObject)) {
                diffCount++;
            }
        }
        assertThat(firstIterator.hasNext(), is(false));
        assertThat(secondIterator.hasNext(), is(false));

        assertThat(diffCount, greaterThan((int) (count * 0.4)));
    }
}
