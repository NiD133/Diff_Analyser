/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.ERAS;
import static java.time.temporal.ChronoUnit.MILLENNIA;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for {@link JulianChronology} and {@link JulianDate}.
 */
public class TestJulianChronology {

    //-----------------------------------------------------------------------
    // Chronology Lookup Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_chronologyLookup_byName_shouldReturnJulianChronology() {
        Chronology chrono = Chronology.of("Julian");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(JulianChronology.INSTANCE, chrono, "Chronology should be JulianChronology");
        assertEquals("Julian", chrono.getId(), "Chronology ID should be 'Julian'");
        assertEquals("julian", chrono.getCalendarType(), "Calendar type should be 'julian'");
    }

    @Test
    public void test_chronologyLookup_byNameId_shouldReturnJulianChronology() {
        Chronology chrono = Chronology.of("julian");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(JulianChronology.INSTANCE, chrono, "Chronology should be JulianChronology");
        assertEquals("Julian", chrono.getId(), "Chronology ID should be 'Julian'");
        assertEquals("julian", chrono.getCalendarType(), "Calendar type should be 'julian'");
    }

    //-----------------------------------------------------------------------
    // Date Conversion Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for Julian to ISO date conversions.
     * Each entry contains a JulianDate and its corresponding ISO LocalDate.
     */
    public static Object[][] julianToIsoSamples() {
        return new Object[][] {
            // Julian Year 1 starts on ISO date 0000-12-30
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},

            // Regular month boundaries
            {JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)},
            {JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)},
            {JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)},
            {JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)},

            // Leap year handling (year 4 is leap)
            {JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)},
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)},
            {JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)},

            // Century year (non-leap in Julian but leap in Gregorian)
            {JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)},
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            {JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)},
            {JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)},

            // Year 0 (1 BC) handling
            {JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)},
            {JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)},

            // Gregorian cutover dates
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},

            // Modern dates
            {JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)},
            {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)},
        };
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_conversion_fromJulianDate_toIsoLocalDate(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian), "Conversion from JulianDate to LocalDate failed");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_conversion_fromIsoLocalDate_toJulianDate(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JulianDate.from(iso), "Conversion from LocalDate to JulianDate failed");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_epochDayConversion_usingDateEpochDay(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()),
            "Epoch day conversion failed for JulianChronology.dateEpochDay");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_epochDayConversion_usingToEpochDay(JulianDate julian, LocalDate iso) {
        assertEquals(iso.toEpochDay(), julian.toEpochDay(),
            "Epoch day value mismatch between JulianDate and LocalDate");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_periodCalculation_sameDate_shouldBeZero(JulianDate julian, LocalDate iso) {
        assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian),
            "Period between same JulianDate should be zero");
        assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso),
            "Period between JulianDate and equivalent LocalDate should be zero");
        assertEquals(Period.ZERO, iso.until(julian),
            "Period between LocalDate and equivalent JulianDate should be zero");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_chronologyDateCreation_fromTemporal(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JulianChronology.INSTANCE.date(iso),
            "JulianChronology.date() should create correct JulianDate from Temporal");
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_dayArithmetic_plusDays(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)),
            "Adding zero days should yield same date");
        assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)),
            "Adding one day failed");
        assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)),
            "Adding 35 days failed");
        assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)),
            "Subtracting one day (plus negative) failed");
        assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)),
            "Subtracting 60 days (plus negative) failed");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_dayArithmetic_minusDays(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)),
            "Subtracting zero days should yield same date");
        assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)),
            "Subtracting one day failed");
        assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)),
            "Subtracting 35 days failed");
        assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)),
            "Adding one day (minus negative) failed");
        assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)),
            "Adding 60 days (minus negative) failed");
    }

    @ParameterizedTest
    @MethodSource("julianToIsoSamples")
    public void test_dayDifferenceCalculation(JulianDate julian, LocalDate iso) {
        assertEquals(0, julian.until(iso.plusDays(0), DAYS),
            "Days until same date should be zero");
        assertEquals(1, julian.until(iso.plusDays(1), DAYS),
            "Days until next day should be one");
        assertEquals(35, julian.until(iso.plusDays(35), DAYS),
            "Days until 35 days later should be 35");
        assertEquals(-40, julian.until(iso.minusDays(40), DAYS),
            "Days until 40 days earlier should be -40");
    }

    //-----------------------------------------------------------------------
    // Invalid Date Handling Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for invalid Julian dates.
     * Each entry contains {year, month, day-of-month} that should be invalid.
     */
    public static Object[][] invalidJulianDates() {
        return new Object[][] {
            // Invalid month values
            {1900, 0, 0},
            {1900, -1, 1},
            {1900, 0, 1},
            {1900, 13, 1},
            {1900, 14, 1},

            // Invalid day-of-month values (general)
            {1900, 1, -1},
            {1900, 1, 0},
            {1900, 1, 32},

            // February invalid days (non-leap year)
            {1900, 2, -1},
            {1900, 2, 0},
            {1900, 2, 30},  // 1900 is not leap in Julian calendar
            {1900, 2, 31},
            {1900, 2, 32},

            // February invalid days (leap year 1899? Note: 1899 mod 4 = 3 -> not leap)
            {1899, 2, -1},
            {1899, 2, 0},
            {1899, 2, 29},  // 1899 is not leap
            {1899, 2, 30},
            {1899, 2, 31},
            {1899, 2, 32},

            // December invalid days
            {1900, 12, -1},
            {1900, 12, 0},
            {1900, 12, 32},

            // Months with 30 days (invalid 31st)
            {1900, 4, 31},
            {1900, 6, 31},
            {1900, 9, 31},
            {1900, 11, 31},

            // Months with 31 days (invalid 32nd)
            {1900, 3, 32},
            {1900, 5, 32},
            {1900, 7, 32},
            {1900, 8, 32},
            {1900, 10, 32},
            {1900, 12, 32},
        };
    }

    @ParameterizedTest
    @MethodSource("invalidJulianDates")
    public void test_invalidDateCreation_shouldThrowException(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom),
            "Creating invalid Julian date should throw DateTimeException");
    }

    @Test
    public void test_invalidDateYearDayCreation_shouldThrowException() {
        // 2001 is not a leap year (2001 % 4 = 1), so day 366 is invalid
        assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.dateYearDay(2001, 366),
            "Creating Julian date with invalid day-of-year should throw DateTimeException");
    }

    //-----------------------------------------------------------------------
    // Leap Year Handling Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_leapYearDetection_usingLoop() {
        // Verify leap year rule: divisible by 4
        for (int year = -200; year < 200; year++) {
            JulianDate base = JulianDate.of(year, 1, 1);
            boolean expectedLeap = (year % 4) == 0;
            assertEquals(expectedLeap, base.isLeapYear(),
                "Year " + year + " should " + (expectedLeap ? "" : "not ") + "be leap");
            assertEquals(expectedLeap, JulianChronology.INSTANCE.isLeapYear(year),
                "Year " + year + " should " + (expectedLeap ? "" : "not ") + "be leap (via Chronology)");
        }
    }

    @Test
    public void test_leapYearDetection_specificCases() {
        // Positive leap years
        assertEquals(true, JulianChronology.INSTANCE.isLeapYear(8), "Year 8 should be leap");
        assertEquals(true, JulianChronology.INSTANCE.isLeapYear(4), "Year 4 should be leap");
        assertEquals(true, JulianChronology.INSTANCE.isLeapYear(0), "Year 0 should be leap");
        assertEquals(true, JulianChronology.INSTANCE.isLeapYear(-4), "Year -4 should be leap");

        // Non-leap years
        assertEquals(false, JulianChronology.INSTANCE.isLeapYear(7), "Year 7 should not be leap");
        assertEquals(false, JulianChronology.INSTANCE.isLeapYear(3), "Year 3 should not be leap");
        assertEquals(false, JulianChronology.INSTANCE.isLeapYear(1), "Year 1 should not be leap");
        assertEquals(false, JulianChronology.INSTANCE.isLeapYear(-1), "Year -1 should not be leap");
        assertEquals(false, JulianChronology.INSTANCE.isLeapYear(-5), "Year -5 should not be leap");
    }

    /**
     * Test data for month lengths in Julian calendar.
     * Format: {year, month, expectedLength}
     */
    public static Object[][] monthLengths() {
        return new Object[][] {
            // Common year months
            {1900, 1, 31},
            {1900, 2, 29},  // 1900 is leap in Julian (divisible by 4)
            {1900, 3, 31},
            {1900, 4, 30},
            {1900, 5, 31},
            {1900, 6, 30},
            {1900, 7, 31},
            {1900, 8, 31},
            {1900, 9, 30},
            {1900, 10, 31},
            {1900, 11, 30},
            {1900, 12, 31},

            // February in non-leap years
            {1901, 2, 28},
            {1902, 2, 28},
            {1903, 2, 28},
            
            // February in leap years
            {1904, 2, 29},
            {2000, 2, 29},
            {2100, 2, 29},  // 2100 is leap in Julian (divisible by 4)
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengths")
    public void test_monthLengthCalculation(int year, int month, int length) {
        assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth(),
            "Month length mismatch for year=" + year + ", month=" + month);
    }

    //-----------------------------------------------------------------------
    // Era and Year Calculation Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_eraAndYearFields_usingLoop() {
        for (int year = -200; year < 200; year++) {
            JulianDate base = JulianChronology.INSTANCE.date(year, 1, 1);
            
            // Verify proleptic year
            assertEquals(year, base.get(YEAR), "Proleptic year mismatch for " + year);
            
            // Verify era
            JulianEra expectedEra = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(expectedEra, base.getEra(), "Era mismatch for year " + year);
            
            // Verify year-of-era
            int expectedYearOfEra = (year <= 0 ? 1 - year : year);
            assertEquals(expectedYearOfEra, base.get(YEAR_OF_ERA), "Year-of-era mismatch for year " + year);
            
            // Verify era-based date creation
            JulianDate eraBased = JulianChronology.INSTANCE.date(expectedEra, expectedYearOfEra, 1, 1);
            assertEquals(base, eraBased, "Era-based date should match proleptic date for year " + year);
        }
    }

    @Test
    public void test_eraAndYearFields_yearDay_usingLoop() {
        for (int year = -200; year < 200; year++) {
            JulianDate base = JulianChronology.INSTANCE.dateYearDay(year, 1);
            
            // Verify proleptic year
            assertEquals(year, base.get(YEAR), "Proleptic year mismatch for " + year);
            
            // Verify era
            JulianEra expectedEra = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(expectedEra, base.getEra(), "Era mismatch for year " + year);
            
            // Verify year-of-era
            int expectedYearOfEra = (year <= 0 ? 1 - year : year);
            assertEquals(expectedYearOfEra, base.get(YEAR_OF_ERA), "Year-of-era mismatch for year " + year);
            
            // Verify era-based date creation
            JulianDate eraBased = JulianChronology.INSTANCE.dateYearDay(expectedEra, expectedYearOfEra, 1);
            assertEquals(base, eraBased, "Era-based date should match proleptic date for year " + year);
        }
    }

    @Test
    public void test_prolepticYearCalculation() {
        assertEquals(4, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 4));
        assertEquals(3, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 3));
        assertEquals(2, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 2));
        assertEquals(1, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 1));
        assertEquals(0, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 1));
        assertEquals(-1, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 2));
        assertEquals(-2, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 3));
        assertEquals(-3, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 4));
    }

    @Test
    public void test_prolepticYearCalculation_withInvalidEra_shouldThrowException() {
        assertThrows(ClassCastException.class, 
            () -> JulianChronology.INSTANCE.prolepticYear(IsoEra.CE, 4),
            "Using ISO era with Julian chronology should throw ClassCastException");
    }

    @Test
    public void test_eraLookup() {
        assertEquals(JulianEra.AD, JulianChronology.INSTANCE.eraOf(1));
        assertEquals(JulianEra.BC, JulianChronology.INSTANCE.eraOf(0));
    }

    @Test
    public void test_eraLookup_withInvalidValue_shouldThrowException() {
        assertThrows(DateTimeException.class, 
            () -> JulianChronology.INSTANCE.eraOf(2),
            "Invalid era value should throw DateTimeException");
    }

    @Test
    public void test_availableEras() {
        List<Era> eras = JulianChronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "Should have two eras");
        assertEquals(true, eras.contains(JulianEra.BC), "Should contain BC era");
        assertEquals(true, eras.contains(JulianEra.AD), "Should contain AD era");
    }

    //-----------------------------------------------------------------------
    // Field Range Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_chronologyFieldRanges() {
        assertEquals(ValueRange.of(1, 7), JulianChronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), JulianChronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), JulianChronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), JulianChronology.INSTANCE.range(MONTH_OF_YEAR));
    }

    /**
     * Test data for JulianDate.range().
     * Format: {year, month, day, field, expectedMin, expectedMax}
     */
    public static Object[][] fieldRanges() {
        return new Object[][] {
            // 2012 is leap year
            {2012, 1, 23, DAY_OF_MONTH, 1, 31},
            {2012, 2, 23, DAY_OF_MONTH, 1, 29},  // February in leap year
            {2012, 3, 23, DAY_OF_MONTH, 1, 31},
            {2012, 4, 23, DAY_OF_MONTH, 1, 30},
            {2012, 5, 23, DAY_OF_MONTH, 1, 31},
            {2012, 6, 23, DAY_OF_MONTH, 1, 30},
            {2012, 7, 23, DAY_OF_MONTH, 1, 31},
            {2012, 8, 23, DAY_OF_MONTH, 1, 31},
            {2012, 9, 23, DAY_OF_MONTH, 1, 30},
            {2012, 10, 23, DAY_OF_MONTH, 1, 31},
            {2012, 11, 23, DAY_OF_MONTH, 1, 30},
            {2012, 12, 23, DAY_OF_MONTH, 1, 31},
            {2012, 1, 23, DAY_OF_YEAR, 1, 366},  // Leap year
            
            // Aligned week fields
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2012, 3, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},

            // 2011 is not leap
            {2011, 2, 23, DAY_OF_MONTH, 1, 28},  // February in non-leap year
            {2011, 2, 23, DAY_OF_YEAR, 1, 365},  // Non-leap year
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},  // February with 28 days
        };
    }

    @ParameterizedTest
    @MethodSource("fieldRanges")
    public void test_dateFieldRanges(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
        ValueRange range = JulianDate.of(year, month, dom).range(field);
        assertEquals(ValueRange.of(expectedMin, expectedMax), range,
            "Field range mismatch for " + field + " at " + year + "-" + month + "-" + dom);
    }

    @Test
    public void test_unsupportedFieldRange_shouldThrowException() {
        JulianDate date = JulianDate.of(2012, 6, 30);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.range(MINUTE_OF_DAY),
            "MINUTE_OF_DAY should be unsupported for JulianDate");
    }

    //-----------------------------------------------------------------------
    // Field Value Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for JulianDate.getLong().
     * Format: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValues() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},  // Monday (ISO Sunday=7)
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},  // Jan(31) + Feb(28) + Mar(31) + Apr(30) + May(26)
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},  // 26th is 5th aligned day (Mon-Sun alignment)
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},  // Fourth aligned week
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6},  // Sixth aligned day in year
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},  // 21st aligned week
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},  // Months since epoch
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},  // AD
            {1, 6, 8, ERA, 1},      // AD
            {0, 6, 8, ERA, 0},      // BC

            // WeekFields integration
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},  // Monday (ISO Sunday=7)
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValues")
    public void test_fieldValueRetrieval(int year, int month, int dom, TemporalField field, long expected) {
        long value = JulianDate.of(year, month, dom).getLong(field);
        assertEquals(expected, value, 
            "Field value mismatch for " + field + " at " + year + "-" + month + "-" + dom);
    }

    @Test
    public void test_unsupportedFieldValue_shouldThrowException() {
        JulianDate date = JulianDate.of(2012, 6, 30);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.getLong(MINUTE_OF_DAY),
            "MINUTE_OF_DAY should be unsupported for JulianDate");
    }

    //-----------------------------------------------------------------------
    // Date Manipulation Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for JulianDate.with().
     * Format: {originalYear, originalMonth, originalDay, field, newValue, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] dateAdjustments() {
        return new Object[][] {
            // Day adjustments
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},  // Set to 3rd day of week (Wednesday)
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            
            // Aligned week adjustments
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 22},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            
            // Month adjustments
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28},  // Non-leap year
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29},  // Leap year
            {2012, 3, 31, MONTH_OF_YEAR, 6, 2012, 6, 30},  // June has 30 days
            
            // Year adjustments
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28},  // 2011 not leap
            
            // Proleptic month adjustments
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            
            // Era adjustments
            {2014, 5, 26, ERA, 0, -2013, 5, 26},  // Switch to BC
            {2014, 5, 26, ERA, 1, 2014, 5, 26},   // Stay in AD
            
            // Year-of-era adjustments
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {-2013, 6, 8, YEAR_OF_ERA, 2012, -2011, 6, 8}, // BC: yearOfEra 2012 = proleptic year -2011
            
            // WeekFields integration
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 3, 2014, 5, 22},  // Set to Wednesday (ISO)
        };
    }

    @ParameterizedTest
    @MethodSource("dateAdjustments")
    public void test_dateAdjustment_withTemporalField(
            int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
        JulianDate original = JulianDate.of(year, month, dom);
        JulianDate adjusted = original.with(field, value);
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom), adjusted,
            "Date adjustment with " + field + " failed");
    }

    @Test
    public void test_unsupportedDateAdjustment_shouldThrowException() {
        JulianDate date = JulianDate.of(2012, 6, 30);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.with(MINUTE_OF_DAY, 0),
            "Adjusting MINUTE_OF_DAY should be unsupported");
    }

    //-----------------------------------------------------------------------
    // TemporalAdjuster Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_temporalAdjuster_lastDayOfMonth_nonLeapFebruary() {
        JulianDate base = JulianDate.of(2011, 2, 23);  // 2011 not leap
        JulianDate adjusted = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(JulianDate.of(2011, 2, 28), adjusted,
            "Last day of February in non-leap year should be 28");
    }

    @Test
    public void test_temporalAdjuster_lastDayOfMonth_leapFebruary() {
        JulianDate base = JulianDate.of(2012, 2, 23);  // 2012 is leap
        JulianDate adjusted = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(JulianDate.of(2012, 2, 29), adjusted,
            "Last day of February in leap year should be 29");
    }

    @Test
    public void test_temporalAdjuster_lastDayOfMonth_regularMonth() {
        JulianDate base = JulianDate.of(2012, 6, 23);
        JulianDate adjusted = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(JulianDate.of(2012, 6, 30), adjusted,
            "Last day of June should be 30");
    }

    @Test
    public void test_adjustToLocalDate() {
        JulianDate julian = JulianDate.of(2000, 1, 4);
        JulianDate adjusted = julian.with(LocalDate.of(2012, 7, 6));
        assertEquals(JulianDate.of(2012, 6, 23), adjusted,
            "Adjusting to LocalDate should convert correctly");
    }

    @Test
    public void test_adjustToMonth_shouldThrowException() {
        JulianDate julian = JulianDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class, 
            () -> julian.with(Month.APRIL),
            "Adjusting to Month should throw since it requires conversion");
    }

    //-----------------------------------------------------------------------
    // Conversion Integration Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_localDateAdjustToJulianDate() {
        JulianDate julian = JulianDate.of(2012, 6, 23);
        LocalDate adjusted = LocalDate.MIN.with(julian);
        assertEquals(LocalDate.of(2012, 7, 6), adjusted,
            "LocalDate adjustment to JulianDate should convert correctly");
    }

    @Test
    public void test_localDateTimeAdjustToJulianDate() {
        JulianDate julian = JulianDate.of(2012, 6, 23);
        LocalDateTime adjusted = LocalDateTime.MIN.with(julian);
        assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), adjusted,
            "LocalDateTime adjustment to JulianDate should convert correctly");
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic (Plus/Minus) Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for date arithmetic.
     * Format: {year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] dateArithmetic() {
        return new Object[][] {
            // Days
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            
            // Weeks
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 21},
            
            // Months
            {2014, 5, 26, 0, MONTHS, 2014, 5, 26},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
            
            // Years
            {2014, 5, 26, 0, YEARS, 2014, 5, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},
            
            // Decades
            {2014, 5, 26, 0, DECADES, 2014, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, -5, DECADES, 1964, 5, 26},
            
            // Centuries
            {2014, 5, 26, 0, CENTURIES, 2014, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},
            
            // Millennia
            {2014, 5, 26, 0, MILLENNIA, 2014, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -5, MILLENNIA, 2014 - 5000, 5, 26},
            
            // Eras
            {2014, 5, 26, -1, ERAS, -2013, 5, 26},  // Switch from AD to BC
        };
    }

    @ParameterizedTest
    @MethodSource("dateArithmetic")
    public void test_dateArithmetic_plusTemporalUnit(
            int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
        JulianDate base = JulianDate.of(year, month, dom);
        JulianDate result = base.plus(amount, unit);
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom), result,
            "Adding " + amount + " " + unit + " failed");
    }

    @ParameterizedTest
    @MethodSource("dateArithmetic")
    public void test_dateArithmetic_minusTemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
        JulianDate base = JulianDate.of(year, month, dom);
        JulianDate result = base.minus(amount, unit);
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom), result,
            "Subtracting " + amount + " " + unit + " failed");
    }

    @Test
    public void test_unsupportedTemporalUnit_shouldThrowException() {
        JulianDate date = JulianDate.of(2012, 6, 30);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.plus(0, MINUTES),
            "Adding MINUTES should be unsupported");
    }

    //-----------------------------------------------------------------------
    // Period Calculation Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for period calculations.
     * Format: {year1, month1, dom1, year2, month2, dom2, unit, expected}
     */
    public static Object[][] periodCalculations() {
        return new Object[][] {
            // Days
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            
            // Weeks
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 1, WEEKS, 0},   // 6 days is less than a week
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},   // Exactly 7 days
            
            // Months
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 25, MONTHS, 0}, // Not full month
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, // Exactly one month
            
            // Years
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            {2014, 5, 26, 2015, 5, 25, YEARS, 0},  // Not full year
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},  // Exactly one year
            
            // Decades
            {2014, 5, 26, 2014, 5, 26, DECADES, 0},
            {2014, 5, 26, 2024, 5, 25, DECADES, 0}, // Not full decade
            {2014, 5, 26, 2024, 5, 26, DECADES, 1}, // Exactly one decade
            
            // Eras
            {-2013, 5, 26, 0, 5, 26, ERAS, 0},     // Same era (BC)
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},  // Different era (BC to AD)
        };
    }

    @ParameterizedTest
    @MethodSource("periodCalculations")
    public void test_periodCalculation_betweenDates(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
        JulianDate start = JulianDate.of(year1, month1, dom1);
        JulianDate end = JulianDate.of(year2, month2, dom2);
        long result = start.until(end, unit);
        assertEquals(expected, result,
            "Period between " + start + " and " + end + " in " + unit + " should be " + expected);
    }

    @Test
    public void test_unsupportedPeriodUnit_shouldThrowException() {
        JulianDate start = JulianDate.of(2012, 6, 30);
        JulianDate end = JulianDate.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> start.until(end, MINUTES),
            "Period calculation in MINUTES should be unsupported");
    }

    //-----------------------------------------------------------------------
    // Period Arithmetic Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_periodArithmetic_plusJulianPeriod() {
        JulianDate start = JulianDate.of(2014, 5, 26);
        JulianDate result = start.plus(JulianChronology.INSTANCE.period(0, 2, 3));
        assertEquals(JulianDate.of(2014, 7, 29), result,
            "Adding 2 months and 3 days should yield correct date");
    }

    @Test
    public void test_periodArithmetic_plusIsoPeriod_shouldThrowException() {
        JulianDate start = JulianDate.of(2014, 5, 26);
        assertThrows(DateTimeException.class, 
            () -> start.plus(Period.ofMonths(2)),
            "Adding ISO period to JulianDate should throw");
    }

    @Test
    public void test_periodArithmetic_minusJulianPeriod() {
        JulianDate start = JulianDate.of(2014, 5, 26);
        JulianDate result = start.minus(JulianChronology.INSTANCE.period(0, 2, 3));
        assertEquals(JulianDate.of(2014, 3, 23), result,
            "Subtracting 2 months and 3 days should yield correct date");
    }

    @Test
    public void test_periodArithmetic_minusIsoPeriod_shouldThrowException() {
        JulianDate start = JulianDate.of(2014, 5, 26);
        assertThrows(DateTimeException.class, 
            () -> start.minus(Period.ofMonths(2)),
            "Subtracting ISO period from JulianDate should throw");
    }

    //-----------------------------------------------------------------------
    // Object Contract Tests
    //-----------------------------------------------------------------------

    @Test
    public void test_equalsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(JulianDate.of(2000, 1, 3), JulianDate.of(2000, 1, 3))
            .addEqualityGroup(JulianDate.of(2000, 1, 4))
            .addEqualityGroup(JulianDate.of(2000, 2, 3))
            .addEqualityGroup(JulianDate.of(2001, 1, 3))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------

    public static Object[][] stringRepresentations() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
            {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("stringRepresentations")
    public void test_toString(JulianDate julian, String expected) {
        assertEquals(expected, julian.toString(),
            "String representation should match expected format");
    }
}