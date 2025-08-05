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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link HexDump}.
 */
class HexDumpTest {

    private static final String EOL = System.lineSeparator();
    private static final int TEST_ARRAY_SIZE = 256;
    private static final String FULL_DUMP = String.join(EOL,
        "00000000 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................",
        "00000010 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................",
        "00000020 20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F  !\"#$%&'()*+,-./",
        "00000030 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F 0123456789:;<=>?",
        "00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO",
        "00000050 50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F PQRSTUVWXYZ[\\]^_",
        "00000060 60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F `abcdefghijklmno",
        "00000070 70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F pqrstuvwxyz{|}~.",
        "00000080 80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F ................",
        "00000090 90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F ................",
        "000000A0 A0 A1 A2 A3 A4 A5 A6 A7 A8 A9 AA AB AC AD AE AF ................",
        "000000B0 B0 B1 B2 B3 B4 B5 B6 B7 B8 B9 BA BB BC BD BE BF ................",
        "000000C0 C0 C1 C2 C3 C4 C5 C6 C7 C8 C9 CA CB CC CD CE CF ................",
        "000000D0 D0 D1 D2 D3 D4 D5 D6 D7 D8 D9 DA DB DC DD DE DF ................",
        "000000E0 E0 E1 E2 E3 E4 E5 E6 E7 E8 E9 EA EB EC ED EE EF ................",
        "000000F0 F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF ................"
    ) + EOL;

    private byte[] testArray;

    @BeforeEach
    void setUp() {
        testArray = new byte[TEST_ARRAY_SIZE];
        for (int j = 0; j < TEST_ARRAY_SIZE; j++) {
            testArray[j] = (byte) j;
        }
    }

    @Test
    void dumpAppendable_ShouldDumpEntireArray() throws IOException {
        final StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, out);
        assertEquals(FULL_DUMP, out.toString());
    }

    @Test
    void dumpAppendable_ShouldDumpWithOffsetAndLength() throws IOException {
        final StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, 0x10000000, out, 0x28, 32);
        final String expected = String.join(EOL,
            "10000028 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 ()*+,-./01234567",
            "10000038 38 39 3A 3B 3C 3D 3E 3F 40 41 42 43 44 45 46 47 89:;<=>?@ABCDEFG"
        ) + EOL;
        assertEquals(expected, out.toString());
    }

    @Test
    void dumpAppendable_ShouldDumpWithNonZeroIndex() throws IOException {
        final StringBuilder out = new StringBuilder();
        HexDump.dump(testArray, 0, out, 0x40, 24);
        final String expected = String.join(EOL,
            "00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO",
            "00000050 50 51 52 53 54 55 56 57                         PQRSTUVW"
        ) + EOL;
        assertEquals(expected, out.toString());
    }

    @Test
    void dumpAppendable_ShouldThrowException_WhenNegativeIndex() {
        final StringBuilder out = new StringBuilder();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0x10000000, out, -1, testArray.length)
        );
        assertEquals("Range [-1, -1 + 256) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpAppendable_ShouldThrowException_WhenIndexTooLarge() {
        final StringBuilder out = new StringBuilder();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0x10000000, out, testArray.length, testArray.length)
        );
        assertEquals("Range [256, 256 + 256) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpAppendable_ShouldThrowException_WhenNegativeLength() {
        final StringBuilder out = new StringBuilder();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0, out, 0, -1)
        );
        assertEquals("Range [0, 0 + -1) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpAppendable_ShouldThrowException_WhenLengthTooLarge() {
        final StringBuilder out = new StringBuilder();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0, out, 1, testArray.length)
        );
        assertEquals("Range [1, 1 + 256) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpAppendable_ShouldThrowException_WhenAppendableIsNull() {
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> HexDump.dump(testArray, 0x10000000, null, 0, testArray.length)
        );
        assertEquals("appendable", exception.getMessage());
    }

    @Test
    void dumpOutputStream_ShouldDumpEntireArray_WithZeroOffset() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(testArray, 0, out, 0);
        assertOutputMatchesAppendableVersion(out, 0, 0);
    }

    @Test
    void dumpOutputStream_ShouldDumpEntireArray_WithNonZeroOffset() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(testArray, 0x10000000, out, 0);
        assertOutputMatchesAppendableVersion(out, 0x10000000, 0);
    }

    @Test
    void dumpOutputStream_ShouldDumpEntireArray_WithNegativeOffset() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(testArray, 0xFF000000, out, 0);
        assertOutputMatchesAppendableVersion(out, 0xFF000000, 0);
    }

    @Test
    void dumpOutputStream_ShouldDumpPartialArray_WithNonZeroIndex() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HexDump.dump(testArray, 0x10000000, out, 0x81);
        assertOutputMatchesAppendableVersion(out, 0x10000000, 0x81);
    }

    @Test
    void dumpOutputStream_ShouldThrowException_WhenNegativeIndex() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0x10000000, out, -1)
        );
        assertEquals("Range [-1, -1 + 256) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpOutputStream_ShouldThrowException_WhenIndexTooLarge() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> HexDump.dump(testArray, 0x10000000, out, testArray.length)
        );
        assertEquals("Range [256, 256 + 256) out of bounds for length 256", exception.getMessage());
    }

    @Test
    void dumpOutputStream_ShouldThrowException_WhenOutputStreamIsNull() {
        final NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> HexDump.dump(testArray, 0x10000000, null, 0)
        );
        assertEquals("stream", exception.getMessage());
    }

    @Test
    void dumpOutputStream_ShouldNotCloseOutputStream() throws IOException {
        final ThrowOnCloseOutputStream out = new ThrowOnCloseOutputStream(new ByteArrayOutputStream());
        HexDump.dump(testArray, 0, out, 0);
        // Test passes if no exception is thrown
    }

    private void assertOutputMatchesAppendableVersion(
            final ByteArrayOutputStream actual,
            final long offset,
            final int index) throws IOException {
        final StringBuilder expectedBuilder = new StringBuilder();
        HexDump.dump(testArray, offset, expectedBuilder, index, testArray.length - index);
        final byte[] expectedBytes = expectedBuilder.toString().getBytes(Charset.defaultCharset());
        assertArrayEquals(expectedBytes, actual.toByteArray());
    }
}