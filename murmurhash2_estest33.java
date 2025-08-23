package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the MurmurHash2 class.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64 produces a known, pre-calculated hash for a zero-length input.
     * The content of the input byte array should be ignored when the provided length is zero.
     */
    @Test
    public void testHash64WithZeroLength() {
        // Arrange
        // A non-empty byte array is used to ensure that the method correctly
        // processes only the specified length (0) and ignores the rest of the array.
        final byte[] data = "some-irrelevant-data".getBytes();
        final int length = 0;
        final int seed = -66;

        // This is the pre-calculated MurmurHash2 value for a zero-length input
        // with a seed of -66.
        final long expectedHash = 2692789288766115L;

        // Act
        final long actualHash = MurmurHash2.hash64(data, length, seed);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}