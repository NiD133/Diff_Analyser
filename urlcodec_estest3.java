package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Test suite for the URLCodec class, focusing on decoding error handling.
 */
public class URLCodecTest {

    /**
     * Tests that the decode method throws a DecoderException when the input string
     * contains an incomplete URL escape sequence (e.g., a '%' character not
     * followed by two hexadecimal digits).
     */
    @Test
    public void decodeWithIncompleteEscapeSequenceThrowsDecoderException() {
        // Arrange: Create a URLCodec instance and define a malformed input string.
        // The malformed string has a '%' character at the end, which is not
        // followed by the required two hexadecimal digits.
        final URLCodec urlCodec = new URLCodec();
        final String malformedInput = "test%";

        // Act & Assert: Verify that decoding the malformed string throws a DecoderException.
        try {
            urlCodec.decode(malformedInput);
            fail("A DecoderException should have been thrown due to the incomplete escape sequence.");
        } catch (final DecoderException e) {
            // Check the exception message to ensure it's the one we expect for this specific error.
            assertEquals("Invalid URL encoding: Incomplete trailing escape (%) pattern", e.getMessage());

            // Also, verify that the cause of the exception is what we expect.
            // The implementation catches an ArrayIndexOutOfBoundsException and wraps it.
            assertNotNull("The cause of the exception should not be null.", e.getCause());
            assertEquals(ArrayIndexOutOfBoundsException.class, e.getCause().getClass());
        }
    }
}