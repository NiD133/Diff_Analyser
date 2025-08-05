package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.junit.Test;

/**
 * A clear, understandable test suite for the {@link DayOfMonth} class.
 */
public class DayOfMonthTest {

    private static final DayOfMonth TEST_DOM_15 = DayOfMonth.of(15);
    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2023-06-15T10:00:00Z"), ZoneOffset.UTC);

    //-----------------------------------------------------------------------
    // Factory methods: of(), now(), from()
    //-----------------------------------------------------------------------

    @Test
    public void of_shouldCreateDayOfMonthForValidDay() {
        DayOfMonth dayOfMonth = DayOfMonth.of(21);
        assertEquals(21, dayOfMonth.getValue());
    }

    @Test
    public void of_shouldCacheInstances() {
        for (int i = 1; i <= 31; i++) {
            DayOfMonth dom1 = DayOfMonth.of(i);
            DayOfMonth dom2 = DayOfMonth.of(i);
            assertSame("Instances should be cached and identical", dom1, dom2);
        }
    }

    @Test(expected = DateTimeException.class)
    public void of_shouldThrowException_whenDayIsTooLow() {
        DayOfMonth.of(0);
    }

    @Test(expected = DateTimeException.class)
    public void of_shouldThrowException_whenDayIsTooHigh() {
        DayOfMonth.of(32);
    }

    @Test
    public void now_withClock_shouldReturnCurrentDayOfMonthFromClock() {
        DayOfMonth now = DayOfMonth.now(FIXED_CLOCK);
        assertEquals(15, now.getValue());
    }



    @Test(expected = NullPointerException.class)
    public void now_shouldThrowException_whenClockIsNull() {
        DayOfMonth.now((Clock) null);
    }

    @Test(expected = NullPointerException.class)
    public void now_shouldThrowException_whenZoneIdIsNull() {
        DayOfMonth.now((ZoneId) null);
    }

    @Test
    public void from_shouldCreateDayOfMonthFromTemporalAccessor() {
        LocalDate date = LocalDate.of(2023, Month.AUGUST, 29);
        DayOfMonth fromDate = DayOfMonth.from(date);
        assertEquals(DayOfMonth.of(29), fromDate);
    }

    @Test(expected = DateTimeException.class)
    public void from_shouldThrowException_whenTemporalAccessorLacksDayOfMonth() {
        // YearMonth does not contain a day-of-month field
        YearMonth yearMonth = YearMonth.of(2023, Month.AUGUST);
        DayOfMonth.from(yearMonth);
    }

    @Test(expected = NullPointerException.class)
    public void from_shouldThrowException_whenTemporalAccessorIsNull() {
        DayOfMonth.from(null);
    }

    //-----------------------------------------------------------------------
    // Core functionality: getValue(), isSupported(), range(), get(), getLong()
    //-----------------------------------------------------------------------

    @Test
    public void getValue_shouldReturnDayOfMonth() {
        assertEquals(15, TEST_DOM_15.getValue());
    }

