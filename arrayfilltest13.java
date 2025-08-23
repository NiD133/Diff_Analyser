package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ArrayFill} utility class.
 */
// The original class name "ArrayFillTestTest13" was unusual.
// Renaming to "ArrayFillTest" for clarity and adherence to standard naming conventions.
class ArrayFillTest {

    @Test
    void fill_shouldReturnSameInstance_whenIntArrayIsNull() {
        // Arrange
        final int[] nullArray = null;
        final int fillValue = 1; // This value is arbitrary as the array is null.

        // Act
        final int[] result = ArrayFill.fill(nullArray, fillValue);

        // Assert
        // The contract states the input array is returned. For a null input,
        // the result should be the same null reference.
        assertSame(nullArray, result, "The method should return the same null instance it was given.");
    }
}