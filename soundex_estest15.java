package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Soundex} class.
 */
public class SoundexGenealogyMappingTest {

    /**
     * Tests that the {@code soundex} method, when using the {@code US_ENGLISH_GENEALOGY} mapping,
     * correctly encodes a string that contains non-alphabetic characters.
     * The Soundex algorithm should ignore these non-alphabetic characters.
     *
     * <p>
     * The encoding process for "org.apache.commons.codec.language.Soundex" is as follows:
     * 1. Clean the input by removing non-letters: "orgapachecommonscodecSoundex"
     * 2. The first letter 'O' is kept.
     * 3. 'r' maps to '6'. Code: "O6"
     * 4. 'g' maps to '2'. Code: "O62"
     * 5. 'a' is a vowel and is ignored.
     * 6. 'p' maps to '1'. Code: "O621"
     * 7. The code has reached the standard length of 4, so the process stops.
     * </p>
     */
    @Test
    public void shouldIgnoreNonAlphabeticCharactersForGenealogyMapping() {
        // Arrange
        final Soundex soundexEncoder = Soundex.US_ENGLISH_GENEALOGY;
        final String input = "org.apache.commons.codec.language.Soundex";
        final String expectedCode = "O621";

        // Act
        final String actualCode = soundexEncoder.soundex(input);

        // Assert
        assertEquals("The Soundex code should be correctly generated, ignoring non-letters.",
                expectedCode, actualCode);
    }
}