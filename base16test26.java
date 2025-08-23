package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for the streaming (chunked) decoding functionality of the {@link Base16} class.
 */
class Base16StreamingTest {

    private static final String DECODED_STRING = "ABCDE";
    private static final String ENCODED_STRING = "4142434445"; // Hexadecimal representation of "ABCDE"

    /**
     * Tests that the decoder correctly processes a stream of data fed in multiple chunks,
     * including chunks with an odd number of characters. This ensures that the codec's
     * internal state is managed correctly across multiple `decode` calls.
     */
    @Test
    void shouldCorrectlyDecodeWhenDataIsFedInChunksOfVaryingLengths() {
        // Arrange: Set up the Base16 codec and the context for streaming.
        final Base16 base16 = new Base16();
        final BaseNCodec.Context context = new BaseNCodec.Context();
        final byte[] encodedBytes = ENCODED_STRING.getBytes(StandardCharsets.UTF_8);

        // Act: Decode the stream in three chunks of varying (odd/even) lengths.
        // This tests that the codec correctly handles partial hex pairs between calls.

        // Chunk 1: "414" (3 bytes, odd length). Decodes "41" -> 'A'. Caches the trailing '4'.
        base16.decode(encodedBytes, 0, 3, context);

        // Chunk 2: "2434" (4 bytes, even length).
        // Uses cached '4' + new '2' -> "42" -> 'B'.
        // Decodes "43" -> 'C'. Caches the trailing '4'.
        base16.decode(encodedBytes, 3, 4, context);

        // Chunk 3: "445" (3 bytes, odd length).
        // Uses cached '4' + new '4' -> "44" -> 'D'.
        // Decodes "45" -> 'E'.
        base16.decode(encodedBytes, 7, 3, context);

        // Assert: Verify the fully decoded string.
        // We manually extract the result from the context's internal buffer because we are
        // testing the low-level, stateful `decode` method.
        final byte[] resultBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, resultBytes, 0, resultBytes.length);
        final String resultString = new String(resultBytes, StandardCharsets.UTF_8);

        assertEquals(DECODED_STRING, resultString);
    }
}