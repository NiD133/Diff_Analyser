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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the interoperability and conversion between JulianDate and other temporal types.
 */
@DisplayName("JulianChronology Interoperability and Conversions")
public class JulianChronologyInteroperabilityTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> julianAndIsoDateProvider() {
        return Stream.of(
            // Near start of Julian calendar
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            // Julian leap year
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            // Julian leap year, but not a Gregorian leap year
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            // Dates around the Gregorian calendar cutover
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Modern dates
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("LocalDate.from(julianDate) should create the correct ISO date")
        void convertsJulianDateToLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("JulianDate.from(localDate) should create the correct Julian date")
        void convertsLocalDateToJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("chronology.dateEpochDay() should create the correct Julian date")
        void createsJulianDateFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("julianDate.toEpochDay() should match the ISO date's epoch day")
        void convertsJulianDateToEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("chronology.date(temporal) should create the correct Julian date from an ISO date")
        void createsJulianDateFromTemporalAccessor(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Arithmetic Operation Tests")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("julianDate.plus(days) should be equivalent to isoDate.plusDays()")
        void plusDays_isEquivalentToIsoPlusDays(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("julianDate.minus(days) should be equivalent to isoDate.minusDays()")
        void minusDays_isEquivalentToIsoMinusDays(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
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

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("plus() should add the correct amount for various units")
        void plus_withVariousUnits_addsCorrectAmount(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("minus() should subtract the correct amount for various units")
        void minus_withVariousUnits_subtractsCorrectAmount(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            // Reuses plusProvider data by swapping start and end dates
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Duration (until) Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("until() between different representations of the same day should be zero")
        void until_forSameDayInDifferentChronologies_returnsZero(JulianDate julianDate, LocalDate isoDate) {
            // The period between a date and itself should be zero.
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));

            // The period between a Julian date and an ISO date representing the same day
            // is zero because the calculation is based on the epoch day.
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteroperabilityTest#julianAndIsoDateProvider")
        @DisplayName("until() should calculate the correct number of days between dates")
        void until_withDaysUnit_calculatesCorrectDayDifference(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }

        static Stream<Arguments> untilProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
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

        @ParameterizedTest
        @MethodSource("untilProvider")
        @DisplayName("until() should calculate correct duration for various units")
        void until_withVariousUnits_calculatesCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field-based Operation Tests")
    class FieldBasedOperationTests {

        static Stream<Arguments> withProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                // Adjusting month to a shorter month
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28),
                // Adjusting year on a leap day
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        @DisplayName("with() should modify a temporal field correctly")
        void with_modifiesFieldCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        @DisplayName("with() should throw exception for unsupported fields")
        void with_unsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0));
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for May 26th in a non-leap year: 31+28+31+30+26 = 146
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(0, 6, 8, ERA, 0L),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        @DisplayName("getLong() should return the correct field value")
        void getLong_returnsCorrectFieldValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Properties and Range Tests")
    class PropertyAndRangeTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Leap year in Julian
                Arguments.of(1901, 2, 28),
                Arguments.of(1904, 2, 29), // Leap year in Julian
                Arguments.of(2000, 2, 29), // Leap year in Julian
                Arguments.of(2100, 2, 29)  // Leap year in Julian (not in Gregorian)
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth_returnsCorrectValueForMonth(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        @DisplayName("range() should return the correct value range for a given field")
        void range_returnsCorrectFieldRange(int year, int month, int dom, TemporalField field, int min, int max) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(ValueRange.of(min, max), date.range(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Invalid Date Creation")
    class InvalidDateCreationTests {

        static Stream<Arguments> badDatesProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day for Jan
                Arguments.of(1899, 2, 29),  // Invalid day for Feb in a non-leap year
                Arguments.of(1900, 2, 30),  // Invalid day for Feb in a leap year
                Arguments.of(1900, 4, 31)   // Invalid day for Apr
            );
        }

        @ParameterizedTest
        @MethodSource("badDatesProvider")
        @DisplayName("of() should throw DateTimeException for invalid date parts")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @Test
        @DisplayName("toString() should return a correctly formatted string")
        void toString_returnsCorrectlyFormattedString() {
            assertEquals("Julian AD 1-01-01", JulianDate.of(1, 1, 1).toString());
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
        }
    }
}