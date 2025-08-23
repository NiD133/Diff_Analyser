package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    @Test
    @DisplayName("Static encode with null BitSet should use default printable characters and correctly encode special characters")
    void testStaticEncodingWithDefaultCharacters() throws DecoderException {
        // Arrange
        final String plainText = "1+1 = 2";
        final String expectedEncodedText = "1+1 =3D 2";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act:
        // Encode using the static method. Passing 'null' for the BitSet
        // triggers the use of the default set of printable characters.
        final byte[] encodedBytes = QuotedPrintableCodec.encodeQuotedPrintable(null, plainText.getBytes(StandardCharsets.UTF_8));
        final String actualEncodedText = new String(encodedBytes, StandardCharsets.UTF_8);

        // Decode using a codec instance to test the round trip.
        final String decodedText = codec.decode(actualEncodedText);

        // Assert
        assertEquals(expectedEncodedText, actualEncodedText, "The '=' character should be encoded as '=3D'.");
        assertEquals(plainText, decodedText, "Decoding the encoded string should yield the original plain text.");
    }
}