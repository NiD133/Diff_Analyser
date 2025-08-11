package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ArraySorterTest extends ArraySorter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSortEmptyShortArray() {
        short[] shortArray = new short[0];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertSame(sortedArray, shortArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyObjectArrayWithComparator() {
        Object[] objectArray = new Object[0];
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertSame(sortedArray, objectArray);
    }

    @Test(timeout = 4000)
    public void testSortIntegerArrayWithDuplicates() {
        Integer[] integerArray = {0, 0, 0, 0, 0};
        Object[] sortedArray = ArraySorter.sort(integerArray);
        assertEquals(5, sortedArray.length);
    }

    @Test(timeout = 4000)
    public void testSortEmptyLongArray() {
        long[] longArray = new long[0];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertSame(sortedArray, longArray);
    }

    @Test(timeout = 4000)
    public void testSortIntArrayWithZeros() {
        int[] intArray = new int[7];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0}, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortSingleElementFloatArray() {
        float[] floatArray = new float[1];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertSame(sortedArray, floatArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyDoubleArray() {
        double[] doubleArray = new double[0];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertSame(sortedArray, doubleArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyCharArray() {
        char[] charArray = new char[0];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertEquals(0, sortedArray.length);
    }

    @Test(timeout = 4000)
    public void testSortByteArrayWithZeros() {
        byte[] byteArray = new byte[4];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullIntegerArrayThrowsNullPointerException() {
        Integer[] integerArray = new Integer[3];
        try {
            ArraySorter.sort(integerArray);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.ComparableTimSort", e);
        }
    }

    @Test(timeout = 4000)
    public void testSortObjectArrayWithNonComparableThrowsClassCastException() {
        Object[] objectArray = new Object[4];
        objectArray[1] = new Object();
        try {
            ArraySorter.sort(objectArray);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.util.ComparableTimSort", e);
        }
    }

    @Test(timeout = 4000)
    public void testSortIntegerArrayWithMockComparator() {
        Integer[] integerArray = new Integer[5];
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0, 0, 0, 0).when(comparator).compare(any(), any());
        Integer[] sortedArray = ArraySorter.sort(integerArray, comparator);
        assertEquals(5, sortedArray.length);
    }

    @Test(timeout = 4000)
    public void testSortNullArrayWithComparatorReturnsNull() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        Integer[] sortedArray = ArraySorter.sort((Integer[]) null, comparator);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyIntegerArray() {
        Integer[] integerArray = new Integer[0];
        Integer[] sortedArray = ArraySorter.sort(integerArray);
        assertSame(integerArray, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullObjectArrayReturnsNull() {
        Object[] sortedArray = ArraySorter.sort((Object[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortShortArray() {
        short[] shortArray = new short[4];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertEquals(4, sortedArray.length);
    }

    @Test(timeout = 4000)
    public void testSortNullShortArrayReturnsNull() {
        short[] sortedArray = ArraySorter.sort((short[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortLongArray() {
        long[] longArray = new long[6];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertSame(longArray, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullLongArrayReturnsNull() {
        long[] sortedArray = ArraySorter.sort((long[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyIntArray() {
        int[] intArray = new int[0];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertSame(intArray, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullIntArrayReturnsNull() {
        int[] sortedArray = ArraySorter.sort((int[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyFloatArray() {
        float[] floatArray = new float[0];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertSame(floatArray, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullFloatArrayReturnsNull() {
        float[] sortedArray = ArraySorter.sort((float[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortDoubleArray() {
        double[] doubleArray = new double[8];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertEquals(8, sortedArray.length);
    }

    @Test(timeout = 4000)
    public void testSortNullDoubleArrayReturnsNull() {
        double[] sortedArray = ArraySorter.sort((double[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortCharArray() {
        char[] charArray = new char[4];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertSame(sortedArray, charArray);
    }

    @Test(timeout = 4000)
    public void testSortNullCharArrayReturnsNull() {
        char[] sortedArray = ArraySorter.sort((char[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortEmptyByteArray() {
        byte[] byteArray = new byte[0];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertSame(byteArray, sortedArray);
    }

    @Test(timeout = 4000)
    public void testSortNullByteArrayReturnsNull() {
        byte[] sortedArray = ArraySorter.sort((byte[]) null);
        assertNull(sortedArray);
    }

    @Test(timeout = 4000)
    public void testArraySorterConstructor() {
        new ArraySorter();
    }
}