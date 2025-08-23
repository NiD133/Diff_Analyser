package org.threeten.extra.chrono;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.EthiopicDate;
import org.threeten.extra.chrono.PaxDate;
import org.threeten.extra.chrono.Symmetry454Chronology;
import org.threeten.extra.chrono.Symmetry454Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Symmetry454ChronologyTest {

    private static final long TEST_EPOCH_DAY = 719162L;
    private static final int INVALID_YEAR = -2133538947;
    private static final int INVALID_MONTH = 1363;
    private static final int INVALID_DAY_OF_YEAR = 371;

    @Test(timeout = 4000)
    public void testInvalidMonthThrowsDateTimeException() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        IsoEra era = IsoEra.BCE;
        try {
            chronology.date(era, 3322, INVALID_MONTH, 8);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            assertEquals("Invalid value for MonthOfYear (valid values 1 - 12): " + INVALID_MONTH, e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testZonedDateTimeCreation() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Instant instant = Instant.ofEpochSecond(3L);
        ChronoZonedDateTime<Symmetry454Date> zonedDateTime = chronology.zonedDateTime(instant, zoneOffset);
        assertNotNull(zonedDateTime);
    }

    @Test(timeout = 4000)
    public void testProlepticYearCalculationForBCE() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        IsoEra era = IsoEra.BCE;
        int prolepticYear = chronology.prolepticYear(era, 1996);
        assertEquals(1996, prolepticYear);
    }

    @Test(timeout = 4000)
    public void testProlepticYearCalculationForCE() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        IsoEra era = IsoEra.CE;
        int prolepticYear = chronology.prolepticYear(era, -1390);
        assertEquals(-1390, prolepticYear);
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeConversion() {
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        LocalDateTime localDateTime = LocalDateTime.now();
        ChronoLocalDateTime<Symmetry454Date> chronoLocalDateTime = chronology.localDateTime(localDateTime);
        assertNotNull(chronoLocalDateTime);
    }

    @Test(timeout = 4000)
    public void testLeapYearCheck() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        assertTrue(chronology.isLeapYear(3L));
        assertFalse(chronology.isLeapYear(32L));
    }

    @Test(timeout = 4000)
    public void testLeapYearsBefore() {
        assertEquals(127633L, Symmetry454Chronology.getLeapYearsBefore(TEST_EPOCH_DAY));
        assertEquals(-11L, Symmetry454Chronology.getLeapYearsBefore(-60L));
        assertEquals(0L, Symmetry454Chronology.getLeapYearsBefore(0L));
    }

    @Test(timeout = 4000)
    public void testEraOf() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        IsoEra era = chronology.eraOf(0);
        assertEquals(IsoEra.BCE, era);
    }

    @Test(timeout = 4000)
    public void testDateYearDay() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        Symmetry454Date date = chronology.dateYearDay(19, 19);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateEpochDay() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        Symmetry454Date date = chronology.dateEpochDay(132L);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateFromTemporalAccessor() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        Symmetry454Date nowDate = Symmetry454Date.now(ZoneId.systemDefault());
        Symmetry454Date date = chronology.date(nowDate);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateWithEra() {
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        IsoEra era = IsoEra.CE;
        Symmetry454Date date = chronology.date(era, 4, 4, 4);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testInvalidEraThrowsDateTimeException() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        try {
            chronology.eraOf(-334);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            assertEquals("Invalid era: -334", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvalidDateYearDayThrowsDateTimeException() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        IsoEra era = IsoEra.BCE;
        try {
            chronology.dateYearDay(era, 1145, INVALID_DAY_OF_YEAR);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            assertEquals("Invalid date 'DayOfYear 371' as '1145' is not a leap year", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetId() {
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        assertEquals("Sym454", chronology.getId());
    }

    @Test(timeout = 4000)
    public void testGetCalendarType() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        assertNull(chronology.getCalendarType());
    }

    @Test(timeout = 4000)
    public void testEras() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        List<Era> eras = chronology.eras();
        assertFalse(eras.isEmpty());
    }

    @Test(timeout = 4000)
    public void testDateNow() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        Symmetry454Date date = chronology.dateNow();
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testInvalidDateThrowsDateTimeException() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        try {
            chronology.date(1, 1, 35);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            assertEquals("Invalid date: 1/1/35", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testRange() {
        Symmetry454Chronology chronology = new Symmetry454Chronology();
        ValueRange range = chronology.range(ChronoField.YEAR);
        assertNotNull(range);
    }
}