package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link QuotedPrintableCodec} class, focusing on its behavior
 * when handling null input for string encoding.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that encoding a null String object returns null, which is the
     * expected behavior for encoders in this library.
     */
    @Test
    void shouldReturnNullWhenEncodingNullString() {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String nullInput = null;

        // Act
        // We use the encode(String, Charset) overload, which is safer and more modern.
        // The charset itself is not used when the input is null, but we provide a
        // standard one for correctness.
        final String encodedResult = codec.encode(nullInput, StandardCharsets.UTF_8);

        // Assert
        assertNull(encodedResult, "Encoding a null string should return null.");
    }
}