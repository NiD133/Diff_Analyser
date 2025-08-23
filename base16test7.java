package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.CodecPolicy;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Base16}.
 */
public class Base16Test {

    /**
     * Tests that decoding a byte array containing characters not in the Base16 alphabet
     * throws an IllegalArgumentException when using the strict decoding policy.
     * In contrast, the default lenient policy would simply ignore these invalid characters.
     */
    @Test
    void decodeWithStrictPolicyShouldThrowExceptionForInvalidInput() {
        // GIVEN: A byte array containing a mix of valid and invalid Base16 characters.
        // The character 'n' and the byte 0x9c are not part of the Base16 alphabet.
        final byte[] inputWithInvalidChars = {'n', 'H', '=', (byte) 0x9c};

        // AND: A Base16 codec configured with a strict decoding policy.
        final Base16 base16Strict = new Base16(false, CodecPolicy.STRICT);

        // WHEN: An attempt is made to decode the invalid input.
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            base16Strict.decode(inputWithInvalidChars);
        });

        // THEN: The exception message should indicate which character caused the failure.
        // The first invalid character is 'n', which has a byte value of 110.
        assertTrue(thrown.getMessage().contains("Invalid octet '110'"));
    }
}