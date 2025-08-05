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
 * Tests for the utility methods in {@link CpioUtil}.
 */
@DisplayName("CpioUtil")
class CpioUtilTest {

    // The CPIO "old binary" magic number (0xc771) as a long.
    private static final long OLD_BINARY_MAGIC_LONG = CpioConstants.MAGIC_OLD_BINARY;

    // Byte representation of the magic number in big-endian format.
    private static final byte[] OLD_BINARY_MAGIC_BIG_ENDIAN = {(byte) 0xc7, 0x71};

    // Byte representation of the magic number in little-endian format (half-words swapped).
    private static final byte[] OLD_BINARY_MAGIC_LITTLE_ENDIAN = {0x71, (byte) 0xc7};

    @Nested
    @DisplayName("long2byteArray()")
    class LongToByteArrayTests {

        @Test
        @DisplayName("should convert a long to a big-endian byte array when not swapping")
        void shouldConvertLongToBigEndianByteArray() {
            // Arrange: 'swapHalfWord = false' corresponds to big-endian byte order.
            final boolean swapHalfWord = false;

            // Act
            final byte[] result = CpioUtil.long2byteArray(OLD_BINARY_MAGIC_LONG, 2, swapHalfWord);

            // Assert
            assertArrayEquals(OLD_BINARY_MAGIC_BIG_ENDIAN, result);
        }

        @Test
        @DisplayName("should convert a long to a little-endian byte array when swapping")
        void shouldConvertLongToLittleEndianByteArray() {
            // Arrange: 'swapHalfWord = true' corresponds to little-endian (swapped) byte order.
            final boolean swapHalfWord = true;

            // Act
            final byte[] result = CpioUtil.long2byteArray(OLD_BINARY_MAGIC_LONG, 2, swapHalfWord);

            // Assert
            assertArrayEquals(OLD_BINARY_MAGIC_LITTLE_ENDIAN, result);
        }

        @Test
        @DisplayName("should throw UnsupportedOperationException for zero length")
        void shouldThrowExceptionForZeroLength() {
            // Per Javadoc, length must be a *positive* multiple of two.
            assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(0L, 0, false));
        }

        @Test
        @DisplayName("should throw UnsupportedOperationException for a non-multiple-of-two length")
        void shouldThrowExceptionForNonMultipleOfTwoLength() {
            // Per Javadoc, length must be a positive *multiple of two*.
            final int invalidLength = 3; // An odd length is not a multiple of two.
            assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(0L, invalidLength, false));
        }
    }

    @Nested
    @DisplayName("byteArray2long()")
    class ByteArrayToLongTests {

        @Test
        @DisplayName("should convert a big-endian byte array to a long when not swapping")
        void shouldConvertBigEndianByteArrayToLong() {
            // Arrange: 'swapHalfWord = false' indicates the input is big-endian.
            final boolean swapHalfWord = false;

            // Act
            final long result = CpioUtil.byteArray2long(OLD_BINARY_MAGIC_BIG_ENDIAN, swapHalfWord);

            // Assert
            assertEquals(OLD_BINARY_MAGIC_LONG, result);
        }

        @Test
        @DisplayName("should convert a little-endian byte array to a long when swapping")
        void shouldConvertLittleEndianByteArrayToLong() {
            // Arrange: 'swapHalfWord = true' indicates the input is little-endian (swapped).
            final boolean swapHalfWord = true;

            // Act
            final long result = CpioUtil.byteArray2long(OLD_BINARY_MAGIC_LITTLE_ENDIAN, swapHalfWord);

            // Assert
            assertEquals(OLD_BINARY_MAGIC_LONG, result);
        }

        @Test
        @DisplayName("should throw UnsupportedOperationException for an array with an odd number of bytes")
        void shouldThrowExceptionForOddLengthArray() {
            // Per Javadoc, the array length must be a multiple of two.
            final byte[] oddLengthArray = {1};
            assertThrows(UnsupportedOperationException.class, () -> CpioUtil.byteArray2long(oddLengthArray, true));
        }
    }
}