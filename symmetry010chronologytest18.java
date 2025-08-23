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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
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

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 * This class focuses on the core functionality, conversions, and date calculations.
 */
@DisplayName("Symmetry010 Chronology and Date")
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideValidSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    //-----------------------------------------------------------------------
    // Conversion and Epoch Day Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion and Epoch Day Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void shouldConvertToCorrectLocalDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void shouldConvertFromCorrectLocalDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void shouldConvertToCorrectEpochDay(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void shouldCreateCorrectDateFromEpochDay(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void shouldCreateCorrectDateFromTemporalAccessor(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Factory Method Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(2000, 0, 1), Arguments.of(2000, 13, 1),
                Arguments.of(2000, 1, 0), Arguments.of(2000, 1, 31), // Jan has 30 days
                Arguments.of(2000, 2, 32), // Feb has 31 days
                Arguments.of(2000, 4, 31), // Apr has 30 days
                Arguments.of(2004, 12, 38) // Leap Dec has 37 days
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        @Test
        void of_withInvalidLeapDay_shouldThrowException() {
            // 2000 is not a leap year in Symmetry010 calendar
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(2000, 12, 37));
        }

        static Stream<Era> provideUnsupportedErasForProlepticYear() {
            return Stream.of(HijrahEra.AH, JapaneseEra.HEISEI, MinguoEra.ROC, ThaiBuddhistEra.BE);
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedErasForProlepticYear")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    //-----------------------------------------------------------------------
    // Field Access and Range Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Field Access and Range Tests")
    class FieldAccessTests {

        static Stream<Arguments> provideMonthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 30), Arguments.of(2000, 2, 31), Arguments.of(2000, 3, 30),
                Arguments.of(2000, 4, 30), Arguments.of(2000, 5, 31), Arguments.of(2000, 6, 30),
                Arguments.of(2000, 7, 30), Arguments.of(2000, 8, 31), Arguments.of(2000, 9, 30),
                Arguments.of(2000, 10, 30), Arguments.of(2000, 11, 31), Arguments.of(2000, 12, 30),
                Arguments.of(2004, 12, 37) // Leap year December
            );
        }

        @ParameterizedTest
        @MethodSource("provideMonthLengths")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                Arguments.of(2012, 1, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, DAY_OF_YEAR, ValueRange.of(1, 364)), // Normal year
                Arguments.of(2015, 1, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2015, 12, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldRanges")
        void range_forGivenField_shouldReturnCorrectRange(int year, int month, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, 1).range(field));
        }

        static Stream<Arguments> provideDateFields() {
            return Stream.of(
                // Test case for 2014-05-26
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L), // Monday
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L), // 30+31+30+30+26
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), // ceil(147 / 7)
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),

                // Test case for 2015-12-37 (a leap year)
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5L), // Friday
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37L),
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L), // 364 + 7
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L) // 52 + 1
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateFields")
        void getLong_forGivenField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Date Arithmetic Tests")
    class DateArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void plusDays_shouldBehaveLikeIsoPlusDays(Symmetry010Date sym010, LocalDate iso) {
            assertAll(
                () -> assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS))),
                () -> assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS))),
                () -> assertEquals(iso.plusDays(35), LocalDate.from(sym010.plus(35, DAYS))),
                () -> assertEquals(iso.plusDays(-1), LocalDate.from(sym010.plus(-1, DAYS))),
                () -> assertEquals(iso.plusDays(-60), LocalDate.from(sym010.plus(-60, DAYS)))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void minusDays_shouldBehaveLikeIsoMinusDays(Symmetry010Date sym010, LocalDate iso) {
            assertAll(
                () -> assertEquals(iso, LocalDate.from(sym010.minus(0, DAYS))),
                () -> assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS))),
                () -> assertEquals(iso.minusDays(35), LocalDate.from(sym010.minus(35, DAYS))),
                () -> assertEquals(iso.minusDays(-1), LocalDate.from(sym010.minus(-1, DAYS))),
                () -> assertEquals(iso.minusDays(-60), LocalDate.from(sym010.minus(-60, DAYS)))
            );
        }

        static Stream<Arguments> provideDatePlusAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Test crossing leap week
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAdjustments")
        void plus_withVariousUnits_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideValidSymmetryAndIsoDates")
        void until_whenComparingDateToItself_returnsZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            ChronoPeriod zeroSymmetryPeriod = Symmetry010Chronology.INSTANCE.period(0, 0, 0);
            assertAll(
                () -> assertEquals(zeroSymmetryPeriod, sym010Date.until(sym010Date)),
                () -> assertEquals(zeroSymmetryPeriod, sym010Date.until(isoDate)),
                () -> assertEquals(Period.ZERO, isoDate.until(sym010Date))
            );
        }

        static Stream<Arguments> provideUntilDurations() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilDurations")
        void until_withVariousUnits_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Adjuster and 'with' Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Adjuster and 'with' Tests")
    class WithAdjusterTests {

        static Stream<Arguments> provideWithFieldAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithFieldAdjustments")
        void with_shouldAdjustFieldCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_shouldReturnCorrectDate() {
            Symmetry010Date date = Symmetry010Date.of(2012, 2, 23); // Feb has 31 days
            Symmetry010Date expected = Symmetry010Date.of(2012, 2, 31);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_lastDayOfMonthInLeapYear_shouldReturnCorrectDate() {
            Symmetry010Date date = Symmetry010Date.of(2009, 12, 23); // 2009 is a leap year
            Symmetry010Date expected = Symmetry010Date.of(2009, 12, 37);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void adjustIntoLocalDateTime_shouldReturnCorrectDateTime() {
            Symmetry010Date sym010 = Symmetry010Date.of(2012, 7, 19);
            LocalDateTime test = LocalDateTime.MIN.with(sym010);
            // ISO equivalent of 2012-07-19 (Sym010) is 2012-07-20
            assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test);
        }
    }

    //-----------------------------------------------------------------------
    // General Method Tests (toString, equals, hashCode)
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("General Method Tests")
    class GeneralMethodTests {

        @Test
        void toString_shouldReturnCorrectFormat() {
            Symmetry010Date date = Symmetry010Date.of(2009, 12, 37);
            assertEquals("Sym010 CE 2009/12/37", date.toString());
        }

        @Test
        void equals_and_hashCode_shouldAdhereToContract() {
            Symmetry010Date dateA = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date dateB = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date dateC = Symmetry010Date.of(2014, 6, 26);

            new EqualsTester()
                .addEqualityGroup(dateA, dateB)
                .addEqualityGroup(dateC)
                .testEquals();
        }
    }
}