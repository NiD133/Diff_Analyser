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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is a Junit unit test for CopticChronology.
 *
 * @author Stephen Colebourne
 */
public class TestCopticChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time for consistent testing: 2002-06-09 (ISO)
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).getMillis();

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    @Before
    public void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() throws Exception {
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
    @Test
    public void getInstanceUTC_shouldReturnSingletonInUTC() {
        // Act
        CopticChronology chrono = CopticChronology.getInstanceUTC();
        // Assert
        assertEquals(DateTimeZone.UTC, chrono.getZone());
        assertSame(CopticChronology.class, chrono.getClass());
    }

    @Test
    public void getInstance_shouldReturnChronologyInDefaultZone() {
        // Act
        CopticChronology chrono = CopticChronology.getInstance();
        // Assert
        assertEquals(LONDON, chrono.getZone());
        assertSame(CopticChronology.class, chrono.getClass());
    }

    @Test
    public void getInstance_withZone_shouldReturnChronologyInThatZone() {
        // Act & Assert
        assertEquals(TOKYO, CopticChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, CopticChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, CopticChronology.getInstance(null).getZone()); // Null zone means default
        assertSame(CopticChronology.class, CopticChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void getInstance_shouldBeSingleton() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(LONDON));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstance(PARIS));
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC());
        assertSame(CopticChronology.getInstance(), CopticChronology.getInstance(LONDON));
    }

    @Test
    public void withUTC_shouldReturnUTCInstance() {
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(LONDON).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstance(TOKYO).withUTC());
        assertSame(CopticChronology.getInstanceUTC(), CopticChronology.getInstanceUTC().withUTC());
    }

    @Test
    public void withZone_shouldReturnInstanceInCorrectZone() {
        assertSame(CopticChronology.getInstance(TOKYO), CopticChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(CopticChronology.getInstance(LONDON), CopticChronology.getInstance(TOKYO).withZone(null));
        assertSame(CopticChronology.getInstance(PARIS), CopticChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void toString_shouldReturnChronologyAndZone() {
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance(LONDON).toString());
        assertEquals("CopticChronology[Asia/Tokyo]", CopticChronology.getInstance(TOKYO).toString());
        assertEquals("CopticChronology[Europe/London]", CopticChronology.getInstance().toString());
        assertEquals("CopticChronology[UTC]", CopticChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Field properties
    //-----------------------------------------------------------------------
    @Test
    public void testDurationFieldProperties() {
        final CopticChronology coptic = CopticChronology.getInstance();

        // Test field names
        assertEquals("eras", coptic.eras().getName());
        assertEquals("centuries", coptic.centuries().getName());
        assertEquals("years", coptic.years().getName());
        assertEquals("months", coptic.months().getName());
        assertEquals("weeks", coptic.weeks().getName());
        assertEquals("days", coptic.days().getName());
        assertEquals("halfdays", coptic.halfdays().getName());
        assertEquals("hours", coptic.hours().getName());
        assertEquals("minutes", coptic.minutes().getName());
        assertEquals("seconds", coptic.seconds().getName());
        assertEquals("millis", coptic.millis().getName());

        // Test field support
        assertFalse(coptic.eras().isSupported());
        assertTrue(coptic.centuries().isSupported());
        assertTrue(coptic.years().isSupported());
        assertTrue(coptic.months().isSupported());
        assertTrue(coptic.weeks().isSupported());
        assertTrue(coptic.days().isSupported());
        assertTrue(coptic.halfdays().isSupported());
        assertTrue(coptic.hours().isSupported());
        assertTrue(coptic.minutes().isSupported());
        assertTrue(coptic.seconds().isSupported());
        assertTrue(coptic.millis().isSupported());

        // Test field precision (in a non-UTC zone)
        assertFalse(coptic.centuries().isPrecise());
        assertFalse(coptic.years().isPrecise());
        assertFalse(coptic.months().isPrecise());
        assertFalse(coptic.weeks().isPrecise());
        assertFalse(coptic.days().isPrecise());
        assertFalse(coptic.halfdays().isPrecise());
        assertTrue(coptic.hours().isPrecise());
        assertTrue(coptic.minutes().isPrecise());
        assertTrue(coptic.seconds().isPrecise());
        assertTrue(coptic.millis().isPrecise());

        // Test field precision (in UTC zone)
        final CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        assertFalse(copticUTC.centuries().isPrecise());
        assertFalse(copticUTC.years().isPrecise());
        assertFalse(copticUTC.months().isPrecise());
        assertTrue(copticUTC.weeks().isPrecise());
        assertTrue(copticUTC.days().isPrecise());
        assertTrue(copticUTC.halfdays().isPrecise());
        assertTrue(copticUTC.hours().isPrecise());
        assertTrue(copticUTC.minutes().isPrecise());
        assertTrue(copticUTC.seconds().isPrecise());
        assertTrue(copticUTC.millis().isPrecise());
    }

    @Test
    public void testDateFieldProperties() {
        final CopticChronology coptic = CopticChronology.getInstance();

        // Test field names
        assertEquals("era", coptic.era().getName());
        assertEquals("centuryOfEra", coptic.centuryOfEra().getName());
        assertEquals("yearOfCentury", coptic.yearOfCentury().getName());
        assertEquals("yearOfEra", coptic.yearOfEra().getName());
        assertEquals("year", coptic.year().getName());
        assertEquals("monthOfYear", coptic.monthOfYear().getName());
        assertEquals("weekyear", coptic.weekyear().getName());
        assertEquals("weekOfWeekyear", coptic.weekOfWeekyear().getName());
        assertEquals("dayOfYear", coptic.dayOfYear().getName());
        assertEquals("dayOfMonth", coptic.dayOfMonth().getName());
        assertEquals("dayOfWeek", coptic.dayOfWeek().getName());

        // Test field support
        assertTrue(coptic.era().isSupported());
        assertTrue(coptic.centuryOfEra().isSupported());
        assertTrue(coptic.yearOfCentury().isSupported());
        assertTrue(coptic.yearOfEra().isSupported());
        assertTrue(coptic.year().isSupported());
        assertTrue(coptic.monthOfYear().isSupported());
        assertTrue(coptic.weekyear().isSupported());
        assertTrue(coptic.weekOfWeekyear().isSupported());
        assertTrue(coptic.dayOfYear().isSupported());
        assertTrue(coptic.dayOfMonth().isSupported());
        assertTrue(coptic.dayOfWeek().isSupported());
    }

    //-----------------------------------------------------------------------
    // Era and Epoch
    //-----------------------------------------------------------------------
    @Test
    public void epoch_shouldMatchJulianDate() {
        // Arrange
        DateTime copticEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        DateTime julianEquivalent = new DateTime(284, 8, 29, 0, 0, 0, 0, JulianChronology.getInstanceUTC());

        // Act & Assert
        assertEquals(julianEquivalent, copticEpoch.withChronology(JulianChronology.getInstanceUTC()));
    }

    @Test
    public void era_shouldBeAM() {
        assertEquals(1, CopticChronology.AM);
        DateTime dt = new DateTime(1, 1, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        assertEquals(CopticChronology.AM, dt.getEra());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_shouldThrowExceptionForYearBefore1() {
        new DateTime(0, 1, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
    }

    //-----------------------------------------------------------------------
    // Calendar calculation tests
    //-----------------------------------------------------------------------
    @Test
    public void nonLeapYear_shouldHave365Days() {
        // Coptic year 1720 is not a leap year (1720 % 4 != 3)
        DateTime startOfYear = new DateTime(1720, 1, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        DateTime endOfYear = new DateTime(1720, 13, 5, 0, 0, 0, 0, CopticChronology.getInstanceUTC());

        assertFalse(startOfYear.year().isLeap());
        assertEquals(365, startOfYear.year().getMaximumValue(CopticChronology.getInstanceUTC().dayOfYear()));
        assertEquals(365, endOfYear.getDayOfYear());
    }

    @Test
    public void leapYear_shouldHave366Days() {
        // Coptic year 1723 is a leap year (1723 % 4 == 3)
        DateTime startOfYear = new DateTime(1723, 1, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        DateTime endOfYear = new DateTime(1723, 13, 6, 0, 0, 0, 0, CopticChronology.getInstanceUTC());

        assertTrue(startOfYear.year().isLeap());
        assertEquals(366, startOfYear.year().getMaximumValue(CopticChronology.getInstanceUTC().dayOfYear()));
        assertEquals(366, endOfYear.getDayOfYear());
    }

    @Test
    public void monthLength_shouldBe30DaysForMonths1to12() {
        CopticChronology chrono = CopticChronology.getInstanceUTC();
        for (int month = 1; month <= 12; month++) {
            DateTime date = new DateTime(1720, month, 1, 0, 0, 0, 0, chrono);
            assertEquals(30, date.dayOfMonth().getMaximumValue());
        }
    }

    @Test
    public void thirteenthMonthLength_shouldBe5DaysInNonLeapYear() {
        // 1720 is not a leap year
        DateTime date = new DateTime(1720, 13, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        assertEquals(5, date.dayOfMonth().getMaximumValue());
    }

    @Test
    public void thirteenthMonthLength_shouldBe6DaysInLeapYear() {
        // 1723 is a leap year
        DateTime date = new DateTime(1723, 13, 1, 0, 0, 0, 0, CopticChronology.getInstanceUTC());
        assertEquals(6, date.dayOfMonth().getMaximumValue());
    }

    @Test
    public void testLeapDayProperties_onDay5And6OfMonth13() {
        // Arrange
        Chronology chrono = CopticChronology.getInstance();
        DateTime day5inLeapYear = new DateTime(3, 13, 5, 0, 0, chrono); // Year 3 is a leap year
        DateTime day6inLeapYear = new DateTime(3, 13, 6, 0, 0, chrono);

        // Assert properties for day 5 (not the leap day itself)
        assertTrue(day5inLeapYear.year().isLeap());
        assertTrue(day5inLeapYear.monthOfYear().isLeap());
        assertFalse(day5inLeapYear.dayOfMonth().isLeap());
        assertFalse(day5inLeapYear.dayOfYear().isLeap());

        // Assert properties for day 6 (the leap day)
        assertTrue(day6inLeapYear.year().isLeap());
        assertTrue(day6inLeapYear.monthOfYear().isLeap());
        assertTrue(day6inLeapYear.dayOfMonth().isLeap());
        assertTrue(day6inLeapYear.dayOfYear().isLeap());
    }

    //-----------------------------------------------------------------------
    // Sample date tests
    //-----------------------------------------------------------------------
    @Test
    public void sampleDate_inUTC_shouldHaveCorrectFieldValues() {
        // Arrange: Convert an ISO date to the Coptic chronology
        DateTime dt = new DateTime(2004, 6, 9, 0, 0, 0, 0, ISOChronology.getInstanceUTC()).withChronology(CopticChronology.getInstanceUTC());

        // Assert: Check all date fields
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(18, dt.getCenturyOfEra());
        assertEquals(20, dt.getYearOfCentury());
        assertEquals(1720, dt.getYearOfEra());
        assertEquals(1720, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        assertEquals(272, dt.getDayOfYear()); // (9 months * 30 days) + 2 days

        // Assert: Check time fields
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    @Test
    public void sampleDate_inParisZone_shouldAdjustForTimeZone() {
        // Arrange: Convert an ISO date in a specific timezone to the Coptic chronology
        DateTime dt = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS).withChronology(CopticChronology.getInstanceUTC());

        // Assert: Check date fields
        assertEquals(CopticChronology.AM, dt.getEra());
        assertEquals(1720, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());

        // Assert: Check time fields (Paris is UTC+2 in summer, so 12:00 becomes 10:00 UTC)
        assertEquals(10, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
    }
}