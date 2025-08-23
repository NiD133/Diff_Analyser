package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Test suite for the RFC1522Codec class, focusing on encoding behavior.
 */
public class RFC1522CodecTest {

    /**
     * Verifies that encodeText() throws an UnsupportedCharsetException when provided
     * with a charset name that is not supported by the Java runtime.
     */
    @Test
    public void encodeTextWithInvalidCharsetShouldThrowUnsupportedCharsetException() throws EncoderException {
        // Arrange: Create a codec instance and define test data, including an
        // obviously invalid charset name.
        final BCodec codec = new BCodec();
        final String textToEncode = "This is some test text.";
        final String invalidCharsetName = "non-existent-charset-123";

        // Act & Assert: Attempt to encode with the invalid charset and verify that the
        // correct exception is thrown.
        try {
            codec.encodeText(textToEncode, invalidCharsetName);
            fail("Expected an UnsupportedCharsetException to be thrown, but it was not.");
        } catch (final UnsupportedCharsetException e) {
            // This is the expected outcome.
            // Additionally, verify that the exception correctly reports the invalid charset name.
            assertEquals("The exception should contain the invalid charset name.", invalidCharsetName, e.getCharsetName());
        }
    }
}