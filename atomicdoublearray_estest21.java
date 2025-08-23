package com.google.common.util.concurrent;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest {

    /**
     * Verifies that updateAndGet throws a NullPointerException when the updater function is null,
     * as the function is a non-nullable parameter.
     */
    @Test
    public void updateAndGet_whenUpdaterFunctionIsNull_throwsNullPointerException() {
        // Arrange
        AtomicDoubleArray array = new AtomicDoubleArray(1);
        int index = 0;

        // Act & Assert
        // The updaterFunction is specified as non-null, so a NullPointerException is expected.
        assertThrows(NullPointerException.class, () -> array.updateAndGet(index, null));
    }
}