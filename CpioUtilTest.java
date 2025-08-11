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
package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CpioUtilTest {

    // Test data constants for better readability and reusability
    private static final byte[] MAGIC_BYTES_NORMAL_ORDER = {(byte) 0xc7, 0x71};
    private static final byte[] MAGIC_BYTES_SWAPPED_ORDER = {0x71, (byte) 0xc7};
    private static final int VALID_BYTE_ARRAY_LENGTH = 2;
    private static final int INVALID_ODD_LENGTH = 1021;
    private static final int INVALID_ZERO_LENGTH = 0;
    private static final long TEST_VALUE = 0L;

    // Tests for byteArray2long method
    @Test
    void byteArray2long_withOddLengthArray_throwsUnsupportedOperationException() {
        // Given: A byte array with odd length (not multiple of 2)
        byte[] oddLengthArray = new byte[1];
        boolean swapHalfWords = true;

        // When & Then: Should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.byteArray2long(oddLengthArray, swapHalfWords),
            "byteArray2long should reject arrays with odd length");
    }

    @Test
    void byteArray2long_withMagicBytesInNormalOrder_returnsCorrectValue() {
        // Given: Magic bytes in normal order without swapping
        boolean swapHalfWords = false;

        // When: Converting byte array to long
        long result = CpioUtil.byteArray2long(MAGIC_BYTES_NORMAL_ORDER, swapHalfWords);

        // Then: Should return the old binary magic constant
        assertEquals(CpioConstants.MAGIC_OLD_BINARY, result,
            "Should correctly convert normal order magic bytes to MAGIC_OLD_BINARY");
    }

    @Test
    void byteArray2long_withMagicBytesInSwappedOrder_returnsCorrectValue() {
        // Given: Magic bytes in swapped order with swapping enabled
        boolean swapHalfWords = true;

        // When: Converting byte array to long with half-word swapping
        long result = CpioUtil.byteArray2long(MAGIC_BYTES_SWAPPED_ORDER, swapHalfWords);

        // Then: Should return the old binary magic constant
        assertEquals(CpioConstants.MAGIC_OLD_BINARY, result,
            "Should correctly convert swapped order magic bytes to MAGIC_OLD_BINARY when swapping is enabled");
    }

    // Tests for long2byteArray method
    @Test
    void long2byteArray_withOddLength_throwsUnsupportedOperationException() {
        // Given: An odd length (not multiple of 2)
        boolean swapHalfWords = false;

        // When & Then: Should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.long2byteArray(TEST_VALUE, INVALID_ODD_LENGTH, swapHalfWords),
            "long2byteArray should reject odd lengths");
    }

    @Test
    void long2byteArray_withZeroLength_throwsUnsupportedOperationException() {
        // Given: Zero length
        boolean swapHalfWords = false;

        // When & Then: Should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.long2byteArray(TEST_VALUE, INVALID_ZERO_LENGTH, swapHalfWords),
            "long2byteArray should reject zero length");
    }

    @Test
    void long2byteArray_withMagicValueAndNormalOrder_returnsCorrectByteArray() {
        // Given: Old binary magic constant without swapping
        boolean swapHalfWords = false;

        // When: Converting long to byte array
        byte[] result = CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, VALID_BYTE_ARRAY_LENGTH, swapHalfWords);

        // Then: Should return bytes in normal order
        assertArrayEquals(MAGIC_BYTES_NORMAL_ORDER, result,
            "Should convert MAGIC_OLD_BINARY to correct byte array in normal order");
    }

    @Test
    void long2byteArray_withMagicValueAndSwappedOrder_returnsCorrectByteArray() {
        // Given: Old binary magic constant with swapping enabled
        boolean swapHalfWords = true;

        // When: Converting long to byte array with half-word swapping
        byte[] result = CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, VALID_BYTE_ARRAY_LENGTH, swapHalfWords);

        // Then: Should return bytes in swapped order
        assertArrayEquals(MAGIC_BYTES_SWAPPED_ORDER, result,
            "Should convert MAGIC_OLD_BINARY to correct byte array in swapped order");
    }
}