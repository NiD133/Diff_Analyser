package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Base16} class, focusing on its streaming decoding capabilities.
 */
public class Base16Test {

    /**
     * Tests that the decoder correctly processes a Base16 string one character at a time,
     * maintaining the decoding state between calls. This is crucial for streaming applications
     * where data may arrive in small, incomplete chunks.
     */
    @Test
    void testStreamingDecodeWithPartialData() {
        // This test simulates a streaming decode of the Base16 string "EF",
        // where each character is processed in a separate call to decode().
        // The hexadecimal string "EF" should decode to the single byte 0xEF.

        // Arrange: Create a Base16 codec and a context object for the streaming operation.
        final Base16 base16 = new Base16();
        final BaseNCodec.Context context = new BaseNCodec.Context();

        // Assert: The initial state of the context should be empty.
        assertEquals(0, context.ibitWorkArea, "Initial work area should be 0");
        assertNull(context.buffer, "Initial buffer should be null");

        // Act: Decode the first character 'E' of the pair "EF".
        // 'E' in hex corresponds to the 4-bit value 14 (0b1110).
        final byte[] firstHexChar = {(byte) 'E'};
        base16.decode(firstHexChar, 0, 1, context);

        // Assert: The context should now hold the value of the first hex character.
        // The 4-bit value 14 is stored in the work area, waiting for the next hex char.
        assertEquals(14, context.ibitWorkArea, "Work area should hold the value of the first hex char");
        assertNull(context.buffer, "Buffer should still be null as a full byte has not been formed");

        // Act: Decode the second character 'F' of the pair "EF".
        // 'F' in hex corresponds to the 4-bit value 15 (0b1111).
        final byte[] secondHexChar = {(byte) 'F'};
        base16.decode(secondHexChar, 0, 1, context);

        // Assert: The two 4-bit values should be combined into a single byte 0xEF.
        // The context's work area should be reset, and the result written to the buffer.
        // Calculation: (14 << 4) | 15  =>  224 | 15  =>  239  =>  0xEF
        assertEquals(0, context.ibitWorkArea, "Work area should be reset after a full byte is decoded");
        final byte[] expectedDecodedBytes = {(byte) 0xEF};
        assertArrayEquals(expectedDecodedBytes, context.buffer, "Buffer should contain the fully decoded byte");
    }
}