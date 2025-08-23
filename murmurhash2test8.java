package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * A sample text used for hashing tests.
     */
    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";

    @Test
    @DisplayName("hash64(String, int, int) should return the correct hash for a substring")
    void testHash64ForSubstring() {
        // Arrange: Define the substring and the expected hash value.
        final int offset = 2;
        final int length = LOREM_IPSUM.length() - 4;
        final String substringToHash = LOREM_IPSUM.substring(offset, offset + length);

        // The expected hash for the substring "rem ipsum dolor sit amet, consectetur adipisicing e"
        // was pre-calculated using the MurmurHash2 64-bit algorithm with its default seed.
        final long expectedHash = 0xa8b33145194985a2L;

        // Act: Compute the hash of the specified substring.
        final long actualHash = MurmurHash2.hash64(LOREM_IPSUM, offset, length);

        // Assert: Verify that the computed hash matches the expected value.
        assertEquals(expectedHash, actualHash,
                "Hash of substring '" + substringToHash + "' did not match the expected value.");
    }
}