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
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Comprehensive test suite for CopticChronology.
 * 
 * The Coptic calendar is used by the Coptic Orthodox Church and runs from year 1 AM (Anno Martyrum),
 * which corresponds to 284 CE in the Julian calendar. It has 12 months of 30 days each, plus a 
 * 13th month of 5 days (6 in leap years). Leap years occur every 4 years when the year number 
 * modulo 4 equals 3.
 *
 * @author Stephen Colebourne
 */
public class TestCopticChronology extends TestCase {

    // ========================================================================
    // Constants and Test Data
    // ========================================================================
    
    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long TEST_TIME_INCREMENT = 1 * MILLIS_PER_DAY;

    // Standard time zones for testing
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
    // Chronology instances for comparisons
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    /**
     * Test reference time: June 9, 2002 (calculated from days since epoch)
     * This corresponds to Coptic date 1720/10/2 (year 1720, month 10, day 2)
     */
    private final long REFERENCE_TEST_TIME;
    
    // State preservation for test isolation
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    // ========================================================================
    // Test Setup and Teardown
    // ========================================================================

    public TestCopticChronology(String name) {
        super(name);
        
        // Calculate reference time: 2002-06-09 (32 years from 1970)
        long daysTo2002 = calculateDaysFromEpochTo2002();
        long daysToJune9 = 31L + 28L + 31L + 30L + 31L + 9L - 1L; // Jan+Feb+Mar+Apr+May+9days-1
        REFERENCE_TEST_TIME = (daysTo2002 + daysToJune9) * MILLIS_PER_DAY;
    }
    
    /**
     * Calculate total days from Unix epoch (1970-01-01) to 2002-01-01
     * Accounts for leap years: 1972, 1976, 1980, 1984, 1988, 1992, 1996, 2000
     */
    private long calculateDaysFromEpochTo2002() {
        // 32 regular years + 8 leap days
        return 365L * 32L + 8L;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCopticChronology.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Fix current time for consistent test results
        DateTimeUtils.setCurrentMillisFixed(REFERENCE_TEST_TIME);
        
        // Preserve original system settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set consistent test environment (London time zone, UK locale)
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore system clock and settings
        DateTimeUtils.setCurrentMillisSystem();
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

    /**
     * Test that getInstanceUTC() returns a UTC chronology instance
     */
    public void testFactoryUTC_ReturnsUTCChronology() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        
        assertEquals("Should use UTC time zone", DateTimeZone.UTC, chronology.getZone());
        assertSame("Should return CopticChronology instance", CopticChronology.class, chronology.getClass());
    }

    /**
     * Test that getInstance() uses the default time zone
     */
    public void testFactory_UsesDefaultTimeZone() {
        CopticChronology chronology = CopticChronology.getInstance();
        
        assertEquals("Should use default time zone (London)", LONDON, chronology.getZone());
        assertSame("Should return CopticChronology instance", CopticChronology.class, chronology.getClass());
    }

