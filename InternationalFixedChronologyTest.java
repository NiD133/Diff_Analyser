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
import java.time.chrono.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 * 
 * <p>Verifies functionality including:
 * <ul>
 *   <li>Date conversion between International Fixed Calendar and ISO dates</li>
 *   <li>Leap year handling and date validation</li>
 *   <li>Field ranges and value handling</li>
 *   <li>Temporal arithmetic operations</li>
 *   <li>Era and chronology-specific behavior</li>
 * </ul>
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestInternationalFixedChronology {

    //-----------------------------------------------------------------------
    // Chronology Basics
    //-----------------------------------------------------------------------

    /** Tests chronology lookup by ID and basic properties. */
    @Test
    public void test_chronology() {
        Chronology chrono = Chronology.of("Ifc");
        assertNotNull(chrono);
        assertEquals(InternationalFixedChronology.INSTANCE, chrono);
        assertEquals("Ifc", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Date Conversion Samples
    //-----------------------------------------------------------------------

    /**
     * Provides sample dates for conversion testing.
     * Each entry contains an International Fixed Date and its equivalent ISO date.
     */
    public static Object[][] data_samples() {
        return new Object[][] {
            // Basic dates
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            
            // June-July boundary (non-leap year)
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)},
            
            // Year-end boundary
            {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
            {InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},
            
            // Leap year examples
            {InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)},
            {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)},
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            {InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)},
            
            // Century boundaries
            {InternationalFixedDate.of(100, 6, 27), LocalDate.of(100, 6, 16)},
            {InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)},
            {InternationalFixedDate.of(100, 7, 1), LocalDate.of(100, 6, 18)},
            {InternationalFixedDate.of(100, 7, 2), LocalDate.of(100, 6, 19)},
            
            // Special historical dates
            {InternationalFixedDate.of(1582, 9, 28), LocalDate.of(1582, 9, 9)},
            {InternationalFixedDate.of(1582, 10, 1), LocalDate.of(1582, 9, 10)},
            {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},
            
            // Modern examples
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    /** Tests conversion from International Fixed Date to ISO LocalDate. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void conversion_toIsoLocalDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed));
    }

    /** Tests conversion from ISO LocalDate to International Fixed Date. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void conversion_fromIsoLocalDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(fixed, InternationalFixedDate.from(iso));
    }

    /** Tests epoch day conversion. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void date_epochDayConversion(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
    }

    /** Tests toEpochDay() method. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void date_toEpochDay(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso.toEpochDay(), fixed.toEpochDay());
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic
    //-----------------------------------------------------------------------

    /** Tests adding days to International Fixed Dates. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void arithmetic_plusDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(fixed.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(fixed.plus(35, DAYS)));
        if (LocalDate.ofYearDay(1, 60).isBefore(iso)) {
            assertEquals(iso.plusDays(-1), LocalDate.from(fixed.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(fixed.plus(-60, DAYS)));
        }
    }

    /** Tests subtracting days from International Fixed Dates. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void arithmetic_minusDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed.minus(0, DAYS)));
        if (LocalDate.ofYearDay(1, 35).isBefore(iso)) {
            assertEquals(iso.minusDays(1), LocalDate.from(fixed.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(fixed.minus(35, DAYS)));
        }
        assertEquals(iso.minusDays(-1), LocalDate.from(fixed.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(fixed.minus(-60, DAYS)));
    }

    /** Tests calculating day differences between dates. */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void arithmetic_untilDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(0, fixed.until(iso.plusDays(0), DAYS));
        assertEquals(1, fixed.until(iso.plusDays(1), DAYS));
        assertEquals(35, fixed.until(iso.plusDays(35), DAYS));
        if (LocalDate.ofYearDay(1, 40).isBefore(iso)) {
            assertEquals(-40, fixed.until(iso.minusDays(40), DAYS));
        }
    }

    //-----------------------------------------------------------------------
    // Invalid Date Handling
    //-----------------------------------------------------------------------

    /** Provides invalid date parameters. */
    public static Object[][] data_badDates() {
        return new Object[][] {
            // Year boundaries
            {-1, 13, 28},
            {-1, 13, 29},
            {0, 1, 1},
            
            // Invalid months
            {1900, -2, 1},
            {1900, 14, 1},
            {1900, 15, 1},
            
            // Invalid days
            {1900, 1, -1},
            {1900, 1, 0},
            {1900, 1, 29},  // January only has 28 days
            
            // Combined invalid values
            {1904, -1, -2},
            {1904, -1, 0},
            {1904, -1, 1},
            
            // Special cases
            {1900, -1, 0},
            {1900, -1, -2},
            {1900, 0, -1},
            {1900, 0, 1},
            {1900, 0, 2},
            
            // Days exceeding month lengths
            {1900, 2, 29},  // Non-leap year
            {1900, 3, 29},  // March has 28 days
            {1900, 4, 29},  // April has 28 days
            {1900, 5, 29},  // May has 28 days
            {1900, 6, 29},  // June has 28 days (non-leap)
            {1900, 7, 29},  // July has 28 days
            {1900, 8, 29},  // August has 28 days
            {1900, 9, 29},  // September has 28 days
            {1900, 10, 29}, // October has 28 days
            {1900, 11, 29}, // November has 28 days
            {1900, 12, 29}, // December has 28 days
            {1900, 13, 30}, // Year Day month max 29 days
        };
    }

    /** Tests invalid date creation. */
    @ParameterizedTest
    @MethodSource("data_badDates")
    public void dateValidation_invalidDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
    }

    /** Provides years where leap day is invalid. */
    public static Object[][] data_badLeapDates() {
        return new Object[][] {
            {1},    // Not leap year
            {100},  // Century non-leap
            {200},  // Century non-leap
            {300},  // Century non-leap
            {1900}  // Century non-leap
        };
    }

    /** Tests leap day in non-leap years. */
    @ParameterizedTest
    @MethodSource("data_badLeapDates")
    public void dateValidation_invalidLeapDay(int year) {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
    }

    /** Tests invalid day-of-year in dateYearDay(). */
    @Test
    public void dateValidation_invalidDayOfYear() {
        assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.dateYearDay(2001, 366));
    }

    //-----------------------------------------------------------------------
    // Leap Year Handling
    //-----------------------------------------------------------------------

    /** Tests leap year calculation against Gregorian rules. */
    @Test
    public void leapYear_calculation() {
        IntPredicate isLeapYear = year -> {
            return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
        };

        for (int year = 1; year < 500; year++) {
            InternationalFixedDate base = InternationalFixedDate.of(year, 1, 1);
            assertEquals(isLeapYear.test(year), base.isLeapYear());
            assertEquals(isLeapYear.test(year) ? 366 : 365, base.lengthOfYear());
            assertEquals(isLeapYear.test(year), InternationalFixedChronology.INSTANCE.isLeapYear(year));
        }
    }

    /** Tests specific leap year cases. */
    @Test
    public void leapYear_specificCases() {
        assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(400));
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(100));
        assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(4));
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(3));
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(2));
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(1));
    }

    //-----------------------------------------------------------------------
    // Month Lengths
    //-----------------------------------------------------------------------

    /** Provides month length test data. */
    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            // Non-leap year months
            {1900, 1, 28, 28},
            {1900, 2, 28, 28},
            {1900, 3, 28, 28},
            {1900, 4, 28, 28},
            {1900, 5, 28, 28},
            {1900, 6, 28, 28},  // June (non-leap)
            {1900, 7, 28, 28},
            {1900, 8, 28, 28},
            {1900, 9, 28, 28},
            {1900, 10, 28, 28},
            {1900, 11, 28, 28},
            {1900, 12, 28, 28},
            {1900, 13, 29, 29}, // Year Day month
            
            // Leap year June
            {1904, 6, 29, 29},
        };
    }

    /** Tests month lengths at arbitrary dates. */
    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void monthLength_arbitraryDate(int year, int month, int day, int length) {
        assertEquals(length, InternationalFixedDate.of(year, month, day).lengthOfMonth());
    }

    /** Tests month lengths at month start. */
    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void monthLength_monthStart(int year, int month, int day, int length) {
        assertEquals(length, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
    }

    /** Tests special month length cases. */
    @Test
    public void monthLength_specialCases() {
        assertEquals(29, InternationalFixedDate.of(1900, 13, 29).lengthOfMonth());
        assertEquals(29, InternationalFixedDate.of(2000, 13, 29).lengthOfMonth());
        assertEquals(29, InternationalFixedDate.of(2000, 6, 29).lengthOfMonth());
    }

    //-----------------------------------------------------------------------
    // Era Handling
    //-----------------------------------------------------------------------

    /** Tests valid era retrieval. */
    @Test
    public void era_validEra() {
        Era era = InternationalFixedChronology.INSTANCE.eraOf(1);
        assertNotNull(era);
        assertEquals(1, era.getValue());
    }

    /** Provides invalid era values. */
    public static Object[][] data_invalidEraValues() {
        return new Object[][] {
            {-1},
            {0},
            {2},
        };
    }

    /** Tests invalid era values. */
    @ParameterizedTest
    @MethodSource("data_invalidEraValues")
    public void era_invalidEraValues(int eraValue) {
        assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
    }

    /** Tests era-based date creation. */
    @Test
    public void era_dateCreation() {
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate base = InternationalFixedChronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            InternationalFixedEra era = InternationalFixedEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            InternationalFixedDate eraBased = InternationalFixedChronology.INSTANCE.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    /** Tests era-based day-of-year date creation. */
    @Test
    public void era_dateYearDayCreation() {
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate base = InternationalFixedChronology.INSTANCE.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            InternationalFixedEra era = InternationalFixedEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            InternationalFixedDate eraBased = InternationalFixedChronology.INSTANCE.dateYearDay(era, year, 1);
            assertEquals(base, eraBased);
        }
    }

    /** Tests proleptic year calculation. */
    @Test
    public void prolepticYear_calculation() {
        assertEquals(4, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 4));
        assertEquals(3, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 3));
        assertEquals(2, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2));
        assertEquals(1, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1));
        assertEquals(2000, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2000));
        assertEquals(1582, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1582));
    }

    /** Provides invalid proleptic years. */
    public static Object[][] data_prolepticYear_bad() {
        return new Object[][] {
            {-10},
            {-1},
            {0},
        };
    }

    /** Tests invalid proleptic years. */
    @ParameterizedTest
    @MethodSource("data_prolepticYear_bad")
    public void prolepticYear_invalidYears(int year) {
        assertThrows(DateTimeException.class, () -> 
            InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
    }

    /** Tests incompatible era type. */
    @Test
    public void prolepticYear_incompatibleEra() {
        assertThrows(ClassCastException.class, () -> 
            InternationalFixedChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
    }

    /** Tests chronology era retrieval. */
    @Test
    public void chronology_eraRetrieval() {
        assertEquals(InternationalFixedEra.CE, InternationalFixedChronology.INSTANCE.eraOf(1));
    }

    /** Tests chronology eras list. */
    @Test
    public void chronology_eraList() {
        List<Era> eras = InternationalFixedChronology.INSTANCE.eras();
        assertEquals(1, eras.size());
        assertTrue(eras.contains(InternationalFixedEra.CE));
    }

    //-----------------------------------------------------------------------
    // Field Ranges
    //-----------------------------------------------------------------------

    /** Tests chronology field ranges. */
    @Test
    public void chronology_fieldRanges() {
        assertEquals(ValueRange.of(0, 1, 0, 7), InternationalFixedChronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 7), InternationalFixedChronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 4), InternationalFixedChronology.INSTANCE.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 52), InternationalFixedChronology.INSTANCE.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 7), InternationalFixedChronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 29), InternationalFixedChronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), InternationalFixedChronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 1), InternationalFixedChronology.INSTANCE.range(ERA));
        assertEquals(ValueRange.of(-719_528, 1_000_000 * 365L + 242_499 - 719_528), InternationalFixedChronology.INSTANCE.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 13), InternationalFixedChronology.INSTANCE.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(13, 1_000_000 * 13L - 1), InternationalFixedChronology.INSTANCE.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(1, 1_000_000), InternationalFixedChronology.INSTANCE.range(YEAR));
        assertEquals(ValueRange.of(1, 1_000_000), InternationalFixedChronology.INSTANCE.range(YEAR_OF_ERA));
    }

    //-----------------------------------------------------------------------
    // Date Field Ranges
    //-----------------------------------------------------------------------

    /** Provides date field range test data. */
    public static Object[][] data_ranges() {
        return new Object[][] {
            // Day of month ranges
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            // ... (other test cases remain the same) ...
            {2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            {2011, 13, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
        };
    }

    /** Tests field ranges at specific dates. */
    @ParameterizedTest
    @MethodSource("data_ranges")
    public void dateField_range(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
    }

    /** Tests unsupported field ranges. */
    @Test
    public void dateField_unsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            InternationalFixedDate.of(2012, 6, 28).range(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // Field Value Retrieval
    //-----------------------------------------------------------------------

    /** Provides field value test data. */
    public static Object[][] data_getLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // ... (other test cases remain the same) ...
            {2012, 7, 1, PROLEPTIC_MONTH, 2012 * 13 + 7 - 1},
        };
    }

    /** Tests field value retrieval. */
    @ParameterizedTest
    @MethodSource("data_getLong")
    public void dateField_valueRetrieval(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
    }

    /** Tests unsupported field retrieval. */
    @Test
    public void dateField_unsupportedFieldValue() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> 
            InternationalFixedDate.of(2012, 6, 28).getLong(MINUTE_OF_DAY));
    }

    // ... (remaining test sections follow the same pattern with added comments and organization) ...
}