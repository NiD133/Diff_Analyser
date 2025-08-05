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
 * This class is a Junit unit test for EthiopicChronology.
 *
 * @author Stephen Colebourne
 */
public class TestEthiopicChronology extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;

    // Test time for 2002-06-09 in London timezone
    private static final long TEST_TIME_2002_06_09 = calculateTestTime();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    // Chronology instances for common timezones
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    // Calculate fixed test time for 2002-06-09
    private static long calculateTestTime() {
        long daysFrom1970To2002 = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                                  366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                                  365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                                  366 + 365;
        long daysIn2002UntilJune = 31L + 28L + 31L + 30L + 31L + 9L - 1L;
        return (daysFrom1970To2002 + daysIn2002UntilJune) * MILLIS_PER_DAY;
    }

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
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_2002_06_09);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

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
    // Test factory methods
    //-----------------------------------------------------------------------
    
    public void testFactoryUTC_ReturnsUTCZone() {
        assertEquals(DateTimeZone.UTC, EthiopicChronology.getInstanceUTC().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstanceUTC().getClass());
    }

    public void testFactory_NoParameter_ReturnsDefaultZone() {
        assertEquals(LONDON, EthiopicChronology.getInstance().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance().getClass());
    }

    public void testFactory_WithZone_ReturnsCorrectZone() {
        assertEquals(TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, EthiopicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, EthiopicChronology.getInstance(null).getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    // Test instance management
    //-----------------------------------------------------------------------
    
    public void testGetInstance_WithSameZone_ReturnsSameInstance() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(PARIS));
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC());
        assertSame(EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }

    public void testWithUTC_ConvertsToUTCInstance() {
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(LONDON).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(TOKYO).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC().withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance().withUTC());
    }

    public void testWithZone_ConvertsToSpecifiedZone() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(null));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstance().withZone(PARIS));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString_ContainsChronologyNameAndZone() {
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance(LONDON).toString());
        assertEquals("EthiopicChronology[Asia/Tokyo]", EthiopicChronology.getInstance(TOKYO).toString());
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance().toString());
        assertEquals("EthiopicChronology[UTC]", EthiopicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Test duration fields
    //-----------------------------------------------------------------------
    
    public void testDurationFieldNamesAndProperties() {
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
        
        // Verify field support
        assertFalse(ethiopic.eras().isSupported());
        assertTrue(ethiopic.centuries().isSupported());
        assertTrue(ethiopic.years().isSupported());
        assertTrue(ethiopic.weekyears().isSupported());
        assertTrue(ethiopic.months().isSupported());
        assertTrue(ethiopic.weeks().isSupported());
        assertTrue(ethiopic.days().isSupported());
        assertTrue(ethiopic.halfdays().isSupported());
        assertTrue(ethiopic.hours().isSupported());
        assertTrue(ethiopic.minutes().isSupported());
        assertTrue(ethiopic.seconds().isSupported());
        assertTrue(ethiopic.millis().isSupported());
        
        // Verify precision in default zone
        assertFalse(ethiopic.centuries().isPrecise());
        assertFalse(ethiopic.years().isPrecise());
        assertFalse(ethiopic.weekyears().isPrecise());
        assertFalse(ethiopic.months().isPrecise());
        assertFalse(ethiopic.weeks().isPrecise());
        assertFalse(ethiopic.days().isPrecise());
        assertFalse(ethiopic.halfdays().isPrecise());
        assertTrue(ethiopic.hours().isPrecise());
        assertTrue(ethiopic.minutes().isPrecise());
        assertTrue(ethiopic.seconds().isPrecise());
        assertTrue(ethiopic.millis().isPrecise());
        
        // Verify precision in UTC
        final EthiopicChronology ethiopicUTC = EthiopicChronology.getInstanceUTC();
        assertFalse(ethiopicUTC.centuries().isPrecise());
        assertFalse(ethiopicUTC.years().isPrecise());
        assertFalse(ethiopicUTC.weekyears().isPrecise());
        assertFalse(ethiopicUTC.months().isPrecise());
        assertTrue(ethiopicUTC.weeks().isPrecise());
        assertTrue(ethiopicUTC.days().isPrecise());
        assertTrue(ethiopicUTC.halfdays().isPrecise());
        assertTrue(ethiopicUTC.hours().isPrecise());
        assertTrue(ethiopicUTC.minutes().isPrecise());
        assertTrue(ethiopicUTC.seconds().isPrecise());
        assertTrue(ethiopicUTC.millis().isPrecise());
        
        // Verify precision in GMT
        final DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        final EthiopicChronology ethiopicGMT = EthiopicChronology.getInstance(gmt);
        assertFalse(ethiopicGMT.centuries().isPrecise());
        assertFalse(ethiopicGMT.years().isPrecise());
        assertFalse(ethiopicGMT.weekyears().isPrecise());
        assertFalse(ethiopicGMT.months().isPrecise());
        assertTrue(ethiopicGMT.weeks().isPrecise());
        assertTrue(ethiopicGMT.days().isPrecise());
        assertTrue(ethiopicGMT.halfdays().isPrecise());
        assertTrue(ethiopicGMT.hours().isPrecise());
        assertTrue(ethiopicGMT.minutes().isPrecise());
        assertTrue(ethiopicGMT.seconds().isPrecise());
        assertTrue(ethiopicGMT.millis().isPrecise());
    }

    //-----------------------------------------------------------------------
    // Test date/time fields
    //-----------------------------------------------------------------------
    
    public void testDateTimeFieldNamesAndProperties() {
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
        
        // Verify field support
        assertTrue(ethiopic.era().isSupported());
        assertTrue(ethiopic.centuryOfEra().isSupported());
        assertTrue(ethiopic.yearOfCentury().isSupported());
        assertTrue(ethiopic.yearOfEra().isSupported());
        assertTrue(ethiopic.year().isSupported());
        assertTrue(ethiopic.monthOfYear().isSupported());
        assertTrue(ethiopic.weekyearOfCentury().isSupported());
        assertTrue(ethiopic.weekyear().isSupported());
        assertTrue(ethiopic.weekOfWeekyear().isSupported());
        assertTrue(ethiopic.dayOfYear().isSupported());
        assertTrue(ethiopic.dayOfMonth().isSupported());
        assertTrue(ethiopic.dayOfWeek().isSupported());
        
        // Verify duration fields
        assertEquals(ethiopic.eras(), ethiopic.era().getDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.centuryOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfCentury().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.yearOfEra().getDurationField());
        assertEquals(ethiopic.years(), ethiopic.year().getDurationField());
        assertEquals(ethiopic.months(), ethiopic.monthOfYear().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyearOfCentury().getDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekyear().getDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.weekOfWeekyear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfYear().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfMonth().getDurationField());
        assertEquals(ethiopic.days(), ethiopic.dayOfWeek().getDurationField());
        
        // Verify range duration fields
        assertNull(ethiopic.era().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.centuryOfEra().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.yearOfCentury().getRangeDurationField());
        assertEquals(ethiopic.eras(), ethiopic.yearOfEra().getRangeDurationField());
        assertNull(ethiopic.year().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.monthOfYear().getRangeDurationField());
        assertEquals(ethiopic.centuries(), ethiopic.weekyearOfCentury().getRangeDurationField());
        assertNull(ethiopic.weekyear().getRangeDurationField());
        assertEquals(ethiopic.weekyears(), ethiopic.weekOfWeekyear().getRangeDurationField());
        assertEquals(ethiopic.years(), ethiopic.dayOfYear().getRangeDurationField());
        assertEquals(ethiopic.months(), ethiopic.dayOfMonth().getRangeDurationField());
        assertEquals(ethiopic.weeks(), ethiopic.dayOfWeek().getRangeDurationField());
    }

    public void testTimeFieldNames() {
        final EthiopicChronology ethiopic = EthiopicChronology.getInstance();
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
        assertTrue(ethiopic.halfdayOfDay().isSupported());
        assertTrue(ethiopic.clockhourOfHalfday().isSupported());
        assertTrue(ethiopic.hourOfHalfday().isSupported());
        assertTrue(ethiopic.clockhourOfDay().isSupported());
        assertTrue(ethiopic.hourOfDay().isSupported());
        assertTrue(ethiopic.minuteOfDay().isSupported());
        assertTrue(ethiopic.minuteOfHour().isSupported());
        assertTrue(ethiopic.secondOfDay().isSupported());
        assertTrue(ethiopic.secondOfMinute().isSupported());
        assertTrue(ethiopic.millisOfDay().isSupported());
        assertTrue(ethiopic.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    // Test epoch and era
    //-----------------------------------------------------------------------
    
    public void testEpoch_FirstDateInEthiopicCorrespondsToJulianDate() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime expectedJulian = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals(expectedJulian, epoch.withChronology(JULIAN_UTC));
    }

    public void testEra_InvalidYearThrowsException() {
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
            fail("Expected IllegalArgumentException for negative year");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Comprehensive calendar test (long-running)
    //-----------------------------------------------------------------------
    
    public void testCalendar_Comprehensive() {
        if (TestAll.FAST) {
            return;
        }
        System.out.println("\nTestEthiopicChronology.testCalendar (comprehensive)");
        
        // Fixed skip value for testing
        final long SKIP_MILLIS = 1 * MILLIS_PER_DAY;
        
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        long millis = epoch.getMillis();
        long endMillis = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        
        DateTimeField dayOfWeek = ETHIOPIC_UTC.dayOfWeek();
        DateTimeField dayOfYear = ETHIOPIC_UTC.dayOfYear();
        DateTimeField dayOfMonth = ETHIOPIC_UTC.dayOfMonth();
        DateTimeField monthOfYear = ETHIOPIC_UTC.monthOfYear();
        DateTimeField year = ETHIOPIC_UTC.year();
        DateTimeField yearOfEra = ETHIOPIC_UTC.yearOfEra();
        DateTimeField era = ETHIOPIC_UTC.era();
        
        // Starting values for expected fields
        int expectedDOW = new DateTime(8, 8, 29, 0, 0, 0, 0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;

        while (millis < endMillis) {
            // Verify era
            assertEquals(1, era.get(millis));
            assertEquals("EE", era.getAsText(millis));
            assertEquals("EE", era.getAsShortText(millis));
            
            // Verify date components
            assertEquals(expectedYear, year.get(millis));
            assertEquals(expectedYear, yearOfEra.get(millis));
            assertEquals(expectedMonth, monthOfYear.get(millis));
            assertEquals(expectedDay, dayOfMonth.get(millis));
            assertEquals(expectedDOW, dayOfWeek.get(millis));
            assertEquals(expectedDOY, dayOfYear.get(millis));
            
            // Verify leap year
            boolean isLeapYear = (expectedYear % 4 == 3);
            assertEquals(isLeapYear, year.isLeap(millis));
            
            // Verify month length
            int actualMonthLength = dayOfMonth.getMaximumValue(millis);
            if (expectedMonth == 13) {
                assertEquals(isLeapYear, monthOfYear.isLeap(millis));
                assertEquals(isLeapYear ? 6 : 5, actualMonthLength);
            } else {
                assertEquals(30, actualMonthLength);
            }
            
            // Advance to next day
            expectedDOW = (expectedDOW % 7) + 1; // Cycle 1-7
            expectedDay++;
            expectedDOY++;
            
            // Handle month/year transitions
            if (expectedMonth < 13 && expectedDay > 30) {
                expectedDay = 1;
                expectedMonth++;
            } else if (expectedMonth == 13) {
                if (isLeapYear && expectedDay > 6) {
                    resetToNewYear(expectedYear + 1);
                } else if (!isLeapYear && expectedDay > 5) {
                    resetToNewYear(expectedYear + 1);
                }
            }
            
            millis += SKIP_MILLIS;
        }
    }

    // Helper method to reset date to new year
    private void resetToNewYear(int newYear) {
        expectedDay = 1;
        expectedMonth = 1;
        expectedYear = newYear;
        expectedDOY = 1;
    }

    //-----------------------------------------------------------------------
    // Test sample date (2004-06-09 in ISO)
    //-----------------------------------------------------------------------
    
    public void testSampleDate_FieldsCorrect() {
        // 2004-06-09 in ISO is 1996-10-02 in Ethiopic
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(ETHIOPIC_UTC);
        
        // Verify era
        assertEquals(EthiopicChronology.EE, dt.getEra());
        
        // Verify year fields
        assertEquals(1996, dt.getYear());
        Property yearField = dt.year();
        assertFalse(yearField.isLeap());
        assertEquals(0, yearField.getLeapAmount());
        assertEquals(DurationFieldType.days(), yearField.getLeapDurationField().getType());
        assertEquals(new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC), yearField.addToCopy(1));
        
        // Verify month fields
        assertEquals(10, dt.getMonthOfYear());
        Property monthField = dt.monthOfYear();
        assertFalse(monthField.isLeap());
        assertEquals(0, monthField.getLeapAmount());
        assertEquals(DurationFieldType.days(), monthField.getLeapDurationField().getType());
        assertEquals(1, monthField.getMinimumValue());
        assertEquals(1, monthField.getMinimumValueOverall());
        assertEquals(13, monthField.getMaximumValue());
        assertEquals(13, monthField.getMaximumValueOverall());
        assertEquals(new DateTime(1997, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), monthField.addToCopy(4));
        assertEquals(new DateTime(1996, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC), monthField.addWrapFieldToCopy(4));
        
        // Verify day fields
        assertEquals(2, dt.getDayOfMonth());
        Property dayField = dt.dayOfMonth();
        assertFalse(dayField.isLeap());
        assertEquals(0, dayField.getLeapAmount());
        assertNull(dayField.getLeapDurationField());
        assertEquals(1, dayField.getMinimumValue());
        assertEquals(1, dayField.getMinimumValueOverall());
        assertEquals(30, dayField.getMaximumValue());
        assertEquals(30, dayField.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), dayField.addToCopy(1));
        
        // Verify day of week
        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        Property dowField = dt.dayOfWeek();
        assertFalse(dowField.isLeap());
        assertEquals(0, dowField.getLeapAmount());
        assertNull(dowField.getLeapDurationField());
        assertEquals(1, dowField.getMinimumValue());
        assertEquals(1, dowField.getMinimumValueOverall());
        assertEquals(7, dowField.getMaximumValue());
        assertEquals(7, dowField.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), dowField.addToCopy(1));
        
        // Verify day of year
        int expectedDOY = 9 * 30 + 2; // 9 months * 30 days + 2 days
        assertEquals(expectedDOY, dt.getDayOfYear());
        Property doyField = dt.dayOfYear();
        assertFalse(doyField.isLeap());
        assertEquals(0, doyField.getLeapAmount());
        assertNull(doyField.getLeapDurationField());
        assertEquals(1, doyField.getMinimumValue());
        assertEquals(1, doyField.getMinimumValueOverall());
        assertEquals(365, doyField.getMaximumValue());
        assertEquals(366, doyField.getMaximumValueOverall());
        assertEquals(new DateTime(1996, 10, 3, 0, 0, 0, 0, ETHIOPIC_UTC), doyField.addToCopy(1));
        
        // Verify time fields
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithTimeZone_FieldsCorrect() {
        // 2004-06-09 12:00 in Paris (UTC+2) is 1996-10-02 10:00 in Ethiopic UTC
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(ETHIOPIC_UTC);
        
        assertEquals(EthiopicChronology.EE, dt.getEra());
        assertEquals(1996, dt.getYear());
        assertEquals(1996, dt.getYearOfEra());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay());  // UTC conversion: 12 - 2 = 10
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    // Test duration fields for years and months
    //-----------------------------------------------------------------------
    
    public void testDurationField_Year() {
        DateTime dt96 = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt97 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt98 = new DateTime(1998, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt99 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt00 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField yearField = dt96.year().getDurationField();
        assertEquals(ETHIOPIC_UTC.years(), yearField);
        
        // Verify millis for year spans
        assertEquals(365 * MILLIS_PER_DAY, yearField.getMillis(1, dt96.getMillis()));
        assertEquals(2 * 365 * MILLIS_PER_DAY, yearField.getMillis(2, dt96.getMillis()));
        assertEquals(3 * 365 * MILLIS_PER_DAY, yearField.getMillis(3, dt96.getMillis()));
        assertEquals((4 * 365 + 1) * MILLIS_PER_DAY, yearField.getMillis(4, dt96.getMillis()));
        
        // Verify average millis
        long avgMillisPerYear = (4 * 365 + 1) * MILLIS_PER_DAY / 4;
        assertEquals(avgMillisPerYear, yearField.getMillis(1));
        assertEquals(avgMillisPerYear * 2, yearField.getMillis(2));
        
        // Verify value calculations
        assertEquals(0, yearField.getValue(365 * MILLIS_PER_DAY - 1, dt96.getMillis()));
        assertEquals(1, yearField.getValue(365 * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(1, yearField.getValue(365 * MILLIS_PER_DAY + 1, dt96.getMillis()));
        assertEquals(3, yearField.getValue(3 * 365 * MILLIS_PER_DAY, dt96.getMillis()));
        assertEquals(4, yearField.getValue((4 * 365 + 1) * MILLIS_PER_DAY, dt96.getMillis()));
        
        // Verify add operations
        assertEquals(dt97.getMillis(), yearField.add(dt96.getMillis(), 1));
        assertEquals(dt98.getMillis(), yearField.add(dt96.getMillis(), 2));
        assertEquals(dt99.getMillis(), yearField.add(dt96.getMillis(), 3));
        assertEquals(dt00.getMillis(), yearField.add(dt96.getMillis(), 4));
    }

    public void testDurationField_Month() {
        DateTime dt11 = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt12 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt13 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DateTime dt01 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        
        DurationField monthField = dt11.monthOfYear().getDurationField();
        assertEquals(ETHIOPIC_UTC.months(), monthField);
        
        // Verify millis for month spans (accounts for leap month)
        assertEquals(30 * MILLIS_PER_DAY, monthField.getMillis(1, dt11.getMillis()));
        assertEquals(60 * MILLIS_PER_DAY, monthField.getMillis(2, dt11.getMillis()));
        assertEquals((60 + 6) * MILLIS_PER_DAY, monthField.getMillis(3, dt11.getMillis()));
        assertEquals((90 + 6) * MILLIS_PER_DAY, monthField.getMillis(4, dt11.getMillis()));
        
        // Verify average millis
        long avgMillisPerMonth = 30 * MILLIS_PER_DAY;
        assertEquals(avgMillisPerMonth, monthField.getMillis(1));
        assertEquals(avgMillisPerMonth * 2, monthField.getMillis(2));
        
        // Verify value calculations
        assertEquals(0, monthField.getValue(30 * MILLIS_PER_DAY - 1, dt11.getMillis()));
        assertEquals(1, monthField.getValue(30 * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(2, monthField.getValue(60 * MILLIS_PER_DAY, dt11.getMillis()));
        assertEquals(3, monthField.getValue((60 + 6) * MILLIS_PER_DAY, dt11.getMillis()));
        
        // Verify add operations
        assertEquals(dt12.getMillis(), monthField.add(dt11.getMillis(), 1));
        assertEquals(dt13.getMillis(), monthField.add(dt11.getMillis(), 2));
        assertEquals(dt01.getMillis(), monthField.add(dt11.getMillis(), 3));
    }

    //-----------------------------------------------------------------------
    // Test leap year behavior
    //-----------------------------------------------------------------------
    
    public void testLeapYear_13thMonthHas5Days() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 5, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    public void testLeapYear_13thMonthHas6Days() {
        Chronology chrono = EthiopicChronology.getInstance();
        DateTime dt = new DateTime(3, 13, 6, 0, 0, chrono);
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap());
        assertTrue(dt.dayOfYear().isLeap());
    }
}