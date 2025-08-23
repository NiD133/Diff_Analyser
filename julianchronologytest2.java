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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianChronology and JulianDate")
public class JulianChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     *
     * @return a stream of arguments: {JulianDate, corresponding LocalDate}
     */
    public static Object[][] julianAndIsoDateProvider() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)},
        };
    }

    /**
     * Provides invalid date components.
     *
     * @return a stream of arguments: {year, month, dayOfMonth}
     */
    public static Object[][] invalidDateProvider() {
        return new Object[][] {
            {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, // 1900 is a leap year in Julian
            {1899, 2, 29}, // 1899 is not a leap year
            {1900, 4, 31},
        };
    }

    /**
     * Provides dates to test the length of the month.
     *
     * @return a stream of arguments: {year, month, expectedLength}
     */
    public static Object[][] lengthOfMonthProvider() {
        return new Object[][] {
            {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30},
            {1901, 2, 28}, {1904, 2, 29}, {2000, 2, 29},
        };
    }

    /**
     * Provides data for testing field ranges.
     *
     * @return a stream of arguments: {year, month, day, field, expectedMin, expectedMax}
     */
    public static Object[][] rangeProvider() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, 1, 31},
            {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // Leap year
            {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // Common year
            {2012, 1, 23, DAY_OF_YEAR, 1, 366},
            {2011, 1, 23, DAY_OF_YEAR, 1, 365},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
        };
    }

    /**
     * Provides data for testing getLong(field).
     *
     * @return a stream of arguments: {year, month, day, field, expectedValue}
     */
    public static Object[][] getLongProvider() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {0, 6, 8, ERA, 0},
        };
    }

    /**
     * Provides data for testing with(field, value).
     *
     * @return a stream of arguments: {year, month, day, field, value, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] withProvider() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 0, -2013, 5, 26}, // Switch to BC era
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29}, // Adjust to leap day
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28}, // Adjust to last day of month
        };
    }

    /**
     * Provides data for testing plus(amount, unit).
     *
     * @return a stream of arguments: {year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] plusProvider() {
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, ERAS, -2013, 5, 26},
        };
    }

    /**
     * Provides data for testing until(endDate, unit).
     *
     * @return a stream of arguments: {startYear, startMonth, startDay, endYear, endMonth, endDay, unit, expectedAmount}
     */
    public static Object[][] untilProvider() {
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    /**
     * Provides data for testing toString().
     *
     * @return a stream of arguments: {JulianDate, expectedString}
     */
    public static Object[][] toStringProvider() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
            {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversions to/from other temporal types")
    class ConversionTests {
        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void fromJulianDate_shouldReturnCorrespondingIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void fromIsoDate_shouldReturnCorrespondingJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void toEpochDay_shouldReturnCorrectEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void chronologyDateFromTemporal_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void chronologyDateFromEpochDay_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date Creation")
    class CreationTests {
        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowDateTimeException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class PropertyTests {
        @ParameterizedTest(name = "{0}-{1} has {2} days")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLengthForMonth(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, {3}: [{4}, {5}]")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#rangeProvider")
        void range_shouldReturnCorrectValueRangeForField(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) -> {4}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#getLongProvider")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {
        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#withProvider")
        void with_shouldAdjustFieldAndReturnNewDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate date = JulianDate.of(y, m, d);
            JulianDate expectedDate = JulianDate.of(ey, em, ed);
            assertEquals(expectedDate, date.with(field, value));
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#plusProvider")
        void plus_shouldAddAmountAndReturnNewDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            JulianDate date = JulianDate.of(y, m, d);
            JulianDate expectedDate = JulianDate.of(ey, em, ed);
            assertEquals(expectedDate, date.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#plusProvider")
        void minus_shouldBeInverseOfPlus(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            JulianDate date = JulianDate.of(y, m, d);
            JulianDate expectedDate = JulianDate.of(ey, em, ed);
            assertEquals(expectedDate, date.minus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void plusDays_shouldBeEquivalentToIsoPlusDays(JulianDate julian, LocalDate iso) {
            assertAll("Adding various number of days",
                () -> assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)), "Adding 0 days"),
                () -> assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)), "Adding 1 day"),
                () -> assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)), "Adding 35 days"),
                () -> assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)), "Adding -1 day"),
                () -> assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)), "Adding -60 days")
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void minusDays_shouldBeEquivalentToIsoMinusDays(JulianDate julian, LocalDate iso) {
            assertAll("Subtracting various number of days",
                () -> assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)), "Subtracting 0 days"),
                () -> assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)), "Subtracting 1 day"),
                () -> assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)), "Subtracting 35 days"),
                () -> assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)), "Subtracting -1 day"),
                () -> assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)), "Subtracting -60 days")
            );
        }
    }

    @Nested
    @DisplayName("Date Difference (until)")
    class UntilTests {
        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void until_sameJulianDate_returnsZeroPeriod(JulianDate julian, @SuppressWarnings("unused") LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian));
        }

        @ParameterizedTest(name = "{index}: {0} until {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void until_equivalentIsoDate_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
        }

        @ParameterizedTest(name = "{index}: {1} until {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void isoDateUntil_equivalentJulianDate_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(julian));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#julianAndIsoDateProvider")
        void until_withDaysUnit_shouldCalculateCorrectDayDifference(JulianDate julian, LocalDate iso) {
            assertAll("Calculating days until a future/past date",
                () -> assertEquals(0, julian.until(iso.plusDays(0), DAYS), "0 days difference"),
                () -> assertEquals(1, julian.until(iso.plusDays(1), DAYS), "1 day difference"),
                () -> assertEquals(35, julian.until(iso.plusDays(35), DAYS), "35 days difference"),
                () -> assertEquals(-40, julian.until(iso.minusDays(40), DAYS), "-40 days difference")
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#untilProvider")
        void until_shouldCalculateCorrectAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("General API")
    class GeneralApiTests {
        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#toStringProvider")
        void toString_shouldReturnFormattedString(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }

        @Test
        void of_withJulianId_shouldReturnJulianChronologyInstance() {
            Chronology chrono = Chronology.of("julian");
            assertNotNull(chrono);
            assertEquals(JulianChronology.INSTANCE, chrono);
            assertEquals("Julian", chrono.getId());
            assertEquals("julian", chrono.getCalendarType());
        }
    }
}