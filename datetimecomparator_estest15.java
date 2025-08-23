package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link DateTimeComparator} class.
 */
public class DateTimeComparatorTest {

    /**
     * Verifies that getInstance(lower, upper) successfully creates a comparator
     * when the lower limit is null and the upper limit is valid.
     */
    @Test
    public void getInstance_withNullLowerLimit_shouldCreateComparator() {
        // Arrange
        DateTimeFieldType upperLimit = DateTimeFieldType.monthOfYear();

        // Act
        DateTimeComparator comparator = DateTimeComparator.getInstance(null, upperLimit);

        // Assert
        assertNotNull("The factory method should return a non-null instance.", comparator);
        assertNull("The lower limit should be null, as provided.", comparator.getLowerLimit());
        assertEquals("The upper limit should be correctly set.", upperLimit, comparator.getUpperLimit());
    }
}