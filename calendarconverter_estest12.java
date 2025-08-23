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
     * Tests that getChronology() correctly infers the GJChronology and the time zone
     * from a GregorianCalendar when the explicit chronology parameter is null.
     */
    @Test
    public void getChronology_fromGregorianCalendar_returnsGJChronologyWithCorrectZone() {
        // Arrange
        // Use the singleton instance as intended by the class design.
        CalendarConverter converter = CalendarConverter.INSTANCE;

        // Create a calendar with a specific, non-default time zone to make the test robust.
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar inputCalendar = new GregorianCalendar(timeZone);
        DateTimeZone expectedZone = DateTimeZone.forTimeZone(timeZone);

        // Act
        // Pass null for the chronology to trigger the inference logic under test.
        Chronology actualChronology = converter.getChronology(inputCalendar, null);

        // Assert
        // Verify that the converter returned a non-null chronology.
        assertNotNull("The resulting chronology should not be null.", actualChronology);

        // Verify that the correct type of chronology was inferred for a GregorianCalendar.
        assertTrue(
            "The chronology should be an instance of GJChronology.",
            actualChronology instanceof GJChronology
        );

        // Verify that the chronology uses the correct time zone from the input calendar.
        assertEquals(
            "The chronology's time zone should match the calendar's time zone.",
            expectedZone,
            actualChronology.getZone()
        );
    }
}