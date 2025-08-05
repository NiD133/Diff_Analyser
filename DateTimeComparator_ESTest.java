package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    // --- Test Data ---
    private static final DateTimeFieldType YEAR = DateTimeFieldType.year();
    private static final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
    private static final DateTimeFieldType DAY = DateTimeFieldType.dayOfMonth();
    private static final DateTimeFieldType HOUR = DateTimeFieldType.hourOfDay();

    // ========================================================================
    // Factory and Singleton Tests
    // ========================================================================

    @Test
    public void getInstance_withoutArgs_shouldReturnDefaultSingleton() {
        // Two calls to getInstance() should return the same instance.
        assertSame(DateTimeComparator.getInstance(), DateTimeComparator.getInstance());
    }

    @Test
    public void getInstance_withNullLimits_shouldReturnDefaultSingleton() {
        // getInstance(null, null) is equivalent to getInstance().
        assertSame(DateTimeComparator.getInstance(), DateTimeComparator.getInstance(null, null));
    }

    @Test
    public void getDateOnlyInstance_shouldReturnDateOnlySingleton() {
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getDateOnlyInstance());
        // The factory method with specific parameters should also return the singleton.
        assertSame(DateTimeComparator.getDateOnlyInstance(), DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null));
    }

    @Test
    public void getTimeOnlyInstance_shouldReturnTimeOnlySingleton() {
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getTimeOnlyInstance());
        // The factory method with specific parameters should also return the singleton.
        assertSame(DateTimeComparator.getTimeOnlyInstance(), DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear()));
    }

    @Test
    public void getInstance_withCustomLimits_shouldCreateNewInstance() {
        // Arrange
        DateTimeComparator comparator1 = DateTimeComparator.getInstance(DAY, YEAR);
        DateTimeComparator comparator2 = DateTimeComparator.getInstance(DAY, YEAR);

        // Assert
        // A new instance should be created for custom limits.
        assertNotSame(comparator1, comparator2);
        // However, they should be equal in value.
        assertEquals(comparator1, comparator2);
    }

    // ========================================================================
    // Getter Tests (getLowerLimit / getUpperLimit)
    // ========================================================================

    @Test
    public void getLimits_forDefaultInstance_shouldBothBeNull() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        // Act & Assert
        assertNull(comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
    }

    @Test
    public void getLimits_forDateOnlyInstance_shouldBeCorrect() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();

        // Act & Assert
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getLowerLimit());
        assertNull(comparator.getUpperLimit());
    }

    @Test
    public void getLimits_forTimeOnlyInstance_shouldBeCorrect() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();

        // Act & Assert
        assertNull(comparator.getLowerLimit());
        assertEquals(DateTimeFieldType.dayOfYear(), comparator.getUpperLimit());
    }

    @Test
    public void getLimits_forCustomInstance_shouldBeCorrect() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance(HOUR, MONTH);

        // Act & Assert
        assertEquals(HOUR, comparator.getLowerLimit());
        assertEquals(MONTH, comparator.getUpperLimit());
    }

    // ========================================================================
    // Comparison Logic Tests
    // ========================================================================

    @Test
    public void compare_defaultInstance_shouldCompareFullDateTime() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        DateTime dt1 = new DateTime(2023, 10, 26, 10, 0, 0);
        DateTime dt2 = new DateTime(2023, 10, 26, 10, 0, 1); // 1 second later
        DateTime dt3 = new DateTime(2023, 10, 26, 10, 0, 0); // Same as dt1

        // Act & Assert
        assertTrue("dt1 should be less than dt2", comparator.compare(dt1, dt2) < 0);
        assertTrue("dt2 should be greater than dt1", comparator.compare(dt2, dt1) > 0);
        assertEquals("dt1 should be equal to dt3", 0, comparator.compare(dt1, dt3));
    }

    @Test
    public void compare_dateOnlyInstance_shouldIgnoreTime() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        DateTime dt1 = new DateTime(2023, 10, 26, 10, 0, 0);
        DateTime dt2 = new DateTime(2023, 10, 26, 12, 0, 0); // Same date, different time
        DateTime dt3 = new DateTime(2023, 10, 27, 8, 0, 0);  // Different date

        // Act & Assert
        assertEquals("Should be equal when only date is considered", 0, comparator.compare(dt1, dt2));
        assertTrue("dt1 should be less than dt3", comparator.compare(dt1, dt3) < 0);
    }

    @Test
    public void compare_timeOnlyInstance_shouldIgnoreDate() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        DateTime dt1 = new DateTime(2023, 10, 26, 10, 0, 0);
        DateTime dt2 = new DateTime(2024, 1, 1, 10, 0, 0);   // Same time, different date
        DateTime dt3 = new DateTime(2023, 10, 26, 11, 0, 0); // Different time

        // Act & Assert
        assertEquals("Should be equal when only time is considered", 0, comparator.compare(dt1, dt2));
        assertTrue("dt1 should be less than dt3", comparator.compare(dt1, dt3) < 0);
    }

    @Test
    public void compare_withTwoNulls_shouldReturnZero() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        // Act
        int result = comparator.compare(null, null);

        // Assert
        assertEquals("Comparing two nulls should result in 0", 0, result);
    }
    
    @Test
    public void compare_withOneNull_shouldCompareAgainstNow() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        // A moment in the past, relative to now
        long pastMillis = System.currentTimeMillis() - 10000;
        
        // Act
        int result = comparator.compare(pastMillis, null);

        // Assert
        // A past instant should be less than null (now)
        assertTrue("Comparing a past date to null (now) should be < 0", result < 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compare_withInvalidObjectType_shouldThrowException() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();
        Object invalidObject = "not a valid date";

        // Act: This should throw an IllegalArgumentException
        comparator.compare(new DateTime(), invalidObject);
    }

    // ========================================================================
    // General Object Method Tests (equals, hashCode, toString)
    // ========================================================================

    @Test
    public void equals_shouldReturnTrueForSameInstance() {
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
        assertTrue("A comparator must be equal to itself", comparator.equals(comparator));
    }

    @Test
    public void equals_shouldReturnTrueForEqualInstances() {
        // Arrange
        DateTimeComparator comparator1 = DateTimeComparator.getInstance(DAY, YEAR);
        DateTimeComparator comparator2 = DateTimeComparator.getInstance(DAY, YEAR);

        // Assert
        assertTrue("Comparators with the same limits should be equal", comparator1.equals(comparator2));
    }

    @Test
    public void equals_shouldReturnFalseForDifferentInstances() {
        // Arrange
        DateTimeComparator comparator1 = DateTimeComparator.getInstance();
        DateTimeComparator comparator2 = DateTimeComparator.getDateOnlyInstance();
        DateTimeComparator comparator3 = DateTimeComparator.getTimeOnlyInstance();

        // Assert
        assertFalse("Default and DateOnly comparators should not be equal", comparator1.equals(comparator2));
        assertFalse("DateOnly and TimeOnly comparators should not be equal", comparator2.equals(comparator3));
    }

    @Test
    public void equals_shouldReturnFalseForNullOrDifferentType() {
        // Arrange
        DateTimeComparator comparator = DateTimeComparator.getInstance();

        // Assert
        assertFalse("Comparator should not be equal to null", comparator.equals(null));
        assertFalse("Comparator should not be equal to an object of a different type", comparator.equals(new Object()));
    }

    @Test
    public void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange
        DateTimeComparator comparator1 = DateTimeComparator.getInstance(DAY, YEAR);
        DateTimeComparator comparator2 = DateTimeComparator.getInstance(DAY, YEAR);

        // Assert
        assertEquals("Hash codes must be equal for equal objects", comparator1.hashCode(), comparator2.hashCode());
    }
    
    @Test
    public void hashCode_forDefaultInstance_shouldBeConsistent() {
        assertEquals(DateTimeComparator.getInstance().hashCode(), DateTimeComparator.getInstance(null, null).hashCode());
    }

    @Test
    public void toString_shouldProduceCorrectRepresentations() {
        assertEquals("DateTimeComparator[]", DateTimeComparator.getInstance().toString());
        assertEquals("DateTimeComparator[dayOfYear-]", DateTimeComparator.getDateOnlyInstance().toString());
        assertEquals("DateTimeComparator[-dayOfYear]", DateTimeComparator.getTimeOnlyInstance().toString());
        assertEquals("DateTimeComparator[hourOfDay-monthOfYear]", DateTimeComparator.getInstance(HOUR, MONTH).toString());
        assertEquals("DateTimeComparator[dayOfMonth]", DateTimeComparator.getInstance(DAY, DAY).toString());
    }
}