package org.apache.commons.codec.binary;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for the Base16 class.
 */
public class Base16Test {

    /**
     * Tests that encoding a byte array containing a negative value (which corresponds to a byte > 127)
     * produces the correct upper-case hexadecimal string representation.
     */
    @Test
    public void encodeByteArrayWithNegativeValueShouldReturnCorrectUpperCaseHexString() {
        // Arrange
        // The original test used (byte) -23, which is 0xE9 in hexadecimal.
        // Using hex literals makes the input data's intent much clearer.
        final byte[] inputData = {0x00, (byte) 0xE9, 0x00};

        // The test uses an upper-case alphabet (the 'false' parameter) and a strict policy.
        // The policy does not affect the encoding behavior.
        final Base16 base16 = new Base16(false, CodecPolicy.STRICT);

        // The expected output is the hexadecimal string "00E900".
        // Representing it as a string and converting to bytes is more readable
        // than an array of ASCII values.
        final byte[] expectedEncodedData = "00E900".getBytes(StandardCharsets.US_ASCII);

        // Act
        final byte[] actualEncodedData = base16.encode(inputData);

        // Assert
        assertArrayEquals(expectedEncodedData, actualEncodedData);
    }
}