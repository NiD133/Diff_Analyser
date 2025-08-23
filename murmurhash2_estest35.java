package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash64 method correctly computes the hash for a single-character
     * substring of a given string. The expected value is a known constant to ensure
     * the hashing algorithm remains consistent.
     */
    @Test
    public void testHash64ForSingleCharacterSubstring() {
        // Arrange
        final String sourceText = "ylLM~55";
        final int offset = 1; // Start at the second character 'l'
        final int length = 1; // Hash only one character
        
        // This is the expected MurmurHash2 64-bit hash for the UTF-8 bytes of the string "l"
        // using the default seed (0xe17a1465).
        final long expectedHash = 4591197677584300775L;

        // Act
        final long actualHash = MurmurHash2.hash64(sourceText, offset, length);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}