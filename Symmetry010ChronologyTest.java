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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry010Chronology {

    //-----------------------------------------------------------------------
    // Chronology Lookup
    //-----------------------------------------------------------------------

    /**
     * Tests that the Symmetry010 chronology can be successfully looked up by ID.
     */
    @Test
    public void test_chronologyLookup() {
        Chronology chrono = Chronology.of("Sym010");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(Symmetry010Chronology.INSTANCE, chrono, "Lookup should return Symmetry010Chronology instance");
        assertEquals("Sym010", chrono.getId(), "Chronology ID should be 'Sym010'");
        assertNull(chrono.getCalendarType(), "Calendar type should be null");
    }

    //-----------------------------------------------------------------------
    // Date Conversion: Symmetry010Date <-> LocalDate
    //-----------------------------------------------------------------------

    /**
     * Data provider for historical date conversions between Symmetry010 and ISO.
     * Each entry contains a Symmetry010 date and its corresponding ISO date.
     */
    public static Object[][] data_samples() {
        return new Object[][] {
            { Symmetry010Date.of(   1,  1,  1), LocalDate.of(   1,  1,  1) },
            { Symmetry010Date.of( 272,  2, 28), LocalDate.of( 272,  2, 27) }, // Constantine the Great
            { Symmetry010Date.of( 272,  2, 27), LocalDate.of( 272,  2, 26) },
            { Symmetry010Date.of( 742,  3, 27), LocalDate.of( 742,  4,  2) }, // Charlemagne
            { Symmetry010Date.of( 742,  4,  2), LocalDate.of( 742,  4,  7) },
            { Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14) }, // Battle of Hastings
            { Symmetry010Date.of(1304,  7, 21), LocalDate.of(1304,  7, 20) }, // Petrarch
            { Symmetry010Date.of(1304,  7, 20), LocalDate.of(1304,  7, 19) },
            { Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10) }, // Charles the Bold
            { Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11,  8) },
            { Symmetry010Date.of(1452,  4, 11), LocalDate.of(1452,  4, 15) }, // Leonardo da Vinci
            { Symmetry010Date.of(1452,  4, 15), LocalDate.of(1452,  4, 19) },
            { Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12) }, // Columbus lands
            { Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14) },
            { Symmetry010Date.of(1564,  2, 18), LocalDate.of(1564,  2, 15) }, // Galileo
            { Symmetry010Date.of(1564,  2, 15), LocalDate.of(1564,  2, 12) },
            { Symmetry010Date.of(1564,  4, 28), LocalDate.of(1564,  4, 26) }, // Shakespeare baptized
            { Symmetry010Date.of(1564,  4, 26), LocalDate.of(1564,  4, 24) },
            { Symmetry010Date.of(1643,  1 , 7), LocalDate.of(1643,  1,  4) }, // Isaac Newton
            { Symmetry010Date.of(1643,  1,  4), LocalDate.of(1643,  1,  1) },
            { Symmetry010Date.of(1707,  4, 12), LocalDate.of(1707,  4, 15) }, // Leonhard Euler
            { Symmetry010Date.of(1707,  4, 15), LocalDate.of(1707,  4, 18) },
            { Symmetry010Date.of(1789,  7, 16), LocalDate.of(1789,  7, 14) }, // Storming of Bastille
            { Symmetry010Date.of(1789,  7, 14), LocalDate.of(1789,  7, 12) },
            { Symmetry010Date.of(1879,  3, 14), LocalDate.of(1879,  3, 14) }, // Albert Einstein
            { Symmetry010Date.of(1941,  9, 11), LocalDate.of(1941,  9,  9) }, // Dennis Ritchie
            { Symmetry010Date.of(1941,  9,  9), LocalDate.of(1941,  9,  7) },
            { Symmetry010Date.of(1970,  1,  4), LocalDate.of(1970,  1,  1) }, // Unix epoch
            { Symmetry010Date.of(1970,  1,  1), LocalDate.of(1969, 12, 29) },
            { Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000,  1,  1) }, // 21st century start
            { Symmetry010Date.of(2000,  1,  1), LocalDate.of(2000,  1,  3) },
        };
    }

    /**
     * Tests conversion from Symmetry010Date to LocalDate.
     */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void test_toLocalDate_fromSymmetry010Date(Symmetry010Date sym010, LocalDate expectedIso) {
        LocalDate actualIso = LocalDate.from(sym010);
        assertEquals(expectedIso, actualIso, "Incorrect conversion from Symmetry010 to LocalDate");
    }

    /**
     * Tests conversion from LocalDate to Symmetry010Date.
     */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void test_fromLocalDate_toSymmetry010Date(Symmetry010Date expectedSym010, LocalDate iso) {
        Symmetry010Date actualSym010 = Symmetry010Date.from(iso);
        assertEquals(expectedSym010, actualSym010, "Incorrect conversion from LocalDate to Symmetry010");
    }

    /**
     * Tests creating Symmetry010Date from epoch day.
     */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void test_dateFromEpochDay(Symmetry010Date expectedSym010, LocalDate iso) {
        long epochDay = iso.toEpochDay();
        Symmetry010Date actualSym010 = Symmetry010Chronology.INSTANCE.dateEpochDay(epochDay);
        assertEquals(expectedSym010, actualSym010, "Incorrect date from epoch day");
    }

    /**
     * Tests converting Symmetry010Date to epoch day.
     */
    @ParameterizedTest
    @MethodSource("data_samples")
    public void test_toEpochDay(Symmetry010Date sym010, LocalDate iso) {
        long expectedEpochDay = iso.toEpochDay();
        long actualEpochDay = sym010.toEpochDay();
        assertEquals(expectedEpochDay, actualEpochDay, "Incorrect epoch day");
    }

    //-----------------------------------------------------------------------
    // Field Validation: Invalid Dates
    //-----------------------------------------------------------------------

    /**
     * Data provider for invalid date combinations (year, month, day).
     */
    public static Object[][] data_invalidDates() {
        return new Object[][] {
            // Year boundaries
            {-1, 13, 28}, {-1, 13, 29},

            // Invalid months
            {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},

            // Invalid days
            {2000, 1, -1}, {2000, 1, 0}, 
            {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},

            // Month day overflows
            {2000, 1, 31}, {2000, 2, 32}, {2000, 3, 31}, 
            {2000, 4, 31}, {2000, 5, 32}, {2000, 6, 31}, 
            {2000, 7, 31}, {2000, 8, 32}, {2000, 9, 31}, 
            {2000, 10, 31}, {2000, 11, 32}, {2000, 12, 31},
            {2004, 12, 38}, // Leap year overflow
        };
    }

    /**
     * Tests that invalid date components throw DateTimeException.
     */
    @ParameterizedTest
    @MethodSource("data_invalidDates")
    public void test_invalidDateComponents(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom),
            "Should reject invalid date: year=" + year + ", month=" + month + ", day=" + dom);
    }

    /**
     * Data provider for leap day tests in non-leap years.
     */
    public static Object[][] data_nonLeapYears() {
        return new Object[][] {{1}, {100}, {200}, {2000}};
    }

    /**
     * Tests that leap day (Dec 37) is rejected in non-leap years.
     */
    @ParameterizedTest
    @MethodSource("data_nonLeapYears")
    public void test_invalidLeapDayInNonLeapYear(int year) {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37),
            "Should reject leap day in non-leap year: " + year);
    }

    //-----------------------------------------------------------------------
    // Leap Year Validation
    //-----------------------------------------------------------------------

    /**
     * Tests specific leap year calculations.
     */
    @Test
    public void test_isLeapYear_specificCases() {
        // Leap years: divisible by 5 or 6 in 293-year cycle
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(3), "Year 3 should be leap");
        assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(6), "Year 6 should not be leap");
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(9), "Year 9 should be leap");
        assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(2000), "Year 2000 should not be leap");
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(2004), "Year 2004 should be leap");
    }

    //-----------------------------------------------------------------------
    // Leap Week Validation
    //-----------------------------------------------------------------------

    /**
     * Tests that all days in the leap week are recognized as part of the leap week.
     */
    @Test
    public void test_daysInLeapWeek() {
        // Last week of leap year (days 31-37 in December)
        assertTrue(Symmetry010Date.of(2015, 12, 31).isLeapWeek(), "Dec 31 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 32).isLeapWeek(), "Dec 32 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 33).isLeapWeek(), "Dec 33 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 34).isLeapWeek(), "Dec 34 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 35).isLeapWeek(), "Dec 35 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 36).isLeapWeek(), "Dec 36 should be in leap week");
        assertTrue(Symmetry010Date.of(2015, 12, 37).isLeapWeek(), "Dec 37 should be in leap week");
    }

    //-----------------------------------------------------------------------
    // Month Length Validation
    //-----------------------------------------------------------------------

    /**
     * Data provider for month lengths (year, month, sample day, expected length).
     */
    public static Object[][] data_monthLengths() {
        return new Object[][] {
            // Standard months
            {2000, 1, 28, 30}, // January: 30 days
            {2000, 2, 28, 31},  // February: 31 days (quarter middle month)
            {2000, 3, 28, 30},  // March: 30 days
            {2000, 4, 28, 30},  // April: 30 days
            {2000, 5, 28, 31},  // May: 31 days (quarter middle month)
            {2000, 6, 28, 30},  // June: 30 days
            {2000, 7, 28, 30},  // July: 30 days
            {2000, 8, 28, 31},  // August: 31 days (quarter middle month)
            {2000, 9, 28, 30},  // September: 30 days
            {2000, 10, 28, 30}, // October: 30 days
            {2000, 11, 28, 31}, // November: 31 days (quarter middle month)
            {2000, 12, 28, 30}, // December: 30 days (non-leap year)
            
            // Leap year December
            {2004, 12, 20, 37}, // December: 37 days (leap year)
        };
    }

    /**
     * Tests month length from a day within the month.
     */
    @ParameterizedTest
    @MethodSource("data_monthLengths")
    public void test_lengthOfMonth_midMonth(int year, int month, int day, int expectedLength) {
        Symmetry010Date date = Symmetry010Date.of(year, month, day);
        assertEquals(expectedLength, date.lengthOfMonth(), "Incorrect month length for: " + date);
    }

    /**
     * Tests month length from the first day of the month.
     */
    @ParameterizedTest
    @MethodSource("data_monthLengths")
    public void test_lengthOfMonth_firstDay(int year, int month, int day, int expectedLength) {
        Symmetry010Date date = Symmetry010Date.of(year, month, 1);
        assertEquals(expectedLength, date.lengthOfMonth(), "Incorrect month length for: " + date);
    }

    //-----------------------------------------------------------------------
    // Era and Year Validation
    //-----------------------------------------------------------------------

    /**
     * Tests era and year calculations across a range of years.
     */
    @Test
    public void test_eraAndProlepticYear_loop() {
        // CE years
        for (int year = 1; year < 200; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, date.get(YEAR), "Year mismatch for CE " + year);
            assertEquals(IsoEra.CE, date.getEra(), "Era should be CE for year " + year);
            assertEquals(year, date.get(YEAR_OF_ERA), "Year-of-era mismatch for CE " + year);
            
            Symmetry010Date eraBased = Symmetry010Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
            assertEquals(date, eraBased, "Era-based date mismatch for CE " + year);
        }

        // BCE years (negative proleptic years)
        for (int year = -200; year < 0; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, date.get(YEAR), "Year mismatch for BCE " + year);
            assertEquals(IsoEra.BCE, date.getEra(), "Era should be BCE for year " + year);
            assertEquals(1 - year, date.get(YEAR_OF_ERA), "Year-of-era should be positive for BCE " + year);
            
            Symmetry010Date eraBased = Symmetry010Chronology.INSTANCE.date(IsoEra.BCE, 1 - year, 1, 1);
            assertEquals(date, eraBased, "Era-based date mismatch for BCE " + year);
        }
    }

    /**
     * Tests year-day constructor with era.
     */
    @Test
    public void test_eraBasedYearDay_loop() {
        for (int year = 1; year < 200; year++) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.dateYearDay(year, 1);
            assertEquals(year, date.get(YEAR), "Year mismatch for CE " + year);
            assertEquals(IsoEra.CE, date.getEra(), "Era should be CE for year " + year);
            assertEquals(year, date.get(YEAR_OF_ERA), "Year-of-era mismatch for CE " + year);
            
            Symmetry010Date eraBased = Symmetry010Chronology.INSTANCE.dateYearDay(IsoEra.CE, year, 1);
            assertEquals(date, eraBased, "Era-based year-day mismatch for CE " + year);
        }
    }

    /**
     * Tests converting era/year to proleptic year.
     */
    @Test
    public void test_prolepticYear_specific() {
        // CE years
        assertEquals(4, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        assertEquals(3, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 3));
        assertEquals(2, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2));
        assertEquals(1, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
        assertEquals(2000, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1582));
    }

    /**
     * Data provider for unsupported eras.
     */
    public static Era[][] data_unsupportedEras() {
        return new Era[][] {
            { AccountingEra.BCE },
            { AccountingEra.CE },
            { CopticEra.BEFORE_AM },
            { CopticEra.AM },
            { DiscordianEra.YOLD },
            { EthiopicEra.BEFORE_INCARNATION },
            { EthiopicEra.INCARNATION },
            { HijrahEra.AH },
            { InternationalFixedEra.CE },
            { JapaneseEra.MEIJI },
            { JapaneseEra.TAISHO },
            { JapaneseEra.SHOWA },
            { JapaneseEra.HEISEI },
            { JulianEra.BC },
            { JulianEra.AD },
            { MinguoEra.BEFORE_ROC },
            { MinguoEra.ROC },
            { PaxEra.BCE },
            { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE },
            { ThaiBuddhistEra.BE },
        };
    }

    /**
     * Tests that unsupported eras throw ClassCastException.
     */
    @ParameterizedTest
    @MethodSource("data_unsupportedEras")
    public void test_unsupportedEraTypes(Era era) {
        assertThrows(ClassCastException.class, 
            () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4),
            "Should reject unsupported era: " + era);
    }

    /**
     * Tests era lookup by numeric value.
     */
    @Test
    public void test_chronology_eraLookup() {
        assertEquals(IsoEra.BCE, Symmetry010Chronology.INSTANCE.eraOf(0), "Era 0 should be BCE");
        assertEquals(IsoEra.CE, Symmetry010Chronology.INSTANCE.eraOf(1), "Era 1 should be CE");
    }

    /**
     * Tests invalid era numeric values.
     */
    @Test
    public void test_chronology_invalidEraValue() {
        assertThrows(DateTimeException.class, 
            () -> Symmetry010Chronology.INSTANCE.eraOf(2),
            "Should reject invalid era value: 2");
    }

    /**
     * Tests the list of supported eras.
     */
    @Test
    public void test_chronology_eraList() {
        List<Era> eras = Symmetry010Chronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "Exactly two eras should be supported");
        assertTrue(eras.contains(IsoEra.BCE), "Eras must include BCE");
        assertTrue(eras.contains(IsoEra.CE), "Eras must include CE");
    }

    //-----------------------------------------------------------------------
    // Chronology Field Ranges
    //-----------------------------------------------------------------------

    /**
     * Tests valid ranges for each temporal field.
     */
    @Test
    public void test_chronology_fieldRanges() {
        assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(1, 4, 5), Symmetry010Chronology.INSTANCE.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 52, 53), Symmetry010Chronology.INSTANCE.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 30, 37), Symmetry010Chronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 364, 371), Symmetry010Chronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), Symmetry010Chronology.INSTANCE.range(ERA));
        assertEquals(ValueRange.of(-1_000_000 * 364L - 177_474 * 7 - 719_162, 1_000_000 * 364L + 177_474 * 7 - 719_162), Symmetry010Chronology.INSTANCE.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 12), Symmetry010Chronology.INSTANCE.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), Symmetry010Chronology.INSTANCE.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), Symmetry010Chronology.INSTANCE.range(YEAR));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), Symmetry010Chronology.INSTANCE.range(YEAR_OF_ERA));
    }

    //-----------------------------------------------------------------------
    // Date Field Ranges
    //-----------------------------------------------------------------------

    /**
     * Data provider for field ranges at specific dates (year, month, day, field, expected range).
     */
    public static Object[][] data_fieldRanges() {
        return new Object[][] {
            // Day-of-Month
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 3, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 4, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 8, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 9, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 10, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 11, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)}, // Leap year

            // Day-of-Week
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 12, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

            // Day-of-Year
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)}, // Non-leap year
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year

            // Month-of-Year
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},

            // Aligned fields
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // Leap year has 5 weeks

            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 6, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 12, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)}, // Leap year has 53 weeks
        };
    }

    /**
     * Tests field ranges at specific dates.
     */
    @ParameterizedTest
    @MethodSource("data_fieldRanges")
    public void test_fieldRangeAtDate(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
        Symmetry010Date date = Symmetry010Date.of(year, month, dom);
        ValueRange actualRange = date.range(field);
        assertEquals(expectedRange, actualRange, "Incorrect range for field " + field + " at " + date);
    }

    /**
     * Tests that unsupported fields throw UnsupportedTemporalTypeException.
     */
    @Test
    public void test_unsupportedFieldRange() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.range(MINUTE_OF_DAY),
            "MINUTE_OF_DAY should be unsupported");
    }

    //-----------------------------------------------------------------------
    // Field Value Retrieval
    //-----------------------------------------------------------------------

    /**
     * Data provider for field values at specific dates (year, month, day, field, expected value).
     */
    public static Object[][] data_fieldValues() {
        return new Object[][] {
            // May 26, 2014
            {2014, 5, 26, DAY_OF_WEEK, 2}, // Tuesday
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26}, // Jan(30) + Feb(31) + Mar(30) + Apr(30) + 26
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5}, // 5th day of the week in May
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4}, // 4th week of May
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7}, // 7th day of aligned week in year
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4}, // Weeks: Jan(4) + Feb(5) + Mar(4) + Apr(4) + May(4) = 21?
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1}, // Months since epoch
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1}, // CE
            {1, 5, 8, ERA, 1}, // CE

            // September 26, 2012
            {2012, 9, 26, DAY_OF_WEEK, 1}, // Monday
            {2012, 9, 26, DAY_OF_YEAR, 3 * (4 + 5 + 4) * 7 - 4}, // Up to Sep: Q1(13w) + Q2(13w) + Q3(13w) - 4 days?
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5}, 
            {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3},
            {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 3 * (4 + 5 + 4)}, // Weeks: Q1(13w) + Q2(13w) + Q3(13w) = 39?

            // Leap day (Dec 37) in leap year 2015
            {2015, 12, 37, DAY_OF_WEEK, 5}, // Friday
            {2015, 12, 37, DAY_OF_MONTH, 37},
            {2015, 12, 37, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7}, // Full quarters (4*13w=52w) + leap week (7d)
            {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2}, // 2nd day of week in leap week
            {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6}, // 6th week in December (leap year)
            {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7}, 
            {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53}, // 53rd week in leap year
            {2015, 12, 37, MONTH_OF_YEAR, 12},
            {2015, 12, 37, PROLEPTIC_MONTH, 2016 * 12 - 1}, // December of 2015 is 2016*12 - 1
        };
    }

    /**
     * Tests field values at specific dates.
     */
    @ParameterizedTest
    @MethodSource("data_fieldValues")
    public void test_getFieldValue(int year, int month, int dom, TemporalField field, long expectedValue) {
        Symmetry010Date date = Symmetry010Date.of(year, month, dom);
        long actualValue = date.getLong(field);
        assertEquals(expectedValue, actualValue, "Incorrect value for field " + field + " at " + date);
    }

    /**
     * Tests that unsupported fields throw UnsupportedTemporalTypeException.
     */
    @Test
    public void test_unsupportedFieldValue() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.getLong(MINUTE_OF_DAY),
            "MINUTE_OF_DAY should be unsupported");
    }

    //-----------------------------------------------------------------------
    // Date Manipulation: with()
    //-----------------------------------------------------------------------

    /**
     * Data provider for with() operations (original y/m/d, field, new value, expected y/m/d).
     */
    public static Object[][] data_withOperations() {
        return new Object[][] {
            // Day-of-week adjustments
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20}, // Tuesday -> Monday (previous week)
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 24}, // Tuesday -> Saturday (same week)
            
            // Day-of-month adjustments
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            
            // Day-of-year adjustments
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30}, // Last day of non-leap year
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 17},  // Day 138 in year
            
            // Aligned day adjustments
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24}, // Aligned Tuesday -> Saturday
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26}, // Same aligned day
            
            // Week adjustments
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},   // 4th week -> 1st week
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26},  // Same week
            
            // Year/month adjustments
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26}, // 2014-05 -> 2013-03
            
            // Leap year adjustments
            {2015, 12, 28, DAY_OF_WEEK, 5, 2015, 12, 28}, // Thursday -> Friday
            {2015, 12, 28, DAY_OF_WEEK, 7, 2015, 12, 30}, // Thursday -> Sunday
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, 12, 15}, // Week 5 -> Week 3
            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015,  1, 20},   // Week 53 -> Week 3
        };
    }

    /**
     * Tests date manipulation with field adjustments.
     */
    @ParameterizedTest
    @MethodSource("data_withOperations")
    public void test_withTemporalField(int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
        Symmetry010Date original = Symmetry010Date.of(year, month, dom);
        Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
        Symmetry010Date actual = original.with(field, value);
        assertEquals(expected, actual, "Incorrect date after adjustment");
    }

    /**
     * Data provider for invalid with() values.
     */
    public static Object[][] data_invalidAdjustments() {
        return new Object[][] {
            // Out-of-range aligned day in month
            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_MONTH,  8},
            
            // Out-of-range aligned week in month
            {2013,  1,  1, ALIGNED_WEEK_OF_MONTH, -1},
            {2013,  2,  1, ALIGNED_WEEK_OF_MONTH,  6}, // February only has 4/5 weeks
            
            // Out-of-range day-of-month
            {2013,  1,  1, DAY_OF_MONTH, -1},
            {2013,  1,  1, DAY_OF_MONTH, 31}, // January only has 30 days
            {2013, 12,  1, DAY_OF_MONTH, 31}, // Normal December only has 30 days
            {2015, 12,  1, DAY_OF_MONTH, 38}, // Leap year December only has 37 days
            
            // Invalid year
            {2013,  1,  1, YEAR, -1_000_001},
            {2013,  1,  1, YEAR,  1_000_001},
        };
    }

    /**
     * Tests that invalid field adjustments throw DateTimeException.
     */
    @ParameterizedTest
    @MethodSource("data_invalidAdjustments")
    public void test_invalidFieldAdjustments(int year, int month, int dom, TemporalField field, long value) {
        Symmetry010Date date = Symmetry010Date.of(year, month, dom);
        assertThrows(DateTimeException.class, 
            () -> date.with(field, value),
            "Should reject invalid field adjustment");
    }

    /**
     * Tests that unsupported fields throw UnsupportedTemporalTypeException.
     */
    @Test
    public void test_unsupportedFieldAdjustment() {
        Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, 
            () -> date.with(MINUTE_OF_DAY, 10),
            "MINUTE_OF_DAY should be unsupported");
    }

    //-----------------------------------------------------------------------
    // Temporal Adjusters
    //-----------------------------------------------------------------------

    /**
     * Data provider for last day of month.
     */
    public static Object[][] data_lastDayOfMonth() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 30}, // January: 30 days
            {2012, 2, 23, 2012, 2, 31},  // February: 31 days
            {2012, 3, 23, 2012, 3, 30},  // March: 30 days
            {2012, 4, 23, 2012, 4, 30},  // April: 30 days
            {2012, 5, 23, 2012, 5, 31},  // May: 31 days
            {2012, 6, 23, 2012, 6, 30},  // June: 30 days
            {2012, 7, 23, 2012, 7, 30},  // July: 30 days
            {2012, 8, 23, 2012, 8, 31},  // August: 31 days
            {2012, 9, 23, 2012, 9, 30},  // September: 30 days
            {2012, 10, 23, 2012,10, 30}, // October: 30 days
            {2012, 11, 23, 2012, 11, 31},// November: 31 days
            {2012, 12, 23, 2012, 12, 30},// December (non-leap): 30 days
            {2009, 12, 23, 2009, 12, 37},// December (leap): 37 days
        };
    }

    /**
     * Tests the last day of month adjuster.
     */
    @ParameterizedTest
    @MethodSource("data_lastDayOfMonth")
    public void test_lastDayOfMonthAdjuster(int year, int month, int day, 
            int expectedYear, int expectedMonth, int expectedDay) {
        Symmetry010Date base = Symmetry010Date.of(year, month, day);
        Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry010Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual, "Incorrect last day of month");
    }

    /**
     * Tests adjustment to LocalDate.
     */
    @Test
    public void test_adjustToLocalDate() {
        Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
        Symmetry010Date adjusted = sym010.with(LocalDate.of(2012, 7, 6));
        assertEquals(Symmetry010Date.of(2012, 7, 5), adjusted, "Incorrect adjustment to LocalDate");
    }

    /**
     * Tests that adjusting to Month enum throws DateTimeException.
     */
    @Test
    public void test_adjustToMonthEnumThrows() {
        Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
        assertThrows(DateTimeException.class, 
            () -> sym010.with(Month.APRIL),
            "Should reject Month enum adjustment");
    }

    //-----------------------------------------------------------------------
    // Object Methods: equals, hashCode, toString
    //-----------------------------------------------------------------------

    /**
     * Tests equals() and hashCode() implementations.
     */
    @Test
    public void test_equalsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(Symmetry010Date.of(2000,  1,  3), Symmetry010Date.of(2000,  1,  3))
            .addEqualityGroup(Symmetry010Date.of(2000,  1,  4), Symmetry010Date.of(2000,  1,  4))
            .addEqualityGroup(Symmetry010Date.of(2000,  2,  3), Symmetry010Date.of(2000,  2,  3))
            .addEqualityGroup(Symmetry010Date.of(2000,  6, 23), Symmetry010Date.of(2000,  6, 23))
            .addEqualityGroup(Symmetry010Date.of(2000,  6, 28), Symmetry010Date.of(2000,  6, 28))
            .addEqualityGroup(Symmetry010Date.of(2000,  7,  1), Symmetry010Date.of(2000,  7,  1))
            .addEqualityGroup(Symmetry010Date.of(2000, 12, 25), Symmetry010Date.of(2000, 12, 25))
            .addEqualityGroup(Symmetry010Date.of(2000, 12, 28), Symmetry010Date.of(2000, 12, 28))
            .addEqualityGroup(Symmetry010Date.of(2001,  1,  1), Symmetry010Date.of(2001,  1,  1))
            .addEqualityGroup(Symmetry010Date.of(2001,  1,  3), Symmetry010Date.of(2001,  1,  3))
            .addEqualityGroup(Symmetry010Date.of(2001, 12, 28), Symmetry010Date.of(2001, 12, 28))
            .addEqualityGroup(Symmetry010Date.of(2004,  6, 28), Symmetry010Date.of(2004,  6, 28))
            .testEquals();
    }

    /**
     * Data provider for toString tests.
     */
    public static Object[][] data_toString() {
        return new Object[][] {
            {Symmetry010Date.of(   1,  1,  1), "Sym010 CE 1/01/01"},
            {Symmetry010Date.of(1970,  2, 31), "Sym010 CE 1970/02/31"},
            {Symmetry010Date.of(2000,  8, 31), "Sym010 CE 2000/08/31"},
            {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"}, // Leap year
        };
    }

    /**
     * Tests toString() representation.
     */
    @ParameterizedTest
    @MethodSource("data_toString")
    public void test_toString(Symmetry010Date date, String expected) {
        assertEquals(expected, date.toString(), "Incorrect string representation");
    }
}