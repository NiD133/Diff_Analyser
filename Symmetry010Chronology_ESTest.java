package org.threeten.extra.chrono;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.Year;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Readable, deterministic tests for Symmetry010Chronology.
 * Focuses on:
 * - identity and era API
 * - leap-year algorithm and helper
 * - date construction and validation
 * - temporal conversions and "now" methods (with fixed clocks)
 * - field ranges and argument validation
 */
class Symmetry010ChronologyTest {

    private static final Symmetry010Chronology CHRONO = Symmetry010Chronology.INSTANCE;

    private static Clock fixedUtcClock(LocalDate date) {
        return Clock.fixed(date.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }

    private static int findLeapYearNear(int start) {
        for (int y = start - 5000; y <= start + 5000; y++) {
            if (CHRONO.isLeapYear(y)) {
                return y;
            }
        }
        throw new AssertionError("Could not find a leap year near " + start);
    }

    private static int findCommonYearNear(int start) {
        for (int y = start - 5000; y <= start + 5000; y++) {
            if (!CHRONO.isLeapYear(y)) {
                return y;
            }
        }
        throw new AssertionError("Could not find a common year near " + start);
    }

    @Nested
    @DisplayName("Identity and eras")
    class IdentityAndEras {

        @Test
        void id_and_calendarType() {
            assertEquals("Sym010", CHRONO.getId());
            assertNull(CHRONO.getCalendarType(), "No LDML identifier exists for this chronology");
        }

        @Test
        void eras_list_and_lookup() {
            List<Era> eras = CHRONO.eras();
            assertEquals(2, eras.size());
            assertEquals(IsoEra.BCE, CHRONO.eraOf(0));
            assertEquals(IsoEra.CE, CHRONO.eraOf(1));
        }

        @Test
        void eraOf_invalid_value() {
            assertThrows(DateTimeException.class, () -> CHRONO.eraOf(2));
        }
    }

    @Nested
    @DisplayName("Proleptic year and leap years")
    class Years {

        @Test
        void prolepticYear_for_iso_eras_is_passthrough() {
            assertEquals(-537, CHRONO.prolepticYear(IsoEra.CE, -537));
            assertEquals(0, CHRONO.prolepticYear(IsoEra.BCE, 0));
            assertEquals(3994, CHRONO.prolepticYear(IsoEra.CE, 3994));
        }

        @Test
        void prolepticYear_invalid_value_throws() {
            assertThrows(DateTimeException.class, () -> CHRONO.prolepticYear(IsoEra.CE, Integer.MIN_VALUE / 2));
        }

        @Test
        void prolepticYear_invalid_era_type_throws() {
            assertThrows(ClassCastException.class, () -> CHRONO.prolepticYear(java.time.chrono.JapaneseEra.MEIJI, 123));
        }

        @Test
        void leapYear_basic_examples() {
            assertTrue(CHRONO.isLeapYear(-1547L), "known leap year example");
            assertFalse(CHRONO.isLeapYear(32L), "known common year example");
        }

        @Test
        void leapYearsBefore_examples() {
            assertEquals(0L, Symmetry010Chronology.getLeapYearsBefore(0L));
            assertEquals(99L, Symmetry010Chronology.getLeapYearsBefore(560L));
            assertEquals(-453L, Symmetry010Chronology.getLeapYearsBefore(-2552L));
        }
    }

    @Nested
    @DisplayName("Date construction and validation")
    class DateConstruction {

        @Test
        void date_prolepticYear_month_day_valid() {
            Symmetry010Date d = CHRONO.date(12, 12, 12);
            assertEquals(IsoEra.CE, d.getEra());
        }

        @Test
        void date_era_yearOfEra_month_day_valid() {
            Symmetry010Date d = CHRONO.date(IsoEra.CE, 10, 10, 10);
            assertEquals(IsoEra.CE, d.getEra());
        }

        @Test
        void date_invalid_month_throws() {
            assertThrows(DateTimeException.class, () -> CHRONO.date(13, 13, 13));
        }

        @Test
        void date_invalid_day_throws() {
            assertThrows(DateTimeException.class, () -> CHRONO.date(5, 5, 35));
        }

        @Test
        void date_invalid_era_type_throws() {
            assertThrows(ClassCastException.class,
                () -> CHRONO.date(java.time.chrono.ThaiBuddhistEra.BEFORE_BE, 2000, 1, 1));
        }

        @Test
        void dateYearDay_proleptic_valid_and_invalid() {
            int leapYear = findLeapYearNear(2000);
            int commonYear = findCommonYearNear(2000);

            // valid in leap years
            assertNotNull(CHRONO.dateYearDay(leapYear, 371));

            // invalid: out of range
            assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(999, 999));

            // invalid: 371 is not valid in non-leap years
            assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(commonYear, 371));
        }

        @Test
        void dateYearDay_with_era() {
            Symmetry010Date d = CHRONO.dateYearDay(IsoEra.CE, 14, 30);
            assertEquals(IsoEra.CE, d.getEra());
        }

