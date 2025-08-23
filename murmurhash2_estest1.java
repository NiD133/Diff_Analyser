package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that hashing a zero-length substring of a String returns the correct pre-calculated 64-bit hash value.
     * The content of the string and the starting offset should not affect the result when the length is zero,
     * as this effectively hashes an empty string.
     */
    @Test
    public void hash64ShouldReturnCorrectHashForZeroLengthSubstring() {
        // Arrange
        // The specific string and starting offset are irrelevant when the length is 0.
        final String input = "bPH \"XdK'x'8?hr";
        final int from = 4;
        final int length = 0; // The key parameter for this test case

        // This is the expected hash for an empty input with the default seed used by this method overload.
        final long expectedHash = -7207201254813729732L;

        // Act
        final long actualHash = MurmurHash2.hash64(input, from, length);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}