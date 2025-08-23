package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that appending an integer correctly adds a single byte
     * and updates the builder's size accordingly.
     */
    @Test
    public void append_whenAddingInteger_shouldAddOneByteAndIncreaseSize() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        // The append(int) method truncates the integer to its lowest 8 bits.
        // For example, 8000 (0x1F40) becomes 64 (0x40).
        int valueToAppend = 8000;

        // Act
        builder.append(valueToAppend);

        // Assert
        assertEquals("Total size should be 1 after appending one byte.", 1, builder.size());
        assertEquals("Current segment length should also be 1.", 1, builder.getCurrentSegmentLength());
    }
}