package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the QuotedPrintableCodec, focusing on soft line break handling and round-trip integrity.
 */
class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec;

    @BeforeEach
    void setUp() {
        // Instantiate the codec for each test to ensure isolation.
        codec = new QuotedPrintableCodec();
    }

    @Test
    @DisplayName("Decoding a string with a soft line break (=\\r\\n) should remove the break")
    void shouldDecodeSoftLineBreak() throws DecoderException {
        // Arrange: A soft line break is represented by "=\r\n" in Quoted-Printable encoding.
        // It's used to wrap long lines without affecting the decoded content.
        final String encodedTextWithSoftLineBreak = "If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.";
        final String expectedDecodedText = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";

        // Act
        final String actualDecodedText = codec.decode(encodedTextWithSoftLineBreak);

        // Assert
        assertEquals(expectedDecodedText, actualDecodedText,
            "Soft line break '=\\r\\n' should be removed after decoding.");
    }

    @Test
    @DisplayName("Encoding and then decoding a string should return the original string")
    void shouldEncodeAndDecodeRoundTripSuccessfully() throws EncoderException, DecoderException {
        // Arrange: A standard string to test the encode-decode cycle.
        final String originalText = "If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.";

        // Act: Encode the original text and then decode it back.
        final String encodedText = codec.encode(originalText);
        final String roundTripText = codec.decode(encodedText);

        // Assert: The result of the round trip should be identical to the original string.
        assertEquals(originalText, roundTripText,
            "Decoding an encoded string should restore it to its original form.");
    }
}