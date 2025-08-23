package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Verifies that calling {@link ByteArrayBuilder#appendFourBytes(int)}
     * increases the builder's size by exactly 4 bytes.
     */
    @Test
    public void appendFourBytes_shouldIncreaseSizeByFour() {
        // Arrange: Create an empty ByteArrayBuilder.
        ByteArrayBuilder builder = new ByteArrayBuilder();
        assertEquals("A new builder should be empty.", 0, builder.size());

        // Act: Append a 4-byte integer to the builder.
        // The specific value (-35) is arbitrary; any integer would suffice.
        builder.appendFourBytes(-35);

        // Assert: The size of the builder should now be 4.
        final int expectedSize = 4;
        assertEquals("The size should be 4 after appending a 4-byte integer.",
                expectedSize, builder.size());
    }
}