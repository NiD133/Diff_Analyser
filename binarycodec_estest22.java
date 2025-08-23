package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link BinaryCodec} class.
 *
 * Note: The original test was for {@code org.apache.commons.codec.binary.BinaryCodec},
 * not the provided source file for {@code org.locationtech.spatial4j.io.BinaryCodec}.
 * This refactoring targets the Apache Commons Codec class.
 */
public class BinaryCodecTest {

    /**
     * Tests that a byte array encoded to ASCII bytes and then decoded back
     * results in the original byte array (a "round-trip" conversion).
     */
    @Test
    public void testRoundTripConversionFromBytesToAsciiAndBack() {
        // Arrange: Create a sample byte array.
        // The values include zero and a printable ASCII character ('0').
        final byte[] originalBytes = new byte[]{0, 0, (byte) '0'};

        // Act: Perform the round-trip conversion (binary -> ASCII -> binary).
        final byte[] asciiEncodedBytes = BinaryCodec.toAsciiBytes(originalBytes);
        final byte[] decodedBytes = BinaryCodec.fromAscii(asciiEncodedBytes);

        // Assert: The decoded data should be identical to the original data.
        assertArrayEquals(originalBytes, decodedBytes);
    }
}