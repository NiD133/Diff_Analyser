package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Base16 class, focusing on different configurations.
 */
public class Base16Test {

    /**
     * Verifies that when the Base16 encoder is constructed with the lower-case
     * option, it correctly encodes data into a lower-case hexadecimal string.
     */
    @Test
    void testEncodeWithLowerCaseAlphabet() {
        // Arrange
        // The Base16TestData class provides the expected lower-case encoded string.
        final String expectedEncodedString = Base16TestData.ENCODED_UTF8_LOWERCASE;
        // The BaseNTestData class provides the raw bytes to be encoded.
        final byte[] decodedBytes = BaseNTestData.DECODED;
        // Create a Base16 encoder configured to use a lower-case alphabet.
        final Base16 base16 = new Base16(true);

        // Act
        // Encode the raw bytes into a new byte array.
        final byte[] encodedBytes = base16.encode(decodedBytes);
        // Convert the resulting bytes to a string for comparison.
        final String actualEncodedString = new String(encodedBytes, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString,
            "Encoding with the lower-case option should produce a lower-case hexadecimal string.");
    }
}