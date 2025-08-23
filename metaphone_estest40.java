package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly processes strings containing
     * non-alphabetic characters by ignoring them.
     */
    @Test
    public void metaphoneShouldIgnoreNonAlphabeticCharactersInInput() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        // The Metaphone algorithm should process only the alphabetic characters
        // from the input string, effectively treating "Am<p[7Pu444indX8o6" as "AMPPUINDXO".
        // The resulting code is "AMPP" because the default max code length is 4.
        final String inputWithNonAlphabetics = "Am<p[7Pu444indX8o6";
        final String expectedCode = "AMPP";

        // Act
        final String resultCode = metaphone.metaphone(inputWithNonAlphabetics);

        // Assert
        assertEquals("The Metaphone algorithm should ignore non-alphabetic characters.",
                     expectedCode,
                     resultCode);
    }
}