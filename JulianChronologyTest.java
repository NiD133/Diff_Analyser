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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.testing.EqualsTester;
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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for {@link JulianChronology} and {@link JulianDate}.
 */
@DisplayName("Tests for JulianChronology and JulianDate")
public class TestJulianChronology {

    private static final JulianDate SAMPLE_JULIAN_DATE = JulianDate.of(2012, 6, 22);
    private static final LocalDate SAMPLE_ISO_DATE = LocalDate.of(2012, 7, 5);

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideDateConversionSamples() {
        return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
                Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
                Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5))
        );
    }

    static Stream<Arguments> provideInvalidDateArguments() {
        return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day
                Arguments.of(1900, 2, 30),  // Invalid day in February (leap)
                Arguments.of(1899, 2, 29),  // Invalid day in February (common)
                Arguments.of(1900, 4, 31)   // Invalid day for 30-day month
        );
    }

    //-----------------------------------------------------------------------
    // Nested test classes for better organization
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @Test
        @DisplayName("Chronology.of(\"Julian\") should return the singleton instance")
        void of_shouldReturnJulianChronologyForKnownNames() {
            Chronology chrono = Chronology.of("Julian");
            assertNotNull(chrono);
            assertEquals(JulianChronology.INSTANCE, chrono);
            assertEquals("Julian", chrono.getId());
            assertEquals("julian", chrono.getCalendarType());
        }

        @Test
        @DisplayName("Chronology.of(\"julian\") should return the singleton instance")
        void of_shouldReturnJulianChronologyForLowerCaseId() {
            Chronology chrono = Chronology.of("julian");
            assertNotNull(chrono);
            assertEquals(JulianChronology.INSTANCE, chrono);
        }

        @Test
        @DisplayName("eraOf() should return correct JulianEra")
        void eraOf_shouldReturnCorrectEra() {
            assertEquals(JulianEra.AD, JulianChronology.INSTANCE.eraOf(1));
            assertEquals(JulianEra.BC, JulianChronology.INSTANCE.eraOf(0));
        }

        @Test
        @DisplayName("eraOf() should throw for invalid era value")
        void eraOf_shouldThrowForInvalidValue() {
            assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.eraOf(2));
        }

        @Test
        @DisplayName("eras() should return a list of BC and AD")
        void eras_shouldReturnListOfEras() {
            List<Era> eras = JulianChronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(JulianEra.BC));
            assertTrue(eras.contains(JulianEra.AD));
        }

        @Test
        @DisplayName("range() should return correct ranges for chrono fields")
        void range_shouldReturnCorrectRanges() {
            assertEquals(ValueRange.of(1, 7), JulianChronology.INSTANCE.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 28, 31), JulianChronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 365, 366), JulianChronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), JulianChronology.INSTANCE.range(MONTH_OF_YEAR));
        }
    }

    @Nested
    @DisplayName("Date Creation")
    class DateCreationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideInvalidDateArguments")
        @DisplayName("of() should throw exception for invalid date components")
        void of_shouldThrowExceptionForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }

        @Test
        @DisplayName("dateYearDay() should throw exception for invalid day of year")
        void dateYearDay_shouldThrowForInvalidDayOfYear() {
            assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.dateYearDay(2001, 366)); // 2001 is not a leap year
        }
    }

    @Nested
    @DisplayName("Date Conversion")
    class DateConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideDateConversionSamples")
        @DisplayName("LocalDate.from(julianDate) should produce the correct ISO date")
        void fromJulianDate_shouldConvertToCorrectIsoLocalDate(JulianDate julian, LocalDate expectedIso) {
            assertEquals(expectedIso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideDateConversionSamples")
        @DisplayName("JulianDate.from(localDate) should produce the correct Julian date")
        void fromLocalDate_shouldConvertToCorrectJulianDate(JulianDate expectedJulian, LocalDate iso) {
            assertEquals(expectedJulian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideDateConversionSamples")
        @DisplayName("toEpochDay() should match ISO date's epoch day")
        void toEpochDay_shouldMatchIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideDateConversionSamples")
        @DisplayName("chronology.dateEpochDay() should create correct Julian date")
        void dateEpochDay_shouldCreateCorrectJulianDate(JulianDate expectedJulian, LocalDate iso) {
            assertEquals(expectedJulian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestJulianChronology#provideDateConversionSamples")
        @DisplayName("chronology.date(TemporalAccessor) should create correct Julian date from ISO date")
        void dateFromTemporal_shouldCreateCorrectJulianDate(JulianDate expectedJulian, LocalDate iso) {
            assertEquals(expectedJulian, JulianChronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Leap Year Logic")
    class LeapYearTests {

        @Test
        @DisplayName("isLeapYear() should be true for years divisible by 4")
        void isLeapYear_shouldBeTrueForYearsDivisibleByFour() {
            for (int year = -200; year < 200; year++) {
                boolean expected = (year % 4) == 0;
                assertEquals(expected, JulianChronology.INSTANCE.isLeapYear(year));
            }
        }

        static Stream<Arguments> provideMonthLengths() {
            return Stream.of(
                    Arguments.of(1900, 1, 31),
                    Arguments.of(1900, 2, 29), // Julian leap year
                    Arguments.of(1901, 2, 28), // Julian common year
                    Arguments.of(1904, 2, 29), // Julian leap year
                    Arguments.of(2000, 2, 29), // Julian leap year
                    Arguments.of(1900, 4, 30)
            );
        }

        @ParameterizedTest
        @MethodSource("provideMonthLengths")
        @DisplayName("lengthOfMonth() should return correct day count")
        void lengthOfMonth_shouldReturnCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Era and Proleptic Year")
    class EraTests {

        @Test
        @DisplayName("getEra() and get(YEAR_OF_ERA) should be consistent")
        void eraAndYearOfEra_shouldBeConsistent() {
            for (int year = -200; year < 200; year++) {
                JulianDate date = JulianChronology.INSTANCE.date(year, 1, 1);
                JulianEra expectedEra = (year <= 0) ? JulianEra.BC : JulianEra.AD;
                int expectedYoe = (year <= 0) ? 1 - year : year;

                assertEquals(expectedEra, date.getEra());
                assertEquals(expectedYoe, date.get(YEAR_OF_ERA));
            }
        }

        @Test
        @DisplayName("prolepticYear() should correctly convert from era and year-of-era")
        void prolepticYear_shouldConvertFromEraAndYoe() {
            assertEquals(4, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 4));
            assertEquals(1, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 1));
            assertEquals(0, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 1));
            assertEquals(-1, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 2));
        }

        @Test
        @DisplayName("prolepticYear() should throw ClassCastException for non-Julian era")
        void prolepticYear_shouldThrowForWrongEraType() {
            assertThrows(ClassCastException.class, () -> JulianChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        }
    }

    @Nested
    @DisplayName("Field Access and Manipulation")
    class FieldAccessAndManipulationTests {

        static Stream<Arguments> provideFieldsAndExpectedValues() {
            return Stream.of(
                    Arguments.of(DAY_OF_WEEK, 7L),
                    Arguments.of(DAY_OF_MONTH, 26L),
                    Arguments.of(DAY_OF_YEAR, 31L + 28 + 31 + 30 + 26),
                    Arguments.of(MONTH_OF_YEAR, 5L),
                    Arguments.of(PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                    Arguments.of(YEAR, 2014L),
                    Arguments.of(ERA, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldsAndExpectedValues")
        @DisplayName("getLong() should return correct value for various fields")
        void getLong_shouldReturnCorrectValue(TemporalField field, long expected) {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> provideWithFieldAndValue() {
            return Stream.of(
                    Arguments.of(DAY_OF_WEEK, 3L, JulianDate.of(2014, 5, 22)),
                    Arguments.of(DAY_OF_MONTH, 31L, JulianDate.of(2014, 5, 31)),
                    Arguments.of(DAY_OF_YEAR, 365L, JulianDate.of(2014, 12, 31)),
                    Arguments.of(MONTH_OF_YEAR, 7L, JulianDate.of(2014, 7, 26)),
                    Arguments.of(YEAR, 2012L, JulianDate.of(2012, 5, 26)),
                    Arguments.of(ERA, 0L, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithFieldAndValue")
        @DisplayName("with(field, value) should return a correctly modified date")
        void with_shouldModifyFieldCorrectly(TemporalField field, long value, JulianDate expectedDate) {
            JulianDate baseDate = JulianDate.of(2014, 5, 26);
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        @DisplayName("with() should handle month length changes correctly")
        void with_shouldHandleMonthLengthChanges() {
            assertEquals(JulianDate.of(2011, 2, 28), JulianDate.of(2011, 3, 31).with(MONTH_OF_YEAR, 2));
            assertEquals(JulianDate.of(2012, 2, 29), JulianDate.of(2012, 3, 31).with(MONTH_OF_YEAR, 2));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should work correctly")
        void with_shouldWorkWithTemporalAdjuster() {
            JulianDate date = JulianDate.of(2012, 2, 23);
            JulianDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
            assertEquals(JulianDate.of(2012, 2, 29), lastDay);
        }

        @Test
        @DisplayName("range(field) should throw for unsupported fields")
        void range_shouldThrowForUnsupportedField() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_JULIAN_DATE.range(MINUTE_OF_DAY));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticTests {

        static Stream<Arguments> providePlusAndMinusCases() {
            return Stream.of(
                    Arguments.of(8, DAYS, JulianDate.of(2014, 6, 3)),
                    Arguments.of(3, WEEKS, JulianDate.of(2014, 6, 16)),
                    Arguments.of(3, MONTHS, JulianDate.of(2014, 8, 26)),
                    Arguments.of(3, YEARS, JulianDate.of(2017, 5, 26)),
                    Arguments.of(3, DECADES, JulianDate.of(2044, 5, 26)),
                    Arguments.of(3, CENTURIES, JulianDate.of(2314, 5, 26)),
                    Arguments.of(3, MILLENNIA, JulianDate.of(5014, 5, 26)),
                    Arguments.of(1, ERAS, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("plus() should correctly add various temporal units")
        void plus_shouldAddUnitsCorrectly(long amount, TemporalUnit unit, JulianDate expected) {
            JulianDate start = (unit == ERAS) ? JulianDate.of(-4013, 5, 26) : JulianDate.of(2014, 5, 26);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("minus() should correctly subtract various temporal units")
        void minus_shouldSubtractUnitsCorrectly(long amount, TemporalUnit unit, JulianDate start) {
            JulianDate expected = (unit == ERAS) ? JulianDate.of(-4013, 5, 26) : JulianDate.of(2014, 5, 26);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        @DisplayName("plus() should throw for unsupported units")
        void plus_shouldThrowForUnsupportedUnit() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_JULIAN_DATE.plus(1, MINUTES));
        }

        static Stream<Arguments> provideUntilCases() {
            return Stream.of(
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 1), DAYS, 6L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 2), WEEKS, 1L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 26), MONTHS, 1L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2015, 5, 26), YEARS, 1L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2024, 5, 26), DECADES, 1L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2114, 5, 26), CENTURIES, 1L),
                    Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(3014, 5, 26), MILLENNIA, 1L),
                    Arguments.of(JulianDate.of(-2013, 5, 26), JulianDate.of(2014, 5, 26), ERAS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        @DisplayName("until() should correctly calculate the duration between dates")
        void until_shouldCalculateDurationCorrectly(JulianDate start, JulianDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        @DisplayName("until() should throw for unsupported units")
        void until_shouldThrowForUnsupportedUnit() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_JULIAN_DATE.until(SAMPLE_JULIAN_DATE.plus(1, DAYS), MINUTES));
        }
    }

    @Nested
    @DisplayName("Interoperability with java.time")
    class InteroperabilityTests {

        @Test
        @DisplayName("LocalDate.with(julianDate) should adjust to the correct ISO date")
        void localDate_withJulianDate_shouldAdjustCorrectly() {
            LocalDate test = LocalDate.MIN.with(SAMPLE_JULIAN_DATE);
            assertEquals(SAMPLE_ISO_DATE, test);
        }

        @Test
        @DisplayName("LocalDateTime.with(julianDate) should adjust to the correct ISO date-time")
        void localDateTime_withJulianDate_shouldAdjustCorrectly() {
            LocalDateTime test = LocalDateTime.MIN.with(SAMPLE_JULIAN_DATE);
            assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), test);
        }

        @Test
        @DisplayName("plus(Period) should throw if period is not Julian")
        void plus_shouldThrowForNonJulianPeriod() {
            assertThrows(DateTimeException.class, () -> SAMPLE_JULIAN_DATE.plus(Period.ofMonths(2)));
        }

        @Test
        @DisplayName("minus(Period) should throw if period is not Julian")
        void minus_shouldThrowForNonJulianPeriod() {
            assertThrows(DateTimeException.class, () -> SAMPLE_JULIAN_DATE.minus(Period.ofMonths(2)));
        }
    }

    @Nested
    @DisplayName("Object method contracts")
    class ObjectMethodTests {

        @Test
        @DisplayName("equals() and hashCode() should follow their contract")
        void equalsAndHashCode_shouldAdhereToContract() {
            new EqualsTester()
                    .addEqualityGroup(JulianDate.of(2000, 1, 3), JulianDate.of(2000, 1, 3))
                    .addEqualityGroup(JulianDate.of(2000, 1, 4))
                    .addEqualityGroup(JulianDate.of(2000, 2, 3))
                    .addEqualityGroup(JulianDate.of(2001, 1, 3))
                    .testEquals();
        }

        @Test
        @DisplayName("toString() should return a descriptive string")
        void toString_shouldReturnDescriptiveString() {
            assertEquals("Julian AD 2012-06-23", SAMPLE_JULIAN_DATE.toString());
            assertEquals("Julian BC 1-01-01", JulianDate.of(0, 1, 1).toString());
        }
    }
}