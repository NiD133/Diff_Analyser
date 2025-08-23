package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill} focusing on null input handling.
 */
public class ArrayFillTestTest8 extends AbstractLangTest {

    /**
     * Tests that ArrayFill.fill() is null-safe and returns null
     * when a null double array is provided.
     */
    @Test
    void testFill_withNullDoubleArray_shouldReturnNull() {
        // Arrange
        final double[] nullArray = null;
        // The value to fill is irrelevant for this test case as the array is null.
        final double valueToFill = 1.0;

        // Act
        final double[] result = ArrayFill.fill(nullArray, valueToFill);

        // Assert
        assertNull(result, "The method should return null when the input array is null.");
    }
}