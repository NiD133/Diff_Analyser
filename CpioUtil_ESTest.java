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

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the CpioUtil class.
 */
public class CpioUtilTest {

    // =========================================================================
    // Tests for long2byteArray
    // =========================================================================

    @Test
    public void long2byteArray_shouldConvertPositiveLongToBigEndian() {
        // 0x01020304L = 16909060
        long number = 16909060L;
        byte[] expected = {0x01, 0x02, 0x03, 0x04};
        
        byte[] result = CpioUtil.long2byteArray(number, 4, false);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void long2byteArray_shouldConvertPositiveLongWithHalfWordSwap() {
        // 0x01020304L = 16909060
        long number = 16909060L;
        // Swapped half-words: 0x02010403
        byte[] expected = {0x02, 0x01, 0x04, 0x03};
        
        byte[] result = CpioUtil.long2byteArray(number, 4, true);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void long2byteArray_shouldPadWithLeadingZerosForSmallerNumbers() {
        long number = 255L; // 0xFF
        byte[] expected = {0x00, 0x00, 0x00, (byte) 0xFF};
        
        byte[] result = CpioUtil.long2byteArray(number, 4, false);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void long2byteArray_shouldReturnArrayOfSpecifiedLength() {
        byte[] result = CpioUtil.long2byteArray(12345L, 10, false);
        assertEquals("The resulting byte array should have the specified length.", 10, result.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArray_shouldThrowExceptionForOddLength() {
        CpioUtil.long2byteArray(100L, 3, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void long2byteArray_shouldThrowExceptionForZeroLength() {
        CpioUtil.long2byteArray(100L, 0, false);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void long2byteArray_shouldThrowExceptionForNegativeLength() {
        CpioUtil.long2byteArray(100L, -2, false);
    }

    // =========================================================================
    // Tests for byteArray2long
    // =========================================================================

    @Test
    public void byteArray2long_shouldConvertBigEndianArrayToLong() {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04};
        // Expected long value for 0x01020304 is 16909060
        long expected = 16909060L;
        
        long result = CpioUtil.byteArray2long(bytes, false);
        
        assertEquals(expected, result);
    }

    @Test
    public void byteArray2long_shouldConvertArrayWithHalfWordSwap() {
        byte[] bytes = {0x02, 0x01, 0x04, 0x03};
        // After swap, bytes become {0x01, 0x02, 0x03, 0x04}
        // Expected long value is 16909060
        long expected = 16909060L;
        
        long result = CpioUtil.byteArray2long(bytes, true);
        
        assertEquals(expected, result);
    }

    @Test
    public void byteArray2long_shouldReturnZeroForZeroFilledArray() {
        byte[] bytes = new byte[8];
        long result = CpioUtil.byteArray2long(bytes, false);
        assertEquals(0L, result);
    }

    @Test
    public void byteArray2long_shouldReturnZeroForEmptyArray() {
        byte[] bytes = new byte[0];
        long result = CpioUtil.byteArray2long(bytes, false);
        assertEquals(0L, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void byteArray2long_shouldThrowExceptionForOddLengthArray() {
        CpioUtil.byteArray2long(new byte[3], false);
    }

    @Test(expected = NullPointerException.class)
    public void byteArray2long_shouldThrowExceptionForNullInput() {
        CpioUtil.byteArray2long(null, false);
    }

    // =========================================================================
    // Round-trip conversion tests
    // =========================================================================

    @Test
    public void longAndByteArrayConversion_shouldBeReversible_withoutSwap() {
        long originalValue = 123456789012345L;
        byte[] byteArray = CpioUtil.long2byteArray(originalValue, 8, false);
        long convertedValue = CpioUtil.byteArray2long(byteArray, false);
        assertEquals(originalValue, convertedValue);
    }

    @Test
    public void longAndByteArrayConversion_shouldBeReversible_withSwap() {
        long originalValue = 987654321098765L;
        byte[] byteArray = CpioUtil.long2byteArray(originalValue, 8, true);
        long convertedValue = CpioUtil.byteArray2long(byteArray, true);
        assertEquals(originalValue, convertedValue);
    }
    
    @Test
    public void longAndByteArrayConversion_shouldBeReversible_forNegativeNumbers() {
        long originalValue = -1L;
        byte[] byteArray = CpioUtil.long2byteArray(originalValue, 8, false);
        long convertedValue = CpioUtil.byteArray2long(byteArray, false);
        assertEquals(originalValue, convertedValue);
    }

    // =========================================================================
    // Tests for fileType
    // =========================================================================

    @Test
    public void fileType_shouldReturnZeroForZeroMode() {
        assertEquals(0L, CpioUtil.fileType(0L));
    }

    @Test
    public void fileType_shouldExtractFileTypeBitsFromMode() {
        // S_IFMT is the file type mask: 0170000 (octal) = 61440 (decimal)
        // S_IFREG is a regular file type: 0100000 (octal) = 32768 (decimal)
        // 0755 is rwxr-xr-x permissions in octal
        long regularFileModeWithPermissions = 32768L | 0755;
        
        long expectedFileType = 32768L;
        long actualFileType = CpioUtil.fileType(regularFileModeWithPermissions);
        
        assertEquals(expectedFileType, actualFileType);
    }

    @Test
    public void fileType_shouldReturnMaskWhenModeIsTheMask() {
        // S_IFMT, the file type mask, is 61440L
        long fileTypeMask = 61440L;
        assertEquals(fileTypeMask, CpioUtil.fileType(fileTypeMask));
    }
}