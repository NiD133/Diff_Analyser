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
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetryAndIsoDatesProvider() {
        return new Object[][] {
            // {Symmetry010Date, corresponding ISO LocalDate}
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)}, // Constantine the Great, Roman emperor (d. 337)
            {Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)},
            {Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)}, // Charlemagne, Frankish king (d. 814)
            {Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)},
            {Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)}, // Norman Conquest: Battle of Hastings
            {Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)}, // Francesco Petrarca - Petrarch, Italian scholar and poet in Renaissance Italy, "Father of Humanism" (d. 1374)
            {Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)}, // Charles the Bold, French son of Isabella of Portugal, Duchess of Burgundy (d. 1477)
            {Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)},
            {Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)}, // Leonardo da Vinci, Italian painter, sculptor, and architect (d. 1519)
            {Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)}, // Christopher Columbus's expedition makes landfall in the Caribbean.
            {Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)}, // Galileo Galilei, Italian astronomer and physicist (d. 1642)
            {Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)},
            {Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)}, // William Shakespeare is baptized in Stratford-upon-Avon, Warwickshire, England (date of actual birth is unknown, d. 1616).
            {Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)}, // Sir Isaac Newton, English physicist and mathematician (d. 1727)
            {Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)}, // Leonhard Euler, Swiss mathematician and physicist (d. 1783)
            {Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)}, // French Revolution: Citizens of Paris storm the Bastille.
            {Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)}, // Albert Einstein, German theoretical physicist (d. 1955)
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)}, // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            {Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)},
            {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)}, // Unix time begins at 00:00:00 UTC/GMT.
            {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)}, // Start of the 21st century / 3rd millennium
            {Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)},
            {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void from_localDate_to_symmetry010Date(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void from_symmetry010Date_to_localDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void chronology_dateEpochDay_createsCorrectDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void toEpochDay_returnsCorrectValue(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void chronology_date_fromTemporal_createsCorrectDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Interoperability with ISO Calendar")
    class InteroperabilityTests {

        @ParameterizedTest(name = "{index}: {0}.until({1}) is zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void until_isoDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
        }

        @ParameterizedTest(name = "{index}: {1}.until({0}) is zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDatesProvider")
        void iso_until_symmetryDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
        }
    }

    @Nested
    @DisplayName("Date Creation with Invalid Values")
    class InvalidDateCreationTests {

        static Object[][] invalidDateProvider() {
            return new Object[][] {
                {-1, 13, 28}, { -1, 13, 29}, {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},
                {2000, 1, -1}, {2000, 1, 0}, {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},
                {2000, 1, 31}, {2000, 2, 32}, {2000, 3, 31}, {2000, 4, 31}, {2000, 5, 32},
                {2000, 6, 31}, {2000, 7, 31}, {2000, 8, 32}, {2000, 9, 31}, {2000, 10, 31},
                {2000, 11, 32}, {2000, 12, 31}, {2004, 12, 38}
            };
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Object[][] invalidLeapDayYearProvider() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest(name = "of({0}, 12, 37)")
        @MethodSource("invalidLeapDayYearProvider")
        void of_withInvalidLeapDay_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertyTests {

        static Object[][] lengthOfMonthProvider() {
            return new Object[][] {
                // {year, month, day, expectedLength}
                {2000, 1, 28, 30}, {2000, 2, 28, 31}, {2000, 3, 28, 30},
                {2000, 4, 28, 30}, {2000, 5, 28, 31}, {2000, 6, 28, 30},
                {2000, 7, 28, 30}, {2000, 8, 28, 31}, {2000, 9, 28, 30},
                {2000, 10, 28, 30}, {2000, 11, 28, 31}, {2000, 12, 28, 30},
                {2004, 12, 20, 37} // Leap year
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} lengthOfMonth is {3}")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int day, int length) {
            assertEquals(length, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-01 lengthOfMonth is {3}")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_isIndependentOfDay(int year, int month, int day, int length) {
            assertEquals(length, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] rangeProvider() {
            return new Object[][] {
                // {year, month, day, field, expectedRange}
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)}, // Leap year
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)}, // 31-day month is 4 weeks + 3 days
                {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // 37-day month is 5 weeks + 2 days
                {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap year
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2}, range({3}) = {4}")
        @MethodSource("rangeProvider")
        void range_forField_returnsCorrectRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Object[][] getLongProvider() {
            return new Object[][] {
                // {year, month, day, field, expectedValue}
                {2014, 5, 26, DAY_OF_WEEK, 2},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, 147}, // (30+31+30+30) + 26
                {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21}, // 4+5+4+4+4
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 24160}, // (2014-1)*12 + (5-1)
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {1, 5, 8, ERA, 1},
                {2012, 9, 26, DAY_OF_WEEK, 1},
                {2012, 9, 26, DAY_OF_YEAR, 237}, // 8*30 + 2*31 - 5 = 240+62-5 = 297
                {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
                {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3},
                {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 34},
                {2015, 12, 37, DAY_OF_WEEK, 5}, // Leap day
                {2015, 12, 37, DAY_OF_MONTH, 37},
                {2015, 12, 37, DAY_OF_YEAR, 371},
                {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2},
                {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
                {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
                {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
                {2015, 12, 37, MONTH_OF_YEAR, 12},
                {2015, 12, 37, PROLEPTIC_MONTH, 24179} // (2015-1)*12 + (12-1)
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2}, getLong({3}) = {4}")
        @MethodSource("getLongProvider")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }

        @Test
        void getLong_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        static Object[][] withFieldProvider() {
            return new Object[][] {
                // {y, m, d, field, value, expectedY, expectedM, expectedD}
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, PROLEPTIC_MONTH, 24158, 2013, 3, 26}, // (2013-1)*12 + 3-1
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
                {2014, 5, 26, ERA, 1, 2014, 5, 26},
                {2015, 12, 37, YEAR, 2004, 2004, 12, 37}, // Leap to leap
                {2015, 12, 37, YEAR, 2013, 2013, 12, 30}, // Leap to non-leap
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withFieldProvider")
        void with_field_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).with(field, value));
        }

        static Object[][] withInvalidFieldProvider() {
            return new Object[][] {
                // {y, m, d, field, value}
                {2013, 1, 1, DAY_OF_MONTH, 31}, {2013, 6, 1, DAY_OF_MONTH, 31},
                {2015, 12, 1, DAY_OF_MONTH, 38}, {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372}, {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001}
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with({3}, {4}) throws")
        @MethodSource("withInvalidFieldProvider")
        void with_invalidFieldValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom).with(field, value));
        }

        static Object[][] lastDayOfMonthProvider() {
            return new Object[][] {
                // {y, m, d, expectedY, expectedM, expectedD}
                {2012, 1, 23, 2012, 1, 30}, {2012, 2, 23, 2012, 2, 31},
                {2009, 12, 23, 2009, 12, 37} // Leap year
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with(lastDayOfMonth) -> {3}-{4}-{5}")
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_adjustsCorrectly(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Object[][] plusMinusProvider() {
            return new Object[][] {
                // {y, m, d, amount, unit, expectedY, expectedM, expectedD}
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("plusMinusProvider")
        void plus_forUnits(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {5}-{6}-{7} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("plusMinusProvider")
        void minus_forUnits(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(y, m, d), Symmetry010Date.of(ey, em, ed).minus(amount, unit));
        }

        static Object[][] untilUnitProvider() {
            return new Object[][] {
                // {y1, m1, d1, y2, m2, d2, unit, expectedAmount}
                {2014, 5, 26, 2014, 6, 4, DAYS, 9},
                {2014, 5, 26, 2014, 6, 1, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0}
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("untilUnitProvider")
        void until_forUnits_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilPeriodProvider() {
            return new Object[][] {
                // {y1, m1, d1, y2, m2, d2, pY, pM, pD}
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
                {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 25, 0, 11, 29},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            };
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} until {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_forPeriod_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        void until_self_returnsZeroPeriod() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }
    }

    @Nested
    @DisplayName("Chronology-specific methods")
    class ChronologyTests {

        static Stream<Era> invalidEraProvider() {
            return Stream.of(
                CopticEra.AM, DiscordianEra.YOLD, EthiopicEra.INCARNATION, HijrahEra.AH,
                JapaneseEra.MEIJI, JapaneseEra.TAISHO, JapaneseEra.SHOWA, JapaneseEra.HEISEI,
                JulianEra.BC, MinguoEra.ROC, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest(name = "{index}: prolepticYear({0}, 4) throws")
        @MethodSource("invalidEraProvider")
        void prolepticYear_withInvalidEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("toString representation")
    class ToStringTests {

        static Object[][] toStringProvider() {
            return new Object[][] {
                {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
                {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
                {Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"},
                {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"}
            };
        }

        @ParameterizedTest(name = "{index}: {0}.toString() -> \"{1}\"")
        @MethodSource("toStringProvider")
        void toString_returnsCorrectFormat(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}