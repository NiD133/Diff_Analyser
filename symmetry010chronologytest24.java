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
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Sample Dates Provider
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleDates() {
        return Stream.of(
            // Constantine the Great, Roman emperor (d. 337)
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)),
            // Charlemagne, Frankish king (d. 814)
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            // Norman Conquest: Battle of Hastings
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            // Francesco Petrarca - Petrarch, Italian scholar and poet (d. 1374)
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            // Charles the Bold, Duke of Burgundy (d. 1477)
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)),
            // Leonardo da Vinci, Italian polymath (d. 1519)
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            // Christopher Columbus's expedition makes landfall in the Caribbean
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            // Galileo Galilei, Italian astronomer (d. 1642)
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)),
            // William Shakespeare is baptized (d. 1616)
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            // Sir Isaac Newton, English physicist (d. 1727)
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            // Leonhard Euler, Swiss mathematician (d. 1783)
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            // French Revolution: Storming of the Bastille
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            // Albert Einstein, German theoretical physicist (d. 1955)
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)),
            // Unix time begins at 00:00:00 UTC/GMT
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            // Start of the 21st century / 3rd millennium
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory methods")
    class FactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidDateProvider")
        @DisplayName("of() should throw for invalid date components")
        void of_shouldThrowExceptionForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#nonLeapYearsProvider")
        @DisplayName("of() should throw for leap day in a non-leap year")
        void of_shouldThrowExceptionForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Inter-calendar conversions")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        void fromSymmetryDate_shouldConvertToCorrectLocalDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        void fromLocalDate_shouldConvertToCorrectSymmetryDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        void fromTemporal_shouldConvertToCorrectSymmetryDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        void toEpochDay_shouldMatchIsoDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        void dateEpochDay_shouldCreateCorrectSymmetryDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date properties")
    class PropertyTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#monthLengthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Chronology methods")
    class ChronologyMethods {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidEraProvider")
        @DisplayName("prolepticYear() should throw for unsupported era")
        void prolepticYear_shouldThrowExceptionForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#fieldRangeProvider")
        @DisplayName("range() should return correct value range for field")
        void range_shouldReturnCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#fieldValueProvider")
        @DisplayName("getLong() should return correct field value")
        void getLong_shouldReturnCorrectFieldValue(int year, int month, int dom, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date modification")
    class ModificationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withFieldProvider")
        @DisplayName("with() should set field to a new value")
        void with_shouldSetFieldToValue(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withInvalidFieldProvider")
        @DisplayName("with() should throw for invalid field value")
        void with_shouldThrowExceptionForInvalidValue(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#lastDayOfMonthProvider")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should adjust to the correct last day")
        void with_lastDayOfMonth_shouldAdjustToLastDay(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusProvider")
        @DisplayName("plus() should add amount of unit")
        void plus_shouldAddAmountOfUnit(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusWithLeapWeekProvider")
        @DisplayName("plus() should handle leap week correctly")
        void plus_shouldHandleLeapWeekCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusProvider")
        @DisplayName("minus() should subtract amount of unit")
        void minus_shouldSubtractAmountOfUnit(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusWithLeapWeekProvider")
        @DisplayName("minus() should handle leap week correctly")
        void minus_shouldHandleLeapWeekCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilUnitProvider")
        @DisplayName("until() should return amount in specified unit")
        void until_shouldReturnAmountInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilPeriodProvider")
        @DisplayName("until() should return period between dates")
        void until_shouldReturnPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int yp, int mp, int dp) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(Symmetry010Chronology.INSTANCE.period(yp, mp, dp), start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleDates")
        @DisplayName("until() with self should return zero period")
        void until_self_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(sym010Date));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(sym010Date));
        }

        @Test
        @DisplayName("minus() should throw for unsupported period type")
        void minus_shouldThrowExceptionForUnsupportedPeriodType() {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(2014, 5, 26).minus(Period.ofMonths(2)));
        }
    }

    @Nested
    @DisplayName("Object behavior")
    class ObjectBehaviorTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#toStringProvider")
        @DisplayName("toString() should return correctly formatted string")
        void toString_shouldReturnCorrectString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
            Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
            Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
            Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 31), Arguments.of(2000, 2, 32),
            Arguments.of(2000, 3, 31), Arguments.of(2000, 4, 31), Arguments.of(2000, 5, 32),
            Arguments.of(2000, 6, 31), Arguments.of(2000, 7, 31), Arguments.of(2000, 8, 32),
            Arguments.of(2000, 9, 31), Arguments.of(2000, 10, 31), Arguments.of(2000, 11, 32),
            Arguments.of(2000, 12, 31), Arguments.of(2004, 12, 38)
        );
    }

    static Stream<Arguments> nonLeapYearsProvider() {
        return Stream.of(
            Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
        );
    }

    static Stream<Arguments> monthLengthProvider() {
        // year, month, day, expectedLength
        return Stream.of(
            Arguments.of(2000, 1, 28, 30), Arguments.of(2000, 2, 28, 31),
            Arguments.of(2000, 3, 28, 30), Arguments.of(2000, 4, 28, 30),
            Arguments.of(2000, 5, 28, 31), Arguments.of(2000, 6, 28, 30),
            Arguments.of(2000, 7, 28, 30), Arguments.of(2000, 8, 28, 31),
            Arguments.of(2000, 9, 28, 30), Arguments.of(2000, 10, 28, 30),
            Arguments.of(2000, 11, 28, 31), Arguments.of(2000, 12, 28, 30),
            Arguments.of(2004, 12, 20, 37)
        );
    }

    static Stream<Era> invalidEraProvider() {
        return Stream.of(
            HijrahEra.AH,
            JapaneseEra.HEISEI,
            MinguoEra.ROC,
            ThaiBuddhistEra.BE
        );
    }

    static Stream<Arguments> fieldRangeProvider() {
        // year, month, dom, field, expectedRange
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
            Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)),
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
            Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Note: spec says 5 weeks for long months
            Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
            Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
        );
    }

    static Stream<Arguments> fieldValueProvider() {
        // year, month, dom, field, expectedValue
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L), // 30+31+30+30+26
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), // 4+5+4+4+4
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L), // 2014 * 12 + 5 - 1
            Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L),
            Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5L),
            Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37L),
            Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L), // 364 + 7
            Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2L),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6L),
            Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L),
            Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 24191L) // 2015 * 12 + 12 - 1
        );
    }

    static Stream<Arguments> withFieldProvider() {
        // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1L, 2014, 5, 20),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28L, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364L, 2014, 12, 30),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1L, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23L, 2014, 6, 9),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4L, 2014, 4, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012L, 2012, 5, 26),
            Arguments.of(2015, 12, 37, YEAR, 2004L, 2004, 12, 37),
            Arguments.of(2015, 12, 37, YEAR, 2013L, 2013, 12, 30),
            Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371L, 2015, 12, 37),
            Arguments.of(2012, 3, 28, DAY_OF_YEAR, 364L, 2012, 12, 30)
        );
    }

    static Stream<Arguments> withInvalidFieldProvider() {
        // year, month, dom, field, value
        return Stream.of(
            Arguments.of(2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8L),
            Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31L),
            Arguments.of(2015, 12, 1, DAY_OF_MONTH, 38L),
            Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365L),
            Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372L),
            Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14L),
            Arguments.of(2013, 1, 1, EPOCH_DAY, 364_523_156L),
            Arguments.of(2013, 1, 1, YEAR, 1_000_001L)
        );
    }

    static Stream<Arguments> lastDayOfMonthProvider() {
        // year, month, day, expectedYear, expectedMonth, expectedDay
        return Stream.of(
            Arguments.of(2012, 1, 23, 2012, 1, 30),
            Arguments.of(2012, 2, 23, 2012, 2, 31),
            Arguments.of(2009, 12, 23, 2009, 12, 37)
        );
    }

    static Stream<Arguments> plusProvider() {
        // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
        return Stream.of(
            Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            Arguments.of(2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28)
        );
    }

    static Stream<Arguments> plusWithLeapWeekProvider() {
        // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
        return Stream.of(
            Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36),
            Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12),
            Arguments.of(2015, 12, 28, 52, WEEKS, 2016, 12, 21),
            Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
            Arguments.of(2015, 12, 28, 12, MONTHS, 2016, 12, 28),
            Arguments.of(2015, 12, 28, 3, YEARS, 2018, 12, 28)
        );
    }

    static Stream<Arguments> untilUnitProvider() {
        // y1, m1, d1, y2, m2, d2, unit, expected
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
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

    static Stream<Arguments> untilPeriodProvider() {
        // y1, m1, d1, y2, m2, d2, yearPeriod, monthPeriod, dayPeriod
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
            Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 29)
        );
    }

    static Stream<Arguments> toStringProvider() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
            Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
            Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
            Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
        );
    }
}