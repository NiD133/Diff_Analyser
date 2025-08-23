package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Metaphone}.
 * This class replaces an auto-generated test suite to improve clarity and maintainability.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone() method correctly handles a string containing
     * non-alphabetic characters (spaces, punctuation, numbers) by ignoring them
     * during the encoding process.
     *
     * This test also confirms that a new Metaphone instance has the expected
     * default maximum code length.
     */
    @Test
    public void metaphoneShouldIgnoreNonAlphabeticCharacters() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        // The Metaphone algorithm is expected to strip non-alphabetic characters
        // before encoding. "ghZ g-7V=6hV Uh" becomes "GHZGVHVUH".
        final String inputWithMixedChars = "ghZ g-7V=6hV Uh";
        final String expectedCode = "SFF";
        final int expectedDefaultMaxCodeLen = 4;

        // Act
        final String actualCode = metaphone.metaphone(inputWithMixedChars);
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        // 1. Verify that the encoded string is correct.
        assertEquals("Metaphone encoding should ignore non-alphabetic characters",
                     expectedCode, actualCode);

        // 2. Verify that the default max code length is as expected.
        assertEquals("Default max code length should be 4",
                     expectedDefaultMaxCodeLen, actualMaxCodeLen);
    }
}