/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.apache.commons.compress.utils.ByteUtils.toLittleEndian;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.Test;

/**
 * Tests for little-endian read/write helpers in ByteUtils.
 *
 * Test data is expressed as byte sequences and converted to numeric values via helpers,
 * avoiding hard-to-read expressions like 2 + 3 * 256 + ...
 */
class ByteUtilsTest {

    // ----------------------------
    // Test helpers
    // ----------------------------

    /** Converts a sequence of unsigned bytes (little-endian order) into a long. */
    private static long asLongLE(final int... unsignedBytes) {
        long value = 0;
        for (int i = 0; i < unsignedBytes.length; i++) {
            value |= ((long) (unsignedBytes[i] & 0xFF)) << (8 * i);
        }
        return value;
    }

    /** Convenience to build a byte[] from unsigned int values (0..255). */
    private static byte[] bytes(final int... unsignedBytes) {
        final byte[] out = new byte[unsignedBytes.length];
        for (int i = 0; i < unsignedBytes.length; i++) {
            out[i] = (byte) (unsignedBytes[i] & 0xFF);
        }
        return out;
    }

    // ----------------------------
    // fromLittleEndian - byte[]
    // ----------------------------

    @Test
    void fromArray_withOffsetAndLength_readsLittleEndian() {
        final byte[] src = { 1, 2, 3, 4, 5 };
        assertEquals(asLongLE(2, 3, 4), fromLittleEndian(src, 1, 3));
    }

    @Test
    void fromArray_entireArray_readsLittleEndian() {
        final byte[] src = { 2, 3, 4 };
        assertEquals(asLongLE(2, 3, 4), fromLittleEndian(src));
    }

    @Test
    void fromArray_entireArray_throwsWhenLengthTooBig() {
        // 9 bytes > 8-byte maximum for a long
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(bytes(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    void fromArray_entireArray_handlesUnsignedInt32() {
        // Top bit set in the highest byte -> verify we read into a long correctly
        assertEquals(asLongLE(2, 3, 4, 128), fromLittleEndian(bytes(2, 3, 4, 128)));
    }

    @Test
    void fromArray_withOffsetAndLength_throwsWhenLengthTooBig() {
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
    }

    @Test
    void fromArray_withOffsetAndLength_handlesUnsignedInt32() {
        final byte[] src = { 1, 2, 3, 4, (byte) 128 };
        assertEquals(asLongLE(2, 3, 4, 128), fromLittleEndian(src, 1, 4));
    }

    // ----------------------------
    // fromLittleEndian - DataInput
    // ----------------------------

    @Test
    void fromDataInput_readsLittleEndian() throws IOException {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(bytes(2, 3, 4, 5)));
        assertEquals(asLongLE(2, 3, 4), fromLittleEndian(din, 3));
    }

    @Test
    void fromDataInput_throwsWhenLengthTooBig() {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(din, 9));
    }

    @Test
    void fromDataInput_throwsOnPrematureEnd() {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(bytes(2, 3)));
        assertThrows(EOFException.class, () -> fromLittleEndian(din, 3));
    }

    @Test
    void fromDataInput_handlesUnsignedInt32() throws IOException {
        final DataInput din = new DataInputStream(new ByteArrayInputStream(bytes(2, 3, 4, 128)));
        assertEquals(asLongLE(2, 3, 4, 128), fromLittleEndian(din, 4));
    }

    // ----------------------------
    // fromLittleEndian - InputStream (deprecated)
    // ----------------------------

    @Test
    void fromInputStream_readsLittleEndian() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3, 4, 5));
        assertEquals(asLongLE(2, 3, 4), fromLittleEndian(in, 3));
    }

    @Test
    void fromInputStream_throwsWhenLengthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), 9));
    }

    @Test
    void fromInputStream_throwsOnPrematureEnd() {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3));
        assertThrows(IOException.class, () -> fromLittleEndian(in, 3));
    }

    @Test
    void fromInputStream_handlesUnsignedInt32() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3, 4, 128));
        assertEquals(asLongLE(2, 3, 4, 128), fromLittleEndian(in, 4));
    }

    // ----------------------------
    // fromLittleEndian - ByteSupplier
    // ----------------------------

    @Test
    void fromSupplier_readsLittleEndian() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3, 4, 5));
        assertEquals(asLongLE(2, 3, 4), fromLittleEndian(new InputStreamByteSupplier(in), 3));
    }

    @Test
    void fromSupplier_throwsWhenLengthTooBig() {
        assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY)), 9));
    }

    @Test
    void fromSupplier_throwsOnPrematureEnd() {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3));
        assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(in), 3));
    }

    @Test
    void fromSupplier_handlesUnsignedInt32() throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes(2, 3, 4, 128));
        assertEquals(asLongLE(2, 3, 4, 128), fromLittleEndian(new InputStreamByteSupplier(in), 4));
    }

    // ----------------------------
    // toLittleEndian - byte[]
    // ----------------------------

    @Test
    void toArray_writesLittleEndianAtOffset() {
        final byte[] out = new byte[4];
        toLittleEndian(out, asLongLE(2, 3, 4), 1, 3);
        assertArrayEquals(bytes(2, 3, 4), Arrays.copyOfRange(out, 1, 4));
    }

    @Test
    void toArray_writesUnsignedInt32() {
        final byte[] out = new byte[4];
        toLittleEndian(out, asLongLE(2, 3, 4, 128), 0, 4);
        assertArrayEquals(bytes(2, 3, 4, 128), out);
    }

    // ----------------------------
    // toLittleEndian - ByteConsumer
    // ----------------------------

    @Test
    void toConsumer_writesLittleEndian() throws IOException {
        final byte[] expected = bytes(2, 3, 4);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(bos), asLongLE(2, 3, 4), 3);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    void toConsumer_writesUnsignedInt32() throws IOException {
        final byte[] expected = bytes(2, 3, 4, 128);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(new OutputStreamByteConsumer(bos), asLongLE(2, 3, 4, 128), 4);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }

    // ----------------------------
    // toLittleEndian - DataOutput (deprecated)
    // ----------------------------

    @Test
    void toDataOutput_writesLittleEndian() throws IOException {
        final byte[] expected = bytes(2, 3, 4);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);
            toLittleEndian(dos, asLongLE(2, 3, 4), 3);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    void toDataOutput_writesUnsignedInt32() throws IOException {
        final byte[] expected = bytes(2, 3, 4, 128);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final DataOutput dos = new DataOutputStream(bos);
            toLittleEndian(dos, asLongLE(2, 3, 4, 128), 4);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }

    // ----------------------------
    // toLittleEndian - OutputStream
    // ----------------------------

    @Test
    void toStream_writesLittleEndian() throws IOException {
        final byte[] expected = bytes(2, 3, 4);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(bos, asLongLE(2, 3, 4), 3);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    void toStream_writesUnsignedInt32() throws IOException {
        final byte[] expected = bytes(2, 3, 4, 128);
        byte[] actual;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            toLittleEndian(bos, asLongLE(2, 3, 4, 128), 4);
            actual = bos.toByteArray();
        }
        assertArrayEquals(expected, actual);
    }
}