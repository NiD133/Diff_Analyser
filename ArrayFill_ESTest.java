package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ArrayFill} class.
 */
class ArrayFillTest {

    @Test
    void testFillBooleanArray() {
        boolean[] emptyBooleanArray = new boolean[0];
        boolean[] filledEmptyBooleanArray = ArrayFill.fill(emptyBooleanArray, false);
        assertArrayEquals(new boolean[]{}, filledEmptyBooleanArray, "Filling an empty boolean array should return an empty array.");

        boolean[] booleanArray = new boolean[3];
        boolean[] filledBooleanArray = ArrayFill.fill(booleanArray, true);
        assertArrayEquals(new boolean[]{true, true, true}, filledBooleanArray, "Boolean array should be filled with the specified value.");

        boolean[] nullBooleanArray = null;
        assertNull(ArrayFill.fill(nullBooleanArray, true), "Filling a null boolean array should return null.");
    }

    @Test
    void testFillByteArray() {
        byte[] emptyByteArray = new byte[0];
        byte[] filledEmptyByteArray = ArrayFill.fill(emptyByteArray, (byte) 1);
        assertArrayEquals(new byte[]{}, filledEmptyByteArray, "Filling an empty byte array should return an empty array.");

        byte[] byteArray = new byte[3];
        byte[] filledByteArray = ArrayFill.fill(byteArray, (byte) 10);
        assertArrayEquals(new byte[]{10, 10, 10}, filledByteArray, "Byte array should be filled with the specified value.");

        byte[] nullByteArray = null;
        assertNull(ArrayFill.fill(nullByteArray, (byte) 10), "Filling a null byte array should return null.");
    }

    @Test
    void testFillCharArray() {
        char[] emptyCharArray = new char[0];
        char[] filledEmptyCharArray = ArrayFill.fill(emptyCharArray, 'a');
        assertArrayEquals(new char[]{}, filledEmptyCharArray, "Filling an empty char array should return an empty array.");

        char[] charArray = new char[3];
        char[] filledCharArray = ArrayFill.fill(charArray, 'Z');
        assertArrayEquals(new char[]{'Z', 'Z', 'Z'}, filledCharArray, "Char array should be filled with the specified value.");

        char[] nullCharArray = null;
        assertNull(ArrayFill.fill(nullCharArray, 'Z'), "Filling a null char array should return null.");
    }

    @Test
    void testFillDoubleArray() {
        double[] emptyDoubleArray = new double[0];
        double[] filledEmptyDoubleArray = ArrayFill.fill(emptyDoubleArray, 1.0);
        assertArrayEquals(new double[]{}, filledEmptyDoubleArray, "Filling an empty double array should return an empty array.");

        double[] doubleArray = new double[3];
        double[] filledDoubleArray = ArrayFill.fill(doubleArray, 3.14);
        assertArrayEquals(new double[]{3.14, 3.14, 3.14}, filledDoubleArray, 0.001, "Double array should be filled with the specified value.");

        double[] nullDoubleArray = null;
        assertNull(ArrayFill.fill(nullDoubleArray, 3.14), "Filling a null double array should return null.");
    }

    @Test
    void testFillFloatArray() {
        float[] emptyFloatArray = new float[0];
        float[] filledEmptyFloatArray = ArrayFill.fill(emptyFloatArray, 1.0f);
        assertArrayEquals(new float[]{}, filledEmptyFloatArray, 0.001f, "Filling an empty float array should return an empty array.");

        float[] floatArray = new float[3];
        float[] filledFloatArray = ArrayFill.fill(floatArray, 2.71f);
        assertArrayEquals(new float[]{2.71f, 2.71f, 2.71f}, filledFloatArray, 0.001f, "Float array should be filled with the specified value.");

        float[] nullFloatArray = null;
        assertNull(ArrayFill.fill(nullFloatArray, 2.71f), "Filling a null float array should return null.");
    }

