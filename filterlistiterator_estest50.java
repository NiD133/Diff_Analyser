package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link FilterListIterator} to ensure that unsupported operations behave as expected.
 */
public class FilterListIteratorTest {

    /**
     * Tests that calling the remove() method throws an UnsupportedOperationException,
     * as the operation is not supported by FilterListIterator.
     */
    @Test
    public void removeShouldThrowUnsupportedOperationException() {
        // Arrange: Create a FilterListIterator instance.
        // Its internal state (e.g., underlying iterator) is not relevant for this test,
        // as remove() is designed to always fail.
        final FilterListIterator<Object> iterator = new FilterListIterator<>();

        // Act & Assert
        try {
            iterator.remove();
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception with the expected message is thrown.
            // The message is part of the method's contract.
            assertEquals("FilterListIterator.remove() is not supported.", e.getMessage());
        }
    }
}