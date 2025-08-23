package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link QuotedPrintableCodec} focusing on decoding error handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when an escape character '='
     * is followed by an invalid hexadecimal character.
     */
    @Test
    public void decodeWithInvalidHexCharacterShouldThrowException() {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // The sequence "=G" is invalid because 'G' is not a hexadecimal digit (0-9, a-f, A-F).
        final String invalidInput = "V6Vz)'=Gpk}MM_";
        final String expectedMessage = "Invalid URL encoding: not a valid digit (radix 16): 71";

        // Act & Assert
        // Use assertThrows to verify that the correct exception is thrown.
        DecoderException thrown = assertThrows(DecoderException.class, () -> {
            codec.decode(invalidInput);
        });

        // Further assert that the exception message is as expected.
        // The message confirms that 'G' (ASCII 71) caused the failure.
        // Note: The "Invalid URL encoding" text comes from a shared utility class.
        assertEquals(expectedMessage, thrown.getMessage());
    }
}