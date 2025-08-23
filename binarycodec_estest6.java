package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test cases for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that the decode method correctly handles an input byte array
     * that does not contain the ASCII character '1'.
     *
     * <p>The BinaryCodec implementation treats any byte that is not the character '1'
     * as a binary '0'. This test verifies that an input of 8 null bytes (value 0)
     * is correctly decoded into a single byte of value 0.</p>
     */
    @Test
    public void decodeEightNullBytesShouldReturnSingleZeroByte() {
        // Arrange: Create a codec and define the input and expected output.
        // The input is an array of 8 null bytes, which the codec should interpret
        // as the binary string "00000000".
        BinaryCodec codec = new BinaryCodec();
        byte[] eightNullBytes = new byte[8];
        byte[] expectedOutput = { 0 };

        // Act: Perform the decoding operation.
        byte[] actualOutput = codec.decode(eightNullBytes);

        // Assert: Verify that the actual output matches the expected single zero byte.
        assertArrayEquals("Decoding 8 null bytes should produce a single zero byte", expectedOutput, actualOutput);
    }
}