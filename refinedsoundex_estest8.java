package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RefinedSoundex} class, focusing on the US English implementation.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the soundex algorithm correctly encodes a complex string containing
     * mixed-case letters and non-alphabetic characters (dots). The algorithm is
     * expected to ignore non-letters and process letters in a case-insensitive manner.
     */
    @Test
    public void shouldCorrectlyEncodeComplexStringWithMixedCaseAndNonLetters() {
        // Arrange
        final RefinedSoundex soundexEncoder = RefinedSoundex.US_ENGLISH;
        final String input = "org.apache.commons.codec.language.RefinedSoundex";
        final String expectedEncoding = "O09401030308083060370840409020806308605";

        // Act
        final String actualEncoding = soundexEncoder.soundex(input);

        // Assert
        assertEquals("The complex string was not encoded as expected.", expectedEncoding, actualEncoding);
    }
}