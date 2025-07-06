package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayFill;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ArrayFill_ESTest extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFillEmptyBooleanArray() {
        boolean[] emptyArray = new boolean[0];
        boolean[] resultArray = ArrayFill.fill(emptyArray, false);
        assertEquals("The length of the filled array should be 0", 0, resultArray.length);
    }

    @Test(timeout = 4000)
    public void testFillEmptyShortArray() {
        short[] emptyArray = new short[0];
        short[] resultArray = ArrayFill.fill(emptyArray, (short) 1);
        assertArrayEquals("The filled array should be empty", new short[]{}, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillObjectArrayWithSameObject() {
        Object[] objectArray = new Object[6];
        Object object = new Object();
        Object[] resultArray = ArrayFill.fill(objectArray, object);
        assertSame("The filled array should be the same as the original array", objectArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillLongArrayWithZero() {
        long[] longArray = new long[7];
        long[] resultArray = ArrayFill.fill(longArray, 0L);
        assertSame("The filled array should be the same as the original array", longArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillIntArrayWithZero() {
        int[] intArray = new int[5];
        int[] resultArray = ArrayFill.fill(intArray, 0);
        assertArrayEquals("The filled array should contain all zeros", new int[]{0, 0, 0, 0, 0}, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyFloatArray() {
        float[] emptyArray = new float[0];
        float[] resultArray = ArrayFill.fill(emptyArray, 1311.7F);
        assertArrayEquals("The filled array should be empty", new float[]{}, resultArray, 0.01F);
    }

    @Test(timeout = 4000)
    public void testFillDoubleArrayWithSpecificValue() {
        double[] doubleArray = new double[7];
        double[] resultArray = ArrayFill.fill(doubleArray, 176.99);
        assertArrayEquals("The filled array should contain the specified value", new double[]{176.99, 176.99, 176.99, 176.99, 176.99, 176.99, 176.99}, resultArray, 0.01);
    }

    @Test(timeout = 4000)
    public void testFillEmptyCharArray() {
        char[] emptyArray = new char[0];
        char[] resultArray = ArrayFill.fill(emptyArray, '>');
        assertEquals("The length of the filled array should be 0", 0, resultArray.length);
    }

    @Test(timeout = 4000)
    public void testFillEmptyByteArray() {
        byte[] emptyArray = new byte[0];
        byte[] resultArray = ArrayFill.fill(emptyArray, (byte) (-83));
        assertSame("The filled array should be the same as the original array", emptyArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyThrowableArray() {
        Throwable[] emptyArray = new Throwable[0];
        MockThrowable mockThrowable = new MockThrowable();
        Throwable[] resultArray = ArrayFill.fill(emptyArray, mockThrowable);
        assertSame("The filled array should be the same as the original array", emptyArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullObjectArray() {
        Object object = new Object();
        Object[] resultArray = ArrayFill.fill((Object[]) null, object);
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillObjectArrayWithFailableFunction() {
        Object[] objectArray = new Object[2];
        FailableIntFunction<Object, Throwable> failableFunction = FailableIntFunction.nop();
        Object[] resultArray = ArrayFill.fill(objectArray, failableFunction);
        assertSame("The filled array should be the same as the original array", objectArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillObjectArrayWithNullFailableFunction() {
        Object[] objectArray = new Object[7];
        Object[] resultArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) null);
        assertSame("The filled array should be the same as the original array", objectArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyObjectArrayWithFailableFunction() {
        FailableIntFunction<Object, Throwable> failableFunction = FailableIntFunction.nop();
        Object[] emptyArray = new Object[0];
        Object[] resultArray = ArrayFill.fill(emptyArray, failableFunction);
        assertSame("The filled array should be the same as the original array", emptyArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullObjectArrayWithFailableFunction() {
        FailableIntFunction<Object, Throwable> failableFunction = FailableIntFunction.nop();
        Object[] resultArray = ArrayFill.fill((Object[]) null, failableFunction);
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillShortArrayWithZero() {
        short[] shortArray = new short[1];
        short[] resultArray = ArrayFill.fill(shortArray, (short) 0);
        assertArrayEquals("The filled array should contain a single zero", new short[]{(short) 0}, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullShortArray() {
        short[] resultArray = ArrayFill.fill((short[]) null, (short) (-1244));
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyLongArray() {
        long[] emptyArray = new long[0];
        long[] resultArray = ArrayFill.fill(emptyArray, 0L);
        assertEquals("The length of the filled array should be 0", 0, resultArray.length);
    }

    @Test(timeout = 4000)
    public void testFillNullLongArray() {
        long[] resultArray = ArrayFill.fill((long[]) null, 1822L);
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyIntArray() {
        int[] emptyArray = new int[0];
        int[] resultArray = ArrayFill.fill(emptyArray, (-1));
        assertSame("The filled array should be the same as the original array", emptyArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullIntArray() {
        int[] resultArray = ArrayFill.fill((int[]) null, (-7720));
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillFloatArrayWithNegativeOne() {
        float[] floatArray = new float[2];
        float[] resultArray = ArrayFill.fill(floatArray, (-1.0F));
        assertArrayEquals("The filled array should contain -1.0 in all positions", new float[]{(-1.0F), (-1.0F)}, resultArray, 0.01F);
    }

    @Test(timeout = 4000)
    public void testFillNullFloatArray() {
        float[] resultArray = ArrayFill.fill((float[]) null, 1.0F);
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyDoubleArray() {
        double[] emptyArray = new double[0];
        double[] resultArray = ArrayFill.fill(emptyArray, 0.0);
        assertSame("The filled array should be the same as the original array", emptyArray, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullDoubleArray() {
        double[] resultArray = ArrayFill.fill((double[]) null, -1244.0);
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillCharArrayWithJ() {
        char[] charArray = new char[2];
        char[] resultArray = ArrayFill.fill(charArray, 'J');
        assertArrayEquals("The filled array should contain 'J' in all positions", new char[]{'J', 'J'}, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullCharArray() {
        char[] resultArray = ArrayFill.fill((char[]) null, 'f');
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillByteArrayWithZero() {
        byte[] byteArray = new byte[1];
        byte[] resultArray = ArrayFill.fill(byteArray, (byte) 0);
        assertArrayEquals("The filled array should contain a single zero", new byte[]{(byte) 0}, resultArray);
    }

    @Test(timeout = 4000)
    public void testFillNullByteArray() {
        byte[] resultArray = ArrayFill.fill((byte[]) null, (byte) (-40));
        assertNull("The filled array should be null", resultArray);
    }

    @Test(timeout = 4000)
    public void testFillBooleanArrayWithFalse() {
        boolean[] booleanArray = new boolean[8];
        boolean[] resultArray = ArrayFill.fill(booleanArray, false);
        assertTrue("The filled array should contain false in all positions", Arrays.equals(new boolean[]{false, false, false, false, false, false, false, false}, resultArray));
    }

    @Test(timeout = 4000)
    public void testFillNullBooleanArray() {
        boolean[] resultArray = ArrayFill.fill((boolean[]) null, false);
        assertNull("The filled array should be null", resultArray);
    }
}