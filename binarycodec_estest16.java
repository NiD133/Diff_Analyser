package org.apache.commons.codec.binary;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link BinaryCodec} class, focusing on its decoding behavior.
 */
public class BinaryCodecTest {

    /**
     * Tests that decoding a byte array that does not contain ASCII '0' or '1' characters
     * results in an empty byte array.
     *
     * This test first decodes a string of ASCII bits ("00000100") into a byte array.
     * It then attempts to decode that resulting byte array. The `decode` method expects its
     * input array to contain ASCII characters, but the result of the first decode is a raw
     * byte with the value 4, which is not the ASCII character '1'. Therefore, the second
     * decode operation should produce an empty array.
     */
    @Test
    public void decode_whenInputIsByteArrayWithoutAsciiBits_returnsEmptyArray() throws DecoderException {
        // Arrange
        BinaryCodec binaryCodec = new BinaryCodec();
        // An input string of 8 characters representing the bits of a single byte.
        String inputStringWithAsciiBits = "00000100";

        // Act
        // 1. Decode the string into a raw byte array.
        // The codec should interpret the '1' and '0' characters and produce a byte array {4}.
        byte[] decodedFromString = (byte[]) binaryCodec.decode(inputStringWithAsciiBits);

        // 2. Attempt to decode the resulting raw byte array.
        // The codec will treat the input {4} as an array of ASCII characters to be decoded.
        // Since the byte value 4 is not the ASCII character '1', it finds no bits to set.
        byte[] decodedFromByteArray = (byte[]) binaryCodec.decode(decodedFromString);

        // Assert
        // Verify the first decode operation produced the expected byte.
        assertNotNull(decodedFromString);
        assertArrayEquals("Decoding the string '00000100' should result in a byte with value 4.",
                new byte[]{4}, decodedFromString);

        // Verify the second decode operation produced an empty array, as intended.
        assertNotNull(decodedFromByteArray);
        assertArrayEquals("Decoding a byte array not containing ASCII '1's should be empty.",
                new byte[]{}, decodedFromByteArray);
    }
}