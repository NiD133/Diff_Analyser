package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue throws an IllegalArgumentException when the
     * minimum value is greater than the maximum value, which is an invalid range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getWrappedValue_shouldThrowException_whenMinIsGreaterThanMax() {
        // Given an invalid range where the minimum value is greater than the maximum.
        int minValue = 1363;
        int maxValue = -3977;

        // The actual value and wrap amount are irrelevant for this test condition.
        int anyCurrentValue = 823;
        int anyWrapValue = 317351877;

        // When getWrappedValue is called with this invalid range
        FieldUtils.getWrappedValue(anyCurrentValue, anyWrapValue, minValue, maxValue);

        // Then an IllegalArgumentException is expected, as specified by the @Test annotation.
    }
}