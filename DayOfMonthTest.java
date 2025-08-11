package org.threeten.extra;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MICRO_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_DAY;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.SECOND_OF_DAY;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import com.google.common.testing.EqualsTester;

public class TestDayOfMonth {

    private static final int MAX_DOM = 31;
    private static final DayOfMonth SAMPLE = DayOfMonth.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    private static final int YEAR_NON_LEAP = 2007;
    private static final int YEAR_LEAP = 2008;

    // Simple custom field that delegates to DAY_OF_MONTH, used to test derived fields
    private static class TestingField implements TemporalField {
        static final TestingField INSTANCE = new TestingField();
        @Override public TemporalUnit getBaseUnit() { return ChronoUnit.DAYS; }
        @Override public TemporalUnit getRangeUnit() { return ChronoUnit.MONTHS; }
        @Override public ValueRange range() { return ValueRange.of(1, 28, 31); }
        @Override public boolean isDateBased() { return true; }
        @Override public boolean isTimeBased() { return false; }
        @Override public boolean isSupportedBy(TemporalAccessor temporal) { return temporal.isSupported(DAY_OF_MONTH); }
        @Override public ValueRange rangeRefinedBy(TemporalAccessor temporal) { return range(); }
        @Override public long getFrom(TemporalAccessor temporal) { return temporal.getLong(DAY_OF_MONTH); }
        @SuppressWarnings("unchecked")
        @Override public <R extends Temporal> R adjustInto(R temporal, long newValue) { return (R) temporal.with(DAY_OF_MONTH, newValue); }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------
    private static int lengthOfMonth(int year, Month month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    private static void assertUnsupportedFields(DayOfMonth dom, TemporalField... fields) {
        for (TemporalField f : fields) {
            assertFalse(dom.isSupported(f), "Expected unsupported: " + f);
        }
    }

    private static void assertFromAcrossYear(int year) {
        LocalDate d = LocalDate.of(year, 1, 1);
        for (Month m : Month.values()) {
            int len = lengthOfMonth(year, m);
            for (int day = 1; day <= len; day++) {
                assertEquals(day, DayOfMonth.from(d).getValue(), "Mismatch on " + d);
                d = d.plusDays(1);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Interfaces / serialization
    // ---------------------------------------------------------------------
    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class));
    }

    @Test
    public void test_serialization_roundtrip() throws IOException, ClassNotFoundException {
        DayOfMonth dom = DayOfMonth.of(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(dom);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(dom, ois.readObject());
        }
    }

    // ---------------------------------------------------------------------
    // now()
    // ---------------------------------------------------------------------
    @RetryingTest(100) // tolerate races at midnight
    public void test_now() {
        assertEquals(LocalDate.now().getDayOfMonth(), DayOfMonth.now().getValue());
    }

    @RetryingTest(100)
    public void test_now_ZoneId() {
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(tokyo).getDayOfMonth(), DayOfMonth.now(tokyo).getValue());
    }

    @Test
    public void test_now_clock() {
        for (int i = 1; i <= 31; i++) { // January 2008 has 31 days
            Instant instant = LocalDate.of(2008, 1, i).atStartOfDay(PARIS).toInstant();
            Clock clock = Clock.fixed(instant, PARIS);
            assertEquals(i, DayOfMonth.now(clock).getValue());
        }
    }

    // ---------------------------------------------------------------------
    // of(int)
    // ---------------------------------------------------------------------
    @Test
    public void test_of_int_singleton_and_value() {
        for (int i = 1; i <= MAX_DOM; i++) {
            DayOfMonth dom = DayOfMonth.of(i);
            assertEquals(i, dom.getValue());
            assertSame(dom, DayOfMonth.of(i), "Instances should be cached singletons");
        }
    }

    @Test public void test_of_int_tooLow() { assertThrows(DateTimeException.class, () -> DayOfMonth.of(0)); }
    @Test public void test_of_int_tooHigh() { assertThrows(DateTimeException.class, () -> DayOfMonth.of(32)); }

    // ---------------------------------------------------------------------
    // from(TemporalAccessor)
    // ---------------------------------------------------------------------
    @Test
    public void test_from_TemporalAccessor_notLeapYear_fullYear() {
        assertFromAcrossYear(YEAR_NON_LEAP);
    }

    @Test
    public void test_from_TemporalAccessor_leapYear_fullYear() {
        assertFromAcrossYear(YEAR_LEAP);
    }

