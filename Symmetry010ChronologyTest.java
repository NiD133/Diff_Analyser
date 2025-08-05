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

import com.google.common.testing.EqualsTester;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
class TestSymmetry010Chronology {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetry010AndIsoDates() {
        return new Object[][] {
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)},
            {Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
            {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)},
        };
    }

    static Object[][] invalidDateProvider() {
        return new Object[][] {
            {-1, 13, 28},
            {2000, 13, 1},
            {2000, 1, 0},
            {2000, 1, 31}, // Jan has 30 days
            {2000, 2, 32}, // Feb has 31 days
            {2004, 12, 38}, // Dec has 37 days in a leap year
        };
    }

    static Object[][] invalidLeapDayDateProvider() {
        return new Object[][] {{1}, {100}, {200}, {2000}};
    }

    //-----------------------------------------------------------------------
    // Chronology API
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @Test
        @DisplayName("of(String) should return singleton instance")
        void of_shouldReturnSingletonInstance() {
            Chronology chrono = Chronology.of("Sym010");
            assertNotNull(chrono);
            assertEquals(Symmetry010Chronology.INSTANCE, chrono);
            assertEquals("Sym010", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        @Test
        @DisplayName("range(ChronoField) should return correct ranges for chronology fields")
        void range_shouldReturnCorrectRangesForChronologyFields() {
            assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 30, 37), Symmetry010Chronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 364, 371), Symmetry010Chronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), Symmetry010Chronology.INSTANCE.range(MONTH_OF_YEAR));
            assertEquals(ValueRange.of(-1_000_000L, 1_000_000), Symmetry010Chronology.INSTANCE.range(YEAR));
            assertEquals(ValueRange.of(0, 1), Symmetry010Chronology.INSTANCE.range(ERA));
        }
    }

    //-----------------------------------------------------------------------
    // Date Creation and Conversion
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Conversion")
    class DateCreationAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("LocalDate.from(Symmetry010Date) should convert to equivalent ISO date")
        void from_shouldConvertSymmetry010DateToEquivalentLocalDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("Symmetry010Date.from(LocalDate) should convert to equivalent Symmetry010 date")
        void from_shouldConvertLocalDateToEquivalentSymmetry010Date(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("toEpochDay should return the same epoch day as the equivalent ISO date")
        void toEpochDay_shouldReturnCorrectEpochDay(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("dateEpochDay should create the correct date from an epoch day")
        void dateEpochDay_shouldCreateCorrectDateFromEpochDay(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("date(TemporalAccessor) should create the correct date from an ISO date")
        void date_fromTemporalAccessor_shouldCreateCorrectDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#invalidDateProvider")
        @DisplayName("of(y, m, d) should throw exception for invalid date components")
        void of_shouldThrowException_forInvalidDateComponents(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#invalidLeapDayDateProvider")
        @DisplayName("of(y, 12, 37) should throw exception for leap day in a non-leap year")
        void of_shouldThrowException_forInvalidLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    //-----------------------------------------------------------------------
    // Leap Year and Month Length
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Leap Year and Month Length")
    class LeapYearTests {

        @Test
        @DisplayName("isLeapYear should correctly identify leap years")
        void isLeapYear_shouldCorrectlyIdentifyLeapYears() {
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(3));
            assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(6));
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(9));
            assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(2000));
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(2004));
        }

        @Test
        @DisplayName("isLeapWeek should be true for all days in a leap week")
        void isLeapWeek_shouldBeTrue_forDatesInLeapWeek() {
            for (int day = 31; day <= 37; day++) {
                assertTrue(Symmetry010Date.of(2015, 12, day).isLeapWeek(), "Day " + day + " should be in a leap week");
            }
        }

        @Test
        @DisplayName("lengthOfMonth should return correct length for different months")
        void lengthOfMonth_shouldReturnCorrectLength() {
            assertEquals(30, Symmetry010Date.of(2000, 1, 1).lengthOfMonth()); // Jan
            assertEquals(31, Symmetry010Date.of(2000, 2, 1).lengthOfMonth()); // Feb
            assertEquals(30, Symmetry010Date.of(2000, 12, 1).lengthOfMonth()); // Dec in non-leap year
            assertEquals(37, Symmetry010Date.of(2004, 12, 1).lengthOfMonth()); // Dec in leap year
        }
    }

    //-----------------------------------------------------------------------
    // Era
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Era Handling")
    class EraTests {
        @Test
        @DisplayName("date(era, y, m, d) should create correct date for CE and BCE eras")
        void date_withEra_shouldCreateCorrectDate() {
            for (int year = 1; year < 200; year++) {
                Symmetry010Date fromProleptic = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
                Symmetry010Date fromEra = Symmetry010Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
                assertEquals(fromProleptic, fromEra);
                assertEquals(year, fromProleptic.get(YEAR));
                assertEquals(IsoEra.CE, fromProleptic.getEra());
                assertEquals(year, fromProleptic.get(YEAR_OF_ERA));
            }
            for (int year = -200; year < 0; year++) {
                Symmetry010Date fromProleptic = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
                // BCE year of era is 1-year
                Symmetry010Date fromEra = Symmetry010Chronology.INSTANCE.date(IsoEra.BCE, 1 - year, 1, 1);
                assertEquals(fromProleptic, fromEra);
                assertEquals(year, fromProleptic.get(YEAR));
                assertEquals(IsoEra.BCE, fromProleptic.getEra());
                assertEquals(1 - year, fromProleptic.get(YEAR_OF_ERA));
            }
        }

        @Test
        @DisplayName("eraOf should return correct era for value")
        void eraOf_shouldReturnCorrectEraForValue() {
            assertEquals(IsoEra.BCE, Symmetry010Chronology.INSTANCE.eraOf(0));
            assertEquals(IsoEra.CE, Symmetry010Chronology.INSTANCE.eraOf(1));
        }

        @Test
        @DisplayName("eraOf should throw exception for invalid value")
        void eraOf_shouldThrowException_forInvalidValue() {
            assertThrows(DateTimeException.class, () -> Symmetry010Chronology.INSTANCE.eraOf(2));
        }

        @Test
        @DisplayName("eras should return BCE and CE")
        void eras_shouldReturnBceAndCe() {
            List<Era> eras = Symmetry010Chronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(IsoEra.BCE));
            assertTrue(eras.contains(IsoEra.CE));
        }

        static Era[] nonIsoEraProvider() {
            return new Era[][] {{HijrahEra.AH}, {JapaneseEra.HEISEI}, {MinguoEra.ROC}, {ThaiBuddhistEra.BE}};
        }

        @ParameterizedTest
        @MethodSource("nonIsoEraProvider")
        @DisplayName("prolepticYear should throw exception for non-ISO era")
        void prolepticYear_shouldThrowException_forNonIsoEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    //-----------------------------------------------------------------------
    // Arithmetic (plus/minus)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Arithmetic")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("plus(days) should add days correctly")
        void plusDays_shouldAddCorrectNumberOfDays(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(sym010Date.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(sym010Date.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(sym010Date.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(sym010Date.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("minus(days) should subtract days correctly")
        void minusDays_shouldSubtractCorrectNumberOfDays(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(sym010Date.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(sym010Date.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(sym010Date.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(sym010Date.minus(-60, DAYS)));
        }

        @Test
        @DisplayName("plus should add various units correctly")
        void plus_shouldAddVariousUnits() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            assertEquals(Symmetry010Date.of(2014, 6, 16), date.plus(3, WEEKS));
            assertEquals(Symmetry010Date.of(2014, 8, 26), date.plus(3, MONTHS));
            assertEquals(Symmetry010Date.of(2017, 5, 26), date.plus(3, YEARS));
            assertEquals(Symmetry010Date.of(2044, 5, 26), date.plus(3, DECADES));
        }

        @Test
        @DisplayName("plus with ChronoPeriod should add the period")
        void plus_withChronoPeriod_shouldAddPeriod() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 21);
            ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(0, 2, 8);
            assertEquals(Symmetry010Date.of(2014, 7, 29), start.plus(period));
        }

        @Test
        @DisplayName("plus with ISO Period should throw exception")
        void plus_withIsoPeriod_shouldThrowException() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> date.plus(Period.ofMonths(2)));
        }

        @Test
        @DisplayName("plus with unsupported unit should throw exception")
        void plus_shouldThrowException_forUnsupportedUnit() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).plus(1, MINUTES));
        }
    }

    //-----------------------------------------------------------------------
    // Until
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Until")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("until(same date) should return zero period")
        void until_onSameDate_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("until(equivalent ISO date) should return zero period")
        void until_withEquivalentLocalDate_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry010Chronology#sampleSymmetry010AndIsoDates")
        @DisplayName("LocalDate.until(equivalent Symmetry010Date) should return zero period")
        void until_onLocalDateWithEquivalentSymmetry010Date_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(sym010Date));
        }

        @Test
        @DisplayName("until with DAYS unit should calculate correct day difference")
        void until_withDaysUnit_shouldCalculateCorrectDayDifference() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            assertEquals(9, date.until(Symmetry010Date.of(2014, 6, 4), DAYS));
            assertEquals(-6, date.until(Symmetry010Date.of(2014, 5, 20), DAYS));
        }

        @Test
        @DisplayName("until should return correct period between dates")
        void until_shouldReturnCorrectPeriod() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date end = Symmetry010Date.of(2015, 6, 28);
            // Expected: 1 year, 1 month, 2 days
            assertEquals(Symmetry010Chronology.INSTANCE.period(1, 1, 2), start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // With
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("With Adjusters")
    class WithTests {

        @Test
        @DisplayName("with(field, value) should adjust the date")
        void with_shouldSetFieldToNewValue() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            assertEquals(Symmetry010Date.of(2014, 5, 20), date.with(DAY_OF_WEEK, 1));
            assertEquals(Symmetry010Date.of(2014, 5, 28), date.with(DAY_OF_MONTH, 28));
            assertEquals(Symmetry010Date.of(2014, 12, 30), date.with(DAY_OF_YEAR, 364));
            assertEquals(Symmetry010Date.of(2014, 4, 26), date.with(MONTH_OF_YEAR, 4));
            assertEquals(Symmetry010Date.of(2012, 5, 26), date.with(YEAR, 2012));
        }

        @Test
        @DisplayName("with(field, value) should throw exception for invalid value")
        void with_shouldThrowException_forInvalidFieldValue() {
            Symmetry010Date date = Symmetry010Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_MONTH, 31));
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_YEAR, 365));
            assertThrows(DateTimeException.class, () -> date.with(YEAR, 1_000_001));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should adjust the date")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            Symmetry010Date date = Symmetry010Date.of(2012, 1, 23); // Jan has 30 days
            assertEquals(Symmetry010Date.of(2012, 1, 30), date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) should adjust to the new date")
        void with_localDate_shouldAdjustToNewDate() {
            Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
            Symmetry010Date test = sym010.with(LocalDate.of(2012, 7, 6));
            assertEquals(Symmetry010Date.of(2012, 7, 5), test);
        }

        @Test
        @DisplayName("with(java.time.Month) should throw exception")
        void with_javaTimeMonth_shouldThrowException() {
            Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
            assertThrows(DateTimeException.class, () -> sym010.with(Month.APRIL));
        }
    }

    //-----------------------------------------------------------------------
    // Equals, hashCode, toString
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object contract")
    class ObjectContractTests {

        @Test
        @DisplayName("equals and hashCode should follow contract")
        void equalsAndHashCode_shouldFollowContract() {
            new EqualsTester()
                .addEqualityGroup(Symmetry010Date.of(2000, 1, 3), Symmetry010Date.of(2000, 1, 3))
                .addEqualityGroup(Symmetry010Date.of(2000, 1, 4))
                .addEqualityGroup(Symmetry010Date.of(2001, 1, 3))
                .testEquals();
        }

        @Test
        @DisplayName("toString should return correct format")
        void toString_shouldReturnCorrectFormat() {
            assertEquals("Sym010 CE 1/01/01", Symmetry010Date.of(1, 1, 1).toString());
            assertEquals("Sym010 CE 1970/02/31", Symmetry010Date.of(1970, 2, 31).toString());
            assertEquals("Sym010 CE 2009/12/37", Symmetry010Date.of(2009, 12, 37).toString());
        }
    }
}