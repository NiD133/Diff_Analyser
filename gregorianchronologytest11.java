package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.Chronology;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the time-related fields of GregorianChronology.
 * This test focuses on verifying the names and support status of fields
 * like hourOfDay, minuteOfHour, etc.
 */
public class GregorianChronologyTimeFieldsTest {

    private Chronology gregorianChronology;

    @Before
    public void setUp() {
        // Using the UTC instance ensures tests are deterministic and not affected by the default time zone.
        gregorianChronology = GregorianChronology.getInstanceUTC();
    }

    @Test
    public void testTimeFieldNamesAreCorrect() {
        assertEquals("halfdayOfDay", gregorianChronology.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", gregorianChronology.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", gregorianChronology.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", gregorianChronology.clockhourOfDay().getName());
        assertEquals("hourOfDay", gregorianChronology.hourOfDay().getName());
        assertEquals("minuteOfDay", gregorianChronology.minuteOfDay().getName());
        assertEquals("minuteOfHour", gregorianChronology.minuteOfHour().getName());
        assertEquals("secondOfDay", gregorianChronology.secondOfDay().getName());
        assertEquals("secondOfMinute", gregorianChronology.secondOfMinute().getName());
        assertEquals("millisOfDay", gregorianChronology.millisOfDay().getName());
        assertEquals("millisOfSecond", gregorianChronology.millisOfSecond().getName());
    }

    @Test
    public void testTimeFieldsAreSupported() {
        assertTrue(gregorianChronology.halfdayOfDay().isSupported());
        assertTrue(gregorianChronology.clockhourOfHalfday().isSupported());
        assertTrue(gregorianChronology.hourOfHalfday().isSupported());
        assertTrue(gregorianChronology.clockhourOfDay().isSupported());
        assertTrue(gregorianChronology.hourOfDay().isSupported());
        assertTrue(gregorianChronology.minuteOfDay().isSupported());
        assertTrue(gregorianChronology.minuteOfHour().isSupported());
        assertTrue(gregorianChronology.secondOfDay().isSupported());
        assertTrue(gregorianChronology.secondOfMinute().isSupported());
        assertTrue(gregorianChronology.millisOfDay().isSupported());
        assertTrue(gregorianChronology.millisOfSecond().isSupported());
    }
}