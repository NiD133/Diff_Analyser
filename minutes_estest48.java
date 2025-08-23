package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class MinutesTest {

    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingMinValueToNull() {
        // The isGreaterThan method treats a null argument as a zero value.
        // This test verifies that MIN_VALUE is correctly identified as not being
        // greater than zero.

        // Arrange
        Minutes minValue = Minutes.MIN_VALUE;

        // Act
        boolean result = minValue.isGreaterThan(null);

        // Assert
        assertFalse("Minutes.MIN_VALUE should not be greater than null (which is treated as zero)", result);
    }
}