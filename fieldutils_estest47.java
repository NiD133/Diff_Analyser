package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds() does not throw an exception when the value
     * is equal to both the lower and upper bounds. This tests the boundary condition
     * where the valid range consists of a single number.
     */
    @Test
    public void verifyValueBounds_shouldNotThrowException_whenValueIsAtTheBoundary() {
        // Arrange
        final String fieldName = "dayOfMonth";
        final int value = 15;
        final int lowerBound = 15;
        final int upperBound = 15;

        // Act & Assert
        // The method should complete without throwing an exception because the value (15)
        // is within the inclusive bounds [15, 15].
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }
}