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
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Readable tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 *
 * Notes on the International Fixed Calendar (IFC):
 *  - 13 months per year, months 1..13
 *  - Months normally have 28 days (4×7).
 *  - Year Day is 13/29 (exists every year) – it is not part of a week.
 *  - Leap Day is 6/29 (only in leap years) – also not part of a week.
 *  - Weekdays run 1..7 in ordinary days; Leap/Year Day report 0 for aligned “week” fields.
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestInternationalFixedChronologyReadable {

    // ------------------------------------------------------------
    // Constants & helpers to reduce noise in tests
    // ------------------------------------------------------------

    private static final InternationalFixedChronology IFC = InternationalFixedChronology.INSTANCE;

    // Special months/days in IFC
    private static final int LEAP_MONTH = 6;
    private static final int YEAR_DAY_MONTH = 13;
    private static final int DAY_28 = 28;
    private static final int DAY_29 = 29;

    // Common TemporalUnits used throughout
    private static final TemporalUnit[] DATE_UNITS = {DAYS, WEEKS, MONTHS, YEARS, DECADES, CENTURIES, MILLENNIA};

    // Factory helpers (short & explicit)
    private static InternationalFixedDate fixed(int y, int m, int d) {
        return InternationalFixedDate.of(y, m, d);
    }
    private static LocalDate iso(int y, int m, int d) {
        return LocalDate.of(y, m, d);
    }

    /** Group several assertions so method bodies stay small and intent-first. */
    private static void assertAllOf(String heading, Executable... checks) {
        assertAll(heading, checks);
    }

    private static void assertSameDateMapping(InternationalFixedDate ifc, LocalDate g) {
        assertAllOf("IFC/ISO conversion must be symmetric",
            () -> assertEquals(g, LocalDate.from(ifc), "LocalDate.from(IFC)"),
            () -> assertEquals(ifc, InternationalFixedDate.from(g), "InternationalFixedDate.from(ISO)"),
            () -> assertEquals(ifc, IFC.date(g), "Chronology.date(ISO)"),
            () -> assertEquals(g.toEpochDay(), ifc.toEpochDay(), "epochDay equality"),
            () -> assertEquals(ifc, IFC.dateEpochDay(g.toEpochDay()), "Chronology.dateEpochDay(epoch)")
        );
    }

    // ------------------------------------------------------------
    // Chronology identity & lookup
    // ------------------------------------------------------------

    @Test
    void chronology_id_and_singleton() {
        Chronology c = Chronology.of("Ifc");
        assertAllOf("Chronology basics",
            () -> assertNotNull(c),
            () -> assertEquals(IFC, c),
            () -> assertEquals("Ifc", c.getId()),
            () -> assertNull(c.getCalendarType())
        );
    }

    // ------------------------------------------------------------
    // Sample conversions IFC <-> ISO
    // ------------------------------------------------------------

    public static Object[][] data_samples() {
        return new Object[][]{
            {fixed(1, 1, 1), iso(1, 1, 1)},
            {fixed(1, 1, 2), iso(1, 1, 2)},

            // Month 6 boundary around Leap Day
            {fixed(1, 6, 27), iso(1, 6, 16)},
            {fixed(1, 6, 28), iso(1, 6, 17)},
            {fixed(1, 7, 1),  iso(1, 6, 18)},
            {fixed(1, 7, 2),  iso(1, 6, 19)},

            // Year Day (13/29) maps to ISO 12/31
            {fixed(1, 13, 27), iso(1, 12, 29)},
            {fixed(1, 13, 28), iso(1, 12, 30)},
            {fixed(1, 13, 29), iso(1, 12, 31)},
            {fixed(2, 1, 1),   iso(2, 1, 1)},

            // Selected other years (including Gregorian-switch era)
            {fixed(4, 6, 27),  iso(4, 6, 15)},
            {fixed(4, 6, 28),  iso(4, 6, 16)},
            {fixed(4, 6, 29),  iso(4, 6, 17)},
            {fixed(4, 7, 1),   iso(4, 6, 18)},
            {fixed(4, 7, 2),   iso(4, 6, 19)},

            {fixed(4, 13, 27), iso(4, 12, 29)},
            {fixed(4, 13, 28), iso(4, 12, 30)},
            {fixed(4, 13, 29), iso(4, 12, 31)},
            {fixed(5, 1, 1),   iso(5, 1, 1)},

            {fixed(100, 6, 27), iso(100, 6, 16)},
            {fixed(100, 6, 28), iso(100, 6, 17)},
            {fixed(100, 7, 1),  iso(100, 6, 18)},
            {fixed(100, 7, 2),  iso(100, 6, 19)},

            {fixed(400, 6, 27), iso(400, 6, 15)},
            {fixed(400, 6, 28), iso(400, 6, 16)},
            {fixed(400, 6, 29), iso(400, 6, 17)},
            {fixed(400, 7, 1),  iso(400, 6, 18)},
            {fixed(400, 7, 2),  iso(400, 6, 19)},

            {fixed(1582, 9, 28), iso(1582, 9, 9)},
            {fixed(1582, 10, 1), iso(1582, 9, 10)},
            {fixed(1945, 10, 27), iso(1945, 10, 6)},

            {fixed(2012, 6, 15), iso(2012, 6, 3)},
            {fixed(2012, 6, 16), iso(2012, 6, 4)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    void conversion_is_symmetric(InternationalFixedDate ifc, LocalDate g) {
        assertSameDateMapping(ifc, g);
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    void until_zero_on_same_date(InternationalFixedDate ifc, LocalDate g) {
        assertAllOf("IFC.until on equal dates is zero",
            () -> assertEquals(IFC.period(0, 0, 0), ifc.until(ifc)),
            () -> assertEquals(IFC.period(0, 0, 0), ifc.until(g)),
            () -> assertEquals(Period.ZERO, g.until(ifc))
        );
    }

    // ------------------------------------------------------------
    // Plus/minus days around boundaries (defensive samples)
    // ------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("data_samples")
    void plus_days_matches_iso(InternationalFixedDate ifc, LocalDate g) {
        assertAllOf("plus days",
            () -> assertEquals(g, LocalDate.from(ifc.plus(0, DAYS))),
            () -> assertEquals(g.plusDays(1), LocalDate.from(ifc.plus(1, DAYS))),
            () -> assertEquals(g.plusDays(35), LocalDate.from(ifc.plus(35, DAYS))),
            () -> {
                if (LocalDate.ofYearDay(1, 60).isBefore(g)) {
                    assertEquals(g.minusDays(1), LocalDate.from(ifc.plus(-1, DAYS)));
                    assertEquals(g.minusDays(60), LocalDate.from(ifc.plus(-60, DAYS)));
                }
            }
        );
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    void minus_days_matches_iso(InternationalFixedDate ifc, LocalDate g) {
        assertAllOf("minus days",
            () -> assertEquals(g, LocalDate.from(ifc.minus(0, DAYS))),
            () -> {
                if (LocalDate.ofYearDay(1, 35).isBefore(g)) {
                    assertEquals(g.minusDays(1), LocalDate.from(ifc.minus(1, DAYS)));
                    assertEquals(g.minusDays(35), LocalDate.from(ifc.minus(35, DAYS)));
                }
            },
            () -> assertEquals(g.plusDays(1), LocalDate.from(ifc.minus(-1, DAYS))),
            () -> assertEquals(g.plusDays(60), LocalDate.from(ifc.minus(-60, DAYS)))
        );
    }

    @ParameterizedTest
    @MethodSource("data_samples")
    void until_in_days_matches_iso(InternationalFixedDate ifc, LocalDate g) {
        assertAllOf("until DAYS",
            () -> assertEquals(0, ifc.until(g.plusDays(0), DAYS)),
            () -> assertEquals(1, ifc.until(g.plusDays(1), DAYS)),
            () -> assertEquals(35, ifc.until(g.plusDays(35), DAYS)),
            () -> {
                if (LocalDate.ofYearDay(1, 40).isBefore(g)) {
                    assertEquals(-40, ifc.until(g.minusDays(40), DAYS));
                }
            }
        );
    }

    // ------------------------------------------------------------
    // Invalid inputs
    // ------------------------------------------------------------

    public static Object[][] data_badDates() {
        return new Object[][] {
            {-1, 13, 28}, {-1, 13, 29}, {0, 1, 1},

            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},

            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29},

            {1904, -1, -2}, {1904, -1, 0}, {1904, -1, 1},

            {1900, -1, 0}, {1900, -1, -2},

            {1900, 0, -1}, {1900, 0, 1}, {1900, 0, 2},

            // 29th only in month 6 (leap years) and month 13 (year day)
            {1900, 2, 29}, {1900, 3, 29}, {1900, 4, 29}, {1900, 5, 29}, {1900, 6, 29},
            {1900, 7, 29}, {1900, 8, 29}, {1900, 9, 29}, {1900, 10, 29}, {1900, 11, 29},
            {1900, 12, 29}, {1900, 13, 30},
        };
    }

    @ParameterizedTest
    @MethodSource("data_badDates")
    void creating_bad_dates_throws(int y, int m, int d) {
        assertThrows(DateTimeException.class, () -> fixed(y, m, d));
    }

    public static Object[][] data_badLeapDates() {
        return new Object[][] {{1}, {100}, {200}, {300}, {1900}};
    }

    @ParameterizedTest
    @MethodSource("data_badLeapDates")
    void leap_day_only_in_leap_years(int year) {
        assertThrows(DateTimeException.class, () -> fixed(year, LEAP_MONTH, DAY_29));
    }

    @Test
    void chronology_dateYearDay_rejects_out_of_range() {
        assertThrows(DateTimeException.class, () -> IFC.dateYearDay(2001, 366));
    }

    // ------------------------------------------------------------
    // Leap year logic mirrors ISO
    // ------------------------------------------------------------

    @Test
    void isLeapYear_matches_gregorian_rule() {
        IntPredicate isoLeap = y -> ((y & 3) == 0) && ((y % 100) != 0 || (y % 400) == 0);

        for (int y = 1; y < 500; y++) {
            InternationalFixedDate jan1 = fixed(y, 1, 1);
            assertAllOf("year " + y,
                () -> assertEquals(isoLeap.test(y), jan1.isLeapYear()),
                () -> assertEquals(isoLeap.test(y) ? 366 : 365, jan1.lengthOfYear()),
                () -> assertEquals(isoLeap.test(y), IFC.isLeapYear(y))
            );
        }
    }

    @Test
    void isLeapYear_specific_cases() {
        assertTrue(IFC.isLeapYear(400));
        assertFalse(IFC.isLeapYear(100));
        assertTrue(IFC.isLeapYear(4));
        assertFalse(IFC.isLeapYear(3));
        assertFalse(IFC.isLeapYear(2));
        assertFalse(IFC.isLeapYear(1));
    }

    // ------------------------------------------------------------
    // Lengths & ranges
    // ------------------------------------------------------------

    public static Object[][] data_lengthOfMonth() {
        return new Object[][]{
            {1900, 1, DAY_28, 28},
            {1900, 2, DAY_28, 28},
            {1900, 3, DAY_28, 28},
            {1900, 4, DAY_28, 28},
            {1900, 5, DAY_28, 28},
            {1900, 6, DAY_28, 28},
            {1900, 7, DAY_28, 28},
            {1900, 8, DAY_28, 28},
            {1900, 9, DAY_28, 28},
            {1900, 10, DAY_28, 28},
            {1900, 11, DAY_28, 28},
            {1900, 12, DAY_28, 28},
            {1900, 13, DAY_29, 29},
            {1904, 6, DAY_29, 29},
        };
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    void length_of_month_on_given_day(int y, int m, int d, int expectedLen) {
        assertEquals(expectedLen, fixed(y, m, d).lengthOfMonth());
    }

    @ParameterizedTest
    @MethodSource("data_lengthOfMonth")
    void length_of_month_on_first_day(int y, int m, int ignored, int expectedLen) {
        assertEquals(expectedLen, fixed(y, m, 1).lengthOfMonth());
    }

    @Test
    void length_of_month_specifics() {
        assertEquals(29, fixed(1900, YEAR_DAY_MONTH, DAY_29).lengthOfMonth());
        assertEquals(29, fixed(2000, YEAR_DAY_MONTH, DAY_29).lengthOfMonth());
        assertEquals(29, fixed(2000, LEAP_MONTH, DAY_29).lengthOfMonth());
    }

    // Era & year resolving

    @Test
    void valid_era() {
        Era era = IFC.eraOf(1);
        assertNotNull(era);
        assertEquals(1, era.getValue());
    }

    public static Object[][] data_invalidEraValues() {
        return new Object[][] {{-1}, {0}, {2}};
    }

    @ParameterizedTest
    @MethodSource("data_invalidEraValues")
    void invalid_era_values_throw(int eraValue) {
        assertThrows(DateTimeException.class, () -> IFC.eraOf(eraValue));
    }

    @Test
    void era_year_and_dateYearDay_are_consistent() {
        for (int y = 1; y < 200; y++) {
            InternationalFixedDate d = IFC.date(y, 1, 1);
            InternationalFixedEra era = InternationalFixedEra.CE;
            assertAllOf("era/year " + y,
                () -> assertEquals(y, d.get(YEAR)),
                () -> assertEquals(era, d.getEra()),
                () -> assertEquals(y, d.get(YEAR_OF_ERA)),
                () -> assertEquals(d, IFC.date(era, y, 1, 1))
            );

            InternationalFixedDate firstDay = IFC.dateYearDay(y, 1);
            assertAllOf("yearDay " + y,
                () -> assertEquals(y, firstDay.get(YEAR)),
                () -> assertEquals(era, firstDay.getEra()),
                () -> assertEquals(y, firstDay.get(YEAR_OF_ERA)),
                () -> assertEquals(firstDay, IFC.dateYearDay(era, y, 1))
            );
        }
    }

    @Test
    void prolepticYear_specifics() {
        assertEquals(4, IFC.prolepticYear(InternationalFixedEra.CE, 4));
        assertEquals(3, IFC.prolepticYear(InternationalFixedEra.CE, 3));
        assertEquals(2, IFC.prolepticYear(InternationalFixedEra.CE, 2));
        assertEquals(1, IFC.prolepticYear(InternationalFixedEra.CE, 1));
        assertEquals(2000, IFC.prolepticYear(InternationalFixedEra.CE, 2000));
        assertEquals(1582, IFC.prolepticYear(InternationalFixedEra.CE, 1582));
    }

    public static Object[][] data_prolepticYear_bad() {
        return new Object[][] {{-10}, {-1}, {0}};
    }

    @ParameterizedTest
    @MethodSource("data_prolepticYear_bad")
    void prolepticYear_rejects_non_positive(int y) {
        assertThrows(DateTimeException.class, () -> IFC.prolepticYear(InternationalFixedEra.CE, y));
    }

    @Test
    void prolepticYear_wrong_era_type_throws() {
        assertThrows(ClassCastException.class, () -> IFC.prolepticYear(IsoEra.CE, 4));
    }

    @Test
    void chronology_era_queries() {
        assertEquals(InternationalFixedEra.CE, IFC.eraOf(1));
        assertThrows(DateTimeException.class, () -> IFC.eraOf(0));
        List<Era> eras = IFC.eras();
        assertEquals(1, eras.size());
        assertTrue(eras.contains(InternationalFixedEra.CE));
    }

    @Test
    void chronology_ranges_cover_all_supported_fields() {
        assertAllOf("range checks",
            () -> assertEquals(ValueRange.of(0, 1, 0, 7), IFC.range(ALIGNED_DAY_OF_WEEK_IN_MONTH)),
            () -> assertEquals(ValueRange.of(0, 1, 0, 7), IFC.range(ALIGNED_DAY_OF_WEEK_IN_YEAR)),
            () -> assertEquals(ValueRange.of(0, 1, 0, 4), IFC.range(ALIGNED_WEEK_OF_MONTH)),
            () -> assertEquals(ValueRange.of(0, 1, 0, 52), IFC.range(ALIGNED_WEEK_OF_YEAR)),
            () -> assertEquals(ValueRange.of(0, 1, 0, 7), IFC.range(DAY_OF_WEEK)),
            () -> assertEquals(ValueRange.of(1, 29), IFC.range(DAY_OF_MONTH)),
            () -> assertEquals(ValueRange.of(1, 365, 366), IFC.range(DAY_OF_YEAR)),
            () -> assertEquals(ValueRange.of(1, 1), IFC.range(ERA)),
            () -> assertEquals(ValueRange.of(-719_528, 1_000_000 * 365L + 242_499 - 719_528), IFC.range(EPOCH_DAY)),
            () -> assertEquals(ValueRange.of(1, 13), IFC.range(MONTH_OF_YEAR)),
            () -> assertEquals(ValueRange.of(13, 1_000_000 * 13L - 1), IFC.range(PROLEPTIC_MONTH)),
            () -> assertEquals(ValueRange.of(1, 1_000_000), IFC.range(YEAR)),
            () -> assertEquals(ValueRange.of(1, 1_000_000), IFC.range(YEAR_OF_ERA))
        );
    }

    // ------------------------------------------------------------
    // Range/getLong/with(...) datasets and checks
    // ------------------------------------------------------------

    // Keeping the original datasets and assertions below to preserve coverage.
    // Only the naming and comments have been improved for clarity.

    public static Object[][] data_ranges() {
        return new Object[][]{
            // Leap Day & Year Day belong to months but are outside the week cycle.
            {2012, 6, DAY_29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 13, DAY_29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 3, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 4, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 8, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 9, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 10, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 11, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)},

            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},

            // Aligned day/week in month/year: Leap/Year Day => empty range [0,0]
            {2012, 6, DAY_29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            {2012, 13, DAY_29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},

            {2012, 6, DAY_29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},
            {2012, 13, DAY_29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 6, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},

            {2012, 6, DAY_29, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(0, 0)},
            {2012, 13, DAY_29, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},

            {2012, 6, DAY_29, ALIGNED_WEEK_OF_YEAR, ValueRange.of(0, 0)},
            {2012, 13, DAY_29, ALIGNED_WEEK_OF_YEAR, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 6, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 12, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},

            {2012, 6, DAY_29, DAY_OF_WEEK, ValueRange.of(0, 0)},
            {2012, 13, DAY_29, DAY_OF_WEEK, ValueRange.of(0, 0)},
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 12, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

            {2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            {2011, 13, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
        };
    }

    @ParameterizedTest
    @MethodSource("data_ranges")
    void field_range_matches_expectation(int y, int m, int d, TemporalField field, ValueRange range) {
        assertEquals(range, fixed(y, m, d).range(field));
    }

    @Test
    void range_unsupported_field_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> fixed(2012, LEAP_MONTH, DAY_28).range(MINUTE_OF_DAY));
    }

    // --- getLong / with / adjusters / plus-minus / until / period ---
    // For brevity, the original datasets are kept intact to preserve case coverage.

    public static Object[][] data_getLong() { /* unchanged dataset from original */ return org.threeten.extra.chrono.TestInternationalFixedChronology.data_getLong(); }

    @ParameterizedTest
    @MethodSource("data_getLong")
    void getLong_returns_expected(int y, int m, int d, TemporalField field, long expected) {
        assertEquals(expected, fixed(y, m, d).getLong(field));
    }

    @Test
    void getLong_unsupported_field_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> fixed(2012, LEAP_MONTH, DAY_28).getLong(MINUTE_OF_DAY));
    }

    public static Object[][] data_with() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_with(); }
    public static Object[][] data_with_bad() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_with_bad(); }

    @ParameterizedTest
    @MethodSource("data_with")
    void with_field_sets_expected_value(
            int y, int m, int d, TemporalField field, long value,
            int ey, int em, int ed) {
        assertEquals(fixed(ey, em, ed), fixed(y, m, d).with(field, value));
    }

    @ParameterizedTest
    @MethodSource("data_with_bad")
    void with_field_rejects_bad_values(int y, int m, int d, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> fixed(y, m, d).with(field, value));
    }

    @Test
    void with_unsupported_field_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> fixed(2012, LEAP_MONTH, DAY_28).with(MINUTE_OF_DAY, 0));
    }

    public static Object[][] data_temporalAdjusters_lastDayOfMonth() {
        return new Object[][]{
            {2012, 6, 23, 2012, 6, 29},
            {2012, 6, 29, 2012, 6, 29},
            {2009, 6, 23, 2009, 6, 28},
            {2007, 13, 23, 2007, 13, 29},
            {2005, 13, 29, 2005, 13, 29},
        };
    }

    @ParameterizedTest
    @MethodSource("data_temporalAdjusters_lastDayOfMonth")
    void temporalAdjusters_lastDayOfMonth_works(
            int y, int m, int d, int ey, int em, int ed) {
        InternationalFixedDate base = fixed(y, m, d);
        assertEquals(fixed(ey, em, ed), base.with(TemporalAdjusters.lastDayOfMonth()));
    }

    // Local* adjusters

    @Test
    void adjust_toLocalDate() {
        InternationalFixedDate base = fixed(2000, 1, 4);
        InternationalFixedDate adjusted = base.with(iso(2012, 7, 6));
        assertEquals(fixed(2012, 7, 19), adjusted);
    }

    @Test
    void adjust_toMonth_rejected() {
        InternationalFixedDate base = fixed(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> base.with(Month.APRIL));
    }

    @Test
    void localDate_adjust_to_ifc() {
        InternationalFixedDate target = fixed(2012, 7, 19);
        LocalDate adjusted = LocalDate.MIN.with(target);
        assertEquals(iso(2012, 7, 6), adjusted);
    }

    @Test
    void localDateTime_adjust_to_ifc() {
        InternationalFixedDate target = fixed(2012, 7, 19);
        LocalDateTime adjusted = LocalDateTime.MIN.with(target);
        assertEquals(LocalDateTime.of(2012, 7, 6, 0, 0), adjusted);
    }

    // plus/minus datasets

    public static Object[][] data_plus() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_plus(); }
    public static Object[][] data_plus_leap_and_year_day() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_plus_leap_and_year_day(); }
    public static Object[][] data_minus_leap_and_year_day() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_minus_leap_and_year_day(); }

    @ParameterizedTest
    @MethodSource("data_plus")
    void plus_by_unit_matches_expected(
            int y, int m, int d, long amount, TemporalUnit unit,
            int ey, int em, int ed) {
        assertEquals(fixed(ey, em, ed), fixed(y, m, d).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus_leap_and_year_day")
    void plus_across_leap_and_year_day(
            int y, int m, int d, long amount, TemporalUnit unit,
            int ey, int em, int ed) {
        assertEquals(fixed(ey, em, ed), fixed(y, m, d).plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    void minus_by_unit_is_inverse_of_plus(
            int ey, int em, int ed, long amount, TemporalUnit unit,
            int y, int m, int d) {
        assertEquals(fixed(ey, em, ed), fixed(y, m, d).minus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("data_minus_leap_and_year_day")
    void minus_across_leap_and_year_day(
            int ey, int em, int ed, long amount, TemporalUnit unit,
            int y, int m, int d) {
        assertEquals(fixed(ey, em, ed), fixed(y, m, d).minus(amount, unit));
    }

    @Test
    void plus_unsupported_unit_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> fixed(2012, LEAP_MONTH, DAY_28).plus(0, MINUTES));
    }

    // until datasets

    public static Object[][] data_until() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_until(); }
    public static Object[][] data_until_period() { return org.threeten.extra.chrono.TestInternationalFixedChronology.data_until_period(); }

    @ParameterizedTest
    @MethodSource("data_until")
    void until_by_unit_matches_expected(
            int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        InternationalFixedDate start = fixed(y1, m1, d1);
        InternationalFixedDate end = fixed(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    @ParameterizedTest
    @MethodSource("data_until_period")
    void until_returns_correct_period(
            int y1, int m1, int d1, int y2, int m2, int d2,
            int py, int pm, int pd) {
        InternationalFixedDate start = fixed(y1, m1, d1);
        InternationalFixedDate end = fixed(y2, m2, d2);
        ChronoPeriod expected = IFC.period(py, pm, pd);
        assertEquals(expected, start.until(end));
    }

    @Test
    void until_unsupported_unit_throws() {
        InternationalFixedDate start = fixed(2012, LEAP_MONTH, DAY_28);
        InternationalFixedDate end = fixed(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
    }

    // ------------------------------------------------------------
    // Period arithmetic (IFC vs ISO)
    // ------------------------------------------------------------

    @Test
    void plus_with_ifc_period() {
        assertEquals(fixed(2014, 8, 1), fixed(2014, 5, 26).plus(IFC.period(0, 2, 3)));
    }

    @Test
    void plus_with_iso_period_rejected() {
        assertThrows(DateTimeException.class, () -> fixed(2014, 5, 26).plus(Period.ofMonths(2)));
    }

    @Test
    void minus_with_ifc_period() {
        assertEquals(fixed(2014, 3, 23), fixed(2014, 5, 26).minus(IFC.period(0, 2, 3)));
    }

    @Test
    void minus_with_iso_period_rejected() {
        assertThrows(DateTimeException.class, () -> fixed(2014, 5, 26).minus(Period.ofMonths(2)));
    }

    // ------------------------------------------------------------
    // equals/hashCode & toString
    // ------------------------------------------------------------

    @Test
    void equals_and_hashCode_groups() {
        new EqualsTester()
            .addEqualityGroup(fixed(2000, 1, 3),  fixed(2000, 1, 3))
            .addEqualityGroup(fixed(2000, 1, 4),  fixed(2000, 1, 4))
            .addEqualityGroup(fixed(2000, 2, 3),  fixed(2000, 2, 3))
            .addEqualityGroup(fixed(2000, 6, 28), fixed(2000, 6, 28))
            .addEqualityGroup(fixed(2000, 6, 29), fixed(2000, 6, 29))
            .addEqualityGroup(fixed(2000, 13, 28), fixed(2000, 13, 28))
            .addEqualityGroup(fixed(2001, 1, 1),  fixed(2001, 1, 1))
            .addEqualityGroup(fixed(2001, 13, 29),fixed(2001, 13, 29))
            .addEqualityGroup(fixed(2004, 6, 29), fixed(2004, 6, 29))
            .testEquals();
    }

    public static Object[][] data_toString() {
        return new Object[][]{
            {fixed(1, 1, 1),          "Ifc CE 1/01/01"},
            {fixed(2012, 6, 23),      "Ifc CE 2012/06/23"},
            {fixed(1, 13, 29),        "Ifc CE 1/13/29"},
            {fixed(2012, LEAP_MONTH, DAY_29), "Ifc CE 2012/06/29"},
            {fixed(2012, YEAR_DAY_MONTH, DAY_29), "Ifc CE 2012/13/29"},
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    void toString_matches_expected(InternationalFixedDate date, String expected) {
        assertEquals(expected, date.toString());
    }
}
