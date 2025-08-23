package org.apache.commons.codec.binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the {@link Base16} class, focusing on handling invalid input.
 */
public class Base16Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that decoding a string containing characters outside of the Base16
     * alphabet (0-9, A-F) throws an IllegalArgumentException.
     */
    @Test
    public void decodeWithInvalidCharacterThrowsIllegalArgumentException() {
        // Arrange: Create a Base16 codec instance.
        final Base16 codec = new Base16();
        // The input string "Gw" contains 'G', which is not a valid hexadecimal character.
        final String invalidInput = "Gw";

        // Assert: Configure the test to expect an IllegalArgumentException.
        // We also verify the message to ensure it correctly identifies the invalid
        // character's byte value (ASCII for 'G' is 71).
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid octet in encoded value: 71");

        // Act: Attempt to decode the invalid string. This call is expected to throw.
        codec.decode(invalidInput);
    }
}