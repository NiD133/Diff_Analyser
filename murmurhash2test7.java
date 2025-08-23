package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on hashing String inputs.
 */
class MurmurHash2Test {

    /**
     * A sample string used for testing the hash function.
     */
    private static final String LOREM_IPSUM_TEXT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit";

    /**
     * The pre-calculated 64-bit MurmurHash2 value for LOREM_IPSUM_TEXT using the default seed.
     * This serves as a "known-answer" test to verify the correctness of the implementation.
     */
    private static final long EXPECTED_HASH_OF_LOREM_IPSUM = 0x0920e0c1b7eeb261L;

    @Test
    @DisplayName("hash64(String) should produce the correct known hash for a sample string")
    void hash64_withStringAndDefaultSeed_returnsCorrectHash() {
        // Arrange: The input string and expected hash are defined as constants.

        // Act: Calculate the hash of the sample string.
        final long actualHash = MurmurHash2.hash64(LOREM_IPSUM_TEXT);

        // Assert: The calculated hash must match the pre-calculated, known-correct value.
        assertEquals(EXPECTED_HASH_OF_LOREM_IPSUM, actualHash,
            "The 64-bit hash for the sample string does not match the expected value.");
    }
}