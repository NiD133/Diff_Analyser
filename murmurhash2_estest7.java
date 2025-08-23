package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the 32-bit hash of an empty byte array with a seed of 0
     * correctly returns 0. This verifies a fundamental edge case of the
     * hashing algorithm.
     */
    @Test
    public void hash32ShouldReturnZeroForEmptyDataAndZeroSeed() {
        // Arrange: Define the inputs for the test case
        final byte[] emptyData = new byte[0];
        final int length = 0;
        final int seed = 0;
        final int expectedHash = 0;

        // Act: Call the method under test
        final int actualHash = MurmurHash2.hash32(emptyData, length, seed);

        // Assert: Verify the result is as expected
        assertEquals(expectedHash, actualHash);
    }
}