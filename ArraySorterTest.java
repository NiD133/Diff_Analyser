package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    /**
     * Tests sorting of a byte array.
     */
    @Test
    void testSortByteArray() {
        final byte[] unsortedArray = {2, 1};
        final byte[] expectedSortedArray = {1, 2};
        final byte[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((byte[]) null), "Sorting a null byte array should return null.");
    }

    /**
     * Tests sorting of a char array.
     */
    @Test
    void testSortCharArray() {
        final char[] unsortedArray = {2, 1};
        final char[] expectedSortedArray = {1, 2};
        final char[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((char[]) null), "Sorting a null char array should return null.");
    }

    /**
     * Tests sorting of a String array using natural ordering.
     */
    @Test
    void testSortComparable() {
        final String[] unsortedArray = {"foo", "bar"};
        final String[] expectedSortedArray = {"bar", "foo"};
        final String[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray, String::compareTo));
        assertNull(ArraySorter.sort((String[]) null), "Sorting a null String array should return null.");
    }

    /**
     * Tests sorting of a double array.
     */
    @Test
    void testSortDoubleArray() {
        final double[] unsortedArray = {2.0, 1.0};
        final double[] expectedSortedArray = {1.0, 2.0};
        final double[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((double[]) null), "Sorting a null double array should return null.");
    }

    /**
     * Tests sorting of a float array.
     */
    @Test
    void testSortFloatArray() {
        final float[] unsortedArray = {2.0f, 1.0f};
        final float[] expectedSortedArray = {1.0f, 2.0f};
        final float[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((float[]) null), "Sorting a null float array should return null.");
    }

    /**
     * Tests sorting of an int array.
     */
    @Test
    void testSortIntArray() {
        final int[] unsortedArray = {2, 1};
        final int[] expectedSortedArray = {1, 2};
        final int[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((int[]) null), "Sorting a null int array should return null.");
    }

    /**
     * Tests sorting of a long array.
     */
    @Test
    void testSortLongArray() {
        final long[] unsortedArray = {2L, 1L};
        final long[] expectedSortedArray = {1L, 2L};
        final long[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((long[]) null), "Sorting a null long array should return null.");
    }

    /**
     * Tests sorting of an Object array using natural ordering.
     */
    @Test
    void testSortObjects() {
        final String[] unsortedArray = {"foo", "bar"};
        final String[] expectedSortedArray = {"bar", "foo"};
        final String[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((String[]) null), "Sorting a null Object array should return null.");
    }

    /**
     * Tests sorting of a short array.
     */
    @Test
    void testSortShortArray() {
        final short[] unsortedArray = {2, 1};
        final short[] expectedSortedArray = {1, 2};
        final short[] clonedArray = unsortedArray.clone();

        Arrays.sort(expectedSortedArray);
        assertArrayEquals(expectedSortedArray, ArraySorter.sort(clonedArray));
        assertNull(ArraySorter.sort((short[]) null), "Sorting a null short array should return null.");
    }
}