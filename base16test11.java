package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.jupiter.api.Test;

/**
 * Tests the streaming decoding capabilities of the {@link Base16} codec.
 */
public class Base16Test {

    private static final String DECODED_TEXT = "Until next time!";
    private static final String ENCODED_TEXT = "556E74696C206E6578742074696D6521"; // "Until next time!" encoded in Base16

    /**
     * Extracts the decoded bytes from the context and converts them to a UTF-8 string.
     *
     * @param context The codec context holding the decoding state.
     * @return The decoded string.
     */
    private String getDecodedString(final Context context) {
        final byte[] decodedBytes = new byte[context.pos];
        System.arraycopy(context.buffer, context.readPos, decodedBytes, 0, decodedBytes.length);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Tests the low-level {@link Base16#decode(byte[], int, int, Context)} method
     * to ensure it correctly handles streaming data that arrives in chunks of varying sizes.
     * This is crucial for verifying the codec's internal state management, especially
     * its handling of partial hex pairs across multiple decode calls.
     */
    @Test
    void testDecodeInChunksWithContext() {
        // Arrange: Set up the codec, context, and input data.
        final Base16 base16 = new Base16();
        final Context context = new Context();
        final byte[] encodedBytes = ENCODED_TEXT.getBytes(StandardCharsets.UTF_8);

        // Act & Assert in stages to test streaming decoding

        // Stage 1: Decode the first character "U" (from "55") byte-by-byte.
        base16.decode(encodedBytes, 0, 1, context); // Feed "5"
        base16.decode(encodedBytes, 1, 1, context); // Feed "5", completes the pair
        assertEquals("U", getDecodedString(context));

        // Stage 2: Decode the second character "n" (from "6E") byte-by-byte.
        base16.decode(encodedBytes, 2, 1, context); // Feed "6"
        base16.decode(encodedBytes, 3, 1, context); // Feed "E", completes the pair
        assertEquals("Un", getDecodedString(context));

        // Stage 3: Decode a chunk of 3 hex characters ("746").
        // This should decode "74" to "t" and buffer the trailing "6".
        base16.decode(encodedBytes, 4, 3, context);
        assertEquals("Unt", getDecodedString(context));

        // Stage 4: Decode another chunk of 3 hex characters ("96C").
        // This should use the buffered "6" and the new "9" to form "69" (decoded to "i"),
        // and then decode the pair "6C" (decoded to "l").
        base16.decode(encodedBytes, 7, 3, context);
        assertEquals("Until", getDecodedString(context));

        // Stage 5: Decode another chunk of 3 hex characters ("206").
        // This should decode "20" to a space and buffer the trailing "6".
        base16.decode(encodedBytes, 10, 3, context);
        assertEquals("Until ", getDecodedString(context));

        // Stage 6: Decode the rest of the input in one large chunk.
        // The remaining data will be combined with the buffered "6" to continue decoding.
        final int remainingOffset = 13;
        final int remainingLength = encodedBytes.length - remainingOffset;
        base16.decode(encodedBytes, remainingOffset, remainingLength, context);

        // Final Assert: Verify the fully decoded string matches the original text.
        assertEquals(DECODED_TEXT, getDecodedString(context));
    }
}