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

@DisplayName("JulianDate Tests")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample JulianDates and their equivalent ISO LocalDates.
     */
    static Stream<Arguments> provideJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, not ISO
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    /**
     * Provides invalid date components for testing exceptions.
     */
    static Stream<Arguments> provideInvalidDateComponents() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month
            Arguments.of(1900, 13, 1),  // Invalid month
            Arguments.of(1900, 1, 0),   // Invalid day
            Arguments.of(1900, 1, 32),  // Invalid day
            Arguments.of(1900, 2, 30),  // Invalid day for Julian leap year
            Arguments.of(1899, 2, 29)   // Invalid day for non-leap year
        );
    }

    /**
     * Provides dates and expected lengths of the month.
     */
    static Stream<Arguments> provideYearMonthAndExpectedLength() {
        return Stream.of(
            Arguments.of(1900, 1, 31),
            Arguments.of(1900, 2, 29), // Julian leap year
            Arguments.of(1901, 2, 28),
            Arguments.of(1904, 2, 29),
            Arguments.of(2000, 2, 29)
        );
    }

    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDates")
        void julianAndIsoDatesShouldBeEquivalentAndInterchangeable(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian), "Conversion from Julian to ISO LocalDate");
            assertEquals(julian, JulianDate.from(iso), "Conversion from ISO to JulianDate");
            assertEquals(iso.toEpochDay(), julian.toEpochDay(), "Epoch day should be equal");
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(julian.toEpochDay()), "Epoch day round trip");
            assertEquals(julian, JulianChronology.INSTANCE.date(iso), "Chronology.date(temporal)");
            assertEquals(Period.ZERO, iso.until(julian), "ISO until Julian should be zero");
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso), "Julian until ISO should be zero");
        }

        @Test
        void until_onSameDate_shouldReturnZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 23);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideInvalidDateComponents")
        void of_withInvalidDateParts_shouldThrowDateTimeException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Property Tests")
    class PropertyTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideYearMonthAndExpectedLength")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @Test
        void range_forDayOfMonth_shouldReturnCorrectValueRange() {
            assertEquals(ValueRange.of(1, 28), JulianDate.of(2011, 2, 23).range(DAY_OF_MONTH)); // Non-leap
            assertEquals(ValueRange.of(1, 29), JulianDate.of(2012, 2, 23).range(DAY_OF_MONTH)); // Leap
            assertEquals(ValueRange.of(1, 30), JulianDate.of(2012, 4, 23).range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 31), JulianDate.of(2012, 1, 23).range(DAY_OF_MONTH));
        }

        @Test
        void getLong_forGivenField_shouldReturnCorrectValue() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(7, date.getLong(DAY_OF_WEEK));
            assertEquals(26, date.getLong(DAY_OF_MONTH));
            assertEquals(31 + 28 + 31 + 30 + 26, date.getLong(DAY_OF_YEAR));
            assertEquals(5, date.getLong(MONTH_OF_YEAR));
            assertEquals(2014, date.getLong(YEAR));
            assertEquals(1, date.getLong(ERA));
        }

        @Test
        void getLong_forUnsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY));
        }

        @Test
        void toString_shouldReturnFormattedString() {
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
            assertEquals("Julian BC 1-01-01", JulianDate.of(0, 1, 1).toString());
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDates")
        void plusAndMinusDays_shouldBehaveLikeIsoDate(JulianDate julian, LocalDate iso) {
            long[] daysToTest = {0, 1, 35, -1, -60};
            for (long days : daysToTest) {
                assertEquals(iso.plusDays(days), LocalDate.from(julian.plus(days, DAYS)), "Testing plus " + days + " days");
                assertEquals(iso.minusDays(days), LocalDate.from(julian.minus(days, DAYS)), "Testing minus " + days + " days");
            }
        }

        @Test
        void with_temporalField_shouldReturnAdjustedDate() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianDate.of(2014, 5, 22), date.with(DAY_OF_WEEK, 3));
            assertEquals(JulianDate.of(2014, 7, 26), date.with(MONTH_OF_YEAR, 7));
            assertEquals(JulianDate.of(2012, 5, 26), date.with(YEAR, 2012));
            assertEquals(JulianDate.of(-2013, 5, 26), date.with(ERA, 0));
        }

        @Test
        void plus_withTemporalUnit_shouldReturnCorrectDate() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianDate.of(2014, 6, 16), date.plus(3, WEEKS));
            assertEquals(JulianDate.of(2014, 8, 26), date.plus(3, MONTHS));
            assertEquals(JulianDate.of(2017, 5, 26), date.plus(3, YEARS));
            assertEquals(JulianDate.of(2044, 5, 26), date.plus(3, DECADES));
            assertEquals(JulianDate.of(2314, 5, 26), date.plus(3, CENTURIES));
            assertEquals(JulianDate.of(5014, 5, 26), date.plus(3, MILLENNIA));
            assertEquals(JulianDate.of(-2013, 5, 26), date.plus(-1, ERAS));
        }

        @Test
        void minus_withTemporalUnit_shouldReturnCorrectDate() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianDate.of(2014, 4, 21), date.minus(5, WEEKS));
            assertEquals(JulianDate.of(2013, 12, 26), date.minus(5, MONTHS));
            assertEquals(JulianDate.of(2009, 5, 26), date.minus(5, YEARS));
            assertEquals(JulianDate.of(1964, 5, 26), date.minus(5, DECADES));
            assertEquals(JulianDate.of(1514, 5, 26), date.minus(5, CENTURIES));
            assertEquals(JulianDate.of(2014 - 5000, 5, 26), date.minus(5, MILLENNIA));
            assertEquals(JulianDate.of(-2013, 5, 26), date.minus(1, ERAS));
        }
    }

    @Nested
    @DisplayName("Until Calculation Tests")
    class UntilCalculationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDates")
        void until_withDaysUnit_shouldCalculateDifferenceInDays(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }

        @Test
        void until_withVariousUnits_shouldCalculateDifference() {
            JulianDate start = JulianDate.of(2014, 5, 26);
            JulianDate endInOneWeek = JulianDate.of(2014, 6, 2);
            JulianDate endInOneMonth = JulianDate.of(2014, 6, 26);
            JulianDate endInOneYear = JulianDate.of(2015, 5, 26);

            assertEquals(0, start.until(endInOneWeek, WEEKS)); // 7 days is not a full week in this context
            assertEquals(1, start.until(endInOneWeek.plus(1, DAYS), WEEKS));
            assertEquals(1, start.until(endInOneMonth, MONTHS));
            assertEquals(1, start.until(endInOneYear, YEARS));
        }
    }
}