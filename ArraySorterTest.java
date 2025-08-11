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

import java.util.Comparator;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    // region: byte[] Tests
    @Test
    void sort_withByteArray_returnsSortedArray() {
        // Arrange
        final byte[] unsortedArray = {3, 1, 2};
        final byte[] expectedArray = {1, 2, 3};

        // Act
        final byte[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullByteArray_returnsNull() {
        // Act
        final byte[] result = ArraySorter.sort((byte[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: char[] Tests
    @Test
    void sort_withCharArray_returnsSortedArray() {
        // Arrange
        final char[] unsortedArray = {3, 1, 2};
        final char[] expectedArray = {1, 2, 3};

        // Act
        final char[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullCharArray_returnsNull() {
        // Act
        final char[] result = ArraySorter.sort((char[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: double[] Tests
    @Test
    void sort_withDoubleArray_returnsSortedArray() {
        // Arrange
        final double[] unsortedArray = {3, 1, 2};
        final double[] expectedArray = {1, 2, 3};

        // Act
        final double[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullDoubleArray_returnsNull() {
        // Act
        final double[] result = ArraySorter.sort((double[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: float[] Tests
    @Test
    void sort_withFloatArray_returnsSortedArray() {
        // Arrange
        final float[] unsortedArray = {3, 1, 2};
        final float[] expectedArray = {1, 2, 3};

        // Act
        final float[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullFloatArray_returnsNull() {
        // Act
        final float[] result = ArraySorter.sort((float[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: int[] Tests
    @Test
    void sort_withIntArray_returnsSortedArray() {
        // Arrange
        final int[] unsortedArray = {3, 1, 2};
        final int[] expectedArray = {1, 2, 3};

        // Act
        final int[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullIntArray_returnsNull() {
        // Act
        final int[] result = ArraySorter.sort((int[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: long[] Tests
    @Test
    void sort_withLongArray_returnsSortedArray() {
        // Arrange
        final long[] unsortedArray = {3, 1, 2};
        final long[] expectedArray = {1, 2, 3};

        // Act
        final long[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullLongArray_returnsNull() {
        // Act
        final long[] result = ArraySorter.sort((long[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: short[] Tests
    @Test
    void sort_withShortArray_returnsSortedArray() {
        // Arrange
        final short[] unsortedArray = {3, 1, 2};
        final short[] expectedArray = {1, 2, 3};

        // Act
        final short[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullShortArray_returnsNull() {
        // Act
        final short[] result = ArraySorter.sort((short[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: Object[] Tests
    @Test
    void sort_withObjectArray_returnsSortedArrayByNaturalOrder() {
        // Arrange
        final String[] unsortedArray = {"foo", "bar", "baz"};
        final String[] expectedArray = {"bar", "baz", "foo"};

        // Act
        final String[] actualArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullObjectArray_returnsNull() {
        // Act
        final String[] result = ArraySorter.sort((String[]) null);

        // Assert
        assertNull(result);
    }
    // endregion

    // region: Object[] with Comparator Tests
    @Test
    void sort_withObjectArrayAndComparator_returnsSortedArray() {
        // Arrange
        final String[] unsortedArray = {"bar", "foo", "baz"};
        final String[] expectedArray = {"foo", "baz", "bar"}; // Expected: reverse alphabetical order

        // Act
        final String[] actualArray = ArraySorter.sort(unsortedArray, Comparator.reverseOrder());

        // Assert
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    void sort_withNullObjectArrayAndComparator_returnsNull() {
        // Act
        final String[] result = ArraySorter.sort(null, Comparator.reverseOrder());

        // Assert
        assertNull(result);
    }
    // endregion
}