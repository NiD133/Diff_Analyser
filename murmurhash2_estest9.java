package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on the 32-bit hash function.
 */
public class MurmurHash2Test {

    /**
     * Tests that hashing a byte array of all zeros produces a consistent, known hash value.
     * This is a "known-answer test" that ensures the hash algorithm's implementation
     * remains stable and correct across changes.
     */
    @Test
    public void testHash32WithAllZeroBytesProducesKnownValue() {
        // Arrange
        final byte[] data = new byte[6]; // An array of all zero bytes
        final int length = data.length;

        // This is the pre-calculated MurmurHash2 32-bit hash for 6 zero-bytes
        // using the hash32(byte[], int) method, which relies on a default seed.
        final int expectedHash = 0x94f36a2b;

        // Act
        final int actualHash = MurmurHash2.hash32(data, length);

        // Assert
        assertEquals("Hashing an all-zero byte array should produce the known, expected hash.",
                expectedHash, actualHash);
    }
}