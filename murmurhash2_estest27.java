package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the exception-handling behavior of the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that {@link MurmurHash2#hash64(byte[], int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided 'length'
     * parameter exceeds the bounds of the input data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hash64ShouldThrowArrayIndexOutOfBoundsExceptionWhenLengthIsTooLarge() {
        // Arrange: Create a small byte array and define a length that is clearly out of bounds.
        final byte[] data = new byte[8];
        final int lengthGreaterThanDataSize = 100; // Any value > data.length will trigger the exception.
        final int arbitrarySeed = 0;               // The seed value is not relevant for this test.

        // Act & Assert: Call the method with the invalid length.
        // The @Test(expected=...) annotation asserts that an ArrayIndexOutOfBoundsException is thrown.
        MurmurHash2.hash64(data, lengthGreaterThanDataSize, arbitrarySeed);
    }
}