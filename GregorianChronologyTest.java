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
 * Unit tests for GregorianChronology.
 * Tests factory methods, field behavior, leap year calculations, and chronology operations.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestGregorianChronology extends TestCase {

    // Test time zones
    private static final DateTimeZone PARIS_TIMEZONE = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON_TIMEZONE = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO_TIMEZONE = DateTimeZone.forID("Asia/Tokyo");

    // Fixed test time: June 9, 2002 (calculated from days since epoch)
    private static final long JUNE_9_2002_MILLIS = calculateJune9_2002Millis();
    
    // Original system settings to restore after tests
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

    @Override
    protected void setUp() throws Exception {
        // Fix current time for consistent test results
        DateTimeUtils.setCurrentMillisFixed(JUNE_9_2002_MILLIS);
        
        // Store original system settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set test environment to London/UK
        DateTimeZone.setDefault(LONDON_TIMEZONE);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore system time
        DateTimeUtils.setCurrentMillisSystem();
        
        // Restore original system settings
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        
        // Clear references
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    // Factory Method Tests
    //-----------------------------------------------------------------------
    
    public void testFactoryUTC_ReturnsUTCInstance() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        assertEquals("Should use UTC timezone", DateTimeZone.UTC, chronology.getZone());
        assertSame("Should return GregorianChronology instance", 
                   GregorianChronology.class, chronology.getClass());
    }

    public void testFactory_ReturnsDefaultTimezoneInstance() {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        assertEquals("Should use default timezone (London)", LONDON_TIMEZONE, chronology.getZone());
        assertSame("Should return GregorianChronology instance", 
                   GregorianChronology.class, chronology.getClass());
    }

    public void testFactory_WithTimezone_ReturnsSpecifiedTimezoneInstance() {
        GregorianChronology tokyoChronology = GregorianChronology.getInstance(TOKYO_TIMEZONE);
        GregorianChronology parisChronology = GregorianChronology.getInstance(PARIS_TIMEZONE);
        GregorianChronology nullTimezoneChronology = GregorianChronology.getInstance(null);
        
        assertEquals("Should use Tokyo timezone", TOKYO_TIMEZONE, tokyoChronology.getZone());
        assertEquals("Should use Paris timezone", PARIS_TIMEZONE, parisChronology.getZone());
        assertEquals("Null timezone should default to London", LONDON_TIMEZONE, nullTimezoneChronology.getZone());
        assertSame("Should return GregorianChronology instance", 
                   GregorianChronology.class, tokyoChronology.getClass());
    }

    public void testFactory_WithTimezoneAndMinDays_ReturnsConfiguredInstance() {
        int minDaysInFirstWeek = 2;
        GregorianChronology chronology = GregorianChronology.getInstance(TOKYO_TIMEZONE, minDaysInFirstWeek);
        
        assertEquals("Should use specified timezone", TOKYO_TIMEZONE, chronology.getZone());
        assertEquals("Should use specified minimum days", minDaysInFirstWeek, chronology.getMinimumDaysInFirstWeek());
    }

    public void testFactory_WithInvalidMinDays_ThrowsException() {
        try {
            GregorianChronology.getInstance(TOKYO_TIMEZONE, 0);
            fail("Should throw IllegalArgumentException for minDays = 0");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
        
        try {
            GregorianChronology.getInstance(TOKYO_TIMEZONE, 8);
            fail("Should throw IllegalArgumentException for minDays = 8");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    //-----------------------------------------------------------------------
    // Instance Management Tests
    //-----------------------------------------------------------------------
    
    public void testEquality_SameTimezoneSameInstance() {
        assertSame("Tokyo instances should be identical", 
                   GregorianChronology.getInstance(TOKYO_TIMEZONE), 
                   GregorianChronology.getInstance(TOKYO_TIMEZONE));
        assertSame("London instances should be identical", 
                   GregorianChronology.getInstance(LONDON_TIMEZONE), 
                   GregorianChronology.getInstance(LONDON_TIMEZONE));
        assertSame("Paris instances should be identical", 
                   GregorianChronology.getInstance(PARIS_TIMEZONE), 
                   GregorianChronology.getInstance(PARIS_TIMEZONE));
        assertSame("UTC instances should be identical", 
                   GregorianChronology.getInstanceUTC(), 
                   GregorianChronology.getInstanceUTC());
        assertSame("Default should equal London instance", 
                   GregorianChronology.getInstance(), 
                   GregorianChronology.getInstance(LONDON_TIMEZONE));
    }

    public void testWithUTC_ReturnsUTCInstance() {
        GregorianChronology utcInstance = GregorianChronology.getInstanceUTC();
        
        assertSame("London chronology should convert to UTC", 
                   utcInstance, GregorianChronology.getInstance(LONDON_TIMEZONE).withUTC());
        assertSame("Tokyo chronology should convert to UTC", 
                   utcInstance, GregorianChronology.getInstance(TOKYO_TIMEZONE).withUTC());
        assertSame("UTC chronology should remain UTC", 
                   utcInstance, GregorianChronology.getInstanceUTC().withUTC());
        assertSame("Default chronology should convert to UTC", 
                   utcInstance, GregorianChronology.getInstance().withUTC());
    }

    public void testWithZone_ReturnsInstanceWithSpecifiedZone() {
        GregorianChronology baseChronology = GregorianChronology.getInstance(TOKYO_TIMEZONE);
        
        assertSame("Same timezone should return same instance", 
                   GregorianChronology.getInstance(TOKYO_TIMEZONE), 
                   baseChronology.withZone(TOKYO_TIMEZONE));
        assertSame("Should return London instance", 
                   GregorianChronology.getInstance(LONDON_TIMEZONE), 
                   baseChronology.withZone(LONDON_TIMEZONE));
        assertSame("Should return Paris instance", 
                   GregorianChronology.getInstance(PARIS_TIMEZONE), 
                   baseChronology.withZone(PARIS_TIMEZONE));
        assertSame("Null zone should return default (London)", 
                   GregorianChronology.getInstance(LONDON_TIMEZONE), 
                   baseChronology.withZone(null));
        
        // Test with default and UTC instances
        assertSame("Default with Paris should return Paris instance", 
                   GregorianChronology.getInstance(PARIS_TIMEZONE), 
                   GregorianChronology.getInstance().withZone(PARIS_TIMEZONE));
        assertSame("UTC with Paris should return Paris instance", 
                   GregorianChronology.getInstance(PARIS_TIMEZONE), 
                   GregorianChronology.getInstanceUTC().withZone(PARIS_TIMEZONE));
    }

    public void testToString_ShowsTimezoneAndConfiguration() {
        assertEquals("GregorianChronology[Europe/London]", 
                     GregorianChronology.getInstance(LONDON_TIMEZONE).toString());
        assertEquals("GregorianChronology[Asia/Tokyo]", 
                     GregorianChronology.getInstance(TOKYO_TIMEZONE).toString());
        assertEquals("GregorianChronology[Europe/London]", 
                     GregorianChronology.getInstance().toString());
        assertEquals("GregorianChronology[UTC]", 
                     GregorianChronology.getInstanceUTC().toString());
        assertEquals("GregorianChronology[UTC,mdfw=2]", 
                     GregorianChronology.getInstance(DateTimeZone.UTC, 2).toString());
    }

    //-----------------------------------------------------------------------
    // Duration Field Tests
    //-----------------------------------------------------------------------
    
    public void testDurationFields_NamesAndSupport() {
        final GregorianChronology gregorian = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("eras", gregorian.eras().getName());
        assertEquals("centuries", gregorian.centuries().getName());
        assertEquals("years", gregorian.years().getName());
        assertEquals("weekyears", gregorian.weekyears().getName());
        assertEquals("months", gregorian.months().getName());
        assertEquals("weeks", gregorian.weeks().getName());
        assertEquals("days", gregorian.days().getName());
        assertEquals("halfdays", gregorian.halfdays().getName());
        assertEquals("hours", gregorian.hours().getName());
        assertEquals("minutes", gregorian.minutes().getName());
        assertEquals("seconds", gregorian.seconds().getName());
        assertEquals("millis", gregorian.millis().getName());
        
        // Test field support (eras are not supported in Gregorian)
        assertEquals("Eras should not be supported", false, gregorian.eras().isSupported());
        assertEquals("Centuries should be supported", true, gregorian.centuries().isSupported());
        assertEquals("Years should be supported", true, gregorian.years().isSupported());
        assertEquals("Weekyears should be supported", true, gregorian.weekyears().isSupported());
        assertEquals("Months should be supported", true, gregorian.months().isSupported());
        assertEquals("Weeks should be supported", true, gregorian.weeks().isSupported());
        assertEquals("Days should be supported", true, gregorian.days().isSupported());
        assertEquals("Halfdays should be supported", true, gregorian.halfdays().isSupported());
        assertEquals("Hours should be supported", true, gregorian.hours().isSupported());
        assertEquals("Minutes should be supported", true, gregorian.minutes().isSupported());
        assertEquals("Seconds should be supported", true, gregorian.seconds().isSupported());
        assertEquals("Millis should be supported", true, gregorian.millis().isSupported());
    }

    public void testDurationFields_PrecisionInTimezone() {
        final GregorianChronology gregorianWithTimezone = GregorianChronology.getInstance();
        
        // In timezone with DST, larger duration fields are not precise due to timezone changes
        assertEquals("Centuries not precise due to timezone", false, gregorianWithTimezone.centuries().isPrecise());
        assertEquals("Years not precise due to timezone", false, gregorianWithTimezone.years().isPrecise());
        assertEquals("Weekyears not precise due to timezone", false, gregorianWithTimezone.weekyears().isPrecise());
        assertEquals("Months not precise due to timezone", false, gregorianWithTimezone.months().isPrecise());
        assertEquals("Weeks not precise due to timezone", false, gregorianWithTimezone.weeks().isPrecise());
        assertEquals("Days not precise due to timezone", false, gregorianWithTimezone.days().isPrecise());
        assertEquals("Halfdays not precise due to timezone", false, gregorianWithTimezone.halfdays().isPrecise());
        
        // Time fields are precise
        assertEquals("Hours should be precise", true, gregorianWithTimezone.hours().isPrecise());
        assertEquals("Minutes should be precise", true, gregorianWithTimezone.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, gregorianWithTimezone.seconds().isPrecise());
        assertEquals("Millis should be precise", true, gregorianWithTimezone.millis().isPrecise());
    }

    public void testDurationFields_PrecisionInUTC() {
        final GregorianChronology gregorianUTC = GregorianChronology.getInstanceUTC();
        
        // In UTC, variable-length fields are still not precise
        assertEquals("Centuries not precise (variable length)", false, gregorianUTC.centuries().isPrecise());
        assertEquals("Years not precise (leap years)", false, gregorianUTC.years().isPrecise());
        assertEquals("Weekyears not precise (variable length)", false, gregorianUTC.weekyears().isPrecise());
        assertEquals("Months not precise (variable length)", false, gregorianUTC.months().isPrecise());
        
        // Fixed-length fields are precise in UTC
        assertEquals("Weeks should be precise in UTC", true, gregorianUTC.weeks().isPrecise());
        assertEquals("Days should be precise in UTC", true, gregorianUTC.days().isPrecise());
        assertEquals("Halfdays should be precise in UTC", true, gregorianUTC.halfdays().isPrecise());
        assertEquals("Hours should be precise in UTC", true, gregorianUTC.hours().isPrecise());
        assertEquals("Minutes should be precise in UTC", true, gregorianUTC.minutes().isPrecise());
        assertEquals("Seconds should be precise in UTC", true, gregorianUTC.seconds().isPrecise());
        assertEquals("Millis should be precise in UTC", true, gregorianUTC.millis().isPrecise());
    }

    public void testDurationFields_PrecisionInFixedOffsetTimezone() {
        final DateTimeZone gmtTimezone = DateTimeZone.forID("Etc/GMT");
        final GregorianChronology gregorianGMT = GregorianChronology.getInstance(gmtTimezone);
        
        // In fixed offset timezone (no DST), behavior is like UTC
        assertEquals("Centuries not precise (variable length)", false, gregorianGMT.centuries().isPrecise());
        assertEquals("Years not precise (leap years)", false, gregorianGMT.years().isPrecise());
        assertEquals("Weekyears not precise (variable length)", false, gregorianGMT.weekyears().isPrecise());
        assertEquals("Months not precise (variable length)", false, gregorianGMT.months().isPrecise());
        assertEquals("Weeks should be precise in fixed offset", true, gregorianGMT.weeks().isPrecise());
        assertEquals("Days should be precise in fixed offset", true, gregorianGMT.days().isPrecise());
        assertEquals("Halfdays should be precise in fixed offset", true, gregorianGMT.halfdays().isPrecise());
        assertEquals("Hours should be precise in fixed offset", true, gregorianGMT.hours().isPrecise());
        assertEquals("Minutes should be precise in fixed offset", true, gregorianGMT.minutes().isPrecise());
        assertEquals("Seconds should be precise in fixed offset", true, gregorianGMT.seconds().isPrecise());
        assertEquals("Millis should be precise in fixed offset", true, gregorianGMT.millis().isPrecise());
    }

    //-----------------------------------------------------------------------
    // Date Field Tests
    //-----------------------------------------------------------------------
    
    public void testDateFields_NamesAndSupport() {
        final GregorianChronology gregorian = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("era", gregorian.era().getName());
        assertEquals("centuryOfEra", gregorian.centuryOfEra().getName());
        assertEquals("yearOfCentury", gregorian.yearOfCentury().getName());
        assertEquals("yearOfEra", gregorian.yearOfEra().getName());
        assertEquals("year", gregorian.year().getName());
        assertEquals("monthOfYear", gregorian.monthOfYear().getName());
        assertEquals("weekyearOfCentury", gregorian.weekyearOfCentury().getName());
        assertEquals("weekyear", gregorian.weekyear().getName());
        assertEquals("weekOfWeekyear", gregorian.weekOfWeekyear().getName());
        assertEquals("dayOfYear", gregorian.dayOfYear().getName());
        assertEquals("dayOfMonth", gregorian.dayOfMonth().getName());
        assertEquals("dayOfWeek", gregorian.dayOfWeek().getName());
        
        // All date fields should be supported
        assertEquals("Era should be supported", true, gregorian.era().isSupported());
        assertEquals("Century of era should be supported", true, gregorian.centuryOfEra().isSupported());
        assertEquals("Year of century should be supported", true, gregorian.yearOfCentury().isSupported());
        assertEquals("Year of era should be supported", true, gregorian.yearOfEra().isSupported());
        assertEquals("Year should be supported", true, gregorian.year().isSupported());
        assertEquals("Month of year should be supported", true, gregorian.monthOfYear().isSupported());
        assertEquals("Weekyear of century should be supported", true, gregorian.weekyearOfCentury().isSupported());
        assertEquals("Weekyear should be supported", true, gregorian.weekyear().isSupported());
        assertEquals("Week of weekyear should be supported", true, gregorian.weekOfWeekyear().isSupported());
        assertEquals("Day of year should be supported", true, gregorian.dayOfYear().isSupported());
        assertEquals("Day of month should be supported", true, gregorian.dayOfMonth().isSupported());
        assertEquals("Day of week should be supported", true, gregorian.dayOfWeek().isSupported());
    }

    public void testDateFields_DurationFieldRelationships() {
        final GregorianChronology gregorian = GregorianChronology.getInstance();
        
        // Test duration field relationships
        assertEquals(gregorian.eras(), gregorian.era().getDurationField());
        assertEquals(gregorian.centuries(), gregorian.centuryOfEra().getDurationField());
        assertEquals(gregorian.years(), gregorian.yearOfCentury().getDurationField());
        assertEquals(gregorian.years(), gregorian.yearOfEra().getDurationField());
        assertEquals(gregorian.years(), gregorian.year().getDurationField());
        assertEquals(gregorian.months(), gregorian.monthOfYear().getDurationField());
        assertEquals(gregorian.weekyears(), gregorian.weekyearOfCentury().getDurationField());
        assertEquals(gregorian.weekyears(), gregorian.weekyear().getDurationField());
        assertEquals(gregorian.weeks(), gregorian.weekOfWeekyear().getDurationField());
        assertEquals(gregorian.days(), gregorian.dayOfYear().getDurationField());
        assertEquals(gregorian.days(), gregorian.dayOfMonth().getDurationField());
        assertEquals(gregorian.days(), gregorian.dayOfWeek().getDurationField());
    }

    public void testDateFields_RangeDurationFieldRelationships() {
        final GregorianChronology gregorian = GregorianChronology.getInstance();
        
        // Test range duration field relationships
        assertEquals("Era has no range", null, gregorian.era().getRangeDurationField());
        assertEquals(gregorian.eras(), gregorian.centuryOfEra().getRangeDurationField());
        assertEquals(gregorian.centuries(), gregorian.yearOfCentury().getRangeDurationField());
        assertEquals(gregorian.eras(), gregorian.yearOfEra().getRangeDurationField());
        assertEquals("Year has no range", null, gregorian.year().getRangeDurationField());
        assertEquals(gregorian.years(), gregorian.monthOfYear().getRangeDurationField());
        assertEquals(gregorian.centuries(), gregorian.weekyearOfCentury().getRangeDurationField());
        assertEquals("Weekyear has no range", null, gregorian.weekyear().getRangeDurationField());
        assertEquals(gregorian.weekyears(), gregorian.weekOfWeekyear().getRangeDurationField());
        assertEquals(gregorian.years(), gregorian.dayOfYear().getRangeDurationField());
        assertEquals(gregorian.months(), gregorian.dayOfMonth().getRangeDurationField());
        assertEquals(gregorian.weeks(), gregorian.dayOfWeek().getRangeDurationField());
    }

    //-----------------------------------------------------------------------
    // Time Field Tests
    //-----------------------------------------------------------------------
    
    public void testTimeFields_NamesAndSupport() {
        final GregorianChronology gregorian = GregorianChronology.getInstance();
        
        // Test field names
        assertEquals("halfdayOfDay", gregorian.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", gregorian.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", gregorian.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", gregorian.clockhourOfDay().getName());
        assertEquals("hourOfDay", gregorian.hourOfDay().getName());
        assertEquals("minuteOfDay", gregorian.minuteOfDay().getName());
        assertEquals("minuteOfHour", gregorian.minuteOfHour().getName());
        assertEquals("secondOfDay", gregorian.secondOfDay().getName());
        assertEquals("secondOfMinute", gregorian.secondOfMinute().getName());
        assertEquals("millisOfDay", gregorian.millisOfDay().getName());
        assertEquals("millisOfSecond", gregorian.millisOfSecond().getName());
        
        // All time fields should be supported
        assertEquals("Halfday of day should be supported", true, gregorian.halfdayOfDay().isSupported());
        assertEquals("Clockhour of halfday should be supported", true, gregorian.clockhourOfHalfday().isSupported());
        assertEquals("Hour of halfday should be supported", true, gregorian.hourOfHalfday().isSupported());
        assertEquals("Clockhour of day should be supported", true, gregorian.clockhourOfDay().isSupported());
        assertEquals("Hour of day should be supported", true, gregorian.hourOfDay().isSupported());
        assertEquals("Minute of day should be supported", true, gregorian.minuteOfDay().isSupported());
        assertEquals("Minute of hour should be supported", true, gregorian.minuteOfHour().isSupported());
        assertEquals("Second of day should be supported", true, gregorian.secondOfDay().isSupported());
        assertEquals("Second of minute should be supported", true, gregorian.secondOfMinute().isSupported());
        assertEquals("Millis of day should be supported", true, gregorian.millisOfDay().isSupported());
        assertEquals("Millis of second should be supported", true, gregorian.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    // Leap Year and Calendar Logic Tests
    //-----------------------------------------------------------------------
    
    public void testMaximumValue_February() {
        YearMonthDay february1999 = new YearMonthDay(1999, DateTimeConstants.FEBRUARY, 1);
        DateMidnight february1999Midnight = new DateMidnight(1999, DateTimeConstants.FEBRUARY, 1);
        Chronology chronology = GregorianChronology.getInstance();
        
        assertEquals("February 1999 should have 28 days (non-leap year)", 
                     28, chronology.dayOfMonth().getMaximumValue(february1999));
        assertEquals("February 1999 should have 28 days (non-leap year)", 
                     28, chronology.dayOfMonth().getMaximumValue(february1999Midnight.getMillis()));
    }

    public void testLeapYear_February28() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTime february28_2012 = new DateTime(2012, 2, 28, 0, 0, chronology);
        
        assertEquals("2012 should be a leap year", true, february28_2012.year().isLeap());
        assertEquals("February should be leap in leap year", true, february28_2012.monthOfYear().isLeap());
        assertEquals("February 28th is not the leap day", false, february28_2012.dayOfMonth().isLeap());
        assertEquals("February 28th is not the leap day of year", false, february28_2012.dayOfYear().isLeap());
    }

    public void testLeapYear_February29() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTime february29_2012 = new DateTime(2012, 2, 29, 0, 0, chronology);
        
        assertEquals("2012 should be a leap year", true, february29_2012.year().isLeap());
        assertEquals("February should be leap in leap year", true, february29_2012.monthOfYear().isLeap());
        assertEquals("February 29th is the leap day of month", true, february29_2012.dayOfMonth().isLeap());
        assertEquals("February 29th is the leap day of year", true, february29_2012.dayOfYear().isLeap());
    }

    //-----------------------------------------------------------------------
    // Helper Methods
    //-----------------------------------------------------------------------
    
    /**
     * Calculates milliseconds for June 9, 2002 from days since epoch.
     * This replaces the complex inline calculation for better readability.
     */
    private static long calculateJune9_2002Millis() {
        // Days from year 0 to 2002: 32 years * 365.25 average days per year
        long daysFrom0To2002 = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                               366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                               365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                               366 + 365;
        
        // Add days for January through May, plus 9 days into June, minus 1 for zero-based
        long daysToJune9 = daysFrom0To2002 + 31L + 28L + 31L + 30L + 31L + 9L - 1L;
        
        return daysToJune9 * DateTimeConstants.MILLIS_PER_DAY;
    }
}