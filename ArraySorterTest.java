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
 * This class contains test cases that verify the functionality of the {@link ArraySorter} class,
 * specifically its ability to sort arrays of various primitive types and objects.
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    void testSortByteArray_sortsCorrectly() {
        // Arrange: Create an unsorted byte array and a copy for comparison
        final byte[] unsortedArray = {2, 1};
        final byte[] arrayToSort = unsortedArray.clone();

        // Act: Sort the original array using Arrays.sort for expected result, and sort the copy using ArraySorter.sort
        Arrays.sort(unsortedArray);
        final byte[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert: Verify that the ArraySorter.sort method returns the correctly sorted array.
        assertArrayEquals(unsortedArray, sortedArray, "The byte array should be sorted in ascending order.");
    }

    @Test
    void testSortByteArray_handlesNullArray() {
        // Arrange: No arrangement needed, testing null input.

        // Act: Call ArraySorter.sort with a null byte array.
        final byte[] sortedArray = ArraySorter.sort((byte[]) null);

        // Assert: Verify that ArraySorter.sort returns null when given a null input.
        assertNull(sortedArray, "The result of sorting a null byte array should be null.");
    }

    @Test
    void testSortCharArray_sortsCorrectly() {
        // Arrange
        final char[] unsortedArray = {2, 1};
        final char[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final char[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The char array should be sorted in ascending order.");
    }

    @Test
    void testSortCharArray_handlesNullArray() {
        // Arrange

        // Act
        final char[] sortedArray = ArraySorter.sort((char[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null char array should be null.");
    }

    @Test
    void testSortComparable_sortsCorrectly() {
        // Arrange
        final String[] unsortedArray = ArrayUtils.toArray("foo", "bar");
        final String[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final String[] sortedArray = ArraySorter.sort(arrayToSort, String::compareTo);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The String array should be sorted in ascending order.");
    }

    @Test
    void testSortComparable_handlesNullArray() {
        // Arrange

        // Act
        final String[] sortedArray = ArraySorter.sort((String[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null String array should be null.");
    }

    @Test
    void testSortDoubleArray_sortsCorrectly() {
        // Arrange
        final double[] unsortedArray = {2, 1};
        final double[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final double[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The double array should be sorted in ascending order.");
    }

    @Test
    void testSortDoubleArray_handlesNullArray() {
        // Arrange

        // Act
        final double[] sortedArray = ArraySorter.sort((double[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null double array should be null.");
    }

    @Test
    void testSortFloatArray_sortsCorrectly() {
        // Arrange
        final float[] unsortedArray = {2, 1};
        final float[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final float[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The float array should be sorted in ascending order.");
    }

    @Test
    void testSortFloatArray_handlesNullArray() {
        // Arrange

        // Act
        final float[] sortedArray = ArraySorter.sort((float[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null float array should be null.");
    }

    @Test
    void testSortIntArray_sortsCorrectly() {
        // Arrange
        final int[] unsortedArray = {2, 1};
        final int[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final int[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The int array should be sorted in ascending order.");
    }

    @Test
    void testSortIntArray_handlesNullArray() {
        // Arrange

        // Act
        final int[] sortedArray = ArraySorter.sort((int[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null int array should be null.");
    }

    @Test
    void testSortLongArray_sortsCorrectly() {
        // Arrange
        final long[] unsortedArray = {2, 1};
        final long[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final long[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The long array should be sorted in ascending order.");
    }

    @Test
    void testSortLongArray_handlesNullArray() {
        // Arrange

        // Act
        final long[] sortedArray = ArraySorter.sort((long[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null long array should be null.");
    }

    @Test
    void testSortObjects_sortsCorrectly() {
        // Arrange
        final String[] unsortedArray = ArrayUtils.toArray("foo", "bar");
        final String[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final String[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The String array should be sorted in ascending order.");
    }

    @Test
    void testSortObjects_handlesNullArray() {
        // Arrange

        // Act
        final String[] sortedArray = ArraySorter.sort((String[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null String array should be null.");
    }

    @Test
    void testSortShortArray_sortsCorrectly() {
        // Arrange
        final short[] unsortedArray = {2, 1};
        final short[] arrayToSort = unsortedArray.clone();

        // Act
        Arrays.sort(unsortedArray);
        final short[] sortedArray = ArraySorter.sort(arrayToSort);

        // Assert
        assertArrayEquals(unsortedArray, sortedArray, "The short array should be sorted in ascending order.");
    }

    @Test
    void testSortShortArray_handlesNullArray() {
        // Arrange

        // Act
        final short[] sortedArray = ArraySorter.sort((short[]) null);

        // Assert
        assertNull(sortedArray, "The result of sorting a null short array should be null.");
    }

}