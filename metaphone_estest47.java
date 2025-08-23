package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that a new Metaphone instance is configured with the correct
     * default maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }

    /**
     * Tests the Metaphone encoding for the string "CGH".
     * According to the Metaphone algorithm rules:
     * - 'C' at the beginning of a word is encoded as 'K'.
     * - 'GH' at the end of a word is silent.
     * Therefore, "CGH" should be encoded as "K".
     */
    @Test
    public void metaphoneShouldEncodeCghAsK() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "CGH";
        final String expectedEncoding = "K";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The metaphone encoding for 'CGH' should be 'K'", expectedEncoding, actualEncoding);
    }
}