package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for URLCodec focusing on UTF-8 encoding and decoding of multi-byte characters.
 */
class URLCodecTest {

    private final URLCodec urlCodec = new URLCodec(CharEncoding.UTF_8);

    /**
     * Provides pairs of plain and URL-encoded strings for testing.
     * The strings contain multi-byte characters to verify correct UTF-8 handling.
     *
     * @return A stream of arguments, each containing a plain string and its expected encoded form.
     */
    static Stream<Arguments> utf8StringProvider() {
        return Stream.of(
            // Test case with Russian (Cyrillic) characters
            Arguments.of("Всем_привет", "%D0%92%D1%81%D0%B5%D0%BC_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82"),
            // Test case with Swiss-German characters (with umlauts)
            Arguments.of("Grüezi_zämä", "Gr%C3%BCezi_z%C3%A4m%C3%A4")
        );
    }

    @DisplayName("Should encode strings with multi-byte characters correctly using UTF-8")
    @ParameterizedTest(name = "Encoding \"{0}\"")
    @MethodSource("utf8StringProvider")
    void shouldEncodeMultiByteStringsInUtf8(final String plainText, final String expectedEncodedText) throws EncoderException, UnsupportedEncodingException {
        // Act
        final String actualEncodedText = urlCodec.encode(plainText, CharEncoding.UTF_8);

        // Assert
        assertEquals(expectedEncodedText, actualEncodedText);
    }

    @DisplayName("Should correctly decode a string after encoding it (round-trip)")
    @ParameterizedTest(name = "Round-trip for \"{0}\"")
    @MethodSource("utf8StringProvider")
    void shouldPreserveStringAfterEncodeAndDecodeRoundTrip(final String originalString, final String encodedString) throws DecoderException, EncoderException, UnsupportedEncodingException {
        // Act
        final String decodedString = urlCodec.decode(encodedString, CharEncoding.UTF_8);

        // Assert
        assertEquals(originalString, decodedString);
    }

    @Test
    @DisplayName("Encoding and decoding should be symmetrical")
    void testRoundTripSymmetry() throws DecoderException, EncoderException, UnsupportedEncodingException {
        final String originalString = "Grüezi_zämä";

        // Act
        final String encoded = urlCodec.encode(originalString, CharEncoding.UTF_8);
        final String decoded = urlCodec.decode(encoded, CharEncoding.UTF_8);

        // Assert
        assertEquals(originalString, decoded, "String should be unchanged after an encode/decode round-trip.");
    }
}