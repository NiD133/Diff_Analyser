package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the encode(Object) method correctly encodes a String
     * by replacing spaces with '+' characters.
     */
    @Test
    public void encodeObjectWithStringShouldReplaceSpacesWithPlus() throws EncoderException {
        // Arrange
        String plainText = " cannot be URL decoded";
        String expectedEncodedText = "+cannot+be+URL+decoded";
        URLCodec urlCodec = new URLCodec();

        // Act
        Object result = urlCodec.encode(plainText);

        // Assert
        assertEquals(expectedEncodedText, result);
    }
}