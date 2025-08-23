package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void getPeriodType_shouldReturnMinutesType() {
        // Arrange
        Minutes testMinutes = Minutes.minutes(20);
        PeriodType expectedType = PeriodType.minutes();

        // Act
        PeriodType actualType = testMinutes.getPeriodType();

        // Assert
        assertEquals("The period type of a Minutes object must be 'minutes'.", expectedType, actualType);
    }
}