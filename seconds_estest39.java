package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Seconds} class, focusing on comparison logic.
 */
public class SecondsTest {

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingPositiveValueToNull() {
        // The contract of isLessThan() states that a null argument is treated as Seconds.ZERO.
        // This test verifies that a positive Seconds value is not considered "less than" null.

        // Arrange
        Seconds positiveSeconds = Seconds.MAX_VALUE;

        // Act
        boolean result = positiveSeconds.isLessThan(null);

        // Assert
        assertFalse("A positive Seconds value should not be less than null (which is treated as zero).", result);
    }
}