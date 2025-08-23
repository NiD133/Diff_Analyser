package org.threeten.extra;

import org.junit.Test;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DayOfMonth#adjustInto(Temporal)} method.
 */
public class DayOfMonthAdjustIntoTest {

    @Test
    public void adjustInto_shouldSetDayOfMonthOnZonedDateTime() {
        // Arrange: Define the inputs and the expected outcome.
        // A DayOfMonth instance representing the 15th day.
        DayOfMonth dayOfMonthToSet = DayOfMonth.of(15);
        
        // A ZonedDateTime on a different day (the 10th) that will be adjusted.
        ZonedDateTime originalDateTime = ZonedDateTime.of(2023, 1, 10, 12, 0, 0, 0, ZoneOffset.UTC);
        
        // The expected ZonedDateTime after the day has been adjusted to the 15th.
        ZonedDateTime expectedDateTime = ZonedDateTime.of(2023, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);

        // Act: Execute the method under test.
        Temporal adjustedDateTime = dayOfMonthToSet.adjustInto(originalDateTime);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedDateTime, adjustedDateTime);
    }
}