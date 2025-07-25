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
 * Tests for {@link ArraySorter}.
 *
 * <p>This class contains test methods to verify the correct behavior of the {@link ArraySorter} class,
 * which provides utility methods for sorting arrays. Each test method focuses on a specific data type
 * and ensures that the sorting logic produces the expected results.</p>
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    void testSortByteArray() {
        // Given an unsorted byte array
        final byte[] unsortedArray = {2, 1};
        final byte[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final byte[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted byte array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((byte[]) null), "Sorting a null byte array should return null.");
    }

    @Test
    void testSortCharArray() {
        // Given an unsorted char array
        final char[] unsortedArray = {2, 1};
        final char[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final char[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted char array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((char[]) null), "Sorting a null char array should return null.");
    }

    @Test
    void testSortComparable() {
        // Given an unsorted String array (String implements Comparable)
        final String[] unsortedArray = ArrayUtils.toArray("foo", "bar");
        final String[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter with a comparator (String::compareTo)
        final String[] sortedArray = ArraySorter.sort(unsortedArray, String::compareTo);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted String array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((String[]) null), "Sorting a null String array should return null.");
    }

    @Test
    void testSortDoubleArray() {
        // Given an unsorted double array
        final double[] unsortedArray = {2, 1};
        final double[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final double[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted double array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((double[]) null), "Sorting a null double array should return null.");
    }

    @Test
    void testSortFloatArray() {
        // Given an unsorted float array
        final float[] unsortedArray = {2, 1};
        final float[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final float[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted float array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((float[]) null), "Sorting a null float array should return null.");
    }

    @Test
    void testSortIntArray() {
        // Given an unsorted int array
        final int[] unsortedArray = {2, 1};
        final int[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final int[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted int array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((int[]) null), "Sorting a null int array should return null.");
    }

    @Test
    void testSortLongArray() {
        // Given an unsorted long array
        final long[] unsortedArray = {2, 1};
        final long[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final long[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted long array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((long[]) null), "Sorting a null long array should return null.");
    }

    @Test
    void testSortObjects() {
        // Given an unsorted String array
        final String[] unsortedArray = ArrayUtils.toArray("foo", "bar");
        final String[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final String[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted String array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((String[]) null), "Sorting a null String array should return null.");
    }

    @Test
    void testSortShortArray() {
        // Given an unsorted short array
        final short[] unsortedArray = {2, 1};
        final short[] expectedArray = unsortedArray.clone();
        Arrays.sort(expectedArray); // Expected result after sorting

        // When sorting the array using ArraySorter
        final short[] sortedArray = ArraySorter.sort(unsortedArray);

        // Then the sorted array should match the expected sorted array
        assertArrayEquals(expectedArray, sortedArray, "The sorted short array should match the expected sorted array.");

        // Also, verify that sorting a null array returns null
        assertNull(ArraySorter.sort((short[]) null), "Sorting a null short array should return null.");
    }

}