    @Test
    void testFillIntArray() {
        int[] emptyIntArray = new int[0];
        int[] filledEmptyIntArray = ArrayFill.fill(emptyIntArray, 1);
        assertArrayEquals(new int[]{}, filledEmptyIntArray, "Filling an empty int array should return an empty array.");

        int[] intArray = new int[3];
        int[] filledIntArray = ArrayFill.fill(intArray, 42);
        assertArrayEquals(new int[]{42, 42, 42}, filledIntArray, "Int array should be filled with the specified value.");

        int[] nullIntArray = null;
        assertNull(ArrayFill.fill(nullIntArray, 42), "Filling a null int array should return null.");
    }

    @Test
    void testFillLongArray() {
        long[] emptyLongArray = new long[0];
        long[] filledEmptyLongArray = ArrayFill.fill(emptyLongArray, 1L);
        assertArrayEquals(new long[]{}, filledEmptyLongArray, "Filling an empty long array should return an empty array.");

        long[] longArray = new long[3];
        long[] filledLongArray = ArrayFill.fill(longArray, 12345L);
        assertArrayEquals(new long[]{12345L, 12345L, 12345L}, filledLongArray, "Long array should be filled with the specified value.");

        long[] nullLongArray = null;
        assertNull(ArrayFill.fill(nullLongArray, 12345L), "Filling a null long array should return null.");
    }

    @Test
    void testFillShortArray() {
        short[] emptyShortArray = new short[0];
        short[] filledEmptyShortArray = ArrayFill.fill(emptyShortArray, (short) 1);
        assertArrayEquals(new short[]{}, filledEmptyShortArray, "Filling an empty short array should return an empty array.");

        short[] shortArray = new short[3];
        short[] filledShortArray = ArrayFill.fill(shortArray, (short) 100);
        assertArrayEquals(new short[]{100, 100, 100}, filledShortArray, "Short array should be filled with the specified value.");

        short[] nullShortArray = null;
        assertNull(ArrayFill.fill(nullShortArray, (short) 100), "Filling a null short array should return null.");
    }

    @Test
    void testFillObjectArrayWithValue() {
        Object[] objectArray = new Object[3];
        String fillValue = "test";
        Object[] filledObjectArray = ArrayFill.fill(objectArray, fillValue);

        assertSame(objectArray, filledObjectArray, "Should return the same array instance.");
        for (Object element : filledObjectArray) {
            assertEquals(fillValue, element, "Object array should be filled with the specified value.");
        }

        Object[] nullObjectArray = null;
        assertNull(ArrayFill.fill(nullObjectArray, fillValue), "Filling a null object array should return null.");

        Object[] emptyObjectArray = new Object[0];
        Object[] filledEmptyObjectArray = ArrayFill.fill(emptyObjectArray, fillValue);
        assertArrayEquals(new Object[]{}, filledEmptyObjectArray, "Filling an empty object array should return an empty array.");
    }

    @Test
    void testFillObjectArrayWithFunction() {
        Object[] objectArray = new Object[3];
        FailableIntFunction<String, Throwable> function = i -> "value" + i;
        Object[] filledObjectArray = ArrayFill.fill(objectArray, function);

        assertSame(objectArray, filledObjectArray, "Should return the same array instance.");
        assertEquals("value0", filledObjectArray[0], "First element should be 'value0'");
        assertEquals("value1", filledObjectArray[1], "Second element should be 'value1'");
        assertEquals("value2", filledObjectArray[2], "Third element should be 'value2'");

        Object[] nullObjectArray = null;
        assertNull(ArrayFill.fill(nullObjectArray, function), "Filling a null object array should return null.");

        Object[] emptyObjectArray = new Object[0];
        Object[] filledEmptyObjectArray = ArrayFill.fill(emptyObjectArray, function);
        assertArrayEquals(new Object[]{}, filledEmptyObjectArray, "Filling an empty object array should return an empty array.");
    }

    @Test
    void testFillObjectArrayWithNullFunction() {
        Object[] objectArray = new Object[3];
        Object[] filledObjectArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) null);

        assertSame(objectArray, filledObjectArray, "Should return the same array instance.");
        assertNull(filledObjectArray[0], "First element should be null");
        assertNull(filledObjectArray[1], "Second element should be null");
        assertNull(filledObjectArray[2], "Third element should be null");
    }
}