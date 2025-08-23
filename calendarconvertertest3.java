package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.joda.time.Chronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;

/**
 * Unit tests for {@link CalendarConverter#getInstantMillis(Object, Chronology)}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getInstantMillis() correctly extracts the millisecond value from a Calendar
     * instance, ignoring the provided Chronology. It also verifies that the original
     * Calendar object is not modified during the conversion.
     */
    @Test
    public void getInstantMillis_shouldReturnMillisFromCalendarAndIgnoreChronology() {
        // Arrange
        long expectedMillis = 123L;
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(expectedMillis);

        // The converter is expected to ignore this Chronology and simply return
        // the millis from the Calendar object itself.
        Chronology ignoredChronology = JulianChronology.getInstance();
        
        // Act
        long actualMillis = CalendarConverter.INSTANCE.getInstantMillis(calendar, ignoredChronology);

        // Assert
        // 1. The returned value should be the millisecond instant from the calendar.
        assertEquals(expectedMillis, actualMillis);

        // 2. The original Calendar object should not be modified (no side effects).
        assertEquals("The converter should not modify the original calendar object.",
                expectedMillis, calendar.getTimeInMillis());
    }
}