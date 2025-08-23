package org.apache.commons.codec.net;

import org.junit.Test;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding an empty string results in an empty string,
     * which is the expected behavior for a no-op case.
     */
    @Test
    public void encodeEmptyStringShouldReturnEmptyString() {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String emptyString = "";

        // Act
        // The encode(String) method uses the codec's default charset (UTF-8).
        final String encodedResult = codec.encode(emptyString);

        // Assert
        assertEquals("Encoding an empty string should produce an empty string.", emptyString, encodedResult);
    }
}