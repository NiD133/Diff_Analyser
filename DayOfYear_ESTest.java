package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.HijrahDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.evosuite.runtime.mock.java.time.MockYearMonth;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockThaiBuddhistDate;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DayOfYear_ESTest extends DayOfYear_ESTest_scaffolding {

    private static final int EXPECTED_DAY_OF_YEAR = 45;
    private static final int LEAP_YEAR_DAY = 366;
    private static final int NON_LEAP_YEAR_DAY = 1;

    @Test(timeout = 4000)
    public void testDayOfYearEqualityWithDifferentZones() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYearWithMaxOffset = DayOfYear.now(maxOffset);
        DayOfYear dayOfYearDefaultZone = DayOfYear.now();

        assertFalse(dayOfYearDefaultZone.equals(dayOfYearWithMaxOffset));
        assertFalse(dayOfYearWithMaxOffset.equals(dayOfYearDefaultZone));
    }

    @Test(timeout = 4000)
    public void testDayOfYearRangeForDayOfYearField() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField dayOfYearField = ChronoField.DAY_OF_YEAR;

        dayOfYear.range(dayOfYearField);
        assertEquals(EXPECTED_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearQueryWithMockTemporalQuery() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);
        TemporalQuery<ChronoField> mockQuery = mock(TemporalQuery.class, new ViolatedAssumptionAnswer());

        doReturn(null).when(mockQuery).queryFrom(any(TemporalAccessor.class));
        dayOfYear.query(mockQuery);
        assertEquals(46, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearComparisonWithDifferentZones() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYearWithMaxOffset = DayOfYear.now(maxOffset);
        DayOfYear dayOfYearDefaultZone = DayOfYear.now();

        int comparisonResult = dayOfYearWithMaxOffset.compareTo(dayOfYearDefaultZone);
        assertEquals(1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testDayOfYearComparisonWithMockClock() {
        DayOfYear dayOfYear = DayOfYear.of(NON_LEAP_YEAR_DAY);
        Clock mockClock = MockClock.systemDefaultZone();
        DayOfYear dayOfYearWithMockClock = DayOfYear.now(mockClock);

        int comparisonResult = dayOfYear.compareTo(dayOfYearWithMockClock);
        assertEquals(-44, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testDayOfYearQueryWithChronoLocalDate() {
        DayOfYear dayOfYear = DayOfYear.of(NON_LEAP_YEAR_DAY);
        Year mockYear = MockYear.now();
        LocalDate localDate = dayOfYear.atYear(mockYear);
        TemporalQuery<ChronoLocalDate> mockQuery = mock(TemporalQuery.class, new ViolatedAssumptionAnswer());

        doReturn(localDate).when(mockQuery).queryFrom(any(TemporalAccessor.class));
        ChronoLocalDate result = dayOfYear.query(mockQuery);
        assertSame(localDate, result);
    }

    @Test(timeout = 4000)
    public void testDayOfYearAdjustmentIntoTemporal() {
        DayOfYear dayOfYear = DayOfYear.now();
        LocalDate localDate = dayOfYear.atYear(-2684);
        Temporal adjustedTemporal = dayOfYear.adjustInto(localDate);

        assertEquals(EXPECTED_DAY_OF_YEAR, dayOfYear.getValue());
        assertSame(adjustedTemporal, localDate);
    }

    @Test(timeout = 4000)
    public void testUnsupportedTemporalFieldRange() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField unsupportedField = ChronoField.SECOND_OF_MINUTE;

        try {
            dayOfYear.range(unsupportedField);
            fail("Expecting exception: UnsupportedTemporalTypeException");
        } catch (UnsupportedTemporalTypeException e) {
            verifyException("java.time.temporal.TemporalAccessor", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidDayOfYearCreation() {
        try {
            DayOfYear.of(0);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearNowWithNullZone() {
        try {
            DayOfYear.now((ZoneId) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearNowWithOffsetClock() {
        Clock defaultClock = MockClock.systemDefaultZone();
        ChronoUnit chronoUnit = ChronoUnit.ERAS;
        Duration duration = chronoUnit.getDuration();
        Clock offsetClock = MockClock.offset(defaultClock, duration);

        try {
            DayOfYear.now(offsetClock);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.Instant", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearNowWithOverflowClock() {
        ZoneOffset utcOffset = ZoneOffset.UTC;
        Clock systemClock = MockClock.system(utcOffset);
        ChronoUnit chronoUnit = ChronoUnit.FOREVER;
        Duration duration = chronoUnit.getDuration();
        Clock overflowClock = MockClock.offset(systemClock, duration);

        try {
            DayOfYear.now(overflowClock);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("java.lang.Math", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnsupportedTemporalFieldGet() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField unsupportedField = ChronoField.ERA;

        try {
            dayOfYear.get(unsupportedField);
            fail("Expecting exception: UnsupportedTemporalTypeException");
        } catch (UnsupportedTemporalTypeException e) {
            verifyException("java.time.temporal.TemporalAccessor", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearGetWithNullField() {
        DayOfYear dayOfYear = DayOfYear.now();

        try {
            dayOfYear.get((TemporalField) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearFromNullTemporalAccessor() {
        try {
            DayOfYear.from((TemporalAccessor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearCompareToNull() {
        ZoneId systemZone = ZoneId.systemDefault();
        DayOfYear dayOfYear = DayOfYear.now(systemZone);

        try {
            dayOfYear.compareTo((DayOfYear) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearAtYearWithNullYear() {
        DayOfYear dayOfYear = DayOfYear.now();

        try {
            dayOfYear.atYear((Year) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearAdjustmentIntoNonLeapYear() {
        DayOfYear dayOfYear = DayOfYear.of(LEAP_YEAR_DAY);
        ZonedDateTime mockZonedDateTime = MockZonedDateTime.now();

        try {
            dayOfYear.adjustInto(mockZonedDateTime);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.LocalDate", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearAdjustmentIntoNullTemporal() {
        DayOfYear dayOfYear = DayOfYear.now();

        try {
            dayOfYear.adjustInto((Temporal) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearGetLongForDayOfYearField() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField dayOfYearField = ChronoField.DAY_OF_YEAR;

        long result = dayOfYear.getLong(dayOfYearField);
        assertEquals(EXPECTED_DAY_OF_YEAR, result);
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsSupportedForDayOfYearField() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField dayOfYearField = ChronoField.DAY_OF_YEAR;

        assertTrue(dayOfYear.isSupported(dayOfYearField));
        assertEquals(EXPECTED_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsNotSupportedForClockHourOfAmPmField() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField unsupportedField = ChronoField.CLOCK_HOUR_OF_AMPM;

        assertFalse(dayOfYear.isSupported(unsupportedField));
        assertEquals(EXPECTED_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearNowWithNullClock() {
        try {
            DayOfYear.now((Clock) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearRangeWithNullField() {
        DayOfYear dayOfYear = DayOfYear.now();

        try {
            dayOfYear.range((TemporalField) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearToString() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);

        assertEquals("DayOfYear:46", dayOfYear.toString());
    }

    @Test(timeout = 4000)
    public void testDayOfYearEqualsZoneOffset() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);

        assertFalse(dayOfYear.equals(maxOffset));
        assertEquals(46, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearEqualsItself() {
        DayOfYear dayOfYear = DayOfYear.now();

        assertTrue(dayOfYear.equals(dayOfYear));
        assertEquals(EXPECTED_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearFromThaiBuddhistDate() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(8, 8);
        ThaiBuddhistDate thaiBuddhistDate = MockThaiBuddhistDate.now(offset);
        DayOfYear dayOfYearFromThaiBuddhistDate = DayOfYear.from(thaiBuddhistDate);
        DayOfYear dayOfYear = DayOfYear.now();

        assertFalse(dayOfYearFromThaiBuddhistDate.equals(dayOfYear));
        assertFalse(dayOfYear.equals(dayOfYearFromThaiBuddhistDate));
    }

    @Test(timeout = 4000)
    public void testDayOfYearAdjustmentIntoHijrahDate() {
        DayOfYear dayOfYear = DayOfYear.now();
        HijrahDate hijrahDate = MockHijrahDate.now();

        try {
            dayOfYear.adjustInto(hijrahDate);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearAdjustmentIntoYearMonth() {
        DayOfYear dayOfYear = DayOfYear.now();
        YearMonth yearMonth = MockYearMonth.now();

        try {
            dayOfYear.adjustInto(yearMonth);
            fail("Expecting exception: UnsupportedTemporalTypeException");
        } catch (UnsupportedTemporalTypeException e) {
            verifyException("java.time.YearMonth", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearQueryWithNullTemporalQuery() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(8, 8);
        ThaiBuddhistDate thaiBuddhistDate = MockThaiBuddhistDate.now(offset);
        DayOfYear dayOfYear = DayOfYear.from(thaiBuddhistDate);

        try {
            dayOfYear.query((TemporalQuery<ChronoField>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.time.temporal.TemporalAccessor", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsValidYearForNonLeapYear() {
        DayOfYear dayOfYear = DayOfYear.of(LEAP_YEAR_DAY);

        assertFalse(dayOfYear.isValidYear(LEAP_YEAR_DAY));
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsValidYearForLeapYear() {
        DayOfYear dayOfYear = DayOfYear.of(LEAP_YEAR_DAY);

        assertTrue(dayOfYear.isValidYear(364));
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsValidYearForThaiBuddhistDate() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(8, 8);
        ThaiBuddhistDate thaiBuddhistDate = MockThaiBuddhistDate.now(offset);
        DayOfYear dayOfYear = DayOfYear.from(thaiBuddhistDate);

        assertTrue(dayOfYear.isValidYear(8));
    }

    @Test(timeout = 4000)
    public void testUnsupportedTemporalFieldGetLong() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField unsupportedField = ChronoField.EPOCH_DAY;

        try {
            dayOfYear.getLong(unsupportedField);
            fail("Expecting exception: UnsupportedTemporalTypeException");
        } catch (UnsupportedTemporalTypeException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearGetLongWithNullField() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);

        try {
            dayOfYear.getLong((TemporalField) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearGetForDayOfYearField() {
        DayOfYear dayOfYear = DayOfYear.now();
        ChronoField dayOfYearField = ChronoField.DAY_OF_YEAR;

        int result = dayOfYear.get(dayOfYearField);
        assertEquals(EXPECTED_DAY_OF_YEAR, result);
    }

    @Test(timeout = 4000)
    public void testDayOfYearFromMonth() {
        Month month = Month.NOVEMBER;

        try {
            DayOfYear.from(month);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.DayOfYear", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearFromItself() {
        DayOfYear dayOfYear = DayOfYear.now();
        DayOfYear result = DayOfYear.from(dayOfYear);

        assertEquals(EXPECTED_DAY_OF_YEAR, result.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearIsSupportedWithNullField() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(8, 8);
        ThaiBuddhistDate thaiBuddhistDate = MockThaiBuddhistDate.now(offset);
        DayOfYear dayOfYear = DayOfYear.from(thaiBuddhistDate);

        assertFalse(dayOfYear.isSupported((TemporalField) null));
        assertEquals(46, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearGetValue() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);

        assertEquals(46, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearHashCode() {
        DayOfYear dayOfYear = DayOfYear.of(NON_LEAP_YEAR_DAY);
        dayOfYear.hashCode();

        assertEquals(NON_LEAP_YEAR_DAY, dayOfYear.getValue());
    }

    @Test(timeout = 4000)
    public void testDayOfYearCompareToItself() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayOfYear = DayOfYear.now(maxOffset);

        int comparisonResult = dayOfYear.compareTo(dayOfYear);
        assertEquals(46, dayOfYear.getValue());
        assertEquals(0, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testDayOfYearAtYearWithInvalidLeapYearDay() {
        DayOfYear dayOfYear = DayOfYear.of(LEAP_YEAR_DAY);

        try {
            dayOfYear.atYear(LEAP_YEAR_DAY);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.LocalDate", e);
        }
    }

    @Test(timeout = 4000)
    public void testDayOfYearAtYearWithMockYear() {
        DayOfYear dayOfYear = DayOfYear.of(LEAP_YEAR_DAY);
        Year mockYear = MockYear.of(LEAP_YEAR_DAY);

        try {
            dayOfYear.atYear(mockYear);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.LocalDate", e);
        }
    }

    @Test(timeout = 4000)
    public void testMockYearFromDayOfYear() {
        DayOfYear dayOfYear = DayOfYear.now();

        try {
            MockYear.from(dayOfYear);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.Year", e);
        }
    }
}