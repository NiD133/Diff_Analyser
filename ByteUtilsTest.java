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

class ByteUtilsTest {

    private static final byte[] EMPTY_BYTE_ARRAY = ByteUtils.EMPTY_BYTE_ARRAY;

    @Test
    void testFromLittleEndianFromArray() {
        byte[] inputBytes = { 1, 2, 3, 4, 5 };
        int offset = 1;
        int length = 3;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;

        long actualValue = fromLittleEndian(inputBytes, offset, length);

        assertEquals(expectedValue, actualValue, "Should convert bytes to little endian long correctly.");
    }

    @Test
    void testFromLittleEndianFromArrayOneArg() {
        byte[] inputBytes = { 2, 3, 4 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;

        long actualValue = fromLittleEndian(inputBytes);

        assertEquals(expectedValue, actualValue, "Should convert bytes to little endian long correctly.");
    }

    @Test
    void testFromLittleEndianFromArrayOneArgThrowsForLengthTooBig() {
        byte[] inputBytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(inputBytes),
                "Should throw IllegalArgumentException when byte array length exceeds maximum allowed length.");
    }

    @Test
    void testFromLittleEndianFromArrayOneArgUnsignedInt32() {
        byte[] inputBytes = { 2, 3, 4, (byte) 128 };
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;

        long actualValue = fromLittleEndian(inputBytes);

        assertEquals(expectedValue, actualValue, "Should convert bytes to little endian long correctly, handling unsigned int32.");
    }

