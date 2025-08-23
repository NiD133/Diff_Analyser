package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Base16#encodeToString(byte[])} method.
 */
class Base16EncodeToStringTest {

    private Base16 base16;

    @BeforeEach
    void setUp() {
        base16 = new Base16();
    }

    @Test
    @DisplayName("Encoding a non-empty byte array should produce the correct hex string")
    void testEncodeToStringWithNonEmptyInput() {
        // Arrange
        final String plainText = "Hello World";
        final byte[] plainBytes = StringUtils.getBytesUtf8(plainText);
        final String expectedEncodedString = "48656C6C6F20576F726C64";

        // Act
        final String actualEncodedString = base16.encodeToString(plainBytes);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString);
    }

    @Test
    @DisplayName("Encoding an empty byte array should result in an empty string")
    void testEncodeToStringWithEmptyInput() {
        // Arrange
        final byte[] emptyBytes = {};

        // Act
        final String actualEncodedString = base16.encodeToString(emptyBytes);

        // Assert
        assertEquals("", actualEncodedString);
    }

    @Test
    @DisplayName("Encoding a null byte array should result in null")
    void testEncodeToStringWithNullInput() {
        // Arrange
        final byte[] nullBytes = null;

        // Act
        final String actualEncodedString = base16.encodeToString(nullBytes);

        // Assert
        assertNull(actualEncodedString);
    }
}