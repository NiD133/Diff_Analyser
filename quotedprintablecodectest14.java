package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the QuotedPrintableCodec, focusing on its handling of characters
 * that should not be encoded.
 */
class QuotedPrintableCodecTest {

    /**
     * According to RFC 1521, all printable ASCII characters (decimal 33-126)
     * except for '=' (decimal 61) are considered "safe" and do not require encoding.
     * This string includes a comprehensive set of such characters.
     */
    private static final String SAFE_CHARACTERS = "abc123_-.*~!@#$%^&()+{}\"\\;:`,/[]";

    private QuotedPrintableCodec codec;

    @BeforeEach
    void setUp() {
        // Using the default constructor which uses UTF-8 and non-strict mode.
        codec = new QuotedPrintableCodec();
    }

    @Test
    @DisplayName("Safe characters should remain unchanged after encoding and decoding")
    void safeCharactersShouldRemainUnchangedAfterEncodingAndDecoding() throws EncoderException, DecoderException {
        // This test verifies two things:
        // 1. Encoding a string of "safe" characters results in an identical string.
        // 2. Decoding that string returns the original string, confirming the round-trip integrity.

        // Act: Encode the string of safe characters.
        final String encodedString = codec.encode(SAFE_CHARACTERS);

        // Assert: The encoded string should be identical to the original.
        assertEquals(SAFE_CHARACTERS, encodedString,
            "Encoding safe characters should not alter the original string.");

        // Act: Decode the (unaltered) encoded string.
        final String decodedString = codec.decode(encodedString);

        // Assert: The decoded string should also be identical to the original.
        assertEquals(SAFE_CHARACTERS, decodedString,
            "Decoding the result of an safe-character encoding should yield the original string.");
    }
}