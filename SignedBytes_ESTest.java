/*
 * Copyright (C) 2009 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Comparator;

/**
 * Readable and maintainable tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    // --- checkedCast tests ---

    @Test
    public void checkedCast_shouldReturnSameValue_whenValueIsInByteRange() {
        assertEquals((byte) 0, SignedBytes.checkedCast(0L));
        assertEquals((byte) 39, SignedBytes.checkedCast(39L));
        assertEquals((byte) -1, SignedBytes.checkedCast(-1L));
        assertEquals(Byte.MAX_VALUE, SignedBytes.checkedCast(Byte.MAX_VALUE));
        assertEquals(Byte.MIN_VALUE, SignedBytes.checkedCast(Byte.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_shouldThrowException_whenValueIsTooLarge() {
        SignedBytes.checkedCast(Byte.MAX_VALUE + 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_shouldThrowException_whenValueIsTooSmall() {
        SignedBytes.checkedCast(Byte.MIN_VALUE - 1L);
    }

    // --- saturatedCast tests ---

    @Test
    public void saturatedCast_shouldReturnSameValue_whenValueIsInByteRange() {
        assertEquals((byte) 0, SignedBytes.saturatedCast(0L));
        assertEquals(Byte.MAX_VALUE, SignedBytes.saturatedCast((long) Byte.MAX_VALUE));
        assertEquals(Byte.MIN_VALUE, SignedBytes.saturatedCast((long) Byte.MIN_VALUE));
    }

    @Test
    public void saturatedCast_shouldReturnMaxByte_whenValueIsTooLarge() {
        assertEquals(Byte.MAX_VALUE, SignedBytes.saturatedCast(Byte.MAX_VALUE + 100L));
    }

    @Test
    public void saturatedCast_shouldReturnMinByte_whenValueIsTooSmall() {
        assertEquals(Byte.MIN_VALUE, SignedBytes.saturatedCast(Byte.MIN_VALUE - 100L));
    }

    // --- min/max tests ---

    @Test
    public void max_shouldReturnLargestValueInArray() {
        byte[] array = {(byte) 1, (byte) 50, (byte) -100, Byte.MAX_VALUE, (byte) 0};
        assertEquals(Byte.MAX_VALUE, SignedBytes.max(array));
    }

    @Test
    public void max_shouldWorkWithNegativeValues() {
        byte[] array = {(byte) -10, (byte) -5, (byte) -128, (byte) -1};
        assertEquals((byte) -1, SignedBytes.max(array));
    }

    @Test(expected = IllegalArgumentException.class)
    public void max_shouldThrowException_whenArrayIsEmpty() {
        SignedBytes.max(new byte[0]);
    }

    @Test
    public void min_shouldReturnSmallestValueInArray() {
        byte[] array = {(byte) 1, (byte) -50, Byte.MIN_VALUE, (byte) 100, (byte) 0};
        assertEquals(Byte.MIN_VALUE, SignedBytes.min(array));
    }

    @Test
    public void min_shouldWorkWithPositiveValues() {
        byte[] array = {(byte) 10, (byte) 5, (byte) 127, (byte) 1};
        assertEquals((byte) 1, SignedBytes.min(array));
    }

    @Test(expected = IllegalArgumentException.class)
    public void min_shouldThrowException_whenArrayIsEmpty() {
        SignedBytes.min(new byte[0]);
    }

    // --- compare tests ---

    @Test
    public void compare_shouldReturnZero_whenValuesAreEqual() {
        assertEquals(0, SignedBytes.compare((byte) 5, (byte) 5));
        assertEquals(0, SignedBytes.compare(Byte.MIN_VALUE, Byte.MIN_VALUE));
    }

    @Test
    public void compare_shouldReturnPositive_whenFirstIsLarger() {
        assertTrue(SignedBytes.compare((byte) 10, (byte) 5) > 0);
        assertTrue(SignedBytes.compare((byte) 0, (byte) -5) > 0);
        assertTrue(SignedBytes.compare(Byte.MAX_VALUE, Byte.MIN_VALUE) > 0);
    }

    @Test
    public void compare_shouldReturnNegative_whenFirstIsSmaller() {
        assertTrue(SignedBytes.compare((byte) 5, (byte) 10) < 0);
        assertTrue(SignedBytes.compare((byte) -5, (byte) 0) < 0);
        assertTrue(SignedBytes.compare(Byte.MIN_VALUE, Byte.MAX_VALUE) < 0);
    }

    // --- join tests ---

    @Test
    public void join_shouldJoinElementsWithSeparator() {
        byte[] array = {(byte) 1, (byte) 2, (byte) -1};
        assertEquals("1:2:-1", SignedBytes.join(":", array));
    }

    @Test
    public void join_shouldReturnEmptyString_whenArrayIsEmpty() {
        assertEquals("", SignedBytes.join(":", new byte[0]));
    }

    @Test
    public void join_shouldWorkWithSingleElementArray() {
        assertEquals("127", SignedBytes.join(":", new byte[]{Byte.MAX_VALUE}));
    }

    @Test(expected = NullPointerException.class)
    public void join_shouldThrowException_whenArrayIsNull() {
        SignedBytes.join(":", (byte[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void join_shouldThrowException_whenSeparatorIsNull() {
        SignedBytes.join(null, new byte[]{1});
    }

    // --- sortDescending tests ---

    @Test
    public void sortDescending_shouldSortArrayInDescendingOrder() {
        byte[] array = {(byte) 1, (byte) 5, (byte) -3, (byte) 0, (byte) 10};
        byte[] expected = {(byte) 10, (byte) 5, (byte) 1, (byte) 0, (byte) -3};
        SignedBytes.sortDescending(array);
        assertArrayEquals(expected, array);
    }

    @Test
    public void sortDescending_shouldHandleEmptyArray() {
        byte[] array = {};
        SignedBytes.sortDescending(array);
        assertArrayEquals(new byte[]{}, array);
    }

    @Test
    public void sortDescending_withRange_shouldSortSubArrayInDescendingOrder() {
        byte[] array = {(byte) 1, (byte) 5, (byte) -3, (byte) 0, (byte) 10};
        // Only sort elements at indices 2 and 3 ([-3, 0])
        byte[] expected = {(byte) 1, (byte) 5, (byte) 0, (byte) -3, (byte) 10};
        SignedBytes.sortDescending(array, 2, 4);
        assertArrayEquals(expected, array);
    }

    @Test(expected = NullPointerException.class)
    public void sortDescending_shouldThrowException_whenArrayIsNull() {
        SignedBytes.sortDescending(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void sortDescending_withRange_shouldThrowException_forInvalidRange() {
        byte[] array = {(byte) 1, (byte) 2, (byte) 3};
        SignedBytes.sortDescending(array, 3, 1); // fromIndex > toIndex
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void sortDescending_withRange_shouldThrowException_forOutOfBoundsFromIndex() {
        byte[] array = {(byte) 1, (byte) 2, (byte) 3};
        SignedBytes.sortDescending(array, -1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void sortDescending_withRange_shouldThrowException_forOutOfBoundsToIndex() {
        byte[] array = {(byte) 1, (byte) 2, (byte) 3};
        SignedBytes.sortDescending(array, 0, 4);
    }

    // --- lexicographicalComparator tests ---

    @Test
    public void lexicographicalComparator_shouldCompareArraysCorrectly() {
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();

        byte[] empty = {};
        byte[] a = {(byte) 1};
        byte[] b = {(byte) 2};
        byte[] ab = {(byte) 1, (byte) 2};
        byte[] ac = {(byte) 1, (byte) -1}; // -1 is smaller than 2

        assertTrue("empty < a", comparator.compare(empty, a) < 0);
        assertTrue("a > empty", comparator.compare(a, empty) > 0);
        assertTrue("a < b", comparator.compare(a, b) < 0);
        assertTrue("b > a", comparator.compare(b, a) > 0);
        assertTrue("a < ab (prefix is smaller)", comparator.compare(a, ab) < 0);
        assertTrue("ab > a", comparator.compare(ab, a) > 0);
        assertTrue("ac < ab", comparator.compare(ac, ab) < 0);
        assertTrue("ab > ac", comparator.compare(ab, ac) > 0);
        assertEquals("a equals a", 0, comparator.compare(a, new byte[]{(byte) 1}));
    }

    @Test
    public void lexicographicalComparator_toString_shouldReturnSensibleString() {
        assertEquals("SignedBytes.lexicographicalComparator()", SignedBytes.lexicographicalComparator().toString());
    }
}