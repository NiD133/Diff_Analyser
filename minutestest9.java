package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void getFieldType_returnsMinutesType() {
        // Arrange
        Minutes testMinutes = Minutes.minutes(20);
        DurationFieldType expectedType = DurationFieldType.minutes();

        // Act
        DurationFieldType actualType = testMinutes.getFieldType();

        // Assert
        assertEquals(expectedType, actualType);
    }
}