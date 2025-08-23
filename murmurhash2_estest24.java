package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

// The test class name and inheritance are kept to match the original structure.
public class MurmurHash2_ESTestTest24 extends MurmurHash2_ESTest_scaffolding {

    /**
     * Tests that the 64-bit MurmurHash2 algorithm produces a known, consistent hash
     * for a single-byte slice of a zero-filled array with a custom seed.
     *
     * This test case validates the hash function's behavior on a minimal, non-empty
     * input segment.
     */
    @Test(timeout = 4000)
    public void testHash64WithSingleByteAndCustomSeed() {
        // Arrange: Define the input data, hashing parameters, and the expected result.
        // The input is an array of zeros, but we only hash the first byte.
        final byte[] data = new byte[5]; // All elements are initialized to 0
        final int lengthToHash = 1;
        final int seed = 1;

        // This is the known hash value for a single zero byte with a seed of 1.
        final long expectedHash = -5720937396023583481L;

        // Act: Compute the hash of the specified portion of the byte array.
        final long actualHash = MurmurHash2.hash64(data, lengthToHash, seed);

        // Assert: Verify that the computed hash matches the expected value.
        assertEquals(expectedHash, actualHash);
    }
}