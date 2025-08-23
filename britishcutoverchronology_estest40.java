package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests the valid range for the DAY_OF_YEAR field.
     *
     * The British calendar skipped 11 days in the cutover year of 1752,
     * making that year only 355 days long. This represents the smallest possible
     * maximum for the day-of-year field. In a regular leap year, the maximum is 366.
     * The test verifies that the range correctly reflects these constraints.
     */
    @Test
    public void range_forDayOfYear_returnsCorrectRangeForCutover() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        // The expected range is 1 to {355, 366}, where 355 is the length of the cutover year (1752)
        // and 366 is the length of a leap year.
        ValueRange expectedRange = ValueRange.of(1, 355, 366);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_YEAR);

        // Assert
        assertEquals("The range for DAY_OF_YEAR should account for the short cutover year",
                expectedRange, actualRange);
    }
}