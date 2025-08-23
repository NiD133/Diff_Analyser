package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that the {@code notFinite(double)} method correctly identifies
     * a standard finite number.
     */
    @Test
    public void notFinite_shouldReturnFalse_whenGivenFiniteDouble() {
        // Arrange: A standard, finite double value.
        // The original test used `(double) (byte) (-128)`, which is equivalent to -128.0.
        double finiteDouble = -128.0;

        // Act: Check if the value is considered "not finite".
        boolean isNotFinite = NumberOutput.notFinite(finiteDouble);

        // Assert: The result should be false, as the number is finite.
        assertFalse("A standard number like -128.0 should be considered finite.", isNotFinite);
    }
}