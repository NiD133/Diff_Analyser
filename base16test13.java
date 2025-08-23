package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests Base16 encoding and decoding with empty or null inputs.
 */
@DisplayName("Base16 - Empty and Null Input Tests")
class Base16EmptyOrNullInputTest {

    private final Base16 base16 = new Base16();

    @Test
    @DisplayName("Encoding an empty byte array should return an empty byte array")
    void encode_withEmptyArray_shouldReturnEmptyArray() {
        // Arrange
        final byte[] emptyArray = {};
        final byte[] expected = {};

        // Act
        final byte[] result = base16.encode(emptyArray);

        // Assert
        assertArrayEquals(expected, result, "Encoding an empty array should produce an empty array.");
    }

    @Test
    @DisplayName("Encoding a null array should return null")
    void encode_withNullArray_shouldReturnNull() {
        // Act
        final byte[] result = base16.encode((byte[]) null);

        // Assert
        assertNull(result, "Encoding a null array should return null.");
    }

    @Test
    @DisplayName("Decoding an empty byte array should return an empty byte array")
    void decode_withEmptyArray_shouldReturnEmptyArray() {
        // Arrange
        final byte[] emptyArray = {};
        final byte[] expected = {};

        // Act
        final byte[] result = base16.decode(emptyArray);

        // Assert
        assertArrayEquals(expected, result, "Decoding an empty array should produce an empty array.");
    }

    @Test
    @DisplayName("Decoding a null array should return null")
    void decode_withNullArray_shouldReturnNull() {
        // Act
        final byte[] result = base16.decode((byte[]) null);

        // Assert
        assertNull(result, "Decoding a null array should return null.");
    }

    @Test
    @DisplayName("Encoding a slice of an empty array should return an empty array")
    void encode_withEmptyArrayAndOffset_shouldReturnEmptyArray() {
        // Arrange
        final byte[] emptyArray = {};
        final byte[] expected = {};

        // Act
        // The BaseNCodec#encode(byte[], int, int) implementation returns an empty array
        // if the input array itself is empty, regardless of offset and length parameters.
        final byte[] result = base16.encode(emptyArray, 0, 1);

        // Assert
        assertArrayEquals(expected, result, "Encoding a slice of an empty array should be empty.");
    }
}