    @Test
    public void isSupported_shouldReturnTrueForDayOfMonthField() {
        assertTrue(TEST_DOM_15.isSupported(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void isSupported_shouldReturnFalseForOtherFields() {
        assertFalse(TEST_DOM_15.isSupported(ChronoField.MONTH_OF_YEAR));
        assertFalse(TEST_DOM_15.isSupported(ChronoField.YEAR));
        assertFalse(TEST_DOM_15.isSupported(null));
    }

    @Test
    public void range_shouldReturnValidRangeForDayOfMonth() {
        ValueRange expectedRange = ValueRange.of(1, 31);
        assertEquals(expectedRange, TEST_DOM_15.range(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_shouldThrowException_whenFieldIsUnsupported() {
        TEST_DOM_15.range(ChronoField.MONTH_OF_YEAR);
    }

    @Test
    public void get_shouldReturnValueForDayOfMonthField() {
        assertEquals(15, TEST_DOM_15.get(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void get_shouldThrowException_whenFieldIsUnsupported() {
        TEST_DOM_15.get(ChronoField.DAY_OF_WEEK);
    }

    @Test
    public void getLong_shouldReturnValueForDayOfMonthField() {
        assertEquals(15L, TEST_DOM_15.getLong(ChronoField.DAY_OF_MONTH));
    }

    //-----------------------------------------------------------------------
    // Query and Adjuster methods: query(), adjustInto()
    //-----------------------------------------------------------------------

    @Test
    public void query_shouldReturnExpectedValueForSupportedQueries() {
        assertEquals(IsoChronology.INSTANCE, TEST_DOM_15.query(TemporalQueries.chronology()));
        assertEquals(null, TEST_DOM_15.query(TemporalQueries.zoneId()));
        assertEquals(null, TEST_DOM_15.query(TemporalQueries.localDate()));
    }

    @Test
    public void adjustInto_shouldAdjustDateToDayOfMonth() {
        LocalDate originalDate = LocalDate.of(2023, Month.SEPTEMBER, 1);
        LocalDate adjustedDate = (LocalDate) TEST_DOM_15.adjustInto(originalDate);
        LocalDate expectedDate = LocalDate.of(2023, Month.SEPTEMBER, 15);
        assertEquals(expectedDate, adjustedDate);
    }

    @Test(expected = DateTimeException.class)
    public void adjustInto_shouldThrowException_whenResultingDateIsInvalid() {
        DayOfMonth day31 = DayOfMonth.of(31);
        LocalDate dateInFebruary = LocalDate.of(2023, Month.FEBRUARY, 1);
        day31.adjustInto(dateInFebruary); // February 31st is invalid
    }
    
    @Test(expected = UnsupportedTemporalTypeException.class)
    public void adjustInto_shouldThrowException_whenTypeIsUnsupported() {
        // YearMonth does not support being adjusted by a DayOfMonth
        YearMonth yearMonth = YearMonth.of(2023, Month.APRIL);
        TEST_DOM_15.adjustInto(yearMonth);
    }

    //-----------------------------------------------------------------------
    // Other methods: isValidYearMonth(), atMonth(), atYearMonth()
    //-----------------------------------------------------------------------

    @Test
    public void isValidYearMonth_shouldValidateCorrectly() {
        DayOfMonth day29 = DayOfMonth.of(29);
        assertTrue(day29.isValidYearMonth(YearMonth.of(2024, Month.FEBRUARY))); // Leap year
        assertFalse(day29.isValidYearMonth(YearMonth.of(2023, Month.FEBRUARY))); // Non-leap year

        DayOfMonth day31 = DayOfMonth.of(31);
        assertTrue(day31.isValidYearMonth(YearMonth.of(2023, Month.JANUARY)));
        assertFalse(day31.isValidYearMonth(YearMonth.of(2023, Month.APRIL)));

        assertFalse(day29.isValidYearMonth(null));
    }

    @Test
    public void atMonth_shouldCombineWithMonthToCreateMonthDay() {
        MonthDay expected = MonthDay.of(Month.JUNE, 15);
        assertEquals(expected, TEST_DOM_15.atMonth(Month.JUNE));
        assertEquals(expected, TEST_DOM_15.atMonth(6));
    }

    @Test
    public void atYearMonth_shouldCombineWithYearMonthToCreateLocalDate() {
        YearMonth ym = YearMonth.of(2023, Month.OCTOBER);
        LocalDate expected = LocalDate.of(2023, Month.OCTOBER, 15);
        assertEquals(expected, TEST_DOM_15.atYearMonth(ym));
    }

    @Test(expected = DateTimeException.class)
    public void atYearMonth_shouldThrowException_whenDateIsInvalid() {
        DayOfMonth day31 = DayOfMonth.of(31);
        YearMonth ym = YearMonth.of(2023, Month.FEBRUARY);
        day31.atYearMonth(ym); // February 31st is invalid
    }

    //-----------------------------------------------------------------------
    // Object contract: compareTo(), equals(), hashCode(), toString()
    //-----------------------------------------------------------------------

    @Test
    public void compareTo_shouldCompareBasedOnValue() {
        DayOfMonth day14 = DayOfMonth.of(14);
        DayOfMonth day15 = DayOfMonth.of(15);
        DayOfMonth day16 = DayOfMonth.of(16);

        assertTrue(day15.compareTo(day16) < 0);
        assertTrue(day15.compareTo(day14) > 0);
        assertEquals(0, day15.compareTo(day15));
    }

    @Test
    public void equals_shouldBehaveCorrectly() {
        DayOfMonth day15a = DayOfMonth.of(15);
        DayOfMonth day15b = DayOfMonth.of(15);
        DayOfMonth day16 = DayOfMonth.of(16);

        assertTrue(day15a.equals(day15a)); // self
        assertTrue(day15a.equals(day15b)); // same value
        assertFalse(day15a.equals(day16)); // different value
        assertFalse(day15a.equals(null)); // null
        assertFalse(day15a.equals("15")); // different type
    }

    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        DayOfMonth day15a = DayOfMonth.of(15);
        DayOfMonth day15b = DayOfMonth.of(15);
        assertEquals(day15a.hashCode(), day15b.hashCode());
    }

    @Test
    public void toString_shouldReturnCorrectFormat() {
        assertEquals("DayOfMonth:15", TEST_DOM_15.toString());
        assertEquals("DayOfMonth:1", DayOfMonth.of(1).toString());
    }
}