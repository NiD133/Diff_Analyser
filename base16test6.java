package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Provides test cases for the Base16 class, focusing on the decode functionality.
 */
public class Base16Test {

    /**
     * Tests that decoding a valid two-character hexadecimal string ("21")
     * correctly results in the corresponding single byte (0x21).
     *
     * This test case verifies a basic successful decoding scenario.
     */
    @Test
    public void decodeShouldConvertValidHexStringToByte() {
        // Arrange: Set up the test conditions. [1, 2]
        final Base16 base16 = new Base16();
        final String hexString = "21";
        // The hexadecimal string "21" represents the decimal value 33.
        final byte[] expectedDecodedBytes = new byte[]{(byte) 0x21};

        // Act: Call the method under test. [6]
        final byte[] actualDecodedBytes = base16.decode(hexString);

        // Assert: Verify the outcome. [5, 6]
        assertArrayEquals("The decoded byte array should match the expected value.",
                          expectedDecodedBytes, actualDecodedBytes);
    }
}