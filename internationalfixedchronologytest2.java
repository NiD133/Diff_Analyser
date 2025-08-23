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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    private static InternationalFixedDate date(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleFixedAndIsoDatesProvider() {
        return Stream.of(
                Arguments.of(date(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(date(1, 6, 28), LocalDate.of(1, 6, 17)),
                Arguments.of(date(1, 7, 1), LocalDate.of(1, 6, 18)),
                Arguments.of(date(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
                Arguments.of(date(4, 6, 29), LocalDate.of(4, 6, 17)),   // Leap Day
                Arguments.of(date(4, 7, 1), LocalDate.of(4, 6, 18)),
                Arguments.of(date(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(date(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory methods")
    class FactoryTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateProvider")
        void of_throwsExceptionForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                    Arguments.of(0, 1, 1),      // Invalid year
                    Arguments.of(1900, 14, 1),    // Invalid month
                    Arguments.of(1900, 1, 29),    // Invalid day for month
                    Arguments.of(1900, 6, 29),    // Invalid day for non-leap June
                    Arguments.of(1900, 13, 30)    // Invalid day for Year Day month
            );
        }

        @ParameterizedTest(name = "year {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYearProvider")
        void of_throwsExceptionForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @Test
        void dateYearDay_throwsForDayOfYearTooLargeInNonLeapYear() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.dateYearDay(2001, 366));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion to/from other date systems")
    class ConversionTests {

        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void conversionToAndFromLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest(name = "{index}: {0} -> epoch day {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void conversionToAndFromEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            long epochDay = isoDate.toEpochDay();
            assertEquals(epochDay, fixedDate.toEpochDay());
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(epochDay));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field accessors")
    class FieldAccessorTests {

        @ParameterizedTest(name = "getLong({1}) on {0} -> {2}")
        @MethodSource("getLongProvider")
        void getLong(InternationalFixedDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                    // Regular day in a non-leap year
                    Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 5),
                    Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 26),
                    Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 4 * 28 + 26),
                    Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 5),
                    Arguments.of(date(2014, 5, 26), YEAR, 2014),
                    Arguments.of(date(2014, 5, 26), ERA, 1),
                    // Year Day in a non-leap year
                    Arguments.of(date(2014, 13, 29), DAY_OF_WEEK, 0),
                    Arguments.of(date(2014, 13, 29), DAY_OF_MONTH, 29),
                    Arguments.of(date(2014, 13, 29), DAY_OF_YEAR, 12 * 28 + 28 + 1),
                    // Leap Day
                    Arguments.of(date(2012, 6, 29), DAY_OF_WEEK, 0),
                    Arguments.of(date(2012, 6, 29), DAY_OF_MONTH, 29),
                    Arguments.of(date(2012, 6, 29), DAY_OF_YEAR, 5 * 28 + 28 + 1)
            );
        }

        @ParameterizedTest(name = "range({1}) on {0} -> {2}")
        @MethodSource("rangeProvider")
        void range(InternationalFixedDate date, TemporalField field, ValueRange expected) {
            assertEquals(expected, date.range(field));
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                    // Day of month
                    Arguments.of(date(2011, 1, 1), DAY_OF_MONTH, ValueRange.of(1, 28)), // Normal month
                    Arguments.of(date(2011, 13, 1), DAY_OF_MONTH, ValueRange.of(1, 29)),// Year Day month
                    Arguments.of(date(2012, 6, 1), DAY_OF_MONTH, ValueRange.of(1, 29)), // Leap Day month
                    // Day of year
                    Arguments.of(date(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)), // Non-leap year
                    Arguments.of(date(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366)), // Leap year
                    // Month of year
                    Arguments.of(date(2012, 1, 1), MONTH_OF_YEAR, ValueRange.of(1, 13)),
                    // Day of week
                    Arguments.of(date(2012, 1, 1), DAY_OF_WEEK, ValueRange.of(1, 7)),   // Regular day
                    Arguments.of(date(2012, 6, 29), DAY_OF_WEEK, ValueRange.of(0, 0))    // Leap Day
            );
        }

        @ParameterizedTest(name = "lengthOfMonth for {0}-{1} -> {2}")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth(int year, int month, int expectedLength) {
            assertEquals(expectedLength, date(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                    Arguments.of(1900, 1, 28),   // Regular month
                    Arguments.of(1900, 13, 29),  // Year Day month in non-leap year
                    Arguments.of(1904, 6, 29),   // Leap Day month in leap year
                    Arguments.of(1900, 6, 28)    // June in non-leap year
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date manipulation")
    class ManipulationTests {

        @ParameterizedTest(name = "{0} with({1}, {2}) -> {3}")
        @MethodSource("withProvider")
        void with(InternationalFixedDate base, TemporalField field, long value, InternationalFixedDate expected) {
            assertEquals(expected, base.with(field, value));
        }

        static Stream<Arguments> withProvider() {
            return Stream.of(
                    Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 28, date(2014, 5, 28)),
                    Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 4, date(2014, 4, 26)),
                    Arguments.of(date(2014, 5, 26), YEAR, 2012, date(2012, 5, 26)),
                    // Adjust to a leap day
                    Arguments.of(date(2012, 3, 28), DAY_OF_YEAR, 6 * 28 + 1, date(2012, 6, 29)),
                    // Adjusting year from a leap day
                    Arguments.of(date(2012, 6, 29), YEAR, 2013, date(2013, 6, 28))
            );
        }

        @Test
        void with_lastDayOfMonthAdjuster() {
            assertEquals(date(2012, 6, 29), date(2012, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(date(2009, 6, 28), date(2009, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest(name = "{0} + {1} {2} = {3}")
        @MethodSource("plusProvider")
        void plus(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3} - {1} {2} = {0}")
        @MethodSource("plusProvider")
        void minus(InternationalFixedDate expected, long amount, TemporalUnit unit, InternationalFixedDate base) {
            assertEquals(expected, base.minus(amount, unit));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                    // Days
                    Arguments.of(date(2014, 5, 26), 8, DAYS, date(2014, 6, 6)),
                    Arguments.of(date(2014, 5, 26), -3, DAYS, date(2014, 5, 23)),
                    // Weeks
                    Arguments.of(date(2014, 5, 26), 3, WEEKS, date(2014, 6, 19)),
                    // Months
                    Arguments.of(date(2014, 5, 26), 3, MONTHS, date(2014, 8, 26)),
                    // Years
                    Arguments.of(date(2014, 5, 26), 3, YEARS, date(2017, 5, 26)),
                    // Crossing year boundary
                    Arguments.of(date(2014, 13, 26), 3, WEEKS, date(2015, 1, 19)),
                    // From leap day
                    Arguments.of(date(2012, 6, 29), 8, DAYS, date(2012, 7, 8)),
                    Arguments.of(date(2012, 6, 29), 4, YEARS, date(2016, 6, 29)),
                    Arguments.of(date(2012, 6, 29), 1, YEARS, date(2013, 6, 28)) // lands on non-leap day
            );
        }

        @ParameterizedTest(name = "from {0} to {1} is {3} {2}")
        @MethodSource("untilUnitProvider")
        void until_withUnit(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                    Arguments.of(date(2014, 5, 26), date(2014, 6, 4), DAYS, 6),
                    Arguments.of(date(2014, 5, 26), date(2014, 6, 5), WEEKS, 1),
                    Arguments.of(date(2014, 5, 26), date(2014, 6, 26), MONTHS, 1),
                    Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1),
                    // Across year boundary
                    Arguments.of(date(2014, 13, 28), date(2015, 1, 1), DAYS, 2),
                    // From leap day to year day
                    Arguments.of(date(2012, 6, 29), date(2012, 13, 29), DAYS, 197),
                    Arguments.of(date(2012, 6, 29), date(2012, 13, 29), MONTHS, 7)
            );
        }

        @ParameterizedTest(name = "from {0} to {1} is {2}")
        @MethodSource("untilPeriodProvider")
        void until_withPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                    Arguments.of(date(2014, 5, 26), date(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
                    Arguments.of(date(2014, 5, 26), date(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
                    Arguments.of(date(2014, 5, 26), date(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
                    Arguments.of(date(2014, 5, 26), date(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                    // From leap day
                    Arguments.of(date(2004, 6, 29), date(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0))
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        @DisplayName("until() returns zero period for same date across chronologies")
        void until_zeroPeriodForSameDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology methods")
    class ChronologyTests {

        @ParameterizedTest(name = "eraOf({0})")
        @MethodSource("invalidEraProvider")
        void eraOf_throwsExceptionForInvalidEra(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Arguments> invalidEraProvider() {
            return Stream.of(Arguments.of(-1), Arguments.of(0), Arguments.of(2));
        }

        @ParameterizedTest(name = "prolepticYear({0})")
        @MethodSource("invalidProlepticYearProvider")
        void prolepticYear_throwsExceptionForInvalidYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }

        static Stream<Arguments> invalidProlepticYearProvider() {
            return Stream.of(Arguments.of(-10), Arguments.of(-1), Arguments.of(0));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        void testToString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                    Arguments.of(date(1, 1, 1), "Ifc CE 1/01/01"),
                    Arguments.of(date(2012, 6, 23), "Ifc CE 2012/06/23"),
                    Arguments.of(date(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                    Arguments.of(date(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @Test
        void equalsAndHashCode() {
            new EqualsTester()
                    .addEqualityGroup(date(2014, 5, 26), date(2014, 5, 26))
                    .addEqualityGroup(date(2014, 5, 27))
                    .addEqualityGroup(date(2014, 6, 26))
                    .addEqualityGroup(date(2015, 5, 26))
                    .addEqualityGroup(LocalDate.of(2014, 5, 26))
                    .testEquals();
        }
    }
}