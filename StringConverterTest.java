/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Test suite for StringConverter functionality.
 * Tests conversion from String to various Joda-Time objects including
 * instants, periods, durations, and intervals.
 *
 * @author Stephen Colebourne
 */
public class TestStringConverter extends TestCase {

    // Test timezone constants
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone OFFSET_ONE_HOUR = DateTimeZone.forOffsetHours(1);
    private static final DateTimeZone OFFSET_SIX_HOURS = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone OFFSET_SEVEN_HOURS = DateTimeZone.forOffsetHours(7);
    private static final DateTimeZone OFFSET_EIGHT_HOURS = DateTimeZone.forOffsetHours(8);
    
    // Test chronology constants
    private static final Chronology ISO_CHRONOLOGY_EIGHT_HOURS = ISOChronology.getInstance(OFFSET_EIGHT_HOURS);
    private static final Chronology ISO_CHRONOLOGY_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_CHRONOLOGY_LONDON = ISOChronology.getInstance(LONDON);
    
    // Test data constants
    private static final String FULL_DATETIME_WITH_TIMEZONE = "2004-06-09T12:24:48.501+08:00";
    private static final String FULL_DATETIME_WITHOUT_TIMEZONE = "2004-06-09T12:24:48.501";
    private static final DateTime EXPECTED_DATETIME_EIGHT_HOURS = new DateTime(2004, 6, 9, 12, 24, 48, 501, OFFSET_EIGHT_HOURS);
    
    private static Chronology isoChronology;
    private static Chronology julianChronology;
    
