package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 */
class PercentCodecTest {

    /**
     * This test verifies that the PercentCodec correctly encodes a string containing
     * characters that require encoding (non-ASCII and the '%' character itself),
     * while leaving safe characters (like the space) untouched by default.
     */
    @Test
    void shouldEncodeNonAsciiAndSpecialCharacters() throws EncoderException {
        // Arrange
        // The input string contains Greek letters (non-ASCII), a percent sign, and a space.
        final String originalString = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";
        final byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);

        final String expectedEncodedString = "%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ";
        final byte[] expectedEncodedBytes = expectedEncodedString.getBytes(StandardCharsets.UTF_8);

        final PercentCodec percentCodec = new PercentCodec();

        // Act
        final byte[] actualEncodedBytes = percentCodec.encode(originalBytes);

        // Assert
        assertArrayEquals(expectedEncodedBytes, actualEncodedBytes);
    }

    /**
     * This test verifies that decoding the result of an encoding operation
     * returns the original byte array. This is often called a "round-trip" test
     * and ensures that the encode and decode operations are inverse of each other.
     */
    @Test
    void encodeAndDecodeShouldBeReversible() throws EncoderException, DecoderException {
        // Arrange
        final String originalString = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";
        final byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        final PercentCodec percentCodec = new PercentCodec();

        // Act
        final byte[] encodedBytes = percentCodec.encode(originalBytes);
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);

        // Assert
        assertArrayEquals(originalBytes, decodedBytes);
    }
}