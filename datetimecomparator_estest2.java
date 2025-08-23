package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that getLowerLimit() returns the same DateTimeFieldType that was used
     * to create the comparator instance.
     */
    @Test
    public void getInstance_withLowerLimit_shouldSetLowerLimitCorrectly() {
        // Arrange: Define the field type to be used as the lower limit.
        DateTimeFieldType expectedLowerLimit = DateTimeFieldType.yearOfEra();

        // Act: Create a comparator instance with the specified lower limit.
        DateTimeComparator comparator = DateTimeComparator.getInstance(expectedLowerLimit);
        DateTimeFieldType actualLowerLimit = comparator.getLowerLimit();

        // Assert: The getter should return the same field type instance.
        assertNotNull("The lower limit should not be null", actualLowerLimit);
        assertEquals("The lower limit should match the one provided during construction",
                expectedLowerLimit, actualLowerLimit);
    }
}