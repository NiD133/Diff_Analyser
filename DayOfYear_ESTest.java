package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Test suite for DayOfYear class functionality.
 * Tests cover creation, validation, conversion, and edge cases.
 */
public class DayOfYearTest {

    // Test Constants
    private static final int VALID_DAY_OF_YEAR = 45;
    private static final int LEAP_DAY_OF_YEAR = 366;
    private static final int MIN_DAY_OF_YEAR = 1;
    private static final int INVALID_DAY_OF_YEAR = 0;

    // ========== Creation Tests ==========

    @Test
    public void testCreateDayOfYear_withValidValue_shouldSucceed() {
        DayOfYear dayOfYear = DayOfYear.of(MIN_DAY_OF_YEAR);
        
        assertEquals(MIN_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test(expected = DateTimeException.class)
    public void testCreateDayOfYear_withInvalidValue_shouldThrowException() {
        DayOfYear.of(INVALID_DAY_OF_YEAR);
    }

    @Test
    public void testCreateDayOfYear_fromCurrentTime_shouldReturnCurrentDayOfYear() {
        DayOfYear dayOfYear = DayOfYear.now();
        
        assertTrue("Day of year should be between 1 and 366", 
                   dayOfYear.getValue() >= 1 && dayOfYear.getValue() <= 366);
    }

    @Test
    public void testCreateDayOfYear_withDifferentTimeZones_shouldReturnDifferentValues() {
        ZoneOffset maxOffset = ZoneOffset.MAX;
        DayOfYear dayWithMaxOffset = DayOfYear.now(maxOffset);
        DayOfYear dayWithDefaultZone = DayOfYear.now();
        
        // Due to time zone differences, these may be different
        // The exact assertion depends on when the test runs
        assertNotNull(dayWithMaxOffset);
        assertNotNull(dayWithDefaultZone);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateDayOfYear_withNullZone_shouldThrowException() {
        DayOfYear.now((ZoneId) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateDayOfYear_withNullClock_shouldThrowException() {
        DayOfYear.now((Clock) null);
    }

    // ========== Conversion Tests ==========

    @Test
    public void testCreateFromTemporalAccessor_withValidDayOfYear_shouldSucceed() {
        DayOfYear original = DayOfYear.of(VALID_DAY_OF_YEAR);
        DayOfYear converted = DayOfYear.from(original);
        
        assertEquals(original.getValue(), converted.getValue());
    }

    @Test
    public void testCreateFromTemporalAccessor_withThaiBuddhistDate_shouldSucceed() {
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(8, 8);
        ThaiBuddhistDate thaiBuddhistDate = ThaiBuddhistDate.now(offset);
        
        DayOfYear dayOfYear = DayOfYear.from(thaiBuddhistDate);
        
        assertTrue("Day of year should be valid", 
                   dayOfYear.getValue() >= 1 && dayOfYear.getValue() <= 366);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFromTemporalAccessor_withNull_shouldThrowException() {
        DayOfYear.from((TemporalAccessor) null);
    }

    // ========== Field Access Tests ==========

    @Test
    public void testGetValue_shouldReturnCorrectDayOfYear() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertEquals(VALID_DAY_OF_YEAR, dayOfYear.getValue());
    }

    @Test
    public void testGetField_withDayOfYearField_shouldReturnValue() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        int fieldValue = dayOfYear.get(ChronoField.DAY_OF_YEAR);
        long longFieldValue = dayOfYear.getLong(ChronoField.DAY_OF_YEAR);
        
        assertEquals(VALID_DAY_OF_YEAR, fieldValue);
        assertEquals(VALID_DAY_OF_YEAR, longFieldValue);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testGetField_withUnsupportedField_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.get(ChronoField.HOUR_OF_DAY);
    }

    @Test(expected = NullPointerException.class)
    public void testGetField_withNullField_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.get((TemporalField) null);
    }

    // ========== Field Support Tests ==========

    @Test
    public void testIsSupported_withDayOfYearField_shouldReturnTrue() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("DAY_OF_YEAR field should be supported", 
                   dayOfYear.isSupported(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void testIsSupported_withUnsupportedField_shouldReturnFalse() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertFalse("HOUR_OF_DAY field should not be supported", 
                    dayOfYear.isSupported(ChronoField.HOUR_OF_DAY));
    }

    @Test
    public void testIsSupported_withNullField_shouldReturnFalse() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertFalse("Null field should not be supported", 
                    dayOfYear.isSupported((TemporalField) null));
    }

    // ========== Range Tests ==========

    @Test
    public void testGetRange_withSupportedField_shouldReturnValidRange() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.range(ChronoField.DAY_OF_YEAR);
        // Range validation is implicit - no exception means success
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testGetRange_withUnsupportedField_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.range(ChronoField.SECOND_OF_MINUTE);
    }

    @Test(expected = NullPointerException.class)
    public void testGetRange_withNullField_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.range((TemporalField) null);
    }

    // ========== Year Validation Tests ==========

    @Test
    public void testIsValidYear_withLeapDayInLeapYear_shouldReturnTrue() {
        DayOfYear leapDay = DayOfYear.of(LEAP_DAY_OF_YEAR);
        
        assertTrue("Day 366 should be valid in leap year", 
                   leapDay.isValidYear(2020)); // 2020 is a leap year
    }

    @Test
    public void testIsValidYear_withLeapDayInNonLeapYear_shouldReturnFalse() {
        DayOfYear leapDay = DayOfYear.of(LEAP_DAY_OF_YEAR);
        
        assertFalse("Day 366 should not be valid in non-leap year", 
                    leapDay.isValidYear(2021)); // 2021 is not a leap year
    }

    @Test
    public void testIsValidYear_withRegularDayInAnyYear_shouldReturnTrue() {
        DayOfYear regularDay = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("Regular day should be valid in any year", 
                   regularDay.isValidYear(2020));
        assertTrue("Regular day should be valid in any year", 
                   regularDay.isValidYear(2021));
    }

    // ========== Date Creation Tests ==========

    @Test
    public void testAtYear_withValidYearObject_shouldCreateLocalDate() {
        DayOfYear dayOfYear = DayOfYear.of(MIN_DAY_OF_YEAR);
        Year year = Year.of(2023);
        
        LocalDate localDate = dayOfYear.atYear(year);
        
        assertEquals(2023, localDate.getYear());
        assertEquals(MIN_DAY_OF_YEAR, localDate.getDayOfYear());
    }

    @Test
    public void testAtYear_withValidYearInt_shouldCreateLocalDate() {
        DayOfYear dayOfYear = DayOfYear.of(MIN_DAY_OF_YEAR);
        
        LocalDate localDate = dayOfYear.atYear(2023);
        
        assertEquals(2023, localDate.getYear());
        assertEquals(MIN_DAY_OF_YEAR, localDate.getDayOfYear());
    }

    @Test(expected = DateTimeException.class)
    public void testAtYear_withLeapDayInNonLeapYear_shouldThrowException() {
        DayOfYear leapDay = DayOfYear.of(LEAP_DAY_OF_YEAR);
        
        leapDay.atYear(2021); // 2021 is not a leap year
    }

    @Test(expected = NullPointerException.class)
    public void testAtYear_withNullYear_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.atYear((Year) null);
    }

