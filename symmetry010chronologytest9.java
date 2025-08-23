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

@DisplayName("Symmetry010Chronology and Symmetry010Date Tests")
public class Symmetry010ChronologyTest {

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            // The comments provide historical context for the chosen dates.
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)), // Constantine the Great, Roman emperor (d. 337)
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)), // Charlemagne, Frankish king (d. 814)
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)), // Norman Conquest: Battle of Hastings
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)), // Francesco Petrarca - Petrarch (d. 1374)
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)), // Charles the Bold (d. 1477)
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)), // Leonardo da Vinci (d. 1519)
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)), // Christopher Columbus's expedition
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)), // Galileo Galilei (d. 1642)
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)), // William Shakespeare (d. 1616)
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)), // Sir Isaac Newton (d. 1727)
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)), // Leonhard Euler (d. 1783)
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)), // French Revolution: Storming of the Bastille
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)), // Albert Einstein (d. 1955)
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)), // Dennis MacAlistair Ritchie (d. 2011)
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)), // Unix time begins
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)), // Start of the 21st century
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    @Nested
    @DisplayName("Conversion and Equivalence")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("LocalDate.from(sym010Date) should return the equivalent ISO date")
        void test_fromSymmetryDate_toLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010), "Conversion from Symmetry010Date to LocalDate should be correct");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Symmetry010Date.from(isoDate) should return the equivalent Symmetry010 date")
        void test_fromLocalDate_toSymmetryDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso), "Conversion from LocalDate to Symmetry010Date should be correct");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create the correct date")
        void test_chronologyDateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()),
                "Creating Symmetry010Date from epoch day should be consistent with ISO");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("toEpochDay should return the correct epoch day")
        void test_toEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay(), "toEpochDay should match the equivalent ISO date's epoch day");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct date")
        void test_chronologyDateFromTemporal(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso), "Creating Symmetry010Date from a temporal accessor should work correctly");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(sameDate) should return a zero period")
        void test_until_self_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010), "Period until self should be zero");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(equivalentIsoDate) should return a zero period")
        void test_until_equivalentIsoDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso), "Period until equivalent ISO date should be zero");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("isoDate.until(equivalentSym010Date) should return a zero period")
        void test_isoUntil_equivalentSymmetryDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010), "ISO period until equivalent Symmetry010Date should be zero");
        }
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethodTests {

        static Stream<Arguments> invalidDateParts() {
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

        @ParameterizedTest
        @MethodSource("invalidDateParts")
        @DisplayName("of(y, m, d) with invalid parts should throw DateTimeException")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Stream<Arguments> invalidLeapDayYears() {
            return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayYears")
        @DisplayName("of(y, 12, 37) in a non-leap year should throw DateTimeException")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37),
                "Day 37 of month 12 should only be valid in a leap year");
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 30), Arguments.of(2000, 2, 31), Arguments.of(2000, 3, 30),
                Arguments.of(2000, 4, 30), Arguments.of(2000, 5, 31), Arguments.of(2000, 6, 30),
                Arguments.of(2000, 7, 30), Arguments.of(2000, 8, 31), Arguments.of(2000, 9, 30),
                Arguments.of(2000, 10, 30), Arguments.of(2000, 11, 31), Arguments.of(2000, 12, 30),
                Arguments.of(2004, 12, 37) // Leap year December
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth() should return the correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year December
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Note: 31-day month
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // Leap year December
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        @DisplayName("range(field) should return the correct value range")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Stream<Arguments> dateFields() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147), // 30+31+30+30+26
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172), // 2014 * 12 + 5 - 1
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5),
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37),
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371), // 11*30 + 4*31 + 7 (leap week)
                Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6),
                Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53),
                Arguments.of(2015, 12, 37, MONTH_OF_YEAR, 12),
                Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 24191) // 2016 * 12 - 1
            );
        }

        @ParameterizedTest
        @MethodSource("dateFields")
        @DisplayName("getLong(field) should return the correct field value")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("plus(0, DAYS) should be idempotent")
        void plus_zeroDays_isIdempotent(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("plus(n, DAYS) should be consistent with LocalDate")
        void plus_days_isConsistentWithLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym010.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym010.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym010.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("minus(n, DAYS) should be consistent with LocalDate")
        void minus_days_isConsistentWithLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym010.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym010.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym010.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(other, DAYS) should return correct day difference")
        void until_withDaysUnit_returnsCorrectDifference(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(0, sym010.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym010.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym010.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym010.until(iso.minusDays(40), DAYS));
        }

        static Stream<Arguments> withFieldAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37), // Adjust year on leap day
                Arguments.of(2015, 12, 37, YEAR, 2013, 2013, 12, 30)  // Adjust year on leap day to non-leap
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldAdjustments")
        @DisplayName("with(field, value) should return an adjusted date")
        void with_fieldAndValue_shouldReturnAdjustedDate(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(f, v));
        }

        static Stream<Arguments> invalidWithFieldValues() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365), // Non-leap year
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372), // Leap year
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidWithFieldValues")
        @DisplayName("with(field, value) with an invalid value should throw DateTimeException")
        void with_invalidFieldValue_shouldThrowException(int y, int m, int d, TemporalField f, long v) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(f, v));
        }

        static Stream<Arguments> lastDayOfMonthDates() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 30),
                Arguments.of(2012, 2, 23, 2012, 2, 31),
                Arguments.of(2009, 12, 23, 2009, 12, 37) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthDates")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return the last day of the month")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> datePlusAmount() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("datePlusAmount")
        @DisplayName("plus(amount, unit) should return correctly added date")
        void plus_amountAndUnit_shouldReturnCorrectlyAddedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("datePlusAmount")
        @DisplayName("minus(amount, unit) should return correctly subtracted date")
        void minus_amountAndUnit_shouldReturnCorrectlySubtractedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            // Re-using the 'plus' data by swapping initial and expected dates
            Symmetry010Date initial = Symmetry010Date.of(ey, em, ed);
            Symmetry010Date expected = Symmetry010Date.of(y, m, d);
            assertEquals(expected, initial.minus(amount, unit));
        }

        static Stream<Arguments> untilAmount() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9),
                Arguments.of(2014, 5, 26, 2014, 6, 1, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("untilAmount")
        @DisplayName("until(end, unit) should return the correct amount of time")
        void until_withTemporalUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriod() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriod")
        @DisplayName("until(endDate) should return the correct period")
        void until_withEndDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        static Stream<Arguments> unsupportedEras() {
            return Stream.of(
                Arguments.of(AccountingEra.BCE), Arguments.of(CopticEra.AM),
                Arguments.of(DiscordianEra.YOLD), Arguments.of(EthiopicEra.INCARNATION),
                Arguments.of(HijrahEra.AH), Arguments.of(InternationalFixedEra.CE),
                Arguments.of(JapaneseEra.HEISEI), Arguments.of(JulianEra.AD),
                Arguments.of(MinguoEra.ROC), Arguments.of(PaxEra.CE),
                Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("unsupportedEras")
        @DisplayName("prolepticYear() with an unsupported era should throw ClassCastException")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        @DisplayName("eraOf() with an invalid value should throw DateTimeException")
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> Symmetry010Chronology.INSTANCE.eraOf(2));
        }
    }

    @Nested
    @DisplayName("General Methods")
    class GeneralMethodTests {

        static Stream<Arguments> toStringRepresentations() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
                Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringRepresentations")
        @DisplayName("toString() should return the correct formatted string")
        void toString_shouldReturnFormattedString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}