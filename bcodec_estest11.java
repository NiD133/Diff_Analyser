package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.UnsupportedCharsetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that encode() throws an EncoderException when provided with an unsupported character set name.
     * The underlying cause should be an UnsupportedCharsetException.
     */
    @Test
    public void encodeShouldThrowEncoderExceptionForUnsupportedCharset() {
        // Arrange
        BCodec bCodec = new BCodec();
        String inputText = "test data";
        String unsupportedCharset = "invalid-charset-name"; // This charset is guaranteed not to exist.

        // Act & Assert
        try {
            bCodec.encode(inputText, unsupportedCharset);
            fail("Expected EncoderException to be thrown for an unsupported charset.");
        } catch (EncoderException e) {
            // Verify that the exception message is the name of the unsupported charset.
            assertEquals("The exception message should be the invalid charset name.",
                    unsupportedCharset, e.getMessage());

            // Verify that the cause of the exception is UnsupportedCharsetException.
            Throwable cause = e.getCause();
            assertNotNull("The cause of the EncoderException should not be null.", cause);
            assertTrue("The cause should be an instance of UnsupportedCharsetException.",
                    cause instanceof UnsupportedCharsetException);
        }
    }
}