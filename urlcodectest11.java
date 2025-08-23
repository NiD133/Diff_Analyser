package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec}.
 */
class URLCodecTest {

    @Test
    void encodeStringShouldReturnNullForNullInput() throws UnsupportedEncodingException {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String nullInput = null;

        // Act
        final String result = urlCodec.encode(nullInput, StandardCharsets.UTF_8.name());

        // Assert
        assertNull(result, "Encoding a null string should result in null.");
    }
}