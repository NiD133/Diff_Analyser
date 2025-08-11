package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

/**
 * Readable unit tests for ArrayFill.
 *
 * Goals:
 * - Use descriptive test names.
 * - Avoid EvoSuite-specific scaffolding and timeouts.
 * - Group scenarios consistently: empty arrays, non-empty arrays, and null handling.
 * - Cover both primitive and object array overloads, including generator behavior and type mismatch.
 */
public class ArrayFillTest {

    // -------------------------
    // boolean[]
    // -------------------------

    @Test
    public void fillBoolean_emptyArray_returnsSameInstance() {
        boolean[] a = new boolean[0];
        boolean[] result = ArrayFill.fill(a, true);
        assertSame(a, result);
        assertTrue(Arrays.equals(new boolean[0], result));
    }

    @Test
    public void fillBoolean_nonEmpty_setsAllElementsAndReturnsSame() {
        boolean[] a = new boolean[4];
        boolean[] result = ArrayFill.fill(a, true);
        assertSame(a, result);
        assertTrue(Arrays.equals(new boolean[] { true, true, true, true }, result));
    }

    @Test
    public void fillBoolean_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((boolean[]) null, true));
    }

    // -------------------------
    // byte[]
    // -------------------------

    @Test
    public void fillByte_emptyArray_returnsSameInstance() {
        byte[] a = new byte[0];
        byte[] result = ArrayFill.fill(a, (byte) 0);
        assertSame(a, result);
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void fillByte_nonEmpty_setsAllElements() {
        byte[] a = new byte[1];
        byte[] result = ArrayFill.fill(a, (byte) 76);
        assertSame(a, result);
        assertArrayEquals(new byte[] { 76 }, result);
    }

    @Test
    public void fillByte_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((byte[]) null, (byte) 87));
    }

    // -------------------------
    // char[]
    // -------------------------

    @Test
    public void fillChar_emptyArray_returnsSameInstance() {
        char[] a = new char[0];
        char[] result = ArrayFill.fill(a, 's');
        assertSame(a, result);
        assertEquals(0, result.length);
    }

    @Test
    public void fillChar_nonEmpty_setsAllElements() {
        char[] a = new char[1];
        char[] result = ArrayFill.fill(a, ']');
        assertSame(a, result);
        assertArrayEquals(new char[] { ']' }, result);
    }

    @Test
    public void fillChar_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((char[]) null, 'B'));
    }

    // -------------------------
    // short[]
    // -------------------------

    @Test
    public void fillShort_emptyArray_returnsSameInstance() {
        short[] a = new short[0];
        short[] result = ArrayFill.fill(a, (short) 2);
        assertSame(a, result);
        assertArrayEquals(new short[0], result);
    }

    @Test
    public void fillShort_nonEmpty_setsAllElements() {
        short[] a = new short[6];
        short[] result = ArrayFill.fill(a, (short) -838);
        assertSame(a, result);
        assertArrayEquals(new short[] { -838, -838, -838, -838, -838, -838 }, result);
    }

    @Test
    public void fillShort_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((short[]) null, (short) -3333));
    }

    // -------------------------
    // int[]
    // -------------------------

    @Test
    public void fillInt_emptyArray_returnsSameInstance() {
        int[] a = new int[0];
        int[] result = ArrayFill.fill(a, 0);
        assertSame(a, result);
        assertArrayEquals(new int[0], result);
    }

    @Test
    public void fillInt_nonEmpty_setsAllElements() {
        int[] a = new int[7];
        int[] result = ArrayFill.fill(a, -1);
        assertSame(a, result);
        assertArrayEquals(new int[] { -1, -1, -1, -1, -1, -1, -1 }, result);
    }

    @Test
    public void fillInt_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((int[]) null, 0));
    }

    // -------------------------
    // long[]
    // -------------------------

    @Test
    public void fillLong_emptyArray_returnsSameInstance() {
        long[] a = new long[0];
        long[] result = ArrayFill.fill(a, 0L);
        assertSame(a, result);
        assertArrayEquals(new long[0], result);
    }

    @Test
    public void fillLong_nonEmpty_setsAllElements() {
        long[] a = new long[8];
        long[] result = ArrayFill.fill(a, -1L);
        assertSame(a, result);
        assertArrayEquals(new long[] { -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L }, result);
    }

    @Test
    public void fillLong_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((long[]) null, 1003L));
    }

    // -------------------------
    // float[]
    // -------------------------

    @Test
    public void fillFloat_emptyArray_returnsSameInstance() {
        float[] a = new float[0];
        float[] result = ArrayFill.fill(a, 1.0F);
        assertSame(a, result);
        assertArrayEquals(new float[0], result, 0.0F);
    }

    @Test
    public void fillFloat_nonEmpty_setsAllElements() {
        float[] a = new float[5];
        float[] result = ArrayFill.fill(a, 95.0F);
        assertSame(a, result);
        assertArrayEquals(new float[] { 95.0F, 95.0F, 95.0F, 95.0F, 95.0F }, result, 0.0F);
    }

    @Test
    public void fillFloat_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((float[]) null, -4645.361F));
    }

    // -------------------------
    // double[]
    // -------------------------

    @Test
    public void fillDouble_emptyArray_returnsSameInstance() {
        double[] a = new double[0];
        double[] result = ArrayFill.fill(a, -259.0);
        assertSame(a, result);
        assertArrayEquals(new double[0], result, 0.0);
    }

    @Test
    public void fillDouble_nonEmpty_setsAllElements() {
        double[] a = new double[8];
        double[] result = ArrayFill.fill(a, 0.0);
        assertSame(a, result);
        assertArrayEquals(new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, result, 0.0);
    }

    @Test
    public void fillDouble_nullArray_returnsNull() {
        assertNull(ArrayFill.fill((double[]) null, -3333.0));
    }

    // -------------------------
    // Object[]: fill with value
    // -------------------------

    @Test
    public void fillObject_emptyArray_returnsSameInstance() {
        Object[] a = new Object[0];
        Object marker = new Object();
        Object[] result = ArrayFill.fill(a, marker);
        assertSame(a, result);
        assertEquals(0, result.length);
    }

    @Test
    public void fillObject_nonEmpty_setsAllElements() {
        Throwable[] a = new Throwable[3];
        Throwable t = new Throwable("marker");
        Throwable[] result = ArrayFill.fill(a, t);
        assertSame(a, result);
        for (Throwable e : result) {
            assertSame(t, e);
        }
    }

    @Test
    public void fillObject_nullArray_returnsNull() {
        Object value = new Object();
        assertNull(ArrayFill.fill((Object[]) null, value));
    }

    @Test
    public void fillObject_typeMismatch_throwsArrayStoreException() {
        // Filling a Throwable[] with a non-Throwable value must fail.
        Throwable[] a = new Throwable[3];
        Object notAThrowable = new Object();

        assertThrows(ArrayStoreException.class, () -> ArrayFill.fill(a, notAThrowable));
    }

    // -------------------------
    // Object[]: fill with generator
    // -------------------------

    @Test
    public void fillWithGenerator_nullArray_returnsNull() throws Throwable {
        FailableIntFunction<Object, Throwable> gen = i -> i;
        assertNull(ArrayFill.fill((Object[]) null, gen));
    }

    @Test
    public void fillWithGenerator_nullGenerator_isNoOpAndReturnsSameInstance() throws Throwable {
        Object[] a = new Object[] { "a", "b", "c" };
        Object[] before = Arrays.copyOf(a, a.length);

        Object[] result = ArrayFill.fill(a, (FailableIntFunction<?, Throwable>) null);

        assertSame(a, result);
        assertArrayEquals(before, result);
    }

    @Test
    public void fillWithGenerator_populatesElementsAndReturnsSameInstance() throws Throwable {
        Integer[] a = new Integer[5];

        Integer[] result = ArrayFill.fill(a, (FailableIntFunction<Integer, Throwable>) i -> i * 10);

        assertSame(a, result);
        assertArrayEquals(new Integer[] { 0, 10, 20, 30, 40 }, result);
    }
}