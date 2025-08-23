package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    @Test
    void encodeByteArrayShouldReturnNullForNullInput() {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();

        // Act
        final byte[] result = codec.encode(null);

        // Assert
        assertNull(result, "Encoding a null byte array should return null.");
    }
}