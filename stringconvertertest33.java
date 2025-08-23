package org.joda.time.convert;

import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;

/**
 * Test class for StringConverter.
 * This test focuses on setting an interval from a String.
 */
public class TestStringConverter extends TestCase {

    private static final DateTimeZone SIX = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);

    private DateTimeZone originalDefaultZone = null;
    private Locale originalDefaultLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestStringConverter.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Save and set default zone and locale for test consistency
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original default zone and locale
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
        originalDefaultZone = null;
        originalDefaultLocale = null;
    }

    /**
     * Tests that when parsing an interval string (like "Period/DateTime") with a null
     * chronology parameter, the converter:
     * 1. Parses the DateTime and its time zone from the string.
     * 2. Uses the application's *default* chronology for the period calculation.
     * 3. Sets the interval's final chronology to the *default* chronology.
     */
    public void testSetInto_intervalFromStringWithPeriodAndEnd_usesDefaultChronology() {
        // Given: An interval string with a period and an end time containing a time zone.
        // The default time zone is set to LONDON in setUp().
        final MutableInterval interval = new MutableInterval();
        final String intervalStr = "P1Y2M/2004-06-09T+06:00";
        final Chronology defaultChronology = ISO_LONDON;

        // When: The converter sets the interval's value from the string, with a null chronology.
        StringConverter.INSTANCE.setInto(interval, intervalStr, null);

        // Then: The interval should be correctly calculated and configured.

        // 1. The end instant is determined by the date-time part of the string.
        final DateTimeZone zoneFromString = SIX;
        final DateTime expectedEndInstant = new DateTime(2004, 6, 9, 0, 0, 0, 0, zoneFromString);

        // 2. The start instant is calculated by subtracting the period from the end instant.
        // This calculation correctly uses the default chronology (ISO London) rather than the
        // chronology from the string (ISO +06:00).
        final DateTime expectedStartInstant = new DateTime(2003, 4, 9, 0, 0, 0, 0, zoneFromString);

        // 3. The interval's own chronology is set to the default chronology.
        assertEquals("Interval chronology should be the default", defaultChronology, interval.getChronology());

        // 4. Verify the start and end millisecond instants are correct.
        assertEquals("Start millis should be correct", expectedStartInstant.getMillis(), interval.getStartMillis());
        assertEquals("End millis should be correct", expectedEndInstant.getMillis(), interval.getEndMillis());

        // 5. Verify the complete DateTime objects returned by the interval.
        // These will be constructed with the correct millis and the interval's final chronology.
        DateTime expectedStartWithIntervalChronology = new DateTime(expectedStartInstant.getMillis(), defaultChronology);
        DateTime expectedEndWithIntervalChronology = new DateTime(expectedEndInstant.getMillis(), defaultChronology);

        assertEquals("getStart() should return correct DateTime", expectedStartWithIntervalChronology, interval.getStart());
        assertEquals("getEnd() should return correct DateTime", expectedEndWithIntervalChronology, interval.getEnd());
    }
}