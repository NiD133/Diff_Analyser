package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    @Test
    public void testEncodeObjectWithStringUsesDefaultCharset() throws EncoderException {
        // Arrange
        final String plainText = " encoded content";
        
        // The expected format is defined by RFC 1522: "=?charset?B?encoded_text?="
        // 'B' signifies Base64 encoding.
        // The default BCodec constructor uses UTF-8 as the charset.
        final String expectedEncodedText = "=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=";
        
        final BCodec bCodec = new BCodec();

        // Act
        // We cast the input to Object to specifically test the encode(Object) method overload.
        final Object result = bCodec.encode((Object) plainText);

        // Assert
        assertEquals(expectedEncodedText, result);
    }
}