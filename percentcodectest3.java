package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec} with custom configurations.
 */
class PercentCodecTest {

    @Test
    void shouldCorrectlyEncodeAndDecodeWithCustomSafeChars() throws EncoderException, DecoderException {
        // --- ARRANGE ---

        // The input contains a mix of characters to test different encoding rules:
        // - 'abc': Specified in the constructor to be ALWAYS encoded.
        // - '123_-.*': URI-safe characters that should NOT be encoded.
        // - 'αβ': Non-ASCII characters that should ALWAYS be encoded.
        final String originalString = "abc123_-.*\u03B1\u03B2";

        // The PercentCodec is configured to always encode 'a' through 'f', even though
        // they are normally considered safe. The 'plusForSpace' flag is false.
        final byte[] alwaysEncodeChars = "abcdef".getBytes(StandardCharsets.UTF_8);
        final PercentCodec percentCodec = new PercentCodec(alwaysEncodeChars, false);

        // Expected result after encoding:
        // - 'a' -> %61, 'b' -> %62, 'c' -> %63
        // - '123_-.*' -> remains unchanged
        // - 'α' (U+03B1) -> %CE%B1 (in UTF-8)
        // - 'β' (U+03B2) -> %CE%B2 (in UTF-8)
        final String expectedEncodedString = "%61%62%63123_-.*%CE%B1%CE%B2";

        // --- ACT & ASSERT: ENCODING ---
        final byte[] encodedBytes = percentCodec.encode(originalString.getBytes(StandardCharsets.UTF_8));
        final String actualEncodedString = new String(encodedBytes, StandardCharsets.UTF_8);

        assertEquals(expectedEncodedString, actualEncodedString, "Encoding should handle custom safe characters and non-ASCII correctly.");

        // --- ACT & ASSERT: DECODING (Round-trip check) ---
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        final String roundTripString = new String(decodedBytes, StandardCharsets.UTF_8);

        assertEquals(originalString, roundTripString, "Decoding should perfectly reverse the encoding to restore the original string.");
    }
}