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
package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite verifies the functionality of the {@link SegmentUtils} utility class.
 * The tests are organized by the method under test to improve clarity and maintainability.
 */
public class SegmentUtilsTest {

    // --- Tests for countArgs() and countInvokeInterfaceArgs() ---

    @Test
    public void countArgs_shouldReturnZero_forMethodWithNoArguments() {
        // A method descriptor with no arguments, like "()V", should result in a count of 0.
        assertEquals(0, SegmentUtils.countArgs("()V"));
    }

    @Test
    public void countInvokeInterfaceArgs_shouldReturnZero_forMethodWithNoArguments() {
        // A method descriptor with no arguments, like "()I", should result in a count of 0.
        assertEquals(0, SegmentUtils.countInvokeInterfaceArgs("()I"));
    }

    @Test
    public void countArgs_shouldCountEachArgumentAsOne() {
        // For countArgs, all argument types, including long (J) and double (D), count as 1.
        // Descriptor: (I, D, Ljava/lang/String;, [I)V -> int, double, String, int[]
        String descriptor = "(IDLjava/lang/String;[I)V";
        assertEquals(4, SegmentUtils.countArgs(descriptor));
    }

    @Test
    public void countInvokeInterfaceArgs_shouldCountLongAndDoubleAsTwo() {
        // For countInvokeInterfaceArgs, long (J) and double (D) count as 2, while others count as 1.
        // This reflects their size on the operand stack.
        // Descriptor: (I, D, J, Ljava/lang/String;, [I)V -> int, double, long, String, int[]
        // Count:      1 + 2 + 2 + 1                + 1   = 7
        String descriptor = "(IDJLjava/lang/String;[I)V";
        assertEquals(7, SegmentUtils.countInvokeInterfaceArgs(descriptor));
    }

    @Test
    public void countArgs_shouldThrowException_forDescriptorWithoutParentheses() {
        try {
            SegmentUtils.countArgs("invalidDescriptor");
            fail("Expected IllegalArgumentException for a descriptor missing parentheses.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            assertEquals("No arguments", e.getMessage());
        }
    }

    @Test
    public void countInvokeInterfaceArgs_shouldThrowException_forDescriptorWithoutParentheses() {
        try {
            SegmentUtils.countInvokeInterfaceArgs("invalidDescriptor");
            fail("Expected IllegalArgumentException for a descriptor missing parentheses.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            assertEquals("No arguments", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void countArgs_shouldThrowNullPointerException_forNullDescriptor() {
        SegmentUtils.countArgs(null);
    }

    @Test(expected = NullPointerException.class)
    public void countInvokeInterfaceArgs_shouldThrowNullPointerException_forNullDescriptor() {
        SegmentUtils.countInvokeInterfaceArgs(null);
    }

    // --- Tests for countBit16() ---

    @Test
    public void countBit16_withIntArray_shouldCountValuesOutsideUnsigned16BitRange() {
        // countBit16 should count integers that are negative or >= 65536 (2^16).
        int[] values = {
            -1,       // Counted (negative)
            0,        // Not counted
            65535,    // Not counted (fits in unsigned 16 bits)
            65536     // Counted (exceeds unsigned 16 bits)
        };
        assertEquals(2, SegmentUtils.countBit16(values));
    }

    @Test
    public void countBit16_withLongArray_shouldCountValuesOutsideUnsigned16BitRange() {
        // countBit16 should count longs that are negative or >= 65536 (2^16).
        long[] values = {
            -1L,      // Counted (negative)
            0L,       // Not counted
            65535L,   // Not counted
            65536L    // Counted
        };
        assertEquals(2, SegmentUtils.countBit16(values));
    }

    @Test
    public void countBit16_with2DLongArray_shouldCountValuesOutsideUnsigned16BitRange() {
        // The logic should apply to all elements in a 2D array.
        long[][] values = {
            { -1L, 0L },         // 1 counted
            { 65535L, 65536L }   // 1 counted
        };
        assertEquals(2, SegmentUtils.countBit16(values));
    }

    @Test
    public void countBit16_shouldReturnZero_forEmptyArrays() {
        assertEquals(0, SegmentUtils.countBit16(new int[0]));
        assertEquals(0, SegmentUtils.countBit16(new long[0]));
        assertEquals(0, SegmentUtils.countBit16(new long[0][0]));
    }

    @Test(expected = NullPointerException.class)
    public void countBit16_shouldThrowNullPointerException_forNullIntArray() {
        SegmentUtils.countBit16((int[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void countBit16_shouldThrowNullPointerException_forNullLongArray() {
        SegmentUtils.countBit16((long[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void countBit16_shouldThrowNullPointerException_forNull2DLongArray() {
        SegmentUtils.countBit16((long[][]) null);
    }

    // --- Tests for countMatches() ---

    @Test
    public void countMatches_shouldReturnCorrectCountOfMatchingItems() {
        // A matcher that returns true for any value greater than 10.
        final IMatcher matcher = value -> value > 10;

        long[] array1D = { 5, 10, 15, 20 };
        assertEquals("Should find 2 matches in the 1D array", 2, SegmentUtils.countMatches(array1D, matcher));

        long[][] array2D = { { 5, 15 }, { 20, 10, 25 } };
        assertEquals("Should find 3 matches in the 2D array", 3, SegmentUtils.countMatches(array2D, matcher));
    }

    @Test
    public void countMatches_shouldReturnZero_forEmptyArrays() {
        final IMatcher matcher = value -> true; // Match everything
        assertEquals(0, SegmentUtils.countMatches(new long[0], matcher));
        assertEquals(0, SegmentUtils.countMatches(new long[0][0], matcher));
    }

    @Test(expected = NullPointerException.class)
    public void countMatches_with1DArray_shouldThrowNullPointerException_forNullMatcher() {
        SegmentUtils.countMatches(new long[1], null);
    }

    @Test(expected = NullPointerException.class)
    public void countMatches_with2DArray_shouldThrowNullPointerException_forNullMatcher() {
        SegmentUtils.countMatches(new long[1][1], null);
    }

    // --- Constructor Test ---

    @Test
    public void constructor_shouldBeCreatable() {
        // This test confirms the class can be instantiated, although it only contains static utility methods.
        // This is useful for maintaining code coverage.
        new SegmentUtils();
    }
}