package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.util.Date;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.format.*;
import org.junit.runner.RunWith;

/**
 * Test suite for AbstractPartial class.
 * Tests the base implementation of ReadablePartial interface methods.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class AbstractPartial_ESTest extends AbstractPartial_ESTest_scaffolding {

    // ========== Equality and Comparison Tests ==========

    @Test(timeout = 4000)
    public void testIsEqual_DifferentYearMonthValues_ReturnsFalse() {
        YearMonth original = new YearMonth();
        YearMonth modified = original.plusMonths(4989);
        
        boolean isEqual = modified.isEqual(original);
        
        assertFalse("Different YearMonth values should not be equal", isEqual);
        assertEquals(11, modified.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testIsEqual_SameValues_ReturnsTrue() {
        LocalDateTime localDateTime = LocalDateTime.now();
        
        boolean isEqual = localDateTime.isEqual(localDateTime);
        
        assertTrue("Same instance should be equal to itself", isEqual);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentPartialTypes_ReturnsFalse() {
        LocalDateTime localDateTime = new LocalDateTime();
        LocalDate localDate = new LocalDate(localDateTime);
        
        boolean equals = localDateTime.equals(localDate);
        
        assertFalse("Different partial types should not be equal", equals);
    }

    @Test(timeout = 4000)
    public void testEquals_NonPartialObject_ReturnsFalse() {
        YearMonth yearMonth = YearMonth.now();
        Locale locale = Locale.GERMANY;
        
        boolean equals = yearMonth.equals(locale);
        
        assertFalse("Partial should not equal non-partial object", equals);
    }

    @Test(timeout = 4000)
    public void testCompareTo_NullPartial_ThrowsNullPointerException() {
        YearMonth yearMonth = YearMonth.now();
        
        try {
            yearMonth.compareTo((ReadablePartial) null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCompareTo_LaterDate_ReturnsPositive() {
        YearMonth yearMonth = YearMonth.now();
        YearMonth laterYearMonth = yearMonth.minusYears(-2306);
        
        int comparison = laterYearMonth.compareTo((ReadablePartial) yearMonth);
        
        assertEquals(4320, laterYearMonth.getYear());
        assertEquals(1, comparison);
    }

    @Test(timeout = 4000)
    public void testCompareTo_EarlierDate_ReturnsNegative() {
        YearMonth yearMonth = new YearMonth(4, 4);
        YearMonth earlierYearMonth = yearMonth.minusYears(4);
        
        int comparison = earlierYearMonth.compareTo((ReadablePartial) yearMonth);
        
        assertEquals(-1, comparison);
        assertEquals(0, earlierYearMonth.getYear());
    }

    @Test(timeout = 4000)
    public void testCompareTo_IncompatibleFieldTypes_ThrowsClassCastException() {
        YearMonth yearMonth = new YearMonth();
        LocalTime localTime = LocalTime.now();
        
        try {
            yearMonth.compareTo((ReadablePartial) localTime);
            fail("Expected ClassCastException");
        } catch(ClassCastException e) {
            assertEquals("ReadablePartial objects must have matching field types", e.getMessage());
        }
    }

    // ========== Before/After Tests ==========

    @Test(timeout = 4000)
    public void testIsAfter_NullPartial_ThrowsIllegalArgumentException() {
        YearMonth yearMonth = YearMonth.now();
        
        try {
            yearMonth.isAfter((ReadablePartial) null);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Partial cannot be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIsAfter_LaterDate_ReturnsTrue() {
        LocalDateTime original = new LocalDateTime();
        LocalDateTime later = original.minusWeeks(-4607);
        
        boolean isAfter = later.isAfter(original);
        
        assertTrue("Later date should be after earlier date", isAfter);
    }

    @Test(timeout = 4000)
    public void testIsAfter_SameDate_ReturnsFalse() {
        LocalDateTime localDateTime = LocalDateTime.now();
        
        boolean isAfter = localDateTime.isAfter(localDateTime);
        
        assertFalse("Date should not be after itself", isAfter);
    }

    @Test(timeout = 4000)
    public void testIsBefore_LaterDate_ReturnsTrue() {
        MonthDay monthDay = new MonthDay();
        MonthDay nextDay = monthDay.plusDays(1);
        
        boolean isBefore = monthDay.isBefore(nextDay);
        
        assertTrue("Earlier date should be before later date", isBefore);
        assertEquals(2, nextDay.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testIsBefore_SameDate_ReturnsFalse() {
        YearMonth yearMonth = new YearMonth(4, 4);
        
        boolean isBefore = yearMonth.isBefore(yearMonth);
        
        assertFalse("Date should not be before itself", isBefore);
    }

    // ========== Field Access Tests ==========

    @Test(timeout = 4000)
    public void testGetFieldType_ValidIndex_ReturnsFieldType() {
        MonthDay monthDay = MonthDay.now();
        
        DateTimeFieldType fieldType = monthDay.getFieldType(1);
        
        assertEquals("dayOfMonth", fieldType.toString());
    }

    @Test(timeout = 4000)
    public void testGetFieldType_NegativeIndex_ThrowsIndexOutOfBoundsException() {
        LocalDate localDate = new LocalDate();
        
        try {
            localDate.getFieldType(-3285);
            fail("Expected IndexOutOfBoundsException");
        } catch(IndexOutOfBoundsException e) {
            assertEquals("Invalid index: -3285", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetField_ValidIndex_ReturnsField() {
        YearMonth yearMonth = YearMonth.now();
        
        DateTimeField field = yearMonth.getField(0);
        
        assertNotNull("Field should not be null", field);
    }

    @Test(timeout = 4000)
    public void testGet_SupportedFieldType_ReturnsValue() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        MonthDay monthDay = new MonthDay(1516L, chronology);
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        
        int value = monthDay.get(dayOfMonth);
        
        assertEquals(1, value);
    }

    @Test(timeout = 4000)
    public void testGet_UnsupportedFieldType_ThrowsIllegalArgumentException() {
        MonthDay monthDay = MonthDay.now();
        DateTimeFieldType clockhourOfDay = DateTimeFieldType.clockhourOfDay();
        
        try {
            monthDay.get(clockhourOfDay);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Field 'clockhourOfDay' is not supported", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testIsSupported_SupportedField_ReturnsTrue() {
        LocalDate localDate = new LocalDate();
        DateTimeFieldType yearOfEra = DateTimeFieldType.yearOfEra();
        
        boolean isSupported = localDate.isSupported(yearOfEra);
        
        assertTrue("yearOfEra should be supported by LocalDate", isSupported);
    }

    @Test(timeout = 4000)
    public void testIsSupported_NullField_ReturnsFalse() {
        YearMonth yearMonth = YearMonth.now();
        
        boolean isSupported = yearMonth.isSupported((DateTimeFieldType) null);
        
        assertFalse("Null field should not be supported", isSupported);
    }

    @Test(timeout = 4000)
    public void testIndexOf_ExistingField_ReturnsIndex() {
        YearMonth yearMonth = new YearMonth();
        DateTimeFieldType monthOfYear = DateTimeFieldType.monthOfYear();
        
        int index = yearMonth.indexOf(monthOfYear);
        
        assertEquals(1, index);
    }

    @Test(timeout = 4000)
    public void testIndexOf_NonExistingField_ReturnsNegativeOne() {
        YearMonth yearMonth = new YearMonth(4, 4);
        DateTimeFieldType era = DateTimeFieldType.era();
        
        int index = yearMonth.indexOf(era);
        
        assertEquals(-1, index);
    }

    // ========== Array Access Tests ==========

    @Test(timeout = 4000)
    public void testGetFields_EmptyPartial_ReturnsEmptyArray() {
        Partial partial = new Partial();
        
        DateTimeField[] fields = partial.getFields();
        
        assertEquals(0, fields.length);
    }

    @Test(timeout = 4000)
    public void testGetValues_LocalDateTime_ReturnsCorrectValues() {
        LocalDateTime localDateTime = LocalDateTime.now();
        
        int[] values = localDateTime.getValues();
        
        assertArrayEquals(new int[] {2014, 2, 14, 73281320}, values);
    }

    @Test(timeout = 4000)
    public void testGetFieldTypes_LocalDate_ReturnsThreeFieldTypes() {
        LocalDateTime localDateTime = new LocalDateTime();
        LocalDate localDate = new LocalDate(localDateTime);
        
        DateTimeFieldType[] fieldTypes = localDate.getFieldTypes();
        
        assertEquals(3, fieldTypes.length);
    }

    // ========== Conversion Tests ==========

    @Test(timeout = 4000)
    public void testToDateTime_WithInstant_ReturnsCorrectDateTime() {
        YearMonth yearMonth = YearMonth.now();
        Instant instant = Instant.now();
        
        DateTime dateTime = yearMonth.toDateTime(instant);
        
        assertEquals(1392409281320L, dateTime.getMillis());
    }

    @Test(timeout = 4000)
    public void testToDate_LocalDate_ReturnsCorrectDate() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(1L, chronology);
        
        Date date = localDate.toDate();
        
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", date.toString());
    }

    // ========== String Representation Tests ==========

    @Test(timeout = 4000)
    public void testToString_MonthDay_ReturnsFormattedString() {
        MonthDay monthDay = new MonthDay();
        
        String string = monthDay.toString();
        
        assertEquals("--02-14", string);
    }

    @Test(timeout = 4000)
    public void testToString_NullFormatter_UsesDefaultFormat() {
        YearMonth yearMonth = YearMonth.now();
        
        String string = yearMonth.toString((DateTimeFormatter) null);
        
        assertNotNull("String representation should not be null", string);
    }

    @Test(timeout = 4000)
    public void testToString_FormatterWithoutPrinter_ThrowsUnsupportedOperationException() {
        GJChronology chronology = GJChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(0L, chronology);
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        
        try {
            localDate.toString(formatter);
            fail("Expected UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            assertEquals("Printing not supported", e.getMessage());
        }
    }

    // ========== Arithmetic Operations Tests ==========

    @Test(timeout = 4000)
    public void testMinus_YearsPeriod_ReturnsModifiedYearMonth() {
        Years twoYears = Years.TWO;
        YearMonth yearMonth = new YearMonth(4, 4);
        
        YearMonth result = yearMonth.minus(twoYears);
        
        assertEquals(4, result.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testMinus_WeeksPeriod_NoChangeForMonthDay() {
        MonthDay monthDay = new MonthDay();
        Weeks twoWeeks = Weeks.TWO;
        
        MonthDay result = monthDay.minus(twoWeeks);
        
        assertTrue("MonthDay should not change when subtracting weeks", 
                   result.equals(monthDay));
    }

    @Test(timeout = 4000)
    public void testWithFieldAdded_UnsupportedField_ThrowsIllegalArgumentException() {
        YearMonth yearMonth = new YearMonth();
        DurationFieldType halfdays = DurationFieldType.halfdays();
        
        try {
            yearMonth.withFieldAdded(halfdays, 0);
            fail("Expected IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Field 'halfdays' is not supported", e.getMessage());
        }
    }

    // ========== Other Tests ==========

    @Test(timeout = 4000)
    public void testHashCode_LocalDate_ReturnsConsistentValue() {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(1L, chronology);
        
        // Just verify it doesn't throw an exception
        localDate.hashCode();
    }
}