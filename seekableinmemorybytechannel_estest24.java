package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    @Test
    public void truncateShouldThrowIllegalArgumentExceptionForNegativeSize() {
        // Arrange: Create a channel and define an invalid negative size.
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        final long negativeSize = -1L;

        // Act & Assert: Verify that truncating to a negative size throws an exception.
        try {
            channel.truncate(negativeSize);
            fail("Expected an IllegalArgumentException to be thrown for a negative size.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct.
            final String expectedMessage = "Size must be range [0..2147483647]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}