package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the Base16 class, focusing on its encoding and decoding capabilities
 * as defined in RFC 4648.
 *
 * @see Base16
 */
public class Base16Test {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    /**
     * Tests the default Base16 encoding, which uses an upper-case alphabet.
     */
    @Test
    void testEncodeUsingDefaultUpperCaseAlphabet() {
        // Arrange
        final String plainText = "Hello World";
        final String expectedEncodedString = "48656C6C6F20576F726C64";
        final Base16 base16 = new Base16(); // Defaults to upper-case alphabet

        // Act
        final byte[] plainTextBytes = plainText.getBytes(CHARSET_UTF8);
        final byte[] encodedBytes = base16.encode(plainTextBytes);
        final String actualEncodedString = new String(encodedBytes, CHARSET_UTF8);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString,
            "Encoding should produce the correct upper-case Base16 string.");
    }

    /**
     * Tests the default Base16 decoding, which handles an upper-case alphabet.
     *
     * @throws DecoderException if the input is not a valid Base16 string.
     */
    @Test
    void testDecodeUsingDefaultUpperCaseAlphabet() throws DecoderException {
        // Arrange
        final String encodedString = "48656C6C6F20576F726C64";
        final String expectedPlainText = "Hello World";
        final Base16 base16 = new Base16();

        // Act
        final byte[] encodedBytes = encodedString.getBytes(CHARSET_UTF8);
        final byte[] decodedBytes = base16.decode(encodedBytes);
        final String actualPlainText = new String(decodedBytes, CHARSET_UTF8);

        // Assert
        assertEquals(expectedPlainText, actualPlainText,
            "Decoding a Base16 string should produce the original text.");
    }

    /**
     * Tests that encoding and then decoding data returns the original data,
     * verifying the codec's property of invertibility.
     *
     * @throws DecoderException if the encoded data cannot be decoded.
     */
    @Test
    void testEncodeDecodeRoundTrip() throws DecoderException {
        // Arrange
        final String originalString = "The quick brown fox jumps over the lazy dog.";
        final byte[] originalBytes = originalString.getBytes(CHARSET_UTF8);
        final Base16 base16 = new Base16();

        // Act
        final byte[] encodedBytes = base16.encode(originalBytes);
        final byte[] decodedBytes = base16.decode(encodedBytes);

        // Assert
        assertArrayEquals(originalBytes, decodedBytes,
            "The decoded bytes should be identical to the original bytes.");
    }

    /**
     * Tests the {@link Base16#encode(byte[], int, int)} method, which encodes a sub-array.
     * This ensures that the offset and length parameters are correctly handled by encoding
     * only a specific slice of a larger buffer.
     *
     * @param startPadSize the number of bytes to prepend to the test data.
     * @param endPadSize   the number of bytes to append to the test data.
     */
    @ParameterizedTest
    @CsvSource({
        "0, 0", // No padding, regular case
        "3, 0", // Padding at the start only
        "0, 5", // Padding at the end only
        "3, 5"  // Padding at both ends
    })
    void testEncodeWithOffsetAndLength(final int startPadSize, final int endPadSize) {
        // Arrange
        final String plainText = "Hello World";
        final String expectedHex = "48656C6C6F20576F726C64";
        final Base16 base16 = new Base16();

        final byte[] textBytes = plainText.getBytes(CHARSET_UTF8);

        // Create a larger buffer with the textBytes in the middle, surrounded by padding.
        final byte[] buffer = new byte[startPadSize + textBytes.length + endPadSize];
        System.arraycopy(textBytes, 0, buffer, startPadSize, textBytes.length);

        // Act: Encode only the relevant part of the buffer
        final byte[] encodedBytes = base16.encode(buffer, startPadSize, textBytes.length);
        final String actualHex = new String(encodedBytes, CHARSET_UTF8);

        // Assert
        assertEquals(expectedHex, actualHex,
            "Encoding a sub-array should ignore surrounding data in the buffer.");
    }
}