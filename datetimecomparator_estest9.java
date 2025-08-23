package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method of the {@link DateTimeComparator} class.
 */
public class DateTimeComparatorTest {

    /**
     * Verifies that a standard DateTimeComparator, which compares all date and time fields,
     * is not considered equal to a time-only comparator, which only compares time fields.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingFullAndTmeOnlyComparators() {
        // Arrange: Create two comparators with different comparison rules.
        // The 'full' comparator considers the entire instant (date and time).
        DateTimeComparator fullComparator = DateTimeComparator.getInstance();
        
        // The 'time-only' comparator ignores the date part and only considers the time of day.
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act & Assert: The two comparators are fundamentally different, so they should not be equal.
        assertFalse("A full comparator should not be equal to a time-only comparator",
                fullComparator.equals(timeOnlyComparator));
    }
}