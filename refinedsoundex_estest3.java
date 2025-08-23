package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the RefinedSoundex class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the US English mapping for the character 'y' is '0',
     * which is the code used for vowels and vowel-like letters.
     */
    @Test
    public void getMappingCodeShouldReturnZeroForVowelLikeY() {
        // Arrange
        final RefinedSoundex soundex = RefinedSoundex.US_ENGLISH;
        final char inputChar = 'y';
        final char expectedCode = '0';

        // Act
        final char actualCode = soundex.getMappingCode(inputChar);

        // Assert
        assertEquals("The mapping code for 'y' should be '0' as per the Refined Soundex rules.",
                expectedCode,
                actualCode);
    }
}