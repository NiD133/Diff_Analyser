package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 * Note: The original test class name 'ArrayFillTestTest17' was unconventional.
 * It has been renamed to 'ArrayFillTest' for clarity.
 */
public class ArrayFillTest extends AbstractLangTest {

    /**
     * Tests that ArrayFill.fill() returns the same null reference when the input array is null.
     * This aligns with the method's contract to return the given array.
     */
    @Test
    void fill_withNullObjectArray_shouldReturnNull() {
        // Arrange
        final Object[] inputArray = null;
        final Object valueToFill = 1;

        // Act
        final Object[] result = ArrayFill.fill(inputArray, valueToFill);

        // Assert
        // The method contract specifies that the given array instance is returned.
        // For a null input, the result must be the same null reference.
        assertSame(inputArray, result);
    }
}