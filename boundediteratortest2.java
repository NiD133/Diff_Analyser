package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for BoundedIterator.
 * <p>
 * This class inherits from AbstractIteratorTest to ensure the iterator
 * fulfills the standard Iterator contract.
 * </p>
 * @param <E> the type of element in the iterator.
 */
@DisplayName("BoundedIterator Test")
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private final String[] testArray = {"a", "b", "c", "d", "e", "f", "g"};

    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        // A BoundedIterator over an empty list is always empty.
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // Creates a BoundedIterator that skips the first element of the list.
        final long offset = 1;
        // The max number of elements is the total size minus the number of skipped elements.
        final long max = testList.size() - offset;
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), offset, max);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) testArray);
    }

    @Test
    @DisplayName("Iterator should be empty when max is zero")
    void iteratorShouldBeEmptyWhenMaxIsZero() {
        // Arrange: Create an iterator over a non-empty list.
        final Iterator<E> underlyingIterator = testList.iterator();
        
        // Act: Create a BoundedIterator with max set to 0.
        // The offset is non-zero to ensure it's the 'max' parameter that makes the iterator empty.
        final long offset = 3;
        final long max = 0;
        final Iterator<E> boundedIterator = new BoundedIterator<>(underlyingIterator, offset, max);

        // Assert: The iterator should be immediately exhausted.
        assertFalse(boundedIterator.hasNext(), "Iterator should be empty when max is 0.");
        assertThrows(NoSuchElementException.class, boundedIterator::next,
                "next() should throw NoSuchElementException for an exhausted iterator.");
    }
}