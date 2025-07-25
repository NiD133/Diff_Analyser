package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the ArraySorter class.
 */
public class ArraySorterTest {

    @Test
    void testSortByteArray() {
        // Test sorting of byte array
        byte[] arrayToSort = {2, 1};
        byte[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        byte[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null byte array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortCharArray() {
        // Test sorting of char array
        char[] arrayToSort = {2, 1};
        char[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        char[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null char array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortDoubleArray() {
        // Test sorting of double array
        double[] arrayToSort = {2, 1};
        double[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        double[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray, 0.001);
        
        // Test sorting of null double array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortFloatArray() {
        // Test sorting of float array
        float[] arrayToSort = {2, 1};
        float[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        float[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray, 0.001f);
        
        // Test sorting of null float array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortIntArray() {
        // Test sorting of int array
        int[] arrayToSort = {2, 1};
        int[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        int[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null int array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortLongArray() {
        // Test sorting of long array
        long[] arrayToSort = {2, 1};
        long[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        long[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null long array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortObjectArrayWithNaturalOrdering() {
        // Test sorting of object array with natural ordering
        String[] arrayToSort = {"foo", "bar"};
        String[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        String[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null object array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortObjectArrayWithComparator() {
        // Test sorting of object array with custom comparator
        String[] arrayToSort = {"foo", "bar"};
        String[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray, String::compareTo);
        String[] sortedArray = ArraySorter.sort(arrayToSort, String::compareTo);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null object array
        assertNull(ArraySorter.sort(null));
    }

    @Test
    void testSortShortArray() {
        // Test sorting of short array
        short[] arrayToSort = {2, 1};
        short[] expectedSortedArray = arrayToSort.clone();
        Arrays.sort(expectedSortedArray);
        short[] sortedArray = ArraySorter.sort(arrayToSort);
        assertArrayEquals(expectedSortedArray, sortedArray);
        
        // Test sorting of null short array
        assertNull(ArraySorter.sort(null));
    }
}