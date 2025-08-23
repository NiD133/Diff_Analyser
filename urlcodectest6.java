package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the static methods of the {@link URLCodec} class.
 */
class URLCodecTest {

    @Test
    @DisplayName("URLCodec.decodeUrl(null) should return null")
    void decodeUrlWithNullInputShouldReturnNull() {
        // Act
        final byte[] result = URLCodec.decodeUrl(null);

        // Assert
        assertNull(result, "Decoding a null byte array should result in null.");
    }
}