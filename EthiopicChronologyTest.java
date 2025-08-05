/*
 *  Copyright 2001-2014 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may in a copy of the License at
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
 * This class is a Junit unit test for EthiopicChronology.
 *
 * @author Stephen Colebourne
 */
public class TestEthiopicChronology {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // The Ethiopic epoch (1-01-01) is equivalent to 8-08-29 (Julian).
    private static final DateTime ETHIOPIC_EPOCH_IN_JULIAN = new DateTime(8, 8, 29, 0, 0, 0, 0, JulianChronology.getInstanceUTC());

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

    // Fixed point in time for testing, equivalent to 2002-06-09T00:00:00Z (ISO)
    private static final long TEST_TIME_NOW = new DateTime("2002-06-09T00:00:00Z").getMillis();

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
    }

    //-----------------------------------------------------------------------
    // Factory and Singleton Tests
    //-----------------------------------------------------------------------
    @Test
    public void test_getInstanceUTC_returnsSingletonInUTC() {
        assertEquals(DateTimeZone.UTC, EthiopicChronology.getInstanceUTC().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstanceUTC().getClass());
    }

    @Test
    public void test_getInstance_returnsSingletonInDefaultZone() {
        assertEquals(LONDON, EthiopicChronology.getInstance().getZone());
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance().getClass());
    }

    @Test
    public void test_getInstance_withZone_returnsSingletonInThatZone() {
        assertEquals(TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, EthiopicChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, EthiopicChronology.getInstance(null).getZone()); // Null zone means default
        assertSame(EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }

    @Test
    public void test_getInstance_cachesInstances() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(LONDON));
        assertSame(EthiopicChronology.getInstance(), EthiopicChronology.getInstance(LONDON));
    }

    @Test
    public void test_withUTC_returnsUTCChronology() {
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(LONDON).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstance(TOKYO).withUTC());
        assertSame(EthiopicChronology.getInstanceUTC(), EthiopicChronology.getInstanceUTC().withUTC());
    }

    @Test
    public void test_withZone_returnsChronologyInCorrectZone() {
        assertSame(EthiopicChronology.getInstance(TOKYO), EthiopicChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(EthiopicChronology.getInstance(LONDON), EthiopicChronology.getInstance(TOKYO).withZone(null));
        assertSame(EthiopicChronology.getInstance(PARIS), EthiopicChronology.getInstanceUTC().withZone(PARIS));
    }

    @Test
    public void test_toString_returnsCorrectDescription() {
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance(LONDON).toString());
        assertEquals("EthiopicChronology[Asia/Tokyo]", EthiopicChronology.getInstance(TOKYO).toString());
        assertEquals("EthiopicChronology[Europe/London]", EthiopicChronology.getInstance().toString());
        assertEquals("EthiopicChronology[UTC]", EthiopicChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    // Field Definition Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_durationFields_haveCorrectProperties() {
        final Chronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("eras", ethiopic.eras().getName());
        assertEquals("centuries", ethiopic.centuries().getName());
        assertEquals("years", ethiopic.years().getName());
        assertEquals("months", ethiopic.months().getName());
        assertEquals("days", ethiopic.days().getName());
        // Basic fields are supported
        assertTrue(ethiopic.years().isSupported());
        assertTrue(ethiopic.months().isSupported());
        assertTrue(ethiopic.days().isSupported());
        // Eras are not supported
        assertFalse(ethiopic.eras().isSupported());
    }

    @Test
    public void test_dateFields_haveCorrectProperties() {
        final Chronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("era", ethiopic.era().getName());
        assertEquals("year", ethiopic.year().getName());
        assertEquals("monthOfYear", ethiopic.monthOfYear().getName());
        assertEquals("dayOfMonth", ethiopic.dayOfMonth().getName());
        // All date fields are supported
        assertTrue(ethiopic.era().isSupported());
        assertTrue(ethiopic.year().isSupported());
        assertTrue(ethiopic.monthOfYear().isSupported());
        assertTrue(ethiopic.dayOfMonth().isSupported());
    }

    @Test
    public void test_timeFields_haveCorrectProperties() {
        final Chronology ethiopic = EthiopicChronology.getInstance();
        assertEquals("hourOfDay", ethiopic.hourOfDay().getName());
        assertEquals("minuteOfHour", ethiopic.minuteOfHour().getName());
        assertEquals("secondOfMinute", ethiopic.secondOfMinute().getName());
        assertEquals("millisOfSecond", ethiopic.millisOfSecond().getName());
        // All time fields are supported
        assertTrue(ethiopic.hourOfDay().isSupported());
        assertTrue(ethiopic.minuteOfHour().isSupported());
        assertTrue(ethiopic.secondOfMinute().isSupported());
        assertTrue(ethiopic.millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    // Calendar Rule Tests
    //-----------------------------------------------------------------------
    @Test
    public void test_epoch_correspondsToJulianDate() {
        DateTime ethiopicEpoch = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(ETHIOPIC_EPOCH_IN_JULIAN, ethiopicEpoch.withChronology(JulianChronology.getInstanceUTC()));
    }

    @Test
    public void test_era_isAlwaysEE() {
        assertEquals(EthiopicChronology.EE, 1);
        DateTime dt = new DateTime(1996, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(EthiopicChronology.EE, dt.getEra());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_prolepticYears_areNotSupported() {
        // The Ethiopic chronology is not proleptic, so years before 1 EE are invalid.
        new DateTime(0, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
    }

    /**
     * The Ethiopic calendar has 12 months of 30 days, plus a 13th month (Pagume) of 5 or 6 days.
     * A year is a leap year if (year % 4 == 3). Leap years have 366 days.
     */
    @Test
    public void test_calendarProperties_forNonLeapYear() {
        // Year 1 is not a leap year (1 % 4 != 3)
        DateTime startOfYear1 = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertFalse(startOfYear1.year().isLeap());
        assertEquals(365, startOfYear1.year().getLeapDurationField().getUnitMillis() * 365);

        // Months 1-12 have 30 days
        for(int i = 1; i <= 12; i++) {
            assertEquals(30, startOfYear1.withMonthOfYear(i).dayOfMonth().getMaximumValue());
        }

        // 13th month has 5 days in a non-leap year
        assertEquals(5, startOfYear1.withMonthOfYear(13).dayOfMonth().getMaximumValue());
    }

    @Test
    public void test_calendarProperties_forLeapYear() {
        // Year 3 is a leap year (3 % 4 == 3)
        DateTime startOfYear3 = new DateTime(3, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertTrue(startOfYear3.year().isLeap());
        assertEquals(366, startOfYear3.year().getLeapDurationField().getUnitMillis() * 366);

        // Months 1-12 have 30 days
        for(int i = 1; i <= 12; i++) {
            assertEquals(30, startOfYear3.withMonthOfYear(i).dayOfMonth().getMaximumValue());
        }

        // 13th month has 6 days in a leap year
        assertEquals(6, startOfYear3.withMonthOfYear(13).dayOfMonth().getMaximumValue());
    }

    @Test
    public void test_dayOfWeek_advancesCorrectly() {
        DateTime firstDay = new DateTime(1, 1, 1, 0, 0, 0, 0, ETHIOPIC_UTC); // A Saturday
        assertEquals(DateTimeConstants.SATURDAY, firstDay.getDayOfWeek());

        DateTime secondDay = firstDay.plusDays(1);
        assertEquals(DateTimeConstants.SUNDAY, secondDay.getDayOfWeek());
        assertEquals(1, secondDay.getYear());
        assertEquals(1, secondDay.getMonthOfYear());
        assertEquals(2, secondDay.getDayOfMonth());

        DateTime eighthDay = firstDay.plusDays(7);
        assertEquals(DateTimeConstants.SATURDAY, eighthDay.getDayOfWeek());
    }

    @Test
    public void test_dateTransition_atEndOfNonLeapYear() {
        // End of non-leap year 2 (2 % 4 != 3)
        DateTime lastDayOfYear2 = new DateTime(2, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);

        DateTime firstDayOfYear3 = lastDayOfYear2.plusDays(1);

        assertEquals(3, firstDayOfYear3.getYear());
        assertEquals(1, firstDayOfYear3.getMonthOfYear());
        assertEquals(1, firstDayOfYear3.getDayOfMonth());
    }

    @Test
    public void test_dateTransition_atEndOfLeapYear() {
        // End of leap year 3 (3 % 4 == 3)
        DateTime lastDayOfYear3 = new DateTime(3, 13, 6, 0, 0, 0, 0, ETHIOPIC_UTC);

        DateTime firstDayOfYear4 = lastDayOfYear3.plusDays(1);

        assertEquals(4, firstDayOfYear4.getYear());
        assertEquals(1, firstDayOfYear4.getMonthOfYear());
        assertEquals(1, firstDayOfYear4.getDayOfMonth());
    }


    //-----------------------------------------------------------------------
    // Sample Date Tests
    //-----------------------------------------------------------------------

    /**
     * Tests conversion of a specific ISO date (2004-06-09) to the Ethiopic calendar.
     */
    @Test
    public void test_sampleISODate_convertsCorrectly() {
        DateTime isoDate = new DateTime(2004, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC);
        DateTime dt = isoDate.withChronology(ETHIOPIC_UTC);

        // Year: 1996 is not a leap year (1996 % 4 != 3)
        assertEquals(1996, dt.getYear());
        assertEquals(1996, dt.getYearOfEra());
        assertEquals(96, dt.getYearOfCentury());
        assertEquals(20, dt.getCenturyOfEra()); // (1996-1)/100 + 1 = 20
        assertFalse(dt.year().isLeap());
        assertEquals(365, dt.year().getMaximumValue());

        // Month: Sene (10th month)
        assertEquals(10, dt.getMonthOfYear());

        // Day
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(DateTimeConstants.WEDNESDAY, dt.getDayOfWeek());
        assertEquals(9 * 30 + 2, dt.getDayOfYear()); // (9 full months * 30 days) + 2 days

        // Time fields remain unchanged
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
    }

    /**
     * Tests conversion of an ISO date in a specific timezone.
     */
    @Test
    public void test_sampleISODate_withZone_convertsCorrectly() {
        // Paris is UTC+2 during summer DST
        DateTime isoDate = new DateTime(2004, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime dt = isoDate.withChronology(ETHIOPIC_UTC);

        assertEquals(1996, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(2, dt.getDayOfMonth());
        assertEquals(10, dt.getHourOfDay()); // 12:00 in Paris is 10:00 in UTC
    }

    //-----------------------------------------------------------------------
    // Duration Field Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_yearsDurationField_handlesLeapYearsCorrectly() {
        // Ethiopic year 1999 is a leap year (1999 % 4 == 3)
        DateTime start = new DateTime(1996, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        DurationField fld = start.year().getDurationField();

        // Add 1 year to a non-leap year date
        DateTime year1 = new DateTime(1997, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(year1, start.plusYears(1));

        // Add 3 years, crossing no leap year
        DateTime year3 = new DateTime(1999, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(year3, start.plusYears(3));

        // Add 4 years, crossing one leap year (1999)
        DateTime year4 = new DateTime(2000, 10, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(year4, start.plusYears(4));
        long fourYearsInMillis = (4L * 365L + 1L) * DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(fourYearsInMillis, fld.getMillis(4, start.getMillis()));
    }

    @Test
    public void test_monthsDurationField_handlesLeapYearsCorrectly() {
        // Ethiopic year 1999 is a leap year. Its 13th month (Pagume) has 6 days.
        DateTime start = new DateTime(1999, 11, 2, 0, 0, 0, 0, ETHIOPIC_UTC);

        // Add 1 month (30 days)
        DateTime month1 = new DateTime(1999, 12, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(month1, start.plusMonths(1));

        // Add 2 months, lands in the 6-day 13th month
        DateTime month2 = new DateTime(1999, 13, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(month2, start.plusMonths(2));

        // Add 3 months, crosses the 6-day 13th month into the next year
        DateTime month3 = new DateTime(2000, 1, 2, 0, 0, 0, 0, ETHIOPIC_UTC);
        assertEquals(month3, start.plusMonths(3));
    }

    //-----------------------------------------------------------------------
    // Leap Field Tests on specific days
    //-----------------------------------------------------------------------

    @Test
    public void test_isLeap_onDay5of13thMonth_inLeapYear() {
        // Day 5 of month 13 exists in both leap and non-leap years.
        DateTime dt = new DateTime(3, 13, 5, 0, 0, ETHIOPIC_UTC); // Year 3 is leap
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap()); // The month itself is considered leap
        assertFalse(dt.dayOfMonth().isLeap());
        assertFalse(dt.dayOfYear().isLeap());
    }

    @Test
    public void test_isLeap_onDay6of13thMonth_inLeapYear() {
        // Day 6 of month 13 only exists in a leap year.
        DateTime dt = new DateTime(3, 13, 6, 0, 0, ETHIOPIC_UTC); // Year 3 is leap
        assertTrue(dt.year().isLeap());
        assertTrue(dt.monthOfYear().isLeap());
        assertTrue(dt.dayOfMonth().isLeap()); // This day is the leap day
        assertTrue(dt.dayOfYear().isLeap()); // This day is the leap day of the year
    }
}