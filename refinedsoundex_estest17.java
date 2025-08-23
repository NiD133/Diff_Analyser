package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the soundex algorithm correctly handles a custom mapping where
     * most characters in the input string are not defined in the mapping.
     *
     * <p>The RefinedSoundex algorithm should:
     * <ol>
     *     <li>Retain the first letter of the input string ('E').</li>
     *     <li>Map all subsequent alphabetic characters ('Q', 'K', 'W', 'G', 'F', 'N')
     *         to '0' because they are not defined in the short custom mapping "U>".</li>
     *     <li>Drop all '0' mappings during processing.</li>
     *     <li>Result in a soundex code consisting of only the first letter.</li>
     * </ol>
     */
    @Test
    public void testSoundexWithCustomMappingIgnoresUnmappedCharacters() {
        // Arrange
        // A custom mapping that only defines codes for 'A' ('U') and 'B' ('>').
        // All other letters will be mapped to '0' (like vowels).
        final RefinedSoundex refinedSoundex = new RefinedSoundex("U>");
        
        // The input string contains non-alphabetic characters that will be stripped.
        // The cleaned, uppercase version is "EQQKWGFN".
        final String input = "<+Eq|qK!wg0f\u0006n_~";
        final String expectedCode = "E";

        // Act
        final String actualCode = refinedSoundex.soundex(input);

        // Assert
        assertEquals("The soundex code should only contain the first letter when subsequent characters are unmapped.",
                     expectedCode, actualCode);
    }
}