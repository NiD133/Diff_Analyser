package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for CalendarConverter.
 *
 * Notes:
 * - Uses real JDK types (GregorianCalendar, TimeZone) to keep intent clear.
 * - Avoids time-sensitive assertions (no dependency on "now").
 * - Prefers descriptive test names and Arrange-Act-Assert structure.
 */
public class CalendarConverterTest {

    private CalendarConverter converter;

    @Before
    public void setUp() {
        // Prefer the canonical singleton, but tests also implicitly verify it behaves as expected.
        converter = CalendarConverter.INSTANCE;
    }

    // ----------------------------------------------------------------------
    // Supported type
    // ----------------------------------------------------------------------

    @Test
    public void supportedType_isJavaUtilCalendar() {
        assertEquals(Calendar.class, converter.getSupportedType());
    }

    // ----------------------------------------------------------------------
    // getChronology(Object, Chronology)
    // ----------------------------------------------------------------------

    @Test
    public void getChronology_withProvidedChronology_returnsSameInstance() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        Chronology provided = ISOChronology.getInstanceUTC();

        Chronology actual = converter.getChronology(cal, provided);

        assertSame("Expected the provided chronology to be returned as-is", provided, actual);
    }

    @Test
    public void getChronology_forGregorianCalendar_picksGJChronology_andExtractsCalendarZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar cal = new GregorianCalendar(tz);

        Chronology actual = converter.getChronology(cal, (Chronology) null);

        assertTrue("Expected GJChronology for GregorianCalendar", actual instanceof GJChronology);
        assertEquals("Expected chronology zone to match calendar zone",
                DateTimeZone.forID("UTC"), actual.getZone());
    }

    // ----------------------------------------------------------------------
    // getChronology(Object, DateTimeZone)
    // ----------------------------------------------------------------------

    @Test
    public void getChronology_withZoneOverride_usesOverrideZone_evenForGregorianCalendar() {
        TimeZone calendarZone = TimeZone.getTimeZone("UTC");
        DateTimeZone override = DateTimeZone.forID("Europe/Paris");
        Calendar cal = new GregorianCalendar(calendarZone);

        Chronology actual = converter.getChronology(cal, override);

        assertTrue("Expected GJChronology for GregorianCalendar", actual instanceof GJChronology);
        assertEquals("Expected provided zone override to be used", override, actual.getZone());
    }

    @Test
    public void getChronology_forNonGregorianCalendar_defaultsToISOChronology_andExtractsZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar nonGregorian = new NonGregorianCalendar(tz);

        Chronology actual = converter.getChronology(nonGregorian, (DateTimeZone) null);

        assertTrue("Expected ISOChronology for non-Gregorian Calendar", actual instanceof ISOChronology);
        assertEquals("Expected chronology zone to match calendar zone",
                DateTimeZone.forID("UTC"), actual.getZone());
    }

    // ----------------------------------------------------------------------
    // getInstantMillis(Object, Chronology)
    // ----------------------------------------------------------------------

    @Test
    public void getInstantMillis_returnsCalendarTimeInMillis() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        long expectedMillis = 123_456_789L;
        cal.setTimeInMillis(expectedMillis);

        long actual = converter.getInstantMillis(cal, ISOChronology.getInstanceUTC());

        assertEquals(expectedMillis, actual);
    }

    @Test
    public void getInstantMillis_epochZero_isReturnedAsIs() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(0L);

        long actual = converter.getInstantMillis(cal, ISOChronology.getInstanceUTC());

        assertEquals(0L, actual);
    }

    // ----------------------------------------------------------------------
    // Exceptional cases
    // ----------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void getChronology_withChronologyParam_nullObject_throwsNPE() {
        converter.getChronology(null, ISOChronology.getInstanceUTC());
    }

    @Test(expected = NullPointerException.class)
    public void getChronology_withZoneParam_nullObject_throwsNPE() {
        converter.getChronology(null, DateTimeZone.UTC);
    }

    @Test(expected = NullPointerException.class)
    public void getInstantMillis_nullObject_throwsNPE() {
        converter.getInstantMillis(null, ISOChronology.getInstanceUTC());
    }

    @Test(expected = ClassCastException.class)
    public void getChronology_withChronologyParam_nonCalendarObject_throwsClassCastException() {
        converter.getChronology(new Object(), (Chronology) null);
    }

    @Test(expected = ClassCastException.class)
    public void getChronology_withZoneParam_nonCalendarObject_throwsClassCastException() {
        converter.getChronology(new Object(), DateTimeZone.UTC);
    }

    @Test(expected = ClassCastException.class)
    public void getInstantMillis_nonCalendarObject_throwsClassCastException() {
        converter.getInstantMillis(new Object(), ISOChronology.getInstanceUTC());
    }

    // ----------------------------------------------------------------------
    // Test helper: a minimal non-Gregorian Calendar implementation
    // ----------------------------------------------------------------------

    /**
     * A minimal Calendar subtype that is not a GregorianCalendar. It is sufficient
     * for type-based logic in CalendarConverter; it does not need to be fully functional.
     */
    private static final class NonGregorianCalendar extends Calendar {
        NonGregorianCalendar(TimeZone tz) {
            super(tz);
        }
        @Override protected void computeTime() { /* not used in these tests */ }
        @Override protected void computeFields() { /* not used in these tests */ }
        @Override public void add(int field, int amount) { throw new UnsupportedOperationException(); }
        @Override public void roll(int field, boolean up) { throw new UnsupportedOperationException(); }
        @Override public int getMinimum(int field) { return 0; }
        @Override public int getMaximum(int field) { return 0; }
        @Override public int getGreatestMinimum(int field) { return 0; }
        @Override public int getLeastMaximum(int field) { return 0; }
    }
}