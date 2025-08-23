package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the InternationalFixedChronology class.
 */
public class TestInternationalFixedChronology {

    private static final Chronology CHRONOLOGY = InternationalFixedChronology.INSTANCE;

    // Test Chronology.of(String)
    @Test
    public void testChronologyOfString() {
        Chronology chrono = Chronology.of("Ifc");
        assertNotNull(chrono);
        assertEquals(CHRONOLOGY, chrono);
        assertEquals("Ifc", chrono.getId());
        assertNull(chrono.getCalendarType());
    }

    // Test creation and conversion to LocalDate
    public static Object[][] dataSamples() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateFromInternationalFixedDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testInternationalFixedDateFromLocalDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(fixed, InternationalFixedDate.from(iso));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testInternationalFixedDateChronologyDateEpochDay(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(fixed, CHRONOLOGY.dateEpochDay(iso.toEpochDay()));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testInternationalFixedDateToEpochDay(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso.toEpochDay(), fixed.toEpochDay());
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testInternationalFixedDateUntilInternationalFixedDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(CHRONOLOGY.period(0, 0, 0), fixed.until(fixed));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testInternationalFixedDateUntilLocalDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(CHRONOLOGY.period(0, 0, 0), fixed.until(iso));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateUntilInternationalFixedDate(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(fixed));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testChronologyDateTemporal(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(fixed, CHRONOLOGY.date(iso));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testPlusDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(fixed.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(fixed.plus(35, DAYS)));
        if (LocalDate.ofYearDay(1, 60).isBefore(iso)) {
            assertEquals(iso.plusDays(-1), LocalDate.from(fixed.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(fixed.plus(-60, DAYS)));
        }
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testMinusDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(iso, LocalDate.from(fixed.minus(0, DAYS)));
        if (LocalDate.ofYearDay(1, 35).isBefore(iso)) {
            assertEquals(iso.minusDays(1), LocalDate.from(fixed.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(fixed.minus(35, DAYS)));
        }
        assertEquals(iso.minusDays(-1), LocalDate.from(fixed.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(fixed.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testUntilDays(InternationalFixedDate fixed, LocalDate iso) {
        assertEquals(0, fixed.until(iso.plusDays(0), DAYS));
        assertEquals(1, fixed.until(iso.plusDays(1), DAYS));
        assertEquals(35, fixed.until(iso.plusDays(35), DAYS));
        if (LocalDate.ofYearDay(1, 40).isBefore(iso)) {
            assertEquals(-40, fixed.until(iso.minusDays(40), DAYS));
        }
    }

    public static Object[][] dataBadDates() {
        return new Object[][] {
            {-1, 13, 28},
            {-1, 13, 29},
            {0, 1, 1},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataBadDates")
    public void testBadDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
    }

    public static Object[][] dataBadLeapDates() {
        return new Object[][] {
            {1},
            {100},
            {200},
            {300},
            {1900}
        };
    }

    @ParameterizedTest
    @MethodSource("dataBadLeapDates")
    public void testBadLeapDayDates(int year) {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
    }

    @Test
    public void testChronologyDateYearDayBadDate() {
        assertThrows(DateTimeException.class, () -> CHRONOLOGY.dateYearDay(2001, 366));
    }

    // Test isLeapYear()
    @Test
    public void testIsLeapYearLoop() {
        IntPredicate isLeapYear = year -> ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);

        for (int year = 1; year < 500; year++) {
            InternationalFixedDate base = InternationalFixedDate.of(year, 1, 1);
            assertEquals(isLeapYear.test(year), base.isLeapYear());
            assertEquals(isLeapYear.test(year) ? 366 : 365, base.lengthOfYear());
            assertEquals(isLeapYear.test(year), CHRONOLOGY.isLeapYear(year));
        }
    }

    @Test
    public void testIsLeapYearSpecific() {
        assertTrue(CHRONOLOGY.isLeapYear(400));
        assertFalse(CHRONOLOGY.isLeapYear(100));
        assertTrue(CHRONOLOGY.isLeapYear(4));
        assertFalse(CHRONOLOGY.isLeapYear(3));
        assertFalse(CHRONOLOGY.isLeapYear(2));
        assertFalse(CHRONOLOGY.isLeapYear(1));
    }

    // Test lengthOfMonth()
    public static Object[][] dataLengthOfMonth() {
        return new Object[][] {
            {1900, 1, 28, 28},
            {1900, 2, 28, 28},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataLengthOfMonth")
    public void testLengthOfMonth(int year, int month, int day, int length) {
        assertEquals(length, InternationalFixedDate.of(year, month, day).lengthOfMonth());
    }

    @ParameterizedTest
    @MethodSource("dataLengthOfMonth")
    public void testLengthOfMonthFirst(int year, int month, int day, int length) {
        assertEquals(length, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
    }

    @Test
    public void testLengthOfMonthSpecific() {
        assertEquals(29, InternationalFixedDate.of(1900, 13, 29).lengthOfMonth());
        assertEquals(29, InternationalFixedDate.of(2000, 13, 29).lengthOfMonth());
        assertEquals(29, InternationalFixedDate.of(2000, 6, 29).lengthOfMonth());
    }

    @Test
    public void testEraValid() {
        Era era = CHRONOLOGY.eraOf(1);
        assertNotNull(era);
        assertEquals(1, era.getValue());
    }

    // Test invalid era values
    public static Object[][] dataInvalidEraValues() {
        return new Object[][] {
            {-1},
            {0},
            {2},
        };
    }

    @ParameterizedTest
    @MethodSource("dataInvalidEraValues")
    public void testEraInvalid(int eraValue) {
        assertThrows(DateTimeException.class, () -> CHRONOLOGY.eraOf(eraValue));
    }

    // Test era, prolepticYear and dateYearDay
    @Test
    public void testEraLoop() {
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate base = CHRONOLOGY.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            InternationalFixedEra era = InternationalFixedEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            InternationalFixedDate eraBased = CHRONOLOGY.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void testEraYearDayLoop() {
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate base = CHRONOLOGY.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            InternationalFixedEra era = InternationalFixedEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            InternationalFixedDate eraBased = CHRONOLOGY.dateYearDay(era, year, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void testProlepticYearSpecific() {
        assertEquals(4, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 4));
        assertEquals(3, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 3));
        assertEquals(2, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 2));
        assertEquals(1, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 1));
        assertEquals(2000, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 2000));
        assertEquals(1582, CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, 1582));
    }

    public static Object[][] dataProlepticYearBad() {
        return new Object[][] {
            {-10},
            {-1},
            {0},
        };
    }

    @ParameterizedTest
    @MethodSource("dataProlepticYearBad")
    public void testProlepticYearBad(int year) {
        assertThrows(DateTimeException.class, () -> CHRONOLOGY.prolepticYear(InternationalFixedEra.CE, year));
    }

    @Test
    public void testProlepticYearBadEra() {
        assertThrows(ClassCastException.class, () -> CHRONOLOGY.prolepticYear(IsoEra.CE, 4));
    }

    @Test
    public void testChronologyEraOf() {
        assertEquals(InternationalFixedEra.CE, CHRONOLOGY.eraOf(1));
    }

    @Test
    public void testChronologyEraOfInvalid() {
        assertThrows(DateTimeException.class, () -> CHRONOLOGY.eraOf(0));
    }

    @Test
    public void testChronologyEras() {
        List<Era> eras = CHRONOLOGY.eras();
        assertEquals(1, eras.size());
        assertTrue(eras.contains(InternationalFixedEra.CE));
    }

    // Test Chronology.range
    @Test
    public void testChronologyRange() {
        assertEquals(ValueRange.of(0, 1, 0, 7), CHRONOLOGY.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 7), CHRONOLOGY.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 4), CHRONOLOGY.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 52), CHRONOLOGY.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 7), CHRONOLOGY.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 29), CHRONOLOGY.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), CHRONOLOGY.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 1), CHRONOLOGY.range(ERA));
        assertEquals(ValueRange.of(-719_528, 1_000_000 * 365L + 242_499 - 719_528), CHRONOLOGY.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 13), CHRONOLOGY.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(13, 1_000_000 * 13L - 1), CHRONOLOGY.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(1, 1_000_000), CHRONOLOGY.range(YEAR));
        assertEquals(ValueRange.of(1, 1_000_000), CHRONOLOGY.range(YEAR_OF_ERA));
    }

    // Test InternationalFixedDate.range
    public static Object[][] dataRanges() {
        return new Object[][] {
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataRanges")
    public void testRange(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
    }

    @Test
    public void testRangeUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> InternationalFixedDate.of(2012, 6, 28).range(MINUTE_OF_DAY));
    }

    // Test InternationalFixedDate.getLong
    public static Object[][] dataGetLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 + 28 + 28 + 28 + 26},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataGetLong")
    public void testGetLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
    }

    @Test
    public void testGetLongUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> InternationalFixedDate.of(2012, 6, 28).getLong(MINUTE_OF_DAY));
    }

    // Test InternationalFixedDate.with
    public static Object[][] dataWith() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataWith")
    public void testWithTemporalField(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom), InternationalFixedDate.of(year, month, dom).with(field, value));
    }

    public static Object[][] dataWithBad() {
        return new Object[][] {
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 0},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataWithBad")
    public void testWithTemporalFieldBadValue(int year, int month, int dom, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom).with(field, value));
    }

    @Test
    public void testWithTemporalFieldUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> InternationalFixedDate.of(2012, 6, 28).with(MINUTE_OF_DAY, 0));
    }

    // Test InternationalFixedDate.with(TemporalAdjuster)
    public static Object[][] dataTemporalAdjustersLastDayOfMonth() {
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29},
            {2012, 6, 29, 2012, 6, 29},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataTemporalAdjustersLastDayOfMonth")
    public void testTemporalAdjustersLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
        InternationalFixedDate base = InternationalFixedDate.of(year, month, day);
        InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
        InternationalFixedDate actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual);
    }

    // Test InternationalFixedDate.with(Local*)
    @Test
    public void testAdjustToLocalDate() {
        InternationalFixedDate fixed = InternationalFixedDate.of(2000, 1, 4);
        InternationalFixedDate test = fixed.with(LocalDate.of(2012, 7, 6));
        assertEquals(InternationalFixedDate.of(2012, 7, 19), test);
    }

    @Test
    public void testAdjustToMonth() {
        InternationalFixedDate fixed = InternationalFixedDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> fixed.with(Month.APRIL));
    }

    // Test LocalDate.with(InternationalFixedDate)
    @Test
    public void testLocalDateAdjustToInternationalFixedDate() {
        InternationalFixedDate fixed = InternationalFixedDate.of(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(fixed);
        assertEquals(LocalDate.of(2012, 7, 6), test);
    }

    @Test
    public void testLocalDateTimeAdjustToInternationalFixedDate() {
        InternationalFixedDate fixed = InternationalFixedDate.of(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(fixed);
        assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), test);
    }

    // Test InternationalFixedDate.plus and minus
    public static Object[][] dataPlus() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            // More test data...
        };
    }

    public static Object[][] dataPlusLeapAndYearDay() {
        return new Object[][] {
            {2014, 13, 29, 0, DAYS, 2014, 13, 29},
            {2014, 13, 29, 8, DAYS, 2015, 1, 8},
            // More test data...
        };
    }

    public static Object[][] dataMinusLeapAndYearDay() {
        return new Object[][] {
            {2014, 13, 29, 0, DAYS, 2014, 13, 29},
            {2014, 13, 21, 8, DAYS, 2014, 13, 29},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testPlusTemporalUnit(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom), InternationalFixedDate.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("dataPlusLeapAndYearDay")
    public void testPlusLeapAndYearDayTemporalUnit(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom), InternationalFixedDate.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testMinusTemporalUnit(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
        assertEquals(InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom), InternationalFixedDate.of(year, month, dom).minus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("dataMinusLeapAndYearDay")
    public void testMinusLeapAndYearDayTemporalUnit(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
        assertEquals(InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom), InternationalFixedDate.of(year, month, dom).minus(amount, unit));
    }

    @Test
    public void testPlusTemporalUnitUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> InternationalFixedDate.of(2012, 6, 28).plus(0, MINUTES));
    }

    // Test InternationalFixedDate.until
    public static Object[][] dataUntil() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 6},
            // More test data...
        };
    }

    public static Object[][] dataUntilPeriod() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataUntil")
    public void testUntilTemporalUnit(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
        InternationalFixedDate start = InternationalFixedDate.of(year1, month1, dom1);
        InternationalFixedDate end = InternationalFixedDate.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @ParameterizedTest
    @MethodSource("dataUntilPeriod")
    public void testUntilEnd(int year1, int month1, int dom1, int year2, int month2, int dom2, int yearPeriod, int monthPeriod, int dayPeriod) {
        InternationalFixedDate start = InternationalFixedDate.of(year1, month1, dom1);
        InternationalFixedDate end = InternationalFixedDate.of(year2, month2, dom2);
        ChronoPeriod period = CHRONOLOGY.period(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end));
    }

    @Test
    public void testUntilTemporalUnitUnsupported() {
        InternationalFixedDate start = InternationalFixedDate.of(2012, 6, 28);
        InternationalFixedDate end = InternationalFixedDate.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    // Test InternationalFixedDate.period
    @Test
    public void testPlusPeriod() {
        assertEquals(InternationalFixedDate.of(2014, 8, 1), InternationalFixedDate.of(2014, 5, 26).plus(CHRONOLOGY.period(0, 2, 3)));
    }

    @Test
    public void testPlusPeriodISO() {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(2014, 5, 26).plus(Period.ofMonths(2)));
    }

    @Test
    public void testMinusPeriod() {
        assertEquals(InternationalFixedDate.of(2014, 3, 23), InternationalFixedDate.of(2014, 5, 26).minus(CHRONOLOGY.period(0, 2, 3)));
    }

    @Test
    public void testMinusPeriodISO() {
        assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(2014, 5, 26).minus(Period.ofMonths(2)));
    }

    // Test equals() / hashCode()
    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(InternationalFixedDate.of(2000,  1,  3), InternationalFixedDate.of(2000,  1,  3))
            .addEqualityGroup(InternationalFixedDate.of(2000,  1,  4), InternationalFixedDate.of(2000,  1,  4))
            // More equality groups...
            .testEquals();
    }

    // Test toString()
    public static Object[][] dataToString() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            // More test data...
        };
    }

    @ParameterizedTest
    @MethodSource("dataToString")
    public void testToString(InternationalFixedDate date, String expected) {
        assertEquals(expected, date.toString());
    }
}