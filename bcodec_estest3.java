package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that encoding a string containing various ASCII symbols with the UTF-8 charset
     * produces the correct RFC 1522 format.
     */
    @Test
    public void testEncodeStringWithAsciiSymbols() throws EncoderException {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String plainText = "^QOTD7,4PZ$(<r";
        final Charset charset = StandardCharsets.UTF_8;

        // The expected string follows the RFC 1522 format: "=?charset?encoding?encoded-text?="
        // In this case, charset is UTF-8, encoding is 'B' (Base64), and the text is the Base64
        // representation of the plain text.
        final String expectedEncodedString = "=?UTF-8?B?XlFPVEQ3LDRQWiQoPHI=?=";

        // Act
        final String actualEncodedString = bCodec.encode(plainText, charset);

        // Assert
        assertEquals("The encoded string should match the expected RFC 1522 format.",
                expectedEncodedString, actualEncodedString);
    }
}