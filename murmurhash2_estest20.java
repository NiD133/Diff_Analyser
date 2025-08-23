package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash64 method produces a consistent, known hash value for a specific
     * input. This acts as a regression test to ensure the algorithm's implementation
     * does not change unexpectedly.
     *
     * The test uses an array of zero-bytes but only hashes a portion of it, verifying
     * that the 'length' parameter is respected.
     */
    @Test
    public void hash64ShouldProduceKnownValueForPartialZeroByteArray() {
        // Arrange
        // The expected hash value was pre-calculated from a reference implementation.
        final long expectedHash = -3113210640657759650L;
        final int seed = 56;

        // Input data is an array of six zero-bytes.
        final byte[] data = new byte[6];
        // We will only hash the first 5 bytes to ensure the length parameter is handled correctly.
        final int lengthToHash = 5;

        // Act
        final long actualHash = MurmurHash2.hash64(data, lengthToHash, seed);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}