package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue returns the minimum value when the current value
     * is already the minimum and the wrap amount is zero. This is a test for a
     * basic boundary condition.
     */
    @Test
    public void getWrappedValue_withValueAtMinimumAndZeroWrap_shouldReturnMinimum() {
        // Arrange
        int currentValue = -436;
        int wrapValue = 0;
        int minValue = -436;
        int maxValue = 0;
        int expectedValue = -436;

        // Act
        int actualValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}