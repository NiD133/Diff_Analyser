package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue returns the value itself when it is already
     * within the specified range, specifically when it equals the maximum value.
     */
    @Test
    public void getWrappedValue_shouldReturnSameValue_whenValueIsAtMaximumBoundary() {
        // Arrange
        final int value = 0;
        final int minValue = -3236;
        final int maxValue = 0; // The value is at the maximum boundary of the range [-3236, 0]

        // Act
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);

        // Assert
        assertEquals(value, wrappedValue);
    }
}