package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that getMappingCode() returns a null character for an input that is not an
     * English letter. The Refined Soundex mapping is only defined for characters A-Z.
     */
    @Test
    public void getMappingCodeShouldReturnNullCharForNonAlphabeticCharacter() {
        // Arrange
        // Using the default US English mapping. The behavior for non-letters is the same
        // regardless of the specific character-to-code mapping.
        RefinedSoundex soundex = new RefinedSoundex();
        char nonAlphabeticChar = '+';

        // Act
        char mappingCode = soundex.getMappingCode(nonAlphabeticChar);

        // Assert
        // Non-alphabetic characters should map to the null character ('\u0000'), which has a value of 0.
        assertEquals('\u0000', mappingCode);
    }
}