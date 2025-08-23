package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for the static factory method {@link DateTimeComparator#getInstance(DateTimeFieldType, DateTimeFieldType)}.
 */
public class DateTimeComparatorGetInstanceTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(DateTimeComparatorGetInstanceTest.class);
    }

    public void testGetInstance_withDistinctLowerAndUpperLimits() {
        // Arrange
        DateTimeFieldType lowerLimit = DateTimeFieldType.hourOfDay();
        DateTimeFieldType upperLimit = DateTimeFieldType.dayOfYear();

        // Act
        DateTimeComparator comparator = DateTimeComparator.getInstance(lowerLimit, upperLimit);

        // Assert
        assertEquals(lowerLimit, comparator.getLowerLimit());
        assertEquals(upperLimit, comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay-dayOfYear]", comparator.toString());
    }

    public void testGetInstance_withEqualLimits() {
        // Arrange
        DateTimeFieldType limit = DateTimeFieldType.hourOfDay();

        // Act
        DateTimeComparator comparator = DateTimeComparator.getInstance(limit, limit);

        // Assert
        assertEquals(limit, comparator.getLowerLimit());
        assertEquals(limit, comparator.getUpperLimit());
        assertEquals("DateTimeComparator[hourOfDay]", comparator.toString());
    }

    public void testGetInstance_withNullLimits_returnsDefaultInstance() {
        // Act
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, null);

        // Assert
        // Should return the same singleton instance as the no-arg factory method.
        assertSame(DateTimeComparator.getInstance(), comparator);
    }

    public void testGetInstance_withDayOfYearLowerLimit_returnsDateOnlyInstance() {
        // Act
        // This is a special case that should return the date-only singleton.
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.dayOfYear(), null);

        // Assert
        assertSame(DateTimeComparator.getDateOnlyInstance(), comparator);
    }

    public void testGetInstance_withDayOfYearUpperLimit_returnsTimeOnlyInstance() {
        // Act
        // This is a special case that should return the time-only singleton.
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear());

        // Assert
        assertSame(DateTimeComparator.getTimeOnlyInstance(), comparator);
    }
}