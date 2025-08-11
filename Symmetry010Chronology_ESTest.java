package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.MockOffsetDateTime;
import org.evosuite.runtime.mock.java.time.MockYear;
import org.threeten.extra.chrono.CopticDate;
import org.threeten.extra.chrono.EthiopicDate;
import org.threeten.extra.chrono.JulianDate;
import org.threeten.extra.chrono.JulianEra;
import org.threeten.extra.chrono.PaxDate;

/**
 * Test suite for Symmetry010Chronology class.
 * Tests chronology operations, date creation, validation, and error handling.
 */
public class Symmetry010ChronologyTest {

    private static final Symmetry010Chronology CHRONOLOGY = Symmetry010Chronology.INSTANCE;

    // ========== Basic Properties Tests ==========

    @Test
    public void testGetId_ReturnsCorrectIdentifier() {
        String id = CHRONOLOGY.getId();
        assertEquals("Sym010", id);
    }

    @Test
    public void testGetCalendarType_ReturnsNull() {
        String calendarType = CHRONOLOGY.getCalendarType();
        assertNull(calendarType);
    }

    @Test
    public void testEras_ReturnsCorrectEraList() {
        List<Era> eras = CHRONOLOGY.eras();
        assertEquals(2, eras.size());
    }

    @Test
    public void testEraOf_WithValidValue_ReturnsCorrectEra() {
        IsoEra era = CHRONOLOGY.eraOf(0);
        assertEquals(IsoEra.BCE, era);
    }

    @Test(expected = DateTimeException.class)
    public void testEraOf_WithInvalidValue_ThrowsException() {
        CHRONOLOGY.eraOf(-2135875627);
    }

    // ========== Leap Year Tests ==========

    @Test
    public void testIsLeapYear_WithLeapYear_ReturnsTrue() {
        boolean isLeapYear = CHRONOLOGY.isLeapYear(-1547L);
        assertTrue(isLeapYear);
    }

    @Test
    public void testIsLeapYear_WithNonLeapYear_ReturnsFalse() {
        boolean isLeapYear = CHRONOLOGY.isLeapYear(32L);
        assertFalse(isLeapYear);
    }

