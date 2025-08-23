package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DateTimeComparator} class.
 */
public class DateTimeComparatorTest {

    /**
     * Verifies that the upper limit for a time-only comparator is 'dayOfYear'.
     *
     * A time-only comparator is designed to ignore date-related fields. By setting 'dayOfYear'
     * as the exclusive upper limit, all fields of that magnitude or greater (day, month, year, etc.)
     * are excluded from the comparison, effectively isolating the time portion of a datetime.
     */
    @Test
    public void getUpperLimit_forTimeOnlyInstance_shouldReturnDayOfYear() {
        // Arrange: Get the singleton instance of the time-only comparator.
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act: Retrieve the upper limit field type from the comparator.
        DateTimeFieldType upperLimit = timeOnlyComparator.getUpperLimit();

        // Assert: The upper limit should be dayOfYear to exclude all date fields.
        assertEquals("The upper limit for a time-only comparator should be dayOfYear.",
                DateTimeFieldType.dayOfYear(), upperLimit);
    }
}