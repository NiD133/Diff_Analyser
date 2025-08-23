package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link DateTimeComparator} class, focusing on its equality logic.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the equals() method correctly distinguishes between different
     * standard comparator instances.
     * <p>
     * A comparator that considers all date-time fields should not be equal
     * to a comparator that only considers the date portion.
     */
    @Test
    public void testEquals_returnsFalse_whenComparingDifferentComparatorTypes() {
        // Arrange: Obtain two different standard comparator instances.
        // The 'full' comparator considers all date and time fields.
        DateTimeComparator fullComparator = DateTimeComparator.getInstance();
        
        // The 'date-only' comparator ignores the time-of-day.
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();

        // Assert: Verify that the two distinct comparators are not equal.
        // Their internal comparison logic is different, so equals() should return false.
        assertFalse("A date-only comparator should not be equal to a full date-time comparator.",
                dateOnlyComparator.equals(fullComparator));
        
        // For completeness, we can also use assertNotEquals which is often more readable.
        assertNotEquals(fullComparator, dateOnlyComparator);
    }
}