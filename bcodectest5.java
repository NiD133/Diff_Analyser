package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BCodec#decode(Object)} method.
 */
@DisplayName("BCodec decode(Object)")
class BCodecTest {

    private BCodec bCodec;

    @BeforeEach
    void setUp() {
        this.bCodec = new BCodec();
    }

    @Test
    @DisplayName("should decode a valid RFC 1522 encoded string object")
    void decodeValidStringObjectReturnsDecodedString() throws DecoderException {
        // Arrange
        final String encodedString = "=?UTF-8?B?d2hhdCBub3Q=?=";
        final String expectedDecodedString = "what not";

        // Act
        final Object result = bCodec.decode((Object) encodedString);

        // Assert
        assertEquals(expectedDecodedString, result);
    }

    @Test
    @DisplayName("should return null when decoding a null object")
    void decodeNullObjectReturnsNull() throws DecoderException {
        // Arrange
        final Object input = null;

        // Act
        final Object result = bCodec.decode(input);

        // Assert
        assertNull(result, "Decoding a null Object should return null");
    }

    @Test
    @DisplayName("should throw DecoderException for non-string object input")
    void decodeInvalidObjectTypeThrowsDecoderException() {
        // Arrange
        final Double invalidInput = 3.0d;

        // Act & Assert
        final DecoderException e = assertThrows(DecoderException.class, () -> {
            bCodec.decode(invalidInput);
        });
        
        assertEquals("Objects of type java.lang.Double cannot be decoded", e.getMessage());
    }
}