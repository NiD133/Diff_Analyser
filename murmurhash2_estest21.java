package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that {@link MurmurHash2#hash64(byte[], int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when provided with a negative length.
     * The hashing algorithm should not attempt to process a negative number of bytes,
     * and a negative length is an invalid argument.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hash64ShouldThrowExceptionForNegativeLength() {
        // Arrange: Define the input data and invalid parameters.
        // The actual content and size of the data array do not matter for this test,
        // as the validation for the length parameter should fail before data access.
        final byte[] data = new byte[8];
        final int negativeLength = -14;
        final int seed = 110; // The seed value is arbitrary for this test.

        // Act: Call the method with a negative length.
        // This action is expected to throw an ArrayIndexOutOfBoundsException.
        MurmurHash2.hash64(data, negativeLength, seed);

        // Assert: The test succeeds if the expected exception is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}