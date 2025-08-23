package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash32 method correctly processes only the specified number of bytes
     * from the start of the input array, rather than the entire array.
     */
    @Test
    public void testHash32ShouldRespectLengthParameter() {
        // Arrange: Set up the input data, parameters, and expected outcome.
        // The data array is intentionally made longer than the length to be hashed
        // to verify that the method correctly uses only the specified subsection.
        final byte[] data = {18, 0, 99, 98, 97, 96};
        final int lengthToHash = 2;
        final int seed = -3970;

        // This is the pre-calculated expected hash for the first 2 bytes of `data`
        // ([18, 0]) with the given seed.
        final int expectedHash = -1628438012;

        // Act: Call the method under test.
        final int actualHash = MurmurHash2.hash32(data, lengthToHash, seed);

        // Assert: Verify that the calculated hash matches the expected value.
        assertEquals(expectedHash, actualHash);
    }
}