package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the getPeriodType() method returns the correct, seconds-only period type.
     */
    @Test
    public void testGetPeriodType() {
        // Arrange
        Seconds secondsInstance = Seconds.seconds(20);
        PeriodType expectedType = PeriodType.seconds();

        // Act
        PeriodType actualType = secondsInstance.getPeriodType();

        // Assert
        assertEquals("A Seconds object must have a PeriodType of 'seconds'", expectedType, actualType);
    }
}