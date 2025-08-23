package org.joda.time.base;

import org.joda.time.LocalDateTime;
import org.joda.time.MonthDay;
import org.joda.time.ReadablePartial;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * This test class focuses on the behavior of the equals() method in the AbstractPartial class.
 */
public class AbstractPartial_ESTestTest46 extends AbstractPartial_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when comparing two ReadablePartial
     * objects of different concrete types (e.g., a MonthDay and a LocalDateTime).
     *
     * The equals implementation in AbstractPartial must correctly handle comparisons
     * between different implementations of the ReadablePartial interface by checking
     * that their underlying field types and sizes match.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingPartialsOfDifferentTypes() {
        // Arrange: Create two ReadablePartial objects of different concrete types.
        // A MonthDay represents a partial date (month and day) without a year.
        ReadablePartial monthDay = new MonthDay(2, 14); // February 14th

        // A LocalDateTime represents a full local date and time.
        ReadablePartial localDateTime = new LocalDateTime(2023, 2, 14, 10, 30);

        // Act: Compare the two different partial types for equality.
        boolean areEqual = monthDay.equals(localDateTime);

        // Assert: The result must be false because the objects have different
        // sets of fields, even if some of their values (like month and day) might overlap.
        assertFalse("A MonthDay should not be considered equal to a LocalDateTime.", areEqual);
    }
}