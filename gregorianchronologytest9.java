package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for the duration fields of GregorianChronology.
 */
public class GregorianChronologyDurationFieldTest {

    private static final Chronology GREGORIAN_DEFAULT_ZONE = GregorianChronology.getInstance();
    private static final Chronology GREGORIAN_UTC = GregorianChronology.getInstanceUTC();
    // A zoned chronology instance to explicitly test behavior with Daylight Saving Time.
    private static final Chronology GREGORIAN_LONDON = GregorianChronology.getInstance(DateTimeZone.forID("Europe/London"));

    @Test
    public void testFieldNames() {
        assertEquals("eras", GREGORIAN_DEFAULT_ZONE.eras().getName());
        assertEquals("centuries", GREGORIAN_DEFAULT_ZONE.centuries().getName());
        assertEquals("years", GREGORIAN_DEFAULT_ZONE.years().getName());
        assertEquals("weekyears", GREGORIAN_DEFAULT_ZONE.weekyears().getName());
        assertEquals("months", GREGORIAN_DEFAULT_ZONE.months().getName());
        assertEquals("weeks", GREGORIAN_DEFAULT_ZONE.weeks().getName());
        assertEquals("days", GREGORIAN_DEFAULT_ZONE.days().getName());
        assertEquals("halfdays", GREGORIAN_DEFAULT_ZONE.halfdays().getName());
        assertEquals("hours", GREGORIAN_DEFAULT_ZONE.hours().getName());
        assertEquals("minutes", GREGORIAN_DEFAULT_ZONE.minutes().getName());
        assertEquals("seconds", GREGORIAN_DEFAULT_ZONE.seconds().getName());
        assertEquals("millis", GREGORIAN_DEFAULT_ZONE.millis().getName());
    }

    @Test
    public void testFieldSupported() {
        // Eras are not supported in Joda-Time's core chronologies.
        assertFalse(GREGORIAN_DEFAULT_ZONE.eras().isSupported());

        assertTrue(GREGORIAN_DEFAULT_ZONE.centuries().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.years().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.weekyears().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.months().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.weeks().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.days().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.halfdays().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.hours().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.minutes().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.seconds().isSupported());
        assertTrue(GREGORIAN_DEFAULT_ZONE.millis().isSupported());
    }

    @Test
    public void testFieldPrecision_InZoneWithDST() {
        // In a time zone with DST, larger duration fields are imprecise because their
        // exact millisecond length can vary (e.g., a "day" can be 23 or 25 hours long).
        assertFalse(GREGORIAN_LONDON.centuries().isPrecise());
        assertFalse(GREGORIAN_LONDON.years().isPrecise());
        assertFalse(GREGORIAN_LONDON.weekyears().isPrecise());
        assertFalse(GREGORIAN_LONDON.months().isPrecise());
        assertFalse(GREGORIAN_LONDON.weeks().isPrecise());
        assertFalse(GREGORIAN_LONDON.days().isPrecise());
        assertFalse(GREGORIAN_LONDON.halfdays().isPrecise());

        // Smaller fields have a fixed millisecond duration and are always precise.
        assertTrue(GREGORIAN_LONDON.hours().isPrecise());
        assertTrue(GREGORIAN_LONDON.minutes().isPrecise());
        assertTrue(GREGORIAN_LONDON.seconds().isPrecise());
        assertTrue(GREGORIAN_LONDON.millis().isPrecise());
    }

    @Test
    public void testFieldPrecision_InUTC() {
        // In UTC, there are no DST transitions, so fields from 'days' downwards have a fixed
        // millisecond length and are considered precise.
        assertTrue(GREGORIAN_UTC.weeks().isPrecise());
        assertTrue(GREGORIAN_UTC.days().isPrecise());
        assertTrue(GREGORIAN_UTC.halfdays().isPrecise());
        assertTrue(GREGORIAN_UTC.hours().isPrecise());
        assertTrue(GREGORIAN_UTC.minutes().isPrecise());
        assertTrue(GREGORIAN_UTC.seconds().isPrecise());
        assertTrue(GREGORIAN_UTC.millis().isPrecise());

        // Year, month, and century lengths still vary due to leap years, so they remain imprecise.
        assertFalse(GREGORIAN_UTC.centuries().isPrecise());
        assertFalse(GREGORIAN_UTC.years().isPrecise());
        assertFalse(GREGORIAN_UTC.weekyears().isPrecise());
        assertFalse(GREGORIAN_UTC.months().isPrecise());
    }
}