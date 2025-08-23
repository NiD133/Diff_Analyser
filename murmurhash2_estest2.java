package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 * This test verifies the 64-bit hash function against a known, pre-calculated value.
 */
public class MurmurHash2Test {

    /**
     * Tests that the {@code MurmurHash2.hash64(String)} method produces a consistent,
     * known hash for a given string input.
     *
     * <p>This acts as a regression test to ensure the hashing algorithm's implementation
     * remains correct and stable across code changes.</p>
     */
    @Test
    public void hash64ShouldReturnKnownValueForGivenString() {
        // Arrange: Define the input and the expected, pre-calculated hash value.
        final String input = "q%DCbQXCHT4'G\"^L";
        
        // This expected value is a "golden" or "known" value, likely generated from a
        // trusted, reference implementation of the MurmurHash2 64-bit algorithm.
        final long expectedHash = 3105811143660689330L;

        // Act: Compute the hash of the input string.
        final long actualHash = MurmurHash2.hash64(input);

        // Assert: Verify that the computed hash matches the expected value.
        assertEquals(expectedHash, actualHash);
    }
}