package org.apache.commons.codec.language;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        this.metaphone = new Metaphone();
    }

    @Test
    public void shouldIgnoreNonAlphabeticCharactersWhenEncoding() {
        // Arrange
        // The Metaphone algorithm is documented to work on A-Z characters.
        // This test verifies that non-alphabetic characters are stripped out
        // before encoding. The input "H]H0>$Z" becomes "HHZ", which Metaphone
        // encodes as "S".
        final String inputWithNonLetters = "H]H0>$Z";
        final String expectedEncoding = "S";

        // Act
        final String actualEncoding = metaphone.metaphone(inputWithNonLetters);

        // Assert
        assertEquals("The metaphone encoding should match the expected value.",
                expectedEncoding, actualEncoding);
    }

    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("The default max code length should be 4.",
                expectedMaxCodeLen, actualMaxCodeLen);
    }
}