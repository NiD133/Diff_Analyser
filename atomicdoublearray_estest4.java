package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

public class AtomicDoubleArrayTest {

    private static final double TOLERANCE = 1e-9;

    /**
     * This test verifies the behavior of the updateAndGet method when used
     * with an identity function. It confirms that the method correctly returns
     * the current value of an element without modifying it.
     */
    @Test
    public void updateAndGet_withIdentityOperator_returnsCurrentValueWithoutModification() {
        // Arrange
        final int arraySize = 100;
        final int testIndex = 75;
        final double valueToSet = -692.410613387175;

        // Create an array where all elements are initially 0.0.
        AtomicDoubleArray array = new AtomicDoubleArray(arraySize);

        // To prepare for the main assertion, we first set a specific value at our
        // test index. We use getAndAdd, which conveniently returns the previous value.
        double valueBeforeAdd = array.getAndAdd(testIndex, valueToSet);
        assertEquals("Precondition failed: The initial value at the index should be 0.0",
                0.0, valueBeforeAdd, TOLERANCE);

        // The identity operator is a function that simply returns its input argument.
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();

        // Act
        // We call updateAndGet with the identity operator. This should apply the
        // operator to the current value (valueToSet) and return the result.
        double valueAfterUpdate = array.updateAndGet(testIndex, identityOperator);

        // Assert
        // The value returned by updateAndGet should be the same as the value we set.
        assertEquals("updateAndGet should return the current value.",
                valueToSet, valueAfterUpdate, TOLERANCE);

        // We also verify that the value stored in the array remains unchanged.
        assertEquals("The value in the array should not be changed by the identity update.",
                valueToSet, array.get(testIndex), TOLERANCE);
    }
}