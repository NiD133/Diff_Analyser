package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("URLCodec.decode(Object)")
class URLCodecDecodeObjectTest {

    private URLCodec urlCodec;

    @BeforeEach
    void setUp() {
        urlCodec = new URLCodec();
    }

    @Test
    @DisplayName("should decode a URL-encoded string object")
    void decodeObjectWithStringShouldReturnDecodedString() throws DecoderException {
        // Arrange
        final String encodedString = "Hello+there%21";
        final String expectedDecodedString = "Hello there!";

        // Act
        final Object result = urlCodec.decode(encodedString);

        // Assert
        assertEquals(expectedDecodedString, result);
    }

    @Test
    @DisplayName("should decode a URL-encoded byte array object")
    void decodeObjectWithByteArrayShouldReturnDecodedByteArray() throws DecoderException {
        // Arrange
        final byte[] encodedBytes = "Hello+there%21".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedDecodedBytes = "Hello there!".getBytes(StandardCharsets.UTF_8);

        // Act
        final Object result = urlCodec.decode(encodedBytes);

        // Assert
        // We assert on byte arrays for a more precise and platform-independent test.
        assertArrayEquals(expectedDecodedBytes, (byte[]) result);
    }

    @Test
    @DisplayName("should return null when decoding a null object")
    void decodeObjectWithNullShouldReturnNull() throws DecoderException {
        // Act & Assert
        assertNull(urlCodec.decode(null));
    }

    @Test
    @DisplayName("should throw DecoderException for unsupported object types")
    void decodeObjectWithUnsupportedTypeShouldThrowException() {
        // Arrange
        final Double unsupportedObject = 3.0d;

        // Act & Assert
        assertThrows(DecoderException.class, () -> urlCodec.decode(unsupportedObject));
    }
}