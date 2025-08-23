package org.apache.commons.codec.binary;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * This test suite evaluates the Base16 class, focusing on its ability to correctly
 * identify characters that are not part of the Base16 alphabet.
 */
public class Base16Test {

    /**
     * Tests that the {@code containsAlphabetOrPad} method returns {@code false}
     * when the input byte array contains characters that are not valid in the
     * Base16 alphabet. The Base16 specification does not include a padding character.
     */
    @Test
    public void containsAlphabetOrPadShouldReturnFalseForNonAlphabetCharacters() {
        // Arrange: Set up the test by creating a Base16 codec and providing an
        // input that is known to be outside the Base16 alphabet.
        // The CHUNK_SEPARATOR (\r\n) is used here as it contains control characters
        // which are not hexadecimal digits (0-9, A-F). [11, 12]
        final Base16 base16 = new Base16();
        final byte[] nonAlphabetData = BaseNCodec.CHUNK_SEPARATOR;

        // Act: Execute the method under test to check if the input contains any
        // alphabet characters.
        final boolean result = base16.containsAlphabetOrPad(nonAlphabetData);

        // Assert: Verify that the result is false, confirming that the method
        // correctly identified that no characters in the input belong to the
        // Base16 alphabet.
        assertFalse("The CHUNK_SEPARATOR should not contain any Base16 alphabet characters.", result);
    }
}