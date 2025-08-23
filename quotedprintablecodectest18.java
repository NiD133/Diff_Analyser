package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
class QuotedPrintableCodecTest {

    /**
     * This test verifies a specific behavior of the strict encoding mode. The source
     * code reveals that the strict encoder has a minimum input length requirement.
     * When encoding a string whose byte representation is shorter than this minimum,
     * the encoder returns null.
     */
    @Test
    void encodeWithShortInputInStrictModeShouldReturnNull() throws EncoderException {
        // Arrange
        // The strict mode constructor is used, which enforces the minimum length check.
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);

        // The string "AA" encodes to a 2-byte array in UTF-8. This is shorter than
        // the internal minimum of 3 bytes required by the strict encoder.
        final String shortInput = "AA";

        // Act
        final String encodedResult = strictCodec.encode(shortInput);

        // Assert
        assertNull(encodedResult, "Encoding an input shorter than the minimum required length in strict mode should return null.");
    }
}