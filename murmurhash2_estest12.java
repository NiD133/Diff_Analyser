package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash32 method returns the known, correct hash value
     * for an empty string input when using the default seed.
     */
    @Test
    public void hash32ShouldReturnKnownValueForEmptyString() {
        // Arrange
        final String input = "";
        // The expected hash for an empty string with the default seed (0x9747b28c) is a known value.
        final int expectedHash = 275646681;

        // Act
        final int actualHash = MurmurHash2.hash32(input);

        // Assert
        assertEquals("Hashing an empty string should produce the correct, pre-calculated value.",
                expectedHash, actualHash);
    }
}