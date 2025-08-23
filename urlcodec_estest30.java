package org.apache.commons.codec.net;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Contains tests for the {@link URLCodec} class, focusing on exception handling.
 */
public class URLCodecTest {

    /**
     * Tests that the encode(String, String) method throws an UnsupportedEncodingException
     * when provided with a charset name that is not supported by the Java platform.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void encodeShouldThrowUnsupportedEncodingExceptionForInvalidCharset() throws UnsupportedEncodingException {
        // Arrange
        // Using a non-existent charset name to trigger the exception.
        // The name is deliberately chosen to be a valid class name from the same package
        // to ensure the check is on charset availability, not the string's format.
        final String invalidCharsetName = "org.apache.commons.codec.CharEncoding";
        final String plainText = "This is a test string.";
        
        // The URLCodec is instantiated with its default charset (UTF-8), which is
        // irrelevant here because the encode method's charset parameter takes precedence.
        final URLCodec urlCodec = new URLCodec();

        // Act & Assert
        // The following call is expected to throw an UnsupportedEncodingException
        // because the specified charset is not a valid encoding. The test will
        // pass if this specific exception is thrown.
        urlCodec.encode(plainText, invalidCharsetName);
    }
}