    @Test
    void testFromLittleEndianFromArrayThrowsForLengthTooBig() {
        int offset = 0;
        int length = 9;

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(EMPTY_BYTE_ARRAY, offset, length),
                "Should throw IllegalArgumentException when length exceeds maximum allowed length.");
    }

    @Test
    void testFromLittleEndianFromArrayUnsignedInt32() {
        byte[] inputBytes = { 1, 2, 3, 4, (byte) 128 };
        int offset = 1;
        int length = 4;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;

        long actualValue = fromLittleEndian(inputBytes, offset, length);

        assertEquals(expectedValue, actualValue, "Should convert bytes to little endian long correctly, handling unsigned int32.");
    }

    @Test
    void testFromLittleEndianFromDataInput() throws IOException {
        byte[] inputBytes = { 2, 3, 4, 5 };
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(inputBytes));
        int length = 3;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;

        long actualValue = fromLittleEndian(dataInput, length);

        assertEquals(expectedValue, actualValue, "Should convert DataInput to little endian long correctly.");
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForLengthTooBig() {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(EMPTY_BYTE_ARRAY));
        int length = 9;

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(dataInput, length),
                "Should throw IllegalArgumentException when length exceeds maximum allowed length.");
    }

    @Test
    void testFromLittleEndianFromDataInputThrowsForPrematureEnd() {
        byte[] inputBytes = { 2, 3 };
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(inputBytes));
        int length = 3;

        assertThrows(EOFException.class, () -> fromLittleEndian(dataInput, length),
                "Should throw EOFException when DataInput ends prematurely.");
    }

    @Test
    void testFromLittleEndianFromDataInputUnsignedInt32() throws IOException {
        byte[] inputBytes = { 2, 3, 4, (byte) 128 };
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(inputBytes));
        int length = 4;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;

        long actualValue = fromLittleEndian(dataInput, length);

        assertEquals(expectedValue, actualValue, "Should convert DataInput to little endian long correctly, handling unsigned int32.");
    }

    @Test
    void testFromLittleEndianFromStream() throws IOException {
        byte[] inputBytes = { 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        int length = 3;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;

        long actualValue = fromLittleEndian(inputStream, length);

        assertEquals(expectedValue, actualValue, "Should convert InputStream to little endian long correctly.");
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForLengthTooBig() {
        int length = 9;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(EMPTY_BYTE_ARRAY);

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(inputStream, length),
                "Should throw IllegalArgumentException when length exceeds maximum allowed length.");
    }

    @Test
    void testFromLittleEndianFromStreamThrowsForPrematureEnd() {
        byte[] inputBytes = { 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        int length = 3;

        assertThrows(IOException.class, () -> fromLittleEndian(inputStream, length),
                "Should throw IOException when InputStream ends prematurely.");
    }

    @Test
    void testFromLittleEndianFromStreamUnsignedInt32() throws IOException {
        byte[] inputBytes = { 2, 3, 4, (byte) 128 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        int length = 4;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;

        long actualValue = fromLittleEndian(inputStream, length);

        assertEquals(expectedValue, actualValue, "Should convert InputStream to little endian long correctly, handling unsigned int32.");
    }

    @Test
    void testFromLittleEndianFromSupplier() throws IOException {
        byte[] inputBytes = { 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);
        int length = 3;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256;

        long actualValue = fromLittleEndian(supplier, length);

        assertEquals(expectedValue, actualValue, "Should convert ByteSupplier to little endian long correctly.");
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForLengthTooBig() {
        int length = 9;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
        InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);

        assertThrows(IllegalArgumentException.class, () -> fromLittleEndian(supplier, length),
                "Should throw IllegalArgumentException when length exceeds maximum allowed length.");
    }

    @Test
    void testFromLittleEndianFromSupplierThrowsForPrematureEnd() {
        byte[] inputBytes = { 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);
        int length = 3;

        assertThrows(IOException.class, () -> fromLittleEndian(supplier, length),
                "Should throw IOException when ByteSupplier ends prematurely.");
    }

    @Test
    void testFromLittleEndianFromSupplierUnsignedInt32() throws IOException {
        byte[] inputBytes = { 2, 3, 4, (byte) 128 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);
        int length = 4;
        long expectedValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;

        long actualValue = fromLittleEndian(supplier, length);

        assertEquals(expectedValue, actualValue, "Should convert ByteSupplier to little endian long correctly, handling unsigned int32.");
    }

    @Test
    void testToLittleEndianToByteArray() {
        byte[] destinationArray = new byte[4];
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256;
        int offset = 1;
        int length = 3;
        byte[] expectedBytes = { 2, 3, 4 };

        toLittleEndian(destinationArray, inputValue, offset, length);

        assertArrayEquals(expectedBytes, Arrays.copyOfRange(destinationArray, 1, 4), "Should convert long to little endian byte array correctly.");
    }

    @Test
    void testToLittleEndianToByteArrayUnsignedInt32() {
        byte[] destinationArray = new byte[4];
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        int offset = 0;
        int length = 4;
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };

        toLittleEndian(destinationArray, inputValue, offset, length);

        assertArrayEquals(expectedBytes, destinationArray, "Should convert long to little endian byte array correctly, handling unsigned int32.");
    }

    @Test
    void testToLittleEndianToConsumer() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256;
        int length = 3;
        byte[] expectedBytes = { 2, 3, 4 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(byteArrayOutputStream);
            toLittleEndian(consumer, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via ByteConsumer correctly.");
    }

    @Test
    void testToLittleEndianToConsumerUnsignedInt32() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        int length = 4;
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(byteArrayOutputStream);
            toLittleEndian(consumer, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via ByteConsumer correctly, handling unsigned int32.");
    }

    @Test
    void testToLittleEndianToDataOutput() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256;
        int length = 3;
        byte[] expectedBytes = { 2, 3, 4 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
            toLittleEndian(dataOutput, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via DataOutput correctly.");
    }

    @Test
    void testToLittleEndianToDataOutputUnsignedInt32() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        int length = 4;
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
            toLittleEndian(dataOutput, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via DataOutput correctly, handling unsigned int32.");
    }

    @Test
    void testToLittleEndianToStream() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256;
        int length = 3;
        byte[] expectedBytes = { 2, 3, 4 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            toLittleEndian(byteArrayOutputStream, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via OutputStream correctly.");
    }

    @Test
    void testToLittleEndianToStreamUnsignedInt32() throws IOException {
        long inputValue = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256;
        int length = 4;
        byte[] expectedBytes = { 2, 3, 4, (byte) 128 };
        byte[] actualBytes;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            toLittleEndian(byteArrayOutputStream, inputValue, length);
            actualBytes = byteArrayOutputStream.toByteArray();
        }

        assertArrayEquals(expectedBytes, actualBytes, "Should convert long to little endian via OutputStream correctly, handling unsigned int32.");
    }
}