    /**
     * Test getInstance(zone) with various time zones
     */
    public void testFactoryWithZone_HandlesVariousTimeZones() {
        assertEquals("Should use Tokyo time zone", TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals("Should use Paris time zone", PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals("Should default to London for null zone", LONDON, CopticChronology.getInstance(null).getZone());
        assertSame("Should return CopticChronology instance", CopticChronology.class, 
                  CopticChronology.getInstance(TOKYO).getClass());
    }

    // ========================================================================
    // Instance Identity and Equality Tests
    // ========================================================================

    /**
     * Test that factory methods return the same instances for the same parameters (singleton behavior)
     */
    public void testInstanceEquality_FactoryReturnsSameInstances() {
        assertSame("Tokyo instances should be identical", 
                  CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame("London instances should be identical", 
                  CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame("Paris instances should be identical", 
                  CopticChronology.getInstance(PARIS), CopticChronology.getInstance(PARIS));
        assertSame("UTC instances should be identical", 
                  CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
        assertSame("Default should equal London instance", 
                  CopticChronology.getInstance(), CopticChronology.getInstance(LONDON));
    }

    /**
     * Test withUTC() method returns UTC chronology
     */
    public void testWithUTC_ReturnsUTCChronology() {
        CopticChronology utcExpected = CopticChronology.getInstanceUTC();
        
        assertSame("London chronology.withUTC() should return UTC instance", 
                  utcExpected, CopticChronology.getInstance(LONDON).withUTC());
        assertSame("Tokyo chronology.withUTC() should return UTC instance", 
                  utcExpected, CopticChronology.getInstance(TOKYO).withUTC());
        assertSame("UTC chronology.withUTC() should return itself", 
                  utcExpected, CopticChronology.getInstanceUTC().withUTC());
        assertSame("Default chronology.withUTC() should return UTC instance", 
                  utcExpected, CopticChronology.getInstance().withUTC());
    }

    /**
     * Test withZone() method returns chronology with specified zone
     */
    public void testWithZone_ReturnsChronologyWithSpecifiedZone() {
        CopticChronology tokyoBase = CopticChronology.getInstance(TOKYO);
        
        assertSame("withZone(same) should return same instance", 
                  tokyoBase, tokyoBase.withZone(TOKYO));
        assertSame("withZone(London) should return London instance", 
                  CopticChronology.getInstance(LONDON), tokyoBase.withZone(LONDON));
        assertSame("withZone(Paris) should return Paris instance", 
                  CopticChronology.getInstance(PARIS), tokyoBase.withZone(PARIS));
        assertSame("withZone(null) should return London instance", 
                  CopticChronology.getInstance(LONDON), tokyoBase.withZone(null));
        assertSame("Default.withZone(Paris) should return Paris instance", 
                  CopticChronology.getInstance(PARIS), CopticChronology.getInstance().withZone(PARIS));
        assertSame("UTC.withZone(Paris) should return Paris instance", 
                  CopticChronology.getInstance(PARIS), CopticChronology.getInstanceUTC().withZone(PARIS));
    }

    /**
     * Test string representation includes time zone
     */
    public void testToString_IncludesTimeZoneInformation() {
        assertEquals("Should show London time zone", 
                    "CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("Should show Tokyo time zone", 
                    "CopticChronology[Asia/Tokyo]", CopticChronology.getInstance(TOKYO).toString());
        assertEquals("Should show default time zone", 
                    "CopticChronology[Europe/London]", CopticChronology.getInstance().toString());
        assertEquals("Should show UTC", 
                    "CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    // ========================================================================
    // Duration Field Tests
    // ========================================================================

    /**
     * Test duration field names and support status
     */
    public void testDurationFields_NamesAndSupport() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Verify field names
        assertEquals("eras", coptic.eras().getName());
        assertEquals("centuries", coptic.centuries().getName());
        assertEquals("years", coptic.years().getName());
        assertEquals("weekyears", coptic.weekyears().getName());
        assertEquals("months", coptic.months().getName());
        assertEquals("weeks", coptic.weeks().getName());
        assertEquals("days", coptic.days().getName());
        assertEquals("halfdays", coptic.halfdays().getName());
        assertEquals("hours", coptic.hours().getName());
        assertEquals("minutes", coptic.minutes().getName());
        assertEquals("seconds", coptic.seconds().getName());
        assertEquals("millis", coptic.millis().getName());
        
        // Verify support status
        assertEquals("Eras should not be supported", false, coptic.eras().isSupported());
        assertEquals("Centuries should be supported", true, coptic.centuries().isSupported());
        assertEquals("Years should be supported", true, coptic.years().isSupported());
        assertEquals("Weekyears should be supported", true, coptic.weekyears().isSupported());
        assertEquals("Months should be supported", true, coptic.months().isSupported());
        assertEquals("Weeks should be supported", true, coptic.weeks().isSupported());
        assertEquals("Days should be supported", true, coptic.days().isSupported());
        assertEquals("Halfdays should be supported", true, coptic.halfdays().isSupported());
        assertEquals("Hours should be supported", true, coptic.hours().isSupported());
        assertEquals("Minutes should be supported", true, coptic.minutes().isSupported());
        assertEquals("Seconds should be supported", true, coptic.seconds().isSupported());
        assertEquals("Milliseconds should be supported", true, coptic.millis().isSupported());
    }

    /**
     * Test precision characteristics of duration fields for local time zone
     */
    public void testDurationFields_PrecisionInLocalTimeZone() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Large units are not precise due to DST and calendar variations
        assertEquals("Centuries not precise (calendar variations)", false, coptic.centuries().isPrecise());
        assertEquals("Years not precise (leap years)", false, coptic.years().isPrecise());
        assertEquals("Weekyears not precise (calendar variations)", false, coptic.weekyears().isPrecise());
        assertEquals("Months not precise (different lengths)", false, coptic.months().isPrecise());
        assertEquals("Weeks not precise (DST transitions)", false, coptic.weeks().isPrecise());
        assertEquals("Days not precise (DST transitions)", false, coptic.days().isPrecise());
        assertEquals("Halfdays not precise (DST transitions)", false, coptic.halfdays().isPrecise());
        
        // Time units are precise
        assertEquals("Hours should be precise", true, coptic.hours().isPrecise());
        assertEquals("Minutes should be precise", true, coptic.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, coptic.seconds().isPrecise());
        assertEquals("Milliseconds should be precise", true, coptic.millis().isPrecise());
    }

    /**
     * Test precision characteristics of duration fields for UTC
     */
    public void testDurationFields_PrecisionInUTC() {
        CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        
        // In UTC, more fields are precise since there's no DST
        assertEquals("Centuries not precise (calendar variations)", false, copticUTC.centuries().isPrecise());
        assertEquals("Years not precise (leap years)", false, copticUTC.years().isPrecise());
        assertEquals("Weekyears not precise (calendar variations)", false, copticUTC.weekyears().isPrecise());
        assertEquals("Months not precise (different lengths)", false, copticUTC.months().isPrecise());
        assertEquals("Weeks should be precise in UTC", true, copticUTC.weeks().isPrecise());
        assertEquals("Days should be precise in UTC", true, copticUTC.days().isPrecise());
        assertEquals("Halfdays should be precise in UTC", true, copticUTC.halfdays().isPrecise());
        assertEquals("Hours should be precise", true, copticUTC.hours().isPrecise());
        assertEquals("Minutes should be precise", true, copticUTC.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, copticUTC.seconds().isPrecise());
        assertEquals("Milliseconds should be precise", true, copticUTC.millis().isPrecise());
    }

    /**
     * Test precision characteristics for fixed offset time zone (GMT)
     */
    public void testDurationFields_PrecisionInFixedOffset() {
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        CopticChronology copticGMT = CopticChronology.getInstance(gmt);
        
        // Fixed offset zones behave like UTC for precision
        assertEquals("Centuries not precise (calendar variations)", false, copticGMT.centuries().isPrecise());
        assertEquals("Years not precise (leap years)", false, copticGMT.years().isPrecise());
        assertEquals("Weekyears not precise (calendar variations)", false, copticGMT.weekyears().isPrecise());
        assertEquals("Months not precise (different lengths)", false, copticGMT.months().isPrecise());
        assertEquals("Weeks should be precise in fixed offset", true, copticGMT.weeks().isPrecise());
        assertEquals("Days should be precise in fixed offset", true, copticGMT.days().isPrecise());
        assertEquals("Halfdays should be precise in fixed offset", true, copticGMT.halfdays().isPrecise());
        assertEquals("Hours should be precise", true, copticGMT.hours().isPrecise());
        assertEquals("Minutes should be precise", true, copticGMT.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, copticGMT.seconds().isPrecise());
        assertEquals("Milliseconds should be precise", true, copticGMT.millis().isPrecise());
    }

    // ========================================================================
    // Date Field Tests
    // ========================================================================

    /**
     * Test date field names and support
     */
    public void testDateFields_NamesAndSupport() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Test field names
        assertEquals("era", coptic.era().getName());
        assertEquals("centuryOfEra", coptic.centuryOfEra().getName());
        assertEquals("yearOfCentury", coptic.yearOfCentury().getName());
        assertEquals("yearOfEra", coptic.yearOfEra().getName());
        assertEquals("year", coptic.year().getName());
        assertEquals("monthOfYear", coptic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", coptic.weekyearOfCentury().getName());
        assertEquals("weekyear", coptic.weekyear().getName());
        assertEquals("weekOfWeekyear", coptic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", coptic.dayOfYear().getName());
        assertEquals("dayOfMonth", coptic.dayOfMonth().getName());
        assertEquals("dayOfWeek", coptic.dayOfWeek().getName());
        
        // All date fields should be supported
        assertEquals("Era should be supported", true, coptic.era().isSupported());
        assertEquals("Century of era should be supported", true, coptic.centuryOfEra().isSupported());
        assertEquals("Year of century should be supported", true, coptic.yearOfCentury().isSupported());
        assertEquals("Year of era should be supported", true, coptic.yearOfEra().isSupported());
        assertEquals("Year should be supported", true, coptic.year().isSupported());
        assertEquals("Month of year should be supported", true, coptic.monthOfYear().isSupported());
        assertEquals("Weekyear of century should be supported", true, coptic.weekyearOfCentury().isSupported());
        assertEquals("Weekyear should be supported", true, coptic.weekyear().isSupported());
        assertEquals("Week of weekyear should be supported", true, coptic.weekOfWeekyear().isSupported());
        assertEquals("Day of year should be supported", true, coptic.dayOfYear().isSupported());
        assertEquals("Day of month should be supported", true, coptic.dayOfMonth().isSupported());
        assertEquals("Day of week should be supported", true, coptic.dayOfWeek().isSupported());
    }

    /**
     * Test duration field relationships for date fields
     */
    public void testDateFields_DurationFieldRelationships() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Test duration field mappings
        assertEquals(coptic.eras(), coptic.era().getDurationField());
        assertEquals(coptic.centuries(), coptic.centuryOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfCentury().getDurationField());
        assertEquals(coptic.years(), coptic.yearOfEra().getDurationField());
        assertEquals(coptic.years(), coptic.year().getDurationField());
        assertEquals(coptic.months(), coptic.monthOfYear().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyearOfCentury().getDurationField());
        assertEquals(coptic.weekyears(), coptic.weekyear().getDurationField());
        assertEquals(coptic.weeks(), coptic.weekOfWeekyear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfYear().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfMonth().getDurationField());
        assertEquals(coptic.days(), coptic.dayOfWeek().getDurationField());
    }

    /**
     * Test range duration field relationships for date fields
     */
    public void testDateFields_RangeDurationFieldRelationships() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Test range duration field mappings
        assertEquals("Era has no range field", null, coptic.era().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.centuryOfEra().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.yearOfCentury().getRangeDurationField());
        assertEquals(coptic.eras(), coptic.yearOfEra().getRangeDurationField());
        assertEquals("Year has no range field", null, coptic.year().getRangeDurationField());
        assertEquals(coptic.years(), coptic.monthOfYear().getRangeDurationField());
        assertEquals(coptic.centuries(), coptic.weekyearOfCentury().getRangeDurationField());
        assertEquals("Weekyear has no range field", null, coptic.weekyear().getRangeDurationField());
        assertEquals(coptic.weekyears(), coptic.weekOfWeekyear().getRangeDurationField());
        assertEquals(coptic.years(), coptic.dayOfYear().getRangeDurationField());
        assertEquals(coptic.months(), coptic.dayOfMonth().getRangeDurationField());
        assertEquals(coptic.weeks(), coptic.dayOfWeek().getRangeDurationField());
    }

    // ========================================================================
    // Time Field Tests
    // ========================================================================

    /**
     * Test time field names and support
     */
    public void testTimeFields_NamesAndSupport() {
        CopticChronology coptic = CopticChronology.getInstance();
        
        // Test field names
        assertEquals("halfdayOfDay", coptic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", coptic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", coptic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", coptic.clockhourOfDay().getName());
        assertEquals("hourOfDay", coptic.hourOfDay().getName());
        assertEquals("minuteOfDay", coptic.minuteOfDay().getName());
        assertEquals("minuteOfHour", coptic.minuteOfHour().getName());
        assertEquals("secondOfDay", coptic.secondOfDay().getName());
        assertEquals("secondOfMinute", coptic.secondOfMinute().getName());
        assertEquals("millisOfDay", coptic.millisOfDay().getName());
        assertEquals("millisOfSecond", coptic.millisOfSecond().getName());
        
        // All time fields should be supported
        assertEquals("Half-day of day should be supported", true, coptic.halfdayOfDay().isSupported());
        assertEquals("Clock hour of half-day should be supported", true, coptic.clockhourOfHalfday().isSupported());
        assertEquals("Hour of half-day should be supported", true, coptic.hourOfHalfday().isSupported());
        assertEquals("Clock hour of day should be supported", true, coptic.clockhourOfDay().isSupported());
        assertEquals("Hour of day should be supported", true, coptic.hourOfDay().isSupported());
        assertEquals("Minute of day should be supported", true, coptic.minuteOfDay().isSupported());
        assertEquals("Minute of hour should be supported", true, coptic.minuteOfHour().isSupported());
        assertEquals("Second of day should be supported", true, coptic.secondOfDay().isSupported());
        assertEquals("Second of minute should be supported", true, coptic.secondOfMinute().isSupported());
        assertEquals("Millis of day should be supported", true, coptic.millisOfDay().isSupported());
        assertEquals("Millis of second should be supported", true, coptic.millisOfSecond().isSupported());
    }

    // ========================================================================
    // Epoch and Era Tests
    // ========================================================================

    /**
     * Test that Coptic epoch (1 AM) corresponds to August 29, 284 CE in Julian calendar
     */
    public void testEpoch_CorrespondsToJulianDate() {
        DateTime copticEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        DateTime expectedJulianDate = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC);
        
        assertEquals("Coptic epoch should correspond to Julian 284-08-29", 
                    expectedJulianDate, copticEpoch.withChronology(JULIAN_UTC));
    }

    /**
     * Test era constant and negative year handling
     */
    public void testEra_ConstantAndValidation() {
        assertEquals("AM era constant should equal CE", 1, CopticChronology.AM);
        
        // Test that negative years are rejected
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, COPTIC_UTC);
            fail("Should reject negative years");
        } catch (IllegalArgumentException expected) {
            // Expected behavior - negative years not supported
        }
    }

    // ========================================================================
    // Calendar System Tests
    // ========================================================================

    /**
     * Comprehensive test of Coptic calendar calculations.
     * This test validates era, year, month, day calculations over a long period.
     * 
     * The Coptic calendar has:
     * - 12 months of 30 days each
     * - 1 additional month (13th) of 5 days (6 in leap years)
     * - Leap years when year % 4 == 3
     */
    public void testCalendarCalculations_ComprehensiveValidation() {
        if (TestAll.FAST) {
            return; // Skip comprehensive test in fast mode
        }
        
        System.out.println("\nTestCopticChronology.testCalendarCalculations");
        
        // Test period: from Coptic epoch to ISO year 3000
        DateTime copticEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        long currentMillis = copticEpoch.getMillis();
        long endMillis = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        
        // Get field accessors for efficient testing
        DateTimeField dayOfWeekField = COPTIC_UTC.dayOfWeek();
        DateTimeField dayOfYearField = COPTIC_UTC.dayOfYear();
        DateTimeField dayOfMonthField = COPTIC_UTC.dayOfMonth();
        DateTimeField monthOfYearField = COPTIC_UTC.monthOfYear();
        DateTimeField yearField = COPTIC_UTC.year();
        DateTimeField yearOfEraField = COPTIC_UTC.yearOfEra();
        DateTimeField eraField = COPTIC_UTC.era();
        
        // Expected values starting from epoch
        int expectedDayOfWeek = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDayOfYear = 1;
        int expectedDayOfMonth = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        
        while (currentMillis < endMillis) {
            // Get actual field values
            int actualDayOfWeek = dayOfWeekField.get(currentMillis);
            int actualDayOfYear = dayOfYearField.get(currentMillis);
            int actualDayOfMonth = dayOfMonthField.get(currentMillis);
            int actualMonth = monthOfYearField.get(currentMillis);
            int actualYear = yearField.get(currentMillis);
            int actualYearOfEra = yearOfEraField.get(currentMillis);
            int monthLength = dayOfMonthField.getMaximumValue(currentMillis);
            
            // Validate month range
            if (actualMonth < 1 || actualMonth > 13) {
                fail("Invalid month " + actualMonth + " at millis " + currentMillis);
            }
            
            // Test era (should always be 1 = AM)
            assertEquals("Era should be AM", 1, eraField.get(currentMillis));
            assertEquals("Era text should be AM", "AM", eraField.getAsText(currentMillis));
            assertEquals("Era short text should be AM", "AM", eraField.getAsShortText(currentMillis));
            
            // Validate calculated vs actual values
            assertEquals("Year mismatch", expectedYear, actualYear);
            assertEquals("Year of era mismatch", expectedYear, actualYearOfEra);
            assertEquals("Month mismatch", expectedMonth, actualMonth);
            assertEquals("Day of month mismatch", expectedDayOfMonth, actualDayOfMonth);
            assertEquals("Day of week mismatch", expectedDayOfWeek, actualDayOfWeek);
            assertEquals("Day of year mismatch", expectedDayOfYear, actualDayOfYear);
            
            // Test leap year calculation (year % 4 == 3)
            boolean expectedLeapYear = (actualYear % 4 == 3);
            assertEquals("Leap year calculation", expectedLeapYear, yearField.isLeap(currentMillis));
            
            // Test month length
            if (actualMonth == 13) {
                // 13th month: 5 days normal, 6 days in leap years
                assertEquals("13th month leap status", expectedLeapYear, monthOfYearField.isLeap(currentMillis));
                int expected13thMonthLength = expectedLeapYear ? 6 : 5;
                assertEquals("13th month length", expected13thMonthLength, monthLength);
            } else {
                // Regular months: always 30 days
                assertEquals("Regular month length", 30, monthLength);
            }
            
            // Calculate next day's expected values
            expectedDayOfWeek = (expectedDayOfWeek % 7) + 1; // Cycle through 1-7
            expectedDayOfMonth++;
            expectedDayOfYear++;
            
            // Handle month and year transitions
            if (expectedDayOfMonth == 31 && expectedMonth < 13) {
                // End of regular month (30 days) - move to next month
                expectedDayOfMonth = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                // Handle 13th month end
                boolean isLeapYear = (expectedYear % 4 == 3);
                int thirteenthMonthLength = isLeapYear ? 6 : 5;
                
                if (expectedDayOfMonth > thirteenthMonthLength) {
                    // End of year - move to next year
                    expectedDayOfMonth = 1;
                    expectedMonth = 1;
                    expectedYear++;
                    expectedDayOfYear = 1;
                }
            }
            
            currentMillis += TEST_TIME_INCREMENT;
        }
    }

    // ========================================================================
    // Sample Date Tests
    // ========================================================================

    /**
     * Test specific sample date: ISO 2004-06-09 corresponds to Coptic 1720-10-02
     */
    public void testSampleDate_ISO2004June9() {
        // Convert ISO date to Coptic
        DateTime isoDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC);
        DateTime copticDate = isoDate.withChronology(COPTIC_UTC);
        
        // Test basic date components
        assertEquals("Era should be AM", CopticChronology.AM, copticDate.getEra());
        assertEquals("Century should be 18", 18, copticDate.getCenturyOfEra());
        assertEquals("Year of century should be 20", 20, copticDate.getYearOfCentury());
        assertEquals("Year of era should be 1720", 1720, copticDate.getYearOfEra());
        assertEquals("Year should be 1720", 1720, copticDate.getYear());
        assertEquals("Month should be 10", 10, copticDate.getMonthOfYear());
        assertEquals("Day should be 2", 2, copticDate.getDayOfMonth());
        assertEquals("Should be Wednesday", DateTimeConstants.WEDNESDAY, copticDate.getDayOfWeek());
        assertEquals("Day of year should be 272", 9 * 30 + 2, copticDate.getDayOfYear()); // 9 full months + 2 days
        
        // Test time components (should be unchanged)
        assertEquals("Hour should be 0", 0, copticDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, copticDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, copticDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, copticDate.getMillisOfSecond());
    }

    /**
     * Test year property for sample date
     */
    public void testSampleDate_YearProperty() {
        DateTime copticDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        Property yearProperty = copticDate.year();
        
        // Year 1720 is not a leap year (1720 % 4 = 0, not 3)
        assertEquals("1720 should not be leap year", false, yearProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, yearProperty.getLeapAmount());
        assertEquals("Leap duration should be days", DurationFieldType.days(), 
                    yearProperty.getLeapDurationField().getType());
        
        // Test year arithmetic
        DateTime expectedNextYear = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Adding 1 year", expectedNextYear, yearProperty.addToCopy(1));
    }

    /**
     * Test month property for sample date
     */
    public void testSampleDate_MonthProperty() {
        DateTime copticDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        Property monthProperty = copticDate.monthOfYear();
        
        assertEquals("Month should not be leap", false, monthProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, monthProperty.getLeapAmount());
        assertEquals("Leap duration should be days", DurationFieldType.days(), 
                    monthProperty.getLeapDurationField().getType());
        
        // Test month range
        assertEquals("Minimum month should be 1", 1, monthProperty.getMinimumValue());
        assertEquals("Minimum month overall should be 1", 1, monthProperty.getMinimumValueOverall());
        assertEquals("Maximum month should be 13", 13, monthProperty.getMaximumValue());
        assertEquals("Maximum month overall should be 13", 13, monthProperty.getMaximumValueOverall());
        
        // Test month arithmetic
        DateTime expectedAdd4Months = new DateTime(1721, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Adding 4 months", expectedAdd4Months, monthProperty.addToCopy(4));
        
        DateTime expectedWrap4Months = new DateTime(1720, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Wrapping 4 months", expectedWrap4Months, monthProperty.addWrapFieldToCopy(4));
    }

    /**
     * Test day of month property for sample date
     */
    public void testSampleDate_DayOfMonthProperty() {
        DateTime copticDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        Property dayProperty = copticDate.dayOfMonth();
        
        assertEquals("Day should not be leap", false, dayProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, dayProperty.getLeapAmount());
        assertEquals("Leap duration should be null", null, dayProperty.getLeapDurationField());
        
        // Test day range (regular month has 30 days)
        assertEquals("Minimum day should be 1", 1, dayProperty.getMinimumValue());
        assertEquals("Minimum day overall should be 1", 1, dayProperty.getMinimumValueOverall());
        assertEquals("Maximum day should be 30", 30, dayProperty.getMaximumValue());
        assertEquals("Maximum day overall should be 30", 30, dayProperty.getMaximumValueOverall());
        
        // Test day arithmetic
        DateTime expectedNextDay = new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Adding 1 day", expectedNextDay, dayProperty.addToCopy(1));
    }

    /**
     * Test day of week property for sample date
     */
    public void testSampleDate_DayOfWeekProperty() {
        DateTime copticDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        Property dayOfWeekProperty = copticDate.dayOfWeek();
        
        assertEquals("Day of week should not be leap", false, dayOfWeekProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, dayOfWeekProperty.getLeapAmount());
        assertEquals("Leap duration should be null", null, dayOfWeekProperty.getLeapDurationField());
        
        // Test day of week range
        assertEquals("Minimum day of week should be 1", 1, dayOfWeekProperty.getMinimumValue());
        assertEquals("Minimum day of week overall should be 1", 1, dayOfWeekProperty.getMinimumValueOverall());
        assertEquals("Maximum day of week should be 7", 7, dayOfWeekProperty.getMaximumValue());
        assertEquals("Maximum day of week overall should be 7", 7, dayOfWeekProperty.getMaximumValueOverall());
        
        // Test day of week arithmetic
        DateTime expectedNextDay = new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Adding 1 day of week", expectedNextDay, dayOfWeekProperty.addToCopy(1));
    }

    /**
     * Test day of year property for sample date
     */
    public void testSampleDate_DayOfYearProperty() {
        DateTime copticDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        Property dayOfYearProperty = copticDate.dayOfYear();
        
        assertEquals("Day of year should not be leap", false, dayOfYearProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, dayOfYearProperty.getLeapAmount());
        assertEquals("Leap duration should be null", null, dayOfYearProperty.getLeapDurationField());
        
        // Test day of year range (non-leap year has 365 days)
        assertEquals("Minimum day of year should be 1", 1, dayOfYearProperty.getMinimumValue());
        assertEquals("Minimum day of year overall should be 1", 1, dayOfYearProperty.getMinimumValueOverall());
        assertEquals("Maximum day of year should be 365", 365, dayOfYearProperty.getMaximumValue());
        assertEquals("Maximum day of year overall should be 366", 366, dayOfYearProperty.getMaximumValueOverall());
        
        // Test day of year arithmetic
        DateTime expectedNextDay = new DateTime(1720, 10, 3, 0, 0, 0, 0, COPTIC_UTC);
        assertEquals("Adding 1 day of year", expectedNextDay, dayOfYearProperty.addToCopy(1));
    }

    /**
     * Test sample date with time zone conversion
     */
    public void testSampleDate_WithTimeZoneConversion() {
        // Create Paris time 12:00 (UTC+2 in summer, so 10:00 UTC)
        DateTime parisDate = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime copticDate = parisDate.withChronology(COPTIC_UTC);
        
        assertEquals("Era should be AM", CopticChronology.AM, copticDate.getEra());
        assertEquals("Year should be 1720", 1720, copticDate.getYear());
        assertEquals("Year of era should be 1720", 1720, copticDate.getYearOfEra());
        assertEquals("Month should be 10", 10, copticDate.getMonthOfYear());
        assertEquals("Day should be 2", 2, copticDate.getDayOfMonth());
        assertEquals("Hour should be 10 (12-2 for DST)", 10, copticDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, copticDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, copticDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, copticDate.getMillisOfSecond());
    }

    // ========================================================================
    // Duration Field Behavior Tests
    // ========================================================================

    /**
     * Test year duration field calculations including leap year handling
     */
    public void testYearDuration_LeapYearCalculations() {
        // Test years around leap year 1723 (1723 % 4 == 3, so it's a leap year)
        DateTime year1720 = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime year1721 = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime year1722 = new DateTime(1722, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime year1723 = new DateTime(1723, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime year1724 = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        
        DurationField yearField = year1720.year().getDurationField();
        assertEquals("Should use COPTIC_UTC.years()", COPTIC_UTC.years(), yearField);
        
        // Test duration calculations from year 1720
        long baseMillis = year1720.getMillis();
        assertEquals("1 year from 1720", 1L * 365L * MILLIS_PER_DAY, yearField.getMillis(1, baseMillis));
        assertEquals("2 years from 1720", 2L * 365L * MILLIS_PER_DAY, yearField.getMillis(2, baseMillis));
        assertEquals("3 years from 1720", 3L * 365L * MILLIS_PER_DAY, yearField.getMillis(3, baseMillis));
        assertEquals("4 years from 1720 (includes leap)", (4L * 365L + 1L) * MILLIS_PER_DAY, 
                    yearField.getMillis(4, baseMillis));
        
        // Test average year calculations
        long averageYearMillis = ((4L * 365L + 1L) * MILLIS_PER_DAY) / 4;
        assertEquals("Average year duration", averageYearMillis, yearField.getMillis(1));
        assertEquals("2 average years duration", 2 * averageYearMillis, yearField.getMillis(2));
        assertEquals("Unit millis should be average year", averageYearMillis, yearField.getUnitMillis());
        
        // Test long parameter versions
        assertEquals("1 year from 1720 (long)", 1L * 365L * MILLIS_PER_DAY, yearField.getMillis(1L, baseMillis));
        assertEquals("4 years from 1720 (long)", (4L * 365L + 1L) * MILLIS_PER_DAY, yearField.getMillis(4L, baseMillis));
        assertEquals("1 average year (long)", averageYearMillis, yearField.getMillis(1L));
        
        // Test value calculations (how many years in given duration)
        long oneYearDuration = 1L * 365L * MILLIS_PER_DAY;
        assertEquals("Just under 1 year should be 0", 0, yearField.getValue(oneYearDuration - 1L, baseMillis));
        assertEquals("Exactly 1 year should be 1", 1, yearField.getValue(oneYearDuration, baseMillis));
        assertEquals("Just over 1 year should be 1", 1, yearField.getValue(oneYearDuration + 1L, baseMillis));
        
        long fourYearDuration = (4L * 365L + 1L) * MILLIS_PER_DAY;
        assertEquals("Just under 4 years should be 3", 3, yearField.getValue(fourYearDuration - 1L, baseMillis));
        assertEquals("Exactly 4 years should be 4", 4, yearField.getValue(fourYearDuration, baseMillis));
        assertEquals("Just over 4 years should be 4", 4, yearField.getValue(fourYearDuration + 1L, baseMillis));
        
        // Test year addition
        assertEquals("Add 1 year", year1721.getMillis(), yearField.add(baseMillis, 1));
        assertEquals("Add 2 years", year1722.getMillis(), yearField.add(baseMillis, 2));
        assertEquals("Add 3 years", year1723.getMillis(), yearField.add(baseMillis, 3));
        assertEquals("Add 4 years", year1724.getMillis(), yearField.add(baseMillis, 4));
        
        // Test long parameter versions
        assertEquals("Add 1 year (long)", year1721.getMillis(), yearField.add(baseMillis, 1L));
        assertEquals("Add 4 years (long)", year1724.getMillis(), yearField.add(baseMillis, 4L));
    }

    /**
     * Test month duration field calculations including 13th month handling
     */
    public void testMonthDuration_ThirteenthMonthCalculations() {
        // Test months in leap year 1723, especially around 13th month
        DateTime month11 = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime month12 = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime month13 = new DateTime(1723, 13, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime nextYearMonth1 = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        
        DurationField monthField = month11.monthOfYear().getDurationField();
        assertEquals("Should use COPTIC_UTC.months()", COPTIC_UTC.months(), monthField);
        
        long baseMillis = month11.getMillis();
        
        // Test duration calculations from 11th month of leap year
        assertEquals("1 month duration", 1L * 30L * MILLIS_PER_DAY, monthField.getMillis(1, baseMillis));
        assertEquals("2 months duration", 2L * 30L * MILLIS_PER_DAY, monthField.getMillis(2, baseMillis));
        // 3 months includes 13th month with 6 days (leap year)
        assertEquals("3 months duration (includes leap 13th)", (2L * 30L + 6L) * MILLIS_PER_DAY, 
                    monthField.getMillis(3, baseMillis));
        assertEquals("4 months duration", (3L * 30L + 6L) * MILLIS_PER_DAY, monthField.getMillis(4, baseMillis));
        
        // Test average month calculations
        assertEquals("Average month duration", 1L * 30L * MILLIS_PER_DAY, monthField.getMillis(1));
        assertEquals("13 average months", 13L * 30L * MILLIS_PER_DAY, monthField.getMillis(13));
        
        // Test long parameter versions
        assertEquals("1 month (long)", 1L * 30L * MILLIS_PER_DAY, monthField.getMillis(1L, baseMillis));
        assertEquals("3 months (long, leap)", (2L * 30L + 6L) * MILLIS_PER_DAY, monthField.getMillis(3L, baseMillis));
        
        // Test value calculations
        long oneMonthDuration = 1L * 30L * MILLIS_PER_DAY;
        assertEquals("Just under 1 month", 0, monthField.getValue(oneMonthDuration - 1L, baseMillis));
        assertEquals("Exactly 1 month", 1, monthField.getValue(oneMonthDuration, baseMillis));
        assertEquals("Just over 1 month", 1, monthField.getValue(oneMonthDuration + 1L, baseMillis));
        
        long threeMonthDuration = (2L * 30L + 6L) * MILLIS_PER_DAY;
        assertEquals("Just under 3 months", 2, monthField.getValue(threeMonthDuration - 1L, baseMillis));
        assertEquals("Exactly 3 months", 3, monthField.getValue(threeMonthDuration, baseMillis));
        assertEquals("Just over 3 months", 3, monthField.getValue(threeMonthDuration + 1L, baseMillis));
        
        // Test month addition
        assertEquals("Add 1 month", month12.getMillis(), monthField.add(baseMillis, 1));
        assertEquals("Add 2 months", month13.getMillis(), monthField.add(baseMillis, 2));
        assertEquals("Add 3 months", nextYearMonth1.getMillis(), monthField.add(baseMillis, 3));
        
        // Test long parameter versions
        assertEquals("Add 2 months (long)", month13.getMillis(), monthField.add(baseMillis, 2L));
        assertEquals("Add 3 months (long)", nextYearMonth1.getMillis(), monthField.add(baseMillis, 3L));
    }

    // ========================================================================
    // Leap Year and Day Tests  
    // ========================================================================

    /**
     * Test leap day detection for 5th day of 13th month in leap year
     */
    public void testLeapDay_FifthDayOfThirteenthMonth() {
        Chronology copticChronology = CopticChronology.getInstance();
        DateTime leapYearDate = new DateTime(3, 13, 5, 0, 0, copticChronology);
        
        // Year 3 % 4 == 3, so it's a leap year
        assertEquals("Year 3 should be leap year", true, leapYearDate.year().isLeap());
        assertEquals("13th month should be leap in leap year", true, leapYearDate.monthOfYear().isLeap());
        assertEquals("5th day of 13th month should not be leap day", false, leapYearDate.dayOfMonth().isLeap());
        assertEquals("Day of year should not be leap", false, leapYearDate.dayOfYear().isLeap());
    }

    /**
     * Test leap day detection for 6th day of 13th month in leap year
     */
    public void testLeapDay_SixthDayOfThirteenthMonth() {
        Chronology copticChronology = CopticChronology.getInstance();
        DateTime leapDayDate = new DateTime(3, 13, 6, 0, 0, copticChronology);
        
        // Year 3 % 4 == 3, so it's a leap year
        assertEquals("Year 3 should be leap year", true, leapDayDate.year().isLeap());
        assertEquals("13th month should be leap in leap year", true, leapDayDate.monthOfYear().isLeap());
        assertEquals("6th day of 13th month should be leap day", true, leapDayDate.dayOfMonth().isLeap());
        assertEquals("6th day of year 365+1 should be leap day", true, leapDayDate.dayOfYear().isLeap());
    }

}