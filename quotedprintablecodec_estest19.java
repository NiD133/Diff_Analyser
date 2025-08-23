package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a string that contains no Quoted-Printable encoded characters
     * results in the original, unchanged string.
     */
    @Test
    public void decodeUnencodedStringShouldReturnOriginalString() throws DecoderException {
        // Arrange
        final String originalString = "q]}Ms![j+3/5Ic";
        final Charset charset = StandardCharsets.UTF_8;
        // The 'strict' parameter is set to true, enforcing all RFC 1521 rules.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec(charset, true);

        // Act
        final String decodedString = codec.decode(originalString, charset);

        // Assert
        assertEquals("The decoded string should be identical to the original.",
                originalString, decodedString);
    }
}