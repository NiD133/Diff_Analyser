package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the Base16 class, focusing on encoding operations.
 */
public class Base16EncodingTest {

    private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

    private Base16 base16;

    @BeforeEach
    void setUp() {
        // Use the default Base16 encoder, which produces upper-case output.
        base16 = new Base16();
    }

    /**
     * Provides a stream of test cases with plain text and their known Base16 (hex) encoded representation.
     *
     * @return A stream of arguments for the parameterized test.
     */
    static Stream<Arguments> knownEncodings() {
        return Stream.of(
            Arguments.of("The quick brown fox jumped over the lazy dogs.", "54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e"),
            Arguments.of("It was the best of times, it was the worst of times.", "497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e"),
            Arguments.of("http://jakarta.apache.org/commmons", "687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73"),
            Arguments.of("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz", "4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a"),
            Arguments.of("{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }", "7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d"),
            Arguments.of("xyzzy!", "78797a7a7921")
        );
    }

    @DisplayName("Should encode well-known strings correctly")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @MethodSource("knownEncodings")
    void testEncodeWithKnownValues(final String plainText, final String expectedHex) {
        // Arrange
        final byte[] plainBytes = plainText.getBytes(CHARSET_UTF8);

        // Act
        final byte[] encodedBytes = base16.encode(plainBytes);
        final String actualHex = new String(encodedBytes, CHARSET_UTF8);

        // Assert
        assertEquals(expectedHex, actualHex);
    }

    @Test
    @DisplayName("Should encode a sub-array correctly, ignoring surrounding bytes")
    void testEncodeSubArrayIgnoringPadding() {
        // Arrange
        final String contentToEncode = "Hello World";
        final String expectedHex = "48656C6C6F20576F726C64";
        final byte[] contentBytes = contentToEncode.getBytes(CHARSET_UTF8);

        final int prefixPaddingSize = 5;
        final int suffixPaddingSize = 10;

        // Create a buffer with the content surrounded by padding bytes (zeros)
        final byte[] buffer = new byte[prefixPaddingSize + contentBytes.length + suffixPaddingSize];
        System.arraycopy(contentBytes, 0, buffer, prefixPaddingSize, contentBytes.length);

        // Act: Encode only the relevant part of the buffer
        final byte[] encodedBytes = base16.encode(buffer, prefixPaddingSize, contentBytes.length);
        final String actualHex = new String(encodedBytes, CHARSET_UTF8);

        // Assert
        assertEquals(expectedHex, actualHex, "Encoding a sub-array should ignore the padding bytes.");
    }
}