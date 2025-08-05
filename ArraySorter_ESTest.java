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
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
) 
public class ArraySorter_ESTest extends ArraySorter_ESTest_scaffolding {

    // Constructor test
    @Test(timeout = 4000)
    public void testConstructor() {
        ArraySorter arraySorter = new ArraySorter();
        assertNotNull(arraySorter);
    }

    // Tests for sort(byte[])
    @Test(timeout = 4000)
    public void testSortByteArray_NullInput() {
        byte[] result = ArraySorter.sort((byte[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortByteArray_EmptyArray() {
        byte[] input = new byte[0];
        byte[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortByteArray_NonEmptyArray() {
        byte[] input = new byte[]{0, 0, 0, 0};
        byte[] result = ArraySorter.sort(input);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, result);
    }

    // Tests for sort(char[])
    @Test(timeout = 4000)
    public void testSortCharArray_NullInput() {
        char[] result = ArraySorter.sort((char[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortCharArray_EmptyArray() {
        char[] input = new char[0];
        char[] result = ArraySorter.sort(input);
        assertEquals(0, result.length);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortCharArray_NonEmptyArray() {
        char[] input = new char[4];
        char[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    // Tests for sort(double[])
    @Test(timeout = 4000)
    public void testSortDoubleArray_NullInput() {
        double[] result = ArraySorter.sort((double[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortDoubleArray_EmptyArray() {
        double[] input = new double[0];
        double[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortDoubleArray_NonEmptyArray() {
        double[] input = new double[8];
        double[] result = ArraySorter.sort(input);
        assertEquals(8, result.length);
    }

    // Tests for sort(float[])
    @Test(timeout = 4000)
    public void testSortFloatArray_NullInput() {
        float[] result = ArraySorter.sort((float[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortFloatArray_EmptyArray() {
        float[] input = new float[0];
        float[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortFloatArray_SingleElement() {
        float[] input = new float[1];
        float[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    // Tests for sort(int[])
    @Test(timeout = 4000)
    public void testSortIntArray_NullInput() {
        int[] result = ArraySorter.sort((int[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortIntArray_EmptyArray() {
        int[] input = new int[0];
        int[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortIntArray_NonEmptyArray() {
        int[] input = new int[7];
        int[] result = ArraySorter.sort(input);
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0, 0}, result);
    }

    // Tests for sort(long[])
    @Test(timeout = 4000)
    public void testSortLongArray_NullInput() {
        long[] result = ArraySorter.sort((long[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortLongArray_EmptyArray() {
        long[] input = new long[0];
        long[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortLongArray_NonEmptyArray() {
        long[] input = new long[6];
        long[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    // Tests for sort(short[])
    @Test(timeout = 4000)
    public void testSortShortArray_NullInput() {
        short[] result = ArraySorter.sort((short[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortShortArray_EmptyArray() {
        short[] input = new short[0];
        short[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortShortArray_NonEmptyArray() {
        short[] input = new short[4];
        short[] result = ArraySorter.sort(input);
        assertEquals(4, result.length);
    }

    // Tests for sort(Object[])
    @Test(timeout = 4000)
    public void testSortObjectArray_NullInput() {
        Object[] result = ArraySorter.sort((Object[]) null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortObjectArray_EmptyArray() {
        Object[] input = new Object[0];
        Object[] result = ArraySorter.sort(input);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortObjectArray_ValidElements() {
        Integer zero = 0;
        Integer[] input = {zero, zero, zero, zero, zero};
        Object[] result = ArraySorter.sort(input);
        assertEquals(5, result.length);
    }

    @Test(timeout = 4000)
    public void testSortObjectArray_WithNullElement() {
        Integer[] input = new Integer[3];
        try {
            ArraySorter.sort(input);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testSortObjectArray_NonComparableElement() {
        Object[] input = new Object[4];
        input[1] = new Object(); // Non-comparable element
        try {
            ArraySorter.sort(input);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected behavior
        }
    }

    // Tests for sort(Object[], Comparator)
    @Test(timeout = 4000)
    public void testSortObjectArrayWithComparator_NullArray() {
        Comparator<Object> comparator = mock(Comparator.class);
        Object[] result = ArraySorter.sort(null, comparator);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testSortObjectArrayWithComparator_EmptyArray() {
        Comparator<Object> comparator = mock(Comparator.class);
        Object[] input = new Object[0];
        Object[] result = ArraySorter.sort(input, comparator);
        assertSame(input, result);
    }

    @Test(timeout = 4000)
    public void testSortObjectArrayWithComparator_ValidInput() {
        Comparator<Object> comparator = mock(Comparator.class);
        when(comparator.compare(any(), any())).thenReturn(0);
        
        Integer[] input = new Integer[5];
        Integer[] result = ArraySorter.sort(input, comparator);
        assertEquals(5, result.length);
    }
}