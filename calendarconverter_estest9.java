package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getChronology correctly infers a GJChronology from a GregorianCalendar
     * and uses the calendar's time zone when a null DateTimeZone is provided.
     */
    @Test
    public void getChronology_fromGregorianCalendarWithNullZone_shouldUseCalendarZone() {
        // Arrange
        final CalendarConverter converter = CalendarConverter.INSTANCE;
        
        // Use a specific, non-default time zone to ensure the test is robust.
        final TimeZone calendarTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
        final Calendar gregorianCalendar = new GregorianCalendar(calendarTimeZone);
        final DateTimeZone expectedZone = DateTimeZone.forTimeZone(calendarTimeZone);

        // Act
        // Pass null for the zone to test that the converter correctly
        // extracts the zone from the Calendar object.
        final Chronology resultChronology = converter.getChronology(gregorianCalendar, null);

        // Assert
        // The converter should create a GJChronology for a GregorianCalendar.
        assertNotNull("The resulting chronology should not be null.", resultChronology);
        assertTrue("The chronology should be an instance of GJChronology.",
                resultChronology instanceof GJChronology);

        // The chronology's time zone should match the one from the input calendar.
        assertEquals("The chronology's time zone should be derived from the calendar.",
                expectedZone, resultChronology.getZone());
    }
}