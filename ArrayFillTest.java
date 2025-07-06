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
        testFillArray(new boolean[3], true);
    }

    @Test
    void testFillBooleanArrayNull() {
        testFillArray(null, true);
    }

    @Test
    void testFillByteArray() {
        testFillArray(new byte[3], (byte) 1);
    }

    @Test
    void testFillByteArrayNull() {
        testFillArray(null, (byte) 1);
    }

    @Test
    void testFillCharArray() {
        testFillArray(new char[3], (char) 1);
    }

    @Test
    void testFillCharArrayNull() {
        testFillArray(null, (char) 1);
    }

    @Test
    void testFillDoubleArray() {
        testFillArray(new double[3], 1.0);
    }

    @Test
    void testFillDoubleArrayNull() {
        testFillArray(null, 1.0);
    }

    @Test
    void testFillFloatArray() {
        testFillArray(new float[3], 1.0f);
    }

    @Test
    void testFillFloatArrayNull() {
        testFillArray(null, 1.0f);
    }

    @Test
    void testFillIntArray() {
        testFillArray(new int[3], 1);
    }

    @Test
    void testFillIntArrayNull() {
        testFillArray(null, 1);
    }

    @Test
    void testFillLongArray() {
        testFillArray(new long[3], 1L);
    }

    @Test
    void testFillLongArrayNull() {
        testFillArray(null, 1L);
    }

    @Test
    void testFillShortArray() {
        testFillArray(new short[3], (short) 1);
    }

    @Test
    void testFillShortArrayNull() {
        testFillArray(null, (short) 1);
    }

    @Test
    void testFillObjectArray() {
        testFillArray(new String[3], "A");
    }

    @Test
    void testFillObjectArrayNull() {
        testFillArray(null, 1);
    }

    @Test
    void testFillFunction() throws Exception {
        final FailableIntFunction<?, Exception> nullIntFunction = null;
        assertNull(ArrayFill.fill(null, nullIntFunction));
        assertArrayEquals(null, ArrayFill.fill(null, nullIntFunction));
        assertArrayEquals(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, nullIntFunction));
        assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_OBJECT_ARRAY, nullIntFunction));

        final Integer[] array = new Integer[10];
        final Integer[] filledArray = ArrayFill.fill(array, Integer::valueOf);
        assertSame(array, filledArray);
        for (int i = 0; i < array.length; i++) {
            assertEquals(i, array[i].intValue());
        }
    }

    private <T> void testFillArray(T array, T value) {
        T filledArray = ArrayFill.fill(array, value);
        assertSame(array, filledArray);
        if (array != null) {
            for (T element : filledArray) {
                assertEquals(value, element);
            }
        }
    }
}