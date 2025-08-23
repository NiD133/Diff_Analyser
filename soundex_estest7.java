package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Soundex} focusing on exception handling with custom mappings.
 */
public class SoundexTest {

    @Test
    public void encodeShouldThrowIllegalArgumentExceptionForUnmappedCharacter() {
        // Arrange: Create a Soundex instance with a custom mapping that is too short
        // to map all letters of the alphabet. This mapping only covers 'A', 'B', and 'C'.
        final String shortMapping = "ABC";
        final Soundex soundex = new Soundex(shortMapping);
        final String inputWithUnmappedChar = "David"; // The character 'D' is not covered by the mapping.

        // Act & Assert
        try {
            soundex.encode(inputWithUnmappedChar);
            fail("Expected an IllegalArgumentException because the input contains a character not in the mapping.");
        } catch (final IllegalArgumentException e) {
            // The character 'D' has an index of 3 ('D' - 'A'), which is out of bounds for the mapping of length 3.
            // We verify that the exception message is informative and correct.
            final String expectedMessage = "The character is not mapped: D (index=3)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}