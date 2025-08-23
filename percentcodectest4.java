package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 */
class PercentCodecTest {

    @Test
    void decodeShouldThrowDecoderExceptionForTruncatedSequence() {
        // ARRANGE
        // An input string that will be percent-encoded.
        // The Greek letters alpha and beta are chosen as they are multi-byte UTF-8 characters.
        final String originalString = "\u03B1\u03B2"; // "αβ"
        final PercentCodec percentCodec = new PercentCodec();

        // Create a valid percent-encoded byte array.
        // "αβ" in UTF-8 is [CE B1 CE B2], which encodes to the bytes for "%CE%B1%CE%B2".
        final byte[] validEncodedBytes = percentCodec.encode(originalString.getBytes(StandardCharsets.UTF_8));

        // Create a corrupted input by truncating the last byte.
        // This results in an array for "%CE%B1%CE%B", which is an incomplete escape sequence.
        final byte[] truncatedBytes = Arrays.copyOf(validEncodedBytes, validEncodedBytes.length - 1);

        // ACT & ASSERT
        // We expect a DecoderException because the input ends with an incomplete escape sequence ('%B').
        // The underlying cause should be an ArrayIndexOutOfBoundsException when the decoder
        // tries to read the second hex digit after the 'B', which is missing.
        final DecoderException e = assertThrows(DecoderException.class, () -> {
            percentCodec.decode(truncatedBytes);
        });

        // Verify the specific cause of the failure for a more robust test.
        assertTrue(e.getCause() instanceof ArrayIndexOutOfBoundsException, "The cause of the DecoderException should be an ArrayIndexOutOfBoundsException");
    }
}