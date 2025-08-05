package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;

/**
 * Test suite for InternationalFixedChronology functionality.
 * Tests the International Fixed Calendar system implementation.
 */
public class InternationalFixedChronologyTest {

    private static final InternationalFixedChronology CHRONOLOGY = InternationalFixedChronology.INSTANCE;

    // ========== Basic Properties Tests ==========

    @Test
    public void testGetId_ReturnsCorrectIdentifier() {
        assertEquals("Ifc", CHRONOLOGY.getId());
    }

    @Test
    public void testGetCalendarType_ReturnsNull() {
        assertNull(CHRONOLOGY.getCalendarType());
    }

    @Test
    public void testEras_ReturnsOnlyCommonEra() {
        List<Era> eras = CHRONOLOGY.eras();
        assertEquals(1, eras.size());
        assertEquals(InternationalFixedEra.CE, eras.get(0));
    }

    @Test
    public void testEraOf_ValidValue_ReturnsCommonEra() {
        InternationalFixedEra era = CHRONOLOGY.eraOf(1);
        assertEquals(InternationalFixedEra.CE, era);
    }

    @Test(expected = DateTimeException.class)
    public void testEraOf_InvalidValue_ThrowsException() {
        CHRONOLOGY.eraOf(-2095105997);
    }

    // ========== Leap Year Tests ==========

    @Test
    public void testIsLeapYear_RegularLeapYear_ReturnsTrue() {
        assertTrue("Year 4 should be a leap year", CHRONOLOGY.isLeapYear(4L));
        assertTrue("Year -3708 should be a leap year", CHRONOLOGY.isLeapYear(-3708L));
        assertTrue("Year 365000000 should be a leap year", CHRONOLOGY.isLeapYear(365000000L));
    }

    @Test
    public void testIsLeapYear_NonLeapYear_ReturnsFalse() {
        assertFalse("Year 100 should not be a leap year", CHRONOLOGY.isLeapYear(100L));
        assertFalse("Year 241 should not be a leap year", CHRONOLOGY.isLeapYear(241L));
        assertFalse("Year -1145400 should not be a leap year", CHRONOLOGY.isLeapYear(-1145400L));
    }

    // ========== Date Creation Tests ==========

