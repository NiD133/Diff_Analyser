package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link QuotedPrintableCodec} focusing on decoding error handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a byte array with an incomplete Quoted-Printable escape sequence
     * throws a DecoderException. An escape sequence starts with '=' but must be
     * followed by two hexadecimal digits.
     */
    @Test
    public void decodeShouldThrowExceptionForIncompleteEscapeSequence() {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // The input represents an invalid sequence: an '=' followed by a null byte.
        // The null byte is not a valid hexadecimal digit, making the sequence incomplete.
        final byte[] invalidInput = {(byte) '=', 0};

        // Act & Assert
        final DecoderException thrown = assertThrows(
            "Decoding an incomplete escape sequence should throw a DecoderException.",
            DecoderException.class,
            () -> codec.decode(invalidInput)
        );

        // The DecoderException should be caused by an underlying error
        // detailing the specific parsing failure.
        final Throwable cause = thrown.getCause();
        assertNotNull("The DecoderException should have a cause.", cause);
        assertEquals(
            "Invalid URL encoding: not a valid digit (radix 16): 0",
            cause.getMessage()
        );
    }
}