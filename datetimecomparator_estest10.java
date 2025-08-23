package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link DateTimeComparator} class, focusing on the equals() method.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that a comparator that only considers time fields is not equal to a
     * standard comparator that considers all date and time fields.
     */
    @Test
    public void equals_returnsFalse_whenComparingTimeOnlyAndFullComparators() {
        // Arrange: Create two comparators with different comparison rules.
        // A comparator that only compares the time part of a datetime.
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();
        
        // A standard comparator that compares the full datetime.
        DateTimeComparator fullComparator = DateTimeComparator.getInstance();

        // Act & Assert: The two comparators should not be equal because their
        // underlying comparison logic is different.
        assertNotEquals(timeOnlyComparator, fullComparator);
    }
}