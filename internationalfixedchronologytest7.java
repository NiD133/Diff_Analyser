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

@DisplayName("InternationalFixedDate Tests")
class InternationalFixedDateTest {

    private static InternationalFixedDate ifcDate(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    // --- Data Providers ---

    static Stream<Arguments> sampleDateMappings() {
        return Stream.of(
            Arguments.of(ifcDate(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(ifcDate(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(ifcDate(1, 13, 29), LocalDate.of(1, 12, 31)),
            Arguments.of(ifcDate(4, 6, 29), LocalDate.of(4, 6, 17)),
            Arguments.of(ifcDate(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(ifcDate(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    // --- Test Classes ---

    @Nested
    @DisplayName("Date Creation and Validation")
    class CreationAndValidationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidDateProvider")
        @DisplayName("of() should throw exception for invalid date components")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidLeapDayProvider")
        @DisplayName("of() should throw exception for leap day in a non-leap year")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return correct month length")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, ifcDate(year, month, day).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Conversions")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("LocalDate.from(ifcDate) should perform correct conversion")
        void fromInternationalFixedDate_shouldConvertToCorrectLocalDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("InternationalFixedDate.from(isoDate) should perform correct conversion")
        void fromLocalDate_shouldConvertToCorrectInternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("chronology.date(temporal) should create correct date")
        void chronologyDateFromTemporal_shouldCreateCorrectDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Epoch Day Operations")
    class EpochDayTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("toEpochDay() should return correct epoch day")
        void toEpochDay_shouldReturnCorrectValue(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("chronology.dateEpochDay() should create correct date")
        void chronologyDateFromEpochDay_shouldCreateCorrectDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {

        @ParameterizedTest(name = "{index}: getLong({1}) on {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#getLongProvider")
        @DisplayName("getLong() should return correct field value")
        void getLong_forVariousFields_shouldReturnCorrectValue(InternationalFixedDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        @ParameterizedTest(name = "{index}: range({1}) on {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#rangeProvider")
        @DisplayName("range() should return correct field range")
        void range_forVariousFields_shouldReturnCorrectValueRange(InternationalFixedDate date, TemporalField field, ValueRange expected) {
            assertEquals(expected, date.range(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation with with()")
    class WithModifierTests {

        @ParameterizedTest(name = "{index}: {0}.with({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withProvider")
        @DisplayName("with() should return correctly modified date")
        void with_fieldAndValue_shouldReturnModifiedDate(InternationalFixedDate baseDate, TemporalField field, long value, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @ParameterizedTest(name = "{index}: {0}.with({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withInvalidValueProvider")
        @DisplayName("with() should throw exception for invalid field value")
        void with_invalidFieldValue_shouldThrowException(InternationalFixedDate date, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#lastDayOfMonthProvider")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return last day of the month")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(InternationalFixedDate date, InternationalFixedDate expected) {
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic with plus() and minus()")
    class ArithmeticTests {

        @ParameterizedTest(name = "{index}: {0}.plus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusProvider")
        @DisplayName("plus() should return correctly added date")
        void plus_withVariousUnits_shouldAddCorrectly(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {0}.minus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#minusProvider")
        @DisplayName("minus() should return correctly subtracted date")
        void minus_withVariousUnits_shouldSubtractCorrectly(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Duration Calculation with until()")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDateMappings")
        @DisplayName("until() between a date and its equivalent should return zero")
        void until_equivalentDates_shouldReturnZero(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        @ParameterizedTest(name = "{index}: {0}.until({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilUnitProvider")
        @DisplayName("until() should calculate correct duration in a given unit")
        void until_withVariousUnits_shouldCalculateCorrectDuration(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest(name = "{index}: {0}.until({1})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilPeriodProvider")
        @DisplayName("until() should calculate correct period")
        void until_asChronoPeriod_shouldCalculateCorrectPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific API")
    class ChronologyApiTests {

        @Test
        @DisplayName("era and yearOfEra should be consistent")
        void eraAndYearOfEra_shouldBeConsistentForVariousYears() {
            for (int year = 1; year < 200; year++) {
                InternationalFixedDate base = ifcDate(year, 1, 1);
                assertEquals(year, base.get(YEAR));
                assertEquals(InternationalFixedEra.CE, base.getEra());
                assertEquals(year, base.get(YEAR_OF_ERA));
                assertEquals(base, InternationalFixedChronology.INSTANCE.date(InternationalFixedEra.CE, year, 1, 1));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidEraProvider")
        @DisplayName("eraOf() should throw exception for invalid era value")
        void eraOf_withInvalidValue_shouldThrowException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidProlepticYearProvider")
        @DisplayName("prolepticYear() should throw exception for invalid year")
        void prolepticYear_withInvalidYearForEra_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#toStringProvider")
        @DisplayName("toString() should return correct format")
        void toString_shouldReturnCorrectStringRepresentation(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    // --- Data Providers for Tests ---

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(0, 1, 1), Arguments.of(1900, 14, 1),
            Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 29), Arguments.of(1900, 13, 30)
        );
    }

    static Stream<Arguments> invalidLeapDayProvider() {
        return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(1900));
    }



    static Stream<Arguments> lengthOfMonthProvider() {
        return Stream.of(
            Arguments.of(1900, 1, 28, 28), Arguments.of(1900, 6, 28, 28),
            Arguments.of(1900, 13, 29, 29), Arguments.of(1904, 6, 29, 29)
        );
    }

    static Stream<Arguments> getLongProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 5, 26), DAY_OF_WEEK, 5),
            Arguments.of(ifcDate(2014, 5, 26), DAY_OF_MONTH, 26),
            Arguments.of(ifcDate(2014, 5, 26), DAY_OF_YEAR, 28 * 4 + 26),
            Arguments.of(ifcDate(2014, 5, 26), MONTH_OF_YEAR, 5),
            Arguments.of(ifcDate(2014, 5, 26), YEAR, 2014),
            Arguments.of(ifcDate(2012, 6, 29), DAY_OF_WEEK, 0), // Leap Day
            Arguments.of(ifcDate(2012, 6, 29), DAY_OF_MONTH, 29),
            Arguments.of(ifcDate(2012, 6, 29), DAY_OF_YEAR, 6 * 28 + 1),
            Arguments.of(ifcDate(2014, 13, 29), DAY_OF_WEEK, 0), // Year Day
            Arguments.of(ifcDate(2014, 13, 29), DAY_OF_MONTH, 29),
            Arguments.of(ifcDate(2014, 13, 29), DAY_OF_YEAR, 13 * 28 + 1)
        );
    }

    static Stream<Arguments> rangeProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)),
            Arguments.of(ifcDate(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366)),
            Arguments.of(ifcDate(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
            Arguments.of(ifcDate(2012, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 29)),
            Arguments.of(ifcDate(2012, 13, 23), DAY_OF_MONTH, ValueRange.of(1, 29))
        );
    }

    static Stream<Arguments> withProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 5, 26), DAY_OF_WEEK, 1, ifcDate(2014, 5, 22)),
            Arguments.of(ifcDate(2014, 5, 26), DAY_OF_MONTH, 28, ifcDate(2014, 5, 28)),
            Arguments.of(ifcDate(2014, 5, 26), MONTH_OF_YEAR, 4, ifcDate(2014, 4, 26)),
            Arguments.of(ifcDate(2014, 5, 26), YEAR, 2012, ifcDate(2012, 5, 26)),
            Arguments.of(ifcDate(2012, 6, 29), YEAR, 2013, ifcDate(2013, 6, 28)) // Leap day to non-leap year
        );
    }

    static Stream<Arguments> withInvalidValueProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2013, 1, 1), DAY_OF_MONTH, 29),
            Arguments.of(ifcDate(2013, 6, 1), DAY_OF_MONTH, 29),
            Arguments.of(ifcDate(2012, 6, 1), DAY_OF_MONTH, 30),
            Arguments.of(ifcDate(2013, 1, 1), MONTH_OF_YEAR, 14),
            Arguments.of(ifcDate(2013, 1, 1), DAY_OF_YEAR, 366)
        );
    }

    static Stream<Arguments> lastDayOfMonthProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2012, 6, 23), ifcDate(2012, 6, 29)),
            Arguments.of(ifcDate(2009, 6, 23), ifcDate(2009, 6, 28)),
            Arguments.of(ifcDate(2007, 13, 23), ifcDate(2007, 13, 29))
        );
    }

    static Stream<Arguments> plusProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 5, 26), 8, DAYS, ifcDate(2014, 6, 6)),
            Arguments.of(ifcDate(2014, 5, 26), 3, WEEKS, ifcDate(2014, 6, 19)),
            Arguments.of(ifcDate(2014, 5, 26), 3, MONTHS, ifcDate(2014, 8, 26)),
            Arguments.of(ifcDate(2014, 5, 26), 3, YEARS, ifcDate(2017, 5, 26)),
            // Special days
            Arguments.of(ifcDate(2012, 6, 29), 8, DAYS, ifcDate(2012, 7, 8)), // from leap day
            Arguments.of(ifcDate(2014, 13, 29), 8, DAYS, ifcDate(2015, 1, 8)), // from year day
            Arguments.of(ifcDate(2012, 6, 29), 3, YEARS, ifcDate(2015, 6, 28)) // leap day plus years
        );
    }

    static Stream<Arguments> minusProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 6, 6), 8, DAYS, ifcDate(2014, 5, 26)),
            Arguments.of(ifcDate(2014, 6, 19), 3, WEEKS, ifcDate(2014, 5, 26)),
            Arguments.of(ifcDate(2014, 8, 26), 3, MONTHS, ifcDate(2014, 5, 26)),
            Arguments.of(ifcDate(2017, 5, 26), 3, YEARS, ifcDate(2014, 5, 26)),
            // Special days
            Arguments.of(ifcDate(2012, 7, 8), 8, DAYS, ifcDate(2012, 6, 29)), // to leap day
            Arguments.of(ifcDate(2015, 1, 8), 8, DAYS, ifcDate(2014, 13, 29)), // to year day
            Arguments.of(ifcDate(2015, 6, 28), 3, YEARS, ifcDate(2012, 6, 29)) // to leap day
        );
    }

    static Stream<Arguments> untilUnitProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 4), DAYS, 6),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 5), WEEKS, 1),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 26), MONTHS, 1),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2015, 5, 26), YEARS, 1),
            Arguments.of(ifcDate(2012, 6, 29), ifcDate(2012, 13, 29), DAYS, 197), // leap day to year day
            Arguments.of(ifcDate(2012, 6, 29), ifcDate(2012, 13, 29), MONTHS, 7)
        );
    }

    static Stream<Arguments> untilPeriodProvider() {
        return Stream.of(
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
            Arguments.of(ifcDate(2014, 5, 26), ifcDate(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
            Arguments.of(ifcDate(2004, 6, 29), ifcDate(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0))
        );
    }

    static Stream<Arguments> invalidEraProvider() {
        return Stream.of(Arguments.of(-1), Arguments.of(0), Arguments.of(2));
    }

    static Stream<Arguments> invalidProlepticYearProvider() {
        return Stream.of(Arguments.of(-10), Arguments.of(-1), Arguments.of(0));
    }

    static Stream<Arguments> toStringProvider() {
        return Stream.of(
            Arguments.of(ifcDate(1, 1, 1), "Ifc CE 1/01/01"),
            Arguments.of(ifcDate(2012, 6, 23), "Ifc CE 2012/06/23"),
            Arguments.of(ifcDate(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
            Arguments.of(ifcDate(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
        );
    }
}