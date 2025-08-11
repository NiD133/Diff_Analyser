package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

import static org.junit.Assert.*;

/**
 * Readable, developer-friendly tests for Symmetry454Chronology.
 *
 * The tests are grouped by behavior and avoid EvoSuite-specific mocking.
 * They use fixed clocks/zones where appropriate to keep results deterministic.
 */
public class Symmetry454ChronologyTest {

    private final Symmetry454Chronology chrono = Symmetry454Chronology.INSTANCE;
    private final ZoneId UTC = ZoneOffset.UTC;

    // --- helpers ----------------------------------------------------------------

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Throwable;
    }

    private static <T extends Throwable> T expectThrows(Class<T> expected, ThrowingRunnable code) {
        try {
            code.run();
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                return expected.cast(t);
            }
            String msg = "Expected " + expected.getSimpleName() + " but caught " + t.getClass().getSimpleName();
            fail(msg + " with message: " + t.getMessage());
        }
        fail("Expected " + expected.getSimpleName() + " but no exception was thrown");
        return null; // unreachable
    }

    private Clock fixedClock() {
        // 2000-01-01T00:00Z in ISO, arbitrary but stable
        return Clock.fixed(Instant.parse("2000-01-01T00:00:00Z"), UTC);
    }

    // --- basics -----------------------------------------------------------------

    @Test
    public void idAndCalendarType() {
        assertEquals("Sym454", chrono.getId());
        assertNull(chrono.getCalendarType());
    }

    @Test
    public void eras_andEraOf() {
        assertTrue(chrono.eras().contains(IsoEra.BCE));
        assertTrue(chrono.eras().contains(IsoEra.CE));

        assertEquals(IsoEra.BCE, chrono.eraOf(0));
        assertEquals(IsoEra.CE, chrono.eraOf(1));

        expectThrows(DateTimeException.class, () -> chrono.eraOf(-1));
        expectThrows(DateTimeException.class, () -> chrono.eraOf(2));
    }

    // --- proleptic year mapping --------------------------------------------------

    @Test
    public void prolepticYear_withIsoEra_isReturnedAsIs() {
        assertEquals(1996, chrono.prolepticYear(IsoEra.BCE, 1996));
        assertEquals(-1390, chrono.prolepticYear(IsoEra.CE, -1390));
        assertEquals(0, chrono.prolepticYear(chrono.eraOf(0), 0)); // year-of-era zero is supported
    }

    @Test
    public void prolepticYear_rejectsOutOfRangeYearOfEra() {
        int tooLarge = (int) (Symmetry454Chronology.YEAR_RANGE.getMaximum() + 1); // 1_000_001
        expectThrows(DateTimeException.class, () -> chrono.prolepticYear(IsoEra.BCE, tooLarge));
    }

    // --- leap years and cycle helpers -------------------------------------------

    @Test
    public void isLeapYear_examples() {
        assertTrue(chrono.isLeapYear(3));
        assertFalse(chrono.isLeapYear(32));
    }

    @Test
    public void getLeapYearsBefore_examples() {
        assertEquals(0L, Symmetry454Chronology.getLeapYearsBefore(0));
        assertEquals(127633L, Symmetry454Chronology.getLeapYearsBefore(719_162));
        assertEquals(-11L, Symmetry454Chronology.getLeapYearsBefore(-60));
    }

    // --- date factories ----------------------------------------------------------

    @Test
    public void date_fromEraYearMonthDay_valid() {
        Symmetry454Date d = chrono.date(IsoEra.CE, 4, 4, 4);
        assertEquals(IsoEra.CE, d.getEra());
    }

    @Test
    public void date_fromProlepticYearMonthDay_valid() {
        Symmetry454Date d = chrono.date(3, 2, 3);
        assertEquals(IsoEra.CE, d.getEra());
    }

    @Test
    public void date_rejectsInvalidMonth() {
        expectThrows(DateTimeException.class, () -> chrono.date(2024, 13, 1));
        expectThrows(DateTimeException.class, () -> chrono.date(IsoEra.BCE, 2024, -1, 1));
    }

    @Test
    public void date_rejectsInvalidDayOfMonthForMonthPattern() {
        // January has 28 days in Symmetry454; 35 is invalid
        expectThrows(DateTimeException.class, () -> chrono.date(1, 1, 35));
    }

    @Test
    public void dateYearDay_valid() {
        Symmetry454Date d = chrono.dateYearDay(19, 19);
        assertEquals(IsoEra.CE, d.getEra());
    }

    @Test
    public void dateYearDay_rejectsInvalidDayOfYearRange() {
        expectThrows(DateTimeException.class, () -> chrono.dateYearDay(2024, 0));
        expectThrows(DateTimeException.class, () -> chrono.dateYearDay(-2501, -2501));
    }

    @Test
    public void dateEpochDay_validAndInvalid() {
        Symmetry454Date d = chrono.dateEpochDay(132L);
        assertEquals(IsoEra.CE, d.getEra());

        long invalid = Symmetry454Chronology.EPOCH_DAY_RANGE.getMaximum() + 1;
        expectThrows(DateTimeException.class, () -> chrono.dateEpochDay(invalid));
    }

    // --- current date variants ---------------------------------------------------

    @Test
    public void dateNow_variants() {
        Symmetry454Date a = chrono.dateNow();
        Symmetry454Date b = chrono.dateNow(UTC);
        Symmetry454Date c = chrono.dateNow(fixedClock());

        assertEquals(IsoEra.CE, a.getEra());
        assertEquals(IsoEra.CE, b.getEra());
        assertEquals(IsoEra.CE, c.getEra());
    }

    @Test
    public void dateNow_nullArgumentsRejected() {
        expectThrows(NullPointerException.class, () -> chrono.dateNow((ZoneId) null));
        expectThrows(NullPointerException.class, () -> chrono.dateNow((Clock) null));
    }

    // --- conversions from TemporalAccessor --------------------------------------

    @Test
    public void date_fromTemporal_sameChronology() {
        Symmetry454Date source = chrono.dateNow(fixedClock());
        assertEquals(source, chrono.date(source));
    }

    @Test
    public void date_fromTemporal_unsupportedTemporal() {
        // Era does not expose EPOCH_DAY, so Chronology.date(Temporal) must fail
        expectThrows(UnsupportedTemporalTypeException.class, () -> chrono.date(IsoEra.CE));
    }

    @Test
    public void date_fromTemporal_nullRejected() {
        expectThrows(NullPointerException.class, () -> chrono.date(null));
    }

    @Test
    public void localDateTime_fromTemporal_valid() {
        LocalDateTime ldt = LocalDateTime.now(fixedClock());
        ChronoLocalDateTime<Symmetry454Date> cldt = chrono.localDateTime(ldt);
        assertNotNull(cldt);
    }

    @Test
    public void localDateTime_fromTemporal_nullRejected() {
        expectThrows(NullPointerException.class, () -> chrono.localDateTime(null));
    }

    @Test
    public void zonedDateTime_fromTemporal_valid() {
        OffsetDateTime odt = OffsetDateTime.now(fixedClock());
        ChronoZonedDateTime<Symmetry454Date> czdt = chrono.zonedDateTime(odt);
        assertNotNull(czdt);
    }

    @Test
    public void zonedDateTime_fromTemporal_rejectsUnsupported() {
        expectThrows(DateTimeException.class, () -> chrono.zonedDateTime(java.time.chrono.JapaneseEra.SHOWA));
    }

    @Test
    public void zonedDateTime_fromTemporal_nullRejected() {
        expectThrows(NullPointerException.class, () -> chrono.zonedDateTime((TemporalAccessor) null));
    }

    @Test
    public void zonedDateTime_fromInstant_validAndNullZoneRejected() {
        Instant instant = Instant.ofEpochSecond(3);
        ChronoZonedDateTime<Symmetry454Date> czdt = chrono.zonedDateTime(instant, UTC);
        assertNotNull(czdt);

        expectThrows(NullPointerException.class, () -> chrono.zonedDateTime(instant, null));
    }

    // --- field ranges ------------------------------------------------------------

    @Test
    public void range_forSupportedFields_matchesKnownRanges() {
        assertEquals(Symmetry454Chronology.YEAR_RANGE, chrono.range(ChronoField.YEAR));
        assertEquals(Symmetry454Chronology.YEAR_RANGE, chrono.range(ChronoField.YEAR_OF_ERA));
        assertEquals(Symmetry454Chronology.ERA_RANGE, chrono.range(ChronoField.ERA));
        assertEquals(Symmetry454Chronology.EPOCH_DAY_RANGE, chrono.range(ChronoField.EPOCH_DAY));
        assertEquals(Symmetry454Chronology.DAY_OF_MONTH_RANGE, chrono.range(ChronoField.DAY_OF_MONTH));
        assertEquals(Symmetry454Chronology.DAY_OF_YEAR_RANGE, chrono.range(ChronoField.DAY_OF_YEAR));
        assertEquals(Symmetry454Chronology.MONTH_OF_YEAR_RANGE, chrono.range(ChronoField.MONTH_OF_YEAR));
    }

    @Test
    public void range_forTimeFields_delegatesToField() {
        // Not chronology-specific; should match the field's own range
        ValueRange expected = ChronoField.SECOND_OF_MINUTE.range();
        assertEquals(expected, chrono.range(ChronoField.SECOND_OF_MINUTE));
    }

    @Test
    public void range_forOtherSupportedFields_isNotNull() {
        // These are defined but don't have specific constants to compare against here
        assertNotNull(chrono.range(ChronoField.ALIGNED_WEEK_OF_MONTH));
        assertNotNull(chrono.range(ChronoField.ALIGNED_WEEK_OF_YEAR));
        assertNotNull(chrono.range(ChronoField.DAY_OF_WEEK));
        assertNotNull(chrono.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertNotNull(chrono.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertNotNull(chrono.range(ChronoField.PROLEPTIC_MONTH));
    }

    @Test
    public void range_nullRejected() {
        expectThrows(NullPointerException.class, () -> chrono.range(null));
    }
}