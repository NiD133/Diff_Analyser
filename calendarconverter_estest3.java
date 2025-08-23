package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getInstantMillis() correctly extracts the millisecond instant
     * from a java.util.Calendar object, ignoring the provided Chronology.
     */
    @Test
    public void getInstantMillis_shouldReturnMillisecondsFromCalendar() {
        // Arrange: Create a Calendar instance set to a specific, well-known moment.
        // This makes the test deterministic and its inputs explicit.
        GregorianCalendar calendar = new GregorianCalendar(2004, Calendar.JULY, 9, 12, 30, 0);
        calendar.set(Calendar.MILLISECOND, 500);
        // Set a specific timezone to avoid test failures due to system default settings.
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        long expectedMillis = calendar.getTimeInMillis();
        CalendarConverter converter = CalendarConverter.INSTANCE;

        // Act: Convert the Calendar to milliseconds.
        // The Chronology parameter is ignored by this method, so we pass null to confirm.
        long actualMillis = converter.getInstantMillis(calendar, (Chronology) null);

        // Assert: The converted value should match the original calendar's milliseconds.
        assertEquals(expectedMillis, actualMillis);
    }
}