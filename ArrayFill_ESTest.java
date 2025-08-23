package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayFill;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ArrayFill_ESTest extends ArrayFill_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFillEmptyBooleanArray() {
        boolean[] emptyArray = new boolean[0];
        boolean[] filledArray = ArrayFill.fill(emptyArray, true);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillShortArrayWithNegativeValue() {
        short[] shortArray = new short[6];
        short[] filledArray = ArrayFill.fill(shortArray, (short) -838);
        assertArrayEquals(new short[] {-838, -838, -838, -838, -838, -838}, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyObjectArrayWithNoOpFunction() {
        Object[] emptyArray = new Object[0];
        FailableIntFunction<Object, Throwable> noOpFunction = FailableIntFunction.nop();
        Object[] filledArray = ArrayFill.fill(emptyArray, noOpFunction);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillThrowableArrayWithMockThrowable() {
        Throwable[] throwableArray = new Throwable[8];
        MockThrowable mockThrowable = new MockThrowable();
        Throwable[] filledArray = ArrayFill.fill(throwableArray, mockThrowable);
        assertSame(filledArray, throwableArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyLongArray() {
        long[] emptyArray = new long[0];
        long[] filledArray = ArrayFill.fill(emptyArray, 0L);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillIntArrayWithNegativeValue() {
        int[] intArray = new int[7];
        int[] filledArray = ArrayFill.fill(intArray, -1);
        assertArrayEquals(new int[] {-1, -1, -1, -1, -1, -1, -1}, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillFloatArrayWithSpecificValue() {
        float[] floatArray = new float[5];
        float[] filledArray = ArrayFill.fill(floatArray, 95.0F);
        assertArrayEquals(new float[] {95.0F, 95.0F, 95.0F, 95.0F, 95.0F}, filledArray, 0.01F);
    }

    @Test(timeout = 4000)
    public void testFillEmptyDoubleArray() {
        double[] emptyArray = new double[0];
        double[] filledArray = ArrayFill.fill(emptyArray, -259.0);
        assertEquals(0, filledArray.length);
    }

    @Test(timeout = 4000)
    public void testFillSingleCharArray() {
        char[] charArray = new char[1];
        char[] filledArray = ArrayFill.fill(charArray, ']');
        assertArrayEquals(new char[] {']'}, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillSingleByteArray() {
        byte[] byteArray = new byte[1];
        byte[] filledArray = ArrayFill.fill(byteArray, (byte) 76);
        assertArrayEquals(new byte[] {(byte) 76}, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillThrowableArrayWithInvalidFunction() {
        Throwable[] throwableArray = new Throwable[3];
        FailableIntFunction<Object, Throwable> noOpFunction = FailableIntFunction.nop();
        try {
            ArrayFill.fill((Object[]) throwableArray, noOpFunction);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            verifyException("java.util.Arrays", e);
        }
    }

    @Test(timeout = 4000)
    public void testFillEmptyObjectArrayWithObject() {
        Object[] emptyArray = new Object[0];
        FailableIntFunction<Object, Throwable> noOpFunction = FailableIntFunction.nop();
        Object[] filledArray = ArrayFill.fill(emptyArray, noOpFunction);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullObjectArrayWithMockThrowable() {
        MockThrowable mockThrowable = new MockThrowable(".MLY42", null);
        Object[] filledArray = ArrayFill.fill((Object[]) null, mockThrowable);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillObjectArrayWithNullFunction() {
        Object[] objectArray = new Object[4];
        Object[] filledArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) null);
        assertSame(objectArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillObjectArrayWithNoOpFunction() {
        Object[] objectArray = new Object[6];
        FailableIntFunction<Object, Throwable> noOpFunction = FailableIntFunction.nop();
        Object[] filledArray = ArrayFill.fill(objectArray, noOpFunction);
        assertSame(objectArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullThrowableArrayWithNoOpFunction() {
        FailableIntFunction<Throwable, Throwable> noOpFunction = FailableIntFunction.nop();
        Throwable[] filledArray = ArrayFill.fill((Throwable[]) null, noOpFunction);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyShortArray() {
        short[] emptyArray = new short[0];
        short[] filledArray = ArrayFill.fill(emptyArray, (short) 2);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullShortArray() {
        short[] filledArray = ArrayFill.fill((short[]) null, (short) -3333);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillLongArrayWithNegativeValue() {
        long[] longArray = new long[8];
        long[] filledArray = ArrayFill.fill(longArray, -1L);
        assertArrayEquals(new long[] {-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L}, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullLongArray() {
        long[] filledArray = ArrayFill.fill((long[]) null, 1003L);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyIntArray() {
        int[] emptyArray = new int[0];
        int[] filledArray = ArrayFill.fill(emptyArray, 0);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullIntArray() {
        int[] filledArray = ArrayFill.fill((int[]) null, 0);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyFloatArray() {
        float[] emptyArray = new float[0];
        float[] filledArray = ArrayFill.fill(emptyArray, 1.0F);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullFloatArray() {
        float[] filledArray = ArrayFill.fill((float[]) null, -4645.361F);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillDoubleArrayWithZero() {
        double[] doubleArray = new double[8];
        double[] filledArray = ArrayFill.fill(doubleArray, 0.0);
        assertArrayEquals(new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, filledArray, 0.01);
    }

    @Test(timeout = 4000)
    public void testFillNullDoubleArray() {
        double[] filledArray = ArrayFill.fill((double[]) null, -3333.0);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyCharArray() {
        char[] emptyArray = new char[0];
        char[] filledArray = ArrayFill.fill(emptyArray, 's');
        assertEquals(0, filledArray.length);
    }

    @Test(timeout = 4000)
    public void testFillNullCharArray() {
        char[] filledArray = ArrayFill.fill((char[]) null, 'B');
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillEmptyByteArray() {
        byte[] emptyArray = new byte[0];
        byte[] filledArray = ArrayFill.fill(emptyArray, (byte) 0);
        assertSame(emptyArray, filledArray);
    }

    @Test(timeout = 4000)
    public void testFillNullByteArray() {
        byte[] filledArray = ArrayFill.fill((byte[]) null, (byte) 87);
        assertNull(filledArray);
    }

    @Test(timeout = 4000)
    public void testFillBooleanArrayWithTrue() {
        boolean[] booleanArray = new boolean[4];
        boolean[] filledArray = ArrayFill.fill(booleanArray, true);
        assertTrue(Arrays.equals(new boolean[] {true, true, true, true}, filledArray));
    }

    @Test(timeout = 4000)
    public void testFillNullBooleanArray() {
        boolean[] filledArray = ArrayFill.fill((boolean[]) null, true);
        assertNull(filledArray);
    }
}