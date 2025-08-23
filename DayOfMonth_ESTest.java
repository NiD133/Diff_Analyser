package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.ValueRange;

import static org.junit.Assert.*;

public class DayOfMonthTest {

    // now(...)

    @Test
    public void now_withFixedClock_isDeterministic() {
        Clock clock = Clock.fixed(Instant.parse("2020-02-29T12:34:56Z"), ZoneOffset.UTC);

        DayOfMonth dom = DayOfMonth.now(clock);

        assertEquals(29, dom.getValue());
        assertEquals("DayOfMonth:29", dom.toString());
    }

    @Test
    public void now_withZoneId_isWithinValidRange() {
        DayOfMonth dom = DayOfMonth.now(ZoneId.of("Asia/Tokyo"));

        assertTrue(dom.getValue() >= 1 && dom.getValue() <= 31);
    }

    @Test
    public void now_withNullClock_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.now((Clock) null));
    }

    @Test
    public void now_withNullZone_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.now((ZoneId) null));
    }

    // of(...)

    @Test
    public void of_boundaries() {
        assertEquals(1, DayOfMonth.of(1).getValue());
        assertEquals(31, DayOfMonth.of(31).getValue());
    }

    @Test
    public void of_invalidValues_throwDateTimeException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(0));
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(32));
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(-5));
    }

    // from(...)

    @Test
    public void from_localDate_extractsDay() {
        DayOfMonth dom = DayOfMonth.from(LocalDate.of(2011, 12, 3));
        assertEquals(3, dom.getValue());
    }

    @Test
    public void from_unsupportedTemporal_throwsDateTimeException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(ZoneOffset.UTC));
    }

    @Test
    public void from_null_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.from(null));
    }

    // isSupported / range / get / getLong

    @Test
    public void isSupported_forDayOfMonth_true() {
        assertTrue(DayOfMonth.of(15).isSupported(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void isSupported_forOtherFields_falseOrNull() {
        DayOfMonth dom = DayOfMonth.of(10);
        assertFalse(dom.isSupported(ChronoField.MINUTE_OF_DAY));
        assertFalse(dom.isSupported(null));
    }

    @Test
    public void range_forDayOfMonth_is1to31() {
        ValueRange range = DayOfMonth.of(20).range(ChronoField.DAY_OF_MONTH);
        assertEquals(1L, range.getMinimum());
        assertEquals(31L, range.getMaximum());
    }

    @Test
    public void range_forUnsupportedField_throwsUnsupportedTemporalTypeException() {
        assertThrows(java.time.temporal.UnsupportedTemporalTypeException.class,
                () -> DayOfMonth.of(20).range(ChronoField.ERA));
    }

    @Test
    public void range_nullField_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.of(20).range(null));
    }

    @Test
    public void get_and_getLong_forDayOfMonth() {
        DayOfMonth dom = DayOfMonth.of(7);
        assertEquals(7, dom.get(ChronoField.DAY_OF_MONTH));
        assertEquals(7L, dom.getLong(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void get_forUnsupportedField_throwsUnsupportedTemporalTypeException() {
        DayOfMonth dom = DayOfMonth.of(7);
        assertThrows(java.time.temporal.UnsupportedTemporalTypeException.class,
                () -> dom.get(ChronoField.CLOCK_HOUR_OF_DAY));
    }

    @Test
    public void getLong_forUnsupportedField_throwsUnsupportedTemporalTypeException() {
        DayOfMonth dom = DayOfMonth.of(7);
        assertThrows(java.time.temporal.UnsupportedTemporalTypeException.class,
                () -> dom.getLong(ChronoField.INSTANT_SECONDS));
    }

    @Test
    public void get_withNullField_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.of(7).get(null));
    }

    @Test
    public void getLong_withNullField_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.of(7).getLong(null));
    }

    // isValidYearMonth(...)

    @Test
    public void isValidYearMonth_handlesMonthLength() {
        assertTrue(DayOfMonth.of(31).isValidYearMonth(YearMonth.of(2021, 1))); // January
        assertFalse(DayOfMonth.of(31).isValidYearMonth(YearMonth.of(2021, 4))); // April (30)
        assertTrue(DayOfMonth.of(29).isValidYearMonth(YearMonth.of(2020, 2))); // leap year Feb
        assertFalse(DayOfMonth.of(29).isValidYearMonth(YearMonth.of(2019, 2))); // non-leap Feb
    }

    @Test
    public void isValidYearMonth_withNull_returnsFalse() {
        assertFalse(DayOfMonth.of(10).isValidYearMonth(null));
    }

    // query(...)

    @Test
    public void query_chronology_returnsIso() {
        DayOfMonth dom = DayOfMonth.of(5);
        assertSame(IsoChronology.INSTANCE, dom.query(TemporalQueries.chronology()));
    }

    // adjustInto(...)

    @Test
    public void adjustInto_onIsoTemporal_setsDay() {
        DayOfMonth five = DayOfMonth.of(5);
        LocalDate date = LocalDate.of(2020, 3, 15);

        LocalDate adjusted = (LocalDate) five.adjustInto(date);

        assertEquals(LocalDate.of(2020, 3, 5), adjusted);
    }

    @Test
    public void adjustInto_onZonedDateTime_preservesOtherFields() {
        DayOfMonth dom = DayOfMonth.of(20);
        ZonedDateTime zdt = ZonedDateTime.of(2021, 1, 10, 8, 30, 0, 0, ZoneOffset.UTC);

        ZonedDateTime adjusted = (ZonedDateTime) dom.adjustInto(zdt);

        assertEquals(ZonedDateTime.of(2021, 1, 20, 8, 30, 0, 0, ZoneOffset.UTC), adjusted);
    }

    @Test
    public void adjustInto_invalidDay_throwsDateTimeException() {
        DayOfMonth thirtyOne = DayOfMonth.of(31);
        LocalDate feb2020 = LocalDate.of(2020, 2, 10);

        assertThrows(DateTimeException.class, () -> thirtyOne.adjustInto(feb2020));
    }

    @Test
    public void adjustInto_nonIsoTemporal_throwsDateTimeException() {
        DayOfMonth dom = DayOfMonth.of(10);
        HijrahDate hijrah = HijrahDate.now();

        assertThrows(DateTimeException.class, () -> dom.adjustInto(hijrah));
    }

    // atMonth(...) and atYearMonth(...)

    @Test
    public void atMonth_withEnum_clampsToLastValidDay() {
        MonthDay md = DayOfMonth.of(31).atMonth(Month.FEBRUARY);
        assertEquals(MonthDay.of(2, 29), md); // uses max length for month
    }

    @Test
    public void atMonth_withInt_clampsToLastValidDay() {
        MonthDay md = DayOfMonth.of(31).atMonth(2);
        assertEquals(MonthDay.of(2, 29), md);
    }

    @Test
    public void atMonth_withInvalidInt_throwsDateTimeException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(31).atMonth(13));
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(31).atMonth(0));
    }

    @Test
    public void atYearMonth_clampsToEndOfMonth() {
        LocalDate date = DayOfMonth.of(31).atYearMonth(YearMonth.of(2021, 4));
        assertEquals(LocalDate.of(2021, 4, 30), date);
    }

    @Test
    public void atYearMonth_withLeapYearFeb() {
        LocalDate date = DayOfMonth.of(31).atYearMonth(YearMonth.of(2020, 2));
        assertEquals(LocalDate.of(2020, 2, 29), date);
    }

    @Test
    public void atYearMonth_withNull_throwsNPE() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.of(10).atYearMonth(null));
    }

    // compareTo / equals / hashCode / toString

    @Test
    public void compareTo_ordersByNumericValue() {
        assertTrue(DayOfMonth.of(10).compareTo(DayOfMonth.of(20)) < 0);
        assertTrue(DayOfMonth.of(20).compareTo(DayOfMonth.of(10)) > 0);
        assertEquals(0, DayOfMonth.of(15).compareTo(DayOfMonth.of(15)));
    }

    @Test
    public void equals_and_hashCode() {
        DayOfMonth a = DayOfMonth.of(12);
        DayOfMonth b = DayOfMonth.of(12);
        DayOfMonth c = DayOfMonth.of(13);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertFalse(a.equals(ZoneOffset.UTC));
    }

    @Test
    public void toString_includesValue() {
        assertEquals("DayOfMonth:7", DayOfMonth.of(7).toString());
    }
}