package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Unit tests for the {@link SeekableInMemoryByteChannel} class, focusing on constructor behavior.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the constructor throws a NullPointerException when initialized with a null byte array.
     * This is the expected behavior as the channel cannot be backed by a null array.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullByteArrayShouldThrowNullPointerException() {
        // Attempt to create an instance with a null byte array, which should fail.
        new SeekableInMemoryByteChannel(null);
    }
}