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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InternationalFixedDate}.
 * <p>
 * This test class covers the creation, conversion, manipulation, and field access
 * of International Fixed dates, ensuring their correctness and adherence to the
 * International Fixed Calendar rules.
 */
@DisplayName("InternationalFixedDate Tests")
class InternationalFixedChronologyTestTest18 {

    // -----------------------------------------------------------------------
    // Data Providers
    // -----------------------------------------------------------------------

    static Stream<Arguments> sampleFixedAndIsoDates() {
        return Stream.of(
            Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)),
            Arguments.of(InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)),
            Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)),
            Arguments.of(InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)),
            Arguments.of(InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)),
            Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)),
            Arguments.of(InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)),
            Arguments.of(InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)),
            Arguments.of(InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)),
            Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)),
            Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)),
            Arguments.of(InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    static Stream<Arguments> invalidDateComponents() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(0, 1, 1),
            Arguments.of(1900, -2, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 15, 1),
            Arguments.of(1900, 1, -1), Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 29),
            Arguments.of(1904, -1, -2), Arguments.of(1904, -1, 0), Arguments.of(1904, -1, 1),
            Arguments.of(1900, -1, 0), Arguments.of(1900, -1, -2), Arguments.of(1900, 0, -1),
            Arguments.of(1900, 0, 1), Arguments.of(1900, 0, 2), Arguments.of(1900, 2, 29),
            Arguments.of(1900, 13, 30)
        );
    }

    static Stream<Integer> invalidLeapDayYears() {
        return Stream.of(1, 100, 200, 300, 1900);
    }

    static Stream<Arguments> monthLengths() {
        // year, month, day, expectedLength
        return Stream.of(
            Arguments.of(1900, 1, 28, 28), Arguments.of(1900, 2, 28, 28),
            Arguments.of(1900, 6, 28, 28), Arguments.of(1900, 12, 28, 28),
            Arguments.of(1900, 13, 29, 29), Arguments.of(1904, 6, 29, 29)
        );
    }

    static Stream<Arguments> fieldRanges() {
        // year, month, dom, field, expectedRange
        return Stream.of(
            // --- DAY_OF_MONTH ---
            Arguments.of(2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)), // Leap Day month
            Arguments.of(2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)), // Year Day month
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)), // Regular month
            Arguments.of(2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)), // Non-leap June
            // --- DAY_OF_YEAR ---
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)), // Leap year
            Arguments.of(2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)), // Non-leap year
            // --- MONTH_OF_YEAR ---
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)),
            // --- ALIGNED_DAY_OF_WEEK_IN_MONTH ---
            Arguments.of(2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)), // Leap Day
            Arguments.of(2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)), // Year Day
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)), // Regular day
            // --- ALIGNED_WEEK_OF_MONTH ---
            Arguments.of(2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)), // Leap Day
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Regular day
            // --- DAY_OF_WEEK ---
            Arguments.of(2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)), // Leap Day
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)) // Regular day
        );
    }

    static Stream<Arguments> fieldValues() {
        // year, month, dom, field, expectedValue
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            // Day of year for 2014-05-26: 4 months * 28 days + 26 days
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 4 * 28 + 26L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20L),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 13 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L),
            // Leap year date
            Arguments.of(2012, 9, 26, DAY_OF_YEAR, 5 * 28 + 1 + 2 * 28 + 26L),
            // Year Day (non-leap)
            Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0L),
            Arguments.of(2014, 13, 29, DAY_OF_YEAR, 12 * 28 + 28 + 1L),
            // Leap Day
            Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0L),
            Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29L),
            Arguments.of(2012, 6, 29, DAY_OF_YEAR, 5 * 28 + 28 + 1L)
        );
    }

    static Stream<Arguments> dateWithFieldAdjustments() {
        // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 13 + 3 - 1, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            // Adjusting Year Day
            Arguments.of(2014, 13, 29, DAY_OF_WEEK, 7, 2014, 13, 28),
            // Adjusting Leap Day
            Arguments.of(2012, 6, 29, DAY_OF_WEEK, 1, 2012, 6, 22),
            // Adjusting year of a leap day to a non-leap year
            Arguments.of(2012, 6, 29, YEAR, 2013, 2013, 6, 28)
        );
    }

    static Stream<Arguments> plusMinusCases() {
        // startYear, startMonth, startDom, amount, unit, expectedYear, expectedMonth, expectedDom
        return Stream.of(
            // Regular date arithmetic
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 6),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 19),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            // Year Day arithmetic
            Arguments.of(2014, 13, 29, 8, DAYS, 2015, 1, 8),
            Arguments.of(2014, 13, 29, 3, WEEKS, 2015, 1, 21),
            Arguments.of(2014, 13, 29, 3, MONTHS, 2015, 3, 28),
            Arguments.of(2014, 13, 29, 3, YEARS, 2017, 13, 29),
            // Leap Day arithmetic
            Arguments.of(2012, 6, 29, 8, DAYS, 2012, 7, 8),
            Arguments.of(2012, 6, 29, 3, WEEKS, 2012, 7, 22),
            Arguments.of(2012, 6, 29, 3, MONTHS, 2012, 9, 28),
            Arguments.of(2012, 6, 29, 3, YEARS, 2015, 6, 28), // to non-leap year
            Arguments.of(2012, 6, 29, 4, YEARS, 2016, 6, 29)  // to leap year
        );
    }

    static Stream<Arguments> untilInUnitsCases() {
        // startY, M, D, endY, M, D, unit, expectedAmount
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6L),
            Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L),
            // Spanning Year Day
            Arguments.of(2014, 13, 28, 2015, 1, 1, DAYS, 2L),
            // Spanning Leap Day
            Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2L),
            // From Leap Day to Year Day
            Arguments.of(2012, 6, 29, 2012, 13, 29, DAYS, 197L),
            Arguments.of(2012, 6, 29, 2012, 13, 29, WEEKS, 28L),
            Arguments.of(2012, 6, 29, 2012, 13, 29, MONTHS, 7L)
        );
    }

    static Stream<Arguments> untilAsPeriodCases() {
        // startY, M, D, endY, M, D, periodY, M, D
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
            // From Leap Day to Year Day
            Arguments.of(2004, 6, 29, 2004, 13, 28, 0, 6, 28),
            Arguments.of(2004, 6, 29, 2004, 13, 29, 0, 7, 0)
        );
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void shouldConvertToLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void shouldConvertFromLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void shouldCreateFromEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void shouldConvertToEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void shouldCreateFromTemporalAccessor(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Invalid Date Scenarios")
    class InvalidDateTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#invalidDateComponents")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest(name = "of({0}, 6, 29)")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#invalidLeapDayYears")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Date-Time Field Access")
    class FieldAccessTests {

        @ParameterizedTest(name = "{0}-{1}-{2} has length of month {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#monthLengths")
        void lengthOfMonth_shouldBeCorrect(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#fieldRanges")
        void range_forVariousFields_shouldReturnCorrectValueRange(int y, int m, int d, TemporalField field, ValueRange expected) {
            assertEquals(expected, InternationalFixedDate.of(y, m, d).range(field));
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#fieldValues")
        void getLong_forVariousFields_shouldReturnCorrectValue(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(y, m, d).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustment")
    class AdjustmentTests {

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#dateWithFieldAdjustments")
        void with_field_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_shouldAdjustToEquivalentFixedDate() {
            InternationalFixedDate date = InternationalFixedDate.of(2000, 1, 4);
            LocalDate adjuster = LocalDate.of(2012, 7, 6);
            // Equivalent to InternationalFixedDate.from(adjuster)
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 7, 19);
            assertEquals(expected, date.with(adjuster));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        @ParameterizedTest(name = "{0}.plus({1}, {2}) -> {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#plusMinusCases")
        void plus_shouldReturnCorrectlyAddedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3}.minus({1}, {2}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#plusMinusCases")
        void minus_shouldReturnCorrectlySubtractedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate expected = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate start = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest(name = "{0}.until({1}, DAYS) should be equivalent to LocalDate")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void until_days_shouldBeEquivalentToLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(1, fixedDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, fixedDate.until(isoDate.plusDays(35), DAYS));
            
            // This check avoids creating dates before year 1, which is not supported.
            if (isoDate.getYear() > 1) {
                assertEquals(-40, fixedDate.until(isoDate.minusDays(40), DAYS));
            }
        }

        @ParameterizedTest(name = "{0}.until({3}, {6}) -> {7}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#untilInUnitsCases")
        void until_withUnit_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest(name = "{0}.until({3}) -> P{4}Y{5}M{6}D")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#untilAsPeriodCases")
        void until_asPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "Period between {0} and itself is zero")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void until_sameDate_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
        }

        @ParameterizedTest(name = "Period between {0} and its ISO equivalent {1} is zero")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void until_equivalentLocalDate_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
        }
        
        @ParameterizedTest(name = "Period between {1} and its IFC equivalent {0} is zero")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest18#sampleFixedAndIsoDates")
        void until_fromLocalDateToEquivalentFixedDate_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    @Nested
    @DisplayName("Chronology-specific Methods")
    class ChronologyTests {
        @Test
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(-1));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidYear_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, -1));
        }
    }

    @Nested
    @DisplayName("Object Methods")
    class ObjectMethodTests {
        @Test
        void test_toString() {
            assertEquals("Ifc CE 1/01/01", InternationalFixedDate.of(1, 1, 1).toString());
            assertEquals("Ifc CE 2012/06/23", InternationalFixedDate.of(2012, 6, 23).toString());
            assertEquals("Ifc CE 2012/06/29", InternationalFixedDate.of(2012, 6, 29).toString()); // Leap Day
            assertEquals("Ifc CE 2012/13/29", InternationalFixedDate.of(2012, 13, 29).toString()); // Year Day
        }
    }
}