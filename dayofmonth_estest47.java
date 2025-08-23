package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link DayOfMonth} class.
 */
public class DayOfMonthTest {

    /**
     * Tests that a DayOfMonth instance is equal to itself, as required by the
     * Comparable contract (x.compareTo(x) == 0).
     */
    @Test
    public void compareTo_whenComparedToSelf_returnsZero() {
        // Arrange: Create a specific DayOfMonth instance.
        // Using DayOfMonth.of() makes the test deterministic and independent of the system clock.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Compare the instance to itself.
        int comparisonResult = dayOfMonth.compareTo(dayOfMonth);

        // Assert: The result should be 0, indicating equality.
        assertEquals("A DayOfMonth instance compared to itself should return 0.", 0, comparisonResult);
    }
}