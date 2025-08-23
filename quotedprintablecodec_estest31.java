package org.apache.commons.codec.net;

import org.junit.Test;

/**
 * Test suite for the QuotedPrintableCodec class, focusing on exception handling.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the decode(String, String) method throws a NullPointerException
     * when the provided charset name is null. This is the expected behavior as
     * the underlying {@link java.nio.charset.Charset#forName(String)} method
     * throws a NullPointerException for a null input.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeWithNullCharsetNameThrowsNullPointerException() {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        // The actual content of the input string is irrelevant for this test.
        String anyEncodedString = "some-text";

        // Act & Assert
        // Calling decode with a null charset name should trigger the exception.
        codec.decode(anyEncodedString, (String) null);
    }
}