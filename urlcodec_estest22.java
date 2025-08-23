package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test case for the URLCodec class, focusing on exception handling.
 */
public class URLCodecTest {

    /**
     * Tests that calling encode() on a URLCodec instance initialized with an
     * unsupported character set throws an EncoderException, which is caused by
     * an UnsupportedEncodingException.
     */
    @Test
    public void testEncodeWithUnsupportedCharsetThrowsEncoderException() {
        // Arrange: Create a URLCodec with a deliberately invalid charset name.
        final String invalidCharset = "This-is-an-unsupported-charset";
        final URLCodec urlCodec = new URLCodec(invalidCharset);
        final String originalString = "test string";

        // Act & Assert
        try {
            urlCodec.encode(originalString);
            fail("Expected EncoderException to be thrown due to unsupported charset.");
        } catch (final EncoderException e) {
            // Verify that the exception is of the expected type and has the correct cause.
            assertNotNull("The exception's cause should not be null.", e.getCause());
            assertTrue("The cause should be an UnsupportedEncodingException.",
                    e.getCause() instanceof UnsupportedEncodingException);
            assertEquals("The cause's message should be the invalid charset name.",
                    invalidCharset, e.getCause().getMessage());
        }
    }
}