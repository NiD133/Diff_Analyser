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
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for the {@link Symmetry454Chronology}.
 */
@DisplayName("Symmetry454Chronology")
public class TestSymmetry454Chronology {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Symmetry454Date instances and their equivalent ISO LocalDate.
     *
     * @return a stream of arguments: (Symmetry454Date, LocalDate).
     */
    public static Stream<Arguments> sampleSymmetry454AndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(   1,  1,  1), LocalDate.of(   1,  1,  1)),
            Arguments.of(Symmetry454Date.of( 272,  2, 30), LocalDate.of( 272,  2, 27)), // Constantine the Great
            Arguments.of(Symmetry454Date.of( 742,  3, 25), LocalDate.of( 742,  4,  2)), // Charlemagne
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)), // Battle of Hastings
            Arguments.of(Symmetry454Date.of(1304,  7, 21), LocalDate.of(1304,  7, 20)), // Petrarch
            Arguments.of(Symmetry454Date.of(1452,  4, 11), LocalDate.of(1452,  4, 15)), // Leonardo da Vinci
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)), // Columbus's landfall
            Arguments.of(Symmetry454Date.of(1564,  2, 20), LocalDate.of(1564,  2, 15)), // Galileo Galilei
            Arguments.of(Symmetry454Date.of(1564,  4, 28), LocalDate.of(1564,  4, 26)), // William Shakespeare
            Arguments.of(Symmetry454Date.of(1643,  1,  7), LocalDate.of(1643,  1,  4)), // Sir Isaac Newton
            Arguments.of(Symmetry454Date.of(1707,  4, 12), LocalDate.of(1707,  4, 15)), // Leonhard Euler
            Arguments.of(Symmetry454Date.of(1789,  7, 16), LocalDate.of(1789,  7, 14)), // Storming of the Bastille
            Arguments.of(Symmetry454Date.of(1879,  3, 12), LocalDate.of(1879,  3, 14)), // Albert Einstein
            Arguments.of(Symmetry454Date.of(1941,  9,  9), LocalDate.of(1941,  9,  9)), // Dennis Ritchie
            Arguments.of(Symmetry454Date.of(1970,  1,  4), LocalDate.of(1970,  1,  1)), // Unix epoch
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000,  1,  1))  // Start of 21st century
        );
    }

    /**
     * Provides invalid date components.
     *
     * @return a stream of arguments: (year, month, dayOfMonth).
     */
    public static Stream<Arguments> invalidDateParts() {
        return Stream.of(
            Arguments.of(-1, 13, 28),
            Arguments.of(2000, 13, 1),
            Arguments.of(2000, 1, 0),
            Arguments.of(2000, 1, 29), // Jan has 28 days
            Arguments.of(2000, 2, 36), // Feb has 35 days
            Arguments.of(2004, 12, 36) // Dec has 35 days in a leap year
        );
    }

    /**
     * Provides years that are not leap years in the Symmetry454 calendar.
     *
     * @return a stream of arguments: (year).
     */
    public static Stream<Arguments> nonSymmetry454LeapYears() {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(100),
            Arguments.of(200),
            Arguments.of(2000)
        );
    }

    /**
     * Provides eras from other calendar systems.
     *
     * @return a stream of arguments: (era).
     */
    public static Stream<Arguments> nonIsoEras() {
        return Stream.of(
            Arguments.of(AccountingEra.BCE),
            Arguments.of(CopticEra.AM),
            Arguments.of(DiscordianEra.YOLD),
            Arguments.of(EthiopicEra.INCARNATION),
            Arguments.of(HijrahEra.AH),
            Arguments.of(InternationalFixedEra.CE),
            Arguments.of(JapaneseEra.HEISEI),
            Arguments.of(JulianEra.AD),
            Arguments.of(MinguoEra.ROC),
            Arguments.of(PaxEra.CE),
            Arguments.of(ThaiBuddhistEra.BE)
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {
        @Test
        @DisplayName("of(String) should return singleton instance")
        void of_shouldReturnSingletonInstance() {
            Chronology chrono = Chronology.of("Sym454");
            assertNotNull(chrono);
            assertEquals(Symmetry454Chronology.INSTANCE, chrono);
            assertEquals("Sym454", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        @Test
        @DisplayName("prolepticYear() should calculate correctly for CE and BCE eras")
        void prolepticYear_withIsoEra_shouldCalculateCorrectly() {
            assertEquals(2000, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
            assertEquals(1, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
            assertEquals(0, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.BCE, 1));
            assertEquals(-1, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.BCE, 2));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#nonIsoEras")
        @DisplayName("prolepticYear() with non-ISO era should throw ClassCastException")
        void prolepticYear_withNonIsoEra_shouldThrowClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        @DisplayName("eraOf() should return correct era for valid values")
        void eraOf_shouldReturnCorrectEra() {
            assertEquals(IsoEra.BCE, Symmetry454Chronology.INSTANCE.eraOf(0));
            assertEquals(IsoEra.CE, Symmetry454Chronology.INSTANCE.eraOf(1));
        }

        @Test
        @DisplayName("eraOf() with invalid value should throw DateTimeException")
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.eraOf(2));
        }

        @Test
        @DisplayName("eras() should return a list containing BCE and CE")
        void eras_shouldReturnBceAndCe() {
            List<Era> eras = Symmetry454Chronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(IsoEra.BCE));
            assertTrue(eras.contains(IsoEra.CE));
        }

        @Test
        @DisplayName("range() for chronology fields should return correct ranges")
        void range_forChronologyFields_shouldReturnCorrectRanges() {
            assertEquals(ValueRange.of(1, 7), Symmetry454Chronology.INSTANCE.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 28, 35), Symmetry454Chronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 364, 371), Symmetry454Chronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), Symmetry454Chronology.INSTANCE.range(MONTH_OF_YEAR));
            assertEquals(ValueRange.of(-1_000_000L, 1_000_000), Symmetry454Chronology.INSTANCE.range(YEAR));
            assertEquals(ValueRange.of(0, 1), Symmetry454Chronology.INSTANCE.range(ERA));
        }
    }

    @Nested
    @DisplayName("Factory methods and Conversions")
    class FactoryAndConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#sampleSymmetry454AndIsoDates")
        @DisplayName("from(LocalDate) should create correct Symmetry454Date")
        void from_LocalDate_shouldCreateCorrectSymmetry454Date(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#sampleSymmetry454AndIsoDates")
        @DisplayName("LocalDate.from(Symmetry454Date) should create correct LocalDate")
        void from_Symmetry454Date_shouldCreateCorrectLocalDate(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#sampleSymmetry454AndIsoDates")
        @DisplayName("toEpochDay() should return the correct epoch day")
        void toEpochDay_shouldReturnCorrectValue(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#sampleSymmetry454AndIsoDates")
        @DisplayName("dateEpochDay() should create the correct date from epoch day")
        void dateEpochDay_shouldCreateCorrectDate(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#sampleSymmetry454AndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) should create correct date")
        void chronologyDate_fromTemporal_shouldCreateCorrectDate(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        @Test
        @DisplayName("date() with era should handle CE correctly")
        void date_withEra_shouldHandleCeCorrectly() {
            for (int year = 1; year < 200; year++) {
                Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
                Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
                assertEquals(base, eraBased);
            }
        }

        @Test
        @DisplayName("date() with era should handle BCE correctly")
        void date_withEra_shouldHandleBceCorrectly() {
            for (int year = 1; year < 200; year++) {
                Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(-year + 1, 1, 1);
                Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.BCE, year, 1, 1);
                assertEquals(base, eraBased);
            }
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation")
    class InvalidDateCreationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#invalidDateParts")
        @DisplayName("of() with invalid date parts should throw DateTimeException")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestSymmetry454Chronology#nonSymmetry454LeapYears")
        @DisplayName("of() with day 29 of month 12 for a non-leap year should throw DateTimeException")
        void of_withDay29InMonth12ForNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }

        @Test
        @DisplayName("dateYearDay() with day 365 in a normal year should throw DateTimeException")
        void dateYearDay_withDay365InNormalYear_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.dateYearDay(2000, 365));
        }
    }

    @Nested
    @DisplayName("Leap Year and Month Properties")
    class LeapYearAndMonthTests {
        @Test
        @DisplayName("isLeapYear() should return correct value for specific years")
        void isLeapYear_shouldReturnCorrectValueForSpecificYears() {
            assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(3));
            assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(6));
            assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(9));
            assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2000));
            assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2004));
        }

        @Test
        @DisplayName("isLeapWeek() should be true for all days in a leap week")
        void isLeapWeek_shouldBeTrueForDaysInLeapWeek() {
            for (int day = 29; day <= 35; day++) {
                assertTrue(Symmetry454Date.of(2015, 12, day).isLeapWeek());
            }
        }

        @Test
        @DisplayName("lengthOfMonth() should return 28 for short months and 35 for long months")
        void lengthOfMonth_shouldReturnCorrectLength() {
            assertEquals(28, Symmetry454Date.of(2000, 1, 1).lengthOfMonth());
            assertEquals(35, Symmetry454Date.of(2000, 2, 1).lengthOfMonth());
            assertEquals(28, Symmetry454Date.of(2000, 3, 1).lengthOfMonth());
        }

        @Test
        @DisplayName("lengthOfMonth() for December should depend on whether it's a leap year")
        void lengthOfMonth_forDecember_shouldDependOnLeapYear() {
            assertEquals(28, Symmetry454Date.of(2000, 12, 1).lengthOfMonth(), "Normal year December");
            assertEquals(35, Symmetry454Date.of(2004, 12, 1).lengthOfMonth(), "Leap year December");
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {
        @Test
        @DisplayName("getLong() should return correct values for various fields")
        void getLong_forSupportedFields_shouldReturnCorrectValue() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(5, date.getLong(DAY_OF_WEEK));
            assertEquals(26, date.getLong(DAY_OF_MONTH));
            assertEquals(28 + 35 + 28 + 28 + 26, date.getLong(DAY_OF_YEAR));
            assertEquals(4, date.getLong(ALIGNED_WEEK_OF_MONTH));
            assertEquals(5, date.getLong(MONTH_OF_YEAR));
            assertEquals(2014, date.getLong(YEAR));
            assertEquals(1, date.getLong(ERA));
        }

        @Test
        @DisplayName("getLong() for an unsupported field should throw UnsupportedTemporalTypeException")
        void getLong_forUnsupportedField_shouldThrowException() {
            Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.getLong(MINUTE_OF_DAY));
        }

        @Test
        @DisplayName("range() for date fields should return correct dynamic ranges")
        void range_forDateFields_shouldReturnCorrectRanges() {
            assertEquals(ValueRange.of(1, 28), Symmetry454Date.of(2012, 1, 23).range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 35), Symmetry454Date.of(2012, 2, 23).range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 364), Symmetry454Date.of(2012, 1, 23).range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 371), Symmetry454Date.of(2015, 1, 23).range(DAY_OF_YEAR));
        }

        @Test
        @DisplayName("range() for an unsupported field should throw UnsupportedTemporalTypeException")
        void range_forUnsupportedField_shouldThrowException() {
            Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.range(MINUTE_OF_DAY));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {
        @Test
        @DisplayName("with(field, value) should adjust the date correctly")
        void with_field_shouldAdjustToCorrectDate() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 22), base.with(DAY_OF_WEEK, 1));
            assertEquals(Symmetry454Date.of(2014, 5, 28), base.with(DAY_OF_MONTH, 28));
            assertEquals(Symmetry454Date.of(2014, 4, 26), base.with(MONTH_OF_YEAR, 4));
            assertEquals(Symmetry454Date.of(2012, 5, 26), base.with(YEAR, 2012));
        }

        @Test
        @DisplayName("with(field, value) with an invalid value should throw DateTimeException")
        void with_field_withInvalidValue_shouldThrowException() {
            Symmetry454Date base = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> base.with(DAY_OF_MONTH, 29));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should adjust correctly")
        void with_temporalAdjuster_shouldAdjustCorrectly() {
            Symmetry454Date base = Symmetry454Date.of(2012, 2, 23);
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) should adjust to the correct Symmetry454Date")
        void with_localDate_shouldAdjustToCorrectSymmetry454Date() {
            Symmetry454Date base = Symmetry454Date.of(2000, 1, 4);
            Symmetry454Date adjusted = base.with(LocalDate.of(2012, 7, 6));
            assertEquals(Symmetry454Date.of(2012, 7, 5), adjusted);
        }

        @Test
        @DisplayName("with(Month) should throw DateTimeException")
        void with_isoMonth_shouldThrowException() {
            Symmetry454Date base = Symmetry454Date.of(2000, 1, 4);
            assertThrows(DateTimeException.class, () -> base.with(Month.APRIL));
        }

        @Test
        @DisplayName("LocalDate.with(Symmetry454Date) should adjust to the correct LocalDate")
        void LocalDate_with_symmetry454Date_shouldAdjustToCorrectLocalDate() {
            Symmetry454Date symmDate = Symmetry454Date.of(2012, 7, 19);
            LocalDate adjusted = LocalDate.MIN.with(symmDate);
            assertEquals(LocalDate.of(2012, 7, 20), adjusted);
        }

        @Test
        @DisplayName("plus(amount, unit) should add the amount correctly")
        void plus_amount_shouldCalculateCorrectly() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 34), base.plus(8, DAYS));
            assertEquals(Symmetry454Date.of(2014, 6, 12), base.plus(3, WEEKS));
            assertEquals(Symmetry454Date.of(2014, 8, 26), base.plus(3, MONTHS));
            assertEquals(Symmetry454Date.of(2017, 5, 26), base.plus(3, YEARS));
        }

        @Test
        @DisplayName("minus(amount, unit) should subtract the amount correctly")
        void minus_amount_shouldCalculateCorrectly() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 23), base.minus(3, DAYS));
            assertEquals(Symmetry454Date.of(2014, 4, 19), base.minus(5, WEEKS));
            assertEquals(Symmetry454Date.of(2013, 12, 26), base.minus(5, MONTHS));
            assertEquals(Symmetry454Date.of(2009, 5, 26), base.minus(5, YEARS));
        }

        @Test
        @DisplayName("plus() with an unsupported unit should throw UnsupportedTemporalTypeException")
        void plus_unsupportedUnit_shouldThrowException() {
            Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.plus(1, MINUTES));
        }

        @Test
        @DisplayName("until(endDate, unit) should calculate the difference correctly")
        void until_unit_shouldCalculateCorrectly() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 27);
            assertEquals(396, start.until(end, DAYS));
            assertEquals(56, start.until(end, WEEKS));
            assertEquals(13, start.until(end, MONTHS));
            assertEquals(1, start.until(end, YEARS));
        }

        @Test
        @DisplayName("until(endDate) should return the correct ChronoPeriod")
        void until_period_shouldCalculateCorrectly() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 7, 28);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(1, 2, 2);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Equals and ToString")
    class EqualsAndToStringTests {
        @Test
        @DisplayName("equals() and hashCode() should follow contract")
        void equalsAndHashCode_shouldFollowContract() {
            new EqualsTester()
                .addEqualityGroup(Symmetry454Date.of(2000, 1, 3), Symmetry454Date.of(2000, 1, 3))
                .addEqualityGroup(Symmetry454Date.of(2000, 1, 4))
                .addEqualityGroup(Symmetry454Date.of(2000, 2, 3))
                .addEqualityGroup(Symmetry454Date.of(2001, 1, 3))
                .testEquals();
        }

        @Test
        @DisplayName("toString() should return the correct representation")
        void toString_shouldReturnCorrectRepresentation() {
            assertEquals("Sym454 CE 1/01/01", Symmetry454Date.of(1, 1, 1).toString());
            assertEquals("Sym454 CE 1970/02/35", Symmetry454Date.of(1970, 2, 35).toString());
            assertEquals("Sym454 CE 2000/08/35", Symmetry454Date.of(2000, 8, 35).toString());
        }
    }
}