    @Test
    public void test_from_TemporalAccessor_DayOfMonth_identity() {
        DayOfMonth dom = DayOfMonth.of(6);
        assertEquals(dom, DayOfMonth.from(dom));
    }

    @Test
    public void test_from_TemporalAccessor_nonIso() {
        LocalDate today = LocalDate.now();
        assertEquals(today.getDayOfMonth(), DayOfMonth.from(JapaneseDate.from(today)).getValue());
    }

    @Test public void test_from_TemporalAccessor_noDerive() { assertThrows(DateTimeException.class, () -> DayOfMonth.from(LocalTime.NOON)); }
    @Test public void test_from_TemporalAccessor_null() { assertThrows(NullPointerException.class, () -> DayOfMonth.from((TemporalAccessor) null)); }

    @Test
    public void test_from_parse_CharSequence() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("d");
        assertEquals(DayOfMonth.of(3), f.parse("3", DayOfMonth::from));
    }

    // ---------------------------------------------------------------------
    // isSupported(TemporalField)
    // ---------------------------------------------------------------------
    @Test
    public void test_isSupported() {
        assertFalse(SAMPLE.isSupported((TemporalField) null));

        assertUnsupportedFields(SAMPLE,
                NANO_OF_SECOND, NANO_OF_DAY, MICRO_OF_SECOND, MICRO_OF_DAY,
                MILLI_OF_SECOND, MILLI_OF_DAY, SECOND_OF_MINUTE, SECOND_OF_DAY,
                MINUTE_OF_HOUR, MINUTE_OF_DAY, HOUR_OF_AMPM, CLOCK_HOUR_OF_AMPM,
                HOUR_OF_DAY, CLOCK_HOUR_OF_DAY, AMPM_OF_DAY,
                DAY_OF_WEEK, ALIGNED_DAY_OF_WEEK_IN_MONTH, ALIGNED_DAY_OF_WEEK_IN_YEAR,
                DAY_OF_YEAR, EPOCH_DAY, ALIGNED_WEEK_OF_MONTH, ALIGNED_WEEK_OF_YEAR,
                MONTH_OF_YEAR, PROLEPTIC_MONTH, YEAR_OF_ERA, YEAR, ERA,
                INSTANT_SECONDS, OFFSET_SECONDS, IsoFields.DAY_OF_QUARTER);

        assertTrue(SAMPLE.isSupported(DAY_OF_MONTH));
        assertTrue(SAMPLE.isSupported(TestingField.INSTANCE));
    }

