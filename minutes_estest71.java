package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void getPeriodType_shouldReturnPeriodTypeWithOnlyMinutesField() {
        // Arrange
        // The specific value of Minutes does not affect the PeriodType.
        // We use a simple, representative instance.
        Minutes testMinutes = Minutes.ONE;

        // Act
        PeriodType periodType = testMinutes.getPeriodType();

        // Assert
        // The PeriodType for a Minutes object should contain exactly one field.
        assertEquals("PeriodType should have one field", 1, periodType.size());
        
        // That single field must be of type 'minutes'.
        assertEquals("Field type should be 'minutes'", DurationFieldType.minutes(), periodType.getFieldType(0));
    }
}