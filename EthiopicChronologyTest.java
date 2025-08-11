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
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

/**
 * Comprehensive test suite for EthiopicChronology.
 * 
 * The Ethiopic calendar is similar to Julian calendar with:
 * - 12 months of 30 days each
 * - 13th month of 5 days (6 in leap years)
 * - Every 4th year is a leap year (year % 4 == 3)
 * - Year 1 EE began on August 29, 8 CE (Julian)
 *
 * @author Stephen Colebourne
 */
public class TestEthiopicChronology extends TestCase {

    // ========================================================================
    // Test Constants and Setup
    // ========================================================================
    
    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long TEST_SKIP_INTERVAL = 1 * MILLIS_PER_DAY;
    
    // Time zones for testing
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
    // Chronologies for comparison and testing
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Fixed test time: June 9, 2002 (equivalent to Sene 2, 1994 in Ethiopic calendar)
    private static final long TEST_TIME_NOW = calculateTestTime();
    
    // Test environment state preservation
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    /**
     * Calculates the test time as milliseconds since epoch.
     * This represents June 9, 2002 in the Gregorian calendar.
     */
    private static long calculateTestTime() {
        // Days from year 1 to 2002
        long totalDays = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                        366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                        365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                        366 + 365;
        // Add days for January through June 9, 2002
        long daysInYear2002 = 31L + 28L + 31L + 30L + 31L + 9L - 1L;
        return (totalDays + daysInYear2002) * MILLIS_PER_DAY;
    }

