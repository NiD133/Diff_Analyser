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

@DisplayName("JulianChronology and JulianDate")
class JulianChronologyTest {

    // -----------------------------------------------------------------------
    // Data Providers
    // -----------------------------------------------------------------------

    /**
     * Provides pairs of equivalent Julian and ISO dates.
     *
     * @return a stream of arguments: { JulianDate, LocalDate }.
     */
    static Stream<Arguments> provideEquivalentJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    /**
     * Provides invalid date components.
     *
     * @return a stream of arguments: { year, month, dayOfMonth }.
     */
    static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(1900, 0, 1),
            Arguments.of(1900, 13, 1),
            Arguments.of(1900, 1, 0),
            Arguments.of(1900, 1, 32),
            Arguments.of(1900, 2, 30), // Leap in Julian, not in Gregorian
            Arguments.of(1899, 2, 29)  // Not a leap year
        );
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversions and Factory Methods")
    class ConversionAndFactoryTests {

        @ParameterizedTest(name = "Julian: {0} <=> ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("should convert between JulianDate and LocalDate")
        void conversionBetweenJulianAndIsoDates(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian), "LocalDate.from(julianDate)");
            assertEquals(julian, JulianDate.from(iso), "JulianDate.from(localDate)");
            assertEquals(julian, JulianChronology.INSTANCE.date(iso), "JulianChronology.INSTANCE.date(localDate)");
        }

        @ParameterizedTest(name = "Julian: {0} <=> ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("should convert to and from epoch day")
        void conversionToAndFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay(), "julian.toEpochDay()");
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "chronology.dateEpochDay()");
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideInvalidDateParts")
        @DisplayName("of() should throw exception for invalid date parts")
        void ofWithInvalidDatePartsShouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Properties and Fields")
    class DatePropertyTests {

        private static Stream<Arguments> provideMonthLengths() {
            return Stream.of(
                Arguments.of(1900, 1, 31), Arguments.of(1900, 2, 29), Arguments.of(1900, 3, 31),
                Arguments.of(1900, 4, 30), Arguments.of(1900, 5, 31), Arguments.of(1900, 6, 30),
                Arguments.of(1900, 7, 31), Arguments.of(1900, 8, 31), Arguments.of(1900, 9, 30),
                Arguments.of(1900, 10, 31), Arguments.of(1900, 11, 30), Arguments.of(1900, 12, 31),
                Arguments.of(1901, 2, 28), Arguments.of(1903, 2, 28), Arguments.of(1904, 2, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1} has {2} days")
        @MethodSource("provideMonthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonthShouldBeCorrect(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        private static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("provideFieldRanges")
        @DisplayName("range() should return correct value range for a field")
        void rangeForFieldShouldBeCorrect(int year, int month, int day, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), JulianDate.of(year, month, day).range(field));
        }

        private static Stream<Arguments> provideFieldGetValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("provideFieldGetValues")
        @DisplayName("getLong() should return correct value for a field")
        void getLongForFieldShouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }

        @Test
        @DisplayName("isLeapYear() should be true every 4 years")
        void isLeapYearFollowsJulianRule() {
            for (int year = -200; year <= 200; year++) {
                boolean expected = (year % 4 == 0);
                assertEquals(expected, JulianChronology.INSTANCE.isLeapYear(year), "Year: " + year);
            }
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        private static Stream<Arguments> provideWithAdjustmentCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // Adjust to shorter month
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)      // Adjust leap to non-leap
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with {3}={4} -> {5}-{6}-{7}")
        @MethodSource("provideWithAdjustmentCases")
        @DisplayName("with() should return correctly adjusted date")
        void withFieldValueShouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate initial = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, initial.with(field, value));
        }

        private static Stream<Arguments> providePlusCases() {
            // startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("providePlusCases")
        @DisplayName("plus() should return correctly advanced date")
        void plusAmountOfUnitShouldReturnCorrectDate(int startY, int startM, int startD, long amount, TemporalUnit unit, int endY, int endM, int endD) {
            JulianDate initial = JulianDate.of(startY, startM, startD);
            JulianDate expected = JulianDate.of(endY, endM, endD);
            assertEquals(expected, initial.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("providePlusCases")
        @DisplayName("minus() should return correctly retarded date")
        void minusAmountOfUnitShouldReturnCorrectDate(int startY, int startM, int startD, long amount, TemporalUnit unit, int endY, int endM, int endD) {
            JulianDate initial = JulianDate.of(endY, endM, endD);
            JulianDate expected = JulianDate.of(startY, startM, startD);
            assertEquals(expected, initial.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Until Calculations")
    class PeriodAndUntilTests {

        @Test
        @DisplayName("until() a date from itself should return a zero period")
        void untilSameDateShouldReturnZeroPeriod() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest(name = "Julian: {0} <=> ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("until() an equivalent ISO date should return a zero period")
        void untilEquivalentIsoDateShouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }

        private static Stream<Arguments> provideUntilCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
            );
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}) in {6} is {7}")
        @MethodSource("provideUntilCases")
        @DisplayName("until() should calculate amount between dates correctly")
        void untilDateWithUnitShouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        private static Stream<Arguments> provideToStringCases() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"),
                Arguments.of(JulianDate.of(0, 12, 30), "Julian BC 1-12-30")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("provideToStringCases")
        @DisplayName("toString() should return correct representation")
        void toStringShouldReturnCorrectRepresentation(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }
}