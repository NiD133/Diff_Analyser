package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

/**
 * Tests for {@link PercentCodec}.
 */
public class PercentCodecTest {

    /**
     * Tests that the PercentCodec correctly encodes bytes that are specified
     * in the constructor as 'always encode', in addition to other non-alphanumeric
     * characters that are unsafe by default.
     */
    @Test
    public void encodeShouldExpandUnsafeBytesAndCustomAlwaysEncodeBytes() throws Exception {
        // ARRANGE
        // 1. Define a byte that we want to force the codec to always encode.
        //    The byte 0 is not an unreserved character and would be encoded anyway,
        //    but we explicitly add it to the 'alwaysEncodeChars' list for this test.
        final byte[] alwaysEncodeChars = {0};
        final PercentCodec percentCodec = new PercentCodec(alwaysEncodeChars, true);

        // 2. Create an input array containing bytes that require encoding.
        //    This includes the byte '0' (which we specified) and the byte '22'
        //    (a non-alphanumeric control character that should be encoded by default).
        final byte[] bytesToEncode = {0, 0, 22, 0, 0, 0, 0, 0, 0};

        // 3. Define the expected output. Each of the 9 input bytes should be
        //    encoded into a 3-byte sequence (e.g., '%XX').
        //    - 0  (0x00) -> %00
        //    - 22 (0x16) -> %16
        //    The total length should be 9 bytes * 3 = 27 bytes.
        //    Note: The original test asserted a length of 25, which appears incorrect.
        final byte[] expectedEncodedBytes = "%00%00%16%00%00%00%00%00%00".getBytes("US-ASCII");

        // ACT
        final byte[] actualEncodedBytes = percentCodec.encode(bytesToEncode);

        // ASSERT
        assertEquals("Encoded byte array should have the correct length.", 27, actualEncodedBytes.length);
        assertArrayEquals("Encoded byte array content should match the expected percent-encoded format.",
                expectedEncodedBytes, actualEncodedBytes);
    }
}