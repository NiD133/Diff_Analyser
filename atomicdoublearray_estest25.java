package com.google.common.util.concurrent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void lazySet_withIndexEqualToLength_shouldThrowIndexOutOfBoundsException() {
        // Arrange
        final int arrayLength = 10;
        final int outOfBoundsIndex = arrayLength; // The first invalid index is equal to the length.
        AtomicDoubleArray array = new AtomicDoubleArray(arrayLength);

        // Act & Assert
        try {
            array.lazySet(outOfBoundsIndex, 123.45);
            fail("Expected an IndexOutOfBoundsException because the index was out of bounds.");
        } catch (IndexOutOfBoundsException expected) {
            // The exception message is specified by the underlying AtomicLongArray.
            // We verify it to ensure the correct exception is propagated.
            assertEquals("index " + outOfBoundsIndex, expected.getMessage());
        }
    }
}