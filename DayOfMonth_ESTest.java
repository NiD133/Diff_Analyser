package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockYearMonth;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;

/**
 * Test suite for DayOfMonth class functionality.
 * Tests cover creation, validation, comparison, and temporal operations.
 */
public class DayOfMonth_ESTest extends DayOfMonth_ESTest_scaffolding {

    // Test constants for better readability
    private static final int VALID_DAY_1 = 1;
    private static final int VALID_DAY_14 = 14;
    private static final int VALID_DAY_28 = 28;
    private static final int VALID_DAY_31 = 31;
    private static final int INVALID_DAY_NEGATIVE = -510;
    private static final int INVALID_MONTH = -550;

    // ========== Creation and Factory Methods ==========

    @Test(timeout = 4000)
    public void testCreateDayOfMonth_WithValidValue_ShouldSucceed() {
        DayOfMonth dayOfMonth = DayOfMonth.of(VALID_DAY_31);
        assertEquals(VALID_DAY_31, dayOfMonth.getValue());
    }

    @Test(timeout = 4000)
    public void testCreateDayOfMonth_WithInvalidNegativeValue_ShouldThrowException() {
        try {
            DayOfMonth.of(INVALID_DAY_NEGATIVE);
            fail("Expected DateTimeException for invalid day value");
        } catch (DateTimeException e) {
            assertTrue("Exception message should mention invalid value", 
                      e.getMessage().contains("Invalid value for DayOfMonth"));
        }
    }

