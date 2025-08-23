package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the {@code hash64(String)} method produces a consistent and expected
     * hash for a known input string. This type of test is a "golden value" or
     * "characterization" test, which ensures that the algorithm's output does not
     * change unexpectedly over time.
     */
    @Test
    public void hash64StringShouldReturnCorrectHashForKnownInput() {
        // Arrange: Define a known input and its pre-calculated, expected hash value.
        // This specific value pair serves as a regression check.
        final String input = "}oZe|_r,wwn+'.Z";
        final long expectedHash = -823493256237211900L;

        // Act: Compute the hash of the input string.
        final long actualHash = MurmurHash2.hash64(input);

        // Assert: Verify that the computed hash matches the expected value.
        assertEquals(expectedHash, actualHash);
    }
}