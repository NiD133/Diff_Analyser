package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Contains tests for the {@link AtomicDoubleArray} class.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndUpdate_withIdentityOperator_returnsPreviousValue() {
        // Arrange
        final int ARRAY_SIZE = 1027;
        final int TEST_INDEX = 5;
        final double INITIAL_VALUE = 1027.0;

        AtomicDoubleArray array = new AtomicDoubleArray(ARRAY_SIZE);
        array.set(TEST_INDEX, INITIAL_VALUE);

        // The identity operator is a function that simply returns its input.
        // Using it allows us to test the "get" part of getAndUpdate without a side effect.
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();

        // Act
        // getAndUpdate should atomically update the value and return the *old* value.
        double previousValue = array.getAndUpdate(TEST_INDEX, identityOperator);

        // Assert
        // 1. Verify that the method returned the value from *before* the update.
        assertEquals(
                "getAndUpdate should return the previous value.",
                INITIAL_VALUE,
                previousValue,
                0.0);

        // 2. Verify the value in the array is unchanged, as expected with an identity operator.
        assertEquals(
                "The value at the index should not change after applying the identity operator.",
                INITIAL_VALUE,
                array.get(TEST_INDEX),
                0.0);
        
        // 3. Verify the array's length is unaffected.
        assertEquals("Array length should remain constant.", ARRAY_SIZE, array.length());
    }
}