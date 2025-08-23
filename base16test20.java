package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link Base16} class, focusing on decoding invalid characters.
 */
public class Base16Test {

    private final Base16 base16 = new Base16();

    /**
     * The Base16 alphabet consists of '0'-'9' and 'A'-'F' (case-insensitive).
     * This test verifies that attempting to decode any character outside of this
     * alphabet results in an IllegalArgumentException.
     *
     * @param invalidChar an invalid character that is not part of the Base16 alphabet.
     */
    @ParameterizedTest(name = "Decoding invalid character ''{0}'' should fail")
    @ValueSource(chars = {
        // Characters just outside the valid ranges
        '/', // before '0'
        ':', // after '9'
        '@', // before 'A'
        'G', // after 'F'
        '`', // before 'a'
        'g'  // after 'f'
    })
    void decode_withInvalidCharacter_throwsIllegalArgumentException(final char invalidChar) {
        final byte[] invalidInput = { (byte) invalidChar };

        assertThrows(IllegalArgumentException.class,
            () -> base16.decode(invalidInput),
            "Decoding a byte array containing an invalid Base16 character should throw an exception.");
    }
}