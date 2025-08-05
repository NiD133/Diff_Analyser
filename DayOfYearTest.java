package org.threeten.extra;

import static java.time.temporal.ChronoField.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.*;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the DayOfYear class.
 */
public class TestDayOfYear {

    // Constants for test setup
    private static final Year YEAR_STANDARD = Year.of(2007);
    private static final Year YEAR_LEAP = Year.of(2008);
    private static final int STANDARD_YEAR_LENGTH = 365;
    private static final int LEAP_YEAR_LENGTH = 366;
    private static final DayOfYear TEST_DAY = DayOfYear.of(12);
    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");

    // Custom TemporalField for testing
    private static class TestingField implements TemporalField {
        public static final TestingField INSTANCE = new TestingField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.YEARS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 365, 366);
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
            return temporal.isSupported(DAY_OF_YEAR);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(DAY_OF_YEAR);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_YEAR, newValue);
        }
    }

    // Test interfaces implemented by DayOfYear
    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfYear.class));
    }

    // Test serialization of DayOfYear
    @Test
    public void test_serialization() throws IOException, ClassNotFoundException {
        DayOfYear original = DayOfYear.of(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertSame(original, ois.readObject());
        }
    }

    // Test DayOfYear.now() method
    @RetryingTest(100)
    public void test_now() {
        assertEquals(LocalDate.now().getDayOfYear(), DayOfYear.now().getValue());
    }

    // Test DayOfYear.now(ZoneId) method
    @RetryingTest(100)
    public void test_now_ZoneId() {
        ZoneId zone = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(zone).getDayOfYear(), DayOfYear.now(zone).getValue());
    }

    // Test DayOfYear.of(int) method
    @Test
    public void test_of_int() {
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(day, testDay.getValue());
            assertSame(testDay, DayOfYear.of(day));
        }
    }

    // Test DayOfYear.of(int) with invalid values
    @Test
    public void test_of_int_tooLow() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(0));
    }

    @Test
    public void test_of_int_tooHigh() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(367));
    }

    // Test DayOfYear.from(TemporalAccessor) method
    @Test
    public void test_from_TemporalAccessor_notLeapYear() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.from(date);
            assertEquals(day, testDay.getValue());
            date = date.plusDays(1);
        }
        DayOfYear testDay = DayOfYear.from(date);
        assertEquals(1, testDay.getValue());
    }

    @Test
    public void test_from_TemporalAccessor_leapYear() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.from(date);
            assertEquals(day, testDay.getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void test_from_TemporalAccessor_DayOfYear() {
        DayOfYear dayOfYear = DayOfYear.of(6);
        assertEquals(dayOfYear, DayOfYear.from(dayOfYear));
    }

    @Test
    public void test_from_TemporalAccessor_nonIso() {
        LocalDate date = LocalDate.now();
        assertEquals(date.getDayOfYear(), DayOfYear.from(JapaneseDate.from(date)).getValue());
    }

    @Test
    public void test_from_TemporalAccessor_noDerive() {
        assertThrows(DateTimeException.class, () -> DayOfYear.from(LocalTime.NOON));
    }

    @Test
    public void test_from_TemporalAccessor_null() {
        assertThrows(NullPointerException.class, () -> DayOfYear.from((TemporalAccessor) null));
    }

    @Test
    public void test_from_parse_CharSequence() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
        assertEquals(DayOfYear.of(76), formatter.parse("76", DayOfYear::from));
    }

    // Test isSupported(TemporalField) method
    @Test
    public void test_isSupported() {
        assertFalse(TEST_DAY.isSupported((TemporalField) null));
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
        assertFalse(TEST_DAY.isSupported(DAY_OF_MONTH));
        assertTrue(TEST_DAY.isSupported(DAY_OF_YEAR));
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
        assertTrue(TEST_DAY.isSupported(TestingField.INSTANCE));
    }

    // Test range(TemporalField) method
    @Test
    public void test_range() {
        assertEquals(DAY_OF_YEAR.range(), TEST_DAY.range(DAY_OF_YEAR));
    }

    @Test
    public void test_range_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.range(MONTH_OF_YEAR));
    }

    @Test
    public void test_range_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.range((TemporalField) null));
    }

    // Test get(TemporalField) method
    @Test
    public void test_get() {
        assertEquals(12, TEST_DAY.get(DAY_OF_YEAR));
    }

    @Test
    public void test_get_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY.get(MONTH_OF_YEAR));
    }

    @Test
    public void test_get_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.get((TemporalField) null));
    }

    // Test getLong(TemporalField) method
    @Test
    public void test_getLong() {
        assertEquals(12L, TEST_DAY.getLong(DAY_OF_YEAR));
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
        assertThrows(NullPointerException.class, () -> TEST_DAY.getLong((TemporalField) null));
    }

    // Test isValidYear(int) method
    @Test
    public void test_isValidYear_366() {
        DayOfYear day366 = DayOfYear.of(366);
        assertFalse(day366.isValidYear(2011));
        assertTrue(day366.isValidYear(2012));
        assertFalse(day366.isValidYear(2013));
    }

    @Test
    public void test_isValidYear_365() {
        DayOfYear day365 = DayOfYear.of(365);
        assertTrue(day365.isValidYear(2011));
        assertTrue(day365.isValidYear(2012));
        assertTrue(day365.isValidYear(2013));
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
    public void test_adjustInto_fromStartOfYear_notLeapYear() {
        LocalDate baseDate = LocalDate.of(2007, 1, 1);
        LocalDate expectedDate = baseDate;
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.adjustInto(baseDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_adjustInto_fromEndOfYear_notLeapYear() {
        LocalDate baseDate = LocalDate.of(2007, 12, 31);
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.adjustInto(baseDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_adjustInto_fromStartOfYear_notLeapYear_day366() {
        LocalDate baseDate = LocalDate.of(2007, 1, 1);
        DayOfYear testDay = DayOfYear.of(LEAP_YEAR_LENGTH);
        assertThrows(DateTimeException.class, () -> testDay.adjustInto(baseDate));
    }

    @Test
    public void test_adjustInto_fromEndOfYear_notLeapYear_day366() {
        LocalDate baseDate = LocalDate.of(2007, 12, 31);
        DayOfYear testDay = DayOfYear.of(LEAP_YEAR_LENGTH);
        assertThrows(DateTimeException.class, () -> testDay.adjustInto(baseDate));
    }

    @Test
    public void test_adjustInto_fromStartOfYear_leapYear() {
        LocalDate baseDate = LocalDate.of(2008, 1, 1);
        LocalDate expectedDate = baseDate;
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.adjustInto(baseDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_adjustInto_fromEndOfYear_leapYear() {
        LocalDate baseDate = LocalDate.of(2008, 12, 31);
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.adjustInto(baseDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_adjustInto_nonIso() {
        assertThrows(DateTimeException.class, () -> TEST_DAY.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void test_adjustInto_null() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.adjustInto((Temporal) null));
    }

    // Test atYear(Year) method
    @Test
    public void test_atYear_Year_notLeapYear() {
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.atYear(YEAR_STANDARD));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_atYear_fromStartOfYear_notLeapYear_day366() {
        DayOfYear testDay = DayOfYear.of(LEAP_YEAR_LENGTH);
        assertThrows(DateTimeException.class, () -> testDay.atYear(YEAR_STANDARD));
    }

    @Test
    public void test_atYear_Year_leapYear() {
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.atYear(YEAR_LEAP));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_atYear_Year_nullYear() {
        assertThrows(NullPointerException.class, () -> TEST_DAY.atYear((Year) null));
    }

    // Test atYear(int) method
    @Test
    public void test_atYear_int_notLeapYear() {
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.atYear(2007));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_atYear_int_fromStartOfYear_notLeapYear_day366() {
        DayOfYear testDay = DayOfYear.of(LEAP_YEAR_LENGTH);
        assertThrows(DateTimeException.class, () -> testDay.atYear(2007));
    }

    @Test
    public void test_atYear_int_leapYear() {
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear testDay = DayOfYear.of(day);
            assertEquals(expectedDate, testDay.atYear(2008));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void test_atYear_int_invalidDay() {
        assertThrows(DateTimeException.class, () -> TEST_DAY.atYear(Year.MIN_VALUE - 1));
    }

    // Test compareTo() method
    @Test
    public void test_compareTo() {
        for (int day1 = 1; day1 <= LEAP_YEAR_LENGTH; day1++) {
            DayOfYear dayOfYear1 = DayOfYear.of(day1);
            for (int day2 = 1; day2 <= LEAP_YEAR_LENGTH; day2++) {
                DayOfYear dayOfYear2 = DayOfYear.of(day2);
                if (day1 < day2) {
                    assertTrue(dayOfYear1.compareTo(dayOfYear2) < 0);
                    assertTrue(dayOfYear2.compareTo(dayOfYear1) > 0);
                } else if (day1 > day2) {
                    assertTrue(dayOfYear1.compareTo(dayOfYear2) > 0);
                    assertTrue(dayOfYear2.compareTo(dayOfYear1) < 0);
                } else {
                    assertEquals(0, dayOfYear1.compareTo(dayOfYear2));
                    assertEquals(0, dayOfYear2.compareTo(dayOfYear1));
                }
            }
        }
    }

    @Test
    public void test_compareTo_nullDayOfYear() {
        DayOfYear nullDayOfYear = null;
        DayOfYear testDay = DayOfYear.of(1);
        assertThrows(NullPointerException.class, () -> testDay.compareTo(nullDayOfYear));
    }

    // Test equals() and hashCode() methods
    @Test
    public void test_equals_and_hashCode() {
        EqualsTester equalsTester = new EqualsTester();
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            equalsTester.addEqualityGroup(DayOfYear.of(day), DayOfYear.of(day));
        }
        equalsTester.testEquals();
    }

    // Test toString() method
    @Test
    public void test_toString() {
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals("DayOfYear:" + day, dayOfYear.toString());
        }
    }

    // Test DayOfYear.now(Clock) method
    @Test
    public void test_now_clock_notLeapYear() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int day = 1; day <= STANDARD_YEAR_LENGTH; day++) {
            Instant instant = date.atStartOfDay(PARIS_ZONE).toInstant();
            Clock clock = Clock.fixed(instant, PARIS_ZONE);
            DayOfYear testDay = DayOfYear.now(clock);
            assertEquals(day, testDay.getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void test_now_clock_leapYear() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            Instant instant = date.atStartOfDay(PARIS_ZONE).toInstant();
            Clock clock = Clock.fixed(instant, PARIS_ZONE);
            DayOfYear testDay = DayOfYear.now(clock);
            assertEquals(day, testDay.getValue());
            date = date.plusDays(1);
        }
    }
}