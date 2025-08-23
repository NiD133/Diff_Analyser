package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for UTF-8 encoding and decoding using BCodec.
 */
class BCodecUtf8Test {

    /**
     * Provides test cases for UTF-8 strings.
     * Each argument set contains the original plain text string and its expected
     * RFC 1522 'B' encoded representation.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> utf8StringProvider() {
        return Stream.of(
            Arguments.of(
                "Grüezi_zämä", // Swiss-German
                "=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?="
            ),
            Arguments.of(
                "Всем_привет", // Russian
                "=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?="
            )
        );
    }

    @ParameterizedTest
    @MethodSource("utf8StringProvider")
    void shouldEncodeAndDecodeUtf8StringCorrectly(final String originalString, final String expectedEncodedString)
            throws EncoderException, DecoderException {
        // Arrange
        final BCodec bCodec = new BCodec(StandardCharsets.UTF_8);

        // Act: Encode the original string
        final String actualEncodedString = bCodec.encode(originalString);

        // Assert: Verify the encoded string matches the expected format
        assertEquals(expectedEncodedString, actualEncodedString, "The encoded string should match the expected RFC 1522 format.");

        // Act: Decode the encoded string to test the round-trip
        final String decodedString = bCodec.decode(actualEncodedString);

        // Assert: Verify the decoded string is identical to the original
        assertEquals(originalString, decodedString, "The decoded string should be identical to the original string after a round-trip.");
    }
}