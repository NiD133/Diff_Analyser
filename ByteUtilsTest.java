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
 * Tests for ByteUtils class focusing on little-endian byte conversion operations.
 * 
 * Little-endian format stores the least significant byte first.
 * For example: bytes [2, 3, 4] represent the value 2 + 3*256 + 4*256² = 263,170
 */
class ByteUtilsTest {

    // Test data constants for better readability and maintainability
    private static final byte[] TEST_BYTES_3 = {2, 3, 4};
    private static final byte[] TEST_BYTES_4_WITH_SIGN = {2, 3, 4, (byte) 128};
    private static final byte[] TEST_BYTES_5 = {1, 2, 3, 4, 5};
    
    // Expected values calculated from little-endian interpretation
    private static final long EXPECTED_VALUE_3_BYTES = 2 + 3 * 256 + 4 * 256 * 256; // 263,170
    private static final long EXPECTED_VALUE_4_BYTES_UNSIGNED = 2 + 3 * 256 + 4 * 256 * 256 + 128L * 256 * 256 * 256; // 2,147,746,818
    
    // Constants for array bounds testing
    private static final int MAX_SUPPORTED_BYTE_LENGTH = 8;
    private static final int INVALID_LENGTH = MAX_SUPPORTED_BYTE_LENGTH + 1;

    // ========== fromLittleEndian tests - byte array sources ==========

    @Test
    void testFromLittleEndian_ByteArrayWithOffset_ReturnsCorrectValue() {
        // Test reading 3 bytes starting from offset 1 in a 5-byte array
        long actualValue = fromLittleEndian(TEST_BYTES_5, 1, 3);
        assertEquals(EXPECTED_VALUE_3_BYTES, actualValue);
    }

    @Test
    void testFromLittleEndian_EntireByteArray_ReturnsCorrectValue() {
        long actualValue = fromLittleEndian(TEST_BYTES_3);
        assertEquals(EXPECTED_VALUE_3_BYTES, actualValue);
    }

    @Test
    void testFromLittleEndian_EntireByteArray_HandlesUnsignedInt32() {
        // Test with byte 128 (0x80) which has the sign bit set
        long actualValue = fromLittleEndian(TEST_BYTES_4_WITH_SIGN);
        assertEquals(EXPECTED_VALUE_4_BYTES_UNSIGNED, actualValue);
    }

    @Test
    void testFromLittleEndian_ByteArrayWithOffset_HandlesUnsignedInt32() {
        long actualValue = fromLittleEndian(TEST_BYTES_5, 1, 4);
        assertEquals(EXPECTED_VALUE_4_BYTES_UNSIGNED, actualValue);
    }

