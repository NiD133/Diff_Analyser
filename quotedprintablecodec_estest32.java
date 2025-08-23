package org.apache.commons.codec.net;

import org.junit.Test;
import java.io.UnsupportedEncodingException;

/**
 * Test suite for the QuotedPrintableCodec class, focusing on exception handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a string with an unsupported character encoding throws
     * an UnsupportedEncodingException.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void decodeWithUnsupportedEncodingShouldThrowException() throws Exception {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String encodedText = "This content does not matter for this test";
        // The second argument to decode() is the charset, and this is not a valid name.
        final String invalidCharsetName = "!CM4Ko)*1";

        // Act
        // This call is expected to throw an exception because the charset is not supported.
        codec.decode(encodedText, invalidCharsetName);

        // Assert: The test passes if UnsupportedEncodingException is thrown,
        // which is handled by the 'expected' attribute of the @Test annotation.
    }
}