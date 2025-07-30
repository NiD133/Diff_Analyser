package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HexDump}.
 */
class HexDumpTest {

    private static final int BYTE_ARRAY_SIZE = 256;
    private static final int HEX_DUMP_LINE_LENGTH = 16;
    private static final int HEX_DUMP_LINE_WIDTH = 73;

    /**
     * Tests the HexDump.dump method with an Appendable.
     */
    @Test
    void testDumpAppendable() throws IOException {
        final byte[] testArray = createTestByteArray();

        // Test dumping the entire array
        testDumpEntireArray(testArray);

        // Test dumping with non-zero offset, index, and limited length
        testDumpWithOffsetAndLength(testArray);

        // Test dumping with non-zero index and limited length
        testDumpWithIndexAndLength(testArray);

        // Test dumping with negative index
        testDumpWithNegativeIndex(testArray);

        // Test dumping with index too large
        testDumpWithIndexTooLarge(testArray);

        // Test dumping with negative length
        testDumpWithNegativeLength(testArray);

        // Test dumping with length too large
        testDumpWithLengthTooLarge(testArray);

        // Test dumping with null appendable
        testDumpWithNullAppendable(testArray);
    }

    /**
     * Tests the HexDump.dump method with an OutputStream.
     */
    @Test
    void testDumpOutputStream() throws IOException {
        final byte[] testArray = createTestByteArray();

        // Test dumping the entire array
        testDumpToOutputStream(testArray, 0);

        // Test dumping with non-zero offset
        testDumpToOutputStream(testArray, 0x10000000);

        // Test dumping with negative offset
        testDumpToOutputStream(testArray, 0xFF000000);

        // Test dumping with non-zero index
        testDumpToOutputStreamWithIndex(testArray, 0x10000000, 0x81);

        // Test dumping with negative index
        testDumpWithNegativeIndexToOutputStream(testArray);

        // Test dumping with index too large
        testDumpWithIndexTooLargeToOutputStream(testArray);

        // Test dumping with null stream
        testDumpWithNullStream(testArray);

        // Test output stream is not closed by the dump method
        testStreamNotClosedByDump(testArray);
    }

    private byte[] createTestByteArray() {
        final byte[] array = new byte[BYTE_ARRAY_SIZE];
        for (int i = 0; i < BYTE_ARRAY_SIZE; i++) {
            array[i] = (byte) i;
        }
        return array;
    }

