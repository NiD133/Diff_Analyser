package org.joda.time.convert;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Unit tests for CalendarConverter.getChronology(Object, Chronology).
 * This suite focuses on how the converter determines the Chronology from a Calendar
 * when no Chronology is explicitly provided.
 */
public class TestCalendarConverterGetChronology extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone MOSCOW = DateTimeZone.forID("Europe/Moscow");

    public void testGetChronology_forGregorianCalendar_returnsGJChronologyWithCalendarZone() {
        // Arrange
        GregorianCalendar calendarInParis = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology expected = GJChronology.getInstance(PARIS);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendarInParis, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forGregorianCalendarWithSpecificCutover_returnsGJChronologyWithCutover() {
        // Arrange
        GregorianCalendar calendarWithCutover = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendarWithCutover.setGregorianChange(new Date(0L)); // 1970-01-01T00:00:00Z
        Chronology expected = GJChronology.getInstance(MOSCOW, 0L, 4);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendarWithCutover, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forGregorianCalendarWithFarFutureCutover_returnsJulianChronology() {
        // Arrange: A cutover date far in the future means the calendar is effectively always Julian.
        GregorianCalendar calendarWithMaxCutover = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendarWithMaxCutover.setGregorianChange(new Date(Long.MAX_VALUE));
        Chronology expected = JulianChronology.getInstance(MOSCOW);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendarWithMaxCutover, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forGregorianCalendarWithFarPastCutover_returnsGregorianChronology() {
        // Arrange: A cutover date far in the past means the calendar is effectively always Gregorian.
        GregorianCalendar calendarWithMinCutover = new GregorianCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        calendarWithMinCutover.setGregorianChange(new Date(Long.MIN_VALUE));
        Chronology expected = GregorianChronology.getInstance(MOSCOW);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendarWithMinCutover, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forGregorianCalendarWithUnknownTimeZone_returnsGJChronologyWithDefaultZone() {
        // Arrange: When the calendar's time zone can't be mapped, Joda-Time should use the default time zone.
        GregorianCalendar calendarWithUnknownZone = new GregorianCalendar(new MockUnknownTimeZone());
        Chronology expected = GJChronology.getInstance(DateTimeZone.getDefault());

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(calendarWithUnknownZone, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forUnknownCalendarType_returnsISOChronologyWithCalendarZone() {
        // Arrange: For unknown Calendar subclasses, the converter should default to ISOChronology
        // but still respect the calendar's time zone.
        Calendar unknownCalendar = new MockUnknownCalendar(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology expected = ISOChronology.getInstance(MOSCOW);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(unknownCalendar, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    public void testGetChronology_forBuddhistCalendar_returnsBuddhistChronology() throws Exception {
        // Arrange: This test uses reflection because sun.util.BuddhistCalendar is not a public API
        // and may not be available on all JDKs (e.g., non-Oracle JDKs or JDK 9+).
        Calendar buddhistCalendar;
        try {
            buddhistCalendar = (Calendar) Class.forName("sun.util.BuddhistCalendar").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // If the class is not found or cannot be instantiated, skip the test.
            System.out.println("Skipping test for BuddhistCalendar: " + e.getMessage());
            return;
        }
        buddhistCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        Chronology expected = BuddhistChronology.getInstance(MOSCOW);

        // Act
        Chronology actual = CalendarConverter.INSTANCE.getChronology(buddhistCalendar, (Chronology) null);

        // Assert
        assertEquals(expected, actual);
    }

    //-----------------------------------------------------------------------
    // Mock implementations to make the test self-contained.
    //-----------------------------------------------------------------------

    /**
     * A mock TimeZone subclass that Joda-Time does not have a direct mapping for.
     */
    private static class MockUnknownTimeZone extends TimeZone {
        @Override
        public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) { return 0; }
        @Override
        public void setRawOffset(int offsetMillis) {}
        @Override
        public int getRawOffset() { return 0; }
        @Override
        public boolean useDaylightTime() { return false; }
        @Override
        public boolean inDaylightTime(Date date) { return false; }
    }

    /**
     * A mock Calendar subclass that the converter does not have a specific rule for,
     * forcing it to fall back to the default (ISOChronology).
     */
    private static class MockUnknownCalendar extends GregorianCalendar {
        public MockUnknownCalendar(TimeZone zone) {
            super(zone);
        }
    }
}