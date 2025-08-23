package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link DateTimeComparator} class, focusing on its equals() method.
 */
public class DateTimeComparatorTest {

    /**
     * Verifies that a standard comparator (which compares all date-time fields)
     * is not considered equal to a date-only comparator.
     */
    @Test
    public void fullComparatorShouldNotBeEqualToDateOnlyComparator() {
        // Arrange: Create two comparators with different comparison rules.
        // The standard instance compares the full instant (date and time).
        DateTimeComparator fullComparator = DateTimeComparator.getInstance();
        
        // The date-only instance ignores the time part for comparisons.
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        // Assert: The comparators are configured differently, so they must not be equal.
        assertNotEquals(fullComparator, dateOnlyComparator);
    }
}