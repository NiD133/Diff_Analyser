package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests the String decoding methods of the {@link Base16} class.
 */
public class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private final Base16 base16 = new Base16();

    /**
     * Tests that decoding a valid Base16 (hex) encoded string returns the correct
     * original data as a byte array.
     */
    @Test
    void decode_shouldReturnDecodedBytes_whenGivenValidString() {
        // Arrange
        final String encodedString = "48656C6C6F20576F726C64";
        final byte[] expectedDecodedBytes = "Hello World".getBytes(CHARSET_UTF8);

        // Act
        final byte[] actualDecodedBytes = base16.decode(encodedString);

        // Assert
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes);
    }

    /**
     * Tests that decoding an empty string results in an empty byte array.
     */
    @Test
    void decode_shouldReturnEmptyArray_whenGivenEmptyString() {
        // Arrange
        final String emptyString = "";
        final byte[] expectedBytes = new byte[0];

        // Act
        final byte[] actualBytes = base16.decode(emptyString);

        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Tests that decoding a null string returns null, as per the API contract.
     */
    @Test
    void decode_shouldReturnNull_whenGivenNullString() {
        // Act & Assert
        assertNull(base16.decode((String) null));
    }

    /**
     * Tests the generic {@link Base16#decode(Object)} method with a valid Base16
     * encoded string. The method should correctly identify the input as a String
     * and decode it.
     *
     * @throws DecoderException if the object is not a String or byte array.
     */
    @Test
    void decodeObject_shouldReturnDecodedBytes_whenGivenValidString() throws DecoderException {
        // Arrange
        final Object encodedObject = "48656C6C6F20576F726C64"; // Stored as Object
        final byte[] expectedDecodedBytes = "Hello World".getBytes(CHARSET_UTF8);

        // Act
        final Object resultObject = base16.decode(encodedObject);

        // Assert
        assertTrue(resultObject instanceof byte[], "Result should be a byte array");
        assertArrayEquals(expectedDecodedBytes, (byte[]) resultObject);
    }
}