package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;
import org.junit.Assume;

import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CalendarConverter#getChronology(Object, DateTimeZone)}.
 */
public class CalendarConverterGetChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");

    private final CalendarConverter converter = CalendarConverter.INSTANCE;

    @Test
    public void getChronology_forGregorianCalendar_shouldUseZoneFromParameterOverCalendarsZone() {
        // Arrange
        Calendar calendarInParis = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology expectedChronology = GJChronology.getInstance(MOSCOW);

        // Act
        Chronology actualChronology = converter.getChronology(calendarInParis, MOSCOW);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forGregorianCalendar_whenZoneIsNull_shouldUseChronologyWithDefaultZone() {
        // Arrange
        Calendar calendarInMoscow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // When the zone parameter is null, the converter should use the default time zone.
        Chronology expectedChronology = GJChronology.getInstance(DateTimeZone.getDefault());

        // Act
        Chronology actualChronology = converter.getChronology(calendarInMoscow, (DateTimeZone) null);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forGregorianCalendar_shouldRespectGregorianChangeDate() {
        // Arrange
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendar.setGregorianChange(new Date(0L)); // Set cutover to the epoch

        // A specific GJChronology is expected, with the cutover instant and minimum days in the first week.
        Chronology expectedChronology = GJChronology.getInstance(MOSCOW, 0L, 4);

        // Act
        Chronology actualChronology = converter.getChronology(calendar, MOSCOW);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forGregorianCalendar_withMaxCutover_shouldReturnJulianChronology() {
        // Arrange
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // A cutover date at Long.MAX_VALUE effectively means the calendar is always Julian.
        calendar.setGregorianChange(new Date(Long.MAX_VALUE));

        Chronology expectedChronology = JulianChronology.getInstance(PARIS);

        // Act
        Chronology actualChronology = converter.getChronology(calendar, PARIS);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forGregorianCalendar_withMinCutover_shouldReturnGregorianChronology() {
        // Arrange
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // A cutover date at Long.MIN_VALUE effectively means the calendar is always Gregorian.
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));

        Chronology expectedChronology = GregorianChronology.getInstance(PARIS);

        // Act
        Chronology actualChronology = converter.getChronology(calendar, PARIS);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forUnknownCalendarType_shouldReturnISOChronology() {
        // Arrange
        // Use a mock calendar type that the converter doesn't specifically handle.
        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        // The converter should fall back to the standard ISOChronology.
        Chronology expectedChronology = ISOChronology.getInstance(PARIS);

        // Act
        Chronology actualChronology = converter.getChronology(unknownCalendar, PARIS);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    public void getChronology_forBuddhistCalendar_shouldReturnBuddhistChronology() throws Exception {
        // This test depends on the presence of a specific class in the JDK.
        // We use JUnit's Assume to skip the test if the class is not available.
        Calendar buddhistCalendar;
        try {
            Class<?> buddhistCalendarClass = Class.forName("sun.util.BuddhistCalendar");
            Constructor<?> constructor = buddhistCalendarClass.getConstructor();
            buddhistCalendar = (Calendar) constructor.newInstance();
        } catch (ClassNotFoundException | ReflectiveOperationException e) {
            Assume.assumeNoException("Skipping test: sun.util.BuddhistCalendar not found or accessible.", e);
            return; // Should not be reached, but satisfies the compiler
        }

        // Arrange
        buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology expectedChronology = BuddhistChronology.getInstance(PARIS);

        // Act
        Chronology actualChronology = converter.getChronology(buddhistCalendar, PARIS);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    /**
     * A mock Calendar class used to test the converter's fallback behavior for
     * unknown calendar types. It extends GregorianCalendar so we don't have to
     * implement all of Calendar's abstract methods. The converter's logic is
     * expected to check for specific class types, not just `instanceof`.
     */
    private static class MockUnknownCalendar extends GregorianCalendar {
        MockUnknownCalendar(TimeZone zone) {
            super(zone);
        }
    }
}