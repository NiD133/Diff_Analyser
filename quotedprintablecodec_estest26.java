package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * This test suite focuses on specific behaviors of the QuotedPrintableCodec class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that attempting to encode a string with a QuotedPrintableCodec
     * that was initialized with a null charset results in a NullPointerException.
     * The encode method requires a valid charset to convert the input string into bytes.
     */
    @Test(expected = NullPointerException.class)
    public void encodeStringWithNullCharsetShouldThrowNullPointerException() throws EncoderException {
        // Arrange: Create a codec instance with a null charset.
        final QuotedPrintableCodec codec = new QuotedPrintableCodec((Charset) null);
        final String input = "any string";

        // Act: Attempting to encode the string.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        codec.encode(input);
    }
}