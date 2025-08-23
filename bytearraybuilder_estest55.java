package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its state management methods.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#reset()} on a non-empty builder
     * correctly clears its contents and resets the current segment's length to zero.
     */
    @Test(timeout = 4000)
    public void testResetOnNonEmptyBuilderResetsSegmentLength() {
        // Arrange: Create a builder and add some data to it, making it non-empty.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.appendTwoBytes(1234);

        // Sanity check to ensure the builder has content before the reset.
        assertTrue("Precondition failed: Builder should have content before reset.",
                builder.getCurrentSegmentLength() > 0);

        // Act: Call the method under test.
        builder.reset();

        // Assert: The current segment length should be 0 after the reset.
        assertEquals("After reset, the current segment length should be 0.",
                0, builder.getCurrentSegmentLength());
    }
}