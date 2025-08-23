package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Test suite for {@link AtomicDoubleArray}.
 * Note: The original test class name "AtomicDoubleArray_ESTestTest22" was renamed for clarity.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that calling updateAndGet() with an index equal to the array's length
     * correctly throws an IndexOutOfBoundsException.
     */
    @Test
    public void updateAndGet_withIndexEqualToLength_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array of a small, clear size and define the out-of-bounds index.
        int arrayLength = 10;
        int outOfBoundsIndex = arrayLength; // The first invalid index is the length itself.
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);
        DoubleUnaryOperator identityFunction = DoubleUnaryOperator.identity();

        // Act & Assert: Attempt the operation and verify that the expected exception is thrown.
        try {
            array.updateAndGet(outOfBoundsIndex, identityFunction);
            fail("Expected an IndexOutOfBoundsException to be thrown, but it was not.");
        } catch (IndexOutOfBoundsException expectedException) {
            // Optional: Verify the exception message for more precise testing.
            // The underlying AtomicLongArray is expected to produce this message format.
            String expectedMessage = "index " + outOfBoundsIndex;
            assertEquals(expectedMessage, expectedException.getMessage());
        }
    }

    /**
     * Alternative implementation using JUnit 4's 'expected' parameter for conciseness.
     * This version is less flexible as it cannot verify the exception's message.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void updateAndGet_withOutOfBoundsIndex_throwsException_alternative() {
        // Arrange
        int arrayLength = 10;
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);
        DoubleUnaryOperator identityFunction = DoubleUnaryOperator.identity();

        // Act: This call is expected to throw the exception declared in the @Test annotation.
        array.updateAndGet(arrayLength, identityFunction);
    }
}