package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that encoding an empty string correctly results in an empty string.
     */
    @Test
    public void shouldReturnEmptyStringWhenEncodingEmptyString() {
        // Arrange
        // The original test created a Soundex instance with a custom mapping.
        // However, the encoding of an empty string is a special case that is
        // independent of the mapping, so the default constructor is sufficient and simpler.
        Soundex soundex = new Soundex();

        // Act
        String result = soundex.encode("");

        // Assert
        // The primary assertion is that an empty input produces an empty output.
        assertEquals("Encoding an empty string should return an empty string.", "", result);

        // The original test also verified the default max length. We retain this check for completeness.
        // Note: The getMaxLength() method is deprecated.
        assertEquals("The default max length should be 4.", 4, soundex.getMaxLength());
    }
}