package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 * This class focuses on specific edge cases and contract adherence.
 */
public class ObjectGraphIterator_ESTestTest16 {

    /**
     * Tests that calling remove() before a call to next() throws an IllegalStateException.
     * This behavior is mandated by the java.util.Iterator interface contract.
     */
    @Test
    public void removeShouldThrowIllegalStateExceptionWhenCalledBeforeNext() {
        // Arrange: Create an ObjectGraphIterator with a non-empty root iterator.
        final List<Integer> sourceList = Collections.singletonList(42);
        final Iterator<Integer> rootIterator = sourceList.iterator();
        final ObjectGraphIterator<Object> graphIterator = new ObjectGraphIterator<>(rootIterator);

        // Act & Assert: Attempting to call remove() before next() must throw an exception.
        try {
            graphIterator.remove();
            fail("Expected an IllegalStateException to be thrown.");
        } catch (final IllegalStateException e) {
            // Verify the exception message to ensure the failure is for the correct reason.
            assertEquals("Iterator remove() cannot be called at this time", e.getMessage());
        }
    }
}