package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that a channel created with the default constructor is backed by an empty byte array.
     */
    @Test
    public void arrayShouldReturnEmptyArrayForDefaultConstructedChannel() {
        // Arrange
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        byte[] expectedArray = new byte[0];

        // Act
        byte[] actualArray = channel.array();

        // Assert
        assertNotNull("The backing array should never be null.", actualArray);
        assertArrayEquals("A new channel created with the default constructor should have an empty byte array.",
                expectedArray, actualArray);
    }
}