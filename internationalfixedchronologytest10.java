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
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link InternationalFixedDate} class.
 */
class InternationalFixedDateTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Object[][] sampleIFCAndISODates() {
        return new Object[][]{
                {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
                {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
                {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
                {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
                {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
                {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    static Object[][] invalidDateParts() {
        return new Object[][]{
                {-1, 13, 28}, {0, 1, 1}, {1900, 14, 1}, {1900, 1, 0},
                {1900, 1, 29}, // Month 1 has 28 days
                {1900, 6, 29}, // Month 6 has 28 days in a non-leap year
                {1900, 13, 30}, // Month 13 has 29 days
        };
    }

    static Object[][] invalidLeapDayYears() {
        return new Object[][]{{1}, {100}, {1900}};
    }

    static Object[][] dateAndExpectedMonthLength() {
        return new Object[][]{
                {1900, 1, 28}, {1900, 6, 28}, {1900, 13, 29},
                {1904, 6, 29}, // Leap year
        };
    }

    static Object[][] dateAndFieldAndExpectedValue() {
        return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 5},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                // Day of year for 2014-05-26: (4 full months * 28 days) + 26 days = 138
                {2014, 5, 26, DAY_OF_YEAR, 4 * 28 + 26},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                // Proleptic month is calculated as (year - 1) * 13 + (month - 1)
                {2014, 5, 26, PROLEPTIC_MONTH, (2014 - 1) * 13 + (5 - 1)},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                // Day of year for 2012-09-26 (leap year): (8 full months * 28 days) + 1 leap day + 26 days = 251
                {2012, 9, 26, DAY_OF_YEAR, 8 * 28 + 1 + 26},
                // Year Day is day 29 of month 13, and is outside the weekly cycle
                {2014, 13, 29, DAY_OF_WEEK, 0},
                {2014, 13, 29, DAY_OF_YEAR, 13 * 28 + 1},
                // Leap Day is day 29 of month 6, and is outside the weekly cycle
                {2012, 6, 29, DAY_OF_WEEK, 0},
                {2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1},
        };
    }

    static Object[][] dateAndFieldAndValueAndExpectedDate() {
        return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2012, 6, 29, YEAR, 2013, 2013, 6, 28}, // Adjusting year from a leap day to non-leap year
        };
    }

    static Object[][] dateArithmeticSamples() {
        return new Object[][]{
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 8, DAYS, 2014, 6, 6},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    static Object[][] dateArithmeticSamplesFromSpecialDays() {
        return new Object[][]{
                // From Year Day
                {2014, 13, 29, 8, DAYS, 2015, 1, 8},
                {2014, 13, 29, 3, WEEKS, 2015, 1, 21},
                {2014, 13, 29, 3, MONTHS, 2015, 3, 28},
                {2014, 13, 29, 3, YEARS, 2017, 13, 29},
                // From Leap Day
                {2012, 6, 29, 8, DAYS, 2012, 7, 8},
                {2012, 6, 29, 3, WEEKS, 2012, 7, 22},
                {2012, 6, 29, 3, MONTHS, 2012, 9, 28},
                {2012, 6, 29, 3, YEARS, 2015, 6, 28}, // Adding years from leap day results in non-leap day
                {2012, 6, 29, 4, YEARS, 2016, 6, 29}, // Adding 4 years lands on another leap day
        };
    }

    static Object[][] untilAsPeriodSamples() {
        return new Object[][]{
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                // Spanning a leap day
                {2011, 12, 28, 2012, 13, 1, 1, 0, 1},
                // From a leap day
                {2004, 6, 29, 2004, 13, 29, 0, 7, 0},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleIFCAndISODates")
        void from_ifcDate_shouldReturnEquivalentIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleIFCAndISODates")
        void from_isoDate_shouldReturnEquivalentIfcDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleIFCAndISODates")
        void toEpochDay_shouldReturnCorrectEpochDay(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleIFCAndISODates")
        void chronology_dateEpochDay_shouldReturnCorrectIfcDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidDateParts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidLeapDayYears")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            // Month 6, Day 29 is only valid in a leap year
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Field and Range Tests")
    class FieldAndRangeTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateAndFieldAndExpectedValue")
        void getLong_forVariousFields_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, dom);
            assertEquals(expected, date.getLong(field));
        }

        @Test
        void lengthOfMonth_shouldReturnCorrectLength() {
            assertEquals(28, InternationalFixedDate.of(1900, 1, 1).lengthOfMonth());
            assertEquals(28, InternationalFixedDate.of(1900, 6, 1).lengthOfMonth()); // Non-leap year
            assertEquals(29, InternationalFixedDate.of(1904, 6, 1).lengthOfMonth()); // Leap year
            assertEquals(29, InternationalFixedDate.of(1900, 13, 1).lengthOfMonth());
        }

        @Test
        void range_forDayOfMonth_shouldBeCorrectForMonthAndYear() {
            // Standard month
            assertEquals(ValueRange.of(1, 28), InternationalFixedDate.of(2011, 1, 1).range(DAY_OF_MONTH));
            // Month 6 in non-leap year
            assertEquals(ValueRange.of(1, 28), InternationalFixedDate.of(2011, 6, 1).range(DAY_OF_MONTH));
            // Month 6 in leap year
            assertEquals(ValueRange.of(1, 29), InternationalFixedDate.of(2012, 6, 1).range(DAY_OF_MONTH));
            // Month 13
            assertEquals(ValueRange.of(1, 29), InternationalFixedDate.of(2011, 13, 1).range(DAY_OF_MONTH));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateAndFieldAndValueAndExpectedDate")
        void with_validFieldValue_shouldReturnModifiedDate(int year, int month, int dom, TemporalField field, long value, int exYear, int exMonth, int exDom) {
            InternationalFixedDate baseDate = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expectedDate = InternationalFixedDate.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        void with_invalidFieldValue_shouldThrowException() {
            InternationalFixedDate date = InternationalFixedDate.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_MONTH, 29));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateArithmeticSamples")
        void plus_withVariousUnits_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateArithmeticSamplesFromSpecialDays")
        void plus_fromSpecialDays_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateArithmeticSamples")
        void minus_withVariousUnits_shouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#dateArithmeticSamplesFromSpecialDays")
        void minus_fromSpecialDays_shouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.minus(amount, unit));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            InternationalFixedDate date1 = InternationalFixedDate.of(2009, 6, 23);
            InternationalFixedDate expected1 = InternationalFixedDate.of(2009, 6, 28);
            assertEquals(expected1, date1.with(TemporalAdjusters.lastDayOfMonth()));

            InternationalFixedDate date2 = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate expected2 = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected2, date2.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Until Tests")
    class UntilTests {

        @Test
        void until_sameDate_returnsZeroPeriod() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 15);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), date.until(LocalDate.from(date)));
            assertEquals(Period.ZERO, LocalDate.from(date).until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilAsPeriodSamples")
        void until_otherDateAsPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        void until_otherDateInUnits_shouldReturnCorrectAmount() {
            InternationalFixedDate start = InternationalFixedDate.of(2014, 5, 26);
            InternationalFixedDate end = InternationalFixedDate.of(2015, 6, 27);
            assertEquals(394, start.until(end, DAYS));
            assertEquals(56, start.until(end, WEEKS));
            assertEquals(13, start.until(end, MONTHS));
            assertEquals(1, start.until(end, YEARS));
            assertEquals(0, start.until(end, DECADES));
            assertEquals(0, start.until(end, ERAS));
        }
    }

    @Nested
    @DisplayName("General Method Tests")
    class GeneralMethodTests {

        @Test
        void testToString() {
            assertEquals("Ifc CE 2012/06/23", InternationalFixedDate.of(2012, 6, 23).toString());
            assertEquals("Ifc CE 2012/06/29", InternationalFixedDate.of(2012, 6, 29).toString()); // Leap Day
            assertEquals("Ifc CE 2012/13/29", InternationalFixedDate.of(2012, 13, 29).toString()); // Year Day
        }

        @Test
        void equals_and_hashCode_shouldAdhereToContract() {
            new EqualsTester()
                    .addEqualityGroup(
                            InternationalFixedDate.of(2014, 5, 26),
                            InternationalFixedDate.of(2014, 5, 26))
                    .addEqualityGroup(InternationalFixedDate.of(2014, 5, 27))
                    .addEqualityGroup(InternationalFixedDate.of(2014, 6, 26))
                    .addEqualityGroup(InternationalFixedDate.of(2015, 5, 26))
                    .testEquals();
        }
    }

    @Nested
    @DisplayName("Chronology-specific Tests")
    class ChronologyTests {
        @Test
        void prolepticYear_withInvalidEra_shouldThrowException() {
            assertThrows(ClassCastException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        }

        @Test
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidYearOfEra_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }
    }
}