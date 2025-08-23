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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedDate")
class InternationalFixedDateTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideDateSamples() {
        return Stream.of(
            Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)),
            Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)),
            Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
            Arguments.of(InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)),
            // Leap year (4)
            Arguments.of(InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)),
            Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap Day
            Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)), // Year Day in leap year
            // Century non-leap year (100)
            Arguments.of(InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)),
            // Century leap year (400)
            Arguments.of(InternationalFixedDate.of(400, 6, 29), LocalDate.of(400, 6, 17)), // Leap Day
            // Modern dates
            Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(-1, 13, 28, "invalid year"),
            Arguments.of(0, 1, 1, "invalid year"),
            Arguments.of(1900, 0, 1, "invalid month"),
            Arguments.of(1900, 14, 1, "invalid month"),
            Arguments.of(1900, 1, 0, "invalid day"),
            Arguments.of(1900, 1, 29, "invalid day for 28-day month"),
            Arguments.of(1900, 13, 30, "invalid day for 29-day month"),
            Arguments.of(1900, 2, 29, "invalid day for non-leap month")
        );
    }

    static Stream<Integer> provideNonLeapYearsForLeapDayTest() {
        return Stream.of(1, 100, 200, 300, 1900);
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Creation and Conversion")
    class CreationAndConversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("from ISO LocalDate should be correct")
        void shouldCreateFromIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("to ISO LocalDate should be correct")
        void shouldConvertToIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("from epoch day should be correct")
        void shouldCreateFromEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("to epoch day should be correct")
        void shouldConvertToEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("from TemporalAccessor (LocalDate) should be correct")
        void shouldCreateFromTemporal(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory Validation")
    class FactoryValidation {

        @ParameterizedTest(name = "[{index}] {3}: year={0}, month={1}, day={2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideInvalidDateParts")
        @DisplayName("of() should throw for invalid date parts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int day, String description) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideNonLeapYearsForLeapDayTest")
        @DisplayName("of() should throw for leap day in non-leap year")
        void of_withLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @Test
        @DisplayName("prolepticYear() should throw for invalid year of era")
        void prolepticYear_withInvalidYear_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }
    }

    @Nested
    @DisplayName("Querying")
    class Querying {

        @Test
        void lengthOfMonth_shouldBe28or29() {
            // Standard months have 28 days
            assertEquals(28, InternationalFixedDate.of(2014, 1, 1).lengthOfMonth());
            assertEquals(28, InternationalFixedDate.of(2014, 12, 1).lengthOfMonth());

            // Month 13 has 29 days
            assertEquals(29, InternationalFixedDate.of(2014, 13, 1).lengthOfMonth());

            // Month 6 has 29 days only in a leap year
            assertEquals(28, InternationalFixedDate.of(2014, 6, 1).lengthOfMonth()); // Common year
            assertEquals(29, InternationalFixedDate.of(2012, 6, 1).lengthOfMonth()); // Leap year
        }

        @ParameterizedTest(name = "[{index}] {0}-{1}-{2} .getLong({3}) == {4}")
        @MethodSource("provideGetLongData")
        @DisplayName("getLong() should return correct field value")
        void getLong_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, dom);
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> provideGetLongData() {
            return Stream.of(
                // Common year date
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 4 * 28 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 13L + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                // Leap year date
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 5 * 28 + 1 + 2 * 28 + 26), // +1 for leap day
                Arguments.of(2012, 9, 28, ALIGNED_WEEK_OF_YEAR, 36),
                // Year Day (end of year)
                Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0), // Special value for Year Day
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29),
                Arguments.of(2014, 13, 29, DAY_OF_YEAR, 12 * 28 + 29),
                Arguments.of(2012, 13, 29, DAY_OF_YEAR, 12 * 28 + 29 + 1), // Leap year
                Arguments.of(2014, 13, 29, ALIGNED_WEEK_OF_YEAR, 0),
                // Leap Day
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0), // Special value for Leap Day
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29),
                Arguments.of(2012, 6, 29, DAY_OF_YEAR, 5 * 28 + 29),
                Arguments.of(2012, 6, 29, ALIGNED_WEEK_OF_YEAR, 0)
            );
        }
    }

    @Nested
    @DisplayName("Arithmetic")
    class Arithmetic {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("plus(DAYS) should add days correctly")
        void plusDays(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(fixedDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(fixedDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(fixedDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(fixedDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("minus(DAYS) should subtract days correctly")
        void minusDays(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(fixedDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(fixedDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(fixedDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(fixedDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        @DisplayName("until() should return zero period for same date")
        void until_sameDate_shouldReturnZero(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }

        @ParameterizedTest(name = "[{index}] from {0}-{1}-{2} to {3}-{4}-{5} is {7} {6}")
        @MethodSource("provideUntilData")
        @DisplayName("until() should calculate duration correctly")
        void until_shouldCalculateDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilData() {
            return Stream.of(
                // Days
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                // Weeks
                Arguments.of(2014, 5, 26, 2014, 5, 26, WEEKS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1),
                // Months
                Arguments.of(2014, 5, 26, 2014, 5, 26, MONTHS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                // Years
                Arguments.of(2014, 5, 26, 2014, 5, 26, YEARS, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                // Across Year Day
                Arguments.of(2014, 13, 28, 2015, 1, 1, DAYS, 2),
                // Across Leap Day
                Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2)
            );
        }

        @ParameterizedTest(name = "[{index}] from {0}-{1}-{2} to {3}-{4}-{5} is {6}Y{7}M{8}D")
        @MethodSource("provideUntilPeriodData")
        @DisplayName("until() should return correct ChronoPeriod")
        void until_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> provideUntilPeriodData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                // Across leap year
                Arguments.of(2011, 13, 26, 2012, 13, 26, 1, 0, 0),
                // Start on Year Day
                Arguments.of(2003, 13, 29, 2004, 6, 29, 0, 6, 0),
                // Start on Leap Day
                Arguments.of(2008, 6, 29, 2008, 6, 29, 0, 0, 0),
                Arguments.of(2012, 6, 29, 2016, 6, 29, 4, 0, 0)
            );
        }
    }

    @Nested
    @DisplayName("Adjustment")
    class Adjustment {

        @Test
        @DisplayName("with(TemporalAdjuster.lastDayOfMonth) should work correctly")
        void with_lastDayOfMonth_adjuster() {
            assertEquals(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2012, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(InternationalFixedDate.of(2009, 6, 28), InternationalFixedDate.of(2009, 6, 23).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(InternationalFixedDate.of(2007, 13, 29), InternationalFixedDate.of(2007, 13, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest(name = "[{index}] {0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("provideWithData")
        @DisplayName("with(TemporalField, long) should adjust date correctly")
        void with_field_shouldAdjust(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> provideWithData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                // Adjusting a leap day
                Arguments.of(2012, 6, 29, YEAR, 2013, 2013, 6, 28), // to non-leap year
                // Adjusting Year Day
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 1, 2014, 13, 1)
            );
        }

        @Test
        @DisplayName("with() should throw for invalid field values")
        void with_invalidValue_shouldThrowException() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_MONTH, 30));
            assertThrows(DateTimeException.class, () -> date.with(MONTH_OF_YEAR, 14));
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_YEAR, 366)); // 2014 is not a leap year
        }
    }

    @Nested
    @DisplayName("Object Contract")
    class ObjectContract {

        @ParameterizedTest
        @MethodSource
        @DisplayName("toString() should return correct format")
        void toString_shouldReturnCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        static Stream<Arguments> provideToStringData() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }
    }

    @Nested
    @DisplayName("Chronology-specific Methods")
    class ChronologyMethods {

        @Test
        @DisplayName("range() should return correct value ranges for fields")
        void chronology_range_shouldBeCorrect() {
            assertEquals(ValueRange.of(1, 29), InternationalFixedChronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 365, 366), InternationalFixedChronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 13), InternationalFixedChronology.INSTANCE.range(MONTH_OF_YEAR));
            assertEquals(ValueRange.of(1, 1_000_000), InternationalFixedChronology.INSTANCE.range(YEAR));
            assertEquals(ValueRange.of(1, 1), InternationalFixedChronology.INSTANCE.range(ERA));
        }

        @Test
        @DisplayName("eraOf() should throw for invalid era values")
        void eraOf_invalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }
    }
}