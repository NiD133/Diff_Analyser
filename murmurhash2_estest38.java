package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the {@code hash32(String, int, int)} method correctly calculates the hash
     * for a given substring using the default seed.
     *
     * The expected hash value is a pre-calculated "golden value" to ensure the
     * hashing algorithm remains consistent across changes.
     */
    @Test
    public void testHash32ForSubstringWithDefaultSeed() {
        // Arrange
        final String inputText = "9chG_Yo[`m";
        final int startIndex = 1;
        final int length = 1;
        // The substring extracted by these parameters is "c".
        final int expectedHash = -1877468854;

        // Act
        final int actualHash = MurmurHash2.hash32(inputText, startIndex, length);

        // Assert
        assertEquals("The calculated hash for the substring does not match the expected value.",
                expectedHash, actualHash);
    }
}