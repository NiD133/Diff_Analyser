package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on its size management.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that the builder's size is correctly calculated after appending
     * a single byte and a four-byte integer.
     */
    @Test
    public void shouldReportCorrectSizeAfterAppendingBytes() {
        // Arrange: Create a ByteArrayBuilder. The initial capacity is not critical
        // for this test's logic, so we can use any small number.
        ByteArrayBuilder builder = new ByteArrayBuilder(4);

        // Act: Append one byte and then four bytes to the builder.
        builder.append(123); // Appends 1 byte
        builder.appendFourBytes(456); // Appends 4 bytes

        // Assert: The total size should be the sum of the appended bytes (1 + 4 = 5).
        int expectedSize = 5;
        assertEquals(expectedSize, builder.size());
    }
}