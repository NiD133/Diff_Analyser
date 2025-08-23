package org.apache.commons.codec.net;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the static {@link URLCodec#encodeUrl(BitSet, byte[])} method.
 */
public class URLCodec_ESTestTest1 extends URLCodec_ESTest_scaffolding {

    /**
     * Tests that encodeUrl correctly encodes bytes that are not in the provided
     * 'safe' BitSet, while leaving bytes that are in the set untouched.
     */
    @Test
    public void encodeUrlShouldEncodeUnsafeBytesAndPassThroughSafeBytes() {
        // Arrange

        // 1. Define a custom set of 'safe' characters that should not be encoded.
        // The original test used BitSet.valueOf(new byte[]{65}), which is a non-obvious
        // way to set bits 0 and 6 (since 65 is 0b01000001).
        // Here, we make it explicit that the null character (0) and ACK character (6) are safe.
        BitSet safeChars = new BitSet();
        safeChars.set(0);  // The null character is safe.
        safeChars.set(6);  // The ACK character is safe.

        // 2. Define the input data to be encoded.
        // This array contains 'A' (65), which is NOT in our safe set, and two null
        // characters, which ARE in the safe set.
        byte[] bytesToEncode = new byte[]{'A', 0, 0};

        // Act
        byte[] actualEncodedBytes = URLCodec.encodeUrl(safeChars, bytesToEncode);

        // Assert

        // We expect 'A' (ASCII 65, hex 41) to be percent-encoded to "%41".
        // The null characters (0) are in the safe set, so they should pass through unchanged.
        byte[] expectedEncodedBytes = "%41\0\0".getBytes(StandardCharsets.US_ASCII);

        assertArrayEquals(expectedEncodedBytes, actualEncodedBytes);
    }
}