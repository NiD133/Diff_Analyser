package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the size() method returns the initial capacity
     * when the channel is created with a specific size.
     */
    @Test
    public void shouldReturnInitialCapacityForSize() {
        // Arrange
        final int expectedSize = 1;
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(expectedSize);

        // Act
        final long actualSize = channel.size();

        // Assert
        assertEquals(expectedSize, actualSize);
    }
}