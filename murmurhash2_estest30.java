package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the MurmurHash2 class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash32 method correctly computes a hash for a portion of a byte array
     * using a specific seed. This test case verifies the algorithm's output against a known,
     * pre-computed "golden" value.
     */
    @Test
    public void hash32ShouldProduceKnownHashForPortionOfByteArray() {
        // Arrange
        // Input data is an array of six zero-bytes.
        final byte[] dataToHash = new byte[6];
        
        // However, we instruct the method to only process the first byte of the array.
        final int lengthToHash = 1;
        final int seed = 615;
        
        // This is the pre-computed "golden" value for hashing a single zero-byte
        // with the specified seed.
        final int expectedHash = 1161250932;

        // Act
        final int actualHash = MurmurHash2.hash32(dataToHash, lengthToHash, seed);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}