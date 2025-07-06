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
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.utils.ByteUtils.ByteConsumer;
import org.apache.commons.compress.utils.ByteUtils.ByteSupplier;
import org.junit.jupiter.api.Test;

class ByteUtilsTest {

    private static final byte[] EMPTY_BYTE_ARRAY = ByteUtils.EMPTY_BYTE_ARRAY; // Use the constant
    private static final long TEST_VALUE = 2 + 3 * 256 + 4 * 256 * 256;
    private static final long UNSIGNED_INT32_TEST_VALUE = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
    private static final byte[] TEST_BYTES = { 1, 2, 3, 4, 5 };
    private static final byte[] EXPECTED_BYTES = {2, 3, 4};
    private static final byte[] EXPECTED_UNSIGNED_INT32_BYTES = {2, 3, 4, (byte) 128};


    @Test
    void testFromLittleEndianFromArray() {
        // Arrange
        final byte[] b = TEST_BYTES;

        // Act
        final long result = fromLittleEndian(b, 1, 3);

        // Assert
        assertEquals(TEST_VALUE, result, "Should convert bytes from little-endian to long correctly");
    }

    @Test
    void testFromLittleEndianFromArrayOneArg() {
        // Arrange
        final byte[] b = { 2, 3, 4 };

        // Act
        final long result = fromLittleEndian(b);

        // Assert
        assertEquals(TEST_VALUE, result, "Should convert bytes from little-endian to long correctly");
    }