    // ---------------------------------------------------------------------
    // range(TemporalField)
    // ---------------------------------------------------------------------
    @Test public void test_range_supported() { assertEquals(DAY_OF_MONTH.range(), SAMPLE.range(DAY_OF_MONTH)); }
    @Test public void test_range_invalidField() { assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE.range(MONTH_OF_YEAR)); }
    @Test public void test_range_null() { assertThrows(NullPointerException.class, () -> SAMPLE.range((TemporalField) null)); }

    // ---------------------------------------------------------------------
    // get(TemporalField) / getLong(TemporalField)
    // ---------------------------------------------------------------------
    @Test public void test_get() { assertEquals(12, SAMPLE.get(DAY_OF_MONTH)); }
    @Test public void test_get_invalidField() { assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE.get(MONTH_OF_YEAR)); }
    @Test public void test_get_null() { assertThrows(NullPointerException.class, () -> SAMPLE.get((TemporalField) null)); }

    @Test public void test_getLong() { assertEquals(12L, SAMPLE.getLong(DAY_OF_MONTH)); }
    @Test public void test_getLong_derivedField() { assertEquals(12L, SAMPLE.getLong(TestingField.INSTANCE)); }
    @Test public void test_getLong_invalidField() { assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE.getLong(MONTH_OF_YEAR)); }
    @Test public void test_getLong_invalidField2() { assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE.getLong(IsoFields.DAY_OF_QUARTER)); }
    @Test public void test_getLong_null() { assertThrows(NullPointerException.class, () -> SAMPLE.getLong((TemporalField) null)); }

    // ---------------------------------------------------------------------
    // isValidYearMonth(YearMonth)
    // ---------------------------------------------------------------------
    @Test
    public void test_isValidYearMonth_for31() {
        DayOfMonth d31 = DayOfMonth.of(31);
        for (Month m : Month.values()) {
            boolean expected = YearMonth.of(2012, m).lengthOfMonth() >= 31;
            assertEquals(expected, d31.isValidYearMonth(YearMonth.of(2012, m)), "Month: " + m);
        }
    }

    @Test
    public void test_isValidYearMonth_for30() {
        DayOfMonth d30 = DayOfMonth.of(30);
        for (Month m : Month.values()) {
            boolean expected = YearMonth.of(2012, m).lengthOfMonth() >= 30;
            assertEquals(expected, d30.isValidYearMonth(YearMonth.of(2012, m)), "Month: " + m);
        }
    }

    @Test
    public void test_isValidYearMonth_for29() {
        DayOfMonth d29 = DayOfMonth.of(29);
        for (Month m : Month.values()) {
            assertTrue(d29.isValidYearMonth(YearMonth.of(2012, m)), "Leap year 2012 should allow 29 in " + m);
        }
        assertFalse(d29.isValidYearMonth(YearMonth.of(2011, 2)), "Non-leap Feb should not allow 29");
    }

    @Test
    public void test_isValidYearMonth_for28() {
        DayOfMonth d28 = DayOfMonth.of(28);
        for (Month m : Month.values()) {
            assertTrue(d28.isValidYearMonth(YearMonth.of(2012, m)));
        }
    }

    @Test public void test_isValidYearMonth_null() { assertFalse(SAMPLE.isValidYearMonth((YearMonth) null)); }

    // ---------------------------------------------------------------------
    // query(TemporalQuery)
    // ---------------------------------------------------------------------
    @Test
    public void test_query() {
        assertEquals(IsoChronology.INSTANCE, SAMPLE.query(TemporalQueries.chronology()));
        assertEquals(null, SAMPLE.query(TemporalQueries.localDate()));
        assertEquals(null, SAMPLE.query(TemporalQueries.localTime()));
        assertEquals(null, SAMPLE.query(TemporalQueries.offset()));
        assertEquals(null, SAMPLE.query(TemporalQueries.precision()));
        assertEquals(null, SAMPLE.query(TemporalQueries.zone()));
        assertEquals(null, SAMPLE.query(TemporalQueries.zoneId()));
    }

    // ---------------------------------------------------------------------
    // adjustInto(Temporal)
    // ---------------------------------------------------------------------
    @Test
    public void test_adjustInto_valid_month() {
        LocalDate jan2007 = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= 31; day++) {
            assertEquals(jan2007.withDayOfMonth(day), DayOfMonth.of(day).adjustInto(jan2007));
        }
    }

    @Test public void test_adjustInto_april31_invalid() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(31).adjustInto(LocalDate.of(2007, 4, 1)));
    }

    @Test public void test_adjustInto_february29_notLeapYear_invalid() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(29).adjustInto(LocalDate.of(2007, 2, 1)));
    }

    @Test public void test_adjustInto_nonIso() { assertThrows(DateTimeException.class, () -> SAMPLE.adjustInto(JapaneseDate.now())); }
    @Test public void test_adjustInto_null() { assertThrows(NullPointerException.class, () -> SAMPLE.adjustInto((Temporal) null)); }

    // ---------------------------------------------------------------------
    // atMonth(Month) / atMonth(int)
    // ---------------------------------------------------------------------
    @Test
    public void test_atMonth_Month_31() {
        DayOfMonth d31 = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), d31.atMonth(JANUARY));
        assertEquals(MonthDay.of(2, 29), d31.atMonth(FEBRUARY));
        assertEquals(MonthDay.of(3, 31), d31.atMonth(MARCH));
        assertEquals(MonthDay.of(4, 30), d31.atMonth(APRIL));
        assertEquals(MonthDay.of(5, 31), d31.atMonth(MAY));
        assertEquals(MonthDay.of(6, 30), d31.atMonth(JUNE));
        assertEquals(MonthDay.of(7, 31), d31.atMonth(JULY));
        assertEquals(MonthDay.of(8, 31), d31.atMonth(AUGUST));
        assertEquals(MonthDay.of(9, 30), d31.atMonth(SEPTEMBER));
        assertEquals(MonthDay.of(10, 31), d31.atMonth(OCTOBER));
        assertEquals(MonthDay.of(11, 30), d31.atMonth(NOVEMBER));
        assertEquals(MonthDay.of(12, 31), d31.atMonth(DECEMBER));
    }

    @Test
    public void test_atMonth_Month_28_allMonths() {
        DayOfMonth d28 = DayOfMonth.of(28);
        for (Month m : Month.values()) {
            assertEquals(MonthDay.of(m, 28), d28.atMonth(m));
        }
    }

    @Test public void test_atMonth_Month_null() { assertThrows(NullPointerException.class, () -> SAMPLE.atMonth((Month) null)); }

    @Test
    public void test_atMonth_int_31() {
        DayOfMonth d31 = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), d31.atMonth(1));
        assertEquals(MonthDay.of(2, 29), d31.atMonth(2));
        assertEquals(MonthDay.of(3, 31), d31.atMonth(3));
        assertEquals(MonthDay.of(4, 30), d31.atMonth(4));
        assertEquals(MonthDay.of(5, 31), d31.atMonth(5));
        assertEquals(MonthDay.of(6, 30), d31.atMonth(6));
        assertEquals(MonthDay.of(7, 31), d31.atMonth(7));
        assertEquals(MonthDay.of(8, 31), d31.atMonth(8));
        assertEquals(MonthDay.of(9, 30), d31.atMonth(9));
        assertEquals(MonthDay.of(10, 31), d31.atMonth(10));
        assertEquals(MonthDay.of(11, 30), d31.atMonth(11));
        assertEquals(MonthDay.of(12, 31), d31.atMonth(12));
    }

    @Test
    public void test_atMonth_int_28_allMonths() {
        DayOfMonth d28 = DayOfMonth.of(28);
        for (int m = 1; m <= 12; m++) {
            assertEquals(MonthDay.of(m, 28), d28.atMonth(m));
        }
    }

    @Test public void test_atMonth_int_tooLow() { assertThrows(DateTimeException.class, () -> SAMPLE.atMonth(0)); }
    @Test public void test_atMonth_int_tooHigh() { assertThrows(DateTimeException.class, () -> SAMPLE.atMonth(13)); }

    // ---------------------------------------------------------------------
    // atYearMonth(YearMonth)
    // ---------------------------------------------------------------------
    @Test
    public void test_atYearMonth_31_various() {
        DayOfMonth d31 = DayOfMonth.of(31);
        for (Month m : Month.values()) {
            YearMonth ym2012 = YearMonth.of(2012, m);
            int expected = ym2012.lengthOfMonth() >= 31 ? 31 : ym2012.lengthOfMonth();
            assertEquals(LocalDate.of(2012, m, expected), d31.atYearMonth(ym2012));
        }
        assertEquals(LocalDate.of(2011, 2, 28), d31.atYearMonth(YearMonth.of(2011, 2)));
    }

    @Test
    public void test_atYearMonth_28_allMonths() {
        DayOfMonth d28 = DayOfMonth.of(28);
        for (Month m : Month.values()) {
            assertEquals(LocalDate.of(2012, m, 28), d28.atYearMonth(YearMonth.of(2012, m)));
        }
    }

    @Test public void test_atYearMonth_null() { assertThrows(NullPointerException.class, () -> SAMPLE.atYearMonth((YearMonth) null)); }

    // ---------------------------------------------------------------------
    // compareTo()
    // ---------------------------------------------------------------------
    @Test
    public void test_compareTo_sign() {
        for (int i = 1; i <= MAX_DOM; i++) {
            for (int j = 1; j <= MAX_DOM; j++) {
                int expected = Integer.compare(i, j);
                assertEquals(expected, DayOfMonth.of(i).compareTo(DayOfMonth.of(j)),
                        () -> "compareTo mismatch for " + i + " vs " + j);
            }
        }
    }

    @Test
    public void test_compareTo_null() {
        DayOfMonth dom = DayOfMonth.of(1);
        assertThrows(NullPointerException.class, () -> dom.compareTo(null));
    }

    // ---------------------------------------------------------------------
    // equals() / hashCode()
    // ---------------------------------------------------------------------
    @Test
    public void test_equals_and_hashCode() {
        EqualsTester equalsTester = new EqualsTester();
        for (int i = 1; i <= MAX_DOM; i++) {
            equalsTester.addEqualityGroup(DayOfMonth.of(i), DayOfMonth.of(i));
        }
        equalsTester.testEquals();
    }

    // ---------------------------------------------------------------------
    // toString()
    // ---------------------------------------------------------------------
    @Test
    public void test_toString_allValues() {
        for (int i = 1; i <= MAX_DOM; i++) {
            assertEquals("DayOfMonth:" + i, DayOfMonth.of(i).toString());
        }
    }
}