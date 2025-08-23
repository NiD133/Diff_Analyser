package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    @Test
    void testEncodeDecodeRoundTripWithSpecialCharacters() throws EncoderException, DecoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        
        // This string contains characters that require Quoted-Printable encoding:
        // '=' (equals sign) should be encoded as =3D
        // '\r' (carriage return) should be encoded as =0D
        // '\n' (line feed) should be encoded as =0A
        final String originalString = "= Hello there =\r\n";
        final String expectedEncodedString = "=3D Hello there =3D=0D=0A";

        // Act: Encode the original string
        final String actualEncodedString = codec.encode(originalString);

        // Assert: Verify the encoded string is correct
        assertEquals(expectedEncodedString, actualEncodedString);

        // Act: Decode the encoded string
        final String decodedString = codec.decode(actualEncodedString);

        // Assert: Verify the decoded string matches the original, completing the round trip
        assertEquals(originalString, decodedString);
    }
}