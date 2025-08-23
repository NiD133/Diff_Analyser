package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 *
 * <p>Note: The original test class name and inheritance were artifacts from a test
 * generation tool and have been simplified for better readability.</p>
 */
public class MurmurHash2Test {

    /**
     * Tests that the {@code hash32(String, int, int)} method correctly calculates the hash
     * for a zero-length substring. This is an important edge case, equivalent to hashing
     * an empty input, and should produce a consistent, known value based on the default seed.
     */
    @Test
    public void hash32ShouldReturnKnownValueForEmptySubstring() {
        // Arrange
        // The content of the source string is irrelevant when hashing a zero-length substring.
        final String sourceText = "Dpn ='f";
        final int offset = 0;
        final int length = 0;

        // This is the pre-calculated MurmurHash2 value for an empty input
        // with the default seed (0x9747b28c).
        final int expectedHashForEmptyInput = 275646681;

        // Act
        final int actualHash = MurmurHash2.hash32(sourceText, offset, length);

        // Assert
        assertEquals("Hashing a zero-length substring should produce the known hash for an empty input.",
                expectedHashForEmptyInput, actualHash);
    }
}