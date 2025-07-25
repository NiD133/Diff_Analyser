package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    @Test
    void testFillBooleanArray() {
        boolean[] array = new boolean[3];
        boolean value = true;
        boolean[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillBooleanArrayNull() {
        boolean[] array = null;
        boolean value = true;
        boolean[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillByteArray() {
        byte[] array = new byte[3];
        byte value = 1;
        byte[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillByteArrayNull() {
        byte[] array = null;
        byte value = 1;
        byte[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillCharArray() {
        char[] array = new char[3];
        char value = 1;
        char[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillCharArrayNull() {
        char[] array = null;
        char value = 1;
        char[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillDoubleArray() {
        double[] array = new double[3];
        double value = 1.0;
        double[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillDoubleArrayNull() {
        double[] array = null;
        double value = 1.0;
        double[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillFloatArray() {
        float[] array = new float[3];
        float value = 1.0f;
        float[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillFloatArrayNull() {
        float[] array = null;
        float value = 1.0f;
        float[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillIntArray() {
        int[] array = new int[3];
        int value = 1;
        int[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillIntArrayNull() {
        int[] array = null;
        int value = 1;
        int[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillLongArray() {
        long[] array = new long[3];
        long value = 1L;
        long[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillLongArrayNull() {
        long[] array = null;
        long value = 1L;
        long[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillShortArray() {
        short[] array = new short[3];
        short value = 1;
        short[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillShortArrayNull() {
        short[] array = null;
        short value = 1;
        short[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillObjectArray() {
        String[] array = new String[3];
        String value = "A";
        String[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
        assertArrayFilledWithValue(result, value);
    }

    @Test
    void testFillObjectArrayNull() {
        Object[] array = null;
        Object value = 1;
        Object[] result = ArrayFill.fill(array, value);

        assertSame(array, result);
    }

    @Test
    void testFillFunction() throws Exception {
        FailableIntFunction<?, Exception> nullFunction = null;
        assertNull(ArrayFill.fill(null, nullFunction));
        assertArrayEquals(null, ArrayFill.fill(null, nullFunction));
        assertArrayEquals(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, nullFunction));
        assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_OBJECT_ARRAY, nullFunction));

        Integer[] array = new Integer[10];
        Integer[] result = ArrayFill.fill(array, Integer::valueOf);

        assertSame(array, result);
        for (int i = 0; i < array.length; i++) {
            assertEquals(i, array[i].intValue());
        }
    }

    private <T> void assertArrayFilledWithValue(T[] array, T value) {
        for (T element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(boolean[] array, boolean value) {
        for (boolean element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(byte[] array, byte value) {
        for (byte element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(char[] array, char value) {
        for (char element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(double[] array, double value) {
        for (double element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(float[] array, float value) {
        for (float element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(int[] array, int value) {
        for (int element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(long[] array, long value) {
        for (long element : array) {
            assertEquals(value, element);
        }
    }

    private void assertArrayFilledWithValue(short[] array, short value) {
        for (short element : array) {
            assertEquals(value, element);
        }
    }
}