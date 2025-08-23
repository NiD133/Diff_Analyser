package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the Metaphone class, focusing on specific encoding scenarios.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly handles an input string containing
     * mixed-case letters and numbers. The algorithm is expected to ignore non-alphabetic
     * characters.
     */
    @Test
    public void metaphoneShouldIgnoreNonAlphabeticCharactersInInput() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String inputWithNumbers = "wT0mBTkI2";

        // The Metaphone algorithm processes only the alphabetic characters from the
        // input string ("WTMBTKI" after uppercasing).
        // - 'W' is dropped (not followed by a vowel or 'H').
        // - 'T' -> "T"
        // - 'M' -> "TM"
        // - 'B' -> "TMB"
        // - 'T' -> "TMBT"
        // The encoding stops once the default max length of 4 is reached.
        String expectedCode = "TMBT";

        // Act
        String actualCode = metaphone.metaphone(inputWithNumbers);

        // Assert
        assertEquals("The metaphone code should be generated from alphabetic characters only",
                     expectedCode, actualCode);
    }
}