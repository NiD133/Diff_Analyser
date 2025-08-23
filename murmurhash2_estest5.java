package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on the 64-bit hash function.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash64 method produces a known, pre-calculated hash value
     * for a simple single-byte input. This acts as a basic sanity check and
     * regression test for the hashing algorithm.
     */
    @Test
    public void hash64ShouldProduceKnownHashForSingleByteInput() {
        // Arrange: Define the input data and the expected hash result.
        // The expected hash is a "golden value" calculated beforehand to ensure
        // the implementation remains consistent.
        final byte[] dataToHash = {(byte) 24};
        final int dataLength = 1;
        final long expectedHash = 24027485454243747L;

        // Act: Compute the hash of the input data.
        final long actualHash = MurmurHash2.hash64(dataToHash, dataLength);

        // Assert: Verify that the computed hash matches the expected value.
        assertEquals(expectedHash, actualHash);
    }
}