    @Test
    void testFromLittleEndian_EntireByteArray_ThrowsWhenTooLong() {
        byte[] tooLongArray = new byte[INVALID_LENGTH];
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> fromLittleEndian(tooLongArray));
        // Verify the exception is thrown for the expected reason
    }

    @Test
    void testFromLittleEndian_ByteArrayWithOffset_ThrowsWhenLengthTooLong() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> fromLittleEndian(ByteUtils.EMPTY_BYTE_ARRAY, 0, INVALID_LENGTH));
    }

    // ========== fromLittleEndian tests - DataInput sources ==========

    @Test
    void testFromLittleEndian_DataInput_ReturnsCorrectValue() throws IOException {
        DataInput dataInput = createDataInput(TEST_BYTES_3);
        
        long actualValue = fromLittleEndian(dataInput, 3);
        assertEquals(EXPECTED_VALUE_3_BYTES, actualValue);
    }

    @Test
    void testFromLittleEndian_DataInput_HandlesUnsignedInt32() throws IOException {
        DataInput dataInput = createDataInput(TEST_BYTES_4_WITH_SIGN);
        
        long actualValue = fromLittleEndian(dataInput, 4);
        assertEquals(EXPECTED_VALUE_4_BYTES_UNSIGNED, actualValue);
    }

    @Test
    void testFromLittleEndian_DataInput_ThrowsWhenLengthTooLong() {
        DataInput emptyDataInput = createDataInput(new byte[0]);
        
        assertThrows(IllegalArgumentException.class, 
            () -> fromLittleEndian(emptyDataInput, INVALID_LENGTH));
    }

    @Test
    void testFromLittleEndian_DataInput_ThrowsEOFWhenInsufficientData() {
        // Create input with only 2 bytes but try to read 3
        DataInput insufficientDataInput = createDataInput(new byte[]{2, 3});
        
        assertThrows(EOFException.class, 
            () -> fromLittleEndian(insufficientDataInput, 3));
    }

    // ========== fromLittleEndian tests - InputStream sources ==========

    @Test
    void testFromLittleEndian_InputStream_ReturnsCorrectValue() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_BYTES_3);
        
        long actualValue = fromLittleEndian(inputStream, 3);
        assertEquals(EXPECTED_VALUE_3_BYTES, actualValue);
    }

    @Test
    void testFromLittleEndian_InputStream_HandlesUnsignedInt32() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_BYTES_4_WITH_SIGN);
        
        long actualValue = fromLittleEndian(inputStream, 4);
        assertEquals(EXPECTED_VALUE_4_BYTES_UNSIGNED, actualValue);
    }

    @Test
    void testFromLittleEndian_InputStream_ThrowsWhenLengthTooLong() {
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        
        assertThrows(IllegalArgumentException.class, 
            () -> fromLittleEndian(emptyStream, INVALID_LENGTH));
    }

    @Test
    void testFromLittleEndian_InputStream_ThrowsIOExceptionWhenInsufficientData() {
        ByteArrayInputStream insufficientStream = new ByteArrayInputStream(new byte[]{2, 3});
        
        assertThrows(IOException.class, 
            () -> fromLittleEndian(insufficientStream, 3));
    }

    // ========== fromLittleEndian tests - ByteSupplier sources ==========

    @Test
    void testFromLittleEndian_ByteSupplier_ReturnsCorrectValue() throws IOException {
        InputStreamByteSupplier supplier = createByteSupplier(TEST_BYTES_3);
        
        long actualValue = fromLittleEndian(supplier, 3);
        assertEquals(EXPECTED_VALUE_3_BYTES, actualValue);
    }

    @Test
    void testFromLittleEndian_ByteSupplier_HandlesUnsignedInt32() throws IOException {
        InputStreamByteSupplier supplier = createByteSupplier(TEST_BYTES_4_WITH_SIGN);
        
        long actualValue = fromLittleEndian(supplier, 4);
        assertEquals(EXPECTED_VALUE_4_BYTES_UNSIGNED, actualValue);
    }

    @Test
    void testFromLittleEndian_ByteSupplier_ThrowsWhenLengthTooLong() {
        InputStreamByteSupplier emptySupplier = createByteSupplier(new byte[0]);
        
        assertThrows(IllegalArgumentException.class,
            () -> fromLittleEndian(emptySupplier, INVALID_LENGTH));
    }

    @Test
    void testFromLittleEndian_ByteSupplier_ThrowsIOExceptionWhenInsufficientData() {
        InputStreamByteSupplier insufficientSupplier = createByteSupplier(new byte[]{2, 3});
        
        assertThrows(IOException.class, 
            () -> fromLittleEndian(insufficientSupplier, 3));
    }

    // ========== toLittleEndian tests - byte array destinations ==========

    @Test
    void testToLittleEndian_ByteArray_WritesCorrectBytes() {
        byte[] targetArray = new byte[4];
        
        toLittleEndian(targetArray, EXPECTED_VALUE_3_BYTES, 1, 3);
        
        byte[] actualBytes = Arrays.copyOfRange(targetArray, 1, 4);
        assertArrayEquals(TEST_BYTES_3, actualBytes);
    }

    @Test
    void testToLittleEndian_ByteArray_HandlesUnsignedInt32() {
        byte[] targetArray = new byte[4];
        
        toLittleEndian(targetArray, EXPECTED_VALUE_4_BYTES_UNSIGNED, 0, 4);
        
        assertArrayEquals(TEST_BYTES_4_WITH_SIGN, targetArray);
    }

    // ========== toLittleEndian tests - ByteConsumer destinations ==========

    @Test
    void testToLittleEndian_ByteConsumer_WritesCorrectBytes() throws IOException {
        byte[] resultBytes = writeToByteConsumer(EXPECTED_VALUE_3_BYTES, 3);
        
        assertArrayEquals(TEST_BYTES_3, resultBytes);
    }

    @Test
    void testToLittleEndian_ByteConsumer_HandlesUnsignedInt32() throws IOException {
        byte[] resultBytes = writeToByteConsumer(EXPECTED_VALUE_4_BYTES_UNSIGNED, 4);
        
        assertArrayEquals(TEST_BYTES_4_WITH_SIGN, resultBytes);
    }

    // ========== toLittleEndian tests - DataOutput destinations ==========

    @Test
    void testToLittleEndian_DataOutput_WritesCorrectBytes() throws IOException {
        byte[] resultBytes = writeToDataOutput(EXPECTED_VALUE_3_BYTES, 3);
        
        assertArrayEquals(TEST_BYTES_3, resultBytes);
    }

    @Test
    void testToLittleEndian_DataOutput_HandlesUnsignedInt32() throws IOException {
        byte[] resultBytes = writeToDataOutput(EXPECTED_VALUE_4_BYTES_UNSIGNED, 4);
        
        assertArrayEquals(TEST_BYTES_4_WITH_SIGN, resultBytes);
    }

    // ========== toLittleEndian tests - OutputStream destinations ==========

    @Test
    void testToLittleEndian_OutputStream_WritesCorrectBytes() throws IOException {
        byte[] resultBytes = writeToOutputStream(EXPECTED_VALUE_3_BYTES, 3);
        
        assertArrayEquals(TEST_BYTES_3, resultBytes);
    }

    @Test
    void testToLittleEndian_OutputStream_HandlesUnsignedInt32() throws IOException {
        byte[] resultBytes = writeToOutputStream(EXPECTED_VALUE_4_BYTES_UNSIGNED, 4);
        
        assertArrayEquals(TEST_BYTES_4_WITH_SIGN, resultBytes);
    }

    // ========== Helper methods for test setup ==========

    private DataInput createDataInput(byte[] data) {
        return new DataInputStream(new ByteArrayInputStream(data));
    }

    private InputStreamByteSupplier createByteSupplier(byte[] data) {
        return new InputStreamByteSupplier(new ByteArrayInputStream(data));
    }

    private byte[] writeToByteConsumer(long value, int length) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            OutputStreamByteConsumer consumer = new OutputStreamByteConsumer(outputStream);
            toLittleEndian(consumer, value, length);
            return outputStream.toByteArray();
        }
    }

    private byte[] writeToDataOutput(long value, int length) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DataOutput dataOutput = new DataOutputStream(outputStream);
            toLittleEndian(dataOutput, value, length);
            return outputStream.toByteArray();
        }
    }

    private byte[] writeToOutputStream(long value, int length) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            toLittleEndian(outputStream, value, length);
            return outputStream.toByteArray();
        }
    }
}