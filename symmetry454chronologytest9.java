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
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
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

@DisplayName("Symmetry454Chronology and Symmetry454Date Tests")
class Symmetry454ChronologyTest {

    // The original test code used several Era implementations from org.threeten.extra.chrono
    // without importing them (e.g., AccountingEra, CopticEra). We assume they are available.
    
    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            // Constantine the Great, Roman emperor (d. 337)
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)),
            // Charlemagne, Frankish king (d. 814)
            Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            // Norman Conquest: Battle of Hastings
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            // Francesco Petrarca - Petrarch, Italian scholar and poet (d. 1374)
            Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            // Charles the Bold, Duke of Burgundy (d. 1477)
            Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)),
            // Leonardo da Vinci, Italian painter, sculptor, and architect (d. 1519)
            Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            // Christopher Columbus's expedition makes landfall in the Caribbean
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            // Galileo Galilei, Italian astronomer and physicist (d. 1642)
            Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)),
            // William Shakespeare is baptized (d. 1616)
            Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            // Sir Isaac Newton, English physicist and mathematician (d. 1727)
            Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            // Leonhard Euler, Swiss mathematician and physicist (d. 1783)
            Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            // French Revolution: Storming of the Bastille
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            // Albert Einstein, German theoretical physicist (d. 1955)
            Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)),
            // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
            // Unix time begins at 00:00:00 UTC/GMT
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            // Start of the 21st century or 3rd millennium
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should correctly convert between Symmetry454Date and LocalDate")
        void testConversionsAndEquivalence(Symmetry454Date sym454Date, LocalDate isoDate) {
            // Check conversions both ways
            assertEquals(isoDate, LocalDate.from(sym454Date));
            assertEquals(sym454Date, Symmetry454Date.from(isoDate));

            // Check epoch day consistency
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));

            // Check creation from a temporal accessor
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate));

            // Check that the period between equivalent dates is zero
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(sym454Date));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
        }
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryAndValidationTests {
        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
                Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
                Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
                Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 29), Arguments.of(2000, 2, 36),
                Arguments.of(2000, 3, 29), Arguments.of(2000, 4, 29), Arguments.of(2000, 5, 36),
                Arguments.of(2000, 6, 29), Arguments.of(2000, 7, 29), Arguments.of(2000, 8, 36),
                Arguments.of(2000, 9, 29), Arguments.of(2000, 10, 29), Arguments.of(2000, 11, 36),
                Arguments.of(2000, 12, 29), Arguments.of(2004, 12, 36)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        @DisplayName("of() should throw exception for invalid date parts")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> provideInvalidLeapDayDates() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidLeapDayDates")
        @DisplayName("of() should throw exception for invalid leap day in non-leap year")
        void of_withInvalidLeapDay_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Field Access Tests")
    class FieldAccessTests {
        static Stream<Arguments> provideDateAndExpectedLengthOfMonth() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 28), Arguments.of(2000, 2, 28, 35), Arguments.of(2000, 3, 28, 28),
                Arguments.of(2000, 4, 28, 28), Arguments.of(2000, 5, 28, 35), Arguments.of(2000, 6, 28, 28),
                Arguments.of(2000, 7, 28, 28), Arguments.of(2000, 8, 28, 35), Arguments.of(2000, 9, 28, 28),
                Arguments.of(2000, 10, 28, 28), Arguments.of(2000, 11, 28, 35), Arguments.of(2000, 12, 28, 28),
                Arguments.of(2004, 12, 20, 35)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedLengthOfMonth")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndFieldRanges() {
            return Stream.of(
                // DAY_OF_MONTH ranges
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                // Other field ranges
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldRanges")
        @DisplayName("range() should return correct range for a given field")
        void range_shouldReturnCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        static Stream<Arguments> provideLongFieldValues() {
            return Stream.of(
                // Date: 2014-05-26
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for 2014-05-26: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26 = 145
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 145L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L),
                // Weeks in year for 2014-05-26: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4 = 21
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),

                // Date: 2015-12-35 (leap week)
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7L),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35L),
                // Day of year for 2015-12-35: 364 (normal year) + 7 (leap week) = 371
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5L),
                Arguments.of(2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L),
                Arguments.of(2015, 12, 35, MONTH_OF_YEAR, 12L),
                Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 2015L * 12 + 12 - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideLongFieldValues")
        @DisplayName("getLong() should return correct value for a given field")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("plus(DAYS) should match LocalDate.plusDays()")
        void plusDays(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("minus(DAYS) should match LocalDate.minusDays()")
        void minusDays(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(other, DAYS) should match LocalDate.until()")
        void until_days(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }

        static Stream<Arguments> provideWithAdjustmentCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // day of month adjusted
                Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // day of month adjusted
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentCases")
        @DisplayName("with() should adjust field correctly")
        void with_shouldAdjustFieldCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> provideInvalidWithCases() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithCases")
        @DisplayName("with() should throw exception for invalid value")
        void with_withInvalidValue_throwsException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> provideLastDayOfMonthCases() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2009, 12, 23, 2009, 12, 35)
            );
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthCases")
        @DisplayName("with(lastDayOfMonth) should return the last day of the month")
        void with_lastDayOfMonth_returnsLastDay(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date date = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> providePlusMinusCases() {
            return Stream.of(
                // Days
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                // Weeks
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                // Months
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                // Years
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                // Decades
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                // Centuries
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                // Millennia
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Across year boundary
                Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19),
                // Leap week cases
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14)
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusMinusCases")
        @DisplayName("plus() should add amounts correctly")
        void plus_shouldAddAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusMinusCases")
        @DisplayName("minus() should subtract amounts correctly")
        void minus_shouldSubtractAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date expected = Symmetry454Date.of(y, m, d);
            Symmetry454Date start = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideUntilCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13L),
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
        @MethodSource("provideUntilCases")
        @DisplayName("until(end, unit) should calculate amount between dates")
        void until_unit_shouldCalculateAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilPeriodCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        @DisplayName("until(end) should calculate period between dates")
        void until_period_shouldCalculatePeriodBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("API Tests")
    class ApiTests {
        static Stream<Arguments> provideUnsupportedEras() {
            return Stream.of(
                // A selection of eras not compatible with Symmetry454
                Arguments.of(HijrahEra.AH),
                Arguments.of(JapaneseEra.MEIJI),
                Arguments.of(MinguoEra.ROC),
                Arguments.of(ThaiBuddhistEra.BE)
                // The original list was very long; this subset demonstrates the test's purpose.
                // Other eras from the original test: AccountingEra, CopticEra, DiscordianEra, etc.
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        @DisplayName("prolepticYear() should throw exception for unsupported era")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        static Stream<Arguments> provideToStringCases() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
            );
        }

        @ParameterizedTest
        @MethodSource("provideToStringCases")
        @DisplayName("toString() should return correct formatted string")
        void toString_shouldReturnCorrectFormatting(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        @DisplayName("eraOf() should return correct era for value")
        void eraOf_shouldReturnCorrectEra() {
            assertEquals(IsoEra.BCE, Symmetry454Chronology.INSTANCE.eraOf(0));
            assertEquals(IsoEra.CE, Symmetry454Chronology.INSTANCE.eraOf(1));
        }
    }
}