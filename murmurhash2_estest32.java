package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for exceptional behavior in the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that {@link MurmurHash2#hash64(byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} if the provided length is greater
     * than the actual length of the byte array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hash64ShouldThrowExceptionWhenLengthExceedsArrayBounds() {
        // Arrange: Define an empty byte array and a length that is out of bounds.
        final byte[] emptyData = new byte[0];
        final int lengthGreaterThanDataSize = 1;

        // Act: Attempt to hash the array with the invalid length.
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        MurmurHash2.hash64(emptyData, lengthGreaterThanDataSize);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}