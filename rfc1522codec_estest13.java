package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BCodec} class, focusing on its RFC 1522 encoding functionality.
 */
public class BCodecTest {

    /**
     * Tests that the {@link BCodec#encodeText(String, String)} method correctly encodes a simple
     * ASCII string into the RFC 1522 "encoded-word" format using Base64 encoding.
     */
    @Test
    public void testEncodeTextWithSimpleAsciiString() throws Exception {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String plainText = "@~_=I";
        final String charset = "UTF-8";

        // The expected RFC 1522 format is "=?charset?encoding?encoded-text?=".
        // For BCodec, the encoding is 'B' (Base64).
        // The Base64 encoding of "@~_=I" is "QH5fPUk=".
        final String expectedEncodedString = "=?UTF-8?B?QH5fPUk=?=";

        // Act
        final String actualEncodedString = bCodec.encodeText(plainText, charset);

        // Assert
        assertEquals("The encoded string should match the expected RFC 1522 format.",
                     expectedEncodedString, actualEncodedString);
    }
}