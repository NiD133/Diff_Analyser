package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the generic decode(Object) method in QuotedPrintableCodec.
 * This suite verifies the method's behavior with valid inputs (String, byte[]),
 * null input, and unsupported object types.
 */
class QuotedPrintableCodecObjectDecodingTest {

    private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    @DisplayName("decode(Object) should correctly decode a String object")
    void decodeObjectShouldSucceedForStringInput() throws DecoderException {
        // Arrange
        final String encodedString = "1+1 =3D 2";
        final String expectedDecodedString = "1+1 = 2";

        // Act
        final String actualDecodedString = (String) codec.decode((Object) encodedString);

        // Assert
        assertEquals(expectedDecodedString, actualDecodedString);
    }

    @Test
    @DisplayName("decode(Object) should correctly decode a byte[] object")
    void decodeObjectShouldSucceedForByteArrayInput() throws DecoderException {
        // Arrange
        final byte[] encodedBytes = "1+1 =3D 2".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedDecodedBytes = "1+1 = 2".getBytes(StandardCharsets.UTF_8);

        // Act
        final byte[] actualDecodedBytes = (byte[]) codec.decode((Object) encodedBytes);

        // Assert
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes);
    }

    @Test
    @DisplayName("decode(Object) should return null when the input is null")
    void decodeObjectShouldReturnNullForNullInput() throws DecoderException {
        // Act
        final Object result = codec.decode(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("decode(Object) should throw DecoderException for unsupported input types")
    void decodeObjectShouldThrowExceptionForUnsupportedInputType() {
        // Arrange
        final Double unsupportedObject = 3.0d;

        // Act & Assert
        final DecoderException exception = assertThrows(DecoderException.class, () -> {
            codec.decode(unsupportedObject);
        });

        // Verify the exception message for robustness and clarity.
        final String expectedMessage = "Objects of type " + unsupportedObject.getClass().getName() + " cannot be decoded";
        assertEquals(expectedMessage, exception.getMessage());
    }
}