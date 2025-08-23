package org.apache.commons.codec.language;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    /**
     * Tests that the metaphone algorithm correctly processes a string containing
     * non-alphabetic characters by ignoring them.
     */
    @Test
    public void metaphoneShouldIgnoreNonAlphabeticCharacters() {
        // Arrange
        // The Metaphone algorithm is defined for alphabetic characters. This test
        // verifies that numbers and symbols in the input string are stripped out
        // before encoding.
        // Input "S9B]_aD^" is sanitized to "SBAD".
        final String inputWithNonAlphabetics = "S9B]_aD^";

        // The expected code for "SBAD" is "SBT":
        // S -> S
        // B -> B
        // A -> (vowel after first letter, ignored)
        // D -> T
        final String expectedCode = "SBT";

        // Act
        final String actualCode = metaphone.metaphone(inputWithNonAlphabetics);

        // Assert
        assertEquals("The metaphone code should be generated from only the alphabetic characters.",
                     expectedCode, actualCode);
    }

    /**
     * Tests that a new Metaphone instance has the correct default maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4.",
                     expectedMaxCodeLen, actualMaxCodeLen);
    }
}