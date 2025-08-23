package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Unit tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest {

    /**
     * Tests that calling next() on an iterator bounded to a zero-element range
     * throws a NoSuchElementException.
     */
    @Test
    public void nextShouldThrowNoSuchElementExceptionForEmptyBoundedRange() {
        // Arrange: Create a BoundedIterator with a maximum size of 0.
        // This means it should never have a next element, regardless of the
        // underlying iterator's contents.
        final Iterator<String> emptySourceIterator = Collections.emptyIterator();
        final BoundedIterator<String> boundedIterator =
            new BoundedIterator<>(emptySourceIterator, 0L, 0L);

        // Act & Assert: Verify that calling next() throws NoSuchElementException.
        // The lambda expression `boundedIterator::next` is the action to be performed.
        assertThrows(NoSuchElementException.class, boundedIterator::next);
    }
}