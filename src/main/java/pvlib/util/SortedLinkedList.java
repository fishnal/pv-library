package pvlib.util;

import java.lang.reflect.Array;

public class SortedLinkedList<E> implements DataStructure<E> {
    private final static int NOT_AVAILABLE = 0b0;
    private final static int LESS = 0b1;
    private final static int GREATER = 0b11;

    private class Link {
        E value;
        private Link prev;
        private Link next;

        Link(E value, Link prev, Link next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Link start;
    private int size;

    /**
     * Default constructor.
     */
    public SortedLinkedList() { }

    /**
     * Intended to construct a SortedLinkedList from a set of objects that should be
     * of type <code>E</code>. Mainly for use when splitting the list up or cloning it.
     * @param elements - the elements to add to this list
     */
    private SortedLinkedList(Object[] elements) {
        for (Object o : elements) {
            this.add((E) o);
        }
    }

    @Override
    public void add(E element) {
        binaryInsert(element, toLinkArray(), 0, size - 1, NOT_AVAILABLE, -1);
    }

    /**
     * Inserts an element into the linked list by traversing through the list in a binary-search matter. When
     * the end index is less than the start index, then it is assumed that we have reached a good position to insert
     * the element.
     * @param element - the element to insert.
     * @param elements - the elements to base the position of the to-be-inserted-element off.
     * @param start - the current binary start index.
     * @param end - the current binary end index.
     * @param prevCompare - the result of the previously compared elements (should be NOT_AVAILABLE, LESS, or GREATER).
     * @param prevCompareIndex - the index of the previously compared element with the element to be inserted.
     */
    private void binaryInsert(E element, Link[] elements, int start, int end, int prevCompare, int prevCompareIndex) {
        if (end < start) {
            if (prevCompare >= NOT_AVAILABLE && prevCompare <= GREATER) {
                Link insertLink = new Link(element, null, null);

                if (prevCompare == NOT_AVAILABLE) {
                    // should only happen when size is 0
                    this.start = insertLink;
                } else {
                    Link pce = elements[prevCompareIndex];
                    if (prevCompare == GREATER) {
                        // insert after previously compared element
                        insertAfter(insertLink, pce);
                    } else {
                        // insert before previously compared element
                        insertBefore(insertLink, pce);
                    }
                }

                this.size++;
            } else {
                throw new Error("invalid prevCompare value");
            }

            return;
        }

        int middle = (start + end) / 2;
        Link compareWith = elements[middle];

        int compare = ((Comparable<E>) element).compareTo(compareWith.value);
        if (compare == 0) {
            Link insertLink = new Link(element, null, null);
            insertBefore(insertLink, compareWith);
        } else if (compare < 0) {
            binaryInsert(element, elements, start, middle - 1, LESS, middle);
        } else {
            binaryInsert(element, elements, middle + 1, end, GREATER, middle);
        }
    }

    /**
     * Inserts a link after another link.
     * @param toInsert - the link to insert.
     * @param relative - the location relative to the toInsert link.
     */
    private void insertAfter(Link toInsert, Link relative) {
        // if relative's next value is not null, set relative's next's previous value to toInsert
        // set toInsert's next value to relative's next
        // set relative's next value to toInsert
        // set toInsert's previous value to relative

        if (relative.next != null) {
            relative.next.prev = toInsert;
        }

        toInsert.next = relative.next;
        relative.next = toInsert;
        toInsert.prev = relative;
    }

    /**
     * Inserts a link before another link.
     * @param toInsert - the link to insert.
     * @param relative - the location relative to the toInsert link.
     */
    private void insertBefore(Link toInsert, Link relative) {
        // if relative's previous value is not null, set relative's previous's next value to toInsert
        // set toInsert's previous value to relative's previous
        // set relative's previous value to toInsert
        // st toInsert's next value to relative

        if (relative.prev != null) {
            relative.prev.next = toInsert;
        }

        toInsert.prev = relative.prev;
        relative.prev = toInsert;
        toInsert.next = relative;

        if (relative == this.start) {
            this.start = toInsert;
        }
    }

    @Override
    public E get(int index) {
        return getLink(index).value;
    }

    /**
     * Gets the link at the particular index.
     * @param index - the index.
     * @return the link at the given index.
     * @throws IndexOutOfBoundsException if index is less than 0 or greater than or equal to the
     * size of this list.
     */
    private Link getLink(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index + "");
        }

        int i = 0;
        for (Link link = this.start; link != null; link = link.next, i++) {
            if (i == index) {
                return link;
            }
        }

        // null should never be returned
        return null;
    }

    @Override
    public void set(int index, E newValue) {
        Link link = getLink(index);
        E val = link.value;
        if (val == newValue || (val != null && val.equals(newValue))) {
            return;
        }

        remove(index);
        add(newValue);
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index + "");
        }

        Link[] links = toLinkArray();
        unlink(links[index]);
        this.size--;
    }

    @Override
    public boolean remove(E element) {
        int index = indexOf(element);

        if (index < 0) {
            return false;
        }

        remove(index);
        return true;
    }

    /**
     * Effectively removes a link from the list by un-linking all other links from this link.
     * @param ln - the link to effectively remove.
     */
    private void unlink(Link ln) {
        if (ln == this.start) {
            this.start = ln.next;
        }

        if (ln.prev != null) {
            ln.prev.next = ln.next;
        }

        if (ln.next != null) {
            ln.next.prev = ln.prev;
        }
    }

    @Override
    public void swap(int first, int second) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(E element) {
        int index = 0;

        for (Link link = this.start; link != null; link = link.next, index++) {
            if (link.value == element || (link.value != null && link.value.equals(element))) {
                return index;
            }
        }

        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public DataStructure<E> split(int from, int to) {
        int size = to - from;
        if (size < 0) {
            throw new IndexOutOfBoundsException(String.format("from:%d to:%d", from, to));
        }

        Object[] data = new Object[size];

        int i = 0;
        int di = 0;
        for (Link link = this.start; link != null; link = link.next, i++) {
            if (i >= from && i < to) {
                data[di] = link.value;
                di++;
            } else if (i >= to) {
                break;
            }
        }

        return new SortedLinkedList<E>(data);
    }

    /**
     * @return an array of all the links in this list.
     */
    private Link[] toLinkArray() {
        Object arr = Array.newInstance(Link.class, this.size);

        int i = 0;
        for (Link link = this.start; link != null; link = link.next, i++) {
            Array.set(arr, i, link);
        }

        return (Link[]) arr;
    }

    @Override
    public DataStructure<E> clone() {
        Object[] data = new Object[this.size];

        int i = 0;
        for (Link link = this.start; link != null; link = link.next, i++) {
            data[i] = link.value;
        }

        return new SortedLinkedList<E>(data);
    }

    // TODO toString()
}
