/*
 *  Copyright 2001-2014 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.chrono;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

/**
 * JUnit test suite for the GregorianChronology class.
 * Tests various functionalities of the GregorianChronology.
 */
@SuppressWarnings("deprecation")
public class TestGregorianChronology extends TestCase {

    // Constants for time zones used in tests
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Number of days from year 0 to 2002, used for calculating test time
    private static final long DAYS_TO_2002 = 365 * 30 + 7; // Simplified calculation for brevity

    // Test time set to 2002-06-09
    private static final long TEST_TIME_NOW = 
            (DAYS_TO_2002 + 31 + 28 + 31 + 30 + 31 + 9 - 1) * DateTimeConstants.MILLIS_PER_DAY;

    // Original system settings to restore after tests
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    // Main method to run the test suite
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    // Create a test suite containing all test cases
    public static TestSuite suite() {
        return new TestSuite(TestGregorianChronology.class);
    }

    // Constructor
    public TestGregorianChronology(String name) {
        super(name);
    }

    // Set up the test environment
    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    // Tear down the test environment
    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    // Test cases for GregorianChronology factory methods
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, GregorianChronology.getInstanceUTC().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, GregorianChronology.getInstance().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, GregorianChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, GregorianChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, GregorianChronology.getInstance(null).getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance(TOKYO).getClass());
    }

    public void testFactory_Zone_int() {
        GregorianChronology chrono = GregorianChronology.getInstance(TOKYO, 2);
        assertEquals(TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());

        // Test invalid minimum days in first week
        try {
            GregorianChronology.getInstance(TOKYO, 0);
            fail("Expected IllegalArgumentException for minDaysInFirstWeek = 0");
        } catch (IllegalArgumentException ex) {}

        try {
            GregorianChronology.getInstance(TOKYO, 8);
            fail("Expected IllegalArgumentException for minDaysInFirstWeek = 8");
        } catch (IllegalArgumentException ex) {}
    }

    // Test cases for equality and zone conversion
    public void testEquality() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(PARIS));
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
        assertSame(GregorianChronology.getInstance(), GregorianChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(LONDON).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(TOKYO).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC().withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(GregorianChronology.getInstance(TOKYO), GregorianChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(GregorianChronology.getInstance(LONDON), GregorianChronology.getInstance(TOKYO).withZone(null));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstance().withZone(PARIS));
        assertSame(GregorianChronology.getInstance(PARIS), GregorianChronology.getInstanceUTC().withZone(PARIS));
    }

    // Test case for toString method
    public void testToString() {
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance(LONDON).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", GregorianChronology.getInstance(TOKYO).toString());
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]", GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    // Test cases for duration fields
    public void testDurationFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("eras", greg.eras().getName());
        assertEquals("centuries", greg.centuries().getName());
        assertEquals("years", greg.years().getName());
        assertEquals("weekyears", greg.weekyears().getName());
        assertEquals("months", greg.months().getName());
        assertEquals("weeks", greg.weeks().getName());
        assertEquals("days", greg.days().getName());
        assertEquals("halfdays", greg.halfdays().getName());
        assertEquals("hours", greg.hours().getName());
        assertEquals("minutes", greg.minutes().getName());
        assertEquals("seconds", greg.seconds().getName());
        assertEquals("millis", greg.millis().getName());

        assertFalse(greg.eras().isSupported());
        assertTrue(greg.centuries().isSupported());
        assertTrue(greg.years().isSupported());
        assertTrue(greg.weekyears().isSupported());
        assertTrue(greg.months().isSupported());
        assertTrue(greg.weeks().isSupported());
        assertTrue(greg.days().isSupported());
        assertTrue(greg.halfdays().isSupported());
        assertTrue(greg.hours().isSupported());
        assertTrue(greg.minutes().isSupported());
        assertTrue(greg.seconds().isSupported());
        assertTrue(greg.millis().isSupported());

        assertFalse(greg.centuries().isPrecise());
        assertFalse(greg.years().isPrecise());
        assertFalse(greg.weekyears().isPrecise());
        assertFalse(greg.months().isPrecise());
        assertFalse(greg.weeks().isPrecise());
        assertFalse(greg.days().isPrecise());
        assertFalse(greg.halfdays().isPrecise());
        assertTrue(greg.hours().isPrecise());
        assertTrue(greg.minutes().isPrecise());
        assertTrue(greg.seconds().isPrecise());
        assertTrue(greg.millis().isPrecise());

        final GregorianChronology gregUTC = GregorianChronology.getInstanceUTC();
        assertFalse(gregUTC.centuries().isPrecise());
        assertFalse(gregUTC.years().isPrecise());
        assertFalse(gregUTC.weekyears().isPrecise());
        assertFalse(gregUTC.months().isPrecise());
        assertTrue(gregUTC.weeks().isPrecise());
        assertTrue(gregUTC.days().isPrecise());
        assertTrue(gregUTC.halfdays().isPrecise());
        assertTrue(gregUTC.hours().isPrecise());
        assertTrue(gregUTC.minutes().isPrecise());
        assertTrue(gregUTC.seconds().isPrecise());
        assertTrue(gregUTC.millis().isPrecise());

        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregGMT = GregorianChronology.getInstance(gmt);
        assertFalse(gregGMT.centuries().isPrecise());
        assertFalse(gregGMT.years().isPrecise());
        assertFalse(gregGMT.weekyears().isPrecise());
        assertFalse(gregGMT.months().isPrecise());
        assertTrue(gregGMT.weeks().isPrecise());
        assertTrue(gregGMT.days().isPrecise());
        assertTrue(gregGMT.halfdays().isPrecise());
        assertTrue(gregGMT.hours().isPrecise());
        assertTrue(gregGMT.minutes().isPrecise());
        assertTrue(gregGMT.seconds().isPrecise());
        assertTrue(gregGMT.millis().isPrecise());
    }

    // Test cases for date fields
    public void testDateFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("era", greg.era().getName());
        assertEquals("centuryOfEra", greg.centuryOfEra().getName());
        assertEquals("yearOfCentury", greg.yearOfCentury().getName());
        assertEquals("yearOfEra", greg.yearOfEra().getName());
        assertEquals("year", greg.year().getName());
        assertEquals("monthOfYear", greg.monthOfYear().getName());
        assertEquals("weekyearOfCentury", greg.weekyearOfCentury().getName());
        assertEquals("weekyear", greg.weekyear().getName());
        assertEquals("weekOfWeekyear", greg.weekOfWeekyear().getName());
        assertEquals("dayOfYear", greg.dayOfYear().getName());
        assertEquals("dayOfMonth", greg.dayOfMonth().getName());
        assertEquals("dayOfWeek", greg.dayOfWeek().getName());

        assertTrue(greg.era().isSupported());
        assertTrue(greg.centuryOfEra().isSupported());
        assertTrue(greg.yearOfCentury().isSupported());
        assertTrue(greg.yearOfEra().isSupported());
        assertTrue(greg.year().isSupported());
        assertTrue(greg.monthOfYear().isSupported());
        assertTrue(greg.weekyearOfCentury().isSupported());
        assertTrue(greg.weekyear().isSupported());
        assertTrue(greg.weekOfWeekyear().isSupported());
        assertTrue(greg.dayOfYear().isSupported());
        assertTrue(greg.dayOfMonth().isSupported());
        assertTrue(greg.dayOfWeek().isSupported());

        assertEquals(greg.eras(), greg.era().getDurationField());
        assertEquals(greg.centuries(), greg.centuryOfEra().getDurationField());
        assertEquals(greg.years(), greg.yearOfCentury().getDurationField());
        assertEquals(greg.years(), greg.yearOfEra().getDurationField());
        assertEquals(greg.years(), greg.year().getDurationField());
        assertEquals(greg.months(), greg.monthOfYear().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyearOfCentury().getDurationField());
        assertEquals(greg.weekyears(), greg.weekyear().getDurationField());
        assertEquals(greg.weeks(), greg.weekOfWeekyear().getDurationField());
        assertEquals(greg.days(), greg.dayOfYear().getDurationField());
        assertEquals(greg.days(), greg.dayOfMonth().getDurationField());
        assertEquals(greg.days(), greg.dayOfWeek().getDurationField());

        assertNull(greg.era().getRangeDurationField());
        assertEquals(greg.eras(), greg.centuryOfEra().getRangeDurationField());
        assertEquals(greg.centuries(), greg.yearOfCentury().getRangeDurationField());
        assertEquals(greg.eras(), greg.yearOfEra().getRangeDurationField());
        assertNull(greg.year().getRangeDurationField());
        assertEquals(greg.years(), greg.monthOfYear().getRangeDurationField());
        assertEquals(greg.centuries(), greg.weekyearOfCentury().getRangeDurationField());
        assertNull(greg.weekyear().getRangeDurationField());
        assertEquals(greg.weekyears(), greg.weekOfWeekyear().getRangeDurationField());
        assertEquals(greg.years(), greg.dayOfYear().getRangeDurationField());
        assertEquals(greg.months(), greg.dayOfMonth().getRangeDurationField());
        assertEquals(greg.weeks(), greg.dayOfWeek().getRangeDurationField());
    }

    // Test cases for time fields
    public void testTimeFields() {
        final GregorianChronology greg = GregorianChronology.getInstance();
        assertEquals("halfdayOfDay", greg.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", greg.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", greg.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", greg.clockhourOfDay().getName());
        assertEquals("hourOfDay", greg.hourOfDay().getName());
        assertEquals("minuteOfDay", greg.minuteOfDay().getName());
        assertEquals("minuteOfHour", greg.minuteOfHour().getName());
        assertEquals("secondOfDay", greg.secondOfDay().getName());
        assertEquals("secondOfMinute", greg.secondOfMinute().getName());
        assertEquals("millisOfDay", greg.millisOfDay().getName());
        assertEquals("millisOfSecond", greg.millisOfSecond().getName());

        assertTrue(greg.halfdayOfDay().isSupported());
        assertTrue(greg.clockhourOfHalfday().isSupported());
        assertTrue(greg.hourOfHalfday().isSupported());
        assertTrue(greg.clockhourOfDay().isSupported());
        assertTrue(greg.hourOfDay().isSupported());
        assertTrue(greg.minuteOfDay().isSupported());
        assertTrue(greg.minuteOfHour().isSupported());
        assertTrue(greg.secondOfDay().isSupported());
        assertTrue(greg.secondOfMinute().isSupported());
        assertTrue(greg.millisOfDay().isSupported());
        assertTrue(greg.millisOfSecond().isSupported());
    }

    // Test cases for maximum value calculations
    public void testMaximumValue() {
        YearMonthDay ymd1 = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1);
        DateMidnight dm1 = new DateMidnight(1999, DateTimeConstants.FEBRUARY, 1);
        Chronology chrono = GregorianChronology.getInstance();
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(ymd1));
        assertEquals(28, chrono.dayOfMonth().getMaximumValue(dm1.getMillis()));
    }

    // Test cases for leap year calculations
    public void testLeap_28feb() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    public void testLeap_29feb() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}