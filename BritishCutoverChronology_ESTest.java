package org.threeten.extra.chrono;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.BritishCutoverChronology;
import org.threeten.extra.chrono.BritishCutoverDate;
import org.threeten.extra.chrono.JulianEra;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BritishCutoverChronology_ESTest extends BritishCutoverChronology_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;
    private static final int INVALID_MONTH = 1000;
    private static final int VALID_YEAR = 6;
    private static final int VALID_DAY = 1;

    @Test(timeout = TIMEOUT)
    public void testInvalidMonthThrowsException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JulianEra era = JulianEra.AD;

        try {
            chronology.date(era, VALID_YEAR, INVALID_MONTH, VALID_DAY);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testZonedDateTimeCreation() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Instant instant = Instant.ofEpochSecond(1004L, -1721L);
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(0);

        ChronoZonedDateTime<BritishCutoverDate> zonedDateTime = chronology.zonedDateTime(instant, offset);
        assertNotNull(zonedDateTime);
    }

    @Test(timeout = TIMEOUT)
    public void testResolveDateFromMap() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        fieldValues.put(ChronoField.EPOCH_DAY, 763L);

        BritishCutoverDate date = chronology.resolveDate(fieldValues, ResolverStyle.STRICT);
        assertNotNull(date);
    }

    @Test(timeout = TIMEOUT)
    public void testProlepticYearCalculation() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JulianEra era = JulianEra.AD;

        int prolepticYear = chronology.prolepticYear(era, 13);
        assertEquals(13, prolepticYear);
    }

    @Test(timeout = TIMEOUT)
    public void testLocalDateTimeCreation() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        ChronoLocalDateTime<BritishCutoverDate> localDateTime = chronology.localDateTime(zonedDateTime);
        assertNotNull(localDateTime);
    }

    @Test(timeout = TIMEOUT)
    public void testIsLeapYear() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        assertFalse(chronology.isLeapYear(371));
        assertTrue(chronology.isLeapYear(1752L));
        assertTrue(chronology.isLeapYear(3676L));
    }

    @Test(timeout = TIMEOUT)
    public void testDateYearDayCreation() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        JulianEra era = JulianEra.BC;

        BritishCutoverDate date = chronology.dateYearDay(era, 5, 5);
        assertNotNull(date);
    }

    @Test(timeout = TIMEOUT)
    public void testDateNowWithZone() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ZoneId zoneId = ZoneId.systemDefault();

        BritishCutoverDate date = chronology.dateNow(zoneId);
        assertNotNull(date);
    }

    @Test(timeout = TIMEOUT)
    public void testDateEpochDayCreation() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        BritishCutoverDate date = chronology.dateEpochDay(-1813L);
        assertNotNull(date);
    }

    @Test(timeout = TIMEOUT)
    public void testDateNow() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        BritishCutoverDate date = chronology.dateNow();
        assertNotNull(date);
    }

    @Test(timeout = TIMEOUT)
    public void testDateFromTemporalAccessor() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        BritishCutoverDate dateNow = chronology.dateNow();

        BritishCutoverDate date = chronology.date(dateNow);
        assertSame(dateNow, date);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCutover() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        LocalDate cutoverDate = chronology.getCutover();
        assertNotNull(cutoverDate);
    }

    @Test(timeout = TIMEOUT)
    public void testGetId() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();

        String id = chronology.getId();
        assertEquals("BritishCutover", id);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCalendarType() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();

        String calendarType = chronology.getCalendarType();
        assertNull(calendarType);
    }

    @Test(timeout = TIMEOUT)
    public void testEras() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();

        List<Era> eras = chronology.eras();
        assertEquals(2, eras.size());
    }

    @Test(timeout = TIMEOUT)
    public void testEraOf() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();

        JulianEra era = chronology.eraOf(0);
        assertEquals(JulianEra.BC, era);
    }

    // Additional tests for exceptions and edge cases can be added here
}