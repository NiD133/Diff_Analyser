package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the array() method returns the exact same byte array instance
     * that was provided to the constructor.
     */
    @Test
    public void arrayShouldReturnTheSameInstanceProvidedInConstructor() {
        // Arrange: Create a distinct byte array and use it to initialize the channel.
        // Using non-zero, distinct values makes the test more robust than using an all-zero array.
        final byte[] inputData = { 10, 20, 30, 40, 50 };
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(inputData);

        // Act: Retrieve the backing array from the channel.
        final byte[] resultData = channel.array();

        // Assert: The returned array should be the same instance as the one passed to the constructor,
        // not just an array with equal content.
        assertSame("The array() method should return the original backing array instance.", inputData, resultData);
    }
}