    @Test
    public void testGetLeapYearsBefore_WithZero_ReturnsZero() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(0L);
        assertEquals(0L, leapYears);
    }

    @Test
    public void testGetLeapYearsBefore_WithPositiveYear_ReturnsCorrectCount() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(560L);
        assertEquals(99L, leapYears);
    }

    @Test
    public void testGetLeapYearsBefore_WithNegativeYear_ReturnsCorrectCount() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(-2552);
        assertEquals(-453L, leapYears);
    }

    // ========== Date Creation Tests ==========

    @Test
    public void testDate_WithValidYearMonthDay_CreatesDate() {
        Symmetry010Date date = CHRONOLOGY.date(12, 12, 12);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDate_WithInvalidMonth_ThrowsException() {
        CHRONOLOGY.date(13, 13, 13);
    }

    @Test(expected = DateTimeException.class)
    public void testDate_WithInvalidDay_ThrowsException() {
        CHRONOLOGY.date(5, 5, 35);
    }

    @Test
    public void testDate_WithEraAndValidValues_CreatesDate() {
        IsoEra era = IsoEra.CE;
        Symmetry010Date date = CHRONOLOGY.date(era, 10, 10, 10);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDate_WithEraAndInvalidMonth_ThrowsException() {
        Symmetry010Date testDate = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = testDate.getEra();
        CHRONOLOGY.date(era, 364, -2552, 1769);
    }

    @Test(expected = ClassCastException.class)
    public void testDate_WithInvalidEra_ThrowsClassCastException() {
        JulianDate julianDate = JulianDate.ofEpochDay(-604L);
        JulianEra invalidEra = julianDate.getEra();
        CHRONOLOGY.date(invalidEra, -793, 1231, -3218);
    }

    // ========== Date Year Day Tests ==========

    @Test
    public void testDateYearDay_WithValidValues_CreatesDate() {
        Symmetry010Date date = CHRONOLOGY.dateYearDay(-762, 4);
        assertEquals(IsoEra.BCE, date.getEra());
    }

    @Test
    public void testDateYearDay_WithEraAndValidValues_CreatesDate() {
        Symmetry010Date testDate = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = testDate.getEra();
        Symmetry010Date date = CHRONOLOGY.dateYearDay(era, 14, 30);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_WithInvalidDayOfYear_ThrowsException() {
        CHRONOLOGY.dateYearDay(999, 999);
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_WithEraAndInvalidDayOfYear_ThrowsException() {
        IsoEra era = IsoEra.CE;
        CHRONOLOGY.dateYearDay(era, 1223, 1223);
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_WithNonLeapYearAndInvalidDay_ThrowsException() {
        CHRONOLOGY.dateYearDay(371, 371);
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_WithEraAndNonLeapYearInvalidDay_ThrowsException() {
        IsoEra era = IsoEra.CE;
        CHRONOLOGY.dateYearDay(era, 369, 369);
    }

    @Test(expected = ClassCastException.class)
    public void testDateYearDay_WithInvalidEra_ThrowsClassCastException() {
        ThaiBuddhistEra invalidEra = ThaiBuddhistEra.BEFORE_BE;
        CHRONOLOGY.dateYearDay(invalidEra, 107016, 107016);
    }

    // ========== Date from Epoch Day Tests ==========

    @Test
    public void testDateEpochDay_WithValidEpochDay_CreatesDate() {
        Symmetry010Date date = CHRONOLOGY.dateEpochDay(719162L);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDateEpochDay_WithInvalidEpochDay_ThrowsException() {
        CHRONOLOGY.dateEpochDay(365250000L);
    }

    // ========== Date Now Tests ==========

    @Test
    public void testDateNow_WithDefaultZone_CreatesCurrentDate() {
        Symmetry010Date date = CHRONOLOGY.dateNow();
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void testDateNow_WithSpecificZone_CreatesCurrentDate() {
        ZoneOffset zone = ZoneOffset.UTC;
        Symmetry010Date date = CHRONOLOGY.dateNow(zone);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void testDateNow_WithClock_CreatesCurrentDate() {
        Clock clock = MockClock.systemDefaultZone();
        Symmetry010Date date = CHRONOLOGY.dateNow(clock);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_WithNullZone_ThrowsException() {
        CHRONOLOGY.dateNow((ZoneId) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_WithNullClock_ThrowsException() {
        CHRONOLOGY.dateNow((Clock) null);
    }

    @Test(expected = DateTimeException.class)
    public void testDateNow_WithExtremeClockOffset_ThrowsException() {
        Clock baseClock = MockClock.systemUTC();
        Duration extremeOffset = Duration.ofDays(365250000L);
        Clock offsetClock = MockClock.offset(baseClock, extremeOffset);
        CHRONOLOGY.dateNow(offsetClock);
    }

    @Test(expected = ArithmeticException.class)
    public void testDateNow_WithForeverDurationOffset_ThrowsArithmeticException() {
        Clock baseClock = MockClock.systemDefaultZone();
        Duration foreverDuration = ChronoUnit.FOREVER.getDuration();
        Clock offsetClock = MockClock.offset(baseClock, foreverDuration);
        CHRONOLOGY.dateNow(offsetClock);
    }

    // ========== Date from Temporal Accessor Tests ==========

    @Test
    public void testDate_WithValidTemporalAccessor_CreatesDate() {
        CopticDate copticDate = CopticDate.ofEpochDay(-4481L);
        Symmetry010Date date = CHRONOLOGY.date(copticDate);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = NullPointerException.class)
    public void testDate_WithNullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.date((TemporalAccessor) null);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testDate_WithUnsupportedTemporalType_ThrowsException() {
        Year year = MockYear.now();
        CHRONOLOGY.date(year);
    }

    @Test(expected = DateTimeException.class)
    public void testDate_WithInvalidEpochDayInTemporal_ThrowsException() {
        PaxDate paxDate = PaxDate.ofEpochDay(365242134L);
        CHRONOLOGY.date(paxDate);
    }

    // ========== Proleptic Year Tests ==========

    @Test
    public void testProlepticYear_WithBCEEra_ReturnsCorrectYear() {
        IsoEra era = IsoEra.BCE;
        int year = CHRONOLOGY.prolepticYear(era, 0);
        assertEquals(0, year);
    }

    @Test
    public void testProlepticYear_WithCEEra_ReturnsCorrectYear() {
        IsoEra era = IsoEra.CE;
        int year = CHRONOLOGY.prolepticYear(era, 3994);
        assertEquals(3994, year);
    }

    @Test
    public void testProlepticYear_WithCEEraAndNegativeYear_ReturnsCorrectYear() {
        IsoEra era = IsoEra.CE;
        int year = CHRONOLOGY.prolepticYear(era, -537);
        assertEquals(-537, year);
    }

    @Test(expected = DateTimeException.class)
    public void testProlepticYear_WithInvalidYearOfEra_ThrowsException() {
        IsoEra era = IsoEra.CE;
        CHRONOLOGY.prolepticYear(era, -2136181101);
    }

    @Test(expected = ClassCastException.class)
    public void testProlepticYear_WithInvalidEra_ThrowsClassCastException() {
        JapaneseEra invalidEra = JapaneseEra.MEIJI;
        CHRONOLOGY.prolepticYear(invalidEra, -2097542166);
    }

    // ========== Local Date Time Tests ==========

    @Test
    public void testLocalDateTime_WithValidTemporalAccessor_CreatesLocalDateTime() {
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoLocalDateTime<Symmetry010Date> localDateTime = CHRONOLOGY.localDateTime(offsetDateTime);
        assertNotNull(localDateTime);
    }

    @Test(expected = DateTimeException.class)
    public void testLocalDateTime_WithInvalidTemporalAccessor_ThrowsException() {
        LocalDate localDate = MockLocalDate.now();
        CHRONOLOGY.localDateTime(localDate);
    }

    @Test(expected = NullPointerException.class)
    public void testLocalDateTime_WithNullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.localDateTime((TemporalAccessor) null);
    }

    // ========== Zoned Date Time Tests ==========

    @Test
    public void testZonedDateTime_WithValidTemporalAccessor_CreatesZonedDateTime() {
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<Symmetry010Date> zonedDateTime = CHRONOLOGY.zonedDateTime(offsetDateTime);
        assertNotNull(zonedDateTime);
    }

    @Test
    public void testZonedDateTime_WithInstantAndZone_CreatesZonedDateTime() {
        Instant instant = MockInstant.now();
        ZoneOffset zone = ZoneOffset.UTC;
        ChronoZonedDateTime<Symmetry010Date> zonedDateTime = CHRONOLOGY.zonedDateTime(instant, zone);
        assertNotNull(zonedDateTime);
    }

    @Test(expected = DateTimeException.class)
    public void testZonedDateTime_WithInvalidTemporalAccessor_ThrowsException() {
        EthiopicDate ethiopicDate = EthiopicDate.now();
        CHRONOLOGY.zonedDateTime(ethiopicDate);
    }

    @Test(expected = NullPointerException.class)
    public void testZonedDateTime_WithNullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.zonedDateTime((TemporalAccessor) null);
    }

    @Test(expected = NullPointerException.class)
    public void testZonedDateTime_WithNullInstant_ThrowsException() {
        ZoneId zone = ZoneId.systemDefault();
        CHRONOLOGY.zonedDateTime((Instant) null, zone);
    }

    // ========== Range Tests ==========

    @Test
    public void testRange_WithMicroOfSecond_ReturnsValidRange() {
        ChronoField field = ChronoField.MICRO_OF_SECOND;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithYear_ReturnsValidRange() {
        ChronoField field = ChronoField.YEAR;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithYearOfEra_ReturnsValidRange() {
        ChronoField field = ChronoField.YEAR_OF_ERA;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithProlepticMonth_ReturnsValidRange() {
        ChronoField field = ChronoField.PROLEPTIC_MONTH;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithMonthOfYear_ReturnsValidRange() {
        ChronoField field = ChronoField.MONTH_OF_YEAR;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithEpochDay_ReturnsValidRange() {
        ChronoField field = ChronoField.EPOCH_DAY;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithDayOfYear_ReturnsValidRange() {
        ChronoField field = ChronoField.DAY_OF_YEAR;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithDayOfMonth_ReturnsValidRange() {
        ChronoField field = ChronoField.DAY_OF_MONTH;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithAlignedWeekOfYear_ReturnsValidRange() {
        ChronoField field = ChronoField.ALIGNED_WEEK_OF_YEAR;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithAlignedWeekOfMonth_ReturnsValidRange() {
        ChronoField field = ChronoField.ALIGNED_WEEK_OF_MONTH;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithDayOfWeek_ReturnsValidRange() {
        ChronoField field = ChronoField.DAY_OF_WEEK;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithAlignedDayOfWeekInMonth_ReturnsValidRange() {
        ChronoField field = ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithAlignedDayOfWeekInYear_ReturnsValidRange() {
        ChronoField field = ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test
    public void testRange_WithEra_ReturnsValidRange() {
        ChronoField field = ChronoField.ERA;
        ValueRange range = CHRONOLOGY.range(field);
        assertNotNull(range);
    }

    @Test(expected = NullPointerException.class)
    public void testRange_WithNullField_ThrowsException() {
        CHRONOLOGY.range((ChronoField) null);
    }
}