package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the QuotedPrintableCodec with a focus on strict decoding behavior.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that the strict decoder correctly handles and skips various types of line breaks
     * as per RFC 1521. This includes soft line breaks (e.g., "=\r\n") as well as
     * literal CR, LF, and CRLF sequences that are not part of a valid soft line break.
     */
    @Test
    @DisplayName("Strict decoder should skip various line break types in encoded text")
    void strictDecoder_whenDecodingTextWithVariousLineBreaks_thenSkipsThem() throws DecoderException, EncoderException {
        // Arrange
        // The strict codec should handle different line break representations.
        final QuotedPrintableCodec strictCodec = new QuotedPrintableCodec(true);

        // This input contains a mix of line breaks:
        // - A bare line feed (\n)
        // - A soft line break (=\r\n)
        // - Two bare carriage returns (\r)
        final String encodedTextWithMixedLineBreaks = "CRLF in an\n encoded text should be=20=\r\n\rskipped in the\r decoding.";
        final String expectedDecodedText = "CRLF in an encoded text should be skipped in the decoding.";

        // Act: Decode the text containing the mixed line breaks.
        final String actualDecodedText = strictCodec.decode(encodedTextWithMixedLineBreaks);

        // Assert: The decoder should strip all line breaks and decode the content.
        assertEquals(expectedDecodedText, actualDecodedText);

        // --- Round-trip consistency check ---
        // Arrange & Act: Encode the clean text and decode it again.
        final String roundTripEncoded = strictCodec.encode(expectedDecodedText);
        final String roundTripDecoded = strictCodec.decode(roundTripEncoded);

        // Assert: The result of the round-trip should be identical to the original clean text.
        assertEquals(expectedDecodedText, roundTripDecoded);
    }
}