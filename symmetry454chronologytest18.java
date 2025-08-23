package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Symmetry454Chronology and Symmetry454Date")
class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
                Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
                Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
                Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
                Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1))
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void dateConversion_and_epochDay_shouldBeConsistent(Symmetry454Date sym454Date, LocalDate isoDate) {
            // Test conversions between Symmetry454Date and LocalDate
            assertEquals(isoDate, LocalDate.from(sym454Date));
            assertEquals(sym454Date, Symmetry454Date.from(isoDate));

            // Test conversions via Chronology instance
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate));

            // Test epoch day equivalence
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));

            // Test until() with zero period
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(isoDate));
        }

        @Test
        void adjust_fromLocalDate_shouldReturnCorrectlyAdjustedDate() {
            Symmetry454Date sym454 = Symmetry454Date.of(2012, 7, 19);
            LocalDate test = LocalDate.MIN.with(sym454);
            assertEquals(LocalDate.of(2012, 7, 20), test);
        }

        @ParameterizedTest(name = "of({0}, {1}, {2}) should fail")
        @MethodSource
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        static Stream<Arguments> of_withInvalidDateParts_shouldThrowException() {
            return Stream.of(
                    Arguments.of(2000, 13, 1),
                    Arguments.of(2000, 1, 0),
                    Arguments.of(2000, 1, 29), // Jan has 28 days
                    Arguments.of(2000, 2, 36), // Feb has 35 days
                    Arguments.of(2004, 12, 36) // Dec has 35 days only in leap years
            );
        }

        @ParameterizedTest(name = "of({0}, 12, 29) should fail as it's not a leap year")
        @MethodSource
        void of_forNonLeapYear_shouldThrowException_forLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }

        static Stream<Arguments> of_forNonLeapYear_shouldThrowException_forLeapDay() {
            return Stream.of(
                    Arguments.of(1), Arguments.of(100), Arguments.of(2000)
            );
        }
    }

    @Nested
    @DisplayName("Field Access Tests")
    class FieldAccessTests {

        @ParameterizedTest(name = "{0}-{1} should have {3} days")
        @MethodSource
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfMonth_shouldReturnCorrectValue() {
            return Stream.of(
                    Arguments.of(2000, 1, 28, 28),
                    Arguments.of(2000, 2, 28, 35),
                    Arguments.of(2000, 12, 28, 28),
                    Arguments.of(2004, 12, 20, 35) // Leap year
            );
        }

        @ParameterizedTest(name = "{0} range for {1}-{2}-{3} should be {4}")
        @MethodSource
        void range_shouldReturnCorrectValue(TemporalField field, int y, int m, int d, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(y, m, d).range(field));
        }

        static Stream<Arguments> range_shouldReturnCorrectValue() {
            return Stream.of(
                    Arguments.of(DAY_OF_MONTH, 2012, 1, 23, ValueRange.of(1, 28)),
                    Arguments.of(DAY_OF_MONTH, 2012, 2, 23, ValueRange.of(1, 35)),
                    Arguments.of(DAY_OF_YEAR, 2012, 1, 23, ValueRange.of(1, 364)),
                    Arguments.of(DAY_OF_YEAR, 2015, 1, 23, ValueRange.of(1, 371)), // Leap year
                    Arguments.of(ALIGNED_WEEK_OF_MONTH, 2012, 1, 23, ValueRange.of(1, 4)),
                    Arguments.of(ALIGNED_WEEK_OF_MONTH, 2012, 2, 23, ValueRange.of(1, 5)),
                    Arguments.of(ALIGNED_WEEK_OF_YEAR, 2012, 1, 23, ValueRange.of(1, 52)),
                    Arguments.of(ALIGNED_WEEK_OF_YEAR, 2015, 12, 30, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest(name = "{0} of {1}-{2}-{3} should be {4}")
        @MethodSource
        void getLong_shouldReturnCorrectFieldValue(TemporalField field, int y, int m, int d, long expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).getLong(field));
        }

        static Stream<Arguments> getLong_shouldReturnCorrectFieldValue() {
            return Stream.of(
                    Arguments.of(DAY_OF_WEEK, 2014, 5, 26, 5),
                    Arguments.of(DAY_OF_YEAR, 2014, 5, 26, 28 + 35 + 28 + 28 + 26),
                    Arguments.of(ALIGNED_WEEK_OF_YEAR, 2014, 5, 26, 4 + 5 + 4 + 4 + 4),
                    Arguments.of(PROLEPTIC_MONTH, 2014, 5, 26, 2014 * 12L + 5 - 1),
                    Arguments.of(YEAR, 2014, 5, 26, 2014),
                    Arguments.of(ERA, 2014, 5, 26, 1),
                    Arguments.of(DAY_OF_YEAR, 2015, 12, 35, 371), // Leap year
                    Arguments.of(ALIGNED_WEEK_OF_YEAR, 2015, 12, 35, 53) // Leap year
            );
        }
    }

    @Nested
    @DisplayName("Date Modification Tests")
    class ModificationTests {

        @ParameterizedTest(name = "{0}-{1}-{2} with {3}={4} should be {5}-{6}-{7}")
        @MethodSource
        void with_shouldReturnModifiedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> with_shouldReturnModifiedDate() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29) // Day of month adjusted
            );
        }

        @ParameterizedTest(name = "with {1}={2} should fail")
        @MethodSource
        void with_invalidValue_shouldThrowException(Symmetry454Date date, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        static Stream<Arguments> with_invalidValue_shouldThrowException() {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1);
            return Stream.of(
                    Arguments.of(date, DAY_OF_MONTH, 29), // Jan has 28 days
                    Arguments.of(date, DAY_OF_YEAR, 365), // Non-leap year has 364 days
                    Arguments.of(date, MONTH_OF_YEAR, 14),
                    Arguments.of(date, YEAR, 1_000_001)
            );
        }

        @Test
        void with_lastDayOfMonth_shouldReturnCorrectDate() {
            Symmetry454Date date = Symmetry454Date.of(2012, 2, 23); // Feb has 35 days
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic Tests")
    class ArithmeticTests {

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} should be {5}-{6}-{7}")
        @MethodSource
        void plus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> plus_shouldReturnCorrectDate() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1) // Across leap week
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} minus {3} {4} should be {5}-{6}-{7}")
        @MethodSource
        void minus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> minus_shouldReturnCorrectDate() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 3, DAYS, 2014, 5, 23),
                    Arguments.of(2014, 5, 26, 5, WEEKS, 2014, 4, 19),
                    Arguments.of(2014, 5, 26, 5, MONTHS, 2013, 12, 26),
                    Arguments.of(2014, 5, 26, 5, YEARS, 2009, 5, 26),
                    Arguments.of(2016, 1, 1, 8, DAYS, 2015, 12, 28) // Across leap week
            );
        }

        @ParameterizedTest(name = "until({0}, {1}) in {2} should be {3}")
        @MethodSource
        void until_withTemporalUnit_shouldReturnCorrectAmount(Symmetry454Date start, Symmetry454Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> until_withTemporalUnit_shouldReturnCorrectAmount() {
            Symmetry454Date d1 = Symmetry454Date.of(2014, 5, 26);
            return Stream.of(
                    Arguments.of(d1, Symmetry454Date.of(2014, 6, 4), DAYS, 13),
                    Arguments.of(d1, Symmetry454Date.of(2014, 6, 5), WEEKS, 1),
                    Arguments.of(d1, Symmetry454Date.of(2014, 6, 26), MONTHS, 1),
                    Arguments.of(d1, Symmetry454Date.of(2015, 5, 26), YEARS, 1)
            );
        }

        @Test
        void until_withChronoLocalDate_shouldReturnCorrectPeriod() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 28);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(1, 1, 2);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @ParameterizedTest(name = "prolepticYear for {0} should fail")
        @MethodSource
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        static Stream<Arguments> prolepticYear_withUnsupportedEra_shouldThrowException() {
            return Stream.of(
                    Arguments.of(HijrahEra.AH),
                    Arguments.of(JapaneseEra.HEISEI),
                    Arguments.of(MinguoEra.ROC),
                    Arguments.of(ThaiBuddhistEra.BE)
            );
        }
    }

    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {

        @Test
        void testEqualsAndHashCode() {
            Symmetry454Date dateA = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date dateACopy = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date dateB = Symmetry454Date.of(2014, 5, 27);
            Symmetry454Date dateC = Symmetry454Date.of(2015, 5, 26);

            new EqualsTester()
                    .addEqualityGroup(dateA, dateACopy)
                    .addEqualityGroup(dateB)
                    .addEqualityGroup(dateC)
                    .testEquals();
        }

        @Test
        void testToString() {
            Symmetry454Date date = Symmetry454Date.of(1970, 2, 35);
            assertEquals("Sym454 CE 1970/02/35", date.toString());
        }
    }
}