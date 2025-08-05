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

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.test.ThrowOnCloseOutputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link HexDump}.
 */
class HexDumpTest {

    private static final byte[] TEST_ARRAY = new byte[256];
    private static final String EOL = System.lineSeparator();

    // The expected output for dumping the full 256-byte test array.
    private static final String EXPECTED_FULL_DUMP = """
        00000000 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................
        00000010 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................
        00000020 20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F  !"#$%&'()*+,-./
        00000030 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F 0123456789:;<=>?
        00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO
        00000050 50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F PQRSTUVWXYZ[\\\\]^_
        00000060 60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F `abcdefghijklmno
        00000070 70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F pqrstuvwxyz{|}~.
        00000080 80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F ................
        00000090 90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F ................
        000000A0 A0 A1 A2 A3 A4 A5 A6 A7 A8 A9 AA AB AC AD AE AF ................
        000000B0 B0 B1 B2 B3 B4 B5 B6 B7 B8 B9 BA BB BC BD BE BF ................
        000000C0 C0 C1 C2 C3 C4 C5 C6 C7 C8 C9 CA CB CC CD CE CF ................
        000000D0 D0 D1 D2 D3 D4 D5 D6 D7 D8 D9 DA DB DC DD DE DF ................
        000000E0 E0 E1 E2 E3 E4 E5 E6 E7 E8 E9 EA EB EC ED EE EF ................
        000000F0 F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF ................
        """.replace("\n", EOL);

    @BeforeAll
    static void setup() {
        for (int i = 0; i < 256; i++) {
            TEST_ARRAY[i] = (byte) i;
        }
    }

    @Nested
    @DisplayName("Tests for dump() to Appendable")
    class AppendableTests {

        @Test
        @DisplayName("Dumping a full array should produce the correct hex dump format")
        void testDumpFullArray() throws IOException {
            // Arrange
            final StringBuilder out = new StringBuilder();

            // Act
            HexDump.dump(TEST_ARRAY, out);

            // Assert
            assertEquals(EXPECTED_FULL_DUMP, out.toString());
        }

        @Test
        @DisplayName("Dumping a slice with offset and length should produce the correct output")
        void testDumpSliceWithOffset() throws IOException {
            // Arrange
            final StringBuilder out = new StringBuilder();
            final String expected = """
                10000028 28 29 2A 2B 2C 2D 2E 2F 30 31 32 33 34 35 36 37 ()*+,-./01234567
                10000038 38 39 3A 3B 3C 3D 3E 3F 40 41 42 43 44 45 46 47 89:;<=>?@ABCDEFG
                """.replace("\n", EOL);

            // Act
            HexDump.dump(TEST_ARRAY, 0x10000000, out, 0x28, 32);

            // Assert
            assertEquals(expected, out.toString());
        }

        @Test
        @DisplayName("Dumping a slice that results in a partial final line")
        void testDumpWithPartialLastLine() throws IOException {
            // Arrange
            final StringBuilder out = new StringBuilder();
            final String expected = """
                00000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO
                00000050 50 51 52 53 54 55 56 57                         PQRSTUVW
                """.replace("\n", EOL);

            // Act
            HexDump.dump(TEST_ARRAY, 0, out, 0x40, 24);

            // Assert
            assertEquals(expected, out.toString());
        }

        @Test
        @DisplayName("dump() should throw an exception for a null Appendable")
        void testDumpWithNullAppendable() {
            assertThrows(NullPointerException.class, () -> HexDump.dump(TEST_ARRAY, 0, null, 0, TEST_ARRAY.length));
        }

        @Test
        @DisplayName("dump() should throw an exception for a negative index")
        void testDumpWithNegativeIndex() {
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new StringBuilder(), -1, TEST_ARRAY.length));
        }

        @Test
        @DisplayName("dump() should throw an exception for an index equal to array length")
        void testDumpWithIndexTooLarge() {
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new StringBuilder(), TEST_ARRAY.length, 1));
        }

        @Test
        @DisplayName("dump() should throw an exception for a negative length")
        void testDumpWithNegativeLength() {
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new StringBuilder(), 0, -1));
        }

        @Test
        @DisplayName("dump() should throw an exception for a length that extends beyond the array")
        void testDumpWithLengthTooLarge() {
            final Exception e = assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new StringBuilder(), 1, TEST_ARRAY.length));
            assertEquals("Range [1, 1 + 256) out of bounds for length 256", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for dump() to OutputStream")
    class OutputStreamTests {

        @Test
        @DisplayName("Dumping a full array to a stream should write the correct bytes")
        void testDumpFullArrayToStream() throws IOException {
            // Arrange
            final byte[] expectedBytes = EXPECTED_FULL_DUMP.getBytes(Charset.defaultCharset());
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act
            HexDump.dump(TEST_ARRAY, 0, out, 0);

            // Assert
            assertArrayEquals(expectedBytes, out.toByteArray());
        }

        @Test
        @DisplayName("Dumping with a non-zero offset to a stream should write correct bytes")
        void testDumpWithOffsetToStream() throws IOException {
            // Arrange
            final String expected = """
                10000000 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................
                10000010 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................
                10000020 20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F  !"#$%&'()*+,-./
                10000030 30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F 0123456789:;<=>?
                10000040 40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F @ABCDEFGHIJKLMNO
                10000050 50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F PQRSTUVWXYZ[\\\\]^_
                10000060 60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F `abcdefghijklmno
                10000070 70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F pqrstuvwxyz{|}~.
                10000080 80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F ................
                10000090 90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F ................
                100000A0 A0 A1 A2 A3 A4 A5 A6 A7 A8 A9 AA AB AC AD AE AF ................
                100000B0 B0 B1 B2 B3 B4 B5 B6 B7 B8 B9 BA BB BC BD BE BF ................
                100000C0 C0 C1 C2 C3 C4 C5 C6 C7 C8 C9 CA CB CC CD CE CF ................
                100000D0 D0 D1 D2 D3 D4 D5 D6 D7 D8 D9 DA DB DC DD DE DF ................
                100000E0 E0 E1 E2 E3 E4 E5 E6 E7 E8 E9 EA EB EC ED EE EF ................
                100000F0 F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF ................
                """.replace("\n", EOL);
            final byte[] expectedBytes = expected.getBytes(Charset.defaultCharset());
            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            // Act
            HexDump.dump(TEST_ARRAY, 0x10000000, out, 0);

            // Assert
            assertArrayEquals(expectedBytes, out.toByteArray());
        }

        @Test
        @DisplayName("dump() should not close the provided OutputStream")
        void testDumpDoesNotCloseStream() throws IOException {
            // Arrange
            final ThrowOnCloseOutputStream stream = new ThrowOnCloseOutputStream(new ByteArrayOutputStream());

            // Act & Assert
            // No exception should be thrown, which proves close() was not called.
            HexDump.dump(TEST_ARRAY, 0, stream, 0);
        }

        @Test
        @DisplayName("dump() should throw an exception for a null OutputStream")
        void testDumpWithNullStream() {
            assertThrows(NullPointerException.class, () -> HexDump.dump(TEST_ARRAY, 0, null, 0));
        }

        @Test
        @DisplayName("dump() should throw an exception for a negative index")
        void testDumpWithNegativeIndex() {
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new ByteArrayOutputStream(), -1));
        }

        @Test
        @DisplayName("dump() should throw an exception for an index equal to array length")
        void testDumpWithIndexTooLarge() {
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(TEST_ARRAY, 0, new ByteArrayOutputStream(), TEST_ARRAY.length));
        }
    }
}