package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    /**
     * Tests that ArrayFill.fill() returns the same null array instance it was given.
     * The contract states the method returns "the given array", so a null input
     * should result in a null output.
     */
    @Test
    void fillFloatArray_withNullArray_shouldReturnNull() {
        // Arrange
        final float[] inputArray = null;
        final float fillValue = 1.0f; // This value is required by the method signature but irrelevant for a null array.

        // Act
        final float[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert
        // The method should return the exact same instance that was passed in.
        assertSame(inputArray, resultArray, "The returned array should be the same instance as the input array.");
    }
}