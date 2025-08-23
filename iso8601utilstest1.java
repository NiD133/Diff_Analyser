package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

    @Test
    public void format_dateOnly_isFormattedAsUtcStartOfDay() {
        // Arrange
        // Create a calendar in UTC to avoid timezone-related test flakiness.
        Calendar calendar = new GregorianCalendar(UTC_TIMEZONE);
        // A new calendar instance starts with the current time, so clear it.
        calendar.clear();
        calendar.set(2018, Calendar.JUNE, 25);
        Date dateToFormat = calendar.getTime();

        // Act
        String actualFormattedDate = ISO8601Utils.format(dateToFormat);

        // Assert
        // The format method should produce a full ISO 8601 date-time string in UTC ('Z'),
        // with time fields set to zero because they were not specified.
        String expectedFormattedDate = "2018-06-25T00:00:00Z";
        assertThat(actualFormattedDate).isEqualTo(expectedFormattedDate);
    }
}