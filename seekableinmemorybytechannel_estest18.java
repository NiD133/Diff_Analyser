package org.apache.commons.compress.utils;

import org.junit.Test;

/**
 * Test suite for the SeekableInMemoryByteChannel class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that the constructor throws a NegativeArraySizeException
     * when initialized with a negative size. The underlying byte array
     * cannot be allocated with a negative capacity.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void constructorShouldThrowExceptionForNegativeSize() {
        // Act: Attempt to create a channel with a negative initial size.
        // Assert: Expect a NegativeArraySizeException to be thrown.
        new SeekableInMemoryByteChannel(-1);
    }
}