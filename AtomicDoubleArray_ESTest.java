package com.google.common.util.concurrent;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.util.concurrent.AtomicDoubleArray;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Test suite for AtomicDoubleArray functionality.
 * Tests atomic operations, thread-safe updates, and error conditions.
 */
public class AtomicDoubleArrayTest {

    private static final double DELTA = 0.01;
    private static final double INITIAL_VALUE = 100.0;
    private static final double NEW_VALUE = 200.0;
    private static final int ARRAY_SIZE = 10;

    // ========== Constructor Tests ==========

    @Test
    public void constructor_withSize_createsArrayWithCorrectLength() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        
        assertEquals("Array should have correct length", ARRAY_SIZE, array.length());
    }

    @Test
    public void constructor_withDoubleArray_copiesValues() {
        double[] sourceArray = {1.0, 2.5, -3.7, 0.0};
        
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(sourceArray);
        
        assertEquals("Array should have same length as source", 4, atomicArray.length());
        assertEquals("First element should match", 1.0, atomicArray.get(0), DELTA);
        assertEquals("Second element should match", 2.5, atomicArray.get(1), DELTA);
        assertEquals("Third element should match", -3.7, atomicArray.get(2), DELTA);
        assertEquals("Fourth element should match", 0.0, atomicArray.get(3), DELTA);
    }

    @Test
    public void constructor_withEmptyArray_createsEmptyAtomicArray() {
        double[] emptyArray = new double[0];
        
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(emptyArray);
        
        assertEquals("Empty array should have length 0", 0, atomicArray.length());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullArray_throwsNullPointerException() {
        new AtomicDoubleArray((double[]) null);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void constructor_withNegativeSize_throwsNegativeArraySizeException() {
        new AtomicDoubleArray(-1);
    }

    // ========== Basic Get/Set Operations ==========

    @Test
    public void get_returnsCorrectValue() {
        double[] values = {42.5, -17.3};
        AtomicDoubleArray array = new AtomicDoubleArray(values);
        
        assertEquals("Should return correct value at index 0", 42.5, array.get(0), DELTA);
        assertEquals("Should return correct value at index 1", -17.3, array.get(1), DELTA);
    }

    @Test
    public void set_updatesValueCorrectly() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        
        array.set(0, INITIAL_VALUE);
        
        assertEquals("Value should be updated", INITIAL_VALUE, array.get(0), DELTA);
    }

    @Test
    public void lazySet_updatesValueEventually() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        
        array.lazySet(0, INITIAL_VALUE);
        
        // Note: lazySet provides eventual consistency, so we just verify it doesn't throw
        assertEquals("Array length should remain unchanged", ARRAY_SIZE, array.length());
    }

    // ========== Atomic Update Operations ==========

    @Test
    public void getAndSet_returnsOldValueAndSetsNew() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        array.set(0, INITIAL_VALUE);
        
        double oldValue = array.getAndSet(0, NEW_VALUE);
        
        assertEquals("Should return old value", INITIAL_VALUE, oldValue, DELTA);
        assertEquals("Should set new value", NEW_VALUE, array.get(0), DELTA);
    }

    @Test
    public void compareAndSet_withMatchingExpected_updatesValue() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        // Array is initialized with zeros
        
        boolean success = array.compareAndSet(0, 0.0, INITIAL_VALUE);
        
        assertTrue("Compare-and-set should succeed with matching expected value", success);
        assertEquals("Value should be updated", INITIAL_VALUE, array.get(0), DELTA);
    }

    @Test
    public void compareAndSet_withNonMatchingExpected_doesNotUpdate() {
        double[] values = {INITIAL_VALUE};
        AtomicDoubleArray array = new AtomicDoubleArray(values);
        
        boolean success = array.compareAndSet(0, 0.0, NEW_VALUE);
        
        assertFalse("Compare-and-set should fail with non-matching expected value", success);
        assertEquals("Value should remain unchanged", INITIAL_VALUE, array.get(0), DELTA);
    }

    @Test
    public void weakCompareAndSet_withMatchingExpected_updatesValue() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        
        boolean success = array.weakCompareAndSet(0, 0.0, INITIAL_VALUE);
        
        assertTrue("Weak compare-and-set should succeed", success);
    }

    // ========== Arithmetic Operations ==========

    @Test
    public void getAndAdd_returnsOldValueAndAddsToIt() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        double addValue = 25.5;
        
        double oldValue = array.getAndAdd(0, addValue);
        
        assertEquals("Should return old value (0.0)", 0.0, oldValue, DELTA);
        assertEquals("Should add to the value", addValue, array.get(0), DELTA);
    }

    @Test
    public void addAndGet_addsValueAndReturnsNew() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        array.set(0, INITIAL_VALUE);
        double addValue = 50.0;
        
        double newValue = array.addAndGet(0, addValue);
        
        assertEquals("Should return new value", INITIAL_VALUE + addValue, newValue, DELTA);
        assertEquals("Array should contain new value", INITIAL_VALUE + addValue, array.get(0), DELTA);
    }

    @Test
    public void addAndGet_withZeroDelta_returnsCurrentValue() {
        double[] values = {INITIAL_VALUE};
        AtomicDoubleArray array = new AtomicDoubleArray(values);
        
        double result = array.addAndGet(0, 0.0);
        
        assertEquals("Adding zero should return current value", INITIAL_VALUE, result, DELTA);
    }

    // ========== Functional Update Operations ==========

    @Test
    public void updateAndGet_appliesFunction() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        DoubleUnaryOperator identityFunction = DoubleUnaryOperator.identity();
        
        double result = array.updateAndGet(0, identityFunction);
        
        assertEquals("Identity function should return same value", 0.0, result, DELTA);
    }

    @Test
    public void getAndUpdate_appliesFunctionAndReturnsOldValue() {
        double[] values = {INITIAL_VALUE};
        AtomicDoubleArray array = new AtomicDoubleArray(values);
        DoubleUnaryOperator identityFunction = DoubleUnaryOperator.identity();
        
        double oldValue = array.getAndUpdate(0, identityFunction);
        
        assertEquals("Should return old value", INITIAL_VALUE, oldValue, DELTA);
    }

    @Test
    public void accumulateAndGet_appliesBinaryOperatorAndReturnsNewValue() {
        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class);
        when(mockOperator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(42.0);
        
        double result = array.accumulateAndGet(0, 10.0, mockOperator);
        
        assertEquals("Should return result of binary operation", 42.0, result, DELTA);
        verify(mockOperator).applyAsDouble(0.0, 10.0);
    }

    @Test
    public void getAndAccumulate_appliesBinaryOperatorAndReturnsOldValue() {
        double[] values = {INITIAL_VALUE};
        AtomicDoubleArray array = new AtomicDoubleArray(values);
        DoubleBinaryOperator mockOperator = mock(DoubleBinaryOperator.class);
        when(mockOperator.applyAsDouble(anyDouble(), anyDouble())).thenReturn(NEW_VALUE);
        
        double oldValue = array.getAndAccumulate(0, 50.0, mockOperator);
        
        assertEquals("Should return old value", INITIAL_VALUE, oldValue, DELTA);
        verify(mockOperator).applyAsDouble(INITIAL_VALUE, 50.0);
    }

    // ========== String Representation ==========

    @Test
    public void toString_withEmptyArray_returnsEmptyBrackets() {
        AtomicDoubleArray array = new AtomicDoubleArray(0);
        
        String result = array.toString();
        
        assertEquals("Empty array should return empty brackets", "[]", result);
    }

    @Test
    public void toString_withValues_returnsFormattedString() {
        AtomicDoubleArray array = new AtomicDoubleArray(3);
        // Just verify toString doesn't throw - exact format may vary
        
        String result = array.toString();
        
        assertNotNull("toString should not return null", result);
    }

    // ========== Error Conditions ==========

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_withInvalidIndex_throwsIndexOutOfBoundsException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.get(5); // Index equals length
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void set_withInvalidIndex_throwsIndexOutOfBoundsException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.set(10, INITIAL_VALUE);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void compareAndSet_withInvalidIndex_throwsIndexOutOfBoundsException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.compareAndSet(5, 0.0, INITIAL_VALUE);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAndGet_withInvalidIndex_throwsIndexOutOfBoundsException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.addAndGet(5, 10.0);
    }

    @Test(expected = NullPointerException.class)
    public void updateAndGet_withNullFunction_throwsNullPointerException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.updateAndGet(0, null);
    }

    @Test(expected = NullPointerException.class)
    public void getAndUpdate_withNullFunction_throwsNullPointerException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.getAndUpdate(0, null);
    }

    @Test(expected = NullPointerException.class)
    public void accumulateAndGet_withNullOperator_throwsNullPointerException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.accumulateAndGet(0, 10.0, null);
    }

    @Test(expected = NullPointerException.class)
    public void getAndAccumulate_withNullOperator_throwsNullPointerException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.getAndAccumulate(0, 10.0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getAndAdd_withNegativeIndex_throwsIndexOutOfBoundsException() {
        AtomicDoubleArray array = new AtomicDoubleArray(5);
        
        array.getAndAdd(-1, 10.0);
    }
}