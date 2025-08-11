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
 * Comprehensive test suite for JulianChronology.
 * Tests the Julian calendar system implementation including date conversions,
 * leap year calculations, temporal operations, and field manipulations.
 */
public class TestJulianChronology {

    // Test constants for better readability
    private static final JulianDate JULIAN_YEAR_1_JAN_1 = JulianDate.of(1, 1, 1);
    private static final LocalDate ISO_YEAR_0_DEC_30 = LocalDate.of(0, 12, 30);
    private static final JulianDate JULIAN_2012_JUNE_23 = JulianDate.of(2012, 6, 23);
    private static final LocalDate ISO_2012_JULY_6 = LocalDate.of(2012, 7, 6);

    //-----------------------------------------------------------------------
    // Chronology Discovery Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldRetrieveJulianChronologyByCapitalizedName() {
        // Given: The chronology name "Julian"
        String chronologyName = "Julian";
        
        // When: Looking up chronology by name
        Chronology chronology = Chronology.of(chronologyName);
        
        // Then: Should return Julian chronology instance with correct properties
        assertNotNull(chronology);
        assertEquals(JulianChronology.INSTANCE, chronology);
        assertEquals("Julian", chronology.getId());
        assertEquals("julian", chronology.getCalendarType());
    }

    @Test
    public void shouldRetrieveJulianChronologyByLowercaseName() {
        // Given: The chronology name "julian" (lowercase)
        String chronologyName = "julian";
        
        // When: Looking up chronology by name
        Chronology chronology = Chronology.of(chronologyName);
        
        // Then: Should return Julian chronology instance with correct properties
        assertNotNull(chronology);
        assertEquals(JulianChronology.INSTANCE, chronology);
        assertEquals("Julian", chronology.getId());
        assertEquals("julian", chronology.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Date Conversion Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for Julian to ISO date conversions.
     * Each row contains: {JulianDate, corresponding LocalDate}
     */
    public static Object[][] julianToIsoConversionData() {
        return new Object[][] {
            // Year 1 conversions - Julian calendar starts 2 days ahead of ISO
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},

            // February transitions in non-leap year
            {JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)},
            {JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)},
            {JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)},
            {JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)},

            // February transitions in leap year (year 4)
            {JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)},
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)},
            {JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)},

            // Century year leap year in Julian (year 100 is leap in Julian, not in Gregorian)
            {JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)},
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            {JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)},
            {JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)},

            // Year 0 (1 BC) end of year
            {JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)},
            {JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)},

            // Gregorian calendar adoption dates (October 1582)
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            
            // Modern dates
            {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
            {JULIAN_2012_JUNE_23, ISO_2012_JULY_6},
            {JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)},
        };
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldConvertJulianDateToLocalDate(JulianDate julianDate, LocalDate expectedIsoDate) {
        // When: Converting Julian date to LocalDate
        LocalDate actualIsoDate = LocalDate.from(julianDate);
        
        // Then: Should match expected ISO date
        assertEquals(expectedIsoDate, actualIsoDate);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldConvertLocalDateToJulianDate(JulianDate expectedJulianDate, LocalDate isoDate) {
        // When: Converting LocalDate to Julian date
        JulianDate actualJulianDate = JulianDate.from(isoDate);
        
        // Then: Should match expected Julian date
        assertEquals(expectedJulianDate, actualJulianDate);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCreateJulianDateFromEpochDay(JulianDate expectedJulianDate, LocalDate isoDate) {
        // When: Creating Julian date from epoch day
        JulianDate actualJulianDate = JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay());
        
        // Then: Should match expected Julian date
        assertEquals(expectedJulianDate, actualJulianDate);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldConvertJulianDateToEpochDay(JulianDate julianDate, LocalDate isoDate) {
        // When: Converting Julian date to epoch day
        long actualEpochDay = julianDate.toEpochDay();
        
        // Then: Should match ISO date's epoch day
        assertEquals(isoDate.toEpochDay(), actualEpochDay);
    }

    //-----------------------------------------------------------------------
    // Period Calculation Tests
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCalculateZeroPeriodBetweenSameJulianDates(JulianDate julianDate, LocalDate isoDate) {
        // When: Calculating period between same Julian dates
        var period = julianDate.until(julianDate);
        
        // Then: Should be zero period
        assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), period);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCalculateZeroPeriodBetweenJulianAndEquivalentIsoDate(JulianDate julianDate, LocalDate isoDate) {
        // When: Calculating period between Julian date and equivalent ISO date
        var period = julianDate.until(isoDate);
        
        // Then: Should be zero period
        assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), period);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCalculateZeroPeriodBetweenIsoAndEquivalentJulianDate(JulianDate julianDate, LocalDate isoDate) {
        // When: Calculating period between ISO date and equivalent Julian date
        var period = isoDate.until(julianDate);
        
        // Then: Should be zero period
        assertEquals(Period.ZERO, period);
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCreateJulianDateFromTemporalAccessor(JulianDate expectedJulianDate, LocalDate isoDate) {
        // When: Creating Julian date from temporal accessor
        JulianDate actualJulianDate = JulianChronology.INSTANCE.date(isoDate);
        
        // Then: Should match expected Julian date
        assertEquals(expectedJulianDate, actualJulianDate);
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldAddDaysToJulianDate(JulianDate julianDate, LocalDate equivalentIsoDate) {
        // Test various day additions
        assertEquals(equivalentIsoDate, LocalDate.from(julianDate.plus(0, DAYS)));
        assertEquals(equivalentIsoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)));
        assertEquals(equivalentIsoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)));
        assertEquals(equivalentIsoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)));
        assertEquals(equivalentIsoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldSubtractDaysFromJulianDate(JulianDate julianDate, LocalDate equivalentIsoDate) {
        // Test various day subtractions
        assertEquals(equivalentIsoDate, LocalDate.from(julianDate.minus(0, DAYS)));
        assertEquals(equivalentIsoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)));
        assertEquals(equivalentIsoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)));
        assertEquals(equivalentIsoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)));
        assertEquals(equivalentIsoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("julianToIsoConversionData")
    public void shouldCalculateDaysBetweenJulianAndIsoDates(JulianDate julianDate, LocalDate isoDate) {
        // Test day calculations between dates
        assertEquals(0, julianDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, julianDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, julianDate.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, julianDate.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // Invalid Date Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for invalid Julian dates that should throw exceptions.
     * Each row contains: {year, month, dayOfMonth}
     */
    public static Object[][] invalidJulianDateData() {
        return new Object[][] {
            // Invalid year/month/day combinations
            {1900, 0, 0},   // All zero
            
            // Invalid months
            {1900, -1, 1},  // Negative month
            {1900, 0, 1},   // Zero month
            {1900, 13, 1},  // Month too high
            {1900, 14, 1},  // Month way too high
            
            // Invalid days
            {1900, 1, -1},  // Negative day
            {1900, 1, 0},   // Zero day
            {1900, 1, 32},  // January has only 31 days
            
            // February invalid days
            {1900, 2, -1},  // Negative day
            {1900, 2, 0},   // Zero day
            {1900, 2, 30},  // February never has 30 days
            {1900, 2, 31},  // February never has 31 days
            {1900, 2, 32},  // Way too many days
            
            // February in non-leap year (1899 % 4 = 3, so not leap)
            {1899, 2, -1},  // Negative day
            {1899, 2, 0},   // Zero day
            {1899, 2, 29},  // No Feb 29 in non-leap year
            {1899, 2, 30},  // February never has 30 days
            {1899, 2, 31},  // February never has 31 days
            {1899, 2, 32},  // Way too many days
            
            // December invalid days
            {1900, 12, -1}, // Negative day
            {1900, 12, 0},  // Zero day
            {1900, 12, 32}, // December has only 31 days
            
            // Other months with wrong number of days
            {1900, 3, 32},  // March has 31 days
            {1900, 4, 31},  // April has 30 days
            {1900, 5, 32},  // May has 31 days
            {1900, 6, 31},  // June has 30 days
            {1900, 7, 32},  // July has 31 days
            {1900, 8, 32},  // August has 31 days
            {1900, 9, 31},  // September has 30 days
            {1900, 10, 32}, // October has 31 days
            {1900, 11, 31}, // November has 30 days
        };
    }

    @ParameterizedTest
    @MethodSource("invalidJulianDateData")
    public void shouldThrowExceptionForInvalidJulianDate(int year, int month, int dayOfMonth) {
        // When/Then: Creating invalid Julian date should throw DateTimeException
        assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
    }

    @Test
    public void shouldThrowExceptionForInvalidDayOfYear() {
        // When/Then: Creating date with invalid day of year should throw exception
        assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.dateYearDay(2001, 366));
    }

    //-----------------------------------------------------------------------
    // Leap Year Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldCorrectlyIdentifyLeapYears() {
        // Test leap year logic for range of years
        for (int year = -200; year < 200; year++) {
            JulianDate julianDate = JulianDate.of(year, 1, 1);
            boolean expectedLeapYear = (year % 4) == 0;
            
            assertEquals(expectedLeapYear, julianDate.isLeapYear(), 
                "Year " + year + " leap year calculation incorrect");
            assertEquals(expectedLeapYear, JulianChronology.INSTANCE.isLeapYear(year),
                "Chronology leap year calculation for " + year + " incorrect");
        }
    }

    @Test
    public void shouldIdentifySpecificLeapYears() {
        // Test specific years around year 0
        assertTrue(JulianChronology.INSTANCE.isLeapYear(8));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(7));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(6));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(5));
        assertTrue(JulianChronology.INSTANCE.isLeapYear(4));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(3));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(2));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(1));
        assertTrue(JulianChronology.INSTANCE.isLeapYear(0));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(-1));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(-2));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(-3));
        assertTrue(JulianChronology.INSTANCE.isLeapYear(-4));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(-5));
        assertFalse(JulianChronology.INSTANCE.isLeapYear(-6));
    }

    //-----------------------------------------------------------------------
    // Month Length Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for month lengths in Julian calendar.
     * Each row contains: {year, month, expectedLength}
     */
    public static Object[][] monthLengthData() {
        return new Object[][] {
            // Standard year (1900 is leap in Julian calendar)
            {1900, 1, 31},   // January
            {1900, 2, 29},   // February (leap year)
            {1900, 3, 31},   // March
            {1900, 4, 30},   // April
            {1900, 5, 31},   // May
            {1900, 6, 30},   // June
            {1900, 7, 31},   // July
            {1900, 8, 31},   // August
            {1900, 9, 30},   // September
            {1900, 10, 31},  // October
            {1900, 11, 30},  // November
            {1900, 12, 31},  // December

            // Non-leap years
            {1901, 2, 28},   // February in non-leap year
            {1902, 2, 28},   // February in non-leap year
            {1903, 2, 28},   // February in non-leap year
            
            // Leap years
            {1904, 2, 29},   // February in leap year
            {2000, 2, 29},   // February in leap year (2000 % 4 = 0)
            {2100, 2, 29},   // February in leap year (2100 % 4 = 0, Julian has no 100-year rule)
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengthData")
    public void shouldCalculateCorrectMonthLength(int year, int month, int expectedLength) {
        // When: Getting length of month
        int actualLength = JulianDate.of(year, month, 1).lengthOfMonth();
        
        // Then: Should match expected length
        assertEquals(expectedLength, actualLength);
    }

    //-----------------------------------------------------------------------
    // Era and Proleptic Year Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldCorrectlyHandleErasAndProlepticYears() {
        for (int year = -200; year < 200; year++) {
            JulianDate julianDate = JulianChronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, julianDate.get(YEAR));
            
            JulianEra expectedEra = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(expectedEra, julianDate.getEra());
            
            int expectedYearOfEra = (year <= 0 ? 1 - year : year);
            assertEquals(expectedYearOfEra, julianDate.get(YEAR_OF_ERA));
            
            JulianDate eraBasedDate = JulianChronology.INSTANCE.date(expectedEra, expectedYearOfEra, 1, 1);
            assertEquals(julianDate, eraBasedDate);
        }
    }

    @Test
    public void shouldCorrectlyHandleErasWithYearDay() {
        for (int year = -200; year < 200; year++) {
            JulianDate julianDate = JulianChronology.INSTANCE.dateYearDay(year, 1);
            
            assertEquals(year, julianDate.get(YEAR));
            
            JulianEra expectedEra = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(expectedEra, julianDate.getEra());
            
            int expectedYearOfEra = (year <= 0 ? 1 - year : year);
            assertEquals(expectedYearOfEra, julianDate.get(YEAR_OF_ERA));
            
            JulianDate eraBasedDate = JulianChronology.INSTANCE.dateYearDay(expectedEra, expectedYearOfEra, 1);
            assertEquals(julianDate, eraBasedDate);
        }
    }

    @Test
    public void shouldCalculateCorrectProlepticYears() {
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
    public void shouldThrowExceptionForInvalidEra() {
        // When/Then: Using wrong era type should throw ClassCastException
        assertThrows(ClassCastException.class, () -> 
            JulianChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
    }

    @Test
    public void shouldReturnCorrectEraForValue() {
        assertEquals(JulianEra.AD, JulianChronology.INSTANCE.eraOf(1));
        assertEquals(JulianEra.BC, JulianChronology.INSTANCE.eraOf(0));
    }

    @Test
    public void shouldThrowExceptionForInvalidEraValue() {
        // When/Then: Invalid era value should throw DateTimeException
        assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.eraOf(2));
    }

    @Test
    public void shouldReturnAllEras() {
        List<Era> eras = JulianChronology.INSTANCE.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.BC));
        assertTrue(eras.contains(JulianEra.AD));
    }

    //-----------------------------------------------------------------------
    // Field Range Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldReturnCorrectChronologyFieldRanges() {
        assertEquals(ValueRange.of(1, 7), JulianChronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), JulianChronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), JulianChronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), JulianChronology.INSTANCE.range(MONTH_OF_YEAR));
    }

    /**
     * Test data for field ranges on specific Julian dates.
     * Each row contains: {year, month, day, field, expectedMin, expectedMax}
     */
    public static Object[][] fieldRangeData() {
        return new Object[][] {
            // Day of month ranges for different months
            {2012, 1, 23, DAY_OF_MONTH, 1, 31},   // January
            {2012, 2, 23, DAY_OF_MONTH, 1, 29},   // February (leap year)
            {2012, 3, 23, DAY_OF_MONTH, 1, 31},   // March
            {2012, 4, 23, DAY_OF_MONTH, 1, 30},   // April
            {2012, 5, 23, DAY_OF_MONTH, 1, 31},   // May
            {2012, 6, 23, DAY_OF_MONTH, 1, 30},   // June
            {2012, 7, 23, DAY_OF_MONTH, 1, 31},   // July
            {2012, 8, 23, DAY_OF_MONTH, 1, 31},   // August
            {2012, 9, 23, DAY_OF_MONTH, 1, 30},   // September
            {2012, 10, 23, DAY_OF_MONTH, 1, 31},  // October
            {2012, 11, 23, DAY_OF_MONTH, 1, 30},  // November
            {2012, 12, 23, DAY_OF_MONTH, 1, 31},  // December
            
            // Day of year ranges
            {2012, 1, 23, DAY_OF_YEAR, 1, 366},   // Leap year
            
            // Aligned week of month ranges
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2012, 3, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},

            // Non-leap year comparisons
            {2011, 2, 23, DAY_OF_MONTH, 1, 28},   // February (non-leap year)
            {2011, 2, 23, DAY_OF_YEAR, 1, 365},   // Non-leap year
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
        };
    }

    @ParameterizedTest
    @MethodSource("fieldRangeData")
    public void shouldReturnCorrectFieldRange(int year, int month, int day, 
            TemporalField field, int expectedMin, int expectedMax) {
        // When: Getting field range for specific date
        ValueRange actualRange = JulianDate.of(year, month, day).range(field);
        
        // Then: Should match expected range
        assertEquals(ValueRange.of(expectedMin, expectedMax), actualRange);
    }

    @Test
    public void shouldThrowExceptionForUnsupportedFieldRange() {
        // When/Then: Getting range for unsupported field should throw exception
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            JulianDate.of(2012, 6, 30).range(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // Field Value Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for getting field values from Julian dates.
     * Each row contains: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValueData() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},                              // Monday = 1, Sunday = 7
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},       // Jan+Feb+Mar+Apr+26 days of May
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},        // Zero-based month calculation
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},                                      // AD era
            {1, 6, 8, ERA, 1},                                          // AD era
            {0, 6, 8, ERA, 0},                                          // BC era

            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},               // ISO week fields
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValueData")
    public void shouldReturnCorrectFieldValue(int year, int month, int day, 
            TemporalField field, long expectedValue) {
        // When: Getting field value from Julian date
        long actualValue = JulianDate.of(year, month, day).getLong(field);
        
        // Then: Should match expected value
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void shouldThrowExceptionForUnsupportedFieldValue() {
        // When/Then: Getting unsupported field value should throw exception
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            JulianDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // Field Modification Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for modifying Julian dates with field values.
     * Each row contains: {year, month, day, field, newValue, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] fieldModificationData() {
        return new Object[][] {
            // Day of week modifications
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_WEEK, 7, 2014, 5, 26},
            
            // Day of month modifications
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            
            // Day of year modifications
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            {2014, 5, 26, DAY_OF_YEAR, 146, 2014, 5, 26},
            
            // Aligned day/week modifications
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 22},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21, 2014, 5, 26},
            
            // Month modifications
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26},
            
            // Proleptic month modifications
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1, 2014, 5, 26},
            
            // Year modifications
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR, 2014, 2014, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2014, 2014, 5, 26},
            
            // Era modifications
            {2014, 5, 26, ERA, 0, -2013, 5, 26},  // Switch to BC era
            {2014, 5, 26, ERA, 1, 2014, 5, 26},   // Stay in AD era

            // Edge cases with month length adjustments
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28},  // March 31 -> February 28 (non-leap)
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29},  // March 31 -> February 29 (leap)
            {2012, 3, 31, MONTH_OF_YEAR, 6, 2012, 6, 30},  // March 31 -> June 30
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28},        // Feb 29 -> Feb 28 (leap to non-leap)
            {-2013, 6, 8, YEAR_OF_ERA, 2012, -2011, 6, 8}, // BC year modification

            // ISO week fields
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 3, 2014, 5, 22},
        };
    }

    @ParameterizedTest
    @MethodSource("fieldModificationData")
    public void shouldModifyJulianDateWithFieldValue(int year, int month, int day,
            TemporalField field, long newValue,
            int expectedYear, int expectedMonth, int expectedDay) {
        // Given: A Julian date
        JulianDate originalDate = JulianDate.of(year, month, day);
        JulianDate expectedDate = JulianDate.of(expectedYear, expectedMonth, expectedDay);
        
        // When: Modifying the date with new field value
        JulianDate actualDate = originalDate.with(field, newValue);
        
        // Then: Should match expected date
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void shouldThrowExceptionForUnsupportedFieldModification() {
        // When/Then: Modifying unsupported field should throw exception
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            JulianDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0));
    }

    //-----------------------------------------------------------------------
    // Temporal Adjuster Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldAdjustToLastDayOfMonth() {
        // Given: A Julian date in June
        JulianDate baseDate = JulianDate.of(2012, 6, 23);
        
        // When: Adjusting to last day of month
        JulianDate adjustedDate = baseDate.with(TemporalAdjusters.lastDayOfMonth());
        
        // Then: Should be June 30th
        assertEquals(JulianDate.of(2012, 6, 30), adjustedDate);
    }

    @Test
    public void shouldAdjustToLastDayOfFebruaryInLeapYear() {
        // Given: A Julian date in February of leap year
        JulianDate baseDate = JulianDate.of(2012, 2, 23);
        
        // When: Adjusting to last day of month
        JulianDate adjustedDate = baseDate.with(TemporalAdjusters.lastDayOfMonth());
        
        // Then: Should be February 29th (leap year)
        assertEquals(JulianDate.of(2012, 2, 29), adjustedDate);
    }

    @Test
    public void shouldAdjustJulianDateWithLocalDate() {
        // Given: A Julian date and a LocalDate
        JulianDate originalJulianDate = JulianDate.of(2000, 1, 4);
        LocalDate targetIsoDate = LocalDate.of(2012, 7, 6);
        
        // When: Adjusting Julian date with LocalDate
        JulianDate adjustedJulianDate = originalJulianDate.with(targetIsoDate);
        
        // Then: Should convert to equivalent Julian date
        assertEquals(JulianDate.of(2012, 6, 23), adjustedJulianDate);
    }

    @Test
    public void shouldThrowExceptionWhenAdjustingWithMonth() {
        // Given: A Julian date
        JulianDate julianDate = JulianDate.of(2000, 1, 4);
        
        // When/Then: Adjusting with Month enum should throw exception
        assertThrows(DateTimeException.class, () -> julianDate.with(Month.APRIL));
    }

    @Test
    public void shouldAdjustLocalDateWithJulianDate() {
        // Given: A Julian date
        JulianDate julianDate = JulianDate.of(2012, 6, 23);
        
        // When: Adjusting LocalDate with Julian date
        LocalDate adjustedLocalDate = LocalDate.MIN.with(julianDate);
        
        // Then: Should convert to equivalent ISO date
        assertEquals(LocalDate.of(2012, 7, 6), adjustedLocalDate);
    }

    @Test
    public void shouldAdjustLocalDateTimeWithJulianDate() {
        // Given: A Julian date
        JulianDate julianDate = JulianDate.of(2012, 6, 23);
        
        // When: Adjusting LocalDateTime with Julian date
        LocalDateTime adjustedLocalDateTime = LocalDateTime.MIN.with(julianDate);
        
        // Then: Should convert to equivalent ISO date with time preserved
        assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), adjustedLocalDateTime);
    }

    //-----------------------------------------------------------------------
    // Temporal Unit Addition/Subtraction Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for adding temporal units to Julian dates.
     * Each row contains: {year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] temporalUnitArithmeticData() {
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
            {2014, 5, 26, -1, ERAS, -2013, 5, 26},  // AD to BC
        };
    }

    @ParameterizedTest
    @MethodSource("temporalUnitArithmeticData")
    public void shouldAddTemporalUnitToJulianDate(int year, int month, int day,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDay) {
        // Given: A Julian date
        JulianDate originalDate = JulianDate.of(year, month, day);
        JulianDate expectedDate = JulianDate.of(expectedYear, expectedMonth, expectedDay);
        
        // When: Adding temporal unit
        JulianDate actualDate = originalDate.plus(amount, unit);
        
        // Then: Should match expected date
        assertEquals(expectedDate, actualDate);
    }

    @ParameterizedTest
    @MethodSource("temporalUnitArithmeticData")
    public void shouldSubtractTemporalUnitFromJulianDate(
            int expectedYear, int expectedMonth, int expectedDay,
            long amount, TemporalUnit unit,
            int year, int month, int day) {
        // Given: A Julian date
        JulianDate originalDate = JulianDate.of(year, month, day);
        JulianDate expectedDate = JulianDate.of(expectedYear, expectedMonth, expectedDay);
        
        // When: Subtracting temporal unit (reverse of addition)
        JulianDate actualDate = originalDate.minus(amount, unit);
        
        // Then: Should match expected date
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void shouldThrowExceptionForUnsupportedTemporalUnitAddition() {
        // When/Then: Adding unsupported temporal unit should throw exception
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            JulianDate.of(2012, 6, 30).plus(0, MINUTES));
    }

    //-----------------------------------------------------------------------
    // Temporal Unit Until Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for calculating time between Julian dates.
     * Each row contains: {year1, month1, day1, year2, month2, day2, unit, expectedAmount}
     */
    public static Object[][] temporalUntilData() {
        return new Object[][] {
            // Same dates
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            {2014, 5, 26, 2014, 5, 26, DECADES, 0},
            {2014, 5, 26, 2014, 5, 26, CENTURIES, 0},
            {2014, 5, 26, 2014, 5, 26, MILLENNIA, 0},
            
            // Days
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            
            // Weeks
            {2014, 5, 26, 2014, 6, 1, WEEKS, 0},   // Less than a week
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},   // Exactly one week
            
            // Months
            {2014, 5, 26, 2014, 6, 25, MONTHS, 0}, // Less than a month
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, // Exactly one month
            
            // Years
            {2014, 5, 26, 2015, 5, 25, YEARS, 0},  // Less than a year
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},  // Exactly one year
            
            // Decades
            {2014, 5, 26, 2024, 5, 25, DECADES, 0}, // Less than a decade
            {2014, 5, 26, 2024, 5, 26, DECADES, 1}, // Exactly one decade
            
            // Centuries
            {2014, 5, 26, 2114, 5, 25, CENTURIES, 0}, // Less than a century
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1}, // Exactly one century
            
            // Millennia
            {2014, 5, 26, 3014, 5, 25, MILLENNIA, 0}, // Less than a millennium
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, // Exactly one millennium
            
            // Eras
            {-2013, 5, 26, 0, 5, 26, ERAS, 0},     // Within same era
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},  // Cross era boundary
        };
    }

    @ParameterizedTest
    @MethodSource("temporalUntilData")
    public void shouldCalculateCorrectTemporalUnitsBetweenDates(
            int year1, int month1, int day1,
            int year2, int month2, int day2,
            TemporalUnit unit, long expectedAmount) {
        // Given: Two Julian dates
        JulianDate startDate = JulianDate.of(year1, month1, day1);
        JulianDate endDate = JulianDate.of(year2, month2, day2);
        
        // When: Calculating time between dates
        long actualAmount = startDate.until(endDate, unit);
        
        // Then: Should match expected amount
        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void shouldThrowExceptionForUnsupportedTemporalUnitUntil() {
        // Given: Two Julian dates
        JulianDate startDate = JulianDate.of(2012, 6, 30);
        JulianDate endDate = JulianDate.of(2012, 7, 1);
        
        // When/Then: Using unsupported temporal unit should throw exception
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            startDate.until(endDate, MINUTES));
    }

    //-----------------------------------------------------------------------
    // Period Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldAddJulianPeriodToJulianDate() {
        // Given: A Julian date and a Julian period
        JulianDate originalDate = JulianDate.of(2014, 5, 26);
        var julianPeriod = JulianChronology.INSTANCE.period(0, 2, 3); // 2 months, 3 days
        
        // When: Adding Julian period
        JulianDate resultDate = originalDate.plus(julianPeriod);
        
        // Then: Should add correctly
        assertEquals(JulianDate.of(2014, 7, 29), resultDate);
    }

    @Test
    public void shouldThrowExceptionWhenAddingIsoPeriodToJulianDate() {
        // Given: A Julian date and an ISO period
        JulianDate julianDate = JulianDate.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);
        
        // When/Then: Adding ISO period should throw exception
        assertThrows(DateTimeException.class, () -> julianDate.plus(isoPeriod));
    }

    @Test
    public void shouldSubtractJulianPeriodFromJulianDate() {
        // Given: A Julian date and a Julian period
        JulianDate originalDate = JulianDate.of(2014, 5, 26);
        var julianPeriod = JulianChronology.INSTANCE.period(0, 2, 3); // 2 months, 3 days
        
        // When: Subtracting Julian period
        JulianDate resultDate = originalDate.minus(julianPeriod);
        
        // Then: Should subtract correctly
        assertEquals(JulianDate.of(2014, 3, 23), resultDate);
    }

    @Test
    public void shouldThrowExceptionWhenSubtractingIsoPeriodFromJulianDate() {
        // Given: A Julian date and an ISO period
        JulianDate julianDate = JulianDate.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);
        
        // When/Then: Subtracting ISO period should throw exception
        assertThrows(DateTimeException.class, () -> julianDate.minus(isoPeriod));
    }

    //-----------------------------------------------------------------------
    // Equality and Hash Code Tests
    //-----------------------------------------------------------------------

    @Test
    public void shouldImplementEqualsAndHashCodeCorrectly() {
        new EqualsTester()
            .addEqualityGroup(JulianDate.of(2000, 1, 3), JulianDate.of(2000, 1, 3))
            .addEqualityGroup(JulianDate.of(2000, 1, 4), JulianDate.of(2000, 1, 4))
            .addEqualityGroup(JulianDate.of(2000, 2, 3), JulianDate.of(2000, 2, 3))
            .addEqualityGroup(JulianDate.of(2001, 1, 3), JulianDate.of(2001, 1, 3))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------

    /**
     * Test data for Julian date string representations.
     * Each row contains: {JulianDate, expectedString}
     */
    public static Object[][] toStringData() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
            {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("toStringData")
    public void shouldFormatJulianDateAsString(JulianDate julianDate, String expectedString) {
        // When: Converting Julian date to string
        String actualString = julianDate.toString();
        
        // Then: Should match expected format
        assertEquals(expectedString, actualString);
    }
}