    @Test
    public void testDate_ValidProlepticYear_CreatesCorrectDate() {
        InternationalFixedDate date = CHRONOLOGY.date(2, 2, 13);
        assertEquals(-718757L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void testDate_WithEra_CreatesCorrectDate() {
        InternationalFixedEra era = InternationalFixedEra.CE;
        InternationalFixedDate date = CHRONOLOGY.date(era, 7, 7, 7);
        assertEquals(-716797L, date.toEpochDay());
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidMonth_ThrowsException() {
        CHRONOLOGY.date(36526, 36526, 36526); // Invalid month value
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidDayOfMonth_ThrowsException() {
        InternationalFixedEra era = InternationalFixedEra.of(1);
        CHRONOLOGY.date(era, 6, 1, -347); // Invalid day value
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidDateCombination_ThrowsException() {
        CHRONOLOGY.date(4, 4, 29); // Invalid date combination
    }

    // ========== Date Year Day Tests ==========

    @Test
    public void testDateYearDay_ValidValues_CreatesCorrectDate() {
        InternationalFixedDate date1 = CHRONOLOGY.dateYearDay(134, 134);
        assertEquals(-670452L, date1.toEpochDay());
        assertEquals(365, date1.lengthOfYear());

        InternationalFixedDate date2 = CHRONOLOGY.dateYearDay(4314, 7);
        assertEquals(856134L, date2.toEpochDay());
        assertEquals(365, date2.lengthOfYear());
    }

    @Test
    public void testDateYearDay_WithEra_CreatesCorrectDate() {
        InternationalFixedEra era = InternationalFixedEra.CE;
        InternationalFixedDate date = CHRONOLOGY.dateYearDay(era, 157, 3);
        assertEquals(-662182L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_InvalidDayOfYear_ThrowsException() {
        CHRONOLOGY.dateYearDay(2191, 2191); // Day of year too large
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_NonLeapYearWith366Days_ThrowsException() {
        CHRONOLOGY.dateYearDay(366, 366); // 366 is not a leap year
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_InvalidEra_ThrowsException() {
        JapaneseEra invalidEra = JapaneseEra.HEISEI;
        CHRONOLOGY.dateYearDay(invalidEra, 734, 734);
    }

    // ========== Epoch Day Tests ==========

    @Test
    public void testDateEpochDay_ValidValues_CreatesCorrectDate() {
        InternationalFixedDate date1 = CHRONOLOGY.dateEpochDay(0L);
        assertEquals(365, date1.lengthOfYear());

        InternationalFixedDate date2 = CHRONOLOGY.dateEpochDay(-2219L);
        assertEquals(365, date2.lengthOfYear());
        assertEquals(29, date2.lengthOfMonth());
    }

    @Test(expected = DateTimeException.class)
    public void testDateEpochDay_InvalidValue_ThrowsException() {
        CHRONOLOGY.dateEpochDay(-1145400L); // Value outside valid range
    }

    // ========== Current Date Tests ==========

    @Test
    public void testDateNow_SystemDefault_ReturnsValidDate() {
        InternationalFixedDate date = CHRONOLOGY.dateNow();
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void testDateNow_WithZone_ReturnsValidDate() {
        ZoneOffset utc = ZoneOffset.UTC;
        InternationalFixedDate date = CHRONOLOGY.dateNow(utc);
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void testDateNow_WithClock_ReturnsValidDate() {
        Clock clock = MockClock.systemDefaultZone();
        InternationalFixedDate date = CHRONOLOGY.dateNow(clock);
        assertEquals(365, date.lengthOfYear());
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_NullZone_ThrowsException() {
        CHRONOLOGY.dateNow((ZoneId) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_NullClock_ThrowsException() {
        CHRONOLOGY.dateNow((Clock) null);
    }

    // ========== Temporal Accessor Tests ==========

    @Test
    public void testDate_FromTemporalAccessor_CreatesCorrectDate() {
        InternationalFixedDate originalDate = CHRONOLOGY.dateEpochDay(146096L);
        InternationalFixedDate convertedDate = CHRONOLOGY.date(originalDate);
        assertEquals(365, convertedDate.lengthOfYear());
        assertEquals(29, convertedDate.lengthOfMonth());
    }

    @Test(expected = NullPointerException.class)
    public void testDate_NullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.date((TemporalAccessor) null);
    }

    // ========== DateTime Conversion Tests ==========

    @Test
    public void testLocalDateTime_ValidInput_ReturnsCorrectDateTime() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        LocalDateTime localDateTime = MockLocalDateTime.now(maxOffset);
        assertNotNull(CHRONOLOGY.localDateTime(localDateTime));
    }

    @Test(expected = NullPointerException.class)
    public void testLocalDateTime_NullInput_ThrowsException() {
        CHRONOLOGY.localDateTime(null);
    }

    @Test
    public void testZonedDateTime_ValidInput_ReturnsCorrectZonedDateTime() {
        ZonedDateTime zonedDateTime = MockZonedDateTime.now();
        assertNotNull(CHRONOLOGY.zonedDateTime(zonedDateTime));
    }

    @Test
    public void testZonedDateTime_FromInstant_ReturnsCorrectZonedDateTime() {
        Instant instant = MockInstant.ofEpochSecond(-1512L, 107016L);
        ZoneOffset maxOffset = ZoneOffset.MAX;
        assertNotNull(CHRONOLOGY.zonedDateTime(instant, maxOffset));
    }

    @Test(expected = NullPointerException.class)
    public void testZonedDateTime_NullInput_ThrowsException() {
        CHRONOLOGY.zonedDateTime((TemporalAccessor) null);
    }

    // ========== Proleptic Year Tests ==========

    @Test
    public void testProlepticYear_ValidEraAndYear_ReturnsCorrectYear() {
        InternationalFixedEra era = InternationalFixedEra.CE;
        int result = CHRONOLOGY.prolepticYear(era, 734);
        assertEquals(734, result);
    }

    @Test(expected = DateTimeException.class)
    public void testProlepticYear_InvalidYearOfEra_ThrowsException() {
        InternationalFixedEra era = InternationalFixedEra.CE;
        CHRONOLOGY.prolepticYear(era, -2073432486);
    }

    @Test(expected = ClassCastException.class)
    public void testProlepticYear_InvalidEra_ThrowsException() {
        IsoEra invalidEra = IsoEra.BCE;
        CHRONOLOGY.prolepticYear(invalidEra, -985);
    }

    // ========== Field Range Tests ==========

    @Test
    public void testRange_VariousFields_ReturnsValidRanges() {
        assertNotNull("YEAR range should not be null", 
                     CHRONOLOGY.range(ChronoField.YEAR));
        assertNotNull("YEAR_OF_ERA range should not be null", 
                     CHRONOLOGY.range(ChronoField.YEAR_OF_ERA));
        assertNotNull("MONTH_OF_YEAR range should not be null", 
                     CHRONOLOGY.range(ChronoField.MONTH_OF_YEAR));
        assertNotNull("DAY_OF_MONTH range should not be null", 
                     CHRONOLOGY.range(ChronoField.DAY_OF_MONTH));
        assertNotNull("DAY_OF_YEAR range should not be null", 
                     CHRONOLOGY.range(ChronoField.DAY_OF_YEAR));
        assertNotNull("ERA range should not be null", 
                     CHRONOLOGY.range(ChronoField.ERA));
        assertNotNull("EPOCH_DAY range should not be null", 
                     CHRONOLOGY.range(ChronoField.EPOCH_DAY));
    }

    @Test(expected = NullPointerException.class)
    public void testRange_NullField_ThrowsException() {
        CHRONOLOGY.range(null);
    }

    // ========== Leap Years Calculation Tests ==========

    @Test
    public void testGetLeapYearsBefore_VariousYears_ReturnsCorrectCounts() {
        assertEquals("No leap years before year 0", 
                    0L, InternationalFixedChronology.getLeapYearsBefore(0L));
        assertEquals("Correct leap years before large positive year", 
                    88571217L, InternationalFixedChronology.getLeapYearsBefore(365242134L));
        assertEquals("Correct leap years before negative year", 
                    -390L, InternationalFixedChronology.getLeapYearsBefore(-1610L));
    }
}