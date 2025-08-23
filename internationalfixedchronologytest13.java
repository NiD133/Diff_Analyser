package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
public class InternationalFixedChronologyTest {

    // Provides pairs of InternationalFixedDate and their equivalent ISO LocalDate
    static Stream<Arguments> sampleFixedAndIsoDates() {
        return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
                Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
                Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
                Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)),   // Leap Day
                Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(InternationalFixedDate.of(2012, 13, 29), LocalDate.of(2012, 12, 31))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("converts from InternationalFixedDate to ISO LocalDate")
        void test_toIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("converts from ISO LocalDate to InternationalFixedDate")
        void test_fromIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("creates date from epoch day")
        void test_dateFromEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("converts date to epoch day")
        void test_dateToEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("creates date from a temporal accessor")
        void test_dateFromTemporal(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation")
    class InvalidDateCreationTests {

        @ParameterizedTest
        @CsvSource({"0, 1, 1", "-1, 1, 1"})
        @DisplayName("throws exception for year less than 1")
        void test_of_invalidYear(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest
        @CsvSource({"2023, 0, 1", "2023, 14, 1"})
        @DisplayName("throws exception for invalid month")
        void test_of_invalidMonth(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest
        @CsvSource({
                "2023, 1, 29",  // Standard month has 28 days
                "2023, 13, 30", // Year-day month has 29 days
                "2023, 6, 29",  // Leap-day month has 28 days in non-leap year
                "2024, 6, 30"   // Leap-day month has 29 days in leap year
        })
        @DisplayName("throws exception for invalid day of month")
        void test_of_invalidDayOfMonth(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest
        @ValueSource(ints = {1900, 2001, 2002, 2003})
        @DisplayName("throws exception for leap day in non-leap year")
        void test_of_leapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertyTests {

        @Test
        @DisplayName("length of month is correct for standard months")
        void test_lengthOfMonth_standard() {
            assertEquals(28, InternationalFixedDate.of(2023, 1, 1).lengthOfMonth());
        }

        @Test
        @DisplayName("length of month is correct for leap-day month")
        void test_lengthOfMonth_leapDayMonth() {
            assertEquals(28, InternationalFixedDate.of(2023, 6, 1).lengthOfMonth(), "Non-leap year");
            assertEquals(29, InternationalFixedDate.of(2024, 6, 1).lengthOfMonth(), "Leap year");
        }

        @Test
        @DisplayName("length of month is correct for year-day month")
        void test_lengthOfMonth_yearDayMonth() {
            assertEquals(29, InternationalFixedDate.of(2023, 13, 1).lengthOfMonth());
            assertEquals(29, InternationalFixedDate.of(2024, 13, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {

        @Test
        @DisplayName("getLong returns correct value for various fields")
        void test_getLong() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 5, 26);
            assertEquals(5, date.getLong(DAY_OF_WEEK));
            assertEquals(26, date.getLong(DAY_OF_MONTH));
            assertEquals(138, date.getLong(DAY_OF_YEAR)); // 4 * 28 + 26
            assertEquals(4, date.getLong(ALIGNED_WEEK_OF_MONTH));
            assertEquals(20, date.getLong(ALIGNED_WEEK_OF_YEAR));
            assertEquals(5, date.getLong(MONTH_OF_YEAR));
            assertEquals(26186, date.getLong(PROLEPTIC_MONTH)); // 2014 * 13 + 5 - 1
            assertEquals(2014, date.getLong(YEAR));
            assertEquals(1, date.getLong(ERA));
        }

        @Test
        @DisplayName("getLong handles leap day correctly")
        void test_getLong_leapDay() {
            InternationalFixedDate leapDay = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(0, leapDay.getLong(DAY_OF_WEEK)); // Not part of a week
            assertEquals(29, leapDay.getLong(DAY_OF_MONTH));
            assertEquals(169, leapDay.getLong(DAY_OF_YEAR)); // 6 * 28 + 1
            assertEquals(6, leapDay.getLong(MONTH_OF_YEAR));
        }

        @Test
        @DisplayName("getLong handles year day correctly")
        void test_getLong_yearDay() {
            InternationalFixedDate yearDay = InternationalFixedDate.of(2014, 13, 29);
            assertEquals(0, yearDay.getLong(DAY_OF_WEEK)); // Not part of a week
            assertEquals(29, yearDay.getLong(DAY_OF_MONTH));
            assertEquals(365, yearDay.getLong(DAY_OF_YEAR)); // 13 * 28 + 1
            assertEquals(13, yearDay.getLong(MONTH_OF_YEAR));
        }

        @Test
        @DisplayName("range returns correct value range for fields")
        void test_range() {
            InternationalFixedDate date = InternationalFixedDate.of(2011, 1, 1); // Non-leap year
            InternationalFixedDate leapDate = InternationalFixedDate.of(2012, 1, 1); // Leap year

            assertEquals(ValueRange.of(1, 28), date.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 29), leapDate.withMonth(6).range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 365), date.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 366), leapDate.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 13), date.range(MONTH_OF_YEAR));
        }
    }

    @Nested
    @DisplayName("Date Modification")
    class ModificationTests {

        @Test
        @DisplayName("with() adjusts fields correctly")
        void test_with() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 5, 26);
            assertEquals(InternationalFixedDate.of(2014, 5, 10), date.with(DAY_OF_MONTH, 10));
            assertEquals(InternationalFixedDate.of(2014, 8, 26), date.with(MONTH_OF_YEAR, 8));
            assertEquals(InternationalFixedDate.of(2012, 5, 26), date.with(YEAR, 2012));
        }

        @Test
        @DisplayName("with() handles moving from leap day to non-leap year")
        void test_with_fromLeapDayToNonLeapYear() {
            InternationalFixedDate leapDay = InternationalFixedDate.of(2012, 6, 29);
            // Adjusting year to a non-leap year resolves to the 28th
            assertEquals(InternationalFixedDate.of(2013, 6, 28), leapDay.with(YEAR, 2013));
        }

        @Test
        @DisplayName("with() throws exception for invalid values")
        void test_with_invalidValue() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(DAY_OF_MONTH, 29));
            assertThrows(DateTimeException.class, () -> date.with(MONTH_OF_YEAR, 14));
        }

        @Test
        @DisplayName("plus() adds amounts correctly")
        void test_plus() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 5, 26);
            assertEquals(InternationalFixedDate.of(2014, 6, 6), date.plus(8, DAYS));
            assertEquals(InternationalFixedDate.of(2014, 6, 19), date.plus(3, WEEKS));
            assertEquals(InternationalFixedDate.of(2014, 8, 26), date.plus(3, MONTHS));
            assertEquals(InternationalFixedDate.of(2017, 5, 26), date.plus(3, YEARS));
            assertEquals(InternationalFixedDate.of(2044, 5, 26), date.plus(3, DECADES));
        }

        @Test
        @DisplayName("minus() subtracts amounts correctly")
        void test_minus() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 5, 26);
            assertEquals(InternationalFixedDate.of(2014, 5, 23), date.minus(3, DAYS));
            assertEquals(InternationalFixedDate.of(2014, 4, 19), date.minus(5, WEEKS));
            assertEquals(InternationalFixedDate.of(2013, 13, 26), date.minus(5, MONTHS));
            assertEquals(InternationalFixedDate.of(2009, 5, 26), date.minus(5, YEARS));
            assertEquals(InternationalFixedDate.of(1964, 5, 26), date.minus(5, DECADES));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) works correctly")
        void test_with_adjuster() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate lastDay = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(lastDay, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period Calculation")
    class PeriodTests {

        @ParameterizedTest
        @CsvSource({
                "2014, 5, 26, 2014, 6, 4, DAYS, 6",
                "2014, 5, 26, 2014, 6, 5, WEEKS, 1",
                "2014, 5, 26, 2014, 6, 26, MONTHS, 1",
                "2014, 5, 26, 2015, 5, 26, YEARS, 1",
                "2014, 5, 26, 2024, 5, 26, DECADES, 1"
        })
        @DisplayName("until() calculates duration in single units")
        void test_until_singleUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        @DisplayName("until() calculates a period with years, months, and days")
        void test_until_period() {
            InternationalFixedDate start = InternationalFixedDate.of(2014, 5, 26);
            InternationalFixedDate end = InternationalFixedDate.of(2015, 8, 4);
            ChronoPeriod period = start.until(end);
            assertEquals(InternationalFixedChronology.INSTANCE.period(1, 2, 6), period);
        }

        @Test
        @DisplayName("until() handles identity cases")
        void test_until_identity() {
            InternationalFixedDate date = InternationalFixedDate.of(2023, 1, 1);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(Period.ZERO, LocalDate.from(date).until(date));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @Test
        @DisplayName("eras() returns the correct list of eras")
        void test_eras() {
            List<java.time.chrono.Era> eras = InternationalFixedChronology.INSTANCE.eras();
            assertEquals(1, eras.size());
            assertEquals(InternationalFixedEra.CE, eras.get(0));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0, 2})
        @DisplayName("eraOf() throws exception for invalid era value")
        void test_eraOf_invalid(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest
        @ValueSource(ints = {-10, -1, 0})
        @DisplayName("prolepticYear() throws exception for year less than 1")
        void test_prolepticYear_invalid(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("Object Methods")
    class ObjectMethodTests {

        @ParameterizedTest
        @CsvSource({
                "1, 1, 1, 'Ifc CE 1/01/01'",
                "2012, 6, 23, 'Ifc CE 2012/06/23'",
                "2012, 6, 29, 'Ifc CE 2012/06/29'", // Leap Day
                "2012, 13, 29, 'Ifc CE 2012/13/29'" // Year Day
        })
        @DisplayName("toString() returns correct format")
        void test_toString(int year, int month, int day, String expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, day).toString());
        }

        @Test
        @DisplayName("equals() and hashCode() contract")
        void test_equals_hashCode() {
            new EqualsTester()
                    .addEqualityGroup(
                            InternationalFixedDate.of(2014, 1, 1),
                            InternationalFixedDate.of(2014, 1, 1))
                    .addEqualityGroup(
                            InternationalFixedDate.of(2014, 1, 2))
                    .addEqualityGroup(
                            InternationalFixedDate.of(2014, 2, 1))
                    .addEqualityGroup(
                            InternationalFixedDate.of(2015, 1, 1))
                    .addEqualityGroup(
                            InternationalFixedDate.of(2012, 6, 29)) // Leap Day
                    .addEqualityGroup(
                            InternationalFixedDate.of(2014, 13, 29)) // Year Day
                    .testEquals();
        }
    }
}