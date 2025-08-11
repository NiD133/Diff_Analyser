package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Comparator;
import org.apache.commons.lang3.ArraySorter;

/**
 * Test suite for ArraySorter class.
 * Tests sorting functionality for all primitive array types and object arrays.
 */
public class ArraySorterTest {

    // ========== Null Array Handling Tests ==========
    
    @Test
    public void shouldReturnNullWhenSortingNullByteArray() {
        byte[] result = ArraySorter.sort((byte[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullCharArray() {
        char[] result = ArraySorter.sort((char[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullDoubleArray() {
        double[] result = ArraySorter.sort((double[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullFloatArray() {
        float[] result = ArraySorter.sort((float[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullIntArray() {
        int[] result = ArraySorter.sort((int[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullLongArray() {
        long[] result = ArraySorter.sort((long[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullShortArray() {
        short[] result = ArraySorter.sort((short[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullObjectArray() {
        Object[] result = ArraySorter.sort((Object[]) null);
        assertNull(result);
    }

    @Test
    public void shouldReturnNullWhenSortingNullObjectArrayWithComparator() {
        Comparator<Object> comparator = mock(Comparator.class);
        Object[] result = ArraySorter.sort((Object[]) null, comparator);
        assertNull(result);
    }

    // ========== Empty Array Tests ==========

    @Test
    public void shouldReturnSameInstanceForEmptyByteArray() {
        byte[] emptyArray = new byte[0];
        byte[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyCharArray() {
        char[] emptyArray = new char[0];
        char[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
        assertEquals(0, result.length);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyDoubleArray() {
        double[] emptyArray = new double[0];
        double[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyFloatArray() {
        float[] emptyArray = new float[0];
        float[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyIntArray() {
        int[] emptyArray = new int[0];
        int[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyLongArray() {
        long[] emptyArray = new long[0];
        long[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyShortArray() {
        short[] emptyArray = new short[0];
        short[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyObjectArray() {
        Integer[] emptyArray = new Integer[0];
        Integer[] result = ArraySorter.sort(emptyArray);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    @Test
    public void shouldReturnSameInstanceForEmptyObjectArrayWithComparator() {
        Object[] emptyArray = new Object[0];
        Comparator<Object> comparator = mock(Comparator.class);
        Object[] result = ArraySorter.sort(emptyArray, comparator);
        assertSame("Should return the same array instance", emptyArray, result);
    }

    // ========== Single Element Array Tests ==========

    @Test
    public void shouldReturnSameInstanceForSingleElementFloatArray() {
        float[] singleElementArray = new float[1]; // Contains 0.0f
        float[] result = ArraySorter.sort(singleElementArray);
        assertSame("Should return the same array instance", singleElementArray, result);
    }

    // ========== Multi-Element Array Tests ==========

    @Test
    public void shouldSortByteArrayWithZeros() {
        byte[] array = new byte[4]; // All elements are 0
        byte[] result = ArraySorter.sort(array);
        assertArrayEquals("Array of zeros should remain unchanged", 
                         new byte[] {0, 0, 0, 0}, result);
    }

    @Test
    public void shouldSortCharArrayWithZeros() {
        char[] array = new char[4]; // All elements are '\0'
        char[] result = ArraySorter.sort(array);
        assertSame("Should return the same array instance", array, result);
    }

    @Test
    public void shouldSortDoubleArrayWithZeros() {
        double[] array = new double[8]; // All elements are 0.0
        double[] result = ArraySorter.sort(array);
        assertEquals("Array length should be preserved", 8, result.length);
    }

    @Test
    public void shouldSortIntArrayWithZeros() {
        int[] array = new int[7]; // All elements are 0
        int[] result = ArraySorter.sort(array);
        assertArrayEquals("Array of zeros should remain unchanged", 
                         new int[] {0, 0, 0, 0, 0, 0, 0}, result);
    }

    @Test
    public void shouldSortLongArrayWithZeros() {
        long[] array = new long[6]; // All elements are 0L
        long[] result = ArraySorter.sort(array);
        assertSame("Should return the same array instance", array, result);
    }

    @Test
    public void shouldSortShortArrayWithZeros() {
        short[] array = new short[4]; // All elements are 0
        short[] result = ArraySorter.sort(array);
        assertEquals("Array length should be preserved", 4, result.length);
    }

    @Test
    public void shouldSortIntegerArrayWithSameValues() {
        Integer[] array = createIntegerArrayWithSameValues();
        Object[] result = ArraySorter.sort((Object[]) array);
        assertEquals("Array length should be preserved", 5, result.length);
    }

    @Test
    public void shouldSortIntegerArrayWithComparator() {
        Integer[] array = new Integer[5]; // All elements are null
        Comparator<Object> comparator = createMockComparatorReturningZero();
        
        Integer[] result = ArraySorter.sort(array, comparator);
        assertEquals("Array length should be preserved", 5, result.length);
    }

    // ========== Exception Tests ==========

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenSortingIntegerArrayWithNulls() {
        Integer[] arrayWithNulls = new Integer[3]; // All elements are null
        ArraySorter.sort(arrayWithNulls);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowClassCastExceptionWhenSortingNonComparableObjects() {
        Object[] array = new Object[4];
        array[1] = new Object(); // Object doesn't implement Comparable
        ArraySorter.sort(array);
    }

    // ========== Constructor Test ==========

    @Test
    public void shouldAllowInstantiation() {
        // Testing deprecated constructor for completeness
        ArraySorter sorter = new ArraySorter();
        assertNotNull("Constructor should create instance", sorter);
    }

    // ========== Helper Methods ==========

    private Integer[] createIntegerArrayWithSameValues() {
        Integer[] array = new Integer[5];
        Integer value = Integer.valueOf(0);
        array[0] = value;
        array[1] = value;
        array[2] = array[0];
        array[3] = value;
        array[4] = array[1];
        return array;
    }

    private Comparator<Object> createMockComparatorReturningZero() {
        Comparator<Object> comparator = mock(Comparator.class);
        when(comparator.compare(any(), any())).thenReturn(0);
        return comparator;
    }
}