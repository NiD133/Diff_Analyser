package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range() method in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void range_forDayOfYear_shouldReturnFullValidRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // The International Fixed calendar has standard years of 365 days and leap years of 366 days.
        // The range() method for DAY_OF_YEAR should return the full possible range, which is 1 to 366.
        ValueRange expectedRange = ValueRange.of(1, 366);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_YEAR);

        // Assert
        assertEquals("The range for DAY_OF_YEAR should encompass both normal and leap years.",
                expectedRange, actualRange);
    }
}