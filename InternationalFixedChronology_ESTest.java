package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.*;

public class InternationalFixedChronologyTest {

    private static final InternationalFixedChronology IFc = InternationalFixedChronology.INSTANCE;

    // ----------------------------------------------------------------------
    // Basic identity and era
    // ----------------------------------------------------------------------

    @Test
    public void idAndCalendarType() {
        assertEquals("Ifc", IFc.getId());
        assertNull(IFc.getCalendarType());
    }

    @Test
    public void eraOfAndErasList() {
        InternationalFixedEra era = IFc.eraOf(1);
        assertEquals(InternationalFixedEra.CE, era);

        List<Era> eras = IFc.eras();
        assertEquals(1, eras.size());
        assertEquals(InternationalFixedEra.CE, eras.get(0));
    }

    @Test
    public void eraOf_invalidValue() {
        assertThrows(DateTimeException.class, () -> IFc.eraOf(2));
        assertThrows(DateTimeException.class, () -> IFc.eraOf(0));
        assertThrows(DateTimeException.class, () -> IFc.eraOf(-1));
    }

    // ----------------------------------------------------------------------
    // Leap-year logic and leap-years-before
    // ----------------------------------------------------------------------

    @Test
    public void isLeapYear_matchesGregorianRule() {
        assertTrue(IFc.isLeapYear(4));
        assertFalse(IFc.isLeapYear(100));
        assertTrue(IFc.isLeapYear(400));
        assertFalse(IFc.isLeapYear(241));
    }

    @Test
    public void getLeapYearsBefore_examples() {
        assertEquals(0L, InternationalFixedChronology.getLeapYearsBefore(0L));
        assertEquals(0L, InternationalFixedChronology.getLeapYearsBefore(1L));
        assertEquals(1L, InternationalFixedChronology.getLeapYearsBefore(4L));
        assertEquals(24L, InternationalFixedChronology.getLeapYearsBefore(100L));
        assertEquals(97L, InternationalFixedChronology.getLeapYearsBefore(400L));
    }

    // ----------------------------------------------------------------------
    // date(Era, yoe, m, d) and date(y, m, d)
    // ----------------------------------------------------------------------

