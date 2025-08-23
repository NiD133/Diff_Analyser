package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * This test suite is for the {@link DayOfYear} class.
 * It focuses on improving the understandability of tests related to the range of temporal fields.
 */
public class DayOfYearImprovedTest {

    /**
     * Tests that the {@code range(TemporalField)} method returns the correct
     * value range for the {@code DAY_OF_YEAR} field.
     * The valid range for DAY_OF_YEAR is from 1 to 366, inclusive,
     * as DayOfYear can represent days in a leap year.
     */
    @Test
    public void testRangeForDayOfYearFieldReturnsCorrectRange() {
        // Arrange: Create a DayOfYear instance and define the expected range.
        // The specific day (e.g., 45) does not affect the range of the DAY_OF_YEAR field.
        DayOfYear dayOfYear = DayOfYear.of(45);
        ValueRange expectedRange = ValueRange.of(1, 366);

        // Act: Get the actual range for the DAY_OF_YEAR field.
        ValueRange actualRange = dayOfYear.range(ChronoField.DAY_OF_YEAR);

        // Assert: Verify that the actual range matches the expected range.
        assertEquals("The range for DAY_OF_YEAR should be 1-366", expectedRange, actualRange);
    }
}