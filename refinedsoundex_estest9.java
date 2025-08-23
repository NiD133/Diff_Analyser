package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RefinedSoundex} class.
 */
public class RefinedSoundexTest {

    /**
     * Tests that the soundex algorithm returns an empty string when given an empty string as input.
     * This is the expected behavior as there is nothing to encode.
     */
    @Test
    public void soundexOfEmptyStringShouldReturnEmptyString() {
        // Arrange
        // Use the standard, pre-configured US English instance for clarity.
        // The behavior for an empty input should be consistent regardless of the mapping.
        final RefinedSoundex refinedSoundex = RefinedSoundex.US_ENGLISH;
        final String emptyInput = "";

        // Act
        final String result = refinedSoundex.soundex(emptyInput);

        // Assert
        assertEquals("Encoding an empty string should result in an empty string.", "", result);
    }
}