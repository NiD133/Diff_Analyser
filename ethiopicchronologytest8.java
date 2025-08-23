package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the duration fields of {@link EthiopicChronology}.
 * <p>
 * This test class focuses on verifying the name, support status, and precision
 * of each duration field, and how precision is affected by the time zone.
 */
public class EthiopicChronologyDurationFieldsTest {

    private static final DateTimeZone ZONE_LONDON = DateTimeZone.forID("Europe/London");
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology ETHIOPIC_LONDON = EthiopicChronology.getInstance(ZONE_LONDON);

    @Test
    public void testFieldNamesAreCorrect() {
        assertEquals("eras", ETHIOPIC_UTC.eras().getName());
        assertEquals("centuries", ETHIOPIC_UTC.centuries().getName());
        assertEquals("years", ETHIOPIC_UTC.years().getName());
        assertEquals("weekyears", ETHIOPIC_UTC.weekyears().getName());
        assertEquals("months", ETHIOPIC_UTC.months().getName());
        assertEquals("weeks", ETHIOPIC_UTC.weeks().getName());
        assertEquals("days", ETHIOPIC_UTC.days().getName());
        assertEquals("halfdays", ETHIOPIC_UTC.halfdays().getName());
        assertEquals("hours", ETHIOPIC_UTC.hours().getName());
        assertEquals("minutes", ETHIOPIC_UTC.minutes().getName());
        assertEquals("seconds", ETHIOPIC_UTC.seconds().getName());
        assertEquals("millis", ETHIOPIC_UTC.millis().getName());
    }

    @Test
    public void testFieldSupport() {
        assertFalse("Eras field should not be supported", ETHIOPIC_UTC.eras().isSupported());
        
        assertTrue("Centuries field should be supported", ETHIOPIC_UTC.centuries().isSupported());
        assertTrue("Years field should be supported", ETHIOPIC_UTC.years().isSupported());
        assertTrue("Weekyears field should be supported", ETHIOPIC_UTC.weekyears().isSupported());
        assertTrue("Months field should be supported", ETHIOPIC_UTC.months().isSupported());
        assertTrue("Weeks field should be supported", ETHIOPIC_UTC.weeks().isSupported());
        assertTrue("Days field should be supported", ETHIOPIC_UTC.days().isSupported());
        assertTrue("Halfdays field should be supported", ETHIOPIC_UTC.halfdays().isSupported());
        assertTrue("Hours field should be supported", ETHIOPIC_UTC.hours().isSupported());
        assertTrue("Minutes field should be supported", ETHIOPIC_UTC.minutes().isSupported());
        assertTrue("Seconds field should be supported", ETHIOPIC_UTC.seconds().isSupported());
        assertTrue("Millis field should be supported", ETHIOPIC_UTC.millis().isSupported());
    }

    @Test
    public void testPrecisionInFixedOffsetZone() {
        // In a fixed-offset zone like UTC, date-based fields are precise if their
        // length in milliseconds is constant.
        assertTrue("In UTC, weeks are precise", ETHIOPIC_UTC.weeks().isPrecise());
        assertTrue("In UTC, days are precise", ETHIOPIC_UTC.days().isPrecise());
        assertTrue("In UTC, halfdays are precise", ETHIOPIC_UTC.halfdays().isPrecise());

        // Time-based fields are always precise.
        assertTrue("Hours are always precise", ETHIOPIC_UTC.hours().isPrecise());
        assertTrue("Minutes are always precise", ETHIOPIC_UTC.minutes().isPrecise());
        assertTrue("Seconds are always precise", ETHIOPIC_UTC.seconds().isPrecise());
        assertTrue("Millis are always precise", ETHIOPIC_UTC.millis().isPrecise());

        // Fields with variable length (due to leap years) are never precise.
        assertFalse("Centuries are not precise", ETHIOPIC_UTC.centuries().isPrecise());
        assertFalse("Years are not precise", ETHIOPIC_UTC.years().isPrecise());
        assertFalse("Weekyears are not precise", ETHIOPIC_UTC.weekyears().isPrecise());
        assertFalse("Months are not precise", ETHIOPIC_UTC.months().isPrecise());
    }

    @Test
    public void testPrecisionInVariableOffsetZone() {
        // In a zone with DST (a variable offset), date-based fields are imprecise
        // because a "day" can have a different number of hours across transitions.
        assertFalse("In a DST zone, weeks are not precise", ETHIOPIC_LONDON.weeks().isPrecise());
        assertFalse("In a DST zone, days are not precise", ETHIOPIC_LONDON.days().isPrecise());
        assertFalse("In a DST zone, halfdays are not precise", ETHIOPIC_LONDON.halfdays().isPrecise());

        // Time-based fields are always precise.
        assertTrue("Hours are always precise", ETHIOPIC_LONDON.hours().isPrecise());
        assertTrue("Minutes are always precise", ETHIOPIC_LONDON.minutes().isPrecise());
        assertTrue("Seconds are always precise", ETHIOPIC_LONDON.seconds().isPrecise());
        assertTrue("Millis are always precise", ETHIOPIC_LONDON.millis().isPrecise());

        // Fields with variable length (due to leap years) are never precise.
        assertFalse("Centuries are not precise", ETHIOPIC_LONDON.centuries().isPrecise());
        assertFalse("Years are not precise", ETHIOPIC_LONDON.years().isPrecise());
        assertFalse("Weekyears are not precise", ETHIOPIC_LONDON.weekyears().isPrecise());
        assertFalse("Months are not precise", ETHIOPIC_LONDON.months().isPrecise());
    }
}