package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for {@link org.apache.commons.codec.binary.BinaryCodec}.
 */
public class BinaryCodecTest {

    @Test
    public void fromAscii_whenInputContainsNoOnes_returnsZeroedByteArray() {
        // Arrange
        // The fromAscii method converts an array of ASCII characters '0' and '1'
        // into a compact byte array. It processes the input in 8-character chunks
        // from right to left. Any character that is not '1' is treated as a 0-bit.

        // This input is 9 bytes long to confirm that the method correctly handles
        // input lengths that are not a multiple of 8. The first byte should be ignored.
        byte[] asciiChars = new byte[9];

        // We place a non-'1' character ('f') in the input. The rest are default zeros.
        // Since no '1' characters are present in the last 8 bytes, the result should be zero.
        asciiChars[1] = 'f'; // ASCII 'f' is 102, same as the original test's (byte) 102.

        byte[] expectedDecodedBytes = { 0 };

        // Act
        byte[] actualDecodedBytes = BinaryCodec.fromAscii(asciiChars);

        // Assert
        assertArrayEquals(
            "An ASCII input containing no '1' characters in its last 8 bytes should decode to zero.",
            expectedDecodedBytes,
            actualDecodedBytes);
    }
}