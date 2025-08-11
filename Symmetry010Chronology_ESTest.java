package org.threeten.extra.chrono;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.*;
import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.List;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Symmetry010ChronologyTest extends Symmetry010Chronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testInvalidMonthValueThrowsDateTimeException() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Symmetry010Date date = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = date.getEra();

        try {
            chronology.date(era, 364, -2552, 1769);
            fail("Expected DateTimeException due to invalid month value");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidEraThrowsClassCastException() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        JulianDate julianDate = JulianDate.ofEpochDay(-604L);
        JulianEra era = julianDate.getEra();

        try {
            chronology.date(era, -793, 1231, -3218);
            fail("Expected ClassCastException due to invalid era");
        } catch (ClassCastException e) {
            verifyException("org.threeten.extra.chrono.Symmetry010Chronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testProlepticYearForIsoEraBCE() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        IsoEra era = IsoEra.BCE;
        int prolepticYear = chronology.INSTANCE.prolepticYear(era, 0);
        assertEquals(0, prolepticYear);
    }

    @Test(timeout = 4000)
    public void testProlepticYearForIsoEraCE() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Clock clock = MockClock.systemDefaultZone();
        Symmetry010Date date = chronology.dateNow(clock);
        IsoEra era = date.getEra();
        int prolepticYear = chronology.prolepticYear(era, 3994);
        assertEquals(3994, prolepticYear);
    }

    @Test(timeout = 4000)
    public void testLeapYearCalculation() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        boolean isLeapYear = chronology.isLeapYear(-1547L);
        assertTrue(isLeapYear);
    }

    @Test(timeout = 4000)
    public void testLeapYearsBeforeGivenYear() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(560L);
        assertEquals(99L, leapYears);
    }

    @Test(timeout = 4000)
    public void testChronologyId() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        String id = chronology.getId();
        assertEquals("Sym010", id);
    }

    @Test(timeout = 4000)
    public void testEraOfValue() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        IsoEra era = chronology.eraOf(0);
        assertEquals(IsoEra.BCE, era);
    }

    @Test(timeout = 4000)
    public void testDateYearDay() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        Symmetry010Date date = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = date.getEra();
        Symmetry010Date resultDate = chronology.dateYearDay(era, 14, 30);
        assertEquals(IsoEra.CE, resultDate.getEra());
    }

    @Test(timeout = 4000)
    public void testInvalidDayOfYearThrowsDateTimeException() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        IsoEra era = IsoEra.CE;

        try {
            chronology.dateYearDay(era, 1223, 1223);
            fail("Expected DateTimeException due to invalid day of year");
        } catch (DateTimeException e) {
            verifyException("java.time.temporal.ValueRange", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidEraThrowsClassCastExceptionForDateYearDay() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        ThaiBuddhistEra era = ThaiBuddhistEra.BEFORE_BE;

        try {
            chronology.dateYearDay(era, 107016, 107016);
            fail("Expected ClassCastException due to invalid era");
        } catch (ClassCastException e) {
            verifyException("org.threeten.extra.chrono.Symmetry010Chronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidDateThrowsDateTimeException() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();

        try {
            chronology.date(5, 5, 35);
            fail("Expected DateTimeException due to invalid date");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.chrono.Symmetry010Date", e);
        }
    }

    @Test(timeout = 4000)
    public void testCalendarTypeIsNull() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        String calendarType = chronology.getCalendarType();
        assertNull(calendarType);
    }

    @Test(timeout = 4000)
    public void testEraOfThrowsDateTimeExceptionForInvalidValue() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();

        try {
            chronology.eraOf(-2135875627);
            fail("Expected DateTimeException due to invalid era value");
        } catch (DateTimeException e) {
            verifyException("java.time.chrono.IsoEra", e);
        }
    }

    @Test(timeout = 4000)
    public void testDateNowWithZoneId() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Symmetry010Date date = chronology.dateNow(zoneOffset);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateEpochDay() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        Symmetry010Date date = chronology.dateEpochDay(719162L);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testErasListSize() {
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        List<Era> eras = chronology.eras();
        assertEquals(2, eras.size());
    }

    @Test(timeout = 4000)
    public void testDateNow() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        Symmetry010Date date = chronology.dateNow();
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(timeout = 4000)
    public void testZonedDateTimeFromTemporalAccessor() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<Symmetry010Date> zonedDateTime = chronology.zonedDateTime(offsetDateTime);
        assertNotNull(zonedDateTime);
    }

    @Test(timeout = 4000)
    public void testInvalidDayOfYearInLeapYearThrowsDateTimeException() {
        Symmetry010Chronology chronology = new Symmetry010Chronology();

        try {
            chronology.dateYearDay(371, 371);
            fail("Expected DateTimeException due to invalid day of year in non-leap year");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.chrono.Symmetry010Date", e);
        }
    }
}