package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec}.
 */
class URLCodecTest {

    @Test
    void encodeByteArrayWithNullInputShouldReturnNull() {
        // Arrange
        final URLCodec urlCodec = new URLCodec();

        // Act
        final byte[] result = urlCodec.encode((byte[]) null);

        // Assert
        assertNull(result, "Encoding a null byte array should return null.");
    }
}