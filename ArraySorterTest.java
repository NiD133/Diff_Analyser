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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    // Helper methods to reduce code duplication
    private void testSortArray(byte[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((byte[]) null));
        } else {
            byte[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(char[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((char[]) null));
        } else {
            char[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(double[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((double[]) null));
        } else {
            double[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(float[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((float[]) null));
        } else {
            float[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(int[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((int[]) null));
        } else {
            int[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(long[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((long[]) null));
        } else {
            long[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private void testSortArray(short[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((short[]) null));
        } else {
            short[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    private <T> void testSortArray(T[] array) {
        if (array == null) {
            assertNull(ArraySorter.sort((T[]) null));
        } else {
            T[] sorted = array.clone();
            Arrays.sort(sorted);
            assertArrayEquals(sorted, ArraySorter.sort(array.clone()));
        }
    }

    // Test methods
    @Test
    void testSortByteArray() {
        testSortArray(new byte[]{2, 1});
        testSortArray((byte[]) null);
    }

    @Test
    void testSortCharArray() {
        testSortArray(new char[]{2, 1});
        testSortArray((char[]) null);
    }

    @Test
    void testSortDoubleArray() {
        testSortArray(new double[]{2, 1});
        testSortArray((double[]) null);
    }

    @Test
    void testSortFloatArray() {
        testSortArray(new float[]{2, 1});
        testSortArray((float[]) null);
    }

    @Test
    void testSortIntArray() {
        testSortArray(new int[]{2, 1});
        testSortArray((int[]) null);
    }

    @Test
    void testSortLongArray() {
        testSortArray(new long[]{2, 1});
        testSortArray((long[]) null);
    }

    @Test
    void testSortShortArray() {
        testSortArray(new short[]{2, 1});
        testSortArray((short[]) null);
    }

    @Test
    void testSortObjects() {
        testSortArray(new String[]{"foo", "bar"});
        testSortArray((String[]) null);
    }

    @Test
    void testSortComparable() {
        final String[] array = {"foo", "bar"};
        final String[] arrayCopy = array.clone();
        Arrays.sort(array);
        assertArrayEquals(array, ArraySorter.sort(arrayCopy, String::compareTo));
        assertNull(ArraySorter.sort((String[]) null, String::compareTo));
    }
}