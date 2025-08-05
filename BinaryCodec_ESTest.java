/*
 * Copyright 2002-2012 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.codec.binary;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link org.apache.commons.codec.binary.BinaryCodec}.
 *
 * This class tests the functionality of the BinaryCodec, which is responsible for
 * converting between raw byte arrays and their ASCII string representation of
 * binary '0's and '1's.
 */
public class BinaryCodecTest {

    private BinaryCodec binaryCodec;

    @Before
    public void setUp() {
        binaryCodec = new BinaryCodec();
    }

    // --- Static Method Tests ---

    @Test
    public void isEmpty_withNullArray_shouldReturnTrue() {
        assertTrue(BinaryCodec.isEmpty(null));
    }

    @Test
    public void isEmpty_withEmptyArray_shouldReturnTrue() {
        assertTrue(BinaryCodec.isEmpty(new byte[0]));
    }

    @Test
    public void isEmpty_withNonEmptyArray_shouldReturnFalse() {
        assertFalse(BinaryCodec.isEmpty(new byte[1]));
    }

    @Test
    public void toAsciiBytes_withNullArray_shouldReturnEmptyArray() {
        byte[] result = BinaryCodec.toAsciiBytes(null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void toAsciiChars_withNullArray_shouldReturnEmptyArray() {
        char[] result = BinaryCodec.toAsciiChars(null);
        assertEquals(0, result.length);
    }

    @Test
    public void fromAscii_withNullCharArray_shouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii((char[]) null);
        assertEquals(0, result.length);
    }

    @Test
    public void fromAscii_withEmptyByteArray_shouldReturnEmptyArray() {
        byte[] result = BinaryCodec.fromAscii(new byte[0]);
        assertEquals(0, result.length);
    }
    
    // --- Encoding Tests ---

    @Test
    public void encode_withNullArray_shouldReturnEmptyArray() {
        byte[] result = binaryCodec.encode((byte[]) null);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void encode_shouldProduceOutput8TimesLargerThanInput() {
        byte[] rawData = new byte[5];
        int expectedLength = rawData.length * 8; // 5 * 8 = 40

        byte[] encodedData = binaryCodec.encode(rawData);

        assertEquals(expectedLength, encodedData.length);
    }

    @Test
    public void toAsciiString_forSingleZeroByte_shouldReturnEightZeros() {
        byte[] rawData = new byte[1]; // A single byte with value 0
        String expected = "00000000";

        String result = BinaryCodec.toAsciiString(rawData);

        assertEquals(expected, result);
    }

    // --- Decoding Tests ---

    @Test
    public void decode_withInputLengthLessThan8_shouldReturnEmptyArray() {
        // Input length is less than 8, so the output should be an empty byte array.
        byte[] asciiEncoded = new byte[]{'0', '1', '0', '1'};

        byte[] result = binaryCodec.decode(asciiEncoded);

        assertEquals(0, result.length);
    }

    @Test
    public void decode_withNonAsciiBinaryBytes_shouldProduceZeroByte() {
        // Input is an array of 8 bytes, none of which are the ASCII character '1'.
        // The decoder treats any byte that is not '1' as a 0 bit.
        byte[] asciiEncoded = new byte[8];
        byte[] expected = new byte[]{0};

        byte[] result = binaryCodec.decode(asciiEncoded);

        assertArrayEquals(expected, result);
    }

    @Test
    public void toByteArray_withStringContainingNoBinaryChars_shouldProduceZeroBytes() {
        // The input string does not contain any '0' or '1' characters.
        String input = "Apache Commons Codec"; // length 20
        // The output array size is input.length() / 8, which is 20 / 8 = 2.
        // Since there are no '1's, the resulting bytes should both be 0.
        byte[] expected = new byte[]{0, 0};

        byte[] result = binaryCodec.toByteArray(input);

        assertArrayEquals(expected, result);
    }

    // --- Round-trip Conversion Tests ---

    @Test
    public void toAndFromAscii_shouldPerformRoundTripConversion() {
        // The byte for '@' is 64, which is 01000000 in binary.
        byte[] rawData = new byte[]{'@'};

        // 1. Convert the raw byte to its ASCII character representation.
        char[] asciiChars = BinaryCodec.toAsciiChars(rawData);
        assertArrayEquals("01000000".toCharArray(), asciiChars);

        // 2. Convert the ASCII characters back to a raw byte array.
        byte[] resultData = BinaryCodec.fromAscii(asciiChars);

        // The result should be the original raw data.
        assertArrayEquals(rawData, resultData);
    }

    @Test
    public void toAsciiBytesAndFromAscii_shouldPerformRoundTripConversion() {
        // Input data includes a byte that will be converted to ASCII '0's and '1's.
        // The byte for ASCII '0' is 48, which is 00110000 in binary.
        byte[] rawData = new byte[]{1, 2, (byte) 48};

        // 1. Convert raw data to ASCII bytes.
        byte[] asciiBytes = BinaryCodec.toAsciiBytes(rawData);

        // 2. Convert ASCII bytes back to raw data.
        byte[] resultData = BinaryCodec.fromAscii(asciiBytes);

        // The result should match the original data.
        assertArrayEquals(rawData, resultData);
    }

    // --- Object-based Interface and Exception Tests ---

    @Test(expected = EncoderException.class)
    public void encode_withNonByteArrayObject_shouldThrowEncoderException() throws EncoderException {
        // The encode(Object) method expects a byte array.
        // Providing another type of object should result in an EncoderException.
        binaryCodec.encode("This is not a byte array");
    }

    @Test(expected = DecoderException.class)
    public void decode_withInvalidObjectType_shouldThrowDecoderException() throws DecoderException {
        // The decode(Object) method expects a byte array, char array, or String.
        // Providing an incompatible object should result in a DecoderException.
        binaryCodec.decode(new Object());
    }

    @Test
    public void decode_withValidObjectTypes_shouldSucceed() throws DecoderException {
        String asciiString = "01000001"; // Represents 'A'
        byte[] expected = new byte[]{'A'};

        // Test with String
        Object resultFromString = binaryCodec.decode((Object) asciiString);
        assertArrayEquals(expected, (byte[]) resultFromString);

        // Test with char[]
        Object resultFromChars = binaryCodec.decode((Object) asciiString.toCharArray());
        assertArrayEquals(expected, (byte[]) resultFromChars);

        // Test with byte[]
        Object resultFromBytes = binaryCodec.decode((Object) asciiString.getBytes());
        assertArrayEquals(expected, (byte[]) resultFromBytes);
    }
}