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
    private List<E> sampleList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(sampleList).iterator(), 1, sampleList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        sampleList = Arrays.asList((E[]) sampleData);
    }

    @Test
    void shouldIterateWithinBounds() {
        Iterator<E> iterator = new BoundedIterator<>(sampleList.iterator(), 2, 4);

        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next, "Expected NoSuchElementException.");
    }

    @Test
    void shouldBehaveAsEmptyWhenMaxIsZero() {
        Iterator<E> iterator = new BoundedIterator<>(sampleList.iterator(), 3, 0);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldIterateToEndWhenMaxExceedsSize() {
        Iterator<E> iterator = new BoundedIterator<>(sampleList.iterator(), 1, 10);

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("g", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldThrowExceptionForNegativeMax() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new BoundedIterator<>(sampleList.iterator(), 3, -1));
        assertEquals("Max parameter must not be negative.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNegativeOffset() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            new BoundedIterator<>(sampleList.iterator(), -1, 4));
        assertEquals("Offset parameter must not be negative.", exception.getMessage());
    }

    @Test
    void shouldBehaveAsEmptyWhenOffsetExceedsSize() {
        Iterator<E> iterator = new BoundedIterator<>(sampleList.iterator(), 10, 4);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldThrowExceptionWhenRemoveCalledTwice() {
        List<E> listCopy = new ArrayList<>(sampleList);
        Iterator<E> iterator = new BoundedIterator<>(listCopy.iterator(), 1, 5);

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        iterator.remove();

        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void shouldRemoveFirstElement() {
        List<E> listCopy = new ArrayList<>(sampleList);
        Iterator<E> iterator = new BoundedIterator<>(listCopy.iterator(), 1, 5);

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());

        iterator.remove();
        assertFalse(listCopy.contains("b"));

        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldRemoveLastElement() {
        List<E> listCopy = new ArrayList<>(sampleList);
        Iterator<E> iterator = new BoundedIterator<>(listCopy.iterator(), 1, 5);

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        iterator.remove();
        assertFalse(listCopy.contains("f"));

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldRemoveMiddleElement() {
        List<E> listCopy = new ArrayList<>(sampleList);
        Iterator<E> iterator = new BoundedIterator<>(listCopy.iterator(), 1, 5);

        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());

        iterator.remove();
        assertFalse(listCopy.contains("d"));

        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldThrowExceptionWhenRemoveNotSupported() {
        Iterator<E> mockIterator = new AbstractIteratorDecorator<E>(sampleList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        Iterator<E> iterator = new BoundedIterator<>(mockIterator, 1, 5);
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertNull(exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRemoveCalledBeforeNext() {
        List<E> listCopy = new ArrayList<>(sampleList);
        Iterator<E> iterator = new BoundedIterator<>(listCopy.iterator(), 1, 5);

        IllegalStateException exception = assertThrows(IllegalStateException.class, iterator::remove);
        assertEquals("remove() cannot be called before calling next()", exception.getMessage());
    }

    @Test
    void shouldReturnAllElementsWhenOffsetIsZeroAndMaxIsSize() {
        Iterator<E> iterator = new BoundedIterator<>(sampleList.iterator(), 0, sampleList.size());

        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("c", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("d", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("e", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("f", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("g", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}