package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the date fields of GregorianChronology.
 * This test verifies the fundamental properties of each date field, such as its name,
 * whether it's supported, and its associated duration and range duration fields.
 */
public class GregorianChronologyTest {

    private GregorianChronology chronology;

    @Before
    public void setUp() {
        // Use the UTC instance of GregorianChronology to ensure tests are consistent
        // and not affected by the system's default time zone.
        chronology = GregorianChronology.getInstanceUTC();
    }

    @Test
    public void testDateFields_haveCorrectNames() {
        assertEquals("era", chronology.era().getName());
        assertEquals("centuryOfEra", chronology.centuryOfEra().getName());
        assertEquals("yearOfCentury", chronology.yearOfCentury().getName());
        assertEquals("yearOfEra", chronology.yearOfEra().getName());
        assertEquals("year", chronology.year().getName());
        assertEquals("monthOfYear", chronology.monthOfYear().getName());
        assertEquals("weekyearOfCentury", chronology.weekyearOfCentury().getName());
        assertEquals("weekyear", chronology.weekyear().getName());
        assertEquals("weekOfWeekyear", chronology.weekOfWeekyear().getName());
        assertEquals("dayOfYear", chronology.dayOfYear().getName());
        assertEquals("dayOfMonth", chronology.dayOfMonth().getName());
        assertEquals("dayOfWeek", chronology.dayOfWeek().getName());
    }

    @Test
    public void testDateFields_areSupported() {
        assertTrue(chronology.era().isSupported());
        assertTrue(chronology.centuryOfEra().isSupported());
        assertTrue(chronology.yearOfCentury().isSupported());
        assertTrue(chronology.yearOfEra().isSupported());
        assertTrue(chronology.year().isSupported());
        assertTrue(chronology.monthOfYear().isSupported());
        assertTrue(chronology.weekyearOfCentury().isSupported());
        assertTrue(chronology.weekyear().isSupported());
        assertTrue(chronology.weekOfWeekyear().isSupported());
        assertTrue(chronology.dayOfYear().isSupported());
        assertTrue(chronology.dayOfMonth().isSupported());
        assertTrue(chronology.dayOfWeek().isSupported());
    }

    @Test
    public void testDateFields_haveCorrectDurationFields() {
        assertSame(chronology.eras(), chronology.era().getDurationField());
        assertSame(chronology.centuries(), chronology.centuryOfEra().getDurationField());
        assertSame(chronology.years(), chronology.yearOfCentury().getDurationField());
        assertSame(chronology.years(), chronology.yearOfEra().getDurationField());
        assertSame(chronology.years(), chronology.year().getDurationField());
        assertSame(chronology.months(), chronology.monthOfYear().getDurationField());
        assertSame(chronology.weekyears(), chronology.weekyearOfCentury().getDurationField());
        assertSame(chronology.weekyears(), chronology.weekyear().getDurationField());
        assertSame(chronology.weeks(), chronology.weekOfWeekyear().getDurationField());
        assertSame(chronology.days(), chronology.dayOfYear().getDurationField());
        assertSame(chronology.days(), chronology.dayOfMonth().getDurationField());
        assertSame(chronology.days(), chronology.dayOfWeek().getDurationField());
    }

    @Test
    public void testDateFields_haveCorrectRangeDurationFields() {
        assertNull(chronology.era().getRangeDurationField());
        assertSame(chronology.eras(), chronology.centuryOfEra().getRangeDurationField());
        assertSame(chronology.centuries(), chronology.yearOfCentury().getRangeDurationField());
        assertSame(chronology.eras(), chronology.yearOfEra().getRangeDurationField());
        assertNull(chronology.year().getRangeDurationField());
        assertSame(chronology.years(), chronology.monthOfYear().getRangeDurationField());
        assertSame(chronology.centuries(), chronology.weekyearOfCentury().getRangeDurationField());
        assertNull(chronology.weekyear().getRangeDurationField());
        assertSame(chronology.weekyears(), chronology.weekOfWeekyear().getRangeDurationField());
        assertSame(chronology.years(), chronology.dayOfYear().getRangeDurationField());
        assertSame(chronology.months(), chronology.dayOfMonth().getRangeDurationField());
        assertSame(chronology.weeks(), chronology.dayOfWeek().getRangeDurationField());
    }
}