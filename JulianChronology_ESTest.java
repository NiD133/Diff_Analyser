package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for JulianChronology.
 *
 * Notes:
 * - Prefer the singleton instance (JulianChronology.INSTANCE) over the deprecated constructor.
 * - Use fixed clocks where needed to keep tests deterministic.
 * - Focus on clear names and minimal mocking.
 */
public class JulianChronologyTest {

    private static final JulianChronology JC = JulianChronology.INSTANCE;

    // ---------------------------------------------------------------------
    // Basic identity and metadata
    // ---------------------------------------------------------------------

    @Test
    public void getId_and_getCalendarType() {
        assertEquals("Julian", JC.getId());
        assertEquals("julian", JC.getCalendarType());
    }

    @Test
    public void eras_exposesBCandAD() {
        List<Era> eras = JC.eras();
        assertTrue(eras.containsAll(Arrays.asList(JulianEra.BC, JulianEra.AD)));
        assertEquals(JulianEra.BC, JC.eraOf(0));
        assertEquals(JulianEra.AD, JC.eraOf(1));
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_invalidValue_throws() {
        JC.eraOf(99);
    }

    // ---------------------------------------------------------------------
    // Proleptic year and leap-year
    // ---------------------------------------------------------------------

    @Test
    public void prolepticYear_withAD_and_BC() {
        assertEquals(2025, JC.prolepticYear(JulianEra.AD, 2025));
        // In JulianChronology, 1 BC -> proleptic year 0; 2 BC -> -1
        assertEquals(0, JC.prolepticYear(JulianEra.BC, 1));
        assertEquals(-1, JC.prolepticYear(JulianEra.BC, 2));
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_wrongEraType_throwsClassCast() {
        JC.prolepticYear(java.time.chrono.JapaneseEra.HEISEI, 10);
    }

    @Test
    public void isLeapYear_julianRule_every4Years() {
        assertTrue(JC.isLeapYear(1900)); // leap in Julian
        assertFalse(JC.isLeapYear(1901));
        assertTrue(JC.isLeapYear(2000));
        assertTrue(JC.isLeapYear(0));    // proleptic year zero is divisible by 4
    }

    // ---------------------------------------------------------------------
    // Creating dates
    // ---------------------------------------------------------------------

    @Test
    public void date_prolepticYearMonthDay_roundTripsViaEpochDay() {
        JulianDate d = JC.date(2020, 2, 29); // leap year in Julian
        JulianDate roundTrip = JC.dateEpochDay(d.toEpochDay());
        assertEquals(d, roundTrip);
    }

    @Test(expected = DateTimeException.class)
    public void date_invalidMonth_throws() {
        JC.date(2020, 13, 1);
    }

    @Test
    public void dateYearDay_validValues() {
        JulianDate d1 = JC.dateYearDay(2019, 60); // valid for non-leap (should be March 1 in ISO, Julian differs)
        JulianDate d2 = JC.dateYearDay(2020, 366); // valid leap year day-of-year
        // sanity: both round-trip through epoch-day
        assertEquals(d1, JC.dateEpochDay(d1.toEpochDay()));
        assertEquals(d2, JC.dateEpochDay(d2.toEpochDay()));
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_366_on_nonLeapYear_throws() {
        JC.dateYearDay(2019, 366);
    }

    @Test
    public void date_zeroProlepticYear_isBC_era() {
        JulianDate d = JC.date(0, 1, 1);
        assertEquals(JulianEra.BC, d.getEra());
    }

    // ---------------------------------------------------------------------
    // date(TemporalAccessor), localDateTime, zonedDateTime
    // ---------------------------------------------------------------------

    @Test
    public void date_fromISO_LocalDate_usesEpochDay() {
        LocalDate iso = LocalDate.of(1970, 1, 1); // epoch day 0
        JulianDate julian = JC.date(iso);
        assertEquals(0, julian.toEpochDay());
    }

    @Test(expected = NullPointerException.class)
    public void date_fromNull_throws() {
        JC.date((java.time.temporal.TemporalAccessor) null);
    }

    @Test
    public void localDateTime_fromLocalDateTime_isCreated() {
        LocalDateTime ldt = LocalDateTime.of(2000, 1, 1, 12, 30);
        ChronoLocalDateTime<JulianDate> cldt = JC.localDateTime(ldt);
        assertNotNull(cldt);
        assertEquals(JC, cldt.toLocalDate().getChronology());
    }

    @Test
    public void zonedDateTime_fromInstantAndZone_isCreated() {
        Instant instant = Instant.parse("2020-01-01T00:00:00Z");
        ZoneId zone = ZoneOffset.UTC;
        ChronoZonedDateTime<JulianDate> zdt = JC.zonedDateTime(instant, zone);
        assertNotNull(zdt);
        assertEquals(zone, zdt.getZone());
    }

    @Test(expected = DateTimeException.class)
    public void zonedDateTime_fromUnsupportedTemporal_throws() {
        JC.zonedDateTime(ZoneOffset.UTC); // ZoneOffset alone cannot be turned into a zoned date-time
    }

    // ---------------------------------------------------------------------
    // dateNow
    // ---------------------------------------------------------------------

    @Test
    public void dateNow_withFixedClock_matchesEpochDayConversion() {
        // Given a fixed instant in UTC, the Julian date should match conversion from ISO epoch-day
        Instant instant = Instant.parse("2021-03-14T09:26:53Z");
        Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
        JulianDate expected = JC.dateEpochDay(LocalDate.now(clock).toEpochDay());

        JulianDate actual = JC.dateNow(clock);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_zoneNull_throws() {
        JC.dateNow((ZoneId) null);
    }

    // ---------------------------------------------------------------------
    // Ranges
    // ---------------------------------------------------------------------

    @Test
    public void range_forKeyFields_coversDocumentedBounds() {
        // YEAR: [-999_998, 999_999]
        ValueRange year = JC.range(ChronoField.YEAR);
        assertEquals(-999_998, year.getMinimum());
        assertEquals(999_999, year.getMaximum());

        // YEAR_OF_ERA: [1, 999_999]
        ValueRange yoe = JC.range(ChronoField.YEAR_OF_ERA);
        assertEquals(1, yoe.getMinimum());
        assertEquals(999_999, yoe.getMaximum());

        // PROLEPTIC_MONTH: [-999_998 * 12, 999_999 * 12 + 11]
        ValueRange pm = JC.range(ChronoField.PROLEPTIC_MONTH);
        long expectedMin = -999_998L * 12L;
        long expectedMax = 999_999L * 12L + 11L;
        assertEquals(expectedMin, pm.getMinimum());
        assertEquals(expectedMax, pm.getMaximum());
    }

    // ---------------------------------------------------------------------
    // resolveDate
    // ---------------------------------------------------------------------

    @Test
    public void resolveDate_withEpochDay_buildsExpectedDate() {
        Map<java.time.temporal.TemporalField, Long> fields = new HashMap<>();
        fields.put(ChronoField.EPOCH_DAY, 0L);

        JulianDate resolved = JC.resolveDate(fields, ResolverStyle.STRICT);
        assertNotNull(resolved);
        assertEquals(0, resolved.toEpochDay());
        // resolveDate should consume the field
        assertTrue(fields.isEmpty());
    }

    @Test(expected = DateTimeException.class)
    public void resolveDate_invalidEraValue_throws() {
        Map<java.time.temporal.TemporalField, Long> fields = new HashMap<>();
        fields.put(ChronoField.ERA, 867L);
        JC.resolveDate(fields, ResolverStyle.STRICT);
    }

    // ---------------------------------------------------------------------
    // Mixed-argument validation
    // ---------------------------------------------------------------------

    @Test
    public void date_withEra_BC_createsBCDate() {
        JulianDate d = JC.date(JulianEra.BC, 2, 1, 1); // 2 BC -> proleptic -1
        assertEquals(JulianEra.BC, d.getEra());
    }

    @Test(expected = ClassCastException.class)
    public void date_withWrongEraType_throwsClassCast() {
        JC.date(java.time.chrono.JapaneseEra.HEISEI, 1, 1, 1);
    }
}