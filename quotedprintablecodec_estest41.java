package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the decode(String, Charset) method throws a NullPointerException
     * when the provided Charset is null.
     */
    @Test(expected = NullPointerException.class)
    public void decodeWithNullCharsetShouldThrowNullPointerException() throws DecoderException {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "any-string";

        // Act & Assert
        // This call is expected to throw a NullPointerException because the Charset parameter is null.
        // The @Test(expected = ...) annotation handles the assertion.
        codec.decode(input, (Charset) null);
    }
}