package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear#get(TemporalField)}.
 */
class DayOfYearGetTest {

    @Test
    void get_forSupportedDayOfYearField_returnsCorrectValue() {
        // Arrange: Create a DayOfYear instance for a specific day.
        int dayValue = 12;
        DayOfYear dayOfYear = DayOfYear.of(dayValue);
        String assertionMessage = "get(DAY_OF_YEAR) should return the day-of-year value the object was created with.";

        // Act: Retrieve the value using the DAY_OF_YEAR field.
        int result = dayOfYear.get(DAY_OF_YEAR);

        // Assert: Verify that the retrieved value matches the original value.
        assertEquals(dayValue, result, assertionMessage);
    }
}