package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.apache.commons.lang3.ArraySorter;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ArraySorter_ESTest extends ArraySorter_ESTest_scaffolding {

    // Test sorting a single-element short array
    @Test(timeout = 4000)
    public void testSortSingleElementShortArray() throws Throwable {
        short[] shortArray = new short[1];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertEquals(1, sortedArray.length);
    }

    // Test sorting an empty object array with a comparator
    @Test(timeout = 4000)
    public void testSortEmptyObjectArrayWithComparator() throws Throwable {
        Object[] objectArray = new Object[0];
        Comparator<Object> comparator = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertSame(sortedArray, objectArray);
    }

    // Test sorting an object array with mixed float values
    @Test(timeout = 4000)
    public void testSortObjectArrayWithFloats() throws Throwable {
        Object[] objectArray = {4951.0F, 0.0F, 0.0F, 0.0F, 4951.0F, 4951.0F, -1750.1F};
        Object[] sortedArray = ArraySorter.sort(objectArray);
        assertSame(sortedArray, objectArray);
    }

    // Test sorting an empty object array
    @Test(timeout = 4000)
    public void testSortEmptyObjectArray() throws Throwable {
        Object[] objectArray = new Object[0];
        Object[] sortedArray = ArraySorter.sort(objectArray);
        assertSame(sortedArray, objectArray);
    }

    // Test sorting an empty long array
    @Test(timeout = 4000)
    public void testSortEmptyLongArray() throws Throwable {
        long[] longArray = new long[0];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertEquals(0, sortedArray.length);
    }

    // Test sorting a single-element int array
    @Test(timeout = 4000)
    public void testSortSingleElementIntArray() throws Throwable {
        int[] intArray = new int[1];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertSame(sortedArray, intArray);
    }

    // Test sorting an empty float array
    @Test(timeout = 4000)
    public void testSortEmptyFloatArray() throws Throwable {
        float[] floatArray = new float[8];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertSame(floatArray, sortedArray);
    }

    // Test sorting an empty double array
    @Test(timeout = 4000)
    public void testSortEmptyDoubleArray() throws Throwable {
        double[] doubleArray = new double[0];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertArrayEquals(new double[]{}, sortedArray, 0.01);
    }

    // Test sorting an empty char array
    @Test(timeout = 4000)
    public void testSortEmptyCharArray() throws Throwable {
        char[] charArray = new char[0];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertArrayEquals(new char[]{}, sortedArray);
    }

    // Test sorting an empty byte array
    @Test(timeout = 4000)
    public void testSortEmptyByteArray() throws Throwable {
        byte[] byteArray = new byte[0];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertSame(sortedArray, byteArray);
    }

    // Test sorting an object array with null comparator
    @Test(timeout = 4000)
    public void testSortObjectArrayWithNullComparator() throws Throwable {
        Object object = new Object();
        Object[] objectArray = new Object[4];
        objectArray[1] = object;
        try {
            ArraySorter.sort(objectArray, null);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.util.ComparableTimSort", e);
        }
    }

    // Test sorting an object array with incompatible types
    @Test(timeout = 4000)
    public void testSortObjectArrayWithIncompatibleTypes() throws Throwable {
        Object[] objectArray = {0L, (short) 0};
        try {
            ArraySorter.sort(objectArray);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("java.lang.Short", e);
        }
    }

    // Test sorting an object array with a mock comparator
    @Test(timeout = 4000)
    public void testSortObjectArrayWithMockComparator() throws Throwable {
        Object[] objectArray = new Object[2];
        Comparator<Object> comparator = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertEquals(2, sortedArray.length);
    }

    // Test sorting a null object array with a comparator
    @Test(timeout = 4000)
    public void testSortNullObjectArrayWithComparator() throws Throwable {
        Comparator<Object> comparator = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] sortedArray = ArraySorter.sort((Object[]) null, comparator);
        assertNull(sortedArray);
    }

    // Test sorting an object array with null elements
    @Test(timeout = 4000)
    public void testSortObjectArrayWithNullElements() throws Throwable {
        Object[] objectArray = new Object[6];
        try {
            ArraySorter.sort(objectArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.ComparableTimSort", e);
        }
    }

    // Test sorting a null object array
    @Test(timeout = 4000)
    public void testSortNullObjectArray() throws Throwable {
        Object[] sortedArray = ArraySorter.sort((Object[]) null);
        assertNull(sortedArray);
    }

    // Test sorting an empty short array
    @Test(timeout = 4000)
    public void testSortEmptyShortArray() throws Throwable {
        short[] shortArray = new short[0];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertSame(sortedArray, shortArray);
    }

    // Test sorting a null short array
    @Test(timeout = 4000)
    public void testSortNullShortArray() throws Throwable {
        short[] sortedArray = ArraySorter.sort((short[]) null);
        assertNull(sortedArray);
    }

    // Test sorting a long array with default values
    @Test(timeout = 4000)
    public void testSortLongArrayWithDefaultValues() throws Throwable {
        long[] longArray = new long[3];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertSame(sortedArray, longArray);
    }

    // Test sorting a null long array
    @Test(timeout = 4000)
    public void testSortNullLongArray() throws Throwable {
        long[] sortedArray = ArraySorter.sort((long[]) null);
        assertNull(sortedArray);
    }

    // Test sorting an empty int array
    @Test(timeout = 4000)
    public void testSortEmptyIntArray() throws Throwable {
        int[] intArray = new int[0];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertSame(intArray, sortedArray);
    }

    // Test sorting a null int array
    @Test(timeout = 4000)
    public void testSortNullIntArray() throws Throwable {
        int[] sortedArray = ArraySorter.sort((int[]) null);
        assertNull(sortedArray);
    }

    // Test sorting an empty float array
    @Test(timeout = 4000)
    public void testSortEmptyFloatArrayWithPrecision() throws Throwable {
        float[] floatArray = new float[0];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertArrayEquals(new float[]{}, sortedArray, 0.01F);
    }

    // Test sorting a null float array
    @Test(timeout = 4000)
    public void testSortNullFloatArray() throws Throwable {
        float[] sortedArray = ArraySorter.sort((float[]) null);
        assertNull(sortedArray);
    }

    // Test sorting a double array with default values
    @Test(timeout = 4000)
    public void testSortDoubleArrayWithDefaultValues() throws Throwable {
        double[] doubleArray = new double[7];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertSame(doubleArray, sortedArray);
    }

    // Test sorting a null double array
    @Test(timeout = 4000)
    public void testSortNullDoubleArray() throws Throwable {
        double[] sortedArray = ArraySorter.sort((double[]) null);
        assertNull(sortedArray);
    }

    // Test sorting a single-element char array
    @Test(timeout = 4000)
    public void testSortSingleElementCharArray() throws Throwable {
        char[] charArray = new char[1];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertSame(charArray, sortedArray);
    }

    // Test sorting a null char array
    @Test(timeout = 4000)
    public void testSortNullCharArray() throws Throwable {
        char[] sortedArray = ArraySorter.sort((char[]) null);
        assertNull(sortedArray);
    }

    // Test sorting a single-element byte array
    @Test(timeout = 4000)
    public void testSortSingleElementByteArray() throws Throwable {
        byte[] byteArray = new byte[1];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertArrayEquals(new byte[]{(byte) 0}, sortedArray);
    }

    // Test sorting a null byte array
    @Test(timeout = 4000)
    public void testSortNullByteArray() throws Throwable {
        byte[] sortedArray = ArraySorter.sort((byte[]) null);
        assertNull(sortedArray);
    }

    // Test creating an instance of ArraySorter (deprecated)
    @Test(timeout = 4000)
    public void testCreateArraySorterInstance() throws Throwable {
        ArraySorter arraySorter = new ArraySorter();
    }
}