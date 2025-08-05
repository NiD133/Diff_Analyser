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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

/**
 * Tests {@link ArraySorter}.
 */
@DisplayName("ArraySorter Tests")
class ArraySorterTest extends AbstractLangTest {

    @Nested
    @DisplayName("Primitive Array Sorting Tests")
    class PrimitiveArrayTests {
        
        @Test
        @DisplayName("should sort byte array in ascending order")
        void testSortByteArray() {
            // Given: an unsorted byte array
            final byte[] unsortedArray = {2, 1};
            final byte[] expectedSortedArray = {1, 2};
            
            // When: sorting the array using ArraySorter
            final byte[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null byte array")
        void testSortNullByteArray() {
            assertNull(ArraySorter.sort((byte[]) null));
        }

        @Test
        @DisplayName("should sort char array in ascending order")
        void testSortCharArray() {
            // Given: an unsorted char array
            final char[] unsortedArray = {'b', 'a'};
            final char[] expectedSortedArray = {'a', 'b'};
            
            // When: sorting the array using ArraySorter
            final char[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null char array")
        void testSortNullCharArray() {
            assertNull(ArraySorter.sort((char[]) null));
        }

        @Test
        @DisplayName("should sort double array in ascending order")
        void testSortDoubleArray() {
            // Given: an unsorted double array
            final double[] unsortedArray = {2.5, 1.3};
            final double[] expectedSortedArray = {1.3, 2.5};
            
            // When: sorting the array using ArraySorter
            final double[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null double array")
        void testSortNullDoubleArray() {
            assertNull(ArraySorter.sort((double[]) null));
        }

        @Test
        @DisplayName("should sort float array in ascending order")
        void testSortFloatArray() {
            // Given: an unsorted float array
            final float[] unsortedArray = {2.5f, 1.3f};
            final float[] expectedSortedArray = {1.3f, 2.5f};
            
            // When: sorting the array using ArraySorter
            final float[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null float array")
        void testSortNullFloatArray() {
            assertNull(ArraySorter.sort((float[]) null));
        }

        @Test
        @DisplayName("should sort int array in ascending order")
        void testSortIntArray() {
            // Given: an unsorted int array
            final int[] unsortedArray = {5, 3, 1, 4, 2};
            final int[] expectedSortedArray = {1, 2, 3, 4, 5};
            
            // When: sorting the array using ArraySorter
            final int[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null int array")
        void testSortNullIntArray() {
            assertNull(ArraySorter.sort((int[]) null));
        }

        @Test
        @DisplayName("should sort long array in ascending order")
        void testSortLongArray() {
            // Given: an unsorted long array
            final long[] unsortedArray = {100L, 50L};
            final long[] expectedSortedArray = {50L, 100L};
            
            // When: sorting the array using ArraySorter
            final long[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null long array")
        void testSortNullLongArray() {
            assertNull(ArraySorter.sort((long[]) null));
        }

        @Test
        @DisplayName("should sort short array in ascending order")
        void testSortShortArray() {
            // Given: an unsorted short array
            final short[] unsortedArray = {20, 10};
            final short[] expectedSortedArray = {10, 20};
            
            // When: sorting the array using ArraySorter
            final short[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted in ascending order
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null short array")
        void testSortNullShortArray() {
            assertNull(ArraySorter.sort((short[]) null));
        }
    }

    @Nested
    @DisplayName("Object Array Sorting Tests")
    class ObjectArrayTests {
        
        @Test
        @DisplayName("should sort String array using natural ordering")
        void testSortStringArrayNaturalOrdering() {
            // Given: an unsorted String array
            final String[] unsortedArray = {"zebra", "apple", "banana"};
            final String[] expectedSortedArray = {"apple", "banana", "zebra"};
            
            // When: sorting the array using ArraySorter
            final String[] actualSortedArray = ArraySorter.sort(unsortedArray.clone());
            
            // Then: the array should be sorted alphabetically
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should return null when sorting null String array")
        void testSortNullStringArray() {
            assertNull(ArraySorter.sort((String[]) null));
        }

        @Test
        @DisplayName("should sort String array using custom comparator")
        void testSortStringArrayWithComparator() {
            // Given: an unsorted String array
            final String[] unsortedArray = {"foo", "bar", "baz"};
            final String[] expectedSortedArray = {"bar", "baz", "foo"};
            
            // When: sorting the array using ArraySorter with a comparator
            final String[] actualSortedArray = ArraySorter.sort(unsortedArray.clone(), String::compareTo);
            
            // Then: the array should be sorted according to the comparator
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
        
        @Test
        @DisplayName("should sort String array by length using custom comparator")
        void testSortStringArrayByLength() {
            // Given: an unsorted String array
            final String[] unsortedArray = {"longer", "short", "a"};
            final String[] expectedSortedArray = {"a", "short", "longer"};
            
            // When: sorting the array by string length
            final String[] actualSortedArray = ArraySorter.sort(
                unsortedArray.clone(), 
                (s1, s2) -> Integer.compare(s1.length(), s2.length())
            );
            
            // Then: the array should be sorted by length
            assertArrayEquals(expectedSortedArray, actualSortedArray);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("should handle empty arrays")
        void testSortEmptyArrays() {
            // Given: empty arrays of different types
            final int[] emptyIntArray = {};
            final String[] emptyStringArray = {};
            
            // When: sorting empty arrays
            final int[] sortedIntArray = ArraySorter.sort(emptyIntArray);
            final String[] sortedStringArray = ArraySorter.sort(emptyStringArray);
            
            // Then: empty arrays should remain empty
            assertArrayEquals(new int[]{}, sortedIntArray);
            assertArrayEquals(new String[]{}, sortedStringArray);
        }
        
        @Test
        @DisplayName("should handle single element arrays")
        void testSortSingleElementArrays() {
            // Given: single element arrays
            final int[] singleIntArray = {42};
            final String[] singleStringArray = {"hello"};
            
            // When: sorting single element arrays
            final int[] sortedIntArray = ArraySorter.sort(singleIntArray.clone());
            final String[] sortedStringArray = ArraySorter.sort(singleStringArray.clone());
            
            // Then: single element arrays should remain unchanged
            assertArrayEquals(new int[]{42}, sortedIntArray);
            assertArrayEquals(new String[]{"hello"}, sortedStringArray);
        }
        
        @Test
        @DisplayName("should handle already sorted arrays")
        void testSortAlreadySortedArrays() {
            // Given: already sorted arrays
            final int[] sortedIntArray = {1, 2, 3, 4, 5};
            final String[] sortedStringArray = {"a", "b", "c", "d"};
            
            // When: sorting already sorted arrays
            final int[] resultIntArray = ArraySorter.sort(sortedIntArray.clone());
            final String[] resultStringArray = ArraySorter.sort(sortedStringArray.clone());
            
            // Then: arrays should remain in the same order
            assertArrayEquals(sortedIntArray, resultIntArray);
            assertArrayEquals(sortedStringArray, resultStringArray);
        }
    }
}