package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link BinaryCodec} class.
 */
public class BinaryCodecTest {

    /**
     * Tests that the {@code toAsciiString} method correctly converts a single byte
     * with the value 0 into a string of eight '0' characters, representing its
     * 8-bit binary form.
     */
    @Test
    public void toAsciiString_convertsSingleZeroByte_toEightZeroCharacters() {
        // Arrange: Create a byte array containing a single byte with the value 0.
        byte[] inputBytes = {0};
        String expectedBinaryString = "00000000";

        // Act: Convert the byte array to its ASCII binary string representation.
        String actualBinaryString = BinaryCodec.toAsciiString(inputBytes);

        // Assert: Verify the output string matches the expected 8-bit representation.
        assertEquals(expectedBinaryString, actualBinaryString);
    }
}