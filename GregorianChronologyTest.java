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
 * This class is a Junit unit test for GregorianChronology.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestGregorianChronology extends TestCase {

    // Constants for time zones used in tests
    private static final DateTimeZone ZONE_PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone ZONE_LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone ZONE_TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time for consistent test results: 2002-06-09T00:00:00Z
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    // Store original system defaults
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestGregorianChronology.class);
    }

    public TestGregorianChronology(String name) {
        super(name);
    }

    /**
     * Sets up the test environment.
     * This involves fixing the current time and setting a default TimeZone and Locale.
     */
    @Override
    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(ZONE_LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    /**
     * Tears down the test environment.
     * This restores the original system time, TimeZone, and Locale.
     */
    @Override
    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    // Factory methods and singletons
    //-----------------------------------------------------------------------

    public void testGetInstanceUTC_returnsChronologyInUTC() {
        // Act
        GregorianChronology chrono = GregorianChronology.getInstanceUTC();

        // Assert
        assertEquals(DateTimeZone.UTC, chrono.getZone());
        assertSame(GregorianChronology.class, chrono.getClass());
    }

    public void testGetInstance_returnsChronologyInDefaultZone() {
        // Assert
        assertEquals(ZONE_LONDON, GregorianChronology.getInstance().getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance().getClass());
    }

    public void testGetInstance_withZone_returnsChronologyInThatZone() {
        // Assert
        assertEquals(ZONE_TOKYO, GregorianChronology.getInstance(ZONE_TOKYO).getZone());
        assertEquals(ZONE_PARIS, GregorianChronology.getInstance(ZONE_PARIS).getZone());
        assertSame(GregorianChronology.class, GregorianChronology.getInstance(ZONE_TOKYO).getClass());
    }

    public void testGetInstance_withNullZone_returnsChronologyInDefaultZone() {
        // Assert
        assertEquals(ZONE_LONDON, GregorianChronology.getInstance(null).getZone());
    }

    public void testGetInstance_withZoneAndMinDays_returnsCorrectChronology() {
        // Act
        GregorianChronology chrono = GregorianChronology.getInstance(ZONE_TOKYO, 2);

        // Assert
        assertEquals(ZONE_TOKYO, chrono.getZone());
        assertEquals(2, chrono.getMinimumDaysInFirstWeek());
    }

    public void testGetInstance_withInvalidMinDays_throwsIllegalArgumentException() {
        // Test with minimum days less than 1
        try {
            GregorianChronology.getInstance(ZONE_TOKYO, 0);
            fail("Expected IllegalArgumentException for minimum days < 1");
        } catch (IllegalArgumentException ex) {
            // Expected
        }

        // Test with minimum days greater than 7
        try {
            GregorianChronology.getInstance(ZONE_TOKYO, 8);
            fail("Expected IllegalArgumentException for minimum days > 7");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Equality and Caching
    //-----------------------------------------------------------------------

    public void testGetInstance_forSameZone_returnsCachedInstance() {
        // Assert
        assertSame(GregorianChronology.getInstance(ZONE_TOKYO), GregorianChronology.getInstance(ZONE_TOKYO));
        assertSame(GregorianChronology.getInstance(ZONE_LONDON), GregorianChronology.getInstance(ZONE_LONDON));
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC());
        assertSame(GregorianChronology.getInstance(), GregorianChronology.getInstance(ZONE_LONDON));
    }

    public void testWithUTC_returnsUTCChronologyInstance() {
        // Assert
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(ZONE_LONDON).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance(ZONE_TOKYO).withUTC());
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstance().withUTC());

        // Test that calling withUTC() on a UTC instance returns itself
        assertSame(GregorianChronology.getInstanceUTC(), GregorianChronology.getInstanceUTC().withUTC());
    }

    public void testWithZone_returnsChronologyInCorrectZone() {
        // Assert
        assertSame(GregorianChronology.getInstance(ZONE_TOKYO), GregorianChronology.getInstance(ZONE_TOKYO).withZone(ZONE_TOKYO));
        assertSame(GregorianChronology.getInstance(ZONE_LONDON), GregorianChronology.getInstance(ZONE_TOKYO).withZone(ZONE_LONDON));
        assertSame(GregorianChronology.getInstance(ZONE_PARIS), GregorianChronology.getInstance(ZONE_TOKYO).withZone(ZONE_PARIS));
        assertSame(GregorianChronology.getInstance(ZONE_PARIS), GregorianChronology.getInstance().withZone(ZONE_PARIS));
        assertSame(GregorianChronology.getInstance(ZONE_PARIS), GregorianChronology.getInstanceUTC().withZone(ZONE_PARIS));
    }

    public void testWithZone_withNull_returnsDefaultZoneChronology() {
        // Assert
        assertSame(GregorianChronology.getInstance(ZONE_LONDON), GregorianChronology.getInstance(ZONE_TOKYO).withZone(null));
    }

    public void testToString_returnsCorrectRepresentation() {
        // Assert
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance(ZONE_LONDON).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", GregorianChronology.getInstance(ZONE_TOKYO).toString());
        assertEquals("GregorianChronology[Europe/London]", GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]", GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    //-----------------------------------------------------------------------
    // Field properties
    //-----------------------------------------------------------------------

    public void testDurationFields_haveCorrectNames() {
        Chronology chrono = GregorianChronology.getInstance();
        assertEquals("eras", chrono.eras().getName());
        assertEquals("centuries", chrono.centuries().getName());
        assertEquals("years", chrono.years().getName());
        assertEquals("weekyears", chrono.weekyears().getName());
        assertEquals("months", chrono.months().getName());
        assertEquals("weeks", chrono.weeks().getName());
        assertEquals("days", chrono.days().getName());
        assertEquals("halfdays", chrono.halfdays().getName());
        assertEquals("hours", chrono.hours().getName());
        assertEquals("minutes", chrono.minutes().getName());
        assertEquals("seconds", chrono.seconds().getName());
        assertEquals("millis", chrono.millis().getName());
    }

    public void testDateFields_haveCorrectNames() {
        Chronology chrono = GregorianChronology.getInstance();
        assertEquals("era", chrono.era().getName());
        assertEquals("centuryOfEra", chrono.centuryOfEra().getName());
        assertEquals("yearOfCentury", chrono.yearOfCentury().getName());
        assertEquals("yearOfEra", chrono.yearOfEra().getName());
        assertEquals("year", chrono.year().getName());
        assertEquals("monthOfYear", chrono.monthOfYear().getName());
        assertEquals("weekyearOfCentury", chrono.weekyearOfCentury().getName());
        assertEquals("weekyear", chrono.weekyear().getName());
        assertEquals("weekOfWeekyear", chrono.weekOfWeekyear().getName());
        assertEquals("dayOfYear", chrono.dayOfYear().getName());
        assertEquals("dayOfMonth", chrono.dayOfMonth().getName());
        assertEquals("dayOfWeek", chrono.dayOfWeek().getName());
    }

    public void testTimeFields_haveCorrectNames() {
        Chronology chrono = GregorianChronology.getInstance();
        assertEquals("halfdayOfDay", chrono.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", chrono.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", chrono.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", chrono.clockhourOfDay().getName());
        assertEquals("hourOfDay", chrono.hourOfDay().getName());
        assertEquals("minuteOfDay", chrono.minuteOfDay().getName());
        assertEquals("minuteOfHour", chrono.minuteOfHour().getName());
        assertEquals("secondOfDay", chrono.secondOfDay().getName());
        assertEquals("secondOfMinute", chrono.secondOfMinute().getName());
        assertEquals("millisOfDay", chrono.millisOfDay().getName());
        assertEquals("millisOfSecond", chrono.millisOfSecond().getName());
    }

    public void testFields_areSupported() {
        Chronology chrono = GregorianChronology.getInstance();

        // Duration fields
        assertEquals(false, chrono.eras().isSupported()); // Eras are infinite
        assertEquals(true, chrono.centuries().isSupported());
        assertEquals(true, chrono.years().isSupported());
        assertEquals(true, chrono.weekyears().isSupported());
        assertEquals(true, chrono.months().isSupported());
        assertEquals(true, chrono.weeks().isSupported());
        assertEquals(true, chrono.days().isSupported());
        assertEquals(true, chrono.halfdays().isSupported());
        assertEquals(true, chrono.hours().isSupported());
        assertEquals(true, chrono.minutes().isSupported());
        assertEquals(true, chrono.seconds().isSupported());
        assertEquals(true, chrono.millis().isSupported());

        // Date fields
        assertEquals(true, chrono.era().isSupported());
        assertEquals(true, chrono.centuryOfEra().isSupported());
        assertEquals(true, chrono.yearOfCentury().isSupported());
        assertEquals(true, chrono.yearOfEra().isSupported());
        assertEquals(true, chrono.year().isSupported());
        assertEquals(true, chrono.monthOfYear().isSupported());
        assertEquals(true, chrono.weekyearOfCentury().isSupported());
        assertEquals(true, chrono.weekyear().isSupported());
        assertEquals(true, chrono.weekOfWeekyear().isSupported());
        assertEquals(true, chrono.dayOfYear().isSupported());
        assertEquals(true, chrono.dayOfMonth().isSupported());
        assertEquals(true, chrono.dayOfWeek().isSupported());

        // Time fields
        assertEquals(true, chrono.halfdayOfDay().isSupported());
        assertEquals(true, chrono.hourOfHalfday().isSupported());
        assertEquals(true, chrono.clockhourOfHalfday().isSupported());
        assertEquals(true, chrono.hourOfDay().isSupported());
        assertEquals(true, chrono.clockhourOfDay().isSupported());
        assertEquals(true, chrono.minuteOfDay().isSupported());
        assertEquals(true, chrono.minuteOfHour().isSupported());
        assertEquals(true, chrono.secondOfDay().isSupported());
        assertEquals(true, chrono.secondOfMinute().isSupported());
        assertEquals(true, chrono.millisOfDay().isSupported());
        assertEquals(true, chrono.millisOfSecond().isSupported());
    }

    public void testDurationFields_Precision_inVariableTimeZone() {
        // In a time zone with DST, larger fields are imprecise
        GregorianChronology chrono = GregorianChronology.getInstance(ZONE_LONDON);
        assertEquals(false, chrono.centuries().isPrecise());
        assertEquals(false, chrono.years().isPrecise());
        assertEquals(false, chrono.weekyears().isPrecise());
        assertEquals(false, chrono.months().isPrecise());
        assertEquals(false, chrono.weeks().isPrecise());
        assertEquals(false, chrono.days().isPrecise());
        assertEquals(false, chrono.halfdays().isPrecise());

        // Smaller time-based fields are precise
        assertEquals(true, chrono.hours().isPrecise());
        assertEquals(true, chrono.minutes().isPrecise());
        assertEquals(true, chrono.seconds().isPrecise());
        assertEquals(true, chrono.millis().isPrecise());
    }

    public void testDurationFields_Precision_inUTC() {
        // In UTC, date-based fields are also precise
        GregorianChronology chrono = GregorianChronology.getInstanceUTC();
        assertEquals(false, chrono.centuries().isPrecise());
        assertEquals(false, chrono.years().isPrecise());
        assertEquals(false, chrono.weekyears().isPrecise());
        assertEquals(false, chrono.months().isPrecise());
        assertEquals(true, chrono.weeks().isPrecise());
        assertEquals(true, chrono.days().isPrecise());
        assertEquals(true, chrono.halfdays().isPrecise());
        assertEquals(true, chrono.hours().isPrecise());
        assertEquals(true, chrono.minutes().isPrecise());
        assertEquals(true, chrono.seconds().isPrecise());
        assertEquals(true, chrono.millis().isPrecise());
    }

    //-----------------------------------------------------------------------
    // Leap year behavior
    //-----------------------------------------------------------------------

    public void testDayOfMonth_getMaximumValue_forNonLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        YearMonthDay ymd = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1, chrono);

        assertEquals("February in a non-leap year should have 28 days", 28, chrono.dayOfMonth().getMaximumValue(ymd));
    }

    public void testDayOfMonth_getMaximumValue_forLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        // Test with a partial (YearMonthDay)
        YearMonthDay ymd = new YearMonthDay(2012, DateTimeConstants.FEBRUARY, 1, chrono);
        assertEquals("February in a leap year should have 29 days", 29, chrono.dayOfMonth().getMaximumValue(ymd));

        // Test with a full instant (DateMidnight)
        DateMidnight dm = new DateMidnight(2012, DateTimeConstants.FEBRUARY, 1, chrono);
        assertEquals("February in a leap year should have 29 days", 29, chrono.dayOfMonth().getMaximumValue(dm.getMillis()));
    }

    public void testIsLeap_onFebruary28thOfLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 28, 0, 0, chrono);

        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(false, dt.dayOfMonth().isLeap()); // Day 28 is not a leap day itself
        assertEquals(false, dt.dayOfYear().isLeap());
    }

    public void testIsLeap_onFebruary29thOfLeapYear() {
        Chronology chrono = GregorianChronology.getInstance();
        DateTime dt = new DateTime(2012, 2, 29, 0, 0, chrono);

        assertEquals(true, dt.year().isLeap());
        assertEquals(true, dt.monthOfYear().isLeap());
        assertEquals(true, dt.dayOfMonth().isLeap()); // Day 29 is the leap day
        assertEquals(true, dt.dayOfYear().isLeap());
    }

}