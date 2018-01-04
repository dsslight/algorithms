package coursera.assignment2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Olena on 12/10/2017.
 */
public abstract class QueueCommon {

    @BeforeMethod
    protected abstract void before();

    protected abstract Iterable retrieve();

    protected abstract void addToQueue(final Object item);

    protected abstract Object removeFromQueue();

    @DataProvider
    public Object[][] batchSizes() {
        return new Object[][] {{8}, {16}, {32}};
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void removeFirstRainDay() {
        removeFromQueue();
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void emptyDequeIteratorRainDay() {
        retrieve().iterator().next();
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void iteratorNextOnElementRunOutRainDay() {
        final Object item = new Object();
        addToQueue(item);
        final Iterator actualIterator = retrieve().iterator();
        for(int i = 0; i <= 1; i++){
            actualIterator.next();
        }
    }

}
