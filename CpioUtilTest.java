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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CpioUtil that focus on byte/long conversion with and without half-word swapping.
 *
 * The tests use the well-known OLD BINARY CPIO magic value as a compact, unambiguous fixture.
 * - Not swapped (native order):   0xC7 0x71
 * - Swapped (half-words flipped): 0x71 0xC7
 */
@DisplayName("CpioUtil")
class CpioUtilTest {

    // Test fixture: the OLD BINARY CPIO magic value in different representations.
    private static final long OLD_BIN_MAGIC = CpioConstants.MAGIC_OLD_BINARY;
    private static final byte[] OLD_BIN_MAGIC_BYTES = { (byte) 0xC7, 0x71 };
    private static final byte[] OLD_BIN_MAGIC_BYTES_SWAPPED = { 0x71, (byte) 0xC7 };

    @Nested
    @DisplayName("byteArray2long")
    class ByteArray2LongTests {

        @Test
        @DisplayName("rejects odd-length input (not multiple of 2)")
        void rejectsOddLengthInput() {
            // length 1 (odd) is invalid
            assertThrows(UnsupportedOperationException.class,
                    () -> CpioUtil.byteArray2long(new byte[1], true));
        }

        @Test
        @DisplayName("parses OLD BIN magic when not swapped")
        void parsesOldBinMagic_NotSwapped() {
            // given: bytes in native (not swapped) order
            // when: converting to long without swapping
            // then: value matches the known magic
            assertEquals(OLD_BIN_MAGIC, CpioUtil.byteArray2long(OLD_BIN_MAGIC_BYTES, false));
        }

        @Test
        @DisplayName("parses OLD BIN magic when swapped")
        void parsesOldBinMagic_Swapped() {
            // given: bytes with half-words swapped
            // when: converting to long with swapping enabled
            // then: value matches the known magic
            assertEquals(OLD_BIN_MAGIC, CpioUtil.byteArray2long(OLD_BIN_MAGIC_BYTES_SWAPPED, true));
        }
    }

    @Nested
    @DisplayName("long2byteArray")
    class Long2ByteArrayTests {

        @Test
        @DisplayName("rejects non-positive or odd lengths: length=1021")
        void rejectsInvalidLength_OddLarge() {
            assertThrows(UnsupportedOperationException.class,
                    () -> CpioUtil.long2byteArray(0L, 1021, false));
        }

        @Test
        @DisplayName("rejects non-positive or odd lengths: length=0")
        void rejectsInvalidLength_Zero() {
            assertThrows(UnsupportedOperationException.class,
                    () -> CpioUtil.long2byteArray(0L, 0, false));
        }

        @Test
        @DisplayName("encodes OLD BIN magic when not swapped")
        void encodesOldBinMagic_NotSwapped() {
            // given: the known magic value
            // when: encoded to two bytes without swapping
            // then: bytes are in native order
            assertArrayEquals(OLD_BIN_MAGIC_BYTES, CpioUtil.long2byteArray(OLD_BIN_MAGIC, 2, false));
        }

        @Test
        @DisplayName("encodes OLD BIN magic when swapped")
        void encodesOldBinMagic_Swapped() {
            // given: the known magic value
            // when: encoded to two bytes with swapping enabled
            // then: bytes are swapped
            assertArrayEquals(OLD_BIN_MAGIC_BYTES_SWAPPED, CpioUtil.long2byteArray(OLD_BIN_MAGIC, 2, true));
        }
    }
}