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
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.ERAS;
import static java.time.temporal.ChronoUnit.MILLENNIA;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
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
import java.time.temporal.ValueRange;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry010Chronology and Symmetry010Date Tests")
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideSymmetryAndIsoDatePairs() {
        return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
                Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
                Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
                Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
                Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
                Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
                Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
                Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
                Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
                Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
                Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
                Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Date Creation and Validation")
    class DateCreationTests {

        @ParameterizedTest
        @CsvSource({
                "-1, 13, 28", "2000, 13, 1", "2000, 1, 0", "2000, 1, 31",
                "2000, 2, 32", "2000, 4, 31", "2004, 12, 38"
        })
        @DisplayName("of(y, m, d) with invalid components should throw exception")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        @ParameterizedTest
        @CsvSource({"1", "100", "200", "2000"})
        @DisplayName("of(y, 12, 37) for non-leap year should throw exception")
        void of_withInvalidLeapDay_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("ISO Conversion")
    class ConversionTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        @DisplayName("LocalDate.from(Symmetry010Date) should return correct ISO date")
        void fromSymmetryDate_toLocalDate_isCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        @DisplayName("Symmetry010Date.from(LocalDate) should return correct Symmetry010 date")
        void fromLocalDate_toSymmetryDate_isCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest(name = "{index}: {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        @DisplayName("Chronology.date(TemporalAccessor) should be equivalent to from()")
        void chronologyDate_fromTemporal_isCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Epoch Day Operations")
    class EpochDayTests {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        @DisplayName("toEpochDay() should be consistent with ISO")
        void toEpochDay_isConsistentWithIso(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        @DisplayName("Chronology.dateEpochDay() should be consistent with ISO")
        void chronologyDateEpochDay_isConsistentWithIso(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Field Access and Ranges")
    class FieldAccessTests {

        static Stream<Arguments> provideDateAndFieldWithExpectedValue() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L), // 30+31+30+30+26
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), // 4+5+4+4+4
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L), // 2014*12 + 5-1
                    Arguments.of(2014, 5, 26, YEAR, 2014L),
                    Arguments.of(2014, 5, 26, ERA, 1L),
                    Arguments.of(2012, 9, 26, DAY_OF_YEAR, 269L), // 3*(4+5+4)*7 - 4
                    Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L), // 3*(4+5+4)
                    Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L), // 364 + 7
                    Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L),
                    Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 24191L) // 2016*12 - 1
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, {3} -> {4}")
        @MethodSource("provideDateAndFieldWithExpectedValue")
        void getLong_forVariousFields_returnsCorrectValue(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(y, m, d).getLong(field));
        }

        static Stream<Arguments> provideDateAndFieldWithExpectedRange() {
            return Stream.of(
                    Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                    Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                    Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)),
                    Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                    Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                    Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
                    Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                    Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                    Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                    Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                    Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, {3} -> {4}")
        @MethodSource("provideDateAndFieldWithExpectedRange")
        void range_forVariousFields_returnsCorrectRange(int y, int m, int d, TemporalField field, ValueRange expected) {
            assertEquals(expected, Symmetry010Date.of(y, m, d).range(field));
        }

        @ParameterizedTest
        @CsvSource({
                "2000, 1, 30", "2000, 2, 31", "2000, 3, 30", "2000, 4, 30",
                "2000, 5, 31", "2000, 6, 30", "2000, 7, 30", "2000, 8, 31",
                "2000, 9, 30", "2000, 10, 30", "2000, 11, 31", "2000, 12, 30",
                "2004, 12, 37" // Leap year
        })
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            Symmetry010Date date = Symmetry010Date.of(year, month, 1);
            assertEquals(expectedLength, date.lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void plusDays_shouldBeConsistentWithIso(Symmetry010Date sym010, LocalDate iso) {
            LongStream.of(0, 1, 35, -1, -60).forEach(days -> {
                Symmetry010Date actual = sym010.plus(days, DAYS);
                LocalDate expected = iso.plusDays(days);
                assertEquals(expected, LocalDate.from(actual), () -> "Adding " + days + " days");
            });
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void minusDays_shouldBeConsistentWithIso(Symmetry010Date sym010, LocalDate iso) {
            LongStream.of(0, 1, 35, -1, -60).forEach(days -> {
                Symmetry010Date actual = sym010.minus(days, DAYS);
                LocalDate expected = iso.minusDays(days);
                assertEquals(expected, LocalDate.from(actual), () -> "Subtracting " + days + " days");
            });
        }

        static Stream<Arguments> provideDateAndAmountToAdd() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                    Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                    Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36), // Leap week
                    Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12) // Leap week
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4}")
        @MethodSource("provideDateAndAmountToAdd")
        void plus_withVariousUnits_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4}")
        @MethodSource("provideDateAndAmountToAdd")
        void minus_withVariousUnits_returnsCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideDateAndFieldToModify() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4})")
        @MethodSource("provideDateAndFieldToModify")
        void with_forVariousFields_returnsCorrectDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @CsvSource({
                "2013, 1, 1, DAY_OF_WEEK, 8", "2013, 1, 1, DAY_OF_MONTH, 31",
                "2013, 1, 1, DAY_OF_YEAR, 365", "2015, 1, 1, DAY_OF_YEAR, 372",
                "2013, 1, 1, MONTH_OF_YEAR, 14", "2013, 1, 1, YEAR, 1000001"
        })
        void with_withInvalidValue_throwsException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(field, value));
        }

        @ParameterizedTest
        @CsvSource({
                "2012, 1, 23, 2012, 1, 30", "2012, 2, 23, 2012, 2, 31",
                "2009, 12, 23, 2009, 12, 37"
        })
        void with_lastDayOfMonth_returnsCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_self_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010));
        }

        @ParameterizedTest(name = "{index}: {0} until {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_equivalentIsoDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
        }

        @ParameterizedTest(name = "{index}: {1} until {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void isoDate_until_equivalentSymmetryDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_withDaysUnit_isConsistentWithIso(Symmetry010Date sym010, LocalDate iso) {
            LongStream.of(0, 1, 35, -40).forEach(days -> {
                LocalDate endIso = iso.plusDays(days);
                long actual = sym010.until(endIso, DAYS);
                assertEquals(days, actual, () -> "Days between " + sym010 + " and " + endIso);
            });
        }

        static Stream<Arguments> provideDatePairsAndExpectedPeriod() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                    Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} until {3}-{4}-{5}")
        @MethodSource("provideDatePairsAndExpectedPeriod")
        void until_asChronoPeriod_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        static Stream<Arguments> provideUnsupportedErasForProlepticYear() {
            return Stream.of(
                    // Add other chronology eras here to test for incompatibility
                    Arguments.of(HijrahEra.AH),
                    Arguments.of(JapaneseEra.HEISEI),
                    Arguments.of(MinguoEra.ROC),
                    Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideUnsupportedErasForProlepticYear")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Object Methods")
    class ObjectMethodTests {

        @ParameterizedTest
        @CsvSource({
                "1, 1, 1, 'Sym010 CE 1/01/01'",
                "1970, 2, 31, 'Sym010 CE 1970/02/31'",
                "2000, 8, 31, 'Sym010 CE 2000/08/31'",
                "2009, 12, 37, 'Sym010 CE 2009/12/37'"
        })
        void toString_returnsCorrectFormat(int y, int m, int d, String expected) {
            assertEquals(expected, Symmetry010Date.of(y, m, d).toString());
        }

        @Test
        void equalsAndHashCode_shouldAdhereToContract() {
            new EqualsTester()
                    .addEqualityGroup(Symmetry010Date.of(2000, 1, 3), Symmetry010Date.of(2000, 1, 3))
                    .addEqualityGroup(Symmetry010Date.of(2000, 1, 4))
                    .addEqualityGroup(Symmetry010Date.of(2001, 1, 3))
                    .addEqualityGroup(Symmetry010Date.of(2000, 2, 3))
                    .testEquals();
        }
    }
}