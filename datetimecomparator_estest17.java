package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that getInstance() can create a comparator with a null lower limit
     * and a specific upper limit. This configuration creates a "time-only" comparator.
     */
    @Test
    public void getInstance_withNullLowerLimit_shouldCreateComparatorWithCorrectBounds() {
        // Arrange: Define the upper limit for the comparison.
        // A null lower limit means the comparison is unbounded at the lower end.
        DateTimeFieldType upperLimit = DateTimeFieldType.dayOfYear();

        // Act: Create the comparator instance.
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, upperLimit);

        // Assert: Verify that the comparator was created and configured correctly.
        assertNotNull("The created comparator should not be null.", comparator);
        assertNull("The lower limit should be null as specified.", comparator.getLowerLimit());
        assertEquals("The upper limit should be set to the specified field type.", upperLimit, comparator.getUpperLimit());
    }
}