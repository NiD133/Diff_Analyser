package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the duration fields of CopticChronology.
 */
public class CopticChronologyDurationFieldTest {

    // A specific instant in time, 2002-06-09T00:00:00Z.
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, DateTimeZone.UTC).getMillis();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();

        // Set a default time zone with DST to test its impact on field precision.
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    @Test
    public void testFieldNamesAndSupport() {
        Chronology coptic = CopticChronology.getInstanceUTC();

        assertEquals("eras", coptic.eras().getName());
        assertFalse("Eras field should not be supported", coptic.eras().isSupported());

        assertEquals("centuries", coptic.centuries().getName());
        assertTrue(coptic.centuries().isSupported());

        assertEquals("years", coptic.years().getName());
        assertTrue(coptic.years().isSupported());

        assertEquals("weekyears", coptic.weekyears().getName());
        assertTrue(coptic.weekyears().isSupported());

        assertEquals("months", coptic.months().getName());
        assertTrue(coptic.months().isSupported());

        assertEquals("weeks", coptic.weeks().getName());
        assertTrue(coptic.weeks().isSupported());

        assertEquals("days", coptic.days().getName());
        assertTrue(coptic.days().isSupported());

        assertEquals("halfdays", coptic.halfdays().getName());
        assertTrue(coptic.halfdays().isSupported());

        assertEquals("hours", coptic.hours().getName());
        assertTrue(coptic.hours().isSupported());

        assertEquals("minutes", coptic.minutes().getName());
        assertTrue(coptic.minutes().isSupported());

        assertEquals("seconds", coptic.seconds().getName());
        assertTrue(coptic.seconds().isSupported());

        assertEquals("millis", coptic.millis().getName());
        assertTrue(coptic.millis().isSupported());
    }

    @Test
    public void testFieldPrecision_InZoneWithDST() {
        // The default zone is set to "Europe/London" in setUp(), which has Daylight Saving Time.
        // Fields longer than a day are imprecise due to DST transitions.
        Chronology coptic = CopticChronology.getInstance();

        assertFalse("centuries", coptic.centuries().isPrecise());
        assertFalse("years", coptic.years().isPrecise());
        assertFalse("weekyears", coptic.weekyears().isPrecise());
        assertFalse("months", coptic.months().isPrecise());
        assertFalse("weeks", coptic.weeks().isPrecise());
        assertFalse("days", coptic.days().isPrecise());
        assertFalse("halfdays", coptic.halfdays().isPrecise());

        assertTrue("hours", coptic.hours().isPrecise());
        assertTrue("minutes", coptic.minutes().isPrecise());
        assertTrue("seconds", coptic.seconds().isPrecise());
        assertTrue("millis", coptic.millis().isPrecise());
    }

    @Test
    public void testFieldPrecision_InUTC() {
        Chronology copticUTC = CopticChronology.getInstanceUTC();
        assertPrecisionForFixedOffsetZone(copticUTC);
    }

    @Test
    public void testFieldPrecision_InFixedOffsetZone() {
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        Chronology copticGMT = CopticChronology.getInstance(gmt);
        assertPrecisionForFixedOffsetZone(copticGMT);
    }

    /**
     * Asserts the precision of duration fields for a Chronology in a fixed-offset time zone (like UTC or GMT).
     * In these zones, day-based fields are precise, but year/month-based fields are not due to leap years.
     */
    private void assertPrecisionForFixedOffsetZone(Chronology chronology) {
        assertFalse("centuries", chronology.centuries().isPrecise());
        assertFalse("years", chronology.years().isPrecise());
        assertFalse("weekyears", chronology.weekyears().isPrecise());
        assertFalse("months", chronology.months().isPrecise());

        assertTrue("weeks", chronology.weeks().isPrecise());
        assertTrue("days", chronology.days().isPrecise());
        assertTrue("halfdays", chronology.halfdays().isPrecise());
        assertTrue("hours", chronology.hours().isPrecise());
        assertTrue("minutes", chronology.minutes().isPrecise());
        assertTrue("seconds", chronology.seconds().isPrecise());
        assertTrue("millis", chronology.millis().isPrecise());
    }
}