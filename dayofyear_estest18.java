package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link DayOfYear#adjustInto(Temporal)} method.
 */
public class DayOfYearTest {

    @Test
    public void adjustInto_forDay366OnNonLeapYear_throwsDateTimeException() {
        // Arrange: Define the 366th day of the year and a date within a non-leap year.
        // Day 366 is only valid in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        // The year 2014 is explicitly chosen as it is not a leap year.
        ZonedDateTime dateInNonLeapYear = ZonedDateTime.of(2014, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        // Act & Assert: Verify that adjusting a date in a non-leap year to day 366
        // throws a DateTimeException with a specific, informative message.
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> day366.adjustInto(dateInNonLeapYear)
        );

        assertEquals("Invalid date 'DayOfYear 366' as '2014' is not a leap year", thrown.getMessage());
    }
}