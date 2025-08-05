/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link HexDump}.
 */
class HexDumpTest {

    private static final int FULL_BYTE_ARRAY_SIZE = 256;
    private static final long POSITIVE_OFFSET = 0x10000000L;
    private static final long NEGATIVE_OFFSET = 0xFF000000L;
    private static final int PARTIAL_DUMP_START_INDEX = 0x28;
    private static final int PARTIAL_DUMP_LENGTH = 32;
    private static final int ANOTHER_PARTIAL_START_INDEX = 0x40;
    private static final int ANOTHER_PARTIAL_LENGTH = 24;
    private static final int NON_ZERO_START_INDEX = 0x81;

    private byte[] testByteArray;

    @BeforeEach
    void setUp() {
        testByteArray = createSequentialByteArray(FULL_BYTE_ARRAY_SIZE);
    }

    // ========== Tests for Appendable output ==========

    @Test
    void testDumpEntireArrayToAppendable() throws IOException {
        StringBuilder output = new StringBuilder();
        
        HexDump.dump(testByteArray, output);
        
        String expectedOutput = buildExpectedFullArrayOutput();
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    void testDumpPartialArrayWithOffsetToAppendable() throws IOException {
        StringBuilder output = new StringBuilder();
        
        HexDump.dump(testByteArray, POSITIVE_OFFSET, output, PARTIAL_DUMP_START_INDEX, PARTIAL_DUMP_LENGTH);
        
        String expectedOutput = buildExpectedPartialOutputWithOffset();
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    void testDumpPartialArrayWithoutOffsetToAppendable() throws IOException {
        StringBuilder output = new StringBuilder();
        
        HexDump.dump(testByteArray, 0, output, ANOTHER_PARTIAL_START_INDEX, ANOTHER_PARTIAL_LENGTH);
        
        String expectedOutput = buildExpectedPartialOutputWithoutOffset();
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    void testDumpAppendableWithInvalidParameters() {
        // Test negative index
        assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, new StringBuilder(), -1, testByteArray.length),
            "Should throw exception for negative index");

        // Test index too large
        assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, new StringBuilder(), testByteArray.length, testByteArray.length),
            "Should throw exception for index beyond array bounds");

