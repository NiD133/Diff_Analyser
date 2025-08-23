package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64 returns 0 when the specified length of the input data is zero,
     * regardless of the actual content of the byte array.
     */
    @Test
    public void hash64ShouldReturnZeroForZeroLengthInput() {
        // Arrange
        // The content of the data array is irrelevant when the length to hash is zero.
        final byte[] data = new byte[] { 0x1, 0x2, 0x3, 0x4 };
        final int lengthToHash = 0;
        final int seed = 0; // A zero seed is used for this specific test case.
        final long expectedHash = 0L;

        // Act
        final long actualHash = MurmurHash2.hash64(data, lengthToHash, seed);

        // Assert
        assertEquals("Hashing zero bytes should result in a hash of 0", expectedHash, actualHash);
    }
}