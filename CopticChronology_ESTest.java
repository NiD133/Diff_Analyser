/*
 * Copyright 2001-2014 Stephen Colebourne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class contains unit tests for the CopticChronology.
 * The tests are organized by the functionality they cover, such as
 * instance creation, time zone handling, and calendar-specific logic.
 */
public class CopticChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // --- Factory Methods and Caching ---

    @Test
    public void getInstance_noArgs_returnsInstanceInDefaultZone() {
        CopticChronology chronology = CopticChronology.getInstance();
        assertEquals(DateTimeZone.getDefault(), chronology.getZone());
    }

    @Test
    public void getInstance_withUTC_returnsCachedUTCInstance() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        assertEquals(UTC, chronology.getZone());
        assertSame(chronology, CopticChronology.getInstance(UTC));
    }

    @Test
    public void getInstance_withZone_returnsCachedInstanceForThatZone() {
        CopticChronology chronology = CopticChronology.getInstance(PARIS);
        assertEquals(PARIS, chronology.getZone());
        assertSame(chronology, CopticChronology.getInstance(PARIS));
    }

    @Test
    public void getInstance_withNullZone_returnsInstanceInDefaultZone() {
        CopticChronology chronology = CopticChronology.getInstance(null);
        assertEquals(DateTimeZone.getDefault(), chronology.getZone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstance_withInvalidMinDays_throwsIllegalArgumentException() {
        // The valid range for minDaysInFirstWeek is 1 to 7.
        CopticChronology.getInstance(UTC, 0);
    }

    // --- Time Zone Handling (withZone, withUTC) ---

    @Test
    public void withUTC_onNonUTCChronology_returnsUTCChronology() {
        CopticChronology parisChronology = CopticChronology.getInstance(PARIS);
        Chronology utcChronology = parisChronology.withUTC();
        
        assertNotSame(parisChronology, utcChronology);
        assertEquals(UTC, utcChronology.getZone());
        assertSame(CopticChronology.getInstanceUTC(), utcChronology);
    }

    @Test
    public void withZone_withDifferentZone_returnsNewInstance() {
        CopticChronology utcChronology = CopticChronology.getInstanceUTC();
        Chronology parisChronology = utcChronology.withZone(PARIS);

        assertNotSame(utcChronology, parisChronology);
        assertEquals(PARIS, parisChronology.getZone());
    }

    @Test
    public void withZone_withSameZone_returnsSameInstance() {
        CopticChronology parisChronology = CopticChronology.getInstance(PARIS);
        Chronology sameChronology = parisChronology.withZone(PARIS);
        assertSame(parisChronology, sameChronology);
    }

    @Test
    public void withZone_withNullZone_returnsDefaultZoneInstance() {
        CopticChronology parisChronology = CopticChronology.getInstance(PARIS);
        Chronology defaultZoneChronology = parisChronology.withZone(null);
        assertEquals(DateTimeZone.getDefault(), defaultZoneChronology.getZone());
    }

    // --- Calendar Calculation Logic ---

    @Test
    public void calculateFirstDayOfYearMillis_forYear1_isCorrect() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 1 starts on 284-08-29 (Julian).
        long expectedMillis = new DateTime(284, 8, 29, 0, 0, JulianChronology.getInstanceUTC()).getMillis();
        assertEquals(expectedMillis, chronology.calculateFirstDayOfYearMillis(1));
    }
    
    @Test
    public void calculateFirstDayOfYearMillis_forLeapYear_isCorrect() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 3 is a leap year. Year 4 starts after 366 days.
        long year3Start = chronology.calculateFirstDayOfYearMillis(3);
        long year4Start = chronology.calculateFirstDayOfYearMillis(4);
        
        long expectedDuration = 366L * 24 * 60 * 60 * 1000;
        assertEquals(expectedDuration, year4Start - year3Start);
    }

    @Test
    public void isLeapDay_onLeapDayOfLeapYear_returnsTrue() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 3 is a leap year (3 % 4 == 3). The leap day is the 6th day of the 13th month.
        DateTime leapDay = new DateTime(3, 13, 6, 0, 0, chronology);
        assertTrue(chronology.isLeapDay(leapDay.getMillis()));
    }

    @Test
    public void isLeapDay_onNonLeapDayOfLeapYear_returnsFalse() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 3 is a leap year, but the 5th day of the 13th month is not the leap day.
        DateTime nonLeapDay = new DateTime(3, 13, 5, 0, 0, chronology);
        assertFalse(chronology.isLeapDay(nonLeapDay.getMillis()));
    }

    @Test
    public void isLeapDay_onLastDayOfNonLeapYear_returnsFalse() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 1 is not a leap year. Its last day is the 5th day of the 13th month.
        DateTime lastDay = new DateTime(1, 13, 5, 0, 0, chronology);
        assertFalse(chronology.isLeapDay(lastDay.getMillis()));
    }

    @Test(expected = IllegalFieldValueException.class)
    public void dayOfMonth_for6thEpagomenalDayInNonLeapYear_throwsException() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // Coptic year 1 is not a leap year, so it has no 6th day in the 13th month.
        new DateTime(1, 13, 6, 0, 0, chronology);
    }

    // --- Boundary and Constant Value Tests ---

    @Test
    public void getMinAndMaxYear_returnCorrectConstants() {
        CopticChronology chronology = CopticChronology.getInstance();
        assertEquals(-292269337, chronology.getMinYear());
        assertEquals(292272708, chronology.getMaxYear());
    }

    @Test
    public void getApproxMillisAtEpochDividedByTwo_returnsCorrectValue() {
        CopticChronology chronology = CopticChronology.getInstance();
        assertEquals(26607895200000L, chronology.getApproxMillisAtEpochDividedByTwo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void isLeapDay_forInstantBelowMinimum_throwsIllegalArgumentException() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        // The minimum supported instant is the first day of Coptic year 1.
        long firstDayYear1 = chronology.calculateFirstDayOfYearMillis(1);
        chronology.isLeapDay(firstDayYear1 - 1);
    }
}