package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class CalendarConverter_ESTestTest10 extends CalendarConverter_ESTest_scaffolding {

    /**
     * Tests that getChronology() returns a GJChronology when given a GregorianCalendar,
     * and that it correctly uses the specified DateTimeZone, overriding the calendar's own zone.
     */
    @Test
    public void getChronologyWithGregorianCalendarShouldReturnGJChronologyWithSpecifiedZone() {
        // Arrange
        CalendarConverter converter = new CalendarConverter();

        // Create a calendar with a specific time zone (UTC) to ensure the converter
        // correctly prioritizes the zone passed as a parameter later.
        Calendar calendarInUTC = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

        // Define the time zone that we expect the resulting chronology to have.
        DateTimeZone expectedZone = DateTimeZone.forID("America/New_York");

        // Act
        // Request the chronology for the calendar, but specify a different time zone.
        Chronology resultChronology = converter.getChronology(calendarInUTC, expectedZone);

        // Assert
        // The converter should return a GJChronology for a standard GregorianCalendar.
        assertNotNull("The resulting chronology should not be null.", resultChronology);
        assertTrue("The chronology should be an instance of GJChronology.", resultChronology instanceof GJChronology);

        // Crucially, the chronology's time zone should be the one we passed to the method,
        // not the original time zone from the Calendar object.
        assertEquals("The chronology's time zone should match the specified zone.", expectedZone, resultChronology.getZone());
    }
}