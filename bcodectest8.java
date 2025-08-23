package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@code encode(Object)} method of the {@link BCodec} class.
 */
@DisplayName("BCodec encode(Object)")
class BCodecObjectEncodingTest {

    private BCodec bCodec;

    @BeforeEach
    void setUp() {
        bCodec = new BCodec();
    }

    @Test
    @DisplayName("should correctly B-encode a String object")
    void testEncodeStringObject() throws EncoderException {
        // Arrange
        final String plainText = "what not";
        final String expectedEncodedText = "=?UTF-8?B?d2hhdCBub3Q=?=";

        // Act
        final Object result = bCodec.encode((Object) plainText);

        // Assert
        assertEquals(expectedEncodedText, result);
    }

    @Test
    @DisplayName("should return null when encoding a null object")
    void testEncodeNullObject() throws EncoderException {
        // Act
        final Object result = bCodec.encode(null);

        // Assert
        assertNull(result, "Encoding a null Object should return null");
    }

    @Test
    @DisplayName("should throw EncoderException when encoding an unsupported object type")
    void testEncodeUnsupportedObjectThrowsException() {
        // Arrange
        final Double unsupportedObject = 3.0d;

        // Act & Assert
        assertThrows(EncoderException.class, () -> {
            bCodec.encode(unsupportedObject);
        }, "Encoding an object of an unsupported type (e.g., Double) should fail.");
    }
}