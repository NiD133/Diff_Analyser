/*
 *  Copyright 2001-2013 Stephen Colebourne
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
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;

/**
 * Unit tests for IslamicChronology, focusing on Islamic calendar functionality.
 * Includes tests for chronology creation, field properties, date conversions, 
 * and leap year patterns.
 */
public class TestIslamicChronology extends TestCase {

    private static long SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;

    // Time zones for testing
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
    // Chronologies for testing
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Base calculation for fixed test time (2002-06-09)
    private long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    // Environment preservation
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        return new TestSuite(TestIslamicChronology.class);
    }

    public TestIslamicChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Freeze time at TEST_TIME_NOW for consistent test results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set default to London time zone and UK locale
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original environment
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    // Tests for chronology instance creation
    //-----------------------------------------------------------------------
    
    public void testFactoryUTC() {
        assertSame("UTC instance should be reused", IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
        assertEquals("UTC time zone", DateTimeZone.UTC, IslamicChronology.getInstanceUTC().getZone());
    }

    public void testFactoryDefaultZone() {
        Chronology instance = IslamicChronology.getInstance();
        assertEquals("Default zone should be London", LONDON, instance.getZone());
        assertSame("Instance should be IslamicChronology", IslamicChronology.class, instance.getClass());
    }

    public void testFactorySpecificZone() {
        assertEquals("TOKYO time zone", TOKYO, IslamicChronology.getInstance(TOKYO).getZone());
        assertEquals("PARIS time zone", PARIS, IslamicChronology.getInstance(PARIS).getZone());
        assertEquals("Null parameter should default to London", LONDON, IslamicChronology.getInstance(null).getZone());
    }

    //-----------------------------------------------------------------------
    // Tests for chronology instance management
    //-----------------------------------------------------------------------

    public void testChronologySingleton() {
        assertSame("Same instance for TOKYO", IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO));
        assertSame("Same instance for UTC", IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
    }

    public void testWithUTC() {
        assertSame("UTC conversion", IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(LONDON).withUTC());
        assertSame("UTC conversion", IslamicChronology.getInstanceUTC(), IslamicChronology.getInstance(TOKYO).withUTC());
    }

    public void testWithZone() {
        assertSame("Same zone", IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame("Different zone", IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame("Null zone defaults to London", IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(TOKYO).withZone(null));
    }

    public void testToString() {
        assertEquals("String representation for London", "IslamicChronology[Europe/London]", IslamicChronology.getInstance(LONDON).toString());
        assertEquals("String representation for UTC", "IslamicChronology[UTC]", IslamicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Tests for duration fields
    //-----------------------------------------------------------------------

    public void testDurationFields() {
        IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("eras", islamic.eras().getName());
        assertEquals("centuries", islamic.centuries().getName());
        assertEquals("years", islamic.years().getName());
        assertEquals("months", islamic.months().getName());
        
        // Test field support
        assertFalse("Eras not supported", islamic.eras().isSupported());
        assertTrue("Years supported", islamic.years().isSupported());
        assertTrue("Months supported", islamic.months().isSupported());
        
        // Test precision
        assertFalse("Centuries not precise", islamic.centuries().isPrecise());
        assertTrue("Hours precise", islamic.hours().isPrecise());
    }

    //-----------------------------------------------------------------------
    // Tests for date fields
    //-----------------------------------------------------------------------

    public void testDateFields() {
        IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("era", islamic.era().getName());
        assertEquals("year", islamic.year().getName());
        assertEquals("monthOfYear", islamic.monthOfYear().getName());
        
        // Test field support
        assertTrue("Era field supported", islamic.era().isSupported());
        assertTrue("Year field supported", islamic.year().isSupported());
        
        // Test duration field links
        assertSame("Era duration field", islamic.eras(), islamic.era().getDurationField());
        assertSame("Year duration field", islamic.years(), islamic.year().getDurationField());
    }

    //-----------------------------------------------------------------------
    // Tests for time fields
    //-----------------------------------------------------------------------

    public void testTimeFields() {
        IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("hourOfDay", islamic.hourOfDay().getName());
        assertEquals("minuteOfHour", islamic.minuteOfHour().getName());
        
        // Test field support
        assertTrue("Hour field supported", islamic.hourOfDay().isSupported());
        assertTrue("Minute field supported", islamic.minuteOfHour().isSupported());
    }

    //-----------------------------------------------------------------------
    // Tests for epoch and date ranges
    //-----------------------------------------------------------------------

    public void testEpoch() {
        DateTime islamicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime julianEpoch = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals("Islamic epoch should match Julian date", julianEpoch.getMillis(), islamicEpoch.getMillis());
    }

    public void testInvalidEra() {
        try {
            // Attempt to create date with invalid era (negative year)
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ISLAMIC_UTC);
            fail("Expected IllegalArgumentException for negative year");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Tests for date conversions
    //-----------------------------------------------------------------------

    public void testFieldConstructor() {
        DateTime islamicDate = new DateTime(1364, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedDate = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        assertEquals("Islamic date should convert to ISO date", expectedDate.getMillis(), islamicDate.getMillis());
    }

    //-----------------------------------------------------------------------
    // Comprehensive calendar validation tests
    //-----------------------------------------------------------------------

    /**
     * Comprehensive test of calendar progression over a long period.
     * Verifies day, month, year, and leap year calculations.
     */
    public void testCalendar() {
        if (TestAll.FAST) {
            return; // Skip long-running test in fast mode
        }

        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        
        // Fields for calendar components
        DateTimeField dayOfWeek = ISLAMIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ISLAMIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ISLAMIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ISLAMIC_UTC.monthOfYear();
        DateTimeField year = ISLAMIC_UTC.year();
        
        // Initial calendar state (AH 1-1-1)
        int expectedDOW = 6; // Saturday (Julian epoch: 622-07-16 was a Saturday)
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;

        while (millis < end) {
            // Get current field values
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            int yearLen = dayOfYear.getMaximumValue(millis);

            // Verify day of week progression
            assertEquals("Day of week mismatch", expectedDOW, dowValue);
            // Verify date components
            assertEquals("Day of month mismatch", expectedDay, dayValue);
            assertEquals("Month mismatch", expectedMonth, monthValue);
            assertEquals("Year mismatch", expectedYear, yearValue);
            assertEquals("Day of year mismatch", expectedDOY, doyValue);

            // Calculate leap year status
            boolean isLeapYear = ((11 * yearValue + 14) % 30) < 11;
            assertEquals("Leap year status", isLeapYear, year.isLeap(millis));
            assertEquals("Year length", isLeapYear ? 355 : 354, yearLen);

            // Verify month lengths
            switch (monthValue) {
                case 1: case 3: case 5: case 7: case 9: case 11:
                    assertEquals("Long month length", 30, monthLen);
                    break;
                case 2: case 4: case 6: case 8: case 10:
                    assertEquals("Short month length", 29, monthLen);
                    break;
                case 12:
                    assertEquals("Final month length", isLeapYear ? 30 : 29, monthLen);
                    break;
            }

            // Advance to next day
            expectedDOW = (expectedDOW % 7) + 1; // Cycle 1-7
            expectedDay++;
            expectedDOY++;
            
            // Handle month/year transitions
            if (expectedDay > monthLen) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth > 12) {
                    expectedMonth = 1;
                    expectedDOY = 1;
                    expectedYear++;
                }
            }
            millis += SKIP; // Advance by one day
        }
    }

    //-----------------------------------------------------------------------
    // Tests for specific dates
    //-----------------------------------------------------------------------

    /**
     * Test for 1945-11-12 (ISO) which is 1364-12-06 (Islamic).
     */
    public void testSampleDate1() {
        DateTime dt = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC)
            .withChronology(ISLAMIC_UTC);
        
        assertEquals("Era", IslamicChronology.AH, dt.getEra());
        assertEquals("Year", 1364, dt.getYear());
        assertEquals("Month", 12, dt.getMonthOfYear());
        assertEquals("Day", 6, dt.getDayOfMonth());
        assertEquals("Day of week", DateTimeConstants.MONDAY, dt.getDayOfWeek());
        
        // Year property tests
        Property yearProp = dt.year();
        assertFalse("1364 should not be leap", yearProp.isLeap());
        assertEquals("Leap amount", 0, yearProp.getLeapAmount());
    }

    /**
     * Test for 2005-11-26 (ISO) which is 1426-10-24 (Islamic).
     */
    public void testSampleDate2() {
        DateTime dt = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC)
            .withChronology(ISLAMIC_UTC);
        
        assertEquals("Era", IslamicChronology.AH, dt.getEra());
        assertEquals("Year", 1426, dt.getYear());
        assertEquals("Month", 10, dt.getMonthOfYear());
        assertEquals("Day", 24, dt.getDayOfMonth());
        assertEquals("Day of week", DateTimeConstants.SATURDAY, dt.getDayOfWeek());
        
        // Year property tests
        Property yearProp = dt.year();
        assertTrue("1426 should be leap", yearProp.isLeap());
        assertEquals("Leap amount", 1, yearProp.getLeapAmount());
    }

    /**
     * Test for 1426-12-24 (Islamic).
     */
    public void testSampleDate3() {
        DateTime dt = new DateTime(1426, 12, 24, 0, 0, 0, 0, ISLAMIC_UTC);
        
        assertEquals("Era", IslamicChronology.AH, dt.getEra());
        assertEquals("Year", 1426, dt.getYear());
        assertEquals("Month", 12, dt.getMonthOfYear());
        assertEquals("Day", 24, dt.getDayOfMonth());
        assertEquals("Day of week", DateTimeConstants.TUESDAY, dt.getDayOfWeek());
        
        // Month property tests (leap month)
        Property monthProp = dt.monthOfYear();
        assertTrue("Month 12 in leap year should be leap", monthProp.isLeap());
        assertEquals("Leap amount", 1, monthProp.getLeapAmount());
    }

    //-----------------------------------------------------------------------
    // Tests for time zone handling
    //-----------------------------------------------------------------------

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS)
            .withChronology(ISLAMIC_UTC);
        
        assertEquals("Hour adjusted for time zone", 11, dt.getHourOfDay()); // PARIS is UTC+1 in summer
        assertEquals("Date components unchanged", 
            new DateTime(1426, 10, 24, 11, 0, 0, 0, ISLAMIC_UTC), dt);
    }

    //-----------------------------------------------------------------------
    // Tests for leap year patterns
    //-----------------------------------------------------------------------

    public void test15BasedLeapYear() {
        assertFalse("Year 1 not leap", IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(1));
        assertTrue("Year 2 leap", IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(2));
        assertFalse("Year 30 not leap", IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(30));
    }

    public void test16BasedLeapYear() {
        assertFalse("Year 1 not leap", IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(1));
        assertTrue("Year 2 leap", IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(2));
        assertFalse("Year 30 not leap", IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(30));
    }

    public void testIndianBasedLeapYear() {
        assertFalse("Year 1 not leap", IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(1));
        assertTrue("Year 2 leap", IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(2));
        assertFalse("Year 30 not leap", IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(30));
    }

    public void testHabashAlHasibBasedLeapYear() {
        assertFalse("Year 1 not leap", IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(1));
        assertTrue("Year 2 leap", IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(2));
        assertTrue("Year 30 leap", IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(30));
    }
}