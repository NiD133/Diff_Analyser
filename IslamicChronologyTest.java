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
 * This class is a JUnit unit test for IslamicChronology.
 *
 * @author Stephen Colebourne
 */
public class TestIslamicChronology extends TestCase {

    // Test configuration constants
    private static final long ONE_DAY_IN_MILLIS = DateTimeConstants.MILLIS_PER_DAY;
    private static long SKIP = ONE_DAY_IN_MILLIS;

    // Time zones used in tests
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
    // Chronology instances used in tests
    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Test date: June 9, 2002 (calculated as days from epoch)
    private static final long DAYS_TO_2002 = calculateDaysTo2002();
    private static final long TEST_TIME_NOW = 
            (DAYS_TO_2002 + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * ONE_DAY_IN_MILLIS;

    // Original system settings to restore after tests
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    /**
     * Calculates the number of days from epoch to year 2002.
     * This represents 32 years of days including leap years.
     */
    private static long calculateDaysTo2002() {
        return 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
               366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
               365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
               366 + 365;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = ONE_DAY_IN_MILLIS;
        return new TestSuite(TestIslamicChronology.class);
    }

    public TestIslamicChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Fix current time for consistent test results
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        
        // Store original system settings
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set test environment to London/UK
        DateTimeZone.setDefault(LONDON);
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
    // Factory method tests
    //-----------------------------------------------------------------------
    
    public void testFactoryUTC_ReturnsUTCChronology() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        assertEquals("Should use UTC timezone", DateTimeZone.UTC, chronology.getZone());
        assertSame("Should return IslamicChronology instance", 
                   IslamicChronology.class, chronology.getClass());
    }

