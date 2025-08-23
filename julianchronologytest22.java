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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate")
public class JulianDateTest {

    /**
     * Provides equivalent Julian and ISO dates.
     * Includes edge cases like year boundaries, leap years (Julian vs. ISO),
     * and the Gregorian calendar reform cutover date.
     */
    public static Object[][] julianAndIsoDateProvider() {
        return new Object[][]{
                {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)}, // Julian leap year
                {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)}, // Julian leap, ISO non-leap
                {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)}, // Day before Gregorian cutover
                {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)}, // Day of Gregorian cutover
                {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)},
        };
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryAndValidationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("of(year, month, day) should create correct date")
        void factory_of_shouldCreateCorrectDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.of(julianDate.getYear(), julianDate.getMonthValue(), julianDate.getDayOfMonth()));
        }

        public static Object[][] invalidDateProvider() {
            return new Object[][]{
                    {1900, 0, 1},  // Invalid month
                    {1900, 13, 1}, // Invalid month
                    {1900, 1, 0},  // Invalid day
                    {1900, 1, 32}, // Invalid day
                    {1900, 2, 30}, // Invalid day for month (Julian leap year)
                    {1899, 2, 29}, // Invalid day for month (Julian non-leap year)
                    {1900, 4, 31}, // Invalid day for month
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        @DisplayName("of() should throw exception for invalid date components")
        void of_whenDateIsInvalid_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Conversion and Interoperability Tests")
    class ConversionAndInteroperabilityTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("should be equivalent to its corresponding ISO date")
        void julianAndIsoDatesShouldBeEquivalent(JulianDate julianDate, LocalDate isoDate) {
            assertAll(
                    "Equivalency checks between JulianDate " + julianDate + " and LocalDate " + isoDate,
                    () -> assertEquals(isoDate, LocalDate.from(julianDate), "LocalDate.from(julianDate)"),
                    () -> assertEquals(julianDate, JulianDate.from(isoDate), "JulianDate.from(isoDate)"),
                    () -> assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay(), "toEpochDay()"),
                    () -> assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()), "chronology.dateEpochDay()"),
                    () -> assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate), "chronology.date(TemporalAccessor)")
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("until() an equivalent date should result in a zero period")
        void until_withEquivalentDateInDifferentChronology_shouldReturnZero(JulianDate julianDate, LocalDate isoDate) {
            assertAll(
                    "until() should return zero for equivalent dates",
                    () -> assertEquals(Period.ZERO, isoDate.until(julianDate)),
                    () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate))
            );
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        public static Object[][] dateAndExpectedMonthLengthProvider() {
            return new Object[][]{
                    {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30},
                    {1901, 2, 28}, {1903, 2, 28}, {1904, 2, 29}, {2000, 2, 29},
            };
        }

        @ParameterizedTest
        @MethodSource("dateAndExpectedMonthLengthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        public static Object[][] rangeProvider() {
            return new Object[][]{
                    {2012, 1, 23, DAY_OF_MONTH, 1, 31},
                    {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // Leap year
                    {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // Non-leap year
                    {2012, 1, 23, DAY_OF_YEAR, 1, 366}, // Leap year
                    {2011, 1, 23, DAY_OF_YEAR, 1, 365}, // Non-leap year
                    {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
                    {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
            };
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        @DisplayName("range() should return correct value range for a given field")
        void range_shouldReturnCorrectRangeForField(int year, int month, int dayOfMonth, TemporalField field, long expectedMin, long expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, JulianDate.of(year, month, dayOfMonth).range(field));
        }
    }

    @Nested
    @DisplayName("Field Accessor Tests")
    class FieldAccessorTests {
        public static Object[][] getLongProvider() {
            return new Object[][]{
                    {2014, 5, 26, DAY_OF_WEEK, 7},
                    {2014, 5, 26, DAY_OF_MONTH, 26},
                    {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},
                    {2014, 5, 26, MONTH_OF_YEAR, 5},
                    {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1},
                    {2014, 5, 26, YEAR, 2014},
                    {2014, 5, 26, ERA, 1},
                    {0, 6, 8, ERA, 0},
                    {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},
            };
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        @DisplayName("getLong() should return correct value for a given field")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    @DisplayName("Manipulation Tests")
    class ManipulationTests {

        public static Object[][] withProvider() {
            return new Object[][]{
                    {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
                    {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
                    {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
                    {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
                    {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                    {2014, 5, 26, ERA, 0, -2013, 5, 26},
                    {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28}, // Adjust to shorter month
                    {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29}, // Adjust to shorter leap month
                    {2012, 2, 29, YEAR, 2011, 2011, 2, 28},      // Adjust leap day to non-leap year
            };
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        @DisplayName("with() should adjust date correctly for a given field")
        void with_shouldAdjustDateCorrectly(int year, int month, int dayOfMonth, TemporalField field, long value, int exYear, int exMonth, int exDay) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, start.with(field, value));
        }

        public static Object[][] plusProvider() {
            return new Object[][]{
                    {2014, 5, 26, 8, DAYS},
                    {2014, 5, 26, 3, WEEKS},
                    {2014, 5, 26, 3, MONTHS},
                    {2014, 5, 26, 3, YEARS},
                    {2014, 5, 26, 3, DECADES},
                    {2014, 5, 26, 3, CENTURIES},
                    {2014, 5, 26, 3, MILLENNIA},
                    {2014, 5, 26, -1, ERAS},
            };
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("plus() and minus() should be inverse operations")
        void plusAndMinus_shouldBeInverse(int year, int month, int dayOfMonth, long amount, TemporalUnit unit) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate afterPlus = start.plus(amount, unit);
            JulianDate afterMinus = start.minus(amount, unit);

            assertAll(
                    () -> assertEquals(start, afterPlus.minus(amount, unit), "plus(N).minus(N) should return to start"),
                    () -> assertEquals(start, afterMinus.plus(amount, unit), "minus(N).plus(N) should return to start")
            );
        }

        public static Object[][] untilProvider() {
            return new Object[][]{
                    {2014, 5, 26, 2014, 6, 1, DAYS, 6},
                    {2014, 5, 26, 2014, 5, 20, DAYS, -6},
                    {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
                    {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                    {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                    {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                    {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                    {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                    {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
            };
        }

        @ParameterizedTest
        @MethodSource("untilProvider")
        @DisplayName("until() should calculate correct amount of time between dates")
        void until_shouldCalculateCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Formatting and Adjusting Tests")
    class FormattingAndAdjustingTests {

        public static Object[][] toStringProvider() {
            return new Object[][]{
                    {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
                    {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
                    {JulianDate.of(0, 12, 31), "Julian BC 1-12-31"},
            };
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return correct string representation")
        void toString_shouldReturnCorrectRepresentation(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }

        @Test
        @DisplayName("adjustInto() a LocalDateTime should work correctly")
        void adjustIntoLocalDateTime_shouldReturnCorrectlyAdjustedDateTime() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23); // Equivalent to ISO 2012-07-06
            LocalDateTime baseDateTime = LocalDateTime.of(2000, 1, 1, 10, 20);
            LocalDateTime adjustedDateTime = baseDateTime.with(julianDate);

            assertEquals(LocalDateTime.of(2012, 7, 6, 10, 20), adjustedDateTime);
        }
    }
}