    // ========================================================================
    // Test Suite Infrastructure
    // ========================================================================

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestEthiopicChronology.class);
    }

    public TestEthiopicChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Fix current time for consistent test results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        
        // Preserve original environment settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set consistent test environment
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore system time
        DateTimeUtils.setCurrentMillisSystem();
        
        // Restore original environment settings
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        
        // Clear references
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    // ========================================================================
    // Factory Method Tests
    // ========================================================================

    public void testFactoryUTC_ReturnsUTCChronologyInstance() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        assertEquals("Should use UTC timezone", DateTimeZone.UTC, chronology.getZone());
        assertSame("Should return EthiopicChronology instance", 
                  EthiopicChronology.class, chronology.getClass());
    }

    public void testFactory_ReturnsDefaultTimezoneInstance() {
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        
        assertEquals("Should use default timezone", LONDON, chronology.getZone());
        assertSame("Should return EthiopicChronology instance", 
                  EthiopicChronology.class, chronology.getClass());
    }

    public void testFactoryWithZone_ReturnsCorrectTimezoneInstance() {
        assertEquals("Tokyo timezone should be preserved", 
                    TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals("Paris timezone should be preserved", 
                    PARIS, EthiopicChronology.getInstance(PARIS).getZone());
        assertEquals("Null timezone should default to London", 
                    LONDON, EthiopicChronology.getInstance(null).getZone());
        assertSame("Should return EthiopicChronology instance", 
                  EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }

    // ========================================================================
    // Instance Management Tests
    // ========================================================================

    public void testEquality_SameTimezonesReturnSameInstance() {
        assertSame("Tokyo instances should be identical", 
                  EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame("London instances should be identical", 
                  EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame("Paris instances should be identical", 
                  EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(PARIS));
        assertSame("UTC instances should be identical", 
                  EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
        assertSame("Default should match London timezone", 
                  EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }

    public void testWithUTC_ConvertsToUTCChronology() {
        EthiopicChronology utcExpected = EthiopicChronology.getInstanceUTC();
        
        assertSame("London chronology should convert to UTC", 
                  utcExpected, EthiopicChronology.getInstance(LONDON).withUTC());
        assertSame("Tokyo chronology should convert to UTC", 
                  utcExpected, EthiopicChronology.getInstance(TOKYO).withUTC());
        assertSame("UTC chronology should return itself", 
                  utcExpected, EthiopicChronology.getInstanceUTC().withUTC());
        assertSame("Default chronology should convert to UTC", 
                  utcExpected, EthiopicChronology.getInstance().withUTC());
    }

    public void testWithZone_ConvertsToSpecifiedTimezone() {
        EthiopicChronology baseChronology = EthiopicChronology.getInstance(TOKYO);
        
        assertSame("Same timezone should return same instance", 
                  EthiopicChronology.getInstance(TOKYO), baseChronology.withZone(TOKYO));
        assertSame("Should convert to London timezone", 
                  EthiopicChronology.getInstance(LONDON), baseChronology.withZone(LONDON));
        assertSame("Should convert to Paris timezone", 
                  EthiopicChronology.getInstance(PARIS), baseChronology.withZone(PARIS));
        assertSame("Null timezone should default to London", 
                  EthiopicChronology.getInstance(LONDON), baseChronology.withZone(null));
        assertSame("Default chronology should convert to Paris", 
                  EthiopicChronology.getInstance(PARIS), 
                  EthiopicChronology.getInstance().withZone(PARIS));
        assertSame("UTC chronology should convert to Paris", 
                  EthiopicChronology.getInstance(PARIS), 
                  EthiopicChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString_DisplaysCorrectFormat() {
        assertEquals("London chronology toString", 
                    "EthiopicChronology[Europe/London]", 
                    EthiopicChronology.getInstance(LONDON).toString());
        assertEquals("Tokyo chronology toString", 
                    "EthiopicChronology[Asia/Tokyo]", 
                    EthiopicChronology.getInstance(TOKYO).toString());
        assertEquals("Default chronology toString", 
                    "EthiopicChronology[Europe/London]", 
                    EthiopicChronology.getInstance().toString());
        assertEquals("UTC chronology toString", 
                    "EthiopicChronology[UTC]", 
                    EthiopicChronology.getInstanceUTC().toString());
    }

    // ========================================================================
    // Duration Field Tests
    // ========================================================================

    public void testDurationFields_NamesAndSupport() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Verify field names
        assertEquals("eras", ethiopic.eras().getName());
        assertEquals("centuries", ethiopic.centuries().getName());
        assertEquals("years", ethiopic.years().getName());
        assertEquals("weekyears", ethiopic.weekyears().getName());
        assertEquals("months", ethiopic.months().getName());
        assertEquals("weeks", ethiopic.weeks().getName());
        assertEquals("days", ethiopic.days().getName());
        assertEquals("halfdays", ethiopic.halfdays().getName());
        assertEquals("hours", ethiopic.hours().getName());
        assertEquals("minutes", ethiopic.minutes().getName());
        assertEquals("seconds", ethiopic.seconds().getName());
        assertEquals("millis", ethiopic.millis().getName());
        
        // Verify support status
        assertEquals("Eras should not be supported", false, ethiopic.eras().isSupported());
        assertEquals("Centuries should be supported", true, ethiopic.centuries().isSupported());
        assertEquals("Years should be supported", true, ethiopic.years().isSupported());
        assertEquals("Weekyears should be supported", true, ethiopic.weekyears().isSupported());
        assertEquals("Months should be supported", true, ethiopic.months().isSupported());
        assertEquals("Weeks should be supported", true, ethiopic.weeks().isSupported());
        assertEquals("Days should be supported", true, ethiopic.days().isSupported());
        assertEquals("Halfdays should be supported", true, ethiopic.halfdays().isSupported());
        assertEquals("Hours should be supported", true, ethiopic.hours().isSupported());
        assertEquals("Minutes should be supported", true, ethiopic.minutes().isSupported());
        assertEquals("Seconds should be supported", true, ethiopic.seconds().isSupported());
        assertEquals("Millis should be supported", true, ethiopic.millis().isSupported());
    }

    public void testDurationFields_PrecisionWithTimezone() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Time-zone dependent fields are imprecise due to DST
        assertEquals("Centuries should be imprecise with timezone", false, ethiopic.centuries().isPrecise());
        assertEquals("Years should be imprecise with timezone", false, ethiopic.years().isPrecise());
        assertEquals("Weekyears should be imprecise with timezone", false, ethiopic.weekyears().isPrecise());
        assertEquals("Months should be imprecise with timezone", false, ethiopic.months().isPrecise());
        assertEquals("Weeks should be imprecise with timezone", false, ethiopic.weeks().isPrecise());
        assertEquals("Days should be imprecise with timezone", false, ethiopic.days().isPrecise());
        assertEquals("Halfdays should be imprecise with timezone", false, ethiopic.halfdays().isPrecise());
        
        // Sub-day fields are always precise
        assertEquals("Hours should be precise", true, ethiopic.hours().isPrecise());
        assertEquals("Minutes should be precise", true, ethiopic.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, ethiopic.seconds().isPrecise());
        assertEquals("Millis should be precise", true, ethiopic.millis().isPrecise());
    }

    public void testDurationFields_PrecisionWithUTC() {
        final EthiopicChronology ethiopicUTC = EthiopicChronology.getInstanceUTC();
        
        // Variable-length fields remain imprecise even in UTC
        assertEquals("Centuries should be imprecise", false, ethiopicUTC.centuries().isPrecise());
        assertEquals("Years should be imprecise", false, ethiopicUTC.years().isPrecise());
        assertEquals("Weekyears should be imprecise", false, ethiopicUTC.weekyears().isPrecise());
        assertEquals("Months should be imprecise", false, ethiopicUTC.months().isPrecise());
        
        // Fixed-length fields are precise in UTC
        assertEquals("Weeks should be precise in UTC", true, ethiopicUTC.weeks().isPrecise());
        assertEquals("Days should be precise in UTC", true, ethiopicUTC.days().isPrecise());
        assertEquals("Halfdays should be precise in UTC", true, ethiopicUTC.halfdays().isPrecise());
        assertEquals("Hours should be precise", true, ethiopicUTC.hours().isPrecise());
        assertEquals("Minutes should be precise", true, ethiopicUTC.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, ethiopicUTC.seconds().isPrecise());
        assertEquals("Millis should be precise", true, ethiopicUTC.millis().isPrecise());
    }

    public void testDurationFields_PrecisionWithFixedOffsetTimezone() {
        final DateTimeZone fixedGMT = DateTimeZone.forID("Etc/GMT");
        final EthiopicChronology ethiopicGMT = EthiopicChronology.getInstance(fixedGMT);
        
        // Variable-length fields remain imprecise
        assertEquals("Centuries should be imprecise", false, ethiopicGMT.centuries().isPrecise());
        assertEquals("Years should be imprecise", false, ethiopicGMT.years().isPrecise());
        assertEquals("Weekyears should be imprecise", false, ethiopicGMT.weekyears().isPrecise());
        assertEquals("Months should be imprecise", false, ethiopicGMT.months().isPrecise());
        
        // Fixed-length fields are precise with fixed offset
        assertEquals("Weeks should be precise with fixed offset", true, ethiopicGMT.weeks().isPrecise());
        assertEquals("Days should be precise with fixed offset", true, ethiopicGMT.days().isPrecise());
        assertEquals("Halfdays should be precise with fixed offset", true, ethiopicGMT.halfdays().isPrecise());
        assertEquals("Hours should be precise", true, ethiopicGMT.hours().isPrecise());
        assertEquals("Minutes should be precise", true, ethiopicGMT.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, ethiopicGMT.seconds().isPrecise());
        assertEquals("Millis should be precise", true, ethiopicGMT.millis().isPrecise());
    }

    // ========================================================================
    // Date Field Tests
    // ========================================================================

    public void testDateFields_NamesAndSupport() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Verify field names
        assertEquals("era", ethiopic.era().getName());
        assertEquals("centuryOfEra", ethiopic.centuryOfEra().getName());
        assertEquals("yearOfCentury", ethiopic.yearOfCentury().getName());
        assertEquals("yearOfEra", ethiopic.yearOfEra().getName());
        assertEquals("year", ethiopic.year().getName());
        assertEquals("monthOfYear", ethiopic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", ethiopic.weekyearOfCentury().getName());
        assertEquals("weekyear", ethiopic.weekyear().getName());
        assertEquals("weekOfWeekyear", ethiopic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", ethiopic.dayOfYear().getName());
        assertEquals("dayOfMonth", ethiopic.dayOfMonth().getName());
        assertEquals("dayOfWeek", ethiopic.dayOfWeek().getName());
        
        // All date fields should be supported
        assertEquals("Era should be supported", true, ethiopic.era().isSupported());
        assertEquals("Century of era should be supported", true, ethiopic.centuryOfEra().isSupported());
        assertEquals("Year of century should be supported", true, ethiopic.yearOfCentury().isSupported());
        assertEquals("Year of era should be supported", true, ethiopic.yearOfEra().isSupported());
        assertEquals("Year should be supported", true, ethiopic.year().isSupported());
        assertEquals("Month of year should be supported", true, ethiopic.monthOfYear().isSupported());
        assertEquals("Weekyear of century should be supported", true, ethiopic.weekyearOfCentury().isSupported());
        assertEquals("Weekyear should be supported", true, ethiopic.weekyear().isSupported());
        assertEquals("Week of weekyear should be supported", true, ethiopic.weekOfWeekyear().isSupported());
        assertEquals("Day of year should be supported", true, ethiopic.dayOfYear().isSupported());
        assertEquals("Day of month should be supported", true, ethiopic.dayOfMonth().isSupported());
        assertEquals("Day of week should be supported", true, ethiopic.dayOfWeek().isSupported());
    }

    public void testDateFields_DurationFieldAssociations() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Verify each field uses the correct duration field
        assertEquals("Era duration field", ethiopic.eras(), ethiopic.era().getDurationField());
        assertEquals("Century duration field", ethiopic.centuries(), ethiopic.centuryOfEra().getDurationField());
        assertEquals("Year of century duration field", ethiopic.years(), ethiopic.yearOfCentury().getDurationField());
        assertEquals("Year of era duration field", ethiopic.years(), ethiopic.yearOfEra().getDurationField());
        assertEquals("Year duration field", ethiopic.years(), ethiopic.year().getDurationField());
        assertEquals("Month duration field", ethiopic.months(), ethiopic.monthOfYear().getDurationField());
        assertEquals("Weekyear of century duration field", ethiopic.weekyears(), ethiopic.weekyearOfCentury().getDurationField());
        assertEquals("Weekyear duration field", ethiopic.weekyears(), ethiopic.weekyear().getDurationField());
        assertEquals("Week duration field", ethiopic.weeks(), ethiopic.weekOfWeekyear().getDurationField());
        assertEquals("Day of year duration field", ethiopic.days(), ethiopic.dayOfYear().getDurationField());
        assertEquals("Day of month duration field", ethiopic.days(), ethiopic.dayOfMonth().getDurationField());
        assertEquals("Day of week duration field", ethiopic.days(), ethiopic.dayOfWeek().getDurationField());
    }

    public void testDateFields_RangeDurationFieldAssociations() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Verify range duration field associations
        assertEquals("Era range duration field", null, ethiopic.era().getRangeDurationField());
        assertEquals("Century range duration field", ethiopic.eras(), ethiopic.centuryOfEra().getRangeDurationField());
        assertEquals("Year of century range duration field", ethiopic.centuries(), ethiopic.yearOfCentury().getRangeDurationField());
        assertEquals("Year of era range duration field", ethiopic.eras(), ethiopic.yearOfEra().getRangeDurationField());
        assertEquals("Year range duration field", null, ethiopic.year().getRangeDurationField());
        assertEquals("Month range duration field", ethiopic.years(), ethiopic.monthOfYear().getRangeDurationField());
        assertEquals("Weekyear of century range duration field", ethiopic.centuries(), ethiopic.weekyearOfCentury().getRangeDurationField());
        assertEquals("Weekyear range duration field", null, ethiopic.weekyear().getRangeDurationField());
        assertEquals("Week range duration field", ethiopic.weekyears(), ethiopic.weekOfWeekyear().getRangeDurationField());
        assertEquals("Day of year range duration field", ethiopic.years(), ethiopic.dayOfYear().getRangeDurationField());
        assertEquals("Day of month range duration field", ethiopic.months(), ethiopic.dayOfMonth().getRangeDurationField());
        assertEquals("Day of week range duration field", ethiopic.weeks(), ethiopic.dayOfWeek().getRangeDurationField());
    }

    // ========================================================================
    // Time Field Tests
    // ========================================================================

    public void testTimeFields_NamesAndSupport() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
        
        // Verify field names
        assertEquals("halfdayOfDay", ethiopic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", ethiopic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", ethiopic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", ethiopic.clockhourOfDay().getName());
        assertEquals("hourOfDay", ethiopic.hourOfDay().getName());
        assertEquals("minuteOfDay", ethiopic.minuteOfDay().getName());
        assertEquals("minuteOfHour", ethiopic.minuteOfHour().getName());
        assertEquals("secondOfDay", ethiopic.secondOfDay().getName());
        assertEquals("secondOfMinute", ethiopic.secondOfMinute().getName());
        assertEquals("millisOfDay", ethiopic.millisOfDay().getName());
        assertEquals("millisOfSecond", ethiopic.millisOfSecond().getName());
        
        // All time fields should be supported
        assertEquals("Halfday of day should be supported", true, ethiopic.halfdayOfDay().isSupported());
        assertEquals("Clockhour of halfday should be supported", true, ethiopic.clockhourOfHalfday().isSupported());
        assertEquals("Hour of halfday should be supported", true, ethiopic.hourOfHalfday().isSupported());
        assertEquals("Clockhour of day should be supported", true, ethiopic.clockhourOfDay().isSupported());
        assertEquals("Hour of day should be supported", true, ethiopic.hourOfDay().isSupported());
        assertEquals("Minute of day should be supported", true, ethiopic.minuteOfDay().isSupported());
        assertEquals("Minute of hour should be supported", true, ethiopic.minuteOfHour().isSupported());
        assertEquals("Second of day should be supported", true, ethiopic.secondOfDay().isSupported());
        assertEquals("Second of minute should be supported", true, ethiopic.secondOfMinute().isSupported());
        assertEquals("Millis of day should be supported", true, ethiopic.millisOfDay().isSupported());
        assertEquals("Millis of second should be supported", true, ethiopic.millisOfSecond().isSupported());
    }

    // ========================================================================
    // Epoch and Era Tests
    // ========================================================================

    public void testEpoch_CorrespondsToJulianDate() {
        // Ethiopic year 1, month 1, day 1 should correspond to Julian August 29, 8 CE
        DateTime ethiopicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime expectedJulianDate = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC);
        
        assertEquals("Ethiopic epoch should match Julian date", 
                    expectedJulianDate, ethiopicEpoch.withChronology(JULIAN_UTC));
    }

    public void testEra_OnlyPositiveEraSupported() {
        assertEquals("EE era constant", 1, EthiopicChronology.EE);
        
        try {
            // Attempt to create date before era 1 (negative year)
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
            fail("Should not allow dates before era 1");
        } catch (IllegalArgumentException expected) {
            // This is expected behavior
        }
    }

    // ========================================================================
    // Comprehensive Calendar Tests
    // ========================================================================

    /**
     * Tests the complete calendar system including era, year, monthOfYear, 
     * dayOfMonth and dayOfWeek calculations across a significant time range.
     */
    public void testCalendar_ComprehensiveValidation() {
        if (TestAll.FAST) {
            return; // Skip intensive test in fast mode
        }
        
        System.out.println("\nTestEthiopicChronology.testCalendar - Running comprehensive validation");
        
        DateTime startDate = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime endDate = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC);
        
        long currentMillis = startDate.getMillis();
        long endMillis = endDate.getMillis();
        
        // Get field references for performance
        DateTimeField dayOfWeek = ETHIOPIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ETHIOPIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ETHIOPIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ETHIOPIC_UTC.monthOfYear();
        DateTimeField year = ETHIOPIC_UTC.year();
        DateTimeField yearOfEra = ETHIOPIC_UTC.yearOfEra();
        DateTimeField era = ETHIOPIC_UTC.era();
        
        // Initialize expected values based on epoch correspondence
        int expectedDayOfWeek = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDayOfYear = 1;
        int expectedDayOfMonth = 1;
        int expectedMonthOfYear = 1;
        int expectedYear = 1;
        
        while (currentMillis < endMillis) {
            validateCalendarFieldsAtInstant(currentMillis, era, year, yearOfEra, monthOfYear, 
                                          dayOfMonth, dayOfYear, dayOfWeek,
                                          expectedYear, expectedMonthOfYear, expectedDayOfMonth, 
                                          expectedDayOfYear, expectedDayOfWeek);
            
            // Advance to next day and update expected values
            currentMillis += TEST_SKIP_INTERVAL;
            updateExpectedCalendarValues(expectedYear, expectedMonthOfYear, expectedDayOfMonth,
                                       expectedDayOfYear, expectedDayOfWeek);
        }
    }

    private void validateCalendarFieldsAtInstant(long instant, DateTimeField era, DateTimeField year, 
                                               DateTimeField yearOfEra, DateTimeField monthOfYear,
                                               DateTimeField dayOfMonth, DateTimeField dayOfYear, 
                                               DateTimeField dayOfWeek,
                                               int expectedYear, int expectedMonth, int expectedDay,
                                               int expectedDOY, int expectedDOW) {
        
        int actualMonth = monthOfYear.get(instant);
        
        // Validate month range
        if (actualMonth < 1 || actualMonth > 13) {
            fail("Invalid month value: " + actualMonth + " at instant: " + instant);
        }
        
        // Test era
        assertEquals("Era should be EE", 1, era.get(instant));
        assertEquals("Era text should be EE", "EE", era.getAsText(instant));
        assertEquals("Era short text should be EE", "EE", era.getAsShortText(instant));
        
        // Test date field values
        assertEquals("Year mismatch", expectedYear, year.get(instant));
        assertEquals("Year of era mismatch", expectedYear, yearOfEra.get(instant));
        assertEquals("Month mismatch", expectedMonth, actualMonth);
        assertEquals("Day mismatch", expectedDay, dayOfMonth.get(instant));
        assertEquals("Day of week mismatch", expectedDOW, dayOfWeek.get(instant));
        assertEquals("Day of year mismatch", expectedDOY, dayOfYear.get(instant));
        
        // Test leap year calculation (every 4th year when year % 4 == 3)
        boolean expectedLeapYear = expectedYear % 4 == 3;
        assertEquals("Leap year calculation", expectedLeapYear, year.isLeap(instant));
        
        // Test month length
        validateMonthLength(instant, actualMonth, expectedLeapYear, monthOfYear, dayOfMonth);
    }

    private void validateMonthLength(long instant, int month, boolean isLeapYear, 
                                   DateTimeField monthOfYear, DateTimeField dayOfMonth) {
        int monthLength = dayOfMonth.getMaximumValue(instant);
        
        if (month == 13) {
            // 13th month (Pagumē): 5 days normally, 6 in leap years
            assertEquals("13th month leap status", isLeapYear, monthOfYear.isLeap(instant));
            if (isLeapYear) {
                assertEquals("13th month length in leap year", 6, monthLength);
            } else {
                assertEquals("13th month length in regular year", 5, monthLength);
            }
        } else {
            // Regular months: always 30 days
            assertEquals("Regular month length", 30, monthLength);
        }
    }

    private int[] updateExpectedCalendarValues(int year, int month, int day, int dayOfYear, int dayOfWeek) {
        // Advance day of week (1-7 cycle)
        int newDayOfWeek = (dayOfWeek % 7) + 1;
        
        // Advance day and day of year
        int newDay = day + 1;
        int newDayOfYear = dayOfYear + 1;
        int newMonth = month;
        int newYear = year;
        
        // Check if we need to advance month
        if (newDay == 31 && month < 13) {
            // Regular months have 30 days, advance to next month
            newDay = 1;
            newMonth++;
        } else if (month == 13) {
            // Handle 13th month transitions
            boolean isLeapYear = year % 4 == 3;
            int maxDaysInMonth13 = isLeapYear ? 6 : 5;
            
            if (newDay > maxDaysInMonth13) {
                // Advance to next year
                newDay = 1;
                newMonth = 1;
                newYear++;
                newDayOfYear = 1;
            }
        }
        
        return new int[]{newYear, newMonth, newDay, newDayOfYear, newDayOfWeek};
    }

    // ========================================================================
    // Specific Date Sample Tests
    // ========================================================================

    public void testSampleDate_June9_2004_Conversion() {
        // Test conversion of June 9, 2004 (ISO) to Ethiopic calendar
        DateTime gregorianDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC);
        DateTime ethiopicDate = gregorianDate.withChronology(ETHIOPIC_UTC);
        
        // Verify basic date components
        assertEquals("Era should be EE", EthiopicChronology.EE, ethiopicDate.getEra());
        assertEquals("Century of era", 20, ethiopicDate.getCenturyOfEra());
        assertEquals("Year of century", 96, ethiopicDate.getYearOfCentury());
        assertEquals("Year of era", 1996, ethiopicDate.getYearOfEra());
        assertEquals("Year", 1996, ethiopicDate.getYear());
        assertEquals("Month of year", 10, ethiopicDate.getMonthOfYear());
        assertEquals("Day of month", 2, ethiopicDate.getDayOfMonth());
        assertEquals("Day of week", DateTimeConstants.WEDNESDAY, ethiopicDate.getDayOfWeek());
        assertEquals("Day of year", 9 * 30 + 2, ethiopicDate.getDayOfYear()); // 272
        
        // Verify time components remain unchanged
        assertEquals("Hour of day", 0, ethiopicDate.getHourOfDay());
        assertEquals("Minute of hour", 0, ethiopicDate.getMinuteOfHour());
        assertEquals("Second of minute", 0, ethiopicDate.getSecondOfMinute());
        assertEquals("Millis of second", 0, ethiopicDate.getMillisOfSecond());
    }

    public void testSampleDate_YearFieldProperties() {
        DateTime ethiopicDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);
        Property yearField = ethiopicDate.year();
        
        // Year 1996 EE is not a leap year (1996 % 4 != 3)
        assertEquals("Year should not be leap", false, yearField.isLeap());
        assertEquals("Leap amount should be 0", 0, yearField.getLeapAmount());
        assertEquals("Leap duration field type", DurationFieldType.days(), 
                    yearField.getLeapDurationField().getType());
        
        // Test year arithmetic
        DateTime nextYear = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals("Adding 1 year", nextYear, yearField.addToCopy(1));
    }

    public void testSampleDate_MonthFieldProperties() {
        DateTime ethiopicDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);
        Property monthField = ethiopicDate.monthOfYear();
        
        assertEquals("Month should not be leap", false, monthField.isLeap());
        assertEquals("Leap amount should be 0", 0, monthField.getLeapAmount());
        assertEquals("Leap duration field type", DurationFieldType.days(), 
                    monthField.getLeapDurationField().getType());
        
        // Test month range values
        assertEquals("Month minimum value", 1, monthField.getMinimumValue());
        assertEquals("Month minimum value overall", 1, monthField.getMinimumValueOverall());
        assertEquals("Month maximum value", 13, monthField.getMaximumValue());
        assertEquals("Month maximum value overall", 13, monthField.getMaximumValueOverall());
        
        // Test month arithmetic
        DateTime fourMonthsLater = new DateTime(1997, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime wrappedMonth = new DateTime(1996, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals("Adding 4 months", fourMonthsLater, monthField.addToCopy(4));
        assertEquals("Wrapping 4 months", wrappedMonth, monthField.addWrapFieldToCopy(4));
    }

    public void testSampleDate_DayFieldProperties() {
        DateTime ethiopicDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);
        
        // Test day of month properties
        Property dayOfMonthField = ethiopicDate.dayOfMonth();
        assertEquals("Day of month should not be leap", false, dayOfMonthField.isLeap());
        assertEquals("Day leap amount should be 0", 0, dayOfMonthField.getLeapAmount());
        assertEquals("Day leap duration field should be null", null, dayOfMonthField.getLeapDurationField());
        assertEquals("Day minimum value", 1, dayOfMonthField.getMinimumValue());
        assertEquals("Day minimum value overall", 1, dayOfMonthField.getMinimumValueOverall());
        assertEquals("Day maximum value", 30, dayOfMonthField.getMaximumValue());
        assertEquals("Day maximum value overall", 30, dayOfMonthField.getMaximumValueOverall());
        
        DateTime nextDay = new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals("Adding 1 day", nextDay, dayOfMonthField.addToCopy(1));
        
        // Test day of week properties
        Property dayOfWeekField = ethiopicDate.dayOfWeek();
        assertEquals("Day of week minimum value", 1, dayOfWeekField.getMinimumValue());
        assertEquals("Day of week maximum value", 7, dayOfWeekField.getMaximumValue());
        assertEquals("Adding 1 day to day of week", nextDay, dayOfWeekField.addToCopy(1));
        
        // Test day of year properties
        Property dayOfYearField = ethiopicDate.dayOfYear();
        assertEquals("Day of year minimum value", 1, dayOfYearField.getMinimumValue());
        assertEquals("Day of year maximum value", 365, dayOfYearField.getMaximumValue());
        assertEquals("Day of year maximum value overall", 366, dayOfYearField.getMaximumValueOverall());
        assertEquals("Adding 1 day to day of year", nextDay, dayOfYearField.addToCopy(1));
    }

    public void testSampleDateWithTimezone_HandlesOffsetCorrectly() {
        // Test that timezone conversions work correctly
        DateTime parisTime = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime ethiopicDate = parisTime.withChronology(ETHIOPIC_UTC);
        
        assertEquals("Era should be EE", EthiopicChronology.EE, ethiopicDate.getEra());
        assertEquals("Year should be 1996", 1996, ethiopicDate.getYear());
        assertEquals("Year of era should be 1996", 1996, ethiopicDate.getYearOfEra());
        assertEquals("Month should be 10", 10, ethiopicDate.getMonthOfYear());
        assertEquals("Day should be 2", 2, ethiopicDate.getDayOfMonth());
        
        // PARIS is UTC+2 in summer, so 12:00 Paris time = 10:00 UTC
        assertEquals("Hour should be adjusted for timezone", 10, ethiopicDate.getHourOfDay());
        assertEquals("Minutes should be unchanged", 0, ethiopicDate.getMinuteOfHour());
        assertEquals("Seconds should be unchanged", 0, ethiopicDate.getSecondOfMinute());
        assertEquals("Milliseconds should be unchanged", 0, ethiopicDate.getMillisOfSecond());
    }

    // ========================================================================
    // Duration Calculation Tests
    // ========================================================================

    public void testDurationYear_LeapYearCycles() {
        // Test year duration calculations across leap year boundaries
        // Leap year: 1999 EE (1999 % 4 == 3)
        // Regular years: 1996, 1997, 1998 EE
        DateTime year1996 = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime year1997 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime year1998 = new DateTime(1998, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime year1999 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime year2000 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField yearField = year1996.year().getDurationField();
        assertEquals("Duration field should be years", ETHIOPIC_UTC.years(), yearField);
        
        // Test duration calculations from a base year
        long oneYearMillis = 365L * MILLIS_PER_DAY;
        long fourYearCycleMillis = (4L * 365L + 1L) * MILLIS_PER_DAY; // 3 regular + 1 leap
        
        assertEquals("1 year duration", oneYearMillis, yearField.getMillis(1, year1996.getMillis()));
        assertEquals("2 year duration", 2L * oneYearMillis, yearField.getMillis(2, year1996.getMillis()));
        assertEquals("3 year duration", 3L * oneYearMillis, yearField.getMillis(3, year1996.getMillis()));
        assertEquals("4 year duration with leap", fourYearCycleMillis, yearField.getMillis(4, year1996.getMillis()));
        
        // Test average year duration
        long avgYearMillis = fourYearCycleMillis / 4;
        assertEquals("Average 1 year duration", avgYearMillis, yearField.getMillis(1));
        assertEquals("Average 2 year duration", avgYearMillis * 2, yearField.getMillis(2));
        assertEquals("Unit milliseconds", avgYearMillis, yearField.getUnitMillis());
        
        // Test year value calculations
        testYearValueCalculations(yearField, year1996.getMillis(), oneYearMillis);
        
        // Test year addition
        assertEquals("Add 1 year", year1997.getMillis(), yearField.add(year1996.getMillis(), 1));
        assertEquals("Add 2 years", year1998.getMillis(), yearField.add(year1996.getMillis(), 2));
        assertEquals("Add 3 years", year1999.getMillis(), yearField.add(year1996.getMillis(), 3));
        assertEquals("Add 4 years", year2000.getMillis(), yearField.add(year1996.getMillis(), 4));
        
        // Test with long values
        assertEquals("Add 1 year (long)", year1997.getMillis(), yearField.add(year1996.getMillis(), 1L));
        assertEquals("Add 4 years (long)", year2000.getMillis(), yearField.add(year1996.getMillis(), 4L));
    }

    private void testYearValueCalculations(DurationField yearField, long baseMillis, long oneYearMillis) {
        // Test boundary conditions for year value calculations
        assertEquals("Just before 1 year", 0, yearField.getValue(oneYearMillis - 1L, baseMillis));
        assertEquals("Exactly 1 year", 1, yearField.getValue(oneYearMillis, baseMillis));
        assertEquals("Just after 1 year", 1, yearField.getValue(oneYearMillis + 1L, baseMillis));
        
        assertEquals("Just before 2 years", 1, yearField.getValue(2L * oneYearMillis - 1L, baseMillis));
        assertEquals("Exactly 2 years", 2, yearField.getValue(2L * oneYearMillis, baseMillis));
        assertEquals("Just after 2 years", 2, yearField.getValue(2L * oneYearMillis + 1L, baseMillis));
        
        // Test 4-year cycle boundary (includes leap year)
        long fourYearMillis = (4L * 365L + 1L) * MILLIS_PER_DAY;
        assertEquals("Just before 4 years", 3, yearField.getValue(fourYearMillis - 1L, baseMillis));
        assertEquals("Exactly 4 years", 4, yearField.getValue(fourYearMillis, baseMillis));
        assertEquals("Just after 4 years", 4, yearField.getValue(fourYearMillis + 1L, baseMillis));
    }

    public void testDurationMonth_WithLeapYearMonth() {
        // Test month duration across the 13th month in a leap year (1999 EE)
        DateTime month11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime month12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime month13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime nextYearMonth1 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField monthField = month11.monthOfYear().getDurationField();
        assertEquals("Duration field should be months", ETHIOPIC_UTC.months(), monthField);
        
        long regularMonthMillis = 30L * MILLIS_PER_DAY;
        long leapMonth13Millis = 6L * MILLIS_PER_DAY; // 13th month in leap year
        
        // Test month duration calculations
        assertEquals("Regular month duration", regularMonthMillis, monthField.getMillis(1, month11.getMillis()));
        assertEquals("Two regular months", 2L * regularMonthMillis, monthField.getMillis(2, month11.getMillis()));
        assertEquals("Two months + leap 13th", 2L * regularMonthMillis + leapMonth13Millis, 
                    monthField.getMillis(3, month11.getMillis()));
        assertEquals("Three months + leap 13th", 3L * regularMonthMillis + leapMonth13Millis, 
                    monthField.getMillis(4, month11.getMillis()));
        
        // Test average month durations
        assertEquals("Average month duration", regularMonthMillis, monthField.getMillis(1));
        assertEquals("13 average months", 13L * regularMonthMillis, monthField.getMillis(13));
        
        // Test month value calculations
        testMonthValueCalculations(monthField, month11.getMillis(), regularMonthMillis, leapMonth13Millis);
        
        // Test month addition
        assertEquals("Add 1 month", month12.getMillis(), monthField.add(month11.getMillis(), 1));
        assertEquals("Add 2 months", month13.getMillis(), monthField.add(month11.getMillis(), 2));
        assertEquals("Add 3 months", nextYearMonth1.getMillis(), monthField.add(month11.getMillis(), 3));
    }

    private void testMonthValueCalculations(DurationField monthField, long baseMillis, 
                                          long regularMonthMillis, long leapMonth13Millis) {
        // Test month value calculations across regular and leap month boundaries
        assertEquals("Just before 1 month", 0, monthField.getValue(regularMonthMillis - 1L, baseMillis));
        assertEquals("Exactly 1 month", 1, monthField.getValue(regularMonthMillis, baseMillis));
        assertEquals("Just after 1 month", 1, monthField.getValue(regularMonthMillis + 1L, baseMillis));
        
        // Test across the leap 13th month boundary
        long twoMonthsPlusLeap = 2L * regularMonthMillis + leapMonth13Millis;
        assertEquals("Just before leap month end", 2, monthField.getValue(twoMonthsPlusLeap - 1L, baseMillis));
        assertEquals("Exactly at leap month end", 3, monthField.getValue(twoMonthsPlusLeap, baseMillis));
        assertEquals("Just after leap month end", 3, monthField.getValue(twoMonthsPlusLeap + 1L, baseMillis));
    }

    // ========================================================================
    // Leap Year/Month/Day Tests
    // ========================================================================

    public void testLeapYear_Day5Of13thMonth() {
        // Test 5th day of 13th month in a leap year - should not be a leap day itself
        Chronology ethiopic = EthiopicChronology.getInstance();
        DateTime date = new DateTime(3, 13, 5, 0, 0, ethiopic); // Year 3 EE is leap (3 % 4 == 3)
        
        assertEquals("Year should be leap", true, date.year().isLeap());
        assertEquals("13th month should be leap", true, date.monthOfYear().isLeap());
        assertEquals("Day 5 should not be leap day", false, date.dayOfMonth().isLeap());
        assertEquals("Day of year should not be leap", false, date.dayOfYear().isLeap());
    }

    public void testLeapYear_Day6Of13thMonth() {
        // Test 6th day of 13th month in a leap year - this IS the leap day
        Chronology ethiopic = EthiopicChronology.getInstance();
        DateTime date = new DateTime(3, 13, 6, 0, 0, ethiopic); // Year 3 EE is leap (3 % 4 == 3)
        
        assertEquals("Year should be leap", true, date.year().isLeap());
        assertEquals("13th month should be leap", true, date.monthOfYear().isLeap());
        assertEquals("Day 6 should be leap day", true, date.dayOfMonth().isLeap());
        assertEquals("Day of year should be leap", true, date.dayOfYear().isLeap());
    }

    public void testNonLeapYear_13thMonth() {
        // Test that non-leap years only have 5 days in the 13th month
        Chronology ethiopic = EthiopicChronology.getInstance();
        DateTime date = new DateTime(4, 13, 5, 0, 0, ethiopic); // Year 4 EE is not leap (4 % 4 != 3)
        
        assertEquals("Year should not be leap", false, date.year().isLeap());
        assertEquals("13th month should not be leap", false, date.monthOfYear().isLeap());
        assertEquals("Day should not be leap", false, date.dayOfMonth().isLeap());
        assertEquals("Day of year should not be leap", false, date.dayOfYear().isLeap());
        
        try {
            // Attempting to create 6th day of 13th month in non-leap year should fail
            new DateTime(4, 13, 6, 0, 0, ethiopic);
            fail("Should not allow 6th day of 13th month in non-leap year");
        } catch (IllegalArgumentException expected) {
            // This is expected behavior
        }
    }
}