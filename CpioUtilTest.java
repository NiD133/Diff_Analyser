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

    @Test
    void shouldThrowUnsupportedOperationExceptionWhenByteArrayLengthIsInvalid() {
        // Test that an UnsupportedOperationException is thrown when the byte array length is not a multiple of 2
        assertThrows(UnsupportedOperationException.class, () -> 
            CpioUtil.byteArray2long(new byte[1], true)
        );
    }

    @Test
    void shouldThrowUnsupportedOperationExceptionWhenLong2ByteArrayLengthIsNotPositiveMultipleOfTwo() {
        // Test that an UnsupportedOperationException is thrown when the length is not a positive multiple of two
        assertThrows(UnsupportedOperationException.class, () -> 
            CpioUtil.long2byteArray(0L, 1021, false)
        );
        assertThrows(UnsupportedOperationException.class, () -> 
            CpioUtil.long2byteArray(0L, 0, false)
        );
    }

    @Test
    void shouldConvertOldBinMagicToByteArrayWithoutSwapping() {
        // Test conversion of MAGIC_OLD_BINARY to byte array without swapping halfwords
        byte[] expected = { (byte) 0xc7, 0x71 };
        byte[] actual = CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, 2, false);
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldConvertOldBinMagicToByteArrayWithSwapping() {
        // Test conversion of MAGIC_OLD_BINARY to byte array with swapping halfwords
        byte[] expected = { 0x71, (byte) 0xc7 };
        byte[] actual = CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, 2, true);
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldConvertByteArrayToOldBinMagicWithoutSwapping() {
        // Test conversion of byte array to MAGIC_OLD_BINARY without swapping halfwords
        long expected = CpioConstants.MAGIC_OLD_BINARY;
        long actual = CpioUtil.byteArray2long(new byte[] { (byte) 0xc7, 0x71 }, false);
        assertEquals(expected, actual);
    }

    @Test
    void shouldConvertByteArrayToOldBinMagicWithSwapping() {
        // Test conversion of byte array to MAGIC_OLD_BINARY with swapping halfwords
        long expected = CpioConstants.MAGIC_OLD_BINARY;
        long actual = CpioUtil.byteArray2long(new byte[] { 0x71, (byte) 0xc7 }, true);
        assertEquals(expected, actual);
    }
}