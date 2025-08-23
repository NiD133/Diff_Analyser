package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the encode method correctly handles a string containing non-alphabetic characters
     * by ignoring them during the encoding process.
     */
    @Test
    public void encodeShouldIgnoreNonAlphabeticCharacters() {
        // Arrange
        Soundex soundex = new Soundex();
        // The input string contains letters, numbers, and symbols.
        // The Soundex algorithm should ignore non-letters.
        // Transformation for "atW+2N,x7`1kf@":
        // 1. Retain first letter: 'A'
        // 2. Map subsequent consonants: t -> 3, W -> ignored, N -> 5, x -> 2
        // 3. Pad to 4 characters: A352
        String inputWithNonLetters = "atW+2N,x7`1kf@";
        String expectedEncoding = "A352";

        // Act
        String actualEncoding = soundex.encode(inputWithNonLetters);

        // Assert
        assertEquals("The Soundex encoding should ignore non-alphabetic characters",
                expectedEncoding, actualEncoding);
    }
}