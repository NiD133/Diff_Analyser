package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Minutes} class, focusing on comparison logic.
 */
public class MinutesTest {

    /**
     * Tests that isGreaterThan() returns false when comparing a zero-minute instance to null.
     * The Joda-Time API specifies that a null Minutes object is treated as zero.
     * Therefore, this test is equivalent to checking if zero is greater than zero.
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingZeroToNull() {
        // Arrange
        // The minutesIn(null) factory method returns a Minutes object representing zero.
        Minutes zeroMinutes = Minutes.minutesIn(null);

        // Act
        // Comparing to a null Minutes object, which is treated as zero.
        boolean isGreater = zeroMinutes.isGreaterThan(null);

        // Assert
        assertFalse("Zero minutes should not be greater than null (which is treated as zero).", isGreater);
    }
}