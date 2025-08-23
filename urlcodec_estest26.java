package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the URLCodec class, focusing on decoding error handling.
 */
public class URLCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when it encounters
     * an invalid URL-encoded sequence. A valid sequence consists of a '%' character
     * followed by two hexadecimal digits.
     */
    @Test
    public void decodeWithInvalidHexSequenceThrowsDecoderException() throws UnsupportedEncodingException {
        // Arrange: Create a URLCodec instance and define the invalid input.
        URLCodec urlCodec = new URLCodec();

        // The input string is invalid because the '%' character is followed by 'k',
        // which is not a valid hexadecimal digit (0-9, a-f, A-F).
        final String invalidUrlEncodedString = "prefix-%ky-suffix";

        try {
            // Act: Attempt to decode the invalid string.
            // The charset argument ("UTF-8") is required by the method signature but is not
            // relevant to this specific failure, which occurs during byte parsing.
            urlCodec.decode(invalidUrlEncodedString, "UTF-8");

            // Assert: If no exception is thrown, the test should fail.
            fail("Expected a DecoderException to be thrown due to the invalid hex sequence.");
        } catch (DecoderException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            // The character 'k' has an ASCII value of 107. The codec reports this in the error.
            String expectedMessage = "Invalid URL encoding: not a valid digit (radix 16): 107";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}