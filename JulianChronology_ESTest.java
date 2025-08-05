package org.threeten.extra.chrono;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.BritishCutoverDate;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;
import org.threeten.extra.chrono.JulianEra;
import org.threeten.extra.chrono.PaxDate;

import java.time.*;
import java.time.chrono.*;
import java.time.format.ResolverStyle;
import java.time.temporal.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
        mockJVMNonDeterminism = true,
        useVFS = true,
        useVNET = true,
        resetStaticState = true,
        separateClassLoader = true
)
public class JulianChronology_ESTest extends JulianChronology_ESTest_scaffolding {

    // Test: Create a JulianDate from a negative epoch day and verify its era is BC
    @Test(timeout = 4000)
    public void testCreateDateFromNegativeEpochDay() {
        JulianChronology julianChronology = new JulianChronology();
        JulianDate julianDate = julianChronology.dateEpochDay(-11999976L);
        assertEquals(JulianEra.BC, julianDate.getEra());
    }

    // Test: Attempt to create a JulianDate with an invalid day of year and expect a DateTimeException
    @Test(timeout = 4000)
    public void testInvalidDayOfYearThrowsException() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JulianEra julianEra = JulianEra.BC;
        try {
            julianChronology.dateYearDay(julianEra, 2493, 1461);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Create a ChronoZonedDateTime from an instant and zone offset
    @Test(timeout = 4000)
    public void testCreateChronoZonedDateTime() {
        JulianChronology julianChronology = new JulianChronology();
        Instant instant = MockInstant.ofEpochMilli(3L);
        ZoneOffset zoneOffset = ZoneOffset.MAX;
        ChronoZonedDateTime<JulianDate> chronoZonedDateTime = julianChronology.zonedDateTime(instant, zoneOffset);
        assertNotNull(chronoZonedDateTime);
    }

    // Test: Resolve a JulianDate from a map of temporal fields and verify it is not equal to another date
    @Test(timeout = 4000)
    public void testResolveDateFromTemporalFields() {
        JulianDate julianDateNow = JulianDate.now();
        JulianChronology julianChronology = julianDateNow.getChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        fieldValues.put(ChronoField.EPOCH_DAY, 3L);
        ResolverStyle resolverStyle = ResolverStyle.LENIENT;
        JulianDate resolvedDate = julianChronology.resolveDate(fieldValues, resolverStyle);
        assertFalse(resolvedDate.equals(julianDateNow));
    }

    // Test: Verify proleptic year calculation for a BritishCutoverDate
    @Test(timeout = 4000)
    public void testProlepticYearCalculation() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        LocalDate localDate = MockLocalDate.now();
        BritishCutoverDate britishCutoverDate = new BritishCutoverDate(localDate);
        JulianEra julianEra = britishCutoverDate.getEra();
        int prolepticYear = julianChronology.prolepticYear(julianEra, 1850);
        assertEquals(1850, prolepticYear);
    }

    // Test: Create a ChronoLocalDateTime from a LocalDateTime
    @Test(timeout = 4000)
    public void testCreateChronoLocalDateTime() {
        JulianChronology julianChronology = new JulianChronology();
        LocalDateTime localDateTime = MockLocalDateTime.now();
        ChronoLocalDateTime<JulianDate> chronoLocalDateTime = julianChronology.localDateTime(localDateTime);
        assertNotNull(chronoLocalDateTime);
    }

