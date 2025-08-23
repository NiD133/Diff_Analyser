package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for edge cases in PercentCodec, specifically null and empty inputs.
 */
class PercentCodecTest {

    private PercentCodec percentCodec;

    @BeforeEach
    void setUp() {
        // The PercentCodec is configured to encode spaces as '+' for these tests,
        // though it doesn't affect null or empty inputs.
        percentCodec = new PercentCodec(null, true);
    }

    @Test
    void encode_withNullInput_shouldReturnNull() {
        // A null input should result in a null output.
        assertNull(percentCodec.encode(null), "Encoding a null byte array should return null.");
    }



    @Test
    void decode_withNullInput_shouldReturnNull() {
        // A null input should result in a null output.
        assertNull(percentCodec.decode(null), "Decoding a null byte array should return null.");
    }

    @Test
    void encode_withEmptyInput_shouldReturnEmptyArray() {
        final byte[] emptyArray = new byte[0];
        final byte[] result = percentCodec.encode(emptyArray);
        assertArrayEquals(emptyArray, result, "Encoding an empty byte array should return an empty byte array.");
    }

    @Test
    void decode_withEmptyInput_shouldReturnEmptyArray() {
        final byte[] emptyArray = new byte[0];
        final byte[] result = percentCodec.decode(emptyArray);
        assertArrayEquals(emptyArray, result, "Decoding an empty byte array should return an empty byte array.");
    }
}