package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the static utility methods in {@link BinaryCodec}.
 */
public class BinaryCodecTest {

    /**
     * Tests that a byte array converted to its binary ASCII representation
     * can be converted back to the original byte array. This verifies a
     * successful round-trip conversion.
     */
    @Test
    public void toAsciiCharsAndFromAsciiShouldPerformRoundTripConversion() {
        // Arrange
        // The byte 64 has a binary representation of "01000000".
        final byte[] originalBytes = {(byte) 64};
        final char[] expectedAsciiChars = "01000000".toCharArray();

        // Act
        // 1. Convert the byte array to its ASCII binary representation.
        final char[] actualAsciiChars = BinaryCodec.toAsciiChars(originalBytes);

        // 2. Convert the ASCII representation back to a byte array.
        final byte[] decodedBytes = BinaryCodec.fromAscii(actualAsciiChars);

        // Assert
        // Verify the intermediate ASCII representation is correct.
        assertArrayEquals("The conversion to ASCII chars should match the binary representation.",
                expectedAsciiChars, actualAsciiChars);

        // Verify the final byte array matches the original.
        assertArrayEquals("The decoded bytes should match the original bytes after a round trip.",
                originalBytes, decodedBytes);
    }
}