    // Test: Check if a specific year is a leap year
    @Test(timeout = 4000)
    public void testIsLeapYear() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        boolean isLeapYear = julianChronology.isLeapYear(2110L);
        assertFalse(isLeapYear);
    }

    // Test: Get the era of a JulianChronology instance
    @Test(timeout = 4000)
    public void testGetEra() {
        JulianChronology julianChronology = new JulianChronology();
        JulianEra julianEra = julianChronology.eraOf(0);
        assertEquals(JulianEra.BC, julianEra);
    }

    // Test: Create a JulianDate from year and day of year and verify its era
    @Test(timeout = 4000)
    public void testCreateDateYearDay() {
        JulianChronology julianChronology = new JulianChronology();
        JulianDate julianDate = julianChronology.dateYearDay(327, 327);
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Create a JulianDate from the current date and verify its era
    @Test(timeout = 4000)
    public void testCreateDateNow() {
        JulianChronology julianChronology = new JulianChronology();
        ZoneOffset zoneOffset = ZoneOffset.MIN;
        JulianDate julianDate = julianChronology.dateNow(zoneOffset);
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Create a JulianDate from a JapaneseDate and verify its era
    @Test(timeout = 4000)
    public void testCreateDateFromJapaneseDate() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JapaneseDate japaneseDate = MockJapaneseDate.now();
        JulianDate julianDate = julianChronology.date(japaneseDate);
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Create a JulianDate from era, year, month, and day and verify its era
    @Test(timeout = 4000)
    public void testCreateDateFromEraYearMonthDay() {
        JulianChronology julianChronology = new JulianChronology();
        JulianEra julianEra = JulianEra.AD;
        JulianDate julianDate = julianChronology.date(julianEra, -2282, 9, 2);
        assertEquals(JulianEra.BC, julianDate.getEra());
    }

    // Test: Attempt to create a ChronoZonedDateTime from a ZoneOffset and expect a DateTimeException
    @Test(timeout = 4000)
    public void testZonedDateTimeFromZoneOffsetThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        ZoneOffset zoneOffset = ZoneOffset.MAX;
        try {
            julianChronology.zonedDateTime(zoneOffset);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.chrono.Chronology", e);
        }
    }

    // Test: Attempt to create a ChronoZonedDateTime from a null TemporalAccessor and expect a NullPointerException
    @Test(timeout = 4000)
    public void testZonedDateTimeFromNullTemporalAccessorThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.zonedDateTime(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.time.ZoneId", e);
        }
    }

    // Test: Attempt to create a ChronoZonedDateTime from an invalid instant and expect a DateTimeException
    @Test(timeout = 4000)
    public void testZonedDateTimeFromInvalidInstantThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        Instant instant = MockInstant.ofEpochSecond(-1L);
        Period period = Period.ofDays(-1431655764);
        Instant invalidInstant = MockInstant.plus(instant, period);
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        try {
            julianChronology.zonedDateTime(invalidInstant, zoneOffset);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.chrono.Chronology", e);
        }
    }

    // Test: Attempt to resolve a date with an invalid era value and expect a DateTimeException
    @Test(timeout = 4000)
    public void testResolveDateWithInvalidEraThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        fieldValues.put(ChronoField.ERA, 867L);
        ResolverStyle resolverStyle = ResolverStyle.STRICT;
        try {
            julianChronology.resolveDate(fieldValues, resolverStyle);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Attempt to resolve a date with a null map of temporal fields and expect a NullPointerException
    @Test(timeout = 4000)
    public void testResolveDateWithNullFieldsThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        ResolverStyle resolverStyle = ResolverStyle.STRICT;
        try {
            julianChronology.resolveDate(null, resolverStyle);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.time.chrono.AbstractChronology", e);
        }
    }

    // Test: Attempt to create a ChronoLocalDateTime from a ThaiBuddhistEra and expect a DateTimeException
    @Test(timeout = 4000)
    public void testLocalDateTimeFromThaiBuddhistEraThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        ThaiBuddhistEra thaiBuddhistEra = ThaiBuddhistEra.BE;
        try {
            julianChronology.localDateTime(thaiBuddhistEra);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.chrono.Chronology", e);
        }
    }

    // Test: Attempt to create a JulianDate with a JapaneseEra and expect a ClassCastException
    @Test(timeout = 4000)
    public void testDateYearDayWithJapaneseEraThrowsException() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JapaneseEra japaneseEra = JapaneseEra.HEISEI;
        try {
            julianChronology.dateYearDay(japaneseEra, -401, -401);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.threeten.extra.chrono.JulianChronology", e);
        }
    }

    // Test: Attempt to create a JulianDate with an invalid day of year and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateYearDayWithInvalidDayThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.dateYearDay(366, 366);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.chrono.JulianDate", e);
        }
    }

    // Test: Attempt to create a JulianDate from an invalid clock and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateNowWithInvalidClockThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        Instant instant = MockInstant.ofEpochSecond(1702L);
        Period period = Period.ofDays(-1073741823);
        Instant invalidInstant = MockInstant.minus(instant, period);
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Clock clock = MockClock.fixed(invalidInstant, zoneOffset);
        try {
            julianChronology.dateNow(clock);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Attempt to create a JulianDate with a null clock and expect a NullPointerException
    @Test(timeout = 4000)
    public void testDateNowWithNullClockThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.dateNow((Clock) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    // Test: Attempt to create a JulianDate with an offset clock and expect an ArithmeticException
    @Test(timeout = 4000)
    public void testDateNowWithOffsetClockThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        Clock clock = MockClock.systemDefaultZone();
        ChronoUnit chronoUnit = ChronoUnit.FOREVER;
        Duration duration = chronoUnit.getDuration();
        Clock offsetClock = MockClock.offset(clock, duration);
        try {
            julianChronology.dateNow(offsetClock);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("java.lang.Math", e);
        }
    }

    // Test: Attempt to create a JulianDate from an invalid epoch day and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateEpochDayWithInvalidValueThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.dateEpochDay(365250000L);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Attempt to create a JulianDate from a Month and expect an UnsupportedTemporalTypeException
    @Test(timeout = 4000)
    public void testDateFromMonthThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        Month month = Month.APRIL;
        try {
            julianChronology.date(month);
            fail("Expecting exception: UnsupportedTemporalTypeException");
        } catch (UnsupportedTemporalTypeException e) {
            verifyException("java.time.Month", e);
        }
    }

    // Test: Attempt to create a JulianDate from a PaxDate and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateFromPaxDateThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        HijrahDate hijrahDate = MockHijrahDate.now();
        PaxDate paxDate = PaxDate.from(hijrahDate);
        ChronoUnit chronoUnit = ChronoUnit.MILLENNIA;
        PaxDate modifiedPaxDate = paxDate.minus(-2458L, chronoUnit).plusYears(1308L);
        try {
            julianChronology.date(modifiedPaxDate);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Attempt to create a JulianDate with invalid month and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateWithInvalidMonthThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.date(-3115, -3115, -3115);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Verify proleptic year calculation for a BritishCutoverDate with year 0
    @Test(timeout = 4000)
    public void testProlepticYearCalculationForYearZero() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        LocalDate localDate = MockLocalDate.now();
        BritishCutoverDate britishCutoverDate = new BritishCutoverDate(localDate);
        JulianEra julianEra = britishCutoverDate.getEra();
        int prolepticYear = julianChronology.prolepticYear(julianEra, 0);
        assertEquals(0, prolepticYear);
    }

    // Test: Verify proleptic year calculation for BC era
    @Test(timeout = 4000)
    public void testProlepticYearCalculationForBCEra() {
        JulianChronology julianChronology = new JulianChronology();
        JulianEra julianEra = JulianEra.BC;
        int prolepticYear = julianChronology.prolepticYear(julianEra, 981);
        assertEquals(-980, prolepticYear);
    }

    // Test: Attempt to create a JulianDate with a JapaneseEra and expect a ClassCastException
    @Test(timeout = 4000)
    public void testProlepticYearWithJapaneseEraThrowsException() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JapaneseEra japaneseEra = JapaneseEra.HEISEI;
        try {
            julianChronology.prolepticYear(japaneseEra, -1768);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.threeten.extra.chrono.JulianChronology", e);
        }
    }

    // Test: Verify leap year calculation for a negative year
    @Test(timeout = 4000)
    public void testLeapYearCalculationForNegativeYear() {
        JulianChronology julianChronology = new JulianChronology();
        boolean isLeapYear = julianChronology.isLeapYear(-236);
        assertTrue(isLeapYear);
    }

    // Test: Attempt to create a JulianDate from a null TemporalAccessor and expect a NullPointerException
    @Test(timeout = 4000)
    public void testDateFromNullTemporalAccessorThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.date(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.threeten.extra.chrono.JulianDate", e);
        }
    }

    // Test: Attempt to create a JulianDate with an invalid day of year and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateYearDayWithInvalidDayOfYearThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.dateYearDay(2373, 2373);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Attempt to create a ChronoLocalDateTime from a null TemporalAccessor and expect a NullPointerException
    @Test(timeout = 4000)
    public void testLocalDateTimeFromNullTemporalAccessorThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.localDateTime(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.threeten.extra.chrono.JulianDate", e);
        }
    }

    // Test: Attempt to create a ChronoZonedDateTime from a null Instant and expect a NullPointerException
    @Test(timeout = 4000)
    public void testZonedDateTimeFromNullInstantThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        ZoneId zoneId = ZoneId.systemDefault();
        try {
            julianChronology.zonedDateTime(null, zoneId);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.time.chrono.ChronoZonedDateTimeImpl", e);
        }
    }

    // Test: Verify the range of CLOCK_HOUR_OF_AMPM field
    @Test(timeout = 4000)
    public void testRangeOfClockHourOfAmPm() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        ChronoField chronoField = ChronoField.CLOCK_HOUR_OF_AMPM;
        ValueRange valueRange = julianChronology.range(chronoField);
        assertNotNull(valueRange);
    }

    // Test: Verify the range of YEAR_OF_ERA field
    @Test(timeout = 4000)
    public void testRangeOfYearOfEra() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        ChronoField chronoField = ChronoField.YEAR_OF_ERA;
        ValueRange valueRange = julianChronology.range(chronoField);
        assertNotNull(valueRange);
    }

    // Test: Verify the range of YEAR field
    @Test(timeout = 4000)
    public void testRangeOfYear() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        ChronoField chronoField = ChronoField.YEAR;
        ValueRange valueRange = julianChronology.range(chronoField);
        assertNotNull(valueRange);
    }

    // Test: Verify the range of PROLEPTIC_MONTH field
    @Test(timeout = 4000)
    public void testRangeOfProlepticMonth() {
        JulianChronology julianChronology = new JulianChronology();
        ChronoField chronoField = ChronoField.PROLEPTIC_MONTH;
        ValueRange valueRange = julianChronology.range(chronoField);
        assertNotNull(valueRange);
    }

    // Test: Create a JulianDate from era, year, and day of year and verify its era
    @Test(timeout = 4000)
    public void testCreateDateYearDayFromEra() {
        JulianChronology julianChronology = new JulianChronology();
        JulianEra julianEra = JulianEra.AD;
        JulianDate julianDate = julianChronology.dateYearDay(julianEra, 84, 84);
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Attempt to create a JulianDate with a JapaneseEra and expect a ClassCastException
    @Test(timeout = 4000)
    public void testDateWithJapaneseEraThrowsException() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JapaneseEra japaneseEra = JapaneseEra.HEISEI;
        try {
            julianChronology.date(japaneseEra, -1610612735, -1610612735, -1610612735);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.threeten.extra.chrono.JulianChronology", e);
        }
    }

    // Test: Create a JulianDate, subtract days, and verify its era
    @Test(timeout = 4000)
    public void testSubtractDaysFromDate() {
        JulianChronology julianChronology = new JulianChronology();
        JulianDate julianDate = julianChronology.date(3, 3, 3);
        ChronoUnit chronoUnit = ChronoUnit.DAYS;
        JulianDate modifiedDate = julianDate.minus(3L, chronoUnit);
        assertEquals(JulianEra.AD, modifiedDate.getEra());
    }

    // Test: Create a JulianDate from the current date using a clock and verify its era
    @Test(timeout = 4000)
    public void testCreateDateNowWithClock() {
        JulianChronology julianChronology = new JulianChronology();
        Clock clock = MockClock.systemDefaultZone();
        JulianDate julianDate = julianChronology.dateNow(clock);
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Verify the list of eras is not empty
    @Test(timeout = 4000)
    public void testErasListIsNotEmpty() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        List<Era> eras = julianChronology.eras();
        assertFalse(eras.isEmpty());
    }

    // Test: Attempt to create a JulianDate with an invalid month and expect a DateTimeException
    @Test(timeout = 4000)
    public void testDateWithInvalidMonthThrowsExceptionAgain() {
        JulianChronology julianChronology = new JulianChronology();
        JulianEra julianEra = JulianEra.BC;
        try {
            julianChronology.date(julianEra, 983, 983, 983);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    // Test: Verify the calendar type is 'julian'
    @Test(timeout = 4000)
    public void testGetCalendarType() {
        JulianChronology julianChronology = new JulianChronology();
        String calendarType = julianChronology.getCalendarType();
        assertEquals("julian", calendarType);
    }

    // Test: Verify the chronology ID is 'Julian'
    @Test(timeout = 4000)
    public void testGetChronologyId() {
        JulianChronology julianChronology = new JulianChronology();
        String chronologyId = julianChronology.getId();
        assertEquals("Julian", chronologyId);
    }

    // Test: Attempt to get an era with an invalid value and expect a DateTimeException
    @Test(timeout = 4000)
    public void testGetEraWithInvalidValueThrowsException() {
        JulianChronology julianChronology = new JulianChronology();
        try {
            julianChronology.eraOf(3);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.chrono.JulianEra", e);
        }
    }

    // Test: Create a ChronoZonedDateTime from an OffsetDateTime
    @Test(timeout = 4000)
    public void testCreateChronoZonedDateTimeFromOffsetDateTime() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<JulianDate> chronoZonedDateTime = julianChronology.zonedDateTime(offsetDateTime);
        assertNotNull(chronoZonedDateTime);
    }

    // Test: Create a JulianDate from the current date and verify its era
    @Test(timeout = 4000)
    public void testCreateDateNowAgain() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        JulianDate julianDate = julianChronology.dateNow();
        assertEquals(JulianEra.AD, julianDate.getEra());
    }

    // Test: Resolve a JulianDate from an empty map of temporal fields and verify it returns null
    @Test(timeout = 4000)
    public void testResolveDateFromEmptyFields() {
        JulianChronology julianChronology = new JulianChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        ResolverStyle resolverStyle = ResolverStyle.STRICT;
        JulianDate resolvedDate = julianChronology.resolveDate(fieldValues, resolverStyle);
        assertNull(resolvedDate);
    }

    // Test: Attempt to create a JulianDate with a null zone and expect a NullPointerException
    @Test(timeout = 4000)
    public void testDateNowWithNullZoneThrowsException() {
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        try {
            julianChronology.dateNow((ZoneId) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }
}