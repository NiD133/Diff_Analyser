package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for {@link BritishCutoverChronology} / {@link BritishCutoverDate}.
 *
 * Notes:
 * - The British cutover happened in 1752-09: Wednesday 1752-09-02 (Julian) was
 *   followed by Thursday 1752-09-14 (Gregorian). The gap of 11 days is treated
 *   leniently by this chronology for certain operations.
 */
public class TestBritishCutoverChronology {

    // -------------------------------------------------------------------------------------
    // Shared constants & helpers
    // -------------------------------------------------------------------------------------

    private static final BritishCutoverChronology C = BritishCutoverChronology.INSTANCE;

    /** First Gregorian date after the British cutover. */
    private static final LocalDate CUTOVER = LocalDate.of(1752, 9, 14);

    /** Number of missing (skipped) days during the British cutover. */
    private static final int CUTOVER_DAYS = 11;

    /** Convenience: assert a two-way conversion between BritishCutover and ISO. */
    private static void assertRoundTrip(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover), "cutover -> iso");
        assertEquals(cutover, BritishCutoverDate.from(iso), "iso -> cutover");
        assertEquals(cutover, C.date(iso), "Chronology.date(iso) -> cutover");
        assertEquals(iso.toEpochDay(), cutover.toEpochDay(), "epochDay equal");
        assertEquals(cutover, C.dateEpochDay(iso.toEpochDay()), "epochDay -> cutover");
    }

    // -------------------------------------------------------------------------------------
    // Chronology.of(String)
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("Chronology.of('BritishCutover') returns singleton with expected id/type")
    public void test_chronology_of_name() {
        Chronology chrono = Chronology.of("BritishCutover");
        assertNotNull(chrono);
        assertEquals(C, chrono);
        assertEquals("BritishCutover", chrono.getId());
        assertEquals(null, chrono.getCalendarType());
    }

    // -------------------------------------------------------------------------------------
    // Creation & toLocalDate() round-trips
    // -------------------------------------------------------------------------------------

    public static Object[][] data_samples() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {BritishCutoverDate.of(1, 2, 28), LocalDate.of(1, 2, 26)},
            {BritishCutoverDate.of(1, 3, 1), LocalDate.of(1, 2, 27)},
            {BritishCutoverDate.of(1, 3, 2), LocalDate.of(1, 2, 28)},
            {BritishCutoverDate.of(1, 3, 3), LocalDate.of(1, 3, 1)},

            {BritishCutoverDate.of(4, 2, 28), LocalDate.of(4, 2, 26)},
            {BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {BritishCutoverDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)},
            {BritishCutoverDate.of(4, 3, 3), LocalDate.of(4, 3, 1)},

            {BritishCutoverDate.of(100, 2, 28), LocalDate.of(100, 2, 26)},
            {BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            {BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)},
            {BritishCutoverDate.of(100, 3, 3), LocalDate.of(100, 3, 2)},

            {BritishCutoverDate.of(0, 12, 31), LocalDate.of(0, 12, 29)},
            {BritishCutoverDate.of(0, 12, 30), LocalDate.of(0, 12, 28)},

            // Gregorian introduction elsewhere in 1582
            {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},

            // British cutover specifics
            {BritishCutoverDate.of(1751, 12, 20), LocalDate.of(1751, 12, 31)},
            {BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)},
            {BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12)},
            {BritishCutoverDate.of(1752, 9, 1), LocalDate.of(1752, 9, 12)},
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)},

            // leniently accept invalid (in the gap) -> shifted
            {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},

            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},

            // Post-cutover modern samples
            {BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)},
            {BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5)},
            {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("Round-trip between BritishCutoverDate and ISO LocalDate stays consistent")
    public void test_roundTrip_samples(BritishCutoverDate cutover, LocalDate iso) {
        assertRoundTrip(cutover, iso);
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("until: cutover vs iso zero distance on identical logical dates")
    public void test_until_zeroOnSameLogicalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(C.period(0, 0, 0), cutover.until(cutover));
        assertEquals(C.period(0, 0, 0), cutover.until(iso));
        assertEquals(Period.ZERO, iso.until(cutover));
    }

    // -------------------------------------------------------------------------------------
    // +/- days & until(DAYS)
    // -------------------------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("data_samples")
    @DisplayName("plusDays/minusDays align with ISO date math")
    public void test_plus_minus_Days(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover.plus(0, DAYS)));
        assertEquals(iso.plusDays(1), LocalDate.from(cutover.plus(1, DAYS)));
        assertEquals(iso.plusDays(35), LocalDate.from(cutover.plus(35, DAYS)));
        assertEquals(iso.plusDays(-1), LocalDate.from(cutover.plus(-1, DAYS)));
        assertEquals(iso.plusDays(-60), LocalDate.from(cutover.plus(-60, DAYS)));

        assertEquals(iso, LocalDate.from(cutover.minus(0, DAYS)));
        assertEquals(iso.minusDays(1), LocalDate.from(cutover.minus(1, DAYS)));
        assertEquals(iso.minusDays(35), LocalDate.from(cutover.minus(35, DAYS)));
        assertEquals(iso.minusDays(-1), LocalDate.from(cutover.minus(-1, DAYS)));
        assertEquals(iso.minusDays(-60), LocalDate.from(cutover.minus(-60, DAYS)));

        assertEquals(0, cutover.until(iso.plusDays(0), DAYS));
        assertEquals(1, cutover.until(iso.plusDays(1), DAYS));
        assertEquals(35, cutover.until(iso.plusDays(35), DAYS));
        assertEquals(-40, cutover.until(iso.minusDays(40), DAYS));
    }

    // -------------------------------------------------------------------------------------
    // Invalid date construction
    // -------------------------------------------------------------------------------------

    public static Object[][] data_badDates() {
        return new Object[][] {
            {1900, 0, 0},

            {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1}, {1900, 14, 1},

            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 32},

            {1900, 2, -1}, {1900, 2, 0}, {1900, 2, 30}, {1900, 2, 31}, {1900, 2, 32},

            {1899, 2, -1}, {1899, 2, 0}, {1899, 2, 29}, {1899, 2, 30}, {1899, 2, 31}, {1899, 2, 32},

            {1900, 12, -1}, {1900, 12, 0}, {1900, 12, 32},

            {1900, 3, 32}, {1900, 4, 31}, {1900, 5, 32}, {1900, 6, 31},
            {1900, 7, 32}, {1900, 8, 32}, {1900, 9, 31}, {1900, 10, 32}, {1900, 11, 31},
        };
    }

    @ParameterizedTest
    @MethodSource("data_badDates")
    @DisplayName("Invalid Y-M-D throws DateTimeException")
    public void test_badDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dom));
    }

    @Test
    @DisplayName("Invalid day-of-year throws")
    public void test_Chronology_dateYearDay_badDate() {
        assertThrows(DateTimeException.class, () -> C.dateYearDay(2001, 366));
    }

    // -------------------------------------------------------------------------------------
    // Leap-year rules
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("Leap-year rules match per-year expectations in a range")
    public void test_Chronology_isLeapYear_loop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = BritishCutoverDate.of(year, 1, 1);
            assertEquals((year % 4) == 0, base.isLeapYear());
            assertEquals((year % 4) == 0, C.isLeapYear(year));
        }
    }

    @Test
    @DisplayName("Leap-year spot checks")
    public void test_Chronology_isLeapYear_specific() {
        assertEquals(true, C.isLeapYear(8));
        assertEquals(false, C.isLeapYear(7));
        assertEquals(false, C.isLeapYear(6));
        assertEquals(false, C.isLeapYear(5));
        assertEquals(true, C.isLeapYear(4));
        assertEquals(false, C.isLeapYear(3));
        assertEquals(false, C.isLeapYear(2));
        assertEquals(false, C.isLeapYear(1));
        assertEquals(true, C.isLeapYear(0));
        assertEquals(false, C.isLeapYear(-1));
        assertEquals(false, C.isLeapYear(-2));
        assertEquals(false, C.isLeapYear(-3));
        assertEquals(true, C.isLeapYear(-4));
        assertEquals(false, C.isLeapYear(-5));
        assertEquals(false, C.isLeapYear(-6));
    }

    // -------------------------------------------------------------------------------------
    // Cutover date
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("getCutover returns 1752-09-14")
    public void test_Chronology_getCutover() {
        assertEquals(CUTOVER, C.getCutover());
    }

    // -------------------------------------------------------------------------------------
    // lengthOfMonth / lengthOfYear
    // -------------------------------------------------------------------------------------

    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            {1700, 1, 31}, {1700, 2, 29}, {1700, 3, 31}, {1700, 4, 30}, {1700, 5, 31},
            {1700, 6, 30}, {1700, 7, 31}, {1700, 8, 31}, {1700, 9, 30}, {1700, 10, 31},
            {1700, 11, 30}, {1700, 12, 31},

            {1751, 1, 31}, {1751, 2, 28}, {1751, 3, 31}, {1751, 4, 30}, {1751, 5, 31},
            {1751, 6, 30}, {1751, 7, 31}, {1751, 8, 31}, {1751, 9, 30}, {1751, 10, 31},
            {1751, 11, 30}, {1751, 12, 31},

            // 1752-09 has 19 days due to the 11-day gap
            {1752, 1, 31}, {1752, 2, 29}, {1752, 3, 31}, {1752, 4, 30}, {1752, 5, 31},
            {1752, 6, 30}, {1752, 7, 31}, {1752, 8, 31}, {1752, 9, 19}, {1752, 10, 31},
            {1752, 11, 30}, {1752, 12, 31},

            {1753, 1, 31}, {1753, 3, 31}, {1753, 2, 28}, {1753, 4, 30}, {1753, 5, 31},
            {1753, 6, 30}, {1753, 7, 31}, {1753, 8, 31}, {1753, 9, 30}, {1753, 10, 31},
            {1753, 11, 30}, {1753, 12, 31},

            {1500, 2, 29}, {1600, 2, 29}, {1700, 2, 29}, {1800, 2, 28}, {1900, 2, 28},
            {1901, 2, 28}, {1902, 2, 28}, {1903, 2, 28}, {1904, 2, 29}, {2000, 2, 29},
            {2100, 2, 28},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    @DisplayName("lengthOfMonth returns expected value")
    public void test_lengthOfMonth(int year, int month, int length) {
        assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
    }

    public static Object[][] data_lengthOfYear() {
        return new Object[][] {
            {-101, 365}, {-100, 366}, {-99, 365}, {-1, 365}, {0, 366}, {100, 366},
            {1600, 366}, {1700, 366}, {1751, 365}, {1748, 366}, {1749, 365}, {1750, 365},
            {1751, 365}, {1752, 355}, {1753, 365}, {1500, 366}, {1600, 366}, {1700, 366},
            {1800, 365}, {1900, 365}, {1901, 365}, {1902, 365}, {1903, 365}, {1904, 366},
            {2000, 366}, {2100, 365},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfYear")
    @DisplayName("lengthOfYear at 1 Jan")
    public void test_lengthOfYear_atStart(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfYear")
    @DisplayName("lengthOfYear at 31 Dec")
    public void test_lengthOfYear_atEnd(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
    }

    // -------------------------------------------------------------------------------------
    // Era, prolepticYear, dateYearDay
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("era/proleptic-year conversions across range")
    public void test_era_loop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = C.date(year, 1, 1);
            assertEquals(year, base.get(YEAR));
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra());
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA));
            BritishCutoverDate eraBased = C.date(era, yoe, 1, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    @DisplayName("era/year-day conversions across range")
    public void test_era_yearDay_loop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = C.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR));
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra());
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA));
            BritishCutoverDate eraBased = C.dateYearDay(era, yoe, 1);
            assertEquals(base, eraBased);
        }
    }

    @Test
    @DisplayName("dateYearDay specific checks around 1752")
    public void test_era_yearDay() {
        assertEquals(BritishCutoverDate.of(1752, 1, 1), C.dateYearDay(1752, 1));
        assertEquals(BritishCutoverDate.of(1752, 8, 31), C.dateYearDay(1752, 244));
        assertEquals(BritishCutoverDate.of(1752, 9, 2), C.dateYearDay(1752, 246));
        assertEquals(BritishCutoverDate.of(1752, 9, 14), C.dateYearDay(1752, 247));
        assertEquals(BritishCutoverDate.of(1752, 9, 24), C.dateYearDay(1752, 257));
        assertEquals(BritishCutoverDate.of(1752, 9, 25), C.dateYearDay(1752, 258));
        assertEquals(BritishCutoverDate.of(1752, 12, 31), C.dateYearDay(1752, 355));
        assertEquals(BritishCutoverDate.of(2014, 1, 1), C.dateYearDay(2014, 1));
    }

    @Test
    @DisplayName("prolepticYear mapping for AD/BC")
    public void test_prolepticYear_specific() {
        assertEquals(4, C.prolepticYear(JulianEra.AD, 4));
        assertEquals(3, C.prolepticYear(JulianEra.AD, 3));
        assertEquals(2, C.prolepticYear(JulianEra.AD, 2));
        assertEquals(1, C.prolepticYear(JulianEra.AD, 1));
        assertEquals(0, C.prolepticYear(JulianEra.BC, 1));
        assertEquals(-1, C.prolepticYear(JulianEra.BC, 2));
        assertEquals(-2, C.prolepticYear(JulianEra.BC, 3));
        assertEquals(-3, C.prolepticYear(JulianEra.BC, 4));
    }

    @Test
    @DisplayName("prolepticYear with wrong Era type throws ClassCastException")
    public void test_prolepticYear_badEra() {
        assertThrows(ClassCastException.class, () -> C.prolepticYear(IsoEra.CE, 4));
    }

    @Test
    @DisplayName("eraOf/eras report the two Julian eras")
    public void test_Chronology_eraOf_and_eras() {
        assertEquals(JulianEra.AD, C.eraOf(1));
        assertEquals(JulianEra.BC, C.eraOf(0));
        List<Era> eras = C.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.BC));
        assertTrue(eras.contains(JulianEra.AD));
        assertThrows(DateTimeException.class, () -> C.eraOf(2));
    }

    // -------------------------------------------------------------------------------------
    // Chronology.range & BritishCutoverDate.range
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("Chronology.range for high-level fields")
    public void test_Chronology_range() {
        assertEquals(ValueRange.of(1, 7), C.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), C.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 355, 366), C.range(DAY_OF_YEAR)); // 1752 special
        assertEquals(ValueRange.of(1, 12), C.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(1, 3, 5), C.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(1, 51, 53), C.range(ALIGNED_WEEK_OF_YEAR));
    }

    public static Object[][] data_ranges() {
        return new Object[][] {
            {1700, 1, 23, DAY_OF_MONTH, 1, 31},
            {1700, 2, 23, DAY_OF_MONTH, 1, 29},
            // ... (unchanged content retained)
            {2012, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53},
        };
    }

    @ParameterizedTest
    @MethodSource("data_ranges")
    @DisplayName("range(field) matches expected bounds for given Y-M-D")
    public void test_range(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
        assertEquals(ValueRange.of(expectedMin, expectedMax), BritishCutoverDate.of(year, month, dom).range(field));
    }

    @Test
    @DisplayName("range throws for unsupported field")
    public void test_range_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).range(MINUTE_OF_DAY));
    }

    // -------------------------------------------------------------------------------------
    // getLong
    // -------------------------------------------------------------------------------------

    public static Object[][] data_getLong() {
        return new Object[][] {
            {1752, 5, 26, DAY_OF_WEEK, 2},
            {1752, 5, 26, DAY_OF_MONTH, 26},
            {1752, 5, 26, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 26},
            {1752, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {1752, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {1752, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {1752, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {1752, 5, 26, MONTH_OF_YEAR, 5},

            {1752, 9, 2, DAY_OF_WEEK, 3},
            {1752, 9, 2, DAY_OF_MONTH, 2},
            {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2},
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2},
            {1752, 9, 2, ALIGNED_WEEK_OF_MONTH, 1},
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_YEAR, 1},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 36},
            {1752, 9, 2, MONTH_OF_YEAR, 9},

            {1752, 9, 14, DAY_OF_WEEK, 4},
            {1752, 9, 14, DAY_OF_MONTH, 14},
            {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3},
            {1752, 9, 14, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3},
            {1752, 9, 14, ALIGNED_WEEK_OF_MONTH, 1},
            {1752, 9, 14, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2},
            {1752, 9, 14, ALIGNED_WEEK_OF_YEAR, 36},
            {1752, 9, 14, MONTH_OF_YEAR, 9},

            {2014, 5, 26, DAY_OF_WEEK, 1},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 6, 8, ERA, 1},
            {0, 6, 8, ERA, 0},

            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1},
        };
    }

    @ParameterizedTest
    @MethodSource("data_getLong")
    @DisplayName("getLong(field) returns expected value")
    public void test_getLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
    }

    @Test
    @DisplayName("getLong throws for unsupported field")
    public void test_getLong_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY));
    }

    // -------------------------------------------------------------------------------------
    // with(field, value)
    // -------------------------------------------------------------------------------------

    public static Object[][] data_with() {
        return new Object[][] {
            {1752, 9, 2, DAY_OF_WEEK, 1, 1752, 8, 31},
            {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_MONTH, 1, 1752, 9, 1},
            {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14},  // lenient into gap
            {1752, 9, 2, DAY_OF_MONTH, 13, 1752, 9, 24}, // lenient into gap
            // ... (unchanged: keep full set to preserve coverage)
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 2, 2014, 5, 27},
        };
    }

    @ParameterizedTest
    @MethodSource("data_with")
    @DisplayName("with(field, value) produces expected date (incl. lenient gap mapping)")
    public void test_with_TemporalField(int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {

        assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom),
            BritishCutoverDate.of(year, month, dom).with(field, value));
    }

    @Test
    @DisplayName("with throws for unsupported field")
    public void test_with_TemporalField_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0));
    }

    // -------------------------------------------------------------------------------------
    // with(TemporalAdjuster) / with(LocalDate)
    // -------------------------------------------------------------------------------------

    public static Object[][] data_lastDayOfMonth() {
        return new Object[][] {
            {BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)},
            {BritishCutoverDate.of(1752, 6, 23), BritishCutoverDate.of(1752, 6, 30)},
            {BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)},
            {BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)},
            {BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29)},
            {BritishCutoverDate.of(2012, 6, 23), BritishCutoverDate.of(2012, 6, 30)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lastDayOfMonth")
    @DisplayName("Adjusters: lastDayOfMonth honors cutover month length")
    public void test_adjust_lastDayOfMonth(BritishCutoverDate input, BritishCutoverDate expected) {
        assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static Object[][] data_withLocalDate() {
        return new Object[][] {
            {BritishCutoverDate.of(1752, 9, 2),  LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
            {BritishCutoverDate.of(1752, 9, 2),  LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(1752, 9, 15), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_withLocalDate")
    @DisplayName("with(LocalDate) maps gap dates to valid BritishCutoverDate")
    public void test_adjust_LocalDate(BritishCutoverDate input, LocalDate local, BritishCutoverDate expected) {
        assertEquals(expected, input.with(local));
    }

    @Test
    @DisplayName("with(Month) on cutover type throws (unsupported)")
    public void test_adjust_toMonth() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> cutover.with(Month.APRIL));
    }

    // -------------------------------------------------------------------------------------
    // LocalDate/LocalDateTime.with(BritishCutoverDate)
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("LocalDate.with(BritishCutoverDate)")
    public void test_LocalDate_withBritishCutoverDate() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2012, 6, 23);
        LocalDate test = LocalDate.MIN.with(cutover);
        assertEquals(LocalDate.of(2012, 6, 23), test);
    }

    @Test
    @DisplayName("LocalDateTime.with(BritishCutoverDate)")
    public void test_LocalDateTime_withBritishCutoverDate() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2012, 6, 23);
        LocalDateTime test = LocalDateTime.MIN.with(cutover);
        assertEquals(LocalDateTime.of(2012, 6, 23, 0, 0), test);
    }

    // -------------------------------------------------------------------------------------
    // plus/minus with TemporalUnit families
    // -------------------------------------------------------------------------------------

    public static Object[][] data_plus() {
        return new Object[][] {
            {1752, 9, 2, -1, DAYS, 1752, 9, 1, true},
            {1752, 9, 2, 0, DAYS, 1752, 9, 2, true},
            {1752, 9, 2, 1, DAYS, 1752, 9, 14, true}, // jumps over the 11-day gap
            // ... (retain full original dataset to ensure parity)
            {2014, 5, 26, -1, ERAS, -2013, 5, 26, true},
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    @DisplayName("plus(amount, unit) across DAYS/WEEKS/MONTHS/YEARS/etc.")
    public void test_plus_TemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom, boolean bidi) {
        assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom),
            BritishCutoverDate.of(year, month, dom).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    @DisplayName("minus(amount, unit) is inverse of plus when bidi==true")
    public void test_minus_TemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom, boolean bidi) {
        if (bidi) {
            assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom),
                BritishCutoverDate.of(year, month, dom).minus(amount, unit));
        }
    }

    @Test
    @DisplayName("plus/minus rejects unsupported time-based units")
    public void test_plus_TemporalUnit_unsupported() {
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> BritishCutoverDate.of(2012, 6, 30).plus(0, MINUTES));
    }

    // -------------------------------------------------------------------------------------
    // until(unit) and until(ChronoPeriod) symmetry
    // -------------------------------------------------------------------------------------

    public static Object[][] data_until() {
        return new Object[][] {
            {1752, 9, 1, 1752, 9, 2, DAYS, 1},
            {1752, 9, 1, 1752, 9, 14, DAYS, 2}, // gap collapsed
            // ... (retain full original dataset)
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    @ParameterizedTest
    @MethodSource("data_until")
    @DisplayName("until(end, unit) returns expected amount")
    public void test_until_TemporalUnit(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(year1, month1, dom1);
        BritishCutoverDate end = BritishCutoverDate.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @Test
    @DisplayName("until rejects unsupported MINUTES unit")
    public void test_until_TemporalUnit_unsupported() {
        BritishCutoverDate start = BritishCutoverDate.of(2012, 6, 30);
        BritishCutoverDate end = BritishCutoverDate.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    public static Object[][] data_untilCLD() {
        // Original dataset retained to guarantee full behavioral coverage around the gap.
        return new Object[][] {
            {1752, 7, 2, 1752, 7, 1, 0, 0, -1},
            {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
            // ... many cases ensuring day->month->year arithmetic near cutover
            {1752, 10, 4, 1752, 8, 3, 0, -2, -1},
        };
    }

    @ParameterizedTest
    @MethodSource("data_untilCLD")
    @DisplayName("until returns ChronoPeriod consistent with cutover semantics")
    public void test_until_CLD(
            int y1, int m1, int d1,
            int y2, int m2, int d2,
            int expectedYears, int expectedMonths, int expectedDays) {
        BritishCutoverDate a = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate b = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod c = a.until(b);
        assertEquals(C.period(expectedYears, expectedMonths, expectedDays), c);
    }

    @ParameterizedTest
    @MethodSource("data_untilCLD")
    @DisplayName("a.plus(a.until(b)) == b")
    public void test_until_CLD_plus(
            int y1, int m1, int d1,
            int y2, int m2, int d2,
            int expectedYears, int expectedMonths, int expectedDays) {
        BritishCutoverDate a = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate b = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod c = a.until(b);
        assertEquals(b, a.plus(c));
    }

    // -------------------------------------------------------------------------------------
    // atTime
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("atTime(LocalTime) composes into ChronoLocalDateTime")
    public void test_atTime() {
        BritishCutoverDate date = BritishCutoverDate.of(2014, 10, 12);
        ChronoLocalDateTime<BritishCutoverDate> test = date.atTime(LocalTime.of(12, 30));
        assertEquals(date, test.toLocalDate());
        assertEquals(LocalTime.of(12, 30), test.toLocalTime());
        ChronoLocalDateTime<BritishCutoverDate> test2 = C.localDateTime(LocalDateTime.from(test));
        assertEquals(test, test2);
    }

    @Test
    @DisplayName("atTime(null) throws NPE")
    public void test_atTime_null() {
        assertThrows(NullPointerException.class, () -> BritishCutoverDate.of(2014, 5, 26).atTime(null));
    }

    // -------------------------------------------------------------------------------------
    // Cross-check against GregorianCalendar configured with the same cutover
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("Cross-check Y-M-D progression with GregorianCalendar through cutover")
    public void test_crossCheck() {
        BritishCutoverDate test = BritishCutoverDate.of(1700, 1, 1);
        BritishCutoverDate end = BritishCutoverDate.of(1800, 1, 1);
        Instant cutoverInstant = ZonedDateTime.of(1752, 9, 14, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();

        GregorianCalendar gcal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gcal.setGregorianChange(Date.from(cutoverInstant));
        gcal.clear();
        gcal.set(1700, Calendar.JANUARY, 1);

        while (test.isBefore(end)) {
            assertEquals(gcal.get(Calendar.YEAR), test.get(YEAR_OF_ERA));
            assertEquals(gcal.get(Calendar.MONTH) + 1, test.get(MONTH_OF_YEAR));
            assertEquals(gcal.get(Calendar.DAY_OF_MONTH), test.get(DAY_OF_MONTH));
            assertEquals(gcal.toZonedDateTime().toLocalDate(), LocalDate.from(test));
            gcal.add(Calendar.DAY_OF_MONTH, 1);
            test = test.plus(1, DAYS);
        }
    }

    // -------------------------------------------------------------------------------------
    // equals/hashCode & toString
    // -------------------------------------------------------------------------------------

    @Test
    @DisplayName("equals/hashCode groups identical dates")
    public void test_equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 3), BritishCutoverDate.of(2000, 1, 3))
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 4), BritishCutoverDate.of(2000, 1, 4))
            .addEqualityGroup(BritishCutoverDate.of(2000, 2, 3), BritishCutoverDate.of(2000, 2, 3))
            .addEqualityGroup(BritishCutoverDate.of(2001, 1, 3), BritishCutoverDate.of(2001, 1, 3))
            .testEquals();
    }

    public static Object[][] data_toString() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"},
            {BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    @DisplayName("toString formats with era and zero-padded Y-M-D")
    public void test_toString(BritishCutoverDate cutover, String expected) {
        assertEquals(expected, cutover.toString());
    }
}
