package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArraySorter}.
 */
public class ArraySorterTest {

    @Test
    void testSortPrimitives() {
        // Test cases for primitive types
        Object[][] testCases = {
            {new byte[]{2, 1}, new byte[]{1, 2}},
            {new char[]{2, 1}, new char[]{1, 2}},
            {new double[]{2, 1}, new double[]{1, 2}},
            {new float[]{2, 1}, new float[]{1, 2}},
            {new int[]{2, 1}, new int[]{1, 2}},
            {new long[]{2, 1}, new long[]{1, 2}},
            {new short[]{2, 1}, new short[]{1, 2}}
        };

        for (Object[] testCase : testCases) {
            Comparable[] unsorted = (Comparable[]) testCase[0];
            Comparable[] expected = (Comparable[]) testCase[1];

            Comparable[] cloned = cloneArray(unsorted);
            Arrays.sort(cloned);

            assertArrayEquals(expected, cloned);

            // Test null case for each primitive type
            Object sortedArray = ArraySorter.sort(unsorted);
            assertArrayEquals(cloned, sortedArray);

            // Check null input returns null
            assertNull(ArraySorter.sort(null));
        }
    }

    @Test
    void testSortObjects() {
        // Test case for object array
        String[] array = {"foo", "bar"};
        String[] expected = {"bar", "foo"};

        String[] cloned = array.clone();
        Arrays.sort(cloned);

        assertArrayEquals(expected, cloned);

        // Test ArraySorter with default comparator
        String[] sortedArray = ArraySorter.sort(array);
        assertArrayEquals(cloned, sortedArray);

        // Test ArraySorter with custom comparator
        String[] sortedArrayWithComparator = ArraySorter.sort(array, String::compareTo);
        assertArrayEquals(cloned, sortedArrayWithComparator);

        // Check null input returns null
        assertNull(ArraySorter.sort((String[]) null));
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> T[] cloneArray(T[] array) {
        return (T[]) array.clone();
    }
}