package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link Base16} class.
 * This class focuses on verifying the behavior of the isInAlphabet method.
 */
public class Base16Test {

    @Test
    public void isInAlphabetShouldReturnFalseForCharacterOutsideAlphabet() {
        // Arrange: Create a standard Base16 codec, which uses the uppercase
        // hexadecimal alphabet (0-9, A-F) by default.
        final Base16 base16 = new Base16();
        
        // The backslash character is not a valid Base16 character.
        final byte nonAlphabetChar = '\\';

        // Act: Check if the character is in the alphabet.
        final boolean result = base16.isInAlphabet(nonAlphabetChar);

        // Assert: The result should be false.
        assertFalse(
            "The backslash character '\\' should not be considered part of the Base16 alphabet.",
            result
        );
    }
}