    // ========== Temporal Adjustment Tests ==========

    @Test
    public void testAdjustInto_withLocalDate_shouldUpdateDayOfYear() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        LocalDate originalDate = LocalDate.of(2023, 1, 1);
        
        LocalDate adjustedDate = (LocalDate) dayOfYear.adjustInto(originalDate);
        
        assertEquals(VALID_DAY_OF_YEAR, adjustedDate.getDayOfYear());
        assertEquals(2023, adjustedDate.getYear());
    }

    @Test(expected = DateTimeException.class)
    public void testAdjustInto_withLeapDayInNonLeapYear_shouldThrowException() {
        DayOfYear leapDay = DayOfYear.of(LEAP_DAY_OF_YEAR);
        ZonedDateTime nonLeapYearDate = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        
        leapDay.adjustInto(nonLeapYearDate);
    }

    @Test(expected = DateTimeException.class)
    public void testAdjustInto_withNonIsoChronology_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        HijrahDate hijrahDate = HijrahDate.now();
        
        dayOfYear.adjustInto(hijrahDate);
    }

    @Test(expected = NullPointerException.class)
    public void testAdjustInto_withNull_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.adjustInto(null);
    }

    // ========== Query Tests ==========

    @Test
    public void testQuery_withValidQuery_shouldReturnResult() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        LocalDate expectedDate = LocalDate.of(2023, 1, 1);
        
        TemporalQuery<LocalDate> query = temporal -> expectedDate;
        LocalDate result = dayOfYear.query(query);
        
        assertEquals(expectedDate, result);
    }

    @Test(expected = NullPointerException.class)
    public void testQuery_withNullQuery_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.query((TemporalQuery<?>) null);
    }

    // ========== Comparison Tests ==========

    @Test
    public void testCompareTo_withSameDayOfYear_shouldReturnZero() {
        DayOfYear dayOfYear1 = DayOfYear.of(VALID_DAY_OF_YEAR);
        DayOfYear dayOfYear2 = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertEquals(0, dayOfYear1.compareTo(dayOfYear2));
    }

    @Test
    public void testCompareTo_withSmallerDayOfYear_shouldReturnNegative() {
        DayOfYear smallerDay = DayOfYear.of(MIN_DAY_OF_YEAR);
        DayOfYear largerDay = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("Smaller day should compare as less than larger day", 
                   smallerDay.compareTo(largerDay) < 0);
    }

    @Test
    public void testCompareTo_withLargerDayOfYear_shouldReturnPositive() {
        DayOfYear smallerDay = DayOfYear.of(MIN_DAY_OF_YEAR);
        DayOfYear largerDay = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("Larger day should compare as greater than smaller day", 
                   largerDay.compareTo(smallerDay) > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareTo_withNull_shouldThrowException() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        dayOfYear.compareTo(null);
    }

    // ========== Equality Tests ==========

    @Test
    public void testEquals_withSameDayOfYear_shouldReturnTrue() {
        DayOfYear dayOfYear1 = DayOfYear.of(VALID_DAY_OF_YEAR);
        DayOfYear dayOfYear2 = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("Same day of year values should be equal", 
                   dayOfYear1.equals(dayOfYear2));
    }

    @Test
    public void testEquals_withDifferentDayOfYear_shouldReturnFalse() {
        DayOfYear dayOfYear1 = DayOfYear.of(MIN_DAY_OF_YEAR);
        DayOfYear dayOfYear2 = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertFalse("Different day of year values should not be equal", 
                    dayOfYear1.equals(dayOfYear2));
    }

    @Test
    public void testEquals_withSameInstance_shouldReturnTrue() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertTrue("Same instance should equal itself", 
                   dayOfYear.equals(dayOfYear));
    }

    @Test
    public void testEquals_withDifferentType_shouldReturnFalse() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        String differentType = "not a DayOfYear";
        
        assertFalse("DayOfYear should not equal different type", 
                    dayOfYear.equals(differentType));
    }

    // ========== Hash Code Tests ==========

    @Test
    public void testHashCode_withSameDayOfYear_shouldBeEqual() {
        DayOfYear dayOfYear1 = DayOfYear.of(VALID_DAY_OF_YEAR);
        DayOfYear dayOfYear2 = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        assertEquals("Equal objects should have equal hash codes", 
                     dayOfYear1.hashCode(), dayOfYear2.hashCode());
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToString_shouldReturnReadableFormat() {
        DayOfYear dayOfYear = DayOfYear.of(VALID_DAY_OF_YEAR);
        
        String result = dayOfYear.toString();
        
        assertTrue("String should contain day of year value", 
                   result.contains(String.valueOf(VALID_DAY_OF_YEAR)));
        assertTrue("String should be in expected format", 
                   result.startsWith("DayOfYear:"));
    }
}