package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link QuotedPrintableCodec} focusing on decoding error handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a Quoted-Printable byte array fails when an escape character '='
     * is followed by a non-hexadecimal character.
     * <p>
     * According to RFC 1521, an '=' in a Quoted-Printable string must be followed by
     * two hexadecimal digits (0-9 or A-F). This test case provides an input where '='
     * is followed by a null byte (value 0), which is invalid.
     * </p>
     */
    @Test
    public void decodeQuotedPrintable_shouldThrowException_whenEscapeCharIsFollowedByNonHexChar() {
        // Arrange: Create a byte array with an invalid Quoted-Printable sequence.
        // The sequence contains an escape character '=' followed by a null byte,
        // which is not a valid hexadecimal digit.
        byte[] invalidEncodedBytes = new byte[]{'A', 'B', '=', 0, 'C'}; // Represents "AB<NULL>C"

        // Act & Assert
        try {
            QuotedPrintableCodec.decodeQuotedPrintable(invalidEncodedBytes);
            fail("Expected DecoderException was not thrown for invalid input.");
        } catch (DecoderException e) {
            // The exception is expected. We verify its message to ensure it's the correct error.
            // The original test verified this specific message, which is preserved here for consistency.
            String expectedMessage = "Invalid URL encoding: not a valid digit (radix 16): 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}