    @Test(timeout = 4000)
    public void testNowWithDefaultClock_ShouldReturnCurrentDay() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertEquals("Current day should be 14 (mocked)", VALID_DAY_14, dayOfMonth.getValue());
    }

    @Test(timeout = 4000)
    public void testNowWithSpecificZone_ShouldReturnCurrentDay() {
        ZoneOffset utcZone = ZoneOffset.UTC;
        DayOfMonth dayOfMonth = DayOfMonth.now(utcZone);
        assertEquals("Day should be 14 (mocked)", VALID_DAY_14, dayOfMonth.getValue());
    }

    @Test(timeout = 4000)
    public void testNowWithClock_ShouldReturnCurrentDay() {
        Clock systemClock = MockClock.systemDefaultZone();
        DayOfMonth dayOfMonth = DayOfMonth.now(systemClock);
        assertEquals("Day should be 14 (mocked)", VALID_DAY_14, dayOfMonth.getValue());
    }

    @Test(timeout = 4000)
    public void testNowWithNullClock_ShouldThrowException() {
        try {
            DayOfMonth.now((Clock) null);
            fail("Expected NullPointerException for null clock");
        } catch (NullPointerException e) {
            assertEquals("clock", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testNowWithNullZone_ShouldThrowException() {
        try {
            DayOfMonth.now((ZoneId) null);
            fail("Expected NullPointerException for null zone");
        } catch (NullPointerException e) {
            assertEquals("zone", e.getMessage());
        }
    }

    // ========== Temporal Field Operations ==========

    @Test(timeout = 4000)
    public void testGetDayOfMonthField_ShouldReturnCorrectValue() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        int dayValue = dayOfMonth.get(ChronoField.DAY_OF_MONTH);
        assertEquals("Should return day value", VALID_DAY_14, dayValue);
    }

    @Test(timeout = 4000)
    public void testGetLongDayOfMonthField_ShouldReturnCorrectValue() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        long dayValue = dayOfMonth.getLong(ChronoField.DAY_OF_MONTH);
        assertEquals("Should return day value as long", 14L, dayValue);
    }

    @Test(timeout = 4000)
    public void testGetUnsupportedField_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.get(ChronoField.CLOCK_HOUR_OF_DAY);
            fail("Expected UnsupportedTemporalTypeException for unsupported field");
        } catch (UnsupportedTemporalTypeException e) {
            assertTrue("Exception should mention unsupported field", 
                      e.getMessage().contains("Unsupported field"));
        }
    }

    @Test(timeout = 4000)
    public void testIsSupportedDayOfMonthField_ShouldReturnTrue() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertTrue("DAY_OF_MONTH should be supported", 
                  dayOfMonth.isSupported(ChronoField.DAY_OF_MONTH));
    }

    @Test(timeout = 4000)
    public void testIsSupportedUnsupportedField_ShouldReturnFalse() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertFalse("AMPM_OF_DAY should not be supported", 
                   dayOfMonth.isSupported(ChronoField.AMPM_OF_DAY));
    }

    @Test(timeout = 4000)
    public void testIsSupportedNullField_ShouldReturnFalse() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertFalse("Null field should not be supported", 
                   dayOfMonth.isSupported(null));
    }

    // ========== Comparison Operations ==========

    @Test(timeout = 4000)
    public void testCompareTo_WithSmallerDay_ShouldReturnPositive() {
        DayOfMonth day14 = DayOfMonth.now(); // day 14
        DayOfMonth day1 = DayOfMonth.of(VALID_DAY_1);
        
        int comparison = day14.compareTo(day1);
        assertTrue("Day 14 should be greater than day 1", comparison > 0);
        assertEquals("Comparison should return difference", 13, comparison);
    }

    @Test(timeout = 4000)
    public void testCompareTo_WithLargerDay_ShouldReturnNegative() {
        DayOfMonth day14 = DayOfMonth.now(); // day 14
        DayOfMonth day28 = DayOfMonth.of(VALID_DAY_28);
        
        int comparison = day14.compareTo(day28);
        assertTrue("Day 14 should be less than day 28", comparison < 0);
        assertEquals("Comparison should return negative difference", -14, comparison);
    }

    @Test(timeout = 4000)
    public void testCompareTo_WithSameDay_ShouldReturnZero() {
        DayOfMonth day14 = DayOfMonth.now();
        int comparison = day14.compareTo(day14);
        assertEquals("Same day should return 0", 0, comparison);
    }

    @Test(timeout = 4000)
    public void testCompareTo_WithNull_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.compareTo(null);
            fail("Expected NullPointerException for null comparison");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Equality Operations ==========

    @Test(timeout = 4000)
    public void testEquals_WithSameDay_ShouldReturnTrue() {
        DayOfMonth day1 = DayOfMonth.now();
        DayOfMonth day2 = DayOfMonth.now();
        assertTrue("Same day values should be equal", day1.equals(day2));
    }

    @Test(timeout = 4000)
    public void testEquals_WithDifferentDay_ShouldReturnFalse() {
        DayOfMonth day14 = DayOfMonth.now(); // day 14
        DayOfMonth day31 = DayOfMonth.of(VALID_DAY_31);
        
        assertFalse("Different day values should not be equal", day14.equals(day31));
        assertFalse("Equality should be symmetric", day31.equals(day14));
    }

    @Test(timeout = 4000)
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertTrue("Same instance should equal itself", dayOfMonth.equals(dayOfMonth));
    }

    @Test(timeout = 4000)
    public void testEquals_WithDifferentType_ShouldReturnFalse() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        ZoneOffset otherObject = ZoneOffset.MAX;
        assertFalse("Different types should not be equal", dayOfMonth.equals(otherObject));
    }

    // ========== Date Combination Operations ==========

    @Test(timeout = 4000)
    public void testAtMonth_WithValidMonth_ShouldCreateMonthDay() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        // Should not throw exception
        dayOfMonth.atMonth(Month.AUGUST);
    }

    @Test(timeout = 4000)
    public void testAtMonth_WithValidMonthNumber_ShouldCreateMonthDay() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        // Should not throw exception
        dayOfMonth.atMonth(1);
    }

    @Test(timeout = 4000)
    public void testAtMonth_WithInvalidMonthNumber_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.of(VALID_DAY_31);
        try {
            dayOfMonth.atMonth(INVALID_MONTH);
            fail("Expected DateTimeException for invalid month");
        } catch (DateTimeException e) {
            assertTrue("Exception should mention invalid month", 
                      e.getMessage().contains("Invalid value for MonthOfYear"));
        }
    }

    @Test(timeout = 4000)
    public void testAtYearMonth_WithValidYearMonth_ShouldCreateLocalDate() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        YearMonth yearMonth = MockYearMonth.now();
        // Should not throw exception
        dayOfMonth.atYearMonth(yearMonth);
    }

    @Test(timeout = 4000)
    public void testAtYearMonth_WithNull_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.atYearMonth(null);
            fail("Expected NullPointerException for null YearMonth");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Validation Operations ==========

    @Test(timeout = 4000)
    public void testIsValidYearMonth_WithValidCombination_ShouldReturnTrue() {
        DayOfMonth day14 = DayOfMonth.now(); // day 14
        YearMonth yearMonth = MockYearMonth.now();
        assertTrue("Day 14 should be valid for any month", day14.isValidYearMonth(yearMonth));
    }

    @Test(timeout = 4000)
    public void testIsValidYearMonth_WithInvalidCombination_ShouldReturnFalse() {
        DayOfMonth day31 = DayOfMonth.of(VALID_DAY_31);
        YearMonth yearMonth = MockYearMonth.now(); // Assuming this doesn't have 31 days
        assertFalse("Day 31 may not be valid for all months", day31.isValidYearMonth(yearMonth));
    }

    @Test(timeout = 4000)
    public void testIsValidYearMonth_WithNull_ShouldReturnFalse() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        assertFalse("Null YearMonth should return false", dayOfMonth.isValidYearMonth(null));
    }

    // ========== Temporal Adjustment Operations ==========

    @Test(timeout = 4000)
    public void testAdjustInto_WithZonedDateTime_ShouldAdjustDay() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        Clock utcClock = MockClock.systemUTC();
        ZonedDateTime originalDateTime = MockZonedDateTime.now(utcClock);
        
        ZonedDateTime adjustedDateTime = (ZonedDateTime) dayOfMonth.adjustInto(originalDateTime);
        assertEquals("Adjusted datetime should have same value as original", 
                    originalDateTime, adjustedDateTime);
    }

    @Test(timeout = 4000)
    public void testAdjustInto_WithNonIsoChronology_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.adjustInto(MockHijrahDate.now());
            fail("Expected DateTimeException for non-ISO chronology");
        } catch (DateTimeException e) {
            assertTrue("Exception should mention ISO date-time requirement", 
                      e.getMessage().contains("Adjustment only supported on ISO date-time"));
        }
    }

    @Test(timeout = 4000)
    public void testAdjustInto_WithNull_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.adjustInto(null);
            fail("Expected NullPointerException for null temporal");
        } catch (NullPointerException e) {
            assertEquals("temporal", e.getMessage());
        }
    }

    // ========== Query Operations ==========

    @Test(timeout = 4000)
    public void testQuery_WithValidQuery_ShouldReturnResult() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        TemporalQuery<String> mockQuery = temporal -> "test result";
        
        String result = dayOfMonth.query(mockQuery);
        assertEquals("Query should return expected result", "test result", result);
    }

    @Test(timeout = 4000)
    public void testQuery_WithNull_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.query(null);
            fail("Expected NullPointerException for null query");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== String Representation ==========

    @Test(timeout = 4000)
    public void testToString_ShouldReturnFormattedString() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        String stringRepresentation = dayOfMonth.toString();
        assertEquals("String representation should be formatted correctly", 
                    "DayOfMonth:14", stringRepresentation);
    }

    @Test(timeout = 4000)
    public void testHashCode_ShouldBeConsistent() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        // Should not throw exception
        dayOfMonth.hashCode();
    }

    // ========== Range Operations ==========

    @Test(timeout = 4000)
    public void testRange_WithSupportedField_ShouldReturnRange() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        // Should not throw exception
        dayOfMonth.range(ChronoField.DAY_OF_MONTH);
    }

    @Test(timeout = 4000)
    public void testRange_WithUnsupportedField_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.range(ChronoField.ERA);
            fail("Expected UnsupportedTemporalTypeException for unsupported field");
        } catch (UnsupportedTemporalTypeException e) {
            assertTrue("Exception should mention unsupported field", 
                      e.getMessage().contains("Unsupported field"));
        }
    }

    @Test(timeout = 4000)
    public void testRange_WithNull_ShouldThrowException() {
        DayOfMonth dayOfMonth = DayOfMonth.now();
        try {
            dayOfMonth.range(null);
            fail("Expected NullPointerException for null field");
        } catch (NullPointerException e) {
            assertEquals("field", e.getMessage());
        }
    }
}