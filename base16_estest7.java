package org.apache.commons.codec.binary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.binary.BaseNCodec.Context;

/**
 * Contains tests for the {@link Base16} class, focusing on exception handling for invalid data.
 */
public class Base16_ESTestTest7 { // In a real-world scenario, this file would be merged into a single Base16Test.java

    /**
     * Tests that the decode method throws an IllegalArgumentException when it encounters a byte
     * that is not part of the Base16 alphabet and the decoding policy is strict (the default).
     */
    @Test
    public void decodeWithInvalidByteInStrictPolicyThrowsIllegalArgumentException() {
        // Arrange
        // The Base16 decoder uses a strict policy by default.
        Base16 base16 = new Base16();
        
        // The byte value 0 (NUL character) is not a valid Base16 character ('0'-'9', 'A'-'F').
        byte[] invalidEncodedData = new byte[] { (byte) 0 };
        Context context = new Context();

        // Act & Assert
        try {
            base16.decode(invalidEncodedData, 0, invalidEncodedData.length, context);
            fail("Expected an IllegalArgumentException to be thrown for an invalid character.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the invalid byte.
            String expectedMessage = "Invalid octet in encoded value: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}