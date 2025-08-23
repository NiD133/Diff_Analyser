package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.binary.Base16;
import org.apache.commons.codec.binary.BaseNCodec;

public class Base16_ESTestTest12 extends Base16_ESTest_scaffolding {

    /**
     * Tests that the decode method throws an IllegalArgumentException when it encounters
     * an invalid character in the input data.
     *
     * This test specifically checks the case where a valid Base16 character is followed
     * by an invalid one (a null byte). The decoder is expected to be strict and reject
     * any character not in the Base16 alphabet.
     */
    @Test
    public void decodeShouldThrowExceptionForInvalidCharacter() {
        // Arrange: Create a Base16 decoder and prepare input with an invalid character.
        final Base16 base16 = new Base16();
        final BaseNCodec.Context context = new BaseNCodec.Context();

        // The input contains a valid Base16 character ('E') followed by an invalid one (the null byte).
        final byte[] encodedDataWithInvalidChar = new byte[]{'E', 0};

        // Act & Assert: Attempt to decode the data and verify the expected exception.
        try {
            base16.decode(encodedDataWithInvalidChar, 0, encodedDataWithInvalidChar.length, context);
            fail("Expected an IllegalArgumentException to be thrown for invalid input.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid octet.
            // The integer value of the null byte is 0.
            final String expectedMessage = "Invalid octet in encoded value: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}