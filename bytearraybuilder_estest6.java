package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling {@link ByteArrayBuilder#appendTwoBytes(int)} increases the
     * current segment's length by exactly 2.
     */
    @Test
    public void appendTwoBytes_shouldIncreaseSegmentLengthByTwo() {
        // Arrange: Create a new, empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("Initial segment length should be 0.", 0, builder.getCurrentSegmentLength());

        // Act: Append a two-byte integer value.
        // The specific value (-2767) is arbitrary; any integer will suffice.
        builder.appendTwoBytes(-2767);

        // Assert: Verify that the current segment length is now 2.
        int expectedLength = 2;
        assertEquals("The segment length should be 2 after appending two bytes.",
                expectedLength, builder.getCurrentSegmentLength());
    }
}