    private void testDumpEntireArray(byte[] testArray) throws IOException {
        StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, out);
        assertEquals(expectedFullHexDump(), out.toString(), "Hex dump of the entire array did not match expected output.");
    }

    private void testDumpWithOffsetAndLength(byte[] testArray) throws IOException {
        StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, 0x10000000, out, 0x28, 32);
        assertEquals(expectedPartialHexDumpWithOffset(), out.toString(), "Hex dump with offset and length did not match expected output.");
    }

    private void testDumpWithIndexAndLength(byte[] testArray) throws IOException {
        StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, 0, out, 0x40, 24);
        assertEquals(expectedPartialHexDumpWithIndex(), out.toString(), "Hex dump with index and length did not match expected output.");
    }

    private void testDumpWithNegativeIndex(byte[] testArray) {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0x10000000, new StringBuilder(), -1, testArray.length));
    }

    private void testDumpWithIndexTooLarge(byte[] testArray) {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0x10000000, new StringBuilder(), testArray.length, testArray.length));
    }

    private void testDumpWithNegativeLength(byte[] testArray) {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0, new StringBuilder(), 0, -1));
    }

    private void testDumpWithLengthTooLarge(byte[] testArray) {
        final Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0, new StringBuilder(), 1, testArray.length));
        assertEquals("Range [1, 1 + 256) out of bounds for length 256", exception.getMessage());
    }

    private void testDumpWithNullAppendable(byte[] testArray) {
        assertThrows(NullPointerException.class, () -> HexDump.dump(testArray, 0x10000000, null, 0, testArray.length));
    }

    private void testDumpToOutputStream(byte[] testArray, long offset) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HexDump.dump(testArray, offset, stream, 0);
        byte[] expectedOutput = createExpectedOutputArray(offset);
        byte[] actualOutput = stream.toByteArray();
        assertEquals(expectedOutput.length, actualOutput.length, "Array size mismatch");
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], actualOutput[i], "Array[" + i + "] mismatch");
        }
    }

    private void testDumpToOutputStreamWithIndex(byte[] testArray, long offset, int index) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        HexDump.dump(testArray, offset, stream, index);
        byte[] expectedOutput = createExpectedOutputArrayWithIndex(offset, index);
        byte[] actualOutput = stream.toByteArray();
        assertEquals(expectedOutput.length, actualOutput.length, "Array size mismatch");
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], actualOutput[i], "Array[" + i + "] mismatch");
        }
    }

    private void testDumpWithNegativeIndexToOutputStream(byte[] testArray) {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0x10000000, new ByteArrayOutputStream(), -1));
    }

    private void testDumpWithIndexTooLargeToOutputStream(byte[] testArray) {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(testArray, 0x10000000, new ByteArrayOutputStream(), testArray.length));
    }

    private void testDumpWithNullStream(byte[] testArray) {
        assertThrows(NullPointerException.class, () -> HexDump.dump(testArray, 0x10000000, null, 0));
    }

    private void testStreamNotClosedByDump(byte[] testArray) throws IOException {
        HexDump.dump(testArray, 0, new ThrowOnCloseOutputStream(new ByteArrayOutputStream()), 0);
    }

    private String expectedFullHexDump() {
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

    private String expectedPartialHexDumpWithOffset() {
        return "10000028 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 ()*+,-./01234567" + System.lineSeparator() +
               "10000038 38 39 3A 3B 3C 3D 3E 3F 40 41 42 43 44 45 46 47 89:;<=>?@ABCDEFG" + System.lineSeparator();
    }

    private String expectedPartialHexDumpWithIndex() {
        return "00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO" + System.lineSeparator() +
               "00000050 50 51 52 53 54 55 56 57                         PQRSTUVW" + System.lineSeparator();
    }

    private byte[] createExpectedOutputArray(long offset) {
        byte[] outputArray = new byte[HEX_DUMP_LINE_LENGTH * (HEX_DUMP_LINE_WIDTH + System.lineSeparator().length())];
        for (int i = 0; i < HEX_DUMP_LINE_LENGTH; i++) {
            int lineOffset = (HEX_DUMP_LINE_WIDTH + System.lineSeparator().length()) * i;
            fillHexDumpLine(outputArray, lineOffset, offset, i);
        }
        return outputArray;
    }

    private byte[] createExpectedOutputArrayWithIndex(long offset, int index) {
        byte[] outputArray = new byte[8 * (HEX_DUMP_LINE_WIDTH + System.lineSeparator().length()) - 1];
        for (int i = 0; i < 8; i++) {
            int lineOffset = (HEX_DUMP_LINE_WIDTH + System.lineSeparator().length()) * i;
            fillHexDumpLineWithIndex(outputArray, lineOffset, offset, index, i);
        }
        return outputArray;
    }

    private void fillHexDumpLine(byte[] outputArray, int lineOffset, long offset, int lineIndex) {
        int currentOffset = lineOffset;
        currentOffset = fillHexOffset(outputArray, currentOffset, offset, lineIndex);
        currentOffset = fillHexValues(outputArray, currentOffset, lineIndex);
        fillAsciiValues(outputArray, currentOffset, lineIndex);
    }

    private void fillHexDumpLineWithIndex(byte[] outputArray, int lineOffset, long offset, int index, int lineIndex) {
        int currentOffset = lineOffset;
        currentOffset = fillHexOffset(outputArray, currentOffset, offset, lineIndex + 8);
        currentOffset = fillHexValuesWithIndex(outputArray, currentOffset, index, lineIndex);
        fillAsciiValuesWithIndex(outputArray, currentOffset, index, lineIndex);
    }

    private int fillHexOffset(byte[] outputArray, int offset, long baseOffset, int lineIndex) {
        String hexOffset = String.format("%08X", baseOffset + lineIndex * HEX_DUMP_LINE_LENGTH);
        for (char c : hexOffset.toCharArray()) {
            outputArray[offset++] = (byte) c;
        }
        outputArray[offset++] = (byte) ' ';
        return offset;
    }

    private int fillHexValues(byte[] outputArray, int offset, int lineIndex) {
        for (int i = 0; i < HEX_DUMP_LINE_LENGTH; i++) {
            outputArray[offset++] = (byte) toHex(lineIndex);
            outputArray[offset++] = (byte) toHex(i);
            outputArray[offset++] = (byte) ' ';
        }
        return offset;
    }

    private int fillHexValuesWithIndex(byte[] outputArray, int offset, int index, int lineIndex) {
        for (int i = 0; i < HEX_DUMP_LINE_LENGTH; i++) {
            int byteIndex = index + lineIndex * HEX_DUMP_LINE_LENGTH + i;
            if (byteIndex < BYTE_ARRAY_SIZE) {
                outputArray[offset++] = (byte) toHex(byteIndex / 16);
                outputArray[offset++] = (byte) toHex(byteIndex);
            } else {
                outputArray[offset++] = (byte) ' ';
                outputArray[offset++] = (byte) ' ';
            }
            outputArray[offset++] = (byte) ' ';
        }
        return offset;
    }

    private void fillAsciiValues(byte[] outputArray, int offset, int lineIndex) {
        for (int i = 0; i < HEX_DUMP_LINE_LENGTH; i++) {
            outputArray[offset++] = (byte) toAscii(lineIndex * HEX_DUMP_LINE_LENGTH + i);
        }
        System.arraycopy(System.lineSeparator().getBytes(), 0, outputArray, offset, System.lineSeparator().getBytes().length);
    }

    private void fillAsciiValuesWithIndex(byte[] outputArray, int offset, int index, int lineIndex) {
        for (int i = 0; i < HEX_DUMP_LINE_LENGTH; i++) {
            int byteIndex = index + lineIndex * HEX_DUMP_LINE_LENGTH + i;
            if (byteIndex < BYTE_ARRAY_SIZE) {
                outputArray[offset++] = (byte) toAscii(byteIndex);
            }
        }
        System.arraycopy(System.lineSeparator().getBytes(), 0, outputArray, offset, System.lineSeparator().getBytes().length);
    }

    private char toAscii(final int c) {
        return (c >= 32 && c <= 126) ? (char) c : '.';
    }

    private char toHex(final int n) {
        final char[] hexChars = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        return hexChars[n % 16];
    }
}