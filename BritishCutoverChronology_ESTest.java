package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BritishCutoverChronologyTest {

    private static final BritishCutoverChronology CHRONO = BritishCutoverChronology.INSTANCE;

    // Basic identity and metadata

    @Test
    public void getId_returnsBritishCutover() {
        assertEquals("BritishCutover", CHRONO.getId());
    }

    @Test
    public void getCalendarType_returnsNull() {
        assertNull(CHRONO.getCalendarType());
    }

    @Test
    public void getCutover_returnsKnownConstant() {
        assertEquals(LocalDate.of(1752, 9, 14), CHRONO.getCutover());
        assertEquals(BritishCutoverChronology.CUTOVER, CHRONO.getCutover());
    }

    // Eras

    @Test
    public void eras_containsTwoEras() {
        List<Era> eras = CHRONO.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.AD));
        assertTrue(eras.contains(JulianEra.BC));
    }

    @Test
    public void eraOf_valid() {
        assertEquals(JulianEra.BC, CHRONO.eraOf(0));
        assertEquals(JulianEra.AD, CHRONO.eraOf(1));
    }

    @Test
    public void eraOf_invalid_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.eraOf(4));
    }

    // Proleptic year

    @Test
    public void prolepticYear_withJulianEra() {
        assertEquals(13, CHRONO.prolepticYear(JulianEra.AD, 13));
        assertEquals(0, CHRONO.prolepticYear(JulianEra.AD, 0));
        assertEquals(-1777, CHRONO.prolepticYear(JulianEra.BC, 1778));
    }

    @Test
    public void prolepticYear_wrongEraType_throws() {
        assertThrows(ClassCastException.class, () -> CHRONO.prolepticYear(ThaiBuddhistEra.BE, -535));
    }

    // date(Era/Y/M/D) and date(Y/M/D)

    @Test
    public void date_withEra_valid() {
        BritishCutoverDate d = CHRONO.date(JulianEra.AD, 12, 6, 12);
        assertNotNull(d);
    }

    @Test
    public void date_withEra_invalidMonth_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.date(JulianEra.AD, 6, 1000, 1));
    }

    @Test
    public void date_withWrongEraType_throws() {
        assertThrows(ClassCastException.class, () -> CHRONO.date(MinguoEra.BEFORE_ROC, 293, 293, 293));
    }

    @Test
    public void date_YMD_invalidMonth_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.date(1737, 1737, 1737));
    }

    @Test
    public void date_YMD_invalidDayForMonth_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.date(9, 9, 31)); // September 31 does not exist
    }

    // dateYearDay

    @Test
    public void dateYearDay_withEra_valid() {
        BritishCutoverDate d = CHRONO.dateYearDay(JulianEra.BC, 5, 5);
        assertNotNull(d);
    }

    @Test
    public void dateYearDay_valid() {
        BritishCutoverDate d = CHRONO.dateYearDay(103, 103);
        assertNotNull(d);
    }

    @Test
    public void dateYearDay_negativeDay_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(JulianEra.BC, 1778, -5738));
    }

    @Test
    public void dateYearDay_day366InNonLeapYear_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(366, 366));
    }

    @Test
    public void dateYearDay_dayOfYearTooLarge_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(874, 874));
    }

    @Test
    public void dateYearDay_wrongEraType_throws() {
        assertThrows(ClassCastException.class, () -> CHRONO.dateYearDay(JapaneseEra.SHOWA, -5181, -5181));
    }

    // dateEpochDay

    @Test
    public void dateEpochDay_validNegative() {
        BritishCutoverDate d = CHRONO.dateEpochDay(-1813L);
        assertNotNull(d);
    }

    @Test
    public void dateEpochDay_tooSmall_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.dateEpochDay(-2_135_812_540L));
    }

    // date(Temporal), localDateTime(Temporal), zonedDateTime(Temporal)

    @Test
    public void date_fromTemporal_returnsSameInstance() {
        BritishCutoverDate today = CHRONO.dateNow();
        assertSame(today, CHRONO.date(today));
    }

    @Test
    public void date_fromInvalidTemporal_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.date(IsoEra.CE));
    }

    @Test
    public void date_fromNull_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.date((TemporalAccessor) null));
    }

    @Test
    public void localDateTime_fromZonedDateTime_valid() {
        ChronoLocalDateTime<BritishCutoverDate> ldt = CHRONO.localDateTime(ZonedDateTime.now());
        assertNotNull(ldt);
    }

    @Test
    public void localDateTime_fromLocalDate_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.localDateTime(BritishCutoverChronology.CUTOVER));
    }

    @Test
    public void localDateTime_fromNull_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.localDateTime((TemporalAccessor) null));
    }

    @Test
    public void zonedDateTime_fromTemporal_valid() {
        ChronoZonedDateTime<BritishCutoverDate> zdt = CHRONO.zonedDateTime(ZonedDateTime.now());
        assertNotNull(zdt);
    }

    @Test
    public void zonedDateTime_fromLocalDate_throws() {
        assertThrows(DateTimeException.class, () -> CHRONO.zonedDateTime(BritishCutoverChronology.CUTOVER));
    }

    @Test
    public void zonedDateTime_fromNullTemporal_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.zonedDateTime((TemporalAccessor) null));
    }

    // zonedDateTime(Instant, ZoneId)

    @Test
    public void zonedDateTime_fromInstantAndZone_valid() {
        ChronoZonedDateTime<BritishCutoverDate> zdt = CHRONO.zonedDateTime(Instant.ofEpochSecond(1004L), ZoneOffset.UTC);
        assertNotNull(zdt);
    }

    @Test
    public void zonedDateTime_nullInstant_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.zonedDateTime((Instant) null, ZoneOffset.UTC));
    }

    // Now/today APIs

    @Test
    public void dateNow_defaultAndZoneAndClock() {
        assertNotNull(CHRONO.dateNow());
        assertNotNull(CHRONO.dateNow(ZoneId.systemDefault()));
        assertNotNull(CHRONO.dateNow(Clock.systemDefaultZone()));
    }

    @Test
    public void dateNow_nullZone_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.dateNow((ZoneId) null));
    }

    @Test
    public void dateNow_nullClock_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.dateNow((Clock) null));
    }

    @Test
    public void dateNow_clockWithTooLargeInstant_throws() {
        Clock tooLarge = Clock.fixed(Instant.MAX, ZoneOffset.UTC);
        assertThrows(DateTimeException.class, () -> CHRONO.dateNow(tooLarge));
    }

    // Leap year

    @Test
    public void isLeapYear_examples() {
        assertFalse(CHRONO.isLeapYear(371));     // simple non-leap
        assertTrue(CHRONO.isLeapYear(1752));     // cutover year is treated correctly
        assertTrue(CHRONO.isLeapYear(3676));     // future leap by Gregorian rules
    }

    // Range for fields

    @Test
    public void range_knownFields() {
        assertEquals(BritishCutoverChronology.YOE_RANGE, CHRONO.range(ChronoField.YEAR_OF_ERA));
        assertEquals(BritishCutoverChronology.YEAR_RANGE, CHRONO.range(ChronoField.YEAR));
        assertEquals(BritishCutoverChronology.PROLEPTIC_MONTH_RANGE, CHRONO.range(ChronoField.PROLEPTIC_MONTH));
        assertEquals(BritishCutoverChronology.ALIGNED_WOY_RANGE, CHRONO.range(ChronoField.ALIGNED_WEEK_OF_YEAR));
        assertEquals(BritishCutoverChronology.ALIGNED_WOM_RANGE, CHRONO.range(ChronoField.ALIGNED_WEEK_OF_MONTH));
        assertEquals(BritishCutoverChronology.DOY_RANGE, CHRONO.range(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void range_nullField_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.range((ChronoField) null));
    }

    // resolveDate

    @Test
    public void resolveDate_withEpochDay() {
        Map<TemporalField, Long> fields = new HashMap<>();
        fields.put(ChronoField.EPOCH_DAY, 763L);
        assertNotNull(CHRONO.resolveDate(fields, ResolverStyle.STRICT));
    }

    @Test
    public void resolveDate_emptyMap_returnsNull() {
        Map<TemporalField, Long> fields = new HashMap<>();
        assertNull(CHRONO.resolveDate(fields, ResolverStyle.STRICT));
    }

    @Test
    public void resolveDate_invalidYearOfEra_throws() {
        Map<TemporalField, Long> fields = new HashMap<>();
        fields.put(ChronoField.YEAR_OF_ERA, -4135L);
        assertThrows(DateTimeException.class, () -> CHRONO.resolveDate(fields, ResolverStyle.STRICT));
    }

    @Test
    public void resolveDate_nullMap_throws() {
        assertThrows(NullPointerException.class, () -> CHRONO.resolveDate(null, ResolverStyle.SMART));
    }

    // Period operations with dates

    @Test
    public void period_appliedToDate_changesDate() {
        ChronoPeriod p = BritishCutoverChronology.INSTANCE.period(371, 371, 371);
        BritishCutoverDate start = BritishCutoverDate.ofEpochDay(371);
        BritishCutoverDate end = start.minus(p);
        assertNotEquals(start, end);
    }

    // Interop: derive era from a date and create another date

    @Test
    public void dateWithEra_fromExistingDateEra() {
        BritishCutoverDate fromCutover = BritishCutoverDate.from(BritishCutoverChronology.CUTOVER);
        Era era = fromCutover.getEra();
        BritishCutoverDate other = CHRONO.date(era, 5, 5, 5);
        assertNotSame(fromCutover, other);
    }
}