    @Test
    public void date_withEra_valid() {
        InternationalFixedDate d = IFc.date(InternationalFixedEra.CE, 7, 7, 7);
        // Basic sanity checks
        assertEquals(7, d.get(ChronoField.YEAR_OF_ERA));
        assertEquals(7, d.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(7, d.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void date_withEra_invalidEraType() {
        assertThrows(ClassCastException.class, () -> IFc.date(JapaneseEra.HEISEI, 10, 1, 1));
        assertThrows(ClassCastException.class, () -> IFc.date(IsoEra.BCE, 10, 1, 1));
    }

    @Test
    public void date_prolepticYearMonthDay_valid() {
        InternationalFixedDate d = IFc.date(3309, 8, 10);
        assertEquals(8, d.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(10, d.get(ChronoField.DAY_OF_MONTH));
        assertEquals(365, d.lengthOfYear()); // 3309 is not a leap year
        assertEpochDayRoundTrip(d.toEpochDay());
    }

    @Test
    public void date_prolepticYearMonthDay_invalidDayInMonth() {
        // Months have 28 days, except month 12 always has 29, and month 6 has 29 only in leap years
        assertThrows(DateTimeException.class, () -> IFc.date(2021, 4, 29));
    }

    @Test
    public void month12_has29DaysAlways() {
        InternationalFixedDate d = IFc.date(2021, 12, 29);
        assertEquals(12, d.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(29, d.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void month6_day29_onlyInLeapYears() {
        assertThrows(DateTimeException.class, () -> IFc.date(2019, 6, 29)); // not leap
        InternationalFixedDate d = IFc.date(2020, 6, 29);                   // leap
        assertEquals(6, d.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(29, d.get(ChronoField.DAY_OF_MONTH));
    }

    // ----------------------------------------------------------------------
    // dateYearDay(y, doy) and dateYearDay(era, yoe, doy)
    // ----------------------------------------------------------------------

    @Test
    public void dateYearDay_valid_nonLeapYear() {
        InternationalFixedDate d = IFc.dateYearDay(134, 134);
        assertEquals(134, d.get(ChronoField.DAY_OF_YEAR));
        assertEquals(365, d.lengthOfYear());
    }

    @Test
    public void dateYearDay_valid_leapYearDay366() {
        InternationalFixedDate d = IFc.dateYearDay(2020, 366);
        assertEquals(366, d.get(ChronoField.DAY_OF_YEAR));
        assertEquals(366, d.lengthOfYear());
    }

    @Test
    public void dateYearDay_invalid_nonLeapYearDay366() {
        assertThrows(DateTimeException.class, () -> IFc.dateYearDay(2019, 366));
    }

    @Test
    public void dateYearDay_withEra_valid() {
        InternationalFixedDate d = IFc.dateYearDay(InternationalFixedEra.CE, 157, 3);
        assertEquals(3, d.get(ChronoField.DAY_OF_YEAR));
        assertEquals(157, d.get(ChronoField.YEAR_OF_ERA));
    }

    @Test
    public void dateYearDay_withEra_invalidEraType() {
        assertThrows(ClassCastException.class, () -> IFc.dateYearDay(JapaneseEra.HEISEI, 734, 10));
    }

    // ----------------------------------------------------------------------
    // Epoch day
    // ----------------------------------------------------------------------

    @Test
    public void dateEpochDay_roundTripSamples() {
        assertEpochDayRoundTrip(-1);
        assertEpochDayRoundTrip(0);
        assertEpochDayRoundTrip(146096); // end of 400-year cycle - 1
    }

    @Test
    public void dateEpochDay_outOfRange() {
        assertThrows(DateTimeException.class, () -> IFc.dateEpochDay(Long.MIN_VALUE));
    }

    private static void assertEpochDayRoundTrip(long epochDay) {
        InternationalFixedDate d = IFc.dateEpochDay(epochDay);
        assertEquals(epochDay, d.toEpochDay());
    }

    // ----------------------------------------------------------------------
    // Now methods
    // ----------------------------------------------------------------------

    @Test
    public void dateNow_withClock_fixedInstant() {
        Clock fixed = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
        InternationalFixedDate today = IFc.dateNow(fixed);
        assertEquals(0L, today.toEpochDay());
        assertEquals(IFc.dateEpochDay(0L), today);
    }

    @Test
    public void dateNow_zone_nonNull() {
        // Not asserting a specific date, just that it returns something for a given zone
        InternationalFixedDate todayUtc = IFc.dateNow(ZoneOffset.UTC);
        assertNotNull(todayUtc);
    }

    @Test
    public void dateNow_nullArguments() {
        assertThrows(NullPointerException.class, () -> IFc.dateNow((ZoneId) null));
        assertThrows(NullPointerException.class, () -> IFc.dateNow((Clock) null));
    }

    // ----------------------------------------------------------------------
    // Conversions from TemporalAccessor
    // ----------------------------------------------------------------------

    @Test
    public void date_fromInternationalFixedDate_returnsSame() {
        InternationalFixedDate src = IFc.date(2024, 12, 29);
        assertEquals(src, IFc.date(src));
    }

    @Test
    public void date_fromUnsupportedTemporal_throws() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> IFc.date(Month.MAY));
    }

    @Test
    public void date_nullTemporal_throws() {
        assertThrows(NullPointerException.class, () -> IFc.date((TemporalAccessor) null));
    }

    @Test
    public void localDateTime_fromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        ChronoLocalDateTime<InternationalFixedDate> cldt = IFc.localDateTime(ldt);
        assertNotNull(cldt);
    }

    @Test
    public void localDateTime_fromUnsupportedTemporal_throws() {
        assertThrows(DateTimeException.class, () -> IFc.localDateTime(Year.of(2024)));
    }

    @Test
    public void localDateTime_nullTemporal_throws() {
        assertThrows(NullPointerException.class, () -> IFc.localDateTime((TemporalAccessor) null));
    }

    @Test
    public void zonedDateTime_fromZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
        ChronoZonedDateTime<InternationalFixedDate> czdt = IFc.zonedDateTime(zdt);
        assertNotNull(czdt);
    }

    @Test
    public void zonedDateTime_fromUnsupportedTemporal_throws() {
        assertThrows(DateTimeException.class, () -> IFc.zonedDateTime(JapaneseEra.HEISEI));
    }

    @Test
    public void zonedDateTime_fromInstantAndZone() {
        ChronoZonedDateTime<InternationalFixedDate> czdt = IFc.zonedDateTime(Instant.EPOCH, ZoneOffset.UTC);
        assertNotNull(czdt);
    }

    @Test
    public void zonedDateTime_nullArguments_throws() {
        assertThrows(NullPointerException.class, () -> IFc.zonedDateTime((Instant) null, ZoneOffset.UTC));
        assertThrows(NullPointerException.class, () -> IFc.zonedDateTime(Instant.EPOCH, null));
    }

    // ----------------------------------------------------------------------
    // prolepticYear
    // ----------------------------------------------------------------------

    @Test
    public void prolepticYear_valid() {
        assertEquals(734, IFc.prolepticYear(InternationalFixedEra.CE, 734));
    }

    @Test
    public void prolepticYear_invalidYearOfEra() {
        assertThrows(DateTimeException.class, () -> IFc.prolepticYear(InternationalFixedEra.CE, 0));
        assertThrows(DateTimeException.class, () -> IFc.prolepticYear(InternationalFixedEra.CE, 1_000_001));
    }

    @Test
    public void prolepticYear_invalidEraType() {
        assertThrows(ClassCastException.class, () -> IFc.prolepticYear(IsoEra.BCE, 10));
    }

    // ----------------------------------------------------------------------
    // Field ranges
    // ----------------------------------------------------------------------

    @Test
    public void range_notNullForRepresentativeFields() {
        assertNotNull(IFc.range(ChronoField.YEAR));
        assertNotNull(IFc.range(ChronoField.YEAR_OF_ERA));
        assertNotNull(IFc.range(ChronoField.PROLEPTIC_MONTH));
        assertNotNull(IFc.range(ChronoField.DAY_OF_YEAR));
        assertNotNull(IFc.range(ChronoField.DAY_OF_MONTH));
        assertNotNull(IFc.range(ChronoField.MONTH_OF_YEAR));
        assertNotNull(IFc.range(ChronoField.EPOCH_DAY));
        assertNotNull(IFc.range(ChronoField.ERA));
    }

    @Test
    public void range_containsExpectedValues() {
        ValueRange y = IFc.range(ChronoField.YEAR);
        assertTrue(y.isValidValue(1));
        assertTrue(y.isValidValue(1_000_000));
        assertFalse(y.isValidValue(0));

        ValueRange moy = IFc.range(ChronoField.MONTH_OF_YEAR);
        assertTrue(moy.isValidValue(1));
        assertTrue(moy.isValidValue(13));
        assertFalse(moy.isValidValue(14));

        ValueRange dom = IFc.range(ChronoField.DAY_OF_MONTH);
        assertTrue(dom.isValidValue(1));
        assertTrue(dom.isValidValue(29));
        assertFalse(dom.isValidValue(30));

        ValueRange doy = IFc.range(ChronoField.DAY_OF_YEAR);
        assertTrue(doy.isValidValue(1));
        assertTrue(doy.isValidValue(365));
    }

    @Test
    public void range_nullField_throws() {
        assertThrows(NullPointerException.class, () -> IFc.range(null));
    }
}