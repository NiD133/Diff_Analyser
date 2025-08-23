package com.google.common.util.concurrent;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for {@link AtomicDoubleArray}.
 * This class focuses on verifying boundary conditions and exception handling.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that addAndGet() throws an IndexOutOfBoundsException when
     * the specified index is outside the valid range of the array.
     */
    @Test
    public void addAndGet_whenIndexIsOutOfBounds_shouldThrowException() {
        // Arrange: Create an array and define an index that is out of bounds.
        int arrayLength = 10;
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);
        
        // The first invalid index is equal to the array's length.
        int invalidIndex = arrayLength;
        double valueToAdd = 1.0;

        // Act & Assert: Attempt the operation and verify the correct exception is thrown.
        try {
            array.addAndGet(invalidIndex, valueToAdd);
            fail("Expected an IndexOutOfBoundsException to be thrown for index " + invalidIndex);
        } catch (IndexOutOfBoundsException e) {
            // This is the expected outcome.
            // For a more robust test, we can verify the exception message.
            String expectedMessageFragment = "index " + invalidIndex;
            assertTrue(
                "The exception message should contain the invalid index.",
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}