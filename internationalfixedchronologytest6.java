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
import static java.time.temporal.ChunioUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleFixedAndIsoDates() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // leap year
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    static Object[][] invalidDateParts() {
        // year, month, dayOfMonth
        return new Object[][] {
            {-1, 13, 28}, {0, 1, 1},
            {1900, 14, 1}, {1900, 0, 1},
            {1900, 1, 29}, {1900, 2, 29},
            {1900, 13, 30},
        };
    }

    static Object[][] nonLeapYears() {
        // year
        return new Object[][] {{1}, {100}, {1900}};
    }

    static Object[][] monthLengths() {
        // year, month, dayOfMonth, expectedLength
        return new Object[][] {
            {1900, 1, 28, 28},
            {1900, 6, 28, 28},
            {1900, 13, 29, 29},
            {1904, 6, 29, 29}, // leap year
        };
    }

    static Object[][] fieldRanges() {
        // year, month, dayOfMonth, field, expectedRange
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // leap month
            {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // year-day month
            {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)}, // leap day
        };
    }

    static Object[][] fieldValues() {
        // year, month, dayOfMonth, field, expectedValue
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 4 * 28 + 26}, // 4 months of 28 days + 26
            {2012, 9, 26, DAY_OF_YEAR, 8 * 28 + 1 + 26}, // 8 months of 28 days + leap day + 26
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {2014, 13, 29, DAY_OF_WEEK, 0}, // year day
            {2012, 6, 29, DAY_OF_WEEK, 0}, // leap day
        };
    }

    static Object[][] withFieldValueCases() {
        // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29},
            {2012, 5, 26, DAY_OF_YEAR, 366, 2012, 13, 29},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2012, 6, 29, YEAR, 2013, 2013, 6, 28}, // adjusting year from a leap day
        };
    }

    static Object[][] withInvalidFieldValueCases() {
        // year, month, dom, field, value
        return new Object[][] {
            {2013, 1, 1, DAY_OF_MONTH, 29},
            {2013, 6, 1, DAY_OF_MONTH, 29}, // not a leap year
            {2012, 6, 1, DAY_OF_MONTH, 30},
            {2013, 1, 1, DAY_OF_YEAR, 366}, // not a leap year
            {2012, 1, 1, DAY_OF_YEAR, 367},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, YEAR, 0},
        };
    }

    static Object[][] lastDayOfMonthAdjustmentCases() {
        // year, month, day, expectedYear, expectedMonth, expectedDay
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29},
            {2009, 6, 23, 2009, 6, 28},
            {2007, 13, 23, 2007, 13, 29},
        };
    }

    static Object[][] plusCases() {
        // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    static Object[][] plusOnSpecialDaysCases() {
        // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
        return new Object[][] {
            {2014, 13, 29, 8, DAYS, 2015, 1, 8}, // from year day
            {2012, 6, 29, 8, DAYS, 2012, 7, 8}, // from leap day
            {2014, 13, 29, 3, MONTHS, 2015, 3, 28},
            {2012, 6, 29, 3, MONTHS, 2012, 9, 28},
            {2012, 6, 29, 4, YEARS, 2016, 6, 29}, // leap to leap
            {2012, 6, 29, 3, YEARS, 2015, 6, 28}, // leap to non-leap
        };
    }

    static Object[][] untilInUnitsCases() {
        // y1, m1, d1, y2, m2, d2, unit, expectedAmount
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 4, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            {2012, 6, 29, 2012, 13, 29, DAYS, 197}, // leap day to year day
        };
    }

    static Object[][] untilAsPeriodCases() {
        // y1, m1, d1, y2, m2, d2, expectedYears, expectedMonths, expectedDays
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2012, 6, 29, 2016, 6, 29, 4, 0, 0}, // leap day to leap day
            {2004, 6, 29, 2004, 13, 29, 0, 7, 0}, // leap day to year day in same year
        };
    }

    static Object[][] toStringCases() {
        // date, expectedString
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // leap day
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"}, // year day
        };
    }

    @Nested
    @DisplayName("Date Creation")
    class DateCreationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateParts")
        @DisplayName("of() with invalid parts should throw exception")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYears")
        @DisplayName("of() with leap day in non-leap year should throw exception")
        void of_withLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Conversion")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("LocalDate.from(fixedDate) should return correct ISO date")
        void fromFixedDate_shouldReturnCorrectIsoDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("InternationalFixedDate.from(isoDate) should return correct fixed date")
        void fromIsoDate_shouldReturnCorrectFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("toEpochDay() should match ISO date's epoch day")
        void toEpochDay_shouldMatchIsoDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("chronology.date(isoDate) should return correct fixed date")
        void chronologyDateFromTemporal_shouldReturnCorrectFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("chronology.dateEpochDay() should return correct fixed date")
        void chronologyDateFromEpochDay_shouldReturnCorrectFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class PropertyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#monthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int length) {
            assertEquals(length, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldRanges")
        @DisplayName("range() for a field should return correct value range")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldValues")
        @DisplayName("getLong() for a field should return correct value")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class DateArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withFieldValueCases")
        @DisplayName("with(field, value) should return adjusted date")
        void with_fieldValue_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withInvalidFieldValueCases")
        @DisplayName("with(field, value) with invalid value should throw exception")
        void with_invalidFieldValue_shouldThrowException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusCases")
        @DisplayName("plus(amount, unit) should return added date")
        void plus_shouldReturnAddedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(InternationalFixedDate.of(ey, em, ed), InternationalFixedDate.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusCases")
        @DisplayName("minus(amount, unit) should return subtracted date")
        void minus_shouldReturnSubtractedDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            assertEquals(InternationalFixedDate.of(ey, em, ed), InternationalFixedDate.of(y, m, d).minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusOnSpecialDaysCases")
        @DisplayName("plus() on special days should work correctly")
        void plus_onSpecialDays_shouldWorkCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(InternationalFixedDate.of(ey, em, ed), InternationalFixedDate.of(y, m, d).plus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Date Adjusters")
    class AdjusterTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#lastDayOfMonthAdjustmentCases")
        @DisplayName("with(lastDayOfMonth) should return correct date")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            InternationalFixedDate base = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period Calculation")
    class PeriodCalculationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("until(self) should return zero period")
        void until_self_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("until(equivalentIsoDate) should return zero period")
        void until_equivalentIsoDate_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("isoDate.until(equivalentFixedDate) should return zero period")
        void isoDate_until_equivalentFixedDate_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilInUnitsCases")
        @DisplayName("until(endDate, unit) should return correct amount")
        void until_temporalUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilAsPeriodCases")
        @DisplayName("until(endDate) should return correct period")
        void until_chronoPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {
        @Test
        @DisplayName("eraOf(1) should return CE era")
        void eraOf_validValue_shouldReturnEra() {
            Era era = InternationalFixedChronology.INSTANCE.eraOf(1);
            assertNotNull(era);
            assertEquals(1, era.getValue());
        }

        @Test
        @DisplayName("eraOf() with invalid value should throw exception")
        void eraOf_invalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        @DisplayName("prolepticYear() with invalid year for era should throw exception")
        void prolepticYear_invalidYearForEra_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#toStringCases")
        @DisplayName("toString() should return correct representation")
        void toString_shouldReturnCorrectRepresentation(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        @DisplayName("equals() and hashCode() should follow contract")
        void equalsAndHashCode_shouldFollowContract() {
            InternationalFixedDate date1 = InternationalFixedDate.of(2014, 5, 26);
            InternationalFixedDate date2 = InternationalFixedDate.of(2014, 5, 26);
            InternationalFixedDate date3 = InternationalFixedDate.of(2014, 5, 27);

            // equals
            assertEquals(date1, date1);
            assertEquals(date1, date2);
            assertEquals(date2, date1);
            assertNotEquals(date1, date3);
            assertNotEquals(date2, date3);
            assertNotEquals(date1, null);
            assertNotEquals(date1, "String");

            // hashCode
            assertEquals(date1.hashCode(), date2.hashCode());
        }
    }
}