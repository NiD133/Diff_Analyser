package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#appendTwoBytes(int)}
     * correctly appends two bytes, increasing the builder's size by 2.
     */
    @Test
    public void appendTwoBytesShouldIncreaseSizeByTwo() {
        // Arrange: Create an empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("A new builder should have an initial size of 0.", 0, builder.size());

        // Act: Append a two-byte value. The specific integer is not critical for this test.
        builder.appendTwoBytes(5000);

        // Assert: The size should now be exactly 2.
        assertEquals("The size should be 2 after appending two bytes.", 2, builder.size());
    }
}