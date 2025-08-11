package org.threeten.extra.chrono;

import org.junit.Test;
import org.threeten.extra.chrono.Symmetry454Chronology;
import org.threeten.extra.chrono.Symmetry454Date;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    private final Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

    // --- date() factories ---

    @Test
    public void date_withProlepticYearMonthDay_createsCorrectDate() {
        // When
        Symmetry454Date date = chronology.date(2014, 2, 3);

        // Then
        assertEquals(2014, date.get(ChronoField.YEAR));
        assertEquals(2, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(3, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void date_withEraYearMonthDay_createsCorrectDate() {
        // When
        Symmetry454Date date = chronology.date(IsoEra.CE, 2014, 4, 4);

        // Then
        assertEquals(2014, date.get(ChronoField.YEAR_OF_ERA));
        assertEquals(4, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(4, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void date_withInvalidMonth_throwsException() {
        // A month value of 13 is invalid.
        chronology.date(2014, 13, 1);
    }

    @Test(expected = DateTimeException.class)
    public void date_withInvalidDayForShortMonth_throwsException() {
        // January has 28 days in the Symmetry454 calendar.
        chronology.date(2014, 1, 29);
    }

    @Test(expected = DateTimeException.class)
    public void date_withInvalidDayForLongMonth_throwsException() {
        // February has 35 days in the Symmetry454 calendar.
        chronology.date(2014, 2, 36);
    }

    @Test(expected = ClassCastException.class)
    public void date_withInvalidEraType_throwsException() {
        // Only IsoEra is supported.
        chronology.date(HijrahEra.AH, 2014, 1, 1);
    }

    // --- dateYearDay() factories ---

    @Test
    public void dateYearDay_withProlepticYear_createsCorrectDate() {
        // When
        Symmetry454Date date = chronology.dateYearDay(2014, 19);

        // Then
        assertEquals(2014, date.get(ChronoField.YEAR));
        assertEquals(19, date.get(ChronoField.DAY_OF_YEAR));
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void dateYearDay_withEra_createsCorrectDate() {
        // When
        Symmetry454Date date = chronology.dateYearDay(IsoEra.CE, 29, 11);

        // Then
        assertEquals(29, date.get(ChronoField.YEAR_OF_ERA));
        assertEquals(11, date.get(ChronoField.DAY_OF_YEAR));
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_withLeapDayInNonLeapYear_throwsException() {
        // Year 371 is not a leap year, so it has 364 days. Day 371 is invalid.
        chronology.dateYearDay(371, 371);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_withInvalidDayOfYear_throwsException() {
        chronology.dateYearDay(2014, -1);
    }

    @Test(expected = ClassCastException.class)
    public void dateYearDay_withInvalidEraType_throwsException() {
        // Only IsoEra is supported.
        chronology.dateYearDay(JapaneseEra.SHOWA, 2014, 1);
    }

    // --- dateEpochDay() factory ---

    @Test
    public void dateEpochDay_createsCorrectDate() {
        // Given an epoch day of 132
        long epochDay = 132L;

        // When
        Symmetry454Date date = chronology.dateEpochDay(epochDay);

        // Then: 1970-01-01 (ISO) is epoch day 0. 132 days later is 1970-05-13.
        // In Symmetry454 (1970 is non-leap):
        // Q1 (Jan-Mar): 28 + 35 + 28 = 91 days.
        // Day 132 is 132 - 91 = 41 days into Q2.
        // Apr (28 days), May (35 days). Day 41 is the 13th day of May (41-28=13).
        assertEquals(1970, date.getYear());
        assertEquals(5, date.getMonthValue()); // May
        assertEquals(13, date.getDayOfMonth());
        assertEquals(epochDay + 1, date.getDayOfYear()); // Day-of-year is 1-based
    }

    @Test(expected = DateTimeException.class)
    public void dateEpochDay_withOutOfRangeEpochDay_throwsException() {
        // The valid range is -365961480 to 364523156.
        long outOfRangeEpochDay = 364523157L;
        chronology.dateEpochDay(outOfRangeEpochDay);
    }

    // --- dateNow() factories ---

    @Test
    public void dateNow_withClock_returnsDateFromClock() {
        // Given a fixed clock
        Instant fixedInstant = LocalDateTime.of(2014, 1, 1, 10, 15).toInstant(ZoneOffset.UTC);
        Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        // When
        Symmetry454Date date = chronology.dateNow(clock);

        // Then the date should match the clock's date
        assertEquals(2014, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClock_throwsException() {
        chronology.dateNow((Clock) null);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullZone_throwsException() {
        chronology.dateNow((ZoneId) null);
    }

    // --- date(TemporalAccessor) ---

    @Test
    public void date_fromSymmetry454Date_returnsSameDate() {
        // Given
        Symmetry454Date originalDate = Symmetry454Date.of(2014, 2, 3);

        // When
        Symmetry454Date fromTemporal = chronology.date(originalDate);

        // Then
        assertEquals(originalDate, fromTemporal);
    }

    @Test
    public void date_fromIsoDate_returnsConvertedDate() {
        // Given an ISO date that corresponds to epoch day 132
        LocalDateTime isoDateTime = LocalDateTime.of(1970, 5, 13, 0, 0);

        // When
        Symmetry454Date symDate = chronology.date(isoDateTime);

        // Then the converted date should be correct
        assertEquals(1970, symDate.getYear());
        assertEquals(5, symDate.getMonthValue());
        assertEquals(13, symDate.getDayOfMonth());
    }

    @Test(expected = DateTimeException.class)
    public void date_fromUnsupportedTemporal_throwsException() {
        // An Era object does not contain enough information to form a date.
        chronology.date(IsoEra.CE);
    }

    // --- localDateTime(TemporalAccessor) ---

    @Test
    public void localDateTime_fromTemporalAccessor_createsCorrectLocalDateTime() {
        // Given
        LocalDateTime isoDateTime = LocalDateTime.of(2014, 2, 14, 20, 15, 16);

        // When
        ChronoLocalDateTime<Symmetry454Date> symDateTime = chronology.localDateTime(isoDateTime);

        // Then
        assertEquals(chronology.date(isoDateTime), symDateTime.toLocalDate());
        assertEquals(isoDateTime.toLocalTime(), symDateTime.toLocalTime());
    }

    // --- zonedDateTime(...) ---

    @Test
    public void zonedDateTime_fromInstantAndZone_createsCorrectZonedDateTime() {
        // Given
        Instant instant = Instant.EPOCH.plusSeconds(3);
        ZoneId zone = ZoneOffset.UTC;

        // When
        ChronoZonedDateTime<Symmetry454Date> zdt = chronology.zonedDateTime(instant, zone);

        // Then
        assertEquals(1970, zdt.get(ChronoField.YEAR));
        assertEquals(1, zdt.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(1, zdt.get(ChronoField.DAY_OF_MONTH));
        assertEquals(3, zdt.get(ChronoField.SECOND_OF_DAY));
        assertEquals(zone, zdt.getZone());
    }

    @Test
    public void zonedDateTime_fromOffsetDateTime_createsCorrectZonedDateTime() {
        // Given
        OffsetDateTime odt = OffsetDateTime.of(2014, 2, 14, 20, 15, 16, 0, ZoneOffset.ofHours(2));

        // When
        ChronoZonedDateTime<Symmetry454Date> zdt = chronology.zonedDateTime(odt);

        // Then
        assertEquals(chronology.date(odt.toLocalDate()), zdt.toLocalDate());
        assertEquals(odt.toLocalTime(), zdt.toLocalTime());
        assertEquals(odt.getOffset(), zdt.getOffset());
        assertEquals(odt.getZone(), zdt.getZone());
    }

    // --- isLeapYear() ---

    @Test
    public void isLeapYear_forLeapYear_returnsTrue() {
        // Year 3 is a leap year in Symmetry454.
        assertTrue(chronology.isLeapYear(3L));
    }

    @Test
    public void isLeapYear_forNonLeapYear_returnsFalse() {
        // Year 32 is not a leap year in Symmetry454.
        assertFalse(chronology.isLeapYear(32L));
    }

    // --- prolepticYear() ---

    @Test
    public void prolepticYear_withCeEra_returnsSameYear() {
        assertEquals(-1390, chronology.prolepticYear(IsoEra.CE, -1390));
        assertEquals(1996, chronology.prolepticYear(IsoEra.CE, 1996));
    }

    @Test
    public void prolepticYear_withBceEra_returnsSameYear() {
        // The implementation returns the year-of-era directly, even for BCE.
        // This is unusual but we test the implemented behavior.
        assertEquals(1996, chronology.prolepticYear(IsoEra.BCE, 1996));
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_withInvalidEraType_throwsException() {
        chronology.prolepticYear(JapaneseEra.SHOWA, 29);
    }

    // --- eraOf() and eras() ---

    @Test
    public void eraOf_withValue0_returnsBce() {
        assertEquals(IsoEra.BCE, chronology.eraOf(0));
    }

    @Test
    public void eraOf_withValue1_returnsCe() {
        assertEquals(IsoEra.CE, chronology.eraOf(1));
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_withInvalidValue_throwsException() {
        chronology.eraOf(2);
    }

    @Test
    public void eras_returnsListOfEras() {
        // When
        List<Era> eras = chronology.eras();

        // Then
        assertEquals(2, eras.size());
        assertTrue(eras.containsAll(List.of(IsoEra.BCE, IsoEra.CE)));
    }

    // --- range(ChronoField) ---

    @Test
    public void range_forEraAndYearFields_returnsCorrectRanges() {
        assertEquals("ERA", ValueRange.of(0, 1), chronology.range(ChronoField.ERA));
        assertEquals("YEAR_OF_ERA", ValueRange.of(1, 1_000_000), chronology.range(ChronoField.YEAR_OF_ERA));
        assertEquals("YEAR", Symmetry454Chronology.YEAR_RANGE, chronology.range(ChronoField.YEAR));
    }

    @Test
    public void range_forMonthAndDayFields_returnsCorrectRanges() {
        assertEquals("MONTH_OF_YEAR", ValueRange.of(1, 12), chronology.range(ChronoField.MONTH_OF_YEAR));
        assertEquals("PROLEPTIC_MONTH", ValueRange.of(-12_000_000L, 11_999_999L), chronology.range(ChronoField.PROLEPTIC_MONTH));
        assertEquals("DAY_OF_MONTH", ValueRange.of(1, 28, 35), chronology.range(ChronoField.DAY_OF_MONTH));
        assertEquals("DAY_OF_YEAR", ValueRange.of(1, 364, 371), chronology.range(ChronoField.DAY_OF_YEAR));
        assertEquals("EPOCH_DAY", Symmetry454Chronology.EPOCH_DAY_RANGE, chronology.range(ChronoField.EPOCH_DAY));
    }

    @Test
    public void range_forWeekBasedFields_returnsCorrectRanges() {
        assertEquals("DAY_OF_WEEK", ChronoField.DAY_OF_WEEK.range(), chronology.range(ChronoField.DAY_OF_WEEK));
        assertEquals("ALIGNED_DAY_OF_WEEK_IN_MONTH", ValueRange.of(1, 7), chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals("ALIGNED_DAY_OF_WEEK_IN_YEAR", ValueRange.of(1, 7), chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals("ALIGNED_WEEK_OF_MONTH", ValueRange.of(1, 4, 5), chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH));
        assertEquals("ALIGNED_WEEK_OF_YEAR", ValueRange.of(1, 52, 53), chronology.range(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_forUnsupportedField_throwsException() {
        // DAY_OF_QUARTER is not supported by Symmetry454Chronology.
        chronology.range(ChronoField.DAY_OF_QUARTER);
    }

    // --- Identity and Singleton ---

    @Test
    public void getId_returnsSym454() {
        assertEquals("Sym454", chronology.getId());
    }

    @Test
    public void getCalendarType_returnsNull() {
        assertNull(chronology.getCalendarType());
    }

    @Test
    public void singleton_isEnforced() {
        assertSame("INSTANCE should be a singleton", Symmetry454Chronology.INSTANCE, chronology);
    }

    // --- Static helpers ---

    @Test
    public void getLeapYearsBefore_withPositiveYear_returnsCorrectCount() {
        assertEquals(127633L, Symmetry454Chronology.getLeapYearsBefore(719162L));
    }

    @Test
    public void getLeapYearsBefore_withNegativeYear_returnsCorrectCount() {
        assertEquals(-11L, Symmetry454Chronology.getLeapYearsBefore(-60L));
    }

    @Test
    public void getLeapYearsBefore_withZero_returnsZero() {
        assertEquals(0L, Symmetry454Chronology.getLeapYearsBefore(0L));
    }
}