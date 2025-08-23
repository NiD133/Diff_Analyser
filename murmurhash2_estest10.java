package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that hashing a null byte array with a length of 0 using the
     * hash32 method with its default seed produces a consistent, known hash value.
     * This is an important edge case to ensure predictable behavior for empty or
     * null inputs.
     */
    @Test
    public void testHash32WithNullDataAndZeroLength() {
        // Arrange: Define the input data and the expected result.
        final byte[] data = null;
        final int length = 0;
        // This is the known hash value for null/empty data with the default seed.
        final int expectedHash = 275646681;

        // Act: Call the method under test.
        final int actualHash = MurmurHash2.hash32(data, length);

        // Assert: Verify the result.
        assertEquals("Hashing null data with zero length should produce the expected default hash.",
                expectedHash, actualHash);
    }
}