package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Base16} class.
 */
public class Base16Test {

    /**
     * Tests that the Base16 encoder correctly converts a byte array
     * into its uppercase hexadecimal string representation.
     */
    @Test
    void shouldEncodeByteArrayAsUpperCaseHexString() {
        // Arrange
        final String plainText = "Hello World";
        final byte[] dataToEncode = plainText.getBytes(StandardCharsets.UTF_8);
        final String expectedHexString = "48656C6C6F20576F726C64";
        final Base16 base16Codec = new Base16();

        // Act
        final byte[] encodedBytes = base16Codec.encode(dataToEncode);
        final String actualHexString = new String(encodedBytes, StandardCharsets.US_ASCII);

        // Assert
        assertEquals(expectedHexString, actualHexString);
    }
}