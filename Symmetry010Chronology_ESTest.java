package org.threeten.extra.chrono;

import static org.junit.Assert.*;
import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class Symmetry010Chronology_ESTest extends Symmetry010Chronology_ESTest_scaffolding {
    private static final Symmetry010Chronology CHRONOLOGY = Symmetry010Chronology.INSTANCE;

    // ========================================================================
    // Date Creation Tests
    // ========================================================================
    
    @Test(expected = DateTimeException.class)
    public void date_throwsExceptionWhenMonthInvalid() {
        Symmetry010Date date = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = date.getEra();
        CHRONOLOGY.date(era, 364, -2552, 1769);
    }

    @Test(expected = ClassCastException.class)
    public void date_throwsExceptionWhenEraInvalid() {
        JulianDate julianDate = JulianDate.ofEpochDay(-604L);
        JulianEra julianEra = julianDate.getEra();
        CHRONOLOGY.date(julianEra, -793, 1231, -3218);
    }

    @Test
    public void date_prolepticYearWithBCE() {
        IsoEra bce = IsoEra.BCE;
        int year = CHRONOLOGY.prolepticYear(bce, 0);
        assertEquals(0, year);
    }

    @Test
    public void date_prolepticYearWithCE() {
        Clock clock = MockClock.systemDefaultZone();
        Symmetry010Date date = CHRONOLOGY.dateNow(clock);
        IsoEra era = date.getEra();
        int year = CHRONOLOGY.prolepticYear(era, 3994);
        assertEquals(3994, year);
    }

    @Test
    public void date_prolepticYearWithCENegativeYear() {
        IsoEra ce = IsoEra.CE;
        int year = CHRONOLOGY.prolepticYear(ce, -537);
        assertEquals(-537, year);
    }

    @Test
    public void date_localDateTimeFromOffsetDateTime() {
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoLocalDateTime<Symmetry010Date> result = CHRONOLOGY.localDateTime(offsetDateTime);
        assertNotNull(result);
    }

    // ========================================================================
    // Leap Year Calculations
    // ========================================================================
    
    @Test
    public void leapYear_calculatesForNegativeYear() {
        assertTrue(CHRONOLOGY.isLeapYear(-1547L));
    }

    @Test
    public void leapYearsBefore_zeroForYearZero() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(0L);
        assertEquals(0L, leapYears);
    }

    @Test
    public void leapYearsBefore_calculatesForPositiveYear() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(560L);
        assertEquals(99L, leapYears);
    }

    @Test
    public void leapYearsBefore_calculatesForNegativeYear() {
        long leapYears = Symmetry010Chronology.getLeapYearsBefore(-2552);
        assertEquals(-453L, leapYears);
    }

    // ========================================================================
    // Chronology Metadata Tests
    // ========================================================================
    
    @Test
    public void getId_returnsCorrectValue() {
        assertEquals("Sym010", CHRONOLOGY.getId());
    }

    @Test
    public void eraOf_returnsBCEForValue0() {
        Era era = CHRONOLOGY.eraOf(0);
        assertEquals(IsoEra.BCE, era);
    }

    @Test
    public void getCalendarType_returnsNull() {
        assertNull(CHRONOLOGY.getCalendarType());
    }

    @Test
    public void eras_returnsListOfSize2() {
        List<Era> eras = CHRONOLOGY.eras();
        assertEquals(2, eras.size());
    }

    // ========================================================================
    // Date Conversion Tests
    // ========================================================================
    
    @Test
    public void dateYearDay_withValidEra() {
        Symmetry010Date date = Symmetry010Date.ofYearDay(30, 14);
        IsoEra era = date.getEra();
        Symmetry010Date result = CHRONOLOGY.dateYearDay(era, 14, 30);
        assertEquals(IsoEra.CE, result.getEra());
    }

    @Test
    public void date_fromCopticDate() {
        CopticDate copticDate = CopticDate.ofEpochDay(-4481L);
        Symmetry010Date date = CHRONOLOGY.date(copticDate);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void date_withValidParams() {
        Symmetry010Date date = CHRONOLOGY.date(12, 12, 12);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void zonedDateTime_fromEthiopicDateThrowsException() {
        EthiopicDate ethiopicDate = EthiopicDate.now();
        CHRONOLOGY.zonedDateTime(ethiopicDate);
    }

    @Test(expected = NullPointerException.class)
    public void zonedDateTime_withNullTemporalThrowsException() {
        CHRONOLOGY.zonedDateTime((TemporalAccessor) null);
    }

    @Test(expected = NullPointerException.class)
    public void zonedDateTime_withNullInstantThrowsException() {
        CHRONOLOGY.zonedDateTime(null, ZoneId.systemDefault());
    }

    // ========================================================================
    // Exception Handling Tests
    // ========================================================================
    
    @Test(expected = NullPointerException.class)
    public void range_withNullChronoFieldThrowsException() {
        CHRONOLOGY.range(null);
    }

    @Test(expected = DateTimeException.class)
    public void localDateTime_fromLocalDateThrowsException() {
        LocalDate localDate = MockLocalDate.now();
        CHRONOLOGY.localDateTime(localDate);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_throwsWhenDayOfYearInvalid() {
        IsoEra ce = IsoEra.CE;
        CHRONOLOGY.dateYearDay(ce, 1223, 1223);
    }

    @Test(expected = ClassCastException.class)
    public void dateYearDay_throwsWhenEraInvalid() {
        ThaiBuddhistEra era = ThaiBuddhistEra.BEFORE_BE;
        CHRONOLOGY.dateYearDay(era, 107016, 107016);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_throwsWhenDayOfYearInvalidWithoutEra() {
        CHRONOLOGY.dateYearDay(999, 999);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullZoneThrowsException() {
        CHRONOLOGY.dateNow((ZoneId) null);
    }

    @Test(expected = DateTimeException.class)
    public void dateNow_withLargeClockOffsetThrowsException() {
        Clock clock = MockClock.systemUTC();
        Duration duration = Duration.ofDays(365250000L);
        Clock offsetClock = MockClock.offset(clock, duration);
        CHRONOLOGY.dateNow(offsetClock);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClockThrowsException() {
        CHRONOLOGY.dateNow((Clock) null);
    }

    @Test(expected = ArithmeticException.class)
    public void dateNow_withForeverDurationThrowsException() {
        Clock clock = MockClock.systemDefaultZone();
        Duration duration = ChronoUnit.FOREVER.getDuration();
        Clock offsetClock = MockClock.offset(clock, duration);
        CHRONOLOGY.dateNow(offsetClock);
    }

    @Test(expected = DateTimeException.class)
    public void dateEpochDay_throwsWhenValueTooLarge() {
        CHRONOLOGY.dateEpochDay(365250000L);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void date_fromYearThrowsException() {
        Year year = MockYear.now();
        CHRONOLOGY.date(year);
    }

    @Test(expected = DateTimeException.class)
    public void date_fromPaxDateThrowsException() {
        PaxDate paxDate = PaxDate.ofEpochDay(365242134L);
        CHRONOLOGY.date(paxDate);
    }

    @Test(expected = DateTimeException.class)
    public void date_throwsWhenMonthInvalidWithoutEra() {
        CHRONOLOGY.date(13, 13, 13);
    }

    @Test(expected = DateTimeException.class)
    public void prolepticYear_throwsWhenYearOutOfRange() {
        IsoEra ce = IsoEra.CE;
        CHRONOLOGY.prolepticYear(ce, -2136181101);
    }

    @Test
    public void isLeapYear_returnsFalseForYear32() {
        assertFalse(CHRONOLOGY.isLeapYear(32L));
    }

    @Test(expected = NullPointerException.class)
    public void date_throwsWhenTemporalNull() {
        CHRONOLOGY.date((TemporalAccessor) null);
    }

    @Test(expected = NullPointerException.class)
    public void localDateTime_throwsWhenTemporalNull() {
        CHRONOLOGY.localDateTime((TemporalAccessor) null);
    }

    // ========================================================================
    // Value Range Tests
    // ========================================================================
    
    @Test
    public void zonedDateTime_withInstantAndZoneOffset() {
        Instant instant = MockInstant.now();
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        ChronoZonedDateTime<Symmetry010Date> result = CHRONOLOGY.zonedDateTime(instant, zoneOffset);
        assertNotNull(result);
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_throwsWhenEraInvalid() {
        JapaneseEra era = JapaneseEra.MEIJI;
        CHRONOLOGY.prolepticYear(era, -2097542166);
    }

    @Test
    public void range_forMicroOfSecond() {
        ValueRange range = CHRONOLOGY.range(ChronoField.MICRO_OF_SECOND);
        assertNotNull(range);
    }

    @Test
    public void range_forYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.YEAR);
        assertNotNull(range);
    }

    @Test
    public void range_forYearOfEra() {
        ValueRange range = CHRONOLOGY.range(ChronoField.YEAR_OF_ERA);
        assertNotNull(range);
    }

    @Test
    public void range_forProlepticMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.PROLEPTIC_MONTH);
        assertNotNull(range);
    }

    @Test
    public void range_forMonthOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.MONTH_OF_YEAR);
        assertNotNull(range);
    }

    @Test
    public void range_forEpochDay() {
        ValueRange range = CHRONOLOGY.range(ChronoField.EPOCH_DAY);
        assertNotNull(range);
    }

    @Test
    public void range_forDayOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_YEAR);
        assertNotNull(range);
    }

    @Test
    public void range_forDayOfMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_MONTH);
        assertNotNull(range);
    }

    @Test
    public void range_forAlignedWeekOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_WEEK_OF_YEAR);
        assertNotNull(range);
    }

    @Test
    public void range_forAlignedWeekOfMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_WEEK_OF_MONTH);
        assertNotNull(range);
    }

    @Test
    public void range_forDayOfWeek() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_WEEK);
        assertNotNull(range);
    }

    @Test
    public void range_forAlignedDayOfWeekInMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        assertNotNull(range);
    }

    @Test
    public void range_forEra() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ERA);
        assertNotNull(range);
    }

    @Test
    public void range_forAlignedDayOfWeekInYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);
        assertNotNull(range);
    }

    // ========================================================================
    // Special Case Tests
    // ========================================================================
    
    @Test
    public void dateYearDay_withNegativeYear() {
        Symmetry010Date date = CHRONOLOGY.dateYearDay(-762, 4);
        assertEquals(IsoEra.BCE, date.getEra());
    }

    @Test
    public void date_withValidEra() {
        IsoEra ce = IsoEra.CE;
        Symmetry010Date date = CHRONOLOGY.date(ce, 10, 10, 10);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_throwsWhenDayInvalidForNonLeapYear() {
        IsoEra ce = IsoEra.CE;
        CHRONOLOGY.dateYearDay(ce, 369, 369);
    }

    @Test(expected = DateTimeException.class)
    public void date_throwsWhenDayOfMonthInvalid() {
        CHRONOLOGY.date(5, 5, 35);
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_throwsWhenInvalidValue() {
        CHRONOLOGY.eraOf(-2135875627);
    }

    @Test
    public void dateNow_withZoneOffset() {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Symmetry010Date date = CHRONOLOGY.dateNow(zoneOffset);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void dateEpochDay_withValidValue() {
        Symmetry010Date date = CHRONOLOGY.dateEpochDay(719162L);
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void dateNow_default() {
        Symmetry010Date date = CHRONOLOGY.dateNow();
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test
    public void zonedDateTime_fromOffsetDateTime() {
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<Symmetry010Date> result = CHRONOLOGY.zonedDateTime(offsetDateTime);
        assertNotNull(result);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_throwsWhenDayInvalidForNonLeapYearWithoutEra() {
        CHRONOLOGY.dateYearDay(371, 371);
    }
}