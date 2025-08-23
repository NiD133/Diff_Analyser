package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// The original class name is kept to show the context of the improvement.
public class CalendarConverter_ESTestTest13 extends CalendarConverter_ESTest_scaffolding {

    /**
     * Tests that getChronology correctly infers the chronology and time zone
     * from a Calendar object when a specific DateTimeZone is not provided.
     */
    @Test
    public void getChronologyWithNullZoneShouldReturnChronologyBasedOnCalendar() {
        // Arrange: Create a converter and a Calendar with a specific, non-default time zone
        // to ensure the test is deterministic.
        CalendarConverter converter = new CalendarConverter();
        TimeZone specificTimeZone = TimeZone.getTimeZone("America/New_York");
        Calendar calendar = new GregorianCalendar(specificTimeZone);
        DateTimeZone expectedZone = DateTimeZone.forTimeZone(specificTimeZone);

        // Act: Call getChronology with a null zone, which should cause the converter
        // to use the zone from the Calendar object.
        Chronology actualChronology = converter.getChronology(calendar, (DateTimeZone) null);

        // Assert: Verify that the returned chronology is of the correct type and has the correct time zone.
        // For a GregorianCalendar, the converter should return a GJChronology.
        assertTrue(
            "The returned chronology should be an instance of GJChronology for a GregorianCalendar input.",
            actualChronology instanceof GJChronology
        );

        // The chronology's time zone should match the one from the input calendar.
        assertEquals(
            "The chronology's time zone should be inferred from the calendar.",
            expectedZone,
            actualChronology.getZone()
        );
    }
}