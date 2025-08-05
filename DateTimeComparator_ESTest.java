package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

/**
 * Test suite for DateTimeComparator functionality.
 * Tests cover factory methods, comparison operations, equality, and string representation.
 */
public class DateTimeComparatorTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void getInstance_withNoParameters_shouldReturnComparatorForAllFields() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        
        assertNotNull(comparator);
        assertEquals("DateTimeComparator[]", comparator.toString());
        assertNull(comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
    }

    @Test
    public void getInstance_withLowerLimitOnly_shouldReturnComparatorWithCorrectLimits() {
        DateTimeFieldType yearOfEra = DateTimeFieldType.yearOfEra();
        
        DateTimeComparator comparator = DateTimeComparator.getInstance(yearOfEra);
        
        assertNotNull(comparator);
        assertEquals(yearOfEra, comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
    }

    @Test
    public void getInstance_withBothLimits_shouldReturnComparatorWithCorrectLimits() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        
        DateTimeComparator comparator = DateTimeComparator.getInstance(dayOfYear, null);
        
        assertNotNull(comparator);
        assertEquals(dayOfYear, comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
    }

    @Test
    public void getInstance_withNullLimits_shouldReturnValidComparator() {
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, null);
        
        assertNotNull(comparator);
    }

    @Test
    public void getInstance_withSameLowerAndUpperLimit_shouldReturnValidComparator() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        
        DateTimeComparator comparator = DateTimeComparator.getInstance(dayOfYear, dayOfYear);
        
        assertNotNull(comparator);
    }

    @Test
    public void getInstance_withNullLowerAndSpecificUpper_shouldReturnValidComparator() {
        DateTimeFieldType monthOfYear = DateTimeFieldType.monthOfYear();
        
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, monthOfYear);
        
        assertNotNull(comparator);
    }

    @Test
    public void getDateOnlyInstance_shouldReturnComparatorForDateFieldsOnly() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        
        assertNotNull(comparator);
        assertEquals("DateTimeComparator[dayOfYear-]", comparator.toString());
        assertNull(comparator.getLowerLimit());
        assertEquals("dayOfYear", comparator.getUpperLimit().toString());
    }

    @Test
    public void getTimeOnlyInstance_shouldReturnComparatorForTimeFieldsOnly() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        
        assertNotNull(comparator);
        assertEquals("DateTimeComparator[-dayOfYear]", comparator.toString());
        assertNull(comparator.getLowerLimit());
        assertEquals("dayOfYear", comparator.getUpperLimit().toString());
    }

    // ========== Comparison Tests ==========

    @Test
    public void compare_withBothNullObjects_shouldReturnZero() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        
        int result = comparator.compare(null, null);
        
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compare_withInvalidStringFormat_shouldThrowException() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        
        // This should throw IllegalArgumentException due to invalid date format
        comparator.compare(null, "DateTimeComparator[dayOfYear-]");
    }

    // ========== Equality Tests ==========

    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        
        boolean result = comparator.equals(comparator);
        
        assertTrue(result);
    }

    @Test
    public void equals_withDifferentComparatorTypes_shouldReturnFalse() {
        DateTimeComparator allFieldsComparator = DateTimeComparator.getInstance();
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        
        assertFalse(allFieldsComparator.equals(timeOnlyComparator));
        assertFalse(timeOnlyComparator.equals(allFieldsComparator));
    }

    @Test
    public void equals_betweenAllFieldsAndDateOnly_shouldReturnFalse() {
        DateTimeComparator allFieldsComparator = DateTimeComparator.getInstance();
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        
        assertFalse(allFieldsComparator.equals(dateOnlyComparator));
        assertFalse(dateOnlyComparator.equals(allFieldsComparator));
    }

    @Test
    public void equals_withNonComparatorObject_shouldReturnFalse() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        Object otherObject = new Object();
        
        boolean result = comparator.equals(otherObject);
        
        assertFalse(result);
    }

    // ========== String Representation Tests ==========

    @Test
    public void toString_forAllFieldsComparator_shouldShowEmptyBrackets() {
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        
        String result = comparator.toString();
        
        assertEquals("DateTimeComparator[]", result);
    }

    @Test
    public void toString_forTimeOnlyComparator_shouldShowCorrectFormat() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        
        String result = comparator.toString();
        
        assertEquals("DateTimeComparator[-dayOfYear]", result);
    }

    @Test
    public void toString_forDateOnlyComparator_shouldShowCorrectFormat() {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        
        String result = comparator.toString();
        
        assertEquals("DateTimeComparator[dayOfYear-]", result);
    }

    @Test
    public void toString_forSameLowerAndUpperLimit_shouldShowFieldName() {
        DateTimeFieldType weekyearOfCentury = DateTimeFieldType.weekyearOfCentury();
        DateTimeComparator comparator = DateTimeComparator.getInstance(weekyearOfCentury, weekyearOfCentury);
        
        String result = comparator.toString();
        
        assertEquals("DateTimeComparator[weekyearOfCentury]", result);
    }

    // ========== Hash Code Tests ==========

    @Test
    public void hashCode_shouldExecuteWithoutException() {
        DateTimeComparator allFieldsComparator = DateTimeComparator.getInstance();
        DateTimeFieldType dayOfMonth = DateTimeFieldType.dayOfMonth();
        DateTimeComparator customComparator = new DateTimeComparator(dayOfMonth, dayOfMonth);
        
        // These should not throw exceptions
        allFieldsComparator.hashCode();
        customComparator.hashCode();
    }

    // ========== Limit Accessor Tests ==========

    @Test
    public void getLowerLimit_forCustomComparator_shouldReturnCorrectField() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator comparator = DateTimeComparator.getInstance(dayOfYear);
        
        DateTimeFieldType result = comparator.getLowerLimit();
        
        assertEquals(dayOfYear, result);
    }

    @Test
    public void getUpperLimit_forComparatorWithOnlyLowerLimit_shouldReturnNull() {
        DateTimeFieldType dayOfYear = DateTimeFieldType.dayOfYear();
        DateTimeComparator comparator = DateTimeComparator.getInstance(dayOfYear);
        
        DateTimeFieldType result = comparator.getUpperLimit();
        
        assertNull(result);
    }
}