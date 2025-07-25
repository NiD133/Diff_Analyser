package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BoundedIterator}.
 *
 * @param <E> the type of elements tested by this iterator.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    /** Sample data for testing */
    private final String[] sampleData = {"a", "b", "c", "d", "e", "f", "g"};
    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) sampleData);
    }

    @Test
    void testBoundedIteratorWithinRange() {
        final Iterator<E> iter = new BoundedIterator<>(testList.iterator(), 2, 4);

        assertIteratorElements(iter, "c", "d", "e", "f");
        assertNoMoreElements(iter);
    }

    @Test
    void testEmptyBoundedIterator() {
        final Iterator<E> iter = new BoundedIterator<>(testList.iterator(), 3, 0);
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void testMaxExceedsIteratorSize() {
        final Iterator<E> iter = new BoundedIterator<>(testList.iterator(), 1, 10);

        assertIteratorElements(iter, "b", "c", "d", "e", "f", "g");
        assertNoMoreElements(iter);
    }

    @Test
    void testNegativeMaxThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> new BoundedIterator<>(testList.iterator(), 3, -1));
        assertEquals("Max parameter must not be negative.", exception.getMessage());
    }

    @Test
    void testNegativeOffsetThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> new BoundedIterator<>(testList.iterator(), -1, 4));
        assertEquals("Offset parameter must not be negative.", exception.getMessage());
    }

    @Test
    void testOffsetExceedsIteratorSize() {
        final Iterator<E> iter = new BoundedIterator<>(testList.iterator(), 10, 4);
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void testRemoveCalledTwiceThrowsException() {
        final List<E> testListCopy = new ArrayList<>(testList);
        final Iterator<E> iter = new BoundedIterator<>(testListCopy.iterator(), 1, 5);

        iter.next();
        iter.remove();
        assertThrows(IllegalStateException.class, iter::remove);
    }

    @Test
    void testRemoveFirstElement() {
        final List<E> testListCopy = new ArrayList<>(testList);
        final Iterator<E> iter = new BoundedIterator<>(testListCopy.iterator(), 1, 5);

        iter.next();
        iter.remove();
        assertFalse(testListCopy.contains("b"));

        assertIteratorElements(iter, "c", "d", "e", "f");
        assertNoMoreElements(iter);
    }

    @Test
    void testRemoveLastElement() {
        final List<E> testListCopy = new ArrayList<>(testList);
        final Iterator<E> iter = new BoundedIterator<>(testListCopy.iterator(), 1, 5);

        assertIteratorElements(iter, "b", "c", "d", "e", "f");
        assertNoMoreElements(iter);

        iter.remove();
        assertFalse(testListCopy.contains("f"));
        assertNoMoreElements(iter);
    }

    @Test
    void testRemoveMiddleElement() {
        final List<E> testListCopy = new ArrayList<>(testList);
        final Iterator<E> iter = new BoundedIterator<>(testListCopy.iterator(), 1, 5);

        iter.next();
        iter.next();
        iter.next();
        iter.remove();
        assertFalse(testListCopy.contains("d"));

        assertIteratorElements(iter, "e", "f");
        assertNoMoreElements(iter);
    }

    @Test
    void testRemoveUnsupportedOperation() {
        final Iterator<E> mockIterator = new AbstractIteratorDecorator<E>(testList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        final Iterator<E> iter = new BoundedIterator<>(mockIterator, 1, 5);
        iter.next();
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    void testRemoveWithoutNextThrowsException() {
        final List<E> testListCopy = new ArrayList<>(testList);
        final Iterator<E> iter = new BoundedIterator<>(testListCopy.iterator(), 1, 5);

        IllegalStateException exception = assertThrows(IllegalStateException.class, iter::remove);
        assertEquals("remove() cannot be called before calling next()", exception.getMessage());
    }

    @Test
    void testIteratorSameAsDecorated() {
        final Iterator<E> iter = new BoundedIterator<>(testList.iterator(), 0, testList.size());

        assertIteratorElements(iter, "a", "b", "c", "d", "e", "f", "g");
        assertNoMoreElements(iter);
    }

    private void assertIteratorElements(Iterator<E> iter, String... expectedElements) {
        for (String expected : expectedElements) {
            assertTrue(iter.hasNext());
            assertEquals(expected, iter.next());
        }
    }

    private void assertNoMoreElements(Iterator<E> iter) {
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }
}