    // Store original settings for cleanup
    private DateTimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestStringConverter.class);
    }

    public TestStringConverter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        // Store original settings
        originalTimeZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        
        // Set test environment
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
        
        // Initialize chronologies
        julianChronology = JulianChronology.getInstance();
        isoChronology = ISOChronology.getInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore original settings
        DateTimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalTimeZone = null;
    }

    // ========================================================================
    // Singleton Pattern Tests
    // ========================================================================
    
    /**
     * Tests that StringConverter follows the singleton pattern correctly
     * by verifying constructor and field access modifiers.
     */
    public void testSingletonPattern() throws Exception {
        Class<?> converterClass = StringConverter.class;
        
        // Verify class is package-private
        assertFalse("Class should not be public", Modifier.isPublic(converterClass.getModifiers()));
        assertFalse("Class should not be protected", Modifier.isProtected(converterClass.getModifiers()));
        assertFalse("Class should not be private", Modifier.isPrivate(converterClass.getModifiers()));
        
        // Verify single protected constructor
        Constructor<?> constructor = converterClass.getDeclaredConstructor((Class[]) null);
        assertEquals("Should have exactly one constructor", 1, converterClass.getDeclaredConstructors().length);
        assertTrue("Constructor should be protected", Modifier.isProtected(constructor.getModifiers()));
        
        // Verify INSTANCE field is package-private
        Field instanceField = converterClass.getDeclaredField("INSTANCE");
        assertFalse("INSTANCE field should not be public", Modifier.isPublic(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be protected", Modifier.isProtected(instanceField.getModifiers()));
        assertFalse("INSTANCE field should not be private", Modifier.isPrivate(instanceField.getModifiers()));
    }

    // ========================================================================
    // Basic Functionality Tests
    // ========================================================================
    
    /**
     * Tests that the converter reports String.class as its supported type.
     */
    public void testSupportedType() throws Exception {
        assertEquals("Should support String.class", String.class, StringConverter.INSTANCE.getSupportedType());
    }

    /**
     * Tests the toString() method returns expected format.
     */
    public void testToString() {
        assertEquals("Converter[java.lang.String]", StringConverter.INSTANCE.toString());
    }

    // ========================================================================
    // Instant Conversion Tests
    // ========================================================================
    
    /**
     * Tests conversion of various ISO 8601 datetime string formats to milliseconds.
     * Covers full datetime, partial datetime, and different precision levels.
     */
    public void testInstantConversion_VariousFormats() throws Exception {
        // Full datetime with timezone
        DateTime expectedDateTime = EXPECTED_DATETIME_EIGHT_HOURS;
        long actualMillis = StringConverter.INSTANCE.getInstantMillis(FULL_DATETIME_WITH_TIMEZONE, ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Full datetime with timezone", expectedDateTime.getMillis(), actualMillis);
        
        // Year only
        expectedDateTime = new DateTime(2004, 1, 1, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Year only format", expectedDateTime.getMillis(), actualMillis);
        
        // Year-month
        expectedDateTime = new DateTime(2004, 6, 1, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Year-month format", expectedDateTime.getMillis(), actualMillis);
        
        // Year-month-day
        expectedDateTime = new DateTime(2004, 6, 9, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Year-month-day format", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests conversion with ordinal day format (day of year).
     */
    public void testInstantConversion_OrdinalDay() throws Exception {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-161T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Ordinal day format (161st day)", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests conversion with week date format.
     */
    public void testInstantConversion_WeekDate() throws Exception {
        // Week 24, day 3 (Wednesday)
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-W24-3T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Week date format with day", expectedDateTime.getMillis(), actualMillis);
        
        // Week 24 (Monday of that week)
        expectedDateTime = new DateTime(2004, 6, 7, 0, 0, 0, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-W24T+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Week date format without day", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests conversion with various time precision levels.
     */
    public void testInstantConversion_TimePrecision() throws Exception {
        // Hour precision
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 0, 0, 0, OFFSET_EIGHT_HOURS);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Hour precision", expectedDateTime.getMillis(), actualMillis);
        
        // Minute precision
        expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 0, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Minute precision", expectedDateTime.getMillis(), actualMillis);
        
        // Second precision
        expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Second precision", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests conversion with fractional time components.
     */
    public void testInstantConversion_FractionalTime() throws Exception {
        // Fractional hour (12.5 = 12:30)
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 30, 0, 0, OFFSET_EIGHT_HOURS);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12.5+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Fractional hour", expectedDateTime.getMillis(), actualMillis);
        
        // Fractional minute (24.5 = 24:30)
        expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 30, 0, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24.5+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Fractional minute", expectedDateTime.getMillis(), actualMillis);
        
        // Fractional second (48.5 = 48.500)
        expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 500, OFFSET_EIGHT_HOURS);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.5+08:00", ISO_CHRONOLOGY_EIGHT_HOURS);
        assertEquals("Fractional second", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests conversion without timezone information (uses default chronology timezone).
     */
    public void testInstantConversion_NoTimezone() throws Exception {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis(FULL_DATETIME_WITHOUT_TIMEZONE, isoChronology);
        assertEquals("Datetime without timezone", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests instant conversion with different timezone contexts.
     */
    public void testInstantConversion_DifferentTimezones() throws Exception {
        // Paris timezone
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+02:00", ISO_CHRONOLOGY_PARIS);
        assertEquals("Paris timezone with offset", expectedDateTime.getMillis(), actualMillis);
        
        actualMillis = StringConverter.INSTANCE.getInstantMillis(FULL_DATETIME_WITHOUT_TIMEZONE, ISO_CHRONOLOGY_PARIS);
        assertEquals("Paris timezone without offset", expectedDateTime.getMillis(), actualMillis);
        
        // London timezone
        expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);
        actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", ISO_CHRONOLOGY_LONDON);
        assertEquals("London timezone with offset", expectedDateTime.getMillis(), actualMillis);
        
        actualMillis = StringConverter.INSTANCE.getInstantMillis(FULL_DATETIME_WITHOUT_TIMEZONE, ISO_CHRONOLOGY_LONDON);
        assertEquals("London timezone without offset", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests instant conversion with Julian chronology.
     */
    public void testInstantConversion_JulianChronology() throws Exception {
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, JulianChronology.getInstance(LONDON));
        long actualMillis = StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", julianChronology);
        assertEquals("Julian chronology", expectedDateTime.getMillis(), actualMillis);
    }

    /**
     * Tests that invalid datetime strings throw appropriate exceptions.
     */
    public void testInstantConversion_InvalidStrings() {
        assertInstantConversionThrowsException("", "Empty string should throw exception");
        assertInstantConversionThrowsException("X", "Invalid format should throw exception");
    }

    private void assertInstantConversionThrowsException(String input, String message) {
        try {
            StringConverter.INSTANCE.getInstantMillis(input, (Chronology) null);
            fail(message);
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }

    // ========================================================================
    // Chronology Tests
    // ========================================================================
    
    /**
     * Tests chronology extraction from datetime strings with timezone context.
     */
    public void testChronologyExtraction_WithTimezone() throws Exception {
        assertEquals("Paris timezone with offset", 
                ISOChronology.getInstance(PARIS), 
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", PARIS));
        
        assertEquals("Paris timezone without offset", 
                ISOChronology.getInstance(PARIS), 
                StringConverter.INSTANCE.getChronology(FULL_DATETIME_WITHOUT_TIMEZONE, PARIS));
        
        assertEquals("Null timezone with offset defaults to London", 
                ISOChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", (DateTimeZone) null));
        
        assertEquals("Null timezone without offset defaults to London", 
                ISOChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology(FULL_DATETIME_WITHOUT_TIMEZONE, (DateTimeZone) null));
    }

    /**
     * Tests chronology extraction with explicit chronology parameter.
     */
    public void testChronologyExtraction_WithChronology() throws Exception {
        assertEquals("Julian chronology with offset", 
                JulianChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", julianChronology));
        
        assertEquals("Julian chronology without offset", 
                JulianChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology(FULL_DATETIME_WITHOUT_TIMEZONE, julianChronology));
        
        assertEquals("Null chronology with offset defaults to ISO London", 
                ISOChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", (Chronology) null));
        
        assertEquals("Null chronology without offset defaults to ISO London", 
                ISOChronology.getInstance(LONDON), 
                StringConverter.INSTANCE.getChronology(FULL_DATETIME_WITHOUT_TIMEZONE, (Chronology) null));
    }

    // ========================================================================
    // Partial Values Tests
    // ========================================================================
    
    /**
     * Tests extraction of partial time values from time string.
     */
    public void testPartialValuesExtraction() throws Exception {
        TimeOfDay timeOfDay = new TimeOfDay();
        int[] expectedValues = new int[] {3, 4, 5, 6};
        int[] actualValues = StringConverter.INSTANCE.getPartialValues(timeOfDay, "T03:04:05.006", ISOChronology.getInstance());
        assertTrue("Should extract time components correctly", Arrays.equals(expectedValues, actualValues));
    }

    // ========================================================================
    // DateTime Construction Tests
    // ========================================================================
    
    /**
     * Tests DateTime construction from string representation.
     */
    public void testDateTimeConstruction_FromString() throws Exception {
        DateTime baseDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        DateTime constructedDateTime = new DateTime(baseDateTime.toString(), PARIS);
        assertEquals("DateTime from string should match original", baseDateTime, constructedDateTime);
    }

    /**
     * Tests DateTime construction with timezone offset in string.
     */
    public void testDateTimeConstruction_WithTimezoneOffset() throws Exception {
        DateTime dateTime = new DateTime("2004-06-09T12:24:48.501+01:00");
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    /**
     * Tests DateTime construction without timezone (uses default).
     */
    public void testDateTimeConstruction_WithoutTimezone() throws Exception {
        DateTime dateTime = new DateTime(FULL_DATETIME_WITHOUT_TIMEZONE);
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    /**
     * Tests DateTime construction with explicit timezone parameter.
     */
    public void testDateTimeConstruction_WithExplicitTimezone() throws Exception {
        DateTime dateTime = new DateTime("2004-06-09T12:24:48.501+02:00", PARIS);
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, PARIS);
        
        dateTime = new DateTime(FULL_DATETIME_WITHOUT_TIMEZONE, PARIS);
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    /**
     * Tests DateTime construction with Julian chronology.
     */
    public void testDateTimeConstruction_WithJulianChronology() throws Exception {
        DateTime dateTime = new DateTime("2004-06-09T12:24:48.501+02:00", JulianChronology.getInstance(PARIS));
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, PARIS);
        
        dateTime = new DateTime(FULL_DATETIME_WITHOUT_TIMEZONE, JulianChronology.getInstance(PARIS));
        assertDateTimeComponents(dateTime, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    private void assertDateTimeComponents(DateTime dateTime, int year, int month, int day, 
                                        int hour, int minute, int second, int millis, DateTimeZone expectedZone) {
        assertEquals("Year", year, dateTime.getYear());
        assertEquals("Month", month, dateTime.getMonthOfYear());
        assertEquals("Day", day, dateTime.getDayOfMonth());
        assertEquals("Hour", hour, dateTime.getHourOfDay());
        assertEquals("Minute", minute, dateTime.getMinuteOfHour());
        assertEquals("Second", second, dateTime.getSecondOfMinute());
        assertEquals("Millisecond", millis, dateTime.getMillisOfSecond());
        assertEquals("Timezone", expectedZone, dateTime.getZone());
    }

    // ========================================================================
    // Duration Tests
    // ========================================================================
    
    /**
     * Tests conversion of ISO 8601 duration strings to milliseconds.
     */
    public void testDurationConversion_ValidFormats() throws Exception {
        assertDurationConversion("PT12.345S", 12345, "Standard format with fractional seconds");
        assertDurationConversion("pt12.345s", 12345, "Lowercase format");
        assertDurationConversion("pt12s", 12000, "Whole seconds only");
        assertDurationConversion("pt12.s", 12000, "Trailing decimal point");
        assertDurationConversion("pt-12.32s", -12320, "Negative duration");
        assertDurationConversion("pt-0.32s", -320, "Negative fractional second");
        assertDurationConversion("pt-0.0s", 0, "Negative zero");
        assertDurationConversion("pt0.0s", 0, "Positive zero");
        assertDurationConversion("pt12.3456s", 12345, "High precision (truncated to millis)");
    }

    private void assertDurationConversion(String input, long expectedMillis, String description) throws Exception {
        long actualMillis = StringConverter.INSTANCE.getDurationMillis(input);
        assertEquals(description, expectedMillis, actualMillis);
    }

    /**
     * Tests that invalid duration strings throw appropriate exceptions.
     */
    public void testDurationConversion_InvalidFormats() throws Exception {
        String[] invalidDurations = {
            "P2Y6M9DXYZ",    // Invalid characters
            "PTS",           // Missing value
            "XT0S",          // Invalid prefix
            "PX0S",          // Invalid period designator
            "PT0X",          // Invalid time designator
            "PTXS",          // Invalid seconds value
            "PT0.0.0S",      // Multiple decimal points
            "PT0-00S",       // Invalid minus placement
            "PT-.001S"       // Invalid decimal format
        };

        for (String invalidDuration : invalidDurations) {
            assertDurationConversionThrowsException(invalidDuration);
        }
    }

    private void assertDurationConversionThrowsException(String input) {
        try {
            StringConverter.INSTANCE.getDurationMillis(input);
            fail("Should throw exception for invalid duration: " + input);
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }

    // ========================================================================
    // Period Tests
    // ========================================================================
    
    /**
     * Tests that period type extraction returns standard period type.
     */
    public void testPeriodTypeExtraction() throws Exception {
        assertEquals("Should return standard period type", 
                PeriodType.standard(), 
                StringConverter.INSTANCE.getPeriodType("P2Y6M9D"));
    }

    /**
     * Tests setting period values from ISO 8601 period strings.
     */
    public void testPeriodSetting_YearMonthDayTime() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearMonthDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y6M9DT12H24M48S", null);
        
        assertPeriodComponents(period, 2, 6, 0, 9, 12, 24, 48, 0);
    }

    /**
     * Tests setting period values with week-based format.
     */
    public void testPeriodSetting_YearWeekDayTime() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M48S", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 12, 24, 48, 0);
    }

    /**
     * Tests setting period values with fractional seconds.
     */
    public void testPeriodSetting_WithFractionalSeconds() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M48.034S", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 12, 24, 48, 34);
    }

    /**
     * Tests setting period values with fractional minutes.
     */
    public void testPeriodSetting_WithFractionalMinutes() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M.056S", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 12, 24, 0, 56);
    }

    /**
     * Tests setting period values with trailing decimal point.
     */
    public void testPeriodSetting_WithTrailingDecimal() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M56.S", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 12, 24, 56, 0);
    }

    /**
     * Tests setting period values with high precision fractional seconds (truncated).
     */
    public void testPeriodSetting_WithHighPrecisionFraction() throws Exception {
        MutablePeriod period = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3DT12H24M56.1234567S", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 12, 24, 56, 123);
    }

    /**
     * Tests that date-only periods clear time components.
     */
    public void testPeriodSetting_DateOnlyPeriod() throws Exception {
        MutablePeriod period = new MutablePeriod(1, 0, 1, 1, 1, 1, 1, 1, PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(period, "P2Y4W3D", null);
        
        assertPeriodComponents(period, 2, 0, 4, 3, 0, 0, 0, 0);
    }

    private void assertPeriodComponents(MutablePeriod period, int years, int months, int weeks, int days,
                                      int hours, int minutes, int seconds, int millis) {
        assertEquals("Years", years, period.getYears());
        assertEquals("Months", months, period.getMonths());
        assertEquals("Weeks", weeks, period.getWeeks());
        assertEquals("Days", days, period.getDays());
        assertEquals("Hours", hours, period.getHours());
        assertEquals("Minutes", minutes, period.getMinutes());
        assertEquals("Seconds", seconds, period.getSeconds());
        assertEquals("Milliseconds", millis, period.getMillis());
    }

    /**
     * Tests that invalid period strings throw appropriate exceptions.
     */
    public void testPeriodSetting_InvalidFormats() throws Exception {
        MutablePeriod period = new MutablePeriod();
        String[] invalidPeriods = {
            "",                      // Empty string
            "PXY",                   // Invalid year designator
            "PT0SXY",               // Invalid suffix
            "P2Y4W3DT12H24M48SX",   // Invalid trailing characters
        };

        for (String invalidPeriod : invalidPeriods) {
            assertPeriodSettingThrowsException(period, invalidPeriod);
        }
    }

    private void assertPeriodSettingThrowsException(MutablePeriod period, String input) {
        try {
            StringConverter.INSTANCE.setInto(period, input, null);
            fail("Should throw exception for invalid period: " + input);
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }

    // ========================================================================
    // Interval Tests
    // ========================================================================
    
    /**
     * Tests that string intervals are not considered readable intervals.
     */
    public void testIntervalReadability() throws Exception {
        assertFalse("String should not be readable interval", 
                StringConverter.INSTANCE.isReadableInterval("", null));
    }

    /**
     * Tests interval parsing with start datetime and period.
     */
    public void testIntervalSetting_StartDateTimePlusPeriod() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2004-06-09/P1Y2M", null);
        
        DateTime expectedStart = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        DateTime expectedEnd = new DateTime(2005, 8, 9, 0, 0, 0, 0);
        assertIntervalComponents(interval, expectedStart, expectedEnd, ISOChronology.getInstance());
    }

    /**
     * Tests interval parsing with period and end datetime.
     */
    public void testIntervalSetting_PeriodPlusEndDateTime() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "P1Y2M/2004-06-09", null);
        
        DateTime expectedStart = new DateTime(2003, 4, 9, 0, 0, 0, 0);
        DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        assertIntervalComponents(interval, expectedStart, expectedEnd, ISOChronology.getInstance());
    }

    /**
     * Tests interval parsing with start and end datetimes.
     */
    public void testIntervalSetting_StartAndEndDateTime() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2003-08-09/2004-06-09", null);
        
        DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0);
        DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0);
        assertIntervalComponents(interval, expectedStart, expectedEnd, ISOChronology.getInstance());
    }

    /**
     * Tests interval parsing with timezone offsets.
     */
    public void testIntervalSetting_WithTimezoneOffsets() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2004-06-09T+06:00/P1Y2M", null);
        
        DateTime expectedStart = new DateTime(2004, 6, 9, 0, 0, 0, 0, OFFSET_SIX_HOURS).withChronology(null);
        DateTime expectedEnd = new DateTime(2005, 8, 9, 0, 0, 0, 0, OFFSET_SIX_HOURS).withChronology(null);
        assertIntervalComponents(interval, expectedStart, expectedEnd, ISOChronology.getInstance());
    }

    /**
     * Tests interval parsing with different timezone offsets for start and end.
     */
    public void testIntervalSetting_WithDifferentTimezoneOffsets() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2003-08-09T+06:00/2004-06-09T+07:00", null);
        
        DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0, OFFSET_SIX_HOURS).withChronology(null);
        DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0, OFFSET_SEVEN_HOURS).withChronology(null);
        assertIntervalComponents(interval, expectedStart, expectedEnd, ISOChronology.getInstance());
    }

    /**
     * Tests interval parsing with Buddhist chronology.
     */
    public void testIntervalSetting_WithBuddhistChronology() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2003-08-09/2004-06-09", BuddhistChronology.getInstance());
        
        DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance());
        DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance());
        assertIntervalComponents(interval, expectedStart, expectedEnd, BuddhistChronology.getInstance());
    }

    /**
     * Tests interval parsing with Buddhist chronology and timezone conversion.
     */
    public void testIntervalSetting_WithBuddhistChronologyAndTimezoneConversion() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(interval, "2003-08-09T+06:00/2004-06-09T+07:00", 
                BuddhistChronology.getInstance(OFFSET_EIGHT_HOURS));
        
        DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(OFFSET_SIX_HOURS))
                .withZone(OFFSET_EIGHT_HOURS);
        DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(OFFSET_SEVEN_HOURS))
                .withZone(OFFSET_EIGHT_HOURS);
        assertIntervalComponents(interval, expectedStart, expectedEnd, BuddhistChronology.getInstance(OFFSET_EIGHT_HOURS));
    }

    private void assertIntervalComponents(MutableInterval interval, DateTime expectedStart, 
                                        DateTime expectedEnd, Chronology expectedChronology) {
        assertEquals("Interval start", expectedStart, interval.getStart());
        assertEquals("Interval end", expectedEnd, interval.getEnd());
        assertEquals("Interval chronology", expectedChronology, interval.getChronology());
    }

    /**
     * Tests that invalid interval strings throw appropriate exceptions.
     */
    public void testIntervalSetting_InvalidFormats() throws Exception {
        MutableInterval interval = new MutableInterval(-1000L, 1000L);
        String[] invalidIntervals = {
            "",           // Empty string
            "/",          // Just separator
            "P1Y/",       // Missing end
            "/P1Y",       // Missing start
            "P1Y/P2Y",    // Two periods
        };

        for (String invalidInterval : invalidIntervals) {
            assertIntervalSettingThrowsException(interval, invalidInterval);
        }
    }

    private void assertIntervalSettingThrowsException(MutableInterval interval, String input) {
        try {
            StringConverter.INSTANCE.setInto(interval, input, null);
            fail("Should throw exception for invalid interval: " + input);
        } catch (IllegalArgumentException expected) {
            // Expected exception
        }
    }
}