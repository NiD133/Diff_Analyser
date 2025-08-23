package org.apache.commons.lang3;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class ArraySorterTest {

    // -------- Primitive arrays: null handling --------

    @Test
    public void sortByteArray_null_returnsNull() {
        assertNull(ArraySorter.sort((byte[]) null));
    }

    @Test
    public void sortShortArray_null_returnsNull() {
        assertNull(ArraySorter.sort((short[]) null));
    }

    @Test
    public void sortCharArray_null_returnsNull() {
        assertNull(ArraySorter.sort((char[]) null));
    }

    @Test
    public void sortIntArray_null_returnsNull() {
        assertNull(ArraySorter.sort((int[]) null));
    }

    @Test
    public void sortLongArray_null_returnsNull() {
        assertNull(ArraySorter.sort((long[]) null));
    }

    @Test
    public void sortFloatArray_null_returnsNull() {
        assertNull(ArraySorter.sort((float[]) null));
    }

    @Test
    public void sortDoubleArray_null_returnsNull() {
        assertNull(ArraySorter.sort((double[]) null));
    }

    // -------- Primitive arrays: empty arrays return same instance --------

    @Test
    public void sortByteArray_empty_returnsSameInstance() {
        byte[] a = new byte[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortShortArray_empty_returnsSameInstance() {
        short[] a = new short[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortCharArray_empty_returnsSameInstance() {
        char[] a = new char[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortIntArray_empty_returnsSameInstance() {
        int[] a = new int[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortLongArray_empty_returnsSameInstance() {
        long[] a = new long[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortFloatArray_empty_returnsSameInstance() {
        float[] a = new float[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortDoubleArray_empty_returnsSameInstance() {
        double[] a = new double[0];
        assertSame(a, ArraySorter.sort(a));
    }

    // -------- Primitive arrays: sorts in place and ascending --------

    @Test
    public void sortByteArray_sortsAscending_inPlace() {
        byte[] a = new byte[] {5, -1, 2};
        byte[] result = ArraySorter.sort(a);
        assertSame(a, result);
        assertArrayEquals(new byte[] {-1, 2, 5}, result);
    }

    @Test
    public void sortIntArray_sortsAscending_inPlace() {
        int[] a = new int[] {3, 1, 2, -7, 0};
        int[] result = ArraySorter.sort(a);
        assertSame(a, result);
        assertArrayEquals(new int[] {-7, 0, 1, 2, 3}, result);
    }

    @Test
    public void sortDoubleArray_sortsAscending_inPlace() {
        double[] a = new double[] {3.5, -1.2, 0.0, 2.2};
        double[] result = ArraySorter.sort(a);
        assertSame(a, result);
        assertArrayEquals(new double[] {-1.2, 0.0, 2.2, 3.5}, result, 0.0);
    }

    @Test
    public void sortCharArray_sortsAscending_inPlace() {
        char[] a = new char[] {'b', 'a', 'c'};
        char[] result = ArraySorter.sort(a);
        assertSame(a, result);
        assertArrayEquals(new char[] {'a', 'b', 'c'}, result);
    }

    // -------- Object arrays without Comparator --------

    @Test
    public void sortObjectArray_null_returnsNull() {
        assertNull(ArraySorter.sort((Object[]) null));
    }

    @Test
    public void sortObjectArray_empty_returnsSameInstance() {
        Integer[] a = new Integer[0];
        assertSame(a, ArraySorter.sort(a));
    }

    @Test
    public void sortObjectArray_ofComparables_sortsAscending_inPlace() {
        Integer[] a = new Integer[] {3, 1, 2};
        Integer[] result = ArraySorter.sort(a);
        assertSame(a, result);
        assertArrayEquals(new Integer[] {1, 2, 3}, result);
    }

    @Test(expected = NullPointerException.class)
    public void sortObjectArray_withNullElement_throwsNullPointerException() {
        Integer[] a = new Integer[] {1, null, 2};
        ArraySorter.sort(a);
    }

    @Test(expected = ClassCastException.class)
    public void sortObjectArray_withNonComparableElement_throwsClassCastException() {
        Object[] a = new Object[] {new Object(), new Object()};
        ArraySorter.sort(a);
    }

    // -------- Object arrays with Comparator --------

    @Test
    public void sortWithComparator_nullArray_returnsNull() {
        Comparator<Integer> cmp = Comparator.nullsLast(Integer::compareTo);
        assertNull(ArraySorter.sort((Integer[]) null, cmp));
    }

    @Test
    public void sortWithComparator_handlesNulls_inPlace() {
        Integer[] a = new Integer[] {null, 3, 1, null, 2};
        Comparator<Integer> cmp = Comparator.nullsLast(Integer::compareTo);
        Integer[] result = ArraySorter.sort(a, cmp);
        assertSame(a, result);
        assertArrayEquals(new Integer[] {1, 2, 3, null, null}, result);
    }

    @Test
    public void sortWithNullComparator_usesNaturalOrdering() {
        Integer[] a = new Integer[] {3, 1, 2};
        Integer[] result = ArraySorter.sort(a, null); // null comparator => natural order
        assertSame(a, result);
        assertArrayEquals(new Integer[] {1, 2, 3}, result);
    }

    // -------- Constructor (deprecated) --------

    @SuppressWarnings("deprecation")
    @Test
    public void deprecatedConstructor_isCallable() {
        // Exists only for compatibility and will be removed in 4.0
        new ArraySorter();
    }
}