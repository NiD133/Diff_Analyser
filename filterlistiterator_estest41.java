package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains a specific test case for the {@link FilterListIterator}.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class FilterListIterator_ESTestTest41 {

    /**
     * Tests that calling the set() method on a FilterListIterator throws an
     * UnsupportedOperationException, as the operation is not supported.
     */
    @Test
    public void setShouldThrowUnsupportedOperationException() {
        // Arrange: Create a FilterListIterator instance.
        // No underlying iterator or predicate is needed because set() is
        // unconditionally unsupported.
        FilterListIterator<String> filterListIterator = new FilterListIterator<>();

        // Act & Assert
        try {
            filterListIterator.set("any-value");
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "FilterListIterator.set(Object) is not supported.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}