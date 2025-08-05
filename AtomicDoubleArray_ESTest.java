package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.google.common.util.concurrent.AtomicDoubleArray;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class AtomicDoubleArray_ESTest extends AtomicDoubleArray_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCompareAndSetSuccess() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1027);
        boolean result = atomicArray.compareAndSet(1025, 0.0, 1027);
        assertEquals(1027, atomicArray.length());
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testWeakCompareAndSetFailure() throws Throwable {
        double[] initialValues = new double[9];
        initialValues[0] = -1583.803774;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        boolean result = atomicArray.weakCompareAndSet(0, 0.0, 690.74034879);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testGetAndAdd() throws Throwable {
        double[] initialValues = new double[12];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double originalValue = atomicArray.getAndAdd(4, 4);
        assertEquals(0.0, originalValue, 0.01);

        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double updatedValue = atomicArray.updateAndGet(4, identityOperator);
        assertEquals(4.0, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testUpdateAndGetWithIdentityOperator() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(993);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double originalValue = atomicArray.getAndAdd(75, -692.410613387175);
        assertEquals(0.0, originalValue, 0.01);

        double updatedValue = atomicArray.updateAndGet(75, identityOperator);
        assertEquals(993, atomicArray.length());
        assertEquals(-692.410613387175, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testEmptyArrayLength() throws Throwable {
        double[] emptyArray = new double[0];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(emptyArray);
        int length = atomicArray.length();
        assertEquals(0, length);
    }

    @Test(timeout = 4000)
    public void testGetAndSet() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1027);
        atomicArray.getAndSet(5, 1027);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double originalValue = atomicArray.getAndUpdate(5, identityOperator);
        assertEquals(1027.0, originalValue, 0.01);
        assertEquals(1027, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testGetAndUpdateWithIdentityOperator() throws Throwable {
        double[] initialValues = new double[9];
        initialValues[0] = -1583.803774;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double originalValue = atomicArray.getAndUpdate(0, identityOperator);
        assertEquals(-1583.803774, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAndSetWithNewValue() throws Throwable {
        double[] initialValues = new double[4];
        initialValues[1] = 540.0;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double originalValue = atomicArray.getAndSet(1, 2806.574374631918);
        assertEquals(540.0, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAndSetWithNegativeValue() throws Throwable {
        double[] initialValues = new double[9];
        initialValues[0] = -1583.803774;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double originalValue = atomicArray.getAndSet(0, -1710.836556);
        assertEquals(-1583.803774, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAddAndGet() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double updatedValue = atomicArray.addAndGet(0, 1775.31884);
        assertEquals(1775.31884, updatedValue, 0.01);

        double currentValue = atomicArray.getAndAdd(0, 0.0);
        assertEquals(1775.31884, currentValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAndAccumulateWithMockOperator() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(3813);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(-558.4039384).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double originalValue = atomicArray.getAndAccumulate(1661, 1.0, mockOperator);
        assertEquals(0.0, originalValue, 0.01);

        double currentValue = atomicArray.getAndAdd(1661, 0.0);
        assertEquals(-558.4039384, currentValue, 0.01);
        assertEquals(3813, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testGetAndAccumulateWithNegativeResult() throws Throwable {
        double[] initialValues = new double[6];
        initialValues[1] = 354.258;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(-1.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double originalValue = atomicArray.getAndAccumulate(1, -2032.2, mockOperator);
        assertEquals(354.258, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAndAccumulateWithZeroResult() throws Throwable {
        double[] initialValues = new double[9];
        initialValues[0] = -1583.803774;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(0.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double originalValue = atomicArray.getAndAccumulate(0, -1583.803774, mockOperator);
        assertEquals(-1583.803774, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetWithNegativeResult() throws Throwable {
        double[] initialValues = new double[6];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(-1.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double updatedValue = atomicArray.accumulateAndGet(1, -2032.2, mockOperator);
        assertEquals(-1.0, updatedValue, 0.01);

        double currentValue = atomicArray.get(1);
        assertEquals(-1.0, currentValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAddAndGetWithZeroDelta() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double updatedValue = atomicArray.addAndGet(0, 0.0);
        assertEquals(0.0, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAddAndGetWithNegativeInitialValue() throws Throwable {
        double[] initialValues = new double[6];
        initialValues[0] = -970.960157577528;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double updatedValue = atomicArray.addAndGet(0, 0.0);
        assertEquals(-970.960157577528, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetWithZeroResult() throws Throwable {
        double[] initialValues = new double[9];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(0.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double updatedValue = atomicArray.accumulateAndGet(0, 1814.345964, mockOperator);
        assertEquals(0.0, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetWithOneResult() throws Throwable {
        double[] initialValues = new double[9];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(1.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double updatedValue = atomicArray.accumulateAndGet(0, 0.0, mockOperator);
        assertEquals(1.0, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetWithNegativeResultAndLargeArray() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(3740);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(-1.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double updatedValue = atomicArray.accumulateAndGet(67, 3740, mockOperator);
        assertEquals(-1.0, updatedValue, 0.01);
        assertEquals(3740, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testWeakCompareAndSetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(93);
        try {
            atomicArray.weakCompareAndSet(93, 93, 93);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateAndGetWithNullOperator() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1109);
        try {
            atomicArray.updateAndGet(156, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.AtomicDoubleArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateAndGetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(2924);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        try {
            atomicArray.updateAndGet(2924, identityOperator);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringOnEmptyArray() throws Throwable {
        double[] emptyArray = new double[0];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(emptyArray);
        String arrayString = atomicArray.toString();
        assertEquals("[]", arrayString);
    }

    @Test(timeout = 4000)
    public void testSetOutOfBounds() throws Throwable {
        double[] initialValues = new double[1];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        try {
            atomicArray.set(1102, 1102);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testLazySetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(600);
        try {
            atomicArray.lazySet(600, 600);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAndUpdateWithNullOperator() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(19);
        try {
            atomicArray.getAndUpdate(4, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.AtomicDoubleArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAndUpdateOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1027);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        try {
            atomicArray.getAndUpdate(1027, identityOperator);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAndSetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(3);
        try {
            atomicArray.getAndSet(3, 3);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAndAccumulateWithNullOperator() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(3);
        try {
            atomicArray.getAndAccumulate(3, 3, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAndAccumulateOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(8);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        try {
            atomicArray.getAndAccumulate(8, 8, mockOperator);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(93);
        try {
            atomicArray.get(93);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testCompareAndSetOutOfBounds() throws Throwable {
        double[] emptyArray = new double[0];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(emptyArray);
        try {
            atomicArray.compareAndSet(44, 44, 44);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAndGetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(2363);
        try {
            atomicArray.addAndGet(2363, 2363);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetOutOfBounds() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(3740);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        try {
            atomicArray.accumulateAndGet(3740, 3740, mockOperator);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullArray() throws Throwable {
        try {
            AtomicDoubleArray atomicArray = new AtomicDoubleArray((double[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.util.concurrent.AtomicDoubleArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNegativeSize() throws Throwable {
        try {
            AtomicDoubleArray atomicArray = new AtomicDoubleArray(-1);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateAndGetWithIdentityOperatorOnZero() throws Throwable {
        double[] initialValues = new double[12];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double updatedValue = atomicArray.updateAndGet(4, identityOperator);
        assertEquals(0.0, updatedValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testGetAndUpdateWithIdentityOperatorOnZero() throws Throwable {
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(1027);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        double originalValue = atomicArray.getAndUpdate(5, identityOperator);
        assertEquals(0.0, originalValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testArrayLength() throws Throwable {
        double[] initialValues = new double[2];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        int length = atomicArray.length();
        assertEquals(2, length);
    }

    @Test(timeout = 4000)
    public void testAccumulateAndGetWithNullOperator() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        try {
            atomicArray.accumulateAndGet(1953, 1953, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringOnNonEmptyArray() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        String arrayString = atomicArray.toString();
        assertEquals("[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]", arrayString);
    }

    @Test(timeout = 4000)
    public void testCompareAndSetWithMockOperator() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class, new ViolatedAssumptionAnswer());
        doReturn(-1.0).when(mockOperator).applyAsDouble(anyDouble(), anyDouble());
        double originalValue = atomicArray.getAndAccumulate(0, 1303.997034846174, mockOperator);
        assertEquals(0.0, originalValue, 0.01);

        boolean result = atomicArray.compareAndSet(0, 0.0, 0.0);
        assertFalse(result);
        assertEquals(8, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testLazySet() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        atomicArray.lazySet(0, 0.0);
        assertEquals(8, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testGet() throws Throwable {
        double[] initialValues = new double[6];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        atomicArray.get(1);
        assertEquals(6, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testSet() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        atomicArray.set(2, 2358.26006);
        assertEquals(8, atomicArray.length());
    }

    @Test(timeout = 4000)
    public void testGetAndAddOutOfBounds() throws Throwable {
        double[] initialValues = new double[9];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        try {
            atomicArray.getAndAdd(-200, -198.0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.concurrent.atomic.AtomicLongArray", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAndGetWithPositiveDelta() throws Throwable {
        double[] initialValues = new double[8];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        double updatedValue = atomicArray.addAndGet(0, 1775.31884);
        assertEquals(1775.31884, updatedValue, 0.01);

        double currentValue = atomicArray.get(0);
        assertEquals(8, atomicArray.length());
        assertEquals(1775.31884, currentValue, 0.01);
    }

    @Test(timeout = 4000)
    public void testWeakCompareAndSetSuccess() throws Throwable {
        double[] initialValues = new double[9];
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(initialValues);
        boolean result = atomicArray.weakCompareAndSet(0, 0, -1.0);
        assertEquals(9, atomicArray.length());
        assertTrue(result);
    }
}