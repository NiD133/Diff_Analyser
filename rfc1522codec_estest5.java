package org.apache.commons.codec.net;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Tests for RFC1522Codec implementations like BCodec.
 */
public class RFC1522CodecTest {

    /**
     * Tests that BCodec correctly decodes a valid RFC 1522 "encoded-word".
     * The test case uses a string where the decoded payload itself contains
     * characters used in the RFC 1522 syntax, verifying that the decoder
     * handles them correctly.
     */
    @Test
    public void decodeTextWithValidBEncodedStringShouldReturnDecodedString() throws DecoderException, UnsupportedEncodingException {
        // Arrange
        // The encoded string is in the RFC 1522 "encoded-word" format: =?charset?encoding?encoded-text?=
        // Here, the encoding is 'B' for Base64.
        // The payload "PT9eLT89Pz0=" is the Base64 representation of the ASCII string "=?^-?=?=".
        final String encodedText = "=?UTF-8?B?PT9eLT89Pz0=?=";
        final String expectedDecodedText = "=?^-?=?=";

        // The BCodec is configured to be STRICT, meaning it will throw exceptions on malformed input.
        // The charset is specified as UTF-8, matching the charset in the encoded-word.
        final BCodec bCodec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);

        // Act
        final String decodedText = bCodec.decodeText(encodedText);

        // Assert
        assertEquals("The decoded text should match the expected value.", expectedDecodedText, decodedText);
    }
}