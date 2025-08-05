package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockYearMonth;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockMinguoDate;
import org.junit.runner.RunWith;
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
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DayOfMonth_ESTest extends DayOfMonth_ESTest_scaffolding {

    // Test DayOfMonth.of() method
    @Test
    public void of_validDayOfMonth_createsInstance() {
        DayOfMonth day = DayOfMonth.of(31);
        assertEquals(31, day.getValue());
    }

    @Test(expected = DateTimeException.class)
    public void of_invalidDayOfMonth_throwsException() {
        DayOfMonth.of(-510);
    }

    // Test DayOfMonth.now() methods
    @Test
    public void now_defaultZone_returnsCurrentDay() {
        DayOfMonth currentDay = DayOfMonth.now();
        assertEquals(14, currentDay.getValue());
    }

    @Test
    public void now_withZoneOffset_returnsCurrentDay() {
        DayOfMonth currentDay = DayOfMonth.now(ZoneOffset.UTC);
        assertEquals(14, currentDay.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void now_withNullClock_throwsException() {
        DayOfMonth.now((Clock) null);
    }

    @Test(expected = NullPointerException.class)
    public void now_withNullZone_throwsException() {
        DayOfMonth.now((ZoneId) null);
    }

    @Test(expected = DateTimeException.class)
    public void now_withInvalidClock_throwsException() {
        Clock clock = MockClock.systemUTC();
        Clock offsetClock = MockClock.offset(clock, ChronoUnit.ERAS.getDuration());
        DayOfMonth.now(offsetClock);
    }

    // Test DayOfMonth.from() method
    @Test
    public void from_DayOfMonthInstance_returnsSameValue() {
        DayOfMonth day = DayOfMonth.now();
        DayOfMonth result = DayOfMonth.from(day);
        assertEquals(14, result.getValue());
    }

    @Test
    public void from_otherTemporalAccessor_returnsDayOfMonth() {
        MinguoDate date = MockMinguoDate.now();
        DayOfMonth day = DayOfMonth.from(date);
        assertEquals(14, day.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void from_nullTemporalAccessor_throwsException() {
        DayOfMonth.from((TemporalAccessor) null);
    }

    @Test(expected = DateTimeException.class)
    public void from_invalidTemporalAccessor_throwsException() {
        DayOfMonth.from(ZoneOffset.MAX);
    }

    // Test getValue() method
    @Test
    public void getValue_returnsDayValue() {
        DayOfMonth day = DayOfMonth.of(31);
        assertEquals(31, day.getValue());
    }

    // Test isSupported() method
    @Test
    public void isSupported_dayOfMonthField_returnsTrue() {
        DayOfMonth day = DayOfMonth.now();
        assertTrue(day.isSupported(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void isSupported_unsupportedField_returnsFalse() {
        DayOfMonth day = DayOfMonth.now();
        assertFalse(day.isSupported(ChronoField.AMPM_OF_DAY));
        assertFalse(day.isSupported(null));
    }

    // Test range() method
    @Test
    public void range_dayOfMonthField_returnsValidRange() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals(ValueRange.of(1, 31), day.range(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_unsupportedField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.range(ChronoField.ERA);
    }

    @Test(expected = NullPointerException.class)
    public void range_nullField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.range(null);
    }

    // Test get() method
    @Test
    public void get_dayOfMonthField_returnsValue() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals(14, day.get(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void get_unsupportedField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.get(ChronoField.CLOCK_HOUR_OF_DAY);
    }

    @Test(expected = NullPointerException.class)
    public void get_nullField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.get(null);
    }

    // Test getLong() method
    @Test
    public void getLong_dayOfMonthField_returnsValue() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals(14L, day.getLong(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void getLong_unsupportedField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.getLong(ChronoField.INSTANT_SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void getLong_nullField_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.getLong(null);
    }

    // Test query() method
    @Test
    public void query_validTemporalQuery_returnsResult() {
        DayOfMonth day = DayOfMonth.now();
        TemporalQuery<Object> query = mock(TemporalQuery.class);
        when(query.queryFrom(any(TemporalAccessor.class)).thenReturn(null);
        assertNull(day.query(query));
    }

    @Test(expected = NullPointerException.class)
    public void query_nullTemporalQuery_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.query(null);
    }

    // Test adjustInto() method
    @Test
    public void adjustInto_zonedDateTime_adjustsDay() {
        DayOfMonth day = DayOfMonth.of(15);
        ZonedDateTime zdt = MockZonedDateTime.now(MockClock.systemUTC());
        Temporal result = day.adjustInto(zdt);
        assertEquals(15, result.get(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = DateTimeException.class)
    public void adjustInto_nonIsoChronology_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        HijrahDate hijrahDate = MockHijrahDate.now();
        day.adjustInto(hijrahDate);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void adjustInto_unsupportedTemporal_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        YearMonth yearMonth = MockYearMonth.now();
        day.adjustInto(yearMonth);
    }

    @Test(expected = NullPointerException.class)
    public void adjustInto_nullTemporal_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.adjustInto(null);
    }

    // Test atMonth() methods
    @Test
    public void atMonth_withMonthEnum_createsMonthDay() {
        DayOfMonth day = DayOfMonth.of(15);
        MonthDay monthDay = day.atMonth(Month.JANUARY);
        assertEquals(15, monthDay.getDayOfMonth());
    }

    @Test
    public void atMonth_withMonthInt_createsMonthDay() {
        DayOfMonth day = DayOfMonth.of(15);
        MonthDay monthDay = day.atMonth(1);
        assertEquals(15, monthDay.getDayOfMonth());
    }

    @Test(expected = DateTimeException.class)
    public void atMonth_invalidMonth_throwsException() {
        DayOfMonth day = DayOfMonth.of(31);
        day.atMonth(-550);
    }

    // Test atYearMonth() method
    @Test
    public void atYearMonth_validYearMonth_createsDate() {
        DayOfMonth day = DayOfMonth.of(15);
        YearMonth yearMonth = MockYearMonth.now();
        assertEquals(15, day.atYearMonth(yearMonth).getDayOfMonth());
    }

    @Test(expected = NullPointerException.class)
    public void atYearMonth_nullYearMonth_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.atYearMonth(null);
    }

    // Test isValidYearMonth() method
    @Test
    public void isValidYearMonth_validDate_returnsTrue() {
        DayOfMonth day = DayOfMonth.of(14);
        YearMonth yearMonth = MockYearMonth.now(ZoneOffset.UTC);
        assertTrue(day.isValidYearMonth(yearMonth));
    }

    @Test
    public void isValidYearMonth_invalidDate_returnsFalse() {
        DayOfMonth day = DayOfMonth.of(31);
        YearMonth yearMonth = MockYearMonth.now(ZoneOffset.UTC);
        assertFalse(day.isValidYearMonth(yearMonth));
    }

    @Test
    public void isValidYearMonth_nullYearMonth_returnsFalse() {
        DayOfMonth day = DayOfMonth.now();
        assertFalse(day.isValidYearMonth(null));
    }

    // Test compareTo() method
    @Test
    public void compareTo_sameDay_returnsZero() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals(0, day.compareTo(day));
    }

    @Test
    public void compareTo_laterDay_returnsNegative() {
        DayOfMonth day1 = DayOfMonth.of(14);
        DayOfMonth day2 = DayOfMonth.of(28);
        assertTrue(day1.compareTo(day2) < 0);
    }

    @Test
    public void compareTo_earlierDay_returnsPositive() {
        DayOfMonth day1 = DayOfMonth.of(14);
        DayOfMonth day2 = DayOfMonth.of(1);
        assertTrue(day1.compareTo(day2) > 0);
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_nullDay_throwsException() {
        DayOfMonth day = DayOfMonth.now();
        day.compareTo(null);
    }

    // Test equals() and hashCode()
    @Test
    public void equals_sameValue_returnsTrue() {
        DayOfMonth day1 = DayOfMonth.now();
        DayOfMonth day2 = DayOfMonth.now();
        assertEquals(day1, day2);
        assertEquals(day1.hashCode(), day2.hashCode());
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        DayOfMonth day1 = DayOfMonth.of(14);
        DayOfMonth day2 = DayOfMonth.of(31);
        assertNotEquals(day1, day2);
        assertNotEquals(day1.hashCode(), day2.hashCode());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals(day, day);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        DayOfMonth day = DayOfMonth.now();
        assertNotEquals(day, ZoneOffset.MAX);
    }

    // Test toString() method
    @Test
    public void toString_returnsFormattedString() {
        DayOfMonth day = DayOfMonth.now();
        assertEquals("DayOfMonth:14", day.toString());
    }

    // Test additional functionality
    @Test
    public void chronology_fromDayOfMonth_returnsIsoChronology() {
        DayOfMonth day = DayOfMonth.now();
        Chronology chronology = Chronology.from(day);
        assertEquals(IsoChronology.INSTANCE, chronology);
    }
}