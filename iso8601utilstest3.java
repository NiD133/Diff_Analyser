package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 * This class focuses on formatting dates with specific timezones.
 */
public class ISO8601UtilsTest {

    @Test
    public void format_withTimeZone_producesCorrectOffsetString() {
        // Arrange
        // Use a specific timezone to test non-UTC formatting.
        // Brazil/East was UTC-3 at this time, providing a clear offset to verify.
        TimeZone brazilEastTimeZone = TimeZone.getTimeZone("Brazil/East");

        // Create a specific date in UTC to represent a fixed point in time.
        // This avoids magic numbers and makes the input date explicit.
        // The time is 18:06:16.870 in UTC.
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(2018, Calendar.JUNE, 28, 18, 6, 16);
        calendar.set(Calendar.MILLISECOND, 870);
        Date inputDate = calendar.getTime();

        // The expected string represents the UTC time formatted for the Brazil/East timezone (UTC-3).
        // 18:06 (UTC) becomes 15:06 (UTC-3).
        String expectedFormattedString = "2018-06-28T15:06:16.870-03:00";

        // Act
        // Format the date, including milliseconds, using the specified timezone.
        String actualFormattedString = ISO8601Utils.format(inputDate, true, brazilEastTimeZone);

        // Assert
        assertThat(actualFormattedString).isEqualTo(expectedFormattedString);
    }
}