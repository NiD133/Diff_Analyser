package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link XXHash32} class.
 */
public class XXHash32Test {

    /**
     * Tests that the hash is calculated correctly when the input data is larger
     * than the internal 16-byte buffer, requiring multiple processing steps.
     */
    @Test
    public void testUpdateWithDataLargerThanInternalBuffer() {
        // Arrange: Set up the hasher and the input data.
        // The XXHash32 algorithm processes data in 16-byte chunks.
        // We use 24 bytes of data to ensure the logic for handling more than one chunk is exercised.
        XXHash32 xxHash32 = new XXHash32();
        byte[] inputData = new byte[25];
        inputData[3] = 16; // Use a non-zero value to create a non-trivial hash.

        // This expected value was pre-calculated using a trusted implementation of xxHash32
        // for the given input, ensuring our implementation is consistent.
        final long expectedHash = 281612550L;

        // Act: Update the hasher with the first 24 bytes of the input data.
        xxHash32.update(inputData, 0, 24);
        final long actualHash = xxHash32.getValue();

        // Assert: Verify that the calculated hash matches the expected value.
        assertEquals("The calculated hash for a multi-chunk update should match the expected value.",
                expectedHash, actualHash);
    }
}