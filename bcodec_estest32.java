package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the {@link BCodec#decode(Object)} method correctly decodes a valid
     * RFC 1522 B-encoded string. The input is provided as an Object.
     *
     * @throws DecoderException if the decoding process fails, which is not expected here.
     */
    @Test
    public void testDecodeValidBEncodedStringAsObject() throws DecoderException {
        // Arrange
        final BCodec bCodec = new BCodec();
        // This is an RFC 1522 B-encoded string for ",w.,p%+" using UTF-8.
        final String encodedString = "=?UTF-8?B?LHcuLHAlKw==?=";
        final String expectedDecodedString = ",w.,p%+";

        // Act
        final Object decodedResult = bCodec.decode((Object) encodedString);

        // Assert
        assertEquals("The decoded object should be the correctly decoded string.",
                     expectedDecodedString, decodedResult);
    }
}