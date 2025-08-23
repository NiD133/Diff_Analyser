package org.apache.commons.codec.binary;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * This test case verifies the character validation logic within the Base16 class.
 */
public class Base16Test {

    /**
     * Tests that the {@code containsAlphabetOrPad} method returns false for byte arrays
     * that do not contain any Base16 alphabet characters.
     *
     * <p>This test uses the {@link BaseNCodec#CHUNK_SEPARATOR} (carriage return and line feed)
     * as input. According to the Base16 specification (RFC 4648), these characters are not
     * part of the valid alphabet, and this test confirms they are handled correctly.</p>
     */
    @Test
    public void testContainsAlphabetOrPadWithNonAlphabetCharacters() {
        // Arrange
        final Base16 base16 = new Base16();
        // The CHUNK_SEPARATOR constant represents the byte sequence for \r\n.
        final byte[] nonAlphabetBytes = BaseNCodec.CHUNK_SEPARATOR;

        // Act
        final boolean result = base16.containsAlphabetOrPad(nonAlphabetBytes);

        // Assert
        assertFalse("Chunk separator characters should not be considered part of the Base16 alphabet.", result);
    }
}