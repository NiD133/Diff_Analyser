package org.apache.commons.codec.net;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the encode(String, String) method throws a NullPointerException
     * when the provided charset name is null.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeStringWithNullCharsetNameThrowsNullPointerException() throws UnsupportedEncodingException {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String plainText = "This is a test string.";

        // Act & Assert
        // The encode method is expected to throw a NullPointerException because the charset name is null.
        // This is the specified behavior of the underlying java.lang.String.getBytes(String charsetName) method.
        codec.encode(plainText, (String) null);
    }
}