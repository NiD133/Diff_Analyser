package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 */
// Renamed from ISO8601UtilsTestTest8 for clarity and convention.
public class ISO8601UtilsTest {

    /**
     * Creates a {@link GregorianCalendar} instance for the UTC time zone.
     * The calendar is cleared to ensure it represents the epoch (January 1, 1970, 00:00:00).
     *
     * @return A cleared, UTC-based GregorianCalendar.
     */
    private static GregorianCalendar createUtcCalendar() {
        // Use UTC to prevent the system's default time zone from affecting the test.
        TimeZone utc = TimeZone.getTimeZone("UTC");
        GregorianCalendar calendar = new GregorianCalendar(utc);
        // A new GregorianCalendar is initialized with the current time.
        // We must clear it to avoid unexpected field values from the current time.
        calendar.clear();
        return calendar;
    }

    @Test
    public void parse_withNonFullHourTimezoneOffset_returnsCorrectUtcDate() throws ParseException {
        // Arrange
        // A date-time string with a timezone offset that is not a full hour (-02:58).
        // Such offsets are rare but valid according to the ISO 8601 standard.
        String dateStringWithUncommonOffset = "2018-06-25T00:02:00-02:58";

        // Act
        Date actualDate = ISO8601Utils.parse(dateStringWithUncommonOffset, new ParsePosition(0));

        // Assert
        // The expected time in UTC is calculated by adding the offset to the local time:
        // 00:02:00 + 02:58 offset = 03:00:00 UTC on the same day.
        GregorianCalendar expectedCalendar = createUtcCalendar();
        expectedCalendar.set(Calendar.YEAR, 2018);
        expectedCalendar.set(Calendar.MONTH, Calendar.JUNE);
        expectedCalendar.set(Calendar.DAY_OF_MONTH, 25);
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 3);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0); // Be explicit for clarity

        Date expectedDate = expectedCalendar.getTime();

        assertThat(actualDate).isEqualTo(expectedDate);
    }
}