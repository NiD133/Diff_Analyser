package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArraySorterTest {

    @Test
    void testSortShortArray() {
        short[] shortArray = new short[]{1, 3, 2};
        short[] sortedArray = ArraySorter.sort(shortArray);
        Arrays.sort(shortArray); // Use standard library sort for comparison
        assertArrayEquals(shortArray, sortedArray, "Short array should be sorted in ascending order.");
    }

    @Test
    void testSortShortArray_alreadySorted() {
        short[] shortArray = new short[]{1, 2, 3};
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertArrayEquals(new short[]{1, 2, 3}, sortedArray, "Already sorted array should remain unchanged.");
    }


    @Test
    void testSortObjectArrayWithComparator() {
        String[] stringArray = {"banana", "apple", "cherry"};
        Comparator<String> reverseComparator = (s1, s2) -> s2.compareTo(s1); // Reverse alphabetical order
        String[] sortedArray = ArraySorter.sort(stringArray, reverseComparator);
        assertArrayEquals(new String[]{"cherry", "banana", "apple"}, sortedArray, "Object array should be sorted according to the comparator.");
    }

    @Test
    void testSortObjectArrayWithComparator_mockedComparator() {
        Object[] objectArray = new Object[2];
        Comparator<Object> mockComparator = mock(Comparator.class);
        when(mockComparator.compare(objectArray[0], objectArray[1])).thenReturn(0);
        Object[] sortedArray = ArraySorter.sort(objectArray, mockComparator);
        assertArrayEquals(objectArray, sortedArray, "Array should be returned but not sorted.");

    }

    @Test
    void testSortEmptyObjectArrayWithComparator() {
        Object[] objectArray = new Object[0];
        Comparator<Object> comparator = (o1, o2) -> 0; // Dummy comparator
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertArrayEquals(objectArray, sortedArray, "Empty array should remain empty.");
    }


    @Test
    void testSortObjectArrayNaturalOrdering() {
        Integer[] intArray = {3, 1, 2};
        Integer[] sortedArray = ArraySorter.sort(intArray);
        assertArrayEquals(new Integer[]{1, 2, 3}, sortedArray, "Object array of Comparables should be sorted in natural order.");
    }

    @Test
    void testSortEmptyObjectArrayNaturalOrdering() {
        Object[] objectArray = new Object[0];
        Object[] sortedArray = ArraySorter.sort(objectArray);
        assertArrayEquals(objectArray, sortedArray, "Empty array should remain empty.");
    }

    @Test
    void testSortLongArray() {
        long[] longArray = {3L, 1L, 2L};
        long[] sortedArray = ArraySorter.sort(longArray);
        Arrays.sort(longArray); // Use standard library sort for comparison
        assertArrayEquals(longArray, sortedArray, "Long array should be sorted in ascending order.");
    }

    @Test
    void testSortEmptyLongArray() {
        long[] longArray = new long[0];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertArrayEquals(longArray, sortedArray, "Empty array should remain empty.");
    }

    @Test
    void testSortIntArray() {
        int[] intArray = {3, 1, 2};
        int[] sortedArray = ArraySorter.sort(intArray);
        Arrays.sort(intArray); // Use standard library sort for comparison
        assertArrayEquals(intArray, sortedArray, "Int array should be sorted in ascending order.");
    }

    @Test
    void testSortEmptyIntArray() {
        int[] intArray = new int[0];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertArrayEquals(intArray, sortedArray, "Empty array should remain empty.");
    }

    @Test
    void testSortFloatArray() {
        float[] floatArray = {3.0f, 1.0f, 2.0f};
        float[] sortedArray = ArraySorter.sort(floatArray);
        Arrays.sort(floatArray); // Use standard library sort for comparison
        assertArrayEquals(floatArray, sortedArray, "Float array should be sorted in ascending order.");
    }

     @Test
    void testSortFloatArray_alreadySorted() {
        float[] floatArray = {1.0f, 2.0f, 3.0f};
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f}, sortedArray, "Already sorted array should remain unchanged.");
    }


    @Test
    void testSortEmptyFloatArray() {
        float[] floatArray = new float[0];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertArrayEquals(floatArray, sortedArray, 0.001f, "Empty array should remain empty.");
    }

    @Test
    void testSortDoubleArray() {
        double[] doubleArray = {3.0, 1.0, 2.0};
        double[] sortedArray = ArraySorter.sort(doubleArray);
        Arrays.sort(doubleArray); // Use standard library sort for comparison
        assertArrayEquals(doubleArray, sortedArray, "Double array should be sorted in ascending order.");
    }

    @Test
    void testSortEmptyDoubleArray() {
        double[] doubleArray = new double[0];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertArrayEquals(doubleArray, sortedArray, 0.001, "Empty array should remain empty.");
    }

    @Test
    void testSortCharArray() {
        char[] charArray = {'c', 'a', 'b'};
        char[] sortedArray = ArraySorter.sort(charArray);
        Arrays.sort(charArray); // Use standard library sort for comparison
        assertArrayEquals(charArray, sortedArray, "Char array should be sorted in ascending order.");
    }

    @Test
    void testSortEmptyCharArray() {
        char[] charArray = new char[0];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertArrayEquals(charArray, sortedArray, "Empty array should remain empty.");
    }

    @Test
    void testSortByteArray() {
        byte[] byteArray = {3, 1, 2};
        byte[] sortedArray = ArraySorter.sort(byteArray);
        Arrays.sort(byteArray); // Use standard library sort for comparison
        assertArrayEquals(byteArray, sortedArray, "Byte array should be sorted in ascending order.");
    }

    @Test
    void testSortEmptyByteArray() {
        byte[] byteArray = new byte[0];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertArrayEquals(byteArray, sortedArray, "Empty array should remain empty.");
    }

    @Test
    void testSortObjectArray_nullElement_naturalOrdering() {
        Object[] objectArray = new Object[3];
        objectArray[0] = 3;
        objectArray[1] = null;
        objectArray[2] = 1;
        assertThrows(NullPointerException.class, () -> ArraySorter.sort(objectArray), "Sorting Object array with null elements and natural ordering should throw a NullPointerException.");
    }

    @Test
    void testSortObjectArray_differentTypes_naturalOrdering() {
       Object[] objectArray = new Object[2];
       objectArray[0] = 0L;
       objectArray[1] = (short) 0;
       assertThrows(ClassCastException.class, () -> ArraySorter.sort(objectArray), "Sorting Object array with different object types without a comparator should throw a ClassCastException.");
    }

    @Test
    void testSortObjectArray_differentTypes_withComparator() {
        Object[] objectArray = new Object[2];
        objectArray[0] = 0L;
        objectArray[1] = (short) 0;
        Comparator<Object> comparator = (o1, o2) -> {
            if (o1 instanceof Long && o2 instanceof Short) {
                return Long.compare((Long) o1, (long) ((Short) o2));
            } else {
                throw new IllegalArgumentException("Unsupported types for comparison.");
            }
        };
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertArrayEquals(new Object[]{0L, (short) 0}, sortedArray);
    }

    @Test
    void testSortObjectArray_nullComparator() {
        Object[] objectArray = new Object[3];
        objectArray[0] = 3;
        objectArray[1] = 1;
        objectArray[2] = 2;
        Object[] sortedArray = ArraySorter.sort(objectArray, null);
        assertArrayEquals(new Object[]{1, 2, 3}, sortedArray);
    }

    @Test
    void testSortObjectArray_nullElements_withComparator() {
        Object[] objectArray = new Object[3];
        objectArray[0] = 3;
        objectArray[1] = null;
        objectArray[2] = 1;

        Comparator<Object> comparator = (o1, o2) -> {
            if (o1 == null) {
                return (o2 == null) ? 0 : -1;
            } else if (o2 == null) {
                return 1;
            }
            return ((Integer) o1).compareTo((Integer) o2);
        };
        assertThrows(ClassCastException.class, () -> ArraySorter.sort(objectArray, comparator));
    }

     @Test
    void testSortObjectArray_nullComparator_ClassCastException() {
        Object object0 = new Object();
        Object[] objectArray0 = new Object[4];
        objectArray0[1] = object0;
        assertThrows(ClassCastException.class, () -> ArraySorter.sort(objectArray0, (Comparator<? super Object>) null));

    }

    @Test
    void testSortObjectArray_nullArray_withComparator() {
        Comparator<Object> comparator = (o1, o2) -> 0;
        Object[] sortedArray = ArraySorter.sort(null, comparator);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortObjectArray_nullArray() {
        Object[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortShortArray_nullArray() {
        short[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortLongArray_nullArray() {
        long[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortIntArray_nullArray() {
        int[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortFloatArray_nullArray() {
        float[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortDoubleArray_nullArray() {
        double[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortCharArray_nullArray() {
        char[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testSortByteArray_nullArray() {
        byte[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray, "Sorting a null array should return null.");
    }

    @Test
    void testConstructor() {
        ArraySorter arraySorter = new ArraySorter();
        assertNotNull(arraySorter);
    }

}