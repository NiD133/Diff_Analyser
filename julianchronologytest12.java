package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.Era;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link JulianDate} and its interactions with {@link JulianChronology}.
 * This class covers conversions, date arithmetic, and field access.
 */
@DisplayName("JulianDate and JulianChronology")
public class JulianChronologyTestTest12 {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] provider_sampleJulianAndIsoDates() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            // Leap year in Julian (year 4), not in proleptic Gregorian
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)}, // ISO leap day
            // Leap year in Julian (year 100), not in Gregorian
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            // Year 0
            {JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)},
            // Near Gregorian cutover
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            // Modern dates
            {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
            {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)},
        };
    }

    @ParameterizedTest(name = "{0} <=> {1}")
    @MethodSource("provider_sampleJulianAndIsoDates")
    @DisplayName("should convert consistently to and from ISO (LocalDate)")
    void conversionsToAndFromIso_shouldBeConsistent(JulianDate julian, LocalDate iso) {
        assertAll("Conversions between Julian and ISO",
            () -> assertEquals(iso, LocalDate.from(julian)),
            () -> assertEquals(julian, JulianDate.from(iso)),
            () -> assertEquals(iso.toEpochDay(), julian.toEpochDay()),
            () -> assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay())),
            () -> assertEquals(julian, JulianChronology.INSTANCE.date(iso))
        );
    }

    @ParameterizedTest(name = "plus {1} days")
    @MethodSource("provider_sampleJulianAndIsoDates")
    @DisplayName("plus(DAYS) should be consistent with ISO")
    void plusDays_shouldBehaveLikeIsoPlusDays(JulianDate julian, LocalDate iso) {
        assertAll("Adding days",
            () -> assertEquals(iso, LocalDate.from(julian.plus(0, DAYS))),
            () -> assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS))),
            () -> assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS))),
            () -> assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS))),
            () -> assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)))
        );
    }

    @ParameterizedTest(name = "minus {1} days")
    @MethodSource("provider_sampleJulianAndIsoDates")
    @DisplayName("minus(DAYS) should be consistent with ISO")
    void minusDays_shouldBehaveLikeIsoMinusDays(JulianDate julian, LocalDate iso) {
        assertAll("Subtracting days",
            () -> assertEquals(iso, LocalDate.from(julian.minus(0, DAYS))),
            () -> assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS))),
            () -> assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS))),
            () -> assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS))),
            () -> assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)))
        );
    }

    @ParameterizedTest(name = "until({1})")
    @MethodSource("provider_sampleJulianAndIsoDates")
    @DisplayName("until(..., DAYS) should be consistent with ISO")
    void until_days_shouldBehaveLikeIsoUntil(JulianDate julian, LocalDate iso) {
        assertAll("Calculating days until a date",
            () -> assertEquals(0, julian.until(iso.plusDays(0), DAYS)),
            () -> assertEquals(1, julian.until(iso.plusDays(1), DAYS)),
            () -> assertEquals(35, julian.until(iso.plusDays(35), DAYS)),
            () -> assertEquals(-40, julian.until(iso.minusDays(40), DAYS))
        );
    }

    @Test
    @DisplayName("until() should return a zero period for the same date")
    void until_zeroDifference_shouldReturnZeroPeriod() {
        JulianDate date = JulianDate.of(2012, 6, 23);
        LocalDate isoDate = LocalDate.of(2012, 7, 6); // Equivalent ISO date

        assertAll("until() with zero difference",
            () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date)),
            // until() is specified to return a zero period for other chronologies
            () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(isoDate)),
            () -> assertEquals(Period.ZERO, isoDate.until(date))
        );
    }

    static Object[][] provider_invalidDateParts() {
        return new Object[][] {
            {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, // 1900 is a leap year in Julian
            {1899, 2, 29}, // 1899 is not a leap year
            {1900, 4, 31},
        };
    }

    @ParameterizedTest(name = "of({0}, {1}, {2})")
    @MethodSource("provider_invalidDateParts")
    @DisplayName("of() should throw DateTimeException for invalid date parts")
    void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
    }

    static Object[][] provider_lengthOfMonth() {
        return new Object[][] {
            {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30},
            {1900, 5, 31}, {1900, 6, 30}, {1900, 7, 31}, {1900, 8, 31},
            {1900, 9, 30}, {1900, 10, 31}, {1900, 11, 30}, {1900, 12, 31},
            {1901, 2, 28}, {1904, 2, 29}, {2000, 2, 29}, {2100, 2, 29},
        };
    }

    @ParameterizedTest(name = "of({0}, {1}, 1).lengthOfMonth() is {2}")
    @MethodSource("provider_lengthOfMonth")
    @DisplayName("lengthOfMonth() should return correct length")
    void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int length) {
        assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
    }

    static Object[][] provider_range() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, 1, 31}, {2012, 2, 23, DAY_OF_MONTH, 1, 29},
            {2011, 2, 23, DAY_OF_MONTH, 1, 28}, {2012, 4, 23, DAY_OF_MONTH, 1, 30},
            {2012, 1, 23, DAY_OF_YEAR, 1, 366}, {2011, 1, 23, DAY_OF_YEAR, 1, 365},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, range({3}) is {4}-{5}")
    @MethodSource("provider_range")
    @DisplayName("range() should return correct range for a given field")
    void range_shouldReturnCorrectRangeForField(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
        assertEquals(ValueRange.of(expectedMin, expectedMax), JulianDate.of(year, month, dom).range(field));
    }

    static Object[][] provider_getLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // DAY_OF_YEAR for 2014-05-26: 31(Jan)+28(Feb)+31(Mar)+30(Apr)+26 = 146
            {2014, 5, 26, DAY_OF_YEAR, 146},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            // PROLEPTIC_MONTH: (year * 12) + month - 1
            {2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 6, 8, ERA, 1},
            {0, 6, 8, ERA, 0},
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) is {4}")
    @MethodSource("provider_getLong")
    @DisplayName("getLong() should return correct value for a given field")
    void getLong_shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
    }

    static Object[][] provider_with() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013L * 12 + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 0, -2013, 5, 26},
            // Adjusting month to a shorter month
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28},
            // Adjusting month to a shorter month in a leap year
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29},
            // Adjusting year of a leap day to a non-leap year
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28},
        };
    }

    @ParameterizedTest(name = "of({0},{1},{2}).with({3}, {4})")
    @MethodSource("provider_with")
    @DisplayName("with() should adjust field correctly")
    void with_shouldSetFieldCorrectly(int y, int m, int d, TemporalField field, long val, int ey, int em, int ed) {
        JulianDate start = JulianDate.of(y, m, d);
        JulianDate expected = JulianDate.of(ey, em, ed);
        assertEquals(expected, start.with(field, val));
    }

    static Object[][] provider_plus() {
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, ERAS, -2013, 5, 26},
        };
    }

    @ParameterizedTest(name = "of({0},{1},{2}).plus({3}, {4})")
    @MethodSource("provider_plus")
    @DisplayName("plus() should add temporal amount correctly")
    void plus_shouldAddAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
        JulianDate start = JulianDate.of(y, m, d);
        JulianDate expected = JulianDate.of(ey, em, ed);
        assertEquals(expected, start.plus(amount, unit));
    }

    @ParameterizedTest(name = "of({4},{5},{6}).minus({3}, {2})")
    @MethodSource("provider_plus") // Reusing 'plus' data for 'minus'
    @DisplayName("minus() should subtract temporal amount correctly")
    void minus_shouldSubtractAmountCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
        JulianDate start = JulianDate.of(y, m, d);
        JulianDate expected = JulianDate.of(ey, em, ed);
        assertEquals(expected, start.minus(amount, unit));
    }

    static Object[][] provider_until() {
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    @ParameterizedTest(name = "of({0},{1},{2}).until(of({3},{4},{5}), {6})")
    @MethodSource("provider_until")
    @DisplayName("until() should calculate amount between dates")
    void until_shouldCalculateAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        JulianDate start = JulianDate.of(y1, m1, d1);
        JulianDate end = JulianDate.of(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    static Object[][] provider_toString() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
            {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("provider_toString")
    @DisplayName("toString() should return correctly formatted string")
    void toString_shouldReturnFormattedString(JulianDate julian, String expected) {
        assertEquals(expected, julian.toString());
    }

    @Test
    @DisplayName("eras() should contain BC and AD")
    void eras_shouldContainBCAndAD() {
        List<Era> eras = JulianChronology.INSTANCE.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.BC));
        assertTrue(eras.contains(JulianEra.AD));
    }
}