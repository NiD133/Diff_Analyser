package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Tests that truncate() throws an IllegalArgumentException when the requested
     * size is larger than Integer.MAX_VALUE. The channel's size is limited by the
     * maximum size of a byte array.
     */
    @Test
    public void truncateShouldThrowExceptionForSizeGreaterThanMaxInt() {
        // Arrange
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        final long sizeTooLarge = (long) Integer.MAX_VALUE + 1;

        // Act & Assert
        try {
            channel.truncate(sizeTooLarge);
            fail("Expected an IllegalArgumentException to be thrown for a size greater than Integer.MAX_VALUE");
        } catch (final IllegalArgumentException e) {
            // Verify the exception message for correctness and clarity.
            final String expectedMessage = "Size must be range [0.." + Integer.MAX_VALUE + "]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}