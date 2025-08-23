package org.apache.commons.codec.digest;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on handling invalid arguments.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64() throws an ArrayIndexOutOfBoundsException when provided with a negative length.
     * The underlying implementation attempts to calculate an array index based on the length,
     * which results in a negative index and an exception for negative length values.
     */
    @Test
    public void hash64ShouldThrowArrayIndexOutOfBoundsExceptionForNegativeLength() {
        // Arrange: Define the input data and the invalid length.
        final byte[] data = new byte[1];
        final int negativeLength = -1;
        final int seed = 0;

        // Act & Assert: Verify that the expected exception is thrown.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            MurmurHash2.hash64(data, negativeLength, seed);
        });
    }
}