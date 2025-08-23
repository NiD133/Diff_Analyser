package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for creating a {@link DayOfYear} by parsing a CharSequence.
 * This specifically tests the {@link DayOfYear#from(TemporalAccessor)} method
 * when used as a query for a {@link DateTimeFormatter}.
 */
class DayOfYearFromTest {

    @Test
    void from_shouldCreateDayOfYear_whenUsedAsQueryForFormatter() {
        // Arrange: Define the input string and a formatter that parses the day-of-year ("D").
        String dayOfYearString = "76";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
        DayOfYear expectedDayOfYear = DayOfYear.of(76);

        // Act: Parse the string using the formatter and the DayOfYear::from method reference as a query.
        DayOfYear parsedDayOfYear = formatter.parse(dayOfYearString, DayOfYear::from);

        // Assert: The parsed object should be equal to the expected DayOfYear.
        assertEquals(expectedDayOfYear, parsedDayOfYear);
    }
}