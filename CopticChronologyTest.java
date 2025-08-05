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
 * This class is a Junit unit test for CopticChronology.
 *
 * @author Stephen Colebourne
 */
public class TestCopticChronology extends TestCase {

    private static final int MILLIS_PER_DAY = DateTimeConstants.MILLIS_PER_DAY;
    private static final long TEST_TIME_2002_06_09 = calculateTestTimeNow();

    // Time zones for testing
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    
    // Chronologies for testing
    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestCopticChronology.class);
    }

    public TestCopticChronology(String name) {
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

    // Helper method to calculate fixed test time (2002-06-09)
    private static long calculateTestTimeNow() {
        // Days from 1970 to 2002 (including leap days)
        long days1970To2002 = 365 * 32 + 8; // 8 leap days (1972,1976,1980,1984,1988,1992,1996,2000)
        // Days from Jan to June 9 in 2002 (Jan31 + Feb28 + Mar31 + Apr30 + May31 + 9)
        int daysIn2002 = 31 + 28 + 31 + 30 + 31 + 9;
        return (days1970To2002 + daysIn2002) * MILLIS_PER_DAY;
    }

    //-----------------------------------------------------------------------
    // Test Chronology Factory Methods
    //-----------------------------------------------------------------------
    
    public void testFactoryUTC() {
        assertEquals("UTC instance should have UTC zone", 
            DateTimeZone.UTC, CopticChronology.getInstanceUTC().getZone());
        assertSame("Should be same CopticChronology class", 
            CopticChronology.class, CopticChronology.getInstanceUTC().getClass());
    }

    public void testFactoryDefaultZone() {
        assertEquals("Default instance should have London zone", 
            LONDON, CopticChronology.getInstance().getZone());
        assertSame("Should be same CopticChronology class", 
            CopticChronology.class, CopticChronology.getInstance().getClass());
    }

    public void testFactorySpecificZone() {
        assertEquals("Tokyo zone should be respected", 
            TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals("Paris zone should be respected", 
            PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals("Null zone should default to London", 
            LONDON, CopticChronology.getInstance(null).getZone());
        assertSame("Should be same CopticChronology class", 
            CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    // Test Chronology Instance Management
    //-----------------------------------------------------------------------
    
    public void testInstanceEquality() {
        assertSame("Same instance for TOKYO should be returned", 
            CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame("Same instance for LONDON should be returned", 
            CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame("Same instance for UTC should be returned", 
            CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
    }

    public void testWithUTC() {
        assertSame("withUTC should return UTC instance", 
            CopticChronology.getInstanceUTC(), CopticChronology.getInstance(LONDON).withUTC());
        assertSame("withUTC should return same UTC instance", 
            CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC().withUTC());
    }

    public void testWithZone() {
        assertSame("Same instance when zone unchanged", 
            CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame("Switching zone should return correct instance", 
            CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame("Null zone should default to London", 
            CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(null));
    }

    public void testToString() {
        assertEquals("toString should include zone info", 
            "CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("toString should include zone info", 
            "CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Test Field Characteristics
    //-----------------------------------------------------------------------
    
    public void testDurationFieldCharacteristics() {
        verifyDurationFields(CopticChronology.getInstance());
        verifyDurationFieldsWithZone(CopticChronology.getInstanceUTC());
    }

    private void verifyDurationFields(Chronology chrono) {
        // Field names
        assertEquals("eras", chrono.eras().getName());
        assertEquals("centuries", chrono.centuries().getName());
        assertEquals("years", chrono.years().getName());
        
        // Precision checks
        assertFalse("Centuries should not be precise", chrono.centuries().isPrecise());
        assertFalse("Years should not be precise", chrono.years().isPrecise());
        assertTrue("Hours should be precise", chrono.hours().isPrecise());
    }

    private void verifyDurationFieldsWithZone(Chronology chrono) {
        assertTrue("Weeks should be precise in UTC", chrono.weeks().isPrecise());
        assertTrue("Days should be precise in UTC", chrono.days().isPrecise());
    }

    public void testDateFieldCharacteristics() {
        Chronology chrono = CopticChronology.getInstance();
        
        // Field names
        assertEquals("era", chrono.era().getName());
        assertEquals("year", chrono.year().getName());
        assertEquals("monthOfYear", chrono.monthOfYear().getName());
        
        // Support checks
        assertTrue("Era should be supported", chrono.era().isSupported());
        assertTrue("Year should be supported", chrono.year().isSupported());
    }

    public void testTimeFieldCharacteristics() {
        Chronology chrono = CopticChronology.getInstance();
        assertTrue("hourOfDay should be supported", chrono.hourOfDay().isSupported());
        assertTrue("minuteOfHour should be supported", chrono.minuteOfHour().isSupported());
    }

    //-----------------------------------------------------------------------
    // Test Date Conversions
    //-----------------------------------------------------------------------
    
    public void testEpochConversion() {
        DateTime copticEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        DateTime julianEquivalent = new DateTime(284, 8, 29, 0, 0, 0, 0, JULIAN_UTC);
        assertEquals("Coptic epoch should match Julian date", julianEquivalent, copticEpoch.withChronology(JULIAN_UTC));
    }

    public void testEraValidation() {
        try {
            new DateTime(-1, 13, 5, 0, 0, 0, 0, COPTIC_UTC);
            fail("Should not allow negative year (BC) in Coptic chronology");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    // Test Comprehensive Calendar Functionality
    //-----------------------------------------------------------------------
    
    public void testCalendarConsistency() {
        if (TestAll.FAST) {
            return; // Skip long-running test in fast mode
        }
        
        DateTime current = new DateTime(1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
        DateTime end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC);
        
        int[] expectedValues = new int[] {1, 1, 1, 1}; // year, month, day, dow
        long step = MILLIS_PER_DAY; // Test every day
        
        while (current.isBefore(end)) {
            verifyCalendarValues(current, expectedValues);
            updateExpectedValues(expectedValues);
            current = current.plus(step);
        }
    }

    private void verifyCalendarValues(DateTime dt, int[] expected) {
        int year = expected[0];
        int month = expected[1];
        int day = expected[2];
        int dow = expected[3];
        
        assertEquals("Year mismatch", year, dt.getYear());
        assertEquals("Month mismatch", month, dt.getMonthOfYear());
        assertEquals("Day mismatch", day, dt.getDayOfMonth());
        assertEquals("Day of week mismatch", dow, dt.getDayOfWeek());
        
        // Verify month length
        int actualMonthLength = dt.dayOfMonth().getMaximumValue();
        if (month == 13) { // 13th month (Epagomenal days)
            assertEquals("Leap year should have 6 days in month 13", 
                dt.year().isLeap() ? 6 : 5, actualMonthLength);
        } else {
            assertEquals("Regular month should have 30 days", 30, actualMonthLength);
        }
    }

    private void updateExpectedValues(int[] expected) {
        // Update day of week (1-7)
        expected[3] = (expected[3] % 7) + 1;
        expected[2]++; // Increment day
        
        // Handle month/year rollover
        if (expected[1] < 13 && expected[2] > 30) {
            expected[2] = 1;
            expected[1]++;
        } else if (expected[1] == 13) {
            int maxDays = (expected[0] % 4 == 3) ? 6 : 5;
            if (expected[2] > maxDays) {
                expected[2] = 1;
                expected[1] = 1;
                expected[0]++;
            }
        }
    }

    //-----------------------------------------------------------------------
    // Test Specific Date Functionality
    //-----------------------------------------------------------------------
    
    public void testSpecificDateProperties() {
        // 2004-06-09 in ISO is 1720-10-02 in Coptic
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISO_UTC).withChronology(COPTIC_UTC);
        
        // Verify basic date properties
        assertEquals("Era should be AM", CopticChronology.AM, dt.getEra());
        assertEquals("Year should be 1720", 1720, dt.getYear());
        assertEquals("Month should be 10", 10, dt.getMonthOfYear());
        assertEquals("Day should be 2", 2, dt.getDayOfMonth());
        
        // Verify year properties
        Property yearProp = dt.year();
        assertFalse("1720 should not be leap year", yearProp.isLeap());
        assertEquals("Leap amount should be 0", 0, yearProp.getLeapAmount());
        
        // Verify month properties
        Property monthProp = dt.monthOfYear();
        assertEquals("Min month should be 1", 1, monthProp.getMinimumValue());
        assertEquals("Max month should be 13", 13, monthProp.getMaximumValue());
    }

    public void testDateWithTimeZone() {
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(COPTIC_UTC);
        assertEquals("Hour should adjust for timezone", 10, dt.getHourOfDay()); // PARIS is UTC+2
    }

    //-----------------------------------------------------------------------
    // Test Duration Calculations
    //-----------------------------------------------------------------------
    
    public void testYearDuration() {
        DateTime start = new DateTime(1720, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime plus1 = new DateTime(1721, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime plus4 = new DateTime(1724, 10, 2, 0, 0, 0, 0, COPTIC_UTC);
        
        DurationField field = COPTIC_UTC.years();
        assertEquals("1 year duration", 
            plus1.getMillis() - start.getMillis(), field.getMillis(1, start.getMillis()));
        assertEquals("4 years duration (including leap)", 
            plus4.getMillis() - start.getMillis(), field.getMillis(4, start.getMillis()));
    }

    public void testMonthDuration() {
        DateTime start = new DateTime(1723, 11, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime plus1 = new DateTime(1723, 12, 2, 0, 0, 0, 0, COPTIC_UTC);
        DateTime plus3 = new DateTime(1724, 1, 2, 0, 0, 0, 0, COPTIC_UTC);
        
        DurationField field = COPTIC_UTC.months();
        assertEquals("1 month duration", 
            plus1.getMillis() - start.getMillis(), field.getMillis(1, start.getMillis()));
        assertEquals("3 months duration (including short month)", 
            plus3.getMillis() - start.getMillis(), field.getMillis(3, start.getMillis()));
    }

    //-----------------------------------------------------------------------
    // Test Leap Year Functionality
    //-----------------------------------------------------------------------
    
    public void testLeapDayDetection() {
        // Leap year (year % 4 == 3)
        DateTime leapDay = new DateTime(3, 13, 6, 0, 0, COPTIC_UTC);
        assertTrue("Should be leap day", leapDay.dayOfMonth().isLeap());
        
        // Non-leap day in leap month
        DateTime nonLeapDay = new DateTime(3, 13, 5, 0, 0, COPTIC_UTC);
        assertFalse("Should not be leap day", nonLeapDay.dayOfMonth().isLeap());
    }
}