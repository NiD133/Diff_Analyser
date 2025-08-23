package org.apache.commons.collections4.iterators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link FilterListIterator} class, focusing on its modification methods.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that the add() method is unsupported and throws an UnsupportedOperationException.
     * The FilterListIterator is a read-only decorator for an iterator and should not
     * allow adding elements.
     */
    @Test
    public void addShouldThrowUnsupportedOperationException() {
        // Given: A default FilterListIterator instance.
        // No underlying iterator or predicate is needed to test this behavior.
        final FilterListIterator<Object> filterIterator = new FilterListIterator<>();

        // When & Then: Calling add() should throw an exception.
        try {
            filterIterator.add("any element");
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (final UnsupportedOperationException e) {
            // Verify the exception message to ensure it's the one we expect.
            assertEquals("FilterListIterator.add(Object) is not supported.", e.getMessage());
        }
    }
}