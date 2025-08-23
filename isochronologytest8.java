package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the duration fields of ISOChronology.
 * This test focuses on the name, support, and precision of each duration field.
 */
public class ISOChronologyDurationFieldTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone GMT = DateTimeZone.forID("Etc/GMT");
    private static final DateTimeZone OFFSET_PLUS_1 = DateTimeZone.forOffsetHours(1);

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Save and set default settings to ensure tests are predictable.
        // Using a non-fixed zone like London helps test behavior related to DST.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(LONDON.toTimeZone());
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore original default settings to avoid side-effects on other tests.
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testFieldNames() {
        Chronology iso = ISOChronology.getInstanceUTC();
        assertEquals("eras", iso.eras().getName());
        assertEquals("centuries", iso.centuries().getName());
        assertEquals("years", iso.years().getName());
        assertEquals("weekyears", iso.weekyears().getName());
        assertEquals("months", iso.months().getName());
        assertEquals("weeks", iso.weeks().getName());
        assertEquals("days", iso.days().getName());
        assertEquals("halfdays", iso.halfdays().getName());
        assertEquals("hours", iso.hours().getName());
        assertEquals("minutes", iso.minutes().getName());
        assertEquals("seconds", iso.seconds().getName());
        assertEquals("millis", iso.millis().getName());
    }

    @Test
    public void testFieldSupport() {
        Chronology iso = ISOChronology.getInstanceUTC();
        assertFalse("Eras field should not be supported", iso.eras().isSupported());
        assertTrue("Centuries field should be supported", iso.centuries().isSupported());
        assertTrue("Years field should be supported", iso.years().isSupported());
        assertTrue("Weekyears field should be supported", iso.weekyears().isSupported());
        assertTrue("Months field should be supported", iso.months().isSupported());
        assertTrue("Weeks field should be supported", iso.weeks().isSupported());
        assertTrue("Days field should be supported", iso.days().isSupported());
        assertTrue("Halfdays field should be supported", iso.halfdays().isSupported());
        assertTrue("Hours field should be supported", iso.hours().isSupported());
        assertTrue("Minutes field should be supported", iso.minutes().isSupported());
        assertTrue("Seconds field should be supported", iso.seconds().isSupported());
        assertTrue("Millis field should be supported", iso.millis().isSupported());
    }

    @Test
    public void testPrecision_InZoneWithDaylightSaving() {
        // In a zone with DST like Europe/London, day-based fields are imprecise due to DST transitions.
        // ISOChronology.getInstance() should use the default zone (set to LONDON in setUp).
        Chronology isoDefault = ISOChronology.getInstance();
        assertFieldPrecision(isoDefault, false);

        // Explicitly getting the chronology for the same zone should yield the same result.
        Chronology isoLondon = ISOChronology.getInstance(LONDON);
        assertFieldPrecision(isoLondon, false);
    }

    @Test
    public void testPrecision_InFixedOffsetZones() {
        // For fixed-offset zones (UTC, GMT, +01:00), day-based fields are precise
        // as there are no DST transitions to make their length variable.
        assertFieldPrecision(ISOChronology.getInstance(UTC), true);
        assertFieldPrecision(ISOChronology.getInstance(GMT), true);
        assertFieldPrecision(ISOChronology.getInstance(OFFSET_PLUS_1), true);
    }

    /**
     * Asserts the precision of duration fields for a given chronology.
     *
     * @param chronology The chronology to test.
     * @param areDayFieldsPrecise The expected precision for day-based fields (days, weeks, halfdays),
     *                            which is true for fixed-offset zones and false for zones with DST.
     */
    private void assertFieldPrecision(Chronology chronology, boolean areDayFieldsPrecise) {
        // Time-based fields are always precise.
        assertTrue(chronology.millis().isPrecise());
        assertTrue(chronology.seconds().isPrecise());
        assertTrue(chronology.minutes().isPrecise());
        assertTrue(chronology.hours().isPrecise());

        // Date-based fields with variable lengths (e.g., year, month) are never precise.
        assertFalse(chronology.centuries().isPrecise());
        assertFalse(chronology.years().isPrecise());
        assertFalse(chronology.months().isPrecise());
        assertFalse(chronology.weekyears().isPrecise());

        // Day-based fields are only precise in fixed-offset time zones.
        assertEquals(areDayFieldsPrecise, chronology.halfdays().isPrecise());
        assertEquals(areDayFieldsPrecise, chronology.days().isPrecise());
        assertEquals(areDayFieldsPrecise, chronology.weeks().isPrecise());
    }
}