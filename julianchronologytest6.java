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

/**
 * Tests for {@link JulianChronology} and {@link JulianDate}.
 * This class focuses on conversions, date manipulation, and field access.
 */
@DisplayName("JulianChronology and JulianDate")
class JulianChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideSampleJulianAndIsoDates() {
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

    private static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(1900, 0, 1), Arguments.of(1900, 13, 1),
            Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32),
            Arguments.of(1900, 2, 30), // 1900 is a leap year in Julian
            Arguments.of(1899, 2, 29), // 1899 is not a leap year
            Arguments.of(1900, 4, 31)
        );
    }

    private static Stream<Arguments> provideLengthOfMonthData() {
        return Stream.of(
            Arguments.of(1900, 1, 31), Arguments.of(1900, 2, 29), // Julian leap year
            Arguments.of(1900, 3, 31), Arguments.of(1900, 4, 30),
            Arguments.of(1901, 2, 28), // Not a leap year
            Arguments.of(1904, 2, 29), // Julian leap year
            Arguments.of(2000, 2, 29)  // Julian leap year
        );
    }

    private static Stream<Arguments> provideRangeData() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
            Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
            Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
        );
    }

    private static Stream<Arguments> provideGetLongData() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7), // Sunday
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
            // 2014 is not a Julian leap year (28 days in Feb)
            // Day of year: Jan(31) + Feb(28) + Mar(31) + Apr(30) + 26
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1), // AD
            Arguments.of(0, 6, 8, ERA, 0)      // BC
        );
    }

    private static Stream<Arguments> provideWithFieldData() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26), // Switch to BC
            Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjusts day for shorter month
            Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28) // Adjusts for non-leap year
        );
    }

    private static Stream<Arguments> providePlusAndMinusData() {
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

    private static Stream<Arguments> provideUntilData() {
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

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversions to/from other calendar systems")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void fromJulianToIso_shouldSucceed(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void fromIsoToJulian_shouldSucceed(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void chronologyDateFromTemporal_shouldSucceed(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void toEpochDay_shouldReturnCorrectValue(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void chronologyDateFromEpochDay_shouldSucceed(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Factory and validation")
    class FactoryAndValidationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideInvalidDateParts")
        void of_shouldThrowExceptionForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideLengthOfMonthData")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field and range access")
    class FieldAndRangeTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideRangeData")
        void range_shouldReturnCorrectRangeForField(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, JulianDate.of(year, month, day).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideGetLongData")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date manipulation")
    class ManipulationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideWithFieldData")
        void with_shouldReturnDateWithModifiedField(int year, int month, int day, TemporalField field, long value, int exYear, int exMonth, int exDay) {
            JulianDate initialDate = JulianDate.of(year, month, day);
            JulianDate expectedDate = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expectedDate, initialDate.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#providePlusAndMinusData")
        void plus_shouldAddTemporalAmount(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate initialDate = JulianDate.of(year, month, day);
            JulianDate expectedDate = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expectedDate, initialDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#providePlusAndMinusData")
        void minus_shouldSubtractTemporalAmount(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate endDate = JulianDate.of(exYear, exMonth, exDay);
            JulianDate expectedDate = JulianDate.of(year, month, day);
            assertEquals(expectedDate, endDate.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        @DisplayName("plusDays should behave equivalently to ISO LocalDate")
        void plusDays_shouldBeEquivalentToIso(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        @DisplayName("minusDays should behave equivalently to ISO LocalDate")
        void minusDays_shouldBeEquivalentToIso(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
        }
    }

    @Nested
    @DisplayName("Period calculations")
    class PeriodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideUntilData")
        void until_shouldCalculateAmountBetweenDatesInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void until_aTemporallyEquivalentDate_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideSampleJulianAndIsoDates")
        void until_days_shouldBeEquivalentToIso(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }
    }

    @Nested
    @DisplayName("General methods")
    class GeneralMethodTests {
        @Test
        void toString_shouldReturnCorrectFormat() {
            assertEquals("Julian AD 1-01-01", JulianDate.of(1, 1, 1).toString());
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
        }

        @Test
        @DisplayName("Era and YearOfEra should be consistent for years around the BC/AD boundary")
        void eraAndYearOfEra_shouldBeConsistent() {
            for (int year = -200; year < 200; year++) {
                if (year == 0) continue; // Proleptic year 0 is 1 BC
                JulianDate base = JulianChronology.INSTANCE.date(year, 1, 1);
                assertEquals(year, base.get(YEAR));

                JulianEra expectedEra = (year <= 0 ? JulianEra.BC : JulianEra.AD);
                assertEquals(expectedEra, base.getEra());

                int expectedYoe = (year <= 0 ? 1 - year : year);
                assertEquals(expectedYoe, base.get(YEAR_OF_ERA));

                // Verify that creating a date from era and year-of-era yields the same date
                JulianDate eraBased = JulianChronology.INSTANCE.date(expectedEra, expectedYoe, 1, 1);
                assertEquals(base, eraBased);
            }
        }
    }
}