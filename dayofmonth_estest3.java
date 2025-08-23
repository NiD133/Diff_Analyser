package org.threeten.extra;

import org.junit.Test;

import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range() method of the DayOfMonth class.
 */
public class DayOfMonthTest {

    /**
     * Verifies that the range() method returns the correct, fixed range for the DAY_OF_MONTH field.
     */
    @Test
    public void range_forDayOfMonthField_returnsCorrectRange() {
        // Arrange: Create an arbitrary DayOfMonth instance. The specific value does not affect the range.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        ValueRange expectedRange = ValueRange.of(1, 31);

        // Act: Get the range for the DAY_OF_MONTH field.
        ValueRange actualRange = dayOfMonth.range(ChronoField.DAY_OF_MONTH);

        // Assert: The valid range for a day of the month is always 1 to 31.
        assertEquals("The range for DAY_OF_MONTH should be 1-31", expectedRange, actualRange);
    }
}