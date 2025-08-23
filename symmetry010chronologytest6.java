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

import com.google.common.testing.EqualsTester;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideSampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)), // Constantine the Great (d. 337)
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)), // Charlemagne (d. 814)
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)), // Battle of Hastings
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)), // Petrarch (d. 1374)
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)), // Charles the Bold (d. 1477)
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)), // Leonardo da Vinci (d. 1519)
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)), // Columbus's landfall
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)), // Galileo Galilei (d. 1642)
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)), // William Shakespeare (d. 1616)
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)), // Sir Isaac Newton (d. 1727)
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)), // Leonhard Euler (d. 1783)
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)), // Storming of the Bastille
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)), // Albert Einstein (d. 1955)
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)), // Dennis Ritchie (d. 2011)
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)), // Unix time epoch
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)) // Start of 21st century
        );
    }

    //-----------------------------------------------------------------------
    // Conversion and Epoch Day Tests
    //-----------------------------------------------------------------------
    @Nested
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void fromSymmetryDate_shouldReturnEquivalentIsoDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void fromIsoDate_shouldReturnEquivalentSymmetryDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void dateEpochDay_shouldCreateCorrectDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void chronologyDate_fromTemporalAccessor_shouldCreateCorrectDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Invalid Date Creation Tests
    //-----------------------------------------------------------------------
    @Nested
    class InvalidDateCreationTests {
        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1),
                Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1),
                Arguments.of(2000, 1, 31), Arguments.of(2000, 2, 32), Arguments.of(2000, 3, 31),
                Arguments.of(2000, 4, 31), Arguments.of(2000, 5, 32), Arguments.of(2000, 6, 31),
                Arguments.of(2000, 7, 31), Arguments.of(2000, 8, 32), Arguments.of(2000, 9, 31),
                Arguments.of(2000, 10, 31), Arguments.of(2000, 11, 32), Arguments.of(2000, 12, 31),
                Arguments.of(2004, 12, 38)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        void of_forInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> provideYearsWithInvalidLeapDay() {
            return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
        }

        @ParameterizedTest
        @MethodSource("provideYearsWithInvalidLeapDay")
        void of_forInvalidLeapDay_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    //-----------------------------------------------------------------------
    // Date Property Tests
    //-----------------------------------------------------------------------
    @Nested
    class DatePropertyTests {
        static Stream<Arguments> provideDateAndExpectedLengthOfMonth() {
            return Stream.of(
                Arguments.of(2000, 1, 30), Arguments.of(2000, 2, 31), Arguments.of(2000, 3, 30),
                Arguments.of(2000, 4, 30), Arguments.of(2000, 5, 31), Arguments.of(2000, 6, 30),
                Arguments.of(2000, 7, 30), Arguments.of(2000, 8, 31), Arguments.of(2000, 9, 30),
                Arguments.of(2000, 10, 30), Arguments.of(2000, 11, 31), Arguments.of(2000, 12, 30),
                Arguments.of(2004, 12, 37) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedLengthOfMonth")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndFieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                Arguments.of(2012, 1, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Note: 31 days is 4 weeks + 3 days
                Arguments.of(2015, 12, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // Leap year, Dec has 37 days
                Arguments.of(2012, 1, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldRanges")
        void range_forField_shouldReturnCorrectRange(int year, int month, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, 1).range(field));
        }

        static Stream<Arguments> provideDateAndExpectedFieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                // Day of year for 2014-05-26: Jan(30) + Feb(31) + Mar(30) + Apr(30) + 26
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                // Week of year for 2014-05-26: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 4 + 1 + 4 + 4 + 4), // Corrected calculation
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                // Leap year date
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5),
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37),
                // Day of year for 2015-12-37: 364 (normal year) + 7 (leap week)
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 364 + 7),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedFieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, day).getLong(field));
        }

        static Stream<Era> provideUnsupportedErasForProlepticYear() {
            return Stream.of(
                HijrahEra.AH, JapaneseEra.MEIJI, MinguoEra.ROC, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedErasForProlepticYear")
        void prolepticYear_forUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        void dateYearDay_shouldCreateCorrectDateForConsecutiveYears() {
            for (int year = 1; year < 200; year++) {
                Symmetry010Date base = Symmetry010Chronology.INSTANCE.dateYearDay(year, 1);
                assertEquals(year, base.get(YEAR));
                assertEquals(IsoEra.CE, base.getEra());
                assertEquals(year, base.get(YEAR_OF_ERA));

                Symmetry010Date eraBased = Symmetry010Chronology.INSTANCE.dateYearDay(IsoEra.CE, year, 1);
                assertEquals(base, eraBased);
            }
        }
    }

    //-----------------------------------------------------------------------
    // Date Modification Tests
    //-----------------------------------------------------------------------
    @Nested
    class DateModificationTests {
        @Test
        void plusDays_shouldCorrectlyAddDays() {
            Symmetry010Date symDate = Symmetry010Date.of(1970, 1, 4); // Corresponds to 1970-01-01 ISO
            LocalDate isoDate = LocalDate.of(1970, 1, 1);

            assertEquals(isoDate, LocalDate.from(symDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symDate.plus(-60, DAYS)));
        }

        @Test
        void minusDays_shouldCorrectlySubtractDays() {
            Symmetry010Date symDate = Symmetry010Date.of(1970, 1, 4); // Corresponds to 1970-01-01 ISO
            LocalDate isoDate = LocalDate.of(1970, 1, 1);

            assertEquals(isoDate, LocalDate.from(symDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symDate.minus(-60, DAYS)));
        }

        static Stream<Arguments> provideDateAndAdjusterResult() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 30),
                Arguments.of(2012, 2, 23, 2012, 2, 31),
                Arguments.of(2009, 12, 23, 2009, 12, 37) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndAdjusterResult")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date date = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> provideWithFieldAndValue() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37) // Leap day to another leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithFieldAndValue")
        void with_fieldAndValue_shouldReturnModifiedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(field, value));
        }
    }

    //-----------------------------------------------------------------------
    // Period and Duration Tests
    //-----------------------------------------------------------------------
    @Nested
    class PeriodAndDurationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void until_self_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        void isoUntil_equivalentSymmetryDate_shouldReturnZeroPeriod(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(sym010Date));
        }

        @Test
        void until_withDaysUnit_shouldReturnCorrectDayCount() {
            Symmetry010Date symDate = Symmetry010Date.of(1970, 1, 4); // Corresponds to 1970-01-01 ISO
            LocalDate isoDate = LocalDate.of(1970, 1, 1);

            assertEquals(0, symDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, symDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, symDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, symDate.until(isoDate.minusDays(40), DAYS));
        }

        static Stream<Arguments> provideUntilPeriodData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodData")
        void until_asPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // General Method Tests (toString, equals, hashCode)
    //-----------------------------------------------------------------------
    @Nested
    class GeneralMethodTests {
        static Stream<Arguments> provideDatesForToString() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                Arguments.of(2000, 8, 31, "Sym010 CE 2000/08/31"),
                Arguments.of(2009, 12, 37, "Sym010 CE 2009/12/37")
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesForToString")
        void toString_shouldReturnCorrectFormat(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void getChronology_shouldReturnSymmetry010ChronologyInstance() {
            Symmetry010Date date = Symmetry010Date.of(2000, 1, 1);
            assertEquals(Symmetry010Chronology.INSTANCE, date.getChronology());
        }
    }

    @Nested
    class EqualsAndHashCodeTest {
        @Test
        void equalsAndHashCode_shouldFollowContract() {
            Symmetry010Date date_2014_5_26 = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date date_2014_5_27 = Symmetry010Date.of(2014, 5, 27);
            Symmetry010Date date_2014_6_26 = Symmetry010Date.of(2014, 6, 26);
            Symmetry010Date date_2015_5_26 = Symmetry010Date.of(2015, 5, 26);

            new EqualsTester()
                .addEqualityGroup(date_2014_5_26, Symmetry010Date.of(2014, 5, 26))
                .addEqualityGroup(date_2014_5_27)
                .addEqualityGroup(date_2014_6_26)
                .addEqualityGroup(date_2015_5_26)
                .addEqualityGroup(LocalDate.of(2014, 5, 26)) // Different class
                .testEquals();
        }
    }
}