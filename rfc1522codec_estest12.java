package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Tests for the RFC1522Codec class, focusing on decoding with invalid inputs.
 */
public class RFC1522CodecTest {

    /**
     * Tests that decodeText() throws an UnsupportedEncodingException when the
     * charset specified in the encoded string is invalid or unsupported.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void decodeTextWithInvalidCharsetShouldThrowUnsupportedEncodingException()
            throws DecoderException, UnsupportedEncodingException {
        // Arrange
        // The RFC 1522 encoded string format is: "=?charset?encoding?encoded-text?="
        // The test input provides "TF-", which is not a valid charset name.
        final String encodedTextWithInvalidCharset = "=?TF-?B??=?=";
        final BCodec bCodec = new BCodec();

        // Act & Assert
        // The decodeText method is expected to fail by throwing an UnsupportedEncodingException.
        // The assertion is handled declaratively by the @Test(expected=...) annotation.
        bCodec.decodeText(encodedTextWithInvalidCharset);
    }
}