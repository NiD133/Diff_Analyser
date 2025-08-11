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
 * Tests for the Symmetry454 calendar system.
 * 
 * The Symmetry454 calendar is a proposed calendar reform where:
 * - Each quarter has exactly 13 weeks (91 days)
 * - Months follow a 4-5-4 week pattern (28-35-28 days)
 * - Normal years have 364 days (52 weeks)
 * - Leap years add a "leap week" making 371 days (53 weeks)
 * - Every month starts on Monday
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry454Chronology {

    // Constants for better readability
    private static final int NORMAL_MONTH_DAYS = 28;
    private static final int LONG_MONTH_DAYS = 35;
    private static final int NORMAL_YEAR_DAYS = 364;
    private static final int LEAP_YEAR_DAYS = 371;

    //-----------------------------------------------------------------------
    // Basic Chronology Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCreateSymmetry454ChronologyFromId() {
        // Given: The chronology ID "Sym454"
        String chronologyId = "Sym454";
        
        // When: Creating chronology from ID
        Chronology chrono = Chronology.of(chronologyId);
        
        // Then: Should return the Symmetry454 chronology instance
        assertNotNull(chrono);
        assertEquals(Symmetry454Chronology.INSTANCE, chrono);
        assertEquals("Sym454", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Date Conversion Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data containing historical dates and their Symmetry454 equivalents.
     * Format: {Symmetry454Date, equivalent LocalDate (ISO), description}
     */
    public static Object[][] historicalDateConversions() {
        return new Object[][] {
            // Basic epoch alignment
            { Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1), "Start of Common Era" },
            
            // Historical figures and events with their birth/event dates
            { Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27), "Constantine the Great birth" },
            { Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2), "Charlemagne birth" },
            { Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14), "Battle of Hastings" },
            { Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15), "Leonardo da Vinci birth" },
            { Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12), "Columbus reaches Americas" },
            { Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26), "Shakespeare baptism" },
            { Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4), "Isaac Newton birth" },
            { Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14), "Storming of Bastille" },
            { Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14), "Albert Einstein birth" },
            
            // Modern era
            { Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1), "Unix epoch start" },
            { Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1), "Y2K / 21st century start" },
        };
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldConvertSymmetry454DateToLocalDate(Symmetry454Date sym454, LocalDate iso, String description) {
        assertEquals(iso, LocalDate.from(sym454), 
            "Failed converting Symmetry454 to LocalDate for: " + description);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldConvertLocalDateToSymmetry454Date(Symmetry454Date sym454, LocalDate iso, String description) {
        assertEquals(sym454, Symmetry454Date.from(iso), 
            "Failed converting LocalDate to Symmetry454 for: " + description);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCreateSymmetry454DateFromEpochDay(Symmetry454Date sym454, LocalDate iso, String description) {
        assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()),
            "Failed creating Symmetry454Date from epoch day for: " + description);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCalculateCorrectEpochDay(Symmetry454Date sym454, LocalDate iso, String description) {
        assertEquals(iso.toEpochDay(), sym454.toEpochDay(),
            "Epoch day calculation failed for: " + description);
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------
    
    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCalculateZeroPeriodBetweenSameDates(Symmetry454Date sym454, LocalDate iso, String description) {
        ChronoPeriod zeroPeriod = Symmetry454Chronology.INSTANCE.period(0, 0, 0);
        assertEquals(zeroPeriod, sym454.until(sym454),
            "Same date should have zero period for: " + description);
        assertEquals(zeroPeriod, sym454.until(iso),
            "Equivalent dates should have zero period for: " + description);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldAddAndSubtractDaysCorrectly(Symmetry454Date sym454, LocalDate iso, String description) {
        // Test adding days
        assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)),
            "Adding 1 day failed for: " + description);
        assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)),
            "Adding 35 days failed for: " + description);
        
        // Test subtracting days
        assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)),
            "Subtracting 1 day failed for: " + description);
        assertEquals(iso.minusDays(60), LocalDate.from(sym454.minus(60, DAYS)),
            "Subtracting 60 days failed for: " + description);
    }

    @ParameterizedTest
    @MethodSource("historicalDateConversions")
    public void shouldCalculateDaysBetweenDatesCorrectly(Symmetry454Date sym454, LocalDate iso, String description) {
        assertEquals(0, sym454.until(iso.plusDays(0), DAYS),
            "Zero days difference failed for: " + description);
        assertEquals(1, sym454.until(iso.plusDays(1), DAYS),
            "One day difference failed for: " + description);
        assertEquals(35, sym454.until(iso.plusDays(35), DAYS),
            "35 days difference failed for: " + description);
        assertEquals(-40, sym454.until(iso.minusDays(40), DAYS),
            "Negative 40 days difference failed for: " + description);
    }

    //-----------------------------------------------------------------------
    // Invalid Date Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for invalid date combinations that should throw exceptions.
     * Format: {year, month, day, reason}
     */
    public static Object[][] invalidDateCombinations() {
        return new Object[][] {
            // Invalid years
            {-1, 13, 28, "Negative year with invalid month"},
            
            // Invalid months
            {2000, -2, 1, "Negative month"},
            {2000, 0, 1, "Zero month"},
            {2000, 13, 1, "Month too high"},
            {2000, 15, 1, "Month way too high"},
            
            // Invalid days
            {2000, 1, -1, "Negative day"},
            {2000, 1, 0, "Zero day"},
            
            // Days exceeding month limits
            {2000, 1, 29, "Day 29 in 28-day month (January)"},
            {2000, 2, 36, "Day 36 in 35-day month (February)"},
            {2000, 3, 29, "Day 29 in 28-day month (March)"},
            {2000, 12, 29, "Day 29 in non-leap year December"},
        };
    }

    @ParameterizedTest
    @MethodSource("invalidDateCombinations")
    public void shouldRejectInvalidDateCombinations(int year, int month, int day, String reason) {
        assertThrows(DateTimeException.class, 
            () -> Symmetry454Date.of(year, month, day),
            "Should reject invalid date: " + reason);
    }

    /**
     * Years that should NOT have leap weeks (29th day in December).
     */
    public static Object[][] nonLeapYears() {
        return new Object[][] {
            {1, "Year 1"},
            {100, "Year 100"},
            {200, "Year 200"},
            {2000, "Year 2000"}
        };
    }

    @ParameterizedTest
    @MethodSource("nonLeapYears")
    public void shouldRejectLeapDayInNonLeapYears(int year, String description) {
        assertThrows(DateTimeException.class, 
            () -> Symmetry454Date.of(year, 12, 29),
            "Should reject leap day in non-leap year: " + description);
    }

    @Test
    public void shouldRejectInvalidDayOfYear() {
        assertThrows(DateTimeException.class, 
            () -> Symmetry454Chronology.INSTANCE.dateYearDay(2000, 365),
            "Should reject day 365 in non-leap year");
    }

    //-----------------------------------------------------------------------
    // Leap Year Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldIdentifyLeapYearsCorrectly() {
        // Test specific known leap years and non-leap years
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(3), "Year 3 should be leap year");
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(6), "Year 6 should not be leap year");
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(9), "Year 9 should be leap year");
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2000), "Year 2000 should not be leap year");
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2004), "Year 2004 should be leap year");
    }

    @Test
    public void shouldIdentifyLeapWeekDaysCorrectly() {
        // Test that days 29-35 in December of leap year 2015 are leap week days
        for (int day = 29; day <= 35; day++) {
            assertTrue(Symmetry454Date.of(2015, 12, day).isLeapWeek(),
                "Day " + day + " in December 2015 should be in leap week");
        }
    }

    //-----------------------------------------------------------------------
    // Month Length Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for month lengths in the Symmetry454 calendar.
     * Format: {year, month, sampleDay, expectedLength, description}
     */
    public static Object[][] monthLengthTestData() {
        return new Object[][] {
            // Normal year pattern: 28-35-28, 28-35-28, 28-35-28, 28-35-28
            {2000, 1, 15, NORMAL_MONTH_DAYS, "January (normal month)"},
            {2000, 2, 15, LONG_MONTH_DAYS, "February (long month)"},
            {2000, 3, 15, NORMAL_MONTH_DAYS, "March (normal month)"},
            {2000, 4, 15, NORMAL_MONTH_DAYS, "April (normal month)"},
            {2000, 5, 15, LONG_MONTH_DAYS, "May (long month)"},
            {2000, 6, 15, NORMAL_MONTH_DAYS, "June (normal month)"},
            {2000, 7, 15, NORMAL_MONTH_DAYS, "July (normal month)"},
            {2000, 8, 15, LONG_MONTH_DAYS, "August (long month)"},
            {2000, 9, 15, NORMAL_MONTH_DAYS, "September (normal month)"},
            {2000, 10, 15, NORMAL_MONTH_DAYS, "October (normal month)"},
            {2000, 11, 15, LONG_MONTH_DAYS, "November (long month)"},
            {2000, 12, 15, NORMAL_MONTH_DAYS, "December in normal year"},
            
            // Leap year December
            {2004, 12, 15, LONG_MONTH_DAYS, "December in leap year"},
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengthTestData")
    public void shouldCalculateMonthLengthCorrectly(int year, int month, int day, int expectedLength, String description) {
        assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth(),
            "Month length calculation failed for: " + description);
    }

    @Test
    public void shouldCalculateDecemberLengthBasedOnLeapYear() {
        assertEquals(NORMAL_MONTH_DAYS, Symmetry454Date.of(2000, 12, 28).lengthOfMonth(),
            "December in non-leap year should have 28 days");
        assertEquals(LONG_MONTH_DAYS, Symmetry454Date.of(2004, 12, 28).lengthOfMonth(),
            "December in leap year should have 35 days");
    }

    //-----------------------------------------------------------------------
    // Era and Year Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldHandleErasCorrectlyForPositiveYears() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date date = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, date.get(YEAR), "Year field should match for year " + year);
            assertEquals(IsoEra.CE, date.getEra(), "Should be CE era for year " + year);
            assertEquals(year, date.get(YEAR_OF_ERA), "Year of era should match for year " + year);
            
            // Test era-based construction
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
            assertEquals(date, eraBased, "Era-based construction should match for year " + year);
        }
    }

    @Test
    public void shouldHandleErasCorrectlyForNegativeYears() {
        for (int year = -200; year < 0; year++) {
            Symmetry454Date date = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            
            assertEquals(year, date.get(YEAR), "Year field should match for year " + year);
            assertEquals(IsoEra.BCE, date.getEra(), "Should be BCE era for year " + year);
            assertEquals(1 - year, date.get(YEAR_OF_ERA), "Year of era should be 1-year for year " + year);
            
            // Test era-based construction
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.BCE, year, 1, 1);
            assertEquals(date, eraBased, "Era-based construction should match for year " + year);
        }
    }

    @Test
    public void shouldCalculateProlepticYearCorrectly() {
        assertEquals(4, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        assertEquals(2000, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1582));
    }

    @Test
    public void shouldProvideCorrectEraList() {
        List<Era> eras = Symmetry454Chronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "Should have exactly 2 eras");
        assertTrue(eras.contains(IsoEra.BCE), "Should contain BCE era");
        assertTrue(eras.contains(IsoEra.CE), "Should contain CE era");
    }

    @Test
    public void shouldCreateEraFromValue() {
        assertEquals(IsoEra.BCE, Symmetry454Chronology.INSTANCE.eraOf(0));
        assertEquals(IsoEra.CE, Symmetry454Chronology.INSTANCE.eraOf(1));
    }

    @Test
    public void shouldRejectInvalidEraValue() {
        assertThrows(DateTimeException.class, 
            () -> Symmetry454Chronology.INSTANCE.eraOf(2),
            "Should reject invalid era value");
    }

    //-----------------------------------------------------------------------
    // Field Range Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldProvideCorrectFieldRanges() {
        Symmetry454Chronology chrono = Symmetry454Chronology.INSTANCE;
        
        // Day and week related ranges
        assertEquals(ValueRange.of(1, 7), chrono.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, NORMAL_MONTH_DAYS, LONG_MONTH_DAYS), chrono.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, NORMAL_YEAR_DAYS, LEAP_YEAR_DAYS), chrono.range(DAY_OF_YEAR));
        
        // Month and year ranges
        assertEquals(ValueRange.of(1, 12), chrono.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), chrono.range(ERA));
        
        // Week ranges
        assertEquals(ValueRange.of(1, 4, 5), chrono.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 52, 53), chrono.range(ALIGNED_WEEK_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    // Field Value Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for field value calculations.
     * Format: {year, month, day, field, expectedValue, description}
     */
    public static Object[][] fieldValueTestData() {
        return new Object[][] {
            // Basic field values
            {2014, 5, 26, DAY_OF_WEEK, 5, "Day of week (Friday)"},
            {2014, 5, 26, DAY_OF_MONTH, 26, "Day of month"},
            {2014, 5, 26, MONTH_OF_YEAR, 5, "Month of year (May)"},
            {2014, 5, 26, YEAR, 2014, "Year"},
            {2014, 5, 26, ERA, 1, "Era (CE)"},
            
            // Day of year calculation: Jan(28) + Feb(35) + Mar(28) + Apr(28) + May(26) = 145
            {2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26, "Day of year"},
            
            // Aligned week calculations
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, "Aligned week of month"},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, "Aligned day of week in month"},
            
            // Leap week tests
            {2015, 12, 35, DAY_OF_WEEK, 7, "Last day of leap week (Sunday)"},
            {2015, 12, 35, DAY_OF_MONTH, 35, "Last day of leap month"},
            {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5, "5th week in leap December"},
            {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53, "53rd week in leap year"},
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValueTestData")
    public void shouldCalculateFieldValuesCorrectly(int year, int month, int day, TemporalField field, long expectedValue, String description) {
        assertEquals(expectedValue, Symmetry454Date.of(year, month, day).getLong(field),
            "Field value calculation failed for: " + description);
    }

    @Test
    public void shouldRejectUnsupportedFieldAccess() {
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> Symmetry454Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY),
            "Should reject unsupported field access");
    }

    //-----------------------------------------------------------------------
    // Date Adjustment Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldAdjustToLastDayOfMonth() {
        // Test last day of month adjustment for different month types
        assertEquals(Symmetry454Date.of(2012, 1, 28), 
            Symmetry454Date.of(2012, 1, 15).with(TemporalAdjusters.lastDayOfMonth()),
            "Should adjust to last day of 28-day month");
            
        assertEquals(Symmetry454Date.of(2012, 2, 35), 
            Symmetry454Date.of(2012, 2, 15).with(TemporalAdjusters.lastDayOfMonth()),
            "Should adjust to last day of 35-day month");
            
        assertEquals(Symmetry454Date.of(2009, 12, 35), 
            Symmetry454Date.of(2009, 12, 15).with(TemporalAdjusters.lastDayOfMonth()),
            "Should adjust to last day of leap December");
    }

    @Test
    public void shouldAdjustBetweenCalendarSystems() {
        Symmetry454Date sym454 = Symmetry454Date.of(2000, 1, 4);
        
        // Adjust Symmetry454 date using LocalDate
        Symmetry454Date adjusted = sym454.with(LocalDate.of(2012, 7, 6));
        assertEquals(Symmetry454Date.of(2012, 7, 5), adjusted,
            "Should adjust Symmetry454 date using LocalDate");
        
        // Adjust LocalDate using Symmetry454 date
        LocalDate localAdjusted = LocalDate.MIN.with(Symmetry454Date.of(2012, 7, 19));
        assertEquals(LocalDate.of(2012, 7, 20), localAdjusted,
            "Should adjust LocalDate using Symmetry454 date");
    }

    //-----------------------------------------------------------------------
    // Period Arithmetic Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldAddPeriodsCorrectly() {
        Symmetry454Date baseDate = Symmetry454Date.of(2014, 5, 21);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(0, 2, 8);
        
        assertEquals(Symmetry454Date.of(2014, 8, 1), baseDate.plus(period),
            "Should add Symmetry454 period correctly");
    }

    @Test
    public void shouldSubtractPeriodsCorrectly() {
        Symmetry454Date baseDate = Symmetry454Date.of(2014, 5, 26);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(0, 2, 3);
        
        assertEquals(Symmetry454Date.of(2014, 3, 23), baseDate.minus(period),
            "Should subtract Symmetry454 period correctly");
    }

    @Test
    public void shouldRejectISOPeriods() {
        Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
        
        assertThrows(DateTimeException.class, 
            () -> date.plus(Period.ofMonths(2)),
            "Should reject ISO period for addition");
            
        assertThrows(DateTimeException.class, 
            () -> date.minus(Period.ofMonths(2)),
            "Should reject ISO period for subtraction");
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------
    
    /**
     * Test data for string representation.
     * Format: {date, expectedString, description}
     */
    public static Object[][] stringRepresentationTestData() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01", "Start of era"},
            {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35", "Last day of February 1970"},
            {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35", "Last day of August 2000"},
            {Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35", "Leap week day"},
        };
    }

    @ParameterizedTest
    @MethodSource("stringRepresentationTestData")
    public void shouldFormatDatesCorrectly(Symmetry454Date date, String expectedString, String description) {
        assertEquals(expectedString, date.toString(),
            "String representation failed for: " + description);
    }

    //-----------------------------------------------------------------------
    // Equality Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementEqualsAndHashCodeCorrectly() {
        new EqualsTester()
            .addEqualityGroup(Symmetry454Date.of(2000, 1, 3), Symmetry454Date.of(2000, 1, 3))
            .addEqualityGroup(Symmetry454Date.of(2000, 1, 4), Symmetry454Date.of(2000, 1, 4))
            .addEqualityGroup(Symmetry454Date.of(2000, 2, 3), Symmetry454Date.of(2000, 2, 3))
            .addEqualityGroup(Symmetry454Date.of(2001, 1, 3), Symmetry454Date.of(2001, 1, 3))
            .addEqualityGroup(Symmetry454Date.of(2000, 12, 28), Symmetry454Date.of(2000, 12, 28))
            .addEqualityGroup(Symmetry454Date.of(2015, 12, 35), Symmetry454Date.of(2015, 12, 35))
            .testEquals();
    }
}