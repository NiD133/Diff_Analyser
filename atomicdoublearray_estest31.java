package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void get_withIndexEqualToLength_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create an array of a specific length.
        int arrayLength = 10;
        AtomicDoubleArray atomicArray = new AtomicDoubleArray(arrayLength);
        
        // The first invalid index is equal to the array's length (valid indices are 0 to 9).
        int outOfBoundsIndex = arrayLength;

        // Act & Assert: Attempt to access the out-of-bounds index and verify the exception.
        try {
            atomicArray.get(outOfBoundsIndex);
            fail("Expected an IndexOutOfBoundsException to be thrown for index " + outOfBoundsIndex);
        } catch (IndexOutOfBoundsException expectedException) {
            // This is the expected behavior.
            // For more robustness, we can also verify the exception message.
            String expectedMessage = "index " + outOfBoundsIndex;
            assertEquals(expectedMessage, expectedException.getMessage());
        }
    }
}