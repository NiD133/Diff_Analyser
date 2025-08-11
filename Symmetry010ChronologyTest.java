/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 * [License text remains the same...]
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
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for the Symmetry010 calendar system.
 * 
 * The Symmetry010 calendar is a proposed calendar reform with these key features:
 * - Each year has exactly 52 weeks (364 days) or 53 weeks (371 days) in leap years
 * - Each quarter has exactly 13 weeks (91 days)
 * - Months alternate between 30 and 31 days: Jan(30), Feb(31), Mar(30), Apr(30), May(31), Jun(30), 
 *   Jul(30), Aug(31), Sep(30), Oct(30), Nov(31), Dec(30 or 37 in leap years)
 * - Every year starts on Monday, ensuring consistent weekday patterns
 * - Leap years occur approximately every 5-6 years based on a 293-year cycle
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry010Chronology {

    //-----------------------------------------------------------------------
    // Basic chronology identification tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldIdentifyChronologyCorrectly() {
        Chronology chrono = Chronology.of("Sym010");
        
        assertNotNull(chrono);
        assertEquals(Symmetry010Chronology.INSTANCE, chrono);
        assertEquals("Sym010", chrono.getId());
        assertEquals(null, chrono.getCalendarType()); // No Unicode LDML identifier
    }

    //-----------------------------------------------------------------------
    // Date conversion tests between Symmetry010 and ISO calendars
    //-----------------------------------------------------------------------
    
    /**
     * Test data comparing Symmetry010 dates with their ISO (Gregorian) equivalents.
     * Each row contains: {Symmetry010Date, corresponding LocalDate}
     * 
     * Historical dates are included to verify accuracy across different time periods.
     */
    public static Object[][] historicalDateConversions() {
        return new Object[][] {
            // Basic epoch alignment
            { Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1) },
            
            // Historical figures and events with their birth/event dates
            { Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27) }, // Constantine the Great
            { Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2) },  // Charlemagne
            { Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14) }, // Battle of Hastings
            { Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20) }, // Petrarch
            { Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15) }, // Leonardo da Vinci
            { Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12) }, // Columbus lands in Caribbean
            { Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15) }, // Galileo Galilei
            { Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26) }, // Shakespeare baptized
            { Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4) },   // Isaac Newton
            { Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14) }, // Storming of Bastille
            { Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14) }, // Albert Einstein
            { Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1) },   // Unix epoch
            { Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3) },   // Y2K/21st century
        };
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldConvertFromSymmetry010ToIsoDate(Symmetry010Date sym010Date, LocalDate expectedIsoDate) {
        LocalDate actualIsoDate = LocalDate.from(sym010Date);
        assertEquals(expectedIsoDate, actualIsoDate);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldConvertFromIsoToSymmetry010Date(Symmetry010Date expectedSym010Date, LocalDate isoDate) {
        Symmetry010Date actualSym010Date = Symmetry010Date.from(isoDate);
        assertEquals(expectedSym010Date, actualSym010Date);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldConvertEpochDaysBidirectionally(Symmetry010Date sym010Date, LocalDate isoDate) {
        // Test epoch day conversion both ways
        assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCalculateZeroPeriodBetweenEquivalentDates(Symmetry010Date sym010Date, LocalDate isoDate) {
        ChronoPeriod zeroPeriod = Symmetry010Chronology.INSTANCE.period(0, 0, 0);
        
        assertEquals(zeroPeriod, sym010Date.until(sym010Date));
        assertEquals(zeroPeriod, sym010Date.until(isoDate));
        assertEquals(Period.ZERO, isoDate.until(sym010Date));
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldPerformDateArithmeticCorrectly(Symmetry010Date sym010Date, LocalDate isoDate) {
        // Test adding/subtracting days
        assertEquals(isoDate, LocalDate.from(sym010Date.plus(0, DAYS)));
        assertEquals(isoDate.plusDays(1), LocalDate.from(sym010Date.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(sym010Date.plus(35, DAYS)));
        assertEquals(isoDate.minusDays(1), LocalDate.from(sym010Date.minus(1, DAYS)));
        assertEquals(isoDate.minusDays(60), LocalDate.from(sym010Date.minus(60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCalculateDayDifferencesCorrectly(Symmetry010Date sym010Date, LocalDate isoDate) {
        assertEquals(0, sym010Date.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, sym010Date.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, sym010Date.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, sym010Date.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // Invalid date validation tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for dates that should be rejected as invalid.
     * Format: {year, month, dayOfMonth}
     */
    public static Object[][] invalidDateInputs() {
        return new Object[][] {
            // Invalid years
            {-1, 13, 28}, {-1, 13, 29},
            
            // Invalid months
            {2000, -2, 1}, {2000, 0, 1}, {2000, 13, 1}, {2000, 15, 1},
            
            // Invalid days
            {2000, 1, -1}, {2000, 1, 0},
            
            // Days exceeding month limits (30-day months)
            {2000, 1, 31}, {2000, 3, 31}, {2000, 4, 31}, 
            {2000, 6, 31}, {2000, 7, 31}, {2000, 9, 31}, 
            {2000, 10, 31}, {2000, 12, 31},
            
            // Days exceeding month limits (31-day months: Feb, May, Aug, Nov)
            {2000, 2, 32}, {2000, 5, 32}, {2000, 8, 32}, {2000, 11, 32},
            
            // Days exceeding leap year December limit
            {2004, 12, 38},
        };
    }

    @ParameterizedTest
    @MethodSource("invalidDateInputs")
    public void shouldRejectInvalidDates(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, 
            () -> Symmetry010Date.of(year, month, dayOfMonth),
            String.format("Should reject invalid date: %d/%d/%d", year, month, dayOfMonth));
    }

    /**
     * Years that are NOT leap years in the Symmetry010 system.
     * These years should reject day 37 in December.
     */
    public static Object[][] nonLeapYears() {
        return new Object[][] {{1}, {100}, {200}, {2000}};
    }

    @ParameterizedTest
    @MethodSource("nonLeapYears")
    public void shouldRejectLeapDayInNonLeapYears(int year) {
        assertThrows(DateTimeException.class, 
            () -> Symmetry010Date.of(year, 12, 37),
            String.format("Year %d is not a leap year, should reject December 37", year));
    }

    //-----------------------------------------------------------------------
    // Leap year calculation tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldIdentifyLeapYearsCorrectly() {
        // Test specific known leap years and non-leap years
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(3), "Year 3 should be a leap year");
        assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(6), "Year 6 should not be a leap year");
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(9), "Year 9 should be a leap year");
        assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(2000), "Year 2000 should not be a leap year");
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(2004), "Year 2004 should be a leap year");
    }

    @Test
    public void shouldIdentifyLeapWeekDaysCorrectly() {
        // In leap years, December has days 31-37 which form the "leap week"
        Symmetry010Date[] leapWeekDays = {
            Symmetry010Date.of(2015, 12, 31),
            Symmetry010Date.of(2015, 12, 32),
            Symmetry010Date.of(2015, 12, 33),
            Symmetry010Date.of(2015, 12, 34),
            Symmetry010Date.of(2015, 12, 35),
            Symmetry010Date.of(2015, 12, 36),
            Symmetry010Date.of(2015, 12, 37)
        };
        
        for (Symmetry010Date date : leapWeekDays) {
            assertTrue(date.isLeapWeek(), 
                String.format("Day %d of December in leap year should be part of leap week", date.getDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    // Month length validation tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for month lengths in the Symmetry010 calendar.
     * Format: {year, month, anyDayInMonth, expectedMonthLength}
     */
    public static Object[][] monthLengthTestData() {
        return new Object[][] {
            // Regular year (2000) - alternating 30/31 day pattern
            {2000, 1, 15, 30},   // January: 30 days
            {2000, 2, 15, 31},   // February: 31 days (quarter middle month)
            {2000, 3, 15, 30},   // March: 30 days
            {2000, 4, 15, 30},   // April: 30 days
            {2000, 5, 15, 31},   // May: 31 days (quarter middle month)
            {2000, 6, 15, 30},   // June: 30 days
            {2000, 7, 15, 30},   // July: 30 days
            {2000, 8, 15, 31},   // August: 31 days (quarter middle month)
            {2000, 9, 15, 30},   // September: 30 days
            {2000, 10, 15, 30},  // October: 30 days
            {2000, 11, 15, 31},  // November: 31 days (quarter middle month)
            {2000, 12, 15, 30},  // December: 30 days (regular year)
            
            // Leap year (2004) - December has extra week
            {2004, 12, 20, 37},  // December in leap year: 37 days
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengthTestData")
    public void shouldReturnCorrectMonthLength(int year, int month, int dayOfMonth, int expectedLength) {
        Symmetry010Date date = Symmetry010Date.of(year, month, dayOfMonth);
        assertEquals(expectedLength, date.lengthOfMonth(),
            String.format("Month %d in year %d should have %d days", month, year, expectedLength));
    }

    @ParameterizedTest
    @MethodSource("monthLengthTestData")
    public void shouldReturnCorrectMonthLengthFromFirstDay(int year, int month, int dayOfMonth, int expectedLength) {
        Symmetry010Date firstDayOfMonth = Symmetry010Date.of(year, month, 1);
        assertEquals(expectedLength, firstDayOfMonth.lengthOfMonth(),
            String.format("Month %d in year %d should have %d days (tested from first day)", month, year, expectedLength));
    }

    @Test
    public void shouldHandleDecemberLengthInDifferentYearTypes() {
        assertEquals(30, Symmetry010Date.of(2000, 12, 1).lengthOfMonth(), 
            "December in regular year should have 30 days");
        assertEquals(37, Symmetry010Date.of(2004, 12, 1).lengthOfMonth(), 
            "December in leap year should have 37 days");
    }

    //-----------------------------------------------------------------------
    // Era and year handling tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldHandleErasAndYearsCorrectly() {
        // Test CE (Common Era) years
        for (int year = 1; year < 200; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, date.get(YEAR));
            assertEquals(IsoEra.CE, date.getEra());
            assertEquals(year, date.get(YEAR_OF_ERA));
            
            // Test era-based construction
            Symmetry010Date eraBasedDate = Symmetry010Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
            assertEquals(date, eraBasedDate);
        }

        // Test BCE (Before Common Era) years
        for (int year = -200; year < 0; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, date.get(YEAR));
            assertEquals(IsoEra.BCE, date.getEra());
            assertEquals(1 - year, date.get(YEAR_OF_ERA)); // BCE year numbering
            
            // Test era-based construction
            Symmetry010Date eraBasedDate = Symmetry010Chronology.INSTANCE.date(IsoEra.BCE, year, 1, 1);
            assertEquals(date, eraBasedDate);
        }
    }

    @Test
    public void shouldHandleYearDayConstructionCorrectly() {
        for (int year = 1; year < 200; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.dateYearDay(year, 1);
            
            assertEquals(year, date.get(YEAR));
            assertEquals(IsoEra.CE, date.getEra());
            assertEquals(year, date.get(YEAR_OF_ERA));
            
            Symmetry010Date eraBasedDate = Symmetry010Chronology.INSTANCE.dateYearDay(IsoEra.CE, year, 1);
            assertEquals(date, eraBasedDate);
        }
    }

    @Test
    public void shouldCalculateProlepticYearsCorrectly() {
        assertEquals(4, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        assertEquals(3, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 3));
        assertEquals(2, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2));
        assertEquals(1, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
        assertEquals(2000, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1582));
    }

    /**
     * Eras from other calendar systems that should be rejected.
     */
    public static Object[][] unsupportedEras() {
        return new Era[][] {
            { AccountingEra.BCE }, { AccountingEra.CE },
            { CopticEra.BEFORE_AM }, { CopticEra.AM },
            { DiscordianEra.YOLD },
            { EthiopicEra.BEFORE_INCARNATION }, { EthiopicEra.INCARNATION },
            { HijrahEra.AH },
            { InternationalFixedEra.CE },
            { JapaneseEra.MEIJI }, { JapaneseEra.TAISHO }, { JapaneseEra.SHOWA }, { JapaneseEra.HEISEI },
            { JulianEra.BC }, { JulianEra.AD },
            { MinguoEra.BEFORE_ROC }, { MinguoEra.ROC },
            { PaxEra.BCE }, { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE }, { ThaiBuddhistEra.BE },
        };
    }

    @ParameterizedTest
    @MethodSource("unsupportedEras")
    public void shouldRejectUnsupportedEras(Era era) {
        assertThrows(ClassCastException.class, 
            () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4),
            "Should reject era from different calendar system: " + era);
    }

    @Test
    public void shouldProvideCorrectEraInformation() {
        assertEquals(IsoEra.BCE, Symmetry010Chronology.INSTANCE.eraOf(0));
        assertEquals(IsoEra.CE, Symmetry010Chronology.INSTANCE.eraOf(1));
        
        assertThrows(DateTimeException.class, 
            () -> Symmetry010Chronology.INSTANCE.eraOf(2),
            "Should reject invalid era value");

        List<Era> eras = Symmetry010Chronology.INSTANCE.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(IsoEra.BCE));
        assertTrue(eras.contains(IsoEra.CE));
    }

    //-----------------------------------------------------------------------
    // Field range validation tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldProvideCorrectChronologyFieldRanges() {
        Symmetry010Chronology chrono = Symmetry010Chronology.INSTANCE;
        
        // Day and week related ranges
        assertEquals(ValueRange.of(1, 7), chrono.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(1, 7), chrono.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(1, 4, 5), chrono.range(ALIGNED_WEEK_OF_MONTH)); // 4 weeks normal, 5 in leap month
        assertEquals(ValueRange.of(1, 52, 53), chrono.range(ALIGNED_WEEK_OF_YEAR)); // 52 weeks normal, 53 in leap year
        assertEquals(ValueRange.of(1, 7), chrono.range(DAY_OF_WEEK));
        
        // Date ranges
        assertEquals(ValueRange.of(1, 30, 37), chrono.range(DAY_OF_MONTH)); // 30 normal, 31 long months, 37 leap December
        assertEquals(ValueRange.of(1, 364, 371), chrono.range(DAY_OF_YEAR)); // 364 normal year, 371 leap year
        assertEquals(ValueRange.of(1, 12), chrono.range(MONTH_OF_YEAR));
        
        // Era and year ranges
        assertEquals(ValueRange.of(0, 1), chrono.range(ERA)); // BCE=0, CE=1
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), chrono.range(YEAR));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), chrono.range(YEAR_OF_ERA));
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), chrono.range(PROLEPTIC_MONTH));
    }

    //-----------------------------------------------------------------------
    // Date field value tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for field value calculations.
     * Format: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValueTestData() {
        return new Object[][] {
            // Basic field values for a mid-year date
            {2014, 5, 26, DAY_OF_WEEK, 2}, // Tuesday (Monday=1)
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26}, // Jan+Feb+Mar+Apr+26 days of May
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1}, // CE
            
            // Aligned week calculations
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            
            // Proleptic month calculation
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            
            // Year boundary test
            {1, 5, 8, ERA, 1}, // CE era
            
            // Quarter boundary tests
            {2012, 9, 26, DAY_OF_WEEK, 1}, // Monday (start of quarter pattern)
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
            
            // Leap week tests
            {2015, 12, 37, DAY_OF_WEEK, 5}, // Friday
            {2015, 12, 37, DAY_OF_MONTH, 37},
            {2015, 12, 37, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7}, // Full year plus leap week
            {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2},
            {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6}, // 6th week in December
            {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53}, // 53rd week of year
            {2015, 12, 37, MONTH_OF_YEAR, 12},
            {2015, 12, 37, PROLEPTIC_MONTH, 2016 * 12 - 1},
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValueTestData")
    public void shouldCalculateFieldValuesCorrectly(int year, int month, int day, TemporalField field, long expectedValue) {
        Symmetry010Date date = Symmetry010Date.of(year, month, day);
        assertEquals(expectedValue, date.getLong(field),
            String.format("Field %s for date %d/%d/%d should be %d", field, year, month, day, expectedValue));
    }

    @Test
    public void shouldRejectUnsupportedFieldsInGetLong() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.getLong(MINUTE_OF_DAY),
            "Should reject time-based fields");
    }

    //-----------------------------------------------------------------------
    // Date field range tests for specific dates
    //-----------------------------------------------------------------------
    
    /**
     * Test data for field ranges that depend on the specific date.
     * Format: {year, month, day, field, expectedRange}
     */
    public static Object[][] dateSpecificRangeTestData() {
        return new Object[][] {
            // Day of month ranges for different month types
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},    // 30-day month
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},    // 31-day month
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},    // 31-day month
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},   // December in regular year
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)},   // December in leap year
            
            // Day of year ranges
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},    // Regular year
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},    // Leap year
            
            // Week-related ranges
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},     // Regular month
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},     // 31-day month still 4 weeks
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},    // Leap December has 5 weeks
            
            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},     // Regular year
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},    // Leap year
            
            // Consistent ranges regardless of date
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
        };
    }

    @ParameterizedTest
    @MethodSource("dateSpecificRangeTestData")
    public void shouldProvideCorrectDateSpecificRanges(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
        Symmetry010Date date = Symmetry010Date.of(year, month, day);
        assertEquals(expectedRange, date.range(field),
            String.format("Range for field %s on date %d/%d/%d should be %s", field, year, month, day, expectedRange));
    }

    @Test
    public void shouldRejectUnsupportedFieldsInRange() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.range(MINUTE_OF_DAY),
            "Should reject time-based fields in range queries");
    }

    //-----------------------------------------------------------------------
    // Date modification tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for date field modifications.
     * Format: {originalYear, originalMonth, originalDay, field, newValue, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] dateModificationTestData() {
        return new Object[][] {
            // Day of week modifications
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20}, // Change to Monday
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 24}, // Change to Friday
            
            // Day of month modifications
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26}, // No change
            
            // Day of year modifications
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30}, // Last day of regular year
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 17},
            
            // Month modifications
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26}, // No change
            
            // Year modifications
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 1, 2014, 5, 26}, // No change (already CE)
            
            // Proleptic month modifications
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            
            // Aligned week/day modifications
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 21},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            
            // Leap year specific tests
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28},
            {2015, 12, 29, DAY_OF_WEEK, 6, 2015, 12, 29}, // Leap week day
            {2015, 12, 37, YEAR, 2004, 2004, 12, 37}, // Move leap day to another leap year
            {2015, 12, 37, YEAR, 2013, 2013, 12, 30}, // Move leap day to regular year (clamps to last valid day)
        };
    }

    @ParameterizedTest
    @MethodSource("dateModificationTestData")
    public void shouldModifyDateFieldsCorrectly(int originalYear, int originalMonth, int originalDay,
            TemporalField field, long newValue,
            int expectedYear, int expectedMonth, int expectedDay) {
        
        Symmetry010Date originalDate = Symmetry010Date.of(originalYear, originalMonth, originalDay);
        Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry010Date actualDate = originalDate.with(field, newValue);
        
        assertEquals(expectedDate, actualDate,
            String.format("Setting %s to %d on %d/%d/%d should result in %d/%d/%d", 
                field, newValue, originalYear, originalMonth, originalDay, expectedYear, expectedMonth, expectedDay));
    }

    /**
     * Test data for invalid field modifications that should be rejected.
     * Format: {year, month, day, field, invalidValue}
     */
    public static Object[][] invalidModificationTestData() {
        return new Object[][] {
            // Invalid day of week
            {2013, 1, 1, DAY_OF_WEEK, -1}, {2013, 1, 1, DAY_OF_WEEK, 8},
            
            // Invalid day of month
            {2013, 1, 1, DAY_OF_MONTH, -1}, {2013, 1, 1, DAY_OF_MONTH, 31}, // January only has 30 days
            {2013, 6, 1, DAY_OF_MONTH, 31}, {2015, 12, 1, DAY_OF_MONTH, 38}, // December leap year limit
            
            // Invalid day of year
            {2013, 1, 1, DAY_OF_YEAR, -1}, {2013, 1, 1, DAY_OF_YEAR, 365}, // Regular year max is 364
            {2015, 1, 1, DAY_OF_YEAR, 372}, // Leap year max is 371
            
            // Invalid month
            {2013, 1, 1, MONTH_OF_YEAR, -1}, {2013, 1, 1, MONTH_OF_YEAR, 13},
            
            // Invalid year (outside supported range)
            {2013, 1, 1, YEAR, -1_000_001}, {2013, 1, 1, YEAR, 1_000_001},
            
            // Invalid aligned values
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1}, {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, -1}, {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, -1}, {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},
        };
    }

    @ParameterizedTest
    @MethodSource("invalidModificationTestData")
    public void shouldRejectInvalidFieldModifications(int year, int month, int day, TemporalField field, long invalidValue) {
        Symmetry010Date date = Symmetry010Date.of(year, month, day);
        assertThrows(DateTimeException.class, 
            () -> date.with(field, invalidValue),
            String.format("Should reject invalid %s value %d for date %d/%d/%d", field, invalidValue, year, month, day));
    }

    @Test
    public void shouldRejectUnsupportedFieldsInWith() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.with(MINUTE_OF_DAY, 10),
            "Should reject time-based fields in with() operations");
    }

    //-----------------------------------------------------------------------
    // Temporal adjuster tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for last day of month adjustments.
     * Format: {year, month, day, expectedLastDayYear, expectedLastDayMonth, expectedLastDay}
     */
    public static Object[][] lastDayOfMonthTestData() {
        return new Object[][] {
            // Regular months (30 days)
            {2012, 1, 23, 2012, 1, 30}, {2012, 3, 23, 2012, 3, 30}, {2012, 4, 23, 2012, 4, 30},
            {2012, 6, 23, 2012, 6, 30}, {2012, 7, 23, 2012, 7, 30}, {2012, 9, 23, 2012, 9, 30},
            {2012, 10, 23, 2012, 10, 30}, {2012, 12, 23, 2012, 12, 30},
            
            // Long months (31 days) - quarter middle months
            {2012, 2, 23, 2012, 2, 31}, {2012, 5, 23, 2012, 5, 31}, 
            {2012, 8, 23, 2012, 8, 31}, {2012, 11, 23, 2012, 11, 31},
            
            // Leap year December (37 days)
            {2009, 12, 23, 2009, 12, 37},
        };
    }

    @ParameterizedTest
    @MethodSource("lastDayOfMonthTestData")
    public void shouldAdjustToLastDayOfMonth(int year, int month, int day, 
            int expectedYear, int expectedMonth, int expectedDay) {
        
        Symmetry010Date originalDate = Symmetry010Date.of(year, month, day);
        Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry010Date adjustedDate = originalDate.with(TemporalAdjusters.lastDayOfMonth());
        
        assertEquals(expectedDate, adjustedDate,
            String.format("Last day of month adjustment for %d/%d/%d should result in %d/%d/%d", 
                year, month, day, expectedYear, expectedMonth, expectedDay));
    }

    //-----------------------------------------------------------------------
    // Cross-calendar adjustment tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldConvertToIsoDateCorrectly() {
        Symmetry010Date sym010Date = Symmetry010Date.of(2000, 1, 4);
        Symmetry010Date adjustedDate = sym010Date.with(LocalDate.of(2012, 7, 6));
        
        assertEquals(Symmetry010Date.of(2012, 7, 5), adjustedDate,
            "Should convert ISO date to equivalent Symmetry010 date");
    }

    @Test
    public void shouldRejectIncompatibleTemporalAdjusters() {
        Symmetry010Date sym010Date = Symmetry010Date.of(2000, 1, 4);
        assertThrows(DateTimeException.class, 
            () -> sym010Date.with(Month.APRIL),
            "Should reject Month enum as it's not compatible with Symmetry010 calendar");
    }

    @Test
    public void shouldAllowIsoDateToAdjustToSymmetry010() {
        Symmetry010Date sym010Date = Symmetry010Date.of(2012, 7, 19);
        LocalDate adjustedIsoDate = LocalDate.MIN.with(sym010Date);
        
        assertEquals(LocalDate.of(2012, 7, 20), adjustedIsoDate,
            "ISO LocalDate should be adjustable to Symmetry010 equivalent");
    }

    @Test
    public void shouldAllowIsoDateTimeToAdjustToSymmetry010() {
        Symmetry010Date sym010Date = Symmetry010Date.of(2012, 7, 19);
        LocalDateTime adjustedDateTime = LocalDateTime.MIN.with(sym010Date);
        
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), adjustedDateTime,
            "ISO LocalDateTime should be adjustable to Symmetry010 equivalent");
    }

    //-----------------------------------------------------------------------
    // Date arithmetic tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for date arithmetic operations.
     * Format: {year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] dateArithmeticTestData() {
        return new Object[][] {
            // Day arithmetic
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},    // No change
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},     // Add days within month
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},   // Subtract days within month
            
            // Week arithmetic
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},   // No change
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},   // Add weeks
            {2014, 5, 26, -5, WEEKS, 2014, 4, 21},  // Subtract weeks
            
            // Month arithmetic
            {2014, 5, 26, 0, MONTHS, 2014, 5, 26},  // No change
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},  // Add months
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26}, // Subtract months (cross year)
            
            // Year arithmetic
            {2014, 5, 26, 0, YEARS, 2014, 5, 26},   // No change
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},   // Add years
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},  // Subtract years
            
            // Larger time units
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},     // Add decades
            {2014, 5, 26, -5, DECADES, 1964, 5, 26},    // Subtract decades
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},   // Add centuries
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},  // Subtract centuries
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},   // Add millennia
            {2014, 5, 26, -1, MILLENNIA, 1014, 5, 26},  // Subtract millennia
            
            // Cross-year boundary tests
            {2014, 12, 26, 3, WEEKS, 2015, 1, 17},  // Cross year boundary with weeks
            {2014, 1, 26, -5, WEEKS, 2013, 12, 21}, // Cross year boundary backwards
            
            // Multi-year arithmetic
            {2012, 6, 21, 53, WEEKS, 2013, 6, 28},      // More than one year in weeks
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21}, // Multiple years
        };
    }

    @ParameterizedTest
    @MethodSource("dateArithmeticTestData")
    public void shouldPerformDateArithmeticCorrectly(int year, int month, int day, 
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDay) {
        
        Symmetry010Date originalDate = Symmetry010Date.of(year, month, day);
        Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        
        // Test addition
        Symmetry010Date resultDate = originalDate.plus(amount, unit);
        assertEquals(expectedDate, resultDate,
            String.format("Adding %d %s to %d/%d/%d should result in %d/%d/%d", 
                amount, unit, year, month, day, expectedYear, expectedMonth, expectedDay));
        
        // Test subtraction (reverse operation)
        Symmetry010Date reversedDate = expectedDate.minus(amount, unit);
        assertEquals(originalDate, reversedDate,
            String.format("Subtracting %d %s from %d/%d/%d should result in %d/%d/%d", 
                amount, unit, expectedYear, expectedMonth, expectedDay, year, month, day));
    }

    /**
     * Test data for leap week arithmetic.
     * Format: {year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] leapWeekArithmeticTestData() {
        return new Object[][] {
            // Arithmetic within leap week
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},   // No change
            {2015, 12, 28, 8, DAYS, 2015, 12, 36},   // Add days within leap week
            {2015, 12, 28, -3, DAYS, 2015, 12, 25},  // Subtract days
            
            // Week arithmetic crossing leap week
            {2015, 12, 28, 3, WEEKS, 2016, 1, 12},   // Add weeks from leap week
            {2015, 12, 28, -5, WEEKS, 2015, 11, 24}, // Subtract weeks to previous month
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21}, // Add full year of weeks
            
            // Month arithmetic from leap week
            {2015, 12, 28, 3, MONTHS, 2016, 3, 28},  // Add months from leap week
            {2015, 12, 28, -5, MONTHS, 2015, 7, 28}, // Subtract months
            {2015, 12, 28, 12, MONTHS, 2016, 12, 28}, // Add full year
            
            // Year arithmetic preserving leap week day
            {2015, 12, 28, 3, YEARS, 2018, 12, 28},  // Add years (leap day preserved if possible)
            {2015, 12, 28, -5, YEARS, 2010, 12, 28}, // Subtract years
        };
    }

    @ParameterizedTest
    @MethodSource("leapWeekArithmeticTestData")
    public void shouldHandleLeapWeekArithmeticCorrectly(int year, int month, int day, 
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDay) {
        
        Symmetry010Date originalDate = Symmetry010Date.of(year, month, day);
        Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        
        Symmetry010Date resultDate = originalDate.plus(amount, unit);
        assertEquals(expectedDate, resultDate,
            String.format("Adding %d %s to leap week date %d/%d/%d should result in %d/%d/%d", 
                amount, unit, year, month, day, expectedYear, expectedMonth, expectedDay));
    }

    @Test
    public void shouldRejectUnsupportedTemporalUnits() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.plus(0, MINUTES),
            "Should reject time-based temporal units");
    }

    //-----------------------------------------------------------------------
    // Period calculation tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for period calculations between dates.
     * Format: {startYear, startMonth, startDay, endYear, endMonth, endDay, unit, expectedAmount}
     */
    public static Object[][] periodCalculationTestData() {
        return new Object[][] {
            // Same date
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            
            // Day differences
            {2014, 5, 26, 2014, 6, 4, DAYS, 9},     // Forward
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},   // Backward
            
            // Week differences
            {2014, 5, 26, 2014, 6, 1, WEEKS, 1},    // Just over 1 week
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},    // Still 1 week (partial)
            
            // Month differences
            {2014, 5, 26, 2014, 6, 25, MONTHS, 0},  // Less than 1 month
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},  // Exactly 1 month
            
            // Year differences
            {2014, 5, 26, 2015, 5, 25, YEARS, 0},   // Less than 1 year
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},   // Exactly 1 year
            
            // Larger unit differences
            {2014, 5, 26, 2024, 5, 25, DECADES, 0}, // Less than 1 decade
            {2014, 5, 26, 2024, 5, 26, DECADES, 1}, // Exactly 1 decade
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1}, // 1 century
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, // 1 millennium
            
            // Era differences (should be 0 within same era)
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
        };
    }

    @ParameterizedTest
    @MethodSource("periodCalculationTestData")
    public void shouldCalculatePeriodsCorrectly(int startYear, int startMonth, int startDay,
            int endYear, int endMonth, int endDay,
            TemporalUnit unit, long expectedAmount) {
        
        Symmetry010Date startDate = Symmetry010Date.of(startYear, startMonth, startDay);
        Symmetry010Date endDate = Symmetry010Date.of(endYear, endMonth, endDay);
        
        long actualAmount = startDate.until(endDate, unit);
        assertEquals(expectedAmount, actualAmount,
            String.format("Period from %d/%d/%d to %d/%d/%d in %s should be %d", 
                startYear, startMonth, startDay, endYear, endMonth, endDay, unit, expectedAmount));
    }

    /**
     * Test data for detailed period calculations (years, months, days).
     * Format: {startYear, startMonth, startDay, endYear, endMonth, endDay, expectedYears, expectedMonths, expectedDays}
     */
    public static Object[][] detailedPeriodTestData() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},    // Same date
            {2014, 5, 26, 2014, 6, 4, 0, 0, 9},     // 9 days later
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6},   // 6 days earlier
            {2014, 5, 26, 2014, 6, 5, 0, 0, 10},    // 10 days later
            {2014, 5, 26, 2014, 6, 25, 0, 0, 30},   // 30 days later (end of month)
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},    // Exactly 1 month
            {2014, 5, 26, 2015, 5, 25, 0, 11, 29},  // 11 months, 29 days
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},    // Exactly 1 year
            {2014, 5, 26, 2024, 5, 25, 9, 11, 29},  // 9 years, 11 months, 29 days
        };
    }

    @ParameterizedTest
    @MethodSource("detailedPeriodTestData")
    public void shouldCalculateDetailedPeriodsCorrectly(int startYear, int startMonth, int startDay,
            int endYear, int endMonth, int endDay,
            int expectedYears, int expectedMonths, int expectedDays) {
        
        Symmetry010Date startDate = Symmetry010Date.of(startYear, startMonth, startDay);
        Symmetry010Date endDate = Symmetry010Date.of(endYear, endMonth, endDay);
        
        ChronoPeriod expectedPeriod = Symmetry010Chronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
        ChronoPeriod actualPeriod = startDate.until(endDate);
        
        assertEquals(expectedPeriod, actualPeriod,
            String.format("Detailed period from %d/%d/%d to %d/%d/%d should be %d years, %d months, %d days", 
                startYear, startMonth, startDay, endYear, endMonth, endDay, expectedYears, expectedMonths, expectedDays));
    }

    @Test
    public void shouldRejectUnsupportedTemporalUnitsInUntil() {
        Symmetry010Date startDate = Symmetry010Date.of(2012, 6, 28);
        Symmetry010Date endDate = Symmetry010Date.of(2012, 7, 1);
        
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> startDate.until(endDate, MINUTES),
            "Should reject time-based temporal units in until() calculations");
    }

    //-----------------------------------------------------------------------
    // Period arithmetic tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldAddSymmetry010PeriodsCorrectly() {
        Symmetry010Date baseDate = Symmetry010Date.of(2014, 5, 21);
        ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(0, 2, 8);
        Symmetry010Date expectedDate = Symmetry010Date.of(2014, 7, 29);
        
        Symmetry010Date resultDate = baseDate.plus(period);
        assertEquals(expectedDate, resultDate,
            "Adding Symmetry010 period should work correctly");
    }

    @Test
    public void shouldRejectIsoPeriodsInArithmetic() {
        Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);
        
        assertThrows(DateTimeException.class, 
            () -> date.plus(isoPeriod),
            "Should reject ISO Period objects");
    }

    @Test
    public void shouldSubtractSymmetry010PeriodsCorrectly() {
        Symmetry010Date baseDate = Symmetry010Date.of(2014, 5, 26);
        ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(0, 2, 3);
        Symmetry010Date expectedDate = Symmetry010Date.of(2014, 3, 23);
        
        Symmetry010Date resultDate = baseDate.minus(period);
        assertEquals(expectedDate, resultDate,
            "Subtracting Symmetry010 period should work correctly");
    }

    @Test
    public void shouldRejectIsoPeriodsInSubtraction() {
        Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);
        
        assertThrows(DateTimeException.class, 
            () -> date.minus(isoPeriod),
            "Should reject ISO Period objects in subtraction");
    }

    //-----------------------------------------------------------------------
    // Equality and hash code tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementEqualsAndHashCodeCorrectly() {
        new EqualsTester()
            .addEqualityGroup(Symmetry010Date.of(2000, 1, 3), Symmetry010Date.of(2000, 1, 3))
            .addEqualityGroup(Symmetry010Date.of(2000, 1, 4), Symmetry010Date.of(2000, 1, 4))
            .addEqualityGroup(Symmetry010Date.of(2000, 2, 3), Symmetry010Date.of(2000, 2, 3))
            .addEqualityGroup(Symmetry010Date.of(2000, 6, 23), Symmetry010Date.of(2000, 6, 23))
            .addEqualityGroup(Symmetry010Date.of(2000, 6, 28), Symmetry010Date.of(2000, 6, 28))
            .addEqualityGroup(Symmetry010Date.of(2000, 7, 1), Symmetry010Date.of(2000, 7, 1))
            .addEqualityGroup(Symmetry010Date.of(2000, 12, 25), Symmetry010Date.of(2000, 12, 25))
            .addEqualityGroup(Symmetry010Date.of(2000, 12, 28), Symmetry010Date.of(2000, 12, 28))
            .addEqualityGroup(Symmetry010Date.of(2001, 1, 1), Symmetry010Date.of(2001, 1, 1))
            .addEqualityGroup(Symmetry010Date.of(2001, 1, 3), Symmetry010Date.of(2001, 1, 3))
            .addEqualityGroup(Symmetry010Date.of(2001, 12, 28), Symmetry010Date.of(2001, 12, 28))
            .addEqualityGroup(Symmetry010Date.of(2004, 6, 28), Symmetry010Date.of(2004, 6, 28))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // String representation tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for string representations.
     * Format: {date, expectedString}
     */
    public static Object[][] stringRepresentationTestData() {
        return new Object[][] {
            {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
            {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
            {Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"},
            {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"}, // Leap week day
        };
    }

    @ParameterizedTest
    @MethodSource("stringRepresentationTestData")
    public void shouldFormatDatesCorrectly(Symmetry010Date date, String expectedString) {
        assertEquals(expectedString, date.toString(),
            "String representation should follow expected format");
    }
}