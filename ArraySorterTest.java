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

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ArraySorter} class.
 */
class ArraySorterTest extends AbstractLangTest {

    /**
     * Tests sorting of a byte array.
     */
    @Test
    void testSortByteArray() {
        final byte[] unsortedArray = {2, 1};
        final byte[] expectedSortedArray = {1, 2};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((byte[]) null));
    }

    /**
     * Tests sorting of a char array.
     */
    @Test
    void testSortCharArray() {
        final char[] unsortedArray = {2, 1};
        final char[] expectedSortedArray = {1, 2};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((char[]) null));
    }

    /**
     * Tests sorting of a String array using natural ordering.
     */
    @Test
    void testSortComparable() {
        final String[] unsortedArray = {"foo", "bar"};
        final String[] expectedSortedArray = {"bar", "foo"};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone(), String::compareTo));

        // Test sorting of null array
        assertNull(ArraySorter.sort((String[]) null));
    }

    /**
     * Tests sorting of a double array.
     */
    @Test
    void testSortDoubleArray() {
        final double[] unsortedArray = {2.0, 1.0};
        final double[] expectedSortedArray = {1.0, 2.0};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((double[]) null));
    }

    /**
     * Tests sorting of a float array.
     */
    @Test
    void testSortFloatArray() {
        final float[] unsortedArray = {2.0f, 1.0f};
        final float[] expectedSortedArray = {1.0f, 2.0f};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((float[]) null));
    }

    /**
     * Tests sorting of an int array.
     */
    @Test
    void testSortIntArray() {
        final int[] unsortedArray = {2, 1};
        final int[] expectedSortedArray = {1, 2};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((int[]) null));
    }

    /**
     * Tests sorting of a long array.
     */
    @Test
    void testSortLongArray() {
        final long[] unsortedArray = {2L, 1L};
        final long[] expectedSortedArray = {1L, 2L};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((long[]) null));
    }

    /**
     * Tests sorting of an Object array using natural ordering.
     */
    @Test
    void testSortObjects() {
        final String[] unsortedArray = {"foo", "bar"};
        final String[] expectedSortedArray = {"bar", "foo"};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((String[]) null));
    }

    /**
     * Tests sorting of a short array.
     */
    @Test
    void testSortShortArray() {
        final short[] unsortedArray = {2, 1};
        final short[] expectedSortedArray = {1, 2};

        // Test sorting functionality
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(unsortedArray.clone()));

        // Test sorting of null array
        assertNull(ArraySorter.sort((short[]) null));
    }
}