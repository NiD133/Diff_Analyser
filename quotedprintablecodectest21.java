package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    @Test
    @DisplayName("Encoding and decoding should handle special characters like '=', CR, and LF correctly")
    void testSpecialCharacterRoundTrip() throws EncoderException, DecoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // The input string contains characters that have special meaning in Quoted-Printable encoding
        // and must be escaped: the '=' escape character itself, and the CR/LF control characters.
        final String originalString = "=\r\n";

        // The expected result is the hexadecimal representation of each character, prefixed with '='.
        //   '='  (ASCII 61, 0x3D) -> =3D
        //   '\r' (ASCII 13, 0x0D) -> =0D
        //   '\n' (ASCII 10, 0x0A) -> =0A
        final String expectedEncodedString = "=3D=0D=0A";

        // Act
        final String actualEncodedString = codec.encode(originalString);
        final String decodedString = codec.decode(actualEncodedString);

        // Assert
        // Verify that the encoding produces the correct Quoted-Printable string.
        assertEquals(expectedEncodedString, actualEncodedString, "The string was not encoded correctly.");

        // Verify that decoding the encoded string returns the original string (a successful round-trip).
        assertEquals(originalString, decodedString, "The decoded string does not match the original.");
    }
}