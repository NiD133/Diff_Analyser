package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 * This test suite focuses on verifying the correctness of the hash function
 * by comparing its output against pre-computed, known values.
 */
public class MurmurHash2Test {

    /**
     * Tests that the {@code hash32(String)} method produces a consistent,
     * known hash value for a specific string input. This acts as a regression test
     * to ensure the algorithm's implementation does not change unexpectedly.
     */
    @Test
    public void testHash32StringShouldReturnKnownValue() {
        // Arrange
        final String input = "org.apache.commons.codec.binary.StringUtils";
        // The expected hash is a pre-calculated value for the given input string.
        // This ensures that the hash function's output is stable across versions.
        final int expectedHash = -1819289676;

        // Act
        final int actualHash = MurmurHash2.hash32(input);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}