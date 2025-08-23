package org.apache.commons.codec.language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Soundex} focusing on exception handling with custom mappings.
 */
public class SoundexTest {

    /**
     * Tests that the difference() method throws an IllegalArgumentException when a string
     * contains a character that is not defined in the custom Soundex mapping.
     */
    @Test
    public void differenceShouldThrowExceptionForUnmappedCharacterInCustomMapping() {
        // Arrange: Create a custom mapping that is intentionally incomplete.
        // This mapping only covers the first 8 letters of the alphabet ('A' through 'H').
        char[] incompleteMapping = new char[8];
        Soundex soundex = new Soundex(incompleteMapping);

        // The input string "KING" contains 'K'. The Soundex algorithm maps 'K' by calculating
        // its index ('K' - 'A' = 10). Since index 10 is out of bounds for our 8-element
        // mapping array, an exception is expected.
        String string1WithUnmappedChar = "KING";
        String string2 = "KONG";

        // Act & Assert
        try {
            soundex.difference(string1WithUnmappedChar, string2);
            fail("Expected an IllegalArgumentException because 'K' is not in the custom mapping.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "The character is not mapped: K (index=10)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}