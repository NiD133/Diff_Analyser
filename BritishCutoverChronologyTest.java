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

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for BritishCutoverChronology - a calendar system that switches from Julian to Gregorian
 * calendar on September 14, 1752, causing an 11-day gap (Sept 3-13, 1752 never existed).
 */
public class TestBritishCutoverChronology {

    // Test constants for readability
    private static final int CUTOVER_YEAR = 1752;
    private static final int CUTOVER_MONTH = 9;
    private static final int CUTOVER_DAY = 14;
    private static final LocalDate BRITISH_CUTOVER_DATE = LocalDate.of(CUTOVER_YEAR, CUTOVER_MONTH, CUTOVER_DAY);

    //-----------------------------------------------------------------------
    // Chronology identification tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldReturnCorrectChronologyWhenLookingUpByName() {
        // When looking up chronology by name
        Chronology chronology = Chronology.of("BritishCutover");
        
        // Then it should return the correct instance with proper identification
        assertNotNull(chronology);
        assertEquals(BritishCutoverChronology.INSTANCE, chronology);
        assertEquals("BritishCutover", chronology.getId());
        assertEquals(null, chronology.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Date conversion tests between British Cutover and ISO calendars
    //-----------------------------------------------------------------------
    
    /**
     * Test data showing how British Cutover dates map to ISO dates.
     * Key test cases include:
     * - Early dates (year 1-4) showing 2-day offset due to missing leap years
     * - Year 100 showing 1-day offset (Julian treats 100 as leap year, Gregorian doesn't)
     * - 1582 showing 10-day Gregorian cutover
     * - 1752 showing British cutover with 11-day gap
     * - Modern dates (post-1752) showing no offset
     */
    public static Object[][] dateConversionTestData() {
        return new Object[][] {
            // Early years - Julian calendar has different leap year rules
            {britishDate(1, 1, 1), isoDate(0, 12, 30)},
            {britishDate(1, 1, 2), isoDate(0, 12, 31)},
            {britishDate(1, 1, 3), isoDate(1, 1, 1)},

            // February/March boundary in leap years
            {britishDate(4, 2, 28), isoDate(4, 2, 26)},
            {britishDate(4, 2, 29), isoDate(4, 2, 27)},
            {britishDate(4, 3, 1), isoDate(4, 2, 28)},

            // Century year differences (Julian vs Gregorian leap year rules)
            {britishDate(100, 2, 28), isoDate(100, 2, 26)},
            {britishDate(100, 2, 29), isoDate(100, 2, 27)},
            {britishDate(100, 3, 1), isoDate(100, 2, 28)},

            // Around the British cutover (September 1752)
            {britishDate(1752, 9, 1), isoDate(1752, 9, 12)},
            {britishDate(1752, 9, 2), isoDate(1752, 9, 13)},
            {britishDate(1752, 9, 3), isoDate(1752, 9, 14)},  // Lenient: invalid date accepted
            {britishDate(1752, 9, 14), isoDate(1752, 9, 14)}, // First valid Gregorian date

            // Modern dates (post-cutover) - should match exactly
            {britishDate(2012, 7, 5), isoDate(2012, 7, 5)},
            {britishDate(2012, 7, 6), isoDate(2012, 7, 6)},
        };
    }

    @ParameterizedTest
    @MethodSource("dateConversionTestData")
    public void shouldConvertBritishCutoverDateToIsoDate(BritishCutoverDate britishDate, LocalDate expectedIsoDate) {
        LocalDate actualIsoDate = LocalDate.from(britishDate);
        assertEquals(expectedIsoDate, actualIsoDate);
    }

    @ParameterizedTest
    @MethodSource("dateConversionTestData")
    public void shouldConvertIsoDateToBritishCutoverDate(BritishCutoverDate expectedBritishDate, LocalDate isoDate) {
        BritishCutoverDate actualBritishDate = BritishCutoverDate.from(isoDate);
        assertEquals(expectedBritishDate, actualBritishDate);
    }

    @ParameterizedTest
    @MethodSource("dateConversionTestData")
    public void shouldCreateBritishDateFromEpochDay(BritishCutoverDate expectedBritishDate, LocalDate isoDate) {
        BritishCutoverDate actualBritishDate = BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay());
        assertEquals(expectedBritishDate, actualBritishDate);
    }

