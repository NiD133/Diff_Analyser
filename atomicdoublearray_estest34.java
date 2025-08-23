package com.google.common.util.concurrent;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.function.DoubleBinaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that accumulateAndGet throws an IndexOutOfBoundsException when the provided
     * index is equal to the array's length, which is an invalid, out-of-bounds index.
     */
    @Test(timeout = 4000)
    public void accumulateAndGet_withIndexEqualToLength_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array of a small, easy-to-understand size.
        int arrayLength = 10;
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);

        // The first invalid index is the one equal to the array's length.
        int outOfBoundsIndex = arrayLength;

        // The accumulator function is not used when the index check fails,
        // so a simple dummy implementation is sufficient.
        DoubleBinaryOperator dummyAccumulator = (left, right) -> left + right;

        // Act & Assert
        try {
            array.accumulateAndGet(outOfBoundsIndex, 1.0, dummyAccumulator);
            fail("Expected an IndexOutOfBoundsException for index " + outOfBoundsIndex);
        } catch (IndexOutOfBoundsException expected) {
            // This is the expected behavior.
            // For a more robust test, we can verify the exception message.
            String expectedMessageFragment = "index " + outOfBoundsIndex;
            assertTrue(
                "Exception message should contain the out-of-bounds index.",
                expected.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}