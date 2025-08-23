package org.apache.commons.io.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.OutputStream;
import java.nio.charset.UnsupportedCharsetException;
import org.junit.Test;

/**
 * Tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the {@link XmlStreamWriter} constructor throws an
     * {@link UnsupportedCharsetException} when an invalid encoding name is provided.
     */
    @Test
    public void constructorWithInvalidEncodingShouldThrowUnsupportedCharsetException() {
        final String invalidEncoding = "wm";

        try {
            // Attempt to create a writer with a null stream and an unsupported encoding.
            // The exception is expected due to the invalid encoding.
            new XmlStreamWriter((OutputStream) null, invalidEncoding);
            fail("Expected UnsupportedCharsetException was not thrown for invalid encoding: " + invalidEncoding);
        } catch (final UnsupportedCharsetException e) {
            // The exception is expected.
            // Verify that the exception message correctly identifies the invalid encoding.
            assertEquals(invalidEncoding, e.getMessage());
        }
    }
}