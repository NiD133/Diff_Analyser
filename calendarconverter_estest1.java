package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.junit.Test;

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
     * and preserves the calendar's time zone when no explicit chronology is provided.
     */
    @Test
    public void getChronology_forGregorianCalendar_returnsGJChronologyWithCorrectZone() {
        // Arrange
        // Use the singleton instance as recommended by the class design.
        final CalendarConverter converter = CalendarConverter.INSTANCE;

        // Create a standard GregorianCalendar with a specific, non-default time zone.
        final TimeZone jdkTimeZone = TimeZone.getTimeZone("America/New_York");
        final GregorianCalendar calendar = new GregorianCalendar(jdkTimeZone);
        final DateTimeZone expectedZone = DateTimeZone.forTimeZone(jdkTimeZone);

        // Act
        // Pass null for the chronology to let the converter determine it from the input object.
        final Chronology actualChronology = converter.getChronology(calendar, null);

        // Assert
        // The converter should select GJChronology for a GregorianCalendar input, as per Javadoc.
        assertNotNull("The resulting chronology should not be null.", actualChronology);
        assertTrue("The chronology should be an instance of GJChronology.",
                   actualChronology instanceof GJChronology);

        // The time zone from the calendar should be correctly converted and applied.
        assertEquals("The chronology's time zone should match the calendar's.",
                     expectedZone, actualChronology.getZone());
    }
}