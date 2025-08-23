package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Tests that byteArray2long correctly converts an 8-byte array to a long
     * in big-endian format (when half-word swapping is disabled).
     */
    @Test
    public void byteArray2longShouldConvertBigEndianByteArrayToLong() {
        // Arrange: Create a byte array representing a 64-bit number in big-endian format.
        // The most significant byte is set to 0xCD. In two's complement, this value
        // at the most significant position results in a negative number.
        byte[] inputBytes = new byte[8];
        inputBytes[0] = (byte) 0xCD;

        // The expected long value corresponding to the hex representation 0xCD00000000000000L.
        // Using a hex literal makes the expected value easy to verify against the input bytes.
        long expectedLong = 0xCD00000000000000L;

        // Act: Convert the byte array to a long without swapping half-words.
        long actualLong = CpioUtil.byteArray2long(inputBytes, false);

        // Assert: The converted long should match the expected value.
        assertEquals(expectedLong, actualLong);
    }
}