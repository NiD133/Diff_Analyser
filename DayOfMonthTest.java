package org.threeten.extra;

import static java.time.Month.*;
import static java.time.temporal.ChronoField.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import com.google.common.testing.EqualsTester;

/**
 * Test suite for the DayOfMonth class.
 */
public class TestDayOfMonth {

    private static final int MAX_DAY = 31;
    private static final DayOfMonth TEST_DAY = DayOfMonth.of(12);
    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");

    /**
     * Custom TemporalField for testing purposes.
     */
    private static class TestingField implements TemporalField {
        public static final TestingField INSTANCE = new TestingField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.MONTHS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 28, 31);
        }

        @Override
        public boolean isDateBased() {
            return true;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(DAY_OF_MONTH);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(DAY_OF_MONTH);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_MONTH, newValue);
        }
    }

    // Test interfaces implemented by DayOfMonth
    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class));
    }

    // Test serialization of DayOfMonth
    @Test
    public void test_serialization() throws IOException, ClassNotFoundException {
        DayOfMonth test = DayOfMonth.of(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(test);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(test, ois.readObject());
        }
    }

    // Test now() methods
    @RetryingTest(100)
    public void test_now() {
        assertEquals(LocalDate.now().getDayOfMonth(), DayOfMonth.now().getValue());
    }

    @RetryingTest(100)
    public void test_now_ZoneId() {
        ZoneId zone = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(zone).getDayOfMonth(), DayOfMonth.now(zone).getValue());
    }

    // Test of(int) method
    @Test
    public void test_of_int_singleton() {
        for (int i = 1; i <= MAX_DAY; i++) {
            DayOfMonth test = DayOfMonth.of(i);
            assertEquals(i, test.getValue());
            assertSame(test, DayOfMonth.of(i));
        }
    }

    @Test
    public void test_of_int_tooLow() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(0));
    }

    @Test
    public void test_of_int_tooHigh() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(32));
    }

    // Test from(TemporalAccessor) method
    @Test
    public void test_from_TemporalAccessor_notLeapYear() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int i = 1; i <= 31; i++) {  // January
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 28; i++) {  // February
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // March
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 30; i++) {  // April
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // May
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 30; i++) {  // June
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // July
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // August
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 30; i++) {  // September
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // October
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 30; i++) {  // November
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // December
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void test_from_TemporalAccessor_leapYear() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int i = 1; i <= 31; i++) {  // January
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 29; i++) {  // February
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
        for (int i = 1; i <= 31; i++) {  // March
            assertEquals(i, DayOfMonth.from(date).getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void test_from_TemporalAccessor_DayOfMonth() {
        DayOfMonth dom = DayOfMonth.of(6);
        assertEquals(dom, DayOfMonth.from(dom));
    }

    @Test
    public void test_from_TemporalAccessor_nonIso() {
        LocalDate date = LocalDate.now();
        assertEquals(date.getDayOfMonth(), DayOfMonth.from(JapaneseDate.from(date)).getValue());
    }

    @Test
    public void test_from_TemporalAccessor_noDerive() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(LocalTime.NOON));
    }

    @Test
    public void test_from_TemporalAccessor_null() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.from((TemporalAccessor) null));
    }

    @Test
    public void test_from_parse_CharSequence() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        assertEquals(DayOfMonth.of(3), formatter.parse("3", DayOfMonth::from));
    }

    // Test isSupported(TemporalField) method
    @Test
    public void test_isSupported() {
        assertFalse(TEST_DAY.isSupported(null));
        assertFalse(TEST_DAY.isSupported(NANO_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(NANO_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MICRO_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(MICRO_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MILLI_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(MILLI_OF_DAY));
        assertFalse(TEST_DAY.isSupported(SECOND_OF_MINUTE));
        assertFalse(TEST_DAY.isSupported(SECOND_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MINUTE_OF_HOUR));
        assertFalse(TEST_DAY.isSupported(MINUTE_OF_DAY));
        assertFalse(TEST_DAY.isSupported(HOUR_OF_AMPM));
        assertFalse(TEST_DAY.isSupported(CLOCK_HOUR_OF_AMPM));
        assertFalse(TEST_DAY.isSupported(HOUR_OF_DAY));
        assertFalse(TEST_DAY.isSupported(CLOCK_HOUR_OF_DAY));
        assertFalse(TEST_DAY.isSupported(AMPM_OF_DAY));
        assertFalse(TEST_DAY.isSupported(DAY_OF_WEEK));
        assertFalse(TEST_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertFalse(TEST_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertTrue(TEST_DAY.isSupported(DAY_OF_MONTH));
        assertFalse(TEST_DAY.isSupported(DAY_OF_YEAR));
        assertFalse(TEST_DAY.isSupported(EPOCH_DAY));
        assertFalse(TEST_DAY.isSupported(ALIGNED_WEEK_OF_MONTH));
        assertFalse(TEST_DAY.isSupported(ALIGNED_WEEK_OF_YEAR));
        assertFalse(TEST_DAY.isSupported(MONTH_OF_YEAR));
        assertFalse(TEST_DAY.isSupported(PROLEPTIC_MONTH));
        assertFalse(TEST_DAY.isSupported(YEAR_OF_ERA));
        assertFalse(TEST_DAY.isSupported(YEAR));
        assertFalse(TEST_DAY.isSupported(ERA));
        assertFalse(TEST_DAY.isSupported(INSTANT_SECONDS));
        assertFalse(TEST_DAY.isSupported(OFFSET_SECONDS));
        assertFalse(TEST_DAY.isSupported(IsoFields.DAY_OF_QUARTER));
        assertTrue(TEST_DAY.isSupported(TestingField.INSTANCE));
    }

    // Test range(TemporalField) method
    @Test
    public void test_range() {
        assertEquals(DAY_OF_MONTH.range(), TEST_DAY.range(DAY_OF_MONTH));
    }

    @Test
    public void test_range_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.range(MONTH_OF_YEAR));
    }

    @Test
    public void test_range_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.range(null));
    }

    // Test get(TemporalField) method
    @Test
    public void test_get() {
        assertEquals(12, TEST_DAY.get(DAY_OF_MONTH));
    }

    @Test
    public void test_get_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.get(MONTH_OF_YEAR));
    }

    @Test
    public void test_get_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.get(null));
    }

    // Test getLong(TemporalField) method
    @Test
    public void test_getLong() {
        assertEquals(12L, TEST_DAY.getLong(DAY_OF_MONTH));
    }

    @Test
    public void test_getLong_derivedField() {
        assertEquals(12L, TEST_DAY.getLong(TestingField.INSTANCE));
    }

    @Test
    public void test_getLong_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.getLong(MONTH_OF_YEAR));
    }

    @Test
    public void test_getLong_invalidField2() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void test_getLong_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.getLong(null));
    }

    // Test isValidYearMonth(YearMonth) method
    @Test
    public void test_isValidYearMonth_31() {
        DayOfMonth test = DayOfMonth.of(31);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 1)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 2)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 3)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 4)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 5)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 6)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 7)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 8)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 9)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 10)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 11)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 12)));
    }

    @Test
    public void test_isValidYearMonth_30() {
        DayOfMonth test = DayOfMonth.of(30);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 1)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 2)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 3)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 4)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 5)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 6)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 7)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 8)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 9)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 10)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 11)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 12)));
    }

    @Test
    public void test_isValidYearMonth_29() {
        DayOfMonth test = DayOfMonth.of(29);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 1)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 2)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 3)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 4)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 5)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 6)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 7)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 8)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 9)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 10)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 11)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 12)));
        assertFalse(test.isValidYearMonth(YearMonth.of(2011, 2)));
    }

    @Test
    public void test_isValidYearMonth_28() {
        DayOfMonth test = DayOfMonth.of(28);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 1)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 2)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 3)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 4)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 5)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 6)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 7)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 8)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 9)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 10)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 11)));
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 12)));
    }

    @Test
    public void test_isValidYearMonth_null() {
        assertFalse(TEST_DAY.isValidYearMonth(null));
    }

    // Test query(TemporalQuery) method
    @Test
    public void test_query() {
        assertEquals(IsoChronology.INSTANCE, TEST_DAY.query(TemporalQueries.chronology()));
        assertNull(TEST_DAY.query(TemporalQueries.localDate()));
        assertNull(TEST_DAY.query(TemporalQueries.localTime()));
        assertNull(TEST_DAY.query(TemporalQueries.offset()));
        assertNull(TEST_DAY.query(TemporalQueries.precision()));
        assertNull(TEST_DAY.query(TemporalQueries.zone()));
        assertNull(TEST_DAY.query(TemporalQueries.zoneId()));
    }

    // Test adjustInto(Temporal) method
    @Test
    public void test_adjustInto() {
        LocalDate base = LocalDate.of(2007, 1, 1);
        LocalDate expected = base;
        for (int i = 1; i <= MAX_DAY; i++) {  // January
            Temporal result = DayOfMonth.of(i).adjustInto(base);
            assertEquals(expected, result);
            expected = expected.plusDays(1);
        }
    }

    @Test
    public void test_adjustInto_april31() {
        LocalDate base = LocalDate.of(2007, 4, 1);
        DayOfMonth test = DayOfMonth.of(31);
        assertThrows(DateTimeException.class, () -> test.adjustInto(base));
    }

    @Test
    public void test_adjustInto_february29_notLeapYear() {
        LocalDate base = LocalDate.of(2007, 2, 1);
        DayOfMonth test = DayOfMonth.of(29);
        assertThrows(DateTimeException.class, () -> test.adjustInto(base));
    }

    @Test
    public void test_adjustInto_nonIso() {
        assertThrows(DateTimeException.class, () -> TEST_DAY.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void test_adjustInto_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.adjustInto(null));
    }

    // Test atMonth(Month) method
    @Test
    public void test_atMonth_Month_31() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), test.atMonth(JANUARY));
        assertEquals(MonthDay.of(2, 29), test.atMonth(FEBRUARY));
        assertEquals(MonthDay.of(3, 31), test.atMonth(MARCH));
        assertEquals(MonthDay.of(4, 30), test.atMonth(APRIL));
        assertEquals(MonthDay.of(5, 31), test.atMonth(MAY));
        assertEquals(MonthDay.of(6, 30), test.atMonth(JUNE));
        assertEquals(MonthDay.of(7, 31), test.atMonth(JULY));
        assertEquals(MonthDay.of(8, 31), test.atMonth(AUGUST));
        assertEquals(MonthDay.of(9, 30), test.atMonth(SEPTEMBER));
        assertEquals(MonthDay.of(10, 31), test.atMonth(OCTOBER));
        assertEquals(MonthDay.of(11, 30), test.atMonth(NOVEMBER));
        assertEquals(MonthDay.of(12, 31), test.atMonth(DECEMBER));
    }

    @Test
    public void test_atMonth_Month_28() {
        DayOfMonth test = DayOfMonth.of(28);
        assertEquals(MonthDay.of(1, 28), test.atMonth(JANUARY));
        assertEquals(MonthDay.of(2, 28), test.atMonth(FEBRUARY));
        assertEquals(MonthDay.of(3, 28), test.atMonth(MARCH));
        assertEquals(MonthDay.of(4, 28), test.atMonth(APRIL));
        assertEquals(MonthDay.of(5, 28), test.atMonth(MAY));
        assertEquals(MonthDay.of(6, 28), test.atMonth(JUNE));
        assertEquals(MonthDay.of(7, 28), test.atMonth(JULY));
        assertEquals(MonthDay.of(8, 28), test.atMonth(AUGUST));
        assertEquals(MonthDay.of(9, 28), test.atMonth(SEPTEMBER));
        assertEquals(MonthDay.of(10, 28), test.atMonth(OCTOBER));
        assertEquals(MonthDay.of(11, 28), test.atMonth(NOVEMBER));
        assertEquals(MonthDay.of(12, 28), test.atMonth(DECEMBER));
    }

    @Test
    public void test_atMonth_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.atMonth(null));
    }

    // Test atMonth(int) method
    @Test
    public void test_atMonth_int_31() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), test.atMonth(1));
        assertEquals(MonthDay.of(2, 29), test.atMonth(2));
        assertEquals(MonthDay.of(3, 31), test.atMonth(3));
        assertEquals(MonthDay.of(4, 30), test.atMonth(4));
        assertEquals(MonthDay.of(5, 31), test.atMonth(5));
        assertEquals(MonthDay.of(6, 30), test.atMonth(6));
        assertEquals(MonthDay.of(7, 31), test.atMonth(7));
        assertEquals(MonthDay.of(8, 31), test.atMonth(8));
        assertEquals(MonthDay.of(9, 30), test.atMonth(9));
        assertEquals(MonthDay.of(10, 31), test.atMonth(10));
        assertEquals(MonthDay.of(11, 30), test.atMonth(11));
        assertEquals(MonthDay.of(12, 31), test.atMonth(12));
    }

    @Test
    public void test_atMonth_int_28() {
        DayOfMonth test = DayOfMonth.of(28);
        assertEquals(MonthDay.of(1, 28), test.atMonth(1));
        assertEquals(MonthDay.of(2, 28), test.atMonth(2));
        assertEquals(MonthDay.of(3, 28), test.atMonth(3));
        assertEquals(MonthDay.of(4, 28), test.atMonth(4));
        assertEquals(MonthDay.of(5, 28), test.atMonth(5));
        assertEquals(MonthDay.of(6, 28), test.atMonth(6));
        assertEquals(MonthDay.of(7, 28), test.atMonth(7));
        assertEquals(MonthDay.of(8, 28), test.atMonth(8));
        assertEquals(MonthDay.of(9, 28), test.atMonth(9));
        assertEquals(MonthDay.of(10, 28), test.atMonth(10));
        assertEquals(MonthDay.of(11, 28), test.atMonth(11));
        assertEquals(MonthDay.of(12, 28), test.atMonth(12));
    }

    @Test
    public void test_atMonth_tooLow() {
        assertThrows(DateTimeException.class, () -> TEST_DAY.atMonth(0));
    }

    @Test
    public void test_atMonth_tooHigh() {
        assertThrows(DateTimeException.class, () -> TEST_DAY.atMonth(13));
    }

    // Test atYearMonth(YearMonth) method
    @Test
    public void test_atYearMonth_31() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(LocalDate.of(2012, 1, 31), test.atYearMonth(YearMonth.of(2012, 1)));
        assertEquals(LocalDate.of(2012, 2, 29), test.atYearMonth(YearMonth.of(2012, 2)));
        assertEquals(LocalDate.of(2012, 3, 31), test.atYearMonth(YearMonth.of(2012, 3)));
        assertEquals(LocalDate.of(2012, 4, 30), test.atYearMonth(YearMonth.of(2012, 4)));
        assertEquals(LocalDate.of(2012, 5, 31), test.atYearMonth(YearMonth.of(2012, 5)));
        assertEquals(LocalDate.of(2012, 6, 30), test.atYearMonth(YearMonth.of(2012, 6)));
        assertEquals(LocalDate.of(2012, 7, 31), test.atYearMonth(YearMonth.of(2012, 7)));
        assertEquals(LocalDate.of(2012, 8, 31), test.atYearMonth(YearMonth.of(2012, 8)));
        assertEquals(LocalDate.of(2012, 9, 30), test.atYearMonth(YearMonth.of(2012, 9)));
        assertEquals(LocalDate.of(2012, 10, 31), test.atYearMonth(YearMonth.of(2012, 10)));
        assertEquals(LocalDate.of(2012, 11, 30), test.atYearMonth(YearMonth.of(2012, 11)));
        assertEquals(LocalDate.of(2012, 12, 31), test.atYearMonth(YearMonth.of(2012, 12)));
        assertEquals(LocalDate.of(2011, 2, 28), test.atYearMonth(YearMonth.of(2011, 2)));
    }

    @Test
    public void test_atYearMonth_28() {
        DayOfMonth test = DayOfMonth.of(28);
        assertEquals(LocalDate.of(2012, 1, 28), test.atYearMonth(YearMonth.of(2012, 1)));
        assertEquals(LocalDate.of(2012, 2, 28), test.atYearMonth(YearMonth.of(2012, 2)));
        assertEquals(LocalDate.of(2012, 3, 28), test.atYearMonth(YearMonth.of(2012, 3)));
        assertEquals(LocalDate.of(2012, 4, 28), test.atYearMonth(YearMonth.of(2012, 4)));
        assertEquals(LocalDate.of(2012, 5, 28), test.atYearMonth(YearMonth.of(2012, 5)));
        assertEquals(LocalDate.of(2012, 6, 28), test.atYearMonth(YearMonth.of(2012, 6)));
        assertEquals(LocalDate.of(2012, 7, 28), test.atYearMonth(YearMonth.of(2012, 7)));
        assertEquals(LocalDate.of(2012, 8, 28), test.atYearMonth(YearMonth.of(2012, 8)));
        assertEquals(LocalDate.of(2012, 9, 28), test.atYearMonth(YearMonth.of(2012, 9)));
        assertEquals(LocalDate.of(2012, 10, 28), test.atYearMonth(YearMonth.of(2012, 10)));
        assertEquals(LocalDate.of(2012, 11, 28), test.atYearMonth(YearMonth.of(2012, 11)));
        assertEquals(LocalDate.of(2012, 12, 28), test.atYearMonth(YearMonth.of(2012, 12)));
    }

    @Test
    public void test_atYearMonth_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.atYearMonth(null));
    }

    // Test compareTo() method
    @Test
    public void test_compareTo() {
        for (int i = 1; i <= MAX_DAY; i++) {
            DayOfMonth a = DayOfMonth.of(i);
            for (int j = 1; j <= MAX_DAY; j++) {
                DayOfMonth b = DayOfMonth.of(j);
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0);
                    assertTrue(b.compareTo(a) > 0);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0);
                    assertTrue(b.compareTo(a) < 0);
                } else {
                    assertEquals(0, a.compareTo(b));
                    assertEquals(0, b.compareTo(a));
                }
            }
        }
    }

    @Test
    public void test_compareTo_nullDayOfMonth() {
        DayOfMonth dom = null;
        DayOfMonth test = DayOfMonth.of(1);
        assertThrows(NullPointerException.class, () -> test.compareTo(dom));
    }

    // Test equals() and hashCode() methods
    @Test
    public void test_equals_and_hashCode() {
        EqualsTester equalsTester = new EqualsTester();
        for (int i = 1; i <= MAX_DAY; i++) {
            equalsTester.addEqualityGroup(DayOfMonth.of(i), DayOfMonth.of(i));
        }
        equalsTester.testEquals();
    }

    // Test toString() method
    @Test
    public void test_toString() {
        for (int i = 1; i <= MAX_DAY; i++) {
            DayOfMonth a = DayOfMonth.of(i);
            assertEquals("DayOfMonth:" + i, a.toString());
        }
    }

    // Test now(Clock) method
    @Test
    public void test_now_clock() {
        for (int i = 1; i <= 31; i++) {  // January
            Instant instant = LocalDate.of(2008, 1, i).atStartOfDay(PARIS_ZONE).toInstant();
            Clock clock = Clock.fixed(instant, PARIS_ZONE);
            assertEquals(i, DayOfMonth.now(clock).getValue());
        }
    }
}