package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64() throws an ArrayIndexOutOfBoundsException when provided with a negative length.
     * The underlying implementation attempts to access an array with a negative index,
     * which is an invalid operation.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hash64ShouldThrowExceptionForNegativeLength() {
        // Arrange
        final byte[] data = new byte[10]; // The data content is irrelevant for this test.
        final int negativeLength = -1;
        final int seed = 0; // The seed value is also irrelevant.

        // Act & Assert
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        MurmurHash2.hash64(data, negativeLength, seed);
    }
}