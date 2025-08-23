package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Test suite for the URLCodec class.
 */
public class URLCodecTest {

    /**
     * Tests that {@link URLCodec#decodeUrl(byte[])} throws a DecoderException when the
     * input byte array ends with a single percent sign ('%'). A lone '%' is an
     * incomplete escape sequence, as it must be followed by two hexadecimal digits.
     */
    @Test
    public void decodeUrlShouldThrowExceptionForIncompleteEscapeSequence() {
        // A byte array containing just the escape character '%' is an invalid encoding.
        byte[] bytesWithIncompleteSequence = {'%'};

        try {
            URLCodec.decodeUrl(bytesWithIncompleteSequence);
            fail("Expected DecoderException to be thrown for an incomplete escape sequence.");
        } catch (final DecoderException e) {
            // The exception is expected. We verify its message to ensure it's thrown for the correct reason.
            final String expectedMessage = "Invalid URL encoding: Incomplete trailing escape (%) sequence";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}