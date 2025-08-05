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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ByteUtilsTest {

    @Nested
    class FromLittleEndian {

        @Test
        void fromArray() {
            final byte[] b = { 1, 2, 3, 4, 5 };
            long result = fromLittleEndian(b, 1, 3);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256, result);
        }

        @Test
        void fromArrayOneArg() {
            final byte[] b = { 2, 3, 4 };
            long result = fromLittleEndian(b);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256, result);
        }

        @Test
        void fromArrayOneArgThrowsForLengthTooBig() {
            assertThrows(IllegalArgumentException.class, 
                () -> fromLittleEndian(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
        }

        @Test
        void fromArrayOneArgUnsignedInt32() {
            final byte[] b = { 2, 3, 4, (byte) 128 };
            long result = fromLittleEndian(b);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, result);
        }

        @Test
        void fromArrayThrowsForLengthTooBig() {
            assertThrows(IllegalArgumentException.class, 
                () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
        }

        @Test
        void fromArrayUnsignedInt32() {
            final byte[] b = { 1, 2, 3, 4, (byte) 128 };
            long result = fromLittleEndian(b, 1, 4);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, result);
        }

        @Test
        void fromDataInput() throws IOException {
            final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));
            long result = fromLittleEndian(din, 3);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256, result);
        }

        @Test
        void fromDataInputThrowsForLengthTooBig() {
            final DataInput din = new DataInputStream(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY));
            assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(din, 9));
        }

        @Test
        void fromDataInputThrowsForPrematureEnd() {
            final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));
            assertThrows(EOFException.class, () -> fromLittleEndian(din, 3));
        }

        @Test
        void fromDataInputUnsignedInt32() throws IOException {
            final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));
            long result = fromLittleEndian(din, 4);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, result);
        }

        @Test
        void fromStream() throws IOException {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
            long result = fromLittleEndian(bin, 3);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256, result);
        }

        @Test
        void fromStreamThrowsForLengthTooBig() {
            assertThrows(IllegalArgumentException.class, 
                () -> fromLittleEndian(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY), 9));
        }

        @Test
        void fromStreamThrowsForPrematureEnd() {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });
            assertThrows(IOException.class, () -> fromLittleEndian(bin, 3));
        }

        @Test
        void fromStreamUnsignedInt32() throws IOException {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
            long result = fromLittleEndian(bin, 4);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, result);
        }

        @Test
        void fromSupplier() throws IOException {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
            long result = fromLittleEndian(new InputStreamByteSupplier(bin), 3);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256, result);
        }

        @Test
        void fromSupplierThrowsForLengthTooBig() {
            assertThrows(IllegalArgumentException.class,
                () -> fromLittleEndian(new InputStreamByteSupplier(new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY)), 9));
        }

        @Test
        void fromSupplierThrowsForPrematureEnd() {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });
            assertThrows(IOException.class, () -> fromLittleEndian(new InputStreamByteSupplier(bin), 3));
        }

        @Test
        void fromSupplierUnsignedInt32() throws IOException {
            final ByteArrayInputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
            long result = fromLittleEndian(new InputStreamByteSupplier(bin), 4);
            assertEquals(2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, result);
        }
    }

    @Nested
    class ToLittleEndian {

        @Test
        void toByteArray() {
            final byte[] b = new byte[4];
            toLittleEndian(b, 2 + 3 * 256 + 4 * 256 * 256, 1, 3);
            assertArrayEquals(new byte[] { 2, 3, 4 }, Arrays.copyOfRange(b, 1, 4));
        }

        @Test
        void toByteArrayUnsignedInt32() {
            final byte[] b = new byte[4];
            toLittleEndian(b, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 0, 4);
            assertArrayEquals(new byte[] { 2, 3, 4, (byte) 128 }, b);
        }

        @Test
        void toConsumer() throws IOException {
            final byte[] expected = { 2, 3, 4 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                toLittleEndian(new OutputStreamByteConsumer(bos), 2 + 3 * 256 + 4 * 256 * 256, 3);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }

        @Test
        void toConsumerUnsignedInt32() throws IOException {
            final byte[] expected = { 2, 3, 4, (byte) 128 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                toLittleEndian(new OutputStreamByteConsumer(bos), 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }

        @Test
        void toDataOutput() throws IOException {
            final byte[] expected = { 2, 3, 4 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                final DataOutput dos = new DataOutputStream(bos);
                toLittleEndian(dos, 2 + 3 * 256 + 4 * 256 * 256, 3);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }

        @Test
        void toDataOutputUnsignedInt32() throws IOException {
            final byte[] expected = { 2, 3, 4, (byte) 128 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                final DataOutput dos = new DataOutputStream(bos);
                toLittleEndian(dos, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }

        @Test
        void toStream() throws IOException {
            final byte[] expected = { 2, 3, 4 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                toLittleEndian(bos, 2 + 3 * 256 + 4 * 256 * 256, 3);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }

        @Test
        void toStreamUnsignedInt32() throws IOException {
            final byte[] expected = { 2, 3, 4, (byte) 128 };
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                toLittleEndian(bos, 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256, 4);
                assertArrayEquals(expected, bos.toByteArray());
            }
        }
    }
}