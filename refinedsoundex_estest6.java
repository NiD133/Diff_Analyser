package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that encoding a string containing only a single space results in an empty string.
     * The Refined Soundex algorithm is designed for alphabetic characters and should
     * ignore non-alphabetic input like spaces.
     */
    @Test
    public void shouldReturnEmptyStringWhenEncodingSingleSpace() {
        // Arrange
        final RefinedSoundex soundex = new RefinedSoundex();
        final String input = " ";
        final String expectedEncoding = "";

        // Act
        final String actualEncoding = soundex.encode(input);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }
}