package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class was improved to focus on a specific behavior of the ObjectGraphIterator.
 * The original test was auto-generated and contained significant irrelevant code.
 */
public class ObjectGraphIterator_ESTestTest21 { // Original class name kept for context

    /**
     * Tests that calling remove() on a nested ObjectGraphIterator throws an IllegalStateException
     * if the element being removed originated as a root object of the inner iterator,
     * which does not support removal.
     */
    @Test
    public void removeThrowsExceptionWhenElementIsFromRootOfNestedIterator() {
        // Arrange: Set up a nested iterator scenario.
        // 1. Create an inner iterator with a single root object. The remove() operation
        //    on this iterator will fail because the element is not from a sub-iterator.
        final Integer rootElement = 566;
        final ObjectGraphIterator<Integer> innerIterator = new ObjectGraphIterator<>(rootElement);

        // 2. Create the main iterator that wraps the inner one.
        final ObjectGraphIterator<Integer> graphIterator = new ObjectGraphIterator<>(innerIterator);

        // 3. Advance the iterator to prepare for the remove() call.
        graphIterator.next(); // This retrieves the rootElement from the innerIterator.

        // Act & Assert: Attempting to remove the element should fail.
        try {
            graphIterator.remove();
            fail("Expected an IllegalStateException to be thrown.");
        } catch (final IllegalStateException e) {
            // The exception is expected because the inner iterator's remove() was called
            // on an element that was its root, not from an underlying iterator.
            assertEquals("Iterator remove() cannot be called at this time", e.getMessage());
        }
    }
}