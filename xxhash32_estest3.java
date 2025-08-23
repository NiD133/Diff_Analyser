package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains understandable, human-written unit tests for the {@link XXHash32} class.
 */
public class XXHash32Test {

    /**
     * Tests that the XXHash32 algorithm produces a known, correct hash value
     * when updated with a 16-byte chunk of data. This specific length is
     * significant as it matches the internal buffer size of the algorithm,
     * representing a key execution path.
     */
    @Test
    public void testUpdateWithFullBufferSizedChunkShouldProduceCorrectHash() {
        // Arrange
        // The expected hash is a pre-calculated, known-good result for the specific input data.
        final long expectedHash = 1866244335L;
        final XXHash32 xxHash = new XXHash32();

        // This 16-byte input array is designed to test the hashing of a full internal buffer.
        // It contains mostly zeros with one non-zero byte to create a non-trivial hash input.
        final byte[] dataToHash = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 16, 0
        };

        // Act
        // Process the entire 16-byte array.
        xxHash.update(dataToHash, 0, dataToHash.length);
        final long actualHash = xxHash.getValue();

        // Assert
        assertEquals("The calculated hash should match the pre-computed value for the 16-byte input.",
                expectedHash, actualHash);
    }
}