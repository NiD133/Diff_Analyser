package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Base16} class, focusing on decoding known Base16 (hex) strings.
 */
public class Base16Test {

    /**
     * Provides a stream of arguments for the parameterized test.
     * Each argument consists of a Base16 encoded string and its expected decoded plain text representation.
     *
     * @return A stream of {@link Arguments} for the test.
     */
    private static Stream<Arguments> knownDecodings() {
        return Stream.of(
            Arguments.of("54686520717569636b2062726f776e20666f78206a756d706564206f76657220746865206c617a7920646f67732e", "The quick brown fox jumped over the lazy dogs."),
            Arguments.of("497420776173207468652062657374206f662074696d65732c206974207761732074686520776f727374206f662074696d65732e", "It was the best of times, it was the worst of times."),
            Arguments.of("687474703a2f2f6a616b617274612e6170616368652e6f72672f636f6d6d6d6f6e73", "http://jakarta.apache.org/commmons"),
            Arguments.of("4161426243634464456546664767486849694a6a4b6b4c6c4d6d4e6e4f6f50705171527253735474557556765777587859795a7a", "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz"),
            Arguments.of("7b20302c20312c20322c20332c20342c20352c20362c20372c20382c2039207d", "{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }"),
            Arguments.of("78797a7a7921", "xyzzy!")
        );
    }

    /**
     * Tests that decoding a Base16 (hex) encoded string produces the correct original string.
     * This test is parameterized to run for a set of well-known encoded/decoded string pairs.
     *
     * @param encodedString       The Base16 encoded input string (uppercase hex).
     * @param expectedDecodedString The expected plain text string after decoding.
     */
    @ParameterizedTest(name = "decoding \"{1}\"")
    @MethodSource("knownDecodings")
    void testDecodeWithKnownHexStrings(final String encodedString, final String expectedDecodedString) {
        // Arrange
        // Use the default Base16 constructor, which expects an uppercase alphabet, matching our test data.
        final Base16 base16 = new Base16();
        final byte[] encodedBytes = encodedString.getBytes(StandardCharsets.UTF_8);

        // Act
        final byte[] decodedBytes = base16.decode(encodedBytes);
        final String actualDecodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedDecodedString, actualDecodedString);
    }
}