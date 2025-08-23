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
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link JulianDate} and its integration with {@link JulianChronology}.
 * This class covers conversions, factory methods, field access, arithmetic, and other properties.
 */
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     *
     * @return a stream of arguments: { julianDate, isoDate }
     */
    private static Stream<Arguments> sampleJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    /**
     * Provides arguments for testing date creation with invalid values.
     *
     * @return a stream of arguments: { year, month, dayOfMonth }
     */
    private static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month
            Arguments.of(1900, 13, 1),  // Invalid month
            Arguments.of(1900, 1, 0),   // Invalid day
            Arguments.of(1900, 1, 32),  // Invalid day
            Arguments.of(1900, 2, 30),  // Invalid day for month (leap)
            Arguments.of(1899, 2, 29)   // Invalid day for month (common)
        );
    }

    /**
     * Provides arguments for testing month lengths.
     *
     * @return a stream of arguments: { year, month, expectedLength }
     */
    private static Stream<Arguments> monthLengthProvider() {
        return Stream.of(
            Arguments.of(1900, 1, 31),
            Arguments.of(1900, 2, 29), // Julian leap year
            Arguments.of(1901, 2, 28), // Julian common year
            Arguments.of(1904, 2, 29),
            Arguments.of(2000, 2, 29),
            Arguments.of(2100, 2, 29)  // Julian leap year
        );
    }

    /**
     * Provides arguments for testing field value ranges.
     *
     * @return a stream of arguments: { year, month, dayOfMonth, field, expectedMin, expectedMax }
     */
    private static Stream<Arguments> fieldRangeProvider() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
            Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Common year
            Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
            Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), // Common year
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
        );
    }

    /**
     * Provides arguments for testing `getLong(TemporalField)`.
     *
     * @return a stream of arguments: { year, month, dayOfMonth, field, expectedValue }
     */
    private static Stream<Arguments> fieldValueProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31L + 28 + 31 + 30 + 26),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L),
            Arguments.of(0, 6, 8, ERA, 0L),
            Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
        );
    }

    /**
     * Provides arguments for testing `with(TemporalField, long)`.
     *
     * @return a stream of arguments: { year, month, dom, field, value, expectedYear, expectedMonth, expectedDom }
     */
    private static Stream<Arguments> fieldModificationProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
            Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // Adjusts day for shorter month
            Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjusts day for shorter leap month
            Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)        // Adjusts day for non-leap year
        );
    }

    /**
     * Provides arguments for testing `plus(long, TemporalUnit)`.
     *
     * @return a stream of arguments: { year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom }
     */
    private static Stream<Arguments> plusProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
        );
    }

    /**
     * Provides arguments for testing `until(Temporal, TemporalUnit)`.
     *
     * @return a stream of arguments: { startY, M, D, endY, M, D, unit, expectedAmount }
     */
    private static Stream<Arguments> untilProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
            Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
            Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
            Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
        );
    }

    /**
     * Provides arguments for testing `toString()`.
     *
     * @return a stream of arguments: { julianDate, expectedString }
     */
    private static Stream<Arguments> toStringProvider() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
            Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    class ConversionAndFactoryTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void fromLocalDate_convertsCorrectly(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void toLocalDate_convertsCorrectly(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void toEpochDay_isConsistentWithIso(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void chronologyCreatesCorrectDateFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void chronologyCreatesCorrectDateFromTemporal(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#invalidDateProvider")
        void of_throwsExceptionForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }
    }

    @Nested
    class PropertyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#monthLengthProvider")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#toStringProvider")
        void toString_returnsCorrectFormat(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }

    @Nested
    class FieldAccessTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldRangeProvider")
        void range_returnsCorrectRangeForField(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dom);
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, date.range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldValueProvider")
        void getLong_returnsCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(expected, date.getLong(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldModificationProvider")
        void with_returnsModifiedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate initialDate = JulianDate.of(year, month, dom);
            JulianDate expectedDate = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, initialDate.with(field, value));
        }
    }

    @Nested
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void plusAndMinusDays_isConsistentWithIso(JulianDate julian, LocalDate iso) {
            long[] daysToTest = {0, 1, 35, -1, -60};
            for (long days : daysToTest) {
                String messageSuffix = " for " + julian + " and " + days + " days";
                assertEquals(iso.plusDays(days), LocalDate.from(julian.plus(days, DAYS)), "plus(days)" + messageSuffix);
                assertEquals(iso.minusDays(days), LocalDate.from(julian.minus(days, DAYS)), "minus(days)" + messageSuffix);
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#plusProvider")
        void plusAndMinus_areInverseOperations(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate startDate = JulianDate.of(year, month, dom);
            JulianDate expectedDate = JulianDate.of(expectedYear, expectedMonth, expectedDom);

            assertEquals(expectedDate, startDate.plus(amount, unit), "plus() operation failed");
            assertEquals(startDate, expectedDate.minus(amount, unit), "minus() is not the inverse of plus()");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_days_isConsistentWithIso(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#untilProvider")
        void until_returnsCorrectAmount(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, dom1);
            JulianDate end = JulianDate.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_equivalentDate_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }
    }

    @Nested
    class ChronologyInteractionTests {
        @Test
        void prolepticYear_throwsExceptionForIncorrectEraType() {
            assertThrows(ClassCastException.class, () -> JulianChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        }
    }
}