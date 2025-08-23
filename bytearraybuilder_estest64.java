package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class, focusing on its behavior as an OutputStream.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that writing a single byte to the builder correctly updates the
     * length of the current data segment.
     */
    @Test
    public void writeSingleByteShouldIncrementCurrentSegmentLength() {
        // Arrange: Create a ByteArrayBuilder with a small initial capacity.
        // The exact capacity is not critical for this test.
        ByteArrayBuilder builder = new ByteArrayBuilder(16);
        assertEquals("A new builder should have an empty current segment.",
                0, builder.getCurrentSegmentLength());

        // Act: Write a single byte to the builder.
        // The write(int) method only writes the lowest 8 bits of the integer.
        builder.write(42);

        // Assert: The length of the current segment should now be 1.
        assertEquals("After writing one byte, the segment length should be 1.",
                1, builder.getCurrentSegmentLength());
    }
}