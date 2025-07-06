package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    // --- Tests for Specific Array Types ---

    private void testFillArray(Class<?> arrayType, Object[] array, Object value) {
        Object[] actual = ArrayFill.fill(array, value);
        assertSame(array, actual);
        for (Object v : actual) {
            assertEquals(value, v);
        }
    }

    @Test
    void testFillBooleanArray() {
        boolean[] array = new boolean[3];
        boolean value = true;
        testFillArray(boolean[].class, array, value);
    }

    @Test
    void testFillByteArray() {
        byte[] array = new byte[3];
        byte value = (byte) 1;
        testFillArray(byte[].class, array, value);
    }

    @Test
    void testFillCharArray() {
        char[] array = new char[3];
        char value = 1;
        testFillArray(char[].class, array, value);
    }

    @Test
    void testFillShortArray() {
        short[] array = new short[3];
        short value = (byte) 1;
        testFillArray(short[].class, array, value);
    }

    @Test
    void testFillIntArray() {
        int[] array = new int[3];
        int value = 1;
        // Special case for int array, as ArrayFill.fill(int[], int) is available
        int[] actual = ArrayFill.fill(array, value);
        assertSame(array, actual);
        for (int v : actual) {
            assertEquals(value, v);
        }
    }

    @Test
    void testFillLongArray() {
        long[] array = new long[3];
        long value = 1;
        testFillArray(long[].class, array, value);
    }

    @Test
    void testFillFloatArray() {
        float[] array = new float[3];
        float value = 1;
        testFillArray(float[].class, array, value);
    }

    @Test
    void testFillDoubleArray() {
        double[] array = new double[3];
        double value = 1;
        testFillArray(double[].class, array, value);
    }

    @Test
    void testFillObjectArray() {
        String[] array = new String[3];
        String value = "A";
        testFillArray(String[].class, array, value);
    }

    // --- Tests for Null Input ---

    @Test
    void testFillArrayNull() {
        Object array = null;
        Object value = 1;
        Object[] actual = ArrayFill.fill(array, value);
        assertSame(array, actual);
    }

    // --- Tests for Function ---

    @Test
    void testFillFunction() throws Exception {
        FailableIntFunction<?, Exception> nullIntFunction = null;
        assertNull(ArrayFill.fill(null, nullIntFunction));

        Integer[] array = new Integer[10];
        Integer[] actual = ArrayFill.fill(array, Integer::valueOf);
        assertSame(array, actual);
        for (int i = 0; i < array.length; i++) {
            assertEquals(i, array[i].intValue());
        }
    }
}