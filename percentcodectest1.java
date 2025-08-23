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
     * Tests that the default PercentCodec does not encode safe, unreserved characters.
     * The PercentCodec is a binary codec, so comparisons should be done on byte arrays.
     */
    @Test
    void shouldNotEncodeSafeAlphanumericCharacters() throws EncoderException, DecoderException {
        // Arrange: Create a default codec and an input string containing only safe characters.
        final PercentCodec percentCodec = new PercentCodec();
        final String originalString = "abcdABCD";
        final byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);

        // Act: Encode the bytes.
        final byte[] encodedBytes = percentCodec.encode(originalBytes);

        // Assert: The encoded bytes should be identical to the original, as no characters
        // in the input are subject to percent-encoding by the default codec.
        assertArrayEquals(originalBytes, encodedBytes,
            "Encoding safe characters should not change the data.");

        // Act & Assert for round-trip: Decode the result and verify it matches the original.
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        assertArrayEquals(originalBytes, decodedBytes,
            "Decoding the result should return the original data.");
    }
}