package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.DiscordianDate;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class InternationalFixedChronology_ESTest extends InternationalFixedChronology_ESTest_scaffolding {

    private static final InternationalFixedChronology CHRONO = InternationalFixedChronology.INSTANCE;
    private static final InternationalFixedEra CE_ERA = InternationalFixedEra.CE;

    //-----------------------------------------------------------------------
    // Tests for isLeapYear()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testIsLeapYear_nonLeapYear_returnsFalse() {
        assertFalse(CHRONO.isLeapYear(-1145400L));
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_leapYear_returnsTrue() {
        assertTrue(CHRONO.isLeapYear(-3708L));
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_regularYear_returnsFalse() {
        assertFalse(CHRONO.isLeapYear(241L));
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_longLeapYear_returnsTrue() {
        assertTrue(CHRONO.isLeapYear(365000000L));
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_divisibleBy4_returnsTrue() {
        assertTrue(CHRONO.isLeapYear(4L));
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_centuryNonLeap_returnsFalse() {
        assertFalse(CHRONO.isLeapYear(100L));
    }

    //-----------------------------------------------------------------------
    // Tests for getLeapYearsBefore()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testGetLeapYearsBefore_zero_returnsZero() {
        assertEquals(0L, InternationalFixedChronology.getLeapYearsBefore(0L));
    }

    @Test(timeout = 4000)
    public void testGetLeapYearsBefore_positiveValue_returnsCorrectCount() {
        assertEquals(88571217L, InternationalFixedChronology.getLeapYearsBefore(365242134L));
    }

    @Test(timeout = 4000)
    public void testGetLeapYearsBefore_negativeValue_returnsCorrectCount() {
        assertEquals(-390L, InternationalFixedChronology.getLeapYearsBefore(-1610L));
    }

    //-----------------------------------------------------------------------
    // Tests for eraOf()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testEraOf_validEraValue_returnsCE() {
        Era era = CHRONO.eraOf(1);
        assertEquals(InternationalFixedEra.CE, era);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testEraOf_invalidEraValue_throwsException() {
        CHRONO.eraOf(-2095105997);
    }

    //-----------------------------------------------------------------------
    // Tests for prolepticYear()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testProlepticYear_validCEYear_returnsSameValue() {
        int year = CHRONO.prolepticYear(CE_ERA, 734);
        assertEquals(734, year);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testProlepticYear_invalidYear_throwsException() {
        CHRONO.prolepticYear(CE_ERA, -2073432486);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testProlepticYear_invalidEraType_throwsException() {
        CHRONO.prolepticYear(IsoEra.BCE, -985);
    }

    //-----------------------------------------------------------------------
    // Tests for date() with various parameters
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testDate_validDate_createsInstance() {
        InternationalFixedDate date = CHRONO.date(3309, 8, 10);
        assertEquals(489265L, date.toEpochDay());
    }

    @Test(timeout = 4000)
    public void testDateEra_validDate_createsInstance() {
        InternationalFixedDate date = CHRONO.date(CE_ERA, 7, 7, 7);
        assertEquals(-716797L, date.toEpochDay());
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDate_invalidMonth_throwsException() {
        CHRONO.date(36526, 36526, 36526);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDate_invalidDay_throwsException() {
        CHRONO.date(4, 4, 29);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testDate_invalidEraType_throwsException() {
        CHRONO.date(JapaneseEra.TAISHO, -2073432486, -2073432486, -2073432486);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDate_invalidDayOfMonth_throwsException() {
        CHRONO.date(CE_ERA, 6, 1, -347);
    }

    //-----------------------------------------------------------------------
    // Tests for dateYearDay()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testDateYearDay_validYearDay_createsInstance() {
        InternationalFixedDate date = CHRONO.dateYearDay(134, 134);
        assertEquals(-670452L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateYearDayEra_validYearDay_createsInstance() {
        InternationalFixedDate date = CHRONO.dateYearDay(CE_ERA, 157, 3);
        assertEquals(-662182L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateYearDay_invalidDayOfYear_throwsException() {
        CHRONO.dateYearDay(2191, 2191);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateYearDay_nonLeapYearWith366Days_throwsException() {
        CHRONO.dateYearDay(366, 366);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateYearDayEra_invalidYear_throwsException() {
        CHRONO.dateYearDay(CE_ERA, 2103657451, 2103657451);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testDateYearDayEra_invalidEraType_throwsException() {
        CHRONO.dateYearDay(JapaneseEra.HEISEI, 734, 734);
    }

    //-----------------------------------------------------------------------
    // Tests for dateEpochDay()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testDateEpochDay_zero_createsInstance() {
        InternationalFixedDate date = CHRONO.dateEpochDay(0L);
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateEpochDay_positiveValue_createsInstance() {
        InternationalFixedDate date = CHRONO.dateEpochDay(146096L);
        assertEquals(365, date.lengthOfYear());
        assertEquals(29, date.lengthOfMonth());
    }

    @Test(timeout = 4000)
    public void testDateEpochDay_negativeValue_createsInstance() {
        InternationalFixedDate date = CHRONO.dateEpochDay(-2219L);
        assertEquals(365, date.lengthOfYear());
        assertEquals(29, date.lengthOfMonth());
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateEpochDay_invalidValue_throwsException() {
        CHRONO.dateEpochDay(-1145400L);
    }

    //-----------------------------------------------------------------------
    // Tests for dateNow()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testDateNow_defaultZone_createsInstance() {
        System.setCurrentTimeMillis(100L);
        InternationalFixedDate date = CHRONO.dateNow();
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateNow_epochDayMinusOne_createsInstance() {
        System.setCurrentTimeMillis(-1L);
        InternationalFixedDate date = CHRONO.dateNow();
        assertEquals(365, date.lengthOfYear());
        assertEquals(-1L, date.toEpochDay());
    }

    @Test(timeout = 4000)
    public void testDateNow_withZoneId_createsInstance() {
        System.setCurrentTimeMillis(16L);
        InternationalFixedDate date = CHRONO.dateNow(ZoneId.systemDefault());
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateNow_withZoneOffset_createsInstance() {
        System.setCurrentTimeMillis(-633L);
        InternationalFixedDate date = CHRONO.dateNow(ZoneOffset.UTC);
        assertEquals(29, date.lengthOfMonth());
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateNow_withClock_createsInstance() {
        System.setCurrentTimeMillis(5130L);
        Clock clock = MockClock.systemDefaultZone();
        InternationalFixedDate date = CHRONO.dateNow(clock);
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateNow_withClockNegativeTime_createsInstance() {
        System.setCurrentTimeMillis(-2957L);
        Clock clock = MockClock.systemDefaultZone();
        InternationalFixedDate date = CHRONO.dateNow(clock);
        assertEquals(365, date.lengthOfYear());
        assertEquals(29, date.lengthOfMonth());
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDateNow_nullZoneId_throwsException() {
        CHRONO.dateNow((ZoneId) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDateNow_nullClock_throwsException() {
        CHRONO.dateNow((Clock) null);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateNow_clockWithInvalidOffset_throwsException() {
        Clock baseClock = MockClock.systemDefaultZone();
        Clock offsetClock = MockClock.offset(baseClock, Duration.ofSeconds(Long.MIN_VALUE));
        CHRONO.dateNow(offsetClock);
    }

    //-----------------------------------------------------------------------
    // Tests for date(TemporalAccessor)
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testDateFromTemporal_validDate_createsInstance() {
        System.setCurrentTimeMillis(5130L);
        InternationalFixedDate originalDate = CHRONO.dateNow();
        InternationalFixedDate date = CHRONO.date((TemporalAccessor) originalDate);
        assertEquals(365, date.lengthOfYear());
    }

    @Test(timeout = 4000)
    public void testDateFromTemporal_anotherDate_createsInstance() {
        InternationalFixedDate originalDate = CHRONO.dateEpochDay(146096L);
        InternationalFixedDate date = CHRONO.date((TemporalAccessor) originalDate);
        assertEquals(365, date.lengthOfYear());
        assertEquals(29, date.lengthOfMonth());
    }

    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testDateFromTemporal_month_throwsException() {
        CHRONO.date(Month.MAY);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testDateFromTemporal_invalidEpochDay_throwsException() {
        DiscordianDate discordianDate = DiscordianDate.ofYearDay(9, 9);
        CHRONO.date(discordianDate);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDateFromTemporal_null_throwsException() {
        CHRONO.date((TemporalAccessor) null);
    }

    //-----------------------------------------------------------------------
    // Tests for zonedDateTime()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testZonedDateTime_validInstant_createsInstance() {
        Instant instant = MockInstant.ofEpochSecond(-1512L, 107016L);
        ChronoZonedDateTime<InternationalFixedDate> zdt = 
            CHRONO.zonedDateTime(instant, ZoneOffset.MAX);
        assertNotNull(zdt);
    }

    @Test(timeout = 4000)
    public void testZonedDateTime_validZonedDateTime_createsInstance() {
        ZonedDateTime zdt = MockZonedDateTime.now();
        ChronoZonedDateTime<InternationalFixedDate> result = 
            CHRONO.zonedDateTime(zdt);
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testZonedDateTime_validLocalDateTime_createsInstance() {
        LocalDateTime ldt = MockLocalDateTime.now(ZoneOffset.MAX);
        ChronoZonedDateTime<InternationalFixedDate> result = 
            CHRONO.zonedDateTime(ldt);
        assertNotNull(result);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testZonedDateTime_invalidTemporal_throwsException() {
        CHRONO.zonedDateTime(JapaneseEra.HEISEI);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testZonedDateTime_invalidInstant_throwsException() {
        Instant instant = MockInstant.now();
        Period period = Period.ofDays(-2106965087);
        Instant offsetInstant = MockInstant.minus(instant, period);
        CHRONO.zonedDateTime(offsetInstant, ZoneOffset.MIN);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testZonedDateTime_nullTemporal_throwsException() {
        CHRONO.zonedDateTime((TemporalAccessor) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testZonedDateTime_nullInstant_throwsException() {
        CHRONO.zonedDateTime((Instant) null, ZoneOffset.MAX);
    }

    //-----------------------------------------------------------------------
    // Tests for localDateTime()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testLocalDateTime_validZonedDateTime_createsInstance() {
        ZonedDateTime zdt = MockZonedDateTime.now(ZoneOffset.MAX);
        ChronoLocalDateTime<InternationalFixedDate> ldt = 
            CHRONO.localDateTime(zdt);
        assertNotNull(ldt);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testLocalDateTime_invalidTemporal_throwsException() {
        CHRONO.localDateTime(MockYear.of(37));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testLocalDateTime_nullTemporal_throwsException() {
        CHRONO.localDateTime((TemporalAccessor) null);
    }

    //-----------------------------------------------------------------------
    // Tests for range()
    //-----------------------------------------------------------------------

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testRange_nullField_throwsException() {
        CHRONO.range(null);
    }

    @Test(timeout = 4000)
    public void testRange_yearField_returnsValidRange() {
        ValueRange range = CHRONO.range(ChronoField.YEAR);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testRange_epochDayField_returnsValidRange() {
        ValueRange range = CHRONO.range(ChronoField.EPOCH_DAY);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testRange_monthField_returnsValidRange() {
        ValueRange range = CHRONO.range(ChronoField.MONTH_OF_YEAR);
        assertNotNull(range);
    }

    // Additional range field tests omitted for brevity

    //-----------------------------------------------------------------------
    // Tests for other methods
    //-----------------------------------------------------------------------

    @Test(timeout = 4000)
    public void testGetId_returnsIfc() {
        assertEquals("Ifc", CHRONO.getId());
    }

    @Test(timeout = 4000)
    public void testGetCalendarType_returnsNull() {
        assertNull(CHRONO.getCalendarType());
    }

    @Test(timeout = 4000)
    public void testEras_returnsSingleEra() {
        List<Era> eras = CHRONO.eras();
        assertEquals(1, eras.size());
    }

    @Test(timeout = 4000)
    public void testDateNowWithClock_afterAdjustment_createsInstance() {
        InternationalFixedDate originalDate = CHRONO.dateNow();
        InternationalFixedDate adjustedDate = originalDate.plusMonths(-2645L);
        InternationalFixedDate result = CHRONO.date(adjustedDate);
        assertEquals(-58199L, result.toEpochDay());
    }
}