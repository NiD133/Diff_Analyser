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
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link InternationalFixedChronology}.
 */
@SuppressWarnings({"static-method", "javadoc"})
@DisplayName("InternationalFixedChronology")
public class TestInternationalFixedChronology {

    private static final InternationalFixedDate DATE_2012_06_29 = InternationalFixedDate.of(2012, 6, 29);
    private static final InternationalFixedDate DATE_2014_13_29 = InternationalFixedDate.of(2014, 13, 29);
    private static final InternationalFixedDate DATE_2014_05_26 = InternationalFixedDate.of(2014, 5, 26);

    //-----------------------------------------------------------------------
    // Chronology.of(String)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("of(String) should return singleton instance")
    void chronology_of_shouldReturnSingletonForIfc() {
        Chronology chrono = Chronology.of("Ifc");
        assertNotNull(chrono);
        assertEquals(InternationalFixedChronology.INSTANCE, chrono);
        assertEquals("Ifc", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Date Creation and Conversion
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Conversion")
    class CreationAndConversionTests {

        static Stream<Arguments> provideSampleDateConversions() {
            return Stream.of(
                    Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                    Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
                    Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
                    Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)),
                    Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap year
                    Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)), // Leap year
                    Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3))
            );
        }

        @ParameterizedTest
        @MethodSource("provideSampleDateConversions")
        @DisplayName("LocalDate.from(fixedDate) should convert correctly")
        void localDate_from_fixedDate_shouldConvertCorrectly(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("provideSampleDateConversions")
        @DisplayName("InternationalFixedDate.from(localDate) should convert correctly")
        void fixedDate_from_localDate_shouldConvertCorrectly(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("provideSampleDateConversions")
        @DisplayName("chronology.date(temporal) should convert correctly")
        void chronology_date_fromTemporal_shouldConvertCorrectly(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("provideSampleDateConversions")
        @DisplayName("toEpochDay should be consistent with ISO date")
        void toEpochDay_shouldBeConsistentWithIso(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("provideSampleDateConversions")
        @DisplayName("chronology.dateEpochDay should create correct date")
        void chronology_dateEpochDay_shouldCreateCorrectDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                    Arguments.of(0, 1, 1), // Invalid year
                    Arguments.of(1900, 14, 1), // Invalid month
                    Arguments.of(1900, 1, 29), // Invalid day for month
                    Arguments.of(1900, 13, 30), // Invalid day for Year Day month
                    Arguments.of(1900, 6, 29) // Invalid Leap Day in non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        @DisplayName("of() for invalid date parts throws DateTimeException")
        void of_forInvalidDateParts_throwsDateTimeException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @Test
        @DisplayName("dateYearDay() for day 366 in non-leap year throws DateTimeException")
        void chronology_dateYearDay_forInvalidDay_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.dateYearDay(2001, 366));
        }
    }

    //-----------------------------------------------------------------------
    // Leap Year and Month Length
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Leap Year and Month Length")
    class LeapAndLengthTests {

        @Test
        @DisplayName("isLeapYear should be consistent with Gregorian rule")
        void isLeapYear_shouldBeConsistentWithGregorianRule() {
            assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(4));
            assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(400));
            assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(1));
            assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(100));
            assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(1900));
        }

        @Test
        @DisplayName("lengthOfYear should be 366 for leap years, 365 otherwise")
        void lengthOfYear_shouldReturnCorrectLength() {
            assertEquals(366, InternationalFixedDate.of(2000, 1, 1).lengthOfYear());
            assertEquals(365, InternationalFixedDate.of(2001, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> provideDatesAndExpectedMonthLengths() {
            return Stream.of(
                    Arguments.of(1900, 1, 28), // Standard month
                    Arguments.of(1900, 13, 29), // Year Day month
                    Arguments.of(1904, 6, 29) // Leap Day month in a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Era
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Era Handling")
    class EraTests {

        @Test
        void eraOf_forValidValue_returnsEra() {
            assertEquals(InternationalFixedEra.CE, InternationalFixedChronology.INSTANCE.eraOf(1));
        }

        @Test
        void eraOf_forInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void eras_returnsSingletonList() {
            List<Era> eras = InternationalFixedChronology.INSTANCE.eras();
            assertEquals(1, eras.size());
            assertTrue(eras.contains(InternationalFixedEra.CE));
        }

        @Test
        void prolepticYear_forValidEra_returnsYear() {
            assertEquals(2000, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2000));
            assertEquals(1, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1));
        }

        @Test
        void prolepticYear_forInvalidYear_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }

        @Test
        void prolepticYear_forInvalidEra_throwsException() {
            assertThrows(ClassCastException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        }
    }

    //-----------------------------------------------------------------------
    // Range
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("range(TemporalField)")
    class RangeTests {

        @Test
        void chronology_range_shouldReturnCorrectRangesForFields() {
            assertEquals(ValueRange.of(1, 29), InternationalFixedChronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 365, 366), InternationalFixedChronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 13), InternationalFixedChronology.INSTANCE.range(MONTH_OF_YEAR));
            assertEquals(ValueRange.of(1, 1_000_000), InternationalFixedChronology.INSTANCE.range(YEAR));
            assertEquals(ValueRange.of(1, 1), InternationalFixedChronology.INSTANCE.range(ERA));
        }

        static Stream<Arguments> provideDateAndFieldRanges() {
            return Stream.of(
                    // For a standard month
                    Arguments.of(InternationalFixedDate.of(2011, 1, 1), DAY_OF_MONTH, ValueRange.of(1, 28)),
                    // For the Leap Day month in a leap year
                    Arguments.of(DATE_2012_06_29, DAY_OF_MONTH, ValueRange.of(1, 29)),
                    // For the Leap Day month in a non-leap year
                    Arguments.of(InternationalFixedDate.of(2011, 6, 1), DAY_OF_MONTH, ValueRange.of(1, 28)),
                    // For the Year Day month
                    Arguments.of(DATE_2014_13_29, DAY_OF_MONTH, ValueRange.of(1, 29)),
                    // Day of year
                    Arguments.of(InternationalFixedDate.of(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)),
                    Arguments.of(DATE_2012_06_29, DAY_OF_YEAR, ValueRange.of(1, 366)),
                    // Special day ranges
                    Arguments.of(DATE_2012_06_29, DAY_OF_WEEK, ValueRange.of(0, 0)),
                    Arguments.of(DATE_2014_13_29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldRanges")
        @DisplayName("date.range() should return correct range for the specific date")
        void date_range_shouldReturnCorrectRanges(InternationalFixedDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        @Test
        void range_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> DATE_2012_06_29.range(MINUTE_OF_DAY));
        }
    }

    //-----------------------------------------------------------------------
    // getLong(TemporalField)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("getLong(TemporalField)")
    class GetLongTests {

        @Test
        void getLong_forStandardDate_returnsCorrectValues() {
            assertEquals(5, DATE_2014_05_26.getLong(DAY_OF_WEEK));
            assertEquals(26, DATE_2014_05_26.getLong(DAY_OF_MONTH));
            assertEquals(138, DATE_2014_05_26.getLong(DAY_OF_YEAR)); // 4*28 + 26
            assertEquals(4, DATE_2014_05_26.getLong(ALIGNED_WEEK_OF_MONTH));
            assertEquals(20, DATE_2014_05_26.getLong(ALIGNED_WEEK_OF_YEAR));
            assertEquals(5, DATE_2014_05_26.getLong(MONTH_OF_YEAR));
            assertEquals(2014 * 13 + 5 - 1, DATE_2014_05_26.getLong(PROLEPTIC_MONTH));
            assertEquals(2014, DATE_2014_05_26.getLong(YEAR));
            assertEquals(1, DATE_2014_05_26.getLong(ERA));
        }

        @Test
        void getLong_forLeapDay_returnsCorrectValues() {
            assertEquals(0, DATE_2012_06_29.getLong(DAY_OF_WEEK)); // Not part of a week
            assertEquals(29, DATE_2012_06_29.getLong(DAY_OF_MONTH));
            assertEquals(169, DATE_2012_06_29.getLong(DAY_OF_YEAR)); // 6*28 + 1
            assertEquals(0, DATE_2012_06_29.getLong(ALIGNED_WEEK_OF_MONTH));
            assertEquals(6, DATE_2012_06_29.getLong(MONTH_OF_YEAR));
        }

        @Test
        void getLong_forYearDay_returnsCorrectValues() {
            assertEquals(0, DATE_2014_13_29.getLong(DAY_OF_WEEK)); // Not part of a week
            assertEquals(29, DATE_2014_13_29.getLong(DAY_OF_MONTH));
            assertEquals(365, DATE_2014_13_29.getLong(DAY_OF_YEAR)); // 13*28 + 1
            assertEquals(0, DATE_2014_13_29.getLong(ALIGNED_WEEK_OF_YEAR));
            assertEquals(13, DATE_2014_13_29.getLong(MONTH_OF_YEAR));
        }

        @Test
        void getLong_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> DATE_2012_06_29.getLong(MINUTE_OF_DAY));
        }
    }

    //-----------------------------------------------------------------------
    // with(TemporalField, long)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("with(TemporalField, long)")
    class WithFieldTests {

        static Stream<Arguments> provideWithFieldAndValue() {
            return Stream.of(
                    Arguments.of(DAY_OF_WEEK, 1, InternationalFixedDate.of(2014, 5, 22)),
                    Arguments.of(DAY_OF_MONTH, 1, InternationalFixedDate.of(2014, 5, 1)),
                    Arguments.of(DAY_OF_YEAR, 365, DATE_2014_13_29),
                    Arguments.of(MONTH_OF_YEAR, 1, InternationalFixedDate.of(2014, 1, 26)),
                    Arguments.of(YEAR, 2012, InternationalFixedDate.of(2012, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithFieldAndValue")
        @DisplayName("should return a new date with the updated field")
        void with_validField_returnsUpdatedDate(TemporalField field, long value, InternationalFixedDate expected) {
            assertEquals(expected, DATE_2014_05_26.with(field, value));
        }

        @Test
        @DisplayName("with YEAR should handle leap day correctly")
        void with_year_handlesLeapDayCorrectly() {
            // From a leap day, setting year to a non-leap year adjusts the day
            assertEquals(InternationalFixedDate.of(2013, 6, 28), DATE_2012_06_29.with(YEAR, 2013));
            // From a leap day, setting year to another leap year keeps the day
            assertEquals(InternationalFixedDate.of(2016, 6, 29), DATE_2012_06_29.with(YEAR, 2016));
        }

        static Stream<Arguments> provideInvalidFieldAndValue() {
            return Stream.of(
                    Arguments.of(DAY_OF_MONTH, 30), // Day too large for a standard month
                    Arguments.of(DAY_OF_YEAR, 366), // Day too large for a non-leap year
                    Arguments.of(MONTH_OF_YEAR, 14), // Month too large
                    Arguments.of(YEAR, 0) // Invalid year
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidFieldAndValue")
        @DisplayName("for an invalid value should throw DateTimeException")
        void with_invalidValue_throwsException(TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> DATE_2014_05_26.with(field, value));
        }

        @Test
        @DisplayName("for an unsupported field should throw UnsupportedTemporalTypeException")
        void with_unsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> DATE_2012_06_29.with(MINUTE_OF_DAY, 0));
        }
    }

    //-----------------------------------------------------------------------
    // with(TemporalAdjuster)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("with(TemporalAdjuster)")
    class WithAdjusterTests {

        @Test
        void with_lastDayOfMonth_returnsCorrectDate() {
            assertEquals(InternationalFixedDate.of(2009, 6, 28), InternationalFixedDate.of(2009, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(DATE_2012_06_29, InternationalFixedDate.of(2012, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_LocalDate_adjustsToCorrectDate() {
            InternationalFixedDate base = InternationalFixedDate.of(2000, 1, 4);
            InternationalFixedDate result = base.with(LocalDate.of(2012, 7, 6));
            assertEquals(InternationalFixedDate.of(2012, 7, 19), result);
        }

        @Test
        void with_Month_throwsException() {
            InternationalFixedDate base = InternationalFixedDate.of(2000, 1, 4);
            assertThrows(DateTimeException.class, () -> base.with(Month.APRIL));
        }

        @Test
        void localDate_with_InternationalFixedDate_adjustsToCorrectDate() {
            InternationalFixedDate fixed = InternationalFixedDate.of(2012, 7, 19);
            LocalDate result = LocalDate.MIN.with(fixed);
            assertEquals(LocalDate.of(2012, 7, 6), result);
        }
    }

    //-----------------------------------------------------------------------
    // plus(long, TemporalUnit) and minus(long, TemporalUnit)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus/minus(long, TemporalUnit)")
    class PlusMinusTests {

        static Stream<Arguments> providePlusAndMinusCases() {
            return Stream.of(
                    Arguments.of(DATE_2014_05_26, 8, DAYS, InternationalFixedDate.of(2014, 6, 6)),
                    Arguments.of(DATE_2014_05_26, 3, WEEKS, InternationalFixedDate.of(2014, 6, 19)),
                    Arguments.of(DATE_2014_05_26, 3, MONTHS, InternationalFixedDate.of(2014, 8, 26)),
                    Arguments.of(DATE_2014_05_26, 3, YEARS, InternationalFixedDate.of(2017, 5, 26)),
                    Arguments.of(DATE_2014_05_26, 3, DECADES, InternationalFixedDate.of(2044, 5, 26)),
                    // Leap Day handling
                    Arguments.of(DATE_2012_06_29, 3, YEARS, InternationalFixedDate.of(2015, 6, 28)),
                    Arguments.of(DATE_2012_06_29, 4, YEARS, InternationalFixedDate.of(2016, 6, 29)),
                    // Year Day handling
                    Arguments.of(DATE_2014_13_29, 8, DAYS, InternationalFixedDate.of(2015, 1, 8)),
                    Arguments.of(DATE_2014_13_29, 3, MONTHS, InternationalFixedDate.of(2015, 3, 28))
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("plus() should return correct date")
        void plus_shouldReturnCorrectDate(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("minus() should return correct date")
        void minus_shouldReturnCorrectDate(InternationalFixedDate expected, long amount, TemporalUnit unit, InternationalFixedDate base) {
            assertEquals(expected, base.minus(amount, unit));
        }

        @Test
        void plus_forUnsupportedUnit_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> DATE_2012_06_29.plus(1, MINUTES));
        }
    }

    //-----------------------------------------------------------------------
    // until(endExclusive, unit)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("until(endExclusive, unit)")
    class UntilTests {

        static Stream<Arguments> provideUntilCases() {
            InternationalFixedDate start = InternationalFixedDate.of(2014, 5, 26);
            return Stream.of(
                    Arguments.of(start, InternationalFixedDate.of(2014, 6, 4), DAYS, 6L),
                    Arguments.of(start, InternationalFixedDate.of(2014, 6, 5), WEEKS, 1L),
                    Arguments.of(start, InternationalFixedDate.of(2014, 6, 26), MONTHS, 1L),
                    Arguments.of(start, InternationalFixedDate.of(2015, 5, 26), YEARS, 1L),
                    Arguments.of(start, InternationalFixedDate.of(2024, 5, 26), DECADES, 1L),
                    Arguments.of(start, InternationalFixedDate.of(2114, 5, 26), CENTURIES, 1L),
                    Arguments.of(start, InternationalFixedDate.of(3014, 5, 26), MILLENNIA, 1L),
                    Arguments.of(start, InternationalFixedDate.of(3014, 5, 26), ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        @DisplayName("should return correct duration in given unit")
        void until_inUnits_returnsCorrectDuration(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilPeriodCases() {
            return Stream.of(
                    Arguments.of(DATE_2014_05_26, InternationalFixedDate.of(2015, 6, 28), 1, 1, 2),
                    // Across leap day
                    Arguments.of(InternationalFixedDate.of(2011, 13, 29), DATE_2012_06_29, 0, 6, 0),
                    // From leap day
                    Arguments.of(DATE_2012_06_29, InternationalFixedDate.of(2016, 6, 29), 4, 0, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        @DisplayName("should return correct ChronoPeriod")
        void until_asPeriod_returnsCorrectPeriod(InternationalFixedDate start, InternationalFixedDate end, int y, int m, int d) {
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(y, m, d);
            assertEquals(expected, start.until(end));
        }

        @Test
        void until_forUnsupportedUnit_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> DATE_2014_05_26.until(DATE_2012_06_29, MINUTES));
        }
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode() / toString()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        @Test
        void equals_and_hashCode_shouldFollowContract() {
            new EqualsTester()
                    .addEqualityGroup(InternationalFixedDate.of(2000, 1, 3), InternationalFixedDate.of(2000, 1, 3))
                    .addEqualityGroup(InternationalFixedDate.of(2000, 1, 4))
                    .addEqualityGroup(DATE_2012_06_29, DATE_2012_06_29)
                    .addEqualityGroup(DATE_2014_13_29, DATE_2014_13_29)
                    .testEquals();
        }

        @Test
        void toString_shouldReturnFormattedString() {
            assertEquals("Ifc CE 2012/06/23", InternationalFixedDate.of(2012, 6, 23).toString());
            assertEquals("Ifc CE 2012/06/29", DATE_2012_06_29.toString());
            assertEquals("Ifc CE 2012/13/29", InternationalFixedDate.of(2012, 13, 29).toString());
        }
    }
}