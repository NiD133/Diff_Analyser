package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling remove() on an inner iterator of a nested structure
     * throws an IllegalStateException if its next() method has not been called directly.
     *
     * The state required for a remove() operation is managed per-iterator. Advancing
     * an outer iterator, which internally calls next() on an inner one, does not
     * prepare that inner iterator for a subsequent remove() call.
     */
    @Test
    public void testRemoveOnInnerIteratorAfterOuterNextThrowsException() {
        // Arrange: Create a nested structure of ObjectGraphIterators.
        // The structure is: outerIterator -> middleIterator -> baseIterator -> (rootElement = 2)
        final Integer rootElement = 2;

        // The base iterator contains the single root element. A null transformer is used,
        // so the iterator will simply yield the root element itself.
        final ObjectGraphIterator<Integer> baseIterator = new ObjectGraphIterator<>(rootElement, null);

        // The middle iterator wraps the base iterator.
        final ObjectGraphIterator<Integer> middleIterator = new ObjectGraphIterator<>(baseIterator);

        // The outer iterator wraps the middle iterator.
        final ObjectGraphIterator<Object> outerIterator = new ObjectGraphIterator<>(middleIterator);

        // Act: Advance the outermost iterator. This implicitly consumes the element
        // from the inner iterators to produce its result.
        outerIterator.next();

        // Assert: Calling remove() on the middle iterator should fail.
        // The middle iterator's next() method was not called directly by the test,
        // so its internal state does not permit a remove() operation.
        try {
            middleIterator.remove();
            fail("Expected IllegalStateException was not thrown.");
        } catch (final IllegalStateException e) {
            // Verify the exception message for correctness.
            assertEquals("Iterator remove() cannot be called at this time", e.getMessage());
        }
    }
}