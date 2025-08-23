package org.threeten.extra;

import static org.junit.Assert.*;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

import org.junit.Test;

public class DayOfYearTest {

    // Fixed instants/zones for deterministic tests
    private static final Instant INSTANT_2020_02_15T101530Z = Instant.parse("2020-02-15T10:15:30Z"); // 2020-02-15 = day 46 (leap year)
    private static final Instant INSTANT_2019_03_01T000000Z = Instant.parse("2019-03-01T00:00:00Z"); // 2019-03-01 = day 60 (non-leap)
    private static final ZoneId UTC = ZoneOffset.UTC;

    // -------- Factory: now(Clock) --------

    @Test
    public void now_withFixedClock_returnsExpectedDayOfYear() {
        Clock clock = Clock.fixed(INSTANT_2020_02_15T101530Z, UTC);
        DayOfYear doy = DayOfYear.now(clock);
        assertEquals(46, doy.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void now_withNullClock_throwsNPE() {
        DayOfYear.now((Clock) null);
    }

    // -------- Factory: of(int) --------

    @Test
    public void of_validValues_createInstances() {
        assertEquals(1, DayOfYear.of(1).getValue());
        assertEquals(200, DayOfYear.of(200).getValue());
        assertEquals(366, DayOfYear.of(366).getValue());
    }

    @Test(expected = DateTimeException.class)
    public void of_zero_throwsDateTimeException() {
        DayOfYear.of(0);
    }

    @Test(expected = DateTimeException.class)
    public void of_greaterThan366_throwsDateTimeException() {
        DayOfYear.of(367);
    }

    // -------- Factory: from(TemporalAccessor) --------

    @Test
    public void from_localDate_extractsDayOfYear() {
        LocalDate date = LocalDate.of(2020, 2, 15); // day 46
        DayOfYear doy = DayOfYear.from(date);
        assertEquals(46, doy.getValue());
    }

    @Test
    public void from_dayOfYear_returnsSameValue() {
        DayOfYear source = DayOfYear.of(123);
        DayOfYear result = DayOfYear.from(source);
        assertEquals(123, result.getValue());
    }

    @Test(expected = DateTimeException.class)
    public void from_month_throwsDateTimeException() {
        DayOfYear.from(Month.NOVEMBER);
    }

    @Test(expected = NullPointerException.class)
    public void from_null_throwsNPE() {
        DayOfYear.from(null);
    }

    // -------- Querying support and values --------

    @Test
    public void isSupported_supportedField_returnsTrue() {
        DayOfYear doy = DayOfYear.of(100);
        assertTrue(doy.isSupported(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void isSupported_unsupportedField_returnsFalse() {
        DayOfYear doy = DayOfYear.of(100);
        assertFalse(doy.isSupported(ChronoField.HOUR_OF_DAY));
    }

    @Test
    public void isSupported_nullField_returnsFalse() {
        DayOfYear doy = DayOfYear.of(100);
        assertFalse(doy.isSupported((TemporalField) null));
    }

    @Test
    public void range_dayOfYear_returns1to366() {
        DayOfYear doy = DayOfYear.of(200);
        ValueRange r = doy.range(ChronoField.DAY_OF_YEAR);
        assertEquals(1, r.getMinimum());
        assertEquals(366, r.getMaximum());
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_unsupportedField_throws() {
        DayOfYear.of(200).range(ChronoField.EPOCH_DAY);
    }

    @Test(expected = NullPointerException.class)
    public void range_nullField_throwsNPE() {
        DayOfYear.of(200).range(null);
    }

    @Test
    public void get_supportedField_returnsValue() {
        DayOfYear doy = DayOfYear.of(45);
        assertEquals(45, doy.get(ChronoField.DAY_OF_YEAR));
        assertEquals(45L, doy.getLong(ChronoField.DAY_OF_YEAR));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void get_unsupportedField_throws() {
        DayOfYear.of(45).get(ChronoField.ERA);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void getLong_unsupportedField_throws() {
        DayOfYear.of(45).getLong(ChronoField.EPOCH_DAY);
    }

    @Test(expected = NullPointerException.class)
    public void get_nullField_throwsNPE() {
        DayOfYear.of(45).get(null);
    }

    @Test(expected = NullPointerException.class)
    public void getLong_nullField_throwsNPE() {
        DayOfYear.of(45).getLong(null);
    }

    // -------- isValidYear --------

    @Test
    public void isValidYear_forNon366AlwaysTrue() {
        assertTrue(DayOfYear.of(200).isValidYear(2019));
        assertTrue(DayOfYear.of(200).isValidYear(2020));
    }

    @Test
    public void isValidYear_for366TrueOnlyOnLeapYears() {
        DayOfYear leapDay = DayOfYear.of(366);
        assertFalse(leapDay.isValidYear(2019)); // not leap
        assertTrue(leapDay.isValidYear(2020));  // leap
    }

    // -------- atYear --------

    @Test
    public void atYear_int_validDate() {
        LocalDate expected = LocalDate.ofYearDay(2019, 200);
        assertEquals(expected, DayOfYear.of(200).atYear(2019));
    }

    @Test
    public void atYear_Year_validDate() {
        LocalDate expected = LocalDate.ofYearDay(2019, 200);
        assertEquals(expected, DayOfYear.of(200).atYear(Year.of(2019)));
    }

    @Test
    public void atYear_leapDayInLeapYear_returnsFeb29() {
        LocalDate date = DayOfYear.of(60).atYear(2020);
        assertEquals(LocalDate.of(2020, 2, 29), date);
    }

    @Test(expected = DateTimeException.class)
    public void atYear_366InNonLeapYear_int_throws() {
        DayOfYear.of(366).atYear(2019);
    }

    @Test(expected = DateTimeException.class)
    public void atYear_366InNonLeapYear_Year_throws() {
        DayOfYear.of(366).atYear(Year.of(2019));
    }

    @Test(expected = NullPointerException.class)
    public void atYear_nullYear_throwsNPE() {
        DayOfYear.of(10).atYear((Year) null);
    }

    // -------- adjustInto --------

    @Test
    public void adjustInto_localDate_setsDayOfYearPreservingYear() {
        DayOfYear doy = DayOfYear.of(200);
        LocalDate base = LocalDate.of(2019, 1, 1);
        Temporal adjusted = doy.adjustInto(base);
        assertEquals(LocalDate.ofYearDay(2019, 200), adjusted);
    }

    @Test(expected = DateTimeException.class)
    public void adjustInto_nonIsoTemporal_throwsDateTimeException() {
        DayOfYear doy = DayOfYear.of(10);
        // HijrahDate is non-ISO
        Temporal nonIso = HijrahDate.now();
        doy.adjustInto(nonIso);
    }

    @Test(expected = NullPointerException.class)
    public void adjustInto_null_throwsNPE() {
        DayOfYear.of(10).adjustInto((Temporal) null);
    }

    // -------- compareTo / equals / hashCode / toString --------

    @Test
    public void compareTo_ordersByValue() {
        assertTrue(DayOfYear.of(10).compareTo(DayOfYear.of(20)) < 0);
        assertTrue(DayOfYear.of(20).compareTo(DayOfYear.of(10)) > 0);
        assertEquals(0, DayOfYear.of(15).compareTo(DayOfYear.of(15)));
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_null_throwsNPE() {
        DayOfYear.of(1).compareTo(null);
    }

    @Test
    public void equalsAndHashCode_followContract() {
        DayOfYear a = DayOfYear.of(123);
        DayOfYear b = DayOfYear.of(123);
        DayOfYear c = DayOfYear.of(124);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
        assertNotEquals(a, null);
        assertNotEquals(a, "not-a-day-of-year");
    }

    @Test
    public void toString_containsValue() {
        assertEquals("DayOfYear:200", DayOfYear.of(200).toString());
    }

    // -------- getValue --------

    @Test
    public void getValue_returnsUnderlyingDay() {
        assertEquals(1, DayOfYear.of(1).getValue());
        assertEquals(366, DayOfYear.of(366).getValue());
    }
}