        // Test negative length
        assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, 0, new StringBuilder(), 0, -1),
            "Should throw exception for negative length");

        // Test length too large
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, 0, new StringBuilder(), 1, testByteArray.length),
            "Should throw exception for length that exceeds array bounds");
        assertEquals("Range [1, 1 + 256) out of bounds for length 256", exception.getMessage());

        // Test null appendable
        assertThrows(NullPointerException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, null, 0, testByteArray.length),
            "Should throw exception for null appendable");
    }

    // ========== Tests for OutputStream output ==========

    @Test
    void testDumpToOutputStreamWithZeroOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        HexDump.dump(testByteArray, 0, outputStream, 0);
        
        byte[] expectedOutput = buildExpectedOutputStreamBytes(0);
        assertOutputStreamContentEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    void testDumpToOutputStreamWithPositiveOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        HexDump.dump(testByteArray, POSITIVE_OFFSET, outputStream, 0);
        
        byte[] expectedOutput = buildExpectedOutputStreamBytesWithPositiveOffset();
        assertOutputStreamContentEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    void testDumpToOutputStreamWithNegativeOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        HexDump.dump(testByteArray, NEGATIVE_OFFSET, outputStream, 0);
        
        byte[] expectedOutput = buildExpectedOutputStreamBytesWithNegativeOffset();
        assertOutputStreamContentEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    void testDumpToOutputStreamWithNonZeroIndex() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        HexDump.dump(testByteArray, POSITIVE_OFFSET, outputStream, NON_ZERO_START_INDEX);
        
        byte[] expectedOutput = buildExpectedOutputStreamBytesWithNonZeroIndex();
        assertOutputStreamContentEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    void testDumpOutputStreamWithInvalidParameters() {
        // Test negative index
        assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, new ByteArrayOutputStream(), -1),
            "Should throw exception for negative index");

        // Test index too large
        assertThrows(ArrayIndexOutOfBoundsException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, new ByteArrayOutputStream(), testByteArray.length),
            "Should throw exception for index beyond array bounds");

        // Test null stream
        assertThrows(NullPointerException.class, 
            () -> HexDump.dump(testByteArray, POSITIVE_OFFSET, null, 0),
            "Should throw exception for null output stream");
    }

    @Test
    void testDumpDoesNotCloseOutputStream() throws IOException {
        // Verify that the dump method doesn't close the output stream
        ThrowOnCloseOutputStream throwOnCloseStream = new ThrowOnCloseOutputStream(new ByteArrayOutputStream());
        
        // This should not throw an exception because the stream should not be closed
        HexDump.dump(testByteArray, 0, throwOnCloseStream, 0);
    }

    // ========== Helper methods ==========

    /**
     * Creates a byte array with sequential values from 0 to size-1.
     */
    private byte[] createSequentialByteArray(int size) {
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = (byte) i;
        }
        return array;
    }

    /**
     * Builds the expected output for dumping the entire 256-byte array.
     */
    private String buildExpectedFullArrayOutput() {
        return "00000000 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................" + System.lineSeparator() +
               "00000010 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................" + System.lineSeparator() +
               "00000020 20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F  !\"#$%&'()*+,-./" + System.lineSeparator() +
               "00000030 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F 0123456789:;<=>?" + System.lineSeparator() +
               "00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO" + System.lineSeparator() +
               "00000050 50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F PQRSTUVWXYZ[\\]^_" + System.lineSeparator() +
               "00000060 60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F `abcdefghijklmno" + System.lineSeparator() +
               "00000070 70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F pqrstuvwxyz{|}~." + System.lineSeparator() +
               "00000080 80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F ................" + System.lineSeparator() +
               "00000090 90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F ................" + System.lineSeparator() +
               "000000A0 A0 A1 A2 A3 A4 A5 A6 A7 A8 A9 AA AB AC AD AE AF ................" + System.lineSeparator() +
               "000000B0 B0 B1 B2 B3 B4 B5 B6 B7 B8 B9 BA BB BC BD BE BF ................" + System.lineSeparator() +
               "000000C0 C0 C1 C2 C3 C4 C5 C6 C7 C8 C9 CA CB CC CD CE CF ................" + System.lineSeparator() +
               "000000D0 D0 D1 D2 D3 D4 D5 D6 D7 D8 D9 DA DB DC DD DE DF ................" + System.lineSeparator() +
               "000000E0 E0 E1 E2 E3 E4 E5 E6 E7 E8 E9 EA EB EC ED EE EF ................" + System.lineSeparator() +
               "000000F0 F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF ................" + System.lineSeparator();
    }

    /**
     * Builds expected output for partial dump with offset.
     */
    private String buildExpectedPartialOutputWithOffset() {
        return "10000028 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 ()*+,-./01234567" + System.lineSeparator() +
               "10000038 38 39 3A 3B 3C 3D 3E 3F 40 41 42 43 44 45 46 47 89:;<=>?@ABCDEFG" + System.lineSeparator();
    }

    /**
     * Builds expected output for partial dump without offset.
     */
    private String buildExpectedPartialOutputWithoutOffset() {
        return "00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO" + System.lineSeparator() +
               "00000050 50 51 52 53 54 55 56 57                         PQRSTUVW" + System.lineSeparator();
    }

    /**
     * Builds expected byte array output for OutputStream with given offset prefix.
     */
    private byte[] buildExpectedOutputStreamBytes(long offsetPrefix) {
        int lineLength = 73 + System.lineSeparator().length();
        byte[] output = new byte[16 * lineLength];
        
        for (int lineIndex = 0; lineIndex < 16; lineIndex++) {
            int position = lineLength * lineIndex;
            position = writeOffsetToOutput(output, position, offsetPrefix, lineIndex);
            position = writeHexBytesToOutput(output, position, lineIndex);
            position = writeAsciiRepresentationToOutput(output, position, lineIndex);
            writeLineSeparatorToOutput(output, position);
        }
        
        return output;
    }

    private byte[] buildExpectedOutputStreamBytesWithPositiveOffset() {
        return buildExpectedOutputStreamBytesWithOffsetPrefix("1");
    }

    private byte[] buildExpectedOutputStreamBytesWithNegativeOffset() {
        return buildExpectedOutputStreamBytesWithOffsetPrefix("FF");
    }

    private byte[] buildExpectedOutputStreamBytesWithOffsetPrefix(String prefix) {
        int lineLength = 73 + System.lineSeparator().length();
        byte[] output = new byte[16 * lineLength];
        
        for (int lineIndex = 0; lineIndex < 16; lineIndex++) {
            int position = lineLength * lineIndex;
            
            // Write offset with custom prefix
            for (char c : prefix.toCharArray()) {
                output[position++] = (byte) c;
            }
            // Fill remaining offset positions
            while (position % lineLength < 6) {
                output[position++] = (byte) '0';
            }
            output[position++] = (byte) toHex(lineIndex);
            output[position++] = (byte) '0';
            output[position++] = (byte) ' ';
            
            position = writeHexBytesToOutput(output, position, lineIndex);
            position = writeAsciiRepresentationToOutput(output, position, lineIndex);
            writeLineSeparatorToOutput(output, position);
        }
        
        return output;
    }

    private byte[] buildExpectedOutputStreamBytesWithNonZeroIndex() {
        int lineLength = 73 + System.lineSeparator().length();
        byte[] output = new byte[8 * lineLength - 1]; // Partial output
        
        for (int lineIndex = 0; lineIndex < 8; lineIndex++) {
            int position = lineLength * lineIndex;
            position = writeOffsetToOutput(output, position, POSITIVE_OFFSET, lineIndex + 8, '1');
            position = writeHexBytesToOutputWithBounds(output, position, lineIndex, NON_ZERO_START_INDEX);
            position = writeAsciiRepresentationToOutputWithBounds(output, position, lineIndex, NON_ZERO_START_INDEX);
            writeLineSeparatorToOutput(output, position);
        }
        
        return output;
    }

    private int writeOffsetToOutput(byte[] output, int position, long offsetPrefix, int lineIndex) {
        return writeOffsetToOutput(output, position, offsetPrefix, lineIndex, '0');
    }

    private int writeOffsetToOutput(byte[] output, int position, long offsetPrefix, int lineIndex, char prefixChar) {
        // Write 8-character offset
        output[position++] = (byte) prefixChar;
        output[position++] = (byte) '0';
        output[position++] = (byte) '0';
        output[position++] = (byte) '0';
        output[position++] = (byte) '0';
        output[position++] = (byte) '0';
        output[position++] = (byte) toHex(lineIndex);
        output[position++] = (byte) '0';
        output[position++] = (byte) ' ';
        return position;
    }

    private int writeHexBytesToOutput(byte[] output, int position, int lineIndex) {
        for (int byteIndex = 0; byteIndex < 16; byteIndex++) {
            output[position++] = (byte) toHex(lineIndex);
            output[position++] = (byte) toHex(byteIndex);
            output[position++] = (byte) ' ';
        }
        return position;
    }

    private int writeHexBytesToOutputWithBounds(byte[] output, int position, int lineIndex, int startIndex) {
        for (int byteIndex = 0; byteIndex < 16; byteIndex++) {
            final int dataIndex = startIndex + lineIndex * 16 + byteIndex;
            
            if (dataIndex < FULL_BYTE_ARRAY_SIZE) {
                output[position++] = (byte) toHex(dataIndex / 16);
                output[position++] = (byte) toHex(dataIndex);
            } else {
                output[position++] = (byte) ' ';
                output[position++] = (byte) ' ';
            }
            output[position++] = (byte) ' ';
        }
        return position;
    }

    private int writeAsciiRepresentationToOutput(byte[] output, int position, int lineIndex) {
        for (int byteIndex = 0; byteIndex < 16; byteIndex++) {
            output[position++] = (byte) toAscii(lineIndex * 16 + byteIndex);
        }
        return position;
    }

    private int writeAsciiRepresentationToOutputWithBounds(byte[] output, int position, int lineIndex, int startIndex) {
        for (int byteIndex = 0; byteIndex < 16; byteIndex++) {
            final int dataIndex = startIndex + lineIndex * 16 + byteIndex;
            
            if (dataIndex < FULL_BYTE_ARRAY_SIZE) {
                output[position++] = (byte) toAscii(dataIndex);
            }
        }
        return position;
    }

    private void writeLineSeparatorToOutput(byte[] output, int position) {
        System.arraycopy(System.lineSeparator().getBytes(), 0, output, position, System.lineSeparator().getBytes().length);
    }

    private void assertOutputStreamContentEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length, "Output array size mismatch");
        
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "Byte mismatch at position " + i);
        }
    }

    /**
     * Converts a byte value to its ASCII character representation.
     * Non-printable characters are represented as '.'.
     */
    private char toAscii(final int byteValue) {
        return (byteValue >= 32 && byteValue <= 126) ? (char) byteValue : '.';
    }

    /**
     * Converts a numeric value to its hexadecimal character representation.
     */
    private char toHex(final int value) {
        final char[] hexChars = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        return hexChars[value % 16];
    }
}