package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the encode method correctly handles a charset alias.
     * <p>
     * According to RFC 2047, the encoded-word should contain the canonical
     * charset name. This test verifies that when an alias ("l9") is provided,
     * the codec correctly resolves it to its canonical name ("ISO-8859-15")
     * and uses it in the output string.
     * </p>
     *
     * @throws EncoderException if the encoding fails, which is not expected in this test.
     */
    @Test
    public void testEncodeStringWithCharsetAliasUsesCanonicalNameInResult() throws EncoderException, UnsupportedCharsetException {
        // Arrange
        final BCodec bCodec = new BCodec(); // Using default constructor for clarity.
        final String textToEncode = "l9";

        // "l9" is a known, albeit obscure, alias for the ISO-8859-15 charset.
        final String charsetAlias = "l9";

        // The expected format is =?charset?B?encoded_text?=
        // - The charset should be the canonical name "ISO-8859-15".
        // - 'B' indicates Base64 encoding.
        // - The Base64 encoding of "l9" is "bDk=".
        final String expectedEncodedString = "=?ISO-8859-15?B?bDk=?=";

        // Act
        final String actualEncodedString = bCodec.encode(textToEncode, charsetAlias);

        // Assert
        assertEquals("The encoded string does not match the expected format.",
                expectedEncodedString, actualEncodedString);
    }
}