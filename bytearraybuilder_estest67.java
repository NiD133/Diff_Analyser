package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link ByteArrayBuilder} class.
 * This class focuses on verifying the behavior of the resetAndGetFirstSegment() method.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling resetAndGetFirstSegment() on a new builder
     * returns the initial buffer segment and resets the builder's size to zero.
     */
    @Test
    public void resetAndGetFirstSegmentShouldReturnInitialBufferAndResetSize() {
        // Arrange: Create a new ByteArrayBuilder.
        // The default initial block size in the source class is 500.
        final int EXPECTED_INITIAL_SEGMENT_SIZE = 500;
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act: Reset the builder and get its first segment.
        byte[] firstSegment = builder.resetAndGetFirstSegment();

        // Assert: Verify the state of the builder and the returned segment.
        assertNotNull("The first segment should not be null", firstSegment);
        
        // The method should return the initial buffer with its default size.
        assertEquals("The first segment should have the default initial size",
                EXPECTED_INITIAL_SEGMENT_SIZE, firstSegment.length);

        // The builder's internal size (number of aggregated bytes) should be 0.
        assertEquals("The builder's size should be reset to 0 after the call",
                0, builder.size());
    }
}