    //-----------------------------------------------------------------------
    // Date arithmetic tests
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("dateConversionTestData")
    public void shouldHandleDayArithmeticCorrectly(BritishCutoverDate britishDate, LocalDate isoDate) {
        // Test adding/subtracting days
        assertEquals(isoDate.plusDays(1), LocalDate.from(britishDate.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(britishDate.plus(35, DAYS)));
        assertEquals(isoDate.minusDays(1), LocalDate.from(britishDate.minus(1, DAYS)));
        assertEquals(isoDate.minusDays(60), LocalDate.from(britishDate.minus(60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("dateConversionTestData")
    public void shouldCalculateDaysBetweenDatesCorrectly(BritishCutoverDate britishDate, LocalDate isoDate) {
        assertEquals(0, britishDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, britishDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, britishDate.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, britishDate.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // Invalid date tests
    //-----------------------------------------------------------------------

    public static Object[][] invalidDateTestData() {
        return new Object[][] {
            // Invalid months
            {1900, 0, 1},   // Month 0
            {1900, 13, 1},  // Month 13
            
            // Invalid days
            {1900, 1, 0},   // Day 0
            {1900, 1, 32},  // Day 32 in January
            {1900, 2, 30},  // Day 30 in February
            {1900, 4, 31},  // Day 31 in April (30-day month)
            
            // February edge cases
            {1899, 2, 29},  // Feb 29 in non-leap year
        };
    }

    @ParameterizedTest
    @MethodSource("invalidDateTestData")
    public void shouldRejectInvalidDates(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, 
            () -> BritishCutoverDate.of(year, month, dayOfMonth),
            String.format("Should reject invalid date %d-%d-%d", year, month, dayOfMonth));
    }

    @Test
    public void shouldRejectInvalidDayOfYear() {
        assertThrows(DateTimeException.class, 
            () -> BritishCutoverChronology.INSTANCE.dateYearDay(2001, 366),
            "Should reject day 366 in non-leap year");
    }

    //-----------------------------------------------------------------------
    // Leap year tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldIdentifyLeapYearsCorrectlyInJulianPeriod() {
        // Julian calendar: every 4th year is a leap year (simple rule)
        for (int year = -200; year < CUTOVER_YEAR; year++) {
            boolean expectedLeapYear = (year % 4) == 0;
            assertEquals(expectedLeapYear, BritishCutoverChronology.INSTANCE.isLeapYear(year),
                String.format("Year %d leap year calculation incorrect", year));
        }
    }

    @Test
    public void shouldIdentifySpecificLeapYearsCorrectly() {
        // Test specific years around year 0
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(4));
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(3));
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(0));
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-1));
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(-4));
    }

    //-----------------------------------------------------------------------
    // Cutover date tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldReturnCorrectCutoverDate() {
        assertEquals(BRITISH_CUTOVER_DATE, BritishCutoverChronology.INSTANCE.getCutover());
    }

    //-----------------------------------------------------------------------
    // Month length tests
    //-----------------------------------------------------------------------

    public static Object[][] monthLengthTestData() {
        return new Object[][] {
            // Regular year
            {1751, 1, 31}, {1751, 2, 28}, {1751, 3, 31}, {1751, 4, 30},
            {1751, 5, 31}, {1751, 6, 30}, {1751, 7, 31}, {1751, 8, 31},
            {1751, 9, 30}, {1751, 10, 31}, {1751, 11, 30}, {1751, 12, 31},

            // Cutover year 1752 - September has only 19 days due to 11-day gap
            {1752, 1, 31}, {1752, 2, 29}, {1752, 3, 31}, {1752, 4, 30},
            {1752, 5, 31}, {1752, 6, 30}, {1752, 7, 31}, {1752, 8, 31},
            {1752, 9, 19}, // Special case: only 19 days due to cutover gap
            {1752, 10, 31}, {1752, 11, 30}, {1752, 12, 31},

            // Post-cutover year
            {1753, 1, 31}, {1753, 2, 28}, {1753, 3, 31}, {1753, 4, 30},

            // Leap year tests
            {1700, 2, 29}, // Julian leap year
            {1800, 2, 28}, // Not a Gregorian leap year
            {2000, 2, 29}, // Gregorian leap year
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengthTestData")
    public void shouldReturnCorrectMonthLength(int year, int month, int expectedLength) {
        int actualLength = BritishCutoverDate.of(year, month, 1).lengthOfMonth();
        assertEquals(expectedLength, actualLength,
            String.format("Month length for %d-%d should be %d", year, month, expectedLength));
    }

    //-----------------------------------------------------------------------
    // Year length tests
    //-----------------------------------------------------------------------

    public static Object[][] yearLengthTestData() {
        return new Object[][] {
            // Regular years
            {1751, 365},
            {1753, 365},
            
            // Leap years
            {1748, 366},
            {1700, 366}, // Julian leap year
            {2000, 366}, // Gregorian leap year
            
            // Non-leap years under Gregorian rules
            {1800, 365}, // Century year, not divisible by 400
            {1900, 365},
            
            // The special cutover year with 11-day gap
            {1752, 355}, // 366 - 11 = 355 days
        };
    }

    @ParameterizedTest
    @MethodSource("yearLengthTestData")
    public void shouldReturnCorrectYearLength(int year, int expectedLength) {
        int actualLength = BritishCutoverDate.of(year, 1, 1).lengthOfYear();
        assertEquals(expectedLength, actualLength,
            String.format("Year %d should have %d days", year, expectedLength));
    }

    //-----------------------------------------------------------------------
    // Era and year-of-era tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldHandleErasCorrectly() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate date = BritishCutoverChronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, date.get(YEAR));
            
            JulianEra expectedEra = (year <= 0) ? JulianEra.BC : JulianEra.AD;
            assertEquals(expectedEra, date.getEra());
            
            int expectedYearOfEra = (year <= 0) ? 1 - year : year;
            assertEquals(expectedYearOfEra, date.get(YEAR_OF_ERA));
            
            // Test creating date from era and year-of-era
            BritishCutoverDate eraBasedDate = BritishCutoverChronology.INSTANCE.date(expectedEra, expectedYearOfEra, 1, 1);
            assertEquals(date, eraBasedDate);
        }
    }

    @Test
    public void shouldCreateDateFromYearDay() {
        // Test specific year-day combinations around cutover
        assertEquals(britishDate(1752, 1, 1), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 1));
        assertEquals(britishDate(1752, 9, 2), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 246));
        assertEquals(britishDate(1752, 9, 14), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 247));
        assertEquals(britishDate(1752, 12, 31), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 355));
    }

    @Test
    public void shouldConvertProlepticYearCorrectly() {
        assertEquals(4, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 4));
        assertEquals(1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 1));
        assertEquals(0, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 1));
        assertEquals(-1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 2));
    }

    @Test
    public void shouldRejectInvalidEra() {
        assertThrows(ClassCastException.class, 
            () -> BritishCutoverChronology.INSTANCE.prolepticYear(IsoEra.CE, 4),
            "Should reject non-Julian era");
    }

    @Test
    public void shouldReturnCorrectEras() {
        List<Era> eras = BritishCutoverChronology.INSTANCE.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.BC));
        assertTrue(eras.contains(JulianEra.AD));
    }

    //-----------------------------------------------------------------------
    // Field range tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldReturnCorrectFieldRanges() {
        assertEquals(ValueRange.of(1, 7), BritishCutoverChronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), BritishCutoverChronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 355, 366), BritishCutoverChronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), BritishCutoverChronology.INSTANCE.range(MONTH_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    // Field value tests
    //-----------------------------------------------------------------------

    public static Object[][] fieldValueTestData() {
        return new Object[][] {
            // Regular date
            {2014, 5, 26, DAY_OF_WEEK, 1}, // Monday
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1}, // AD
            
            // Around cutover
            {1752, 9, 2, DAY_OF_WEEK, 3}, // Wednesday
            {1752, 9, 14, DAY_OF_WEEK, 4}, // Thursday
            
            // Era tests
            {1, 6, 8, ERA, 1}, // AD
            {0, 6, 8, ERA, 0}, // BC
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValueTestData")
    public void shouldReturnCorrectFieldValue(int year, int month, int day, TemporalField field, long expectedValue) {
        long actualValue = BritishCutoverDate.of(year, month, day).getLong(field);
        assertEquals(expectedValue, actualValue,
            String.format("Field %s for date %d-%d-%d should be %d", field, year, month, day, expectedValue));
    }

    @Test
    public void shouldRejectUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY),
            "Should reject unsupported temporal field");
    }

    //-----------------------------------------------------------------------
    // Date adjustment tests
    //-----------------------------------------------------------------------

    public static Object[][] dateAdjustmentTestData() {
        return new Object[][] {
            // Regular adjustments
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28}, // Change to Wednesday
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26}, // Change to July
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26}, // Change year
            
            // Cutover period adjustments (lenient handling)
            {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14}, // Day 3 becomes day 14 (gap)
            {1752, 9, 2, DAY_OF_MONTH, 13, 1752, 9, 24}, // Day 13 becomes day 24 (gap)
            
            // Era changes
            {2014, 5, 26, ERA, 0, -2013, 5, 26}, // AD to BC
        };
    }

    @ParameterizedTest
    @MethodSource("dateAdjustmentTestData")
    public void shouldAdjustDateFieldsCorrectly(int year, int month, int day, TemporalField field, long newValue,
                                               int expectedYear, int expectedMonth, int expectedDay) {
        BritishCutoverDate originalDate = BritishCutoverDate.of(year, month, day);
        BritishCutoverDate adjustedDate = originalDate.with(field, newValue);
        BritishCutoverDate expectedDate = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDay);
        
        assertEquals(expectedDate, adjustedDate,
            String.format("Adjusting %s to %d on date %d-%d-%d", field, newValue, year, month, day));
    }

    //-----------------------------------------------------------------------
    // Temporal adjuster tests
    //-----------------------------------------------------------------------

    public static Object[][] lastDayOfMonthTestData() {
        return new Object[][] {
            {britishDate(1752, 2, 23), britishDate(1752, 2, 29)}, // Leap year February
            {britishDate(1752, 9, 2), britishDate(1752, 9, 30)},  // Cutover month
            {britishDate(2012, 6, 23), britishDate(2012, 6, 30)}, // Regular month
        };
    }

    @ParameterizedTest
    @MethodSource("lastDayOfMonthTestData")
    public void shouldAdjustToLastDayOfMonth(BritishCutoverDate inputDate, BritishCutoverDate expectedDate) {
        BritishCutoverDate adjustedDate = inputDate.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expectedDate, adjustedDate);
    }

    //-----------------------------------------------------------------------
    // Date arithmetic with units tests
    //-----------------------------------------------------------------------

    public static Object[][] dateArithmeticTestData() {
        return new Object[][] {
            // Day arithmetic across cutover
            {1752, 9, 2, 1, DAYS, 1752, 9, 14}, // Skip the gap
            {1752, 9, 14, -1, DAYS, 1752, 9, 2}, // Skip the gap backwards
            
            // Week arithmetic
            {1752, 9, 2, 1, WEEKS, 1752, 9, 20},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            
            // Month arithmetic
            {1752, 9, 2, 1, MONTHS, 1752, 10, 2},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
            
            // Year arithmetic
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},
        };
    }

    @ParameterizedTest
    @MethodSource("dateArithmeticTestData")
    public void shouldPerformDateArithmeticCorrectly(int year, int month, int day, long amount, TemporalUnit unit,
                                                    int expectedYear, int expectedMonth, int expectedDay) {
        BritishCutoverDate originalDate = BritishCutoverDate.of(year, month, day);
        BritishCutoverDate resultDate = originalDate.plus(amount, unit);
        BritishCutoverDate expectedDate = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDay);
        
        assertEquals(expectedDate, resultDate,
            String.format("Adding %d %s to %d-%d-%d", amount, unit, year, month, day));
    }

    @Test
    public void shouldRejectUnsupportedTemporalUnit() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).plus(0, MINUTES),
            "Should reject unsupported temporal unit");
    }

    //-----------------------------------------------------------------------
    // Period calculation tests
    //-----------------------------------------------------------------------

    public static Object[][] periodCalculationTestData() {
        return new Object[][] {
            // Simple cases
            {1752, 9, 1, 1752, 9, 2, DAYS, 1},
            {1752, 9, 2, 1752, 9, 14, DAYS, 1}, // Across the gap
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            
            // Week calculations
            {1752, 9, 1, 1752, 9, 19, WEEKS, 1}, // Across cutover
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
            
            // Month calculations
            {1752, 9, 2, 1752, 10, 2, MONTHS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
        };
    }

    @ParameterizedTest
    @MethodSource("periodCalculationTestData")
    public void shouldCalculatePeriodsCorrectly(int year1, int month1, int day1, int year2, int month2, int day2,
                                               TemporalUnit unit, long expectedAmount) {
        BritishCutoverDate startDate = BritishCutoverDate.of(year1, month1, day1);
        BritishCutoverDate endDate = BritishCutoverDate.of(year2, month2, day2);
        
        long actualAmount = startDate.until(endDate, unit);
        assertEquals(expectedAmount, actualAmount,
            String.format("Period between %d-%d-%d and %d-%d-%d in %s", 
                year1, month1, day1, year2, month2, day2, unit));
    }

    //-----------------------------------------------------------------------
    // Complex period tests around cutover
    //-----------------------------------------------------------------------

    public static Object[][] cutoverPeriodTestData() {
        return new Object[][] {
            // Periods across the cutover gap
            {1752, 7, 2, 1752, 9, 2, 0, 2, 0},   // 2 months exactly
            {1752, 7, 2, 1752, 9, 14, 0, 2, 1},  // 2 months + 1 day (accounting for gap)
            {1752, 8, 2, 1752, 9, 14, 0, 1, 1},  // 1 month + 1 day (accounting for gap)
            {1752, 9, 2, 1752, 10, 2, 0, 1, 0},  // 1 month exactly
        };
    }

    @ParameterizedTest
    @MethodSource("cutoverPeriodTestData")
    public void shouldCalculateComplexPeriodsAroundCutover(int year1, int month1, int day1, int year2, int month2, int day2,
                                                          int expectedYears, int expectedMonths, int expectedDays) {
        BritishCutoverDate startDate = BritishCutoverDate.of(year1, month1, day1);
        BritishCutoverDate endDate = BritishCutoverDate.of(year2, month2, day2);
        
        ChronoPeriod period = startDate.until(endDate);
        ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
        
        assertEquals(expectedPeriod, period,
            String.format("Period from %d-%d-%d to %d-%d-%d", year1, month1, day1, year2, month2, day2));
        
        // Verify that adding the period gives us the end date
        assertEquals(endDate, startDate.plus(period));
    }

    //-----------------------------------------------------------------------
    // Integration with LocalTime tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldCombineWithLocalTimeCorrectly() {
        BritishCutoverDate date = BritishCutoverDate.of(2014, 10, 12);
        LocalTime time = LocalTime.of(12, 30);
        
        ChronoLocalDateTime<BritishCutoverDate> dateTime = date.atTime(time);
        
        assertEquals(date, dateTime.toLocalDate());
        assertEquals(time, dateTime.toLocalTime());
    }

    @Test
    public void shouldRejectNullTime() {
        assertThrows(NullPointerException.class,
            () -> BritishCutoverDate.of(2014, 5, 26).atTime(null),
            "Should reject null time");
    }

    //-----------------------------------------------------------------------
    // Cross-validation with GregorianCalendar
    //-----------------------------------------------------------------------

    @Test
    public void shouldMatchGregorianCalendarBehavior() {
        BritishCutoverDate testDate = BritishCutoverDate.of(1700, 1, 1);
        BritishCutoverDate endDate = BritishCutoverDate.of(1800, 1, 1);
        
        // Set up GregorianCalendar with British cutover date
        Instant cutoverInstant = ZonedDateTime.of(1752, 9, 14, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.setGregorianChange(Date.from(cutoverInstant));
        gregorianCalendar.clear();
        gregorianCalendar.set(1700, Calendar.JANUARY, 1);
        
        // Compare dates day by day
        while (testDate.isBefore(endDate)) {
            assertEquals(gregorianCalendar.get(Calendar.YEAR), testDate.get(YEAR_OF_ERA),
                "Year mismatch at " + testDate);
            assertEquals(gregorianCalendar.get(Calendar.MONTH) + 1, testDate.get(MONTH_OF_YEAR),
                "Month mismatch at " + testDate);
            assertEquals(gregorianCalendar.get(Calendar.DAY_OF_MONTH), testDate.get(DAY_OF_MONTH),
                "Day mismatch at " + testDate);
            assertEquals(gregorianCalendar.toZonedDateTime().toLocalDate(), LocalDate.from(testDate),
                "ISO date mismatch at " + testDate);
            
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
            testDate = testDate.plus(1, DAYS);
        }
    }

    //-----------------------------------------------------------------------
    // Equals and hashCode tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldImplementEqualsAndHashCodeCorrectly() {
        new EqualsTester()
            .addEqualityGroup(britishDate(2000, 1, 3), britishDate(2000, 1, 3))
            .addEqualityGroup(britishDate(2000, 1, 4), britishDate(2000, 1, 4))
            .addEqualityGroup(britishDate(2000, 2, 3), britishDate(2000, 2, 3))
            .addEqualityGroup(britishDate(2001, 1, 3), britishDate(2001, 1, 3))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // String representation tests
    //-----------------------------------------------------------------------

    public static Object[][] toStringTestData() {
        return new Object[][] {
            {britishDate(1, 1, 1), "BritishCutover AD 1-01-01"},
            {britishDate(2012, 6, 23), "BritishCutover AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("toStringTestData")
    public void shouldFormatToStringCorrectly(BritishCutoverDate date, String expectedString) {
        assertEquals(expectedString, date.toString());
    }

    //-----------------------------------------------------------------------
    // Helper methods for test readability
    //-----------------------------------------------------------------------

    private static BritishCutoverDate britishDate(int year, int month, int day) {
        return BritishCutoverDate.of(year, month, day);
    }

    private static LocalDate isoDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}