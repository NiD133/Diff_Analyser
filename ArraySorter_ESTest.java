/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    @Test
    public void testConstructor() {
        // The constructor is deprecated, but we test it for full coverage.
        new ArraySorter();
    }

    // --- byte[] Tests ---

    @Test
    public void testSortByteArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((byte[]) null));
    }

    @Test
    public void testSortByteArray_emptyArray_returnsSameInstance() {
        final byte[] array = new byte[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortByteArray_sortsCorrectly() {
        final byte[] array = {2, 1, 4, 3};
        final byte[] expected = {1, 2, 3, 4};
        final byte[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    // --- char[] Tests ---

    @Test
    public void testSortCharArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((char[]) null));
    }

    @Test
    public void testSortCharArray_emptyArray_returnsSameInstance() {
        final char[] array = new char[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortCharArray_sortsCorrectly() {
        final char[] array = {'b', 'd', 'a', 'c'};
        final char[] expected = {'a', 'b', 'c', 'd'};
        final char[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    // --- double[] Tests ---

    @Test
    public void testSortDoubleArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((double[]) null));
    }

    @Test
    public void testSortDoubleArray_emptyArray_returnsSameInstance() {
        final double[] array = new double[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortDoubleArray_sortsCorrectly() {
        final double[] array = {2.5, 1.5, 4.5, 3.5};
        final double[] expected = {1.5, 2.5, 3.5, 4.5};
        final double[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray, 0.0);
    }

    // --- float[] Tests ---

    @Test
    public void testSortFloatArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((float[]) null));
    }

    @Test
    public void testSortFloatArray_emptyArray_returnsSameInstance() {
        final float[] array = new float[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortFloatArray_sortsCorrectly() {
        final float[] array = {2.5f, 1.5f, 4.5f, 3.5f};
        final float[] expected = {1.5f, 2.5f, 3.5f, 4.5f};
        final float[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray, 0.0f);
    }

    // --- int[] Tests ---

    @Test
    public void testSortIntArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((int[]) null));
    }

    @Test
    public void testSortIntArray_emptyArray_returnsSameInstance() {
        final int[] array = new int[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortIntArray_sortsCorrectly() {
        final int[] array = {2, 1, 4, 3};
        final int[] expected = {1, 2, 3, 4};
        final int[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    // --- long[] Tests ---

    @Test
    public void testSortLongArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((long[]) null));
    }

    @Test
    public void testSortLongArray_emptyArray_returnsSameInstance() {
        final long[] array = new long[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortLongArray_sortsCorrectly() {
        final long[] array = {2L, 1L, 4L, 3L};
        final long[] expected = {1L, 2L, 3L, 4L};
        final long[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    // --- short[] Tests ---

    @Test
    public void testSortShortArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((short[]) null));
    }

    @Test
    public void testSortShortArray_emptyArray_returnsSameInstance() {
        final short[] array = new short[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortShortArray_sortsCorrectly() {
        final short[] array = {2, 1, 4, 3};
        final short[] expected = {1, 2, 3, 4};
        final short[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    // --- Object[] Tests ---

    @Test
    public void testSortObjectArray_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((String[]) null));
    }

    @Test
    public void testSortObjectArray_emptyArray_returnsSameInstance() {
        final String[] array = new String[0];
        assertSame(array, ArraySorter.sort(array));
    }

    @Test
    public void testSortObjectArray_sortsCorrectly() {
        final String[] array = {"banana", "apple", "cherry"};
        final String[] expected = {"apple", "banana", "cherry"};
        final String[] sortedArray = ArraySorter.sort(array);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    @Test(expected = NullPointerException.class)
    public void testSortObjectArray_withNullElement_throwsNullPointerException() {
        final Integer[] array = {1, null, 3};
        ArraySorter.sort(array);
    }

    @Test(expected = ClassCastException.class)
    public void testSortObjectArray_nonComparableElements_throwsClassCastException() {
        // java.lang.Object does not implement Comparable
        final Object[] array = {new Object(), new Object()};
        ArraySorter.sort(array);
    }

    // --- Object[] with Comparator Tests ---

    @Test
    public void testSortObjectArrayWithComparator_nullArray_returnsNull() {
        assertNull(ArraySorter.sort((String[]) null, Comparator.reverseOrder()));
    }

    @Test
    public void testSortObjectArrayWithComparator_emptyArray_returnsSameInstance() {
        final String[] array = new String[0];
        assertSame(array, ArraySorter.sort(array, Comparator.reverseOrder()));
    }

    @Test
    public void testSortObjectArrayWithComparator_sortsInReverseOrder() {
        final String[] array = {"banana", "apple", "cherry"};
        final String[] expected = {"cherry", "banana", "apple"};
        final String[] sortedArray = ArraySorter.sort(array, Comparator.reverseOrder());
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    @Test
    public void testSortObjectArrayWithComparator_nullComparator_usesNaturalOrder() {
        final String[] array = {"banana", "apple", "cherry"};
        final String[] expected = {"apple", "banana", "cherry"};
        final String[] sortedArray = ArraySorter.sort(array, null);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }

    @Test
    public void testSortObjectArrayWithComparator_withNullElements() {
        final Integer[] array = {1, null, 3, 2};
        // A null-friendly comparator that sorts nulls first, then by natural order
        final Comparator<Integer> nullsFirstComparator = Comparator.nullsFirst(Comparator.naturalOrder());
        final Integer[] expected = {null, 1, 2, 3};
        final Integer[] sortedArray = ArraySorter.sort(array, nullsFirstComparator);
        assertSame("The returned array should be the same instance", array, sortedArray);
        assertArrayEquals(expected, sortedArray);
    }
}