package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.Charset;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the encode(String, Charset) method returns null when the input string is null.
     */
    @Test
    public void encodeStringWithCharsetShouldReturnNullForNullInput() {
        // Arrange
        // The specific charset and 'strict' mode do not affect the outcome for a null input.
        QuotedPrintableCodec codec = new QuotedPrintableCodec(Charset.defaultCharset(), true);
        String nullString = null;

        // Act
        String result = codec.encode(nullString, Charset.defaultCharset());

        // Assert
        assertNull("Encoding a null string should return null.", result);
    }
}