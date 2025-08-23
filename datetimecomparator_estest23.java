package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the time-only comparator has a null lower limit.
     * A time-only comparator is designed to compare all time fields (from milliseconds upwards),
     * so it should not have a lower boundary.
     */
    @Test
    public void getLowerLimit_forTimeOnlyInstance_shouldReturnNull() {
        // Arrange: Get the singleton instance of the time-only comparator.
        DateTimeComparator timeOnlyComparator = DateTimeComparator.getTimeOnlyInstance();

        // Act: Get the lower limit from the comparator.
        DateTimeFieldType lowerLimit = timeOnlyComparator.getLowerLimit();

        // Assert: The lower limit should be null, as there's no lower boundary for a time-only comparison.
        assertNull("The lower limit for a time-only comparator should be null.", lowerLimit);
    }
}