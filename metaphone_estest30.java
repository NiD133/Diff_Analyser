package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly handles a complex string containing
     * mixed-case letters, numbers, and symbols. The algorithm is expected to
     * convert the input to uppercase and ignore non-alphabetic characters before encoding.
     */
    @Test
    public void metaphoneShouldEncodeStringWithMixedCaseAndNonAlphabeticCharacters() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "chuQ)i92HWt";
        final String expectedCode = "KKT";

        // Explanation of the encoding for "chuQ)i92HWt":
        // 1. Input is sanitized to uppercase letters only -> "CHUQIHWT"
        // 2. "CH" -> "K"
        // 3. "U", "I" (vowels) -> ignored
        // 4. "Q" -> "K"
        // 5. "H", "W" (following vowels) -> ignored
        // 6. "T" -> "T"
        // Result: "KKT"

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The metaphone encoding was incorrect.", expectedCode, actualCode);
    }

    /**
     * Verifies that a new Metaphone instance is created with the default
     * maximum code length of 4.
     */
    @Test
    public void newInstanceShouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4.", expectedMaxCodeLen, actualMaxCodeLen);
    }
}