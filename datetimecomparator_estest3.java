package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DateTimeComparator} class.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that getInstance(lower, upper) returns a comparator
     * correctly configured with a lower limit and a null upper limit.
     */
    @Test
    public void getInstance_withLowerLimitAndNullUpperLimit_shouldReturnConfiguredComparator() {
        // Arrange: Define the comparison limits.
        DateTimeFieldType lowerLimit = DateTimeFieldType.dayOfYear();
        DateTimeFieldType upperLimit = null; // Explicitly null for clarity

        // Act: Get a comparator instance with the specified limits.
        DateTimeComparator comparator = DateTimeComparator.getInstance(lowerLimit, upperLimit);

        // Assert: Verify that the returned comparator is correctly configured.
        assertNotNull("The factory method should always return a comparator instance.", comparator);
        assertEquals("The lower limit should be set correctly.", lowerLimit, comparator.getLowerLimit());
        assertNull("The upper limit should be null as specified.", comparator.getUpperLimit());
    }
}