        @Test
        void dateYearDay_invalid_dayOfYear_with_era_throws() {
            assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(IsoEra.CE, 1223, 1223));
        }

        @Test
        void dateYearDay_invalid_era_type_throws() {
            assertThrows(ClassCastException.class,
                () -> CHRONO.dateYearDay(java.time.chrono.JapaneseEra.SHOWA, 1945, 200));
        }

        @Test
        void dateEpochDay_within_supported_range() {
            ValueRange range = CHRONO.range(ChronoField.EPOCH_DAY);
            long min = range.getMinimum();
            long max = range.getMaximum();

            assertNotNull(CHRONO.dateEpochDay(min));
            assertNotNull(CHRONO.dateEpochDay(max));
        }

        @Test
        void dateEpochDay_outside_supported_range_throws() {
            ValueRange range = CHRONO.range(ChronoField.EPOCH_DAY);
            long min = range.getMinimum();
            long max = range.getMaximum();

            assertThrows(DateTimeException.class, () -> CHRONO.dateEpochDay(min - 1));
            assertThrows(DateTimeException.class, () -> CHRONO.dateEpochDay(max + 1));
        }
    }

    @Nested
    @DisplayName("Now and temporal conversions")
    class NowAndConversions {

        @Test
        void dateNow_with_fixed_clock_is_deterministic() {
            Clock clock = fixedUtcClock(LocalDate.of(1970, 1, 1)); // epoch day 0
            Symmetry010Date d = CHRONO.dateNow(clock);
            assertEquals(Symmetry010Date.ofEpochDay(0), d);
        }

        @Test
        void dateNow_overloads_validate_arguments() {
            assertThrows(NullPointerException.class, () -> CHRONO.dateNow((Clock) null));
            assertThrows(NullPointerException.class, () -> CHRONO.dateNow((ZoneId) null));
            assertNotNull(CHRONO.dateNow()); // should always work
        }

        @Test
        void date_from_temporal() {
            Symmetry010Date fromLocalDate = CHRONO.date(LocalDate.of(1970, 1, 1));
            assertEquals(Symmetry010Date.ofEpochDay(0), fromLocalDate);

            assertThrows(NullPointerException.class, () -> CHRONO.date(null));
            assertThrows(UnsupportedTemporalTypeException.class, () -> CHRONO.date(Year.of(2000)));
        }

        @Test
        void localDateTime_from_temporal() {
            ChronoLocalDateTime<Symmetry010Date> ldt =
                CHRONO.localDateTime(OffsetDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC));
            assertNotNull(ldt);

            assertThrows(DateTimeException.class, () -> CHRONO.localDateTime(LocalDate.of(2000, 1, 1)));
            assertThrows(NullPointerException.class, () -> CHRONO.localDateTime(null));
        }

        @Test
        void zonedDateTime_from_temporal() {
            ChronoZonedDateTime<Symmetry010Date> zdt =
                CHRONO.zonedDateTime(OffsetDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC));
            assertNotNull(zdt);

            assertThrows(NullPointerException.class, () -> CHRONO.zonedDateTime((java.time.temporal.TemporalAccessor) null));
        }

        @Test
        void zonedDateTime_from_instant_and_zone() {
            ChronoZonedDateTime<Symmetry010Date> zdt = CHRONO.zonedDateTime(Instant.EPOCH, ZoneOffset.UTC);
            assertNotNull(zdt);

            assertThrows(NullPointerException.class, () -> CHRONO.zonedDateTime((Instant) null, ZoneOffset.UTC));
            assertThrows(NullPointerException.class, () -> CHRONO.zonedDateTime(Instant.EPOCH, null));
        }
    }

    @Nested
    @DisplayName("Field ranges")
    class FieldRanges {

        @Test
        void range_basic_fields_are_non_null() {
            assertNotNull(CHRONO.range(ChronoField.YEAR));
            assertNotNull(CHRONO.range(ChronoField.YEAR_OF_ERA));
            assertNotNull(CHRONO.range(ChronoField.PROLEPTIC_MONTH));
            assertNotNull(CHRONO.range(ChronoField.MONTH_OF_YEAR));
            assertNotNull(CHRONO.range(ChronoField.DAY_OF_MONTH));
            assertNotNull(CHRONO.range(ChronoField.DAY_OF_YEAR));
            assertNotNull(CHRONO.range(ChronoField.DAY_OF_WEEK));
            assertNotNull(CHRONO.range(ChronoField.EPOCH_DAY));
            assertNotNull(CHRONO.range(ChronoField.ERA));
        }

        @Test
        void range_expected_bounds_for_selected_fields() {
            ValueRange year = CHRONO.range(ChronoField.YEAR);
            assertEquals(ValueRange.of(-1_000_000, 1_000_000), year);

            ValueRange month = CHRONO.range(ChronoField.MONTH_OF_YEAR);
            assertEquals(1, month.getMinimum());
            assertEquals(12, month.getMaximum());

            ValueRange dayOfMonth = CHRONO.range(ChronoField.DAY_OF_MONTH);
            assertEquals(1, dayOfMonth.getMinimum());
            assertEquals(37, dayOfMonth.getMaximum()); // December in leap years has 37 days

            ValueRange dayOfYear = CHRONO.range(ChronoField.DAY_OF_YEAR);
            assertEquals(1, dayOfYear.getMinimum());
            assertEquals(371, dayOfYear.getMaximum());

            ValueRange prolepticMonth = CHRONO.range(ChronoField.PROLEPTIC_MONTH);
            assertEquals(-12_000_000, prolepticMonth.getMinimum());
            assertEquals(11_999_999, prolepticMonth.getMaximum());

            ValueRange era = CHRONO.range(ChronoField.ERA);
            assertEquals(0, era.getMinimum());
            assertEquals(1, era.getMaximum());
        }

        @Test
        void range_null_field_throws() {
            assertThrows(NullPointerException.class, () -> CHRONO.range(null));
        }
    }
}