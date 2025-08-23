package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the reset() method correctly clears any existing content
     * in the builder and sets its size back to zero.
     */
    @Test
    public void reset_onNonEmptyBuilder_shouldClearContentAndSetSizeToZero() {
        // Arrange: Create a builder and add some data to make it non-empty.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(42);
        assertTrue("Pre-condition failed: Builder should not be empty after appending data.", builder.size() > 0);

        // Act: Reset the builder.
        builder.reset();

        // Assert: The size should be 0 after the reset.
        assertEquals("The builder's size should be 0 after being reset.", 0, builder.size());
    }
}