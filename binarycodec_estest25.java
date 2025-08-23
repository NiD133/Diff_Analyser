package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that the encode method produces an output byte array that is
     * exactly 8 times the size of the input array. This is because each
     * byte of input is converted into 8 bytes representing its ASCII
     * character bits ('0' or '1').
     */
    @Test
    public void encodeShouldReturnArrayEightTimesTheInputSize() {
        // Arrange
        final BinaryCodec binaryCodec = new BinaryCodec();
        final byte[] inputData = new byte[17];
        final int expectedLength = inputData.length * 8; // 17 bytes * 8 bits/byte = 136

        // Act
        final byte[] encodedData = binaryCodec.encode(inputData);

        // Assert
        assertEquals("The encoded array's length should be 8 times the input's length.",
                     expectedLength, encodedData.length);
    }
}