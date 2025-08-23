package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link org.apache.commons.codec.language.Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the soundex algorithm correctly processes a string containing non-alphabetic characters
     * by ignoring them and encoding only the letters.
     */
    @Test
    public void soundexIgnoresNonAlphabeticCharacters() {
        // Arrange
        final Soundex soundex = new Soundex(); // Uses the default US English mapping
        final String inputWithNonLetters = "[&L!ug<F4wFviM+`RV{";
        final String expectedCode = "L215";

        // Act
        final String actualCode = soundex.soundex(inputWithNonLetters);

        // Assert
        assertEquals("The Soundex code should be generated correctly from a string containing non-letters.",
                     expectedCode, actualCode);
    }

    /**
     * Tests that a default Soundex instance is configured with a max length of 4,
     * as per the standard Soundex algorithm definition.
     */
    @Test
    public void defaultInstanceHasMaxLengthOfFour() {
        // Arrange
        final Soundex soundex = new Soundex();

        // Act
        final int maxLength = soundex.getMaxLength();

        // Assert
        assertEquals("The default max length for a Soundex code should be 4.", 4, maxLength);
    }
}