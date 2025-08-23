package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ObjectGraphIterator}.
 * This test focuses on the behavior of the remove() method when iterators are nested.
 */
public class ObjectGraphIterator_ESTestTest12 {

    /**
     * Tests that calling remove() on a wrapped iterator that was advanced
     * indirectly by an outer iterator throws an IllegalStateException.
     *
     * The remove() method is only valid on the specific iterator instance
     * whose next() method was most recently called.
     */
    @Test
    public void removeOnWrappedIteratorAdvancedIndirectlyShouldThrowException() {
        // --- Arrange ---
        // Create a chain of three iterators: topIterator -> middleIterator -> baseIterator.
        // The test will advance the entire chain via the top iterator and then
        // attempt to call remove() on the middle one.

        // 1. A base iterator containing a single element.
        final Integer rootElement = 2;
        final ObjectGraphIterator<Integer> baseIterator =
                new ObjectGraphIterator<>(rootElement, (Transformer<? super Integer, ? extends Integer>) null);

        // Manually prime the base iterator's state by calling the protected findNext().
        // This simulates the internal state after a hasNext() call would have occurred.
        baseIterator.findNext(rootElement);

        // 2. A middle iterator that wraps the base iterator.
        final ObjectGraphIterator<Integer> middleIterator = new ObjectGraphIterator<>(baseIterator);

        // 3. A top-level iterator that wraps the middle iterator.
        final ObjectGraphIterator<Object> topIterator = new ObjectGraphIterator<>(middleIterator);

        // --- Act ---
        // Advance the entire iterator chain by calling next() on the top-level iterator.
        // This propagates the next() call down to the baseIterator.
        topIterator.next();

        // --- Assert ---
        // Attempting to call remove() on the middle iterator should fail. The remove()
        // operation is only valid on the iterator whose next() was *directly* invoked.
        // In this chain, only the topIterator's state would permit a remove() call.
        try {
            middleIterator.remove();
            fail("Expected an IllegalStateException because remove() was called on an indirectly advanced iterator.");
        } catch (final IllegalStateException e) {
            // Verify the exception message is correct.
            assertEquals("Iterator remove() cannot be called at this time", e.getMessage());
        }
    }
}