    @Test
    void testFromLittleEndianFromArrayOneArgThrowsForLengthTooBig() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }),
                "Should throw IllegalArgumentException for length > 8");
    }

    @Test
    void testFromLittleEndianFromArrayOneArgUnsignedInt32() {
        // Arrange
        final byte[] b = { 2, 3, 4, (byte) 128 };

        // Act
        final long result = fromLittleEndian(b);

        // Assert
        assertEquals(UNSIGNED_INT32_TEST_VALUE, result, "Should convert unsigned int32 from little-endian correctly");
    }

    @Test
    void testFromLittleEndianFromArrayThrowsForLengthTooBig() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(EMPTY_BYTE_ARRAY, 0, 9),
                "Should throw IllegalArgumentException for length > 8");
    }

    @Test
    void testFromLittleEndianFromArrayUnsignedInt32() {
        // Arrange
        final byte[] b = TEST_BYTES;

        // Act
        final long result = fromLittleEndian(b, 1, 4);

        // Assert
        assertEquals(UNSIGNED_INT32_TEST_VALUE, result, "Should convert unsigned int32 from little-endian correctly");
    }

    @Test
    void testFromLittleEndianFromDataInput() throws IOException {
        // Arrange
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 }));

        // Act
        final long result = fromLittleEndian(din, 3);

        // Assert
        assertEquals(TEST_VALUE, result, "Should convert bytes from DataInput in little-endian to long correctly");
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForLengthTooBig() {
        // Arrange
        final DataInput din = new DataInputStream(new ByteArrayInputStream(EMPTY_BYTE_ARRAY));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(din, 9),
                "Should throw IllegalArgumentException for length > 8");
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForPrematureEnd() {
        // Arrange
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3 }));

        // Assert
        assertThrows(EOFException.class, () -> fromLittleEndian(din, 3), "Should throw EOFException if not enough bytes are available");
    }

    @Test
    void testFromLittleEndianFromDataInputUnsignedInt32() throws IOException {
        // Arrange
        final DataInput din = new DataInputStream(new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 }));

        // Act
        final long result = fromLittleEndian(din, 4);

        // Assert
        assertEquals(UNSIGNED_INT32_TEST_VALUE, result, "Should convert unsigned int32 from DataInput in little-endian correctly");
    }

    @Test
    void testFromLittleEndianFromStream() throws IOException {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });

        // Act
        final long result = fromLittleEndian(bin, 3);

        // Assert
        assertEquals(TEST_VALUE, result, "Should convert bytes from InputStream in little-endian to long correctly");
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForLengthTooBig() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(new ByteArrayInputStream(EMPTY_BYTE_ARRAY), 9),
                "Should throw IllegalArgumentException for length > 8");
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForPrematureEnd() {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });

        // Assert
        assertThrows(IOException.class, () -> fromLittleEndian(bin, 3), "Should throw IOException if not enough bytes are available");
    }

    @Test
    void testFromLittleEndianFromStreamUnsignedInt32() throws IOException {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });

        // Act
        final long result = fromLittleEndian(bin, 4);

        // Assert
        assertEquals(UNSIGNED_INT32_TEST_VALUE, result, "Should convert unsigned int32 from InputStream in little-endian correctly");
    }

    @Test
    void testFromLittleEndianFromSupplier() throws IOException {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, 5 });
        final ByteSupplier supplier = bin::read;

        // Act
        final long result = fromLittleEndian(supplier, 3);

        // Assert
        assertEquals(TEST_VALUE, result, "Should convert bytes from ByteSupplier in little-endian to long correctly");
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForLengthTooBig() {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
        final ByteSupplier supplier = bin::read;


        // Assert
        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(supplier, 9),
                "Should throw IllegalArgumentException for length > 8");
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForPrematureEnd() {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3 });
        final ByteSupplier supplier = bin::read;

        // Assert
        assertThrows(IOException.class, () -> fromLittleEndian(supplier, 3), "Should throw IOException if not enough bytes are available");
    }

    @Test
    void testFromLittleEndianFromSupplierUnsignedInt32() throws IOException {
        // Arrange
        final InputStream bin = new ByteArrayInputStream(new byte[] { 2, 3, 4, (byte) 128 });
        final ByteSupplier supplier = bin::read;

        // Act
        final long result = fromLittleEndian(supplier, 4);

        // Assert
        assertEquals(UNSIGNED_INT32_TEST_VALUE, result, "Should convert unsigned int32 from ByteSupplier in little-endian correctly");
    }

    @Test
    void testToLittleEndianToByteArray() {
        // Arrange
        final byte[] b = new byte[4];

        // Act
        toLittleEndian(b, TEST_VALUE, 1, 3);

        // Assert
        assertArrayEquals(EXPECTED_BYTES, Arrays.copyOfRange(b, 1, 4), "Should convert long to little-endian bytes in array correctly");
    }

    @Test
    void testToLittleEndianToByteArrayUnsignedInt32() {
        // Arrange
        final byte[] b = new byte[4];

        // Act
        toLittleEndian(b, UNSIGNED_INT32_TEST_VALUE, 0, 4);

        // Assert
        assertArrayEquals(EXPECTED_UNSIGNED_INT32_BYTES, b, "Should convert unsigned int32 to little-endian bytes in array correctly");
    }

    @Test
    void testToLittleEndianToConsumer() throws IOException {
        // Arrange
        final byte[] byteArray;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ByteConsumer consumer = bos::write;

        // Act
        toLittleEndian(consumer, TEST_VALUE, 3);
        byteArray = bos.toByteArray();
        bos.close();


        // Assert
        assertArrayEquals(EXPECTED_BYTES, byteArray, "Should convert long to little-endian bytes using ByteConsumer correctly");
    }

    @Test
    void testToLittleEndianToConsumerUnsignedInt32() throws IOException {
        // Arrange
        final byte[] byteArray;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ByteConsumer consumer = bos::write;

        // Act
        toLittleEndian(consumer, UNSIGNED_INT32_TEST_VALUE, 4);
        byteArray = bos.toByteArray();
        bos.close();

        // Assert
        assertArrayEquals(EXPECTED_UNSIGNED_INT32_BYTES, byteArray, "Should convert unsigned int32 to little-endian bytes using ByteConsumer correctly");
    }

    @Test
    void testToLittleEndianToDataOutput() throws IOException {
        // Arrange
        final byte[] byteArray;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutput dos = new DataOutputStream(bos)) {

            // Act
            toLittleEndian(dos, TEST_VALUE, 3);
            byteArray = bos.toByteArray();

            // Assert
            assertArrayEquals(EXPECTED_BYTES, byteArray, "Should convert long to little-endian bytes using DataOutput correctly");
        }
    }

    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        // Arrange
        final byte[] byteArray;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             DataOutput dos = new DataOutputStream(bos)) {

            // Act
            toLittleEndian(dos, UNSIGNED_INT32_TEST_VALUE, 4);
            byteArray = bos.toByteArray();

            // Assert
            assertArrayEquals(EXPECTED_UNSIGNED_INT32_BYTES, byteArray, "Should convert unsigned int32 to little-endian bytes using DataOutput correctly");
        }
    }

    @Test
    void testToLittleEndianToStream() throws IOException {
        // Arrange
        final byte[] byteArray;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            // Act
            toLittleEndian(bos, TEST_VALUE, 3);
            byteArray = bos.toByteArray();
            // Assert
            assertArrayEquals(EXPECTED_BYTES, byteArray, "Should convert long to little-endian bytes using OutputStream correctly");
        }

    }

    @Test
    void testToLittleEndianToStreamUnsignedInt32() throws IOException {
        // Arrange
        final byte[] byteArray;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            // Act
            toLittleEndian(bos, UNSIGNED_INT32_TEST_VALUE, 4);
            byteArray = bos.toByteArray();

            // Assert
            assertArrayEquals(EXPECTED_UNSIGNED_INT32_BYTES, byteArray, "Should convert unsigned int32 to little-endian bytes using OutputStream correctly");
        }
    }
}