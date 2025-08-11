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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

@SuppressWarnings({"static-method", "javadoc"})
public class TestSymmetry010Chronology {

    // Common helpers to make test definitions concise and consistent
    private static final Symmetry010Chronology CHRONO = Symmetry010Chronology.INSTANCE;

    private static Symmetry010Date s(int y, int m, int d) {
        return Symmetry010Date.of(y, m, d);
    }

    private static LocalDate iso(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }

    private static ChronoPeriod p(int y, int m, int d) {
        return CHRONO.period(y, m, d);
    }

    //-----------------------------------------------------------------------
    // Chronology.of(String)
    //-----------------------------------------------------------------------
    @Test
    public void chronology_lookupById() {
        Chronology chrono = Chronology.of("Sym010");
        assertNotNull(chrono);
        assertEquals(CHRONO, chrono);
        assertEquals("Sym010", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // Symmetry010Date <-> LocalDate samples
    //-----------------------------------------------------------------------
    public static Object[][] data_samples() {
        return new Object[][] {
            { s(   1,  1,  1), iso(   1,  1,  1) },
            { s( 272,  2, 28), iso( 272,  2, 27) },
            { s( 272,  2, 27), iso( 272,  2, 26) },
            { s( 742,  3, 27), iso( 742,  4,  2) },
            { s( 742,  4,  2), iso( 742,  4,  7) },
            { s(1066, 10, 14), iso(1066, 10, 14) },
            { s(1304,  7, 21), iso(1304,  7, 20) },
            { s(1304,  7, 20), iso(1304,  7, 19) },
            { s(1433, 11, 12), iso(1433, 11, 10) },
            { s(1433, 11, 10), iso(1433, 11,  8) },
            { s(1452,  4, 11), iso(1452,  4, 15) },
            { s(1452,  4, 15), iso(1452,  4, 19) },
            { s(1492, 10, 10), iso(1492, 10, 12) },
            { s(1492, 10, 12), iso(1492, 10, 14) },
            { s(1564,  2, 18), iso(1564,  2, 15) },
            { s(1564,  2, 15), iso(1564,  2, 12) },
            { s(1564,  4, 28), iso(1564,  4, 26) },
            { s(1564,  4, 26), iso(1564,  4, 24) },
            { s(1643,  1,  7), iso(1643,  1,  4) },
            { s(1643,  1,  4), iso(1643,  1,  1) },
            { s(1707,  4, 12), iso(1707,  4, 15) },
            { s(1707,  4, 15), iso(1707,  4, 18) },
            { s(1789,  7, 16), iso(1789,  7, 14) },
            { s(1789,  7, 14), iso(1789,  7, 12) },
            { s(1879,  3, 14), iso(1879,  3, 14) },
            { s(1941,  9, 11), iso(1941,  9,  9) },
            { s(1941,  9,  9), iso(1941,  9,  7) },
            { s(1970,  1,  4), iso(1970,  1,  1) },
            { s(1970,  1,  1), iso(1969, 12, 29) },
            { s(1999, 12, 29), iso(2000,  1,  1) },
            { s(2000,  1,  1), iso(2000,  1,  3) },
        };
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void localDate_from_symmetryDate(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void symmetryDate_from_localDate(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(sym010, Symmetry010Date.from(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void symmetryChronology_dateEpochDay_matches(LocalDate iso, Symmetry010Date sym010) {
        assertEquals(sym010, CHRONO.dateEpochDay(iso.toEpochDay()));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void epochDay_roundTrip(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso.toEpochDay(), sym010.toEpochDay());
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void until_sameDate_zeroPeriod_symmetryToSymmetry(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(p(0, 0, 0), sym010.until(sym010));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void until_sameDate_zeroPeriod_symmetryToIso(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(p(0, 0, 0), sym010.until(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void chronology_date_temporal_convertsIso(LocalDate iso, Symmetry010Date sym010) {
        assertEquals(sym010, CHRONO.date(iso));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void iso_until_symmetry_zeroPeriod(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(sym010));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void plus_days_behavesLikeIso(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(sym010.plus(35, DAYS)));
        assertEquals(iso.plusDays(-1), LocalDate.from(sym010.plus(-1, DAYS)));
        assertEquals(iso.plusDays(-60), LocalDate.from(sym010.plus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void minus_days_behavesLikeIso(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010.minus(0, DAYS)));
        assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS)));
        assertEquals(iso.minusDays(35), LocalDate.from(sym010.minus(35, DAYS)));
        assertEquals(iso.minusDays(-1), LocalDate.from(sym010.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(sym010.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    public void until_inDays_matchesIso(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(0, sym010.until(iso.plusDays(0), DAYS));
        assertEquals(1, sym010.until(iso.plusDays(1), DAYS));
        assertEquals(35, sym010.until(iso.plusDays(35), DAYS));
        assertEquals(-40, sym010.until(iso.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // Invalid date samples
    //-----------------------------------------------------------------------
    public static Object[][] data_badDates() {
        return new Object[][] {
            {-1, 13, 28},
            {-1, 13, 29},

            {2000, -2, 1},
            {2000, 13, 1},
            {2000, 15, 1},

            {2000, 1, -1},
            {2000, 1, 0},

            {2000, 0, 1},
            {2000, -1, 0},
            {2000, -1, 1},

            {2000, 1, 31},
            {2000, 2, 32},
            {2000, 3, 31},
            {2000, 4, 31},
            {2000, 5, 32},
            {2000, 6, 31},
            {2000, 7, 31},
            {2000, 8, 32},
            {2000, 9, 31},
            {2000, 10, 31},
            {2000, 11, 32},
            {2000, 12, 31},
            {2004, 12, 38},
        };
    }

    @ParameterizedTest
    @MethodSource("data_badDates")
    public void of_rejectsInvalidDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> s(year, month, dom));
    }

    public static Object[][] data_badLeapDates() {
        return new Object[][] { {1}, {100}, {200}, {2000} };
    }

    @ParameterizedTest
    @MethodSource("data_badLeapDates")
    public void of_rejectsNonLeapYearLeapDay(int year) {
        assertThrows(DateTimeException.class, () -> s(year, 12, 37));
    }

    //-----------------------------------------------------------------------
    // Leap year / week behavior
    //-----------------------------------------------------------------------
    @Test
    public void isLeapYear_examples() {
        assertTrue(CHRONO.isLeapYear(3));
        assertFalse(CHRONO.isLeapYear(6));
        assertTrue(CHRONO.isLeapYear(9));
        assertFalse(CHRONO.isLeapYear(2000));
        assertTrue(CHRONO.isLeapYear(2004));
    }

    @Test
    public void leapWeek_daysAreMarked() {
        assertTrue(s(2015, 12, 31).isLeapWeek());
        assertTrue(s(2015, 12, 32).isLeapWeek());
        assertTrue(s(2015, 12, 33).isLeapWeek());
        assertTrue(s(2015, 12, 34).isLeapWeek());
        assertTrue(s(2015, 12, 35).isLeapWeek());
        assertTrue(s(2015, 12, 36).isLeapWeek());
        assertTrue(s(2015, 12, 37).isLeapWeek());
    }

    //-----------------------------------------------------------------------
    // lengthOfMonth
    //-----------------------------------------------------------------------
    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            {2000, 1, 28, 30},
            {2000, 2, 28, 31},
            {2000, 3, 28, 30},
            {2000, 4, 28, 30},
            {2000, 5, 28, 31},
            {2000, 6, 28, 30},
            {2000, 7, 28, 30},
            {2000, 8, 28, 31},
            {2000, 9, 28, 30},
            {2000, 10, 28, 30},
            {2000, 11, 28, 31},
            {2000, 12, 28, 30},
            {2004, 12, 20, 37},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void lengthOfMonth_onDate(int year, int month, int day, int length) {
        assertEquals(length, s(year, month, day).lengthOfMonth());
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    public void lengthOfMonth_onFirstOfMonth(int year, int month, int day, int length) {
        assertEquals(length, s(year, month, 1).lengthOfMonth());
    }

    @Test
    public void lengthOfMonth_examples() {
        assertEquals(30, s(2000, 12, 1).lengthOfMonth());
        assertEquals(37, s(2004, 12, 1).lengthOfMonth());
    }

    //-----------------------------------------------------------------------
    // Era and year-of-era behavior
    //-----------------------------------------------------------------------
    @Test
    public void era_loop_positiveYears() {
        for (int year = 1; year < 200; year++) {
            Symmetry010Date base = CHRONO.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            Symmetry010Date eraBased = CHRONO.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void era_loop_negativeYears() {
        for (int year = -200; year < 0; year++) {
            Symmetry010Date base = CHRONO.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.BCE;
            assertEquals(era, base.getEra());
            assertEquals(1 - year, base.get(YEAR_OF_ERA));
            Symmetry010Date eraBased = CHRONO.date(era, year, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void era_yearDay_loop_positiveYears() {
        for (int year = 1; year < 200; year++) {
            Symmetry010Date base = CHRONO.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra());
            assertEquals(year, base.get(YEAR_OF_ERA));
            Symmetry010Date eraBased = CHRONO.dateYearDay(era, year, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    public void prolepticYear_examples() {
        assertEquals(4, CHRONO.prolepticYear(IsoEra.CE, 4));
        assertEquals(3, CHRONO.prolepticYear(IsoEra.CE, 3));
        assertEquals(2, CHRONO.prolepticYear(IsoEra.CE, 2));
        assertEquals(1, CHRONO.prolepticYear(IsoEra.CE, 1));
        assertEquals(2000, CHRONO.prolepticYear(IsoEra.CE, 2000));
        assertEquals(1582, CHRONO.prolepticYear(IsoEra.CE, 1582));
    }

    public static Object[][] data_prolepticYear_badEra() {
        return new Era[][] {
            { AccountingEra.BCE },
            { AccountingEra.CE },
            { CopticEra.BEFORE_AM },
            { CopticEra.AM },
            { DiscordianEra.YOLD },
            { EthiopicEra.BEFORE_INCARNATION },
            { EthiopicEra.INCARNATION },
            { HijrahEra.AH },
            { InternationalFixedEra.CE },
            { JapaneseEra.MEIJI },
            { JapaneseEra.TAISHO },
            { JapaneseEra.SHOWA },
            { JapaneseEra.HEISEI },
            { JulianEra.BC },
            { JulianEra.AD },
            { MinguoEra.BEFORE_ROC },
            { MinguoEra.ROC },
            { PaxEra.BCE },
            { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE },
            { ThaiBuddhistEra.BE },
        };
    }

    @ParameterizedTest
    @MethodSource("data_prolepticYear_badEra")
    public void prolepticYear_rejectsNonIsoEra(Era era) {
        assertThrows(ClassCastException.class, () -> CHRONO.prolepticYear(era, 4));
    }

    @Test
    public void chronology_eraOf_values() {
        assertEquals(IsoEra.BCE, CHRONO.eraOf(0));
        assertEquals(IsoEra.CE, CHRONO.eraOf(1));
    }

    @Test
    public void chronology_eraOf_invalidValue() {
        assertThrows(DateTimeException.class, () -> CHRONO.eraOf(2));
    }

    @Test
    public void chronology_eras_containsIsoEras() {
        List<Era> eras = CHRONO.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(IsoEra.BCE));
        assertTrue(eras.contains(IsoEra.CE));
    }

    //-----------------------------------------------------------------------
    // Chronology.range
    //-----------------------------------------------------------------------
    @Test
    public void chronology_range_supportedFields() {
        assertEquals(ValueRange.of(1, 7), CHRONO.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(1, 7), CHRONO.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(1, 4, 5), CHRONO.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 52, 53), CHRONO.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(1, 7), CHRONO.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 30, 37), CHRONO.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 364, 371), CHRONO.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), CHRONO.range(ERA));
        assertEquals(ValueRange.of(-1_000_000 * 364L - 177_474 * 7 - 719_162, 1_000_000 * 364L + 177_474 * 7 - 719_162), CHRONO.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 12), CHRONO.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), CHRONO.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), CHRONO.range(YEAR));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), CHRONO.range(YEAR_OF_ERA));
    }

    //-----------------------------------------------------------------------
    // Symmetry010Date.range
    //-----------------------------------------------------------------------
    public static Object[][] data_ranges() {
        return new Object[][] {
            // Day of month ranges (including long months and leap-week December)
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 3, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 4, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 8, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 9, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 10, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 11, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)},

            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 12, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},

            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},

            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},

            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},

            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 6, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 12, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_ranges")
    public void range_supportedField_valuesMatch(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, s(year, month, dom).range(field));
    }

    @Test
    public void range_unsupportedField_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> s(2012, 6, 28).range(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // getLong
    //-----------------------------------------------------------------------
    public static Object[][] data_getLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 2},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 5, 8, ERA, 1},

            {2012, 9, 26, DAY_OF_WEEK, 1},
            {2012, 9, 26, DAY_OF_YEAR, 3 * (4 + 5 + 4) * 7 - 4},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3},
            {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 3 * (4 + 5 + 4)},

            {2015, 12, 37, DAY_OF_WEEK, 5},
            {2015, 12, 37, DAY_OF_MONTH, 37},
            {2015, 12, 37, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7},
            {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2},
            {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
            {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 12, 37, MONTH_OF_YEAR, 12},
            {2015, 12, 37, PROLEPTIC_MONTH, 2016 * 12 - 1},
        };
    }

    @ParameterizedTest
    @MethodSource("data_getLong")
    public void getLong_returnsExpected(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, s(year, month, dom).getLong(field));
    }

    @Test
    public void getLong_unsupportedField_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> s(2012, 6, 28).getLong(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // with(field, value)
    //-----------------------------------------------------------------------
    public static Object[][] data_with() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 24},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30},
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 17},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 21},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6,  9},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20, 2014, 5, 19},
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

            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 1, 2015, 12, 17},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2015, 12, 18},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3, 2015, 12, 19},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 4, 2015, 12, 20},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2015, 12, 21},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, 12, 23},

            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, 12, 15},

            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015,  1, 20},

            {2015, 12, 29, DAY_OF_WEEK, 0, 2015, 12, 29},
            {2015, 12, 28, DAY_OF_WEEK, 1, 2015, 12, 24},
            {2015, 12, 28, DAY_OF_WEEK, 2, 2015, 12, 25},
            {2015, 12, 28, DAY_OF_WEEK, 3, 2015, 12, 26},
            {2015, 12, 28, DAY_OF_WEEK, 4, 2015, 12, 27},
            {2015, 12, 28, DAY_OF_WEEK, 5, 2015, 12, 28},
            {2015, 12, 28, DAY_OF_WEEK, 6, 2015, 12, 29},
            {2015, 12, 28, DAY_OF_WEEK, 7, 2015, 12, 30},

            {2015, 12, 29, DAY_OF_MONTH, 1, 2015, 12, 1},
            {2015, 12, 29, DAY_OF_MONTH, 3, 2015, 12, 3},

            {2015, 12, 29, MONTH_OF_YEAR, 1, 2015, 1, 29},
            {2015, 12, 29, MONTH_OF_YEAR, 12, 2015, 12, 29},
            {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29},

            {2015, 12, 37, YEAR, 2004, 2004, 12, 37},
            {2015, 12, 37, YEAR, 2013, 2013, 12, 30},

            {2014, 3, 28, DAY_OF_MONTH, 1, 2014, 3, 1},
            {2014, 1, 28, DAY_OF_MONTH, 1, 2014, 1, 1},
            {2014, 3, 28, MONTH_OF_YEAR, 1, 2014, 1, 28},
            {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 37},
            {2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 30},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with")
    public void with_field_setsExpectedDate(int year, int month, int dom,
                                            TemporalField field, long value,
                                            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(s(expectedYear, expectedMonth, expectedDom), s(year, month, dom).with(field, value));
    }

    public static Object[][] data_with_bad() {
        return new Object[][] {
            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_MONTH,  8},

            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_YEAR, -1},
            {2013,  1,  1, ALIGNED_DAY_OF_WEEK_IN_YEAR,  8},

            {2013,  1,  1, ALIGNED_WEEK_OF_MONTH, -1},
            {2013,  2,  1, ALIGNED_WEEK_OF_MONTH,  6},

            {2013,  1,  1, ALIGNED_WEEK_OF_YEAR, -1},
            {2015,  1,  1, ALIGNED_WEEK_OF_YEAR, 54},

            {2013,  1,  1, DAY_OF_WEEK, -1},
            {2013,  1,  1, DAY_OF_WEEK,  8},
            {2013,  1,  1, DAY_OF_MONTH, -1},
            {2013,  1,  1, DAY_OF_MONTH, 31},
            {2013,  6,  1, DAY_OF_MONTH, 31},
            {2013, 12,  1, DAY_OF_MONTH, 31},
            {2015, 12,  1, DAY_OF_MONTH, 38},

            {2013,  1,  1, DAY_OF_YEAR,  -1},
            {2013,  1,  1, DAY_OF_YEAR, 365},
            {2015,  1,  1, DAY_OF_YEAR, 372},

            {2013,  1,  1, MONTH_OF_YEAR, -1},
            {2013,  1,  1, MONTH_OF_YEAR, 14},
            {2013,  1,  1, MONTH_OF_YEAR, -2},
            {2015,  1,  1, MONTH_OF_YEAR, 14},

            {2013,  1,  1, EPOCH_DAY, -365_961_481},
            {2013,  1,  1, EPOCH_DAY,  364_523_156},
            {2013,  1,  1, YEAR, -1_000_001},
            {2013,  1,  1, YEAR,  1_000_001},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with_bad")
    public void with_field_rejectsOutOfRangeValues(int year, int month, int dom, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> s(year, month, dom).with(field, value));
    }

    @Test
    public void with_field_unsupported_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> s(2012, 6, 28).with(MINUTE_OF_DAY, 10));
    }

    //-----------------------------------------------------------------------
    // TemporalAdjusters
    //-----------------------------------------------------------------------
    public static Object[][] data_temporalAdjusters_lastDayOfMonth() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 30},
            {2012, 2, 23, 2012, 2, 31},
            {2012, 3, 23, 2012, 3, 30},
            {2012, 4, 23, 2012, 4, 30},
            {2012, 5, 23, 2012, 5, 31},
            {2012, 6, 23, 2012, 6, 30},
            {2012, 7, 23, 2012, 7, 30},
            {2012, 8, 23, 2012, 8, 31},
            {2012, 9, 23, 2012, 9, 30},
            {2012, 10, 23, 2012,10, 30},
            {2012, 11, 23, 2012, 11, 31},
            {2012, 12, 23, 2012, 12, 30},
            {2009, 12, 23, 2009, 12, 37},
        };
    }

    @ParameterizedTest
    @MethodSource("data_temporalAdjusters_lastDayOfMonth")
    public void temporalAdjusters_lastDayOfMonth_works(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
        Symmetry010Date base = s(year, month, day);
        Symmetry010Date expected = s(expectedYear, expectedMonth, expectedDay);
        Symmetry010Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual);
    }

    //-----------------------------------------------------------------------
    // Adjust with Local* types
    //-----------------------------------------------------------------------
    @Test
    public void adjust_toLocalDate() {
        Symmetry010Date base = s(2000, 1, 4);
        Symmetry010Date adjusted = base.with(iso(2012, 7, 6));
        assertEquals(s(2012, 7, 5), adjusted);
    }

    @Test
    public void adjust_toMonth_rejected() {
        Symmetry010Date base = s(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> base.with(Month.APRIL));
    }

    //-----------------------------------------------------------------------
    // LocalDate.with(Symmetry010Date)
    //-----------------------------------------------------------------------
    @Test
    public void localDate_adjustTo_Symmetry010Date() {
        Symmetry010Date sym010 = s(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(sym010);
        assertEquals(iso(2012, 7, 20), test);
    }

    @Test
    public void localDateTime_adjustTo_Symmetry010Date() {
        Symmetry010Date sym010 = s(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(sym010);
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test);
    }

    //-----------------------------------------------------------------------
    // plus / minus with TemporalUnit
    //-----------------------------------------------------------------------
    public static Object[][] data_plus() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 21},
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

            {2014, 12, 26, 3, WEEKS, 2015, 1, 17},
            {2014, 1, 26, -5, WEEKS, 2013, 12, 21},

            {2012, 6, 26, 3, WEEKS, 2012, 7, 17},
            {2012, 7, 26, -5, WEEKS, 2012, 6, 21},

            {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28},
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21},
        };
    }

    public static Object[][] data_plus_leapWeek() {
        return new Object[][] {
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},
            {2015, 12, 28, 8, DAYS, 2015, 12, 36},
            {2015, 12, 28, -3, DAYS, 2015, 12, 25},
            {2015, 12, 28, 0, WEEKS, 2015, 12, 28},
            {2015, 12, 28, 3, WEEKS, 2016,  1, 12},
            {2015, 12, 28, -5, WEEKS, 2015, 11, 24},
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21},
            {2015, 12, 28, 0, MONTHS, 2015, 12, 28},
            {2015, 12, 28, 3, MONTHS, 2016,  3, 28},
            {2015, 12, 28, -5, MONTHS, 2015,  7, 28},
            {2015, 12, 28, 12, MONTHS, 2016, 12, 28},
            {2015, 12, 28, 0, YEARS, 2015, 12, 28},
            {2015, 12, 28, 3, YEARS, 2018, 12, 28},
            {2015, 12, 28, -5, YEARS, 2010, 12, 28},
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void plus_withTemporalUnit_movesAsExpected(int year, int month, int dom, long amount, TemporalUnit unit,
                                                      int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(s(expectedYear, expectedMonth, expectedDom), s(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    public void plus_acrossLeapWeek_movesAsExpected(int year, int month, int dom, long amount, TemporalUnit unit,
                                                    int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(s(expectedYear, expectedMonth, expectedDom), s(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void minus_withTemporalUnit_movesAsExpected(int expectedYear, int expectedMonth, int expectedDom,
                                                       long amount, TemporalUnit unit,
                                                       int year, int month, int dom) {
        assertEquals(s(expectedYear, expectedMonth, expectedDom), s(year, month, dom).minus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus_leapWeek")
    public void minus_acrossLeapWeek_movesAsExpected(int expectedYear, int expectedMonth, int expectedDom,
                                                     long amount, TemporalUnit unit,
                                                     int year, int month, int dom) {
        assertEquals(s(expectedYear, expectedMonth, expectedDom), s(year, month, dom).minus(amount, unit));
    }

    @Test
    public void plus_withUnsupportedUnit_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> s(2012, 6, 28).plus(0, MINUTES));
    }

    //-----------------------------------------------------------------------
    // until (TemporalUnit) and until (ChronoPeriod)
    //-----------------------------------------------------------------------
    public static Object[][] data_until() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6,  4, DAYS, 9},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 6,  1, WEEKS, 1},
            {2014, 5, 26, 2014, 6,  5, WEEKS, 1},
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
            {2014, 5, 26, 2014, 5, 26, 0,  0,  0},
            {2014, 5, 26, 2014, 6,  4, 0,  0,  9},
            {2014, 5, 26, 2014, 5, 20, 0,  0, -6},
            {2014, 5, 26, 2014, 6,  5, 0,  0, 10},
            {2014, 5, 26, 2014, 6, 25, 0,  0, 30},
            {2014, 5, 26, 2014, 6, 26, 0,  1,  0},
            {2014, 5, 26, 2015, 5, 25, 0, 11, 29},
            {2014, 5, 26, 2015, 5, 26, 1,  0,  0},
            {2014, 5, 26, 2024, 5, 25, 9, 11, 29},
        };
    }

    @ParameterizedTest
    @MethodSource("data_until")
    public void until_withTemporalUnit_returnsExpected(int year1, int month1, int dom1,
                                                       int year2, int month2, int dom2,
                                                       TemporalUnit unit, long expected) {
        Symmetry010Date start = s(year1, month1, dom1);
        Symmetry010Date end = s(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @ParameterizedTest
    @MethodSource("data_until_period")
    public void until_returnsChronoPeriod(int year1, int month1, int dom1,
                                          int year2, int month2, int dom2,
                                          int yearPeriod, int monthPeriod, int dayPeriod) {
        Symmetry010Date start = s(year1, month1, dom1);
        Symmetry010Date end = s(year2, month2, dom2);
        ChronoPeriod period = p(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end));
    }

    @Test
    public void until_withUnsupportedUnit_throws() {
        Symmetry010Date start = s(2012, 6, 28);
        Symmetry010Date end = s(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    //-----------------------------------------------------------------------
    // plus/minus with ChronoPeriod
    //-----------------------------------------------------------------------
    @Test
    public void plus_withChronoPeriod_works() {
        assertEquals(s(2014, 7, 29), s(2014, 5, 21).plus(p(0, 2, 8)));
    }

    @Test
    public void plus_withIsoPeriod_rejected() {
        assertThrows(DateTimeException.class, () -> s(2014, 5, 26).plus(Period.ofMonths(2)));
    }

    @Test
    public void minus_withChronoPeriod_works() {
        assertEquals(s(2014, 3, 23), s(2014, 5, 26).minus(p(0, 2, 3)));
    }

    @Test
    public void minus_withIsoPeriod_rejected() {
        assertThrows(DateTimeException.class, () -> s(2014, 5, 26).minus(Period.ofMonths(2)));
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    @Test
    public void equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(s(2000,  1,  3), s(2000,  1,  3))
            .addEqualityGroup(s(2000,  1,  4), s(2000,  1,  4))
            .addEqualityGroup(s(2000,  2,  3), s(2000,  2,  3))
            .addEqualityGroup(s(2000,  6, 23), s(2000,  6, 23))
            .addEqualityGroup(s(2000,  6, 28), s(2000,  6,  28))
            .addEqualityGroup(s(2000,  7,  1), s(2000,  7,   1))
            .addEqualityGroup(s(2000, 12, 25), s(2000, 12, 25))
            .addEqualityGroup(s(2000, 12, 28), s(2000, 12, 28))
            .addEqualityGroup(s(2001,  1,  1), s(2001,  1,  1))
            .addEqualityGroup(s(2001,  1,  3), s(2001,  1,  3))
            .addEqualityGroup(s(2001, 12, 28), s(2001, 12, 28))
            .addEqualityGroup(s(2004,  6, 28), s(2004,  6, 28))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // toString
    //-----------------------------------------------------------------------
    public static Object[][] data_toString() {
        return new Object[][] {
            {s(   1,  1,  1), "Sym010 CE 1/01/01"},
            {s(1970,  2, 31), "Sym010 CE 1970/02/31"},
            {s(2000,  8, 31), "Sym010 CE 2000/08/31"},
            {s(2009, 12, 37), "Sym010 CE 2009/12/37"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void toString_formatsAsExpected(Symmetry010Date date, String expected) {
        assertEquals(expected, date.toString());
    }
}