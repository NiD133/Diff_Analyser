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
import static java.time.temporal.ChronoField.EPOCH_DAY;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry454Chronology {

    //-----------------------------------------------------------------------
    // Chronology Lookup Tests
    //-----------------------------------------------------------------------
    
    @Test
    public void chronologyLookup_byName_returnsCorrectInstance() {
        Chronology chrono = Chronology.of("Sym454");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(Symmetry454Chronology.INSTANCE, chrono, "Should be Symmetry454 instance");
        assertEquals("Sym454", chrono.getId(), "ID should match");
        assertEquals(null, chrono.getCalendarType(), "Calendar type should be null");
    }

    //-----------------------------------------------------------------------
    // Date Conversion Tests
    //-----------------------------------------------------------------------

    /**
     * Data for sample date conversions between Symmetry454 and ISO.
     * Format: {Symmetry454Date, Expected LocalDate}
     */
    public static Object[][] data_samples() {
        return new Object[][] {
            { Symmetry454Date.of(   1,  1,  1), LocalDate.of(   1,  1,  1) },
            { Symmetry454Date.of( 272,  2, 30), LocalDate.of( 272,  2, 27) }, // Constantine the Great
            { Symmetry454Date.of( 272,  2, 27), LocalDate.of( 272,  2, 24) },
            { Symmetry454Date.of( 742,  3, 25), LocalDate.of( 742,  4,  2) }, // Charlemagne
            { Symmetry454Date.of( 742,  4,  2), LocalDate.of( 742,  4,  7) },
            { Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14) }, // Battle of Hastings
            { Symmetry454Date.of(1304,  7, 21), LocalDate.of(1304,  7, 20) }, // Petrarch
            { Symmetry454Date.of(1304,  7, 20), LocalDate.of(1304,  7, 19) },
            { Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10) }, // Charles the Bold
            { Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11,  6) },
            { Symmetry454Date.of(1452,  4, 11), LocalDate.of(1452,  4, 15) }, // Leonardo da Vinci
            { Symmetry454Date.of(1452,  4, 15), LocalDate.of(1452,  4, 19) },
            { Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12) }, // Columbus lands
            { Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14) },
            { Symmetry454Date.of(1564,  2, 20), LocalDate.of(1564,  2, 15) }, // Galileo
            { Symmetry454Date.of(1564,  2, 15), LocalDate.of(1564,  2, 10) },
            { Symmetry454Date.of(1564,  4, 28), LocalDate.of(1564,  4, 26) }, // Shakespeare baptized
            { Symmetry454Date.of(1564,  4, 26), LocalDate.of(1564,  4, 24) },
            { Symmetry454Date.of(1643,  1,  7), LocalDate.of(1643,  1,  4) }, // Newton
            { Symmetry454Date.of(1643,  1,  4), LocalDate.of(1643,  1,  1) },
            { Symmetry454Date.of(1707,  4, 12), LocalDate.of(1707,  4, 15) }, // Euler
            { Symmetry454Date.of(1707,  4, 15), LocalDate.of(1707,  4, 18) },
            { Symmetry454Date.of(1789,  7, 16), LocalDate.of(1789,  7, 14) }, // Storming of Bastille
            { Symmetry454Date.of(1789,  7, 14), LocalDate.of(1789,  7, 12) },
            { Symmetry454Date.of(1879,  3, 12), LocalDate.of(1879,  3, 14) }, // Einstein
            { Symmetry454Date.of(1879,  3, 14), LocalDate.of(1879,  3, 16) },
            { Symmetry454Date.of(1941,  9,  9), LocalDate.of(1941,  9,  9) }, // Dennis Ritchie
            { Symmetry454Date.of(1970,  1,  4), LocalDate.of(1970,  1,  1) }, // Unix epoch
            { Symmetry454Date.of(1970,  1,  1), LocalDate.of(1969, 12, 29) },
            { Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000,  1,  1) }, // Start of 21st century
            { Symmetry454Date.of(2000,  1,  1), LocalDate.of(2000,  1,  3) },
        };
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void convertToLocalDate_fromSymmetry454Date_returnsCorrectDate(Symmetry454Date sym454, LocalDate expectedIso) {
        assertEquals(expectedIso, LocalDate.from(sym454), "Conversion to LocalDate should match");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void convertFromLocalDate_toSymmetry454Date_returnsCorrectDate(Symmetry454Date expectedSym454, LocalDate iso) {
        assertEquals(expectedSym454, Symmetry454Date.from(iso), "Conversion from LocalDate should match");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void createFromEpochDay_usingChronology_returnsCorrectDate(Symmetry454Date expected, LocalDate iso) {
        long epochDay = iso.toEpochDay();
        assertEquals(expected, Symmetry454Chronology.INSTANCE.dateEpochDay(epochDay), 
            "Date from epoch day should match");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void toEpochDay_fromSymmetry454Date_returnsCorrectValue(Symmetry454Date sym454, LocalDate expectedIso) {
        assertEquals(expectedIso.toEpochDay(), sym454.toEpochDay(), 
            "Epoch day should match ISO equivalent");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void untilSameDate_returnsZeroPeriod(Symmetry454Date date, LocalDate ignored) {
        ChronoPeriod zeroPeriod = Symmetry454Chronology.INSTANCE.period(0, 0, 0);
        assertEquals(zeroPeriod, date.until(date), "Period between same date should be zero");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void untilLocalDateSameValue_returnsZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
        ChronoPeriod zeroPeriod = Symmetry454Chronology.INSTANCE.period(0, 0, 0);
        assertEquals(zeroPeriod, sym454.until(iso), 
            "Period between Symmetry454Date and equivalent LocalDate should be zero");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void createDateFromTemporal_usingChronology_returnsCorrectDate(Symmetry454Date expected, LocalDate input) {
        assertEquals(expected, Symmetry454Chronology.INSTANCE.date(input), 
            "Date created from temporal should match");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void localDateUntilSymmetry454DateSameValue_returnsZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(sym454), 
            "Period between LocalDate and equivalent Symmetry454Date should be zero");
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("data_samples")
    public void plusDays_returnsCorrectDate(Symmetry454Date base, LocalDate baseIso) {
        assertEquals(baseIso, LocalDate.from(base.plus(0, DAYS)), "Plus zero days");
        assertEquals(baseIso.plusDays(1), LocalDate.from(base.plus(1, DAYS)), "Plus one day");
        assertEquals(baseIso.plusDays(35), LocalDate.from(base.plus(35, DAYS)), "Plus 35 days");
        assertEquals(baseIso.plusDays(-1), LocalDate.from(base.plus(-1, DAYS)), "Minus one day");
        assertEquals(baseIso.plusDays(-60), LocalDate.from(base.plus(-60, DAYS)), "Minus 60 days");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void minusDays_returnsCorrectDate(Symmetry454Date base, LocalDate baseIso) {
        assertEquals(baseIso, LocalDate.from(base.minus(0, DAYS)), "Minus zero days");
        assertEquals(baseIso.minusDays(1), LocalDate.from(base.minus(1, DAYS)), "Minus one day");
        assertEquals(baseIso.minusDays(35), LocalDate.from(base.minus(35, DAYS)), "Minus 35 days");
        assertEquals(baseIso.minusDays(-1), LocalDate.from(base.minus(-1, DAYS)), "Plus one day");
        assertEquals(baseIso.minusDays(-60), LocalDate.from(base.minus(-60, DAYS)), "Plus 60 days");
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void untilDays_returnsCorrectValue(Symmetry454Date base, LocalDate baseIso) {
        assertEquals(0, base.until(baseIso.plusDays(0), DAYS), "Same day");
        assertEquals(1, base.until(baseIso.plusDays(1), DAYS), "Next day");
        assertEquals(35, base.until(baseIso.plusDays(35), DAYS), "35 days later");
        assertEquals(-40, base.until(baseIso.minusDays(40), DAYS), "40 days earlier");
    }

    //-----------------------------------------------------------------------
    // Invalid Date Tests
    //-----------------------------------------------------------------------

    /**
     * Data for invalid date combinations (year, month, day)
     */
    public static Object[][] data_badDates() {
        return new Object[][] {
            {-1, 13, 28},
            {-1, 13, 29},

            {2000, -2, 1},
            {2000, 13, 1},
            {2000, 15, 1},

            {2000, 1, -1},
            {2000, 1, 0},

            {2000, 0, 1},
            {2000, -1, 0},
            {2000, -1, 1},

            {2000, 1, 29},     // January max 28 days
            {2000, 2, 36},     // February max 35 days
            {2000, 3, 29},     // March max 28 days
            {2000, 4, 29},     // April max 28 days
            {2000, 5, 36},     // May max 35 days
            {2000, 6, 29},     // June max 28 days
            {2000, 7, 29},     // July max 28 days
            {2000, 8, 36},     // August max 35 days
            {2000, 9, 29},     // September max 28 days
            {2000, 10, 29},    // October max 28 days
            {2000, 11, 36},    // November max 35 days
            {2000, 12, 29},    // December max 28 days in non-leap year
            {2004, 12, 36},    // December max 35 days in leap year
        };
    }

    @ParameterizedTest
    @MethodSource("data_badDates")
    public void createInvalidDate_throwsDateTimeException(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom),
            "Should throw for invalid date: " + year + "-" + month + "-" + dom);
    }

    /**
     * Data for years where Dec 29 is invalid (only allowed in leap years)
     */
    public static Object[][] data_badLeapDates() {
        return new Object[][] {
            {1},    // Non-leap year
            {100},  // Non-leap year
            {200},  // Non-leap year
            {2000}  // Non-leap year
        };
    }

    @ParameterizedTest
    @MethodSource("data_badLeapDates")
    public void createDec29InNonLeapYear_throwsDateTimeException(int year) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29),
            "Should throw for Dec 29 in non-leap year: " + year);
    }

    @Test
    public void createInvalidDateYearDay_throwsDateTimeException() {
        // Day 365 is only valid in leap years (which have 371 days)
        assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.dateYearDay(2000, 365),
            "Should throw for day 365 in non-leap year");
    }

    //-----------------------------------------------------------------------
    // Leap Year Tests
    //-----------------------------------------------------------------------

    @Test
    public void isLeapYear_specificYears_returnsCorrectValue() {
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(3), "Year 3 should be leap");
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(6), "Year 6 should not be leap");
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(9), "Year 9 should be leap");
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2000), "Year 2000 should not be leap");
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2004), "Year 2004 should be leap");
    }

    //-----------------------------------------------------------------------
    // Leap Week Tests
    //-----------------------------------------------------------------------

    @Test
    public void isLeapWeek_inLeapWeek_returnsTrue() {
        // All days in the leap week (last week of December in leap year) should return true
        Symmetry454Date date = Symmetry454Date.of(2015, 12, 29);
        assertTrue(date.isLeapWeek(), "Day in leap week should be marked as leap week");
        
        // Test all days in the leap week
        for (int day = 29; day <= 35; day++) {
            assertTrue(Symmetry454Date.of(2015, 12, day).isLeapWeek(), 
                "Day " + day + " in Dec 2015 should be in leap week");
        }
    }

    //-----------------------------------------------------------------------
    // Month Length Tests
    //-----------------------------------------------------------------------

    /**
     * Data for month length tests (year, month, sampleDay, expectedLength)
     */
    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            {2000, 1, 28, 28},    // January (normal)
            {2000, 2, 28, 35},    // February (long)
            {2000, 3, 28, 28},    // March (normal)
            {2000, 4, 28, 28},    // April (normal)
            {2000, 5, 28, 35},    // May (long)
            {2000, 6, 28, 28},    // June (normal)
            {2000, 7, 28, 28},    // July (normal)
            {2000, 8, 28, 35},    // August (long)
            {2000, 9, 28, 28},    // September (normal)
            {2000, 10, 28, 28},   // October (normal)
            {2000, 11, 28, 35},   // November (long)
            {2000, 12, 28, 28},   // December (normal in non-leap year)
            {2004, 12, 20, 35},   // December (long in leap year)
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void lengthOfMonth_returnsCorrectValue(int year, int month, int day, int expectedLength) {
        Symmetry454Date date = Symmetry454Date.of(year, month, day);
        assertEquals(expectedLength, date.lengthOfMonth(), 
            "Month length should be " + expectedLength + " for " + year + "-" + month);
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void lengthOfMonthFirstDay_returnsSameValue(int year, int month, int ignored, int expectedLength) {
        Symmetry454Date date = Symmetry454Date.of(year, month, 1);
        assertEquals(expectedLength, date.lengthOfMonth(), 
            "Month length from first day should be " + expectedLength + " for " + year + "-" + month);
    }

    @Test
    public void lengthOfMonth_specificCases() {
        // Non-leap year December
        assertEquals(28, Symmetry454Date.of(2000, 12, 28).lengthOfMonth(), 
            "Non-leap year December should be 28 days");
        // Leap year December
        assertEquals(35, Symmetry454Date.of(2004, 12, 28).lengthOfMonth(), 
            "Leap year December should be 35 days");
    }

    //-----------------------------------------------------------------------
    // Era and Year Tests
    //-----------------------------------------------------------------------

    @Test
    public void eraAndYearConversion_commonEra_consistentValues() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Proleptic year should match");
            assertEquals(IsoEra.CE, base.getEra(), "Era should be CE");
            assertEquals(year, base.get(YEAR_OF_ERA), "Year of era should match proleptic year");
            
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
            assertEquals(base, eraBased, "Date created with era should match");
        }
    }

    @Test
    public void eraAndYearConversion_beforeCommonEra_consistentValues() {
        for (int year = -200; year < 0; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Proleptic year should match");
            assertEquals(IsoEra.BCE, base.getEra(), "Era should be BCE");
            assertEquals(1 - year, base.get(YEAR_OF_ERA), "Year of era should be positive");
            
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.BCE, 1 - year, 1, 1);
            assertEquals(base, eraBased, "Date created with era should match");
        }
    }

    @Test
    public void eraAndYearDayConversion_commonEra_consistentValues() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR), "Proleptic year should match");
            assertEquals(IsoEra.CE, base.getEra(), "Era should be CE");
            assertEquals(year, base.get(YEAR_OF_ERA), "Year of era should match proleptic year");
            
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.dateYearDay(IsoEra.CE, year, 1);
            assertEquals(base, eraBased, "Date created with era should match");
        }
    }

    @Test
    public void prolepticYearConversion_commonEra_returnsSameValue() {
        assertEquals(4, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        assertEquals(3, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 3));
        assertEquals(2, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2));
        assertEquals(1, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
        assertEquals(2000, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1582));
    }

    /**
     * Data for invalid era types
     */
    public static Object[][] data_prolepticYear_badEra() {
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

    @ParameterizedTest
    @MethodSource("data_prolepticYear_badEra")
    public void prolepticYearWithWrongEraType_throwsClassCastException(Era era) {
        assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
    }

    @Test
    public void eraFromValue_validValues_returnsCorrectEra() {
        assertEquals(IsoEra.BCE, Symmetry454Chronology.INSTANCE.eraOf(0), "Value 0 should be BCE");
        assertEquals(IsoEra.CE, Symmetry454Chronology.INSTANCE.eraOf(1), "Value 1 should be CE");
    }

    @Test
    public void eraFromValue_invalidValue_throwsDateTimeException() {
        assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.eraOf(2),
            "Invalid era value should throw");
    }

    @Test
    public void availableEras_containsBothEras() {
        List<Era> eras = Symmetry454Chronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "Should have two eras");
        assertTrue(eras.contains(IsoEra.BCE), "Should contain BCE");
        assertTrue(eras.contains(IsoEra.CE), "Should contain CE");
    }

    //-----------------------------------------------------------------------
    // Field Range Tests
    //-----------------------------------------------------------------------

    @Test
    public void chronologyFieldRange_returnsCorrectValue() {
        assertEquals(ValueRange.of(1, 7), INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(1, 7), INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(1, 4, 5), INSTANCE.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 52, 53), INSTANCE.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(1, 7), INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 35), INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 364, 371), INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), INSTANCE.range(ERA));
        assertEquals(ValueRange.of(-1_000_000 * 364L - 177_474 * 7 - 719_162, 1_000_000 * 364L + 177_474 * 7 - 719_162), INSTANCE.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 12), INSTANCE.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), INSTANCE.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), INSTANCE.range(YEAR));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), INSTANCE.range(YEAR_OF_ERA));
    }

    //-----------------------------------------------------------------------
    // Date Field Range Tests
    //-----------------------------------------------------------------------

    /**
     * Data for date field ranges (year, month, day, field, expectedRange)
     */
    public static Object[][] data_ranges() {
        return new Object[][] {
            // Day of month ranges
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},    // January (normal)
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},    // February (long)
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},   // December (normal)
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},   // December (leap year)
            
            // Day of week ranges
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            
            // Day of year ranges
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},    // Normal year
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},    // Leap year
            
            // Month of year range
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
            
            // Aligned day of week in month
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            
            // Aligned week of month
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},    // Normal month
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},    // Long month
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},   // Leap year December
            
            // Aligned day of week in year
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            
            // Aligned week of year
            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},    // Normal year
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},   // Leap year
        };
    }

    @ParameterizedTest
    @MethodSource("data_ranges")
    public void dateFieldRange_returnsCorrectValue(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
        Symmetry454Date date = Symmetry454Date.of(year, month, dom);
        assertEquals(expectedRange, date.range(field), 
            "Field range should match for " + field + " on " + year + "-" + month + "-" + dom);
    }

    @Test
    public void rangeForUnsupportedField_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, () -> date.range(MINUTE_OF_DAY),
            "Should throw for unsupported field");
    }

    //-----------------------------------------------------------------------
    // Field Value Tests
    //-----------------------------------------------------------------------

    /**
     * Data for field value tests (year, month, day, field, expectedValue)
     */
    public static Object[][] data_getLong() {
        return new Object[][] {
            // Standard fields
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            
            // Complex calculations
            {2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
            
            // Leap week dates
            {2015, 12, 35, DAY_OF_WEEK, 7},
            {2015, 12, 35, DAY_OF_MONTH, 35},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 12, 35, MONTH_OF_YEAR, 12},
            {2015, 12, 35, PROLEPTIC_MONTH, 2016 * 12 - 1},
            {2015, 12, 35, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7},
        };
    }

    @ParameterizedTest
    @MethodSource("data_getLong")
    public void getLongValue_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
        Symmetry454Date date = Symmetry454Date.of(year, month, dom);
        assertEquals(expected, date.getLong(field), 
            "Field value should match for " + field + " on " + year + "-" + month + "-" + dom);
    }

    @Test
    public void getLongForUnsupportedField_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, () -> date.getLong(MINUTE_OF_DAY),
            "Should throw for unsupported field");
    }

    //-----------------------------------------------------------------------
    // With Field Tests
    //-----------------------------------------------------------------------

    /**
     * Data for with() tests (year, month, day, field, newValue, expectedYear, expectedMonth, expectedDay)
     */
    public static Object[][] data_with() {
        return new Object[][] {
            // Day of week adjustments
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 26},
            
            // Day of month adjustments
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            
            // Day of year adjustments
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 19},
            
            // Aligned day of week in month
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            
            // Aligned week of month
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            
            // Aligned day of week in year
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 23},
            
            // Aligned week of year
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
            
            // Month of year
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            
            // Proleptic month
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            
            // Year
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            
            // Year of era
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            
            // Era
            {2014, 5, 26, ERA, 1, 2014, 5, 26},
            
            // Leap week adjustments
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 1, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28},
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 0, 2015, 12, 29},
            {2015, 12, 28, DAY_OF_WEEK, 1, 2015, 12, 22},
            {2015, 12, 29, DAY_OF_MONTH, 1, 2015, 12, 1},
            {2015, 12, 29, MONTH_OF_YEAR, 1, 2015, 1, 28},
            {2015, 12, 29, YEAR, 2014, 2014, 12, 28},
            
            // Boundary cases
            {2014, 3, 28, DAY_OF_MONTH, 1, 2014, 3, 1},
            {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with")
    public void withFieldAdjustment_returnsCorrectDate(
            int year, int month, int dom, TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
            
        Symmetry454Date original = Symmetry454Date.of(year, month, dom);
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, original.with(field, value),
            "Date after with(" + field + ", " + value + ") should match");
    }

    /**
     * Data for invalid field adjustments
     */
    public static Object[][] data_with_bad() {
        return new Object[][] {
            // Aligned day of week in month
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
            
            // Aligned week of month
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5},      // Max 4 in normal month
            {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},      // Max 5 in long month
            
            // Aligned week of year
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53},      // Max 52 in normal year
            {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},      // Max 53 in leap year
            
            // Day of week
            {2013, 1, 1, DAY_OF_WEEK, -1},
            {2013, 1, 1, DAY_OF_WEEK, 8},
            
            // Day of month
            {2013, 1, 1, DAY_OF_MONTH, -1},
            {2013, 1, 1, DAY_OF_MONTH, 29},              // January max 28
            {2013, 6, 1, DAY_OF_MONTH, 29},              // June max 28
            {2013, 12, 1, DAY_OF_MONTH, 30},             // December max 28 in non-leap
            {2015, 12, 1, DAY_OF_MONTH, 36},             // December max 35 in leap
            
            // Day of year
            {2013, 1, 1, DAY_OF_YEAR, -1},
            {2013, 1, 1, DAY_OF_YEAR, 365},             // Max 364 in normal year
            {2015, 1, 1, DAY_OF_YEAR, 372},             // Max 371 in leap year
            
            // Month of year
            {2013, 1, 1, MONTH_OF_YEAR, -1},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            
            // Epoch day
            {2013, 1, 1, EPOCH_DAY, -365_961_481},      // Below min
            {2013, 1, 1, EPOCH_DAY,  364_523_156},      // Above max
            
            // Year
            {2013, 1, 1, YEAR, -1_000_001},             // Below min
            {2013, 1, 1, YEAR,  1_000_001},             // Above max
        };
    }

    @ParameterizedTest
    @MethodSource("data_with_bad")
    public void withInvalidFieldValue_throwsDateTimeException(int year, int month, int dom, TemporalField field, long value) {
        Symmetry454Date date = Symmetry454Date.of(year, month, dom);
        assertThrows(DateTimeException.class, () -> date.with(field, value),
            "Should throw for invalid field value: " + field + " " + value);
    }

    @Test
    public void withUnsupportedField_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, () -> date.with(MINUTE_OF_DAY, 10),
            "Should throw for unsupported field");
    }

    //-----------------------------------------------------------------------
    // Temporal Adjuster Tests
    //-----------------------------------------------------------------------

    /**
     * Data for last day of month tests
     */
    public static Object[][] data_temporalAdjusters_lastDayOfMonth() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 28},    // January (normal)
            {2012, 2, 23, 2012, 2, 35},    // February (long)
            {2012, 3, 23, 2012, 3, 28},    // March (normal)
            {2012, 11, 23, 2012, 11, 35},  // November (long)
            {2012, 12, 23, 2012, 12, 28},  // December (normal)
            {2009, 12, 23, 2009, 12, 35},  // December (leap year)
        };
    }

    @ParameterizedTest
    @MethodSource("data_temporalAdjusters_lastDayOfMonth")
    public void withTemporalAdjuster_lastDayOfMonth_returnsCorrectDate(
            int year, int month, int day, 
            int expectedYear, int expectedMonth, int expectedDay) {
            
        Symmetry454Date base = Symmetry454Date.of(year, month, day);
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry454Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual, "Last day of month should be correct");
    }

    //-----------------------------------------------------------------------
    // Type Conversion Tests
    //-----------------------------------------------------------------------

    @Test
    public void adjustToLocalDate_returnsCorrectDate() {
        Symmetry454Date sym454 = Symmetry454Date.of(2000, 1, 4);
        Symmetry454Date test = sym454.with(LocalDate.of(2012, 7, 6));
        assertEquals(Symmetry454Date.of(2012, 7, 5), test, 
            "Adjust to LocalDate should convert correctly");
    }

    @Test
    public void adjustToMonth_throwsException() {
        Symmetry454Date sym454 = Symmetry454Date.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> sym454.with(Month.APRIL),
            "Adjust to Month should throw");
    }

    @Test
    public void localDateAdjustToSymmetry454_returnsCorrectDate() {
        Symmetry454Date sym454 = Symmetry454Date.of(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(sym454);
        assertEquals(LocalDate.of(2012, 7, 20), test, 
            "LocalDate adjustTo should convert correctly");
    }

    @Test
    public void localDateTimeAdjustToSymmetry454_returnsCorrectDateTime() {
        Symmetry454Date sym454 = Symmetry454Date.of(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(sym454);
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test, 
            "LocalDateTime adjustTo should convert correctly");
    }

    //-----------------------------------------------------------------------
    // Period Arithmetic Tests
    //-----------------------------------------------------------------------

    /**
     * Data for plus/minus tests
     */
    public static Object[][] data_plus() {
        return new Object[][] {
            // Days
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 5, 34},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            
            // Weeks
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 19},
            
            // Months
            {2014, 5, 26, 0, MONTHS, 2014, 5, 26},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
            
            // Years
            {2014, 5, 26, 0, YEARS, 2014, 5, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},
            
            // Decades
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            
            // Centuries
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            
            // Millenniums
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            
            // Year boundaries
            {2014, 12, 26, 3, WEEKS, 2015, 1, 19},
            {2014, 1, 26, -5, WEEKS, 2013, 12, 19},
            
            // Multi-year
            {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28},
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21},
        };
    }

    /**
     * Data for leap week arithmetic tests
     */
    public static Object[][] data_plus_leapWeek() {
        return new Object[][] {
            // Days in leap week
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},
            {2015, 12, 28, 8, DAYS, 2016, 1, 1},
            {2015, 12, 28, -3, DAYS, 2015, 12, 25},
            
            // Weeks crossing leap week
            {2015, 12, 28, 0, WEEKS, 2015, 12, 28},
            {2015, 12, 28, 3, WEEKS, 2016, 1, 14},
            {2015, 12, 28, -5, WEEKS, 2015, 11, 28},
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21},
            
            // Months in leap year
            {2015, 12, 28, 0, MONTHS, 2015, 12, 28},
            {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
            {2015, 12, 28, 12, MONTHS, 2016, 12, 28},
            
            // Years with leap week
            {2015, 12, 28, 0, YEARS, 2015, 12, 28},
            {2015, 12, 28, 3, YEARS, 2018, 12, 28},
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void plusTemporalUnit_returnsCorrectDate(
            int year, int month, int dom, long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
            
        Symmetry454Date base = Symmetry454Date.of(year, month, dom);
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, base.plus(amount, unit),
            "Date after plus(" + amount + " " + unit + ") should match");
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    public void plusTemporalUnitInLeapWeek_returnsCorrectDate(
            int year, int month, int dom, long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
            
        Symmetry454Date base = Symmetry454Date.of(year, month, dom);
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, base.plus(amount, unit),
            "Date after plus(" + amount + " " + unit + ") in leap week should match");
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void minusTemporalUnit_returnsCorrectDate(
            int expectedYear, int expectedMonth, int expectedDom, 
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
            
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
        Symmetry454Date base = Symmetry454Date.of(year, month, dom);
        assertEquals(expected, base.minus(amount, unit),
            "Date after minus(" + amount + " " + unit + ") should match");
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    public void minusTemporalUnitInLeapWeek_returnsCorrectDate(
            int expectedYear, int expectedMonth, int expectedDom, 
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
            
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
        Symmetry454Date base = Symmetry454Date.of(year, month, dom);
        assertEquals(expected, base.minus(amount, unit),
            "Date after minus(" + amount + " " + unit + ") in leap week should match");
    }

    @Test
    public void plusUnsupportedUnit_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class, () -> date.plus(0, MINUTES),
            "Should throw for unsupported unit");
    }

    //-----------------------------------------------------------------------
    // Period Calculation Tests
    //-----------------------------------------------------------------------

    /**
     * Data for until() tests
     */
    public static Object[][] data_until() {
        return new Object[][] {
            // Days
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 13},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            
            // Weeks
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            
            // Months
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            
            // Years
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            
            // Decades
            {2014, 5, 26, 2014, 5, 26, DECADES, 0},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            
            // Centuries
            {2014, 5, 26, 2014, 5, 26, CENTURIES, 0},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            
            // Millenniums
            {2014, 5, 26, 2014, 5, 26, MILLENNIA, 0},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            
            // Eras
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
        };
    }

    /**
     * Data for period calculations
     */
    public static Object[][] data_until_period() {
        return new Object[][] {
            // Same day
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            // Days within same month
            {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
            // Cross month
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            // Cross year
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            // Complex period
            {2014, 5, 26, 2024, 5, 25, 9, 11, 27},
        };
    }

    @ParameterizedTest
    @MethodSource("data_until")
    public void untilTemporalUnit_returnsCorrectValue(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
            
        Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
        Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit), 
            "Period in " + unit + " should match");
    }

    @ParameterizedTest
    @MethodSource("data_until_period")
    public void untilDate_returnsCorrectPeriod(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            int yearPeriod, int monthPeriod, int dayPeriod) {
            
        Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
        Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end), 
            "Period between dates should match");
    }

    @Test
    public void untilWithUnsupportedUnit_throwsException() {
        Symmetry454Date start = Symmetry454Date.of(2012, 6, 28);
        Symmetry454Date end = Symmetry454Date.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES),
            "Should throw for unsupported unit");
    }

    //-----------------------------------------------------------------------
    // Period Arithmetic Tests
    //-----------------------------------------------------------------------

    @Test
    public void plusChronoPeriod_returnsCorrectDate() {
        Symmetry454Date start = Symmetry454Date.of(2014, 5, 21);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(0, 2, 8);
        assertEquals(Symmetry454Date.of(2014, 8, 1), start.plus(period),
            "Date after plus period should match");
    }

    @Test
    public void plusIsoPeriod_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
        assertThrows(DateTimeException.class, () -> date.plus(Period.ofMonths(2)),
            "Should throw for ISO period");
    }

    @Test
    public void minusChronoPeriod_returnsCorrectDate() {
        Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(0, 2, 3);
        assertEquals(Symmetry454Date.of(2014, 3, 23), start.minus(period),
            "Date after minus period should match");
    }

    @Test
    public void minusIsoPeriod_throwsException() {
        Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
        assertThrows(DateTimeException.class, () -> date.minus(Period.ofMonths(2)),
            "Should throw for ISO period");
    }

    //-----------------------------------------------------------------------
    // Object Equality Tests
    //-----------------------------------------------------------------------

    @Test
    public void equalsAndHashCode_consistentForSameDates() {
        new EqualsTester()
            .addEqualityGroup(Symmetry454Date.of(2000,  1,  3), Symmetry454Date.of(2000,  1,  3))
            .addEqualityGroup(Symmetry454Date.of(2000,  1,  4), Symmetry454Date.of(2000,  1,  4))
            .addEqualityGroup(Symmetry454Date.of(2000,  2,  3), Symmetry454Date.of(2000,  2,  3))
            .addEqualityGroup(Symmetry454Date.of(2001,  1,  3), Symmetry454Date.of(2001,  1,  3))
            .addEqualityGroup(Symmetry454Date.of(2000, 12, 28), Symmetry454Date.of(2000, 12, 28))
            .addEqualityGroup(Symmetry454Date.of(2000, 12, 25), Symmetry454Date.of(2000, 12, 25))
            .addEqualityGroup(Symmetry454Date.of(2001,  1,  1), Symmetry454Date.of(2001,  1,  1))
            .addEqualityGroup(Symmetry454Date.of(2001, 12, 28), Symmetry454Date.of(2001, 12, 28))
            .addEqualityGroup(Symmetry454Date.of(2000,  6, 28), Symmetry454Date.of(2000,  6, 28))
            .addEqualityGroup(Symmetry454Date.of(2000,  6, 23), Symmetry454Date.of(2000,  6, 23))
            .addEqualityGroup(Symmetry454Date.of(2000,  7,  1), Symmetry454Date.of(2000,  7,  1))
            .addEqualityGroup(Symmetry454Date.of(2004,  6, 28), Symmetry454Date.of(2004,  6, 28))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------

    /**
     * Data for toString() tests
     */
    public static Object[][] data_toString() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
            {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
            {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"},
            {Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
        assertEquals(expected, date.toString(), "String representation should match");
    }
}