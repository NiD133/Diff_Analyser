package org.apache.commons.codec.net;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Test suite for the QuotedPrintableCodec class, focusing on exception handling.
 *
 * Note: The original test class name "QuotedPrintableCodec_ESTestTest25" and its
 * base class have been replaced to improve clarity and focus on a standard,
 * human-written test structure.
 */
public class QuotedPrintableCodecTest {

    /**
     * Verifies that the encode(String, String) method throws an UnsupportedEncodingException
     * when provided with an invalid or unsupported character set name.
     */
    @Test(expected = UnsupportedEncodingException.class)
    public void encodeStringWithInvalidCharsetNameShouldThrowUnsupportedEncodingException() throws UnsupportedEncodingException {
        // Arrange: Set up the test conditions
        final String invalidCharsetName = "?*Fid=v-(J"; // An example of an illegal charset name
        final String inputText = "This is some text to be encoded.";
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act & Assert: Execute the method and verify the expected exception is thrown
        // The encode method is expected to throw the exception due to the invalid charset.
        codec.encode(inputText, invalidCharsetName);
    }
}