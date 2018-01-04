package coursera.assignment2;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Element<Item> top;
    private Element<Item> last;
    private int size = 0;

    private static final class Element<Item> {
        private final Item item;
        private Element<Item> linkForward, linkBackward;

        private Element(final Item task, final Element<Item> linkForward, final Element<Item> linkBackward) {
            this.item = task;
            this.linkForward = linkForward;
            this.linkBackward = linkBackward;

            if (linkForward != null) {
                this.linkForward.linkBackward = this;
            }
            if (linkBackward != null) {
                this.linkBackward.linkForward = this;
            }
        }

        public Item getItem() {
            return item;
        }

        public Element<Item> getLinkForward() {
            return linkForward;
        }

        public Element<Item> getLinkBackward() {
            return linkBackward;
        }

        public static <Item> Element<Item> makeFirstElement(final Item task) {
            return makeElement(task, null, null);
        }

        public static <Item> Element<Item> makeFirstElement(final Item task, final Element<Item> linkForward) {
            return makeElement(task, linkForward, null);
        }

        public static <Item> Element<Item> makeElement(final Item task, final Element<Item> linkForward, final Element<Item> linkBackward) {
            return new Element<>(task, linkForward, linkBackward);
        }

        /**
         * @param task
         * @param linkBackward please keep in mind that method update internal state - linkBackward.linkForward to last element
         * @return last element
         */
        public static <Item> Element<Item> makeLastElement(final Item task, final Element<Item> linkBackward) {
            final Element<Item> last = makeElement(task, null, linkBackward);
            linkBackward.linkForward = last;
            return last;
        }
    }

    private static final class LinkedIterator<Item> implements Iterator<Item> {
        private Element<Item> current;

        public LinkedIterator(final Element<Item> top) {
            this.current = top;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                try {
                    return current.getItem();
                } finally {
                    current = current.getLinkForward();
                }
            } else {
                throw new NoSuchElementException("no next element");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return ("class = " + getClass().getSimpleName() + "; [ next element = " + current + "]");

        }
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
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

    // add the item to the front
    public void addFirst(final Item item) {
        checkNotNull(item);
        if (top == null && last == null) {
            top = Element.makeFirstElement(item);
            last = top;
        } else {
            top = Element.makeFirstElement(item, top);
        }
        size++;
    }

    // add the item to the end
    public void addLast(final Item item) {
        checkNotNull(item);
        if (top == null && last == null) {
            top = Element.makeFirstElement(item);
            last = top;
        } else {
            last = Element.makeLastElement(item, last);
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkNotEmpty();
        final Item item = top.getItem();
        top = top.linkForward;
        if (top != null) {
            top.linkBackward = null;
        } else {
            last = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        checkNotEmpty();
        final Item item = last.getItem();
        last = last.linkBackward;
        if (last != null) {
            last.linkForward = null;
        } else {
            top = null;
        }
        size--;
        return item;

    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new LinkedIterator<>(top);
    }
}