package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestJulianChronology {

    private static final JulianChronology JULIAN = JulianChronology.INSTANCE;
    private static final Period JULIAN_ZERO_PERIOD = JULIAN.period(0, 0, 0);

    // ---------------------------------------------------------------------
    // Data providers (named for clarity)
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> samples() {
        return Stream.of(
            arguments(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            arguments(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            arguments(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),

            arguments(JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            arguments(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            arguments(JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
            arguments(JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),

            arguments(JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
            arguments(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            arguments(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            arguments(JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            arguments(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),

            arguments(JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
            arguments(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            arguments(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            arguments(JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            arguments(JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)),

            arguments(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            arguments(JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),

            arguments(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            arguments(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            arguments(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),

            arguments(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
            arguments(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> badDates() {
        return Stream.of(
            arguments(1900, 0, 0),

            arguments(1900, -1, 1),
            arguments(1900, 0, 1),
            arguments(1900, 13, 1),
            arguments(1900, 14, 1),

            arguments(1900, 1, -1),
            arguments(1900, 1, 0),
            arguments(1900, 1, 32),

            arguments(1900, 2, -1),
            arguments(1900, 2, 0),
            arguments(1900, 2, 30),
            arguments(1900, 2, 31),
            arguments(1900, 2, 32),

            arguments(1899, 2, -1),
            arguments(1899, 2, 0),
            arguments(1899, 2, 29),
            arguments(1899, 2, 30),
            arguments(1899, 2, 31),
            arguments(1899, 2, 32),

            arguments(1900, 12, -1),
            arguments(1900, 12, 0),
            arguments(1900, 12, 32),

            arguments(1900, 3, 32),
            arguments(1900, 4, 31),
            arguments(1900, 5, 32),
            arguments(1900, 6, 31),
            arguments(1900, 7, 32),
            arguments(1900, 8, 32),
            arguments(1900, 9, 31),
            arguments(1900, 10, 32),
            arguments(1900, 11, 31)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> lengthOfMonthData() {
        return Stream.of(
            arguments(1900, 1, 31),
            arguments(1900, 2, 29),
            arguments(1900, 3, 31),
            arguments(1900, 4, 30),
            arguments(1900, 5, 31),
            arguments(1900, 6, 30),
            arguments(1900, 7, 31),
            arguments(1900, 8, 31),
            arguments(1900, 9, 30),
            arguments(1900, 10, 31),
            arguments(1900, 11, 30),
            arguments(1900, 12, 31),

            arguments(1901, 2, 28),
            arguments(1902, 2, 28),
            arguments(1903, 2, 28),
            arguments(1904, 2, 29),
            arguments(2000, 2, 29),
            arguments(2100, 2, 29)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> rangesData() {
        return Stream.of(
            arguments(2012, 1, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 2, 23, DAY_OF_MONTH, 1, 29),
            arguments(2012, 3, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 4, 23, DAY_OF_MONTH, 1, 30),
            arguments(2012, 5, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 6, 23, DAY_OF_MONTH, 1, 30),
            arguments(2012, 7, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 8, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 9, 23, DAY_OF_MONTH, 1, 30),
            arguments(2012, 10, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 11, 23, DAY_OF_MONTH, 1, 30),
            arguments(2012, 12, 23, DAY_OF_MONTH, 1, 31),
            arguments(2012, 1, 23, DAY_OF_YEAR, 1, 366),
            arguments(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            arguments(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            arguments(2012, 3, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),

            arguments(2011, 2, 23, DAY_OF_MONTH, 1, 28),
            arguments(2011, 2, 23, DAY_OF_YEAR, 1, 365),
            arguments(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> getLongData() {
        return Stream.of(
            arguments(2014, 5, 26, DAY_OF_WEEK, 7),
            arguments(2014, 5, 26, DAY_OF_MONTH, 26),
            arguments(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
            arguments(2014, 5, 26, MONTH_OF_YEAR, 5),
            arguments(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
            arguments(2014, 5, 26, YEAR, 2014),
            arguments(2014, 5, 26, ERA, 1),
            arguments(1, 6, 8, ERA, 1),
            arguments(0, 6, 8, ERA, 0),

            arguments(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> withFieldData() {
        return Stream.of(
            arguments(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
            arguments(2014, 5, 26, DAY_OF_WEEK, 7, 2014, 5, 26),
            arguments(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            arguments(2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26),
            arguments(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
            arguments(2014, 5, 26, DAY_OF_YEAR, 146, 2014, 5, 26),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 22),
            arguments(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6, 2014, 5, 26),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
            arguments(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21, 2014, 5, 26),
            arguments(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
            arguments(2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26),
            arguments(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
            arguments(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1, 2014, 5, 26),
            arguments(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            arguments(2014, 5, 26, YEAR, 2014, 2014, 5, 26),
            arguments(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            arguments(2014, 5, 26, YEAR_OF_ERA, 2014, 2014, 5, 26),
            arguments(2014, 5, 26, ERA, 0, -2013, 5, 26),
            arguments(2014, 5, 26, ERA, 1, 2014, 5, 26),

            arguments(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28),
            arguments(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29),
            arguments(2012, 3, 31, MONTH_OF_YEAR, 6, 2012, 6, 30),
            arguments(2012, 2, 29, YEAR, 2011, 2011, 2, 28),
            arguments(-2013, 6, 8, YEAR_OF_ERA, 2012, -2011, 6, 8),
            arguments(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 3, 2014, 5, 22)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> plusData() {
        return Stream.of(
            arguments(2014, 5, 26, 0, DAYS, 2014, 5, 26),
            arguments(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            arguments(2014, 5, 26, -3, DAYS, 2014, 5, 23),
            arguments(2014, 5, 26, 0, WEEKS, 2014, 5, 26),
            arguments(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            arguments(2014, 5, 26, -5, WEEKS, 2014, 4, 21),
            arguments(2014, 5, 26, 0, MONTHS, 2014, 5, 26),
            arguments(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            arguments(2014, 5, 26, -5, MONTHS, 2013, 12, 26),
            arguments(2014, 5, 26, 0, YEARS, 2014, 5, 26),
            arguments(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            arguments(2014, 5, 26, -5, YEARS, 2009, 5, 26),
            arguments(2014, 5, 26, 0, DECADES, 2014, 5, 26),
            arguments(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            arguments(2014, 5, 26, -5, DECADES, 1964, 5, 26),
            arguments(2014, 5, 26, 0, CENTURIES, 2014, 5, 26),
            arguments(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            arguments(2014, 5, 26, -5, CENTURIES, 1514, 5, 26),
            arguments(2014, 5, 26, 0, MILLENNIA, 2014, 5, 26),
            arguments(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            arguments(2014, 5, 26, -5, MILLENNIA, 2014 - 5000, 5, 26),
            arguments(2014, 5, 26, -1, ERAS, -2013, 5, 26)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> untilData() {
        return Stream.of(
            arguments(2014, 5, 26, 2014, 5, 26, DAYS, 0),
            arguments(2014, 5, 26, 2014, 6, 1, DAYS, 6),
            arguments(2014, 5, 26, 2014, 5, 20, DAYS, -6),
            arguments(2014, 5, 26, 2014, 5, 26, WEEKS, 0),
            arguments(2014, 5, 26, 2014, 6, 1, WEEKS, 0),
            arguments(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
            arguments(2014, 5, 26, 2014, 5, 26, MONTHS, 0),
            arguments(2014, 5, 26, 2014, 6, 25, MONTHS, 0),
            arguments(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
            arguments(2014, 5, 26, 2014, 5, 26, YEARS, 0),
            arguments(2014, 5, 26, 2015, 5, 25, YEARS, 0),
            arguments(2014, 5, 26, 2015, 5, 26, YEARS, 1),
            arguments(2014, 5, 26, 2014, 5, 26, DECADES, 0),
            arguments(2014, 5, 26, 2024, 5, 25, DECADES, 0),
            arguments(2014, 5, 26, 2024, 5, 26, DECADES, 1),
            arguments(2014, 5, 26, 2014, 5, 26, CENTURIES, 0),
            arguments(2014, 5, 26, 2114, 5, 25, CENTURIES, 0),
            arguments(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
            arguments(2014, 5, 26, 2014, 5, 26, MILLENNIA, 0),
            arguments(2014, 5, 26, 3014, 5, 25, MILLENNIA, 0),
            arguments(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
            arguments(-2013, 5, 26, 0, 5, 26, ERAS, 0),
            arguments(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> toStringData() {
        return Stream.of(
            arguments(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
            arguments(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
        );
    }

    // ---------------------------------------------------------------------
    // Chronology lookup
    // ---------------------------------------------------------------------

    @Test
    public void chronologyOf_byName_returnsSingleton() {
        Chronology chrono = Chronology.of("Julian");
        assertNotNull(chrono);
        assertEquals(JULIAN, chrono);
        assertEquals("Julian", chrono.getId());
        assertEquals("julian", chrono.getCalendarType());
    }

    @Test
    public void chronologyOf_byCalendarType_returnsSingleton() {
        Chronology chrono = Chronology.of("julian");
        assertNotNull(chrono);
        assertEquals(JULIAN, chrono);
        assertEquals("Julian", chrono.getId());
        assertEquals("julian", chrono.getCalendarType());
    }

    // ---------------------------------------------------------------------
    // Conversions between JulianDate and ISO LocalDate
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("samples")
    public void converts_toIsoLocalDate(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void converts_fromIsoLocalDate(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JulianDate.from(iso));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void chronology_dateEpochDay_roundTrip(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JULIAN.dateEpochDay(iso.toEpochDay()));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void toEpochDay_matchesIso(JulianDate julian, LocalDate iso) {
        assertEquals(iso.toEpochDay(), julian.toEpochDay());
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void until_sameDate_zeroPeriod(JulianDate julian, LocalDate iso) {
        assertEquals(JULIAN_ZERO_PERIOD, julian.until(julian));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void until_isoSameDate_zeroPeriod(JulianDate julian, LocalDate iso) {
        assertEquals(JULIAN_ZERO_PERIOD, julian.until(iso));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void iso_until_julian_zeroPeriod(JulianDate julian, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(julian));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void chronology_date_fromTemporal(JulianDate julian, LocalDate iso) {
        assertEquals(julian, JULIAN.date(iso));
    }

    // ---------------------------------------------------------------------
    // Basic arithmetic in days
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("samples")
    public void plusDays_matchesIso(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
        assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
        assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void minusDays_matchesIso(JulianDate julian, LocalDate iso) {
        assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
        assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
        assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
        assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("samples")
    public void until_inDays(JulianDate julian, LocalDate iso) {
        assertEquals(0, julian.until(iso.plusDays(0), DAYS));
        assertEquals(1, julian.until(iso.plusDays(1), DAYS));
        assertEquals(35, julian.until(iso.plusDays(35), DAYS));
        assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
    }

    // ---------------------------------------------------------------------
    // Validation (bad dates)
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("badDates")
    public void creatingBadDates_throws(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
    }

    @Test
    public void dateYearDay_outOfRange_throws() {
        assertThrows(DateTimeException.class, () -> JULIAN.dateYearDay(2001, 366));
    }

    // ---------------------------------------------------------------------
    // Leap years
    // ---------------------------------------------------------------------

    @Test
    public void isLeapYear_simpleRule_every4Years() {
        for (int year = -200; year < 200; year++) {
            JulianDate base = JulianDate.of(year, 1, 1);
            assertEquals((year % 4) == 0, base.isLeapYear());
            assertEquals((year % 4) == 0, JULIAN.isLeapYear(year));
        }
    }

    @Test
    public void isLeapYear_specificYears() {
        assertEquals(true, JULIAN.isLeapYear(8));
        assertEquals(false, JULIAN.isLeapYear(7));
        assertEquals(false, JULIAN.isLeapYear(6));
        assertEquals(false, JULIAN.isLeapYear(5));
        assertEquals(true, JULIAN.isLeapYear(4));
        assertEquals(false, JULIAN.isLeapYear(3));
        assertEquals(false, JULIAN.isLeapYear(2));
        assertEquals(false, JULIAN.isLeapYear(1));
        assertEquals(true, JULIAN.isLeapYear(0));
        assertEquals(false, JULIAN.isLeapYear(-1));
        assertEquals(false, JULIAN.isLeapYear(-2));
        assertEquals(false, JULIAN.isLeapYear(-3));
        assertEquals(true, JULIAN.isLeapYear(-4));
        assertEquals(false, JULIAN.isLeapYear(-5));
        assertEquals(false, JULIAN.isLeapYear(-6));
    }

    @ParameterizedTest
    @MethodSource("lengthOfMonthData")
    public void lengthOfMonth_correctPerMonth(int year, int month, int length) {
        assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
    }

    // ---------------------------------------------------------------------
    // Era and proleptic year
    // ---------------------------------------------------------------------

    @Test
    public void era_and_yearOfEra_roundTrip_date() {
        for (int year = -200; year < 200; year++) {
            JulianDate base = JULIAN.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra());
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA));
            JulianDate eraBased = JULIAN.date(era, yoe, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void era_and_yearOfEra_roundTrip_yearDay() {
        for (int year = -200; year < 200; year++) {
            JulianDate base = JULIAN.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra());
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA));
            JulianDate eraBased = JULIAN.dateYearDay(era, yoe, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void prolepticYear_conversions() {
        assertEquals(4, JULIAN.prolepticYear(JulianEra.AD, 4));
        assertEquals(3, JULIAN.prolepticYear(JulianEra.AD, 3));
        assertEquals(2, JULIAN.prolepticYear(JulianEra.AD, 2));
        assertEquals(1, JULIAN.prolepticYear(JulianEra.AD, 1));
        assertEquals(0, JULIAN.prolepticYear(JulianEra.BC, 1));
        assertEquals(-1, JULIAN.prolepticYear(JulianEra.BC, 2));
        assertEquals(-2, JULIAN.prolepticYear(JulianEra.BC, 3));
        assertEquals(-3, JULIAN.prolepticYear(JulianEra.BC, 4));
    }

    @Test
    public void prolepticYear_wrongEraType_throws() {
        assertThrows(ClassCastException.class, () -> JULIAN.prolepticYear(IsoEra.CE, 4));
    }

    @Test
    public void eraOf_validValues() {
        assertEquals(JulianEra.AD, JULIAN.eraOf(1));
        assertEquals(JulianEra.BC, JULIAN.eraOf(0));
    }

    @Test
    public void eraOf_invalidValue_throws() {
        assertThrows(DateTimeException.class, () -> JULIAN.eraOf(2));
    }

    @Test
    public void eras_listContainsBoth() {
        List<Era> eras = JULIAN.eras();
        assertEquals(2, eras.size());
        assertEquals(true, eras.contains(JulianEra.BC));
        assertEquals(true, eras.contains(JulianEra.AD));
    }

    // ---------------------------------------------------------------------
    // Field ranges (Chronology and JulianDate)
    // ---------------------------------------------------------------------

    @Test
    public void chronology_range_supportedFields() {
        assertEquals(ValueRange.of(1, 7), JULIAN.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), JULIAN.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), JULIAN.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), JULIAN.range(MONTH_OF_YEAR));
    }

    @ParameterizedTest
    @MethodSource("rangesData")
    public void date_range_variousFields(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
        assertEquals(ValueRange.of(expectedMin, expectedMax), JulianDate.of(year, month, dom).range(field));
    }

    @Test
    public void date_range_unsupportedField_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).range(MINUTE_OF_DAY));
    }

    // ---------------------------------------------------------------------
    // getLong over various fields
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("getLongData")
    public void getLong_variousFields(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
    }

    @Test
    public void getLong_unsupportedField_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY));
    }

    // ---------------------------------------------------------------------
    // with(field, value)
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("withFieldData")
    public void with_field_setsExpectedDate(
        int year, int month, int dom,
        TemporalField field, long value,
        int expectedYear, int expectedMonth, int expectedDom
    ) {
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom),
            JulianDate.of(year, month, dom).with(field, value));
    }

    @Test
    public void with_unsupportedField_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0));
    }

    // ---------------------------------------------------------------------
    // TemporalAdjuster support
    // ---------------------------------------------------------------------

    @Test
    public void adjust_lastDayOfMonth_normalMonth() {
        JulianDate base = JulianDate.of(2012, 6, 23);
        JulianDate test = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(JulianDate.of(2012, 6, 30), test);
    }

    @Test
    public void adjust_lastDayOfMonth_leapYearFebruary() {
        JulianDate base = JulianDate.of(2012, 2, 23);
        JulianDate test = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(JulianDate.of(2012, 2, 29), test);
    }

    // ---------------------------------------------------------------------
    // with(Local*)
    // ---------------------------------------------------------------------

    @Test
    public void adjust_toIsoLocalDate() {
        JulianDate julian = JulianDate.of(2000, 1, 4);
        JulianDate test = julian.with(LocalDate.of(2012, 7, 6));
        assertEquals(JulianDate.of(2012, 6, 23), test);
    }

    @Test
    public void adjust_toMonth_unsupported() {
        JulianDate julian = JulianDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> julian.with(Month.APRIL));
    }

    // ---------------------------------------------------------------------
    // LocalDate/LocalDateTime with(JulianDate)
    // ---------------------------------------------------------------------

    @Test
    public void localDate_adjustWithJulianDate() {
        JulianDate julian = JulianDate.of(2012, 6, 23);
        LocalDate test = LocalDate.MIN.with(julian);
        assertEquals(LocalDate.of(2012, 7, 6), test);
    }

    @Test
    public void localDateTime_adjustWithJulianDate() {
        JulianDate julian = JulianDate.of(2012, 6, 23);
        LocalDateTime test = LocalDateTime.MIN.with(julian);
        assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), test);
    }

    // ---------------------------------------------------------------------
    // plus/minus using TemporalUnit
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("plusData")
    public void plus_unit_movesToExpectedDate(
        int year, int month, int dom,
        long amount, TemporalUnit unit,
        int expectedYear, int expectedMonth, int expectedDom
    ) {
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom),
            JulianDate.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("plusData")
    public void minus_unit_movesToExpectedDate(
        int expectedYear, int expectedMonth, int expectedDom,
        long amount, TemporalUnit unit,
        int year, int month, int dom
    ) {
        assertEquals(JulianDate.of(expectedYear, expectedMonth, expectedDom),
            JulianDate.of(year, month, dom).minus(amount, unit));
    }

    @Test
    public void plus_unsupportedUnit_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).plus(0, MINUTES));
    }

    // ---------------------------------------------------------------------
    // until using TemporalUnit
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("untilData")
    public void until_unit_returnsExpectedAmount(
        int year1, int month1, int dom1,
        int year2, int month2, int dom2,
        TemporalUnit unit, long expected
    ) {
        JulianDate start = JulianDate.of(year1, month1, dom1);
        JulianDate end = JulianDate.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @Test
    public void until_unsupportedUnit_throws() {
        JulianDate start = JulianDate.of(2012, 6, 30);
        JulianDate end = JulianDate.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    // ---------------------------------------------------------------------
    // Period arithmetic
    // ---------------------------------------------------------------------

    @Test
    public void plus_julianPeriod_supported() {
        assertEquals(JulianDate.of(2014, 7, 29),
            JulianDate.of(2014, 5, 26).plus(JULIAN.period(0, 2, 3)));
    }

    @Test
    public void plus_isoPeriod_unsupported() {
        assertThrows(DateTimeException.class, () -> JulianDate.of(2014, 5, 26).plus(Period.ofMonths(2)));
    }

    @Test
    public void minus_julianPeriod_supported() {
        assertEquals(JulianDate.of(2014, 3, 23),
            JulianDate.of(2014, 5, 26).minus(JULIAN.period(0, 2, 3)));
    }

    @Test
    public void minus_isoPeriod_unsupported() {
        assertThrows(DateTimeException.class, () -> JulianDate.of(2014, 5, 26).minus(Period.ofMonths(2)));
    }

    // ---------------------------------------------------------------------
    // equals() / hashCode()
    // ---------------------------------------------------------------------

    @Test
    public void equals_and_hashCode_groups() {
        new EqualsTester()
            .addEqualityGroup(JulianDate.of(2000, 1, 3), JulianDate.of(2000, 1, 3))
            .addEqualityGroup(JulianDate.of(2000, 1, 4), JulianDate.of(2000, 1, 4))
            .addEqualityGroup(JulianDate.of(2000, 2, 3), JulianDate.of(2000, 2, 3))
            .addEqualityGroup(JulianDate.of(2001, 1, 3), JulianDate.of(2001, 1, 3))
            .testEquals();
    }

    // ---------------------------------------------------------------------
    // toString()
    // ---------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("toStringData")
    public void toString_formatsAsExpected(JulianDate julian, String expected) {
        assertEquals(expected, julian.toString());
    }
}