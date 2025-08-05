package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Understandable tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    // A small delta for comparing floating-point numbers.
    private static final double DELTA = 1e-9;

    // --- Constructor Tests ---

    @Test
    public void constructorWithLength_shouldCreateArrayOfZeros() {
        // Arrange
        int length = 10;

        // Act
        AtomicDoubleArray array = new AtomicDoubleArray(length);

        // Assert
        assertEquals(length, array.length());
        for (int i = 0; i < length; i++) {
            assertEquals(0.0, array.get(i), DELTA);
        }
    }

    @Test
    public void constructorWithArray_shouldCreateDefensiveCopy() {
        // Arrange
        double[] initialValues = {1.1, 2.2, 3.3};

        // Act
        AtomicDoubleArray array = new AtomicDoubleArray(initialValues);

        // Assert
        assertEquals(initialValues.length, array.length());
        assertEquals(1.1, array.get(0), DELTA);

        // Modify the original array to ensure the AtomicDoubleArray is a copy
        initialValues[0] = 9.9;
        assertEquals("AtomicDoubleArray should not be affected by changes to the original array.", 1.1, array.get(0), DELTA);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void constructorWithNegativeLength_shouldThrowException() {
        // Act
        new AtomicDoubleArray(-1);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullArray_shouldThrowException() {
        // Act
        new AtomicDoubleArray(null);
    }

    // --- Basic Operation Tests (length, get, set, lazySet) ---

    @Test
    public void length_shouldReturnArrayLength() {
        // Arrange
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(0);
        AtomicDoubleArray sizedArray = new AtomicDoubleArray(5);

        // Act & Assert
        assertEquals(0, emptyArray.length());
        assertEquals(5, sizedArray.length());
    }

    @Test
    public void setAndGet_shouldUpdateAndRetrieveValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(3);
        double valueToSet = 123.45;

        // Act
        array.set(1, valueToSet);
        double retrievedValue = array.get(1);

        // Assert
        assertEquals(valueToSet, retrievedValue, DELTA);
    }

    @Test
    public void lazySet_shouldUpdateValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(3);
        double valueToSet = 543.21;

        // Act
        array.lazySet(1, valueToSet);

        // Assert
        // In a single-threaded test, the result of lazySet should be immediately visible.
        assertEquals(valueToSet, array.get(1), DELTA);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_withInvalidIndex_shouldThrowException() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(3);
        // Act
        array.get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void set_withInvalidIndex_shouldThrowException() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(3);
        // Act
        array.set(3, 1.0);
    }

    // --- Atomic Operation Tests ---

    @Test
    public void compareAndSet_shouldSucceedAndUpdateValue_whenExpectedValueMatches() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        double expect = 20.0;
        double update = 30.0;

        // Act
        boolean result = array.compareAndSet(1, expect, update);

        // Assert
        assertTrue("compareAndSet should succeed when expectation matches.", result);
        assertEquals(update, array.get(1), DELTA);
    }

    @Test
    public void compareAndSet_shouldFail_whenExpectedValueDoesNotMatch() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        double expect = 99.0; // Incorrect expected value
        double update = 30.0;
        double originalValue = array.get(1);

        // Act
        boolean result = array.compareAndSet(1, expect, update);

        // Assert
        assertFalse("compareAndSet should fail when expectation does not match.", result);
        assertEquals("Value should not change on failed CAS.", originalValue, array.get(1), DELTA);
    }

    @Test
    public void weakCompareAndSet_shouldSucceed_whenExpectedValueMatches() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 0.0});
        double expect = 0.0;
        double update = -1.0;

        // Act
        boolean result = array.weakCompareAndSet(1, expect, update);

        // Assert
        assertTrue("weakCompareAndSet should succeed when expectation matches.", result);
        assertEquals(update, array.get(1), DELTA);
    }

    @Test
    public void getAndSet_shouldReturnOldValueAndUpdateElement() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        double newValue = 30.0;
        double originalValue = array.get(1);

        // Act
        double oldValue = array.getAndSet(1, newValue);

        // Assert
        assertEquals(originalValue, oldValue, DELTA);
        assertEquals(newValue, array.get(1), DELTA);
    }

    @Test
    public void addAndGet_shouldAddDeltaAndReturnNewValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        double delta = 5.5;

        // Act
        double newValue = array.addAndGet(1, delta);

        // Assert
        assertEquals(25.5, newValue, DELTA);
        assertEquals(25.5, array.get(1), DELTA);
    }

    @Test
    public void getAndAdd_shouldAddDeltaAndReturnOldValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        double delta = 5.5;
        double originalValue = array.get(1);

        // Act
        double oldValue = array.getAndAdd(1, delta);

        // Assert
        assertEquals(originalValue, oldValue, DELTA);
        assertEquals(originalValue + delta, array.get(1), DELTA);
    }

    // --- Function-based Atomic Operation Tests ---

    @Test
    public void updateAndGet_shouldApplyOperatorAndReturnNewValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        DoubleUnaryOperator doubler = (x) -> x * 2;

        // Act
        double newValue = array.updateAndGet(1, doubler);

        // Assert
        assertEquals(40.0, newValue, DELTA);
        assertEquals(40.0, array.get(1), DELTA);
    }

    @Test
    public void getAndUpdate_shouldApplyOperatorAndReturnOldValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        DoubleUnaryOperator doubler = (x) -> x * 2;
        double originalValue = array.get(1);

        // Act
        double oldValue = array.getAndUpdate(1, doubler);

        // Assert
        assertEquals(originalValue, oldValue, DELTA);
        assertEquals(originalValue * 2, array.get(1), DELTA);
    }

    @Test
    public void accumulateAndGet_shouldApplyAccumulatorAndReturnNewValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        DoubleBinaryOperator accumulator = (current, update) -> current + update * 2;
        double updateValue = 5.0;

        // Act
        double newValue = array.accumulateAndGet(1, updateValue, accumulator);

        // Assert
        assertEquals(30.0, newValue, DELTA); // 20.0 + 5.0 * 2
        assertEquals(30.0, array.get(1), DELTA);
    }

    @Test
    public void getAndAccumulate_shouldApplyAccumulatorAndReturnOldValue() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{10.0, 20.0});
        DoubleBinaryOperator mockAccumulator = mock(DoubleBinaryOperator.class);
        when(mockAccumulator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(99.0);
        double updateValue = 5.0;
        double originalValue = array.get(1);

        // Act
        double oldValue = array.getAndAccumulate(1, updateValue, mockAccumulator);

        // Assert
        assertEquals(originalValue, oldValue, DELTA);
        verify(mockAccumulator).applyAsDouble(originalValue, updateValue);
        assertEquals("Value should be updated to the result of the accumulator.", 99.0, array.get(1), DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void getAndAccumulate_withNullAccumulator_shouldThrowException() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(2);
        // Act
        array.getAndAccumulate(0, 1.0, null);
    }

    // --- toString Test ---

    @Test
    public void toString_shouldReturnStringRepresentationOfArray() {
        // Arrange
        AtomicDoubleArray emptyArray = new AtomicDoubleArray(0);
        AtomicDoubleArray array = new AtomicDoubleArray(new double[]{1.0, 2.5, -3.0});

        // Act & Assert
        assertEquals("[]", emptyArray.toString());
        assertEquals("[1.0, 2.5, -3.0]", array.toString());
    }
}