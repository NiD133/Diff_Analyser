package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth#range(TemporalField)}.
 */
class DayOfMonthTest {

    @Test
    void range_forDayOfMonthField_returnsCorrectRange() {
        // Arrange: Set up the test objects and expected values.
        DayOfMonth testDayOfMonth = DayOfMonth.of(12);
        ValueRange expectedRange = DAY_OF_MONTH.range();

        // Act: Call the method under test.
        ValueRange actualRange = testDayOfMonth.range(DAY_OF_MONTH);

        // Assert: Verify the result.
        // This assertion confirms that DayOfMonth.range() for the DAY_OF_MONTH field
        // returns the same standard value range as the field itself.
        assertEquals(expectedRange, actualRange);
    }
}