package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Soundex} focusing on exception handling with custom mappings.
 */
public class SoundexCustomMappingTest {

    /**
     * Tests that the soundex method throws an IllegalArgumentException when it encounters a character
     * that is not covered by a custom, short mapping.
     */
    @Test
    public void soundexShouldThrowExceptionForCharacterOutsideShortMapping() {
        // Arrange
        // A custom mapping that is shorter than the 26 characters required for the English alphabet.
        // This mapping only covers characters 'A' through 'N' (14 characters).
        final String shortMapping = "ABCDEFGHIJKLMN";
        final Soundex soundex = new Soundex(shortMapping, false);

        // The input string contains 'Z'. The Soundex algorithm calculates the mapping index
        // for 'Z' as 25 ('Z' - 'A'), which is out of bounds for our 14-character mapping.
        final String inputWithUnmappedChar = "ZULU";

        // Act & Assert
        try {
            soundex.soundex(inputWithUnmappedChar);
            fail("Expected an IllegalArgumentException because the character 'Z' is not in the custom mapping.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason.
            final String expectedMessage = "The character is not mapped: Z (index=25)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}