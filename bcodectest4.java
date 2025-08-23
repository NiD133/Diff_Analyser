package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BCodec}, focusing on the RFC 1522 "B" encoding scheme.
 */
class BCodecTest {

    private BCodec codec;

    @BeforeEach
    void setUp() {
        this.codec = new BCodec();
    }

    @Test
    @DisplayName("Should perform a basic round-trip (encode and decode) for a simple ASCII string")
    void shouldEncodeAndDecodeBasicAsciiString() throws EncoderException, DecoderException {
        // Arrange
        final String originalString = "Hello there";
        // The expected format is defined by RFC 1522: =?charset?encoding?encoded-text?=
        final String expectedEncodedString = "=?UTF-8?B?SGVsbG8gdGhlcmU=?=";

        // Act
        final String actualEncodedString = codec.encode(originalString);
        final String decodedString = codec.decode(actualEncodedString);

        // Assert
        assertEquals(expectedEncodedString, actualEncodedString, "Encoding should produce a valid RFC 1522 encoded-word");
        assertEquals(originalString, decodedString, "Decoding should return the original string");
    }
}