package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// The class name and inheritance from the original test are preserved for context.
public class URLCodec_ESTestTest4 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that decoding with a URLCodec initialized with an invalid charset
     * throws a DecoderException.
     */
    @Test
    public void decodeWithInvalidCharsetShouldThrowDecoderException() {
        // Arrange: A URLCodec is instantiated with an invalid charset (an empty string).
        // The Java platform does not support an empty string as a valid encoding name.
        URLCodec urlCodec = new URLCodec("");

        try {
            // Act: Attempt to decode a string, which will internally use the invalid charset.
            urlCodec.decode("");
            fail("Expected a DecoderException to be thrown due to the invalid charset.");
        } catch (DecoderException e) {
            // Assert: The thrown exception should be caused by an UnsupportedEncodingException,
            // confirming that the failure is due to the bad charset.
            Throwable cause = e.getCause();
            assertNotNull("The DecoderException should have a cause.", cause);
            assertTrue("The cause should be an instance of UnsupportedEncodingException.",
                    cause instanceof UnsupportedEncodingException);
        }
    }
}