    public void testFactory_ReturnsDefaultTimezoneChronology() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        assertEquals("Should use default timezone (London)", LONDON, chronology.getZone());
        assertSame("Should return IslamicChronology instance", 
                   IslamicChronology.class, chronology.getClass());
    }

    public void testFactoryWithZone_ReturnsChronologyWithSpecifiedZone() {
        assertEquals("Should use Tokyo timezone", 
                     TOKYO, IslamicChronology.getInstance(TOKYO).getZone());
        assertEquals("Should use Paris timezone", 
                     PARIS, IslamicChronology.getInstance(PARIS).getZone());
        assertEquals("Null zone should default to London", 
                     LONDON, IslamicChronology.getInstance(null).getZone());
        assertSame("Should return IslamicChronology instance", 
                   IslamicChronology.class, IslamicChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    // Instance equality and identity tests
    //-----------------------------------------------------------------------
    
    public void testEquality_SameZoneReturnsSameInstance() {
        assertSame("Tokyo instances should be identical", 
                   IslamicChronology.getInstance(TOKYO), IslamicChronology.getInstance(TOKYO));
        assertSame("London instances should be identical", 
                   IslamicChronology.getInstance(LONDON), IslamicChronology.getInstance(LONDON));
        assertSame("Paris instances should be identical", 
                   IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance(PARIS));
        assertSame("UTC instances should be identical", 
                   IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
        assertSame("Default should equal London instance", 
                   IslamicChronology.getInstance(), IslamicChronology.getInstance(LONDON));
    }

    public void testWithUTC_ConvertsToUTCChronology() {
        IslamicChronology utcInstance = IslamicChronology.getInstanceUTC();
        
        assertSame("London chronology should convert to UTC", 
                   utcInstance, IslamicChronology.getInstance(LONDON).withUTC());
        assertSame("Tokyo chronology should convert to UTC", 
                   utcInstance, IslamicChronology.getInstance(TOKYO).withUTC());
        assertSame("UTC chronology should return itself", 
                   utcInstance, utcInstance.withUTC());
        assertSame("Default chronology should convert to UTC", 
                   utcInstance, IslamicChronology.getInstance().withUTC());
    }

    public void testWithZone_ConvertsToSpecifiedTimezone() {
        IslamicChronology tokyoChronology = IslamicChronology.getInstance(TOKYO);
        
        assertSame("Same zone should return same instance", 
                   IslamicChronology.getInstance(TOKYO), tokyoChronology.withZone(TOKYO));
        assertSame("Should convert to London", 
                   IslamicChronology.getInstance(LONDON), tokyoChronology.withZone(LONDON));
        assertSame("Should convert to Paris", 
                   IslamicChronology.getInstance(PARIS), tokyoChronology.withZone(PARIS));
        assertSame("Null zone should default to London", 
                   IslamicChronology.getInstance(LONDON), tokyoChronology.withZone(null));
        assertSame("Default should convert to Paris", 
                   IslamicChronology.getInstance(PARIS), IslamicChronology.getInstance().withZone(PARIS));
        assertSame("UTC should convert to Paris", 
                   IslamicChronology.getInstance(PARIS), IslamicChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString_ShowsChronologyNameAndTimezone() {
        assertEquals("IslamicChronology[Europe/London]", 
                     IslamicChronology.getInstance(LONDON).toString());
        assertEquals("IslamicChronology[Asia/Tokyo]", 
                     IslamicChronology.getInstance(TOKYO).toString());
        assertEquals("IslamicChronology[Europe/London]", 
                     IslamicChronology.getInstance().toString());
        assertEquals("IslamicChronology[UTC]", 
                     IslamicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Duration field tests
    //-----------------------------------------------------------------------
    
    public void testDurationFields_NamesAndSupport() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("eras", islamic.eras().getName());
        assertEquals("centuries", islamic.centuries().getName());
        assertEquals("years", islamic.years().getName());
        assertEquals("weekyears", islamic.weekyears().getName());
        assertEquals("months", islamic.months().getName());
        assertEquals("weeks", islamic.weeks().getName());
        assertEquals("days", islamic.days().getName());
        assertEquals("halfdays", islamic.halfdays().getName());
        assertEquals("hours", islamic.hours().getName());
        assertEquals("minutes", islamic.minutes().getName());
        assertEquals("seconds", islamic.seconds().getName());
        assertEquals("millis", islamic.millis().getName());
        
        // Test field support
        assertEquals("Eras should not be supported", false, islamic.eras().isSupported());
        assertEquals("Centuries should be supported", true, islamic.centuries().isSupported());
        assertEquals("Years should be supported", true, islamic.years().isSupported());
        assertEquals("Weekyears should be supported", true, islamic.weekyears().isSupported());
        assertEquals("Months should be supported", true, islamic.months().isSupported());
        assertEquals("Weeks should be supported", true, islamic.weeks().isSupported());
        assertEquals("Days should be supported", true, islamic.days().isSupported());
        assertEquals("Halfdays should be supported", true, islamic.halfdays().isSupported());
        assertEquals("Hours should be supported", true, islamic.hours().isSupported());
        assertEquals("Minutes should be supported", true, islamic.minutes().isSupported());
        assertEquals("Seconds should be supported", true, islamic.seconds().isSupported());
        assertEquals("Millis should be supported", true, islamic.millis().isSupported());
    }

    public void testDurationFields_PrecisionInTimezone() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Calendar fields are not precise due to timezone variations
        assertEquals("Centuries not precise in timezone", false, islamic.centuries().isPrecise());
        assertEquals("Years not precise in timezone", false, islamic.years().isPrecise());
        assertEquals("Weekyears not precise in timezone", false, islamic.weekyears().isPrecise());
        assertEquals("Months not precise in timezone", false, islamic.months().isPrecise());
        assertEquals("Weeks not precise in timezone", false, islamic.weeks().isPrecise());
        assertEquals("Days not precise in timezone", false, islamic.days().isPrecise());
        assertEquals("Halfdays not precise in timezone", false, islamic.halfdays().isPrecise());
        
        // Time fields are precise
        assertEquals("Hours should be precise", true, islamic.hours().isPrecise());
        assertEquals("Minutes should be precise", true, islamic.minutes().isPrecise());
        assertEquals("Seconds should be precise", true, islamic.seconds().isPrecise());
        assertEquals("Millis should be precise", true, islamic.millis().isPrecise());
    }

    public void testDurationFields_PrecisionInUTC() {
        final IslamicChronology islamicUTC = IslamicChronology.getInstanceUTC();
        
        // Variable length fields are not precise even in UTC
        assertEquals("Centuries not precise", false, islamicUTC.centuries().isPrecise());
        assertEquals("Years not precise", false, islamicUTC.years().isPrecise());
        assertEquals("Weekyears not precise", false, islamicUTC.weekyears().isPrecise());
        assertEquals("Months not precise", false, islamicUTC.months().isPrecise());
        
        // Fixed length fields are precise in UTC
        assertEquals("Weeks should be precise in UTC", true, islamicUTC.weeks().isPrecise());
        assertEquals("Days should be precise in UTC", true, islamicUTC.days().isPrecise());
        assertEquals("Halfdays should be precise in UTC", true, islamicUTC.halfdays().isPrecise());
        assertEquals("Hours should be precise in UTC", true, islamicUTC.hours().isPrecise());
        assertEquals("Minutes should be precise in UTC", true, islamicUTC.minutes().isPrecise());
        assertEquals("Seconds should be precise in UTC", true, islamicUTC.seconds().isPrecise());
        assertEquals("Millis should be precise in UTC", true, islamicUTC.millis().isPrecise());
    }

    public void testDurationFields_PrecisionInGMT() {
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final IslamicChronology islamicGMT = IslamicChronology.getInstance(gmt);
        
        // Variable length fields are not precise
        assertEquals("Centuries not precise in GMT", false, islamicGMT.centuries().isPrecise());
        assertEquals("Years not precise in GMT", false, islamicGMT.years().isPrecise());
        assertEquals("Weekyears not precise in GMT", false, islamicGMT.weekyears().isPrecise());
        assertEquals("Months not precise in GMT", false, islamicGMT.months().isPrecise());
        
        // Fixed length fields are precise in GMT (no DST)
        assertEquals("Weeks should be precise in GMT", true, islamicGMT.weeks().isPrecise());
        assertEquals("Days should be precise in GMT", true, islamicGMT.days().isPrecise());
        assertEquals("Halfdays should be precise in GMT", true, islamicGMT.halfdays().isPrecise());
        assertEquals("Hours should be precise in GMT", true, islamicGMT.hours().isPrecise());
        assertEquals("Minutes should be precise in GMT", true, islamicGMT.minutes().isPrecise());
        assertEquals("Seconds should be precise in GMT", true, islamicGMT.seconds().isPrecise());
        assertEquals("Millis should be precise in GMT", true, islamicGMT.millis().isPrecise());
    }

    //-----------------------------------------------------------------------
    // Date field tests
    //-----------------------------------------------------------------------
    
    public void testDateFields_NamesAndSupport() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("era", islamic.era().getName());
        assertEquals("centuryOfEra", islamic.centuryOfEra().getName());
        assertEquals("yearOfCentury", islamic.yearOfCentury().getName());
        assertEquals("yearOfEra", islamic.yearOfEra().getName());
        assertEquals("year", islamic.year().getName());
        assertEquals("monthOfYear", islamic.monthOfYear().getName());
        assertEquals("weekyearOfCentury", islamic.weekyearOfCentury().getName());
        assertEquals("weekyear", islamic.weekyear().getName());
        assertEquals("weekOfWeekyear", islamic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", islamic.dayOfYear().getName());
        assertEquals("dayOfMonth", islamic.dayOfMonth().getName());
        assertEquals("dayOfWeek", islamic.dayOfWeek().getName());
        
        // All date fields should be supported
        assertEquals("Era should be supported", true, islamic.era().isSupported());
        assertEquals("Century of era should be supported", true, islamic.centuryOfEra().isSupported());
        assertEquals("Year of century should be supported", true, islamic.yearOfCentury().isSupported());
        assertEquals("Year of era should be supported", true, islamic.yearOfEra().isSupported());
        assertEquals("Year should be supported", true, islamic.year().isSupported());
        assertEquals("Month of year should be supported", true, islamic.monthOfYear().isSupported());
        assertEquals("Weekyear of century should be supported", true, islamic.weekyearOfCentury().isSupported());
        assertEquals("Weekyear should be supported", true, islamic.weekyear().isSupported());
        assertEquals("Week of weekyear should be supported", true, islamic.weekOfWeekyear().isSupported());
        assertEquals("Day of year should be supported", true, islamic.dayOfYear().isSupported());
        assertEquals("Day of month should be supported", true, islamic.dayOfMonth().isSupported());
        assertEquals("Day of week should be supported", true, islamic.dayOfWeek().isSupported());
    }

    public void testDateFields_DurationFieldAssociations() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test duration field associations
        assertEquals(islamic.eras(), islamic.era().getDurationField());
        assertEquals(islamic.centuries(), islamic.centuryOfEra().getDurationField());
        assertEquals(islamic.years(), islamic.yearOfCentury().getDurationField());
        assertEquals(islamic.years(), islamic.yearOfEra().getDurationField());
        assertEquals(islamic.years(), islamic.year().getDurationField());
        assertEquals(islamic.months(), islamic.monthOfYear().getDurationField());
        assertEquals(islamic.weekyears(), islamic.weekyearOfCentury().getDurationField());
        assertEquals(islamic.weekyears(), islamic.weekyear().getDurationField());
        assertEquals(islamic.weeks(), islamic.weekOfWeekyear().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfYear().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfMonth().getDurationField());
        assertEquals(islamic.days(), islamic.dayOfWeek().getDurationField());
    }

    public void testDateFields_RangeDurationFieldAssociations() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test range duration field associations
        assertEquals(null, islamic.era().getRangeDurationField());
        assertEquals(islamic.eras(), islamic.centuryOfEra().getRangeDurationField());
        assertEquals(islamic.centuries(), islamic.yearOfCentury().getRangeDurationField());
        assertEquals(islamic.eras(), islamic.yearOfEra().getRangeDurationField());
        assertEquals(null, islamic.year().getRangeDurationField());
        assertEquals(islamic.years(), islamic.monthOfYear().getRangeDurationField());
        assertEquals(islamic.centuries(), islamic.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, islamic.weekyear().getRangeDurationField());
        assertEquals(islamic.weekyears(), islamic.weekOfWeekyear().getRangeDurationField());
        assertEquals(islamic.years(), islamic.dayOfYear().getRangeDurationField());
        assertEquals(islamic.months(), islamic.dayOfMonth().getRangeDurationField());
        assertEquals(islamic.weeks(), islamic.dayOfWeek().getRangeDurationField());
    }

    //-----------------------------------------------------------------------
    // Time field tests
    //-----------------------------------------------------------------------
    
    public void testTimeFields_NamesAndSupport() {
        final IslamicChronology islamic = IslamicChronology.getInstance();
        
        // Test field names
        assertEquals("halfdayOfDay", islamic.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", islamic.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", islamic.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", islamic.clockhourOfDay().getName());
        assertEquals("hourOfDay", islamic.hourOfDay().getName());
        assertEquals("minuteOfDay", islamic.minuteOfDay().getName());
        assertEquals("minuteOfHour", islamic.minuteOfHour().getName());
        assertEquals("secondOfDay", islamic.secondOfDay().getName());
        assertEquals("secondOfMinute", islamic.secondOfMinute().getName());
        assertEquals("millisOfDay", islamic.millisOfDay().getName());
        assertEquals("millisOfSecond", islamic.millisOfSecond().getName());
        
        // All time fields should be supported
        assertEquals("Halfday of day should be supported", true, islamic.halfdayOfDay().isSupported());
        assertEquals("Clockhour of halfday should be supported", true, islamic.clockhourOfHalfday().isSupported());
        assertEquals("Hour of halfday should be supported", true, islamic.hourOfHalfday().isSupported());
        assertEquals("Clockhour of day should be supported", true, islamic.clockhourOfDay().isSupported());
        assertEquals("Hour of day should be supported", true, islamic.hourOfDay().isSupported());
        assertEquals("Minute of day should be supported", true, islamic.minuteOfDay().isSupported());
        assertEquals("Minute of hour should be supported", true, islamic.minuteOfHour().isSupported());
        assertEquals("Second of day should be supported", true, islamic.secondOfDay().isSupported());
        assertEquals("Second of minute should be supported", true, islamic.secondOfMinute().isSupported());
        assertEquals("Millis of day should be supported", true, islamic.millisOfDay().isSupported());
        assertEquals("Millis of second should be supported", true, islamic.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    // Islamic calendar specific tests
    //-----------------------------------------------------------------------
    
    public void testEpoch_IslamicYearOneEqualsJulianDate() {
        DateTime islamicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedJulianDate = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC);
        
        assertEquals("Islamic year 1 should equal Julian 622-07-16", 
                     expectedJulianDate.getMillis(), islamicEpoch.getMillis());
    }

    public void testEra_OnlyAHEraSupported() {
        assertEquals("AH era constant should be 1", 1, IslamicChronology.AH);
        
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ISLAMIC_UTC);
            fail("Should not allow dates before Islamic era");
        } catch (IllegalArgumentException expected) {
            // Expected behavior
        }
    }

    public void testFieldConstructor_IslamicToISOConversion() {
        DateTime islamicDate = new DateTime(1364, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC);
        DateTime expectedISODate = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        
        assertEquals("Islamic 1364-12-06 should equal ISO 1945-11-12", 
                     expectedISODate.getMillis(), islamicDate.getMillis());
    }

    //-----------------------------------------------------------------------
    // Comprehensive calendar validation test
    //-----------------------------------------------------------------------
    
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek across a range of dates.
     * This is a comprehensive validation test that checks Islamic calendar calculations
     * against expected values.
     */
    public void testCalendar_ComprehensiveValidation() {
        if (TestAll.FAST) {
            return; // Skip in fast test mode
        }
        
        System.out.println("\nTestIslamicChronology.testCalendar");
        
        // Test range: from Islamic epoch to year 3000 ISO
        DateTime islamicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
        long currentMillis = islamicEpoch.getMillis();
        long endMillis = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        
        // Get field accessors
        DateTimeField dayOfWeek = ISLAMIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ISLAMIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ISLAMIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ISLAMIC_UTC.monthOfYear();
        DateTimeField year = ISLAMIC_UTC.year();
        DateTimeField yearOfEra = ISLAMIC_UTC.yearOfEra();
        DateTimeField era = ISLAMIC_UTC.era();
        
        // Expected initial values (Islamic epoch corresponds to Julian 622-07-16)
        int expectedDayOfWeek = new DateTime(622, 7, 16, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDayOfYear = 1;
        int expectedDayOfMonth = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        
        while (currentMillis < endMillis) {
            // Get actual field values
            int actualDayOfWeek = dayOfWeek.get(currentMillis);
            int actualDayOfYear = dayOfYear.get(currentMillis);
            int actualDayOfMonth = dayOfMonth.get(currentMillis);
            int actualMonth = monthOfYear.get(currentMillis);
            int actualYear = year.get(currentMillis);
            int actualYearOfEra = yearOfEra.get(currentMillis);
            int yearLength = dayOfYear.getMaximumValue(currentMillis);
            int monthLength = dayOfMonth.getMaximumValue(currentMillis);
            
            // Validate month range
            if (actualMonth < 1 || actualMonth > 12) {
                fail("Invalid month value: " + actualMonth + " at millis: " + currentMillis);
            }
            
            // Test era
            assertEquals("Era should always be AH (1)", 1, era.get(currentMillis));
            assertEquals("Era text should be 'AH'", "AH", era.getAsText(currentMillis));
            assertEquals("Era short text should be 'AH'", "AH", era.getAsShortText(currentMillis));
            
            // Test date field values
            assertEquals("Day of year mismatch", expectedDayOfYear, actualDayOfYear);
            assertEquals("Month mismatch", expectedMonth, actualMonth);
            assertEquals("Day of month mismatch", expectedDayOfMonth, actualDayOfMonth);
            assertEquals("Day of week mismatch", expectedDayOfWeek, actualDayOfWeek);
            assertEquals("Year mismatch", expectedYear, actualYear);
            assertEquals("Year of era mismatch", expectedYear, actualYearOfEra);
            
            // Test leap year calculation
            boolean isLeapYear = ((11 * actualYear + 14) % 30) < 11;
            assertEquals("Leap year calculation mismatch", isLeapYear, year.isLeap(currentMillis));
            
            // Test month lengths according to Islamic calendar rules
            validateMonthLength(actualMonth, monthLength, isLeapYear);
            
            // Test year length
            int expectedYearLength = isLeapYear ? 355 : 354;
            assertEquals("Year length mismatch", expectedYearLength, yearLength);
            
            // Calculate next day's expected values
            expectedDayOfWeek = (expectedDayOfWeek % 7) + 1;
            expectedDayOfMonth++;
            expectedDayOfYear++;
            
            if (expectedDayOfMonth > monthLength) {
                expectedDayOfMonth = 1;
                expectedMonth++;
                if (expectedMonth == 13) {
                    expectedMonth = 1;
                    expectedDayOfYear = 1;
                    expectedYear++;
                }
            }
            
            currentMillis += SKIP;
        }
    }

    /**
     * Validates Islamic month lengths according to calendar rules.
     */
    private void validateMonthLength(int month, int actualLength, boolean isLeapYear) {
        int expectedLength;
        
        switch (month) {
            case 1: case 3: case 5: case 7: case 9: case 11:
                expectedLength = 30; // Odd months have 30 days
                break;
            case 2: case 4: case 6: case 8: case 10:
                expectedLength = 29; // Even months have 29 days
                break;
            case 12:
                expectedLength = isLeapYear ? 30 : 29; // Last month varies with leap year
                break;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
        
        assertEquals("Month " + month + " length mismatch (leap=" + isLeapYear + ")", 
                     expectedLength, actualLength);
    }

    //-----------------------------------------------------------------------
    // Sample date tests with detailed field validation
    //-----------------------------------------------------------------------
    
    /**
     * Tests Islamic date 1364-12-06 which corresponds to ISO 1945-11-12.
     * This is a non-leap year test case.
     */
    public void testSampleDate1_NonLeapYear() {
        // Convert ISO date to Islamic chronology
        DateTime isoDate = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        DateTime islamicDate = isoDate.withChronology(ISLAMIC_UTC);
        
        // Test era and year fields
        assertEquals("Era should be AH", IslamicChronology.AH, islamicDate.getEra());
        assertEquals("Century should be 14", 14, islamicDate.getCenturyOfEra());
        assertEquals("Year of century should be 64", 64, islamicDate.getYearOfCentury());
        assertEquals("Year of era should be 1364", 1364, islamicDate.getYearOfEra());
        assertEquals("Year should be 1364", 1364, islamicDate.getYear());
        
        // Test year properties
        Property yearProperty = islamicDate.year();
        assertEquals("1364 should not be a leap year", false, yearProperty.isLeap());
        assertEquals("Leap amount should be 0", 0, yearProperty.getLeapAmount());
        assertEquals("Leap duration should be days", 
                     DurationFieldType.days(), yearProperty.getLeapDurationField().getType());
        assertEquals("Adding 1 year should give 1365-12-06", 
                     new DateTime(1365, 12, 6, 0, 0, 0, 0, ISLAMIC_UTC), yearProperty.addToCopy(1));
        
        // Test month fields
        assertEquals("Month should be 12", 12, islamicDate.getMonthOfYear());
        Property monthProperty = islamicDate.monthOfYear();
        assertEquals("Month should not be leap", false, monthProperty.isLeap());
        assertEquals("Month leap amount should be 0", 0, monthProperty.getLeapAmount());
        assertEquals("Month leap duration should be days", 
                     DurationFieldType.days(), monthProperty.getLeapDurationField().getType());
        assertEquals("Month minimum should be 1", 1, monthProperty.getMinimumValue());
        assertEquals("Month minimum overall should be 1", 1, monthProperty.getMinimumValueOverall());
        assertEquals("Month maximum should be 12", 12, monthProperty.getMaximumValue());
        assertEquals("Month maximum overall should be 12", 12, monthProperty.getMaximumValueOverall());
        assertEquals("Adding 1 month should give 1365-01-06", 
                     new DateTime(1365, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), monthProperty.addToCopy(1));
        assertEquals("Wrapping 1 month should give 1364-01-06", 
                     new DateTime(1364, 1, 6, 0, 0, 0, 0, ISLAMIC_UTC), monthProperty.addWrapFieldToCopy(1));
        
        // Test day of month fields
        assertEquals("Day of month should be 6", 6, islamicDate.getDayOfMonth());
        Property dayProperty = islamicDate.dayOfMonth();
        assertEquals("Day should not be leap", false, dayProperty.isLeap());
        assertEquals("Day leap amount should be 0", 0, dayProperty.getLeapAmount());
        assertEquals("Day should have no leap duration field", null, dayProperty.getLeapDurationField());
        assertEquals("Day minimum should be 1", 1, dayProperty.getMinimumValue());
        assertEquals("Day minimum overall should be 1", 1, dayProperty.getMinimumValueOverall());
        assertEquals("Day maximum should be 29 (month 12 in non-leap year)", 29, dayProperty.getMaximumValue());
        assertEquals("Day maximum overall should be 30", 30, dayProperty.getMaximumValueOverall());
        assertEquals("Adding 1 day should give 1364-12-07", 
                     new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), dayProperty.addToCopy(1));
        
        // Test day of week
        assertEquals("Should be Monday", DateTimeConstants.MONDAY, islamicDate.getDayOfWeek());
        Property dowProperty = islamicDate.dayOfWeek();
        assertEquals("Day of week should not be leap", false, dowProperty.isLeap());
        assertEquals("Day of week leap amount should be 0", 0, dowProperty.getLeapAmount());
        assertEquals("Day of week should have no leap duration field", null, dowProperty.getLeapDurationField());
        assertEquals("Day of week minimum should be 1", 1, dowProperty.getMinimumValue());
        assertEquals("Day of week minimum overall should be 1", 1, dowProperty.getMinimumValueOverall());
        assertEquals("Day of week maximum should be 7", 7, dowProperty.getMaximumValue());
        assertEquals("Day of week maximum overall should be 7", 7, dowProperty.getMaximumValueOverall());
        assertEquals("Adding 1 day should give Tuesday (1364-12-07)", 
                     new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), dowProperty.addToCopy(1));
        
        // Test day of year (6 months of 30 days + 5 months of 29 days + 6 days)
        int expectedDayOfYear = 6 * 30 + 5 * 29 + 6;
        assertEquals("Day of year calculation", expectedDayOfYear, islamicDate.getDayOfYear());
        Property doyProperty = islamicDate.dayOfYear();
        assertEquals("Day of year should not be leap", false, doyProperty.isLeap());
        assertEquals("Day of year leap amount should be 0", 0, doyProperty.getLeapAmount());
        assertEquals("Day of year should have no leap duration field", null, doyProperty.getLeapDurationField());
        assertEquals("Day of year minimum should be 1", 1, doyProperty.getMinimumValue());
        assertEquals("Day of year minimum overall should be 1", 1, doyProperty.getMinimumValueOverall());
        assertEquals("Day of year maximum should be 354 (non-leap year)", 354, doyProperty.getMaximumValue());
        assertEquals("Day of year maximum overall should be 355", 355, doyProperty.getMaximumValueOverall());
        assertEquals("Adding 1 day should give 1364-12-07", 
                     new DateTime(1364, 12, 7, 0, 0, 0, 0, ISLAMIC_UTC), doyProperty.addToCopy(1));
        
        // Test time fields (should all be zero)
        assertEquals("Hour should be 0", 0, islamicDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, islamicDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, islamicDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, islamicDate.getMillisOfSecond());
    }

    /**
     * Tests Islamic date 1426-10-24 which corresponds to ISO 2005-11-26.
     * This is a leap year test case.
     */
    public void testSampleDate2_LeapYear() {
        // Convert ISO date to Islamic chronology
        DateTime isoDate = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC);
        DateTime islamicDate = isoDate.withChronology(ISLAMIC_UTC);
        
        // Test era and year fields
        assertEquals("Era should be AH", IslamicChronology.AH, islamicDate.getEra());
        assertEquals("Century should be 15", 15, islamicDate.getCenturyOfEra());
        assertEquals("Year of century should be 26", 26, islamicDate.getYearOfCentury());
        assertEquals("Year of era should be 1426", 1426, islamicDate.getYearOfEra());
        assertEquals("Year should be 1426", 1426, islamicDate.getYear());
        
        // Test year properties (1426 is a leap year)
        Property yearProperty = islamicDate.year();
        assertEquals("1426 should be a leap year", true, yearProperty.isLeap());
        assertEquals("Leap amount should be 1", 1, yearProperty.getLeapAmount());
        assertEquals("Leap duration should be days", 
                     DurationFieldType.days(), yearProperty.getLeapDurationField().getType());
        
        // Test month fields
        assertEquals("Month should be 10", 10, islamicDate.getMonthOfYear());
        Property monthProperty = islamicDate.monthOfYear();
        assertEquals("Month 10 should not be leap", false, monthProperty.isLeap());
        assertEquals("Month leap amount should be 0", 0, monthProperty.getLeapAmount());
        assertEquals("Month leap duration should be days", 
                     DurationFieldType.days(), monthProperty.getLeapDurationField().getType());
        assertEquals("Month minimum should be 1", 1, monthProperty.getMinimumValue());
        assertEquals("Month minimum overall should be 1", 1, monthProperty.getMinimumValueOverall());
        assertEquals("Month maximum should be 12", 12, monthProperty.getMaximumValue());
        assertEquals("Month maximum overall should be 12", 12, monthProperty.getMaximumValueOverall());
        
        // Test day of month fields
        assertEquals("Day of month should be 24", 24, islamicDate.getDayOfMonth());
        Property dayProperty = islamicDate.dayOfMonth();
        assertEquals("Day should not be leap", false, dayProperty.isLeap());
        assertEquals("Day leap amount should be 0", 0, dayProperty.getLeapAmount());
        assertEquals("Day should have no leap duration field", null, dayProperty.getLeapDurationField());
        assertEquals("Day minimum should be 1", 1, dayProperty.getMinimumValue());
        assertEquals("Day minimum overall should be 1", 1, dayProperty.getMinimumValueOverall());
        assertEquals("Day maximum should be 29 (month 10 is even)", 29, dayProperty.getMaximumValue());
        assertEquals("Day maximum overall should be 30", 30, dayProperty.getMaximumValueOverall());
        
        // Test day of week
        assertEquals("Should be Saturday", DateTimeConstants.SATURDAY, islamicDate.getDayOfWeek());
        Property dowProperty = islamicDate.dayOfWeek();
        assertEquals("Day of week should not be leap", false, dowProperty.isLeap());
        assertEquals("Day of week leap amount should be 0", 0, dowProperty.getLeapAmount());
        assertEquals("Day of week should have no leap duration field", null, dowProperty.getLeapDurationField());
        assertEquals("Day of week minimum should be 1", 1, dowProperty.getMinimumValue());
        assertEquals("Day of week minimum overall should be 1", 1, dowProperty.getMinimumValueOverall());
        assertEquals("Day of week maximum should be 7", 7, dowProperty.getMaximumValue());
        assertEquals("Day of week maximum overall should be 7", 7, dowProperty.getMaximumValueOverall());
        
        // Test day of year (5 months of 30 days + 4 months of 29 days + 24 days)
        int expectedDayOfYear = 5 * 30 + 4 * 29 + 24;
        assertEquals("Day of year calculation", expectedDayOfYear, islamicDate.getDayOfYear());
        Property doyProperty = islamicDate.dayOfYear();
        assertEquals("Day of year should not be leap", false, doyProperty.isLeap());
        assertEquals("Day of year leap amount should be 0", 0, doyProperty.getLeapAmount());
        assertEquals("Day of year should have no leap duration field", null, doyProperty.getLeapDurationField());
        assertEquals("Day of year minimum should be 1", 1, doyProperty.getMinimumValue());
        assertEquals("Day of year minimum overall should be 1", 1, doyProperty.getMinimumValueOverall());
        assertEquals("Day of year maximum should be 355 (leap year)", 355, doyProperty.getMaximumValue());
        assertEquals("Day of year maximum overall should be 355", 355, doyProperty.getMaximumValueOverall());
        
        // Test time fields (should all be zero)
        assertEquals("Hour should be 0", 0, islamicDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, islamicDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, islamicDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, islamicDate.getMillisOfSecond());
    }

    /**
     * Tests Islamic date 1426-12-24 in a leap year.
     * This tests the leap month (month 12 in a leap year has 30 days instead of 29).
     */
    public void testSampleDate3_LeapMonth() {
        DateTime islamicDate = new DateTime(1426, 12, 24, 0, 0, 0, 0, ISLAMIC_UTC);
        
        assertEquals("Era should be AH", IslamicChronology.AH, islamicDate.getEra());
        assertEquals("Year should be 1426", 1426, islamicDate.getYear());
        
        // Test year properties (1426 is a leap year)
        Property yearProperty = islamicDate.year();
        assertEquals("1426 should be a leap year", true, yearProperty.isLeap());
        assertEquals("Leap amount should be 1", 1, yearProperty.getLeapAmount());
        assertEquals("Leap duration should be days", 
                     DurationFieldType.days(), yearProperty.getLeapDurationField().getType());
        
        // Test month properties (month 12 in leap year)
        assertEquals("Month should be 12", 12, islamicDate.getMonthOfYear());
        Property monthProperty = islamicDate.monthOfYear();
        assertEquals("Month 12 in leap year should be leap", true, monthProperty.isLeap());
        assertEquals("Month leap amount should be 1", 1, monthProperty.getLeapAmount());
        assertEquals("Month leap duration should be days", 
                     DurationFieldType.days(), monthProperty.getLeapDurationField().getType());
        assertEquals("Month minimum should be 1", 1, monthProperty.getMinimumValue());
        assertEquals("Month minimum overall should be 1", 1, monthProperty.getMinimumValueOverall());
        assertEquals("Month maximum should be 12", 12, monthProperty.getMaximumValue());
        assertEquals("Month maximum overall should be 12", 12, monthProperty.getMaximumValueOverall());
        
        // Test day of month properties
        assertEquals("Day of month should be 24", 24, islamicDate.getDayOfMonth());
        Property dayProperty = islamicDate.dayOfMonth();
        assertEquals("Day should not be leap", false, dayProperty.isLeap());
        assertEquals("Day leap amount should be 0", 0, dayProperty.getLeapAmount());
        assertEquals("Day should have no leap duration field", null, dayProperty.getLeapDurationField());
        assertEquals("Day minimum should be 1", 1, dayProperty.getMinimumValue());
        assertEquals("Day minimum overall should be 1", 1, dayProperty.getMinimumValueOverall());
        assertEquals("Day maximum should be 30 (month 12 in leap year)", 30, dayProperty.getMaximumValue());
        assertEquals("Day maximum overall should be 30", 30, dayProperty.getMaximumValueOverall());
        
        // Test day of week
        assertEquals("Should be Tuesday", DateTimeConstants.TUESDAY, islamicDate.getDayOfWeek());
        Property dowProperty = islamicDate.dayOfWeek();
        assertEquals("Day of week should not be leap", false, dowProperty.isLeap());
        assertEquals("Day of week leap amount should be 0", 0, dowProperty.getLeapAmount());
        assertEquals("Day of week should have no leap duration field", null, dowProperty.getLeapDurationField());
        assertEquals("Day of week minimum should be 1", 1, dowProperty.getMinimumValue());
        assertEquals("Day of week minimum overall should be 1", 1, dowProperty.getMinimumValueOverall());
        assertEquals("Day of week maximum should be 7", 7, dowProperty.getMaximumValue());
        assertEquals("Day of week maximum overall should be 7", 7, dowProperty.getMaximumValueOverall());
        
        // Test day of year (6 months of 30 days + 5 months of 29 days + 24 days)
        int expectedDayOfYear = 6 * 30 + 5 * 29 + 24;
        assertEquals("Day of year calculation", expectedDayOfYear, islamicDate.getDayOfYear());
        Property doyProperty = islamicDate.dayOfYear();
        assertEquals("Day of year should not be leap", false, doyProperty.isLeap());
        assertEquals("Day of year leap amount should be 0", 0, doyProperty.getLeapAmount());
        assertEquals("Day of year should have no leap duration field", null, doyProperty.getLeapDurationField());
        assertEquals("Day of year minimum should be 1", 1, doyProperty.getMinimumValue());
        assertEquals("Day of year minimum overall should be 1", 1, doyProperty.getMinimumValueOverall());
        assertEquals("Day of year maximum should be 355 (leap year)", 355, doyProperty.getMaximumValue());
        assertEquals("Day of year maximum overall should be 355", 355, doyProperty.getMaximumValueOverall());
        
        // Test time fields (should all be zero)
        assertEquals("Hour should be 0", 0, islamicDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, islamicDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, islamicDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, islamicDate.getMillisOfSecond());
    }

    public void testSampleDateWithZone_TimezoneConversion() {
        // Create date in Paris timezone, then convert to Islamic UTC
        DateTime parisDate = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS);
        DateTime islamicDate = parisDate.withChronology(ISLAMIC_UTC);
        
        assertEquals("Era should be AH", IslamicChronology.AH, islamicDate.getEra());
        assertEquals("Year should be 1426", 1426, islamicDate.getYear());
        assertEquals("Month should be 10", 10, islamicDate.getMonthOfYear());
        assertEquals("Day should be 24", 24, islamicDate.getDayOfMonth());
        assertEquals("Hour should be 11 (Paris UTC+1)", 11, islamicDate.getHourOfDay());
        assertEquals("Minute should be 0", 0, islamicDate.getMinuteOfHour());
        assertEquals("Second should be 0", 0, islamicDate.getSecondOfMinute());
        assertEquals("Millisecond should be 0", 0, islamicDate.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    // Leap year pattern tests
    //-----------------------------------------------------------------------
    
    public void test15BasedLeapYear_PatternValidation() {
        // Test the 15-based leap year pattern used by Microsoft
        // Leap years: 2, 5, 7, 10, 13, 15, 18, 21, 24, 26, 29
        boolean[] expectedPattern = {
            false, true, false, false, true, false, true, false, false, true,
            false, false, true, false, true, false, false, true, false, false,
            true, false, false, true, false, true, false, false, true, false
        };
        
        for (int year = 1; year <= 30; year++) {
            boolean expected = expectedPattern[year - 1];
            boolean actual = IslamicChronology.LEAP_YEAR_15_BASED.isLeapYear(year);
            assertEquals("15-based pattern year " + year, expected, actual);
        }
    }

    public void test16BasedLeapYear_PatternValidation() {
        // Test the 16-based leap year pattern (most commonly used)
        // Leap years: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29
        boolean[] expectedPattern = {
            false, true, false, false, true, false, true, false, false, true,
            false, false, true, false, false, true, false, true, false, false,
            true, false, false, true, false, true, false, false, true, false
        };
        
        for (int year = 1; year <= 30; year++) {
            boolean expected = expectedPattern[year - 1];
            boolean actual = IslamicChronology.LEAP_YEAR_16_BASED.isLeapYear(year);
            assertEquals("16-based pattern year " + year, expected, actual);
        }
    }

    public void testIndianBasedLeapYear_PatternValidation() {
        // Test the Indian leap year pattern
        // Leap years: 2, 5, 8, 10, 13, 16, 19, 21, 24, 27, 29
        boolean[] expectedPattern = {
            false, true, false, false, true, false, false, true, false, true,
            false, false, true, false, false, true, false, false, true, false,
            true, false, false, true, false, false, true, false, true, false
        };
        
        for (int year = 1; year <= 30; year++) {
            boolean expected = expectedPattern[year - 1];
            boolean actual = IslamicChronology.LEAP_YEAR_INDIAN.isLeapYear(year);
            assertEquals("Indian pattern year " + year, expected, actual);
        }
    }

    public void testHabashAlHasibBasedLeapYear_PatternValidation() {
        // Test the Habash al-Hasib leap year pattern
        // Leap years: 2, 5, 8, 11, 13, 16, 19, 21, 24, 27, 30
        boolean[] expectedPattern = {
            false, true, false, false, true, false, false, true, false, false,
            true, false, true, false, false, true, false, false, true, false,
            true, false, false, true, false, false, true, false, false, true
        };
        
        for (int year = 1; year <= 30; year++) {
            boolean expected = expectedPattern[year - 1];
            boolean actual = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB.isLeapYear(year);
            assertEquals("Habash al-Hasib pattern year " + year, expected, actual);
        }
    }
}