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

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link HexDump}.
 */
public class HexDumpTest {

    private static final String EOL = System.lineSeparator();

    @Test
    public void constructor_shouldCreateInstance() {
        new HexDump();
        // Test passes if constructor doesn't throw.
    }

    // --- Test dump(byte[], Appendable) ---

    @Test
    public void dump_fullArrayToAppendable_shouldProduceCorrectHexAndAscii() throws IOException {
        final StringWriter writer = new StringWriter();
        final byte[] data = {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF};

        HexDump.dump(data, writer);

        final String expected = "0000000000000000 DE AD BE EF                                           ....            " + EOL;
        assertEquals(expected, writer.toString());
    }

    @Test
    public void dump_fullArrayWithPrintableAndNonPrintableChars_shouldRenderCorrectly() throws IOException {
        final StringWriter writer = new StringWriter();
        // 'H', 'e', 'l', 'l', 'o', non-printable 0x00, non-printable 0xFF
        final byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x00, (byte) 0xFF};

        HexDump.dump(data, writer);

        final String expected = "0000000000000000 48 65 6C 6C 6F 00 FF                               Hello..         " + EOL;
        assertEquals(expected, writer.toString());
    }

    @Test
    public void dump_emptyArrayToAppendable_shouldThrowException() throws IOException {
        final StringWriter writer = new StringWriter();
        final byte[] data = new byte[0];

        // The dump method throws when index (0) is >= data.length (0)
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> HexDump.dump(data, writer));
    }

    @Test(expected = NullPointerException.class)
    public void dump_byteArrayToNullAppendable_shouldThrowNullPointerException() throws IOException {
        HexDump.dump(new byte[1], null);
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void dump_byteArrayToReadOnlyBuffer_shouldThrowException() throws IOException {
        final byte[] data = new byte[2];
        final CharBuffer readOnlyBuffer = CharBuffer.wrap("AB").asReadOnlyBuffer();
        HexDump.dump(data, readOnlyBuffer);
    }

    @Test(expected = BufferOverflowException.class)
    public void dump_byteArrayToSmallBuffer_shouldThrowException() throws IOException {
        final byte[] data = new byte[10];
        final CharBuffer smallBuffer = CharBuffer.allocate(5);
        HexDump.dump(data, smallBuffer);
    }

    // --- Test dump(byte[], long, OutputStream, int) ---

    @Test
    public void dump_partialArrayToOutputStream_shouldProduceCorrectOutput() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        // Dump 3 bytes starting from index 2 with an offset of 0x100
        HexDump.dump(data, 0x100L, baos, 2);

        // Should dump bytes 2, 3, 4, 5, 6, 7
        final String expected = "0000000000000100 02 03 04 05 06 07                                  ......          " + EOL;
        assertEquals(expected, baos.toString());
    }

    @Test
    public void dump_toOutputStreamWithNegativeOffset_shouldFormatOffsetCorrectly() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] data = {0x0A, 0x0B};

        HexDump.dump(data, -1L, baos, 0);

        final String expected = "FFFFFFFFFFFFFFFF 0A 0B                                              ..              " + EOL;
        assertEquals(expected, baos.toString());
    }

    @Test(expected = NullPointerException.class)
    public void dump_nullArrayToOutputStream_shouldThrowNullPointerException() throws IOException {
        HexDump.dump(null, 0L, new ByteArrayOutputStream(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void dump_byteArrayToNullOutputStream_shouldThrowNullPointerException() throws IOException {
        HexDump.dump(new byte[1], 0L, null, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dump_toOutputStreamWithIndexOutOfBounds_shouldThrowException() throws IOException {
        HexDump.dump(new byte[5], 0L, new ByteArrayOutputStream(), 5);
    }

    @Test(expected = IOException.class)
    public void dump_toClosedOutputStream_shouldThrowIOException() throws IOException {
        final PipedOutputStream closedStream = new PipedOutputStream();
        // A PipedOutputStream not connected to a PipedInputStream will throw IOException on write.
        HexDump.dump(new byte[1], 0L, closedStream, 0);
    }

    // --- Test dump(byte[], long, Appendable, int, int) ---

    @Test
    public void dump_multipleLinesToAppendable_shouldProduceCorrectOutput() throws IOException {
        final StringWriter writer = new StringWriter();
        final byte[] data = new byte[33]; // More than two lines
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }

        // Dump 33 bytes from index 0 with offset 0
        HexDump.dump(data, 0L, writer, 0, 33);

        final String expected =
            "0000000000000000 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F ................" + EOL +
            "0000000000000010 10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F ................" + EOL +
            "0000000000000020 20                                                 .               " + EOL;

        assertEquals(expected, writer.toString());
    }

    @Test
    public void dump_zeroLengthToAppendable_shouldProduceNoOutput() throws IOException {
        final StringWriter writer = new StringWriter();
        HexDump.dump(new byte[5], 0L, writer, 1, 0);
        assertEquals("", writer.toString());
    }

    @Test(expected = NullPointerException.class)
    public void dump_nullArrayToAppendable_shouldThrowNullPointerException() throws IOException {
        HexDump.dump(null, 0L, new StringWriter(), 0, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dump_partialWithNegativeIndex_shouldThrowException() throws IOException {
        HexDump.dump(new byte[5], 0L, new StringWriter(), -1, 2);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void dump_partialWithNegativeLength_shouldThrowException() throws IOException {
        HexDump.dump(new byte[5], 0L, new StringWriter(), 0, -1);
    }

    @Test
    public void dump_partialWithLengthOutOfBounds_shouldThrowException() throws IOException {
        try {
            HexDump.dump(new byte[10], 0L, new StringWriter(), 5, 6);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Expected
            assertEquals("Range [5, 5 + 6) out of bounds for length 10", e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void dump_toClosedAppendable_shouldThrowIOException() throws IOException {
        final PipedWriter writer = new PipedWriter();
        // A PipedWriter not connected to a PipedReader will throw IOException on write.
        HexDump.dump(new byte[1], 0L, writer, 0, 1);
    }
}