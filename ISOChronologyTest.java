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
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * Comprehensive test suite for ISOChronology functionality.
 * Tests factory methods, field operations, date arithmetic, and edge cases.
 *
 * @author Stephen Colebourne
 */
@SuppressWarnings("deprecation")
public class TestISOChronology extends TestCase {

    // Test time zones
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fixed test time: 2002-06-09 (calculated from days since epoch)
    private static final long DAYS_TO_2002 = calculateDaysTo2002();
    private static final long TEST_TIME_2002_06_09 = 
            (DAYS_TO_2002 + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    // Store original system settings for restoration
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISOChronology.class);
    }

    public TestISOChronology(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        setupFixedTestTime();
        saveOriginalSystemSettings();
        configureTestEnvironment();
    }

    @Override
    protected void tearDown() throws Exception {
        restoreSystemTime();
        restoreOriginalSystemSettings();
        clearStoredSettings();
    }

    //-----------------------------------------------------------------------
    // Factory Method Tests
    //-----------------------------------------------------------------------

    public void testFactoryUTC_CreatesUTCInstance() {
        ISOChronology chronology = ISOChronology.getInstanceUTC();
        
        assertEquals("Should use UTC timezone", DateTimeZone.UTC, chronology.getZone());
        assertSame("Should return ISOChronology instance", ISOChronology.class, chronology.getClass());
    }

    public void testFactory_CreatesDefaultTimezoneInstance() {
        ISOChronology chronology = ISOChronology.getInstance();
        
        assertEquals("Should use default timezone (London)", LONDON, chronology.getZone());
        assertSame("Should return ISOChronology instance", ISOChronology.class, chronology.getClass());
    }

    public void testFactory_WithSpecificTimezone() {
        assertEquals("Should create Tokyo instance", TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals("Should create Paris instance", PARIS, ISOChronology.getInstance(PARIS).getZone());
        assertEquals("Should default to London for null", LONDON, ISOChronology.getInstance(null).getZone());
        assertSame("Should return ISOChronology instance", ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    // Instance Management Tests
    //-----------------------------------------------------------------------

    public void testEquality_SameTimezoneSameInstance() {
        assertSame("Tokyo instances should be same", 
                   ISOChronology.getInstance(TOKYO), ISOChronology.getInstance(TOKYO));
        assertSame("London instances should be same", 
                   ISOChronology.getInstance(LONDON), ISOChronology.getInstance(LONDON));
        assertSame("Paris instances should be same", 
                   ISOChronology.getInstance(PARIS), ISOChronology.getInstance(PARIS));
        assertSame("UTC instances should be same", 
                   ISOChronology.getInstanceUTC(), ISOChronology.getInstanceUTC());
        assertSame("Default should equal London", 
                   ISOChronology.getInstance(), ISOChronology.getInstance(LONDON));
    }

    public void testWithUTC_ConvertsToUTC() {
        ISOChronology utcExpected = ISOChronology.getInstanceUTC();
        
        assertSame("London to UTC", utcExpected, ISOChronology.getInstance(LONDON).withUTC());
        assertSame("Tokyo to UTC", utcExpected, ISOChronology.getInstance(TOKYO).withUTC());
        assertSame("UTC to UTC", utcExpected, ISOChronology.getInstanceUTC().withUTC());
        assertSame("Default to UTC", utcExpected, ISOChronology.getInstance().withUTC());
    }

    public void testWithZone_ConvertsToSpecifiedTimezone() {
        ISOChronology tokyoChronology = ISOChronology.getInstance(TOKYO);
        
        assertSame("Same timezone returns same instance", 
                   ISOChronology.getInstance(TOKYO), tokyoChronology.withZone(TOKYO));
        assertSame("Convert to London", 
                   ISOChronology.getInstance(LONDON), tokyoChronology.withZone(LONDON));
        assertSame("Convert to Paris", 
                   ISOChronology.getInstance(PARIS), tokyoChronology.withZone(PARIS));
        assertSame("Null defaults to London", 
                   ISOChronology.getInstance(LONDON), tokyoChronology.withZone(null));
        assertSame("Default to Paris", 
                   ISOChronology.getInstance(PARIS), ISOChronology.getInstance().withZone(PARIS));
        assertSame("UTC to Paris", 
                   ISOChronology.getInstance(PARIS), ISOChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString_ShowsTimezoneInformation() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Duration Field Tests
    //-----------------------------------------------------------------------

    public void testDurationFields_NamesAndSupport() {
        final ISOChronology iso = ISOChronology.getInstance();
        
        // Verify field names
        assertEquals("eras", iso.eras().getName());
        assertEquals("centuries", iso.centuries().getName());
        assertEquals("years", iso.years().getName());
        assertEquals("weekyears", iso.weekyears().getName());
        assertEquals("months", iso.months().getName());
        assertEquals("weeks", iso.weeks().getName());
        assertEquals("days", iso.days().getName());
        assertEquals("halfdays", iso.halfdays().getName());
        assertEquals("hours", iso.hours().getName());
        assertEquals("minutes", iso.minutes().getName());
        assertEquals("seconds", iso.seconds().getName());
        assertEquals("millis", iso.millis().getName());
        
        // Verify support status
        assertFieldSupport(iso);
    }

    public void testDurationFields_PrecisionInDifferentTimezones() {
        final ISOChronology londonChronology = ISOChronology.getInstance();
        final ISOChronology utcChronology = ISOChronology.getInstanceUTC();
        final ISOChronology gmtChronology = ISOChronology.getInstance(DateTimeZone.forID("Etc/GMT"));
        final ISOChronology offsetChronology = ISOChronology.getInstance(DateTimeZone.forOffsetHours(1));
        
        verifyPrecisionForTimezoneWithDST(londonChronology, "London (with DST)");
        verifyPrecisionForFixedTimezone(utcChronology, "UTC");
        verifyPrecisionForFixedTimezone(gmtChronology, "GMT");
        verifyPrecisionForFixedTimezone(offsetChronology, "Fixed offset");
    }

    //-----------------------------------------------------------------------
    // Date Field Tests
    //-----------------------------------------------------------------------

    public void testDateFields_NamesAndSupport() {
        final ISOChronology iso = ISOChronology.getInstance();
        
        verifyDateFieldNames(iso);
        verifyDateFieldSupport(iso);
        verifyDateFieldDurations(iso);
        verifyDateFieldRanges(iso);
    }

    public void testTimeFields_NamesAndSupport() {
        final ISOChronology iso = ISOChronology.getInstance();
        
        verifyTimeFieldNames(iso);
        verifyTimeFieldSupport(iso);
    }

    //-----------------------------------------------------------------------
    // Year Boundary Tests
    //-----------------------------------------------------------------------

    public void testMaxYear_BoundaryConditions() {
        final ISOChronology chronology = ISOChronology.getInstanceUTC();
        final int maxYear = chronology.year().getMaximumValue();

        DateTime startOfMaxYear = new DateTime(maxYear, 1, 1, 0, 0, 0, 0, chronology);
        DateTime endOfMaxYear = new DateTime(maxYear, 12, 31, 23, 59, 59, 999, chronology);
        
        verifyMaxYearBoundaries(startOfMaxYear, endOfMaxYear, maxYear, chronology);
        verifyMaxYearStringParsing(startOfMaxYear, endOfMaxYear, maxYear, chronology);
        verifyMaxYearArithmetic(startOfMaxYear, endOfMaxYear);
        verifyMaxYearEdgeCases(chronology, maxYear);
    }

    public void testMinYear_BoundaryConditions() {
        final ISOChronology chronology = ISOChronology.getInstanceUTC();
        final int minYear = chronology.year().getMinimumValue();

        DateTime startOfMinYear = new DateTime(minYear, 1, 1, 0, 0, 0, 0, chronology);
        DateTime endOfMinYear = new DateTime(minYear, 12, 31, 23, 59, 59, 999, chronology);
        
        verifyMinYearBoundaries(startOfMinYear, endOfMinYear, minYear, chronology);
        verifyMinYearStringParsing(startOfMinYear, endOfMinYear, minYear, chronology);
        verifyMinYearArithmetic(startOfMinYear, endOfMinYear);
        verifyMinYearEdgeCases(chronology, minYear);
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------

    public void testCutoverAddYears_GregorianTransition() {
        // Test year addition around the Gregorian calendar cutover (1582)
        verifyYearAddition("1582-01-01", 1, "1583-01-01");
        verifyYearAddition("1582-02-15", 1, "1583-02-15");
        verifyYearAddition("1582-02-28", 1, "1583-02-28");
        verifyYearAddition("1582-03-01", 1, "1583-03-01");
        verifyYearAddition("1582-09-30", 1, "1583-09-30");
        verifyYearAddition("1582-10-01", 1, "1583-10-01");
        verifyYearAddition("1582-10-04", 1, "1583-10-04");
        verifyYearAddition("1582-10-15", 1, "1583-10-15");
        verifyYearAddition("1582-10-16", 1, "1583-10-16");
        
        // Test leap year handling across cutover
        verifyYearAddition("1580-01-01", 4, "1584-01-01");
        verifyYearAddition("1580-02-29", 4, "1584-02-29");
        verifyYearAddition("1580-10-01", 4, "1584-10-01");
        verifyYearAddition("1580-10-10", 4, "1584-10-10");
        verifyYearAddition("1580-10-15", 4, "1584-10-15");
        verifyYearAddition("1580-12-31", 4, "1584-12-31");
    }

    public void testAddMonths_VariousScenarios() {
        // Basic month addition
        verifyMonthAddition("1582-01-01", 1, "1582-02-01");
        verifyMonthAddition("1582-01-01", 6, "1582-07-01");
        verifyMonthAddition("1582-01-01", 12, "1583-01-01");
        
        // Month addition around cutover period
        verifyMonthAddition("1582-11-15", 1, "1582-12-15");
        verifyMonthAddition("1582-09-04", 2, "1582-11-04");
        verifyMonthAddition("1582-09-05", 2, "1582-11-05");
        verifyMonthAddition("1582-09-10", 2, "1582-11-10");
        verifyMonthAddition("1582-09-15", 2, "1582-11-15");
        
        // Long-term month addition with leap years
        verifyMonthAddition("1580-01-01", 48, "1584-01-01");
        verifyMonthAddition("1580-02-29", 48, "1584-02-29");
        verifyMonthAddition("1580-10-01", 48, "1584-10-01");
        verifyMonthAddition("1580-10-10", 48, "1584-10-10");
        verifyMonthAddition("1580-10-15", 48, "1584-10-15");
        verifyMonthAddition("1580-12-31", 48, "1584-12-31");
    }

    //-----------------------------------------------------------------------
    // Time Arithmetic Tests
    //-----------------------------------------------------------------------

    public void testTimeOfDayAdd_HourAndMinuteArithmetic() {
        TimeOfDay startTime = new TimeOfDay(12, 30);
        TimeOfDay expectedEndTime = new TimeOfDay(10, 30);
        
        assertEquals("Adding 22 hours should wrap to next day", 
                     expectedEndTime, startTime.plusHours(22));
        assertEquals("Subtracting 22 hours should wrap to previous day", 
                     startTime, expectedEndTime.minusHours(22));
        assertEquals("Adding 22*60 minutes should equal adding 22 hours", 
                     expectedEndTime, startTime.plusMinutes(22 * 60));
        assertEquals("Subtracting 22*60 minutes should equal subtracting 22 hours", 
                     startTime, expectedEndTime.minusMinutes(22 * 60));
    }

    public void testPartialDayOfYearAdd_LeapYearHandling() {
        Partial startPartial = new Partial()
                .with(DateTimeFieldType.year(), 2000)
                .with(DateTimeFieldType.dayOfYear(), 366);
        Partial endPartial = new Partial()
                .with(DateTimeFieldType.year(), 2004)
                .with(DateTimeFieldType.dayOfYear(), 366);
        
        int daysToAdd = 365 + 365 + 365 + 366; // 2001, 2002, 2003, 2004 (leap year)
        
        assertEquals("Should handle leap years correctly in day-of-year addition", 
                     endPartial, startPartial.withFieldAdded(DurationFieldType.days(), daysToAdd));
        assertEquals("Should handle leap years correctly in day-of-year subtraction", 
                     startPartial, endPartial.withFieldAdded(DurationFieldType.days(), -daysToAdd));
    }

    //-----------------------------------------------------------------------
    // Field Value Tests
    //-----------------------------------------------------------------------

    public void testMaximumValue_ConsistencyAcrossTypes() {
        DateMidnight currentDate = new DateMidnight(1570, 1, 1);
        
        // Test consistency between DateTime and YearMonthDay maximum values
        while (currentDate.getYear() < 1590) {
            currentDate = currentDate.plusDays(1);
            YearMonthDay ymd = currentDate.toYearMonthDay();
            
            assertEquals("Year maximum should be consistent", 
                         currentDate.year().getMaximumValue(), ymd.year().getMaximumValue());
            assertEquals("Month maximum should be consistent", 
                         currentDate.monthOfYear().getMaximumValue(), ymd.monthOfYear().getMaximumValue());
            assertEquals("Day maximum should be consistent", 
                         currentDate.dayOfMonth().getMaximumValue(), ymd.dayOfMonth().getMaximumValue());
        }
    }

    public void testLeap_February28InLeapYear() {
        Chronology chronology = ISOChronology.getInstance();
        DateTime feb28LeapYear = new DateTime(2012, 2, 28, 0, 0, chronology);
        
        assertEquals("2012 should be a leap year", true, feb28LeapYear.year().isLeap());
        assertEquals("February should be leap in leap year", true, feb28LeapYear.monthOfYear().isLeap());
        assertEquals("28th is not the leap day", false, feb28LeapYear.dayOfMonth().isLeap());
        assertEquals("28th is not the leap day of year", false, feb28LeapYear.dayOfYear().isLeap());
    }

    public void testLeap_February29InLeapYear() {
        Chronology chronology = ISOChronology.getInstance();
        DateTime feb29LeapYear = new DateTime(2012, 2, 29, 0, 0, chronology);
        
        assertEquals("2012 should be a leap year", true, feb29LeapYear.year().isLeap());
        assertEquals("February should be leap in leap year", true, feb29LeapYear.monthOfYear().isLeap());
        assertEquals("29th is the leap day of month", true, feb29LeapYear.dayOfMonth().isLeap());
        assertEquals("29th is the leap day of year", true, feb29LeapYear.dayOfYear().isLeap());
    }

    //-----------------------------------------------------------------------
    // Helper Methods
    //-----------------------------------------------------------------------

    private static long calculateDaysTo2002() {
        // Calculate days from epoch to 2002: 32 years with leap years
        return 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
               366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
               365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
               366 + 365;
    }

    private void setupFixedTestTime() {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_2002_06_09);
    }

    private void saveOriginalSystemSettings() {
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
    }

    private void configureTestEnvironment() {
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    private void restoreSystemTime() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    private void restoreOriginalSystemSettings() {
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    private void clearStoredSettings() {
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    private void assertFieldSupport(ISOChronology iso) {
        assertEquals("Eras should not be supported", false, iso.eras().isSupported());
        assertEquals("Centuries should be supported", true, iso.centuries().isSupported());
        assertEquals("Years should be supported", true, iso.years().isSupported());
        assertEquals("Weekyears should be supported", true, iso.weekyears().isSupported());
        assertEquals("Months should be supported", true, iso.months().isSupported());
        assertEquals("Weeks should be supported", true, iso.weeks().isSupported());
        assertEquals("Days should be supported", true, iso.days().isSupported());
        assertEquals("Halfdays should be supported", true, iso.halfdays().isSupported());
        assertEquals("Hours should be supported", true, iso.hours().isSupported());
        assertEquals("Minutes should be supported", true, iso.minutes().isSupported());
        assertEquals("Seconds should be supported", true, iso.seconds().isSupported());
        assertEquals("Millis should be supported", true, iso.millis().isSupported());
    }

    private void verifyPrecisionForTimezoneWithDST(ISOChronology chronology, String description) {
        assertEquals(description + " - centuries not precise", false, chronology.centuries().isPrecise());
        assertEquals(description + " - years not precise", false, chronology.years().isPrecise());
        assertEquals(description + " - weekyears not precise", false, chronology.weekyears().isPrecise());
        assertEquals(description + " - months not precise", false, chronology.months().isPrecise());
        assertEquals(description + " - weeks not precise", false, chronology.weeks().isPrecise());
        assertEquals(description + " - days not precise", false, chronology.days().isPrecise());
        assertEquals(description + " - halfdays not precise", false, chronology.halfdays().isPrecise());
        assertEquals(description + " - hours precise", true, chronology.hours().isPrecise());
        assertEquals(description + " - minutes precise", true, chronology.minutes().isPrecise());
        assertEquals(description + " - seconds precise", true, chronology.seconds().isPrecise());
        assertEquals(description + " - millis precise", true, chronology.millis().isPrecise());
    }

    private void verifyPrecisionForFixedTimezone(ISOChronology chronology, String description) {
        assertEquals(description + " - centuries not precise", false, chronology.centuries().isPrecise());
        assertEquals(description + " - years not precise", false, chronology.years().isPrecise());
        assertEquals(description + " - weekyears not precise", false, chronology.weekyears().isPrecise());
        assertEquals(description + " - months not precise", false, chronology.months().isPrecise());
        assertEquals(description + " - weeks precise", true, chronology.weeks().isPrecise());
        assertEquals(description + " - days precise", true, chronology.days().isPrecise());
        assertEquals(description + " - halfdays precise", true, chronology.halfdays().isPrecise());
        assertEquals(description + " - hours precise", true, chronology.hours().isPrecise());
        assertEquals(description + " - minutes precise", true, chronology.minutes().isPrecise());
        assertEquals(description + " - seconds precise", true, chronology.seconds().isPrecise());
        assertEquals(description + " - millis precise", true, chronology.millis().isPrecise());
    }

    private void verifyDateFieldNames(ISOChronology iso) {
        assertEquals("era", iso.era().getName());
        assertEquals("centuryOfEra", iso.centuryOfEra().getName());
        assertEquals("yearOfCentury", iso.yearOfCentury().getName());
        assertEquals("yearOfEra", iso.yearOfEra().getName());
        assertEquals("year", iso.year().getName());
        assertEquals("monthOfYear", iso.monthOfYear().getName());
        assertEquals("weekyearOfCentury", iso.weekyearOfCentury().getName());
        assertEquals("weekyear", iso.weekyear().getName());
        assertEquals("weekOfWeekyear", iso.weekOfWeekyear().getName());
        assertEquals("dayOfYear", iso.dayOfYear().getName());
        assertEquals("dayOfMonth", iso.dayOfMonth().getName());
        assertEquals("dayOfWeek", iso.dayOfWeek().getName());
    }

    private void verifyDateFieldSupport(ISOChronology iso) {
        assertEquals(true, iso.era().isSupported());
        assertEquals(true, iso.centuryOfEra().isSupported());
        assertEquals(true, iso.yearOfCentury().isSupported());
        assertEquals(true, iso.yearOfEra().isSupported());
        assertEquals(true, iso.year().isSupported());
        assertEquals(true, iso.monthOfYear().isSupported());
        assertEquals(true, iso.weekyearOfCentury().isSupported());
        assertEquals(true, iso.weekyear().isSupported());
        assertEquals(true, iso.weekOfWeekyear().isSupported());
        assertEquals(true, iso.dayOfYear().isSupported());
        assertEquals(true, iso.dayOfMonth().isSupported());
        assertEquals(true, iso.dayOfWeek().isSupported());
    }

    private void verifyDateFieldDurations(ISOChronology iso) {
        assertEquals(iso.eras(), iso.era().getDurationField());
        assertEquals(iso.centuries(), iso.centuryOfEra().getDurationField());
        assertEquals(iso.years(), iso.yearOfCentury().getDurationField());
        assertEquals(iso.years(), iso.yearOfEra().getDurationField());
        assertEquals(iso.years(), iso.year().getDurationField());
        assertEquals(iso.months(), iso.monthOfYear().getDurationField());
        assertEquals(iso.weekyears(), iso.weekyearOfCentury().getDurationField());
        assertEquals(iso.weekyears(), iso.weekyear().getDurationField());
        assertEquals(iso.weeks(), iso.weekOfWeekyear().getDurationField());
        assertEquals(iso.days(), iso.dayOfYear().getDurationField());
        assertEquals(iso.days(), iso.dayOfMonth().getDurationField());
        assertEquals(iso.days(), iso.dayOfWeek().getDurationField());
    }

    private void verifyDateFieldRanges(ISOChronology iso) {
        assertEquals(null, iso.era().getRangeDurationField());
        assertEquals(iso.eras(), iso.centuryOfEra().getRangeDurationField());
        assertEquals(iso.centuries(), iso.yearOfCentury().getRangeDurationField());
        assertEquals(iso.eras(), iso.yearOfEra().getRangeDurationField());
        assertEquals(null, iso.year().getRangeDurationField());
        assertEquals(iso.years(), iso.monthOfYear().getRangeDurationField());
        assertEquals(iso.centuries(), iso.weekyearOfCentury().getRangeDurationField());
        assertEquals(null, iso.weekyear().getRangeDurationField());
        assertEquals(iso.weekyears(), iso.weekOfWeekyear().getRangeDurationField());
        assertEquals(iso.years(), iso.dayOfYear().getRangeDurationField());
        assertEquals(iso.months(), iso.dayOfMonth().getRangeDurationField());
        assertEquals(iso.weeks(), iso.dayOfWeek().getRangeDurationField());
    }

    private void verifyTimeFieldNames(ISOChronology iso) {
        assertEquals("halfdayOfDay", iso.halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", iso.clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", iso.hourOfHalfday().getName());
        assertEquals("clockhourOfDay", iso.clockhourOfDay().getName());
        assertEquals("hourOfDay", iso.hourOfDay().getName());
        assertEquals("minuteOfDay", iso.minuteOfDay().getName());
        assertEquals("minuteOfHour", iso.minuteOfHour().getName());
        assertEquals("secondOfDay", iso.secondOfDay().getName());
        assertEquals("secondOfMinute", iso.secondOfMinute().getName());
        assertEquals("millisOfDay", iso.millisOfDay().getName());
        assertEquals("millisOfSecond", iso.millisOfSecond().getName());
    }

    private void verifyTimeFieldSupport(ISOChronology iso) {
        assertEquals(true, iso.halfdayOfDay().isSupported());
        assertEquals(true, iso.clockhourOfHalfday().isSupported());
        assertEquals(true, iso.hourOfHalfday().isSupported());
        assertEquals(true, iso.clockhourOfDay().isSupported());
        assertEquals(true, iso.hourOfDay().isSupported());
        assertEquals(true, iso.minuteOfDay().isSupported());
        assertEquals(true, iso.minuteOfHour().isSupported());
        assertEquals(true, iso.secondOfDay().isSupported());
        assertEquals(true, iso.secondOfMinute().isSupported());
        assertEquals(true, iso.millisOfDay().isSupported());
        assertEquals(true, iso.millisOfSecond().isSupported());
    }

    private void verifyMaxYearBoundaries(DateTime start, DateTime end, int maxYear, ISOChronology chronology) {
        assertTrue("Start of max year should be positive", start.getMillis() > 0);
        assertTrue("End should be after start", end.getMillis() > start.getMillis());
        assertEquals("Start year should be max year", maxYear, start.getYear());
        assertEquals("End year should be max year", maxYear, end.getYear());
        
        long actualDelta = end.getMillis() - start.getMillis();
        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals("Year duration should be correct", expectedDelta, actualDelta);
    }

    private void verifyMaxYearStringParsing(DateTime start, DateTime end, int maxYear, ISOChronology chronology) {
        assertEquals("Should parse start of max year", 
                     start, new DateTime(maxYear + "-01-01T00:00:00.000Z", chronology));
        assertEquals("Should parse end of max year", 
                     end, new DateTime(maxYear + "-12-31T23:59:59.999Z", chronology));
    }

    private void verifyMaxYearArithmetic(DateTime start, DateTime end) {
        try {
            start.plusYears(1);
            fail("Should not be able to add year to max year start");
        } catch (IllegalFieldValueException expected) {
            // Expected exception
        }

        try {
            end.plusYears(1);
            fail("Should not be able to add year to max year end");
        } catch (IllegalFieldValueException expected) {
            // Expected exception
        }
    }

    private void verifyMaxYearEdgeCases(ISOChronology chronology, int maxYear) {
        assertEquals("Should handle Long.MAX_VALUE", 
                     maxYear + 1, chronology.year().get(Long.MAX_VALUE));
    }

    private void verifyMinYearBoundaries(DateTime start, DateTime end, int minYear, ISOChronology chronology) {
        assertTrue("Start of min year should be negative", start.getMillis() < 0);
        assertTrue("End should be after start", end.getMillis() > start.getMillis());
        assertEquals("Start year should be min year", minYear, start.getYear());
        assertEquals("End year should be min year", minYear, end.getYear());
        
        long actualDelta = end.getMillis() - start.getMillis();
        long expectedDelta = (start.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY - 1;
        assertEquals("Year duration should be correct", expectedDelta, actualDelta);
    }

    private void verifyMinYearStringParsing(DateTime start, DateTime end, int minYear, ISOChronology chronology) {
        assertEquals("Should parse start of min year", 
                     start, new DateTime(minYear + "-01-01T00:00:00.000Z", chronology));
        assertEquals("Should parse end of min year", 
                     end, new DateTime(minYear + "-12-31T23:59:59.999Z", chronology));
    }

    private void verifyMinYearArithmetic(DateTime start, DateTime end) {
        try {
            start.minusYears(1);
            fail("Should not be able to subtract year from min year start");
        } catch (IllegalFieldValueException expected) {
            // Expected exception
        }

        try {
            end.minusYears(1);
            fail("Should not be able to subtract year from min year end");
        } catch (IllegalFieldValueException expected) {
            // Expected exception
        }
    }

    private void verifyMinYearEdgeCases(ISOChronology chronology, int minYear) {
        assertEquals("Should handle Long.MIN_VALUE", 
                     minYear - 1, chronology.year().get(Long.MIN_VALUE));
    }

    private void verifyYearAddition(String startDate, int yearsToAdd, String expectedEndDate) {
        testAdd(startDate, DurationFieldType.years(), yearsToAdd, expectedEndDate);
    }

    private void verifyMonthAddition(String startDate, int monthsToAdd, String expectedEndDate) {
        testAdd(startDate, DurationFieldType.months(), monthsToAdd, expectedEndDate);
    }

    private void testAdd(String startDateStr, DurationFieldType fieldType, int amount, String expectedEndDateStr) {
        DateTime startDate = new DateTime(startDateStr, ISOChronology.getInstanceUTC());
        DateTime expectedEndDate = new DateTime(expectedEndDateStr, ISOChronology.getInstanceUTC());
        
        // Test forward addition
        assertEquals("Forward addition should work", 
                     expectedEndDate, startDate.withFieldAdded(fieldType, amount));
        
        // Test reverse addition
        assertEquals("Reverse addition should work", 
                     startDate, expectedEndDate.withFieldAdded(fieldType, -amount));

        // Test field difference calculation
        DurationField field = fieldType.getField(ISOChronology.getInstanceUTC());
        int actualDifference = field.getDifference(expectedEndDate.getMillis(), startDate.getMillis());
        assertEquals("Field difference should match amount", amount, actualDifference);
        
        // Test YearMonthDay operations for date fields
        if (isDateField(fieldType)) {
            YearMonthDay startYmd = new YearMonthDay(startDateStr, ISOChronology.getInstanceUTC());
            YearMonthDay expectedEndYmd = new YearMonthDay(expectedEndDateStr, ISOChronology.getInstanceUTC());
            
            assertEquals("YearMonthDay forward addition should work", 
                         expectedEndYmd, startYmd.withFieldAdded(fieldType, amount));
            assertEquals("YearMonthDay reverse addition should work", 
                         startYmd, expectedEndYmd.withFieldAdded(fieldType, -amount));
        }
    }

    private boolean isDateField(DurationFieldType fieldType) {
        return fieldType == DurationFieldType.years() ||
               fieldType == DurationFieldType.months() ||
               fieldType == DurationFieldType.days();
    }
}