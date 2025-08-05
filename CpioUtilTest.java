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

    // Constants for Old Binary Magic values
    private static final long MAGIC_OLD_BINARY = CpioConstants.MAGIC_OLD_BINARY;
    private static final byte[] OLD_BIN_MAGIC_BYTES_NOT_SWAPPED = new byte[] { (byte) 0xc7, 0x71 };
    private static final byte[] OLD_BIN_MAGIC_BYTES_SWAPPED = new byte[] { 0x71, (byte) 0xc7 };

    // Tests for byteArray2long
    @Test
    void byteArray2long_WithArrayLengthNotMultipleOfTwo_ThrowsException() {
        // byteArray2long requires array length to be multiple of 2
        byte[] invalidArray = new byte[1]; // Length 1 is invalid
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.byteArray2long(invalidArray, true));
    }

    @Test
    void byteArray2long_WithOldBinMagicNotSwapped_ReturnsCorrectValue() {
        long converted = CpioUtil.byteArray2long(OLD_BIN_MAGIC_BYTES_NOT_SWAPPED, false);
        assertEquals(MAGIC_OLD_BINARY, converted);
    }

    @Test
    void byteArray2long_WithOldBinMagicSwapped_ReturnsCorrectValue() {
        long converted = CpioUtil.byteArray2long(OLD_BIN_MAGIC_BYTES_SWAPPED, true);
        assertEquals(MAGIC_OLD_BINARY, converted);
    }

    // Tests for long2byteArray
    @Test
    void long2byteArray_WithZeroLength_ThrowsException() {
        // Length must be a positive multiple of two (zero is invalid)
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.long2byteArray(0L, 0, false));
    }

    @Test
    void long2byteArray_WithNonMultipleOfTwoLength_ThrowsException() {
        // Length 1021 is not a multiple of two
        assertThrows(UnsupportedOperationException.class, 
            () -> CpioUtil.long2byteArray(0L, 1021, false));
    }

    @Test
    void long2byteArray_WithOldBinMagicNotSwapped_ReturnsExpectedBytes() {
        byte[] converted = CpioUtil.long2byteArray(MAGIC_OLD_BINARY, 2, false);
        assertArrayEquals(OLD_BIN_MAGIC_BYTES_NOT_SWAPPED, converted);
    }

    @Test
    void long2byteArray_WithOldBinMagicSwapped_ReturnsExpectedBytes() {
        byte[] converted = CpioUtil.long2byteArray(MAGIC_OLD_BINARY, 2, true);
        assertArrayEquals(OLD_BIN_MAGIC_BYTES_SWAPPED, converted);
    }
}