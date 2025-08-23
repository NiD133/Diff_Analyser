package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding an empty string correctly results in an empty string.
     * This is an important edge case to ensure the codec handles empty input gracefully.
     */
    @Test
    public void testDecodeEmptyStringReturnsEmptyString() throws DecoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "";
        final String expected = "";
        // Using the system's default charset, as in the original test.
        final Charset charset = Charset.defaultCharset();

        // Act
        final String result = codec.decode(input, charset);

        // Assert
        assertEquals("Decoding an empty string should result in an empty string.", expected, result);
    }
}