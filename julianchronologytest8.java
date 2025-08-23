package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate Functionality Tests")
public class JulianChronologyTestTest8 {

    static Stream<Arguments> julianAndIsoDatePairs() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, not ISO
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("from(JulianDate) should return the correct LocalDate")
        void fromJulianDate_shouldReturnCorrectLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("from(LocalDate) should return the correct JulianDate")
        void fromLocalDate_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("chronology.date(epochDay) should return the correct JulianDate")
        void chronologyDateFromEpochDay_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("toEpochDay() should match the equivalent ISO date")
        void toEpochDay_shouldMatchIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("chronology.date(TemporalAccessor) should return the correct JulianDate")
        void chronologyDateFromTemporal_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("until() with an equivalent date should return a zero period")
        void until_withEquivalentDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            // until() on the same object or an equal object of the same chronology returns a ChronoPeriod
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian));

            // until() on a Temporal of a different chronology returns a standard Period
            assertEquals(Period.ZERO, julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 0), { 1900, -1, 1 }, { 1900, 0, 1 },
                Arguments.of(1900, 13, 1), { 1900, 14, 1 }, { 1900, 1, -1 },
                Arguments.of(1900, 1, 0), { 1900, 1, 32 }, { 1900, 2, 29 + 1 }, // 1900 is a leap year in Julian
                Arguments.of(1899, 2, 28 + 1) // 1899 is not a leap year
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2}) should throw DateTimeException")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        static Stream<Arguments> monthLengthProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1900, 1, 1), 31),
                Arguments.of(JulianDate.of(1900, 2, 1), 29), // Julian leap year
                Arguments.of(JulianDate.of(1901, 2, 1), 28),
                Arguments.of(1904, 2, 1), 29),
                Arguments.of(2000, 2, 1), 29)
            );
        }

        @ParameterizedTest(name = "{0} should have month length {1}")
        @MethodSource("monthLengthProvider")
        void lengthOfMonth_shouldReturnCorrectValue(JulianDate date, int length) {
            assertEquals(length, date.lengthOfMonth());
        }

        static Stream<Arguments> fieldRangeProvider() {
            return Stream.of(
                // year, month, day, field, min, max
                Arguments.of(JulianDate.of(2012, 1, 23), DAY_OF_MONTH, 1, 31),
                Arguments.of(JulianDate.of(2012, 2, 23), DAY_OF_MONTH, 1, 29), // leap
                Arguments.of(JulianDate.of(2011, 2, 23), DAY_OF_MONTH, 1, 28), // non-leap
                Arguments.of(JulianDate.of(2012, 4, 23), DAY_OF_MONTH, 1, 30),
                Arguments.of(JulianDate.of(2012, 1, 23), DAY_OF_YEAR, 1, 366), // leap
                Arguments.of(JulianDate.of(2011, 1, 23), DAY_OF_YEAR, 1, 365), // non-leap
                Arguments.of(JulianDate.of(2012, 2, 23), ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(JulianDate.of(2011, 2, 23), ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest(name = "range of {1} for {0} should be {2}-{3}")
        @MethodSource("fieldRangeProvider")
        void range_forTemporalField_shouldReturnCorrectRange(JulianDate date, TemporalField field, int min, int max) {
            assertEquals(ValueRange.of(min, max), date.range(field));
        }

        static Stream<Arguments> temporalFieldGetProvider() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            return Stream.of(
                // date, field, expected value
                Arguments.of(date, DAY_OF_WEEK, 7), // A Sunday
                Arguments.of(date, DAY_OF_MONTH, 26),
                // Jan(31) + Feb(28) + Mar(31) + Apr(30) + May(26) = 146
                Arguments.of(date, DAY_OF_YEAR, 146),
                Arguments.of(date, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(date, MONTH_OF_YEAR, 5),
                Arguments.of(date, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(date, YEAR, 2014),
                Arguments.of(date, ERA, 1),
                Arguments.of(JulianDate.of(0, 6, 8), ERA, 0),
                Arguments.of(date, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest(name = "{0} getLong({1}) should be {2}")
        @MethodSource("temporalFieldGetProvider")
        void getLong_forTemporalField_shouldReturnCorrectValue(JulianDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("plus(days) should behave like LocalDate.plusDays()")
        void plusDays_shouldBehaveLikeIsoDate(JulianDate julian, LocalDate iso) {
            long[] daysToAdd = {0, 1, 35, -1, -60};
            for (long days : daysToAdd) {
                LocalDate expected = iso.plusDays(days);
                LocalDate actual = LocalDate.from(julian.plus(days, DAYS));
                assertEquals(expected, actual,
                    () -> String.format("%s plus %d days should match %s plus %d days", julian, days, iso, days));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("minus(days) should behave like LocalDate.minusDays()")
        void minusDays_shouldBehaveLikeIsoDate(JulianDate julian, LocalDate iso) {
            long[] daysToSubtract = {0, 1, 35, -1, -60};
            for (long days : daysToSubtract) {
                LocalDate expected = iso.minusDays(days);
                LocalDate actual = LocalDate.from(julian.minus(days, DAYS));
                assertEquals(expected, actual,
                    () -> String.format("%s minus %d days should match %s minus %d days", julian, days, iso, days));
            }
        }

        static Stream<Arguments> dateWithFieldProvider() {
            // start date, field, new value, expected date
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_WEEK, 3, JulianDate.of(2014, 5, 22)),
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_MONTH, 31, JulianDate.of(2014, 5, 31)),
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_YEAR, 365, JulianDate.of(2014, 12, 31)),
                Arguments.of(JulianDate.of(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1, JulianDate.of(2014, 5, 5)),
                Arguments.of(JulianDate.of(2014, 5, 26), MONTH_OF_YEAR, 7, JulianDate.of(2014, 7, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, JulianDate.of(2013, 3, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), YEAR, 2012, JulianDate.of(2012, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), ERA, 0, JulianDate.of(-2013, 5, 26)),
                Arguments.of(JulianDate.of(2012, 3, 31), MONTH_OF_YEAR, 2, JulianDate.of(2012, 2, 29)), // leap
                Arguments.of(JulianDate.of(2011, 3, 31), MONTH_OF_YEAR, 2, JulianDate.of(2011, 2, 28))  // non-leap
            );
        }

        @ParameterizedTest(name = "{0} with({1}, {2}) should be {3}")
        @MethodSource("dateWithFieldProvider")
        void with_temporalField_shouldReturnAdjustedDate(JulianDate start, TemporalField field, long value, JulianDate expected) {
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> datePlusAmountProvider() {
            // start date, amount, unit, expected end date
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), 8L, DAYS, JulianDate.of(2014, 6, 3)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, WEEKS, JulianDate.of(2014, 6, 16)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, MONTHS, JulianDate.of(2014, 8, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, YEARS, JulianDate.of(2017, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, DECADES, JulianDate.of(2044, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, CENTURIES, JulianDate.of(2314, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, MILLENNIA, JulianDate.of(5014, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), -1L, ERAS, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0} plus {1} {2} should be {3}")
        @MethodSource("datePlusAmountProvider")
        void plusAndMinus_temporalUnit_shouldBeSymmetrical(JulianDate start, long amount, TemporalUnit unit, JulianDate end) {
            assertEquals(end, start.plus(amount, unit), "plus operation");
            assertEquals(start, end.minus(amount, unit), "minus operation (symmetry)");
        }
    }

    @Nested
    @DisplayName("Period and Duration Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest8#julianAndIsoDatePairs")
        @DisplayName("until(DAYS) should return the correct day difference")
        void until_daysUnit_shouldReturnCorrectDayDifference(JulianDate julian, LocalDate iso) {
            long[] dayOffsets = {0, 1, 35, -40};
            for (long offset : dayOffsets) {
                assertEquals(offset, julian.until(iso.plusDays(offset), DAYS),
                    () -> String.format("Days between %s and %s should be %d", julian, iso.plusDays(offset), offset));
            }
        }

        static Stream<Arguments> dateUntilProvider() {
            // start date, end date, unit, expected amount
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 1), DAYS, 6),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 2), WEEKS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 26), MONTHS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2015, 5, 26), YEARS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2024, 5, 26), DECADES, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2114, 5, 26), CENTURIES, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(3014, 5, 26), MILLENNIA, 1),
                Arguments.of(JulianDate.of(-2013, 5, 26), JulianDate.of(2014, 5, 26), ERAS, 1)
            );
        }

        @ParameterizedTest(name = "Amount of {2} from {0} until {1} should be {3}")
        @MethodSource("dateUntilProvider")
        void until_temporalUnit_shouldReturnCorrectAmount(JulianDate start, JulianDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest(name = "{0}.toString() should be \"{1}\"")
        @MethodSource("toStringProvider")
        void toString_shouldReturnCorrectFormatting(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }

    @Nested
    @DisplayName("JulianChronology Specific Tests")
    class ChronologyMethodTests {

        @Test
        @DisplayName("prolepticYear should be correct for AD and BC eras")
        void prolepticYear_shouldBeCorrectForAdAndBcEras() {
            assertEquals(4, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 4));
            assertEquals(1, JulianChronology.INSTANCE.prolepticYear(JulianEra.AD, 1));
            assertEquals(0, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 1));
            assertEquals(-1, JulianChronology.INSTANCE.prolepticYear(JulianEra.BC, 2));
        }
    }
}