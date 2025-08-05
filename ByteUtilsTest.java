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
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.apache.commons.compress.utils.ByteUtils.OutputStreamByteConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ByteUtilsTest {

    // Using hexadecimal literals makes the little-endian byte order clear.
    // 0x040302L corresponds to byte array {0x02, 0x03, 0x04}.
    private static final long VALUE_3_BYTES = 0x040302L;
    private static final byte[] BYTES_3 = {2, 3, 4};

    // Represents a 32-bit unsigned integer that would overflow a signed int.
    // 0x80040302L corresponds to byte array {0x02, 0x03, 0x04, 0x80}.
    private static final long VALUE_UNSIGNED_INT = 0x80040302L;
    private static final byte[] BYTES_UNSIGNED_INT = {2, 3, 4, (byte) 128};

    @Nested
    @DisplayName("fromLittleEndian reads from various sources")
    class FromLittleEndianTests {

        @Nested
        @DisplayName("from a byte array")
        class FromArray {
            @Test
            @DisplayName("should convert a full byte array")
            void shouldConvertFullArray() {
                assertEquals(VALUE_3_BYTES, fromLittleEndian(BYTES_3));
            }

            @Test
            @DisplayName("should convert a slice of a byte array")
            void shouldConvertArraySlice() {
                final byte[] data = {0, 2, 3, 4, 5};
                assertEquals(VALUE_3_BYTES, fromLittleEndian(data, 1, 3));
            }

            @Test
            @DisplayName("should convert an unsigned int value from a full byte array")
            void shouldConvertUnsignedIntFromFullArray() {
                assertEquals(VALUE_UNSIGNED_INT, fromLittleEndian(BYTES_UNSIGNED_INT));
            }

            @Test
            @DisplayName("should convert an unsigned int value from a slice of a byte array")
            void shouldConvertUnsignedIntFromArraySlice() {
                final byte[] data = {0, 2, 3, 4, (byte) 128};
                assertEquals(VALUE_UNSIGNED_INT, fromLittleEndian(data, 1, 4));
            }

            @Test
            @DisplayName("should throw exception if array length is > 8")
            void shouldThrowForArrayLongerThan8() {
                final byte[] tooLong = new byte[9];
                assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(tooLong));
            }

            @Test
            @DisplayName("should throw exception if requested length is > 8")
            void shouldThrowForLengthParameterGreaterThan8() {
                assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, 9));
            }
        }

        private void testFromInput(final FromInputFactory factory) throws IOException {
            // Standard value
            assertEquals(VALUE_3_BYTES, fromLittleEndian(factory.create(BYTES_3), 3));

            // Unsigned int value
            assertEquals(VALUE_UNSIGNED_INT, fromLittleEndian(factory.create(BYTES_UNSIGNED_INT), 4));

            // Premature end of input
            assertThrows(IOException.class, () -> fromLittleEndian(factory.create(new byte[]{2, 3}), 3));

            // Length > 8
            assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(factory.create(ByteUtils.EMPTY_BYTE_ARRAY), 9));
        }

        @Test
        @DisplayName("from a DataInput")
        void fromDataInput() throws IOException {
            testFromInput(data -> new DataInputStream(new ByteArrayInputStream(data)));
        }

        @Test
        @DisplayName("from an InputStream")
        void fromInputStream() throws IOException {
            testFromInput(ByteArrayInputStream::new);
        }

        @Test
        @DisplayName("from a ByteSupplier")
        void fromByteSupplier() throws IOException {
            testFromInput(data -> new InputStreamByteSupplier(new ByteArrayInputStream(data)));
        }

        // Functional interface to abstract input creation for different sources
        @FunctionalInterface
        private interface FromInputFactory {
            Object create(byte[] data) throws IOException;
        }
    }

    @Nested
    @DisplayName("toLittleEndian writes to various targets")
    class ToLittleEndianTests {

        @Nested
        @DisplayName("to a byte array")
        class ToArray {
            @Test
            @DisplayName("should write value to a slice of a byte array")
            void shouldWriteToByteArraySlice() {
                final byte[] target = new byte[5];
                toLittleEndian(target, VALUE_3_BYTES, 1, 3);
                final byte[] writtenBytes = Arrays.copyOfRange(target, 1, 4);
                assertArrayEquals(BYTES_3, writtenBytes);
            }

            @Test
            @DisplayName("should write an unsigned int value to a byte array")
            void shouldWriteUnsignedIntToByteArray() {
                final byte[] target = new byte[4];
                toLittleEndian(target, VALUE_UNSIGNED_INT, 0, 4);
                assertArrayEquals(BYTES_UNSIGNED_INT, target);
            }
        }

        private void testToOutput(final ToOutputRunner runner) throws IOException {
            // Standard value
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                runner.run(bos, VALUE_3_BYTES, 3);
                assertArrayEquals(BYTES_3, bos.toByteArray());
            }

            // Unsigned int value
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                runner.run(bos, VALUE_UNSIGNED_INT, 4);
                assertArrayEquals(BYTES_UNSIGNED_INT, bos.toByteArray());
            }
        }

        @Test
        @DisplayName("to a DataOutput")
        void toDataOutput() throws IOException {
            testToOutput((bos, value, length) -> {
                final DataOutput dos = new DataOutputStream(bos);
                toLittleEndian(dos, value, length);
            });
        }

        @Test
        @DisplayName("to an OutputStream")
        void toOutputStream() throws IOException {
            testToOutput((bos, value, length) -> toLittleEndian(bos, value, length));
        }

        @Test
        @DisplayName("to a ByteConsumer")
        void toByteConsumer() throws IOException {
            testToOutput((bos, value, length) -> {
                final OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(bos);
                toLittleEndian(consumer, value, length);
            });
        }

        // Functional interface to abstract writing for different targets
        @FunctionalInterface
        private interface ToOutputRunner {
            void run(ByteArrayOutputStream bos, long value, int length) throws IOException;
        }
    }
}