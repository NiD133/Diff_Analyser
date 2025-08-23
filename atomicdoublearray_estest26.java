package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import java.util.function.DoubleUnaryOperator;
import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    @Test
    public void getAndUpdate_withNullFunction_shouldThrowNullPointerException() {
        // Arrange
        // Create an array with a single element; the size and content are not relevant.
        AtomicDoubleArray array = new AtomicDoubleArray(1);
        int aValidIndex = 0;

        // Act & Assert
        // The getAndUpdate method is documented to reject a null updater function.
        // We verify that it throws a NullPointerException as expected.
        assertThrows(
            NullPointerException.class,
            () -> array.getAndUpdate(aValidIndex, (DoubleUnaryOperator) null));
    }
}