package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;

/**
 * Tests for the QuotedPrintableCodec class, focusing on exception handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encode(String, Charset) throws a NullPointerException when the provided Charset is null.
     * This is the expected behavior as the method relies on the Charset to convert the input string to bytes.
     */
    @Test(expected = NullPointerException.class)
    public void encodeStringWithNullCharsetShouldThrowNullPointerException() {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String plainText = "This is a test string.";

        // Act
        // The cast to Charset is necessary to resolve method ambiguity between
        // encode(String, Charset) and encode(String, String).
        codec.encode(plainText, (Charset) null);

        // Assert (is handled by the 'expected' attribute of the @Test annotation)
    }
}