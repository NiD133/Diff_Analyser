package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the getInstance(lower, upper) factory method successfully creates a comparator
     * when the lower and upper limit field types are identical.
     */
    @Test
    public void getInstance_shouldCreateComparator_whenLowerAndUpperLimitsAreSame() {
        // Arrange: Define a single field type to be used for both limits.
        DateTimeFieldType limitFieldType = DateTimeFieldType.dayOfYear();

        // Act: Create a comparator with the same lower and upper limit.
        DateTimeComparator comparator = DateTimeComparator.getInstance(limitFieldType, limitFieldType);

        // Assert: Verify that the comparator is created and its limits are set correctly.
        assertNotNull("The created comparator should not be null.", comparator);
        assertEquals("The lower limit should be set to the provided field type.", limitFieldType, comparator.getLowerLimit());
        assertEquals("The upper limit should be set to the provided field type.", limitFieldType, comparator.getUpperLimit());
    }
}