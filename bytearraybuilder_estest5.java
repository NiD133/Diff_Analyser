package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#appendThreeBytes(int)} increases the builder's
     * size by exactly 3. This test also implicitly checks the buffer's resizing logic
     * by starting with an initial capacity smaller than the number of bytes being appended.
     */
    @Test
    public void appendThreeBytes_shouldIncreaseSizeByThree() {
        // Arrange: Create a builder with an initial capacity of 2 bytes, which is
        // intentionally smaller than the 3 bytes we will append.
        ByteArrayBuilder builder = new ByteArrayBuilder(2);
        assertEquals("A new builder should have an initial size of 0.", 0, builder.size());

        // Act: Append a 24-bit integer, which adds 3 bytes to the builder.
        // The specific value (2341) is arbitrary.
        builder.appendThreeBytes(2341);

        // Assert: Verify that the final size is 3.
        assertEquals("The size should be 3 after appending three bytes.", 3, builder.size());
    }
}