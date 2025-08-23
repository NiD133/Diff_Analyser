package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the URLCodec class, focusing on decoding error scenarios.
 */
public class URLCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when it encounters a '%'
     * character that is not followed by two valid hexadecimal digits.
     * In this case, it's followed by a null byte.
     */
    @Test
    public void decodeWithInvalidHexCharacterThrowsException() {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        // A URL-encoded escape sequence must be a '%' followed by two hex digits.
        // Here, '%' is followed by a null byte (value 0), which is not a valid hex digit.
        byte[] invalidUrlEncodedBytes = new byte[]{'%', 0};
        String expectedMessage = "Invalid URL encoding: not a valid digit (radix 16): 0";

        // Act & Assert
        try {
            urlCodec.decode(invalidUrlEncodedBytes);
            fail("Expected a DecoderException to be thrown for an invalid escape sequence.");
        } catch (DecoderException e) {
            // Verify that the exception message correctly identifies the issue.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}