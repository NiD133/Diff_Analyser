package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the duration fields of the IslamicChronology.
 * This test focuses on properties like name, support, and precision of fields
 * such as years, months, days, hours, etc.
 */
public class IslamicChronologyDurationFieldTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology ISLAMIC_CHRONOLOGY_UTC = IslamicChronology.getInstanceUTC();

    // A fixed point in time for consistent test results: 2002-06-09
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        // Fix the current time and default zone to ensure tests are repeatable.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore original system state
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testDurationFieldNames() {
        Chronology islamic = IslamicChronology.getInstance();
        assertEquals("eras", islamic.eras().getName());
        assertEquals("centuries", islamic.centuries().getName());
        assertEquals("years", islamic.years().getName());
        assertEquals("weekyears", islamic.weekyears().getName());
        assertEquals("months", islamic.months().getName());
        assertEquals("weeks", islamic.weeks().getName());
        assertEquals("days", islamic.days().getName());
        assertEquals("halfdays", islamic.halfdays().getName());
        assertEquals("hours", islamic.hours().getName());
        assertEquals("minutes", islamic.minutes().getName());
        assertEquals("seconds", islamic.seconds().getName());
        assertEquals("millis", islamic.millis().getName());
    }

    @Test
    public void testDurationFieldSupported() {
        Chronology islamic = IslamicChronology.getInstance();
        assertFalse("Eras field should not be supported", islamic.eras().isSupported());
        assertTrue("Centuries field should be supported", islamic.centuries().isSupported());
        assertTrue("Years field should be supported", islamic.years().isSupported());
        assertTrue("Weekyears field should be supported", islamic.weekyears().isSupported());
        assertTrue("Months field should be supported", islamic.months().isSupported());
        assertTrue("Weeks field should be supported", islamic.weeks().isSupported());
        assertTrue("Days field should be supported", islamic.days().isSupported());
        assertTrue("Halfdays field should be supported", islamic.halfdays().isSupported());
        assertTrue("Hours field should be supported", islamic.hours().isSupported());
        assertTrue("Minutes field should be supported", islamic.minutes().isSupported());
        assertTrue("Seconds field should be supported", islamic.seconds().isSupported());
        assertTrue("Millis field should be supported", islamic.millis().isSupported());
    }

    @Test
    public void testDurationFieldPrecisionInZoneWithDST() {
        // For a zone with Daylight Saving Time (like London), date-based fields are imprecise
        // because the length of a day can vary (e.g., 23 or 25 hours).
        Chronology islamic = IslamicChronology.getInstance(LONDON);

        // Date-based fields are imprecise
        assertFalse(islamic.centuries().isPrecise());
        assertFalse(islamic.years().isPrecise());
        assertFalse(islamic.weekyears().isPrecise());
        assertFalse(islamic.months().isPrecise());
        assertFalse(islamic.weeks().isPrecise());
        assertFalse(islamic.days().isPrecise());
        assertFalse(islamic.halfdays().isPrecise());

        // Time-based fields are always precise
        assertTrue(islamic.hours().isPrecise());
        assertTrue(islamic.minutes().isPrecise());
        assertTrue(islamic.seconds().isPrecise());
        assertTrue(islamic.millis().isPrecise());
    }

    @Test
    public void testDurationFieldPrecisionInUTC() {
        // In UTC, there is no Daylight Saving, so a day is always exactly 24 hours.
        // This makes date-based fields (days, weeks) precise.
        // Larger fields (months, years) are still imprecise due to variable lengths.
        Chronology islamicUTC = ISLAMIC_CHRONOLOGY_UTC;

        // Larger, variable-length fields are imprecise
        assertFalse(islamicUTC.centuries().isPrecise());
        assertFalse(islamicUTC.years().isPrecise());
        assertFalse(islamicUTC.weekyears().isPrecise());
        assertFalse(islamicUTC.months().isPrecise());

        // Fields with a fixed millisecond duration are precise in UTC
        assertTrue(islamicUTC.weeks().isPrecise());
        assertTrue(islamicUTC.days().isPrecise());
        assertTrue(islamicUTC.halfdays().isPrecise());
        assertTrue(islamicUTC.hours().isPrecise());
        assertTrue(islamicUTC.minutes().isPrecise());
        assertTrue(islamicUTC.seconds().isPrecise());
        assertTrue(islamicUTC.millis().isPrecise());
    }

    @Test
    public void testDurationFieldPrecisionInFixedOffsetZone() {
        // Any fixed-offset zone (like GMT) behaves like UTC regarding field precision.
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        Chronology islamicGMT = IslamicChronology.getInstance(gmt);

        assertFalse(islamicGMT.centuries().isPrecise());
        assertFalse(islamicGMT.years().isPrecise());
        assertFalse(islamicGMT.weekyears().isPrecise());
        assertFalse(islamicGMT.months().isPrecise());

        assertTrue(islamicGMT.weeks().isPrecise());
        assertTrue(islamicGMT.days().isPrecise());
        assertTrue(islamicGMT.halfdays().isPrecise());
        assertTrue(islamicGMT.hours().isPrecise());
        assertTrue(islamicGMT.minutes().isPrecise());
        assertTrue(islamicGMT.seconds().isPrecise());
        assertTrue(islamicGMT.millis().isPrecise());
    }
}