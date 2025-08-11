package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.MinguoDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockYearMonth;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockMinguoDate;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DayOfMonth_ESTest extends DayOfMonth_ESTest_scaffolding {

    private static final int CURRENT_DAY = 14;

    @Test(timeout = 4000)
    public void testEqualityWithDifferentDays() {
        DayOfMonth day31 = DayOfMonth.of(31);
        DayOfMonth today = DayOfMonth.now();
        assertFalse(today.equals(day31));
    }

    @Test(timeout = 4000)
    public void testToString() {
        DayOfMonth today = DayOfMonth.now();
        assertEquals("DayOfMonth:" + CURRENT_DAY, today.toString());
    }

    @Test(timeout = 4000)
    public void testRangeForDayOfMonth() {
        DayOfMonth today = DayOfMonth.now();
        ChronoField dayField = ChronoField.DAY_OF_MONTH;
        today.range(dayField);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testQueryWithMock() {
        DayOfMonth today = DayOfMonth.now();
        TemporalQuery<Object> mockQuery = mock(TemporalQuery.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockQuery).queryFrom(any(TemporalAccessor.class));
        today.query(mockQuery);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testNowWithSystemDefaultZone() {
        Clock defaultClock = MockClock.systemDefaultZone();
        DayOfMonth today = DayOfMonth.now(defaultClock);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testComparisonWithEarlierDay() {
        DayOfMonth today = DayOfMonth.now();
        DayOfMonth firstDay = DayOfMonth.of(1);
        int comparison = today.compareTo(firstDay);
        assertEquals(13, comparison);
    }

    @Test(timeout = 4000)
    public void testComparisonWithLaterDay() {
        DayOfMonth today = DayOfMonth.now();
        DayOfMonth lastDay = DayOfMonth.of(28);
        int comparison = today.compareTo(lastDay);
        assertEquals(-14, comparison);
    }

    @Test(timeout = 4000)
    public void testAtYearMonth() {
        DayOfMonth today = DayOfMonth.now();
        YearMonth currentYearMonth = MockYearMonth.now();
        today.atYearMonth(currentYearMonth);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testAdjustIntoZonedDateTime() {
        DayOfMonth today = DayOfMonth.now();
        Clock utcClock = MockClock.systemUTC();
        ZonedDateTime zonedDateTime = MockZonedDateTime.now(utcClock);
        Temporal adjusted = today.adjustInto(zonedDateTime);
        assertEquals(zonedDateTime, adjusted);
    }

    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testUnsupportedFieldRange() {
        DayOfMonth today = DayOfMonth.now();
        today.range(ChronoField.ERA);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testQueryWithNull() {
        DayOfMonth today = DayOfMonth.now();
        today.query(null);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testInvalidDayOfMonth() {
        DayOfMonth.of(-510);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testNowWithOffsetClock() {
        Clock utcClock = MockClock.systemUTC();
        Duration eraDuration = ChronoUnit.ERAS.getDuration();
        Clock offsetClock = MockClock.offset(utcClock, eraDuration);
        DayOfMonth.now(offsetClock);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNowWithNullClock() {
        DayOfMonth.now((Clock) null);
    }

    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testUnsupportedFieldGet() {
        DayOfMonth today = DayOfMonth.now();
        today.get(ChronoField.CLOCK_HOUR_OF_DAY);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testGetWithNullField() {
        DayOfMonth today = DayOfMonth.now();
        today.get(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testFromWithNullTemporal() {
        DayOfMonth.from(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCompareToNull() {
        DayOfMonth today = DayOfMonth.now();
        today.compareTo(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAdjustIntoNullTemporal() {
        DayOfMonth today = DayOfMonth.now();
        today.adjustInto(null);
    }

    @Test(timeout = 4000)
    public void testGetLongForDayOfMonth() {
        DayOfMonth today = DayOfMonth.now();
        long dayValue = today.getLong(ChronoField.DAY_OF_MONTH);
        assertEquals(CURRENT_DAY, dayValue);
    }

    @Test(timeout = 4000)
    public void testIsSupportedForUnsupportedField() {
        DayOfMonth today = DayOfMonth.now();
        boolean supported = today.isSupported(ChronoField.AMPM_OF_DAY);
        assertFalse(supported);
    }

    @Test(timeout = 4000)
    public void testIsSupportedForSupportedField() {
        DayOfMonth today = DayOfMonth.now();
        boolean supported = today.isSupported(ChronoField.DAY_OF_MONTH);
        assertTrue(supported);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testRangeWithNullField() {
        DayOfMonth today = DayOfMonth.now();
        today.range(null);
    }

    @Test(timeout = 4000)
    public void testEqualityWithSameDay() {
        Clock defaultClock = MockClock.systemDefaultZone();
        DayOfMonth today = DayOfMonth.now(defaultClock);
        assertTrue(today.equals(today));
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentType() {
        DayOfMonth today = DayOfMonth.now();
        ZoneOffset maxOffset = ZoneOffset.MAX;
        assertFalse(today.equals(maxOffset));
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testAdjustIntoNonIsoChronology() {
        DayOfMonth today = DayOfMonth.now();
        HijrahDate hijrahDate = MockHijrahDate.now();
        today.adjustInto(hijrahDate);
    }

    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testAdjustIntoYearMonth() {
        DayOfMonth today = DayOfMonth.now();
        YearMonth currentYearMonth = MockYearMonth.now();
        today.adjustInto(currentYearMonth);
    }

    @Test(timeout = 4000)
    public void testChronologyFromDayOfMonth() {
        DayOfMonth today = DayOfMonth.now();
        Chronology.from(today);
    }

    @Test(timeout = 4000)
    public void testIsValidYearMonthWithInvalidDay() {
        ZoneOffset utcOffset = ZoneOffset.UTC;
        YearMonth currentYearMonth = MockYearMonth.now(utcOffset);
        DayOfMonth day31 = DayOfMonth.of(31);
        assertFalse(day31.isValidYearMonth(currentYearMonth));
    }

    @Test(timeout = 4000)
    public void testIsValidYearMonthWithValidDay() {
        DayOfMonth today = DayOfMonth.now();
        ZoneOffset utcOffset = ZoneOffset.UTC;
        YearMonth currentYearMonth = MockYearMonth.now(utcOffset);
        assertTrue(today.isValidYearMonth(currentYearMonth));
    }

    @Test(timeout = 4000)
    public void testIsValidYearMonthWithNull() {
        DayOfMonth today = DayOfMonth.now();
        assertFalse(today.isValidYearMonth(null));
    }

    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testGetLongForUnsupportedField() {
        DayOfMonth today = DayOfMonth.now();
        today.getLong(ChronoField.INSTANT_SECONDS);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testGetLongWithNullField() {
        DayOfMonth today = DayOfMonth.now();
        today.getLong(null);
    }

    @Test(timeout = 4000)
    public void testIsSupportedWithNullField() {
        DayOfMonth today = DayOfMonth.now();
        assertFalse(today.isSupported(null));
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testFromWithZoneOffset() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfMonth.from(maxOffset);
    }

    @Test(timeout = 4000)
    public void testFromWithDayOfMonth() {
        Clock defaultClock = MockClock.systemDefaultZone();
        DayOfMonth today = DayOfMonth.now(defaultClock);
        DayOfMonth fromToday = DayOfMonth.from(today);
        assertEquals(CURRENT_DAY, fromToday.getValue());
    }

    @Test(timeout = 4000)
    public void testFromWithMinguoDate() {
        MinguoDate minguoDate = MockMinguoDate.now();
        DayOfMonth fromMinguoDate = DayOfMonth.from(minguoDate);
        assertEquals(CURRENT_DAY, fromMinguoDate.getValue());
    }

    @Test(timeout = 4000)
    public void testAtMonthWithMonthEnum() {
        DayOfMonth today = DayOfMonth.now();
        Month august = Month.AUGUST;
        today.atMonth(august);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNowWithNullZoneId() {
        DayOfMonth.now((ZoneId) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAtYearMonthWithNull() {
        DayOfMonth today = DayOfMonth.now();
        today.atYearMonth(null);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testAtMonthWithInvalidMonth() {
        DayOfMonth day31 = DayOfMonth.of(31);
        day31.atMonth(-550);
    }

    @Test(timeout = 4000)
    public void testGetValue() {
        Clock defaultClock = MockClock.systemDefaultZone();
        DayOfMonth today = DayOfMonth.now(defaultClock);
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testHashCode() {
        DayOfMonth today = DayOfMonth.now();
        today.hashCode();
        assertEquals(CURRENT_DAY, today.getValue());
    }

    @Test(timeout = 4000)
    public void testCompareToSelf() {
        DayOfMonth today = DayOfMonth.now();
        assertEquals(0, today.compareTo(today));
    }

    @Test(timeout = 4000)
    public void testGetForDayOfMonthField() {
        DayOfMonth today = DayOfMonth.now();
        int dayValue = today.get(ChronoField.DAY_OF_MONTH);
        assertEquals(CURRENT_DAY, dayValue);
    }
}