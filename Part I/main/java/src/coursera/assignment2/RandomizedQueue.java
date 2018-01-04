package coursera.assignment2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int minArraySize;
    private double loadFactor;
    private int size = 0;
    private Item[] items;

    private static final class ArrayDequeueIterator<Item> implements Iterator<Item> {
        private Deque<Item>[] items;
        private double loadFactor;
        private int rowCount;
        private int size;

        public ArrayDequeueIterator(final RandomizedQueue<Item> randomizedQueue) {
            final int pow2 = (int) (Math.log(randomizedQueue.size()) / Math.log(2));
            this.rowCount = (int) Math.pow(2, pow2 * 0.75);
            this.items = (Deque<Item>[]) new Deque[rowCount];
            this.loadFactor = randomizedQueue.loadFactor;
            for (int i = 0; i < randomizedQueue.items.length; i++) {
                if (randomizedQueue.items[i] != null) {
                    final int dequeIndex = StdRandom.uniform(rowCount);
                    if (this.items[dequeIndex] == null) {
                        this.items[dequeIndex] = new Deque<>();
                    }
                    this.items[dequeIndex].addLast(randomizedQueue.items[i]);
                }
            }
            this.size = randomizedQueue.size();
        }

        @Override
        public boolean hasNext() {
            return size != 0;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                int uniform;
                do {
                    uniform = StdRandom.uniform(items.length);
                } while (items[uniform] == null);
                size--;
                final Item item = items[uniform].removeFirst();
                if (items[uniform].isEmpty()) {
                    items[uniform] = null;
                    rowCount--;
                    if (rowCount < items.length * this.loadFactor) {
                        final Deque<Item>[] tmpItems = (Deque<Item>[]) new Deque[rowCount];
                        for (int i = 0, k = 0; i < items.length; i++) {
                            if (items[i] != null) {
                                tmpItems[k++] = items[i];
                            }
                        }
                        items = tmpItems;
                    }
                }
                return item;
            } else {
                throw new NoSuchElementException("no next elements");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        this(16, 0.75d);
    }

    // construct an empty randomized queue
    private RandomizedQueue(final int minArraySize, final double loadFactor) {
        this.minArraySize = minArraySize;
        this.loadFactor = loadFactor;
        this.items = (Item[]) new Object[minArraySize];
    }

    private static <Item> Item generateRandomItem(final Item[] items) {
        return generateRandomItem(items, true);
    }

    private static <Item> Item generateRandomItem(final Item[] items, final boolean isRemove) {
        int uniform;
        do {
            uniform = StdRandom.uniform(items.length);
        } while (items[uniform] == null);

        final Item item = items[uniform];
        if (isRemove) {
            items[uniform] = null;
        }
        return item;
    }

    private static void checkNotNull(final Object item) {
        if (item == null) {
            throw new IllegalArgumentException("argument can't be null");
        }
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue is empty");
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(final Item item) {
        checkNotNull(item);
        if (size() > items.length * loadFactor) {
            items = Arrays.copyOf(items, items.length * 2);
        }
        int uniform;
        do {
            uniform = StdRandom.uniform(items.length);
        } while (items[uniform] != null);
        items[uniform] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        checkNotEmpty();
        final Item item = generateRandomItem(items);
        size--;
        if (items.length > minArraySize && size() < items.length * (1 - loadFactor)) {
            final Item[] newItems = (Item[]) new Object[items.length / 2];
            for (int i = 0, j = 0; i < items.length; i++) {
                if (items[i] != null) {
                    newItems[j++] = items[i];
                }
            }
            items = newItems;
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkNotEmpty();
        return generateRandomItem(items, false);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayDequeueIterator<>(this);
    }
}