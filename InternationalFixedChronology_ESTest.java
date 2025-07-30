package org.threeten.extra.chrono;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.DiscordianDate;
import org.threeten.extra.chrono.InternationalFixedChronology;
import org.threeten.extra.chrono.InternationalFixedDate;
import org.threeten.extra.chrono.InternationalFixedEra;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class InternationalFixedChronologyTest {

    private static final long EPOCH_DAY_0 = 0L;
    private static final long LEAP_YEAR_365242134 = 365242134L;
    private static final long LEAP_YEAR_0 = 0L;
    private static final long LEAP_YEAR_MINUS_1610 = -1610L;
    private static final long LEAP_YEAR_MINUS_1145400 = -1145400L;
    private static final int YEAR_734 = 734;
    private static final int YEAR_134 = 134;
    private static final int YEAR_4314 = 4314;
    private static final int YEAR_3309 = 3309;
    private static final int YEAR_36526 = 36526;
    private static final int YEAR_2191 = 2191;
    private static final int YEAR_2 = 2;
    private static final int YEAR_7 = 7;
    private static final int YEAR_365 = 365;
    private static final int YEAR_366 = 366;
    private static final int YEAR_2103657451 = 2103657451;
    private static final int YEAR_MINUS_2073432486 = -2073432486;
    private static final int YEAR_MINUS_2095105997 = -2095105997;
    private static final int YEAR_MINUS_985 = -985;
    private static final int YEAR_241 = 241;
    private static final int YEAR_100 = 100;
    private static final int YEAR_4 = 4;
    private static final int YEAR_365000000 = 365000000;
    private static final int YEAR_2106965087 = -2106965087;

    @Test
    public void testIsLeapYear() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        assertFalse(chronology.isLeapYear(LEAP_YEAR_MINUS_1145400));
        assertTrue(chronology.isLeapYear(LEAP_YEAR_365242134));
        assertFalse(chronology.isLeapYear(LEAP_YEAR_0));
        assertFalse(chronology.isLeapYear(LEAP_YEAR_MINUS_1610));
        assertFalse(chronology.isLeapYear(YEAR_241));
        assertFalse(chronology.isLeapYear(YEAR_100));
        assertTrue(chronology.isLeapYear(YEAR_4));
        assertTrue(chronology.isLeapYear(YEAR_365000000));
    }

    @Test
    public void testGetLeapYearsBefore() {
        assertEquals(0L, InternationalFixedChronology.getLeapYearsBefore(LEAP_YEAR_0));
        assertEquals(88571217L, InternationalFixedChronology.getLeapYearsBefore(LEAP_YEAR_365242134));
        assertEquals(-390L, InternationalFixedChronology.getLeapYearsBefore(LEAP_YEAR_MINUS_1610));
    }

    @Test
    public void testProlepticYear() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedEra era = InternationalFixedEra.CE;
        assertEquals(YEAR_734, chronology.prolepticYear(era, YEAR_734));

        // Test for invalid era value
        try {
            chronology.prolepticYear(era, YEAR_MINUS_2073432486);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testEraOf() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        assertEquals(InternationalFixedEra.CE, chronology.eraOf(1));

        // Test for invalid era value
        try {
            chronology.eraOf(YEAR_MINUS_2095105997);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDateYearDay() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedEra era = InternationalFixedEra.CE;
        InternationalFixedDate date = chronology.dateYearDay(era, YEAR_157, 3);
        assertEquals(365, date.lengthOfYear());
        assertEquals(-662182L, date.toEpochDay());

        // Test for invalid day of year
        try {
            chronology.dateYearDay(era, YEAR_2103657451, YEAR_2103657451);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDateEpochDay() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedDate date = chronology.dateEpochDay(EPOCH_DAY_0);
        assertEquals(365, date.lengthOfYear());

        // Test for invalid epoch day
        try {
            chronology.dateEpochDay(LEAP_YEAR_MINUS_1145400);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testDateNow() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedDate date = chronology.dateNow();
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void testDateNowWithZone() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ZoneId zone = ZoneId.systemDefault();
        InternationalFixedDate date = chronology.dateNow(zone);
        assertEquals(365, date.lengthOfYear());

        // Test for null zone
        try {
            chronology.dateNow((ZoneId) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testDateNowWithClock() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Clock clock = Clock.systemDefaultZone();
        InternationalFixedDate date = chronology.dateNow(clock);
        assertEquals(365, date.lengthOfYear());

        // Test for null clock
        try {
            chronology.dateNow((Clock) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testDate() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        InternationalFixedDate date = chronology.date(YEAR_2, 2, 13);
        assertEquals(365, date.lengthOfYear());
        assertEquals(-718757L, date.toEpochDay());

        // Test for invalid date
        try {
            chronology.date(YEAR_36526, YEAR_36526, YEAR_36526);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testZonedDateTime() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Instant instant = Instant.now();
        ZoneId zone = ZoneId.systemDefault();
        ChronoZonedDateTime<InternationalFixedDate> zonedDateTime = chronology.zonedDateTime(instant, zone);
        assertNotNull(zonedDateTime);

        // Test for null instant
        try {
            chronology.zonedDateTime((Instant) null, zone);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocalDateTime() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        LocalDateTime localDateTime = LocalDateTime.now();
        ChronoLocalDateTime<InternationalFixedDate> chronoLocalDateTime = chronology.localDateTime(localDateTime);
        assertNotNull(chronoLocalDateTime);

        // Test for null temporal
        try {
            chronology.localDateTime((TemporalAccessor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testRange() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        ValueRange range = chronology.range(ChronoField.YEAR);
        assertNotNull(range);

        // Test for null field
        try {
            chronology.range((ChronoField) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetId() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        assertEquals("Ifc", chronology.getId());
    }

    @Test
    public void testGetCalendarType() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        assertNull(chronology.getCalendarType());
    }

    @Test
    public void testEras() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        List<Era> eras = chronology.eras();
        assertEquals(1, eras.size());
    }
}