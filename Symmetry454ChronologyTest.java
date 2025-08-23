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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for Symmetry454Chronology and Symmetry454Date.
 * 
 * Intent:
 * - Keep a 1:1 parity with LocalDate for conversion, arithmetic and ranges.
 * - Make invariants and expectations explicit.
 * - Use small helper methods to reduce noise and improve readability.
 */
@DisplayName("Symmetry454 chronology and date")
@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry454Chronology {

    // Small helpers to reduce visual noise in the data sets
    private static Symmetry454Date sym(int y, int m, int d) {
        return Symmetry454Date.of(y, m, d);
    }

    private static LocalDate iso(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }

    // ---------------------------------------------------------------------
    // Chronology.of(String)
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("Chronology lookup by ID")
    public void test_chronology() {
        Chronology chrono = Chronology.of("Sym454");
        assertNotNull(chrono);
        assertEquals(Symmetry454Chronology.INSTANCE, chrono);
        assertEquals("Sym454", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    // ---------------------------------------------------------------------
    // Symmetry454Date.of <-> LocalDate conversion samples
    // ---------------------------------------------------------------------
    public static Object[][] data_samples() {
        return new Object[][] {
            { sym(   1,  1,  1), iso(   1,  1,  1) },
            { sym( 272,  2, 30), iso( 272,  2, 27) },
            { sym( 272,  2, 27), iso( 272,  2, 24) },
            { sym( 742,  3, 25), iso( 742,  4,  2) },
            { sym( 742,  4,  2), iso( 742,  4,  7) },
            { sym(1066, 10, 14), iso(1066, 10, 14) },
            { sym(1304,  7, 21), iso(1304,  7, 20) },
            { sym(1304,  7, 20), iso(1304,  7, 19) },
            { sym(1433, 11, 14), iso(1433, 11, 10) },
            { sym(1433, 11, 10), iso(1433, 11,  6) },
            { sym(1452,  4, 11), iso(1452,  4, 15) },
            { sym(1452,  4, 15), iso(1452,  4, 19) },
            { sym(1492, 10, 10), iso(1492, 10, 12) },
            { sym(1492, 10, 12), iso(1492, 10, 14) },
            { sym(1564,  2, 20), iso(1564,  2, 15) },
            { sym(1564,  2, 15), iso(1564,  2, 10) },
            { sym(1564,  4, 28), iso(1564,  4, 26) },
            { sym(1564,  4, 26), iso(1564,  4, 24) },
            { sym(1643,  1,  7), iso(1643,  1,  4) },
            { sym(1643,  1,  4), iso(1643,  1,  1) },
            { sym(1707,  4, 12), iso(1707,  4, 15) },
            { sym(1707,  4, 15), iso(1707,  4, 18) },
            { sym(1789,  7, 16), iso(1789,  7, 14) },
            { sym(1789,  7, 14), iso(1789,  7, 12) },
            { sym(1879,  3, 12), iso(1879,  3, 14) },
            { sym(1879,  3, 14), iso(1879,  3, 16) },
            { sym(1941,  9,  9), iso(1941,  9,  9) },
            { sym(1970,  1,  4), iso(1970,  1,  1) },
            { sym(1970,  1,  1), iso(1969, 12, 29) },
            { sym(1999, 12, 27), iso(2000,  1,  1) },
            { sym(2000,  1,  1), iso(2000,  1,  3) },
        };
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("LocalDate.from(Symmetry454Date) matches sample mapping")
    public void test_LocalDate_from_Symmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("Symmetry454Date.from(LocalDate) matches sample mapping")
    public void test_Symmetry454Date_from_LocalDate(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, Symmetry454Date.from(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("dateEpochDay roundtrips mapping")
    public void test_Symmetry454Date_chronology_dateEpochDay(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("toEpochDay parity with LocalDate")
    public void test_Symmetry454Date_toEpochDay(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso.toEpochDay(), sym454.toEpochDay());
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("until with same date returns zero Sym454 period")
    public void test_Symmetry454Date_until_Symmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(sym454));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("until(LocalDate) with equal instant returns zero Sym454 period")
    public void test_Symmetry454Date_until_LocalDate(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("Chronology.date(Temporal) produces Sym454 date equal to sample")
    public void test_Chronology_date_Temporal(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("LocalDate.until(Symmetry454Date) produces zero ISO period for same instant")
    public void test_LocalDate_until_Symmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(sym454));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("plus days parity with LocalDate")
    public void test_plusDays(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
        assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
        assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("minus days parity with LocalDate")
    public void test_minusDays(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)));
        assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
        assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
        assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("until(..., DAYS) parity with LocalDate arithmetic")
    public void test_until_DAYS(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
        assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
        assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
        assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
    }

    // ---------------------------------------------------------------------
    // Invalid date creation
    // ---------------------------------------------------------------------
    public static Object[][] data_badDates() {
        return new Object[][] {
            {-1, 13, 28}, {-1, 13, 29},
            {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},
            {2000, 1, -1}, {2000, 1, 0},
            {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},
            {2000, 1, 29}, {2000, 2, 36}, {2000, 3, 29}, {2000, 4, 29},
            {2000, 5, 36}, {2000, 6, 29}, {2000, 7, 29}, {2000, 8, 36},
            {2000, 9, 29}, {2000, 10, 29}, {2000, 11, 36}, {2000, 12, 29},
            {2004, 12, 36},
        };
    }

    @ParameterizedTest
    @MethodSource("data_badDates")
    @DisplayName("of(year,month,day) rejects invalid dates")
    public void test_badDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
    }

    public static Object[][] data_badLeapDates() {
        return new Object[][] { {1}, {100}, {200}, {2000} };
    }

    @ParameterizedTest
    @MethodSource("data_badLeapDates")
    @DisplayName("Non-leap years reject day 29 of month 12")
    public void test_badLeapDayDates(int year) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
    }

    @Test
    @DisplayName("dateYearDay rejects invalid day-of-year")
    public void test_chronology_dateYearDay_badDate() {
        assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.dateYearDay(2000, 365));
    }

    // ---------------------------------------------------------------------
    // Leap year / leap week
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("Leap year determination samples")
    public void test_isLeapYear_specific() {
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(3));
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(6));
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(9));
        assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2000));
        assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2004));
    }

    @Test
    @DisplayName("Leap week dates are recognized")
    public void test_leapWeek() {
        assertTrue(sym(2015, 12, 29).isLeapWeek());
        assertTrue(sym(2015, 12, 30).isLeapWeek());
        assertTrue(sym(2015, 12, 31).isLeapWeek());
        assertTrue(sym(2015, 12, 32).isLeapWeek());
        assertTrue(sym(2015, 12, 33).isLeapWeek());
        assertTrue(sym(2015, 12, 34).isLeapWeek());
        assertTrue(sym(2015, 12, 35).isLeapWeek());
    }

    // ---------------------------------------------------------------------
    // lengthOfMonth
    // ---------------------------------------------------------------------
    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            {2000, 1, 28, 28}, {2000, 2, 28, 35}, {2000, 3, 28, 28},
            {2000, 4, 28, 28}, {2000, 5, 28, 35}, {2000, 6, 28, 28},
            {2000, 7, 28, 28}, {2000, 8, 28, 35}, {2000, 9, 28, 28},
            {2000,10, 28, 28}, {2000,11, 28, 35}, {2000,12, 28, 28},
            {2004,12, 20, 35},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    @DisplayName("lengthOfMonth for arbitrary day in month")
    public void test_lengthOfMonth(int year, int month, int day, int length) {
        assertEquals(length, Symmetry454Date.of(year, month, day).lengthOfMonth());
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    @DisplayName("lengthOfMonth for first day in month")
    public void test_lengthOfMonthFirst(int year, int month, int day, int length) {
        assertEquals(length, Symmetry454Date.of(year, month, 1).lengthOfMonth());
    }

    @Test
    @DisplayName("lengthOfMonth specific values (normal vs leap)")
    public void test_lengthOfMonth_specific() {
        assertEquals(28, sym(2000, 12, 28).lengthOfMonth());
        assertEquals(35, sym(2004, 12, 28).lengthOfMonth());
    }

    // ---------------------------------------------------------------------
    // Era / year computations
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("Era – proleptic year loop for CE and BCE")
    public void test_era_loop() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }

        for (int year = -200; year < 0; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.BCE;
            assertEquals(era, base.getEra());
            assertEquals(1 - year, base.get(YEAR_OF_ERA));
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    @DisplayName("Era – dateYearDay for CE")
    public void test_era_yearDay_loop() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = Symmetry454Chronology.INSTANCE.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.dateYearDay(era, year, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    @DisplayName("prolepticYear with ISO era works for samples")
    public void test_prolepticYear_specific() {
        assertEquals(4, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        assertEquals(3, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 3));
        assertEquals(2, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2));
        assertEquals(1, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
        assertEquals(2000, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, Symmetry454Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1582));
    }

    public static Object[][] data_prolepticYear_badEra() {
        return new Era[][] {
            { AccountingEra.BCE }, { AccountingEra.CE },
            { CopticEra.BEFORE_AM }, { CopticEra.AM },
            { DiscordianEra.YOLD },
            { EthiopicEra.BEFORE_INCARNATION }, { EthiopicEra.INCARNATION },
            { HijrahEra.AH },
            { InternationalFixedEra.CE },
            { JapaneseEra.MEIJI }, { JapaneseEra.TAISHO }, { JapaneseEra.SHOWA }, { JapaneseEra.HEISEI },
            { JulianEra.BC }, { JulianEra.AD },
            { MinguoEra.BEFORE_ROC }, { MinguoEra.ROC },
            { PaxEra.BCE }, { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE }, { ThaiBuddhistEra.BE },
        };
    }

    @ParameterizedTest
    @MethodSource("data_prolepticYear_badEra")
    @DisplayName("prolepticYear rejects non-ISO eras")
    public void test_prolepticYear_badEra(Era era) {
        assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
    }

    @Test
    @DisplayName("eraOf returns ISO eras")
    public void test_Chronology_eraOf() {
        assertEquals(IsoEra.BCE, Symmetry454Chronology.INSTANCE.eraOf(0));
        assertEquals(IsoEra.CE, Symmetry454Chronology.INSTANCE.eraOf(1));
    }

    @Test
    @DisplayName("eraOf rejects invalid values")
    public void test_Chronology_eraOf_invalid() {
        assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.eraOf(2));
    }

    @Test
    @DisplayName("eras() returns both ISO eras")
    public void test_Chronology_eras() {
        List<Era> eras = Symmetry454Chronology.INSTANCE.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(IsoEra.BCE));
        assertTrue(eras.contains(IsoEra.CE));
    }

    // ---------------------------------------------------------------------
    // Chronology.range
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("Chronology.range for supported fields")
    public void test_Chronology_range() {
        assertEquals(ValueRange.of(1, 7), Symmetry454Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(1, 7), Symmetry454Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(1, 4, 5), Symmetry454Chronology.INSTANCE.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 52, 53), Symmetry454Chronology.INSTANCE.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(1, 7), Symmetry454Chronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 35), Symmetry454Chronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 364, 371), Symmetry454Chronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), Symmetry454Chronology.INSTANCE.range(ERA));
        assertEquals(ValueRange.of(-1_000_000 * 364L - 177_474 * 7 - 719_162, 1_000_000 * 364L + 177_474 * 7 - 719_162), Symmetry454Chronology.INSTANCE.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 12), Symmetry454Chronology.INSTANCE.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), Symmetry454Chronology.INSTANCE.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), Symmetry454Chronology.INSTANCE.range(YEAR));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), Symmetry454Chronology.INSTANCE.range(YEAR_OF_ERA));
    }

    // ---------------------------------------------------------------------
    // Symmetry454Date.range
    // ---------------------------------------------------------------------
    public static Object[][] data_ranges() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 3, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 4, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 8, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 9, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012,10, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012,11, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012,12, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2015,12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},

            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012,12, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},

            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},

            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012,12, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
            {2015,12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},

            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012,12, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 6, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012,12, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2015,12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_ranges")
    @DisplayName("range(...) returns expected ValueRange")
    public void test_range(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
    }

    @Test
    @DisplayName("range rejects unsupported field")
    public void test_range_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).range(MINUTE_OF_DAY));
    }

    // ---------------------------------------------------------------------
    // Symmetry454Date.getLong
    // ---------------------------------------------------------------------
    public static Object[][] data_getLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 5, 8, ERA, 1},

            {2012, 9, 26, DAY_OF_WEEK, 5},
            {2012, 9, 26, DAY_OF_YEAR, 3 * (4 + 5 + 4) * 7 - 2},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 3 * (4 + 5 + 4)},

            {2015, 12, 35, DAY_OF_WEEK, 7},
            {2015, 12, 35, DAY_OF_MONTH, 35},
            {2015, 12, 35, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 12, 35, MONTH_OF_YEAR, 12},
            {2015, 12, 35, PROLEPTIC_MONTH, 2016 * 12 - 1},
        };
    }

    @ParameterizedTest
    @MethodSource("data_getLong")
    @DisplayName("getLong(field) returns expected value")
    public void test_getLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
    }

    @Test
    @DisplayName("getLong rejects unsupported field")
    public void test_getLong_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY));
    }

    // ---------------------------------------------------------------------
    // Symmetry454Date.with(field, value)
    // ---------------------------------------------------------------------
    public static Object[][] data_with() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 19},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 23},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20, 2014, 5, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1, 2014, 5, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR, 2014, 2014, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2014, 2014, 5, 26},
            {2014, 5, 26, ERA, 1, 2014, 5, 26},

            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 1, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2, 2015, 12, 23},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2015, 12, 24},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 4, 2015, 12, 25},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2015, 12, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 6, 2015, 12, 27},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28},

            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 1, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2015, 12, 23},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3, 2015, 12, 24},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 4, 2015, 12, 25},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2015, 12, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6, 2015, 12, 27},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, 12, 28},

            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, 12, 15},

            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015, 1, 15},

            {2015, 12, 29, DAY_OF_WEEK, 0, 2015, 12, 29},
            {2015, 12, 28, DAY_OF_WEEK, 1, 2015, 12, 22},
            {2015, 12, 28, DAY_OF_WEEK, 2, 2015, 12, 23},
            {2015, 12, 28, DAY_OF_WEEK, 3, 2015, 12, 24},
            {2015, 12, 28, DAY_OF_WEEK, 4, 2015, 12, 25},
            {2015, 12, 28, DAY_OF_WEEK, 5, 2015, 12, 26},
            {2015, 12, 28, DAY_OF_WEEK, 6, 2015, 12, 27},
            {2015, 12, 28, DAY_OF_WEEK, 7, 2015, 12, 28},

            {2015, 12, 29, DAY_OF_MONTH, 1, 2015, 12, 1},
            {2015, 12, 29, DAY_OF_MONTH, 3, 2015, 12, 3},

            {2015, 12, 29, MONTH_OF_YEAR, 1, 2015, 1, 28},
            {2015, 12, 29, MONTH_OF_YEAR, 12, 2015, 12, 29},
            {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29},

            {2015, 12, 29, YEAR, 2014, 2014, 12, 28},
            {2015, 12, 29, YEAR, 2013, 2013, 12, 28},

            {2014, 3, 28, DAY_OF_MONTH, 1, 2014, 3, 1},
            {2014, 1, 28, DAY_OF_MONTH, 1, 2014, 1, 1},
            {2014, 3, 28, MONTH_OF_YEAR, 1, 2014, 1, 28},
            {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35},
            {2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 28},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with")
    @DisplayName("with(field,value) returns expected date")
    public void test_with_TemporalField(int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(sym(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).with(field, value));
    }

    public static Object[][] data_with_bad() {
        return new Object[][] {
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},

            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_YEAR, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_YEAR, 8},

            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5},
            {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},

            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},

            {2013, 1, 1, DAY_OF_WEEK, -1},
            {2013, 1, 1, DAY_OF_WEEK, 8},
            {2013, 1, 1, DAY_OF_MONTH, -1},
            {2013, 1, 1, DAY_OF_MONTH, 29},
            {2013, 6, 1, DAY_OF_MONTH, 29},
            {2013, 12, 1, DAY_OF_MONTH, 30},
            {2015, 12, 1, DAY_OF_MONTH, 36},

            {2013, 1, 1, DAY_OF_YEAR, -1},
            {2013, 1, 1, DAY_OF_YEAR, 365},
            {2015, 1, 1, DAY_OF_YEAR, 372},

            {2013, 1, 1, MONTH_OF_YEAR, -1},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, MONTH_OF_YEAR, -2},
            {2013, 1, 1, MONTH_OF_YEAR, 14},

            {2013, 1, 1, EPOCH_DAY, -365_961_481},
            {2013, 1, 1, EPOCH_DAY,  364_523_156},
            {2013, 1, 1, YEAR, -1_000_001},
            {2013, 1, 1, YEAR,  1_000_001},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with_bad")
    @DisplayName("with(field,value) rejects out-of-range values")
    public void test_with_TemporalField_badValue(int year, int month, int dom, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
    }

    @Test
    @DisplayName("with rejects unsupported field")
    public void test_with_TemporalField_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).with(MINUTE_OF_DAY, 10));
    }

    // ---------------------------------------------------------------------
    // TemporalAdjusters
    // ---------------------------------------------------------------------
    public static Object[][] data_temporalAdjusters_lastDayOfMonth() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 28},
            {2012, 2, 23, 2012, 2, 35},
            {2012, 3, 23, 2012, 3, 28},
            {2012, 4, 23, 2012, 4, 28},
            {2012, 5, 23, 2012, 5, 35},
            {2012, 6, 23, 2012, 6, 28},
            {2012, 7, 23, 2012, 7, 28},
            {2012, 8, 23, 2012, 8, 35},
            {2012, 9, 23, 2012, 9, 28},
            {2012,10, 23, 2012,10, 28},
            {2012,11, 23, 2012,11, 35},
            {2012,12, 23, 2012,12, 28},
            {2009,12, 23, 2009,12, 35},
        };
    }

    @ParameterizedTest
    @MethodSource("data_temporalAdjusters_lastDayOfMonth")
    @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) moves to last day")
    public void test_temporalAdjusters_LastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
        Symmetry454Date base = sym(year, month, day);
        Symmetry454Date expected = sym(expectedYear, expectedMonth, expectedDay);
        Symmetry454Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual);
    }

    // ---------------------------------------------------------------------
    // Adjusters to/from Local*
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("Symmetry454Date.with(LocalDate) maps correctly")
    public void test_adjust_toLocalDate() {
        Symmetry454Date sym454 = sym(2000, 1, 4);
        Symmetry454Date test = sym454.with(LocalDate.of(2012, 7, 6));
        assertEquals(sym(2012, 7, 5), test);
    }

    @Test
    @DisplayName("Symmetry454Date.with(Month) is not supported")
    public void test_adjust_toMonth() {
        Symmetry454Date sym454 = sym(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> sym454.with(Month.APRIL));
    }

    @Test
    @DisplayName("LocalDate.with(Symmetry454Date)")
    public void test_LocalDate_adjustToSymmetry454Date() {
        Symmetry454Date sym454 = sym(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(sym454);
        assertEquals(LocalDate.of(2012, 7, 20), test);
    }

    @Test
    @DisplayName("LocalDateTime.with(Symmetry454Date)")
    public void test_LocalDateTime_adjustToSymmetry454Date() {
        Symmetry454Date sym454 = sym(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(sym454);
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test);
    }

    // ---------------------------------------------------------------------
    // plus/minus with TemporalUnit
    // ---------------------------------------------------------------------
    public static Object[][] data_plus() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 5, 34},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 19},
            {2014, 5, 26, 0, MONTHS, 2014, 5, 26},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
            {2014, 5, 26, 0, YEARS, 2014, 5, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},
            {2014, 5, 26, 0, DECADES, 2014, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, -5, DECADES, 1964, 5, 26},
            {2014, 5, 26, 0, CENTURIES, 2014, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},
            {2014, 5, 26, 0, MILLENNIA, 2014, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, MILLENNIA, 1014, 5, 26},

            {2014, 12, 26, 3, WEEKS, 2015, 1, 19},
            {2014, 1, 26, -5, WEEKS, 2013, 12, 19},

            {2012, 6, 26, 3, WEEKS, 2012, 7, 19},
            {2012, 7, 26, -5, WEEKS, 2012, 6, 19},

            {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28},
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21},
        };
    }

    public static Object[][] data_plus_leapWeek() {
        return new Object[][] {
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},
            {2015, 12, 28, 8, DAYS, 2016, 1, 1},
            {2015, 12, 28, -3, DAYS, 2015, 12, 25},
            {2015, 12, 28, 0, WEEKS, 2015, 12, 28},
            {2015, 12, 28, 3, WEEKS, 2016, 1, 14},
            {2015, 12, 28, -5, WEEKS, 2015, 11, 28},
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21},
            {2015, 12, 28, 0, MONTHS, 2015, 12, 28},
            {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
            {2015, 12, 28, -5, MONTHS, 2015, 7, 28},
            {2015, 12, 28, 12, MONTHS, 2016, 12, 28},
            {2015, 12, 28, 0, YEARS, 2015, 12, 28},
            {2015, 12, 28, 3, YEARS, 2018, 12, 28},
            {2015, 12, 28, -5, YEARS, 2010, 12, 28},
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    @DisplayName("plus(amount, unit) returns expected date")
    public void test_plus_TemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(sym(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    @DisplayName("plus(amount, unit) across leap week returns expected date")
    public void test_plus_leapWeek_TemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(sym(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    @DisplayName("minus(amount, unit) inverse of plus")
    public void test_minus_TemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
        assertEquals(sym(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).minus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    @DisplayName("minus(amount, unit) across leap week inverse of plus")
    public void test_minus_leapWeek_TemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
        assertEquals(sym(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).minus(amount, unit));
    }

    @Test
    @DisplayName("plus rejects unsupported unit")
    public void test_plus_TemporalUnit_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).plus(0, MINUTES));
    }

    // ---------------------------------------------------------------------
    // until
    // ---------------------------------------------------------------------
    public static Object[][] data_until() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 13},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 4, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 25, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            {2014, 5, 26, 2015, 5, 25, YEARS, 0},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2014, 5, 26, DECADES, 0},
            {2014, 5, 26, 2024, 5, 25, DECADES, 0},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2014, 5, 26, CENTURIES, 0},
            {2014, 5, 26, 2114, 5, 25, CENTURIES, 0},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 2014, 5, 26, MILLENNIA, 0},
            {2014, 5, 26, 3014, 5, 25, MILLENNIA, 0},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
        };
    }

    public static Object[][] data_until_period() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
            {2014, 5, 26, 2014, 6, 5, 0, 0, 14},
            {2014, 5, 26, 2014, 6, 25, 0, 0, 34},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 25, 0, 11, 27},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2014, 5, 26, 2024, 5, 25, 9, 11, 27},
        };
    }

    @ParameterizedTest
    @MethodSource("data_until")
    @DisplayName("until(end, unit) returns expected amount")
    public void test_until_TemporalUnit(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
        Symmetry454Date start = sym(year1, month1, dom1);
        Symmetry454Date end = sym(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @ParameterizedTest
    @MethodSource("data_until_period")
    @DisplayName("until(end) returns expected Sym454 ChronoPeriod")
    public void test_until_end(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            int yearPeriod, int monthPeriod, int dayPeriod) {
        Symmetry454Date start = sym(year1, month1, dom1);
        Symmetry454Date end = sym(year2, month2, dom2);
        ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end));
    }

    @Test
    @DisplayName("until rejects unsupported unit")
    public void test_until_TemporalUnit_unsupported() {
        Symmetry454Date start = sym(2012, 6, 28);
        Symmetry454Date end = sym(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    // ---------------------------------------------------------------------
    // period
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("plus(ChronoPeriod) using Symmetry454 period")
    public void test_plus_Period() {
        assertEquals(sym(2014, 8, 1), sym(2014, 5, 21).plus(Symmetry454Chronology.INSTANCE.period(0, 2, 8)));
    }

    @Test
    @DisplayName("plus(Period) using ISO period is rejected")
    public void test_plus_Period_ISO() {
        assertThrows(DateTimeException.class, () -> sym(2014, 5, 26).plus(Period.ofMonths(2)));
    }

    @Test
    @DisplayName("minus(ChronoPeriod) using Symmetry454 period")
    public void test_minus_Period() {
        assertEquals(sym(2014, 3, 23), sym(2014, 5, 26).minus(Symmetry454Chronology.INSTANCE.period(0, 2, 3)));
    }

    @Test
    @DisplayName("minus(Period) using ISO period is rejected")
    public void test_minus_Period_ISO() {
        assertThrows(DateTimeException.class, () -> sym(2014, 5, 26).minus(Period.ofMonths(2)));
    }

    // ---------------------------------------------------------------------
    // equals() / hashCode()
    // ---------------------------------------------------------------------
    @Test
    @DisplayName("equals and hashCode contract")
    public void test_equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(sym(2000,  1,  3), sym(2000,  1,  3))
            .addEqualityGroup(sym(2000,  1,  4), sym(2000,  1,  4))
            .addEqualityGroup(sym(2000,  2,  3), sym(2000,  2,  3))
            .addEqualityGroup(sym(2001,  1,  3), sym(2001,  1,  3))
            .addEqualityGroup(sym(2000, 12, 28), sym(2000, 12, 28))
            .addEqualityGroup(sym(2000, 12, 25), sym(2000, 12, 25))
            .addEqualityGroup(sym(2001,  1,  1), sym(2001,  1,  1))
            .addEqualityGroup(sym(2001, 12, 28), sym(2001, 12, 28))
            .addEqualityGroup(sym(2000,  6, 28), sym(2000,  6, 28))
            .addEqualityGroup(sym(2000,  6, 23), sym(2000,  6, 23))
            .addEqualityGroup(sym(2000,  7,  1), sym(2000,  7,  1))
            .addEqualityGroup(sym(2004,  6, 28), sym(2004,  6, 28))
            .testEquals();
    }

    // ---------------------------------------------------------------------
    // toString
    // ---------------------------------------------------------------------
    public static Object[][] data_toString() {
        return new Object[][] {
            {sym(1, 1, 1), "Sym454 CE 1/01/01"},
            {sym(1970, 2, 35), "Sym454 CE 1970/02/35"},
            {sym(2000, 8, 35), "Sym454 CE 2000/08/35"},
            {sym(1970, 12, 35), "Sym454 CE 1970/12/35"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    @DisplayName("toString returns a stable, readable format")
    public void test_toString(Symmetry454Date date, String expected) {
        assertEquals(expected, date.toString());
    }
}