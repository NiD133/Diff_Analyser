package com.google.common.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that calling set() with an index that is out of the array's bounds
     * throws an IndexOutOfBoundsException.
     */
    @Test
    public void set_withOutOfBoundsIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create an array of a small, known size.
        AtomicDoubleArray array = new AtomicDoubleArray(10);
        int outOfBoundsIndex = 15;
        double valueToSet = 99.9;

        // Act & Assert: Attempt to set a value at the out-of-bounds index and
        // verify that the correct exception is thrown.
        IndexOutOfBoundsException thrown = assertThrows(
            IndexOutOfBoundsException.class,
            () -> array.set(outOfBoundsIndex, valueToSet)
        );

        // Optional but recommended: Verify the exception message for more precise testing.
        String expectedMessageFragment = String.valueOf(outOfBoundsIndex);
        assertTrue(
            "Exception message should contain the out-of-bounds index",
            thrown.getMessage().contains(expectedMessageFragment)
        );
    }
}