package com.google.common.util.concurrent;

import org.junit.Test;
import java.util.function.DoubleBinaryOperator;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test(expected = NullPointerException.class)
    public void accumulateAndGet_withNullFunction_throwsNullPointerException() {
        // Arrange: Create an array and define parameters for the method call.
        // The array size and initial values are not critical, but we need a valid index
        // to ensure the test focuses solely on the null function argument.
        AtomicDoubleArray array = new AtomicDoubleArray(1);
        int validIndex = 0;
        double value = 10.0;
        DoubleBinaryOperator nullFunction = null;

        // Act: Call the method under test with a null function.
        // The test is expected to throw a NullPointerException at this point.
        // The original test confusingly used an out-of-bounds index, which obscured
        // the test's purpose. By using a valid index, we verify that the null check
        // on the function is the specific cause of the failure.
        array.accumulateAndGet(validIndex, value, nullFunction);
    }
}