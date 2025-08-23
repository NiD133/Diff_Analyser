package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/*
 * The original test class name and inheritance are preserved. In a real-world scenario,
 * this would likely be simplified to a name like `AtomicDoubleArrayTest`.
 */
public class AtomicDoubleArray_ESTestTest38 extends AtomicDoubleArray_ESTest_scaffolding {

    /**
     * Tests that getAndUpdate returns the original value before the update and that
     * applying an identity function leaves the element's value unchanged.
     */
    @Test
    public void getAndUpdate_withIdentityOperator_returnsPreviousValueAndValueIsUnchanged() {
        // Arrange
        int arrayLength = 10;
        int indexToUpdate = 5;
        // A new AtomicDoubleArray is initialized with all elements as 0.0.
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();
        
        double expectedInitialValue = 0.0;

        // Act
        // getAndUpdate atomically updates the element and returns the value *before* the update.
        double returnedValue = array.getAndUpdate(indexToUpdate, identityOperator);

        // Assert
        // 1. Verify that the method returned the original value.
        assertEquals("getAndUpdate should return the value before the update",
                expectedInitialValue, returnedValue, 0.0);

        // 2. Verify that the value at the index is unchanged after applying the identity operator.
        double valueAfterUpdate = array.get(indexToUpdate);
        assertEquals("The value at the index should be unchanged",
                expectedInitialValue, valueAfterUpdate, 0.0);

        // 3. Verify the array's length remains constant.
        assertEquals("Array length should not change", arrayLength, array.length());
    }
}