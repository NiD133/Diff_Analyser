package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link URLCodec}, focusing on handling of null inputs.
 */
class URLCodecTest {

    private URLCodec urlCodec;

    @BeforeEach
    void setUp() {
        urlCodec = new URLCodec();
    }

    @Test
    @DisplayName("Encoding a null String should return null")
    void encodeShouldReturnNullForNullInput() {
        // Act
        final String encoded = urlCodec.encode((String) null);

        // Assert
        assertNull(encoded);
    }

    @Test
    @DisplayName("Decoding a null String should return null")
    void decodeShouldReturnNullForNullInput() {
        // Act
        final String decoded = urlCodec.decode((String) null);

        // Assert
        assertNull(decoded);
    }
}