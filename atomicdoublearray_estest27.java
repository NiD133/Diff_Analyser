package com.google.common.util.concurrent;

import org.junit.Test;

import java.util.function.DoubleUnaryOperator;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that getAndUpdate() throws an IndexOutOfBoundsException when the provided
     * index is equal to the array's length, which is an invalid, out-of-bounds index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getAndUpdate_withOutOfBoundsIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array and an operator. The index to be tested is deliberately
        // set to the array's size, which is the first invalid index.
        int size = 10;
        int invalidIndex = size;
        AtomicDoubleArray array = new AtomicDoubleArray(size);
        DoubleUnaryOperator identityOperator = DoubleUnaryOperator.identity();

        // Act: Attempt to update the array at the out-of-bounds index.
        // This call is expected to throw an IndexOutOfBoundsException.
        array.getAndUpdate(invalidIndex, identityOperator);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}